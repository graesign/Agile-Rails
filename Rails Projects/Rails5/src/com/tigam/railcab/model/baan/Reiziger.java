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

import com.tigam.railcab.model.simulatie.Date;

/**
 * Een reiziger reist (in groepen van 1 of meer) met een RailCab van A naar B.
 * 
 * @author Arjan van der Velde
 * 
 */
public class Reiziger {

	/** Datum van vertrek (d.w.z. datum van aanmaak van de reiziger) */
	private Date vertrekDate;

	/** Aankomst datum */
	private Date aankomstDate;

	/** De RailCab waarin deze reiziger zich bevindt */
	private RailCab railcab;

	/** De groep waarin deze reiziger reist */
	private Groep groep;

	/** afgelegde baandelen voor deze reiziger */
	private long afgelegdeBaandelen;

	/**
	 * Constructor, maakt een reiziger aan, met opgave van de groep.
	 * 
	 * @param groep groep van de reiziger
	 */
	public Reiziger(Groep groep) {
		this.groep = groep;
		vertrekDate = new Date();
	}

	/**
	 * Geef de aankomstdatum van de reiziger.
	 * 
	 * @return aankomstdatum / tijd
	 */
	public Date getAankomstDate() {
		return aankomstDate;
	}

	/**
	 * Geef het aantal baandelen dat deze reiziger heeft afgelegd.
	 * 
	 * @return the afgelegdeBaandelen
	 */
	public long getAfgelegdeBaandelen() {
		return afgelegdeBaandelen;
	}

	/**
	 * Geef de groep waarin deze reiziger reist.
	 * 
	 * @return de groep van deze reiziger.
	 */
	public Groep getGroep() {
		return groep;
	}

	/**
	 * Geef de RailCab waarin deze reiziger zich bevindt.
	 * 
	 * @return de RailCab waarin deze reiziger zich bevindt.
	 */
	public RailCab getRailCab() {
		return railcab;
	}

	/**
	 * Geef de vertrekdatum van deze reiziger
	 * 
	 * @return vertrekdatum
	 */
	public Date getVertrekDate() {
		return vertrekDate;
	}

	/**
	 * Verhoog afgelegdeBaandelen
	 */
	public void incrementAfgelegdeBaandelen() {
		afgelegdeBaandelen++;
	}

	/**
	 * Geef aan of de reiziger al op zijn / haar bestemming is aangekomen
	 * 
	 * @return ja of nee
	 */
	public boolean isAangekomen() {
		return aankomstDate != null;
	}

	/**
	 * Set de aankomstdatum van deze reiziger met de huidige datum / tijd.
	 */
	public void setAankomstDate() {
		aankomstDate = new Date();
	}

	/**
	 * Set de RailCab waarin de reiziger zich bevindt.
	 * 
	 * @param railcab de RailCab
	 */
	public void setRailCab(RailCab railcab) {
		this.railcab = railcab;
	}

}
