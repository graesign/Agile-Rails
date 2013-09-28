package org.tigam.railcab.algoritme;

import java.io.Serializable;

/** De reiziger klasse representeert een reiziger in de simulatie die vanaf een vertrekpunt naar een bestemming wil reizen met een <code>Taxi</code>. De reiziger houdt zijn wachttijd bij als het nog op het station wacht.
 * @author Mustapha Bouzaidi
 *
 */
public class Reiziger implements Comparable<Reiziger>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6096969574405515181L;
	private int ID;
	private String naam;

	private ReizigerStatus status;
	private long startTijd = 0, wachtTijd = 0;
	private ReisDetails reisDetails;

	/** Creëert een reiziger met een ID, naam en status.
	 * @param ID Identificatie nummer
	 * @param naam Naam van de reiziger
	 * @param status Status van de reiziger
	 */
	public Reiziger(int ID, String naam, ReizigerStatus status) {
		this.ID = ID;
		this.naam = naam;
		this.status = status;
	}

	/** Creëert een reiziger met een ID, nam, starttijd en status.
	 * @param ID Identificatie nummer
	 * @param naam Naam van de reiziger
	 * @param startTijd starttijd voor het bijwerken van de wachttijd
	 * @param status Status van de reiziger
	 */
	public Reiziger(int ID, String naam, long startTijd, ReizigerStatus status) {
		this(ID, naam, status);
		this.startTijd = startTijd;
	}

	/** Geeft het identificatie nummer van de reiziger terug.
	 * @return Identificatie nummer
	 */
	public int getID() {
		return ID;
	}

	/** Stelt de identificatie nummer van de reiziger in.
	 * @param ID Nieuwe identificatie nummer
	 */
	public void setID(int ID) {
		this.ID = ID;
	}

	/** Geeft de naam van de reiziger terug.
	 * @return Naam van de reiziger
	 */
	public String getNaam() {
		return naam;
	}

	/** Stelt de naam van de reiziger in.
	 * @param naam Naam voor de reiziger
	 */
	public void setNaam(String naam) {
		this.naam = naam;
	}

	/** Geeft de wachttijd van de reiziger terug.
	 * @return Wachttijd voor de reiziger
	 */
	public long getWachtTijd() {
		return wachtTijd;
	}

	/** Stelt de wachttijd van de reiziger in.
	 * @param tijd Nieuwe wachttijd van de reiziger
	 */
	public void setWachtTijd(long tijd) {
		this.wachtTijd = tijd;
	}

	/** Geeft de status van de reiziger terug.
	 * @return Status van de reiziger
	 */
	public ReizigerStatus getStatus() {
		return status;
	}

	/** Stelt de status van de reiziger in.
	 * @param status Nieuwe status van de reiziger
	 */
	public void setStatus(ReizigerStatus status) {
		this.status = status;
	}

	/** Voegt de reiziger uit het station en in de taxi. De status van de reiziger veranderd naar ReisStatus.REIST.
	 * @param taxi Taxi waar de reiziger in moet stappen
	 * @return Is waar als de reiziger in de taxi is gestapt en anders onwaar
	 */
	public boolean stapIn(Taxi taxi) {
		if (taxi.voegReiziger(this)) {
			taxi.getStation().verwijderReiziger(this);
			status = ReizigerStatus.REIST;
			return true;
		}
		return false;
	}

	/** Voegt de reiziger uit de taxi. Verandert de status van de reiziger naar ReisStatus.AANGEKOMEN.
	 * @param taxi Taxi waar de reiziger uit moet stappen.
	 */
	public void stapUit(Taxi taxi) {
		if (taxi.verwijderReiziger(this)) {
			status = ReizigerStatus.AANGEKOMEN;
		}
	}

	/** Geeft de reisdetails van de reiziger terug.
	 * @return Reisdetails van de reiziger
	 */
	public ReisDetails getReisDetails() {
		return reisDetails;
	}

	/** stelt de reisdetails van de reiziger in.
	 * @param rDetails Nieuwe reisdetails
	 */
	public void setReisDetails(ReisDetails rDetails) {
		this.reisDetails = rDetails;
	}

	/** Vergelijkt 2 reizigers aan de hand van het identificatienummer, naam, status, wachttijd en reisdetails.
	 * @param r Reiziger om mee te vergelijken
	 * @return Is waar als de 2 reizigers gelijk en anders onwaar.
	 */
	public boolean equals(Reiziger r) {
		if (ID != r.getID() && !naam.equals(r.getNaam())) return false;
		if (!status.equals(r.getStatus()) && getWachtTijd() != r.getWachtTijd()) return false;
		if (!reisDetails.equals(r.getReisDetails())) return false;
		return true;
	}

	public int compareTo(Reiziger r) {
		if (equals(r) || getStartTijd() == r.getStartTijd()) return 0;
		else if (getStartTijd() > r.getStartTijd()) return 1;
		else if (getStartTijd() < r.getStartTijd()) return -1;
		else return 0;
	}

	/** Geeft de starttijd (voor het bijwerken van de wachttijd) van de reiziger terug.
	 * @return Starttijd van de reiziger
	 */
	public long getStartTijd() {
		return startTijd;
	}

	/** Stelt de starttijd van de reiziger in
	 * @param startTijd Begintijd waarmee wachttijd wordt bijgewerkt.
	 */
	public void setStartTijd(long startTijd) {
		this.startTijd = startTijd;
	}

	/** Stelt de starttijd van de reiziger in op de huidige tijd in milliseconden (System.currentTimeMillis).
	 * 
	 */
	public void resetStartTijd() {
		this.startTijd = System.currentTimeMillis();
	}

	/** Geeft aan of de starttijd van de reiziger is ingesteld (hoger dan de waarde 0).
	 * @return Is waar als de starttijd van de reiziger is ingesteld en anders onwaar.
	 */
	public boolean startTijdIsSet() {
		if (startTijd == 0) return false;
		else return true;
	}

	/**
	 * Werkt de wachttijd bij zolang de reiziger de status ReizigerStatus.WACHT heeft. Starttijd wordt gereset na het bijwerken van de wachttijd.
	 */
	public void updateWachtTijd() {
		if (status == ReizigerStatus.WACHT) {
			if (startTijdIsSet()) {
				wachtTijd += (System.currentTimeMillis() - startTijd) * ReisManager.getVersnelFactor();
				resetStartTijd();
			}
		}
	}
}