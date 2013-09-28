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

package com.tigam.railcab.gui.editor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.tigam.railcab.gui.editor.baan.BaanPanel;
import com.tigam.railcab.gui.editor.baan.BaanPanelRenderThread;
import com.tigam.railcab.gui.editor.model.Baan;
import com.tigam.railcab.model.baan.BaanHeader;
import com.tigam.railcab.util.ReadFile;

/**
 * 
 * Het editor window
 * 
 * @author Nils Dijk
 * 
 */
public class Editor extends JFrame {

	/**
	 * Serializable...
	 */
	private static final long serialVersionUID = 4312816322691773637L;

	/**
	 * Start de editor op als een losstaand programma
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Editor();
	}

	/**
	 * BaanPanel wat in het frame zit
	 */
	private BaanPanel baanPanel;

	/**
	 * De naam van de geopende baan
	 */
	private String openNaam = null;

	/**
	 * Baan Controler
	 */
	private Baan baan;

	/**
	 * opent de editor zonder baan.
	 */
	public Editor() {
		this(null);
	}

	/**
	 * Maak een nieuw editor frame aan de hand van een geladen baan
	 * 
	 * @param baanHeader de geladenBaan
	 */
	public Editor(BaanHeader baanHeader) {
		updateTitle();
		loadIcon();

		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		// Maak het model
		baan = new Baan(baanHeader);
		baan.setEditorFrame(this);

		NamePanel np = new NamePanel(baan);
		baan.setNamePanel(np);
		c.add(np, BorderLayout.NORTH);

		baanPanel = new BaanPanel(baan);
		c.add(baanPanel, BorderLayout.CENTER);

		c.add(new Tools(baan, baanPanel), BorderLayout.EAST);

		//plaats het scherm mooi in het midden en bedek een kwart van het scherm
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screensize.width / 2, screensize.height / 2);
		Dimension framesize = getSize();
		int left = (int) ((screensize.getWidth() - framesize.getWidth()) / 2.0);
		int up = (int) ((screensize.getHeight() - framesize.getHeight()) / 2.0);
		setLocation(left, up);

		// maximalizeer de editor maar meteen :) heb je lekker veel ruimte
		setExtendedState(getExtendedState() | Frame.MAXIMIZED_BOTH);

		setVisible(true);

		baanPanel.setFocusable(true);
		baanPanel.requestFocus();

		new BaanPanelRenderThread(baanPanel);
	}

	/**
	 * Vraag het BaanPanel op
	 * 
	 * @return het BaanPanel
	 */
	public BaanPanel getBaanPanel() {
		return baanPanel;
	}

	/**
	 * Laad het mooie baan icoontje voor de editor
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
	 * Geef de naam op van de geopende baan zodat deze in de titelbar kan
	 * verschijnen.
	 * 
	 * @param naam De naam van de baan, null als je geen naam weergegeven wilt
	 *            hebben
	 */
	public void setTitleNaam(String naam) {
		openNaam = naam;
		updateTitle();
	}

	/**
	 * Update de titelbar van het frame
	 */
	private void updateTitle() {
		if (openNaam == null) {
			setTitle("RailCab Editor");
		} else {
			setTitle("RailCab Editor - " + openNaam);
		}
	}
}
