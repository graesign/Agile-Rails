/*-
 * Copyright (c) 2008 Nils Dijk, Michiel van den Anker, Oscar Orton,
 *  Tonny Wildeman, Arjan van der Velde
 *
 * TIGAM, Hogeschool van Amsterdam
 * http://www.tigam.com, http://home.ie.hva.nl
 * 
 * All rights reserved
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *     * The name of the author may not be used to endorse or promote
 *       products derived from this software without specific prior
 *       written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.tigam.railcab.model.simulatie;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Observable;

import com.tigam.railcab.model.baan.BaanHeader;
import com.tigam.railcab.model.baan.Baandeel;
import com.tigam.railcab.model.baan.RailCab;
import com.tigam.railcab.model.baan.Station;
import com.tigam.railcab.model.baan.StationBaandeel;
import com.tigam.railcab.model.baan.Wissel;
import com.tigam.railcab.model.baan.WisselTerug;
import com.tigam.railcab.model.exception.RailCabException;

/**
 * "Simulatie gedrag". Zowel software als hardware simulaties moeten deze
 * interface implementeren.
 * 
 * @author Arjan van der Velde
 * 
 */
public abstract class Simulatie extends Observable implements Runnable {

	/** Simulatie status waardes: STATUS_NULL: geen status */
	public static final int STATUS_NULL = 0;

	/** Simulatie status waardes: STATUS_STOP: gestopt */
	public static final int STATUS_STOP = 1;

	/** Simulatie status waardes: STATUS_RUNNING: simulatie draait */
	public static final int STATUS_RUNNING = 2;

	/** Simulatie status waardes: STATUS_PAUSE: simulatie gepauzeerd */
	public static final int STATUS_PAUSE = 3;

	/** Represents the time in the simulation. */
	private static long time;

	/**
	 * @return the time
	 */
	public static long getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public static void setTime(long time) {
		Simulatie.time = time;
	}

	/**
	 * Laatste tijd in miliseconde dat de delay wordt uitgevoerd
	 */
	private long lastDelay = 0;

	// baandeel types
	/** indicator voor type Baandeel */
	protected final int BAANDEEL_NORMAAL = 0;

	/** indicator voor type StationBaandeel */
	protected final int BAANDEEL_STATION = 1;

	/** indicator voor type Wissel */
	protected final int BAANDEEL_WISSEL = 2;

	/** indicator voor type WisselTerug */
	protected final int BAANDEEL_WISSEL_TERUG = 3;

	// windrichtingen
	/** noord */
	protected final int N = 0;

	/** noord-oost */
	protected final int NO = 1;

	/** oost */
	protected final int O = 2;

	/** zuid-oost */
	protected final int ZO = 3;

	/** zuid */
	protected final int Z = 4;

	/** zuid-west */
	protected final int ZW = 5;

	/** west */
	protected final int W = 6;

	/** noord-west */
	protected final int NW = 7;

	/** simulatie status */
	protected int simulatieStatus = STATUS_NULL;

	/** Delay used in delay() */
	private int delay = 10;

	/** Lijst van stations */
	protected ArrayList<Station> stations;

	/** Lijst van RailCabs op deze baan... */
	protected ArrayList<RailCab> railcabs;

	/** Eerste node van de baan. Referentie naar de baan */
	protected Baandeel head;

	/** Aantal RailCabs aan te maken in de simu... */
	protected int aantalRailCabs;

	/** Header die verwijst naar een complete baan */
	protected BaanHeader baanHeader;

	/**
	 * Constructor, init stations en roep build() aan
	 * 
	 * @throws RailCabException
	 */
	public Simulatie() throws RailCabException {
		this(null);
	}

	/**
	 * Constructor, init stations en roep build() aan en onthoud de geladen baan
	 * 
	 * @param baanHeader de header die naar de baan verwijst
	 * @throws RailCabException voor als er iets fout gaat
	 */
	public Simulatie(BaanHeader baanHeader) throws RailCabException {
		aantalRailCabs = 0;
		stations = new ArrayList<Station>();
		railcabs = new ArrayList<RailCab>();

		this.baanHeader = baanHeader;

		build();

		//opslaan van de baan
		//BaanLoader.saveBaan(this.head, "Tigam", "tigam.baan");
	}

