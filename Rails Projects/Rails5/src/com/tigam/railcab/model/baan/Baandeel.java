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
import java.util.Observable;

import com.tigam.railcab.model.exception.RailCabException;

/**
 * 
 * Een baandeel is het kleinste deel waaruit een treinbaan is opgebouwd. Het
 * bevat een verwijzing naar het volgende baandeel en kan wel of niet een
 * railcab bevatten.
 * 
 * @author Arjan van der Velde
 * 
 */
public class Baandeel extends Observable {

	/** gepasseerde railcabs, high water mark */
	private static long gepasseerdeCabsHWM;

	/** gepasseerde railcabs, nulpunt */
	private static long gepasseerdeCabsNP;

	/**
	 * Geeft de HWM (high water mark) van het aantal gepasseerde cabs, sinds de
	 * laatste reset van gepasseerdeCabsHWM (resetGepasseerdeCabs()). HWM is
	 * static en geldt dus globaal, voor alle baandelen.
	 * 
	 * @return the gepasseerdeCabsHWM
	 */
	public static long getGepasseerdeCabsHWM() {
		return gepasseerdeCabsHWM - gepasseerdeCabsNP;
	}

	/** volgende en vorige baandeel, we zijn een linked list */
	private Baandeel volgende;

	/** volgende en vorige baandeel, we zijn een linked list */
	private Baandeel vorige;

	/** railcab op dit baandeel. null als geen */
	private RailCab railcab;

	/** fysieke locatie (awt.Point) van dit baandeel */
	private Point locatie;

	/** stoplicht aan of uit. true == aan */
	private boolean stoplicht;

	/** gepasseerde railcabs */
	private long gepasseerdeCabs;

	/**
	 * Constructor, creeer een Baandeel op een zekere locatie
	 * 
	 * @param locatie locatie van het baandeel.
	 */
	public Baandeel(Point locatie) {
		this.locatie = locatie;
	}

	/**
	 * Clear de RailCab op dit baandeel... zelfde als setRailCab(null)
	 */
	public void clearRailCab() {
		try {
			setRailCab(null);
		} catch (RailCabException ignore) {
			// ignore...
			ignore.printStackTrace();
		}
	}

	/**
	 * Geeft het aantal gepasseerde cabs sinds de laatste reset van
	 * gepasseerdeCabsHWM (resetGepasseerdeCabs())
	 * 
	 * @return the gepasseerdeCabs
	 */
	public long getGepasseerdeCabs() {
		long result = gepasseerdeCabs - gepasseerdeCabsNP;
		return result > 0 ? result : 0;
	}

	/**
	 * Geef de locatie (awt.Point) van dit baandeel
	 * 
	 * @return the locatie
	 */
	public Point getLocatie() {
		return locatie;
	}

	/**
	 * Geeft de railcab op dit baandeel (if any, anders null)
	 * 
	 * @return de railcab op dit baandeel
	 */
	public RailCab getRailCab() {
		return railcab;
	}

	/**
	 * Geef het volgende baandeel
	 * 
	 * @return volgende baandeel
	 */
	public Baandeel getVolgende() {
		return volgende;
	}

	/**
	 * Geef het vorige baandeel
	 * 
	 * @return vorige baandeel
	 */
	public Baandeel getVorige() {
		return vorige;
	}

	/**
	 * Staat er op dit baandeel een railcab?
	 * 
	 * @return ja of nee
	 */
	public boolean hasRailCab() {
		return railcab != null;
	}

	/**
	 * Als stoplicht aan is true, anders false
	 * 
	 * @return the stoplicht
	 */
	public boolean isStoplicht() {
		return stoplicht;
	}

	/**
	 * reset gepasseerdeCabs
	 */
	public void resetGepasseerdeCabs() {
		gepasseerdeCabsNP = gepasseerdeCabsHWM;
	}

	/**
	 * Set de railcab op dit baandeel.
	 * 
	 * @param railcab een railcab
	 * @throws RailCabException
	 */
	public void setRailCab(RailCab railcab) throws RailCabException {

		// verhoog gepasseerdeCabs als er een cab geplaatst wordt
		if (railcab != null) {
			if (gepasseerdeCabs < gepasseerdeCabsNP) {
				gepasseerdeCabs = gepasseerdeCabsNP + 1;
			} else {
				gepasseerdeCabs++;
			}
			if (gepasseerdeCabs > gepasseerdeCabsHWM) {
				gepasseerdeCabsHWM = gepasseerdeCabs;
			}
		}

		if (this.railcab != null && railcab != null) {
			throw new RailCabException("Kan RailCab niet plaatsen. Er staat al een RailCab op dit baandeel!");
		}
		this.railcab = railcab;

		// Dit zou de info in cab / rail consistent moeten houden...
		if (this.railcab != null && this.railcab.getHuidigePositie() != this) {
			this.railcab.setHuidigePositie(this);
		}

		// waarschuwen van alle observers dat er een RailCab op ddit baandeel staat.
		setChanged();
		notifyObservers(railcab);
	}

	/**
	 * Zet stoplicht aan
	 */
	public void setStoplicht() {
		if (!stoplicht) {
			stoplicht = true;
			setChanged();
			notifyObservers(railcab);
		}
	}

	/**
	 * Set het volgende baandeel
	 * 
	 * @param volgende volgende baandeel
	 */
	public void setVolgende(Baandeel volgende) {
		this.volgende = volgende;
		if (volgende.getVorige() != this) {
			volgende.setVorige(this); // connect the chain...
		}
	}

	/**
	 * Set het vorige baandeel
	 * 
	 * @param vorige volgende baandeel
	 */
	public void setVorige(Baandeel vorige) {
		this.vorige = vorige;
		if (vorige.getVolgende() != this) {
			vorige.setVolgende(this); // connect the chain...
		}
	}

	/**
	 * Geef een string representatie van een baandeel
	 */
	@Override
	public String toString() {
		return "type: " + this.getClass().getName() + ", hasCab: " + hasRailCab() + ", locatie: " + locatie
				+ ", stoplicht: " + isStoplicht();
	}

	/**
	 * Zet stoplicht uit
	 */
	public void unsetStoplicht() {
		if (stoplicht) {
			stoplicht = false;
			setChanged();
			notifyObservers(railcab);
		}
	}

}
