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
import java.util.LinkedList;
import java.util.Observable;

import com.tigam.railcab.gui.editor.Editor;
import com.tigam.railcab.gui.editor.NamePanel;
import com.tigam.railcab.language.Language;
import com.tigam.railcab.model.baan.BaanHeader;
import com.tigam.railcab.model.baan.Baandeel;
import com.tigam.railcab.model.baan.Station;
import com.tigam.railcab.model.baan.StationBaandeel;
import com.tigam.railcab.model.baan.Wissel;
import com.tigam.railcab.model.baan.WisselTerug;
import com.tigam.railcab.model.util.CloneModel;
import com.tigam.railcab.util.BaanLoader;
import com.tigam.railcab.util.BaanToGrid;
import com.tigam.railcab.util.Direction;

/**
 * Controler van de Editor
 * 
 * @author Nils Dijk
 * 
 */
public class Baan extends Observable {

	/**
	 * Standaard breedte van een nieuwe baan
	 */
	private static final int standaarBreedte = 20;

	/**
	 * Standaard hoogte van een niewe baan
	 */
	private static final int standaarHoogte = 20;

	/**
	 * De baan
	 */
	private Baandeel baan;

	/**
	 * De naam van de baan
	 */
	private String naam = "";

	/**
	 * het grid waar op gewerkt kan worden
	 */
	private Baandeel[][] grid;

	/**
	 * Een stack met alle weizigingen
	 */
	private ArrayList<Baandeel> geschiedenis;

	/**
	 * Pointer naar de huidige versie van de geschiedenis
	 */
	private int geschiedenisPointer = -1;

	/**
	 * Standaard ruimte die aan de linkerkant over is
	 */
	private int defaultMarginLeft = 2;

	/**
	 * Standaard ruimte die aan de rechterkant over is
	 */
	private int defaultMarginRight = 2;

	/**
	 * Standaard ruimte die aan de bovenkant over is
	 */
	private int defaultMarginTop = 2;

	/**
	 * Standaard ruimte die aan de onderkant over is
	 */
	private int defaultMarginBottom = 2;

	/**
	 * Het frame waar de editor in zit
	 */
	private Editor editorFrame = null;

	/**
	 * Een namepanel
	 */
	private NamePanel namePanel = null;

	/**
	 * Maak een standaard baan van 15 bij 15 baandelen.
	 */
	public Baan() {
		maakNieuweBaan(Baan.standaarBreedte, Baan.standaarHoogte);
	}

	/**
	 * Constructor, Laad een baan aan de hand van een BaanHeader Object
	 * 
	 * @param baanHeader object waar de baan van geladen wordt
	 */
	public Baan(BaanHeader baanHeader) {
		this();
		if (baanHeader == null) {
			maakNieuweBaan(Baan.standaarBreedte, Baan.standaarHoogte);
		} else {
			laadBaan(baanHeader);
		}
	}

	/**
	 * Maak een baan met opgegeven hoogte en breedte (maakt altijd een
	 * rechthoek)
	 * 
	 * @param width - De breedte van de nieuwe baan
	 * @param height - De hoogte van de nieuwe baan
	 */
	public Baan(int width, int height) {
		maakNieuweBaan(width, height);
	}

	/**
	 * Constructor, Laad een baan uit bestand
	 * 
	 * @param filename de bestandsnaam
	 */
	public Baan(String filename) {
		this();
		BaanHeader baanHeader = BaanLoader.loadBaan(filename);
		if (baanHeader == null) {
			maakNieuweBaan(Baan.standaarBreedte, Baan.standaarHoogte);
		} else {
			laadBaan(baanHeader);
		}
	}

