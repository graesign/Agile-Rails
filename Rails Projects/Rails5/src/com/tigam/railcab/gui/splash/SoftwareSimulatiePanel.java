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

package com.tigam.railcab.gui.splash;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tigam.railcab.controller.Treinbaan;
import com.tigam.railcab.gui.Kleuren;
import com.tigam.railcab.gui.editor.Editor;
import com.tigam.railcab.gui.openbaan.BaanFileChooser;
import com.tigam.railcab.gui.openbaan.BaanPreview;
import com.tigam.railcab.gui.view.View;
import com.tigam.railcab.language.Lang;
import com.tigam.railcab.language.Language;
import com.tigam.railcab.language.LanguageListener;
import com.tigam.railcab.model.baan.BaanHeader;
import com.tigam.railcab.model.exception.RailCabException;
import com.tigam.railcab.model.simulatie.Simulatie;
import com.tigam.railcab.schedulers.GlobalFIFOScheduler;
import com.tigam.railcab.simulaties.SoftwareSimulatie;
import com.tigam.railcab.util.BaanLoader;
import com.tigam.railcab.util.ReadFile;

/**
 * 
 * Panel voor het instellen van de software simulatie
 * 
 * @author Nils Dijk
 * 
 */
public class SoftwareSimulatiePanel extends JPanel implements LanguageListener {

	/**
	 * 
	 * @author Michiel van den Anker
	 * 
	 */
	class EditorHandler implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			splash.setVisible(false);
			Editor myEditor = new Editor(baanHeader);
			myEditor.addWindowListener(new EditorWindowHandler());
		}
	}

	/**
	 * Voor het reageren op window events van de editor
	 * 
	 * @author Nils Dijk
	 * 
	 */
	class EditorWindowHandler extends WindowAdapter {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
		 */
		@Override
		public void windowClosing(WindowEvent winEvt) {
			splash.setVisible(true);
		}
	}

	/**
	 * 
	 * @author Nils Dijk
	 * 
	 */
	class FileHandler implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			baanHeader = BaanFileChooser.laadBaan(baanHeader);
			baanPreview.setLoadedBaan(baanHeader);
		}
	}

	/**
	 * 
	 * @author Michiel van den Anker
	 * 
	 */
	class StartHandler implements ActionListener {
		/**
		 * De controller als in MVC
		 */
		private Treinbaan controller;

		/**
		 * Simulatie behavior - delegeren...
		 */
		private Simulatie simulatie;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			try {
				splash.setVisible(false);

				simulatie = new SoftwareSimulatie(baanHeader);
				controller = new Treinbaan(simulatie);
				new GlobalFIFOScheduler(controller);
				new View(controller, splash);
			} catch (RailCabException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @author Nils Dijk
	 * 
	 */
	class TaalHandler implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			Language.setLanguage(((Lang) cbTaal.getSelectedItem()));
		}
	}

	/**
	 * Serializable...
	 */
	private static final long serialVersionUID = 5306795332052885363L;

	/**
	 * Combo box voor het selecteren van de taal
	 */
	private JComboBox cbTaal;

	/**
	 * Label Taal (kolom 1)
	 */
	private JLabel lbTaal;

	/**
	 * Label Baan (kolom 2)
	 */
	private JLabel lbBaan;

	/**
	 * Label start (kolom3)
	 */
	private JLabel lbStart;

	/**
	 * Start knop (kolom 3)
	 */
	private JButton btStart;

	/**
	 * andere baan knop (kolom 2)
	 */
	private JButton btFile;

	/**
	 * Editor knop (kolom 3)
	 */
	private JButton btEditor;

	/**
	 * Een BaanPreview Panel.
	 */
	private BaanPreview baanPreview;

	/**
	 * De gekozen baan.
	 */
	private BaanHeader baanHeader;

	/**
	 * Referentie naar het splash scherm
	 */
	private SplashScreen splash;

	/**
	 * Maak een Software simulatie paneel aan
	 * 
	 * @param splash referentie naar het splashscreen waar hij op moet komen
	 */
	public SoftwareSimulatiePanel(SplashScreen splash) {
		super();

		this.splash = splash;

		setLayout(null);
		//this.setBackground(Color.WHITE);
		setBackground(Kleuren.SplashBackground);
		setPreferredSize(new Dimension(600, 220));

		init();
		loadTaal();
		setup();
	}

	/**
	 * Maak alle Gui objecten aan
	 */
	private void init() {
		// De taal keuze kolom
		lbTaal = new JLabel(Language.getString("SplashScreen.0"));
		lbTaal.setBounds(25, 10, 150, 20);

		cbTaal = new JComboBox();
		cbTaal.setBounds(25, 30, 150, 18);
		cbTaal.setBackground(Kleuren.SplashBackground);

		// De baan kolom
		lbBaan = new JLabel();
		lbBaan.setBounds(225, 10, 150, 20);

		baanPreview = new BaanPreview();
		baanPreview.setBounds(225, 35, 150, 150);
		baanPreview.setBackground(Kleuren.SplashBackground);
		baanHeader = BaanLoader.loadBaan(ReadFile.read("resources/default.baan"));
		baanPreview.setLoadedBaan(baanHeader);
		baanPreview.setShowName(false);

		btFile = new JButton();
		btFile.setBounds(225, 190, 150, 20);

		// De start kolom
		lbStart = new JLabel();
		lbStart.setBounds(425, 10, 150, 20);

		btStart = new JButton();
		btStart.setBounds(425, 35, 150, 30);

		btEditor = new JButton();
		btEditor.setBounds(425, 75, 150, 20);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tigam.railcab.language.LanguageListener#LanguageChanged()
	 */
	public void LanguageChanged() {
		loadTaal();
	}

	/**
	 * Methode voor het laden van de taal in de gui. Alle Labels en knoppen
	 * krijgen de tekst in de gekozen taal
	 */
	private void loadTaal() {
		// de taal kolom
		lbTaal.setText(Language.getString("SplashScreen.0"));

		while (cbTaal.getItemCount() > 0) {
			cbTaal.removeItemAt(0);
		}
		Lang langs[] = Language.getLanguages();
		for (int i = 0; i < langs.length; i++) {
			cbTaal.addItem(langs[i]);
			if (langs[i].equals(Language.getCurrentLang())) {
				cbTaal.setSelectedItem(langs[i]);
			}
		}

		// de baan kolom
		lbBaan.setText(Language.getString("SplashScreen.1"));
		btFile.setText(Language.getString("SplashScreen.2"));

		// de start kolom
		lbStart.setText(Language.getString("SplashScreen.3"));
		btStart.setText("RailCab SIMULATOR");
		btEditor.setText("Editor");
	}

	/**
	 * Plak alle gui objecten op het Panel
	 */
	private void setup() {
		add(lbTaal);
		add(cbTaal);
		add(lbBaan);
		add(baanPreview);
		add(btFile);
		add(lbStart);
		add(btStart);
		add(btEditor);

		cbTaal.addActionListener(new TaalHandler());
		btFile.addActionListener(new FileHandler());
		btStart.addActionListener(new StartHandler());
		btEditor.addActionListener(new EditorHandler());

		Language.addLanguageListener(this);
	}
}
