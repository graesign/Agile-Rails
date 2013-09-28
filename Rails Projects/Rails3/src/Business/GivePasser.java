package Business;

import java.util.ArrayList;
import Data.*;

public class GivePasser extends Thread {
	private Central central;
	private ArrayList<Station> stations;

	public GivePasser(Central central) {
		this.central = central;
		this.stations = central.getStationsArrayList();
	}

	public void run() {
		while (true) {
			try {
				for (int i = 0; i < stations.size(); i++) {
					Station station = stations.get(i);

					while (station.isOverflowing()) {
						Station worst = null, neighbor = null;
						int amountOfWorst = 999;
						for (int j = 1; j < 7; j++) {
							neighbor = central.getStation(station.getNearestNeighbor(j));

							if (neighbor.getTotalShuttleAmount() < amountOfWorst) {
								worst = neighbor;
								amountOfWorst = neighbor.getTotalShuttleAmount();
							}
						}
						Shuttle tmpshut = station.getShuttle();
						if (tmpshut != null && worst != null && station != null) {
							central.addVisualShuttle(station, worst, tmpshut, java.awt.Color.MAGENTA);
							central.getDatabase().addShuttlesEmptyTraveling(SimDate.getTimeInMillis());
							worst.getShuntArea().addComming();
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