	/**
	 * Maak een nieuwstuk baandeel aan de hand van een opgegeven Path.
	 * 
	 * @param p Het Path wat de baan moet volgen
	 */
	public void createPath(Path p) {
		if (p == null) {
			return;
		}
		ArrayList<Point> r = p.getPath();
		if (r == null || r.size() < 2 || r.get(0).equals(r.get(r.size() - 1))) {
			return;
		}

		Point start = r.get(0);
		Point stop = r.get(r.size() - 1);

		if (grid[start.x][start.y] == null) {
			return;
		}
		if (grid[stop.x][stop.y] == null) {
			return;
		}

		Point q;
		for (int i = 1; i < r.size() - 1; i++) {
			q = r.get(i);
			if (grid[q.x][q.y] != null) {
				return;
			}
		}

		plaatsOpGeschiedenis();

		try {

			Baandeel baanStart = grid[start.x][start.y];
			if (baanStart == baan) {
				baan = baan.getVolgende();
			}
			Wissel wisselStart = new Wissel(start);
			wisselStart.setVolgende(baanStart.getVolgende());
			wisselStart.setVorige(baanStart.getVorige());
			//			this.grid[start.x][start.y] = wisselStart;

			Baandeel baanStop = grid[stop.x][stop.y];
			if (baanStop == baan) {
				baan = baan.getVolgende();
			}
			WisselTerug wisselStop = new WisselTerug(stop);
			wisselStop.setVolgende(baanStop.getVolgende());
			wisselStop.setVorige(baanStop.getVorige());
			//			this.grid[stop.x][stop.y] = wisselStop;

			Baandeel b = wisselStart;
			wisselStart.zetOm();
			Baandeel b1;
			for (int i = 1; i < r.size() - 1; i++) {
				q = r.get(i);
				b1 = b;
				b = new Baandeel(q);
				b.setVorige(b1);
				//				this.grid[q.x][q.y] = b;
			}
			wisselStop.zetOm();
			wisselStop.setVorige(b);
			wisselStop.zetOm();
			wisselStart.zetOm();
		} catch (Exception ex) {
			System.out.println();
		}

		renderGrid();
	}

	/**
	 * Maak een nieuw Station aan de hand van een opgegeven Path met een gegeven
	 * naam
	 * 
	 * @param p Het Path wat het Station moet volgen
	 * @param stationNaam De naam van het nieuwe Station
	 */
	public void createStation(Path p, String stationNaam) {
		ArrayList<Point> r = p.getPath();
		if (r.size() == 0) {
			return;
		}
		Point n;
		for (int i = 0; i < r.size(); i++) {
			n = r.get(i);
			if (!grid[n.x][n.y].getClass().getName().equals(Baandeel.class.getName())) {
				return;
			}
		}

		plaatsOpGeschiedenis();

		Baandeel b;
		StationBaandeel s = null;

		for (int i = 0; i < r.size(); i++) {
			n = r.get(i);
			b = grid[n.x][n.y];
			s = new StationBaandeel(n);
			s.setVolgende(b.getVolgende());
			s.setVorige(b.getVorige());
			//			this.grid[n.x][n.y] = s;

			if (b == baan) {

				do {
					b = b.getVolgende();
				} while (b instanceof StationBaandeel);
				baan = b;
			}
		}

		while (s != null && s.getVorige() instanceof StationBaandeel) {
			s = (StationBaandeel) s.getVorige();
		}

		new Station(stationNaam, s);

		renderGrid();
	}

	/**
	 * Verwijder alle Baandelen die in de array zitten
	 * 
	 * @param baan Een Array van Baandelen
	 */
	public void deleteBaandeel(ArrayList<Baandeel> baan) {
		plaatsOpGeschiedenis();
		for (int i = 0; i < baan.size(); i++) {
			doDeleteBaandeel(baan.get(i));
		}
	}

	/**
	 * Verwijder alles wat te verwijderen is wat aan baan vast zit
	 * 
	 * @param baan hetbaandeel dat verwijdert moet worden
	 */
	public void deleteBaandeel(Baandeel baan) {
		plaatsOpGeschiedenis();
		doDeleteBaandeel(baan);
	}

	/**
	 * Verwijder een station uit de baan
	 * 
	 * @param station het station wat verwijdert moet worden
	 */
	public void deleteStation(Station station) {
		Baandeel b, vorige;

		Baandeel s = station.getEersteBaandeel();
		vorige = s.getVorige();

		while (s instanceof StationBaandeel) {
			if (s == baan) {
				baan = baan.getVolgende();
			}

			b = new Baandeel(new Point(s.getLocatie()));
			b.setVorige(vorige);
			vorige = b;

			s = s.getVolgende();
		}
		vorige.setVolgende(s);
		renderGrid();
	}

