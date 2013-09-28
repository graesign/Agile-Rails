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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tigam.railcab.language.Language;
import com.tigam.railcab.stats.DataHandler;

/**
 * Een GeschiedenisItem word gebruikt om oude simulaties weer te geven in het
 * GeschiedenisPaneel
 * 
 * @author Michiel
 * 
 */
public class GeschiedenisItem extends JPanel implements ItemListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 566329467678107775L;

	/**
	 * JLabel variabele
	 */
	private JLabel lbInformatie;

	/**
	 * JCheckBox variabele
	 */
	private JCheckBox ckLatenZien;

	/**
	 * DataHandler variabele
	 */
	private DataHandler dataHandler;

	/**
	 * StatistiekPanel variabele
	 */
	private NestedStatistiekPanel statsPanel;

	/**
	 * GeschiedenisPanel variabele
	 */
	private GeschiedenisPanel geschiedenisPanel;

	/**
	 * Contructor
	 * 
	 * @param dataHandler
	 * @param geschiedenisPanel
	 */
	public GeschiedenisItem(DataHandler dataHandler, GeschiedenisPanel geschiedenisPanel) {
		this.dataHandler = dataHandler;
		this.geschiedenisPanel = geschiedenisPanel;
		init();
		setup();
	}

	/**
	 * Getter voor het StatistiekPaneel van dit object
	 * 
	 * @return statsPanel
	 */
	public NestedStatistiekPanel getStatsPanel() {
		return statsPanel;
	}

	/**
	 * Initialisatie van de componenten
	 * 
	 */
	private void init() {
		setLayout(null);
		setBorder(BorderFactory.createTitledBorder(dataHandler.getSimulatieData().toString()));
		setPreferredSize(new Dimension(0, 90));
		setMaximumSize(new Dimension(9999, 90));
		setMinimumSize(new Dimension(200, 90));

		statsPanel = new NestedStatistiekPanel(dataHandler);
		statsPanel.setVisible(true);

		lbInformatie = new JLabel(Language.getString("GeschiedenisItem.0")); //$NON-NLS-1$
		lbInformatie.setBounds(10, 50, 200, 20);
		lbInformatie.setToolTipText(dataHandler.getSimulatieData().getInstelling().toString());

		ckLatenZien = new JCheckBox(Language.getString("GeschiedenisItem.1"), false); //$NON-NLS-1$
		ckLatenZien.setBounds(10, 25, 100, 20);
	}

	/**
	 * Handler voor de JCheckBox ckLatenZien van dit object
	 * 
	 * @param e
	 */
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();

		if (source == ckLatenZien) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				geschiedenisPanel.removeStats(statsPanel);
			} else {
				geschiedenisPanel.addStats(statsPanel);
			}
		}
	}

	/**
	 * Het in elkaar zetten van de componenten
	 * 
	 */
	private void setup() {
		add(ckLatenZien);

		add(lbInformatie);

		ckLatenZien.addItemListener(this);

	}

}