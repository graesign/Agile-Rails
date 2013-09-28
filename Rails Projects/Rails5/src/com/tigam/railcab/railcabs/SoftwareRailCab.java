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

package com.tigam.railcab.railcabs;

import com.tigam.railcab.model.baan.AbstractSoftwareRailCab;
import com.tigam.railcab.model.baan.Baandeel;
import com.tigam.railcab.model.baan.RailCab;
import com.tigam.railcab.model.baan.RailCabMissie;
import com.tigam.railcab.model.baan.StationBaandeel;
import com.tigam.railcab.model.exception.RailCabException;

/**
 * Implementatie van een software RailCab.
 */
public class SoftwareRailCab extends AbstractSoftwareRailCab {

	/** teller voor wacht routine */
	private int wait;

	/** hoelang wachten we op een station? */
	private final int WACHT_OP_LOCATIE = 15;

	/** Variabele om missie status in bij te houden */
	private int huidigeMissieStatus;

	/** Variabele om vorige missie status in bij te houden */
	private int vorigeMissieStatus;

	/** Variabele om railcab status in bij te houden */
	private int huidigeRailCabStatus;

	/** Variabele om vorige railcab status in bij te houden */
	private int vorigeRailCabStatus;

	/**
	 * Construct a RailCab op een gegeven locatie.
	 * 
	 * @param locatie het baandeel waar de railcab geplaatst moet worden
	 * @throws RailCabException
	 */
	public SoftwareRailCab(Baandeel locatie) throws RailCabException {
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
	public SoftwareRailCab(Baandeel locatie, int maxReizigers) throws RailCabException {
		super(locatie, maxReizigers);
	}

	@Override
	public boolean actie() throws RailCabException {
		// bewaar vorige status zodat we een overgang kunnen detecteren
		vorigeRailCabStatus = huidigeRailCabStatus;
		huidigeRailCabStatus = status;

		// bepaal actie
		if (huidigeRailCabStatus == RailCab.STATUS_MISSIE_IN_UITVOERING) {
			return doMissieInUitvoering();
		} else if (status == RailCab.STATUS_NIEUWE_MISSIE) {
			return doNieuweMissie();
		} else if (status == RailCab.STATUS_IDLE) {
			return doIdle();
		} else if (status == RailCab.STATUS_WACHT) {
			return doWacht();
		} else {
			return false; // tja...
		}

	}

	/**
	 * Uit te voeren wanneer de RailCab idle is.
	 * 
	 * @return wel of niet verplaatst
	 * @throws RailCabException
	 */
	private boolean doIdle() throws RailCabException {
		// we zijn idle...
		return rijDoorOpStation();
	}

	/**
	 * Uit te voeren tijdens de uitvoering van een missie.
	 * 
	 * @return wel of niet verplaatst
	 * @throws RailCabException
	 */
	private boolean doMissieInUitvoering() throws RailCabException {
		vorigeMissieStatus = huidigeMissieStatus; // bewaar vorige status
		// zodat we een overgang
		// kunnen detecteren
		huidigeMissieStatus = huidigeMissie.getMissieStatus();
		switch (huidigeMissieStatus) {
		case RailCabMissie.MISSIE_ONDERWEG_NAAR_STARTLOCATIE:
			return doOnderwegNaarStartLocatie();
		case RailCabMissie.MISSIE_OP_STARTLOCATIE:
			return doOpStartLocatie();
		case RailCabMissie.MISSIE_REIZIGERS_AFGEHAALD:
			return doOpStartLocatie();
		case RailCabMissie.MISSIE_ONDERWEG_NAAR_EINDLOCATIE:
			return doOnderwegNaarEindLocatie();
		case RailCabMissie.MISSIE_OP_EINDLOCATIE:
			return doOpEindLocatie();
		default:
			return false; // hier komen we als het goed is niet!
		}
	}

	/**
	 * Uit te voeren op het moment dat de cab een nieuwe missie heeft gekregen
	 * 
	 * @return wel of niet verplaatst
	 * @throws RailCabException
	 */
	private boolean doNieuweMissie() throws RailCabException {
		// we hebben een missie...
		return rijDoorOpStation();
	}

	/**
	 * Uit te voeren als railcab onderweg is naar eindlocatie van missie
	 * 
	 * @return wel of niet verplaatst
	 * @throws RailCabException
	 */
	private boolean doOnderwegNaarEindLocatie() throws RailCabException {
		if (getHead() == this) { // we zijn niet gekoppeld, of we zijn de voorste
			if (!isOpLocatie(getBestemming())) { // zijn we NIET op onze bestemming?
				if (verplaats()) {
					setRailCabStatus(RailCab.STATUS_MISSIE_IN_UITVOERING);
					return true;
				} else { // verplaatsen is klaarblijkelijk niet gelukt.
					setRailCabStatus(RailCab.STATUS_WACHT);
					return false;
				}
			} else { // we zijn WEL op onze bestemming
				wait = 0;
				huidigeMissie.setMissieStatus(RailCabMissie.MISSIE_OP_EINDLOCATIE);
				if (!isGekoppeld()) { // we zijn alleen.
					return rijDoorOpStation();
				} else { // we zijn NIET alleen, maar wel de voorste
					ontkoppel(); // ontkoppel van de rest... daar is nu dus een nieuwe head.
					return rijDoorOpStation(); // rij door tot aan het einde...
				}
			}
		} else { // we zijn wel gekoppeld en niet de voorste
			if (isOpLocatie(getBestemming())) { // zijn we er al?
				wait = 0;
				huidigeMissie.setMissieStatus(RailCabMissie.MISSIE_OP_EINDLOCATIE);
			}
			return rijDoorOpStation();
		}
	}

	/**
	 * Uit te voeren als railcab onderweg is naar start locatie van de missie
	 * 
	 * @return wel of niet verplaatst
	 * @throws RailCabException
	 */
	private boolean doOnderwegNaarStartLocatie() throws RailCabException {
		if (!isGekoppeld() || isGekoppeld() && getVorige() == null) { // we zijn niet gekoppeld, of we zijn de voorste
			if (!isOpLocatie(getBestemming())) {
				if (verplaats()) {
					setRailCabStatus(RailCab.STATUS_MISSIE_IN_UITVOERING);
					return true;
				} else {
					setRailCabStatus(RailCab.STATUS_WACHT);
					return false;
				}
			} else {
				wait = 0;
				huidigeMissie.setMissieStatus(RailCabMissie.MISSIE_OP_STARTLOCATIE);
				return rijDoorOpStation();
			}
		} else {
			if (isOpLocatie(getBestemming())) {
				wait = 0;
				huidigeMissie.setMissieStatus(RailCabMissie.MISSIE_OP_STARTLOCATIE);
			}
			return false;
		}
	}

	/**
	 * Uit te voeren als railcab op eindlocatie van de missie is.
	 * 
	 * @return wel of niet verplaatst
	 * @throws RailCabException
	 */
	private boolean doOpEindLocatie() throws RailCabException {
		if (wait < WACHT_OP_LOCATIE) { // wacht ff op locatie...
			if (!rijDoorOpStation()) {
				wait++;
			}
			return false;
		}
		dumpReizigers();
		setMissieStop();
		return rijDoorOpStation();
	}

	/**
	 * Uit te voeren als railcab op startlocatie van de missie is.
	 * 
	 * @return wel of niet verplaatst
	 * @throws RailCabException
	 */
	private boolean doOpStartLocatie() throws RailCabException {
		if (huidigeMissie.getStartLocatie() instanceof StationBaandeel) {
			if (huidigeMissieStatus != RailCabMissie.MISSIE_REIZIGERS_AFGEHAALD) {
				haalReizigersAf(); // geeft 0 reizigers als EINDLOCATIE == null
			}
			if (wait < WACHT_OP_LOCATIE) { // wacht ff op locatie...
				if (!rijDoorOpStation()) {
					wait++;
				}
				// probeer te koppelen
				if (koppelen) {
					probeerTeKoppelen();
				}
				return false;
			}
		}
		if (reizigers.size() > 0) {
			setBestemming(huidigeMissie.getEindLocatie());
			huidigeMissie.setMissieStatus(RailCabMissie.MISSIE_ONDERWEG_NAAR_EINDLOCATIE);
		} else {
			setMissieStop(); // stop missie als we geen reizigers hebben...
		}
		return rijDoorOpStation();
	}

	/**
	 * Uit te voeren in paniek situatie
	 * 
	 * @return wel of niet verplaatst
	 * @throws RailCabException
	 */
	private boolean doWacht() throws RailCabException {
		//		System.out.println("Wacht... (" + this + ", " + veroorzaker + ")");
		if (huidigeMissie != null) {
			return doMissieInUitvoering();
		} else {
			return false;
		}
	}

	/**
	 * @return the huidigeMissieStatus
	 */
	public int getHuidigeMissieStatus() {
		return huidigeMissieStatus;
	}

	/**
	 * @return the huidigeRailCabStatus
	 */
	public int getHuidigeRailCabStatus() {
		return huidigeRailCabStatus;
	}

	/**
	 * @return the vorigeMissieStatus
	 */
	public int getVorigeMissieStatus() {
		return vorigeMissieStatus;
	}

	/**
	 * @return the vorigeRailCabStatus
	 */
	public int getVorigeRailCabStatus() {
		return vorigeRailCabStatus;
	}

	/**
	 * Probeer te koppelen met railcabs op baandelen die grenzen aan de huidige
	 * positie.
	 * 
	 * Voorwaarden voor koppelen zijn:<br> - er moet natuurlijk een cab zijn op
	 * het betreffende baandeel.<br> - de cab moet een missie hebben.<br> - de
	 * cab moet op zijn startlocatie zijn.<br> - de cab moet dezelfde
	 * bestemming hebben als wij.<br> - de cab moet een missie met
	 * eindbestemming hebben.
	 * 
	 * @throws RailCabException
	 */
	private void probeerTeKoppelen() throws RailCabException {
		// een loop van twee...
		for (Baandeel b : new Baandeel[] { huidigePositie.getVolgende(), huidigePositie.getVorige() }) {
			if (b instanceof StationBaandeel && b.hasRailCab()) {
				RailCab r = b.getRailCab();
				if (r.getBestemming() == bestemming && r.getHuidigeMissie() != null
						&& r.getHuidigeMissie().getMissieStatus() == RailCabMissie.MISSIE_OP_STARTLOCATIE
						&& r.getHuidigeMissie().getEindLocatie() != null) {
					koppel(r);
				}
			}
		}
	}

	/**
	 * @param huidigeMissieStatus the huidigeMissieStatus to set
	 */
	public void setHuidigeMissieStatus(int huidigeMissieStatus) {
		this.huidigeMissieStatus = huidigeMissieStatus;
	}

}
