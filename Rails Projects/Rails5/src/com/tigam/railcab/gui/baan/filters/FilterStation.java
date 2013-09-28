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

package com.tigam.railcab.gui.baan.filters;

import java.awt.Color;
import java.util.ArrayList;

import com.tigam.railcab.gui.Kleuren;
import com.tigam.railcab.language.Language;
import com.tigam.railcab.model.baan.Baandeel;
import com.tigam.railcab.model.baan.Station;
import com.tigam.railcab.model.baan.StationBaandeel;

/**
 * 
 * Een filter om de drukte op station weer te geven
 * 
 * @author Nils Dijk
 * 
 */
public class FilterStation extends Filter {

	/**
	 * Lijst met alle stations, wordt dynamisch gevuld
	 */
	private ArrayList<Station> stations;

	/**
	 * Maak het Filter
	 */
	public FilterStation() {
		filterKleur = Kleuren.FilterStation;

		stations = new ArrayList<Station>();
	}

	@Override
	public Color getColor(Baandeel baan) {
		if (!(baan instanceof StationBaandeel)) {
			return null;
		}
		Station s = ((StationBaandeel) baan).getStation();

		if (!stations.contains(s)) {
			stations.add(s);
		}

		int totaal = 0;

		for (int i = 0; i < stations.size(); i++) {
			totaal = Math.max(totaal, stations.get(i).getAantalWachtenden());
		}

		int mine = s.getAantalWachtenden();

		Color kleur;

		// bereken de kleur
		if (totaal > 0 && mine > 0) {
			kleur = new Color(filterKleur.getRed(), filterKleur.getGreen(), filterKleur.getBlue(),
					(255 * mine / totaal));
		} else {
			kleur = null;
		}

		return kleur;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tigam.railcab.gui.baan.filters.Filter#toString()
	 */
	@Override
	public String toString() {
		return Language.getString("Filter.2");
	}

}