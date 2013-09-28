package Data;

import java.util.Calendar;
import java.util.Date;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class StationData {
	private int 		stationID,
						totalPassengers, 				// Totaal aantal passagiers die op het station zijn geweest
						waitingPassengers, 				// huidig aantal wachtende
						totalWaitingTime, 				// Totale wachttijd (Rekenen)
						totalWaited, 					// Totaal aantal gewacht (Rekenen)
						avgWaitingTime, 				// Gemiddelde wachttijd
						AmountWaited, 					// Aantal personen voor gemiddelde per minuut
						secondsWaited; 					// Aantal seconden voor gemiddelde per minuut
	private TimeSeries	waitingPassengersTS, 			// Coordinaten + waarde voor wachtende passagiers
						avgWaitingTimeDayTS, 			// Coordinaten + waarde voor gemiddelde wachttijd
						avgWaitingTimeMinuteTS; 		// Coordinaten + waarde voor gemiddelde wachttijd
	private TimeSeriesCollection dataset; 				// Container van TimeSeries
	private Calendar avgDate, tmpDate;

	/**
	 * StationData zorgt ervoor dat station gerelateerde gegevens opgeslagen
	 * worden en grafieken de benodigde gegevens krijgen.
	 * 
	 * @param stationID - Integer
	 */
	public StationData(int stationID) {
		this.stationID = stationID;
		secondsWaited = avgWaitingTime = totalWaitingTime = totalWaited = waitingPassengers = 0;
		avgWaitingTimeMinuteTS = avgWaitingTimeDayTS = waitingPassengersTS = new TimeSeries("Per Minute Data", Minute.class);
		avgDate = Calendar.getInstance();
		avgDate.setTime(new Date());
		tmpDate = avgDate;
	}

	/**
	 * Geeft de dataset voor aantal wachtende passagiers
	 * 
	 * @return - DataSet
	 */
	public TimeSeriesCollection getWaitingPassengers() {
		dataset = new TimeSeriesCollection(waitingPassengersTS);
		return dataset;
	}

	/**
	 * Geeft de dataset voor de gemiddelde wachttijd berekend over de hele dag
	 * 
	 * @return - DataSet
	 */
	public TimeSeriesCollection getAverageWaitingTimeDay() {
		dataset = new TimeSeriesCollection(avgWaitingTimeDayTS);
		return dataset;
	}

	/**
	 * Geeft de dataset voor de gemiddelde wachttijd berekend over een minuut
	 * 
	 * @return - DataSet
	 */
	public TimeSeriesCollection getAverageWaitingTimeMinute() {
		dataset = new TimeSeriesCollection(avgWaitingTimeMinuteTS);
		return dataset;
	}

	/**
	 * Berekent het nieuwe gemiddelde wachttijd en voegt deze in de DataSet.
	 * 
	 * @param date - long (tijd verstreken sinds 1-1-1970)
	 * @param seconden - aantal seconden dat er gewacht is
	 */
	private void updateAvgWaitingTime(long date, int seconden) {
		totalWaitingTime += seconden;
		avgWaitingTime = totalWaitingTime / totalWaited;
		tmpDate.setTime(new Date(date));
		avgWaitingTimeDayTS.addOrUpdate(new Minute(tmpDate.get(Calendar.MINUTE), new Hour(new Date(date))), avgWaitingTime);
	}

	/**
	 * Zorgt ervoor dat het gemiddelde wachttijd per minuut in een grafiek komt.
	 * 
	 * @param date - Long
	 * @param seconden - Integer
	 */
	public void addTimeToWait(long date, int seconden) {
		updateAvgWaitingTime(date, seconden);
		tmpDate.setTime(new Date(date));

		if (avgDate.get(Calendar.HOUR_OF_DAY) == tmpDate.get(Calendar.HOUR_OF_DAY) && avgDate.get(Calendar.MINUTE) == tmpDate.get(Calendar.MINUTE)) {
			AmountWaited++;
			secondsWaited += seconden;
		} else {
			avgWaitingTimeMinuteTS.addOrUpdate(new Minute(tmpDate.get(Calendar.MINUTE), new Hour(new Date(date))), (secondsWaited / AmountWaited));
			AmountWaited = 1;
			secondsWaited = seconden;
		}
	}

	/**
	 * Interne functie om de grafiek voor aantal wachtende passagiers te updaten.
	 * 
	 * @param date - long (Tijd verstreken sinds 1-1-1970)
	 */
	private void updatePassengerWaiting(long date) {
		tmpDate.setTime(new Date(date));
		waitingPassengersTS.addOrUpdate(new Minute(tmpDate.get(Calendar.MINUTE), new Hour(new Date(date))), getPassengersWaiting());
	}

	/**
	 * Voegt een wachtende passagier toe de statistieken
	 * 
	 * @param date - long (Tijd verstreken sinds 1-1-1970)
	 */
	public void addPassengerWaiting(long date) {
		waitingPassengers++;
		totalPassengers++;
		updatePassengerWaiting(date);
	}

	/**
	 * Verwijderd een wachtende passagier uit de statistieken
	 * 
	 * @param date - long (Tijd verstreken sinds 1-1-1970)
	 */
	public void removePassengerWaiting(long date, int amountPassengers) {
		waitingPassengers -= amountPassengers;
		updatePassengerWaiting(date);
	}

	/**
	 * Geeft het huidige aantal wachtende personen terug
	 * 
	 * @return - Interger
	 */
	public int getPassengersWaiting() {
		return waitingPassengers;
	}

	/**
	 * Geeft het stationID terug waar de gegevens aan gekoppeld zijn
	 * 
	 * @return - Integer
	 */
	public int getStationID() {
		return stationID;
	}
	
	/**
	 * @return Integer
	 */
	public int getTotalPassengers() {
		return totalPassengers;
	}

}
