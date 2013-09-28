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

package com.tigam.railcab.gui.openbaan;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import com.tigam.railcab.language.Language;
import com.tigam.railcab.model.baan.BaanHeader;
import com.tigam.railcab.util.BaanLoader;

/**
 * Een panel om een voorbeeld te laten zien van hoe een baan er uit ziet
 * 
 * @author Nils Dijk
 * 
 */
public class BaanPreview extends JPanel implements PropertyChangeListener {

	/**
	 * Serializable...
	 */
	private static final long serialVersionUID = -6756053330838297654L;

	/**
	 * De geladen baan
	 */
	private BaanHeader baanHeader = null;

	/**
	 * Is de baan geldig?
	 */
	public boolean valid = true;

	/**
	 * Moet de naam van de baan weergegeven worden?
	 */
	private boolean showName = true;

	/**
	 * Maak een BaanPreview aan
	 */
	public BaanPreview() {
		this(null, null);
	}

	/**
	 * Maak een BaanPreview aan met opgegeven groote
	 * 
	 * @param prefSize
	 */
	public BaanPreview(Dimension prefSize) {
		this(null, prefSize);
	}

	/**
	 * Maak een BaanPreview aan met opgegeven groote
	 * 
	 * @param width
	 * @param height
	 */
	public BaanPreview(int width, int height) {
		this(null, new Dimension(width, height));
	}

	/**
	 * Maak een BaanPreview aan en maar het vast aan een FileChooser
	 * 
	 * @param chooser
	 */
	public BaanPreview(JFileChooser chooser) {
		this(chooser, new Dimension(200, 200));
	}

	/**
	 * Maak een BaanPreview aan met opgegeven groote en maak het vast aan een
	 * FileChooser
	 * 
	 * @param chooser
	 * @param prefSize
	 */
	public BaanPreview(JFileChooser chooser, Dimension prefSize) {
		if (prefSize != null) {
			setPreferredSize(prefSize);
		}

		if (chooser != null) {
			chooser.setAccessory(this);
			chooser.addPropertyChangeListener(this);
		}
	}

	@Override
	public void paint(Graphics g) {
		Dimension s = this.getSize();
		//this.setPreferredSize(new Dimension(s.height,s.height));

		g.setColor(getBackground());
		g.fillRect(0, 0, s.width, s.height);

		if (baanHeader != null) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, s.width, s.height);

			BufferedImage buf;
			// kijk of de naam er wel op moet komen

			buf = CreatePreview.createPreview(baanHeader, s, showName, getBackground());

			g.drawImage(buf, 0, 0, null);
		} else {
			g.setColor(Color.BLACK);
			g.setFont(new Font(null, 0, 10));
			g.drawString(Language.getString("BaanPreview.0"), 0, 10);
		}
	}

	// @Override
	public void propertyChange(PropertyChangeEvent changeEvent) {
		String changeName = changeEvent.getPropertyName();
		if (changeName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
			File file = (File) changeEvent.getNewValue();
			if (file != null) {
				baanHeader = BaanLoader.loadBaan(file.getAbsolutePath(), false);
				setLoadedBaan(baanHeader);
			}
		}
	}

	/**
	 * Geef op welke baan er weergegeven moet worden
	 * 
	 * @param baanHeader
	 */
	public void setLoadedBaan(BaanHeader baanHeader) {
		this.baanHeader = baanHeader;
		repaint();
	}

	/**
	 * Geef aan of de naam weergegeven moet worden
	 * 
	 * @param show
	 */
	public void setShowName(boolean show) {
		if (showName == show) {
			return;
		}
		showName = show;
		repaint();
	}

}
