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

import com.tigam.railcab.model.baan.Baandeel;

/**
 * 
 * Een abstractie laag voor de filters die over de baan heen liggen
 * 
 * @author Nils Dijk
 * 
 */
public abstract class Filter {

	/**
	 * Maakt een lijst met alle bekende objecten die Filter extended
	 * 
	 * @return een lijst met een object van elk bekende filter.
	 */
	public static ArrayList<Filter> getFilters() {
		ArrayList<Filter> filters = new ArrayList<Filter>();

		// voeg alle filters toe uit de filters package
		// LETOP: Moet handmatig
		filters.add(new FilterNone());
		filters.add(new FilterBaanGebruik());
		filters.add(new FilterStation());

		// geeft de lijft met filters terug
		return filters;
	}

	/**
	 * kleur van het filter
	 */
	protected Color filterKleur = null;

	/**
	 * Genereert de kleurcode van een baandeel volgens de formule van het filter
	 * 
	 * @param baan Baandeel waar het filter overheen moet
	 * @return een kleurcode om over een baandeel heen te leggen
	 */
	public abstract Color getColor(Baandeel baan);

	@Override
	public abstract String toString();

}
