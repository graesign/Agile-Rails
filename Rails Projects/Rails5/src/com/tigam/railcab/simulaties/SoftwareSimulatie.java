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

package com.tigam.railcab.simulaties;

import com.tigam.railcab.model.baan.BaanHeader;
import com.tigam.railcab.model.baan.Baandeel;
import com.tigam.railcab.model.baan.RailCab;
import com.tigam.railcab.model.baan.Wissel;
import com.tigam.railcab.model.exception.RailCabException;
import com.tigam.railcab.model.simulatie.Simulatie;
import com.tigam.railcab.railcabs.SoftwareRailCab;

/**
 * "Simulatie gedrag". Zowel software als hardware simulaties moeten deze
 * interface implementeren. SoftwareSimulatie is voor software....
 * 
 * @author Arjan van der Velde
 * 
 */
public class SoftwareSimulatie extends Simulatie {

	/**
	 * Constructor, creeer een simulatie ... (een baan voor nu :)
	 * 
	 * @param baanHeader Het baan header object voor refs naar de baan
	 * @throws RailCabException als er iets fout gaat...
	 */
	public SoftwareSimulatie(BaanHeader baanHeader) throws RailCabException {
		super(baanHeader); // superklasse voert o.a. build() en maakRailCabs() uit.
		setDelay(50);
	}

	/**
	 * Zet 1 RailCab op de baan
	 * 
	 * @param locatie locatie waar de Cab moet komen
	 * @param maxReizigers maximale aantal reizigers voor de cab
	 * @throws RailCabException als er iets fout gaat
	 * @return eerste cab
	 */
	protected RailCab addRailCab(Baandeel locatie, int maxReizigers) throws RailCabException {
		return addRailCab(locatie, 1, true, maxReizigers);
	}

	/**
	 * Zet een RailCab op het spoor.
	 * 
	 * @param begin Baandeel waar te beginnen
	 * @param lengte Aantal gekoppelde cabs (moet >= 1 zijn)
	 * @param richting groei richting als lengte > 1
	 * @param maxReizigers het maximale aantal reizigers voor de cab
	 * @throws RailCabException als er iets fout gaat
	 * @return de eerste (voorste) cab
	 */
	protected RailCab addRailCab(Baandeel begin, int lengte, boolean richting, int maxReizigers)
			throws RailCabException {
		RailCab eersteCab;
		// Check parms
		if (lengte < 1) {
			throw new RailCabException("RailCab lengte moet minimaal 1 zijn!");
		}
		// Eerste cab
		Baandeel locatie = begin;
		RailCab railcab = new SoftwareRailCab(locatie, maxReizigers), nieuweCab = null;
		railcabs.add(railcab);
		eersteCab = railcab;
		// Als lengte > 1 koppelen...
		if (richting) {
			for (int i = 0; i < lengte - 1; i++) {
				// Wissel moet goed staan!
				if (locatie.getVorige() instanceof Wissel && locatie.getVorige().getVolgende() != locatie) {
					((Wissel) locatie.getVorige()).zetOm();
				}
				locatie = locatie.getVorige();
				nieuweCab = new SoftwareRailCab(locatie, maxReizigers);
				railcab = railcab.koppel(nieuweCab);
				railcabs.add(railcab);
			}
		} else {
			for (int i = 0; i < lengte - 1; i++) {
				// Wissel moet goed staan!
				if (locatie.getVolgende() instanceof Wissel && locatie.getVolgende().getVorige() != locatie) {
					((Wissel) locatie.getVolgende()).zetOm();
				}
				locatie = locatie.getVolgende();
				nieuweCab = new SoftwareRailCab(locatie, maxReizigers);
				railcab = railcab.koppel(nieuweCab);
				railcabs.add(railcab);
				eersteCab = railcab; // hou bij wat de voorste cab is
			}
		}
		return eersteCab;
	}

	@Override
	protected void build() {
		if (baanHeader == null) {
			System.out.println("Ongeldig baanbestand");
		} else {
			head = baanHeader.getBaan();
			stations = baanHeader.getStations();
		}
	}

	/**
	 * Maak wat railcabs met missies....
	 */
	@Override
	protected void maakRailCabs(int maxReizigers) throws RailCabException {
		Baandeel locatie = stations.get(0).getEersteBaandeel(); // begin op een station...
		for (int i = 0; i < aantalRailCabs; i++) { // losse cabs zonder missie
			addRailCab(locatie, maxReizigers);
			locatie = locatie.getVolgende();
		}
	}

	public void run() {

		// oneindig lang...
		while (true) {

			// Als we niet lopen...
			while (simulatieStatus != Simulatie.STATUS_RUNNING) {
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					System.out.println(e);
				}
			}

			// als we wel lopen...
			try {
				moveRailCabs(); // schop alle railcabs...
			} catch (RailCabException e) {
				e.printStackTrace();
				System.exit(1);
			}

			delay(); // wacht en verzet de tijd!

		}
	}

}
