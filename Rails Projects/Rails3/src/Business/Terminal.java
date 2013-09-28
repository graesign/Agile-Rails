package Business;

public abstract class Terminal {
	private Central central;
	private Station station;

	public Terminal(Central central, Station station) {
		this.central = central;
		this.station = station;
	}

	public void getDate() {
	}

	public void getDepartmentTime() {
	}
}
