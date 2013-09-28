package UserInterface;

import java.awt.*;
import javax.swing.*;
import java.util.*;

import Business.*;
import Data.*;

public class OverviewPanel extends JLayeredPane {
	private static final long serialVersionUID = 1L;
	private Image bg = new ImageIcon("Plattegrond.PNG").getImage();
	private ArrayList<Route> shuttles;
	private int x, y;
	private Central central;
	private static final int PIXELJUMP = 2;

	private Graphics g;
	private java.util.Timer paintTime;
	private MainUI main;

	public OverviewPanel(Central central, MainUI main) {
		setLayout(new BorderLayout());
		this.central = central;
		this.main = main;
		shuttles = new ArrayList<Route>();

	}

	public void updateTimer() {
		paintTime.cancel();
		central.updateTimer();
		paintTimer();
	}

	public void paintTimer() {
		paintTime = new java.util.Timer();

		paintTime.schedule(new TimerTask() {
			public void run() {
				repaint();

			}
		}, ((14000 * PIXELJUMP) - (central.getDatabase().getShuttleOccupiedTraveling() * 70))
				/ central.getAcceleration() < 0 ? 1 : ((14000 * PIXELJUMP) - (central.getDatabase()
				.getShuttleOccupiedTraveling() * 70))
				/ (central.getAcceleration())); // Kan niet meer dan 178
		// Passagiers vervoeren

	}

	public void paint(Graphics g) {
		this.g = g;
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.drawImage(bg, 0, 0, this);

		g.drawString("" + (central.getStation(0)).getShuttleAmount(), 15, 395);
		g.drawString("" + (central.getStation(1)).getShuttleAmount(), 280, 325);
		g.drawString("" + (central.getStation(2)).getShuttleAmount(), 310, 40);
		g.drawString("" + (central.getStation(3)).getShuttleAmount(), 362, 255);
		g.drawString("" + (central.getStation(4)).getShuttleAmount(), 400, 365);
		g.drawString("" + (central.getStation(5)).getShuttleAmount(), 604, 170);
		g.drawString("" + (central.getStation(6)).getShuttleAmount(), 675, 250);
		g.drawString("" + (central.getStation(7)).getShuttleAmount(), 720, 290);
		g.drawString("" + (central.getStation(8)).getShuttleAmount(), 735, 190);

		g.fillOval(10, 425, 10, 10);

		g.setColor(java.awt.Color.GREEN);
		g.fillOval(10, 440, 10, 10);

		g.setColor(java.awt.Color.YELLOW);
		g.fillOval(10, 455, 10, 10);

		g.setColor(java.awt.Color.PINK);
		g.fillOval(10, 470, 10, 10);

		g.setColor(java.awt.Color.MAGENTA);
		g.fillOval(10, 485, 10, 10);

		g.setColor(java.awt.Color.BLACK);

		g.drawString(" = Shuttle gevuld met 1 persoon", 22, 434);
		g.drawString(" = Shuttle gevuld met meer dan 1 personen", 22, 449);
		g.drawString(" = Shuttle uit zelf reizen", 22, 464);
		g.drawString(" = Lege shuttle, opgevraagt door te kort", 22, 479);
		g.drawString(" = Lege shuttle, doorgestuurd door overschot", 22, 494);

		for (int i = 0; i < shuttles.size(); i++) {
			Route route = shuttles.get(i);
			Color color = route.getColor();
			g2.setColor(color);

			int x = route.getNextCoordinateX();
			int y = route.getNextCoordinateY();

			if (!(x == 0 || y == 0)) {
				if (main.getInfoPanel().getShown().contains(color))
					g2.fillOval(x + 5, y + 5, 10, 10);
			} else {
				removeShuttle(route);
				if (route.getColor().equals(java.awt.Color.PINK) || route.getColor().equals(java.awt.Color.MAGENTA))
					central.getDatabase().removeShuttlesEmptyTraveling(SimDate.getTimeInMillis());
				else {
					central.getDatabase().removeShuttleOccupiedTraveling(SimDate.getTimeInMillis());
					central.getDatabase().addPassengerTransfered(SimDate.getTimeInMillis(),
							route.getShuttle().getPassengerAmount(), 20, 20);
					route.getShuttle().setPassenger(0);
				}

				if (route.getColor().equals(java.awt.Color.YELLOW))
					main.getOwnTravelPanel().startOver();
				route.getDestination().getShuntArea().addShuttle(route.getShuttle());

			}

			if (main.getInfoPanel().getstartstopPassengerAmount().isSelected()) {
				g2.setColor(java.awt.Color.BLACK);
				g2.drawString("" + route.getShuttle().getPassengerAmount(), x + 6, y - 1);
			} else
				g2.drawString("", x + 6, y - 1);
		}
		paintTimer();

	}