	/**
	 * Deze methode bouwt de baan op - initialiseert de linked list die de baan
	 * vormt.
	 */
	protected abstract void build();

	/**
	 * Voor het leeg maken van de hele simulatie, primair voor het stoppen van
	 * een simulatie.
	 */
	public void clear() {
		if (simulatieStatus == Simulatie.STATUS_NULL || simulatieStatus == Simulatie.STATUS_STOP) {
			synchronized (railcabs) {
				while (railcabs.size() > 0) {
					removeRailCab(railcabs.get(0));
				}
			}
			synchronized (stations) {
				for (Station s : stations) {
					// clear station
					s.clear();
				}
			}
			Simulatie.setTime(0);
		}
	}

	/**
	 * Tik tik tik. De tijd. Deze moet worden aangeroepen vanuit de hoofd-lus
	 * van de simulatie. Het introduceert een delay waarmee de snelheid van de
	 * simulatie geregeld wrdt, maar verhoogd ook de teller die gebruikt wordt
	 * als tijd in de hele simulatie.
	 */
	protected void delay() {

		try {
			if (lastDelay == 0) {
				lastDelay = System.currentTimeMillis();
			}
			setChanged();
			notifyObservers(new Date());
			int tijd = (int) (System.currentTimeMillis() - lastDelay);
			if (delay - tijd > 0) {
				Thread.sleep(delay - tijd);
			}
			lastDelay = System.currentTimeMillis();
			time++;
		} catch (Exception ignore) {
			// nothing...
			ignore.printStackTrace();
		}
	}

	/**
	 * Geef een referentie naar de baan (naar een baandeel van de baan)
	 * 
	 * @return referentie naar een baan(deel)
	 */
	public BaanHeader getBaanHeader() {
		return baanHeader;
	}

	/**
	 * Geef de duur van een tik in de simulatie klok.
	 * 
	 * @return de delay
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Vraag een lijst met alle RailCabs op die op de baan staan
	 * 
	 * @return lijst met alle RailCabs
	 */
	public ArrayList<RailCab> getRailCabs() {
		ArrayList<RailCab> deepCopy = new ArrayList<RailCab>();
		synchronized (railcabs) {
			deepCopy.addAll(railcabs);
		}
		return deepCopy;
	}

	/**
	 * Geef een lijst van alle stations
	 * 
	 * @return lijst van stations
	 */
	public ArrayList<Station> getStations() {
		ArrayList<Station> deepCopy = new ArrayList<Station>();
		synchronized (stations) {
			deepCopy.addAll(stations);
		}
		return deepCopy;
	}

	/**
	 * Vraag de status op van de simulatie.
	 * 
	 * @return de status van de simulatie: - Simulatie.STATUS_NULL -
	 *         Simulatie.STATUS_STOP - Simulatie.STATUS_RUNNING -
	 *         Simulatie.STATUS_PAUSE
	 */
	public int getStatus() {
		return simulatieStatus;
	}

	/**
	 * Deze methode wordt aangeroepen vanuit de constructor van Simulatie en
	 * dient voor het aanmaken van de railcabs in de simulatie.
	 * 
	 * @param maxReizigers het maximale aantal reizigers voor de te maken cabs
	 * @throws RailCabException
	 */
	protected abstract void maakRailCabs(int maxReizigers) throws RailCabException;

	/**
	 * Tik tik tik. Geef alle RailCabs een duwtje
	 * 
	 * @throws RailCabException
	 */
	protected void moveRailCabs() throws RailCabException {
		synchronized (railcabs) {
			for (RailCab r : railcabs) {
				r.actie();
			}
		}
	}

