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

package com.tigam.railcab.gui.editor.baan;

import java.awt.Color;
import java.awt.Graphics;

import com.tigam.railcab.gui.Kleuren;
import com.tigam.railcab.gui.editor.model.Finder;
import com.tigam.railcab.gui.editor.model.Selection;
import com.tigam.railcab.model.baan.Baandeel;
import com.tigam.railcab.model.baan.StationBaandeel;
import com.tigam.railcab.model.baan.Wissel;
import com.tigam.railcab.util.Direction;

/**
 * De visuele weergave van een BaandeelSprite
 * 
 * @author Nils Dijk
 * 
 */
public class BaandeelSprite extends AbstractBaandeelSprite {

	/**
	 * Methode voor het maken van de juiste BaandeelSprite
	 * 
	 * @param b Het baandeel waar je een weergave van wilt maken
	 * @return geeft een gepast BaandeelSprite terug voor het gegeven Baandeel
	 */
	public static BaandeelSprite maakBaandeelSprite(Baandeel b) {
		if (b instanceof Wissel) {
			return new WisselSprite((Wissel) b);
		}
		if (b instanceof StationBaandeel) {
			return new StationBaandeelSprite((StationBaandeel) b);
		}
		return new BaandeelSprite(b);
	}

	/**
	 * De locatie op de x-as om te beginnen met tekenen
	 */
	protected int x;

	/**
	 * De locatie op de y-as om te beginnen met tekenen
	 */
	protected int y;

	/**
	 * Het baandeel waar het BaandeelSprite op gebaseert is
	 */
	protected Baandeel baandeel;

	/**
	 * Is het BaandeelSprite geselecteerd ?
	 */
	protected boolean isSelected = false;

	/**
	 * Een selection object waar dit Sprite bij hoort, is nodig om te kijken
	 * welke buren allemaal geselecteerd zijn
	 */
	protected Selection selection;

	/**
	 * Maak een BaandeelSprite aan met een gegeven Baandeel
	 * 
	 * @param baandeel het gegeven baandeel
	 */
	public BaandeelSprite(Baandeel baandeel) {
		this.baandeel = baandeel;
		kleur = Kleuren.Baandeel;
	}

	/**
	 * Bij een double klik op een baandeel
	 * 
	 * @return Een Selection van Baandelen dat je graag geselecteerd hebt met
	 *         jou.
	 */
	public Selection doubleClick() {
		return Finder.omkeerbaar(baandeel);
		/*
		 * Baandeel b;
		 * 
		 * Selection s = new Selection(); s.add(baandeel.getLocatie()); //
		 * Selecteer alles totaan de eerst volgende wissel b =
		 * baandeel.getVolgende(); while (!(b instanceof Wissel) &&
		 * !s.isInSelection(b.getLocatie())){ s.add(b.getLocatie()); b =
		 * b.getVolgende(); } // Selecteer alles totaan de eerst vorig wissel b =
		 * baandeel.getVorige(); while (!(b instanceof Wissel) &&
		 * !s.isInSelection(b.getLocatie())){ s.add(b.getLocatie()); b =
		 * b.getVorige(); }
		 * 
		 * return s;
		 */
	}

	/**
	 * Vraag het baandeel op waarop ik gebaseert ben
	 * 
	 * @return het baandeel
	 */
	public Baandeel getBaandeel() {
		return baandeel;
	}

	/**
	 * Wordt aangeroepen op het moment dat een object de focus krijgt.
	 * 
	 * @return Een Selection van Baandelen dat je graag geselecteerd hebt met
	 *         jou.
	 */
	public Selection onFocus() {
		return new Selection(baandeel.getLocatie());
	}

	/**
	 * Wordt aangeroepen op het moment dat een object de focus verliest
	 */
	public void onLostFocus() {
	}

