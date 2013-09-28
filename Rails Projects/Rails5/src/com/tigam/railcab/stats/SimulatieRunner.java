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

import com.tigam.railcab.model.baan.Groep;
import com.tigam.railcab.model.simulatie.Date;

/**
 * Een klasse voor het het herhalen van oude invoer van passagiers.
 * 
 * @author Nils Dijk
 * 
 */
public class SimulatieRunner implements Observer {

	/**
	 * Alle groepen die toegevoegt moeten worden in de nieuwe simulatie
	 */
	private ArrayList<Groep> groepen;

	/**
	 * Onthoud wekje groep er voor het laatst is geweest
	 */
	private int index;

	/**
	 * Maak een nieuwe SimulatieRunner aan.
	 * 
	 * @param data geef aan op welke simulatie data de invoer gebaseert moet
	 *            zijn
	 */
	public SimulatieRunner(SimulatieData data) {
		groepen = data.getGroepen();
		index = 0;
	}

	//@Override
	public void update(Observable arg0, Object obj) {
		Date d = (Date) obj;
		if (d == null) {
			return;
		}

		while (index < groepen.size() && groepen.get(index).getAanmaakDate().isSame(d)) {
			Groep g = groepen.get(index);
			Groep n = new Groep(g.getVertrek(), g.getBestemming(), g.getAantalReizigers());
			g.getVertrek().addGroep(n);
			index++;
		}
	}

}
