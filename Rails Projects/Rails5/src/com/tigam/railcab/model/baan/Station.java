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

package com.tigam.railcab.model.baan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Observable;

import com.tigam.railcab.model.exception.GroepAlreadyEmptyException;
import com.tigam.railcab.model.exception.GroepException;

/**
 * Station bestaat uit een verwijzing naar het eerste baandeel dat het station
 * voorsteld plus een wachtrij van groepen reizigers. Een station stel observers
 * op de hoogte van wijzigingen in aantallen reizigers (groepen) op het station.
 * 
 * @author Arjan van der Velde
 * 
 */
public class Station extends Observable implements Comparable<Station> {

	/** baandelen waaruit dit station bestaat */
	private HashSet<StationBaandeel> baandelen;

	/** wachtrij van groepen reizigers */
	private LinkedList<Groep> wachtrij;

	/** eerste baandeel van dit station */
	private StationBaandeel eersteBaandeel;

	/** naam van dit station */
	private String naam;

	/**
	 * Constructor. Creeer een station met een lege wachtrij en een naam.
	 * 
	 * @param naam de naam van het station
	 * @param eersteBaandeel eerste baandeel van het station
	 */
	public Station(String naam, StationBaandeel eersteBaandeel) {
		baandelen = new HashSet<StationBaandeel>();
		wachtrij = new LinkedList<Groep>();
		this.naam = naam;
		setEersteBaandeel(eersteBaandeel); // gebruik setter zodat ook de bijbehoren logica af gaat!
	}

	/**
	 * Voeg een baandeel toe aan de lijst van baandelen
	 * 
	 * @param baandeel toe te voegen baandeel
	 */
	public void addBaandeel(StationBaandeel baandeel) {
		baandelen.add(baandeel);
		// Maak baandeel <-> station relatie consistent
		if (baandeel.getStation() != this) {
			baandeel.setStation(this);
		}
	}

	/**
	 * Voeg een groep toe aan de wachtrij.
	 * 
	 * @param groep groep reizigers.
	 */
	public void addGroep(Groep groep) {
		synchronized (wachtrij) {
			wachtrij.addLast(groep); // voeg groepen toe aan het einde van de rij.
		}

		// waarschuwen van alle observers dat er een nieuwe groep is toegevoegd.
		setChanged();
		notifyObservers(groep);
	}

	/**
	 * Voor het netjes leeg maken van een station, primair gemaakt voor het
	 * stoppen van een simulatie
	 */
	public void clear() {
		//wachtrij.clear();
		while (wachtrij.size() > 0) {
			Groep g = wachtrij.get(0);
			if (g.getAantalWachtenden() > 0) {
				g.clear();
			}
			try {
				removeGroep(g);
			} catch (Exception ex) {
				System.out.println("ERROR: Station.clear()\n" + ex);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Station s) {
		return naam.compareTo(s.getNaam()); // vergelijk lexicographically...
	}

	/**
	 * Geef het aantal op dit station aanwezige cabs
	 * 
	 * @return aantal cabs op dit station
	 */
	public int getAantalAanwezigeCabs() {
		int aantal = 0;
		Baandeel b = eersteBaandeel;
		while (b != null && b instanceof StationBaandeel) {
			aantal += b.hasRailCab() ? 1 : 0;
			b = b.getVolgende();
		}
		return aantal;
	}

	// TODO: kan optimaler.. voor nu on the fly.
	/**
	 * get aantal wachtenden
	 * 
	 * @return aantal wachtenden op dit station
	 */
	public int getAantalWachtenden() {
		int aantal = 0;
		synchronized (wachtrij) {
			for (Groep g : wachtrij) {
				aantal += g.getAantalWachtenden();
			}
		}
		return aantal;
	}

	/**
	 * Geef de lijst van baandelen die bij dit station horen
	 * 
	 * @return lijst van baandelen
	 */
	public HashSet<StationBaandeel> getBaandelen() {
		return baandelen;
	}

	/**
	 * Geef het eerste baandeel van het station
	 * 
	 * @return het eerste baandeel
	 */
	public StationBaandeel getEersteBaandeel() {
		return eersteBaandeel;
	}

	/**
	 * Geef de naam van het station.
	 * 
	 * @return de naam
	 */
	public String getNaam() {
		return naam;
	}

	/**
	 * Geef reizigers uit groep. Gedelegeerde functie.
	 * 
	 * @param groep
	 * @param aantal
	 * @return Een ArrayList met "aantal" reizigers uit de gegeven groep.
	 * @throws GroepAlreadyEmptyException
	 */
	public ArrayList<Reiziger> getReizigers(Groep groep, int aantal) throws GroepAlreadyEmptyException {
		synchronized (wachtrij) {
			if (wachtrij.contains(groep)) {
				ArrayList<Reiziger> reizigers = groep.getReizigers(aantal);
				if (groep.getAantalWachtenden() == 0) {
					try {
						removeGroep(groep);
					} catch (Exception e) {
						System.out.println(e);
					}
				} else {
					setChanged();
					notifyObservers();
				}
				return reizigers;
			} else {
				return new ArrayList<Reiziger>();
			}
		}
	}

	/**
	 * Geef reizigers uit eerste groep in wachtrij. Gedelegeerde functie.
	 * 
	 * @param aantal
	 * @return Een ArrayList met reizigers uit de eerste groep in de wachtrij op
	 *         dit station.
	 * @throws GroepAlreadyEmptyException
	 */
	public ArrayList<Reiziger> getReizigers(int aantal) throws GroepAlreadyEmptyException {
		return getReizigers(wachtrij.getFirst(), aantal);
	}

	/**
	 * Geeft de complete wachtrij.
	 * 
	 * @return wachtrij
	 */
	public LinkedList<Groep> getWachtrij() {
		// geen deep copy aangezien we de rij zelf willen en niet alleen de members
		return wachtrij;
	}

	/**
	 * Verwijder een specifieke groep uit de wachtrij.
	 * 
	 * @param groep de te verwijderen groep
	 * @throws GroepException als de groep niet bestaat op het station, of als
	 *             er al reizigers zijn opgehaald uit die groep.
	 */
	public void removeGroep(Groep groep) throws GroepException {
		synchronized (wachtrij) {
			if (!wachtrij.contains(groep)) {
				throw new GroepException("Groep is niet bekend op het station");
			} else if (groep.getAantalReizigers() != groep.getAantalWachtenden() && groep.getAantalWachtenden() > 0) {
				throw new GroepException("Groep is al in behandeling genomen, deze kan nu niet meer verwijderd worden.");
			} else {
				wachtrij.remove(groep);
				setChanged();
				notifyObservers(groep); // geef geremovede groep mee!
			}
		}
	}

	/**
	 * Set eerste baandeel.
	 * 
	 * @param eerste eerste baandeel
	 */
	public void setEersteBaandeel(StationBaandeel eerste) {
		eersteBaandeel = eerste;
		// maak baandeel <-> station consistent
		if (eersteBaandeel.getStation() != this) {
			eersteBaandeel.setStation(this);
		}
	}

	/**
	 * Geef een string representatie van een station
	 */
	@Override
	public String toString() {
		Baandeel s = getEersteBaandeel();
		int i = 1;
		while (s.getVolgende() instanceof StationBaandeel) {
			s = s.getVolgende();
			i++;
		}
		return "Station " + naam;
	}

}
