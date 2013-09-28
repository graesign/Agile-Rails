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
import java.util.HashMap;
import java.util.Observable;

import com.tigam.railcab.model.exception.RailCabException;
import com.tigam.railcab.model.exception.RailCabFullException;
import com.tigam.railcab.model.navigatie.Navigator;

/**
 * Railcab, een "treinwagonnetje" dat over baandelen rijdt.
 * 
 * @author Arjan van der Velde
 * 
 */
public abstract class RailCab extends Observable {

	/**
	 * Default maximaal aantal reizigers per RailCab. Deze waarde wordt gebruikt
	 * als geen aternatief wordt opgegeven via de constructor.
	 */
	public static final int DEFAULT_MAX_REIZIGERS = 4; // voor nu eerst maar hier

	/*
	 * Railcab status
	 */

	/** RailCab is idle */
	public static final int STATUS_IDLE = 0;

	/** RailCab heeft een nieuwe missie */
	public static final int STATUS_NIEUWE_MISSIE = 1;

	/** RailCab haalt reizigers af */
	public static final int STATUS_OPHALEN_REIZIGERS = 4;

	/** RailCab is bezig met uitvoeren van een missie */
	public static final int STATUS_MISSIE_IN_UITVOERING = 2;

	/** RailCab is in paniek! */
	public static final int STATUS_WACHT = 3;

	/*
	 * Richting
	 */

	/** vooruit */
	public static final boolean RICHTING_VOORUIT = true;

	/** achteruit */
	public static final boolean RICHTING_ACHTERUIT = false;

	/** RailCab status */
	protected int status;

	/** koppelen, ja of nee */
	protected boolean koppelen;

	/**
	 * Volgende en vorige vormen de linked list van RailCabs in het geval van
	 * gekoppelde RailCabs.
	 */
	protected RailCab volgende; // we zijn een doubly linked list

	/**
	 * Volgende en vorige vormen de linked list van RailCabs in het geval van
	 * gekoppelde RailCabs.
	 */
	protected RailCab vorige; // we zijn een doubly linked list

	/** huidige positie van de railcab */
	protected Baandeel huidigePositie;

	/** Reizigers in de RailCab. Dit zijn er maximaal maxReizigers */
	protected ArrayList<Reiziger> reizigers; // reizigers

	/** eerst volgende bestemming van de RailCab */
	protected Baandeel bestemming;

	/** het aantal door deze railcab afgelegde baandelen */
	protected long afgelegdeBaandelen; // kilometerteller

	/** History van alle missies door deze RailCab uitgevoerd / ontvangen */
	protected ArrayList<RailCabMissie> afgehandeldeMissies;

	/** Huidige missie van deze RailCab */
	protected RailCabMissie huidigeMissie;

	/**
	 * Limiet op het aantal reizigers. Wordt geset in de constructor, met een
	 * meegegeven waarde of anders DEFAULT_MAX_REIZIGERS.
	 */
	protected int maxReizigers;

	/**
	 * Route kaart. Deze wordt geraadpleegd op het momental dat de railcab een
	 * wissel nadert.
	 */
	protected HashMap<Wissel, Boolean> wisselMap;

	/** Veroorzaker in wacht situatie.... */
	protected RailCab veroorzaker;

	/** Expres verkeerd rijden bij eerst volgende wissel */
	protected boolean gaVerkeerd;

	/**
	 * Constructor. Creeer een RailCab met default maxReizigers van
	 * DEFAULT_MAX_REIZIGERS.
	 * 
	 * @param locatie locatie waar de railcab moet komen
	 * 
	 * @throws RailCabException door gegeven vanuit lager gelegen code
	 */
	public RailCab(Baandeel locatie) throws RailCabException {
		this(locatie, DEFAULT_MAX_REIZIGERS);
	}

