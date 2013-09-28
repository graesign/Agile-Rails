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

import com.tigam.railcab.gui.view.Instelling;
import com.tigam.railcab.language.Language;
import com.tigam.railcab.model.baan.Groep;
import com.tigam.railcab.model.baan.RailCab;
import com.tigam.railcab.model.baan.Station;

/**
 * klasse om de data bij te houden en data te geven
 * 
 * @author Tonny Wildeman
 */
public class SimulatieData {

	/**
	 * De gehele serie waarin de wachttijd van de reiziger bijgehouden wordt
	 */
	private XYSeriesRailCab wachttijdReiziger;

	/**
	 * De live serie waarin de wachttijd van de reiziger bijgehouden wordt
	 */
	private XYSeriesRailCab wachttijdReizigerLive;

	/**
	 * De gehele serie waarin de gemiddelde wachttijd van de reiziger
	 * bijgehouden wordt
	 */
	private XYSeriesRailCab gemiddeldeWachttijdReiziger;

	/**
	 * De live serie waarin de gemiddelde wachttijd van de reiziger bijgehouden
	 * wordt
	 */
	private XYSeriesRailCab gemiddeldeWachttijdReizigerLive;

	/**
	 * De gehele serie waarin de afstand van de railcabs bijgehouden wordt
	 */
	private XYSeriesRailCab afstandDataRailCabs;

	/**
	 * De live serie waarin de afstand van de railcabs bijgehouden wordt
	 */
	private XYSeriesRailCab afstandDataRailCabsLive;

	/**
	 * De gehele serie waarin de afstand van de reizigers bijgehouden wordt
	 */
	private XYSeriesRailCab afstandDataReizigers;

	/**
	 * De live serie waarin de afstand van de reizigers bijgehouden wordt
	 */
	private XYSeriesRailCab afstandDataReizigersLive;

	/**
	 * De gehele serie waarin de baten bijgehouden wordt
	 */
	private XYSeriesRailCab baten;

	/**
	 * De live serie waarin de baten bijgehouden wordt
	 */
	private XYSeriesRailCab batenLive;

	/**
	 * De gehele serie waarin de kosten bijgehouden wordt
	 */
	private XYSeriesRailCab kosten;

	/**
	 * De live serie waarin de kosten bijgehouden wordt
	 */
	private XYSeriesRailCab kostenLive;

	/**
	 * De gehele serie waarin de winst bijgehouden wordt
	 */
	private XYSeriesRailCab winst;

	/**
	 * De live serie waarin de winst bijgehouden wordt
	 */
	private XYSeriesRailCab winstLive;

	/**
	 * De lijst waar de groepen bijgehouden wordt
	 */
	private ArrayList<Groep> groepen;

	/**
	 * De lijst waar de railcabs in opgeslagen liggen
	 */
	private ArrayList<RailCab> railCabs;

	/**
	 * De lijst waar de stations in opgeslagen liggen
	 */
	private ArrayList<Station> stations;

	/**
	 * De switch waar bepaald wordt of data veranderd, toegevoegd of verwijderd
	 * mag worden
	 */
	private boolean editable = true;

	/**
	 * De opslagplaats van de instellingen van de simulatie
	 */
	private Instelling instelling;

	/**
	 * De constructor die bijna alle serie objecten aanmaakt en de instellingen
	 * ontvangt
	 * 
	 * @param instelling
	 */
	public SimulatieData(Instelling instelling) {
		wachttijdReiziger = new XYSeriesRailCab(Language.getString("SimulatieData.0"));
		gemiddeldeWachttijdReiziger = new XYSeriesRailCab(Language.getString("SimulatieData.1"));
		afstandDataReizigers = new XYSeriesRailCab(Language.getString("SimulatieData.2"));
		baten = new XYSeriesRailCab(Language.getString("SimulatieData.3"));
		kosten = new XYSeriesRailCab(Language.getString("SimulatieData.4"));
		winst = new XYSeriesRailCab(Language.getString("SimulatieData.5"));

		this.instelling = instelling;

		wachttijdReizigerLive = new XYSeriesRailCab(Language.getString("SimulatieData.0"));
		wachttijdReizigerLive.setWindowSize(instelling.getWindowSizeLive());
		gemiddeldeWachttijdReizigerLive = new XYSeriesRailCab(Language.getString("SimulatieData.1"));
		gemiddeldeWachttijdReizigerLive.setWindowSize(instelling.getWindowSizeLive());
		afstandDataReizigersLive = new XYSeriesRailCab(Language.getString("SimulatieData.2"));
		afstandDataReizigersLive.setWindowSize(instelling.getWindowSizeLive());
		batenLive = new XYSeriesRailCab(Language.getString("SimulatieData.3"));
		batenLive.setWindowSize(instelling.getWindowSizeLive());
		kostenLive = new XYSeriesRailCab(Language.getString("SimulatieData.4"));
		kostenLive.setWindowSize(instelling.getWindowSizeLive());
		winstLive = new XYSeriesRailCab(Language.getString("SimulatieData.5"));
		winstLive.setWindowSize(instelling.getWindowSizeLive());

		groepen = new ArrayList<Groep>();
	}

