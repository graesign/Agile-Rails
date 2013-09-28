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
import java.util.HashMap;
import java.util.LinkedList;

import com.tigam.railcab.model.baan.Baandeel;
import com.tigam.railcab.model.baan.StationBaandeel;
import com.tigam.railcab.model.baan.Wissel;
import com.tigam.railcab.model.baan.WisselTerug;

/**
 * Een klasse om het een en ander te vinden aan selectie mogelijkheden en
 * verwijdermogelijkheden
 * 
 * @author Nils Dijk
 * 
 */
public class Finder {

	/**
	 * Bereken de selectie van baan tot baan
	 * 
	 * @param b1
	 * @param b2
	 * @return een selectie
	 */
	public static Selection BaanToBaan(Baandeel b1, Baandeel b2) {
		HashMap<Baandeel, Selection> selectionMap = new HashMap<Baandeel, Selection>();
		LinkedList<Baandeel> baanToDo = new LinkedList<Baandeel>();

		Baandeel b;
		Selection s;

		selectionMap.put(b1, new Selection(b1.getLocatie()));
		baanToDo.add(b1);

		while (baanToDo.size() > 0) {
			b = baanToDo.poll();
			s = selectionMap.get(b);
			if (!selectionMap.containsKey(b.getVolgende())) {
				selectionMap.put(b.getVolgende(), new Selection(s, b.getVolgende().getLocatie()));
				baanToDo.add(b.getVolgende());

				if (b instanceof Wissel && !(b instanceof WisselTerug)) {
					if (!selectionMap.containsKey(((Wissel) b).getAndere())) {
						selectionMap.put(((Wissel) b).getAndere(), new Selection(s, ((Wissel) b).getAndere()
								.getLocatie()));
						baanToDo.add(((Wissel) b).getAndere());
					}
				}
			}

			if (!selectionMap.containsKey(b.getVorige())) {
				selectionMap.put(b.getVorige(), new Selection(s, b.getVorige().getLocatie()));
				baanToDo.add(b.getVorige());

				if (b instanceof WisselTerug) {
					if (!selectionMap.containsKey(((WisselTerug) b).getAndere())) {
						selectionMap.put(((WisselTerug) b).getAndere(), new Selection(s, ((WisselTerug) b).getAndere()
								.getLocatie()));
						baanToDo.add(((WisselTerug) b).getAndere());
					}
				}
			}
		}

		return selectionMap.get(b2);

	}

	/**
	 * Berekent een selectie is die geldig is om te verwijderen.
	 * 
	 * @param baan Het baandeel wat zeker in de selectie voor moet komen
	 * @return De selectie die veilig verwijdert kan worden
	 */
	public static Selection deletable(Baandeel baan) {
		Selection s = new Selection(baan.getLocatie());
		Baandeel b;
		if (baan instanceof StationBaandeel) {
			b = baan.getVolgende();
			while (b instanceof StationBaandeel) {
				s.add(b.getLocatie());
				b = b.getVolgende();
			}
			b = baan.getVorige();
			while (b instanceof StationBaandeel) {
				s.add(b.getLocatie());
				b = b.getVorige();
			}
		} else {

			LinkedList<Baandeel> banenToDo = new LinkedList<Baandeel>();
			ArrayList<Wissel> wisselsHad = new ArrayList<Wissel>();

			// dan gaan we nu eerst naar voren
			banenToDo.add(baan.getVolgende());
			if (baan instanceof Wissel && !(baan instanceof WisselTerug)) {
				banenToDo.add(((Wissel) baan).getAndere());
			}
			while (banenToDo.size() > 0) {
				b = banenToDo.poll();
				if (s.isInSelection(b.getLocatie())) {
					continue;
				}
				if (b instanceof WisselTerug) {
					if (wisselsHad.contains(b)) { // wissel komen we nu voor de tweede keer, nu moet je wel verder
						s.add(b.getLocatie());
						banenToDo.add(b.getVolgende());
					} else {
						wisselsHad.add((Wissel) b);
					}
					//TODO: zometeen kijken of ik er al een keer langs ben gekomen
					continue;
				} else if (b instanceof Wissel) {
					s.add(b.getLocatie());
					banenToDo.add(b.getVolgende());
					banenToDo.add(((Wissel) b).getAndere());
				} else {
					s.add(b.getLocatie());
					banenToDo.add(b.getVolgende());
				}
			}

			// dan gaan we nu eerst naar voren
			banenToDo.add(baan.getVorige());
			if (baan instanceof WisselTerug) {
				banenToDo.add(((Wissel) baan).getAndere());
			}
			while (banenToDo.size() > 0) {
				b = banenToDo.poll();
				if (s.isInSelection(b.getLocatie())) {
					continue;
				}
				if (b instanceof WisselTerug) {
					s.add(b.getLocatie());
					banenToDo.add(b.getVorige());
					banenToDo.add(((Wissel) b).getAndere());
				} else if (b instanceof Wissel) {
					if (wisselsHad.contains(b)) { // wissel komen we nu voor de tweede keer, nu moet je wel verder
						s.add(b.getLocatie());
						banenToDo.add(b.getVorige());
					} else {
						wisselsHad.add((Wissel) b);
					}
				} else {
					s.add(b.getLocatie());
					banenToDo.add(b.getVorige());
				}
			}
		}

		return s;
	}

