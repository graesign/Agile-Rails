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
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.tigam.railcab.controller.Treinbaan;
import com.tigam.railcab.gui.view.NestedStatistiekPanel.LeftPanel;
import com.tigam.railcab.language.Language;
import com.tigam.railcab.stats.DataHandler;

/**
 * Hier komen alle voorgaande simulaties in te staan.
 * 
 * @author Michiel
 * 
 */
public class GeschiedenisPanel extends JPanel implements ComponentListener {

	/**
	 * Handler voor de TerugKnop in dit scherm Brengt de gebruiker terug naar
	 * het SimulatieScherm
	 * 
	 * @author Michiel
	 * 
	 */
	public class TerugKnopHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			statPanels = new ArrayList<NestedStatistiekPanel>();
			view.showSimulatie();

		}
	}

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4246153353218140320L;

	/**
	 * ScrollPane die in het linker paneel zit
	 */
	private JScrollPane leftScrollPane;

	/**
	 * ScrollPane die in het rechter paneel zit
	 */
	private JScrollPane rightScrollPane;

	/**
	 * Een paneel die al het andere bevat
	 */
	private JPanel contentPane;

	/**
	 * JPanel die leftPanel en rightPanel bevat
	 */
	private JPanel centerPanel;

	/**
	 * JPanel dat onderinstaat en de TerugKnop bevat
	 */
	private JPanel buttonPanel;

	/**
	 * Het linker paneel, bevat de StatistiekPanelen
	 */
	private JPanel leftPanel;

	/**
	 * Het rechter paneel bevat de GeschiedenisItem objecten
	 */
	private JPanel rightPanel;

	/**
	 * De TerugKnop, staat in buttonPanel
	 */
	private JButton btTerug;

	/**
	 * View variabele als MVC View
	 */
	private View view;

	/**
	 * ArrayList met DataHandler objecten
	 */
	private ArrayList<DataHandler> dataHandlers;

	/**
	 * ArrayList met NestedStatistiekPanel objecten
	 */
	private ArrayList<NestedStatistiekPanel> statPanels;

	/**
	 * Boolean bWorking houd bij of
	 */
	private boolean bWorking = false;

	/**
	 * Constructor
	 * 
	 * @param view
	 * @param controller
	 */
	public GeschiedenisPanel(View view, Treinbaan controller) {
		this.view = view;
		init();
		setup();
	}

	/**
	 * Voegt een gegeven NestedStatistiekPanel aan dit scherm toe
	 * 
	 * @param statPanel
	 */
	public void addStats(NestedStatistiekPanel statPanel) {
		if (statPanels.size() > 0) {

			statPanel.setNext(null);

			statPanels.get(statPanels.size() - 1).setNext(statPanel);
			statPanels.add(statPanel);

			statPanel.getLeftComponent().addComponentListener(this);
		} else {
			statPanels.add(statPanel);
			leftPanel.add(statPanel);
			statPanel.getLeftComponent().addComponentListener(this);
			leftPanel.repaint();
		}
		resizeLeft();
		fixScrollPane();
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	/**
	 * Het verschil in formaat word opgehaalt en toegepast op het eerste paneel.
	 * 
	 * @param e
	 * 
	 */
	public void componentResized(ComponentEvent e) {
		if (!bWorking) {
			bWorking = true;

			Object o = e.getSource();
			if (o instanceof LeftPanel) {
				LeftPanel lp = (LeftPanel) o;
				for (NestedStatistiekPanel sp : statPanels) {
					if (sp.getLeftComponent().equals(lp)) {
						statPanels.get(0).changePreferredWidth(lp.getSizeDiff());
					}
				}
			}
			fixScrollPane();

			bWorking = false;
		}
	}

	public void componentShown(ComponentEvent e) {
	}

	/**
	 * Methode om te zorgen dat de JScrollPane update
	 * 
	 */
	public void fixScrollPane() {
		leftScrollPane.invalidate();
		leftScrollPane.validate();
	}

	/**
	 * Initialisatie van de componenten
	 * 
	 */
	private void init() {
		statPanels = new ArrayList<NestedStatistiekPanel>();

		setBorder(BorderFactory.createTitledBorder(Language.getString("GeschiedenisPanel.0"))); //$NON-NLS-1$
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

		centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.LINE_AXIS));

		buttonPanel = new JPanel();
		buttonPanel.setMaximumSize(new Dimension(9999, 50));
		buttonPanel.setPreferredSize(new Dimension(800, 50));
		buttonPanel.setMinimumSize(new Dimension(0, 50));

		leftScrollPane = new JScrollPane();
		leftScrollPane.setPreferredSize(new Dimension(600, 600));
		rightScrollPane = new JScrollPane();
		rightScrollPane.setPreferredSize(new Dimension(200, 600));
		rightScrollPane.setMaximumSize(new Dimension(200, 9999));
		rightScrollPane.setMinimumSize(new Dimension(200, 0));

		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.LINE_AXIS));

		rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));

		btTerug = new JButton(Language.getString("GeschiedenisPanel.1")); //$NON-NLS-1$
	}

	/**
	 * Leegt het linker en rechter paneel. Maakt vervolgens nieuwe
	 * GeschiedenisItems.
	 * 
	 */
	public void initDataHandlers() {
		leftPanel.removeAll();
		rightPanel.removeAll();
		for (DataHandler dh : dataHandlers) {
			GeschiedenisItem gi = new GeschiedenisItem(dh, this);
			rightPanel.add(gi);
		}
	}

	/**
	 * Verwijdert een gegeven NestedStatistiekPanel uit dit scherm
	 * 
	 * @param statPanel
	 */
	public void removeStats(NestedStatistiekPanel statPanel) {
		statPanel.getLeftComponent().removeComponentListener(this);

		@SuppressWarnings("unused")
		Dimension d;
		if (statPanel.getNext() instanceof NestedStatistiekPanel) {
			d = statPanel.getLeftComponent().getSize();
		} else {
			d = statPanel.getSize();
		}

		if (statPanels.get(0).equals(statPanel)) {
			leftPanel.removeAll();

			if (statPanel.getNext() instanceof NestedStatistiekPanel) {
				leftPanel.add(statPanel.getNext());

			}

			statPanel.setPreferredSize(new Dimension(400, 680));
			statPanel.setNext(null);
			statPanels.remove(0);
		} else {
			for (int i = 0; i < statPanels.size(); i++) {
				if (statPanels.get(i).getNext().equals(statPanel)) {
					statPanels.get(i).setNext(statPanels.get(i).getNext().getNext());

					statPanels.get(i + 1).setNext(null);
					statPanels.remove(i + 1);

					return;
				}
			}
		}
		resizeLeft();
		fixScrollPane();
	}

	/**
	 * Zorgt dat het eerste NestedStatistiekPanel de juiste afmetingen aanneemt
	 * 
	 */
	public void resizeLeft() {
		if (statPanels.size() > 0) {
			NestedStatistiekPanel nsp0 = statPanels.get(0);
			Dimension d = new Dimension(0, nsp0.getHeight());
			d.width += nsp0.getLeftComponent().getWidth();
			NestedStatistiekPanel nsp1 = nsp0.getNext();
			while (nsp1 instanceof NestedStatistiekPanel) {
				d.width += nsp1.getLeftComponent().getWidth();
				if (nsp1.getNext() == null) {
					d.width += nsp1.getRightComponent().getPreferredSize().getWidth();
				}
				nsp1 = nsp1.getNext();
			}
			nsp0.setPreferredSize(d);
		}
	}

	/**
	 * Setter voor de gebruikte DataHandlers
	 * 
	 * @param dataHandlers
	 */
	public void setDataHandlers(ArrayList<DataHandler> dataHandlers) {
		this.dataHandlers = dataHandlers;
	}

	/**
	 * Het samenvoegen van de componenten
	 * 
	 */
	private void setup() {
		add(contentPane);

		contentPane.add(centerPanel);
		contentPane.add(buttonPanel);

		centerPanel.add(leftScrollPane);
		centerPanel.add(rightScrollPane);

		leftScrollPane.getViewport().setView(leftPanel);
		rightScrollPane.getViewport().setView(rightPanel);

		buttonPanel.add(btTerug);
		btTerug.addActionListener(new TerugKnopHandler());
	}

}
