package Business;

import java.util.*;

/*Het is nog een beetje een chaos zonder java docs, ik denk ik share hem tog ff naar groove.
 * Je kan Central gewoon runnen en genieten van je console.
 * Je kan bepaalde waardes wel veranderen zoals hoeveel hied passengers enzo of 
 * meer perrons maken zodat er meer mensen weg kunnen
 * 
 * Code generatie werkt
 * Reservering aanmaak werkt.
 * Reserverins afhandeling werkt.
 * InChecken op station en perron is mogelijk.
 * 
 * TO DO
 * Shuttles sturen naar ander station
 * Algoritme inbouwen
 * Shuttles ontvangen van ander station
 * javadoc
 * code ordenen en overbodige dingen er uit slopen.
 */

import Data.*;
import UserInterface.*;

public class Central {
	// private ArrayList<Reservation> reservations; // Arraylist met alle
	// reserveringen
	private Stack<Integer> unUsedCodes;
	private ArrayList<Station> stations; // Arraylist met alle stations
	private ArrayList<Shuttle> shuttles; // Arraylist met alle Shuttles
	private ArrayList<ShuntArea> shuntAreas; // Arraylist met alle
	// rangeerterreinen
	// private ArrayList<Passenger> passengers; // ArrayList met ale passengers
	private SimDate simDate;
	private String[] stationNamen = { "Amersfoort", "Apeldoorn", "Zwolle", "Deventer", "Zutphen", "Almelo", "Hengelo", "Enschede", "Oldenzaal" };
	public static int[] stationDrukte = { 35, 30, 35, 20, 15, 20, 35, 25, 10 };
	private Database database;
	private AI AI;
	private boolean debug = false;
	private long code;
	// private static final int MAX_RESERVATIONS_PER_STATION = 9999;
	private HashMap<Integer, Reservation> reservationsCK; // CK = code is key
	private HashMap<String, ReservationUnion> reservationsTK; // TK = tijd is
	// key
	private MainUI main;
	private int debugint = 0;
	private boolean interruptAI, zelfReisCanDepart;
	private Reservation zelfres;
	private TimerTask reservationHandler;

	/**
	 * 6-12-2008 Central beheert alle gegeven betreffend de data. Deze klasse is
	 * de hoofdklasse van de Simulatie Deze klasse verschaft verschillende data
	 * aan andere klasse en wordt dan veelal ook meegegeven aan nieuwe objecten.
	 */

