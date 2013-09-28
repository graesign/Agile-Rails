package Data;

import java.util.ArrayList;
import java.util.Date;

import org.jfree.data.time.Minute;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import Business.Station;

public class Database {
	private int amountTravelingPassengers, // Totaal reizende passagiers
			amountTravelledPassengers, // Totaal vervoerde passagiers
			emptyShuttles, // Totaal lege shuttles
			emptyShuttlesTraveling, // Totaal lege shuttles (reizend)
			occupiedShuttles, // Totaal bemande shuttles
			occupiedShuttlesTraveling, // Totaal bemande shuttles (reizend)
			travelTime, // Totale reistijd van alle passagiers
			travelDistance; // Totale reisafstand van alle passagiers
	private StationData stationData; // station data als wisselde variable
	private TimeSeries passengersTraveling, // Dataset voor reizende passagiers
			passengersTravelled, // Dataset voor gereizde passagiers
			emptyShuttlesTS, // Dataset voor totaal lege shuttles
			emptyShuttlesTravelingTS, // Dataset voor totaal lege shuttles
			// (reizend)
			occupiedShuttlesTS, // Dataset voor totaal bemande shuttles
			occupiedShuttlesTravelingTS,// Dataset voor totaal bemande shuttles
			// (reizend)
			travelTimeTS, // Dataset voor de gemiddelde reistijd
			travelDistanceTS, // Dataset voor de gemiddelde reisafstand
			avgShuttleOccupationTS, // Dataset voor gemiddelde shuttle bezetting
			avgSpeed; // Dataset voor gemiddelde snelheid meter per seconden
	private TimeSeriesCollection dataset; // Dataset container voor TimeSeries
	private ArrayList<StationData> sd; // Container voor stationData bij

	// station x

	public Database() {
		sd = new ArrayList<StationData>();

		travelTimeTS = new TimeSeries("Per Minute Data", Minute.class);
		occupiedShuttlesTravelingTS = new TimeSeries("Per Minute Data", Minute.class);
		occupiedShuttlesTS = new TimeSeries("Per Minute Data", Minute.class);
		emptyShuttlesTravelingTS = new TimeSeries("Per Minute Data", Minute.class);
		emptyShuttlesTS = new TimeSeries("Per Minute Data", Minute.class);
		travelDistanceTS = new TimeSeries("Per Minute Data", Minute.class);
		passengersTravelled = new TimeSeries("Per Minute Data", Minute.class);
		passengersTraveling = new TimeSeries("Per Minute Data", Minute.class);
		avgShuttleOccupationTS = new TimeSeries("Per Minute Data", Minute.class);
		avgSpeed = new TimeSeries("Per Minute Data", Minute.class);

		occupiedShuttlesTravelingTS.removeAgedItems(true);
		occupiedShuttlesTS.removeAgedItems(true);
		emptyShuttlesTravelingTS.removeAgedItems(true);
		avgShuttleOccupationTS.removeAgedItems(true);

		travelTime = 0;
		amountTravelingPassengers = 0;
		amountTravelledPassengers = 0;
		emptyShuttles = 0;
		travelDistance = 0;
		emptyShuttlesTraveling = 0;
		occupiedShuttles = 0;
		occupiedShuttlesTraveling = 0;
	}

	/**
	 * Creeert een nieuwe stationData voor station
	 * 
	 * @param station -
	 *            Station
	 */
	public void addStation(Station station) {
		sd.add(station.getStationID(), new StationData(station.getStationID()));
	}

	/**
	 * Voegt een wachtende passagier toe de statistieken
	 * 
	 * @param date -
	 *            long
	 */
	public void addPassengerWaiting(long date, Station station) {
		stationData = getStationData(station);
		stationData.addPassengerWaiting(date);
	}

	/**
	 * Verwijderd een wachtende passagier uit de statistieken
	 * 
	 * @param date -
	 *            long
	 */
	public void removePassengerWaiting(long date, Station station) {
		this.removePassengerWaiting(date, 1, station);
	}

	/**
	 * Verwijderd een wachtende passagier uit de statistieken
	 * 
	 * @param date -
	 *            long
	 */
	public void removePassengerWaiting(long date, int amountPassengers, Station station) {
		stationData = getStationData(station);
		stationData.removePassengerWaiting(date, amountPassengers);
	}

