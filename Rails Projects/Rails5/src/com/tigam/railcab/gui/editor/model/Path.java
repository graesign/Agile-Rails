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

import com.tigam.railcab.util.Direction;

/**
 * Een pad object voor nieuwe baandelen
 * 
 * @author Nils Dijk
 * 
 */
public class Path implements Comparable<Path> {
	/**
	 * Alle baandelen in het pad, op goede volgorde
	 */
	private ArrayList<Point> path;

	/**
	 * de lengte van het pad
	 */
	private int length = 0;

	/**
	 * Maak een laag pad object aan
	 */
	public Path() {
		this(null, null);
	}

	/**
	 * Maak een pad object aan uit een oud pad object
	 * 
	 * @param r oude pad object
	 */
	public Path(Path r) {
		this(r, null);
	}

	/**
	 * maak een nieuw pad object aan uit een oud obejct en voeg meteen een nieuw
	 * punt toe
	 * 
	 * @param r oude pad object
	 * @param p nieuw punt
	 */
	public Path(Path r, Point p) {
		path = new ArrayList<Point>();
		if (r != null) {
			ArrayList<Point> o = r.getPath();
			for (int i = 0; i < o.size(); i++) {
				path.add(o.get(i));
			}
			length = r.length();
		}
		if (p != null) {
			add(p);
		}
	}

	/**
	 * Maak een Padobject aan met 1 punt
	 * 
	 * @param p nieuw punt
	 */
	public Path(Point p) {
		this(null, p);
	}

	/**
	 * Voeg een nieuw punt toe
	 * 
	 * @param p het nieuwe punt
	 */
	public void add(Point p) {
		path.add(new Point(p));
		length++;
	}

	public int compareTo(Path p) {
		if (p == null) {
			return 1;
		}

		int l1 = length();
		int l2 = p.length();

		if (l1 == l2) {

			int b1 = getAantalBochten();
			int b2 = p.getAantalBochten();

			if (b1 == b2) {
				return 0;
			} else if (b1 > b2) {
				return 1;
			} else if (b1 < b2) {
				return -1;
			}
		} else if (l1 > l2) {
			return 1;
		} else if (l1 < l2) {
			return -1;
		}
		return 1;
	}

	/**
	 * Geef aan de het pad minder voorkeur geniet dan andere paden
	 */
	public void geenVoorkeur() {
		length += 1;
	}

	/**
	 * Bereken het aantal bochten in een pad
	 * 
	 * @return het aantal bochten
	 */
	public int getAantalBochten() {
		if (path.size() < 2) {
			return 0;
		}
		int olddir, dir = Direction.getDirection(path.get(0), path.get(1));
		int bochtCount = 0;
		for (int i = 1; i < path.size() - 1; i++) {
			olddir = dir;
			dir = Direction.getDirection(path.get(i), path.get(i + 1));
			if (olddir != dir) {
				bochtCount++;
			}
		}
		return bochtCount;
	}

	/**
	 * Vraag het laatste punt op van het pad
	 * 
	 * @return het laatste punt
	 */
	public Point getLastPoint() {
		return path.get(path.size() - 1);
	}

	/**
	 * Vraag een lijst met alle punten op uit het pad
	 * 
	 * @return een lijst met alle punten
	 */
	public ArrayList<Point> getPath() {
		return path;
	}

	/**
	 * Vraag de lengte van het pad op
	 * 
	 * @return de lengte
	 */
	public int length() {
		return length;
	}

	@Override
	public String toString() {
		String s = "ROUTE";

		for (int i = 0; i < path.size(); i++) {
			s += "\n" + path.get(i);
		}
		return s;
	}
}
