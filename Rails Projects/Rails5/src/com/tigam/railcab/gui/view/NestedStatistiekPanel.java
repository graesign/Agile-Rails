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

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.tigam.railcab.stats.DataHandler;

/**
 * Een NestedStatistiekPanel is een JSplitPane waarvan de rechter helft ruimte
 * bied aan een volgende.
 * 
 * @author Michiel
 * 
 */
public class NestedStatistiekPanel extends JSplitPane {

	/**
	 * Paneel dat zijn oude afmetingen bewaart als het van grootte veranderd
	 * 
	 * @author Michiel
	 * 
	 */
	class LeftPanel extends JPanel {

		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = -4479259177145880522L;

		/**
		 * Het verschil in groote na een vergroting of verkleining
		 */
		private Dimension sizeDiff;

		/**
		 * Constructor
		 */
		public LeftPanel() {
			init();
			setup();
		}

		/**
		 * @return Dimension
		 */
		public Dimension getSizeDiff() {
			return sizeDiff;
		}

		/**
		 * Initialisatie van de componenten
		 */
		public void init() {
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

			setMinimumSize(new Dimension(170, 0));
			setPreferredSize(new Dimension(300, 680));
			setMaximumSize(new Dimension(500, 9999));
		}

		@Override
		public void setBounds(int x, int y, int width, int height) {
			sizeDiff = new Dimension(width - getWidth(), height - getHeight());
			super.setBounds(x, y, width, height);
		}

		/**
		 * Samenstellen van de componenten
		 */
		public void setup() {

		}

	}

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7468384541854811171L;

	/**
	 * Het StatistiekPanel wat in de rechter helft getoond word
	 */
	private StatistiekPanel statistiekPanel;

	/**
	 * Een container voor het StatistiekPanel.
	 */
	private LeftPanel leftPanel;

	/**
	 * De volgende
	 */
	private NestedStatistiekPanel next;

	/**
	 * @param dh
	 */
	public NestedStatistiekPanel(DataHandler dh) {
		statistiekPanel = new StatistiekPanel(dh);
		init();
		setup();
	}

	/**
	 * Verandert de groote op basis van een Dimensie Zie ook de
	 * LeftPanel.setBounds()
	 * 
	 * @param d
	 */
	public void changePreferredWidth(Dimension d) {
		Dimension d0 = getSize();
		Dimension d1 = new Dimension(d0.width + d.width, d0.height);
		setPreferredSize(d1);
	}

	/**
	 * @return NestedStatistiekPanel
	 */
	public NestedStatistiekPanel getNext() {
		return next;
	}

	/**
	 * Initialisatie van de componenten
	 */
	public void init() {
		leftPanel = new LeftPanel();
	}

	/**
	 * @param next
	 */
	public void setNext(NestedStatistiekPanel next) {
		this.next = next;
		if (next != null) {
			setRightComponent(next);
		} else {
			setNextEmpty();
		}

	}

	/**
	 * Maakt dat deze geen NestedStatistiekPanel als rechter buur heeft maar een
	 * leeg paneel
	 */
	public void setNextEmpty() {
		JPanel p = new JPanel();
		p.setMinimumSize(new Dimension(200, 0));
		p.setPreferredSize(new Dimension(300, 0));
		p.setMaximumSize(new Dimension(400, 9999));
		setRightComponent(p);
	}

	/**
	 * Het samenvoegen van de componenten
	 */
	public void setup() {
		if (next == null) {
			setNextEmpty();
		}
		leftPanel.add(statistiekPanel);
		setLeftComponent(leftPanel);
	}

}
