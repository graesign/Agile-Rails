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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.tigam.railcab.controller.Treinbaan;
import com.tigam.railcab.language.Language;
import com.tigam.railcab.model.baan.Groep;
import com.tigam.railcab.model.baan.Station;

/**
 * Een InfoPanel kan de wachtrij van een Station bekijken en groepen verwijderen
 * 
 * @author Michiel
 * 
 */
public class InfoPanel extends JPanel implements Observer {

	/**
	 * De VerwijderHandler verwijdert de in de lijst geselecteerde groepen van
	 * het station.
	 * 
	 * @author Michiel
	 */
	class VerwijderHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			Object[] selectie = groepenLijst.getSelectedValues();
			for (int i = 0; i < selectie.length; i++) {
				if (selectie[i] instanceof Groep) {
					Groep groep = (Groep) selectie[i];
					try {
						station.removeGroep(groep);
					} catch (Exception exception) {
						JDialog dialog = new JDialog();
						dialog.setTitle(Language.getString("InfoPanel.2")); //$NON-NLS-1$
						JLabel lbException = new JLabel(exception.toString());
						dialog.add(lbException);
					}
				}
			}
		}
	}

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1790438849777888798L;

	/**
	 * JList variabele
	 */
	private JList groepenLijst;

	/**
	 * JButton variabele
	 */
	private JButton btVerwijder;

	/**
	 * Station variabele
	 */
	private Station station;

	/**
	 * JScrollPane variabele
	 */
	private JScrollPane scrollpane;

	/**
	 * Object array variabele
	 */
	private Object[] groepen;

	/**
	 * Lege Constructor
	 * 
	 */
	public InfoPanel() {
		init();
		setup();
	}

	/**
	 * Constructor
	 * 
	 * @param stations
	 * @param station
	 */
	public InfoPanel(ArrayList<Station> stations, Station station) {
		this.station = station;
		init();
		setup();
	}

	/**
	 * Initialisatie van de componenten
	 * 
	 */
	public void init() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createTitledBorder(Language.getString("InfoPanel.0"))); //$NON-NLS-1$
		setPreferredSize(new Dimension(400, 200));
		setMaximumSize(new Dimension(9999, 200));

		scrollpane = new JScrollPane();

		groepenLijst = new JList();
		groepenLijst.setEnabled(false);

		btVerwijder = new JButton(Language.getString("InfoPanel.1")); //$NON-NLS-1$
		btVerwijder.setEnabled(false);

	}

	/**
	 * Ontvang een nieuw station
	 * 
	 * @param s
	 */
	public void setStation(Station s) {
		station = s;
		updateList();
	}

	/**
	 * Samenvoegen van de componenten
	 * 
	 */
	public void setup() {
		add(scrollpane);
		scrollpane.getViewport().setView(groepenLijst);
		add(btVerwijder);

		btVerwijder.addActionListener(new VerwijderHandler());
	}

	/**
	 * Enable / disable knoppen op basis van controller status
	 * 
	 * @param o
	 * @param arg
	 */
	public void update(Observable o, Object arg) {
		if (o instanceof Treinbaan) {
			btVerwijder.setEnabled(((Treinbaan) o).isRunning());
			groepenLijst.setEnabled(((Treinbaan) o).isRunning());
		}
	}

	/**
	 * Update de groepenlijst op het scherm
	 * 
	 */
	public void updateList() {
		LinkedList<Groep> list = station.getWachtrij();
		groepen = list.toArray();
		groepenLijst.removeAll();
		groepenLijst.setListData(groepen);
	}
}