	/**
	 * De uiteindelijke methode om een baan te verwijderen
	 * 
	 * @param baan de te verwijderen baan
	 */
	private void doDeleteBaandeel(Baandeel baan) {
		if (baan instanceof StationBaandeel) { // verwijder een station
			deleteStation(((StationBaandeel) baan).getStation());
		} else { // verwijder een baandeel
			Baandeel b;

			LinkedList<Baandeel> banenToDo = new LinkedList<Baandeel>();
			ArrayList<Wissel> wisselsHad = new ArrayList<Wissel>();

			Selection s = new Selection(baan.getLocatie());

			// zorg dat de referentie mee schuift als je die probeert te verwijderen
			if (baan == this.baan) {
				this.baan = this.baan.getVolgende();
			}

			// dan gaan we nu eerst naar voren
			banenToDo.add(baan.getVolgende());
			if (baan instanceof Wissel && !(baan instanceof WisselTerug)) {
				banenToDo.add(((Wissel) baan).getAndere());
			}
			while (banenToDo.size() > 0) {
				b = banenToDo.poll();

				// zorg dat de referentie mee schuift als je die probeert te verwijderen
				if (b == this.baan) {
					this.baan = this.baan.getVolgende();
				}

				if (s.isInSelection(b.getLocatie())) {
					continue;
				}
				if (b instanceof WisselTerug) {
					if (wisselsHad.contains(b)) { // wissel komen we nu voor de tweede keer, nu moet je wel verder
						s.add(b.getLocatie());
						banenToDo.add(b.getVolgende());
						wisselsHad.remove(b);
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

				// zorg dat de referentie mee schuift als je die probeert te verwijderen
				if (b == this.baan) {
					this.baan = this.baan.getVorige();
				}

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

			for (int i = 0; i < wisselsHad.size(); i++) {
				Wissel w = wisselsHad.get(i);

				// zorg dat de head mooi doorschuift anders raken we de baan kwijt.
				if (w == this.baan) {
					if (w instanceof WisselTerug) {
						this.baan = this.baan.getVolgende();
					} else {
						this.baan = this.baan.getVorige();
					}
				}

				// maak de nieuw baan aan
				b = new Baandeel(new Point(w.getLocatie()));

				if (w instanceof WisselTerug) { // het is een wissel terug

					b.setVolgende(w.getVolgende());
					if (s.isInSelection(w.getVorige().getLocatie())) { // De andere blijft bestaan
						b.setVorige(w.getAndere());
					} else { // Vorige blijft bestaan
						b.setVorige(w.getVorige());
					}
				} else { // het is een wissel
					b.setVorige(w.getVorige());
					if (s.isInSelection(w.getVolgende().getLocatie())) { // De andere blijft bestaan
						b.setVolgende(w.getAndere());
					} else { // Volgende blijft bestaan
						b.setVolgende(w.getVolgende());
					}
				}
			}
			renderGrid();
		}
	}

	/**
	 * Vraag de baan op
	 * 
	 * @return de baan
	 */
	public Baandeel getBaan() {
		return baan;
	}

	/**
	 * Een twee-dimensionale Array die het grid voorstelt waar de baan op ligt.
	 * 
	 * @return het grid van baandelen
	 */
	public Baandeel[][] getGrid() {
		return grid;
	}

	/**
	 * Vraag de naam van de baan op
	 * 
	 * @return de naam van de baan
	 */
	public String getNaam() {
		return naam;
	}

	/**
	 * interne afhandeling van het BaanHeader Object
	 * 
	 * @param baanHeader de geladen baan
	 */
	private void laadBaan(BaanHeader baanHeader) {
		// maak een kopie om mee te werken 
		baan = CloneModel.clone(baanHeader.getBaan());

		naam = baanHeader.getNaam();
		renderGrid();
	}

	/**
	 * Laad een baan
	 * 
	 * @param baan
	 */
	public void loadBaan(BaanHeader baan) {
		geschiedenis = new ArrayList<Baandeel>();
		geschiedenisPointer = -1;
		if (baan == null) {
			naam = Language.getString("Editor.0");
			maakNieuweBaan(Baan.standaarBreedte, Baan.standaarHoogte);
		} else {
			this.baan = baan.getBaan();
			setNaam(baan.getNaam());
			renderGrid();
		}
	}

	/**
	 * Maak één baandeel.
	 * 
	 * @param vorige Waar het baandeel aan vast moet komen
	 * @param direction De richting waaraan het vast gemaakt moet worden
	 * @return Het nieuwe baandeel
	 */
	private Baandeel maakBaandeel(Baandeel vorige, int direction) {
		Point p;
		if (vorige == null) {
			p = new Point(0, 0);
		} else {
			p = new Point(vorige.getLocatie());
			p = Direction.newPoint(p, direction);
		}
		Baandeel b = new Baandeel(p);
		if (vorige != null) {
			b.setVorige(vorige);
		}

		return b;
	}

	/**
	 * Een methode om makkelijk een rechte lijn te tekenen, wordt gebruikt als
	 * er een nieuwe baan wordt gemaakt
	 * 
	 * @param vorige Het baandeel waar de lijn aan vast moet komen
	 * @param length Het aantal baandelen dat de lijn moet beslaan
	 * @param direction De Richting waarin de baan zich moet verplaatsen
	 * @return Het laatste baandeel uit de lijn
	 */
	private Baandeel maakLijn(Baandeel vorige, int length, int direction) {
		Baandeel b;
		b = vorige;
		for (int i = 0; i < length; i++) {
			b = maakBaandeel(b, direction);
		}
		return b;
	}

	/**
	 * Maak een nieuwe baan met opgegeven groote
	 * 
	 * @param width de breedte
	 * @param height de hoogte
	 */
	private void maakNieuweBaan(int width, int height) {
		setNaam(Language.getString("Editor.0"));

		Baandeel b;
		baan = maakBaandeel(null, Direction.EAST);
		b = baan;

		b = maakLijn(b, width - 1, Direction.EAST);
		b = maakLijn(b, height - 1, Direction.SOUTH);
		b = maakLijn(b, width - 1, Direction.WEST);
		b = maakLijn(b, height - 2, Direction.NORTH);

		baan.setVorige(b);
		renderGrid();
	}

	/**
	 * Methode om de geschiedenis van de baan bij te houden, zo kan je makkelijk
	 * ctrl+z en ctrl+y uitvoeren.
	 */
	private void plaatsOpGeschiedenis() {
		if (geschiedenis == null) {
			geschiedenis = new ArrayList<Baandeel>();
		}
		//Baandeel baan = (Baandeel)SerializeableUtil.cloneSerializable(this.baan);
		if (geschiedenisPointer != -1) {
			int j = geschiedenis.size();
			int k = geschiedenisPointer;
			for (int i = k; i < j; i++) {
				geschiedenis.remove(k);
			}
			geschiedenisPointer = -1;
		}

		Baandeel baan = CloneModel.clone(this.baan);
		if (baan == null) {
			return;
		}

		geschiedenis.add(baan);
	}

	/**
	 * Voer de laatste wijziging opnieuw uit.
	 */
	public void redo() {
		if (geschiedenis == null) {
			return;
		}
		if (geschiedenisPointer == -1) {
			return;
		}
		geschiedenisPointer++;
		baan = geschiedenis.get(geschiedenisPointer);
		if (geschiedenisPointer == geschiedenis.size() - 1) {
			geschiedenis.remove(geschiedenisPointer);
			geschiedenisPointer = -1;
		}
		renderGrid();

		if (editorFrame != null) {
			editorFrame.getBaanPanel().drawSelection(null);
		}
	}

	/**
	 * Maak het grid.
	 */
	private void renderGrid() {
		grid = BaanToGrid
				.baanToGrid(baan, defaultMarginLeft, defaultMarginRight, defaultMarginTop, defaultMarginBottom);
		setChanged();
		this.notifyObservers(grid);
	}

	/**
	 * Geef het editor frame op waar die panel in zit
	 * 
	 * @param frame het frame
	 */
	public void setEditorFrame(Editor frame) {
		editorFrame = frame;
		editorFrame.setTitleNaam(naam);
	}

	/**
	 * Zet de naam van de baan
	 * 
	 * @param naam de naam
	 */
	public void setNaam(String naam) {
		this.naam = naam;
		if (editorFrame != null) {
			editorFrame.setTitleNaam(this.naam);
		}
		if (namePanel != null) {
			namePanel.setNameText(this.naam);
		}
	}

	/**
	 * geef een NamePanel op
	 * 
	 * @param namePanel het namePanel
	 */
	public void setNamePanel(NamePanel namePanel) {
		this.namePanel = namePanel;
	}

	/**
	 * Maak de laatste wijziging ongedaan
	 */
	public void undo() {
		if (geschiedenis == null) {
			return;
		}
		if (geschiedenisPointer == 0) {
			return;
		}
		if (geschiedenisPointer == -1) {
			plaatsOpGeschiedenis();
			geschiedenisPointer = geschiedenis.size() - 1;
		}
		geschiedenisPointer--;
		baan = geschiedenis.get(geschiedenisPointer);

		renderGrid();

		if (editorFrame != null) {
			editorFrame.getBaanPanel().drawSelection(null);
		}
	}
}