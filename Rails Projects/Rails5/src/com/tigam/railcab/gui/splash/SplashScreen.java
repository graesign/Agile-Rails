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
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;

import com.tigam.railcab.util.ReadFile;

/**
 * 
 * Het RailCab SplashScreen
 * 
 * @author Nils Dijk
 * 
 */
public class SplashScreen extends JFrame {

	/**
	 * Serializable...
	 */
	private static final long serialVersionUID = -3131747172292144348L;

	/**
	 * Start de hele applicatie
	 * 
	 * @param args argumenten om de applicatie te starten zijn er niet.
	 */
	public static void main(String[] args) {
		new SplashScreen();
	}

	/**
	 * Maak een nieuw SplashScreen
	 */
	public SplashScreen() {
		loadIcon();
		init();
		setup();
		deploy();
	}

	/**
	 * Laat het Frame zien
	 */
	private void deploy() {
		setTitle("RailCab SIMULATOR");
		pack();
		setResizable(false);

		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent winEvt) {
				System.exit(0);
			}
		});

		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension framesize = getSize();
		int left = (int) ((screensize.getWidth() - framesize.getWidth()) / 2.0);
		int up = (int) ((screensize.getHeight() - framesize.getHeight()) / 2.0);
		setLocation(left, up);

		setVisible(true);
	}

	/**
	 * Initializeer het SplashScreen
	 */
	private void init() {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
	}

	/**
	 * Laad het mooie baan icoontje voor het splash screen
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
	 * Voeg alle objecten toe aan het Frame
	 */
	private void setup() {
		add(new BannerPanel());
		add(new SoftwareSimulatiePanel(this));
	}

}
