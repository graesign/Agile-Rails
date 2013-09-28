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

import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileView;

import com.tigam.railcab.util.ReadFile;

/**
 * Een aangepaste FileView, zodat de icoontes mooi weergegeven worden
 * 
 * @author Nils Dijk
 * 
 */
public class BaanFileView extends FileView {

	/**
	 * Het baan icoontje
	 */
	private Icon baanIcon;

	/**
	 * Maak de view aan en initializeer het op de juiste manier
	 */
	public BaanFileView() {
		try {
			InputStream in = ReadFile.read("resources/baan_icon_16.png");
			baanIcon = new ImageIcon(ImageIO.read(in));
			in.close();
		} catch (Exception ex) {
			System.out.println("Unable to open baan_icon.png");
		}
	}

	@Override
	public Icon getIcon(File file) {
		if (file.isDirectory()) {
			return null;
		}
		Icon icon = null;
		String filename = file.getName().toLowerCase();
		if (filename.endsWith(".baan")) {
			icon = baanIcon;
		}
		return icon;
	}

	@Override
	public String getName(File file) {
		String filename = file.getName();
		if (filename.endsWith(".baan")) {
			String name = filename.substring(0, filename.length() - 5);
			return name;
		}
		return null;
	}

	@Override
	public String getTypeDescription(File file) {
		String typeDescription = null;
		String filename = file.getName().toLowerCase();

		System.out.println("Get type of " + filename);

		if (filename.endsWith(".baan")) {
			typeDescription = "RailCab Baan";
		}
		return typeDescription;
	}
}
