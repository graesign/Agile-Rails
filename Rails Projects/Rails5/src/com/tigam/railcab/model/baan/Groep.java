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

import com.tigam.railcab.model.exception.GroepAlreadyEmptyException;
import com.tigam.railcab.model.simulatie.Date;

/**
 * Een groep van reizigers. Een groep houdt naast de reizigers zelf een aantal
 * statistieken bij. Reizigers worden daadwerkelijk aangemaakt op het moment dat
 * getReizigers() wordt aangeroepen.
 * 
 * @author Arjan van der Velde
 * 
 */
public class Groep implements Comparable<Groep> {

	/** Datum van aanmaak van de groep */
	private Date aanmaakDate;

	/** Aantal reizigers in de groep */
	private int aantalWachtenden;

	/** Interne lijst van inmiddels aangemaakte reizigers. */
	private ArrayList<Reiziger> reizigers;

	/** vertrekpunt */
	private Station vertrek;

	/** punt van bestemming */
	private Station bestemming;

	/** delete flag... */
	private boolean deleted;

	/**
	 * Constructor, maakt een groep aan. Alle reizigers in een groep hebben
	 * dezelfde vertrek en bestemming stations.
	 * 
	 * @param vertrek vertrek stayion
	 * @param bestemming bestemmings station
	 * @param aantalReizigers het aantal reizgers in deze groep
	 */
	public Groep(Station vertrek, Station bestemming, int aantalReizigers) {
		aantalWachtenden = aantalReizigers;
		reizigers = new ArrayList<Reiziger>();
		aanmaakDate = new Date();
		this.bestemming = bestemming;
		this.vertrek = vertrek;
	}

	/**
	 * Om een groep netjes te veranderen aan het formaat dat nu vervoert is.
	 * Voor als de simulatie wordt afgebroken.
	 */
	public void clear() {
		if (reizigers.size() != 0) {
			aantalWachtenden = 0;
		}
	}

	/**
	 * Hoe moeten groepen onderling vergeleken worden met elkaar
	 * 
	 * @param groepToCompare groep om mee te vergelijken
	 * @return smaller, bigger, equal....
	 */
	public int compareTo(Groep groepToCompare) {
		return aanmaakDate.compareTo(groepToCompare.getAanmaakDate());
	}

	/**
	 * Voor het vergelijken van twee groepen, of ze het zelfde zijn qua: -
	 * moment van aanmaken - vertrek station - aankomst station - en het aantal
	 * reizigers
	 * 
	 * @param obj Groep waarmee deze groep vergeleken moet worden.
	 * @return false - De groepen zijn niet gelijk, true - de groepen zijn
	 *         gelijk
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Groep)) {
			return false; // obj is geen groep!
		}
		Groep g = (Groep) obj;
		if (g == null) {
			return false; // obj is null
		}
		if (!g.getAanmaakDate().equals(getAanmaakDate())) {
			return false; // aanmaak date niet gelijk
		}
		if (g.getVertrek() != getVertrek()) {
			return false; // vertrekpunt niet gelijk
		}
		if (g.getBestemming() != getBestemming()) {
			return false; // bestemming niet gelijk
		}
		if (g.getAantalReizigers() != getAantalReizigers()) {
			return false; // aantal reizigers niet gelijk
		}
		return true; // we hebben een match!
	}

	/**
	 * Geef de datum van aanmaken van deze groep
	 * 
	 * @return de datum van aanmaak
	 */
	public Date getAanmaakDate() {
		return aanmaakDate;
	}

	/**
	 * Geef het totaal aantal reizigers in deze groep, d.w.z. het aantal
	 * reiziger dat initieel in deze groep zat.
	 * 
	 * @return het aantal reizigers, de groepgrootte
	 */
	public int getAantalReizigers() {
		synchronized (reizigers) {
			return aantalWachtenden + reizigers.size();
		}
	}

	/**
	 * Geef het aantal wachtenden, d.w.z. het aantal reizigers in deze groep dat
	 * nog niet uit de groep is gehaald.
	 * 
	 * @return het aantal wachtenden
	 */
	public int getAantalWachtenden() {
		return aantalWachtenden;
	}

	/**
	 * Get afgehaalde reizigers...
	 * 
	 * @return een deepcopy van de lijst van reizigers
	 */
	public ArrayList<Reiziger> getAfgehaaldeReizigers() {
		ArrayList<Reiziger> deepCopy = new ArrayList<Reiziger>();
		;
		synchronized (reizigers) {
			deepCopy.addAll(reizigers);
		}
		return deepCopy;
	}

	/**
	 * Geef de bestemming van de groep reizigers
	 * 
	 * @return het bestemmingsstation
	 */
	public Station getBestemming() {
		return bestemming;
	}

	/**
	 * Haal een gegeven aantal reizigers uit de groep. Deze methode maakt, naast
	 * dat het een gegeven aantal reizigers teruggeeft, de reizigers
	 * daadwerkelijk aan in "reizigers".
	 * 
	 * @param aantalReizigers het aantal reizigers
	 * @return een lijst van reizigers
	 * @throws GroepAlreadyEmptyException als er meer uit de groep gehaald wordt
	 *             dan erin zit
	 */
	public ArrayList<Reiziger> getReizigers(int aantalReizigers) throws GroepAlreadyEmptyException {

		// Kunnen we er zoveel leveren?
		if (aantalWachtenden - aantalReizigers < 0) { // nee
			aantalReizigers = aantalWachtenden;
			if (aantalWachtenden == 0) {
				throw new GroepAlreadyEmptyException("Kan geen reizigers uit een lege groep halen!");
			}
		} // ja

		ArrayList<Reiziger> r = new ArrayList<Reiziger>();
		Reiziger t;

		// Verminder het aantal "wachtenden"...
		aantalWachtenden -= aantalReizigers;

		// Creeer de reizigers in kwestie...
		synchronized (reizigers) {
			for (int i = 0; i < aantalReizigers; i++) {
				t = new Reiziger(this);
				reizigers.add(t);
				r.add(t);
			}
		}
		return r; // de lijst van opgevraagde reizigers...
	}

	/**
	 * Geef het vertek station van de groep reizigers
	 * 
	 * @return het vertek station
	 */
	public Station getVertrek() {
		return vertrek;
	}

	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * markeer groep als zijnde verwijderd
	 */
	public void setDeleted() {
		deleted = true;
	}

	/**
	 * Voor gebruik in de GUI
	 */
	@Override
	public String toString() {
		return "Bestemming: " + bestemming.getNaam() + " Aantal: " + getAantalReizigers() + "(" + getAantalWachtenden()
				+ ")";
	}

}
