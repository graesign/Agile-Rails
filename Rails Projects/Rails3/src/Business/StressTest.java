package Business;

import Data.SimDate;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

public class StressTest {

	private final static long DAG_IN_MILLISEC = 86400000;
	public LinkedBlockingQueue<int[]> reservations;
	private Calendar start;
	private Calendar end;
	private Calendar tmpDate;
	private int start_hour;
	private int start_minutes;
	private int end_hour;
	private int end_minutes;
	private Central central;

	// private int depart;
	// private int destination;

	public StressTest(Central central) {
		reservations = new LinkedBlockingQueue<int[]>();
		start = Calendar.getInstance();
		end = Calendar.getInstance();
		tmpDate = Calendar.getInstance();
		this.central = central;
	}

	public void addMassiveReservation(int[] m) {
		try {
			reservations.put(m);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void startStressTest() {
		while (reservations.size() != 0) {
			createReservations(reservations.poll());
		}
	}

	public void createReservations(int[] massiveReservation) {

		int i = 0;
		long marge = 0;
		// depart = massiveReservation[0];
		// destination = massiveReservation[2];
		start_minutes = massiveReservation[4];
		start_hour = massiveReservation[3];
		end_minutes = massiveReservation[6];
		end_hour = massiveReservation[5];
		tmpDate.setTime(new Date(SimDate.getTimeInMillis()));
		start.set(tmpDate.get(Calendar.YEAR), tmpDate.get(Calendar.MONTH), tmpDate.get(Calendar.DAY_OF_MONTH), start_hour, start_minutes);
		end.set(tmpDate.get(Calendar.YEAR), tmpDate.get(Calendar.MONTH), tmpDate.get(Calendar.DAY_OF_MONTH), end_hour, end_minutes);

		if (tmpDate.getTimeInMillis() > start.getTimeInMillis()) {
			start.setTime(new Date(start.getTimeInMillis() + DAG_IN_MILLISEC));
			end.setTime(new Date(end.getTimeInMillis() + DAG_IN_MILLISEC));
		}

		if (start.getTimeInMillis() > end.getTimeInMillis()) {
			end.setTime(new Date(end.getTimeInMillis() + DAG_IN_MILLISEC));
		}

		marge = end.getTimeInMillis() - start.getTimeInMillis();

		for (i = massiveReservation[1]; i != 0; i--) {
			tmpDate.setTime(new Date(start.getTimeInMillis() + (long) (Math.random() * marge)));

			/*
			 * Passenger p = new Passenger(central,
			 * central.getStation(massiveReservation[0]),
			 * central.getStation(massiveReservation[2]),
			 * central.generateCode(central.getStation(massiveReservation[0])));
			 */
			// Passenger p =
			// central.createPassenger(central.getStation(massiveReservation[0]),
			// central.getStation(massiveReservation[2]));
			central.createReservation(central.getStation(massiveReservation[0]), central.getStation(massiveReservation[2]), tmpDate, central.generateCode(central.getStation(massiveReservation[0])));

		}
	}
}