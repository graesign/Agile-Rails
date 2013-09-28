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

package com.tigam.railcab.schedulers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import com.tigam.railcab.controller.Scheduler;
import com.tigam.railcab.controller.Treinbaan;
import com.tigam.railcab.model.baan.Baandeel;
import com.tigam.railcab.model.baan.Groep;
import com.tigam.railcab.model.baan.RailCab;
import com.tigam.railcab.model.baan.RailCabMissie;
import com.tigam.railcab.model.baan.Station;
import com.tigam.railcab.model.baan.StationBaandeel;
import com.tigam.railcab.model.baan.Wissel;
import com.tigam.railcab.model.baan.WisselTerug;
import com.tigam.railcab.model.exception.RailCabException;
import com.tigam.railcab.model.navigatie.Navigator;
import com.tigam.railcab.model.simulatie.Date;

/**
 * Een test implementatie van {@code Scheduler}. Deze scheduler handelt
 * reizigers in een fifo fashion af.
 * 
 * @author Arjan van der Velde
 */
public class GlobalFIFOScheduler extends Scheduler {

	/**
	 * Een simpele wrapper om een groep en een station aan elkaar te koppelen
	 * 
	 * @author Arjan van der Velde
	 * 
	 */
	private class GroepOpStation {

		/** de groep */
		Groep groep;

		/** het statioon */
		Station station;

		/** aantal nog af te handelen reizigers */
		int nogTeDoen;

		/**
		 * Constructor, maak een GroepOpStation wrapper
		 * 
		 * @param groep de groep
		 * @param station het station
		 */
		public GroepOpStation(Groep groep, Station station) {
			this.groep = groep;
			this.station = station;
			nogTeDoen = groep.getAantalReizigers();
		}

	}

	/** todo list voor de scheduler */
	private LinkedList<GroepOpStation> todolijst;

	/** lijst van groepen, zodat removeGroep event kan worden gedetecteerd */
	private HashSet<Groep> groeplijst;

	/** lijst van "vrije" in te zetten railcabs */
	private HashSet<RailCab> vrijecabs;

	/** lijst van wachtende cabs */
	private HashSet<RailCab> wachtendecabs;

	//	/** lijst van alle baandelen */
	//	private ArrayList<Baandeel> baandelen;

	/** lijst van alle stations */
	private ArrayList<Station> stations;

	/** lijst van aantallen op stations (eigen administratie) */
	private HashMap<Station, Integer> aantallen;

