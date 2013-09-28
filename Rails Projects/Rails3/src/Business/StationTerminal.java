package Business;

public class StationTerminal extends Terminal {
	private int stationTerminalID;
	private Central central;
	private Station station;

	public StationTerminal(Central central, Station station, int id) {
		super(central, station);
		this.central = central;
		this.station = station;
		this.stationTerminalID = id;
	}

	public void checkStationLogin(String code) {
	}

	public void getDepartTime(String code) {
	}

	public int getStationTerminalID() {
		return stationTerminalID;
	}

	public void setStationTerminalID(int val) {
		this.stationTerminalID = val;
	}

	public void getDate() {
	}

	public void getDepartmentTime() {
	}

	// NO USE VERBETERD OP 13 DEC
	public int checkIn(int code) {
		return central.checkInStation(station, code);
	}
}