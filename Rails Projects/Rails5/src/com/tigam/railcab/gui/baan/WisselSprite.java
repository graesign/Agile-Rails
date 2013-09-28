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

package com.tigam.railcab.gui.baan;

import java.awt.Graphics;
import java.util.Observable;

import com.tigam.railcab.gui.Kleuren;
import com.tigam.railcab.model.baan.Baandeel;
import com.tigam.railcab.model.baan.Wissel;
import com.tigam.railcab.util.Direction;

/**
 * 
 * Een Sprite voor het tekenen van een Wissel.
 * 
 * @author Nils Dijk
 * 
 */

public class WisselSprite extends BaandeelSprite {

	/**
	 * Geeft de richting aan waar de wissel naar toe staat.
	 */
	private int direction = 0;

	/**
	 * Onthoud de coördinaten van het lijntje van de wissel
	 */
	private int lastDirection = -1;

	/**
	 * De laatste Versie van de grote van het grid waarop de wisselriching
	 * gecached is
	 */
	private long lastSizeVersion = 0;

	/**
	 * de start X van het wissel lijntje
	 */
	private int midX;

	/**
	 * de start Y van het wissel lijntje
	 */
	private int midY;

	/**
	 * de eind X van het wissel lijntje
	 */
	private int puntX;

	/**
	 * de eind Y van het wissel lijntje
	 */
	private int puntY;

	/**
	 * Constructor, voor het aanmaken van de WisselSprite.
	 * 
	 * @param wissel de Wissel uit het model die getekend moet worden.
	 */
	public WisselSprite(Wissel wissel) {
		super(wissel);
		kleur = Kleuren.Wissel;
	}

	/**
	 * Berekent de coordinaten van het lijntje van de wissel. Deze worden
	 * gecached zodat er niet elke repaint een Sin en Cos berekening gedaan
	 * hoeft te worden.
	 */
	private void calculateDirection() {
		// kijk of je de cache nog kan gebruiken
		if (lastDirection == direction && lastSizeVersion == BaandeelSprite.sizeVersion) {
			return;
		}

		// blijkbaar niet, update de cache gegevens alvast!
		lastDirection = direction;
		lastSizeVersion = BaandeelSprite.sizeVersion;

		double schuin = BaandeelSprite.size / 2;

		midX = x + BaandeelSprite.size / 2;
		midY = y + BaandeelSprite.size / 2;

		double hoek = 0;

		// Wat is de hoek in radialen als je kijkt naar de direction die de wissel nu heeft
		if (direction == Direction.NORTH) {
			hoek = -Math.PI / 2.0;
		} else if (direction == Direction.EAST) {
			hoek = 0;
		} else if (direction == Direction.SOUTH) {
			hoek = Math.PI / 2.0;
		} else if (direction == Direction.WEST) {
			hoek = Math.PI;
		} else if (direction == (Direction.NORTH | Direction.EAST)) {
			hoek = -Math.PI / 4.0;
		} else if (direction == (Direction.SOUTH | Direction.EAST)) {
			hoek = Math.PI / 4.0;
		} else if (direction == (Direction.SOUTH | Direction.WEST)) {
			hoek = Math.PI * 3.0 / 4.0;
		} else if (direction == (Direction.NORTH | Direction.WEST)) {
			hoek = -Math.PI * 3.0 / 4.0;
		}

		// Doe wat moeilijke wiskundige berekeningen
		double overstaand = Math.floor(schuin * Math.sin(hoek));
		double aanliggend = Math.floor(schuin * Math.cos(hoek));

		// Sla het resultaat op
		puntX = (int) (midX + aanliggend);
		puntY = (int) (midY + overstaand);
	}

	/**
	 * Kijkt naar welke kant de wissel staat
	 */
	public void initRichting() {
		Wissel w = (Wissel) baandeel;
		if (w == null) {
			return;
		}
		Baandeel nb = w.getWisselKant();

		direction = Direction.getDirection(baandeel.getLocatie(), nb.getLocatie());
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tigam.railcab.gui.baan.BaandeelSprite#paintLast(java.awt.Graphics)
	 */
	@Override
	protected void paintLast(Graphics g) {

		calculateDirection();

		g.setColor(kleur);

		//g.drawLine(xMid, yMid, xEind, yEind);
		g.drawLine(midX, midY, puntX, puntY);
	}

	@Override
	public void setBaanPanel(BaanPanel bp) {
		super.setBaanPanel(bp);
		initRichting();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tigam.railcab.gui.baan.BaandeelSprite#update(java.util.Observable,
	 *      java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object object) {
		super.update(observable, object);
		if (object instanceof Wissel) {
			initRichting();
		}
	}
}
