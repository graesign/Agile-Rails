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
package com.tigam.railcab.gui.editor.tools;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.tigam.railcab.gui.editor.baan.BaanPanel;
import com.tigam.railcab.gui.editor.model.Baan;
import com.tigam.railcab.language.Language;

/**
 * @author Nils Dijk
 * 
 */
public class SettingsPane extends Tool {

	/**
	 * Handler voor de direction button
	 * 
	 * @author Nils Dijk
	 * 
	 */
	class DirectionHandler implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			editor.setShowRijRichting(!editor.getShowRijRichting());
			LanguageChanged();
			editor.requestFocus();
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1645026695312702053L;

	/**
	 * De richting knop
	 */
	private JButton btDirection;

	/**
	 * @param baan
	 * @param editor
	 */
	public SettingsPane(Baan baan, BaanPanel editor) {
		super(baan, editor);

		init();
		setup();

		setBorderTextLanguage("Editor.Settings.0");
		LanguageChanged();
	}

	/**
	 * het maken van alle knoppen op het paneel
	 */
	private void init() {
		btDirection = new JButton();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tigam.railcab.language.LanguageListener#LanguageChanged()
	 */
	@Override
	public void LanguageChanged() {
		super.LanguageChanged();
		if (btDirection != null) {
			if (editor.getShowRijRichting()) {
				btDirection.setText(Language.getString("Editor.Settings.2"));
			} else {
				btDirection.setText(Language.getString("Editor.Settings.1"));
			}
		}
	}

	/**
	 * Alle knoppen op het paneel plaatsen
	 */
	private void setup() {
		setLayout(new GridLayout(0, 1, 0, 7));

		add(btDirection);

		btDirection.addActionListener(new DirectionHandler());
	}

}