	public void addShuttle(Station depart, Station destination, Shuttle shuttle, java.awt.Color color) {
		// Amersfoort = 0
		// Apeldoorn = 1
		// Zwolle = 2
		// Deventer = 3
		// Zutphen = 4
		// Almelo = 5
		// Hengelo = 6
		// Enschede = 7
		// Oldenzaal = 8

		Route route = new Route(destination, shuttle, color);
		central.echo("ROUTE WORDT GETEKEND");
		switch (depart.getStationID()) {
		case 0: {
			switch (destination.getStationID()) {
			case 1: {

				this.amersfoortApeldoorn(route);
				break;
			}

			case 2: {
				this.amersfoortZwolle(route);
				break;
			}

			case 3: {
				this.amersfoortDeventer(route);
				break;
			}

			case 4: {
				this.amersfoortZutphen(route);
				break;
			}

			case 5: {
				this.amersfoortAlmelo(route);
				break;
			}

			case 6: {
				this.amersfoortHengelo(route);
				break;
			}

			case 7: {
				this.amersfoortEnschede(route);
				break;
			}

			case 8: {
				this.amersfoortOldenzaal(route);
				break;
			}

			}

			break; // Sluit de eerste switch case
		}
		case 1: {
			switch (destination.getStationID()) {
			case 0: {
				this.apeldoornAmersfoort(route);
				break;
			}
			case 2: {
				this.apeldoornZwolle(route);
				break;
			}
			case 3: {
				this.apeldoornDeventer(route);
				break;
			}
			case 4: {
				this.apeldoornZutphen(route);
				break;
			}
			case 5: {
				this.apeldoornAlmelo(route);
				break;
			}
			case 6: {
				this.apeldoornHengelo(route);
				break;
			}
			case 7: {
				this.apeldoornEnschede(route);
				break;
			}
			case 8: {
				this.apeldoornOldenzaal(route);
				break;
			}
			}

			break;
		}

		case 2: {
			switch (destination.getStationID()) {
			case 0: {
				this.zwolleAmersfoort(route);
				break;
			}

			case 1: {
				this.zwolleApeldoorn(route);
				break;
			}

			case 3: {
				this.zwolleDeventer(route);
				break;
			}

			case 4: {
				this.zwolleZutphen(route);
				break;
			}

			case 5: {
				this.zwolleAlmelo(route);
				break;
			}

			case 6: {
				this.zwolleHengelo(route);
				break;
			}

			case 7: {
				this.zwolleEnschede(route);
				break;
			}

			case 8: {
				this.zwolleOldenzaal(route);
				break;
			}
			}

			break;

		}
		case 3: {
			switch (destination.getStationID()) {
			case 0: {
				this.deventerAmersfoort(route);
				break;
			}
			case 1: {
				this.deventerApeldoorn(route);
				break;
			}
			case 2: {
				this.deventerZwolle(route);
				break;
			}
			case 4: {
				this.deventerZutphen(route);
				break;
			}
			case 5: {
				this.deventerAlmelo(route);
				break;
			}
			case 6: {
				this.deventerHengelo(route);
				break;
			}

			case 7: {
				this.deventerEnschede(route);
				break;
			}

			case 8: {
				this.deventerOldenzaal(route);
				break;
			}
			}
			break;
		}
		case 4: {
			switch (destination.getStationID()) {
			case 0: {
				this.zutphenAmersfoort(route);
				break;
			}

			case 1: {
				this.zutphenApeldoorn(route);
				break;
			}
			case 2: {
				this.zutphenZwolle(route);
				break;
			}
			case 3: {
				this.zutphenDeventer(route);
				break;
			}
			case 5: {
				this.zutphenAlmelo(route);
				break;
			}
			case 6: {
				this.zutphenHengelo(route);
				break;
			}
			case 7: {
				this.zutphenEnschede(route);
				break;
			}
			case 8: {
				this.zutphenOldenzaal(route);
				break;
			}
			}

			break;
		}
		case 5: {
			switch (destination.getStationID()) {
			case 0: {
				this.almeloAmersfoort(route);
				break;
			}

			case 1: {
				this.almeloApeldoorn(route);
				break;
			}
			case 2: {
				this.almeloZwolle(route);
				break;
			}
			case 3: {
				this.almeloDeventer(route);
				break;
			}
			case 4: {
				this.almeloZutphen(route);
				break;
			}
			case 6: {
				this.almeloHengelo(route);
				break;
			}
			case 7: {
				this.almeloEnschede(route);
				break;
			}
			case 8: {
				this.almeloOldenzaal(route);
				break;
			}
			}

			break;
		}
		case 6: {
			switch (destination.getStationID()) {
			case 0: {
				this.hengeloAmerfoort(route);
				break;
			}

			case 1: {
				this.hengeloApeldoorn(route);
				break;
			}
			case 2: {
				this.hengeloZwolle(route);
				break;
			}
			case 3: {
				this.hengeloDeventer(route);
				break;
			}
			case 4: {
				this.hengeloZutphen(route);
				break;
			}
			case 5: {
				this.hengeloAlmelo(route);
				break;
			}
			case 7: {
				this.hengeloEnschede(route);
				break;
			}
			case 8: {
				this.hengeloOldenzaal(route);
				break;
			}
			}

			break;
		}
		case 7: {
			switch (destination.getStationID()) {
			case 0: {
				this.enschedeAmerfoort(route);
				break;
			}

			case 1: {
				this.enschedeApeldoorn(route);
				break;
			}
			case 2: {
				this.enschedeZwolle(route);
				break;
			}
			case 3: {
				this.enschedeDeventer(route);
				break;
			}
			case 4: {
				this.enschedeZutphen(route);
				break;
			}
			case 5: {
				this.enschedeAlmelo(route);
				break;
			}
			case 6: {
				this.enschedeHengelo(route);
				break;
			}
			case 8: {
				this.enschedeOldenzaal(route);
				break;
			}
			}

			break;
		}
		case 8: {
			switch (destination.getStationID()) {
			case 0: {
				this.oldenzaalAmerfoort(route);
				break;
			}

			case 1: {
				this.oldenzaalApeldoorn(route);
				break;
			}
			case 2: {
				this.oldenzaalZwolle(route);
				break;
			}
			case 3: {
				this.oldenzaalDeventer(route);
				break;
			}
			case 4: {
				this.oldenzaalZutphen(route);
			}
			case 5: {
				this.oldenzaalAlmelo(route);
				break;
			}
			case 6: {
				this.oldenzaalHengelo(route);
				break;
			}
			case 7: {
				this.oldenzaalEnschede(route);
				break;
			}
			}

			break;
		}
		}

		shuttles.add(route);
	}

