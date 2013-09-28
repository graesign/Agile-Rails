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

package com.tigam.railcab.stats;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;

import com.tigam.railcab.language.Language;
import com.tigam.railcab.model.baan.Groep;
import com.tigam.railcab.model.baan.RailCab;
import com.tigam.railcab.model.baan.Reiziger;
import com.tigam.railcab.model.baan.Station;
import com.tigam.railcab.model.simulatie.Date;
import com.tigam.railcab.model.simulatie.Simulatie;

/**
 * handelt de data af die binnenkomt en zet de goede data in een SimulatieData
 * 
 * @author Tonny Wildeman
 */
public class DataHandler extends Thread implements Observer {
	/**
	 * Het data object waar alle data in opgeslagen wordt
	 */
	private SimulatieData simulatieData;

	/**
	 * De simulatie waar deze data bij hoort
	 */
	private Simulatie simulatie;

	/**
	 * Het totaal aantal verwerkte reizigers
	 */
	private long aantalReizigers;

	/**
	 * Het meest recente tijdstip waarop een reiziger is aangekomen
	 */
	private long laatsteReizigerAankomstTijd = 0;

	/**
	 * De totale kosten die alle railcabs vertegenwoordigen op tijdstip
	 * <code>Date.getCurrentTime()</code>
	 */
	private long kostenRailCabs;

	/**
	 * De totaal afgelegde afstand van alle railcabs bij elkaar
	 */
	private long totaleAfstand;

	/**
	 * De meest recente tijd
	 */
	private long huidigeTijd;

	/**
	 * De totale baten over alle reizigers
	 */
	double baten;

	/**
	 * De totale afstand over alle reizigers
	 */
	double totaalAfstandReizigers;

	/**
	 * De totale wachttijd over alle reizigers
	 */
	double totaleWachttijd;

	/**
	 * Moeten de grafieken nog geupdate worden of is de simulatie ten einde
	 */
	boolean running = true;

	/**
	 * Moeten de grafieken geupdate worden
	 */
	boolean notify = false;

	/**
	 * creeert een leeg datahandler object, waar nog een simulatie aan
	 * toegevoegd moet worden middels:
	 * <code>setSimulatieData(SimulatieData simulatieData</code>
	 * <p>
	 * creert tevens de buffers voor de doublebuffering van de binnenkomende
	 * data
	 * 
	 * @param simulatie - de simulatie waar deze data handler deel van is
	 */
	public DataHandler(Simulatie simulatie) {
		this.simulatie = simulatie;
	}

	/**
	 * deze constructor maakt een data handler object aan waarvan de simulatie
	 * data niet meer veranderd kan worden
	 * 
	 * @param simulatieData
	 */
	public DataHandler(SimulatieData simulatieData) {
		this.simulatieData = simulatieData;
		simulatieData.setNonEditable();
	}

	/**
	 * Berekent de afstand die de railCabs in totaal hebben afgelegd
	 * 
	 * @param date - het tijdstip waarop de railcabs hun afstand vrij geven
	 */
	private void berekenRailCabData(Date date) {
		totaleAfstand = 0;
		for (RailCab railCab : simulatieData.getRailCabs()) {
			totaleAfstand += railCab.getAfgelegdeBaandelen();
		}

		kostenRailCabs = totaleAfstand * (long) simulatieData.getInstelling().getKostenPerBaandeel();

		simulatieData.addTotaalAfgelegdRailCabs(date.getTime(), totaleAfstand, notify);
		simulatieData.addKosten(date.getTime(), kostenRailCabs, notify);
	}

	/**
	 * Berekent de data over alle reizigers
	 * 
	 * @param reizigers
	 */
	private void berekenReizigersData(ArrayList<Reiziger> reizigers) {
		long start, eind, wachttijd;

		Reiziger reiziger;
		for (int i = 0; i < reizigers.size(); i++) {
			reiziger = reizigers.get(i);
			start = reiziger.getGroep().getAanmaakDate().getTime();
			eind = reiziger.getVertrekDate().getTime();
			if (eind != 0) {
				aantalReizigers++;
				wachttijd = eind - start;

				totaalAfstandReizigers += reiziger.getAfgelegdeBaandelen();
				totaleWachttijd += wachttijd;
				laatsteReizigerAankomstTijd = reiziger.getAankomstDate().getTime();

				simulatieData.addWachttijd(laatsteReizigerAankomstTijd, wachttijd, notify);
				simulatieData.addGemiddeldeWachttijd(laatsteReizigerAankomstTijd, totaleWachttijd / aantalReizigers,
						notify);
			}

			baten = totaalAfstandReizigers * simulatieData.getInstelling().getPrijsPerBaandeel()
					+ simulatieData.getInstelling().getStartTarief() * aantalReizigers;
			simulatieData.addTotaalAfgelegdReizigers(laatsteReizigerAankomstTijd, totaalAfstandReizigers, notify);
			simulatieData.addBaten(laatsteReizigerAankomstTijd, baten, notify);
		}
	}

	/**
	 * Zorgt er voor dat de dataset van de wachttijd, gemiddelde wachttijd,
	 * totaal afgelegd reizigers en baten door blijven lopen
	 * 
	 * @param date - de tijd waarop de dataset door moet lopen
	 */
	public void berekenReizigersData(Date date) {
		huidigeTijd = date.getTime();
		simulatieData.addWachttijd(huidigeTijd, 0, notify);
		simulatieData.addGemiddeldeWachttijd(huidigeTijd, totaleWachttijd / aantalReizigers, notify);
		simulatieData.addTotaalAfgelegdReizigers(huidigeTijd, totaalAfstandReizigers, notify);
		simulatieData.addBaten(huidigeTijd, baten, notify);
	}

