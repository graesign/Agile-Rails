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

package com.tigam.railcab.model.baan;

import java.util.ArrayList;

/**
 * 
 * Een geladen baan in het geheugen
 * 
 * @author Nils Dijk
 * 
 */
public class BaanHeader {

	/**
	 * Naam van de baan
	 */
	private String naam;

	/**
	 * Referentie naar een Baandeel uit de baan.
	 */
	private Baandeel baan;

	/**
	 * Een lijst met alle stations in de baan
	 */
	private ArrayList<Station> stations;

	/** Alle wissels */
	private ArrayList<Wissel> wissels;

	/** Alle baandelen */
	private ArrayList<Baandeel> baandelen;

	//TODO: methode maken om zelf de stations uit te zoeken is denk ik iets mooier
	/**
	 * Constructor, voeg alles van de geladen baan toe aan een nieuw Object
	 * 
	 * @param naam De naam van de baan
	 * @param baan De referentie naar de baan
	 * @param baandelen Alle baandelen in de baan
	 * @param wissels Alle wissels in de baan
	 * @param stations Een lijst met stations
	 */
	public BaanHeader(String naam, Baandeel baan, ArrayList<Baandeel> baandelen, ArrayList<Wissel> wissels,
			ArrayList<Station> stations) {
		this.naam = naam;
		this.baan = baan;
		this.baandelen = baandelen;
		this.wissels = wissels;
		this.stations = stations;
	}

	/**
	 * Vraag de referentie naar de baan op
	 * 
	 * @return Een referentie naar de baan
	 */
	public Baandeel getBaan() {
		return baan;
	}

	/**
	 * Geef de lijst van baandelen
	 * 
	 * @return the baandelen
	 */
	public ArrayList<Baandeel> getBaandelen() {
		return baandelen;
	}

	/**
	 * Vraag de naam van de baan op
	 * 
	 * @return De naam van de baan
	 */
	public String getNaam() {
		return naam;
	}

	/**
	 * Vraag de lijst met alle stations op
	 * 
	 * @return Lijst met stations
	 */
	public ArrayList<Station> getStations() {
		return stations;
	}

	/**
	 * Geef de lijst van wissels
	 * 
	 * @return the wissels
	 */
	public ArrayList<Wissel> getWissels() {
		return wissels;
	}

	/**
	 * Reset de baan
	 */
	public void reset() {
		getBaan().resetGepasseerdeCabs();
		for (final Wissel w : wissels) {
			synchronized (w) {
				if (w.getStand()) {
					try {
						if (!w.hasRailCab()) {
							w.zetOm();
						}
					} catch (final Exception ignore) {
						ignore.printStackTrace();
					}
				}
			}
		}
	}

}