	public void removeShuttle(Route route) {
		shuttles.remove(route);
	}

	/**
	 * Door het gebruik van een stack moeten de coordinaten in de tegengestelde
	 * richting ingevoerd worden. Zodra er gepopt wordt komt het laatste
	 * toegevoegde coordinaat eruit (FILO / First In Last Out).
	 * 
	 * @param route
	 */

	public void amersfoortDeventer(Route route) {
		amersfoortApeldoorn(route);
		apeldoornDeventer(route);
	}

	public void amersfoortZutphen(Route route) {
		amersfoortApeldoorn(route);
		apeldoornZutphen(route);
	}

	public void amersfoortHengelo(Route route) {
		amersfoortZutphen(route);
		zutphenHengelo(route);
	}

	public void amersfoortAlmelo(Route route) {
		amersfoortDeventer(route);
		deventerAlmelo(route);
	}

	public void amersfoortOldenzaal(Route route) {
		amersfoortHengelo(route);
		hengeloOldenzaal(route);
	}

	public void amersfoortEnschede(Route route) {
		amersfoortHengelo(route);
		hengeloEnschede(route);
	}

	public void zwolleApeldoorn(Route route) {
		zwolleDeventer(route);
		deventerApeldoorn(route);
	}

	public void zwolleZutphen(Route route) {
		zwolleDeventer(route);
		deventerZutphen(route);
	}

