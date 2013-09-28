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
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;

import com.tigam.railcab.language.Language;

/**
 * Een GrafiekPanel bevat een JFreeChart grafiek die je wel of niet kunt laten
 * zien.
 * 
 * @author Michiel
 * 
 */
public class GrafiekPanel extends JPanel implements ItemListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 301070968239144605L;

	/**
	 * JPanel variabele
	 */
	private JPanel verbergPanel;

	/**
	 * ChartPanel variabele
	 */
	private ChartPanel grafiek;

	/**
	 * JCheckBox variabele
	 */
	private JCheckBox ckLaatZien;

	/**
	 * Constructor
	 * 
	 * @param grafiek
	 */
	public GrafiekPanel(ChartPanel grafiek) {
		this.grafiek = grafiek;
		init();
		setup();
	}

	/**
	 * Initialisatie van de componenten
	 * 
	 */
	public void init() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createTitledBorder(grafiek.getName()));
		setMinimumSize(new Dimension(100, 325));

		verbergPanel = new JPanel();
		verbergPanel.setLayout(new BoxLayout(verbergPanel, BoxLayout.PAGE_AXIS));
		verbergPanel.setMaximumSize(new Dimension(9999, 30));

		ckLaatZien = new JCheckBox(Language.getString("GrafiekPanel.0"), true); //$NON-NLS-1$

		grafiek.setMinimumSize(new Dimension(0, 100));
		grafiek.setPreferredSize(new Dimension(180, 200));
	}

	/**
	 * Handler voor JCheckBox ckLatenZien Maakt het mogelijk een grafiek wel of
	 * niet te laten zien
	 * 
	 * @param e
	 */
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();

		if (source == ckLaatZien) {
			grafiek.setVisible(true);
		}

		if (e.getStateChange() == ItemEvent.DESELECTED) {
			grafiek.setVisible(false);
		}
	}

	/**
	 * Samenstellen van de componenten
	 * 
	 */
	public void setup() {
		add(verbergPanel);
		add(grafiek);

		verbergPanel.add(ckLaatZien);
		ckLaatZien.addItemListener(this);
	}

}
