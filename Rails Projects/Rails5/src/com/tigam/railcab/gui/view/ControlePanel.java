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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.tigam.railcab.controller.Treinbaan;
import com.tigam.railcab.language.Language;

/**
 * Het knoppenpaneel bovenin het simulatiepaneel
 * 
 * @author Michiel
 * 
 */
public class ControlePanel extends JPanel {

	/**
	 * Een snelheidsmodificator in de vorm van een JSlider
	 * 
	 * @author Nils
	 * 
	 */
	class speedSlider implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			if (e.getSource() instanceof JSlider) {
				JSlider s = (JSlider) e.getSource();
				controller.setDelay(max - s.getValue());
				jsSpeed
						.setToolTipText(Language.getString("ControlePanel.8") + (double) jsSpeed.getValue() / (double) jsSpeed.getMaximum() * 100.0 + Language.getString("ControlePanel.9")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}

	/**
	 * Serializable...
	 */
	private static final long serialVersionUID = 8302506625275433277L;

	/**
	 * De startknop
	 */
	private JButton btStart;

	/**
	 * De stopknop
	 */
	private JButton btStop;

	/**
	 * De pauzeknop
	 */
	private JButton btPause;

	/**
	 * Deze knop voegt op alle stations willekeurige groepen toe.
	 */
	private JButton btChaos;

	/**
	 * Deze knop brengt je naar het geschiedenis paneel
	 */
	private JButton btGeschiedenis;

	/**
	 * JSlider variabele
	 */
	private JSlider jsSpeed;

	/**
	 * Treinbaan variabele, ook wel de MVC Controller
	 */
	private Treinbaan controller;

	/**
	 * Variabelen voor JSlider jsSpeed
	 */
	private int max = 500;

	/**
	 * Constructor
	 * 
	 * @param controller
	 */
	public ControlePanel(Treinbaan controller) {
		this.controller = controller;

		init();
		setup();
	}

	/**
	 * Getter voor de ChaosKnop in dit scherm
	 * 
	 * @return JButton
	 */
	public JButton getChaosKnop() {
		return btChaos;
	}

	/**
	 * Getter voor de GeschiedenisKnop in dit scherm
	 * 
	 * @return JButton
	 */
	public JButton getGeschiedenisKnop() {
		return btGeschiedenis;
	}

	/**
	 * Getter voor de PauzeKnop in dit scherm
	 * 
	 * @return JButton
	 */
	public JButton getPauzeKnop() {
		return btPause;
	}

	/**
	 * Getter voor de StartKnop in dit scherm
	 * 
	 * @return JButton
	 */
	public JButton getStartKnop() {
		return btStart;
	}

	/**
	 * Getter voor de StopKnop in dit scherm
	 * 
	 * @return JButton
	 */
	public JButton getStopKnop() {
		return btStop;
	}

	/**
	 * Initialisatie van de componenten
	 * 
	 */
	public void init() {
		setBorder(BorderFactory.createTitledBorder(Language.getString("ControlePanel.0"))); //$NON-NLS-1$
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setPreferredSize(new Dimension(500, 50));
		setMinimumSize(new Dimension(115, 50));
		setMaximumSize(new Dimension(9999, 50));

		btStart = new JButton(Language.getString("ControlePanel.1")); //$NON-NLS-1$
		btStop = new JButton(Language.getString("ControlePanel.2")); //$NON-NLS-1$
		btPause = new JButton(Language.getString("ControlePanel.3")); //$NON-NLS-1$
		btChaos = new JButton(Language.getString("ControlePanel.4")); //$NON-NLS-1$
		btGeschiedenis = new JButton(Language.getString("ControlePanel.5")); //$NON-NLS-1$

		jsSpeed = new JSlider(0, max - 1);
		jsSpeed.setMajorTickSpacing(10);
		jsSpeed.setValue(max - controller.getDelay());
		jsSpeed.setPaintTicks(true);

		jsSpeed
				.setToolTipText(Language.getString("ControlePanel.6") + (double) jsSpeed.getValue() / (double) jsSpeed.getMaximum() * 100.0 + Language.getString("ControlePanel.7")); //$NON-NLS-1$ //$NON-NLS-2$

		jsSpeed.addChangeListener(new speedSlider());
	}

	/**
	 * Maakt een "cement" paneel om tussen de knoppen te stoppen
	 * 
	 * @param width
	 * @param height
	 * @return JPanel
	 */
	public JPanel makeStrut(int width, int height) {
		JPanel p = new JPanel();
		Dimension d = new Dimension(width, height);
		p.setMinimumSize(d);
		p.setPreferredSize(d);
		p.setMaximumSize(d);
		return p;
	}

	/**
	 * Het in elkaar zetten van componenten
	 * 
	 */
	public void setup() {
		add(makeStrut(15, 0));
		add(btStart);
		add(makeStrut(4, 0));
		add(btPause);
		add(makeStrut(4, 0));
		add(btStop);
		add(makeStrut(25, 0));
		add(jsSpeed);
		add(makeStrut(25, 0));
		add(btChaos);
		add(makeStrut(10, 0));
		add(btGeschiedenis);
		add(makeStrut(10, 0));
	}

}