	public void zwolleHengelo(Route route) {
		zwolleAlmelo(route);
		almeloHengelo(route);
	}

	public void zwolleEnschede(Route route) {
		zwolleHengelo(route);
		hengeloEnschede(route);
	}

	public void zwolleOldenzaal(Route route) {
		zwolleHengelo(route);
		hengeloOldenzaal(route);
	}

	public void apeldoornZwolle(Route route) {
		apeldoornDeventer(route);
		deventerZwolle(route);
	}

	public void apeldoornAlmelo(Route route) {
		apeldoornDeventer(route);
		deventerAlmelo(route);
	}

	public void apeldoornHengelo(Route route) {
		apeldoornZutphen(route);
		zutphenHengelo(route);
	}

	public void apeldoornOldenzaal(Route route) {
		apeldoornHengelo(route);
		hengeloOldenzaal(route);
	}

	public void apeldoornEnschede(Route route) {
		apeldoornHengelo(route);
		hengeloEnschede(route);
	}

	public void deventerAmersfoort(Route route) {
		deventerApeldoorn(route);
		apeldoornAmersfoort(route);
	}

	public void deventerHengelo(Route route) {
		deventerAlmelo(route);
		almeloHengelo(route);
	}

	public void deventerEnschede(Route route) {
		deventerHengelo(route);
		hengeloEnschede(route);
	}

	public void deventerOldenzaal(Route route) {
		deventerHengelo(route);
		hengeloOldenzaal(route);
	}

	public void almeloZutphen(Route route) {
		almeloHengelo(route);
		hengeloZutphen(route);
	}

	public void almeloApeldoorn(Route route) {
		almeloDeventer(route);
		deventerApeldoorn(route);

	}

	public void almeloAmersfoort(Route route) {
		almeloApeldoorn(route);
		apeldoornAmersfoort(route);
	}

	public void almeloOldenzaal(Route route) {
		almeloHengelo(route);
		hengeloOldenzaal(route);
	}

	public void almeloEnschede(Route route) {
		almeloHengelo(route);
		hengeloEnschede(route);
	}

	public void zutphenAmersfoort(Route route) {
		zutphenApeldoorn(route);
		apeldoornAmersfoort(route);
	}

	public void zutphenZwolle(Route route) {
		zutphenDeventer(route);
		deventerZwolle(route);
	}

	public void zutphenAlmelo(Route route) {
		zutphenHengelo(route);
		hengeloAlmelo(route);
	}

	public void zutphenOldenzaal(Route route) {
		zutphenHengelo(route);
		hengeloOldenzaal(route);
	}

	public void zutphenEnschede(Route route) {
		zutphenHengelo(route);
		hengeloEnschede(route);
	}

	public void hengeloDeventer(Route route) {
		hengeloAlmelo(route);
		almeloDeventer(route);
	}

	public void hengeloApeldoorn(Route route) {
		hengeloZutphen(route);
		zutphenApeldoorn(route);
	}

	public void hengeloAmerfoort(Route route) {
		hengeloApeldoorn(route);
		apeldoornAmersfoort(route);
	}

	public void hengeloZwolle(Route route) {
		hengeloAlmelo(route);
		almeloZwolle(route);
	}

	public void oldenzaalDeventer(Route route) {
		oldenzaalHengelo(route);
		hengeloAlmelo(route);
		hengeloDeventer(route);
	}