	/**
	 * Voegt de baten toe aan de live serie en de gehele serie
	 * 
	 * @param tijd - de tijd waarop de baten zijn
	 * @param baten - de baten op tijdstip tijd
	 * @param notify - moeten de luisteraars geupdate worden, voornamelijk voor
	 *            de performance van de grafieken
	 */
	public void addBaten(double tijd, double baten, boolean notify) {
		if (editable) {
			this.baten.add(tijd, baten, notify);
			batenLive.add(tijd, baten, notify);
		}
	}

	/**
	 * Voegt de gemiddelde wachttijd toe aan de live serie en de gehele serie
	 * 
	 * @param tijd - de tijd waarop de gemiddelde wachttijd was
	 * @param gemiddeldeWachttijd - de gemiddelde wachttijd op tijdstip tijd
	 * @param notify - moeten de luisteraars geupdate worden, voornamelijk voor
	 *            de performance van de grafieken
	 */
	public void addGemiddeldeWachttijd(double tijd, double gemiddeldeWachttijd, boolean notify) {
		if (editable) {
			gemiddeldeWachttijdReiziger.add(tijd, gemiddeldeWachttijd, notify);
			gemiddeldeWachttijdReizigerLive.add(tijd, gemiddeldeWachttijd, notify);
		}
	}

	/**
	 * Voegt een groep toe voor de replay functie
	 * 
	 * @param groep - de toe te voegen groep
	 */
	public void addGroep(Groep groep) {
		if (editable) {
			if (groepen.contains(groep)) {
				if (groep.getAantalWachtenden() > 0) {
					groepen.remove(groep);
				}
			} else {
				groepen.add(groep);
			}
		}
	}

	/**
	 * Voegt de kosten toe aan de live serie en de gehele serie
	 * 
	 * @param tijd - de tijd waarop de gemiddelde wachttijd was
	 * @param kostenRailCabs - de kosten van de railcabs op tijdstip tijd
	 * @param notify - moeten de luisteraars geupdate worden, voornamelijk voor
	 *            de performance van de grafieken
	 */
	public void addKosten(double tijd, double kostenRailCabs, boolean notify) {
		if (editable) {
			kosten.add(tijd, kostenRailCabs, notify);
			kostenLive.add(tijd, kostenRailCabs, notify);
		}
	}

	/**
	 * Voegt de totaal afgelegde afstand van de railcabs toe aan de live serie
	 * en de gehele serie
	 * 
	 * @param tijd - de tijd waarop de afstand was
	 * @param afstandRailCabs - de afstand van de railcabs op tijdstip tijd
	 * @param notify - moeten de luisteraars geupdate worden, voornamelijk voor
	 *            de performance van de grafieken
	 */
	public void addTotaalAfgelegdRailCabs(double tijd, double afstandRailCabs, boolean notify) {
		if (editable) {
			afstandDataRailCabs.add(tijd, afstandRailCabs, notify);
			afstandDataRailCabsLive.add(tijd, afstandRailCabs, notify);
		}
	}

	/**
	 * Voegt de totaal afgelegde afstand van de reizigers toe aan de live serie
	 * en de gehele serie
	 * 
	 * @param tijd - de tijd waarop de afstand was
	 * @param afstandReizigers - de afstand van de reizigers op tijdstip tijd
	 * @param notify - moeten de luisteraars geupdate worden, voornamelijk voor
	 *            de performance van de grafieken
	 */
	public void addTotaalAfgelegdReizigers(double tijd, double afstandReizigers, boolean notify) {
		if (editable) {
			afstandDataReizigers.add(tijd, afstandReizigers, notify);
			afstandDataReizigersLive.add(tijd, afstandReizigers, notify);
		}
	}

	/**
	 * Voegt de wachttijd toe aan de live serie en de gehele serie
	 * 
	 * @param tijd - de tijd waarop de wachttijd was
	 * @param wachttijd - de wachttijd op tijdstip tijd
	 * @param notify - moeten de luisteraars geupdate worden, voornamelijk voor
	 *            de performance van de grafieken
	 */
	public void addWachttijd(double tijd, double wachttijd, boolean notify) {
		if (editable) {
			wachttijdReiziger.add(tijd, wachttijd, notify);
			wachttijdReizigerLive.add(tijd, wachttijd, notify);
		}
	}

