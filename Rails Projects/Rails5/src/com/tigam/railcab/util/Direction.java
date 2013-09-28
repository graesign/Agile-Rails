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
 * Een klassen met gestandaardiseerde methode om met richtingen te werken op
 * Point objecten
 * 
 * @author Nils Dijk
 * 
 */

public class Direction {
	/**
	 * Flag voor het Noorden
	 */
	public final static int NORTH = 1;

	/**
	 * Flag voor het Oosten
	 */
	public final static int EAST = 2;

	/**
	 * Flag voor het Zuiden
	 */
	public final static int SOUTH = 4;

	/**
	 * Flag voor het Westen
	 */
	public final static int WEST = 8;

	/**
	 * Rekent de richting uit wat het eerste punt moet gaan om bij het tweede
	 * punt te komen
	 * 
	 * @param p1 Eerste punt
	 * @param p2 Tweede punt
	 * @return De richting dat het eerste punt moet gaan om bij het tweede punt
	 *         te komen
	 */
	public static int getDirection(Point p1, Point p2) {
		int dir = 0;

		// NOORD of ZUID
		if (p1.y > p2.y) {
			dir |= Direction.NORTH;
		} else if (p1.y < p2.y) {
			dir |= Direction.SOUTH;
		}

		// OOST of WEST
		if (p1.x > p2.x) {
			dir |= Direction.WEST;
		} else if (p1.x < p2.x) {
			dir |= Direction.EAST;
		}

		return dir;
	}

	/**
	 * Maak van een bestaand punt een nieuw punt in de gegeven richting
	 * 
	 * @param p Het bestaande punt
	 * @param direction De richting. Richtingen kunnen samengesteld worden met
	 *            een OR operatie.
	 * @return het nieuwe punt
	 */
	public static Point newPoint(Point p, int direction) {
		Point np = new Point(p);

		if ((direction & NORTH) == NORTH) {
			np.y--;
		}
		if ((direction & EAST) == EAST) {
			np.x++;
		}
		if ((direction & SOUTH) == SOUTH) {
			np.y++;
		}
		if ((direction & WEST) == WEST) {
			np.x--;
		}

		return np;
	}
}
