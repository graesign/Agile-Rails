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
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.tigam.railcab.gui.Kleuren;
import com.tigam.railcab.gui.editor.baan.AbstractBaandeelSprite;
import com.tigam.railcab.gui.editor.baan.BaandeelSprite;
import com.tigam.railcab.model.baan.BaanHeader;
import com.tigam.railcab.model.baan.Baandeel;
import com.tigam.railcab.util.BaanToGrid;

/**
 * Hulp middelen om een plaatje van een baan te maken
 * 
 * @author Nils Dijk
 * 
 */
public class CreatePreview {

	/**
	 * Maak een plaatje
	 * 
	 * @param baanHeader
	 * @param s
	 * @param showName
	 * @return Een BufferedImage van de baan
	 */
	public static BufferedImage createPreview(BaanHeader baanHeader, Dimension s, boolean showName) {
		return createPreview(baanHeader, s.width, s.height, showName, null);
	}

	/**
	 * Maak een plaatje
	 * 
	 * @param baanHeader
	 * @param s
	 * @param showName
	 * @param background
	 * @return Een BufferedImage van de baan
	 */
	public static BufferedImage createPreview(BaanHeader baanHeader, Dimension s, boolean showName, Color background) {
		return createPreview(baanHeader, s.width, s.height, showName, background);
	}

	/**
	 * Maak een plaatje
	 * 
	 * @param baanHeader
	 * @param width
	 * @param height
	 * @param showName
	 * @return Een BufferedImage van de baan
	 */
	public static BufferedImage createPreview(BaanHeader baanHeader, int width, int height, boolean showName) {
		return createPreview(baanHeader, width, height, showName, null);
	}

	/**
	 * Voor het maken van een voorbeeld van een gegeven baan
	 * 
	 * @param baanHeader de baan waar een plaatje van moet worden gemaakt
	 * @param width de breedte van het plaatje
	 * @param height de hoogte van het plaatje
	 * @param showName moet de naam weergegeven worden
	 * @param background de achtergrond kleur
	 * @return een BufferedImage met het plaatje van de baan
	 */
	public static BufferedImage createPreview(BaanHeader baanHeader, int width, int height, boolean showName,
			Color background) {
		Color backup = null;

		// een backup maken van de orginele achtergrond kleur als er een kleur is opgegeven
		if (background != null) {
			backup = Kleuren.AchtergrondEditor;
			Kleuren.AchtergrondEditor = background;
		}

		BufferedImage buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Baandeel[][] grid = BaanToGrid.baanToGrid(baanHeader.getBaan());

		if (grid.length == 0 || grid[0].length == 0) {
			return null;
			//BaandeelSprite[][] paintGrid = new BaandeelSprite[grid.length][grid[0].length];
		}

		int gridHeight, gridWidth;
		int gridSize;

		gridWidth = width / grid.length;
		gridHeight = height / grid[0].length;
		if (gridWidth > gridHeight) {
			gridSize = gridHeight;
		} else {
			gridSize = gridWidth;
		}

		AbstractBaandeelSprite.setSize(gridSize);

		gridWidth = gridSize * grid.length;
		gridHeight = gridSize * grid[0].length;

		AbstractBaandeelSprite.setOffsetX((width - gridWidth) / 2);
		AbstractBaandeelSprite.setOffsetY((height - gridHeight) / 2);

		BaandeelSprite s;
		Graphics2D g = (Graphics2D) buf.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(Kleuren.AchtergrondEditor);
		g.fillRect(0, 0, width, height);

		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[x].length; y++) {
				if (grid[x][y] != null) {
					s = BaandeelSprite.maakBaandeelSprite(grid[x][y]);
					s.paint(g);
				}
			}
		}

		if (showName) {
			Font f = new Font(null, Font.BOLD, 10);
			g.setColor(Kleuren.AchtergrondEditor);
			g.fillRect(0, 0, (int) f.getStringBounds(baanHeader.getNaam(), g.getFontRenderContext()).getWidth(),
					(int) f.getStringBounds(baanHeader.getNaam(), g.getFontRenderContext()).getHeight() + 2);

			g.setColor(Color.BLACK);
			g.setFont(f);
			g.drawString(baanHeader.getNaam(), 0, (int) f.getStringBounds(baanHeader.getNaam(),
					g.getFontRenderContext()).getHeight());
			//g.getFontMetrics().stringWidth(baanHeader.getNaam());

		}

		// terug zetten van de oude kleur indien nodig
		if (backup != null) {
			Kleuren.AchtergrondEditor = backup;
		}

		return buf;
	}
}
