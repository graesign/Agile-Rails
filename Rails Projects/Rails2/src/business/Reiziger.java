package business;

/*
 * @version 1.0
 */
public class Reiziger{
	
	/* attributes */
	String bestemming;
	String startpunt;
	String mobnr;
	long vertrektijd;
	
	/* constructors */
	
	/**
	 * Creates a traveler
	 * @param bestemming
	 * @param startpunt
	 * @param vertrektijd
	 * @param mobnr
	 */
	public Reiziger(String bestemming, String startpunt, long vertrektijd, String mobnr){
		this.bestemming = bestemming;
		this.startpunt = startpunt;
		this.vertrektijd = vertrektijd;
		this.mobnr = mobnr;
	}
	
	/**
	 * Returns the destination of the traveler
	 * @return bestemming
	 */
	public String getBestemming(){
		return bestemming;
	}

	/**
	 * Returns the start location of the traveler
	 * @return startpunt
	 */
	public String getStartpunt(){
		return startpunt;
	}
	
	/**
	 * Returns the departure time
	 * @return vertrektijd
	 */
	public long getVertrektijd(){
		return vertrektijd;
	}
	
	/**
	 * Returns the mobile number of the traveler
	 * @return mobnr
	 */
	public String getTelefoonNummer(){
		return mobnr;
	}
}