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

package com.tigam.railcab.controller;

import java.util.Observable;
import java.util.Observer;

import com.tigam.railcab.model.baan.Baandeel;
import com.tigam.railcab.model.baan.Groep;
import com.tigam.railcab.model.baan.RailCab;
import com.tigam.railcab.model.baan.Station;
import com.tigam.railcab.model.baan.StationBaandeel;
import com.tigam.railcab.model.exception.RailCabException;
import com.tigam.railcab.model.simulatie.Simulatie;

/**
 * Algemene abstract klasse voor een Scheduler. Biedt een basis om een scheduler
 * te bouwen.
 * 
 * @author Arjan van der Velde
 * 
 */
public abstract class Scheduler implements Observer {

	/** Treinbaan... */
	protected Treinbaan controller;

	/**
	 * Constructor... maak een Scheduler.
	 * 
	 * @param controller
	 */
	public Scheduler(Treinbaan controller) {
		this.controller = controller;
		this.controller.setScheduler(this);
		reset();
		for (Station station : controller.getStations()) { // reg stations
			station.addObserver(this);
			StationBaandeel b = station.getEersteBaandeel();
			while (b.getVolgende() instanceof StationBaandeel) {
				b = (StationBaandeel) b.getVolgende();
			}
			b.addObserver(this); // reg laatste baandeel
		}
		controller.getSimulatie().addObserver(this); // registreer ons bij de simulatie
	}

	/**
	 * Deze methode zet de Scheduler aan tot actie. Met behulp van deze methode
	 * kan een scheduler worden opgezet in een eigen thread of meelopen met een
	 * andere thread. Er dient natuurlijk voor gezorgd te worden dat deze
	 * methode NIET blocking is!
	 * 
	 * By default wordt deze methode aangeroepen vanuit update() wanneer een
	 * "tick" van de simulatie wordt ontvangen.
	 * 
	 * @throws RailCabException tja, er kan van alles fout gaan.
	 */
	protected abstract void actie() throws RailCabException;

	/**
	 * Process baandeel. Called by update()
	 * 
	 * @param baandeel
	 * @param railcab
	 */
	protected abstract void processBaandeelEvent(Baandeel baandeel, RailCab railcab);

	/**
	 * Process RailCab status change event. Called by update()
	 * 
	 * @param railcab de railcab waarop de event plaats vond
	 * @param status de nieuwe status
	 */
	protected abstract void processRailCabStatusChange(RailCab railcab, int status);

	/**
	 * Process Station groep event. Called by update()
	 * 
	 * @param station
	 * @param groep
	 */
	protected abstract void processStationGroepEvent(Station station, Groep groep);

	/**
	 * Reset de Scheduler. Deze default implementatie zorgt ervoor dat de
	 * scheduler als observer geregistreerd wordt bij alle railcabs. Scheduler
	 * dienen deze methode aan te vullen.
	 */
	public void reset() {
		for (RailCab railcab : controller.getRailCabs()) { // reg railcabs
			railcab.addObserver(this);
		}
	}

	public void update(Observable o, Object arg) {
		if (o instanceof Station) {
			// station event
			if (arg instanceof Groep) {
				// station groep toegevoegd
				processStationGroepEvent((Station) o, (Groep) arg);
			}
		} else if (o instanceof RailCab) {
			// railcab event
			if (arg instanceof Integer) {
				// railcab status change
				processRailCabStatusChange((RailCab) o, (Integer) arg);
			}
		} else if (o instanceof StationBaandeel) {
			// railcab event
			if (arg instanceof RailCab) {
				// railcab status change
				if (arg != null) {
					processBaandeelEvent((Baandeel) o, (RailCab) arg);
				} else {
					processBaandeelEvent((Baandeel) o, null);
				}
			}
		} else if (o instanceof Simulatie && Simulatie.getTime() % 10 == 0) { // roep actie() aan
			try {
				actie();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1); // fout == exit!
			}
		}
	}

}