	/**
	 * Maak van een selectie een verwijderbare selectie
	 * 
	 * @param grid het grid waar de selectie zich op bevindt
	 * @param selection de gegeven selectie
	 * @return verwijderbare selectie
	 */
	public static Selection deletable(Baandeel[][] grid, Selection selection) {
		// zoek een baandeel dat de zelfde selectie maakt, of meer natuurlijk als de selectie niet gerenderd is door software
		Selection s = new Selection(selection);
		ArrayList<Baandeel> verwijder = new ArrayList<Baandeel>();

		while (!s.isEmpty()) {
			Point p = s.getSelection().get(0);
			Baandeel b = grid[p.x][p.y];
			Selection s1 = Finder.deletable(b);
			verwijder.add(b);
			s.remove(s1);
		}

		for (int i = 0; i < verwijder.size(); i++) {
			s.add(Finder.deletable(verwijder.get(i)));
		}
		return s;
	}

	/**
	 * Zoek uit welke baandelen verwijdert moeten worden als je een gegeven
	 * selectie wilt verwijderen
	 * 
	 * @param grid het grid waarop de baan zich bevindt.
	 * @param selection de selectie
	 * @return Een Array van baandelen die verwijdert moeten worden uit het
	 *         model.
	 */
	public static ArrayList<Baandeel> getBaandelenToDelete(Baandeel[][] grid, Selection selection) {
		// zoek een baandeel dat de zelfde selectie maakt, of meer natuurlijk als de selectie niet gerenderd is door software
		Selection s = new Selection(selection);
		ArrayList<Baandeel> verwijder = new ArrayList<Baandeel>();

		while (!s.isEmpty()) {
			Point p = s.getSelection().get(0);
			Baandeel b = grid[p.x][p.y];
			Selection s1 = Finder.deletable(b);
			verwijder.add(b);
			s.remove(s1);
		}

		return verwijder;
	}

	/**
	 * Zoek uit of een bepaalde selectie verwijderbaar is ofdat er meer
	 * verwijdert moet worden
	 * 
	 * @param grid het grid waarop de baan zich bevindt
	 * @param selection de selectie
	 * @return <b>true</b> - Hij is verwijderbaar<br>
	 *         <b>false</b> - Hij is niet verwijderbaar
	 */
	public static boolean isSelectionDeletable(Baandeel[][] grid, Selection selection) {
		Selection s = new Selection(selection);
		ArrayList<Baandeel> verwijder = new ArrayList<Baandeel>();

		while (!s.isEmpty()) {
			Point p = s.getSelection().get(0);
			Baandeel b = grid[p.x][p.y];
			Selection s1 = Finder.deletable(b);
			verwijder.add(b);
			s.remove(s1);
		}

		Selection finalSelection = new Selection();
		for (int i = 0; i < verwijder.size(); i++) {
			finalSelection.add(Finder.deletable(verwijder.get(i)));
		}

		return selection.equals(finalSelection);
	}

	/**
	 * Bereken een geldig omkeerbaarstuk aan de hand van een baandeel
	 * 
	 * @param baan het baandeel
	 * @return selectie die omkeerbaar is
	 */
	public static Selection omkeerbaar(Baandeel baan) {
		Selection s = new Selection(baan.getLocatie());
		Baandeel b;

		b = baan.getVolgende();
		while (!(b instanceof WisselTerug) && !s.isInSelection(b.getLocatie())) {
			s.add(b.getLocatie());
			b = b.getVolgende();
		}

		b = baan.getVorige();
		while ((!(b instanceof Wissel) || b instanceof WisselTerug) && !s.isInSelection(b.getLocatie())) {
			s.add(b.getLocatie());
			b = b.getVorige();
		}

		return s;
	}
}
