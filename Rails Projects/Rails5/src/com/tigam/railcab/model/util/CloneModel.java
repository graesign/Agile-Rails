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

package com.tigam.railcab.model.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import com.tigam.railcab.model.baan.Baandeel;
import com.tigam.railcab.model.baan.Station;
import com.tigam.railcab.model.baan.StationBaandeel;
import com.tigam.railcab.model.baan.Wissel;
import com.tigam.railcab.model.baan.WisselTerug;

/**
 * Maakt een copy van een complete baan, gegeven één baandeel.
 * 
 * @author Arjan van der Velde
 * 
 */
public class CloneModel {

	/**
	 * Methode om een deepCopy te maken van de baan. Werkt met zijn eigen stack,
	 * dus een stackoverflow zit er niet zo snel meer in.
	 * 
	 * 
	 * @param baan De baan waar een deepCopy van moet worden gemaakt
	 * @return Genereert een nieuw baan met de zelfde structuur als de gegeven
	 *         baan.
	 */
	public static Baandeel clone(Baandeel baan) {
		Stack<Baandeel> baanToDo = new Stack<Baandeel>();
		HashMap<Baandeel, Baandeel> baanMap = new HashMap<Baandeel, Baandeel>();
		HashMap<Station, StationBaandeel> stationMap = new HashMap<Station, StationBaandeel>();
		ArrayList<Station> stations = new ArrayList<Station>();
		baanToDo.push(baan);

		// Baandelen uit de oude baan
		Baandeel working;

		// Baandelen uit de nieuwe baan
		Baandeel b = null, vorige = null;

		Point l;

		while (baanToDo.size() > 0) {
			working = baanToDo.pop();
			if (baanMap.containsKey(working.getVorige())) {
				b = baanMap.get(working.getVorige());
			} else {
				b = null;
			}
			while (working != null) {
				vorige = b;
				// als 'working' al een keer behandeld is, breek dan uit de while lus
				if (baanMap.containsKey(working)) {
					// kijk of je de WisselTerug nog moet verbinden met de vorige node
					if (working instanceof WisselTerug) {
						WisselTerug w = (WisselTerug) baanMap.get(working);
						try {
							w.zetOm();
							w.setVorige(vorige);
							w.zetOm();
						} catch (Exception ex) {
						}
					}
					break;
				}

				l = new Point(working.getLocatie());

				if (working instanceof WisselTerug) { // Maak een nieuwe WisselTerug
					b = new WisselTerug(l);

					// zet de stand van de nieuwe wissel terug in de zelfde stand als de oude wissel terug;
					if (((WisselTerug) working).getStand() != ((WisselTerug) b).getStand()) {
						try {
							((WisselTerug) b).zetOm();
						} catch (Exception ex) {
						}
					}

				} else if (working instanceof Wissel) { // Maak een nieuwe Wissel
					b = new Wissel(l);

					// Zet de wissel in de zelfde stand als zijn oude tegenhanger
					if (((Wissel) working).getStand() != ((Wissel) b).getStand()) {
						try {
							((Wissel) b).zetOm();
						} catch (Exception ex) {
						}
					}

					// Zet de andere rijRichting op de stack, deze wordt behandeld als we de hele baan rond zijn geweest
					baanToDo.push(((Wissel) working).getAndere());
				} else if (working instanceof StationBaandeel) { // Maak een nieuw StationBaandeel
					b = new StationBaandeel(l);
					StationBaandeel s = (StationBaandeel) working;
					if (!stationMap.containsKey(s.getStation())) {
						stationMap.put(s.getStation(), (StationBaandeel) b);
					}
					if (!stations.contains(s.getStation())) {
						stations.add(s.getStation());
					}
				} else {
					b = new Baandeel(l);
				}

				// verbind de vorige node met de huidige node
				if (vorige != null) {
					vorige.setVolgende(b);
				}

				// als de vorige node een Wissel was (geen WisselTerug) zet deze dan om.
				if (vorige instanceof Wissel && !(vorige instanceof WisselTerug)) {
					try {
						((Wissel) vorige).zetOm();
					} catch (Exception ex) {
					}
				}

				baanMap.put(working, b);

				// pak een nieuw stuk baan om te verwerken
				working = working.getVolgende();
			}
		}

		vorige = baanMap.get(baan.getVorige());

		if (vorige == null) {
			System.out.println("Baangrote baanMap.size() " + baanMap.size());
			System.out.println("Vorige Baan " + baan.getVorige());

			System.out.println("Baan Type " + baan.getClass().getName());

			System.out.println("ERROR");
		}

		b = baanMap.get(baan);
		b.setVorige(vorige);

		for (int i = 0; i < stations.size(); i++) {
			StationBaandeel s = stationMap.get(stations.get(i));
			while (s.getVorige() instanceof StationBaandeel) {
				s = (StationBaandeel) s.getVorige();
			}
			new Station(stations.get(i).getNaam(), s);
		}
		return b;
	}
}