	/**
	 * Zorgt ervoor dat het gemiddelde wachttijd per minuut in een grafiek komt.
	 * 
	 * @param date -
	 *            Long
	 * @param seconden -
	 *            Integer
	 */
	public void addTimeToWait(long date, int seconden, Station station) {
		stationData = getStationData(station);
		stationData.addTimeToWait(date, seconden);
	}

	/**
	 * Verkrijg de data bij een station.
	 * 
	 * @param station -
	 *            Station
	 * @return - StationData
	 */
	private StationData getStationData(Station station) {
		return sd.get(station.getStationID());
	}

	/**
	 * voegt ammountPassengers aantal toe aan de reizende groep.
	 * 
	 * @param date -
	 *            long
	 * @param ammountPassengers -
	 *            Integer
	 */
	public void addPassengerTraveling(long date, int ammountPassengers, Station departStation) {
		addPassengerTraveling(date, ammountPassengers, departStation, false);
	}

	public void addPassengerTraveling(long date, int ammountPassengers, Station departStation, Boolean waiting) {
		amountTravelingPassengers += ammountPassengers;
		updateTS(passengersTraveling, date, amountTravelingPassengers);
		updateTS(avgShuttleOccupationTS, date, (amountTravelingPassengers * 1.0 / (emptyShuttlesTraveling + occupiedShuttlesTraveling) * 1.0));
		//if (waiting)
		removePassengerWaiting(date, ammountPassengers, departStation);
	}

	/**
	 * voegt 1 persoon toe aan de reizende groep.
	 * 
	 * @param date -
	 *            lon
	 */
	public void addPassengerTraveling(long date, Station departStation) {
		addPassengerTraveling(date, 1, departStation);
	}

	/**
	 * verwijder ammountPassengers passagiers uit de reizende groep.
	 * 
	 * @param date -
	 *            long
	 * @param ammountPassengers -
	 *            Integer
	 */
	public void removePassengerTraveling(long date, int ammountPassengers) {
		if (amountTravelingPassengers > 0) {
			amountTravelingPassengers -= ammountPassengers;
		}
		updateTS(passengersTraveling, date, amountTravelingPassengers);
	}

	/**
	 * verwijder een passagier uit de reizende groep.
	 * 
	 * @param date -
	 *            long
	 */
	public void removePassengerTraveling(long date) {
		this.removePassengerTraveling(date, 1);
	}

	/**
	 * Hoogt het aantal vervoerde personen op met ammountPassengers
	 * 
	 * @param date -
	 *            long
	 * @param ammountPassengers -
	 *            Integer
	 * @param reisTijd -
	 *            Integer (reistijd in seconden)
	 */
	public void addPassengerTransfered(long date, int ammountPassengers, int reisTijd, int reisAfstand) {
		if (ammountPassengers != 0) {
			travelDistance += reisAfstand;
			travelTime += reisTijd;
			amountTravelledPassengers += ammountPassengers;

			removePassengerTraveling(date, ammountPassengers);

			updateTS(passengersTravelled, date, amountTravelledPassengers);
			updateTS(travelTimeTS, date, (travelTime / amountTravelledPassengers));
			updateTS(travelDistanceTS, date, (travelDistance / amountTravelledPassengers));
			updateTS(avgSpeed, date, (travelDistance / travelTime));

		}
	}

	/**
	 * Hoogt het aantal vervoerde personen op met 1
	 * 
	 * @param date -
	 *            long
	 */
	public void addPassengerTransfered(long date, int reisTijd, int reisAfstand) {
		this.addPassengerTransfered(date, 1, reisTijd, reisAfstand);
	}

	/**
	 * Hoogt het aantal lege shuttles op die niet aan het reizen zijn
	 * 
	 * @param date -
	 *            long
	 */
	public void addShuttleEmpty(long date) {
		emptyShuttles++;
		updateTS(emptyShuttlesTS, date, emptyShuttles);
	}

	/**
	 * Verlaagt het aantal lege shuttles die niet aan het reizen zijn
	 * 
	 * @param date -
	 *            long
	 */
	public void removeShuttleEmpty(long date) {
		emptyShuttles--;
		updateTS(emptyShuttlesTS, date, emptyShuttles);
	}

	/**
	 * Hoogt het aantal lege shuttles op die wel aan het reizen zijn
	 * 
	 * @param date -
	 *            long
	 */
	public void addShuttlesEmptyTraveling(long date) {
		emptyShuttlesTraveling++;
		updateTS(emptyShuttlesTravelingTS, date, emptyShuttlesTraveling);
	}

