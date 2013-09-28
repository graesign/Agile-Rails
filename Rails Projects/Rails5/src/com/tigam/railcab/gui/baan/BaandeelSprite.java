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

/**
 * Een BaandeelSprite stelt één baandeel voor op het grid. Deze klasse heeft de
 * mogenlijkheid om zown baandeel te tekenen
 * 
 * @author Nils Dijk
 */

package com.tigam.railcab.gui.baan;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import com.tigam.railcab.gui.Kleuren;
import com.tigam.railcab.gui.baan.filters.Filter;
import com.tigam.railcab.model.baan.Baandeel;
import com.tigam.railcab.model.baan.RailCab;
import com.tigam.railcab.model.baan.StationBaandeel;
import com.tigam.railcab.model.baan.Wissel;

/**
 * 
 * Een visuele weergave van een Baandeel uit het model.
 * 
 * @author Nils Dijk
 * 
 */

public class BaandeelSprite extends Observable implements Observer {

	/**
	 * De groote van één vakje in het grid
	 */
	protected static int size = 10;

	/**
	 * Versie van de maten, wordt verhoogd met 1 als het scherm geresized wordt.
	 */
	protected static long sizeVersion = 0;

	/**
	 * De offset van het grid op de X-as
	 */
	protected static int offsetX = 0;

	/**
	 * De offset van het grid op de Y-as
	 */
	protected static int offsetY = 0;

	/**
	 * Het filter wat over het baandeel heen ligt
	 */
	protected static Filter filter = null;

	/**
	 * Static Methode om het juiste BaandeelSprite te maken voor elk baandeel.
	 * 
	 * @param baandeel Baandeel waar de nieuwe Sprite op gebaseerd wordt
	 * @return BaandeelSprite
	 */
	public static BaandeelSprite maakSprite(Baandeel baandeel) {
		if (baandeel == null) {
			return null;
		}
		if (baandeel instanceof Wissel) {
			return new WisselSprite((Wissel) baandeel);
		}
		if (baandeel instanceof StationBaandeel) {
			return new StationBaandeelSprite((StationBaandeel) baandeel);
		}
		return new BaandeelSprite(baandeel);
	}

	/**
	 * Zet een Filter voor de baan.
	 * 
	 * @param filter
	 */
	public static void setFilter(Filter filter) {
		BaandeelSprite.filter = filter;
	}

	/**
	 * Voor het aanpassen van de offset op de x as. de offset wordt gebruikt om
	 * zo de hele treinbaan in het midden te kunnen plaatsen
	 * 
	 * @param offsetX
	 */
	public static void setOffsetX(int offsetX) {
		if (BaandeelSprite.offsetX == offsetX) {
			return;
		}
		BaandeelSprite.offsetX = offsetX;
		BaandeelSprite.sizeVersion++;
	}

	/**
	 * Voor het aanpassen van de offset op de y as. de offset wordt gebruikt om
	 * zo de hele treinbaan in het midden te kunnen plaatsen
	 * 
	 * @param offsetY
	 */
	public static void setOffsetY(int offsetY) {
		if (BaandeelSprite.offsetY == offsetY) {
			return;
		}
		BaandeelSprite.offsetY = offsetY;
		BaandeelSprite.sizeVersion++;
	}

	/**
	 * geef de grote van het een gridvakje op.
	 * 
	 * @param size de grote
	 */
	public static void setSize(int size) {
		if (BaandeelSprite.size == size) {
			return;
		}
		BaandeelSprite.size = size;
		BaandeelSprite.sizeVersion++;
	}

	/**
	 * Baandeel waarop deze Sprite gebaseert is
	 */
	protected Baandeel baandeel;

	/**
	 * De RailCab die op dit baandeel staat. null is het geval van geen RailCab
	 */
	protected RailCab mijnCab;

	/**
	 * Het BaanPanel waarop het sprite getekent moet worden
	 */
	protected BaanPanel bp = null;

	/**
	 * De positite op de X-as om te beginnen met tekenen
	 */
	protected int x;

	/**
	 * De positite op de Y-as om te beginnen met tekenen
	 */
	protected int y;

	/**
	 * Kleur die de buitenkant van het baandeel heeft
	 */
	protected Color kleur = Kleuren.Baandeel;

	/**
	 * Kleur die door het Filter gegenereerd wordt
	 */
	protected Color kleurFilter = null;

	/**
	 * Constructor van BaandeelSprite, slaat het één en ander op en voegt zich
	 * zelf toe als observer van het baandeel waar het bij hoort.
	 * 
	 * @param baandeel baandeel uit het model wat dit deze sprite representeert
	 *            op het scherm
	 */
	public BaandeelSprite(Baandeel baandeel) {
		this.baandeel = baandeel;
		this.baandeel.addObserver(this);

		mijnCab = this.baandeel.getRailCab();
	}

	/**
	 * Vraag het baandeel op waarbij deze Sprite hoort.
	 * 
	 * @return Baandeel Object wat gerepresenteerd wordt door deze Sprite.
	 */
	public Baandeel getBaandeel() {
		return baandeel;
	}