	public Central(MainUI main, String[] shutAmount) {
		this.main = main;

		this.fillStationDrukte(shutAmount);

		simDate = new SimDate();
		simDate.start();

		reservationsCK = new HashMap<Integer, Reservation>();
		reservationsTK = new HashMap<String, ReservationUnion>();

		unUsedCodes = new Stack<Integer>();
		fillCodeStack();

		database = new Database();
		stations = new ArrayList<Station>();
		shuntAreas = new ArrayList<ShuntArea>();
		shuttles = new ArrayList<Shuttle>();
		// reservations = new ArrayList<Reservation>();
		// passengers = new ArrayList<Passenger>();

		// Shunt aanmaken

		for (String s : stationNamen) {

			createStation(stations.size(), stationNamen[stations.size()], stationDrukte[stations.size()], createShuntArea((stationDrukte[stations.size()] * 2), 70, 60));

		}

		/*
		 * // Aantal shuntareas * 100 Shuttles maken for(ShuntArea shuntarea:
		 * shuntAreas){ for(int i = 0; i <= 10; i++){ createShuttle(shuntarea); }
		 * echo("Shuttle Aangemaakt"); }
		 */

		// Amersfoort station, costs
		(stations.get(0)).addNeighbor(1, 1);
		(stations.get(0)).addNeighbor(3, 2);
		(stations.get(0)).addNeighbor(4, 3);
		(stations.get(0)).addNeighbor(2, 4);
		(stations.get(0)).addNeighbor(5, 5);
		(stations.get(0)).addNeighbor(6, 6);
		(stations.get(0)).addNeighbor(7, 7);
		(stations.get(0)).addNeighbor(8, 8);

		// Apeldoorn station, costs
		(stations.get(1)).addNeighbor(3, 1);
		(stations.get(1)).addNeighbor(4, 2);
		(stations.get(1)).addNeighbor(0, 3);
		(stations.get(1)).addNeighbor(2, 4);
		(stations.get(1)).addNeighbor(5, 5);
		(stations.get(1)).addNeighbor(6, 6);
		(stations.get(1)).addNeighbor(7, 7);
		(stations.get(1)).addNeighbor(8, 8);

		// Zwolle station, costs
		(stations.get(2)).addNeighbor(3, 1);
		(stations.get(2)).addNeighbor(5, 2);
		(stations.get(2)).addNeighbor(1, 3);
		(stations.get(2)).addNeighbor(4, 4);
		(stations.get(2)).addNeighbor(6, 5);
		(stations.get(2)).addNeighbor(0, 6);
		(stations.get(2)).addNeighbor(7, 7);
		(stations.get(2)).addNeighbor(8, 8);

		// Deventer station, costs
		(stations.get(3)).addNeighbor(1, 1);
		(stations.get(3)).addNeighbor(4, 2);
		(stations.get(3)).addNeighbor(2, 3);
		(stations.get(3)).addNeighbor(5, 4);
		(stations.get(3)).addNeighbor(0, 5);
		(stations.get(3)).addNeighbor(6, 6);
		(stations.get(3)).addNeighbor(7, 7);
		(stations.get(3)).addNeighbor(8, 8);

		// Zutphen station, costs
		(stations.get(4)).addNeighbor(1, 1);
		(stations.get(4)).addNeighbor(3, 2);
		(stations.get(4)).addNeighbor(6, 3);
		(stations.get(4)).addNeighbor(0, 4);
		(stations.get(4)).addNeighbor(7, 5);
		(stations.get(4)).addNeighbor(5, 6);
		(stations.get(4)).addNeighbor(8, 7);
		(stations.get(4)).addNeighbor(2, 8);

		// Almelo station, costs
		(stations.get(5)).addNeighbor(6, 1);
		(stations.get(5)).addNeighbor(7, 2);
		(stations.get(5)).addNeighbor(8, 3);
		(stations.get(5)).addNeighbor(3, 4);
		(stations.get(5)).addNeighbor(2, 5);
		(stations.get(5)).addNeighbor(1, 6);
		(stations.get(5)).addNeighbor(4, 7);
		(stations.get(5)).addNeighbor(0, 8);

		// Hengelo station, costs
		(stations.get(6)).addNeighbor(7, 1);
		(stations.get(6)).addNeighbor(8, 2);
		(stations.get(6)).addNeighbor(5, 3);
		(stations.get(6)).addNeighbor(4, 4);
		(stations.get(6)).addNeighbor(3, 5);
		(stations.get(6)).addNeighbor(1, 6);
		(stations.get(6)).addNeighbor(2, 7);
		(stations.get(6)).addNeighbor(0, 8);

		// Enschede station, costs
		(stations.get(7)).addNeighbor(6, 1);
		(stations.get(7)).addNeighbor(8, 2);
		(stations.get(7)).addNeighbor(5, 3);
		(stations.get(7)).addNeighbor(4, 4);
		(stations.get(7)).addNeighbor(3, 5);
		(stations.get(7)).addNeighbor(1, 6);
		(stations.get(7)).addNeighbor(2, 7);
		(stations.get(7)).addNeighbor(0, 8);

		// Oldenzaal station, costs
		(stations.get(8)).addNeighbor(6, 1);
		(stations.get(8)).addNeighbor(7, 2);
		(stations.get(8)).addNeighbor(5, 3);
		(stations.get(8)).addNeighbor(4, 4);
		(stations.get(8)).addNeighbor(3, 5);
		(stations.get(8)).addNeighbor(1, 6);
		(stations.get(8)).addNeighbor(2, 7);
		(stations.get(8)).addNeighbor(0, 8);

		// ELKE 60 SIMSECONDE :) Onderstaande code uitvoeren.
		initTimer();

		AI = new AI(this);
		AI.start();

		NeedPasser np = new NeedPasser(this);
		np.start();

		GivePasser gp = new GivePasser(this);
		gp.start();

	}

