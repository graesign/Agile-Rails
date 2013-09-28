package Business;

import java.util.ArrayDeque;
import java.util.List;
import Data.*;

public class ShuntArea {
	private final int capacity; // zomaar een waarde om te testen
	// private Shuttle[] shuttles;
	private Central central;
	private int giveFactor;
	private int needFactor;
	private ArrayDeque<Shuttle> shuttles, sendShuttles;
	private static final int CAPDEELFACTOR = 2;
	private int commingMyWay;

	public ShuntArea(Central central, int capacity, int giveFactor, int needFactor) {
		this.giveFactor = giveFactor;
		this.needFactor = needFactor;
		this.capacity = capacity;
		this.central = central;
		shuttles = new ArrayDeque<Shuttle>();

		for (int i = 0; i < capacity / CAPDEELFACTOR; i++) {
			addShuttle(new Shuttle());
		}

		commingMyWay = 0;
	}

	public void addComming() {
		commingMyWay++;
	}

	public void addShuttle(Shuttle s) {
		if (shuttles.size() < capacity) {
			central.getDatabase().addShuttleEmpty(SimDate.getTimeInMillis());

			shuttles.addLast(s);
			commingMyWay--;
		}
	}

	// ///////////Shuttle ArrayList.

	// public void createShuttle() {
	// addShuttle(new Shuttle());
	// }

	public int getAmountOfNeed() {
		double capacityTMP = (double) (capacity / CAPDEELFACTOR);
		double give = giveFactor * 0.01;
		return (int) ((capacityTMP * give) - shuttles.size());
	}

	public int getCapacity() {
		return capacity;
	}

	public int getGiveFactor() {
		return giveFactor;
	}

	public ArrayDeque<Shuttle> getShuntAreaTable() {
		return shuttles;
	}

	public int getShuttleAmount() {
		return shuttles.size();
	}

	public Shuttle getShuttles() {
		if (!shuttles.isEmpty()) {
			return shuttles.pop();
		} else {
			return null;
		}
	}

	public ArrayDeque<Shuttle> giveShuttle(int amount) {
		sendShuttles = new ArrayDeque<Shuttle>();
		if (amount < getAmountToGive()) {
			while (amount-- > 0) {
				sendShuttles.add(shuttles.pop());
			}
		} else {
			while (getAmountToGive() > 0) {
				sendShuttles.add(shuttles.pop());
			}
		}
		return sendShuttles;
	}

	public int getAmountToGive() {
		if (readyToGive()) {
			double capacityTMP = (capacity / CAPDEELFACTOR);
			// capacityTMP += commingMyWay;
			double give = (capacityTMP * (giveFactor * 0.01));
			double need = (capacityTMP * (needFactor * 0.01));

			return ((int) (shuttles.size() - (int) (give - need)));
		} else {
			return 0;
		}
	}

	public boolean readyToGive() {
		double capacityTMP = (capacity / CAPDEELFACTOR);
		capacityTMP += commingMyWay;
		double give = (capacityTMP * (giveFactor * 0.01));

		if (shuttles.size() > give) {
			return true;
		} else {
			return false;
		}
	}

	public boolean needShuttles() {
		if ((shuttles.size() + (commingMyWay / 2)) < ((int) (((double) (capacity / CAPDEELFACTOR)) * ((double) needFactor / 100)))) {
			return true;
		} else {
			return false;
		}
	}

	public void removeShuttle() {
		if (!shuttles.isEmpty()) {
			shuttles.removeFirst();
		}
	}

	public Shuttle sentShuttleToDestination() {
		if (!shuttles.isEmpty()) {
			return shuttles.pop();
		} else {
			return null;
		}
	}

	/* Overbodig gemaakt door overige methodes */

	public void setGiveFactor(int val) {
		this.giveFactor = val;
	}

	public boolean canAccept() {
		return shuttles.size() < capacity;
	}

	public int getTotalShuttleAmount() {
		return getShuttleAmount() + commingMyWay;
	}

	public boolean isOverflowing() {

		return (getTotalShuttleAmount() > (capacity * 0.9));
	}

}