	/**
	 * Construct de scheduler.
	 * 
	 * @param controller
	 */
	public GlobalFIFOScheduler(Treinbaan controller) {
		super(controller);
		reset();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void actie() throws RailCabException {
		GroepOpStation eerste;
		int aantalvrijecabs; // vrijecabs snapshot.

		// Deadlock detection
		boolean deadlock = false;
		synchronized (wachtendecabs) {
			int size = wachtendecabs.size();
			if (size > 2) { // voor deadlocks moeten er tenminste drie (twee?)
				// zijn...
				for (RailCab r : wachtendecabs) {
					RailCab w = r;
					for (int i = 0; i < size; i++) {
						w = w.getVeroorzaker();
						if (w == null) {
							break;
						}
						if (w == r) {
							// deadlock... kies een random slachtoffer...
							System.out.println(new Date().toString() + ": deadlock!");
							// ArrayList<RailCab> slachtoffers = new ArrayList<RailCab>();
							RailCab slachtoffer = null;
							for (RailCab s : wachtendecabs) { // zoek cabs die voor een wissel staan die niet bezet is.
								if (s.getHuidigePositie().getVolgende() instanceof Wissel
										&& !(s.getHuidigePositie().getVolgende() instanceof WisselTerug) // maar geen wisselterug
										&& !s.getHuidigePositie().getVolgende().hasRailCab()) { // en er staat geen railcab op
									// potentiëel slachtoffer...
									if (slachtoffer == null
											|| slachtoffer.getAantalReizigers() > s.getAantalReizigers()) {
										// we hebben één met minder reizigers gevonden...
										slachtoffer = s;
										if (slachtoffer.getAantalReizigers() == 0) {
											break; // lager dan nul kan niet.. stoppen met zoeken dus :)
										}
									}
								}
							}
							// stuur gekozen cab de verkeerde kant op...
							if (slachtoffer != null) {
								slachtoffer.setGaVerkeerd();
							}
							deadlock = true;
						}
						if (deadlock) {
							break; // deadlock gedetecteerd. stop met kijken.
						}
					}
				}
			} // if (size > 2)
		} // sync

		// Schop veroorzakers van opstoppingen...
		if (!deadlock) { // TODO: Is dit terecht? - gok erop dat als een deadlock is verholpen, er weer leven in de brouwerij komt...
			ArrayList<RailCab> removelist = new ArrayList<RailCab>();
			synchronized (wachtendecabs) {
				int i = 0;
				RailCab[] kick_cabs = new RailCab[wachtendecabs.size()];
				HashMap<Wissel, Boolean>[] kick_maps = new HashMap[wachtendecabs.size()];
				for (RailCab r : wachtendecabs) {
					if (r.getRailCabStatus() == RailCab.STATUS_WACHT) {
						RailCab v = r.getVeroorzaker();
						kick_cabs[i] = v;
						kick_maps[i] = r.getWisselMap();
						i++;
					}
					removelist.add(r);
				}
				for (int j = 0; j < i; j++) {
					migreer(kick_cabs[j], kick_maps[j]);
				}
				for (RailCab r : removelist) { // cleanup wait list...
					wachtendecabs.remove(r);
				}
			}
		} // !deadlock

		// Is er uberhaupt iets te doen?
		synchronized (todolijst) {
			if (todolijst.size() > 0) {
				while (todolijst.size() > 0) {
					eerste = todolijst.getFirst();
					if (!eerste.groep.isDeleted()) {
						break;
					} else {
						todolijst.removeFirst(); // verwijder items waarvan
						// de groep verdwenen is.
					}
				}
			} else {
				return; // niets te doen...
			}
		} // sync

		// Hoeveel vrije cabs? (snapshot)
		synchronized (vrijecabs) {
			aantalvrijecabs = vrijecabs.size();
			if (aantalvrijecabs == 0) {
				return; // geen vrije cabs...
			}
		}

		while (true) { // zie stop-conditie hieronder...

			// Zolang er items op de todolijst staan...
			synchronized (todolijst) {
				if (todolijst.size() > 0) {
					eerste = todolijst.getFirst();
				} else {
					break;
				}
			}

			// Zolang er niet-toegewezen wachtenden zijn...
			while (eerste.nogTeDoen > 0) {
				// Zolang er vrije cabs zijn...
				if (aantalvrijecabs > 0) {
					// er zijn nog vrijecabs...
					RailCab railcab = null;
					Baandeel baandeel = eerste.station.getEersteBaandeel();
					while (baandeel.getVolgende() instanceof StationBaandeel) { // zoek laatste baandeel
						baandeel = baandeel.getVolgende();
					}
					synchronized (vrijecabs) { // zoek dichstbijzijnde cab
						for (RailCab r : vrijecabs) {
							if (railcab == null) {
								railcab = r;
								continue; // eerste iteratie
							}
							int afstand_r = Navigator.afstand(r.getHuidigePositie(), baandeel);
							int afstand_railcab = Navigator.afstand(railcab.getHuidigePositie(), baandeel);
							// System.out.println("r: " + afstand_r + ",
							// railcab: " + afstand_railcab);
							if (afstand_r < afstand_railcab) { // we hebben eentje dichterbij gevonden
								railcab = r;
							}
						}
					}
					aantalvrijecabs--; // verlaag snapshot

					RailCabMissie missie = new RailCabMissie(eerste.station.getEersteBaandeel());
					ArrayList<Groep> groepen = new ArrayList<Groep>(); // maak een groepenlijst...
					groepen.add(eerste.groep); // TODO: met 1 groep voor nu
					missie.setGroepen(groepen);
					missie.setEindLocatie(eerste.groep.getBestemming().getEersteBaandeel());

					if (railcab.getHuidigeMissie() != null) {
						railcab.setMissieStop();
					}

					railcab.setKoppelen(controller.getSimulatieDataController().getHuidigeSimulatieHandler()
							.getSimulatieData().getInstelling().isKoppelen()); // tja...
					railcab.setMissie(missie);
					railcab.setMissieStart();
					// EIND stuur RailCab op pad

					// er is een cab op pad gestuurd...
					eerste.nogTeDoen -= railcab.getMaxReizigers();
					synchronized (vrijecabs) {
						vrijecabs.remove(railcab);
					}

					// is de groep gescheduled?
					if (eerste.nogTeDoen <= 0) {
						synchronized (todolijst) {
							todolijst.remove(eerste);
						}
					}

				} else {
					return; // geen railcabs over...
				}
			}
		} // while (true)

	}

	/**
	 * Verplaats naar een geschikt baandeel...
	 * 
	 * @param r de te migreren cab
	 * @param route wisselMap van de route waarvoor we in de weg staan
	 * @throws RailCabException
	 */
	private void migreer(RailCab r, HashMap<Wissel, Boolean> route) throws RailCabException {
		Baandeel p = null;
		if (r.getRailCabStatus() == RailCab.STATUS_IDLE) { // doublecheck status...
			if (r.getHuidigePositie() instanceof StationBaandeel) { // r staat op een station
				p = r.getHuidigePositie();
				while (p instanceof StationBaandeel) { // ga naar eerst volgende baandeel na het station
					if (p.hasRailCab()) {
						RailCab n = p.getRailCab();
						if (n.getRailCabStatus() == RailCab.STATUS_IDLE) {
							r = n;
						} else {
							return; // er is nog/komt nog plek op het station?
						}
					} else {
						return; // er is nog/komt nog plek op het station?
					}
					p = p.getVolgende();
				}
			}
			p = uitDeWeg(r, route);
			// Geen r de nieuwe missie...
			RailCabMissie missie = new RailCabMissie(p);
			r.setMissie(missie);
			//			System.out.println("RailCab: " + r.hashCode() + "\n" + "RouteInfo:\n" + Navigator.routeInfo(r.getHuidigePositie(), nieuwepos));
			r.setMissieStart();
		}
	}

	@Override
	protected void processBaandeelEvent(Baandeel baandeel, RailCab railcab) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processRailCabStatusChange(RailCab railcab, int status) {

		// Werk wachtlijst bij...
		if (status != RailCab.STATUS_WACHT) {
			synchronized (wachtendecabs) {
				if (wachtendecabs.contains(railcab)) {
					wachtendecabs.remove(railcab);
				}
			}
		} else {
			synchronized (wachtendecabs) {
				wachtendecabs.add(railcab);
			}
			// quick kick! Wacht de cyclus niet af...
			if (railcab.getVeroorzaker() != null && railcab.getVeroorzaker().getRailCabStatus() != RailCab.STATUS_WACHT) {
				try {
					migreer(railcab.getVeroorzaker(), railcab.getWisselMap());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// Zijn we idle?
		if (status == RailCab.STATUS_IDLE) {
			synchronized (vrijecabs) {
				vrijecabs.add(railcab);
			}
		}
	}

	@Override
	protected void processStationGroepEvent(Station station, Groep groep) {
		boolean contains = false;
		synchronized (groeplijst) { // klein lokje
			contains = groeplijst.contains(groep);
		}
		if (!contains) { // voeg to aan todo lijst...
			synchronized (todolijst) {
				todolijst.addLast(new GroepOpStation(groep, station));
			}
			synchronized (groeplijst) { // klein lokje
				groeplijst.add(groep);
			}
		} else {
			groep.setDeleted(); // graceful delete
			synchronized (groeplijst) { // klein lokje
				groeplijst.remove(groep);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tigam.railcab.controller.scheduler.Scheduler#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		groeplijst = new HashSet<Groep>();
		todolijst = new LinkedList<GroepOpStation>();
		vrijecabs = new HashSet<RailCab>();
		wachtendecabs = new HashSet<RailCab>();
		for (RailCab r : controller.getRailCabs()) {
			synchronized (vrijecabs) {
				vrijecabs.add(r);
			}
		}
		stations = controller.getBaanHeader().getStations();
		//		baandelen = controller.getBaanHeader().getBaandelen();
		aantallen = new HashMap<Station, Integer>();
		for (Station s : stations) {
			int aantal = s.getAantalWachtenden();
			if (aantal > 0) {
				aantallen.put(s, aantal);
			}
		}
	}

	/**
	 * Bepaal de eerst volgende positie voor een railcab, waarbij we niet in de
	 * wegstaan gegeven een wissel map voor die route.
	 * 
	 * @param staInDeWeg de cab
	 * @param route wissel map van de route
	 * @return de posisite waarbij we niet in de weg staan
	 */
	private Baandeel uitDeWeg(RailCab staInDeWeg, HashMap<Wissel, Boolean> route) {

		// 1 - we staan al voorbij het eindpunt van deWeg: 1 opschuiven
		//     dit wordt bepaald a.d.h.v. de eerst volgende wissel en is
		//     dus niet altijd juist....
		// 2 - we staan in de weg: ga tot voorbij het uitpunt van deWeg of
		//   - tot de eerst volgende wissel en ga de andere kant op.

		// eigen positie
		Baandeel pos = staInDeWeg.getHuidigePositie();

		// eerst volgende wissel.
		Baandeel w = pos;
		if (w instanceof Wissel && !(w instanceof WisselTerug)) {
			// staInDeWeg staat op een wissel
			w = w.getVolgende();
		}
		Wissel wissel;
		while (w instanceof WisselTerug || !(w instanceof Wissel)) {
			w = w.getVolgende();
		}
		wissel = (Wissel) w;

		// als de wissel niet op de kaart staat, schuiven we 2 op
		if (!route.containsKey(wissel)) {
			return pos.getVolgende().getVolgende();
		} else {
			// als de wisel wel op de kaart staat, gaan we voorbij de 
			// wissel staan, in de andere richting als in route info.
			if (route.get(wissel).booleanValue()) {
				return wissel.getStand() ? wissel.getAndere() : wissel.getVolgende();
			} else {
				return !wissel.getStand() ? wissel.getAndere() : wissel.getVolgende();
			}
		}
	}

}
