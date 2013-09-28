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

/**
 * 
 */
package com.tigam.railcab.gui.editor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.tigam.railcab.gui.editor.model.Baan;
import com.tigam.railcab.language.Language;
import com.tigam.railcab.language.LanguageListener;

/**
 * @author Nils Dijk
 * 
 */
public class NamePanel extends JPanel implements LanguageListener {

	/**
	 * Voor het afhandelen van veranderingen in de naam
	 * 
	 * @author Nils Dijk
	 * 
	 */
	class NaamHandler implements KeyListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
		 */
		public void keyPressed(KeyEvent arg0) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
		 */
		public void keyReleased(KeyEvent arg0) {
			baan.setNaam(txNaam.getText());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
		 */
		public void keyTyped(KeyEvent arg0) {
		}

	}

	/**
	 * Serializable...
	 */
	private static final long serialVersionUID = -935637634251390527L;

	/**
	 * Label Naam
	 */
	private JLabel lbNaam;

	/**
	 * TextField Naam
	 */
	private JTextField txNaam;

	/**
	 * Baan controller
	 */
	private Baan baan;

	/**
	 * Maak het naam paneel aan met de Baan Controler
	 * 
	 * @param baan de controller
	 */
	public NamePanel(Baan baan) {
		this.baan = baan;

		init();
		setup();
		LanguageChanged();

		Language.addLanguageListener(this);

	}

	/**
	 * Initializeer alle elementen
	 */
	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		lbNaam = new JLabel();
		txNaam = new JTextField(baan.getNaam());
		//txNaam.addActionListener(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tigam.railcab.language.LanguageListener#LanguageChanged()
	 */
	public void LanguageChanged() {
		lbNaam.setText(Language.getString("Editor.NamePanel.0") + ": ");
	}

	/**
	 * zet de texte van het naam vakje
	 * 
	 * @param naam het vakje
	 */
	public void setNameText(String naam) {
		if (!txNaam.getText().equals(naam)) {
			txNaam.setText(naam);
		}
	}

	/**
	 * Plaats alle elementen op het Panel
	 */
	private void setup() {
		add(lbNaam);
		add(txNaam);

		txNaam.addKeyListener(new NaamHandler());
	}
}
