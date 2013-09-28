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

import com.tigam.railcab.model.simulatie.Date;

/**
 * Een door een RailCab uit te voeren missie.
 * 
 * @author Arjan van der Velde
 * 
 */
public class RailCabMissie {

	/*
	 * Missie status
	 */

	/** Onderweg naar startlocatie. */
	public static final int MISSIE_ONDERWEG_NAAR_STARTLOCATIE = 0;

	/** Op startlocatie. */
	public static final int MISSIE_OP_STARTLOCATIE = 1;

	/** Onderweg naar eindlocatie */
	public static final int MISSIE_ONDERWEG_NAAR_EINDLOCATIE = 2;

	/** Op eindlocatie. */
	public static final int MISSIE_REIZIGERS_AFGEHAALD = 3;

	/** Op eindlocatie. */
	public static final int MISSIE_OP_EINDLOCATIE = 4;

	/** Uitvoeringsstatus van deze missie */
	private int missieStatus;

	/** Datum van aanmaken van de missie */
	private Date aanmaakDate;

	/** Startlocatie */
	private Baandeel startLocatie;

	/** Eindlocatie */
	private Baandeel eindLocatie;

	/** "Kilometerteller" voor deze missie */
	private long afgelegdeBaandelen;

	/** De op te pikken groepen reizigers */
	private ArrayList<Groep> groepen;

	/**
	 * Constructor maakt een missie aan met een startlocate en eindlocatie.
	 * 
	 * @param startLocatie locatie van vertrek
	 */
	public RailCabMissie(Baandeel startLocatie) {
		this(startLocatie, null, null); // nog geen reizigers...
	}

	/**
	 * Constructor maakt een missie aan met een startlocate en eindlocatie.
	 * 
	 * @param startLocatie locatie van vertrek
	 * @param eindLocatie bestemming
	 */
	public RailCabMissie(Baandeel startLocatie, Baandeel eindLocatie) {
		this(startLocatie, eindLocatie, null); // nog geen reizigers...
	}

	/**
	 * Constructor maakt een missie aan met een startlocate, eindlocatie en een
	 * aantaal reizigers.
	 * 
	 * @param startLocatie locatie van vertrek
	 * @param eindLocatie bestemming
	 * @param groepen mee te nemen groepen reizigers
	 */
	public RailCabMissie(Baandeel startLocatie, Baandeel eindLocatie, ArrayList<Groep> groepen) {
		super();
		this.startLocatie = startLocatie;
		this.eindLocatie = eindLocatie;
		this.groepen = groepen;
		aanmaakDate = new Date(); // aanmaakDate is nu...
	}

	/**
	 * Geef de aanmaak datum van de missie.
	 * 
	 * @return the aanmaakDate
	 */
	public Date getAanmaakDate() {
		return aanmaakDate;
	}

	/**
	 * Geef het aantal afgelegde baandelen voor deze missie
	 * 
	 * @return the afgelegdeBaandelen
	 */
	public long getAfgelegdeBaandelen() {
		return afgelegdeBaandelen;
	}

	/**
	 * Geef de eindlocatie van deze missie (kan null zijn)
	 * 
	 * @return the eindLocatie
	 */
	public Baandeel getEindLocatie() {
		return eindLocatie;
	}

	/**
	 * Geef de groepen waarvoor deze missie bedoeld is. Groepen in missies zijn
	 * indicatief. Het geeft een railcab aanwijzingen over de groep(en) die
	 * opgepakt moeten worden op de startlocatie. Echter, die geldt totdat de
	 * railcab vol is. vol == vol.
	 * 
	 * @return the groepen
	 */
	public ArrayList<Groep> getGroepen() {
		return groepen;
	}

	/**
	 * Notify observers bij statusovergangen
	 * 
	 * @return missie status (RailCabMissie.MISSIE_.*)
	 */
	public int getMissieStatus() {
		return missieStatus;
	}

	/**
	 * Geef de startlocatie van deze missie.
	 * 
	 * @return the startLocatie
	 */
	public Baandeel getStartLocatie() {
		return startLocatie;
	}

	/**
	 * Set de eindlocatie voor deze missie.
	 * 
	 * @param eindLocatie the eindLocatie to set
	 */
	public void setEindLocatie(Baandeel eindLocatie) {
		this.eindLocatie = eindLocatie;
	}

	/**
	 * Set de lijst van groepen voor deze missie. Groepen in missies zijn
	 * indicatief. Het geeft een railcab aanwijzingen over de groep(en) die
	 * opgepakt moeten worden op de startlocatie. Echter, die geldt totdat de
	 * railcab vol is. vol == vol.
	 * 
	 * @param groepen the groepen to set
	 */
	public void setGroepen(ArrayList<Groep> groepen) {
		this.groepen = groepen;
	}

	/**
	 * Notify observers bij statusovergangen
	 * 
	 * @param missieStatus
	 */
	public void setMissieStatus(int missieStatus) {
		this.missieStatus = missieStatus;
	}

	/**
	 * Set de startlocatie van deze missie.
	 * 
	 * @param startLocatie the startLocatie to set
	 */
	public void setStartLocatie(Baandeel startLocatie) {
		this.startLocatie = startLocatie;
	}

	/**
	 * Increase afgelegde baandelen
	 */
	public void verhoogAfgelegdeBaandelen() {
		afgelegdeBaandelen++;
	}

}
