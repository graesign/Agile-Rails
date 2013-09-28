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

package com.tigam.railcab.model.navigatie;

import java.util.HashMap;
import java.util.LinkedList;

import com.tigam.railcab.model.baan.Baandeel;
import com.tigam.railcab.model.baan.StationBaandeel;
import com.tigam.railcab.model.baan.Wissel;

/**
 * Klasse waarmee genavigeerd kan worden oven een baan (als in
 * {@code com.tigam.railcab.model.baan}
 * 
 * Van deze klasse hoeft geen object geinstantieerd te worden, aangezien alle
 * public methoden static zijn. Gebruik de volgende methoden voor het verkrijgen
 * van route informatie: - alle varianten van {@code afstand()} - alle varianten
 * van {@code routeInfo()} - alle varianten van {@code routeKaart()}
 * 
 * @author Arjan van der Velde
 * 
 */
public class Navigator {

	/**
	 * Het gewicht van een baandeel op een route
	 */
	public static final int KOSTEN_BAANDEEL = 1;

	/**
	 * Het gewicht van een wissel terug op een route
	 */
	public static final int KOSTEN_WISSELTERUG = 1;

	/**
	 * Het gewicht van een wissel op een route
	 */
	public static final int KOSTEN_WISSEL = 1;

	/**
	 * Het gewicht van een station op een route
	 */
	public static final int KOSTEN_STATIONBAANDEEL = 30;

	/**
	 * Een HashMap met alle wissels tot eerst volgende wissel lengtes
	 */
	private static HashMap<Wissel, ParseWissel> wisselMap = null;

	/**
	 * TriHashMap
	 */
	private static TriHashMap<Wissel, Wissel, RouteInfo> cache = null;

	/**
	 * Geef de afstand (kosten) voor de kortste afstand tussen twee punten.
	 * Gebruikt cache.
	 * 
	 * @param start hier te starten
	 * @param eind eindpunt
	 * @return afstand (kosten)
	 */
	public static int afstand(Baandeel start, Baandeel eind) {
		return parse(start, eind).getAfstand();
	}

	/**
	 * Methode voor het uitzoeken van een route
	 * 
	 * @param start De wissel bij het begin
	 * @param stop De wissel bij het eind
	 * @return De route
	 */
	private static RouteInfo getWisselToWissel(Wissel start, Wissel stop) {
		if (Navigator.cache == null) {
			Navigator.cache = new TriHashMap<Wissel, Wissel, RouteInfo>();
		}
		if (Navigator.wisselMap == null) {
			Navigator.renderWisselMap(start);
		}

		if (!Navigator.wisselMap.containsKey(start)) {
			Navigator.cache = new TriHashMap<Wissel, Wissel, RouteInfo>();
			Navigator.renderWisselMap(start);
		}

		if (cache.containsKeys(start, stop)) {
			return cache.get(start, stop);
		}

		ParseWissel p = wisselMap.get(start);
		if (p == null) {
			return null;
		}

		RouteInfo r, rn, ro, finalRoute = null;
		HashMap<Wissel, RouteInfo> alleRoutes = new HashMap<Wissel, RouteInfo>();
		LinkedList<Wissel> wisselsToDo = new LinkedList<Wissel>();

		alleRoutes.put(start, new RouteInfo());
		wisselsToDo.add(start);

		Wissel w;

		while (wisselsToDo.size() > 0) {
			w = wisselsToDo.poll();
			p = wisselMap.get(w);
			r = alleRoutes.get(w);

			if (w == stop) {
				finalRoute = r;
				continue;
			}

			if (finalRoute != null && finalRoute.compareTo(r) == -1) {
				continue;
			}

			rn = new RouteInfo(r);
			rn.add(w, false, p.getLengte1());
			if (//(finalRoute == null || finalRoute.compareTo(rn) == 1) &&										// is er geen route gevonden OF de route de gevonden is is groter dan de nieuw berekende route. GA DOOR
			!alleRoutes.containsKey(p.getEind1()) || alleRoutes.get(p.getEind1()).compareTo(rn) == 1) { // bevat de nieuwe wissel geen route of de gevonde route is kleiner. GA DOOR
				alleRoutes.put(p.getEind1(), rn);
				wisselsToDo.add(p.getEind1());
			}

			if (p.getEind2() != null) { // in het geval van een wissel evalueer ook de tweede uitgang				
				ro = new RouteInfo(r);
				ro.add(w, true, p.getLengte2());
				if (//(finalRoute == null || finalRoute.compareTo(rn) == 1) &&									// zie comments hier boven
				!alleRoutes.containsKey(p.getEind2()) || alleRoutes.get(p.getEind2()).compareTo(ro) == 1) {
					alleRoutes.put(p.getEind2(), ro);
					wisselsToDo.add(p.getEind2());
				}
			}
		}

		cache.put(start, stop, alleRoutes.get(stop));

		return alleRoutes.get(stop);
	}

