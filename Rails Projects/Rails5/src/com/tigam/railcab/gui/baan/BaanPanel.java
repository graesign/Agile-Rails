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

package com.tigam.railcab.gui.baan;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.MouseInputAdapter;

import com.tigam.railcab.controller.Treinbaan;
import com.tigam.railcab.gui.Kleuren;
import com.tigam.railcab.gui.baan.filters.Filter;
import com.tigam.railcab.language.Language;
import com.tigam.railcab.language.LanguageListener;
import com.tigam.railcab.model.baan.Baandeel;
import com.tigam.railcab.model.baan.Groep;
import com.tigam.railcab.model.baan.RailCab;
import com.tigam.railcab.model.baan.RailCabMissie;
import com.tigam.railcab.model.baan.Station;
import com.tigam.railcab.model.baan.StationBaandeel;
import com.tigam.railcab.util.BaanToGrid;

/**
 * 
 * Een JPanel waarop de baan getekent wordt. Hij zoekt zelf uit hoe groot het
 * model getekent moet worden.
 * 
 * @author Nils Dijk
 * 
 */

public class BaanPanel extends JPanel {

	/**
	 * 
	 * Een mouselistener voor het BaanPanel. Hiermee kan er gereageerd worden op
	 * een mouseEvent.
	 * 
	 * @author Nils Dijk
	 * 
	 */
	class BaanPanelMouse extends MouseInputAdapter {

		/**
		 * Het laatste BaandeelSprite Object
		 */
		private BaandeelSprite lastFocus = null;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (lastFocus != null) {
					lastFocus.onLostFocus();
					lastFocus = null;
				}

				int x = (e.getX() - BaandeelSprite.offsetX) / BaandeelSprite.size;
				int y = (e.getY() - BaandeelSprite.offsetY) / BaandeelSprite.size;

				if (0 > x || baanGrid.length <= x) {
					return;
				}
				if (0 > y || baanGrid[x].length <= y) {
					return;
				}

