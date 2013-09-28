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

package com.tigam.railcab.util;

import java.awt.Point;

import com.tigam.railcab.model.baan.Baandeel;
import com.tigam.railcab.model.baan.StationBaandeel;
import com.tigam.railcab.model.baan.Wissel;
import com.tigam.railcab.model.baan.WisselTerug;

/**
 * 
 * Een object als hulpmiddel bij het laden van de baan
 * 
 * @author Nils Dijk
 * 
 */

public class RailsConstructor {
	/**
	 * het nummer van het nieuwe baandeel
	 */
	private int id;

	/**
	 * Het nieuwe baandeel
	 */
	private Baandeel b;

	/**
	 * Het nummer van het volgende baandeel
	 */
	private int volgende;

	/**
	 * Het nummer van het andere baandeel in het geval van een wissel
	 */
	private int andere;

	/**
	 * Constructor, Maakt een nieuw Baandeel aan de hand van een stuk tekst wat
	 * uit het baanbestand komt
	 * 
	 * @param construct De constructie tekst uit het baanbestand
	 */
	public RailsConstructor(String construct) {
		String[] params = construct.split(" ");
		id = (new Integer(params[0])).intValue();

		Point p = new Point((new Integer(params[2])).intValue(), (new Integer(params[3])).intValue());

		if (params[1].equals("B")) { // maak een Baandeel
			b = new Baandeel(p);
			volgende = (new Integer(params[4])).intValue();
		} else if (params[1].equals("W")) { // maak een Wissel
			b = new Wissel(p);
			volgende = (new Integer(params[4])).intValue();
			andere = (new Integer(params[5])).intValue();
		} else if (params[1].equals("T")) { // maak een Wissel terug
			b = new WisselTerug(p);
			volgende = (new Integer(params[4])).intValue();
		} else if (params[1].equals("S")) { // maak een StationBaandeel
			b = new StationBaandeel(p);
			volgende = (new Integer(params[4])).intValue();
		}
	}

	/**
	 * Vraag in het geval van een wissel het id van het andere baandeel op
	 * 
	 * @return het id van het andere baandeel
	 */
	public int getAndere() {
		return andere;
	}

	/**
	 * Vraag het gemaakte baandeel op
	 * 
	 * @return het gemaakt baandeel
	 */
	public Baandeel getBaandeel() {
		return b;
	}

	/**
	 * Vraag het id van dit baandeel op zoals dat was opgegeven in het baan
	 * bestand
	 * 
	 * @return het id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Vraag om het id van het volgende stuk baandeel
	 * 
	 * @return het id van het volgende baandeel
	 */
	public int getVolgende() {
		return volgende;
	}
}
