package Business;

import java.util.*;

import Data.*;

public class Shuttle {
	private final static int ACCELERATION = 10, BRAKEDISTANCE = 10, MAXPASSENGERS = 8;
	private Date departTime, arrivalTime;
	// private Station destinationStation;
	private Passenger[] passengers;
	private int passengerAmount = 0; // Hoeveelheid passengers in shuttle en
										// index van het eerste vrije spotje.

	public Shuttle() {
		passengers = new Passenger[MAXPASSENGERS];
		this.departTime = null;
		this.arrivalTime = null;

	}

	public int getAcceleration() {
		return ACCELERATION;
	}

	public Date getDepartTime() {
		return departTime;
	}

	public int getPassengerAmount() {
		return passengerAmount;
	}

	public void setDepartTime() {
		arrivalTime = new Date(SimDate.getTimeInMillis());
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime() {
		arrivalTime = new Date(SimDate.getTimeInMillis());

	}

	public int getBrakeDistance() {
		return BRAKEDISTANCE;
	}

	public int getMaxPassengers() {
		return MAXPASSENGERS;
	}

	/*
	 * public void setDestinationstation( Station s ) { destinationStation = s; }
	 */

	public Passenger[] getPassengers() {
		return passengers;
	}

	public void insertPassenger(Passenger p) {
		passengers[passengerAmount++] = p;
	}

	public void setPassenger(int i) {
		passengerAmount = i;
	}

	public void insertPassenger() {
		passengerAmount++;
	}

	public void insertPassengers(int i) {
		passengerAmount += i;
	}

	public void clearShuttle() { // Passenger objects er uit trappen
		if (passengerAmount > 0) {
			for (int i = 0; i < MAXPASSENGERS; i++) {
				passengers[i] = null;
				passengerAmount--;
			}
		}
	}
}