				lastFocus = baanGrid[x][y];
				if (lastFocus != null) {
					lastFocus.onFocus();
				}
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				if (rightMenu != null) {

					boolean isStation = false;

					int x = (e.getX() - BaandeelSprite.offsetX) / BaandeelSprite.size;
					int y = (e.getY() - BaandeelSprite.offsetY) / BaandeelSprite.size;

					if (!(0 > x || baanGrid.length <= x) && !(0 > y || baanGrid[x].length <= y)) {

						if (baanGrid[x][y] instanceof StationBaandeelSprite) {
							StationBaandeel s = (StationBaandeel) baanGrid[x][y].getBaandeel();
							rightMenu.setStation(s.getStation());
							isStation = true;
						}

					}

					if (!isStation) {
						rightMenu.setStation(null);
					}
					rightMenu.getMenu().show(e.getComponent(), e.getX(), e.getY());
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseDragged(MouseEvent e) {

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseAdapter#mouseExited(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseExited(MouseEvent e) {

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseAdapter#mouseMoved(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseMoved(MouseEvent e) {
			int x = (e.getX() - BaandeelSprite.offsetX) / BaandeelSprite.size;
			int y = (e.getY() - BaandeelSprite.offsetY) / BaandeelSprite.size;

			if (0 > x || baanGrid.length <= x) {
				return;
			}
			if (0 > y || baanGrid[x].length <= y) {
				return;
			}

			if (baanGrid[x][y] instanceof StationBaandeelSprite) {
				Station s = ((StationBaandeel) baanGrid[x][y].getBaandeel()).getStation();
				RailCab r = baanGrid[x][y].getBaandeel().getRailCab();
				if (r != null && railcabStatusInTooltip) {
					RailCabMissie m = r.getHuidigeMissie();
					if (m != null) {
						setToolTipText("RailCab (" + r.hashCode() + "), status: " + r.getRailCabStatus()
								+ ", reizigers: " + r.getAantalReizigers() + ", missie status: " + m.getMissieStatus());
					} else {
						setToolTipText("RailCab (" + r.hashCode() + "), status: " + r.getRailCabStatus()
								+ ", reizigers: " + r.getAantalReizigers() + ", missie status: geen missie");
					}
				} else {
					setToolTipText("Station " + s.getNaam());
				}
			} else if (baanGrid[x][y] instanceof BaandeelSprite) {
				RailCab r = baanGrid[x][y].getBaandeel().getRailCab();
				if (r != null && railcabStatusInTooltip) {
					RailCabMissie m = r.getHuidigeMissie();
					if (m != null) {
						setToolTipText("RailCab (" + r.hashCode() + "), status: " + r.getRailCabStatus()
								+ ", reizigers: " + r.getAantalReizigers() + ", missie status: " + m.getMissieStatus());
					} else {
						setToolTipText("RailCab (" + r.hashCode() + "), status: " + r.getRailCabStatus()
								+ ", reizigers: " + r.getAantalReizigers() + ", missie status: geen missie");
					}
				} else {
					setToolTipText(null);
				}
			} else {
				setToolTipText(null);
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent e) {

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseAdapter#mouseWheelMoved(java.awt.event.MouseWheelEvent)
		 */
		public void mouseWheelMoved(MouseWheelEvent e) {
			// op mac geeft deze een javadoc warning. please ignore...
		}
	}

	/**
	 * Het rechtermuisknop menu van het BaanPaneel.
	 * 
	 * @author Nils Dijk
	 */
	class BaanPanelRightMenu implements ActionListener, LanguageListener {

		/**
		 * Het popup menu
		 */
		private JPopupMenu rightMenu;

		/**
		 * SubMenu voor de filters
		 */
		private JMenu filters;

		/**
		 * Submenu voor het station
		 */
		private JMenu station;

		/**
		 * Een groep voor de radio buttons
		 */
		private ButtonGroup filter_button_groep;

		/**
		 * MenuItem voor Chaos op een station
		 */
		private JMenuItem station_chaos;

		/**
		 * MenuItem voor het leeg gooien van een station
		 */
		private JMenuItem station_clear;

		/**
		 * Station voor het stationmenu
		 */
		private Station selectedStation = null;

		/**
		 * Maak een RechterMuisKnop menu aan
		 */
		public BaanPanelRightMenu() {
			rightMenu = new JPopupMenu();

			// het filtermenu
			filters = new JMenu();

			filter_button_groep = new ButtonGroup();

			ArrayList<Filter> filterArray = Filter.getFilters();

			for (int i = 0; i < filterArray.size(); i++) {
				FilterButton f = new FilterButton(filterArray.get(i));
				if (i == 0) {
					f.setSelected(true);
				}
				f.addActionListener(this);

				filters.add(f);
				filter_button_groep.add(f);
			}

			// het stationmenu
			station = new JMenu();
			station.setVisible(false);

			station_chaos = new JMenuItem();
			station_chaos.addActionListener(this);
			station.add(station_chaos);

			station_clear = new JMenuItem();
			station_clear.addActionListener(this);
			station.add(station_clear);

			// Het menu in elkaar zetten
			rightMenu.add(filters);
			rightMenu.add(station);

			LanguageChanged();
			Language.addLanguageListener(this);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			Object o = arg0.getSource();

			if (o instanceof FilterButton) { // is er op een FilterButton geklikt?
				BaandeelSprite.setFilter(((FilterButton) o).getFilter());
			} else if (o == station_chaos) { // er is op chaos in het submenu station geklikt
				ArrayList<Station> stations = controller.getStations();
				int s2, reizigers;
				for (int i = 0; i < 100; i++) {
					do {
						s2 = (int) (Math.random() * stations.size());
					} while (stations.get(s2) == selectedStation);
					reizigers = (int) (Math.random() * 10) + 1;
					selectedStation.addGroep(new Groep(selectedStation, stations.get(s2), reizigers));
				}
			} else if (o == station_clear) { // er is op clear is het submenu station geklikt
				selectedStation.clear();
			}
		}

		/**
		 * Vraag een referentie naar het menu op
		 * 
		 * @return de referentie naar het menu
		 */
		public JPopupMenu getMenu() {
			return rightMenu;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.tigam.railcab.language.LanguageListener#LanguageChanged()
		 */
		public void LanguageChanged() {
			station.setText(Language.getString("BaanPanel.0"));
			station_chaos.setText(Language.getString("BaanPanel.1"));
			station_clear.setText(Language.getString("BaanPanel.2"));

			filters.setText(Language.getString("BaanPanel.3"));
		}

		/**
		 * Zet het station waarop met de rechtermuisknop is gedrukt
		 * 
		 * @param s Het station
		 */
		public void setStation(Station s) {
			if (s == null) {
				station.setVisible(false);
			} else {
				station.setEnabled(controller.isRunning());
				station.setVisible(true);
				station.setText(Language.getString("BaanPanel.0") + " " + s.getNaam());
				selectedStation = s;
			}

		}

	}

	/**
	 * 
	 * Een Radio Button gebaseert op Filters
	 * 
	 * @author Nils Dijk
	 * 
	 */
	class FilterButton extends JRadioButtonMenuItem implements LanguageListener {

		/**
		 * Serializable...
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Het Filter wat er bij hoort
		 */
		private Filter filter = null;

		/**
		 * Maak een radio button direct van een filter af
		 * 
		 * @param filter het filter waar de radio button vanaf is geleid
		 */
		public FilterButton(Filter filter) {
			setFilter(filter);
			Language.addLanguageListener(this);
		}

		/**
		 * Vraag het Filter Object wat hier bij hoort
		 * 
		 * @return Het filter object
		 */
		public Filter getFilter() {
			return filter;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.tigam.railcab.language.LanguageListener#LanguageChanged()
		 */
		public void LanguageChanged() {
			setText(filter.toString());
		}

		/**
		 * Zet de waarde van het filter
		 * 
		 * @param filter het filter wat bij deze radio button hoort
		 */
		public void setFilter(Filter filter) {
			this.filter = filter;
			LanguageChanged();
		}
	}

	/**
	 * Serializable...
	 */
	static final long serialVersionUID = 78293409852L;

	/**
	 * De achtergrond kleur van het BaanPanel
	 */
	static Color kleurAchtergrond = Kleuren.Achtergrond;

	/**
	 * Grid van BaandeelSprites
	 */
	private BaandeelSprite[][] baanGrid;

	/**
	 * Varibale die bijhoudt of de BaandeelSprites al weten dat ik hub vader ben
	 */
	private boolean hasToldSprites = false;

	/**
	 * De controller waar tegenaan geluld kan worden
	 */
	private Treinbaan controller;

	/**
	 * Is er iets verandert sinds de laatste paint(), Oftewel Moet er gerepaint
	 * worden?
	 */
	private boolean isChanged = false;

	/**
	 * Het recthtermuisknop menu van het BaanPanel
	 */
	private BaanPanelRightMenu rightMenu;

	/**
	 * Het thread waarin de baan getekent wordt
	 */
	private BaanPanelRenderThread render;

	/**
	 * debugging optie... geef railcab status in tooltip
	 */
	private final boolean railcabStatusInTooltip = false;

	/**
	 * Constructor, initializeerd het Panel
	 * 
	 * @param controller krijgt een controller mee waar hij zijn bewerkingen op
	 *            kan doen
	 */
	public BaanPanel(Treinbaan controller) {
		this.controller = controller;

		// Maak een Visueel grid van het model
		Baandeel[][] grid = BaanToGrid.baanToGrid(controller.getBaanHeader().getBaan());
		baanGrid = new BaandeelSprite[grid.length][grid[0].length];
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[x].length; y++) {
				baanGrid[x][y] = BaandeelSprite.maakSprite(grid[x][y]);
			}
		}

		initMouse();
		initStations();
		initRightMenu();

		render = new BaanPanelRenderThread(this);

		setChanged();
	}

	/**
	 * Voeg een Observer toe aan alle StationBaandeelSprites
	 * 
	 * @param o Observer die moet wortden toegevoegd.
	 */
	public void addStationObserver(Observer o) {
		for (int x = 0; x < baanGrid.length; x++) {
			for (int y = 0; y < baanGrid[x].length; y++) {
				if (baanGrid[x][y] instanceof StationBaandeelSprite) {
					baanGrid[x][y].addObserver(o);
				}
			}
		}
	}

	/**
	 * Methode om een BaandeelSprite op te halen.
	 * 
	 * @param x de x co-ordinaat op het grid
	 * @param y de y co-ordinaat op het grid
	 * @return geeft een BaandeelSprite terug, null als het veld leeg is.
	 */
	public BaandeelSprite getBaandeelSpriteFromGrid(int x, int y) {
		if (x < 0 & baanGrid.length >= x) {
			return null;
		}
		if (y < 0 & baanGrid[x].length >= y) {
			return null;
		}

		return baanGrid[x][y];
	}

	/**
	 * Vraag een referentie naar het Thread wat de baan elke keer opnieuw tekent
	 * 
	 * @return een referentie naar het Thead
	 */
	public BaanPanelRenderThread getRenderThread() {
		return render;
	}

	/**
	 * Deze methode zorgt er voor dat er op de muis gereageerd kan worden.
	 */
	private void initMouse() {
		MouseInputAdapter m = new BaanPanelMouse();

		addMouseListener(m);
		addMouseMotionListener(m);
		//this.addMouseWheelListener(m);
	}

	/**
	 * Maak het rechtermenu aan
	 */
	private void initRightMenu() {
		rightMenu = new BaanPanelRightMenu();
	}

	/**
	 * Methode voor het initialiseren van de stations, dit is nodig om te zorgen
	 * dat alle StationBaandeelSprites geselecteerd worden als er op één
	 * StationBaandeelSprite geklikt wordt.
	 */
	private void initStations() {
		Station s;
		Baandeel b;
		BaandeelSprite baanSprite;
		ArrayList<Station> sA = controller.getStations();
		ArrayList<StationBaandeelSprite> stationSprites;

		for (int i = 0; i < sA.size(); i++) {
			stationSprites = new ArrayList<StationBaandeelSprite>();

			s = sA.get(i);
			b = s.getEersteBaandeel();

			while (b instanceof StationBaandeel) {
				baanSprite = baanGrid[b.getLocatie().x][b.getLocatie().y];
				if (baanSprite instanceof StationBaandeelSprite) {
					stationSprites.add((StationBaandeelSprite) baanGrid[b.getLocatie().x][b.getLocatie().y]);
				}
				b = b.getVolgende();
			}
			for (int j = 0; j < stationSprites.size(); j++) {
				stationSprites.get(j).setStationBaandelen(s, stationSprites);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g2) {

		Dimension d = this.getSize();

		// Arjan - Render hints. Nu ziet het er op Mac ook een beetje goed uit :)
		Graphics2D g = (Graphics2D) g2;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		isChanged = false;

		// Heb je de baandelen al vertelt waar ze info vandaan kunnen halen ?
		if (!hasToldSprites) {
			for (int i = 0; i < baanGrid.length; i++) {
				for (int j = 0; j < baanGrid[i].length; j++) {
					if (baanGrid[i][j] != null) {
						baanGrid[i][j].setBaanPanel(this);
					}
				}
			}
			hasToldSprites = true;
		}

		// teken een mooie achtergrond
		if (BaanPanel.kleurAchtergrond == null) {
			BaanPanel.kleurAchtergrond = getBackground();
		}
		g.setColor(BaanPanel.kleurAchtergrond);
		g.fillRect(0, 0, getSize().width, getSize().height);

		// en maak de kleur weer zwart

		// bereken het nieuwe formaat van het grid
		int gridHeight, gridWidth;
		gridWidth = d.width / baanGrid.length;
		gridHeight = d.height / baanGrid[0].length;

		int gridSize;
		if (gridWidth > gridHeight) {
			gridSize = gridHeight;
		} else {
			gridSize = gridWidth;
		}

		BaandeelSprite.setSize(gridSize);

		gridWidth = gridSize * baanGrid.length;
		gridHeight = gridSize * baanGrid[0].length;

		// zet het grid mooi in het midden
		BaandeelSprite.setOffsetX((d.width - gridWidth) / 2);
		BaandeelSprite.setOffsetY((d.height - gridHeight) / 2);

		// geef alle baandelen de opdracht om opnieuw te tekenen
		for (int i = 0; i < baanGrid.length; i++) {
			for (int j = 0; j < baanGrid[i].length; j++) {
				if (baanGrid[i][j] != null) {
					baanGrid[i][j].paint(g);
				}
			}
		}
	}

	/**
	 * Als een BaandeelSprite verandert is moet er een flag gezet worden dat de
	 * baan opnieuw getekent moet worden.
	 */
	public void setChanged() {
		isChanged = true;
	}

	/**
	 * Methode die een repaint aanvraagt op het moment dat er iets verandert is
	 * aan 1 van de BaandeelSprites
	 */
	public void userRepaint() {
		if (isChanged) {
			repaint();
		}
		isChanged = false;
	}
}
