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

package com.tigam.railcab.gui.editor.model;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Een object om geselecteerde punten te groeperen
 * 
 * @author Nils Dijk
 * 
 */
public class Selection {

	/**
	 * Lijst met geselecteerde punten
	 */
	protected ArrayList<Point> selection;

	/**
	 * Maak een nieuwe selectie aan
	 */
	public Selection() {
		init();
	}

	/**
	 * Maak de selectie aan met een punt
	 * 
	 * @param p het punt dat geselecteerd moet worden
	 */
	public Selection(Point p) {
		init();
		this.add(p);
	}

	/**
	 * Maak een nieuwe selectie aan de hand van een oude selectie
	 * 
	 * @param oud de oude selectie
	 */
	public Selection(Selection oud) {
		init();
		this.add(oud);
	}

	/**
	 * Maak een nieuwe selectie aan de hand van een oude selectie
	 * 
	 * @param oud de oude selectie
	 * @param p nieuw punt er meteen bij
	 */
	public Selection(Selection oud, Point p) {
		init();
		this.add(oud);
		this.add(p);
	}

	/**
	 * Voeg een punt toe aan de selectie
	 * 
	 * @param p het punt
	 */
	public void add(Point p) {
		if (p == null) {
			return;
		}
		if (selection.contains(p)) {
			return;
		}
		selection.add(new Point(p));
	}

	/**
	 * Voeg een selectie object toe aan de huidige selectie
	 * 
	 * @param s de toe te voegen selectie
	 */
	public void add(Selection s) {
		if (s == null) {
			return;
		}
		ArrayList<Point> pl = s.getSelection();
		for (int i = 0; i < pl.size(); i++) {
			this.add(pl.get(i));
		}
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Selection)) {
			return false;
		}
		Selection s = (Selection) o;
		if (s == this) {
			return true;
		}
		if (!s.isInSelection(this)) {
			return false;
		}
		if (!this.isInSelection(s)) {
			return false;
		}
		return true;
	}

	/**
	 * Vraag een lijst met alle geselecteerde punten op
	 * 
	 * @return de lijst met alle geselecteerde punten
	 */
	public ArrayList<Point> getSelection() {
		return selection;
	}

	/**
	 * initializeer het object
	 */
	private void init() {
		selection = new ArrayList<Point>();
	}

	/**
	 * Kijk of een selectie leeg is
	 * 
	 * @return <b>true</b> - hij is leeg<br>
	 *         <b>false</b> - hij is niet leeg
	 */
	public boolean isEmpty() {
		return selection.isEmpty();
	}

	/**
	 * Kijk of een gegeven punt voor komt in een selectie
	 * 
	 * @param p het voor te komen punt
	 * @return <b>true</b> - hij komt voor<br>
	 *         <b>false</b> - hij komt niet voor
	 */
	public boolean isInSelection(Point p) {
		return selection.contains(p);
	}

	/**
	 * Kijk of een hele selectie in deze selectie voor komt
	 * 
	 * @param s de voor te komen selectie
	 * @return <b>true</b> - hij komt voor<br>
	 *         <b>false</b> - hij komt niet voor
	 */
	public boolean isInSelection(Selection s) {
		if (s == null) {
			return false;
		}
		ArrayList<Point> punten = s.getSelection();
		for (int i = 0; i < punten.size(); i++) {
			if (!this.isInSelection(punten.get(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Verwijder een punt uit de huidige selectie
	 * 
	 * @param p het te verwijderen punt
	 */
	public void remove(Point p) {
		if (p == null) {
			return;
		}
		selection.remove(p);
	}

	/**
	 * Verwijder een Selectie object uit de huidige selectie
	 * 
	 * @param s het te verwijderen selectie object
	 */
	public void remove(Selection s) {
		if (s == null) {
			return;
		}
		ArrayList<Point> punten = s.getSelection();
		for (int i = 0; i < punten.size(); i++) {
			this.remove(punten.get(i));
		}
	}

	@Override
	public String toString() {
		String text;
		text = "Selection of " + selection.size() + " points\n";
		text += "-------------------------\n";
		for (int i = 0; i < selection.size(); i++) {
			text += selection.get(i) + "\n";
		}
		text += "-------------------------";
		return text;
	}
}
