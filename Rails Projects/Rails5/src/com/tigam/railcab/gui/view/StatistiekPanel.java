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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jfree.chart.ChartPanel;

import com.tigam.railcab.language.Language;
import com.tigam.railcab.stats.DataHandler;

/**
 * TODO: Een Panel dat Aan de hand van een DataHandler grafieken kan ontrekken
 * en afbeelden. Moet ook veelvuldig naast elkaar gezet worden in het
 * GeschiedenisFrame.
 * 
 * @author Michiel
 * 
 */
public class StatistiekPanel extends JScrollPane {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7416467327804013908L;

	/**
	 * De contentpane
	 */
	private JPanel contentPane;

	/**
	 * DataHandler van de bijbehorende simulatie
	 */
	private DataHandler dataHandler;

	/**
	 * Alle grafieken die dit paneel moet weergeven
	 */
	private ArrayList<GrafiekPanel> grafieken;

	/**
	 * Lege Contructor
	 * 
	 */
	public StatistiekPanel() {
		init();
		setup();
	}

	/**
	 * Constructor
	 * 
	 * @param dataHandler
	 */
	public StatistiekPanel(DataHandler dataHandler) {
		setDataHandler(dataHandler);
		init();
		setup();
		initGrafieken();
	}

	/**
	 * Initialisatie van de componenten
	 * 
	 */
	private void init() {
		if (dataHandler == null) {
			setBorder(BorderFactory.createTitledBorder(Language.getString("StatistiekPanel.1"))); //$NON-NLS-1$
		}

		contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		contentPane.setMinimumSize(new Dimension(200, 200));
		contentPane.setMaximumSize(new Dimension(200, 9999));

		setBackground(contentPane.getBackground());
	}

	/**
	 * Initialiseert de grafieken die uit de DataHandler worden verkregen
	 * 
	 */
	public void initGrafieken() {
		if (dataHandler != null) {
			grafieken = new ArrayList<GrafiekPanel>();

			for (ChartPanel cp : dataHandler.getGrafieken()) {
				if (cp != null) {
					GrafiekPanel gp = new GrafiekPanel(cp);
					grafieken.add(gp);
				}
			}

			contentPane.removeAll();

			for (GrafiekPanel gp : grafieken) {
				contentPane.add(gp);
			}

		}
	}

	/**
	 * Setter voor de DataHandler dataHandler variabele
	 * 
	 * @param dataHandler
	 */
	public void setDataHandler(DataHandler dataHandler) {
		this.dataHandler = dataHandler;
		setBorder(BorderFactory
				.createTitledBorder(Language.getString("StatistiekPanel.0") + dataHandler.getSimulatieData().toString())); //$NON-NLS-1$
	}

	/**
	 * Samenstellen van de componenten
	 */
	private void setup() {

		getViewport().setView(contentPane);

	}

	/**
	 * Override van de setVisible methode
	 */
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b == true) {
			setSize(0, 0);
		} else {
			setSize(getPreferredSize());
		}
	}

}
