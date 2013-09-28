package Business;

import java.util.ArrayList;

import Data.SimDate;

public class NeedPasser extends Thread {
	private Central central;
	private ArrayList<Station> stations;

	public NeedPasser(Central central) {
		this.central = central;
		this.stations = central.getStationsArrayList();
	}

	public void run() {
		while (true) {
			try {
				for (int i = 0; i < stations.size(); i++) {
					Station station = stations.get(i);

					int offset = 1;

					while (station.needShuttles()) {
						Station neighbor = central.getStation(station.getNearestNeighbor(offset++));
						if (neighbor.getAmountToGive() > 0) {
							central.addVisualShuttle(neighbor, station, neighbor.getShuttle(), java.awt.Color.PINK);
							station.getShuntArea().addComming();
							central.getDatabase().addShuttlesEmptyTraveling(SimDate.getTimeInMillis());
							offset = 1;
						}
						if (offset > 8) {
							offset = 1;
						}
						sleep(100);
					}

				}
				sleep(2000 / central.getAcceleration());
			} catch (InterruptedException e) {
			}
		}
	}
}