	/**
	 * Maak een nieuw baandeel van een zeker type op een zeker locatie
	 * 
	 * @param x x-waarde
	 * @param y y-waarde
	 * @param type baandeel type
	 * @param vorige baandeel waaraan dit baandeel gekoppeld dient te worden.
	 * @return het aangemaakte baandeel
	 */
	protected Baandeel nieuwBaandeel(Baandeel vorige, int x, int y, int type) {
		Baandeel b = vorige;
		Point locatie = new Point(x, y);
		// Bepaal type baandeel en maak er een
		switch (type) {
		case BAANDEEL_NORMAAL:
			b.setVolgende(new Baandeel(locatie));
			break;
		case BAANDEEL_STATION:
			b.setVolgende(new StationBaandeel(locatie));
			break;
		case BAANDEEL_WISSEL:
			b.setVolgende(new Wissel(locatie));
			break;
		case BAANDEEL_WISSEL_TERUG:
			b.setVolgende(new WisselTerug(locatie));
			break;
		default: // default is een normaal baandeel....
			b.setVolgende(new Baandeel(locatie));
			break;
		}
		return b.getVolgende();
	}

	/**
	 * Voeg nieuwe baandelen toe aan andere baandelen
	 * 
	 * @param startpunt baandeel waaraan delen moeten worden toegevoegd
	 * @param aantal hoeveel baandelen
	 * @param richting in welke richting (N, NO, O, etc...)
	 * @param type baandeel type
	 * @return laatst toegevoegde baandeel
	 */
	protected Baandeel nieuweBaandelen(Baandeel startpunt, int aantal, int richting, int type) {
		Baandeel b = startpunt;
		int x = startpunt.getLocatie().x;
		int y = startpunt.getLocatie().y;
		for (int i = 0; i < aantal; i++) {
			// Bepaal coordinaten op basis van richting
			if (richting == N || richting == NO || richting == NW) {
				y--;
			}
			if (richting == Z || richting == ZO || richting == ZW) {
				y++;
			}
			if (richting == NO || richting == O || richting == ZO) {
				x++;
			}
			if (richting == NW || richting == W || richting == ZW) {
				x--;
			}
			// Maak een baandeel...
			b = nieuwBaandeel(b, x, y, type);
		}
		return b;
	}

	/**
	 * Pauzeert de simulatie
	 */
	public void pauseSimulatie() {
		simulatieStatus = Simulatie.STATUS_PAUSE;
	}

	/**
	 * Verwijder een zekere railcab...
	 * 
	 * @param railcab de te verwijderen cabs
	 */
	protected void removeRailCab(RailCab railcab) {
		synchronized (railcabs) { // lock collection
			railcab.deleteObservers();
			// Haal RailCab van de baan
			railcab.getHuidigePositie().clearRailCab();
			// Ontkoppel eventueel gekoppelde Cabs
			railcab.ontkoppel();
			// Verwijder uit de ArrayList van cabs
			railcabs.remove(railcab);
		}
	}

	/**
	 * Reset een Simulatie, maar laat bepaalde objecten, zoals Stations en baan
	 * bestaan.
	 * 
	 * @param maxReizigers maximale aantal reizigers voor de te maken cabs
	 * @throws RailCabException voor als het fout gaat
	 */
	public void reset(int maxReizigers) throws RailCabException {
		simulatieStatus = Simulatie.STATUS_NULL;
		clear();
		synchronized (railcabs) {
			maakRailCabs(maxReizigers);
		}
	}

	/**
	 * Set aantal railcabs
	 * 
	 * @param aantalRailCabs aantal railcabs
	 */
	public void setAantailRailCabs(int aantalRailCabs) {
		this.aantalRailCabs = aantalRailCabs;
	}

	/**
	 * Zet de delay voor de delay() methode
	 * 
	 * @param delay delay in milleseconden
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}

	/**
	 * Start de simulatie, dit houdt ook in het hervatten als de simulatie op
	 * pauze staat
	 */
	public void startSimulatie() {
		simulatieStatus = Simulatie.STATUS_RUNNING;
	}

	/**
	 * Stop de simulatie
	 * 
	 * @throws RailCabException
	 */
	public void stopSimulatie() throws RailCabException {
		simulatieStatus = Simulatie.STATUS_STOP;
		clear();
	}

}