	public void oldenzaalApeldoorn(Route route) {
		oldenzaalHengelo(route);
		hengeloZutphen(route);
		zutphenApeldoorn(route);
	}

	public void oldenzaalAmerfoort(Route route) {
		oldenzaalHengelo(route);
		hengeloApeldoorn(route);
		apeldoornAmersfoort(route);
	}

	public void oldenzaalZwolle(Route route) {
		oldenzaalHengelo(route);
		hengeloAlmelo(route);
		almeloZwolle(route);
	}

	public void oldenzaalZutphen(Route route) {
		oldenzaalHengelo(route);
		hengeloZutphen(route);
	}

	public void oldenzaalAlmelo(Route route) {
		oldenzaalHengelo(route);
		hengeloAlmelo(route);
	}

	public void oldenzaalEnschede(Route route) {
		oldenzaalHengelo(route);
		hengeloEnschede(route);
	}

	public void enschedeDeventer(Route route) {
		enschedeHengelo(route);
		hengeloAlmelo(route);
		hengeloDeventer(route);
	}

	public void enschedeApeldoorn(Route route) {
		enschedeHengelo(route);
		hengeloZutphen(route);
		zutphenApeldoorn(route);
	}

	public void enschedeAmerfoort(Route route) {
		enschedeHengelo(route);
		hengeloApeldoorn(route);
		apeldoornAmersfoort(route);
	}

	public void enschedeZwolle(Route route) {
		enschedeHengelo(route);
		hengeloAlmelo(route);
		almeloZwolle(route);
	}

	public void enschedeZutphen(Route route) {
		enschedeHengelo(route);
		hengeloZutphen(route);
	}

	public void enschedeAlmelo(Route route) {
		enschedeHengelo(route);
		hengeloAlmelo(route);
	}

	public void enschedeOldenzaal(Route route) {
		enschedeHengelo(route);
		hengeloOldenzaal(route);
	}

	public void zwolleAmersfoort(Route route) {
		// Point 2 - Zwolle
		x = 307;
		for (y = 28; y < 230; y += PIXELJUMP) {
			route.addCoordinates(x -= PIXELJUMP, y);
		}

		// Point 1 - Point 2
		x = 106;
		for (y = 229; y < 287; y += PIXELJUMP) {
			route.addCoordinates(x, y);
		}

		// Amersfoort - Point 1
		y = 286;
		for (x = 107; x > 10; x -= PIXELJUMP) {
			route.addCoordinates(x, y += PIXELJUMP);
		}
	}

	public void amersfoortZwolle(Route route) {
		// Point 1 - Amersfoort
		x = 11;
		for (y = 381; y > 285; y -= PIXELJUMP) {
			route.addCoordinates(x += PIXELJUMP, y);
		}

		// Point 2 - Point 1
		x = 106;
		for (y = 286; y > 228; y -= PIXELJUMP) {
			route.addCoordinates(x, y);
		}

		// Zwolle - Point 2;
		x = 106;
		for (y = 229; y > 27; y -= PIXELJUMP) {
			route.addCoordinates(x += PIXELJUMP, y);
		}
	}

	public void deventerZwolle(Route route) {
		// Point 1 - Deventer
		x = 357;
		for (y = 240; y > 189; y -= PIXELJUMP) {
			route.addCoordinates(x -= PIXELJUMP, y);
		}

		// Zwolle - Point 1
		x = 307;
		for (y = 190; y > 27; y -= PIXELJUMP) {
			route.addCoordinates(x, y);
		}
	}

	public void zwolleDeventer(Route route) {
		// Point 1 - Zwolle
		x = 307;
		for (y = 28; y < 191; y += PIXELJUMP) {
			route.addCoordinates(x, y);
		}

		// Deventer - Point 1
		x = 307;
		for (y = 190; y < 241; y += PIXELJUMP) {
			route.addCoordinates(x += PIXELJUMP, y);
		}
	}