	/**
	 * Constructor. Creeer een RailCab met default maxReizigers van
	 * DEFAULT_MAX_REIZIGERS, op een zekere locatie
	 * 
	 * @param locatie locatie waarop de cab gezet moet worden
	 * @param maxReizigers maximale aantal reizigers voor de cab
	 * @throws RailCabException voor als er iets fout gaat
	 */
	public RailCab(Baandeel locatie, int maxReizigers) throws RailCabException {
		setHuidigePositie(locatie);
		reizigers = new ArrayList<Reiziger>();
		afgehandeldeMissies = new ArrayList<RailCabMissie>();
		this.maxReizigers = maxReizigers;
	}

	/**
	 * Beslis wat de volgende actie van deze RailCab zal zijn en zorg ervoor dat
	 * deze wordt uitgevoerd.
	 * 
	 * @return wel of niet bewogen
	 * @throws RailCabException
	 */
	public abstract boolean actie() throws RailCabException;

	/**
	 * Voeg een reiziger toe aan de railcab.
	 * 
	 * @param reiziger de toe te voegen reiziger.
	 * @throws RailCabFullException In case there are too many reizigers...
	 */
	public void addReiziger(Reiziger reiziger) throws RailCabFullException {
		if (reizigers.size() < maxReizigers) {
			reizigers.add(reiziger);
			reiziger.setRailCab(this); // Set railcab voor elke reiziger
		} else {
			throw new RailCabFullException("Too many reizigers!");
		}
	}

	/**
	 * Voeg een reiziger toe aan de railcab.
	 * 
	 * @param r De toe te voegen reizigers.
	 * @throws RailCabFullException In case there are too many reizigers...
	 */
	public void addReizigers(ArrayList<Reiziger> r) throws RailCabFullException {
		if (reizigers.size() + r.size() <= maxReizigers) {
			reizigers.addAll(r);
			for (Reiziger p : reizigers) { // Set railcab voor elke reiziger
				p.setRailCab(this);
			}
		} else {
			throw new RailCabFullException("Too many reizigers!");
		}
	}

	/**
	 * Dump reizigers en notify observers (geef lijst van gedumpte reizigers)
	 */
	protected void dumpReizigers() {
		ArrayList<Reiziger> lijst = new ArrayList<Reiziger>();
		for (Reiziger reiziger : reizigers) { // set aankomsttijd van reizigers
			reiziger.setAankomstDate();
		}
		lijst.addAll(reizigers); // deep copy
		reizigers = new ArrayList<Reiziger>();
		setChanged();
		notifyObservers(lijst);
	}

	/**
	 * Geef het huidige aantal reizigers in de RailCab.
	 * 
	 * @return huidige aantal reizigers in deze RailCab
	 */
	public int getAantalReizigers() {
		return reizigers.size();
	}

	/**
	 * @return the afgehandeldeMissies
	 */
	public ArrayList<RailCabMissie> getAfgehandeldeMissies() {
		ArrayList<RailCabMissie> deepCopy = new ArrayList<RailCabMissie>();
		synchronized (afgehandeldeMissies) {
			deepCopy.addAll(afgehandeldeMissies);
		}
		return deepCopy;
	}

	/**
	 * @return the afgelegdeBaandelen
	 */
	public long getAfgelegdeBaandelen() {
		return afgelegdeBaandelen;
	}

	/**
	 * @return the bestemming
	 */
	public Baandeel getBestemming() {
		return bestemming;
	}

	/**
	 * Geef de voorste cab in een treintje ... of this als we alleen zijn.
	 * 
	 * @return de voorste cab
	 */
	public RailCab getHead() {
		RailCab head = this;
		while (head.getVorige() != null) {
			head = head.getVorige();
		}
		return head;
	}

	/**
	 * @return the huidigeMissie
	 */
	public synchronized RailCabMissie getHuidigeMissie() {
		return huidigeMissie;
	}

	/**
	 * Geef het baandeel waarop deze RailCab zich momenteel bevindt.
	 * 
	 * @return huidige baandeel
	 */
	public Baandeel getHuidigePositie() {
		return huidigePositie;
	}

