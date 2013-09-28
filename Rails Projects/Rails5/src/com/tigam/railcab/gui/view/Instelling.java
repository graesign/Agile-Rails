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

package com.tigam.railcab.gui.view;

import com.tigam.railcab.language.Language;
import com.tigam.railcab.stats.SimulatieData;

/**
 * Een Instelling bevat invoer van het SetupPanel.
 * 
 * @author Michiel
 * 
 */
public class Instelling {

	/**
	 * String variabele
	 */
	private String omschrijving;

	/**
	 * Capaciteit van een RailCab
	 */
	private int capaciteit;

	/**
	 * Aantal RailCabs in de simulatie
	 */
	private int aantalRailCabs;

	/**
	 * Het domein van de live grafieken, die geshowed worden
	 */
	private double domeinLiveGrafieken;

	/**
	 * Wat de reiziger aan het begin van de reis moet betalen
	 */
	private double startTarief;

	/**
	 * Wat de reiziger per afgelegd baandeel kwijt is.
	 */
	private double prijsPerBaandeel;

	/**
	 * Kosten voor de exploitant
	 */
	private double kostenPerBaandeel;

	/**
	 * Houd bij of RailCabs zich kunnen koppelen
	 */
	private boolean bKoppelen;

	/**
	 * Houd bij of de gebruiker ouude simulatie wens te gebruikern
	 */
	private boolean bOudeSimData;

	/**
	 * SimulatieData variabele
	 */
	private SimulatieData simData;

	/**
	 * Constructor
	 */
	public Instelling() {

	}

	/**
	 * Constructor
	 * 
	 * @param omschrijving
	 * @param capaciteit
	 * @param startTarief
	 * @param prijsPerBaandeel
	 * @param kostenPerBaandeel
	 * @param bKoppelen
	 * @param aantalRailCabs
	 * @param bOudeSimData
	 * @param simData
	 */
	public Instelling(String omschrijving, int capaciteit, double startTarief, double prijsPerBaandeel,
			double kostenPerBaandeel, boolean bKoppelen, int aantalRailCabs, boolean bOudeSimData, SimulatieData simData) {
		this.omschrijving = omschrijving;
		this.capaciteit = capaciteit;
		this.startTarief = startTarief;
		this.prijsPerBaandeel = prijsPerBaandeel;
		this.kostenPerBaandeel = kostenPerBaandeel;
		this.bKoppelen = bKoppelen;
		this.aantalRailCabs = aantalRailCabs;
		this.bOudeSimData = bOudeSimData;
		this.simData = simData;
	}

	/**
	 * @return int
	 */
	public int getAantalRailCabs() {
		return aantalRailCabs;
	}

	/**
	 * @return int
	 */
	public int getCapaciteit() {
		return capaciteit;
	}

	/**
	 * @return double
	 */
	public double getKostenPerBaandeel() {
		return kostenPerBaandeel;
	}

	/**
	 * @return String
	 */
	public String getOmschrijving() {
		return omschrijving;
	}

	/**
	 * @return boolean
	 */
	public boolean getOudeSimData() {
		return bOudeSimData;
	}

	/**
	 * @return double
	 */
	public double getPrijsPerBaandeel() {
		return prijsPerBaandeel;
	}

	/**
	 * @return SimulatieData
	 */
	public SimulatieData getSimData() {
		return simData;
	}

	/**
	 * @return double
	 */
	public double getStartTarief() {
		return startTarief;
	}

	/**
	 * @return int
	 */
	public double getWindowSizeLive() {
		return domeinLiveGrafieken;
	}

	/**
	 * @return boolean
	 */
	public boolean isKoppelen() {
		return bKoppelen;
	}

	/**
	 * @param aantalRailCabs
	 */
	public void setAantalRailCabs(int aantalRailCabs) {
		this.aantalRailCabs = aantalRailCabs;
	}

	/**
	 * @param capaciteit
	 */
	public void setCapaciteit(int capaciteit) {
		this.capaciteit = capaciteit;
	}

	/**
	 * @param bKoppelen
	 */
	public void setKoppelen(boolean bKoppelen) {
		this.bKoppelen = bKoppelen;
	}

	/**
	 * @param kostenPerBaandeel
	 */
	public void setKostenPerBaandeel(double kostenPerBaandeel) {
		this.kostenPerBaandeel = kostenPerBaandeel;
	}

	/**
	 * @param maxItemsLiveGrafieken
	 */
	public void setMaxItemsLive(int maxItemsLiveGrafieken) {
		domeinLiveGrafieken = maxItemsLiveGrafieken;
	}

	/**
	 * @param omschrijving
	 */
	public void setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
	}

	/**
	 * @param oudeSimData
	 */
	public void setOudeSimData(boolean oudeSimData) {
		bOudeSimData = oudeSimData;
	}

	/**
	 * @param prijsPerBaandeel
	 */
	public void setPrijsPerBaandeel(double prijsPerBaandeel) {
		this.prijsPerBaandeel = prijsPerBaandeel;
	}

	/**
	 * @param simData
	 */
	public void setSimData(SimulatieData simData) {
		this.simData = simData;
	}

	/**
	 * @param startTarief
	 */
	public void setStartTarief(double startTarief) {
		this.startTarief = startTarief;
	}

	@Override
	public String toString() {
		String sKoppelen, sOudeInvoer;

		if (bKoppelen == true) {
			sKoppelen = Language.getString("Instelling.0");} //$NON-NLS-1$
		else {
			sKoppelen = Language.getString("Instelling.1");} //$NON-NLS-1$

		if (simData != null) {
			sOudeInvoer = simData.toString();
		} else {
			sOudeInvoer = Language.getString("Instelling.2");} //$NON-NLS-1$

		String s = Language.getString("Instelling.3") + omschrijving.toString() + "<br>" + //$NON-NLS-1$ //$NON-NLS-2$
				Language.getString("Instelling.5") + startTarief + "<br>" + //$NON-NLS-1$ //$NON-NLS-2$
				Language.getString("Instelling.7") + kostenPerBaandeel + "<br>" + //$NON-NLS-1$ //$NON-NLS-2$
				Language.getString("Instelling.9") + capaciteit + "<br>" + //$NON-NLS-1$ //$NON-NLS-2$
				Language.getString("Instelling.11") + kostenPerBaandeel + "<br>" + //$NON-NLS-1$ //$NON-NLS-2$
				Language.getString("Instelling.13") + sKoppelen + "<br>" + //$NON-NLS-1$ //$NON-NLS-2$
				Language.getString("Instelling.15") + aantalRailCabs + "<br>" + //$NON-NLS-1$ //$NON-NLS-2$
				Language.getString("Instelling.17") + sOudeInvoer + "</html>"; //$NON-NLS-1$ //$NON-NLS-2$

		return s;
	}

}