	public void apeldoornDeventer(Route route) {
		// Point 1 - Apeldoorn
		y = 314;
		for (x = 273; x < 348; x += PIXELJUMP) {
			route.addCoordinates(x, y -= PIXELJUMP);
		}

		// Deventer - Point 1
		y = 240;
		for (x = 347; x < 358; x += PIXELJUMP) {
			route.addCoordinates(x, y);
		}
	}

	public void deventerApeldoorn(Route route) {
		// Point 1 - Deventer
		y = 240;
		for (x = 357; x > 346; x -= PIXELJUMP) {
			route.addCoordinates(x, y);
		}

		// Apeldoorn - Point 1
		x = 347;
		for (y = 240; y < 315; y += PIXELJUMP) {
			route.addCoordinates(x -= PIXELJUMP, y);
		}
	}

	public void amersfoortApeldoorn(Route route) {
		// Point 1 - Amersfoort
		y = 381;
		for (x = 11; x < 107; x += PIXELJUMP) {
			route.addCoordinates(x, y);
		}
		// Point 2 - Point 1
		y = 381;
		for (x = 106; x < 173; x += PIXELJUMP) {
			route.addCoordinates(x, y -= PIXELJUMP);
		}

		// Apeldoorn - Point 2
		y = 314;
		for (x = 173; x < 274; x += PIXELJUMP) {
			route.addCoordinates(x, y);
		}
	}

	public void apeldoornAmersfoort(Route route) {
		// Point 2 - Apeldoorn
		y = 314;
		for (x = 273; x > 172; x -= PIXELJUMP) {
			route.addCoordinates(x, y);
		}

		// Point 1 - Point 2
		x = 173;
		for (y = 314; y < 382; y += PIXELJUMP) {
			route.addCoordinates(x -= PIXELJUMP, y);
		}

		// Amerstfoort - Point 1
		y = 381;
		for (x = 106; x > 10; x -= PIXELJUMP) {
			route.addCoordinates(x, y);
		}
	}

	public void deventerZutphen(Route route) {
		// Deventer - Point 1
		x = 357;
		for (y = 240; y < 279; y += PIXELJUMP, x += PIXELJUMP) {
			route.addCoordinates(x, y);
		}

		// Point 1 - Zutphen
		for (y = 278; y < 351; y += PIXELJUMP) {
			route.addCoordinates(x, y);
		}
	}

	public void zutphenDeventer(Route route) {
		// Zutphen - Point 1
		x = 395;
		for (y = 350; y > 277; y -= PIXELJUMP) {
			route.addCoordinates(x, y);
		}

		// Point 1 - Deventer
		for (y = 278; y > 239; y -= PIXELJUMP, x -= PIXELJUMP) {
			route.addCoordinates(x, y);
		}
	}

	public void apeldoornZutphen(Route route) {
		// Apeldoorn - Point 1
		y = 314;
		for (x = 273; x < 360; x += PIXELJUMP) {
			route.addCoordinates(x, y);
		}

		// Point 1 - Zutphen
		for (y = 314; y < 351; y += PIXELJUMP, x += PIXELJUMP) {
			route.addCoordinates(x, y);
		}
	}

	public void zutphenApeldoorn(Route route) {
		// Zutphen - Point 1
		y = 350;
		for (x = 395; x > 358; x -= PIXELJUMP, y -= PIXELJUMP) {
			route.addCoordinates(x, y);
		}

		// Point 1 - Apeldoorn
		for (x = 359; x > 272; x -= PIXELJUMP) {
			route.addCoordinates(x, y);
		}
	}

	public void zwolleAlmelo(Route route) {
		// Zwolle - Point 1
		y = 28;
		for (x = 307; x < 422; x += PIXELJUMP, y += PIXELJUMP) {
			route.addCoordinates(x, y);
		}

		// Point 1 - Point 2
		for (x = 421; x < 583; x += PIXELJUMP) {
			route.addCoordinates(x, y);
		}

		// Point 2 - Almelo
		x = 582;
		for (y = 142; y < 157; y += PIXELJUMP, x += PIXELJUMP) {
			route.addCoordinates(x, y);
		}
	}

