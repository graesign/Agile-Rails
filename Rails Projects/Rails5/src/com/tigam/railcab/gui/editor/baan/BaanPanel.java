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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import com.tigam.railcab.gui.Kleuren;
import com.tigam.railcab.gui.editor.model.Baan;
import com.tigam.railcab.gui.editor.model.Finder;
import com.tigam.railcab.gui.editor.model.Path;
import com.tigam.railcab.gui.editor.model.PathFinder;
import com.tigam.railcab.gui.editor.model.Selection;
import com.tigam.railcab.gui.editor.model.SelectionListener;
import com.tigam.railcab.language.Language;
import com.tigam.railcab.model.baan.Baandeel;
import com.tigam.railcab.util.Direction;

/**
 * Het BaanPanel van de editor
 * 
 * @author Nils Dijk
 * 
 */
public class BaanPanel extends JPanel implements Observer {

	/**
	 * Baan Editor Besturings klasse
	 * 
	 * @author Nils Dijk
	 * 
	 */
	class BaanPanelBesturing extends MouseInputAdapter implements KeyListener {
		/**
		 * Status van een idle Baan Editor
		 */
		final static int STATE_NULL = 0;

		/**
		 * Status van het slepen van nieuwe baandelen
		 */
		final static int STATE_DRAG = 1;

		/**
		 * Status van een Baan Editor in selectie modus
		 */
		final static int STATE_SELECT = 2;

		/**
		 * Welk baandeel was als laatst geselecteerd
		 */
		private BaandeelSprite lastFocus = null;

		/**
		 * Punt van de muis bij een actie
		 */
		private Point p1;

		/**
		 * Punt van de muis bij een actie waar het 1 en ander onthouden moet
		 * worden
		 */
		private Point p2;

		/**
		 * Punt op het grid waar de drag start
		 */
		private Point pDragStart;

		/**
		 * Punt op het grid waar de drag nu is
		 */
		private Point pDragStop;

		/**
		 * De Pathfinder van nieuw baan dingen
		 */
		private PathFinder pf;

		/**
		 * De status van de editor
		 */
		private int state = STATE_NULL;

		/**
		 * De muisknop die bij de laatste actie ingedrukt is
		 */
		private int lastButtonPressed = 0;

		// Toetsenbord events - Verplicht
		public void keyPressed(KeyEvent e) {
			if (state == STATE_DRAG) {
				reDrag(e.isControlDown(), e.isShiftDown());
			}
		}

		public void keyReleased(KeyEvent e) {
			if (state == STATE_DRAG) {
				reDrag(e.isControlDown(), e.isShiftDown());
			}
		}