	/**
	 * Voegt de winst toe aan de live serie en de gehele serie
	 * 
	 * @param tijd - de tijd waarop de winst was
	 * @param winst - de winst op tijdstip tijd
	 * @param notify - moeten de luisteraars geupdate worden, voornamelijk voor
	 *            de performance van de grafieken
	 */
	public void addWinst(double tijd, double winst, boolean notify) {
		if (editable) {
			this.winst.add(tijd, winst, notify);
			winstLive.add(tijd, winst, notify);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SimulatieData)) {
			return false;
		}
		ArrayList<Groep> objGroepen = ((SimulatieData) obj).getGroepen();
		if (groepen.size() != objGroepen.size()) {
			return false;
		}
		for (int i = 0; i < groepen.size(); i++) {
			Groep g1 = groepen.get(i);
			Groep g2 = objGroepen.get(i);
			if (!g1.equals(g2)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Geeft de baten serie terug. Afhankelijk van <code>editable</code> wordt
	 * de live serie of de gehele serie teruggegeven.
	 * 
	 * @return de baten serie
	 */
	public XYSeriesRailCab getBatenSerie() {
		if (editable) {
			return batenLive;
		}
		return baten;
	}

	/**
	 * Geeft de gemiddelde wachttijd serie terug. Afhankelijk van
	 * <code>editable</code> wordt de live serie of de gehele serie
	 * teruggegeven.
	 * 
	 * @return de gemiddelde wachttijd serie
	 */
	public XYSeriesRailCab getGemiddeldeWachttijdSerie() {
		if (editable) {
			return gemiddeldeWachttijdReizigerLive;
		}
		return gemiddeldeWachttijdReiziger;
	}

	/**
	 * Geeft de groepen terug
	 * 
	 * @return de groepen
	 */
	public ArrayList<Groep> getGroepen() {
		// TODO: gesorteerd teruggeven
		return groepen;
	}

	/**
	 * Geeft de instellingen terug
	 * 
	 * @return de instelling
	 */
	public Instelling getInstelling() {
		return instelling;
	}

	/**
	 * Geeft de kosten serie terug. Afhankelijk van <code>editable</code>
	 * wordt de live serie of de gehele serie teruggegeven.
	 * 
	 * @return de kosten serie
	 */
	public XYSeriesRailCab getKostenSerie() {
		if (editable) {
			return kostenLive;
		}
		return kosten;
	}

	/**
	 * Geeft de lijst met railcabs terug
	 * 
	 * @return de lijst met railcabs
	 */
	public ArrayList<RailCab> getRailCabs() {
		return railCabs;
	}

	/**
	 * Geeft de lijst met stations terug
	 * 
	 * @return de lijst met stations
	 */
	public ArrayList<Station> getStations() {
		return stations;
	}

	/**
	 * Geeft de totale afstand van de railcabs serie terug. Afhankelijk van
	 * <code>editable</code> wordt de live serie of de gehele serie
	 * teruggegeven.
	 * 
	 * @return de totale afstand van de railcabs serie
	 */
	public XYSeriesRailCab getTotaalAfstandRailCabsSerie() {
		if (editable) {
			return afstandDataRailCabsLive;
		}
		return afstandDataRailCabs;
	}

	/**
	 * Geeft de totale afstand van de reizigers terug. Afhankelijk van
	 * <code>editable</code> wordt de live serie of de gehele serie
	 * teruggegeven.
	 * 
	 * @return de totale afstand van de reizigers serie
	 */
	public XYSeriesRailCab getTotaalAfstandReizigersSerie() {
		if (editable) {
			return afstandDataReizigersLive;
		}
		return afstandDataReizigers;
	}

	/**
	 * Geeft de wachttijd serie terug. Afhankelijk van <code>editable</code>
	 * wordt de live serie of de gehele serie teruggegeven.
	 * 
	 * @return de wachttijd serie
	 */
	public XYSeriesRailCab getWachttijdSerie() {
		if (editable) {
			return wachttijdReizigerLive;
		}
		return wachttijdReiziger;
	}

	/**
	 * Geeft de winst serie terug. Afhankelijk van <code>editable</code> wordt
	 * de live serie of de gehele serie teruggegeven.
	 * 
	 * @return de winst serie
	 */
	public XYSeriesRailCab getWinstSerie() {
		if (editable) {
			return winstLive;
		}
		return winst;
	}

	/**
	 * Zorgt er voor dat de data niet meer veranderd kan worden en dat de gehele
	 * series teruggegeven worden.
	 * 
	 */
	public void setNonEditable() {
		editable = false;
	}

	/**
	 * Ontvangt de lijst met railcabs en maakt de benodigde railcab series aan
	 * 
	 * @param railCabs - de railcabs
	 */
	public void setRailCabs(ArrayList<RailCab> railCabs) {
		if (editable) {
			this.railCabs = railCabs;
		}
		afstandDataRailCabs = new XYSeriesRailCab(Language.getString("SimulatieData.6"));
		afstandDataRailCabsLive = new XYSeriesRailCab(Language.getString("SimulatieData.6"));
		afstandDataRailCabsLive.setWindowSize(instelling.getWindowSizeLive());
	}

	/**
	 * Ontvangt de lijst met stations
	 * 
	 * @param stations - de stations
	 */
	public void setStations(ArrayList<Station> stations) {
		if (editable) {
			this.stations = stations;
		}
	}

	@Override
	public String toString() {
		return instelling.getOmschrijving();
	}
}
