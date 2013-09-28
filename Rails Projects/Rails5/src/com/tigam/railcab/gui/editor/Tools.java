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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;

import com.tigam.railcab.gui.editor.baan.BaanPanel;
import com.tigam.railcab.gui.editor.model.Baan;
import com.tigam.railcab.gui.editor.tools.FilePane;
import com.tigam.railcab.gui.editor.tools.SelectPane;
import com.tigam.railcab.gui.editor.tools.SelectionPane;
import com.tigam.railcab.gui.editor.tools.SettingsPane;
import com.tigam.railcab.language.Language;
import com.tigam.railcab.language.LanguageListener;

/**
 * @author Nils Dijk
 * 
 */
public class Tools extends JPanel implements LanguageListener {

	/**
	 * Serializable...
	 */
	private static final long serialVersionUID = -8383475284857735765L;

	/**
	 * De Controller
	 */
	private Baan baan;

	/**
	 * Het scherm van de editor
	 */
	private BaanPanel editor;

	/**
	 * panel met selectie tools
	 */
	private SelectionPane selectieTools;

	/**
	 * panel met select tools
	 */
	private SelectPane selectTools;

	/**
	 * Panel met bestands tools
	 */
	private FilePane fileTools;

	/**
	 * een paneeltje met verschillende instellingen
	 */
	private SettingsPane settingTools;

	/**
	 * Maak een tools scherm aan
	 * 
	 * @param baan de baan controller
	 * @param editor het editor panel
	 */
	public Tools(Baan baan, BaanPanel editor) {
		this.baan = baan;
		this.editor = editor;

		init();
		setup();
		LanguageChanged();
		Language.addLanguageListener(this);
	}

	/**
	 * Voor het inititalizeren van het tools panel
	 */
	private void init() {
		//this.setPreferredSize(new Dimension (180,180));
		//setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		selectieTools = new SelectionPane(baan, editor);
		selectTools = new SelectPane(baan, editor);
		fileTools = new FilePane(baan, editor);
		settingTools = new SettingsPane(baan, editor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tigam.railcab.language.LanguageListener#LanguageChanged()
	 */
	public void LanguageChanged() {
		setBorder(BorderFactory.createTitledBorder(Language.getString("Editor.Tools.0")));
	}

	/**
	 * Maak alles vast op de juiste plaats
	 */
	private void setup() {
		Box b = Box.createVerticalBox();

		b.add(selectieTools);
		b.add(selectTools);
		b.add(fileTools);
		b.add(settingTools);
		add(b);
	}
}