	/**
	 * @return the maxReizigers
	 */
	public int getMaxReizigers() {
		return maxReizigers;
	}

	/**
	 * Geef de RailCab status
	 * 
	 * @return status
	 */
	public int getRailCabStatus() {
		return status;
	}

	/**
	 * Geef de lijst van in de railcab aanwezige reizigers.
	 * 
	 * @return lijst van reizigers.
	 */
	public ArrayList<Reiziger> getReizigers() {
		ArrayList<Reiziger> deepCopy = new ArrayList<Reiziger>();
		synchronized (reizigers) {
			deepCopy.addAll(reizigers);
		}
		return deepCopy;
	}

	/**
	 * Geef de achterste cab in een treintje ... of this als we alleen zijn.
	 * 
	 * @return de voorste cab
	 */
	public RailCab getTail() {
		RailCab tail = this;
		while (tail.getVorige() != null) {
			tail = tail.getVorige();
		}
		return tail;
	}

	/**
	 * @return the veroorzaker van een opstopping
	 */
	public RailCab getVeroorzaker() {
		return veroorzaker;
	}

	/**
	 * RailCabs kunnen gekoppeld worden. getVolgende geeft de volgende RailCab
	 * in een treintje.
	 * 
	 * @return volgende railcab in het treintje
	 */
	public RailCab getVolgende() {
		return volgende;
	}

	/**
	 * RailCabs kunnen gekoppeld worden. getVorige geeft de de vorige in het
	 * treintje.
	 * 
	 * @return vorige railcab
	 */
	public RailCab getVorige() {
		return vorige;
	}

	/**
	 * @return the wisselMap
	 */
	public HashMap<Wissel, Boolean> getWisselMap() {
		return wisselMap;
	}

	/**
	 * TODO: Haal reizigers af
	 * 
	 * Deze methode bepaalt welke reizigers moeten worden opgehaald van een
	 * station aan de hand van gegevens in missie.
	 * 
	 * Als er in missie specifieke groepen zijn meegegeven, dan vragen we
	 * station om die gespecificeerde groepen. Als we geen groepen hebben, maar
	 * wel een eindlocatie, dan pakken we reizigers op voor de gegeven
	 * eindlocatie. Als we geen groepen en geen eindlocatie hebben doen we
	 * niets.
	 * 
	 * Gegeven groepen in missie zijn puur indicatief en worden niet allemaal
	 * stuk voor stuk afgehandeld. Als de cab vol is, is ie vol!
	 * 
	 * @throws RailCabException doorgegeven vanuit lager gelegen code
	 */
	protected void haalReizigersAf() throws RailCabException {
		// TODO: haalReizigersAf() nog één scenario uitwerken (low prio)!
		if (huidigeMissie.getGroepen() != null) {
			// we zijn gestuurd voor (een) specifieke groep(en)
			Station station = ((StationBaandeel) huidigePositie).getStation();
			for (Groep g : huidigeMissie.getGroepen()) {
				// negeer gedelete groepen...
				if (!g.isDeleted()) {
					// pak reizigers op. vol == vol!
					if (maxReizigers - reizigers.size() > 0) {
						addReizigers(station.getReizigers(g, maxReizigers - reizigers.size()));
					} else {
						break; // vol == vol!
					}
				}
			}
		} else {
			if (huidigeMissie.getEindLocatie() != null) {
				// TODO: we pikken reizigers voor een spec. bestemming op
				System.out.println("nog implementeren: we pikken reizigers voor een spec. bestemming op");
			} else {
				// we hebben geen groepen en geen eindbestemming... doe niets.
			}
		}
		huidigeMissie.setMissieStatus(RailCabMissie.MISSIE_REIZIGERS_AFGEHAALD);
	}

	/**
	 * Is deze RailCab onderdeel van een treintje (heeft het een volgende of een
	 * vorige)?
	 * 
	 * @return ja of nee
	 */
	public boolean isGekoppeld() {
		return vorige != null || volgende != null;
	}

