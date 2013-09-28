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

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tigam.railcab.language.Language;
import com.tigam.railcab.model.baan.Station;

/**
 * Het StationPanel geeft je de mogenlijkheid om een station te inspecteren en
 * groepen toe te voegen. Het ontvangt updates van baanPanel in View.java door
 * de regel: >>baanPanel.addStationObserver(stationPanel);
 * 
 * @author Michiel
 * 
 */
public class StationPanel extends JPanel implements Observer {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6998882253540478003L;

	/**
	 * De contentpane
	 */
	private JPanel contentPane;

	/**
	 * Paneel voor als er geen station geselecteerd is
	 */
	private JPanel emptyPanel;

	/**
	 * Paneel om de stationsinformatie in te tonen
	 */
	private JPanel stationPanel;

	/**
	 * Station variabele
	 */
	private Station station = null;

	/**
	 * ArrayList<Stattion> variabele
	 */
	private ArrayList<Station> stations;

	/**
	 * JLabel variabele
	 */
	private JLabel instructie;

	/**
	 * InfoPanel variabele
	 */
	private InfoPanel infoPanel;

	/**
	 * VoegToePanel variabele
	 */
	private VoegToePanel voegToePanel;

	/**
	 * Contructor
	 * 
	 * @param stations
	 */
	public StationPanel(ArrayList<Station> stations) {
		this.stations = stations;
		init();
		setup();
	}

	/**
	 * Contructor waarmee je meteen een station instelt
	 * 
	 * @param stations
	 * @param station
	 */
	public StationPanel(ArrayList<Station> stations, Station station) {
		this.stations = stations;
		this.station = station;
		init();
		setup();
	}

	/**
	 * @return the infoPanel
	 */
	public InfoPanel getInfoPanel() {
		return infoPanel;
	}

	/**
	 * @return the voegToePanel
	 */
	public VoegToePanel getVoegToePanel() {
		return voegToePanel;
	}

	/**
	 * Initialisatie van de componenten
	 * 
	 */
	public void init() {
		setPreferredSize(new Dimension(500, 180));
		setMinimumSize(new Dimension(100, 180));
		setMaximumSize(new Dimension(9999, 180));
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.LINE_AXIS));

		emptyPanel = new JPanel();
		emptyPanel.setBorder(BorderFactory.createTitledBorder(Language.getString("StationPanel.1"))); //$NON-NLS-1$

		stationPanel = new JPanel();
		stationPanel.setLayout(new BoxLayout(stationPanel, BoxLayout.LINE_AXIS));

		instructie = new JLabel(Language.getString("StationPanel.2")); //$NON-NLS-1$

		infoPanel = new InfoPanel();
		voegToePanel = new VoegToePanel(stations);

		if (station != null) {
			infoPanel.setStation(station);
			voegToePanel.setStation(station);
		}
	}

	/**
	 * Maakt het scherm leeg door emptyPanel te tonen
	 */
	public void setEmpty() {
		stationPanel.setVisible(false);
		emptyPanel.setVisible(true);
	}

	/**
	 * Maakt van een Station object het huidige station en brengt de
	 * sub-componenten op de hoogte.
	 * 
	 * @param station
	 */
	public void setStation(Station station) {
		this.station = station;

		stationPanel.setBorder(BorderFactory
				.createTitledBorder(Language.getString("StationPanel.0") + station.getNaam())); //$NON-NLS-1$

		infoPanel.setStation(station);
		voegToePanel.setStation(station);

		emptyPanel.setVisible(false);
		stationPanel.setVisible(true);
	}

	/**
	 * Samenvoegen van de componenten
	 * 
	 */
	public void setup() {
		setEmpty();

		add(contentPane);

		contentPane.add(emptyPanel);
		contentPane.add(stationPanel);

		emptyPanel.add(instructie);

		stationPanel.add(infoPanel);
		stationPanel.add(voegToePanel);
	}

	/**
	 * Deze functie luistert zowel naar het baanPanel als naar verschillende
	 * Station objecten
	 * 
	 * @param observable
	 * @param o
	 */
	public void update(Observable observable, Object o) {
		if (observable instanceof Station && station.equals(observable)) {
			infoPanel.setStation(station);
		} else if (o == null) {
			if (station != null) {
				station.deleteObserver(this);
			}
			setEmpty();
		} else if (o instanceof Station) {
			Station s = (Station) o;
			s.addObserver(this);
			setStation(s);
		}

	}

}