	@Override
	public void paint(Graphics g) {
		x = AbstractBaandeelSprite.offsetX + AbstractBaandeelSprite.size * baandeel.getLocatie().x;
		y = AbstractBaandeelSprite.offsetY + AbstractBaandeelSprite.size * baandeel.getLocatie().y;

		g.setColor(Kleuren.AchtergrondEditor);
		g.fillRect(x, y, AbstractBaandeelSprite.size, AbstractBaandeelSprite.size);

		paintBaan(g);
		if (showRichting) {
			paintRichting(g);
		}
		paintLast(g);

		if (isSelected) {
			paintSelection(g);
		}

	}

	@Override
	protected void paintBaan(Graphics g) {
		g.setColor(Kleuren.BaandeelAchtergrond);
		g.fillOval(x, y, AbstractBaandeelSprite.size - 1, AbstractBaandeelSprite.size - 1);
		g.setColor(kleur);
		g.drawOval(x, y, AbstractBaandeelSprite.size - 1, AbstractBaandeelSprite.size - 1);
	}

	/**
	 * Methode om nog het laatste aan het baandeel te tekenen indien nodig.
	 * 
	 * @param g
	 */
	protected void paintLast(Graphics g) {
	}

	/**
	 * Methode voor het visueel weergeven van de richting
	 * 
	 * @param g
	 */
	protected void paintRichting(Graphics g) {
		g.setColor(kleur);

		Baandeel volg = baandeel.getVolgende();
		int dir = Direction.getDirection(baandeel.getLocatie(), volg.getLocatie());

		// kijk aan de hand van de richting wat er getekent moet worden
		if (dir == Direction.NORTH) {
			g.drawLine(x + AbstractBaandeelSprite.size / 2, y + AbstractBaandeelSprite.size / 4, x
					+ AbstractBaandeelSprite.size / 4, y + AbstractBaandeelSprite.size / 2);
			g.drawLine(x + AbstractBaandeelSprite.size / 2, y + AbstractBaandeelSprite.size / 4, x
					+ AbstractBaandeelSprite.size * 3 / 4, y + AbstractBaandeelSprite.size / 2);
		} else if (dir == Direction.EAST) {
			g.drawLine(x + AbstractBaandeelSprite.size * 3 / 4, y + AbstractBaandeelSprite.size / 2, x
					+ AbstractBaandeelSprite.size / 2, y + AbstractBaandeelSprite.size / 4);
			g.drawLine(x + AbstractBaandeelSprite.size * 3 / 4, y + AbstractBaandeelSprite.size / 2, x
					+ AbstractBaandeelSprite.size / 2, y + AbstractBaandeelSprite.size * 3 / 4);
		} else if (dir == Direction.SOUTH) {
			g.drawLine(x + AbstractBaandeelSprite.size / 2, y + AbstractBaandeelSprite.size * 3 / 4, x
					+ AbstractBaandeelSprite.size / 4, y + AbstractBaandeelSprite.size / 2);
			g.drawLine(x + AbstractBaandeelSprite.size / 2, y + AbstractBaandeelSprite.size * 3 / 4, x
					+ AbstractBaandeelSprite.size * 3 / 4, y + AbstractBaandeelSprite.size / 2);
		} else if (dir == Direction.WEST) {
			g.drawLine(x + AbstractBaandeelSprite.size / 4, y + AbstractBaandeelSprite.size / 2, x
					+ AbstractBaandeelSprite.size / 2, y + AbstractBaandeelSprite.size / 4);
			g.drawLine(x + AbstractBaandeelSprite.size / 4, y + AbstractBaandeelSprite.size / 2, x
					+ AbstractBaandeelSprite.size / 2, y + AbstractBaandeelSprite.size * 3 / 4);
		} else if (dir == (Direction.NORTH | Direction.EAST)) {
			g.drawLine(x + AbstractBaandeelSprite.size * 3 / 4, y + AbstractBaandeelSprite.size / 4, x
					+ AbstractBaandeelSprite.size * 2 / 5, y + AbstractBaandeelSprite.size / 4);
			g.drawLine(x + AbstractBaandeelSprite.size * 3 / 4, y + AbstractBaandeelSprite.size / 4, x
					+ AbstractBaandeelSprite.size * 3 / 4, y + AbstractBaandeelSprite.size * 3 / 5);
		} else if (dir == (Direction.NORTH | Direction.WEST)) {
			g.drawLine(x + AbstractBaandeelSprite.size / 4, y + AbstractBaandeelSprite.size / 4, x
					+ AbstractBaandeelSprite.size * 3 / 5, y + AbstractBaandeelSprite.size / 4);
			g.drawLine(x + AbstractBaandeelSprite.size / 4, y + AbstractBaandeelSprite.size / 4, x
					+ AbstractBaandeelSprite.size / 4, y + AbstractBaandeelSprite.size * 3 / 5);
		} else if (dir == (Direction.SOUTH | Direction.EAST)) {
			g.drawLine(x + AbstractBaandeelSprite.size * 3 / 4, y + AbstractBaandeelSprite.size * 3 / 4, x
					+ AbstractBaandeelSprite.size * 2 / 5, y + AbstractBaandeelSprite.size * 3 / 4);
			g.drawLine(x + AbstractBaandeelSprite.size * 3 / 4, y + AbstractBaandeelSprite.size * 3 / 4, x
					+ AbstractBaandeelSprite.size * 3 / 4, y + AbstractBaandeelSprite.size * 2 / 5);
		} else if (dir == (Direction.SOUTH | Direction.WEST)) {
			g.drawLine(x + AbstractBaandeelSprite.size / 4, y + AbstractBaandeelSprite.size * 3 / 4, x
					+ AbstractBaandeelSprite.size * 3 / 5, y + AbstractBaandeelSprite.size * 3 / 4);
			g.drawLine(x + AbstractBaandeelSprite.size / 4, y + AbstractBaandeelSprite.size * 3 / 4, x
					+ AbstractBaandeelSprite.size / 4, y + AbstractBaandeelSprite.size * 2 / 5);
		}
	}

