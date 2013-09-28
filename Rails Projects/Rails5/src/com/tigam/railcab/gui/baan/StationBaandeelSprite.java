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
import java.util.ArrayList;

import com.tigam.railcab.gui.Kleuren;
import com.tigam.railcab.model.baan.Station;
import com.tigam.railcab.model.baan.StationBaandeel;

/**
 * 
 * Een Sprite voor het weergeven van een StationBaandeel
 * 
 * @author Nils Dijk
 * 
 */
public class StationBaandeelSprite extends BaandeelSprite {
	/**
	 * Een lijst met alle StationBaandelen die bij het zelfde station horen
	 */
	private ArrayList<StationBaandeelSprite> stationBaandelen;

	/**
	 * Het Station waar ik bij hoor
	 */
	private Station mijnStation = null;

	/**
	 * Constructor, voor het maken van een Sprite
	 * 
	 * @param station een StationBaandeel uit het model
	 */
	public StationBaandeelSprite(StationBaandeel station) {
		super(station);

		kleur = Kleuren.Station;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tigam.railcab.gui.baan.BaandeelSprite#onFocus()
	 */
	@Override
	public void onFocus() {
		for (int i = 0; i < stationBaandelen.size(); i++) {
			stationBaandelen.get(i).setSelected(true);
		}
		setChanged();
		this.notifyObservers(mijnStation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tigam.railcab.gui.baan.BaandeelSprite#onLostFocus()
	 */
	@Override
	public void onLostFocus() {
		for (int i = 0; i < stationBaandelen.size(); i++) {
			stationBaandelen.get(i).setSelected(false);
		}
		setChanged();
		this.notifyObservers(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tigam.railcab.gui.baan.BaandeelSprite#paintBaan(java.awt.Graphics)
	 */
	@Override
	protected void paintBaan(Graphics g) {
		g.setColor(kleur);
		g.fillRect(x, y, BaandeelSprite.size, BaandeelSprite.size);
		g.setColor(Kleuren.BaandeelAchtergrond);
		g.fillOval(x, y, BaandeelSprite.size - 1, BaandeelSprite.size - 1);

		// Moet het BaanGebruik getekent worden ?
		if (kleurFilter != null) {
			g.setColor(kleurFilter);
			g.fillOval(x, y, BaandeelSprite.size - 1, BaandeelSprite.size - 1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tigam.railcab.gui.baan.BaandeelSprite#paintTrein(java.awt.Graphics)
	 */
	@Override
	protected void paintTrein(Graphics g) {
		super.paintTrein(g);
		g.setColor(Kleuren.BaandeelAchtergrond);
		g.drawOval(x, y, BaandeelSprite.size - 1, BaandeelSprite.size - 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tigam.railcab.gui.baan.BaandeelSprite#setSelected(boolean)
	 */
	@Override
	public void setSelected(boolean isSelected) {
		if (isSelected) {
			kleur = Kleuren.StationSelected;
		} else {
			kleur = Kleuren.Station;
		}

		repaint();
	}

	/**
	 * Aangeven van de StationBaandeelSprite objecten die tot het zelfde station
	 * behoren als dit Object. Er staat ook en referentie naar je zelf in deze
	 * lijst.
	 * 
	 * @param s Station
	 * @param stationBaandelen een ArrayList met alle StationBaandeelSprites die
	 *            tot het zelfde station behoren als jij.
	 */
	public void setStationBaandelen(Station s, ArrayList<StationBaandeelSprite> stationBaandelen) {
		this.stationBaandelen = stationBaandelen;
		mijnStation = s;
	}
}
