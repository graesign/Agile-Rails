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

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.tigam.railcab.language.Language;
import com.tigam.railcab.model.baan.BaanHeader;
import com.tigam.railcab.util.BaanLoader;

/**
 * Een scherm om een baan te openen
 * 
 * @author Nils Dijk
 * 
 */
public class BaanFileChooser extends JFileChooser {

	/**
	 * Serializable...
	 */
	private static final long serialVersionUID = 495858889720063585L;

	/**
	 * Open het scherm en laat hem een baan laden.
	 * 
	 * @return een BaanHeader van de geopende baan
	 */
	public static BaanHeader laadBaan() {
		return BaanFileChooser.laadBaan(null);
	}

	/**
	 * Laad een baan, als er gecanceld wordt wordt de huidigeBaan terug gegeven
	 * 
	 * @param huidigeBaan baan die wordt teruggegeven als er gecanceld wordt
	 * @return een BaanHeader van de geopende baan
	 */
	public static BaanHeader laadBaan(BaanHeader huidigeBaan) {
		BaanFileChooser fileChooser = new BaanFileChooser();

		new BaanPreview(fileChooser);

		int status = fileChooser.showOpenDialog(null);

		if (status == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();

			BaanHeader lb = BaanLoader.loadBaan(selectedFile.getAbsolutePath());

			if (lb != null) {
				return lb;
			}

		}
		return huidigeBaan;
	}

	/**
	 * teste het programma los :)
	 * 
	 * @param args
	 */
	static public void main(String[] args) {
		BaanFileChooser fileChooser = new BaanFileChooser();
		new BaanPreview(fileChooser);

		int status = fileChooser.showOpenDialog(null);

		if (status == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			System.out.println("opened");
			System.out.println(selectedFile.getAbsolutePath());
		} else if (status == JFileChooser.CANCEL_OPTION) {
			System.out.println("canceled");
		}
	}

	/**
	 * Open het sla op scherm
	 * 
	 * @return De bestandsnaam
	 */
	public static String saveBaan() {
		BaanFileChooser fileChooser = new BaanFileChooser();

		int status = fileChooser.showSaveDialog(null);

		if (status == JFileChooser.ERROR_OPTION) {
			return null;
		}
		if (status == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			String file = selectedFile.getAbsolutePath();
			if (!file.toLowerCase().endsWith(".baan")) {
				file += ".baan";
			}
			File f = new File(file);
			if (f.exists()) {
				int option = JOptionPane
						.showConfirmDialog(
								null,
								Language.getString("FileSave.1"), Language.getString("FileSave.0"), JOptionPane.CANCEL_OPTION | JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
				if (option == JOptionPane.CLOSED_OPTION || option == JOptionPane.CANCEL_OPTION) {
					return saveBaan();
				}
			}
			return file;

		}

		return null;

	}

	/**
	 * Maak een standaard scherm
	 */
	public BaanFileChooser() {
		super();
		init();
	}

	/**
	 * initializeer het frame
	 */
	private void init() {
		setFileView(new BaanFileView());
		// kies een goed filter
		FileFilter[] fs = getChoosableFileFilters();
		for (int i = 0; i < fs.length; i++) {
			removeChoosableFileFilter(fs[i]);
		}
		addChoosableFileFilter(new BaanFileFilter());

		// zorg dat er maar 1 bestand geselecteerd mag worden
		setMultiSelectionEnabled(false);
		setFileSelectionMode(JFileChooser.FILES_ONLY);

		//this.setApproveButtonText("Laad");
	}
}
