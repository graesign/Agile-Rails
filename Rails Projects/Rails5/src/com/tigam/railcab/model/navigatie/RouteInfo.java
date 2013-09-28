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

import com.tigam.railcab.model.baan.Wissel;

/**
 * 
 * Een Route Object
 * 
 * @author Nils Dijk
 * 
 */
public class RouteInfo implements Comparable<RouteInfo> {
	/**
	 * De stand die de wissels moeten hebben voor de gevraagde route
	 */
	private HashMap<Wissel, Boolean> routeInfo;

	/**
	 * De afstand van de route
	 */
	private int afstand;

	/**
	 * Maak een nieuwe Route aan
	 */
	public RouteInfo() {
		this(null);
	}

	/**
	 * Maak een Route Object aan de hand van een oud Route Object
	 * 
	 * @param oud het oude object
	 */
	public RouteInfo(RouteInfo oud) {
		routeInfo = new HashMap<Wissel, Boolean>();
		afstand = 0;

		if (oud != null) {
			routeInfo = oud.getWisselMap();
			afstand = oud.getAfstand();
		}
	}

	/**
	 * Voeg een afstand toe aan de route
	 * 
	 * @param afstand de afstand die er bijgevoeg moet worden
	 */
	public void add(int afstand) {
		this.afstand += afstand;
	}

	/**
	 * Voeg een wissel toe met stand en afstand
	 * 
	 * @param wissel De wissel
	 * @param stand De stand
	 * @param afstand De afstand
	 */
	public void add(Wissel wissel, Boolean stand, int afstand) {
		this.add(afstand);
		routeInfo.put(wissel, stand);
	}

	// @Override
	public int compareTo(RouteInfo comp) {
		if (getAfstand() > comp.getAfstand()) {
			return 1;
		} else if (getAfstand() < comp.getAfstand()) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * Vraag de afstand op
	 * 
	 * @return De afstand
	 */
	public int getAfstand() {
		return afstand;
	}

	/**
	 * Vraag de wisselmap met goede richtingen op
	 * 
	 * @return De WisselMap
	 */
	public HashMap<Wissel, Boolean> getWisselMap() {
		HashMap<Wissel, Boolean> wm = new HashMap<Wissel, Boolean>(routeInfo);

		return wm;
	}

	@Override
	public String toString() {
		String out = "Afstand: " + afstand + "\n";
		for (Wissel w : routeInfo.keySet()) {
			out += "wissel (" + w.getLocatie().x + ", " + w.getLocatie().y + ") : " + routeInfo.get(w) + "\n";
		}
		return out;
	}
}