	public void fillStationDrukte(String[] shutAmount) {
		for (int i = 0; i < shutAmount.length; i++) {
			Central.stationDrukte[i] = (Integer.parseInt(shutAmount[i]));

		}
	}

	public AI getAI() {

		return AI;
	}

	public Reservation getZelfreis() {
		return zelfres;
	}

	public void departZelfreis() {
		zelfReisCanDepart = true;
	}

	public void resetZelfreis() {
		zelfres = null;
		zelfReisCanDepart = false;
	}

	public void initTimer() {
		reservationHandler = new TimerTask() {
			public void run() {

				setTitle(); // Nieuwe Titel zetten met de simulatie tijd:)
				main.refreshInfoLabels();

				echo("Check nieuwe reserveringen .......");

				if (zelfres != null) {
					long dif = zelfres.getDepartTime().getTimeInMillis() - SimDate.getTimeInMillis();
					if (dif > 0 && dif < (1000 * 20) * simDate.getDateAcceleration() && !zelfReisCanDepart)
						main.getInfoPanel().drawButton(zelfres, dif);
					else
						main.getInfoPanel().removeButton(zelfres);
				}

				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date(simDate.getSimDate() - (1000 * 60))); // HUIDIGE
				// SIMDATE
				// - 1
				// SIM
				// MINUUT
				String time = buildTimeString(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));

				if (reservationsTK.containsKey(time)) {

					// System.out.println("JUP IK HEB"+time);
					ReservationUnion reservations = reservationsTK.get(time);
					Map tmpMap = reservations.getReservation();
					if (!tmpMap.isEmpty()) {

						Iterator it = tmpMap.values().iterator();

						while (it.hasNext()) {
							Stack tmp = (Stack) it.next();

							Station departStation = null;
							Reservation reservation = null;
							int perronID;

							try {
								reservation = (Reservation) tmp.pop();
								perronID = checkInStation(reservation.getDepartStation(), reservation.getCode());
								tmp.push(reservation);
							} catch (Exception e) {
								perronID = -1;
							}

							while (!tmp.empty() && perronID != -1) {

								for (int j = 0; j < 8 && !tmp.empty(); j++) {
									reservation = (Reservation) tmp.pop();
									
									if ((reservation.getZelfreis() && zelfReisCanDepart) || !reservation.getZelfreis()) {
										code = (int) reservation.getCode();
										departStation = reservation.getDepartStation();
										database.addPassengerWaiting(SimDate.getTimeInMillis(), departStation);
										reservation.setDeparted();
										departStation.getPerron(perronID).addPassenger();

										// Door de code door tien te delen word
										// het
										// station ID er af gehaald
										unUsedCodes.push(((int) code));
										reservationsCK.remove(code);
										if (reservation.getZelfreis())
											resetZelfreis();
										}
								}
								
								if(departStation != null)
								{
									departStation.prepareShuttle(departStation.getPerron(perronID), reservation.getZelfreis());
									departStation.clearPerron(departStation.getPerron(perronID));
									reservation.getArrivalStation().getShuntArea().addComming();
									perronID = checkInStation(departStation, (int) code);
								}
								
								
							
							}
							if (perronID != -1 && !reservation.getZelfreis()) departStation.clearPerron(departStation.getPerron(perronID));
							else if(reservation.getZelfreis()){
								resetZelfreis();
								main.getOwnTravelPanel().startOver();
							}
							while (!tmp.empty()) {
								reservation = (Reservation) tmp.pop();
								code = (int) reservation.getCode();
								departStation = reservation.getDepartStation();
								Station arrivalStation = reservation.getArrivalStation();

								Calendar departTime = reservation.getDepartTime();

//								database.addPassengerWaiting(SimDate.getTimeInMillis(), departStation);
								departTime.setTimeInMillis(departTime.getTimeInMillis() + 120000);

								createReservation(departStation, arrivalStation, departTime, (int) code, reservation.getZelfreis(), true);
							}

							reservations.removeStack(tmp);
						}

					}
					reservationsTK.remove(time);
					System.out.println(reservationsTK);

				}

			}
		};

		new Timer().scheduleAtFixedRate(reservationHandler, 0, 60000 / simDate.getDateAcceleration());

	}

	public void updateTimer() {
		try {
			reservationHandler.cancel();
		} finally {
			initTimer();
		}
	}

	private void fillCodeStack() {
		for (int i = 10000; i >= 0; i--) {
			unUsedCodes.push(i);
		}
	}

	// //////////Reservation ArrayList.

	/**
	 * Maakt een reservering en voegt deze toe aan de reserveringen Array
	 * 
	 * @param -
	 *            Station, Station, Date, int
	 */
	public Reservation createReservation(Station departStation, Station arrivalStation, Calendar departTime, int code) {
		Reservation res = createReservation(departStation, arrivalStation, departTime, code, false, false);
		return res;
	}

	public Reservation createReservation(Station departStation, Station arrivalStation, Calendar departTime, int code, Boolean zelfreis) {
		return createReservation(departStation, arrivalStation, departTime, code, zelfreis, false);
	}

	public Reservation createReservation(Station departStation, Station arrivalStation, Calendar departTime, int code, Boolean zelfreis, Boolean waiting) {
		Reservation res = new Reservation(departStation, arrivalStation, departTime, code, zelfreis);
		String key = buildTimeString(departTime.get(Calendar.HOUR_OF_DAY), departTime.get(Calendar.MINUTE));
		if (zelfreis) {
			zelfres = res;
		}
		if (waiting) {
			res.setWaiting();
			//database.addPassengerWaiting(simDate.getTimeInMillis(), departStation);
		}
		if (!reservationsTK.containsKey(key)) {
			ReservationUnion unionRing = new ReservationUnion();
			unionRing.addReservation(res);
			reservationsTK.put(key, unionRing);

		} else {
			reservationsTK.get(key).addReservation(res);

		}
		reservationsCK.put(code, res);
		return res;
	}

	// public ArrayList<Reservation> getReservation() {
	// return reservations;
	// }

	public Reservation getReservationCK(int index) {
		Reservation reservation = null;
		reservation = this.reservationsCK.get(index);
		return reservation;
	}

	// ///////////Begin Code genererator methoden.

	/**
	 * Geeft een uniek gegenereerde code tussen de 0 en de 10000
	 * 
	 * @return - int
	 */
	public int generateCode(Station s) {
		// int test = unUsedCodes.pop();
		// echo("\n test unUsedCodes "+test+" "+s.getStationID());
		// String temp = ""+test;
		code = unUsedCodes.pop();
		echo("\nPassagier krijgt de volgende code: " + code);
		return (int) code;
	}

	/**
	 * Geeft aan of de opgegeven String een uniek gegenereerde code is.
	 * 
	 * @param -
	 *            long
	 * @return - Boolean
	 */
	public boolean codeIsAvailable(long code, Station s) {
		// if(reservations.isEmpty()) return true;
		if (reservationsCK.containsKey(code)) {
			return false;
		}
		return true;
	}

	// ///////////Station ArrayList.

	public void createStation(int stationid, String stationname, int stationDrukte, ShuntArea shuntarea/*
																										 * ,
																										 * ArrayList
																										 * nebers
																										 */) {
		Station tmp = new Station(this, stationid, stationname, stationDrukte, shuntarea);
		stations.add(tmp);
		database.addStation(tmp);

	}

	public void addVisualShuttle(Station vertrek, Station bestemming, Shuttle shuttle, java.awt.Color color) {
		main.getOverViewPanel().addShuttle(vertrek, bestemming, shuttle, color);

		if (shuttle.getPassengerAmount() != 0)
			database.addShuttleOccupiedTraveling(SimDate.getTimeInMillis());

	}

	public void removeStation(String stationname) {
		for (Station station : stations) {
			if (stationname.equals(station.getStationName())) {
				stations.remove(station);
				/*
				 * if(stations.indexOf(station) != stations.size()){
				 * stations.add(stations.get(stations.size()));
				 * stations.remove(stations.size()); }
				 */

			}
		}
	}

	public Database getDatabase() {
		return database;
	}

	/*
	 * Database Vuller :)
	 * 
	 * 
	 * 
	 * 
	 * public void addTravelingPassengerToDb(Station s){
	 * database.addPassengerTraveling(simDate.getSimDate(), s); }
	 * 
	 * public void addTransferedPassengerToDb(){
	 * database.addPassengerTransfered(simDate.getSimDate(),
	 * (int)simDate.getSeconds(), 20); }
	 * 
	 * public void addTransferedPassengerToDb(int amount){
	 * 
	 * database.addPassengerTransfered(simDate.getSimDate(), amount,
	 * (int)simDate.getSeconds(), 20); }
	 * 
	 * public void addStationToDb(Station s){ database.addStation(s); }
	 * 
	 * public void addEmptyShuttleToDb(){
	 * database.addShuttleEmpty(simDate.getSimDate()); }
	 * 
	 * public void addTravelingShuttleToDb(){
	 * database.addShuttleOccupiedTraveling(SimDate.getTimeInMillis()); }
	 * 
	 * public void addWaitingPassengerToDb(Station s){
	 * database.addPassengerWaiting(simDate.getSimDate(), s); }
	 */
	/**
	 * Maakt een shuttle aan en voegt hem toe in de ArrayList
	 * 
	 * @return - void
	 */

	// ///////////ShuntArea ArrayList.
	/**
	 * Maakt een ShuntArea aan.
	 * 
	 * @param -
	 *            int
	 * @return - void
	 */

	public Station getStation(int index) {
		return stations.get(index);
	}

	public ShuntArea createShuntArea(int capacity, int giveFactor, int needFactor) {
		ShuntArea newshunt = new ShuntArea(this, capacity, giveFactor, needFactor);
		shuntAreas.add(newshunt);
		return newshunt;

	}

	/*
	 * public Passenger createPassenger(Station departStation, Station
	 * destinationStation ){ Passenger p = new Passenger(this, departStation,
	 * destinationStation, generateCode(departStation)); passengers.add(p);
	 * return p; }
	 */

	/**
	 * Genereerd een nieuwe datum gebaseerd op de simulatie datum.
	 * 
	 * @return - ArrayList
	 */

	public Calendar getSimDate() {
		return simDate.getTimer();
	}

	public SimDate getSimDateOb() {
		return simDate;
	}

	public int getAcceleration() {
		return simDate.getDateAcceleration();
	}

	public void setAcceleration(int val) {
		simDate.setDateAcceleration(val);
	}

	public void setTitle() {
		main.setTitle("Railcab Simulator: " + simDate.getHours() + ":" + ((simDate.getMinute() < 10) ? "0" + simDate.getMinute() : simDate.getMinute()));
	}

	/*
	 * public void setNeed(Shuntarea shuntarea) { }
	 * 
	 * public void CreateRandomReservation(Station DepartStation, Station
	 * DestinationStation, Simdate DepartTime) { }
	 */

	// WAAROM ALLE RESERVERINGEN LANGS GAAN :P JE WEET AL DE DEPART STATION DAT
	// GEEF JE OP TOCH ?
	public int checkInStation(Station station, int c) {
		echo("Central checked passenger in op " + station + "met de volgende code: " + c);
		echo("" + reservationsCK.get(c));

		return station.arangePerron(reservationsCK.get(c).getArrivalStation());

	}

	public void prepareShuttle(Station s, Perron p) {
		s.prepareShuttle(p);
	}

	// FIX THIS 2 METHODES ? DIE HETZELFDE DOEN ERROR ERROR :P

	public ArrayList<Station> getStationsArrayList() {
		return stations;
	}

	public void echo(String s) {
		if (debug)
			System.out.println(s);
	}

	public String buildTimeString(int hour, int minute) {
		String key = null;

		if (hour < 10) {
			key = "0" + hour;
		} else {
			key = "" + hour;
		}

		if (minute < 10) {
			key += "0" + minute;
		} else {
			key += minute;
		}

		return key;
	}

}
