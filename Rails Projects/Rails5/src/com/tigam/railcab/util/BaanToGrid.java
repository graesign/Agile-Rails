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
import java.util.ArrayList;
import java.util.LinkedList;

import com.tigam.railcab.model.baan.Baandeel;
import com.tigam.railcab.model.baan.Wissel;

/**
 * 
 * Een klasse met gestandaardiseerde methode om een baan naar een grid om te
 * zetten
 * 
 * @author Nils Dijk
 * 
 */

public class BaanToGrid {

	/**
	 * Maak een grid dat passend is om de baan<br>
	 * <br>
	 * <b>LETOP:</b><br>
	 * andere grids van de zelfde baan kunnen niet meer kloppen na het aanroepen
	 * van deze methode. De interne lokatie van de baan wordt aangepast op het
	 * grid dat nu gemaakt wordt
	 * 
	 * @param baan De baan waar het grid van gemaakt moet worden
	 * @return Een twee-dimensionale array van de baan die het grid voorstelt
	 */
	public static Baandeel[][] baanToGrid(Baandeel baan) {
		return BaanToGrid.baanToGrid(baan, 0, 0, 0, 0);
	}

	/**
	 * Maak een grid van de baan met opgegeven ruimte aan de zijkant<br>
	 * <br>
	 * <b>LETOP:</b><br>
	 * andere grids van de zelfde baan kunnen niet meer kloppen na het aanroepen
	 * van deze methode. De interne lokatie van de baan wordt aangepast op het
	 * grid dat nu gemaakt wordt
	 * 
	 * @param baan De baan waar het grid van gemaakt moet worden
	 * @param marginLeft Ruimte aan de linkerkant
	 * @param marginRight Ruimte aan de rechterkant
	 * @param marginTop Ruimte aan de bovenkant
	 * @param marginBottom Ruimte aan de onderkant
	 * @return Een twee-dimensionale array van de baan die het grid voorstelt
	 */
	public static Baandeel[][] baanToGrid(Baandeel baan, int marginLeft, int marginRight, int marginTop,
			int marginBottom) {
		MinMax mm = new MinMax();

		Baandeel working;
		LinkedList<Baandeel> banenToDo = new LinkedList<Baandeel>();
		banenToDo.add(baan);

		ArrayList<Baandeel> alleBanen = new ArrayList<Baandeel>();

		while (banenToDo.size() > 0) {
			working = banenToDo.poll();
			while (!alleBanen.contains(working)) {
				alleBanen.add(working);
				mm.add(working.getLocatie());
				if (working.getClass().getName().equals(Wissel.class.getName())) {
					banenToDo.add(((Wissel) working).getAndere());
				}
				working = working.getVolgende();
			}
		}

		Baandeel[][] grid = new Baandeel[mm.maxX - mm.minX + marginLeft + marginRight + 1][mm.maxY - mm.minY
				+ marginTop + marginBottom + 1];

		for (int i = 0; i < alleBanen.size(); i++) {
			working = alleBanen.get(i);
			Point p = working.getLocatie();
			p.x = p.x - mm.minX + marginLeft;
			p.y = p.y - mm.minY + marginTop;
			grid[p.x][p.y] = working;
		}

		return grid;
	}
}
