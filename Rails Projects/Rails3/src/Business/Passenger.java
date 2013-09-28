package Business;

import java.util.*;
import Data.*;

public class Passenger {
	private Calendar incheckTime, departTime;
	private Central central;
	private Station departStation, destinationStation;
	private int code;
	private int perronID = 0;
	private Calendar calendar;

	public Passenger(Central central, Station departStation, Station destinationStation, int code) {
		this.code = code;
		this.central = central;
		this.departStation = departStation;
		this.destinationStation = destinationStation;
		calendar = central.getSimDate();
		calendar.set(2007, 10, 30, 11, 36);
	}

	public Calendar getIncheckTime() {
		return incheckTime;
	}

	public void setIncheckTime(Calendar val) {
		this.incheckTime = val;
	}

	public Calendar getDepartTime() {
		return departTime;
	}

	public void setDepartTime(Calendar val) {
		this.departTime = val;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int val) {
		this.code = val;
	}

	public int getPerronID() {
		return perronID;
	}

	public void setPerronID(int perronID) {
		this.perronID = perronID;
	}

	public void checkInStation() {
		perronID = departStation.getStationTerminal().checkIn(code);
	}

	public void checkInPerron() {
		central.getDatabase().addPassengerWaiting(SimDate.getTimeInMillis(), departStation);
		departStation.getPerron(perronID).getTerminal().checkIn(code);

	}
}
