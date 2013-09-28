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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.tigam.railcab.language.Language;
import com.tigam.railcab.stats.SimulatieData;
import com.tigam.railcab.util.ReadFile;

/**
 * Het paneel dat opgeroepen wordt voordat er een nieuwe simulatie gestart kan
 * worden.
 * 
 * @author Michiel
 * 
 */
public class SetupPanel extends JPanel implements ItemListener, KeyListener, ListSelectionListener {

	/**
	 * Een Handler voor de Terug knop. Brengt je weer terug naar de simulatie.
	 * 
	 * @author Michiel
	 * 
	 */
	public class TerugKnopHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			view.showSimulatie();
		}
	}

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4243540788252083130L;

	/**
	 * Variabelen
	 */
	private View view;

	/**
	 * Begin simulatie knop
	 */
	private JButton btBegin;

	/**
	 * Terug knop
	 */
	private JButton btTerug;

	/**
	 * Het paneel dat de labels en invoer bevat
	 */
	private JPanel midden;

	/**
	 * Het paneel dat de begin en terug knop bevat
	 */
	private JPanel onder;

	/**
	 * Label omschrijving
	 */
	private JLabel lbOmschrijving;

	/**
	 * Label start tarief
	 */
	private JLabel lbStartTarief;

	/**
	 * Label prijs per baandeel
	 */
	private JLabel lbPrijsPerBaandeel;

	/**
	 * Label capaciteit
	 */
	private JLabel lbCapaciteit;

	/**
	 * Label kosten
	 */
	private JLabel lbKosten;

	/**
	 * Label koppelen
	 */
	private JLabel lbKoppelen;

	/**
	 * Label aantal railcabs
	 */
	private JLabel lbAantalRailCabs;

	/**
	 * Label domein live
	 */
	private JLabel lbDomeinLive;

	/**
	 * Invoerveld omschrijving
	 */
	private JTextField txOmschrijving;

	/**
	 * Invoerveld start tarief
	 */
	private JTextField txStartTarief;

	/**
	 * Invoerveld prijs per baandeel
	 */
	private JTextField txPrijsPerBaandeel;

	/**
	 * Invoerveld capaciteit
	 */
	private JTextField txCapaciteit;

	/**
	 * Invoerveld kosten
	 */
	private JTextField txKosten;

	/**
	 * Invoerveld aantal railcabs
	 */
	private JTextField txAantalRailCabs;

	/**
	 * Invoerveld domein live
	 */
	private JTextField txDomeinLive;

	/**
	 * CheckBox koppelen beinvloed bKoppelen
	 */
	private JCheckBox ckKoppelen;

	/**
	 * CheckBox oude simulatie data beinvloed bOudeSimData
	 */
	private JCheckBox ckOudeSimData;

	/**
	 * Boolean die bijhoudt of RailCabs gekoppeld mogen worden
	 */
	private boolean bKoppelen = false;

	/**
	 * Boolean die bijhoudt of er oude simulatie data wordt gebruikt
	 */
	private boolean bOudeSimData = false;

	/**
	 * Integer die de horizontale positie van de Labels bepaalt
	 */
	private int lbX;

	/**
	 * Integer die de horizontale positie van de Invoervelden bepaalt
	 */
	private int txX;

	/**
	 * Een lijntje om de invoervelden te verdelen
	 */
	private JSeparator spLine;

	/**
	 * Lijst die de voorgaande simulatie weergeeft
	 */
	private JList lsOudeSimData;

	/**
	 * JScrollPane om de JList te laten scrollen
	 */
	private JScrollPane scrollOudeSimData;

	/**
	 * Plaatje om aan te geven welke invoer niet juist is
	 */
	private Image imgError;

	/**
	 * Namen die niet meer voor mogen komen omdat ze al zijn gebruikt
	 */
	private ArrayList<String> forbiddenNames;

	/**
	 * Constructor
	 * 
	 * @param view
	 */
	public SetupPanel(View view) {
		forbiddenNames = new ArrayList<String>();

		this.view = view;
		init();
		setup();
	}

	/**
	 * @param view
	 * @param inst
	 */
	public SetupPanel(View view, Instelling inst) {
		setInstelling(inst);
		init();
		setup();
	}

	/**
	 * Getter voor de "Begin Simulatie" knop
	 * 
	 * @return JButton
	 */
	public JButton getBeginButton() {
		return btBegin;
	}

	/**
	 * Getter voor een Instelling object, gebaseerd op de ingevoerde waarden.
	 * 
	 * @return Instelling
	 */
	public Instelling getInstelling() {
		Instelling inst = new Instelling();

		String omschrijving = txOmschrijving.getText();
		if (omschrijving.equals(Language.getString("SetupPanel.20"))) { //$NON-NLS-1$
			JOptionPane
					.showMessageDialog(
							this,
							Language.getString("SetupPanel.21"), Language.getString("SetupPanel.22"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			return null;
		}
		inst.setOmschrijving(omschrijving);

		try {
			double startTarief = Double.parseDouble(txStartTarief.getText());
			inst.setStartTarief(startTarief);
		} catch (NumberFormatException nfe) {
			JOptionPane
					.showMessageDialog(
							this,
							Language.getString("SetupPanel.23"), Language.getString("SetupPanel.24"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			return null;
		}

		try {
			double prijsPerBaandeel = Double.parseDouble(txPrijsPerBaandeel.getText());
			inst.setPrijsPerBaandeel(prijsPerBaandeel);
		} catch (NumberFormatException nfe) {
			JOptionPane
					.showMessageDialog(
							this,
							Language.getString("SetupPanel.25"), Language.getString("SetupPanel.26"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			return null;
		}

		try {
			int capaciteit = Integer.parseInt(txCapaciteit.getText());
			inst.setCapaciteit(capaciteit);
		} catch (NumberFormatException nfe) {
			JOptionPane
					.showMessageDialog(
							this,
							Language.getString("SetupPanel.27"), Language.getString("SetupPanel.28"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			return null;
		}

		try {
			double kostenPerBaandeel = Double.parseDouble(txKosten.getText());
			inst.setKostenPerBaandeel(kostenPerBaandeel);
		} catch (NumberFormatException nfe) {
			JOptionPane
					.showMessageDialog(
							this,
							Language.getString("SetupPanel.29"), Language.getString("SetupPanel.30"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			return null;
		}

		inst.setKoppelen(bKoppelen);

		try {
			int aantalRailCabs = Integer.parseInt(txAantalRailCabs.getText());
			inst.setAantalRailCabs(aantalRailCabs);
		} catch (NumberFormatException nfe) {
			JOptionPane
					.showMessageDialog(
							this,
							Language.getString("SetupPanel.31"), Language.getString("SetupPanel.32"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			return null;
		}

		try {
			int maxItemsLiveGrafieken = Integer.parseInt(txDomeinLive.getText());
			inst.setMaxItemsLive(maxItemsLiveGrafieken);
		} catch (NumberFormatException nfe) {
			JOptionPane
					.showMessageDialog(
							this,
							"Voor de maximum items voor een live grafiek, dient u een geheel getal in te voeren", "Fout bij invoer", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			return null;
		}

		if (!bOudeSimData) {
			inst.setOudeSimData(false);
		} else {
			if ((SimulatieData) lsOudeSimData.getSelectedValue() == null) {
				JOptionPane
						.showMessageDialog(
								this,
								Language.getString("SetupPanel.33"), Language.getString("SetupPanel.34"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
				return null;
			} else {
				inst.setSimData((SimulatieData) lsOudeSimData.getSelectedValue());
				inst.setOudeSimData(true);
			}
		}

		return inst;
	}

	/**
	 * Initiatie van de componenten
	 * 
	 */
	private void init() {
		loadErrorImage();

		lbX = 50;
		txX = 250;

		setLayout(new BorderLayout());

		midden = new JPanel();
		midden.setBorder(BorderFactory.createTitledBorder(Language.getString("SetupPanel.2"))); //$NON-NLS-1$
		midden.setLayout(null);
		midden.setPreferredSize(new Dimension(300, 200));
		midden.setMaximumSize(new Dimension(400, 300));

		onder = new JPanel();
		onder.setPreferredSize(new Dimension(800, 50));

		lbOmschrijving = new JLabel(Language.getString("SetupPanel.3")); //$NON-NLS-1$
		lbOmschrijving.setBounds(lbX, 60, 200, 18);

		lbStartTarief = new JLabel(Language.getString("SetupPanel.4")); //$NON-NLS-1$
		lbStartTarief.setBounds(lbX, 80, 200, 18);

		lbPrijsPerBaandeel = new JLabel(Language.getString("SetupPanel.5")); //$NON-NLS-1$
		lbPrijsPerBaandeel.setBounds(lbX, 100, 200, 18);

		lbCapaciteit = new JLabel(Language.getString("SetupPanel.6")); //$NON-NLS-1$
		lbCapaciteit.setBounds(lbX, 120, 200, 18);

		lbKosten = new JLabel(Language.getString("SetupPanel.7")); //$NON-NLS-1$
		lbKosten.setBounds(lbX, 140, 200, 18);

		lbKoppelen = new JLabel(Language.getString("SetupPanel.8")); //$NON-NLS-1$
		lbKoppelen.setBounds(lbX, 160, 200, 18);

		lbAantalRailCabs = new JLabel(Language.getString("SetupPanel.9")); //$NON-NLS-1$
		lbAantalRailCabs.setBounds(lbX, 180, 200, 18);

		//TODO: language string maken
		lbDomeinLive = new JLabel("Domein live grafieken");
		lbDomeinLive.setBounds(lbX, 200, 200, 18);

		txOmschrijving = new JTextField(Language.getString("SetupPanel.10")); //$NON-NLS-1$
		txOmschrijving.setBounds(txX, 60, 150, 18);

		txStartTarief = new JTextField(Language.getString("SetupPanel.11")); //$NON-NLS-1$
		txStartTarief.setBounds(txX, 80, 50, 18);

		txPrijsPerBaandeel = new JTextField(Language.getString("SetupPanel.12")); //$NON-NLS-1$
		txPrijsPerBaandeel.setBounds(txX, 100, 50, 18);

		txCapaciteit = new JTextField(Language.getString("SetupPanel.13")); //$NON-NLS-1$
		txCapaciteit.setBounds(txX, 120, 50, 18);

		txKosten = new JTextField(Language.getString("SetupPanel.14")); //$NON-NLS-1$
		txKosten.setBounds(txX, 140, 50, 18);

		ckKoppelen = new JCheckBox();
		ckKoppelen.setBounds(txX, 160, 50, 18);

		txAantalRailCabs = new JTextField(Language.getString("SetupPanel.15")); //$NON-NLS-1$
		txAantalRailCabs.setBounds(txX, 180, 50, 18);

		//TODO: language string
		txDomeinLive = new JTextField("1000");
		txDomeinLive.setBounds(txX, 200, 50, 18);

		spLine = new JSeparator(SwingConstants.HORIZONTAL);
		spLine.setBounds(lbX, 210, txX - lbX, 8);

		ckOudeSimData = new JCheckBox(Language.getString("SetupPanel.16")); //$NON-NLS-1$
		ckOudeSimData.setBounds(lbX, 220, 218, 18);
		ckOudeSimData.setToolTipText(Language.getString("SetupPanel.17")); //$NON-NLS-1$

		scrollOudeSimData = new JScrollPane();
		scrollOudeSimData.setBounds(lbX, 240, txX - lbX, txX - lbX);

		lsOudeSimData = new JList();
		lsOudeSimData.setEnabled(false);
		lsOudeSimData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lsOudeSimData.addListSelectionListener(this);

		scrollOudeSimData.getViewport().setView(lsOudeSimData);

		btBegin = new JButton(Language.getString("SetupPanel.18")); //$NON-NLS-1$
		btTerug = new JButton(Language.getString("SetupPanel.19")); //$NON-NLS-1$
	}

	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();

		if (source == ckKoppelen) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				bKoppelen = false;
			} else {
				bKoppelen = true;
			}
		} else if (source == ckOudeSimData) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				bOudeSimData = false;
			} else {
				bOudeSimData = true;
			}
			lsOudeSimData.setEnabled(bOudeSimData);
		}
	}

	public void keyPressed(KeyEvent arg0) {
	}

	public void keyReleased(KeyEvent arg0) {
		repaint();
	}

	public void keyTyped(KeyEvent arg0) {
	}

	/**
	 * Laad een plaatje van een uitroepteken in
	 */
	private void loadErrorImage() {
		InputStream in = ReadFile.read(Language.getString("SetupPanel.0")); //$NON-NLS-1$
		try {
			if (in != null) {
				imgError = ImageIO.read(in);
			}
			in.close();
		} catch (Exception ex) {
			imgError = null;
		}
	}

	/**
	 * Maakt een nieuwe, unieke omschrijving op basis van een voorgaande
	 * 
	 * @param naam
	 * @return Sttring
	 */
	private String nieweOmschrijving(String naam) {
		if (!forbiddenNames.contains(naam)) {
			return naam;
		}
		int space = 0;
		int number = 1;
		while (naam.indexOf(' ', space + 1) > 0) {
			space = naam.indexOf(' ', space + 1);
		}

		String sNumber = naam.substring(space + 1);

		try {
			number = Integer.parseInt(sNumber) + 1;
			naam = naam.substring(0, space);
		} catch (NumberFormatException nfe) {
		}

		naam = naam + Language.getString("SetupPanel.35") + number; //$NON-NLS-1$
		if (forbiddenNames.contains(naam)) {
			return nieweOmschrijving(naam);
		}
		return naam;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		validateField(txOmschrijving, g);

		validateField(txStartTarief, g);
		validateField(txPrijsPerBaandeel, g);
		validateField(txKosten, g);

		validateField(txCapaciteit, g);
		validateField(txAantalRailCabs, g);
	}

	/**
	 * @param inst
	 */
	public void setInstelling(Instelling inst) {

	}

	/**
	 * Samenvoegen van de componenten
	 * 
	 */
	private void setup() {
		add(midden, BorderLayout.CENTER);
		add(onder, BorderLayout.SOUTH);

		onder.add(btBegin);
		onder.add(btTerug);

		midden.add(lbOmschrijving);
		midden.add(lbStartTarief);
		midden.add(lbPrijsPerBaandeel);
		midden.add(lbCapaciteit);
		midden.add(lbKosten);
		midden.add(lbKoppelen);
		midden.add(lbAantalRailCabs);

		midden.add(txOmschrijving);
		midden.add(txStartTarief);
		midden.add(txPrijsPerBaandeel);
		midden.add(txCapaciteit);
		midden.add(txKosten);
		midden.add(ckKoppelen);
		midden.add(txAantalRailCabs);

		//midden.add(spLine);
		midden.add(lbDomeinLive);
		midden.add(txDomeinLive);

		midden.add(ckOudeSimData);
		midden.add(scrollOudeSimData);

		ckKoppelen.addItemListener(this);
		ckOudeSimData.addItemListener(this);

		txOmschrijving.addKeyListener(this);
		txStartTarief.addKeyListener(this);
		txPrijsPerBaandeel.addKeyListener(this);
		txCapaciteit.addKeyListener(this);
		txKosten.addKeyListener(this);
		txAantalRailCabs.addKeyListener(this);

		btTerug.addActionListener(new TerugKnopHandler());
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);

		if (b) { // tiggered when he is (re)shown
			ArrayList<SimulatieData> simdata = view.getSimDataController().getOudeSimulaties();
			lsOudeSimData.removeAll();
			lsOudeSimData.setListData(simdata.toArray());

			forbiddenNames = new ArrayList<String>();
			for (int i = 0; i < simdata.size(); i++) {
				forbiddenNames.add(simdata.get(i).toString());
			}
			txOmschrijving.setText(nieweOmschrijving(txOmschrijving.getText()));
		}
	}

	/**
	 * Controleert of de invoer van een gegeven JTextField juist is.
	 * 
	 * @param o JTextField dat je wil controleren
	 * @return true - de ingevulde waarde is juist false - de ingevulde waarde
	 *         is niet juist
	 */
	public boolean validateField(JTextField o) {
		if (o == txOmschrijving && !o.getText().equals(Language.getString("SetupPanel.1"))) { //$NON-NLS-1$
			for (int i = 0; i < forbiddenNames.size(); i++) {
				if (forbiddenNames.get(i).equals(o.getText())) {
					return false;
				}
			}
			return true;
		}
		if (o == txStartTarief) {
			try {
				Double.parseDouble(txStartTarief.getText());
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		if (o == txPrijsPerBaandeel) {
			try {
				Double.parseDouble(txPrijsPerBaandeel.getText());
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		if (o == txKosten) {
			try {
				Double.parseDouble(txKosten.getText());
				return true;
			} catch (Exception e) {
				return false;
			}
		}

		if (o == txCapaciteit) {
			try {
				Integer.parseInt(txCapaciteit.getText());
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		if (o == txAantalRailCabs) {
			try {
				Integer.parseInt(txAantalRailCabs.getText());
				return true;
			} catch (Exception e) {
				return false;
			}
		}

		return false;
	}

	/**
	 * Controleert een JTextField op zijn juiste invoer, als die niet juist is
	 * tekent hij imgError op het JPanel, net voor het JTextField
	 * 
	 * @param tx
	 * @param g
	 */
	public void validateField(JTextField tx, Graphics g) {
		if (imgError == null) {
			return;
		}
		if (!validateField(tx)) {
			g.drawImage(imgError, tx.getBounds().x - imgError.getWidth(null), tx.getBounds().y
					+ (tx.getBounds().height - imgError.getHeight(null)) / 2, null);
		}
	}

	public void valueChanged(ListSelectionEvent e) {

		if (e.getSource() == lsOudeSimData) {
			SimulatieData d = (SimulatieData) lsOudeSimData.getSelectedValue();
			if (d != null) {
				Instelling i = d.getInstelling();

				txOmschrijving.setText(nieweOmschrijving(i.getOmschrijving()));
				txStartTarief.setText(Language.getString("SetupPanel.36") + i.getStartTarief()); //$NON-NLS-1$
				txPrijsPerBaandeel.setText(Language.getString("SetupPanel.37") + i.getPrijsPerBaandeel()); //$NON-NLS-1$
				txCapaciteit.setText(Language.getString("SetupPanel.38") + i.getCapaciteit()); //$NON-NLS-1$

				txKosten.setText(Language.getString("SetupPanel.39") + i.getKostenPerBaandeel()); //$NON-NLS-1$
				txAantalRailCabs.setText(Language.getString("SetupPanel.40") + i.getAantalRailCabs()); //$NON-NLS-1$

				ckKoppelen.setSelected(i.isKoppelen());
				bKoppelen = i.isKoppelen();

				repaint();
			}
		}

	}

}