	/**
	 * Is deze railcab op de opgegeven locatie of het station waarvan de
	 * opgegeven locatie deel uitmaakt?
	 * 
	 * @param baandeel
	 * @return ja of nee
	 */
	public boolean isOpLocatie(Baandeel baandeel) {
		if (huidigePositie == baandeel) {
			return true;
		} else if (baandeel instanceof StationBaandeel) {
			StationBaandeel b = (StationBaandeel) baandeel;
			if (b.getStation().getBaandelen().contains(huidigePositie)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Is deze railcab op het opgegeven station?
	 * 
	 * @param station
	 * @return ja of nee
	 */
	public boolean isOpLocatie(Station station) {
		return isOpLocatie(station.getEersteBaandeel());
	}

	/**
	 * Koppel twee railcabs. Alleen railcab die naast elkaar op de baan staan
	 * kunnen gekoppeld worden
	 * 
	 * @param andereCab de te koppelen railcab
	 * @return de aangekoppelde railcab
	 * @throws RailCabException
	 */
	public RailCab koppel(RailCab andereCab) throws RailCabException {
		if (huidigePositie.getVolgende() != null && huidigePositie.getVolgende().getRailCab() == andereCab) {
			// koppel aan de voorkant
			setVorige(andereCab);
		} else if (huidigePositie.getVorige() != null && huidigePositie.getVorige().getRailCab() == andereCab) {
			// koppel aan de achterkant
			setVolgende(andereCab);
		} else {
			throw new RailCabException("Kan alleen RailCabs koppelen die naast elkaar staan!");
		}
		return andereCab;
	}

	/**
	 * Indien gekoppeld, ontkoppel RailCab van vorige en volgende.
	 */
	public void ontkoppel() {
		if (isGekoppeld()) {
			if (volgende != null) {
				volgende.setVorige(null);
			}
			if (vorige != null) {
				vorige.setVolgende(null);
			}
			setVolgende(null);
			setVorige(null);
		}
	}

	/**
	 * Stel bestemming in en bereken optimale route van huidige positie naar
	 * (nieuwe) bestemming
	 * 
	 * @param bestemming de bestemming van deze RailCab
	 */
	protected void setBestemming(Baandeel bestemming) {
		this.bestemming = bestemming;
		if (bestemming != null) {
			// wisselMap = Navigator.routeKaart(huidigePositie, bestemming);
			wisselMap = Navigator.routeInfo(huidigePositie, bestemming).getWisselMap();
		} else {
			wisselMap = new HashMap<Wissel, Boolean>();
		}
	}

	/**
	 * set gaVerkeerd
	 */
	public void setGaVerkeerd() {
		gaVerkeerd = true;
	}

	/**
	 * Set de het huidige baandeel waarop deze railcab zich bevindt.
	 * 
	 * @param positie huidige baandeel
	 * @throws RailCabException
	 */
	public void setHuidigePositie(Baandeel positie) throws RailCabException {
		huidigePositie = positie;

		// Dit zou de info in cab / rail consistent moeten houden...
		if (huidigePositie != null && huidigePositie.getRailCab() != this) {
			huidigePositie.setRailCab(this);
		}

	}

	/**
	 * @param koppelen the koppelen to set
	 */
	public void setKoppelen(boolean koppelen) {
		this.koppelen = koppelen;
	}

	/**
	 * Set de missie van deze RailCab.
	 * 
	 * We kunnen een nieuwe missie ontvangen in de volgende gevallen: - We
	 * hebben geen missie - We hebben een missie zonder eindlocatie (met alleen
	 * een start locatie)
	 * 
	 * @param missie de uit te voeren missie.
	 * @throws RailCabException Als er al een missie is...
	 */
	public synchronized void setMissie(RailCabMissie missie) throws RailCabException {
		if (missie == null) {
			huidigeMissie = null;
		} else if (huidigeMissie == null) {
			huidigeMissie = missie;
			setRailCabStatus(RailCab.STATUS_NIEUWE_MISSIE);
		} else {
			throw new RailCabException("Railcab already has a mission");
		}
	}

	/**
	 * Start de missie van deze RailCab.
	 * 
	 * @throws RailCabException
	 */
	public void setMissieStart() throws RailCabException {
		if (huidigeMissie != null && status == RailCab.STATUS_NIEUWE_MISSIE) {
			setBestemming(huidigeMissie.getStartLocatie());
			setRailCabStatus(RailCab.STATUS_MISSIE_IN_UITVOERING);
		} else {
			throw new RailCabException("Kan railcab missie niet starten! (missie: " + huidigeMissie + ", status: "
					+ status);
		}
	}

	/**
	 * Start de missie van deze RailCab.
	 * 
	 * @throws RailCabException
	 */
	public void setMissieStop() throws RailCabException {
		if (huidigeMissie != null && status != RailCab.STATUS_IDLE) {
			afgehandeldeMissies.add(huidigeMissie); // keep history
			setMissie(null);
			setRailCabStatus(RailCab.STATUS_IDLE);
		} else {
			throw new RailCabException("Kan railcab missie niet stoppen! (missie: " + huidigeMissie + ", status: "
					+ status);
		}
	}

	/**
	 * Set de RailCab status van de Railcab...
	 * 
	 * @param status
	 */
	protected void setRailCabStatus(int status) {
		this.status = status;
		setChanged();
		notifyObservers(status);
	}

	/**
	 * RailCabs kunnen gekoppeld worden. setVolgende set de volgende RailCab in
	 * een treintje.
	 * 
	 * @param volgende volgende railcab.
	 */
	public void setVolgende(RailCab volgende) {
		this.volgende = volgende;
		if (volgende != null && volgende.getVorige() != this) {
			volgende.setVorige(this);
		}
	}

	/**
	 * RailCabs kunnen gekoppeld worden. setVorige set de vorige RailCab in een
	 * treintje.
	 * 
	 * @param vorige vorige railcab.
	 */
	public void setVorige(RailCab vorige) {
		this.vorige = vorige;
		if (vorige != null && vorige.getVolgende() != this) {
			vorige.setVolgende(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (huidigeMissie != null) {
			return "" + huidigeMissie.getMissieStatus();
		} else {
			return "";
		}
	}

	/**
	 * Trek een RailCab, aan te roepen vanuit een andere RailCab, waarmee deze
	 * Cab gekoppeld is.
	 * 
	 * @param railcab de trekker, alleen gekoppelde cabs mogen trekken.
	 * @param nieuwePositie de nieuwe positie van de cab
	 * @throws RailCabException voor als er iets fout gaat
	 */
	public abstract void trekMij(RailCab railcab, Baandeel nieuwePositie) throws RailCabException;

	/**
	 * Verhoog algelegde baandelen en die van de missie indien nodig.
	 */
	protected void verhoogAfgelegdeBaandelen() {
		afgelegdeBaandelen++;
		if (huidigeMissie != null && huidigeMissie.getMissieStatus() == RailCabMissie.MISSIE_ONDERWEG_NAAR_EINDLOCATIE) {
			huidigeMissie.verhoogAfgelegdeBaandelen();
		}
		if (reizigers != null) {
			for (Reiziger r : reizigers) {
				r.incrementAfgelegdeBaandelen();
			}
		}
	}

	/**
	 * Ga naar de volgende positie. Deze methode is een soort van "fall-thru"
	 * procedure. Op het moment dat aan een zekere voorwaarde wel of niet
	 * voldaan wordt wordt de procedure onderbroken danwel voortgezet.
	 * 
	 * @return gelukt of niet
	 * @throws RailCabException
	 */
	protected abstract boolean verplaats() throws RailCabException;

}
