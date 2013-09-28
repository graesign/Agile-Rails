package simulatie;

/*
 * @version 0.5
 */
public class Route{
	/* attributes */
	private String startpunt;
	private String bestemming;
	private long vertrektijd;
	boolean opgehaald = false;

	/* constructors */
	/**
	 * Creates a route with the given values
	 * @param startpunt
	 * @param bestemming
	 * @param vertrektijd
	 */
	public Route( String startpunt, String bestemming, long vertrektijd ){
		this.startpunt = startpunt;
		this.bestemming = bestemming;
		this.vertrektijd = vertrektijd;
	}

	/* methods */
	/**
	 * Sets route
	 * @param startpunt
	 * @param bestemming
	 * @param vertrektijd
	 */
	public void setRoute( String startpunt, String bestemming, long vertrektijd ){
		this.startpunt = startpunt;
		this.bestemming = bestemming;
		this.vertrektijd = vertrektijd;
	}

	/**
	 * Returns point of departure
	 * @return startpunt
	 */
	public String getStartpunt(){
		return startpunt;
	}

	/**
	 * Returns point of destination
	 * @return bestemming
	 */
	public String getBestemming(){
		return bestemming;
	}

	/**
	 * Returns time of departure
	 * @return vertrektijd
	 */
	public long getVertrektijd(){
		return vertrektijd;
	}

	/**
	 * If a traveler can board the train this method is called
	 *
	 */
	public void setOpgehaald(){
		opgehaald = true;
	}

	/**
	 * returns if the passenger is onboard.
	 * @return
	 */
	public boolean getOpgehaald(){
		return opgehaald;
	}
}