	/**
	 * Methode voor het tekenen van een selectie ovelay.
	 * 
	 * @param g
	 */
	protected void paintSelection(Graphics g) {
		g.setColor(new Color(Kleuren.BaandeelSelected.getRed(), Kleuren.BaandeelSelected.getGreen(),
				Kleuren.BaandeelSelected.getBlue(), 20));
		g.fillRect(x, y, AbstractBaandeelSprite.size, AbstractBaandeelSprite.size);

		// teken de randjes indien er een selectie is opgegeven
		if (selection != null) {
			g.setColor(Kleuren.BaandeelSelected);
			if (!selection.isInSelection(Direction.newPoint(baandeel.getLocatie(), Direction.NORTH))) {
				g.drawLine(x, y, x + size - 1, y);
			}
			if (!selection.isInSelection(Direction.newPoint(baandeel.getLocatie(), Direction.EAST))) {
				g.drawLine(x + size - 1, y, x + size - 1, y + size - 1);
			}
			if (!selection.isInSelection(Direction.newPoint(baandeel.getLocatie(), Direction.SOUTH))) {
				g.drawLine(x, y + size - 1, x + size - 1, y + size - 1);
			}
			if (!selection.isInSelection(Direction.newPoint(baandeel.getLocatie(), Direction.WEST))) {
				g.drawLine(x, y, x, y + size - 1);
			}
		}
	}

	/**
	 * Zet de selectie status van dit baandeel
	 * 
	 * @param selected <b>true</b> zet het object op geselecteerd<br>
	 *            <b>false</b> zet het object op ongeselecteerd
	 */
	public void setSelected(boolean selected) {
		this.setSelected(selected, null);
	}

	/**
	 * Zet de selectie status van dit baandeel
	 * 
	 * @param selected <b>true</b> zet het object op geselecteerd<br>
	 *            <b>false</b> zet het object op ongeselecteerd
	 * @param selection De selection die er bij hoort
	 */
	public void setSelected(boolean selected, Selection selection) {
		isSelected = selected;
		this.selection = selection;
	}
}
