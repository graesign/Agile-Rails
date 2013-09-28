package Business;

public class PerronTerminal extends Terminal {
	private boolean doorOpen;
	private int perronTerminalID;
	private Perron perron;
	private Central central;
	private Station station;

	public PerronTerminal(Central central, Station station, Perron perron, int PerronTerminalID) {
		super(central, station);
		this.station = station;
		this.central = central;
		this.perron = perron;
		this.perronTerminalID = PerronTerminalID;
	}

	public void callShuttle() { // Hoe doen wij dit?

	}

	public Boolean checkPerronLogin(String code, int ID) { // Wat doet dit
															// precies?
		return null;
	}

	public void openDoor() { // Deze methode is er om het principe?
		doorOpen = true;
		doorOpen = false;
	}

	public Perron getPerron() {
		return perron;
	}

	public void setPerron(Perron val) {
		this.perron = val;
	}

	public void getDate() {
	}

	public void getDepartmentTime() {
	}

	public void checkIn(int code) {
		// central.checkInPerron( station, perron, code);
		central.prepareShuttle(station, perron);
	}

	public int getPerronTerminalID() {
		return perronTerminalID;
	}
}