	public void almeloZwolle(Route route) {
		// Almelo - Point 1
		x = 596;
		for (y = 156; y > 141; y -= PIXELJUMP, x -= PIXELJUMP) {
			route.addCoordinates(x, y);
		}

		// Point 1 - Point 2
		for (x = 582; x > 420; x -= PIXELJUMP) {
			route.addCoordinates(x, y);
		}

		// Point 2 - Zwolle
		for (x = 421; x > 306; x -= PIXELJUMP, y -= PIXELJUMP) {
			route.addCoordinates(x, y);
		}
	}

	public void deventerAlmelo(Route route) {
		// Deventer - Point 1
		y = 240;
		for (x = 357; x < 446; x += PIXELJUMP) {
			route.addCoordinates(x, y);
		}

		// Point 1 - Point 2
		for (y = 240; y > 155; y -= PIXELJUMP, x += PIXELJUMP) {
			route.addCoordinates(x, y);
		}

		// Point 2 - Almelo
		for (x = 532; x < 597; x += PIXELJUMP) {
			route.addCoordinates(x, y);
		}
	}

	public void almeloDeventer(Route route) {
		// Almelo - Point 1
		y = 150;
		for (x = 596; x > 530; x -= PIXELJUMP) {
			route.addCoordinates(x, y);
		}

		// Point 1 - Point 2
		for (y = 156; y < 241; y += PIXELJUMP, x -= PIXELJUMP) {
			route.addCoordinates(x, y);
		}

		// Point 2 - Deventer
		for (x = 445; x > 356; x -= PIXELJUMP) {
			route.addCoordinates(x, y);
		}
	}

	public void zutphenHengelo(Route route) {
		// Zutphen - Point 1
		y = 350;
		for (x = 395; x < 492; x += PIXELJUMP) {
			route.addCoordinates(x, y);
		}

		// Point 1 - Point 2
		for (x = 491; x < 604; x += PIXELJUMP, y -= PIXELJUMP) {
			route.addCoordinates(x, y);
		}

		// Point 2 - Hengelo
		for (x = 604; x < 672; x += PIXELJUMP) {
			route.addCoordinates(x, y);
		}
	}

	public void hengeloZutphen(Route route) {
		// Hengelo - Point 1
		y = 235;
		for (x = 672; x > 604; x -= PIXELJUMP) {
			route.addCoordinates(x, y);
		}

		// Point 1 - Point 2
		for (x = 604; x > 491; x -= PIXELJUMP, y += PIXELJUMP) {
			route.addCoordinates(x, y);
		}

		// Point 2 - Zutphen
		for (x = 491; x > 395; x -= PIXELJUMP) {
			route.addCoordinates(x, y);
		}
	}

	public void almeloHengelo(Route route) {
		// almelo - Hengelo
		y = 156;
		for (x = 596; x < 672; x += PIXELJUMP, y += PIXELJUMP) {
			route.addCoordinates(x, y);
		}
	}

	public void hengeloAlmelo(Route route) {
		// Hengelo - Almelo
		y = 235;
		for (x = 672; x > 596; x -= PIXELJUMP, y -= PIXELJUMP) {
			route.addCoordinates(x, y);
		}
	}

	public void hengeloOldenzaal(Route route) {
		// Hengelo - Oldenzaal
		y = 235;
		for (x = 672; x < 727; x += PIXELJUMP, y -= PIXELJUMP) {
			route.addCoordinates(x, y);
		}
	}

	public void oldenzaalHengelo(Route route) {
		// Oldenzaal - Hengelo
		y = 180;
		for (x = 727; x > 672; x -= PIXELJUMP, y += PIXELJUMP) {
			route.addCoordinates(x, y);
		}
	}

	public void hengeloEnschede(Route route) {
		// Hengelo - Enschede
		y = 235;
		for (x = 672; x < 715; x += PIXELJUMP, y += PIXELJUMP) {
			route.addCoordinates(x, y);
		}
	}

	public void enschedeHengelo(Route route) {
		// Enschede - Hengelo
		y = 278;
		for (x = 715; x > 672; x -= PIXELJUMP, y -= PIXELJUMP) {
			route.addCoordinates(x, y);
		}
	}

}
