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

import java.util.ArrayList;
import java.util.Observable;

import com.tigam.railcab.gui.view.Instelling;
import com.tigam.railcab.model.baan.BaanHeader;
import com.tigam.railcab.model.baan.RailCab;
import com.tigam.railcab.model.baan.Station;
import com.tigam.railcab.model.exception.RailCabException;
import com.tigam.railcab.model.simulatie.Simulatie;
import com.tigam.railcab.stats.SimulatieRunner;

/**
 * Dit is de main entry point naar de treinbaan en simulatie, vanuit de GUI. In
 * MVC termen is dit de controller...
 * 
 * @author Arjan van der Velde
 * 
 */
public class Treinbaan extends Observable {

	/** status: stop / start */
	private boolean status;

	/** de simulatie die het model aanstuurt */
	private Simulatie simulatie;

	/** simulatiedata controller */
	private SimulatieDataController simulatieDataController;

	/** de scheduler */
	private Scheduler scheduler;

	/** simulatie runner, voor replays */
	private SimulatieRunner runner;

	/**
	 * Construct de Treinbaan controller.
	 * 
	 * @param simulatie De te draaien simulatie
	 */
	public Treinbaan(Simulatie simulatie) {
		this.simulatie = simulatie;
		new Thread(this.simulatie).start();
	}

	/**
	 * Gedelegeerde getBaanHeader() van Simulatie
	 * 
	 * @return een referentie naar een baandeel van de baan (delegated)
	 */
	public BaanHeader getBaanHeader() {
		return simulatie.getBaanHeader();
	}

	/**
	 * Gedelegeerde getDelay() van Simulatie.
	 * 
	 * @return delay (interval interne klok van de simulatie) (delegated)
	 */
	public int getDelay() {
		return simulatie.getDelay();
	}

	/**
	 * Gedelegeerde getRailCabs() van Simulatie
	 * 
	 * @return Lijst van railcabs in de simulatie (delegated)
	 */
	public ArrayList<RailCab> getRailCabs() {
		return simulatie.getRailCabs();
	}

	/**
	 * Geef de Simulatie
	 * 
	 * @return de simulatie waarvoor de controller controller is
	 */
	public Simulatie getSimulatie() {
		return simulatie;
	}

	/**
	 * Geef de SimulatieDataController...
	 * 
	 * @return simulatie data controller
	 */
	public SimulatieDataController getSimulatieDataController() {
		return simulatieDataController;
	}

	/**
	 * Vraag de status op van de simulatie
	 * 
	 * @return Status van de simulatie, vergelijkbaar met Simulate.STATUS_NULL,
	 *         Simulatie.STATUS_STOP, Simulatie.STATUS_RUNNING,
	 *         Simulatie.STATUS_PAUSE
	 */
	public int getSimulatieStatus() {
		return simulatie.getStatus();
	}

	/**
	 * Gedelegeerde getStations() van Simulatie
	 * 
	 * @return stations in de simulatie (delegated)
	 */
	public ArrayList<Station> getStations() {
		return simulatie.getStations();
	}

	/**
	 * get status
	 * 
	 * @return status
	 */
	public boolean isRunning() {
		return status;
	}

	/**
	 * Pauzeer de simulatie (delegated)
	 */
	public void pauseSimulatie() {
		simulatie.pauseSimulatie();
	}

	/**
	 * "Reset" de simulatie. Zie Simulatie.reset()
	 * 
	 * @param inst instellingen
	 * @throws RailCabException
	 */
	public void resetSimulatie(Instelling inst) throws RailCabException {
		if (runner != null) {
			simulatie.deleteObserver(runner);
			runner = null;
		}
		if (inst.getOudeSimData()) {
			runner = new SimulatieRunner(inst.getSimData());
			simulatie.addObserver(runner);
		}
		simulatie.setAantailRailCabs(inst.getAantalRailCabs());
		simulatie.reset(inst.getCapaciteit());
		scheduler.reset();
		simulatie.getBaanHeader().reset();
	}

	/**
	 * Zet de delay voor de simulatie. (delegate)
	 * 
	 * @param delay delay in milleseconden
	 */
	public void setDelay(int delay) {
		simulatie.setDelay(delay);
	}

	/**
	 * Set de scheduler om de simulatie te sturen
	 * 
	 * @param scheduler de scheduler
	 */
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * Set simulatie data controller.
	 * 
	 * @param simulatieDataController
	 */
	public void setSimulatieDataController(SimulatieDataController simulatieDataController) {
		this.simulatieDataController = simulatieDataController;
	}

	/**
	 * Start de simulatie
	 */
	public void startSimulatie() {
		status = true;
		simulatie.startSimulatie();
		setChanged();
		notifyObservers();
	}

	/**
	 * Stop de simulatie (maar geen reset)
	 * 
	 * @throws RailCabException voor als het fout gaat
	 */
	public void stopSimulatie() throws RailCabException {
		status = false;
		simulatie.stopSimulatie();
		setChanged();
		notifyObservers();
	}

}
