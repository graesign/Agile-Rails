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

import com.tigam.railcab.model.exception.RailCabException;
import com.tigam.railcab.model.exception.WisselException;

/**
 * Implementatie van alle standaard onderdelen van een software RailCab. Alle
 * algemene code, nodig voor een RailCab die alleen in software bestaat. Een
 * "software RailCab" extent deze klasse.
 * 
 * @author Arjan van der Velde
 * 
 */
public abstract class AbstractSoftwareRailCab extends RailCab {

	/** minimum afstand tot andere railcabs */
	protected static final int AFSTAND = 1;

	/** "uitkijk afstand" op wissels */
	protected static final int WISSEL_AFSTAND = 3;

	/**
	 * Construct a RailCab op een gegeven locatie.
	 * 
	 * @param locatie het baandeel waar de railcab geplaatst moet worden
	 * @throws RailCabException
	 */
	public AbstractSoftwareRailCab(Baandeel locatie) throws RailCabException {
		super(locatie);
	}

	/**
	 * Construct a RailCab op een gegeven locatie met een gegeven max aantal
	 * reizigers.
	 * 
	 * @param locatie het baandeel waar de railcab geplaatst moet worden
	 * @param maxReizigers het maximale aantal reizigers dat deze cab mag
	 *            bevatten
	 * @throws RailCabException
	 */
	public AbstractSoftwareRailCab(Baandeel locatie, int maxReizigers) throws RailCabException {
		super(locatie, maxReizigers);
	}

	/**
	 * Kijk of er een RailCab is op of in de buurt van een baandeel
	 * 
	 * @param begin baandeel waar te beginnen met checken
	 * @param hoever hoeveel baandelen checken?
	 * @param richting in welke richting? (true = volgende, false = vorige)
	 * @param stopLichtCheck houd wel of geen rekening met stoplichten
	 * @return wel of geen railcab, true of false
	 */
	protected boolean kijkUit(Baandeel begin, int hoever, boolean richting, boolean stopLichtCheck) {
		Baandeel baandeel = begin;
		for (int i = 0; i < hoever - 1; i++) { // "hoever - 1" aangezien "begin" de eerste is
			if (stopLichtCheck && baandeel.isStoplicht()) {
				return false; // stoplicht.
			}
			if (baandeel.hasRailCab()) {
				veroorzaker = baandeel.getRailCab().getHead();
				return true;
			}
			baandeel = richting ? baandeel.getVolgende() : baandeel.getVorige();
		}
		return false;
	}

	/**
	 * Zorg ervoor dat indien we op een station zijn we doorrijden tot aan het
	 * einde van het station, of totdat we niet verder kunnen natuurlijk.
	 * 
	 * @return wel of niet verplaatst
	 * @throws RailCabException
	 */
	protected boolean rijDoorOpStation() throws RailCabException {
		// rij door tot einde station.
		if (huidigePositie.getVolgende() instanceof StationBaandeel) {
			return verplaats();
		} else {
			return false;
		}
	}

	/**
	 * Trek een RailCab, aan te roepen vanuit een andere RailCab, waarmee deze
	 * Cab gekoppeld is.
	 * 
	 * @param nieuwePositie
	 * @throws RailCabException
	 */
	@Override
	public void trekMij(RailCab railcab, Baandeel nieuwePositie) throws RailCabException {
		if (getVorige() == railcab) {
			Baandeel huidigePositie = getHuidigePositie(); // save pos
			setHuidigePositie(nieuwePositie); // set nieuwe pos
			huidigePositie.clearRailCab(); // en clear oude pos
			verhoogAfgelegdeBaandelen(); // inc. km teller
			if (getVolgende() != null) {
				getVolgende().trekMij(this, huidigePositie);
			}
		} else {
			throw new RailCabException("Alleen gekoppelde RailCab kan deze Cab trekken!");
		}
	}

