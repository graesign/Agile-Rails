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

/**
 * Een abstractie laag van BaandeelSprites, Zowel de nieuwe als de bestaande
 * stammen hier vanaf, maar ze hebben niet allemaal de zelfde functies
 * Bijvoorbeeld de showRichting dingen
 * 
 * @author Nils Dijk
 * 
 */
public abstract class AbstractBaandeelSprite {
	/**
	 * De offset van het grid op de X-as
	 */
	protected static int offsetX;

	/**
	 * De offset van het grid op de Y-as
	 */
	protected static int offsetY;

	/**
	 * De grote van elk vakje op het grid
	 */
	protected static int size;

	/**
	 * Verander de offset van het grid op de X-as
	 * 
	 * @param offsetX de offset
	 */
	public static void setOffsetX(int offsetX) {
		AbstractBaandeelSprite.offsetX = offsetX;
	}

	/**
	 * Verander de offset van het grid op de Y-as
	 * 
	 * @param offsetY de offset
	 */
	public static void setOffsetY(int offsetY) {
		AbstractBaandeelSprite.offsetY = offsetY;
	}

	/**
	 * Verander de grote van het grid
	 * 
	 * @param size
	 */
	public static void setSize(int size) {
		AbstractBaandeelSprite.size = size;
	}

	/**
	 * De kleur van het rondje om het baandeel
	 */
	protected Color kleur = Kleuren.Baandeel;

	/**
	 * Moet de rijrichting weergegeven worden?
	 */
	protected boolean showRichting = false;

	/**
	 * Teken het Baandeel op een Graphics object
	 * 
	 * @param g Graphics object om op te tekenen
	 */
	public abstract void paint(Graphics g);

	/**
	 * Het fysiek tekenen van de baan, overschrijfbaar door subklassen
	 * 
	 * @param g Graphics object om op te tekenen
	 */
	protected abstract void paintBaan(Graphics g);

	/**
	 * Moet de rijrichting weergegeven worden ?
	 * 
	 * @param show
	 */
	public void showRichting(boolean show) {
		showRichting = show;
	}
}
