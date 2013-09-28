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

import com.tigam.railcab.gui.view.Instelling;
import com.tigam.railcab.stats.DataHandler;
import com.tigam.railcab.stats.SimulatieData;

/**
 * Controleert het bijhouden en maken van simulatie data objecten
 * 
 * @author Tonny Wildeman
 */
public class SimulatieDataController {

	/**
	 * De lijst met gedraaide simulaties
	 */
	ArrayList<SimulatieData> oudeSimulatieDatas;

	/**
	 * De data handler die de data ophaalt en verwerkt uit de simulatie
	 */
	DataHandler huidigeDataHandler;

	/**
	 * Het huidige simulatie data object
	 */
	SimulatieData huidigeSimulatie;

	/**
	 * De controller van de treinbaan
	 */
	Treinbaan controller;

	/**
	 * De constructor
	 * 
	 * @param controller - de controller van de treinbaan
	 */
	public SimulatieDataController(Treinbaan controller) {
		this.controller = controller;
		controller.setSimulatieDataController(this);

		oudeSimulatieDatas = new ArrayList<SimulatieData>();
	}

	/**
	 * Geeft de huidige handler van de simulatie data terug
	 * 
	 * @return de huidige simulatie data handler
	 */
	public DataHandler getHuidigeSimulatieHandler() {
		return huidigeDataHandler;
	}

	/**
	 * Geeft de oude simulaties terug
	 * 
	 * @return lijst met oude simulaties
	 */
	public ArrayList<SimulatieData> getOudeSimulaties() {
		return oudeSimulatieDatas;
	}

	/**
	 * Beindigt de huidige simulatie en zorgt dat die opgeslagen wordt in de
	 * lijst met oude simulaties
	 */
	public void huidigeSimulatieOpslaan() {
		if (huidigeSimulatie != null) {
			huidigeDataHandler.setNonEditable();
			oudeSimulatieDatas.add(huidigeSimulatie);
		}
		huidigeSimulatie = null;
	}

	/**
	 * Maakt een nieuwe simulatie data object aan met bijbehorende data handler
	 * en geeft de handler terug
	 * 
	 * @param instelling - de instellingen van de te draaien simulatie
	 * @return de data handler bij de nieuwe simulatie
	 */
	public DataHandler maakNieuweSimulatie(Instelling instelling) {
		huidigeSimulatie = new SimulatieData(instelling);

		huidigeSimulatie.setRailCabs(controller.getRailCabs());
		huidigeSimulatie.setStations(controller.getStations());

		huidigeDataHandler = new DataHandler(controller.getSimulatie());
		huidigeDataHandler.setSimulatieData(huidigeSimulatie);
		return huidigeDataHandler;
	}
}