	/**
	 * Ga naar de volgende positie.
	 * 
	 * Deze methode is een soort van "fall-thru" procedure. Op het moment dat
	 * aan een zeker voorwaarde wel of niet voldaan wordt wordt de procedure
	 * onderbroken danwel voortgezet.
	 * 
	 * @return verplaatst of niet
	 * @throws RailCabException
	 */
	@Override
	protected boolean verplaats() throws RailCabException {
		Baandeel huidigePos = getHuidigePositie();
		Baandeel volgendePos = huidigePos.getVolgende();
		veroorzaker = null; // reset veroorzaker...

		// stoplicht...
		if (huidigePos.isStoplicht()) {
			return false;
		}

		// volgendePos is geen baandeel
		if (!(volgendePos instanceof Baandeel)) {
			return false;
		}

		// volgende bezet
		if (volgendePos.hasRailCab()) {
			veroorzaker = volgendePos.getRailCab().getHead();
			return false;
		}

		// we zijn gekoppeld en niet iedereen is klaar...
		RailCab r = this;
		if (r.isGekoppeld()) {
			while (r.getVolgende() != null) {
				if (r.getVolgende().getHuidigeMissie() == null) {
					break; // zoja, break...
				}
				if (r.getVolgende().getHuidigeMissie().getMissieStatus() == RailCabMissie.MISSIE_OP_STARTLOCATIE) {
					// de gekoppelde cab is wel op start locatie, maar heeft nog geen reizigers opgepikt.
					return false;
				} else {
					r = r.getVolgende();
				}
			}
		}

		// volgende pos is een wissel
		if (volgendePos instanceof Wissel) { // wissel
			if (volgendePos instanceof WisselTerug) { // wissel terug
				// Voorrangsregel
				WisselTerug wissel = (WisselTerug) volgendePos;
				// Simpel "rechts heeft voorrang" :)
				if (wissel.getStand()) { // stand is "links"
					if (wissel.getVorige().getRailCab() == this) {
						// we komen van "links"
						if (kijkUit(wissel.getAndere(), WISSEL_AFSTAND, false, true)) {
							return false;
						}
					}
				} else { // stand is "rechts"
					if (wissel.getVorige().getRailCab() != this) {
						// we komen van "links"
						if (kijkUit(wissel.getVorige(), WISSEL_AFSTAND, false, true)) {
							return false;
						}
					}
				}
				if (wissel.getVorige() != huidigePos) {
					wissel.zetOm();
				}
			} else { // normale wissel
				// Gebruik onze kaart
				if (!zetWisselOm((Wissel) volgendePos)) {
					return false; // kon wissel niet omzetten, wacht...
				}
			}
		}

		// Afstand houden...
		int afstand = volgendePos instanceof StationBaandeel ? 0 : AFSTAND;
		if (!kijkUit(volgendePos, afstand + 2, true, false)) {
			// Als we hier aankomen kunnen we moven....
			setHuidigePositie(volgendePos);
			huidigePos.clearRailCab();
			verhoogAfgelegdeBaandelen();
			if (getVolgende() != null) { // we zijn gekoppeld. Trek de rest (de volgende)
				try {
					getVolgende().trekMij(this, huidigePos);
				} catch (RailCabException e) {
					e.printStackTrace();
				}
			}
			return true;
		} else {

			return false;
		}

	}

	/**
	 * Zet een wissel om, gebaseerd op de route kaart wisselMap. Geeft true als
	 * de wissel is omgezet en false als het niet kan omdat er een andere cab op
	 * staat. Als de wissel niet bekend is op de routekaart, wordt een default
	 * stand (stand=false) ingesteld.
	 * 
	 * @param wissel de om te zetten wissel
	 * @return gelukt of niet
	 * @throws WisselException als omzetten een exception throwt
	 */
	protected boolean zetWisselOm(Wissel wissel) throws WisselException {
		Baandeel bestemming = getBestemming();
		// Werk eventueel incomplete route bij...
		if (bestemming != null && !wisselMap.containsKey(wissel)) {
			setBestemming(bestemming);
		}
		// Zet wissel in stand zoals op kaart vermeld
		if (wisselMap.containsKey(wissel)) { // en hij staaaaat.... wel op de kaart!
			synchronized (wissel) { // lock!
				if (wissel.getStand() != wisselMap.get(wissel).booleanValue() ^ gaVerkeerd) {
					if (!wissel.hasRailCab()) { // staat er geen cab op de wissel?
						wissel.zetOm();
					} else {
						veroorzaker = wissel.getRailCab().getHead();
						return false; // wacht... we kunnen de wissel nu niet omzetten
					}
				}
			}
		} else { // Wissel staat niet op de kaart... doe default (stand=false)
			synchronized (wissel) { // lock!
				if (wissel.getStand() != false) {
					if (!wissel.hasRailCab()) { // staat er geen cab op de wissel?
						wissel.zetOm();
					} else {
						veroorzaker = wissel.getRailCab().getHead();
						return false; // wacht... we kunnen de wissel nu niet omzetten
					}
				}
			}
		}
		gaVerkeerd = false;
		return true; // gelukt.
	}

}