	/**
	 * Geeft de afstandgrafiek terug
	 * 
	 * @return de afstandsgrafiek
	 */
	private ChartPanel getAfstandGrafiek() {
		XYSeriesCollection collectie = new XYSeriesCollection();
		collectie.addSeries(simulatieData.getTotaalAfstandRailCabsSerie());
		collectie.addSeries(simulatieData.getTotaalAfstandReizigersSerie());

		ChartPanel cp = new ChartPanel(ChartFactory.createXYLineChart("", Language.getString("DataHandler.1"), //$NON-NLS-1$ //$NON-NLS-2$
				Language.getString("DataHandler.2"), collectie, PlotOrientation.VERTICAL, true, false, false)); //$NON-NLS-1$
		cp.setName(Language.getString("DataHandler.3")); //$NON-NLS-1$
		return cp;
	}

	/**
	 * Geeft een arraylist met de grafieken terug
	 * 
	 * @return arraylist met grafieken
	 */
	public ArrayList<ChartPanel> getGrafieken() {
		ArrayList<ChartPanel> grafieken = new ArrayList<ChartPanel>();

		grafieken.add(getWachttijdGrafiek());
		grafieken.add(getAfstandGrafiek());
		grafieken.add(getKostenBatenGrafiek());

		return grafieken;
	}

	/**
	 * Geeft de kosten en baten grafiek terug
	 * 
	 * @return de kosten en baten grafiek
	 */
	private ChartPanel getKostenBatenGrafiek() {
		XYSeriesCollection collectie = new XYSeriesCollection();
		collectie.addSeries(simulatieData.getKostenSerie());
		collectie.addSeries(simulatieData.getBatenSerie());
		//collectie.addSeries(simulatieData.getWinstSerie());

		ChartPanel cp = new ChartPanel(ChartFactory.createXYLineChart(
				"", Language.getString("DataHandler.5"), Language.getString("DataHandler.6"), collectie, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				PlotOrientation.VERTICAL, true, false, false));
		cp.setName(Language.getString("DataHandler.7")); //$NON-NLS-1$
		return cp;
	}

	/**
	 * Geeft het huidige simulatieData object terug
	 * 
	 * @return simulatieData
	 */
	public SimulatieData getSimulatieData() {
		return simulatieData;
	}

	/**
	 * Geeft de wachttijd grafiek terug
	 * 
	 * @return de wachttijd grafiek
	 */
	private ChartPanel getWachttijdGrafiek() {
		XYSeriesCollection collectie = new XYSeriesCollection();
		collectie.addSeries(simulatieData.getGemiddeldeWachttijdSerie());
		collectie.addSeries(simulatieData.getWachttijdSerie());

		ChartPanel cp = new ChartPanel(ChartFactory.createXYLineChart("", Language.getString("DataHandler.9"), //$NON-NLS-1$ //$NON-NLS-2$
				Language.getString("DataHandler.10"), collectie, PlotOrientation.VERTICAL, true, false, false)); //$NON-NLS-1$
		cp.setName(Language.getString("DataHandler.11")); //$NON-NLS-1$
		return cp;
	}

	/**
	 * start het observeren van de railcabs, stations en simulatie
	 */
	private void observeer() {
		ArrayList<RailCab> railCabs = simulatieData.getRailCabs();
		start();

		for (RailCab railCab : railCabs) {
			railCab.addObserver(this);
		}

		ArrayList<Station> stations = simulatieData.getStations();
		for (Station station : stations) {
			station.addObserver(this);
		}

		simulatie.addObserver(this);
	}

	/**
	 * stop de observatie van de railcabs, stations en simulatie
	 */
	private void observeerNietMeer() {
		ArrayList<RailCab> railCabs = simulatieData.getRailCabs();
		for (RailCab railcab : railCabs) {
			railcab.deleteObserver(this);
		}

		ArrayList<Station> stations = simulatieData.getStations();
		for (Station station : stations) {
			station.deleteObserver(this);
		}

		simulatie.deleteObserver(this);
		stopRunning();
		simulatieData.setNonEditable();
	}

	@Override
	public void run() {
		while (running) {
			try {
				notify = true;
				sleep(1);
				notify = false;
				sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		notify = true;
	}

	/**
	 * Zorgt er voor dat de data in simulatieData niet meer aanpasbaar is
	 */
	public void setNonEditable() {
		observeerNietMeer();
	}

	/**
	 * Zorgt dat het interne simulatieData object verwijst naar het meegegeven
	 * argument
	 * 
	 * @param simulatieData
	 */
	public void setSimulatieData(SimulatieData simulatieData) {
		this.simulatieData = simulatieData;
		observeer();
	}

	/**
	 * Stopt de thread die de grafieken update
	 */
	public void stopRunning() {
		running = false;
	}

	/**
	 * handelt de data af die binnenkomt
	 * 
	 * @param o - de observable waar de update vandaan komt
	 * @param arg - het object waar wat mee gedaan moet worden
	 */
	@SuppressWarnings("unchecked")
	public void update(Observable o, Object arg) {
		if (o instanceof RailCab) {
			if (arg instanceof ArrayList) {
				berekenReizigersData((ArrayList<Reiziger>) arg);
				return;
			}
		}

		if (o instanceof Station) {
			if (arg instanceof Groep) {
				simulatieData.addGroep((Groep) arg);
				return;
			}
		}

		if (o instanceof Simulatie) {
			if (arg instanceof Date) {
				berekenRailCabData((Date) arg);
				berekenReizigersData((Date) arg);
				return;
			}
		}
	}
}