	/**
	 * Methode die wordt aangeroepen als er op een bepaalde sprite wordt geklikt
	 */
	public void onFocus() {
	}

	/**
	 * Methode die wordt aangeroepen als de sprite de focus heeft (er was
	 * hiervoor op geklikt) en een andere Sprite krijgt de focus (er wordt op
	 * dit moment op geklikt).
	 */
	public void onLostFocus() {
	}

	/**
	 * Methode die het baandeel tekent.
	 * 
	 * @param g het Graphics object waar op getekent moet worden
	 */
	public void paint(Graphics g) {
		x = BaandeelSprite.offsetX + BaandeelSprite.size * baandeel.getLocatie().x;
		y = BaandeelSprite.offsetY + BaandeelSprite.size * baandeel.getLocatie().y;

		// teken een mooie achtergrond			
		g.setColor(BaanPanel.kleurAchtergrond);
		g.fillRect(x, y, BaandeelSprite.size, BaandeelSprite.size);

		// Moet het baangebruik getekent worden ?
		// LETOP: er wordt weer best veel gerekend (gebeurt voor elk baandeel appart)
		if (BaandeelSprite.filter != null) {
			kleurFilter = BaandeelSprite.filter.getColor(baandeel);
		} else {
			kleurFilter = null;
		}

		paintBaan(g);
		paintTrein(g);
		paintStoplicht(g);
		paintLast(g);
	}

	/**
	 * Voor het tekenen van de baan, als een Baandeel er niet zo uitziet kan hij
	 * overschreven worden.
	 * 
	 * @param g
	 */
	protected void paintBaan(Graphics g) {
		g.setColor(Kleuren.BaandeelAchtergrond);
		g.fillOval(x, y, BaandeelSprite.size - 1, BaandeelSprite.size - 1);
		if (kleurFilter != null) {
			g.setColor(kleurFilter);
			g.fillOval(x, y, BaandeelSprite.size - 1, BaandeelSprite.size - 1);
		}
		g.setColor(kleur);
		g.drawOval(x, y, BaandeelSprite.size - 1, BaandeelSprite.size - 1);
	}

	/**
	 * Als je nadat alles getekent is nog iets willen tekenen, grijp je kans.
	 * 
	 * @param g
	 */
	protected void paintLast(Graphics g) {
	}

	/**
	 * Voor het weergeven van een stoplicht. Mocht je het uiterlijk van het
	 * stoplicht willen veranderen kan je deze overschrijven
	 * 
	 * @param g
	 */
	protected void paintStoplicht(Graphics g) {
		if (!baandeel.isStoplicht()) {
			return;
		}
		g.setColor(Kleuren.BaandeelStoplicht);
		// g.fillRect(x+(realWidth-2)/2, y+(realHeight-2)/2, 2, 2);
		g.fillOval(x + BaandeelSprite.size / 4, y + BaandeelSprite.size / 4, BaandeelSprite.size / 2,
				BaandeelSprite.size / 2);
	}

	/**
	 * Voor het tekenen van de trein op de baan. Mocht je het uiterlijk van de
	 * trein op je baan willen veranderen kan je deze methode overschrijven.
	 * 
	 * @param g
	 */
	protected void paintTrein(Graphics g) {
		if (mijnCab == null) {
			return;
		}
		if (mijnCab.getAantalReizigers() == 0) {
			g.setColor(Kleuren.TreinLeeg);
		} else {
			g.setColor(Kleuren.TreinVol);
		}
		g.fillOval(x, y, BaandeelSprite.size - 1, BaandeelSprite.size - 1);

		g.setColor(kleur);
		g.drawOval(x, y, BaandeelSprite.size - 1, BaandeelSprite.size - 1);
	}

	/**
	 * Geef aan het BaanPanel aan dat je een weiziging hebt en dat je graag
	 * opnieuw getekent wil worden.
	 * 
	 * BaanPanel doet nu niks meer met deze informatie, voor latere
	 * uitbreidingen er toch maar ingelaten.
	 */
	public void repaint() {
		//if (this.bp != null) this.bp.repaint(); //this.paint(bp.getGraphics());
		if (bp != null) {
			bp.setChanged();
		}
	}

	/**
	 * Slaat op op welk BaanPanel deze Sprite getekent moet worden
	 * 
	 * @param bp BaanPaneel waar op getekent zal worden
	 */
	public void setBaanPanel(BaanPanel bp) {
		this.bp = bp;
	}

	/**
	 * Om de baan een geselecteerd kleurtje te geven. Kan gebruikt worden bij
	 * onFocus(). Een voorbeeld hiervan is in StationBaandeelSprite.
	 * 
	 * @param isSelected true - het element is geselecteerd false - het element
	 *            is niet geselecteerd
	 */
	public void setSelected(boolean isSelected) {
		if (isSelected) {
			kleur = Kleuren.BaandeelSelected;
		} else {
			kleur = Kleuren.Baandeel;
		}

		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable observable, Object object) {
		if (object == null || object instanceof RailCab) {
			mijnCab = (RailCab) object;
		} else if (object instanceof Wissel) {
			// wissel is in het model omgezet
		}

		repaint();
	}
}
