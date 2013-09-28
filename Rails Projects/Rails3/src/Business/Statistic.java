package Business;

public class Statistic {
	private int amountWaitingPassengers;

	private int amountTravelingPassengers;

	private int amountTravelledPassengers;

	private String avgWaitTime;

	private String avgTravelTime;

	private int amountShuntArea;

	private int avgShuttleOccupation;

	private int shuttlesOccupied;

	public Statistic() {
	}

	public int getAmountWaitingPassengers() {
		return amountWaitingPassengers;
	}

	public void setAmountWaitingPassengers(int val) {
		this.amountWaitingPassengers = val;
	}

	public int getAmountTravelingPassengers() {
		return amountTravelingPassengers;
	}

	public void setAmountTravelingPassengers(int val) {
		this.amountTravelingPassengers = val;
	}

	public int getAmountTravelledPassengers() {
		return amountTravelledPassengers;
	}

	public void setAmountTravelledPassengers(int val) {
		this.amountTravelledPassengers = val;
	}

	public String getAvgWaitTime() {
		return avgWaitTime;
	}

	public void setAvgWaitTime(String val) {
		this.avgWaitTime = val;
	}

	public String getAvgTravelTime() {
		return avgTravelTime;
	}

	public void setAvgTravelTime(String val) {
		this.avgTravelTime = val;
	}

	public int getAmountShuntArea() {
		return amountShuntArea;
	}

	public void setAmountShuntArea(int val) {
		this.amountShuntArea = val;
	}

	public int getAvgShuttleOccupation() {
		return avgShuttleOccupation;
	}

	public void setAvgShuttleOccupation(int val) {
		this.avgShuttleOccupation = val;
	}

	public void addPerron(Perron perron) {
	}

	public void removePerron(Perron Unnamed) {
	}

	public void addStation(Station station) {
	}

	public void removeStation(Station station) {
	}

	public void calcAvgSpeed() {
	}

	public int getShuttlesOccupied() {
		return shuttlesOccupied;
	}

	public void setShuttlesOccupied(int val) {
		this.shuttlesOccupied = val;
	}
}
