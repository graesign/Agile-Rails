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

/**
 * 
 */
package com.tigam.railcab.model.navigatie;

import com.tigam.railcab.model.baan.Baandeel;
import com.tigam.railcab.model.baan.StationBaandeel;
import com.tigam.railcab.model.baan.Wissel;
import com.tigam.railcab.model.baan.WisselTerug;

/**
 * 
 * Een klassen voor het pre-renderen van wissel tot eerst volgende wissel
 * 
 * @author Nils Dijk
 * 
 */
public class ParseWissel {
	/**
	 * De lengte van de Volgende kant
	 */
	private int lengte1;

	/**
	 * Het eind van de volgende kant
	 */
	private Wissel eind1;

	/**
	 * De lengte van de andere kant
	 */
	private int lengte2;

	/**
	 * Het einde van de andere kant
	 */
	private Wissel eind2;

	/**
	 * Bereken de lengtes van een gegeven wissel
	 * 
	 * @param begin De wissel
	 */
	public ParseWissel(Wissel begin) {
		synchronized (begin) {
			int l;
			Baandeel b;

			// Tel het aantal baandelen tot de eerst volgende Wissel(Terug)
			// en sla de eerstvolgende Wissel(Terug) op.
			l = 0;
			b = begin.getVolgende();
			while (!(b instanceof Wissel)) {
				if (b instanceof StationBaandeel) {
					l += Navigator.KOSTEN_STATIONBAANDEEL;
				} else {
					l += Navigator.KOSTEN_BAANDEEL;
				}
				b = b.getVolgende();
			}
			if (b instanceof WisselTerug) {
				l += Navigator.KOSTEN_WISSELTERUG;
			} else if (b instanceof Wissel) {
				l += Navigator.KOSTEN_WISSEL;
			}

			lengte1 = l;
			eind1 = (Wissel) b;

			// Als het een wissel is, pak dan ook de andere kant
			if (!(begin instanceof WisselTerug)) {
				l = 0;
				b = begin.getAndere();
				while (!(b instanceof Wissel)) {
					if (b instanceof StationBaandeel) {
						l += Navigator.KOSTEN_STATIONBAANDEEL;
					} else {
						l += Navigator.KOSTEN_BAANDEEL;
					}
					b = b.getVolgende();
				}
				if (b instanceof WisselTerug) {
					l += Navigator.KOSTEN_WISSELTERUG;
				} else if (b instanceof Wissel) {
					l += Navigator.KOSTEN_WISSEL;
				}
				lengte2 = l;
				eind2 = (Wissel) b;

				// Als de wissel niet in zijn standaard staat (false) swap eind1 en eind2
				if (((Wissel) b).getStand()) {
					Wissel tmpWissel;
					int tmlLengte;

					tmlLengte = lengte1;
					lengte1 = lengte2;
					lengte2 = tmlLengte;

					tmpWissel = eind1;
					eind1 = eind2;
					eind2 = tmpWissel;
				}
			}
		}
	}

	/**
	 * Vraag de wissel terug die je tegen komt bij de Volgende kant
	 * 
	 * @return de Wissel
	 */
	public Wissel getEind1() {
		return eind1;
	}

	/**
	 * Vraag de wissel terug die je tegen komt bij de Andere kant
	 * 
	 * @return de Wissel
	 */
	public Wissel getEind2() {
		return eind2;
	}

	/**
	 * De lengte van de Volgende kant
	 * 
	 * @return de lengte
	 */
	public int getLengte1() {
		return lengte1;
	}

	/**
	 * De lengte van de Andere kant
	 * 
	 * @return de lengte
	 */
	public int getLengte2() {
		return lengte2;
	}
}