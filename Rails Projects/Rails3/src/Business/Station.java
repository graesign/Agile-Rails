package Business;

import java.util.*;

import Data.*;

public class Station {
	private Central central;
	private ArrayDeque<Perron> freeperrons; // 
	private HashMap<Integer, Integer> neighbors;
	private ArrayList<Perron> occupiedperrons;
	// station
	// terminals
	// ook
	// index.
	private ShuntArea shuntArea;
	private Shuttle shuttle = null;
	private int stationID, stationterminalsamount = 0, perrons = 0; // Hoeveelheid
	private String stationname;
	// private ArrayList<ReservationUnion> reservationUnions;
	private StationTerminal[] stationterminals;
	private int stationDrukte;

	public Station(Central central, int stationID, String stationname, int stationDrukte, ShuntArea shuntArea/*
																												 * ,
																												 * ArrayList
																												 * nebers
																												 */) { // Er
																																			// dient
																																			// een
		// lijst met station
		// Terminals te
		// komen woot
		this.central = central;
		this.shuntArea = shuntArea;
		this.stationID = stationID;
		this.stationname = stationname;
		this.stationDrukte = stationDrukte;
		stationterminals = new StationTerminal[5];
		freeperrons = new ArrayDeque<Perron>();
		occupiedperrons = new ArrayList<Perron>();
		addStationTerminals();
		// makeNebers();
		createPerron(); // Afhankelijk van capacitiet van de shuntarea.
		createPerron();
		createPerron();
		createPerron();
		createPerron();
		createPerron();
		// reservationUnions = new ArrayList<ReservationUnion>();
		neighbors = new HashMap<Integer, Integer>();
	}

	public void acceptShuttle(Shuttle s) {
		this.shuttle = s;
		if (!isEmpty()) {

			Perron tmpPerron = popFreePerron();
			tmpPerron.setShuttle(shuttle);
			// tmpPerron.sentShuttleToShuntArea();
			clearPerron(tmpPerron);

			// occupiedperrons.get(occupiedperrons.size() - 1).setShuttle(null);

			// fix this SHUTTLE IS NU 1 OP 1 met de passagier dus na het
			// arriveren van een shuttle del passagier

		}
	}

	public void addNeighbor(int i, int costs) {
		this.neighbors.put(costs, i);
	}

	/**
	 * Zorgt ervoor dat het StationTerminals totaal 5 word. Hij maakt ze zelf
	 * aan.
	 */

	public int getStationDrukte() {
		return stationDrukte;
	}

	public void addStationTerminals() {
		while (stationterminalsamount < 5)
			stationterminals[stationterminalsamount++] = new StationTerminal(central, this, stationterminalsamount);
	}

	// /// Perron regel methodes
	/**
	 * Als iemand inchecked op het station moet deze methode aangeroept worden
	 * om te bepalen naar welk perron de passenger moet. Regelt een perron en
	 * laat een shuttle geregeld worden en returned het perronid. Aan de hand
	 * hiervan moet je dan ff het perron opvragen voor je shuttle. Deze moet
	 * wellicht nog worden aangepast.
	 * 
	 * @return - int
	 */
	public int arangePerron(Station destinationStation) {
		if (!isEmpty() && getShuttleAmount() > 0) {
			Perron p = popFreePerron();
			occupiedperrons.add(p);

			p.setDestinationStation(destinationStation); // FIX THIS
			return p.getPerronID();
		
		}
		return -1;

	}

	/**
	 * Als er een shuttle vertrokken is moet deze methode aangeroepen worden.
	 * Maakt een perron weer leeg aan de hand van het perronid. Dit kan nog
	 * omgebouwd worden dat arg Perron is ofzo.
	 * 
	 * @param -
	 *            int
	 */
	public void clearPerron(int perronID) {
		for (Perron perron : occupiedperrons) {
			if (perron.getPerronID() == perronID) {
				pushFreePerron(perron);
				occupiedperrons.remove(perron);
			}
		}
	}

	// TIM V:)
	public void clearPerron(Perron perron) {
		occupiedperrons.remove(perron);
		pushFreePerron(perron);
	}

	/**
	 * Maakt een nieuw perron aan voor dit station.
	 */
	public void createPerron() {
		pushFreePerron(new Perron(central, this, freeperrons.size()));
		// perrons++;

		// Fix this
	}

	public int getAmountOfNeed() {
		return shuntArea.getAmountOfNeed();
	}

	public int getAmountToGive() {
		return shuntArea.getAmountToGive();
	}

	public int getNearestNeighbor(int offset) {
		return neighbors.get(offset);
	}

	public Perron getPerron(int perronID) {
		for (Perron perron : occupiedperrons) {
			if (perron.getPerronID() == perronID) {
				return perron;
			}
		}
		return null;
	}

	public ShuntArea getShuntArea() {
		return shuntArea;
	}

	public Shuttle getShuttle() {
		return shuntArea.getShuttles();
	}

	/*
	 * public ArrayList getReservationUnion() { return reservationUnions; }
	 * 
	 * public void setReservationUnion(ArrayList<ReservationUnion> val) {
	 * this.reservationUnions = val; }
	 * 
	 * public void addReservationUnion(ReservationUnion r) {
	 * reservationUnions.add(r); }
	 */

	public int getShuttleAmount() {
		return shuntArea.getShuttleAmount();
	}

	public int getStationID() {
		return stationID;
	}

	public String getStationName() {
		return stationname;
	}

	public StationTerminal getStationTerminal() {
		return stationterminals[stationterminalsamount - 1];
	}

	public boolean isEmpty() {
		return freeperrons.isEmpty();
	}

	public boolean needShuttles() {
		return shuntArea.needShuttles();
	}

	public Perron peekFreePerron() {
		return freeperrons.getLast();
	}

	public Perron popFreePerron() {
		occupiedperrons.add(peekFreePerron());
		return freeperrons.removeLast();
	}

	public void prepareShuttle(Perron perron) { // VIA PERRON TERMINAL NAAR
		prepareShuttle(perron, false);
	}

	public void prepareShuttle(Perron perron, boolean zelfreis) {
		// CENTRALE VANAF CENTRALE NAAR
		// STATION BEETJE METHOD
		// REDIRECTION NIET NICE

		if (shuntArea.getShuttleAmount() != 0) {
			perron.setShuttle(shuntArea.sentShuttleToDestination()); // sentshuttle
			perron.sentShuttle(zelfreis);

		}

	}

	// ///// Stack methodes
	public void pushFreePerron(Perron p) {
		freeperrons.addLast(p);
	}

	public ArrayDeque<Shuttle> sendShuttle(int amount) {
		return shuntArea.giveShuttle(amount);
	}

	public void setShuntarea(ShuntArea val) {
		this.shuntArea = val;
	}

	public int getTotalShuttleAmount() {
		return shuntArea.getTotalShuttleAmount();
	}

	public boolean isOverflowing() {
		return shuntArea.isOverflowing();
	}

}
