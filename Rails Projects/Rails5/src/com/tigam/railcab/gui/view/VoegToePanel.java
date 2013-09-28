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

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.tigam.railcab.controller.Treinbaan;
import com.tigam.railcab.language.Language;
import com.tigam.railcab.model.baan.Groep;
import com.tigam.railcab.model.baan.Station;

/**
 * Een VoegToePaneel kan een Groep toevoegen aan een gegeven Station
 * 
 * @author Michiel
 * 
 */
public class VoegToePanel extends JPanel implements Observer {

	/**
	 * De handler van de Chaos knop, voegt 100 willekeurige groepen toe aan het
	 * station wat geinspecteert wordt
	 * 
	 * @author Nils Dijk
	 */
	class ChaosHandler implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			int s2, reizigers;

			for (int i = 0; i < 100; i++) {
				do {
					s2 = (int) (Math.random() * stations.size());
				} while (stations.get(s2) == station);
				reizigers = (int) (Math.random() * 10) + 1;
				station.addGroep(new Groep(station, stations.get(s2), reizigers));
			}
		}

	}

	/**
	 * Deze Handler verzamelt en verifieert de input van de gebruiker. Dan stelt
	 * het een nieuwe Groep samen en voegt die toe aan het Station.
	 * 
	 * @author Michiel
	 * 
	 */
	class VoegToeHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				int aantal = Integer.parseInt(txAantal.getText());
				if (cbBestemming.getSelectedItem() instanceof Station) {
					Station bestemming = (Station) cbBestemming.getSelectedItem();
					Groep g = new Groep(station, bestemming, aantal);
					station.addGroep(g);
				}

			} catch (NumberFormatException nfe) {
				System.out.println(Language.getString("VoegToePanel.6")); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Serializable...
	 */
	private static final long serialVersionUID = -3610361410519706887L;

	/**
	 * Station variabele
	 */
	private Station station;

	/**
	 * ArrayList<Station> variabele
	 */
	private ArrayList<Station> stations;

	/**
	 * JLabel wijst het "aantal personen" invoer vak aan
	 */
	private JLabel lbAantal;

	/**
	 * JLabel wijst de Bestemmings ComboBox aan
	 */
	private JLabel lbBestemming;

	/**
	 * JTextfield variabele
	 */
	private JTextField txAantal;

	/**
	 * JComboBox variabele
	 */
	private JComboBox cbBestemming;

	/**
	 * De Voeg Toe knop
	 */
	private JButton btVoegToe;

	/**
	 * De Willekeur knop
	 */
	private JButton btChaos;

	/**
	 * Constructor waar het geselecteerde station niet bekend is.
	 * 
	 * @param stations
	 */
	public VoegToePanel(ArrayList<Station> stations) {
		this.stations = stations;
		init();
		setup();
	}

	/**
	 * Contructor waar het geselecteerde station bekend is.
	 * 
	 * @param stations
	 * @param station
	 */
	public VoegToePanel(ArrayList<Station> stations, Station station) {
		this.stations = stations;
		this.station = station;
		init();
		setup();
	}

	/**
	 * Initialisatie van de componenten.
	 * 
	 */
	public void init() {
		setLayout(null);

		setPreferredSize(new Dimension(200, 150));
		setMinimumSize(new Dimension(160, 0));
		setMaximumSize(new Dimension(200, 150));

		setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		setBorder(BorderFactory.createTitledBorder(Language.getString("VoegToePanel.0"))); //$NON-NLS-1$

		lbAantal = new JLabel(Language.getString("VoegToePanel.1")); //$NON-NLS-1$
		lbAantal.setBounds(10, 20, 100, 18);

		lbBestemming = new JLabel(Language.getString("VoegToePanel.2")); //$NON-NLS-1$
		lbBestemming.setBounds(10, 40, 100, 18);

		txAantal = new JTextField(Language.getString("VoegToePanel.3")); //$NON-NLS-1$
		txAantal.setBounds(110, 20, 40, 18);
		txAantal.setEnabled(false);

		cbBestemming = new JComboBox();
		cbBestemming.setBounds(10, 58, 140, 18);
		cbBestemming.setEnabled(false);

		btVoegToe = new JButton(Language.getString("VoegToePanel.4")); //$NON-NLS-1$
		btVoegToe.setBounds(10, 85, 140, 25);
		btVoegToe.setEnabled(false);

		btChaos = new JButton(Language.getString("VoegToePanel.5")); //$NON-NLS-1$
		btChaos.setBounds(10, 115, 140, 25);
		btChaos.setEnabled(false);
		btChaos.addActionListener(new ChaosHandler());
	}

	/**
	 * Set het Station en update de inhoud van het scherm
	 * 
	 * @param s
	 */
	public void setStation(Station s) {
		station = s;
		updateOpties();
	}

	/**
	 * Samenstellen van de componenten.
	 * 
	 */
	public void setup() {
		add(lbAantal);
		add(lbBestemming);
		add(txAantal);
		add(cbBestemming);
		add(btVoegToe);
		add(btChaos);

		btVoegToe.addActionListener(new VoegToeHandler());
	}

	/**
	 * Enable / disable knoppen op basis van controller status
	 * 
	 * @param o
	 * @param arg
	 */
	public void update(Observable o, Object arg) {
		if (o instanceof Treinbaan) {
			btVoegToe.setEnabled(((Treinbaan) o).isRunning());
			btChaos.setEnabled(((Treinbaan) o).isRunning());
			cbBestemming.setEnabled(((Treinbaan) o).isRunning());
			txAantal.setEnabled(((Treinbaan) o).isRunning());
		}
	}

	/**
	 * Update de mogelijke opties in de ComboBox cbBestemming
	 * 
	 */
	public void updateOpties() {
		Object o = cbBestemming.getSelectedItem();
		boolean isInNewList = false;
		cbBestemming.removeAllItems();
		for (Station station : stations) {
			if (!station.equals(this.station)) {
				cbBestemming.addItem(station);
				if (station == o) {
					isInNewList = true;
				}
			}
		}
		if (isInNewList) {
			cbBestemming.setSelectedItem(o);
		}
	}

}