	/**
	 * Verlaagt het aantal lege shuttles die wel aan het reizen zijn
	 * 
	 * @param date -
	 *            long
	 */
	public void removeShuttlesEmptyTraveling(long date) {
		emptyShuttlesTraveling--;
		updateTS(emptyShuttlesTravelingTS, date, emptyShuttlesTraveling);
	}

	/**
	 * Hoogt het aantal bemande shuttles op die niet aan het reizen zijn
	 * 
	 * @param date -
	 *            long
	 */
	public void addShuttleOccupied(long date) {
		occupiedShuttles++;
		updateTS(occupiedShuttlesTS, date, occupiedShuttles);
	}

	/**
	 * Verlaagt het aantal bemande shuttles op die niet aan het reizen zijn
	 * 
	 * @param date -
	 *            long
	 */
	public void removeShuttleOccupied(long date) {
		occupiedShuttles--;
		updateTS(occupiedShuttlesTS, date, occupiedShuttles);
	}

	/**
	 * Hoogt het aantal bemande shuttles op die wel aan het reizen zijn
	 * 
	 * @param date -
	 *            long
	 */
	public void addShuttleOccupiedTraveling(long date) {
		occupiedShuttlesTraveling++;
		updateTS(occupiedShuttlesTravelingTS, date, occupiedShuttlesTraveling);
	}

	/**
	 * Verlaagt het aantal bemande shuttles die wel aan het reizen zijn
	 * 
	 * @param date -
	 *            long
	 */
	public void removeShuttleOccupiedTraveling(long date) {
		occupiedShuttlesTraveling--;
		updateTS(occupiedShuttlesTravelingTS, date, occupiedShuttlesTraveling);
	}

	/**
	 * Geeft de dataset voor aantal wachtende passagiers
	 * 
	 * @return - DataSet
	 */
	public TimeSeriesCollection getPassengersWaitingDS(Station station) {
		stationData = getStationData(station);
		return stationData.getWaitingPassengers();
	}

	/**
	 * Geeft de dataset voor de gemiddelde wachttijd berekend over een bepaalde
	 * minuut
	 * 
	 * @param station -
	 *            Station
	 * @return - DataSet
	 */
	public TimeSeriesCollection getTimeToWait(Station station) {
		stationData = getStationData(station);
		return stationData.getAverageWaitingTimeMinute();
	}

	/**
	 * Geeft de dataset voor de gemiddelde wachttijd berekend over de gehele
	 * duur van de sim
	 * 
	 * @return - DataSet
	 */
	public TimeSeriesCollection getAverageWaitingTimeDay(Station station) {
		stationData = getStationData(station);
		return stationData.getAverageWaitingTimeDay();
	}

	/**
	 * Geeft de dataset voor de gemiddelde wachttijd berekend over een bepaalde
	 * minuut
	 * 
	 * @return - DataSet
	 */
	public TimeSeriesCollection getAverageWaitingTimeMinute(Station station) {
		stationData = getStationData(station);
		return stationData.getAverageWaitingTimeMinute();
	}

	/**
	 * Geeft de dataset voor het aantal reizende passagiers
	 * 
	 * @return - DataSet
	 */
	public TimeSeriesCollection getPassengersTravelingDS() {
		dataset = new TimeSeriesCollection(passengersTraveling);
		return dataset;
	}

	/**
	 * Geeft de dataset voor het aantal vervoerde passagiers
	 * 
	 * @return - Dataset
	 */
	public TimeSeriesCollection getPassengersTravelledDS() {
		dataset = new TimeSeriesCollection(passengersTravelled);
		return dataset;
	}

	/**
	 * Geeft de dataset voor het aantal lege, wachtende, shuttles
	 * 
	 * @return - Dataset
	 */
	public TimeSeriesCollection getEmptyShuttlesDS() {
		dataset = new TimeSeriesCollection(emptyShuttlesTS);
		return dataset;
	}

	/**
	 * Geeft de dataset voor het aantal lege , reizende, shuttles
	 * 
	 * @return - Dataset
	 */
	public TimeSeriesCollection getEmptyShuttlesTravelingDS() {
		dataset = new TimeSeriesCollection(emptyShuttlesTravelingTS);
		return dataset;
	}