		public void keyTyped(KeyEvent e) {
			if (e.getKeyChar() == 25) { // ctrl + y
				baan.redo();
				createGrid();
				//repaint();
			} else if (e.getKeyChar() == 26) { // ctrl + z
				baan.undo();
				createGrid();
				//repaint();
			} else if (e.getKeyChar() == 127 || e.getKeyChar() == 8) { // delete
				if (state == STATE_SELECT) {
					deleteSelection();
					//this.state = STATE_NULL;
				}
			}

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (e.getClickCount() == 1) { // single click
					BaandeelSprite tmp = lastFocus;
					if (lastFocus != null) {
						lastFocus.onLostFocus();
						lastFocus = null;
					}

					int x = (e.getX() - AbstractBaandeelSprite.offsetX) / AbstractBaandeelSprite.size;
					int y = (e.getY() - AbstractBaandeelSprite.offsetY) / AbstractBaandeelSprite.size;

					if (0 > x || grid.length <= x || 0 > y || grid[x].length <= y) {
						verwerkSelectie(null, e.isAltDown(), e.isControlDown(), e.isShiftDown());
						return;
					}

					lastFocus = (BaandeelSprite) grid[x][y];
					if (lastFocus != null) {
						verwerkSelectie(lastFocus.onFocus(), e.isAltDown(), e.isControlDown(), e.isShiftDown());
					} else {
						verwerkSelectie(null, e.isAltDown(), e.isControlDown(), e.isShiftDown());
					}

					if (e.isShiftDown()) {
						if (tmp != null && lastFocus != null) {
							verwerkSelectie(Finder.BaanToBaan(tmp.getBaandeel(), lastFocus.getBaandeel()), e
									.isAltDown(), e.isControlDown(), e.isShiftDown());
						}
					}
					repaint();
				} else if (e.getClickCount() == 2) {
					if (lastFocus != null) {
						verwerkSelectie(lastFocus.doubleClick(), e.isAltDown(), e.isControlDown(), e.isShiftDown());
					} else {
						verwerkSelectie(null, e.isAltDown(), e.isControlDown(), e.isShiftDown());
					}
					repaint();
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (lastButtonPressed == MouseEvent.BUTTON1) {
				if (requestState(STATE_DRAG)) {
					p1 = e.getPoint();
					pDragStart = new Point((p1.x - AbstractBaandeelSprite.offsetX) / AbstractBaandeelSprite.size,
							(p1.y - AbstractBaandeelSprite.offsetY) / AbstractBaandeelSprite.size);
					pDragStop = null;

					pf = new PathFinder(baan.getGrid(), pDragStart);
				} else if (state == STATE_DRAG) {
					p2 = e.getPoint();
					pDragStop = new Point((p2.x - AbstractBaandeelSprite.offsetX) / AbstractBaandeelSprite.size,
							(p2.y - AbstractBaandeelSprite.offsetY) / AbstractBaandeelSprite.size);
					reDrag(e.isControlDown(), e.isShiftDown());
				}
			} else if (lastButtonPressed == MouseEvent.BUTTON3) { // als er met de rechtermuis gedragged wordt
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

		// Muise events - Verplicht
		@Override
		public void mouseMoved(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {
			lastButtonPressed = e.getButton();
			requestFocus();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (state == STATE_DRAG) {
				state = STATE_NULL;

				p2 = e.getPoint();
				pDragStop = new Point((p2.x - AbstractBaandeelSprite.offsetX) / AbstractBaandeelSprite.size,
						(p2.y - AbstractBaandeelSprite.offsetY) / AbstractBaandeelSprite.size);

				if (pDragStop.x < 0 || pDragStop.y < 0 || pDragStop.x >= grid.length
						|| pDragStop.y >= grid[pDragStop.x].length || grid[pDragStop.x][pDragStop.y] == null) {
					createGrid();
					return;
				}

				int direction = 0;

				int x = AbstractBaandeelSprite.offsetX + AbstractBaandeelSprite.size * pDragStop.x;
				int y = AbstractBaandeelSprite.offsetY + AbstractBaandeelSprite.size * pDragStop.y;

				x = p2.x - x;
				y = p2.y - y;

				if (AbstractBaandeelSprite.size / 3 > x) {
					direction |= Direction.WEST;
				}
				if (AbstractBaandeelSprite.size * 2 / 3 < x) {
					direction |= Direction.EAST;
				}

				if (AbstractBaandeelSprite.size / 3 > y) {
					direction |= Direction.NORTH;
				}
				if (AbstractBaandeelSprite.size * 2 / 3 < y) {
					direction |= Direction.SOUTH;
				}

				Path p;
				if (e.isControlDown()) {
					p = pf.getStationPath(pDragStop);
					createNewStation(p);
				} else {
					if (e.isShiftDown()) {
						p = pf.getSquarePath(pDragStop, direction);
					} else {
						p = pf.getNormalPath(pDragStop, direction);
					}
					pointCreatePath(p);
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseAdapter#mouseWheelMoved(java.awt.event.MouseWheelEvent)
		 */
		public void mouseWheelMoved(MouseWheelEvent e) {
			// op mac geeft deze een javadoc warning. please ignore...
		}

		// Hier volgen wat hulpmiddelen

		/**
		 * Voor het tekenen van een nieuw te maken baan. Kan op twee manieren
		 * aangeroepen worden:<br>
		 * &nbsp; 1) Door het bewegen van de muis<br>
		 * &nbsp; 2) Door het indrukken van de ctrl of shift
		 * 
		 * @param isControlDown is de ctrl toets ingedrukt
		 * @param isShiftDown is de shift toets ingedrukt
		 */
		private void reDrag(boolean isControlDown, boolean isShiftDown) {
			if (pDragStop == null) {
				return;
			}
			if (pf == null) {
				return;
			}
			int direction = 0;

			int x = AbstractBaandeelSprite.offsetX + AbstractBaandeelSprite.size * pDragStop.x;
			int y = AbstractBaandeelSprite.offsetY + AbstractBaandeelSprite.size * pDragStop.y;

			x = p2.x - x;
			y = p2.y - y;

			if (AbstractBaandeelSprite.size / 3 > x) {
				direction |= Direction.WEST;
			}
			if (AbstractBaandeelSprite.size * 2 / 3 < x) {
				direction |= Direction.EAST;
			}

			if (AbstractBaandeelSprite.size / 3 > y) {
				direction |= Direction.NORTH;
			}
			if (AbstractBaandeelSprite.size * 2 / 3 < y) {
				direction |= Direction.SOUTH;
			}

			Path p;
			if (isControlDown) {
				p = pf.getStationPath(pDragStop);
				drawNewStation(p);
			} else {
				if (isShiftDown) {
					p = pf.getSquarePath(pDragStop, direction);
				} else {
					p = pf.getNormalPath(pDragStop, direction);
				}
				pointDrawPath(p);
			}
		}

		/**
		 * Vraag of je in een bepaalde toestand kan komen
		 * 
		 * @param rState de toestand waar je heen wil
		 * @return mag de gevraagde toestand ingegaan worden
		 */
		private boolean requestState(int rState) {
			if (state == rState) {
				return false;
			}

			if (state == STATE_SELECT) {
				if (rState == STATE_DRAG) {
					if (selection != null && !selection.isEmpty()) {
						return false;
					}
				}
			}

			state = rState;
			return true;
		}

		/**
		 * Methode om nieuwe selecties te verwerken
		 * 
		 * @param newSelection De nieuwe selectie
		 * @param isAltDown is de alt toest ingedrukt
		 * @param isControlDown is de ctrl toets ingedrukt
		 * @param isShiftDown is de shift toets ingedrukt
		 */
		private void verwerkSelectie(Selection newSelection, boolean isAltDown, boolean isControlDown,
				boolean isShiftDown) {
			if (requestState(STATE_SELECT) || state == STATE_SELECT) {
				// als er nog geen selectie was, maak die dan nu aan
				if (selection == null) {
					selection = new Selection();
				}
				if (isAltDown) {
					selection.remove(newSelection);
				} else if (isShiftDown) { // voeg de nieuwe selectie toe en selecteer alles wat er tussen zit
					//TODO: moet nog een formule komen om de tussenliggende banen uit te rekenen

					selection.add(newSelection);
				} else if (isControlDown) { // voeg de nieuwe selectie toe aan de huidige
					selection.add(newSelection);
				} else { // de selectie moet de huidige selectie vervangen
					selection = newSelection;
				}
				drawSelection();

				if (selection == null || selection.isEmpty()) {
					state = STATE_NULL;
				}
			}
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -199288729924082129L;

	/**
	 * De baan controller
	 */
	private Baan baan;

	/**
	 * Moet de rijrichting weergegeven worden ?
	 */
	private boolean showRijRichting = false;

	/**
	 * Een grid van Visuele baandelen
	 */
	private AbstractBaandeelSprite[][] grid;

	/**
	 * Zijn er wijzigingen op het grid
	 */
	private boolean isUpdated = false;

	/**
	 * De Selectie van punten in het grid
	 */
	private Selection selection = null;

	/**
	 * Wat is de laatst bekende selectie
	 */
	private Selection lastSelection = null;

	/**
	 * Lijst met alle geïntereseerde SelectionListener
	 */
	private ArrayList<SelectionListener> selectionListeners;

	/**
	 * Maak een BaanPanel
	 * 
	 * @param baan de Baan Controller
	 */
	public BaanPanel(Baan baan) {
		this.baan = baan;
		this.baan.addObserver(this);

		grid = null;

		selectionListeners = new ArrayList<SelectionListener>();

		initBesturing();
	}

	/**
	 * Voeg een SelectionListener toe aan het Panel
	 * 
	 * @param selectionListener
	 */
	public void addSelectionListener(SelectionListener selectionListener) {
		if (!selectionListeners.contains(selectionListener)) {
			selectionListeners.add(selectionListener);
		}
	}

	/**
	 * Maak van het grid in het model een Visueel grid
	 */
	public void createGrid() {
		Baandeel[][] grid = baan.getGrid();
		this.grid = new AbstractBaandeelSprite[grid.length][grid[0].length];
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[x].length; y++) {
				if (grid[x][y] != null) {
					this.grid[x][y] = BaandeelSprite.maakBaandeelSprite(grid[x][y]);
				}
			}
		}
		setUpdate();
	}

	/**
	 * Maak een nieuw Station
	 * 
	 * @param p het pad van het station
	 */
	private void createNewStation(Path p) {
		if (p == null) {
			createGrid();
			return;
		}

		String stationNaam = JOptionPane.showInputDialog(null, "Wat is de naam van het nieuwe station ?",
				"Nieuw Station", JOptionPane.QUESTION_MESSAGE);
		if (stationNaam == null) {
			createGrid();
			return;
		}

		baan.createStation(p, stationNaam);
	}

	/**
	 * Verwijder de geselecteerde baandelen
	 * 
	 * @return Is hij daadwerkelijk verwijder
	 */
	public boolean deleteSelection() {
		if (!Finder.isSelectionDeletable(baan.getGrid(), lastSelection)) {
			Selection tmp = selection;
			selection = Finder.deletable(baan.getGrid(), selection);
			this.drawSelection();
			int option = JOptionPane
					.showConfirmDialog(
							this,
							Language.getString("Editor.BaanPanel.0"), Language.getString("Editor.BaanPanel.1"), JOptionPane.CANCEL_OPTION | JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			requestFocus();
			if (option == JOptionPane.CLOSED_OPTION || option == JOptionPane.CANCEL_OPTION) {
				selection = tmp;
				this.drawSelection();
				return false;
			}
		}

		ArrayList<Baandeel> bs = Finder.getBaandelenToDelete(baan.getGrid(), lastSelection);
		baan.deleteBaandeel(bs);

		this.drawSelection(null);
		return true;
	}

	/**
	 * Teken een voorbeeld van nieuwe stations
	 * 
	 * @param p het pad van het station
	 */
	private void drawNewStation(Path p) {
		createGrid();
		if (p == null) {
			return;
		}

		ArrayList<Point> q = p.getPath();
		for (int i = 0; i < q.size(); i++) {
			Point n = q.get(i);
			grid[n.x][n.y] = new NewStationBaandeelSprite(n);
		}
	}

	/**
	 * Teken een selection
	 */
	public void drawSelection() {
		if (lastSelection != null) { // verwijder eerst de oude selectie
			ArrayList<Point> punten = lastSelection.getSelection();
			for (int i = 0; i < punten.size(); i++) {
				Point p = punten.get(i);
				if ((BaandeelSprite) grid[p.x][p.y] != null) {
					((BaandeelSprite) grid[p.x][p.y]).setSelected(false);
				}
			}
		}

		// nu de baandelen selecteren die in de nieuwe selectie geselecteerd zijn
		if (selection != null) { // doe natuurlijk niks als er geen selectie is
			ArrayList<Point> punten = selection.getSelection();
			for (int i = 0; i < punten.size(); i++) {
				Point p = punten.get(i);
				if ((BaandeelSprite) grid[p.x][p.y] != null) {
					((BaandeelSprite) grid[p.x][p.y]).setSelected(true, selection);
				}
			}
		}
		lastSelection = selection;
		notifySelectionListeners();
		repaint();
	}

	/**
	 * Teken een selection
	 * 
	 * @param selection
	 */
	public void drawSelection(Selection selection) {
		this.selection = selection;
		this.drawSelection();
	}

	/**
	 * Vraag de huidige selectie op
	 * 
	 * @return de huidige selectie
	 */
	public Selection getSelection() {
		return selection;
	}

	/**
	 * Vraag de status op van de rijrichting
	 * 
	 * @return <b>true</b> - de rijrichting wordt weergegeven<br>
	 *         <b>false</b> - de rijrichting wordt NIET weergegeven
	 */
	public boolean getShowRijRichting() {
		return showRijRichting;
	}

	/**
	 * Laad de besturings elementen
	 */
	private void initBesturing() {
		BaanPanelBesturing m = new BaanPanelBesturing();

		addMouseListener(m);
		addMouseMotionListener(m);
		//this.addMouseWheelListener(m);
		addKeyListener(m);
	}

	/**
	 * Geef een seintje aan alle SelectionListeners
	 */
	private void notifySelectionListeners() {
		for (int i = 0; i < selectionListeners.size(); i++) {
			selectionListeners.get(i).SelectionChanged(selection);
		}
	}

	@Override
	public void paint(Graphics g2) {
		if (grid == null) {
			createGrid();
		}
		Dimension d = this.getSize();

		Graphics2D g = (Graphics2D) g2;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(Kleuren.AchtergrondEditor);
		g.fillRect(0, 0, getSize().width, getSize().height);
		g.setColor(Color.BLACK);

		int gridHeight, gridWidth;
		int gridSize;

		gridWidth = d.width / grid.length;
		gridHeight = d.height / grid[0].length;
		if (gridWidth > gridHeight) {
			gridSize = gridHeight;
		} else {
			gridSize = gridWidth;
		}

		AbstractBaandeelSprite.setSize(gridSize);

		gridWidth = gridSize * grid.length;
		gridHeight = gridSize * grid[0].length;

		AbstractBaandeelSprite.setOffsetX((d.width - gridWidth) / 2);
		AbstractBaandeelSprite.setOffsetY((d.height - gridHeight) / 2);

		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[x].length; y++) {
				if (grid[x][y] != null) {
					grid[x][y].showRichting(showRijRichting);
					grid[x][y].paint(g);
				}
			}
		}

	}

	/**
	 * Maak nieuwe baandelen.
	 * 
	 * @param p het pad
	 */
	private void pointCreatePath(Path p) {
		baan.createPath(p);
	}

	/**
	 * Teken een voorbeeld van de nieuwe baandelen
	 * 
	 * @param p het pad
	 */
	private void pointDrawPath(Path p) {
		createGrid();
		if (p == null) {
			return;
		}

		ArrayList<Point> q = p.getPath();

		if (q.size() < 2 || q.get(0).equals(q.get(q.size() - 1))) {
			return;
		}

		Point n;

		n = q.get(0);
		grid[n.x][n.y] = new NewWisselSprite(n);

		for (int i = 1; i < q.size() - 1; i++) {
			n = q.get(i);
			grid[n.x][n.y] = new NewBaandeelSprite(n);
		}
		n = q.get(q.size() - 1);
		if (grid[n.x][n.y] == null) {
			grid[n.x][n.y] = new NewBaandeelSprite(n);
		} else {
			grid[n.x][n.y] = new NewWisselSprite(n);
		}

		//repaint();
	}

	/**
	 * Verwijder een SelectionListener
	 * 
	 * @param selectionListener
	 */
	public void removeSelectionListener(SelectionListener selectionListener) {
		selectionListeners.remove(selectionListener);
	}

	/**
	 * Zet de status van de rijrichting
	 * 
	 * @param b <b>true</b> - Laat de rijriching zien<br>
	 *            <b>false</b> - Verberg de rijrichting
	 */
	public void setShowRijRichting(boolean b) {
		showRijRichting = b;
		setUpdate();
	}

	/**
	 * Aanroepen als er iets verandert is op het grid
	 */
	public void setUpdate() {
		isUpdated = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	// @Override
	public void update(Observable arg0, Object arg1) {
		createGrid();
	}

	/**
	 * Repaint die wordt aangesproken vanaf het renderThread
	 */
	public void userRepaint() {
		if (isUpdated) {
			repaint();
		}
		isUpdated = false;
	}

}
