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

package com.tigam.railcab.util;

import java.awt.Point;

/**
 * 
 * Een klasse voor het simpel bijhouden van de MinMax op een XY grid.
 * 
 * @author Nils Dijk
 * 
 */
public class MinMax {
	/**
	 * De laagste waarde op de X-as
	 */
	public int minX = 0;

	/**
	 * De hoogste waarde op de X-as
	 */
	public int maxX = 0;

	/**
	 * De laagste waarde op de Y-as
	 */
	public int minY = 0;

	/**
	 * De hoogste waarde op de Y-as
	 */
	public int maxY = 0;

	/**
	 * is er al wat gevonden op de X-as
	 */
	private boolean isMinX = false;

	/**
	 * is er al wat gevonden op de Y-as
	 */
	private boolean isMinY = false;

	/**
	 * Constructor hoeft niets te doen :)
	 */
	public MinMax() {
	}

	/**
	 * Verwerk tegelijkertijd een X en een Y waarde
	 * 
	 * @param x
	 * @param y
	 */
	public void add(int x, int y) {
		addX(x);
		addY(y);
	}

	/**
	 * Verwerk een Point.
	 * 
	 * @param d het punt dat je wil verwerken in de MinMax
	 */
	public void add(Point d) {
		addX(d.x);
		addY(d.y);
	}

	/**
	 * Voor het verwerken van de X waarde
	 * 
	 * @param x
	 */
	public void addX(int x) {
		if (minX > x || !isMinX) {
			minX = x;
			isMinX = true;
		}
		if (maxX < x) {
			maxX = x;
		}
	}

	/**
	 * Voor het verwerken van de Y waarde
	 * 
	 * @param y
	 */
	public void addY(int y) {
		if (minY > y || !isMinY) {
			minY = y;
			isMinY = true;
		}
		if (maxY < y) {
			maxY = y;
		}
	}

	@Override
	public String toString() {
		return "minX: " + minX + " maxX: " + maxX + "\nminY: " + minY + " maxY: " + maxY;
	}
}