	/**
	 * Geef afstand en routekaart voor kortste route van a naar b
	 * 
	 * @param start a
	 * @param stop b
	 * @return cache entry met routekaart en afstand
	 */
	private static RouteInfo parse(Baandeel start, Baandeel stop) {
		Baandeel s1 = null;

		int l = 0;

		while (!(start instanceof Wissel) && start != stop) {
			if (start instanceof StationBaandeel) {
				l += Navigator.KOSTEN_STATIONBAANDEEL;
			} else if (start instanceof Baandeel) {
				l += Navigator.KOSTEN_BAANDEEL;
			}
			start = start.getVolgende();
		}

		if (start == stop) {
			RouteInfo rn = new RouteInfo();
			rn.add(l);
			return rn;
		}

		while (!(stop instanceof Wissel)) {
			if (stop instanceof StationBaandeel) {
				l += Navigator.KOSTEN_STATIONBAANDEEL;
			} else if (stop instanceof Baandeel) {
				l += Navigator.KOSTEN_BAANDEEL;
			}
			s1 = stop;
			stop = stop.getVorige();
		}
		RouteInfo rn = new RouteInfo(getWisselToWissel((Wissel) start, (Wissel) stop));

		synchronized (stop) {
			if (stop.getVolgende() == s1) {
				rn.add((Wissel) stop, ((Wissel) stop).getStand(), l);
			} else {
				rn.add((Wissel) stop, !((Wissel) stop).getStand(), l);
			}
		}

		return rn;
	}

	/**
	 * Pre-render de routes van
	 * 
	 * @param baan De baan waar een de preRender van moet komen
	 */
	private static void renderWisselMap(Baandeel baan) {
		LinkedList<Wissel> wisselsToDo = new LinkedList<Wissel>();

		Navigator.wisselMap = new HashMap<Wissel, ParseWissel>();

		Baandeel b = baan;
		Wissel w;
		ParseWissel p;

		while (!(b instanceof Wissel)) {
			b = b.getVolgende();
		}

		wisselsToDo.add((Wissel) b);

		while (wisselsToDo.size() > 0) {
			w = wisselsToDo.poll();
			if (Navigator.wisselMap.containsKey(w)) {
				continue;
			}

			p = new ParseWissel(w);
			wisselMap.put(w, p);

			if (p.getEind1() != null) {
				wisselsToDo.add(p.getEind1());
			}
			if (p.getEind2() != null) {
				wisselsToDo.add(p.getEind2());
			}
		}
	}

	/**
	 * Reset de Navigator
	 */
	public static void Reset() {
		Navigator.wisselMap = null;
		Navigator.cache = null;
	}

	/**
	 * Geeft een map met wissel standen voor de kortste route van een gegeven
	 * startlocatie naar een gegeven eindlocatie. Gebruikt cache.
	 * 
	 * @param start hier te starten
	 * @param eind eindpunt
	 * @return route kaart (map met wissel standen)
	 */
	public static RouteInfo routeInfo(Baandeel start, Baandeel eind) {
		return parse(start, eind);
	}

}