	/**
	 * Geeft de dataset voor het aantal bemande, wachtende, shuttles
	 * 
	 * @return - Dataset
	 */
	public TimeSeriesCollection getOccupiedShuttlesDS() {
		dataset = new TimeSeriesCollection(occupiedShuttlesTS);
		return dataset;
	}

	/**
	 * Geeft de dataset voor het aantal bemande, reizende, shuttles
	 * 
	 * @return - Dataset
	 */
	public TimeSeriesCollection getOccupiedShuttlesTravelingDS() {
		dataset = new TimeSeriesCollection(occupiedShuttlesTravelingTS);
		return dataset;
	}

	/**
	 * Geeft de dataset voor de gemiddelde reistijd
	 * 
	 * @return - Dataset
	 */
	public TimeSeriesCollection getTravelTimeDS() {
		dataset = new TimeSeriesCollection(travelTimeTS);
		return dataset;
	}

	/**
	 * Geeft de dataset voor de gemiddelde reisafstand
	 * 
	 * @return - Dataset
	 */
	public TimeSeriesCollection getTravelDistanceDS() {
		dataset = new TimeSeriesCollection(travelDistanceTS);
		return dataset;
	}

	/**
	 * Geeft de dataset voor de gemiddelde shuttlebezetting
	 * 
	 * @return - Dataset
	 */
	public TimeSeriesCollection getAvgShuttleOccupationDS() {
		dataset = new TimeSeriesCollection(avgShuttleOccupationTS);
		return dataset;
	}

	/**
	 * Geeft de dataset voor de gemiddelde snelheid in meter per seconden.
	 * 
	 * @return - Dataset
	 */
	public TimeSeriesCollection getAvgSpeedDS() {
		dataset = new TimeSeriesCollection(avgSpeed);
		return dataset;
	}

	/**
	 * Geeft het huidige aantal wachtende personen terug
	 * 
	 * @return - Integer
	 */
	public int getPassengersWaitingNow(Station station) {
		stationData = getStationData(station);
		return stationData.getPassengersWaiting();
	}

	/**
	 * Geeft aantal passagiers die gebruik hebben gemaakt van station x vanaf de
	 * start van de simulatie.
	 * 
	 * @param station -
	 *            Station
	 * @return - Integer
	 */
	public int getTotalPassengers(Station station) {
		stationData = getStationData(station);
		return stationData.getTotalPassengers();
	}

	/**
	 * Geeft het huidig aantal reizende passagiers
	 * 
	 * @return - Integer
	 */
	public int getPassengersTraveling() {
		return amountTravelingPassengers;
	}

	/**
	 * Geeft het huidig aantal vervoerde personen terug.
	 * 
	 * @return - Integer
	 */
	public int getPassengersTransfered() {
		return amountTravelledPassengers;
	}

	/**
	 * Geeft het huidige aantal lege shuttles die niet aan het reizen zijn
	 * 
	 * @return - Integer
	 */
	public int getShuttlesEmpty() {
		return emptyShuttles;
	}

	/**
	 * geeft het huidig aantal lege shuttles op die wel aan het reizen zijn
	 * 
	 * @return - Integer
	 */
	public int getShuttlesEmptyTraveling() {
		return emptyShuttlesTraveling;
	}

	/**
	 * Geeft het huidig aantal bemande shuttles die niet aan het reizen zijn
	 * 
	 * @return - Integer
	 */
	public int getShuttleOccupied() {
		return occupiedShuttles;
	}

	/**
	 * Geeft het huidig aantal bemande shuttles op die wel aan het reizen zijn
	 * 
	 * @return - Integer
	 */
	public int getShuttleOccupiedTraveling() {
		return occupiedShuttlesTraveling;
	}

	/**
	 * Geeft TRUE als het de eerste item probeert te overschrijven.
	 * 
	 * @param ts
	 * @param date
	 * @return
	 */
	private void updateTS(TimeSeries ts, long date, double value) {
		ts.addOrUpdate(new Second(new Date(date)), value);
		if (ts.getValue(0).intValue() > 0) {
			ts.addOrUpdate(ts.getTimePeriod(0), 0);
		}
	}

	private void updateTS(TimeSeries ts, long date, int value) {
		if (date > 0 && value != 0 && ts != null) {
			ts.addOrUpdate(new Second(new Date(date)), value);
			if (ts.getValue(0).intValue() > 0) {
				ts.addOrUpdate(ts.getTimePeriod(0), 0);
			}
		}
	}
}
