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
import javax.swing.JOptionPane;

import com.tigam.railcab.gui.editor.baan.BaanPanel;
import com.tigam.railcab.gui.editor.model.Baan;
import com.tigam.railcab.gui.openbaan.BaanFileChooser;
import com.tigam.railcab.language.Language;
import com.tigam.railcab.model.baan.BaanHeader;
import com.tigam.railcab.util.BaanLoader;

/**
 * @author Nils Dijk
 * 
 */
public class FilePane extends Tool {

	/**
	 * De handler van de Nieuw knop
	 * 
	 * @author Nils Dijk
	 * 
	 */
	class NewHandler implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			int option = JOptionPane
					.showConfirmDialog(
							null,
							Language.getString("Editor.File.5"), Language.getString("Editor.File.4"), JOptionPane.CANCEL_OPTION | JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			requestFocus();
			if (option == JOptionPane.CLOSED_OPTION || option == JOptionPane.CANCEL_OPTION) {
				return;
			}

			baan.loadBaan(null);
			editor.createGrid();
		}

	}

	/**
	 * Handler voor de open knop
	 * 
	 * @author Nils Dijk
	 * 
	 */
	class OpenHandler implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			int option = JOptionPane
					.showConfirmDialog(
							null,
							Language.getString("Editor.File.5"), Language.getString("Editor.File.4"), JOptionPane.CANCEL_OPTION | JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			requestFocus();
			if (option == JOptionPane.CLOSED_OPTION || option == JOptionPane.CANCEL_OPTION) {
				return;
			}

			BaanHeader bh = BaanFileChooser.laadBaan();
			if (bh != null) {
				baan.loadBaan(bh);
				editor.createGrid();
			}
		}

	}

	/**
	 * Handler voor de sla op knop
	 * 
	 * @author Nils Dijk
	 * 
	 */
	class SaveHandler implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			String file = BaanFileChooser.saveBaan();
			if (file != null) {
				BaanLoader.saveBaan(baan.getBaan(), baan.getNaam(), file);
			}
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3082974490511122980L;

	/**
	 * De open knop
	 */
	private JButton btOpen;

	/**
	 * De Nieuw Knop
	 */
	private JButton btNew;

	/**
	 * De Save Knop
	 */
	private JButton btSave;

	/**
	 * Maak een Bestands paneel aan
	 * 
	 * @param baan
	 * @param editor
	 */
	public FilePane(Baan baan, BaanPanel editor) {
		super(baan, editor);

		init();
		setup();

		setBorderTextLanguage("Editor.File.0");
		LanguageChanged();
	}

	/**
	 * Initializeer alle knoppen
	 */
	private void init() {
		btOpen = new JButton();
		btNew = new JButton();
		btSave = new JButton();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tigam.railcab.language.LanguageListener#LanguageChanged()
	 */
	@Override
	public void LanguageChanged() {
		super.LanguageChanged();
		if (btOpen != null) {
			btOpen.setText(Language.getString("Editor.File.1"));
		}
		if (btNew != null) {
			btNew.setText(Language.getString("Editor.File.2"));
		}
		if (btSave != null) {
			btSave.setText(Language.getString("Editor.File.3"));
		}
	}

	/**
	 * Plak alles vast aan het panel
	 */
	private void setup() {
		setLayout(new GridLayout(0, 1, 0, 7));

		add(btOpen);
		add(btNew);
		add(btSave);

		btOpen.addActionListener(new OpenHandler());
		btNew.addActionListener(new NewHandler());
		btSave.addActionListener(new SaveHandler());
	}
}
