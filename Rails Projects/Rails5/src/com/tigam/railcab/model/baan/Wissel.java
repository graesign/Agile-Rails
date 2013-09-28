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

import java.awt.Point;

import com.tigam.railcab.model.exception.WisselException;

/**
 * Een Wissel baandeel, heeft een "ingang" en twee "uitgangen".
 * {@code getVorige()} wijst naar de enkele ingang en afhankelijk van de
 * toestand van de wissel wijzen {@code getVolgende()} en {@code getAndere()}
 * naar een van de twee uitgangen waarbij {@code getVolgende() != getAndere()}.
 * 
 * state - false = normaal - true = alternatieve richting
 * 
 * rijRichting - false = normale wissel - true = wissel terug
 * 
 */
public class Wissel extends Baandeel {

	/** andere, volgende en vorige zijn uiteinden van dit baandeel */
	private Baandeel andere;

	/** andere, volgende en vorige zijn uiteinden van dit baandeel */
	private Baandeel volgende;

	/** andere, volgende en vorige zijn uiteinden van dit baandeel */
	private Baandeel vorige;

	/** toestands informatie */
	private boolean stand;

	/** rijrichting */
	private boolean rijRichting;

	/**
	 * Creeer een wissel op een zeker locatie, met default rijrichting (normale
	 * wissel)
	 * 
	 * @param locatie locatie in 2d space
	 */
	public Wissel(Point locatie) {
		this(locatie, false); // richting default false
	}

	/**
	 * Creeer een wissel op een zeker locatie, en set de rijrichting (voor
	 * wissel terug)
	 * 
	 * @param locatie locatie in 2d space
	 * @param rijRichting rijRichting, false normale wissel, true wissel terug
	 */
	public Wissel(Point locatie, boolean rijRichting) {
		super(locatie);
		this.rijRichting = rijRichting;
	}

	/**
	 * Geeft de alternatieve volgende.
	 * 
	 * @return alternatieve volgende baandeel.
	 */
	public Baandeel getAndere() {
		if (!stand) {
			return andere;
		} else {
			return volgende;
		}
	}

	/**
	 * Geef de rijrichting voor deze wissel
	 * 
	 * @return the rijRichting
	 */
	public boolean getRijRichting() {
		return rijRichting;
	}

	/**
	 * Geef de stand van deze wissel
	 * 
	 * @return the stand
	 */
	public boolean getStand() {
		return stand;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.Baandeel#getVolgende()
	 */
	@Override
	public Baandeel getVolgende() {
		if (!rijRichting) {
			// ***** "normale" wissel *****
			if (stand) { // alternatieve richting
				return andere;
			} else { // normale richting
				return volgende;
			}
		} else {
			// ***** "wissel-terug" *****
			return vorige;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.Baandeel#getVorige()
	 */
	@Override
	public Baandeel getVorige() {
		if (!rijRichting) {
			// ***** "normale" wissel *****
			return vorige;
		} else {
			// ***** "wissel-terug" *****
			if (stand) { // alternatieve richting
				return andere;
			} else { // normale richting
				return volgende;
			}
		}
	}

	/**
	 * Geeft het Baandeel waarnaar de "vork kant" van de wissel wijst.
	 * 
	 * @return baandeel
	 */
	public Baandeel getWisselKant() {
		if (stand) {
			return andere;
		} else {
			return volgende;
		}
	}

	/**
	 * Set alternatieve volgende baandeel.
	 * 
	 * @param andere alternative volgende baandeel.
	 */
	public void setAndere(Baandeel andere) {
		this.andere = andere;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.Baandeel#setVolgende(model.Baandeel)
	 */
	@Override
	public void setVolgende(Baandeel volgende) {
		if (!rijRichting) {
			// ***** "normale" wissel *****
			if (stand) { // alternatieve richting
				andere = volgende;
			} else { // normale richting
				this.volgende = volgende;
			}
		} else {
			// ***** "wissel-terug" *****
			vorige = volgende;
		}
		if (volgende.getVorige() != this) {
			volgende.setVorige(this); // connect the chain...
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.Baandeel#setVorige(model.Baandeel)
	 */
	@Override
	public void setVorige(Baandeel vorige) {
		if (!rijRichting) {
			// ***** "normale" wissel *****
			this.vorige = vorige;
		} else {
			// ***** "wissel-terug" *****
			if (stand) { // alternatieve richting
				andere = vorige;
			} else { // normale richting
				volgende = vorige;
			}
		}
		if (vorige.getVolgende() != this) {
			vorige.setVolgende(this); // connect the chain...
		}
	}

	/**
	 * Zet de wissel om. Concreet betekent dit dat volgende en andere worden
	 * omgewisseld en de state wordt geinverteerd.
	 * 
	 * @throws WisselException
	 */
	public synchronized void zetOm() throws WisselException { // lock!
		if (hasRailCab()) {
			throw new WisselException("Kan wissel niet omzetten als er een RailCab op staat!");
		}
		stand = !stand;
		// bericht de lieve luisteraars over de state change...
		setChanged();
		this.notifyObservers(this);
	}

}
