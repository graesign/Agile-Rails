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
import java.util.LinkedList;

import com.tigam.railcab.model.baan.Baandeel;
import com.tigam.railcab.util.Direction;

/**
 * Een hulp middel voor het uitrekenen van nieuwe paden in het grid
 * 
 * @author Nils Dijk
 * 
 */
public class PathFinder {
	/**
	 * Het grid om op te werken
	 */
	private Baandeel[][] grid;

	/**
	 * Het punt waar begonnen moet worden met zoeken
	 */
	private Point start;

	/**
	 * Een grid met de normale route, schuin en alles
	 */
	private Path normalPath[][]; // grid om de kortste route op te slaan

	/**
	 * De toegestane richtingen
	 */
	private int normalRichtingen[] = { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST,
			Direction.NORTH | Direction.EAST, Direction.NORTH | Direction.WEST, Direction.SOUTH | Direction.EAST,
			Direction.SOUTH | Direction.WEST };

	/**
	 * Het grid met de paden die alleen horizontaal en verticaal gaat
	 */
	private Path squarePath[][]; // grid om de kortste route met rechte bochten op te slaan (Hold Shift)

	/**
	 * De toegestane richtingen
	 */
	private int squareRichtingen[] = { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

	/**
	 * De routes van een nieuw station
	 */
	private Path stationPath[][]; // grid met de route voor een station (hold ctrl)

	/**
	 * Maak een Pathfinder en zoek alle drie de routes meteen uit
	 * 
	 * @param grid het grid waarop gezocht moet worden
	 * @param start de start locatie
	 */
	public PathFinder(Baandeel[][] grid, Point start) {
		this.grid = grid;
		this.start = start;

		renderStationPath();
		renderNormalPath();
		renderSquarePath();
	}

	/**
	 * Vraag normale paden op
	 * 
	 * @param stop het stop punt
	 * @param favDirection de favorite aanrijrichting
	 * @return het te maken pad
	 */
	public Path getNormalPath(Point stop, int favDirection) {
		if (stop.x < 0 || stop.y < 0 || stop.x >= grid.length || stop.y >= grid[stop.x].length) {
			return null;
		}
		if (grid[stop.x][stop.y] == null) {
			return normalPath[stop.x][stop.y];
		}
		if (!grid[stop.x][stop.y].getClass().getName().equals(Baandeel.class.getName())) {
			return null;
		}
		if (!grid[stop.x][stop.y].getVolgende().getClass().getName().equals(Baandeel.class.getName())) {
			return null;
		}
		if (!grid[stop.x][stop.y].getVorige().getClass().getName().equals(Baandeel.class.getName())) {
			return null;
		}

		Point favPoint = Direction.newPoint(stop, favDirection);
		if (!isValidNormalRichting(favDirection) || normalPath[favPoint.x][favPoint.y] == null) {
			Path best = null;
			for (int i = 0; i < normalRichtingen.length; i++) {
				Point p = Direction.newPoint(stop, normalRichtingen[i]);
				if (normalPath[p.x][p.y] != null && (best == null || normalPath[p.x][p.y].compareTo(best) == -1)) {
					best = normalPath[p.x][p.y];
				}
			}
			best = new Path(best, stop);
			return best;
		} else {
			return new Path(normalPath[favPoint.x][favPoint.y], stop);
		}
	}

	/**
	 * Vraag een pad op
	 * 
	 * @param stop het laatste punt van de nieuwe pad
	 * @param favDirection de favorite aanrijrichting
	 * @return een mooi pad
	 */
	public Path getSquarePath(Point stop, int favDirection) {
		if (stop.x < 0 || stop.y < 0 || stop.x >= grid.length || stop.y >= grid[stop.x].length) {
			return null;
		}
		if (grid[stop.x][stop.y] == null) {
			return squarePath[stop.x][stop.y];
		}
		if (!grid[stop.x][stop.y].getClass().getName().equals(Baandeel.class.getName())) {
			return null;
		}
		if (!grid[stop.x][stop.y].getVolgende().getClass().getName().equals(Baandeel.class.getName())) {
			return null;
		}
		if (!grid[stop.x][stop.y].getVorige().getClass().getName().equals(Baandeel.class.getName())) {
			return null;
		}

		Point favPoint = Direction.newPoint(stop, favDirection);
		if (!isValidSquareRichting(favDirection) || squarePath[favPoint.x][favPoint.y] == null) {
			Path best = null;
			for (int i = 0; i < squareRichtingen.length; i++) {
				Point p = Direction.newPoint(stop, squareRichtingen[i]);
				if (squarePath[p.x][p.y] != null && (best == null || squarePath[p.x][p.y].compareTo(best) == -1)) {
					best = squarePath[p.x][p.y];
				}
			}
			best = new Path(best, stop);
			return best;
		} else {
			return new Path(squarePath[favPoint.x][favPoint.y], stop);
		}
	}

	/**
	 * Vraag een stationpad op voor een gegeven punt
	 * 
	 * @param p het punt
	 * @return het pad
	 */
	public Path getStationPath(Point p) {
		if (p.x < 0 || p.y < 0 || p.x >= stationPath.length || p.y >= stationPath[p.x].length) {
			return null;
		}
		if (stationPath[p.x][p.y] == null) {
			return null;
		}
		//if (stationGrid[p.x][p.y].length() <= 1) return null;
		return stationPath[p.x][p.y];
	}

	/**
	 * Bepaal of je graag hier een baandeel wil leggen of niet. Als er een
	 * baandeel om je heen ligt wil je dat liever niet
	 * 
	 * @param p het punt waarvan je wil weten of je er een baandeel wilt hebben
	 * @return true - als je er wel wil liggen false - als je er niet wil liggen
	 */
	private boolean heeftVoorkeur(Point p) {
		Point g;
		g = Direction.newPoint(p, Direction.NORTH);
		if (!(g.x < 0 || g.y < 0 || g.x >= grid.length || g.y >= grid[0].length) && grid[g.x][g.y] != null) {
			return false;
		}
		g = Direction.newPoint(p, Direction.EAST);
		if (!(g.x < 0 || g.y < 0 || g.x >= grid.length || g.y >= grid[0].length) && grid[g.x][g.y] != null) {
			return false;
		}
		g = Direction.newPoint(p, Direction.SOUTH);
		if (!(g.x < 0 || g.y < 0 || g.x >= grid.length || g.y >= grid[0].length) && grid[g.x][g.y] != null) {
			return false;
		}
		g = Direction.newPoint(p, Direction.WEST);
		if (!(g.x < 0 || g.y < 0 || g.x >= grid.length || g.y >= grid[0].length) && grid[g.x][g.y] != null) {
			return false;
		}
		g = Direction.newPoint(p, Direction.NORTH | Direction.EAST);
		if (!(g.x < 0 || g.y < 0 || g.x >= grid.length || g.y >= grid[0].length) && grid[g.x][g.y] != null) {
			return false;
		}
		g = Direction.newPoint(p, Direction.NORTH | Direction.WEST);
		if (!(g.x < 0 || g.y < 0 || g.x >= grid.length || g.y >= grid[0].length) && grid[g.x][g.y] != null) {
			return false;
		}
		g = Direction.newPoint(p, Direction.SOUTH | Direction.EAST);
		if (!(g.x < 0 || g.y < 0 || g.x >= grid.length || g.y >= grid[0].length) && grid[g.x][g.y] != null) {
			return false;
		}
		g = Direction.newPoint(p, Direction.SOUTH | Direction.WEST);
		if (!(g.x < 0 || g.y < 0 || g.x >= grid.length || g.y >= grid[0].length) && grid[g.x][g.y] != null) {
			return false;
		}
		return true;
	}

	/**
	 * Kijk of een gegeven richting geldig is voor een normale route
	 * 
	 * @param direction de richting
	 * @return <b>true</b> - Hij is geldig<br>
	 *         <b>false</b> - Hij is niet geldig
	 */
	private boolean isValidNormalRichting(int direction) {
		for (int i = 0; i < normalRichtingen.length; i++) {
			if (normalRichtingen[i] == direction) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Kijk of een gegeven richting geldig is voor een rechte route
	 * 
	 * @param direction de richting
	 * @return <b>true</b> - Hij is geldig<br>
	 *         <b>false</b> - Hij is niet geldig
	 */
	private boolean isValidSquareRichting(int direction) {
		for (int i = 0; i < squareRichtingen.length; i++) {
			if (squareRichtingen[i] == direction) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Render het normale pad
	 */
	private void renderNormalPath() {
		if (grid.length == 0 || grid[0].length == 0) {
			return;
		}
		normalPath = new Path[grid.length][grid[0].length];

		if (start.x < 0 || start.y < 0 || start.x >= grid.length || start.y >= grid[start.x].length
				|| grid[start.x][start.y] == null) {
			return;
		}
		if (!grid[start.x][start.y].getClass().getName().equals(Baandeel.class.getName())) {
			return;
		}
		if (!grid[start.x][start.y].getVolgende().getClass().getName().equals(Baandeel.class.getName())) {
			return;
		}
		if (!grid[start.x][start.y].getVorige().getClass().getName().equals(Baandeel.class.getName())) {
			return;
		}

		Path r, rn;
		Point p, np;
		r = new Path(start);
		normalPath[start.x][start.y] = r;

		LinkedList<Point> pointsToEvaluate = new LinkedList<Point>();
		pointsToEvaluate.add(start);

		while (pointsToEvaluate.size() > 0) {
			p = pointsToEvaluate.poll();
			r = normalPath[p.x][p.y];
			for (int i = 0; i < normalRichtingen.length; i++) {
				np = Direction.newPoint(p, normalRichtingen[i]);
				if (np.x < 0 || np.y < 0 || np.x >= normalPath.length || np.y >= normalPath[0].length) {
					continue;
				}
				if (grid[np.x][np.y] != null) {
					continue;
				}

				rn = new Path(r, np);
				if (!heeftVoorkeur(np)) {
					rn.geenVoorkeur();
				}

				if (normalPath[np.x][np.y] == null || rn.compareTo(normalPath[np.x][np.y]) == -1) {
					normalPath[np.x][np.y] = rn;
					pointsToEvaluate.add(np);
				}
			}
		}
	}

	/**
	 * Render het grid dat alleen maar horizontaal en verticaal mag
	 */
	private void renderSquarePath() {
		if (grid.length == 0 || grid[0].length == 0) {
			return;
		}
		squarePath = new Path[grid.length][grid[0].length];

		if (start.x < 0 || start.y < 0 || start.x >= grid.length || start.y >= grid[start.x].length
				|| grid[start.x][start.y] == null) {
			return;
		}
		if (!grid[start.x][start.y].getClass().getName().equals(Baandeel.class.getName())) {
			return;
		}
		if (!grid[start.x][start.y].getVolgende().getClass().getName().equals(Baandeel.class.getName())) {
			return;
		}
		if (!grid[start.x][start.y].getVorige().getClass().getName().equals(Baandeel.class.getName())) {
			return;
		}

		Path r, rn;
		Point p, np;
		r = new Path(start);
		squarePath[start.x][start.y] = r;

		LinkedList<Point> pointsToEvaluate = new LinkedList<Point>();
		pointsToEvaluate.add(start);

		while (pointsToEvaluate.size() > 0) {
			p = pointsToEvaluate.poll();
			r = squarePath[p.x][p.y];
			for (int i = 0; i < squareRichtingen.length; i++) {
				np = Direction.newPoint(p, squareRichtingen[i]);
				if (np.x < 0 || np.y < 0 || np.x >= squarePath.length || np.y >= squarePath[0].length) {
					continue;
				}
				if (grid[np.x][np.y] != null) {
					continue;
				}

				rn = new Path(r, np);
				if (!heeftVoorkeur(np)) {
					rn.geenVoorkeur();
				}

				if (squarePath[np.x][np.y] == null || rn.compareTo(squarePath[np.x][np.y]) == -1) {
					squarePath[np.x][np.y] = rn;
					pointsToEvaluate.add(np);
				}
			}
		}
	}

	/**
	 * Render de station paden
	 */
	private void renderStationPath() {

		if (grid.length == 0 || grid[0].length == 0) {
			return;
		}
		stationPath = new Path[grid.length][grid[0].length];

		if (start.x < 0 || start.y < 0 || start.x >= grid.length || start.y >= grid[start.x].length
				|| grid[start.x][start.y] == null) {
			return;
		}
		if (!grid[start.x][start.y].getClass().getName().equals(Baandeel.class.getName())) {
			return;
		}
		if (!grid[start.x][start.y].getVolgende().getClass().getName().equals(Baandeel.class.getName())) {
			return;
		}
		if (!grid[start.x][start.y].getVorige().getClass().getName().equals(Baandeel.class.getName())) {
			return;
		}

		Path r, rn;
		Point p, pn;
		Baandeel b;

		r = new Path(start);
		stationPath[start.x][start.y] = r;

		LinkedList<Point> pointsToEvaluate = new LinkedList<Point>();
		pointsToEvaluate.add(start);

		while (pointsToEvaluate.size() > 0) {
			p = pointsToEvaluate.poll();

			//System.out.println("EVALUATE: " + p);

			r = stationPath[p.x][p.y];

			// volgende baandeel;
			b = grid[p.x][p.y].getVolgende();
			// controleer of er wel een Station op dat baandeel mag, of de delen ernaast dus vrij zijn.
			if (b.getClass().getName().equals(Baandeel.class.getName())
					&& b.getVolgende().getClass().getName().equals(Baandeel.class.getName())
					&& b.getVorige().getClass().getName().equals(Baandeel.class.getName())) {
				pn = b.getLocatie();
				rn = new Path(r, pn);
				if (stationPath[pn.x][pn.y] == null || rn.compareTo(stationPath[pn.x][pn.y]) == -1) {
					stationPath[pn.x][pn.y] = rn;
					pointsToEvaluate.add(pn);
				}
			}

			// vorige baandeel;
			b = grid[p.x][p.y].getVorige();
			// controleer of er wel een Station op dat baandeel mag, of de delen ernaast dus vrij zijn.
			if (b.getClass().getName().equals(Baandeel.class.getName())
					&& b.getVolgende().getClass().getName().equals(Baandeel.class.getName())
					&& b.getVorige().getClass().getName().equals(Baandeel.class.getName())) {
				pn = b.getLocatie();
				rn = new Path(r, pn);
				if (stationPath[pn.x][pn.y] == null || rn.compareTo(stationPath[pn.x][pn.y]) == -1) {
					stationPath[pn.x][pn.y] = rn;
					pointsToEvaluate.add(pn);
				}
			}
		}
	}

}
