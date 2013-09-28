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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import com.tigam.railcab.controller.SimulatieDataController;
import com.tigam.railcab.controller.Treinbaan;
import com.tigam.railcab.gui.baan.BaanPanel;
import com.tigam.railcab.gui.splash.SplashScreen;
import com.tigam.railcab.language.Language;
import com.tigam.railcab.model.baan.Groep;
import com.tigam.railcab.model.baan.Station;
import com.tigam.railcab.model.simulatie.Simulatie;
import com.tigam.railcab.stats.DataHandler;
import com.tigam.railcab.stats.SimulatieData;
import com.tigam.railcab.util.ReadFile;

/**
 * Dit is de hoofdklasse van de View in de MVC architectuur. Het simulatiepaneel
 * zit eveneens verweven met deze klasse.
 * 
 * @author Michiel
 * 
 */
public class View extends JFrame {

	/**
	 * De "Begin Simulatie" knop in het setupPanel Vanaf hier wordt de simulatie
	 * echt gestart
	 * 
	 * @author Michiel
	 * 
	 */
	class BeginSimulatieHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Instelling inst = setupPanel.getInstelling();
			if (inst != null) {
				try {
					controller.resetSimulatie(inst);
				} catch (Exception ex) {
					System.out.println(ex);
				}
				rechterPanel.setDataHandler(simDataController.maakNieuweSimulatie(inst));
				rechterPanel.initGrafieken();

				controller.startSimulatie();
				showSimulatie();

				controlePanel.getStartKnop().setEnabled(false);
				controlePanel.getPauzeKnop().setEnabled(true);
				controlePanel.getStopKnop().setEnabled(true);
				controlePanel.getChaosKnop().setEnabled(true);
			}

		}
	}

	/**
	 * De Chaos knop, voor het random toevoegen van wachtende op een station :)
	 * 
	 * @author Nils Dijk
	 * 
	 */
	class ChaosHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int s1, s2, reizigers;
			ArrayList<Station> stations = controller.getStations();

			for (int i = 0; i < 100; i++) {
				s1 = (int) (Math.random() * stations.size());
				do {
					s2 = (int) (Math.random() * stations.size());
				} while (s1 == s2);
				reizigers = (int) (Math.random() * 10) + 1;
				stations.get(s1).addGroep(new Groep(stations.get(s1), stations.get(s2), reizigers));
			}

		}
	}

	/**
	 * Een Handler voor de GeschiedenisKnop in dit scherm.
	 * 
	 * @author Michiel
	 * 
	 */
	class GeschiedenisHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int status = controller.getSimulatieStatus();
			if (status == Simulatie.STATUS_PAUSE || status == Simulatie.STATUS_RUNNING) {
				int response = JOptionPane.showConfirmDialog(View.this, Language.getString("View.11"), //$NON-NLS-1$
						Language.getString("View.12"), //$NON-NLS-1$
						JOptionPane.OK_CANCEL_OPTION);
				if (response == JOptionPane.OK_OPTION) {
					try {
						controller.stopSimulatie();
						simDataController.huidigeSimulatieOpslaan();
						showGeschiedenis();

						controlePanel.getStartKnop().setEnabled(true);
						controlePanel.getPauzeKnop().setText(Language.getString("View.13")); //$NON-NLS-1$
						controlePanel.getPauzeKnop().setEnabled(false);
						controlePanel.getStopKnop().setEnabled(false);
						controlePanel.getChaosKnop().setEnabled(false);
					} catch (Exception ex) {
						System.out.println(ex.toString());
						System.exit(0);
					}
				}
			} else {
				showGeschiedenis();
			}
		}
	}

	/**
	 * Een Handler voor de PAUZE knop op het controlePanel
	 * 
	 * @author Michiel
	 * 
	 */
	class PauzeerSimulatieHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (controller.getSimulatieStatus() == Simulatie.STATUS_PAUSE) {
				controller.startSimulatie();

				controlePanel.getPauzeKnop().setText(Language.getString("View.9")); //$NON-NLS-1$
			} else if (controller.getSimulatieStatus() == Simulatie.STATUS_RUNNING) {
				controller.pauseSimulatie();

				controlePanel.getPauzeKnop().setText(Language.getString("View.10")); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Een Handler voor de START knop op het controlePanel
	 * 
	 * @author Michiel
	 */
	class StartSimulatieHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			showSetup();

		}
	}

	/**
	 * Een Handler voor de STOP knop op het controlePanel
	 * 
	 * @author Michiel
	 * 
	 */
	class StopSimulatieHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (controller.getSimulatieStatus() == Simulatie.STATUS_STOP) {
				JOptionPane.showMessageDialog(View.this, Language.getString("View.4"), //$NON-NLS-1$
						Language.getString("View.5"), //$NON-NLS-1$
						JOptionPane.WARNING_MESSAGE);
				return;
			} else {
				int response = JOptionPane.showConfirmDialog(View.this, Language.getString("View.6"), //$NON-NLS-1$
						Language.getString("View.7"), //$NON-NLS-1$
						JOptionPane.OK_CANCEL_OPTION);
				if (response == JOptionPane.OK_OPTION) {
					try {
						controller.stopSimulatie();
						simDataController.huidigeSimulatieOpslaan();

						controlePanel.getStartKnop().setEnabled(true);
						controlePanel.getPauzeKnop().setText(Language.getString("View.8")); //$NON-NLS-1$
						controlePanel.getPauzeKnop().setEnabled(false);
						controlePanel.getStopKnop().setEnabled(false);
						controlePanel.getChaosKnop().setEnabled(false);
					} catch (Exception ex) {
						System.out.println(ex.toString());
						System.exit(0);
					}
				}
			}
		}
	}

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1203502723390835657L;

	/**
	 * De MVC controller
	 */
	private Treinbaan controller;

	/**
	 * Koppeling met de huidige en voorgaande simulatiedata
	 */
	private SimulatieDataController simDataController;

	/**
	 * Een contentPane
	 */
	private JPanel contentPane;

	/**
	 * simulatiePanel is het zicht op de simulatie
	 */
	private JPanel simulatiePanel;

	/**
	 * setupPanel laat de gebruiker de simulatie instellen
	 */
	private SetupPanel setupPanel;

	/**
	 * geschiedenisPanel laat de statistieken van voorgaande simulaties zien
	 */
	private GeschiedenisPanel geschiedenisPanel;

	/**
	 * ControlePanel bevat de belangrijkste knoppen om de simulatie aan te
	 * sturent
	 */
	private ControlePanel controlePanel;

	/**
	 * De onderste helft van het scherm
	 */
	private JPanel splitPanelContainer;

	/**
	 * De SplitPane die statistieken en baan verdeelt
	 */
	private JSplitPane splitPanel;

	/**
	 * Linkerpanel bevat de baan en de stationsinformatie
	 */
	private JPanel linkerPanel;

	/**
	 * Rechterpanel bevat de huidige statistieken
	 */
	private StatistiekPanel rechterPanel;

	/**
	 * De baan
	 */
	private BaanPanel baanPanel;

	/**
	 * Bevat alles om stations te manipuleren
	 */
	private StationPanel stationPanel;

	/**
	 * Bevat een referentie naar het SplashScreen
	 */
	private SplashScreen splash;

	/**
	 * Constructor
	 * 
	 * @param controller
	 * @param splash het SplashScreen
	 */
	public View(Treinbaan controller, SplashScreen splash) {
		this.controller = controller;
		this.splash = splash;
		simDataController = new SimulatieDataController(controller);

		loadIcon();

		init();
		setup();
		deploy();
	}

	/**
	 * Brengt het JFrame in gereedheid
	 */
	public void deploy() {
		setTitle(Language.getString("View.1")); //$NON-NLS-1$
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent winEvt) {
				int response = JOptionPane.showConfirmDialog(View.this, Language.getString("View.2"), //$NON-NLS-1$
						Language.getString("View.3"), //$NON-NLS-1$
						JOptionPane.OK_CANCEL_OPTION);
				if (response == JOptionPane.OK_OPTION) {
					try {
						controller.stopSimulatie();
					} catch (Exception ex) {
						System.out.println(ex);
					}
					splash.setVisible(true);
					setVisible(false);
					baanPanel.getRenderThread().kill();
				}
			}
		});

		pack();
		setSize(1024, 786);

		//Dit zet het frame mooi in het midden 
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension framesize = getSize();
		int left = (int) ((screensize.getWidth() - framesize.getWidth()) / 2.0);
		int up = (int) ((screensize.getHeight() - framesize.getHeight()) / 2.0);
		setLocation(left, up);

		showSimulatie();
		setVisible(true);
	}

	/**
	 * Getter voor de Treinbaan controller variabele van dit object
	 * 
	 * @return Treinbaan
	 */
	public Treinbaan getController() {
		return controller;
	}

	/**
	 * @return SimulatieDataController
	 */
	public SimulatieDataController getSimDataController() {
		return simDataController;
	}

	/**
	 * init() initialiseert de componenten
	 * 
	 */
	public void init() {

		contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.LINE_AXIS));

		simulatiePanel = new JPanel();
		simulatiePanel.setLayout(new BoxLayout(simulatiePanel, BoxLayout.PAGE_AXIS));
		simulatiePanel.setPreferredSize(new Dimension(500, 600));
		simulatiePanel.setMaximumSize(new Dimension(9999, 9999));
		simulatiePanel.setMinimumSize(new Dimension(0, 0));

		setupPanel = new SetupPanel(this);

		geschiedenisPanel = new GeschiedenisPanel(this, controller);

		splitPanelContainer = new JPanel();
		splitPanelContainer.setLayout(new BoxLayout(splitPanelContainer, BoxLayout.LINE_AXIS));

		splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPanel.setPreferredSize(new Dimension(500, 500));
		splitPanel.setMaximumSize(new Dimension(9999, 9999));
		splitPanel.setMinimumSize(new Dimension(0, 0));
		splitPanel.setResizeWeight(0.7);
		splitPanel.setDividerLocation(700);

		linkerPanel = new JPanel();
		linkerPanel.setLayout(new BoxLayout(linkerPanel, BoxLayout.PAGE_AXIS));
		linkerPanel.setPreferredSize(new Dimension(500, 600));

		rechterPanel = new StatistiekPanel();

		controlePanel = new ControlePanel(controller);

		baanPanel = new BaanPanel(controller);
		//dit doet niks maar wrom?
		baanPanel.setBorder(BorderFactory.createTitledBorder(Language.getString("View.0"))); //$NON-NLS-1$
		baanPanel.setPreferredSize(new Dimension(500, 400));

		stationPanel = new StationPanel(controller.getStations());

		// observer voor knoppen op panels...
		controller.addObserver(stationPanel.getVoegToePanel());
		controller.addObserver(stationPanel.getInfoPanel());

	}

	/**
	 * Laad het mooie baan icoontje voor de simulator
	 */
	private void loadIcon() {
		InputStream in = ReadFile.read("resources/baan_icon_16.png");

		try {
			if (in != null) {
				setIconImage(ImageIO.read(in));
			}
		} catch (Exception ex) {
		}

		try {
			in.close();
		} catch (Exception ex) {
		}
	}

	/**
	 * Zet alle componenten in elkaar
	 * 
	 */
	public void setup() {
		//In elkaar klikken van alle containers
		setContentPane(contentPane);

		contentPane.add(simulatiePanel);
		contentPane.add(setupPanel);
		contentPane.add(geschiedenisPanel);

		simulatiePanel.add(controlePanel);
		simulatiePanel.add(splitPanelContainer);

		splitPanelContainer.add(splitPanel);

		splitPanel.setLeftComponent(linkerPanel);
		splitPanel.setRightComponent(rechterPanel);

		linkerPanel.add(baanPanel);
		linkerPanel.add(stationPanel);

		baanPanel.addStationObserver(stationPanel);

		//Setup van den knoppen
		controlePanel.getStartKnop().addActionListener(new StartSimulatieHandler());
		controlePanel.getPauzeKnop().addActionListener(new PauzeerSimulatieHandler());
		controlePanel.getPauzeKnop().setEnabled(false);
		controlePanel.getStopKnop().addActionListener(new StopSimulatieHandler());
		controlePanel.getStopKnop().setEnabled(false);
		controlePanel.getChaosKnop().addActionListener(new ChaosHandler());
		controlePanel.getChaosKnop().setEnabled(false);
		controlePanel.getGeschiedenisKnop().addActionListener(new GeschiedenisHandler());

		setupPanel.getBeginButton().addActionListener(new BeginSimulatieHandler());
	}

	/**
	 * Laat het geschiedenis paneel zien en verbergt de rest
	 * 
	 */
	public void showGeschiedenis() {
		ArrayList<SimulatieData> simData = simDataController.getOudeSimulaties();
		ArrayList<DataHandler> dataHandlers = new ArrayList<DataHandler>();
		for (SimulatieData sd : simData) {
			DataHandler dh = new DataHandler(controller.getSimulatie());
			dh.setSimulatieData(sd);
			dataHandlers.add(dh);
		}
		geschiedenisPanel.setDataHandlers(dataHandlers);
		geschiedenisPanel.initDataHandlers();

		geschiedenisPanel.setVisible(true);
		setupPanel.setVisible(false);
		simulatiePanel.setVisible(false);
		baanPanel.setVisible(false);
	}

	/**
	 * Laat het setup scherm zien en verbergt de rest
	 * 
	 */
	public void showSetup() {
		setupPanel.setVisible(true);
		simulatiePanel.setVisible(false);
		baanPanel.setVisible(false);
		geschiedenisPanel.setVisible(false);
	}

	/**
	 * Laat de simulatie zien en verbergt de rest
	 * 
	 */
	public void showSimulatie() {
		simulatiePanel.setVisible(true);
		baanPanel.setVisible(true);
		setupPanel.setVisible(false);
		geschiedenisPanel.setVisible(false);
	}

}
