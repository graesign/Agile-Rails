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
import java.awt.Graphics;
import java.awt.Image;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.tigam.railcab.gui.Kleuren;
import com.tigam.railcab.util.ReadFile;

/**
 * Pannel voor het tonen van de banner
 * 
 * @author Michiel van den Anker
 * 
 */
public class BannerPanel extends JPanel {

	/**
	 * Serializable...
	 */
	private static final long serialVersionUID = -2538334370425150406L;

	/**
	 * Het plaatje dat als banner ingesteld is
	 */
	private Image banner;

	/**
	 * Maak een BannerPanel aan
	 */
	public BannerPanel() {
		// de groote voor het banner gedeelte
		setPreferredSize(new Dimension(600, 200));

		// zet de achtergrondkleur voor als er problemen zijn met het plaajte, mooi RailCab logo :)
		setBackground(Kleuren.SplashBanner);

		// zoek het plaatje en open het bestand
		InputStream in = ReadFile.read("resources/railcabbanner.gif");
		try {
			// laad het plaatje
			if (in != null) {
				banner = ImageIO.read(in);
			}
		} catch (Exception ex) {
			banner = null;
		} finally {
			try {
				// sluit het bestand weer
				in.close();
			} catch (Exception ex) {
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		// kijk of de banner wel in het geheugen geladen is.
		if (banner != null) {
			g.drawImage(banner, 0, 0, null);
		}
	}

}