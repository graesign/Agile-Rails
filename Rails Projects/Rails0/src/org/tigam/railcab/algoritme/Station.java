package org.tigam.railcab.algoritme;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Observable;

/** Het <code>station</code> is een onderdeel van de baanstructuur. Stations bevatten reizigers en/of taxi's.
 * @author Mustapha Bouzaidi
 *
 */
public class Station extends Observable implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5958689306036998509L;

	private int ID;
    private String naam;

    private ArrayList<Reiziger> reizigers;
    private Stationspoor spoor;
    private long positie;
    
    private LinkedList<Taxi> taxis;

    /** Creëert een Station met een ID, naam en een positie op een <code>Stationspoor</code>.
     * @param ID Identificatienummer van het station
     * @param naam Naam van het station
     * @param positieOpSpoor Positie van het station op een <code>Stationspoor</code>
     */
    public Station(int ID, String naam, long positieOpSpoor) {
    	this.ID = ID;
    	this.naam = naam;
    	this.positie = positieOpSpoor;
    	reizigers = new ArrayList<Reiziger>();
    	taxis = new LinkedList<Taxi>();
    }
    
    /** Creëert een Station met een ID, naam, <code>Stationspoor</code> en een positie op het <code>Stationspoor</code>.
     * @param ID Identificatienummer van het station
     * @param naam Naam van het station
     * @param spoor Stationspoor waar het station op zit.
     * @param positieOpSpoor Positie van het station op een <code>Stationspoor</code>
     */
    public Station(int ID, String naam, Stationspoor spoor, long positieOpSpoor) {
    	this(ID, naam, positieOpSpoor);
    	this.spoor = spoor;
    	this.spoor.setStation(this);
    }

    /** Geeft de reizigers op dit <code>Station</code> terug.
     * @return Lijst met reizigers
     */
    public ArrayList<Reiziger> getReizigers() {
        return reizigers;
    }
    
    /** Geeft de namen van de reizigers op dit <code>Station</code> terug.
     * @return Namen van de reizigers
     */
    public String[] getReizigerNamen() {
    	String[] namen = new String[reizigers.size()]; 
    	for (int i = 0; i < reizigers.size(); i++) {
    		namen[i] = reizigers.get(i).getNaam();
    	}
    	return namen;
    }
    
    /** Geeft de reiziger met de opgegeven naam terug.
     * @param naam Naam van de reiziger
     * @return Reiziger met de opgegeven naam.
     */
    public Reiziger getReiziger(String naam) {
    	for (Reiziger r : reizigers) {
    		if (r.getNaam().equals(naam)) return r;
    	}
    	throw new RuntimeException("Reiziger is niet op het station gevonden");
    }

    /** Geeft het aantal reizigers op dit <code>Station</code> terug.
     * @return Het aantal reizigers
     */
    public int getAantalReizigers() {
    	return reizigers.size();
    }

    /** Geeft het aantal taxi's op dit <code>Station</code> terug.
     * @return Het aantal taxi's
     */
    public int getAantalTaxis() {
    	return taxis.size();
    }

    /** Geeft het identificatienummer van dit <code>Station</code> terug.
     * @return Identificatienummer
     */
    public int getID() {
        return ID;
    }

    /** Stelt het identificatienummer van dit <code>Station</code> in.
     * @param ID Nieuwe identificatienummer
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /** Geeft de naam van dit <code>Station</code> terug.
     * @return Naam van het <code>Station</code>
     */
    public String getNaam() {
        return naam;
    }

    /** Stelt de naam van dit <code>Station</code> in.
     * @param naam Nieuwe naam voor het <code>Station</code>.
     */
    public void setNaam(String naam) {
        this.naam = naam;
    }
    
    /** Voegt de taxi aan dit <code>Station</code> toe.
     * @param t Taxi om toe te voegen
     * @return Is waar als de taxi aan het <code>Station</code> is toegevoegd en anders onwaar
     */
    public boolean voegTaxiIn(Taxi t) {
    	return taxis.add(t);
    }
    
    /** Verwijdert de taxi uit dit <code>Station</code>.
     * @param t Taxi om te verwijderen
     * @return Is waar als de taxi is verwijderd en anders onwaar
     */
    public boolean voegTaxiUit(Taxi t) {
    	return taxis.remove(t);
    }
    
    /** Geeft aan of de taxi voorrang heeft om uit dit <code>Station</code> te rijden. De taxi met de hoogste wachttijd heeft voorrang om uit te rijden.
     * @param t Taxi dat wil uitrijden
     * @return Is waar als de taxi voorrang heeft en anders onwaar.
     */
    public boolean voorrang(Taxi t) {
    	Collections.sort(taxis);
    	if (taxis.getLast().equals(t) && !t.isOnbezet()) return true;
    	return false;
    }
    
    /** Geeft het <code>Stationspoor</code> terug waar dit <code>Station</code> op staat.
     * @return Stationspoor
     */
    public Stationspoor getSpoor() {
        return spoor;
    }

	/** Geeft de positie op het <code>Stationspoor</code> terug.
	 * @return Positie
	 */
	public long getPositie() {
		return positie;
	}
	
	/** Geeft de positie terug van het <code>Station</code> vanuit het begin van de zijbaan.
	 * @return Positie
	 */
	public long getZijPositieIn() {
		return spoor.vorige().getLengte() + positie;
	}
	
	/** Geeft de positie terug van het <code>Station</code> tot aan het einde van de zijbaan.
	 * @return Positie
	 */
	public long getZijPositieUit() {
		return spoor.volgende().getLengte() + (spoor.getLengte() - positie);
	}

	/** Stelt de positie van het <code>Station</code> op het <code>Stationspoor</code> in.
	 * @param positieOpSpoor
	 */
	public void setPositie(long positieOpSpoor) {
		this.positie = positieOpSpoor;
	}

	/** Stelt het nieuwe <code>Stationspoor</code> in waar dit <code>Station</code> op zit.
	 * @param spoor Nieuwe <code>Stationspoor</code>
	 */
	public void setSpoor(Stationspoor spoor) {
		this.spoor = spoor;
	}
	
	/** Verwijdert de reiziger uit dit <code>Station</code>.
	 * @param r Reiziger om te verwijderen
	 * @return Is waar als de reiziger is verwijderd uit dit <code>Station</code> en anders onwaar.
	 */
	public boolean verwijderReiziger(Reiziger r) {
		return reizigers.remove(r);
	}
	
	/** Voegt de reizigers aan dit <code>Station</code>.
	 * @param r Reiziger om toe te voegen.
	 * @return Is waar als de reiziger is toegevoegd aan dit <code>Station</code> en anders onwaar.
	 */
	public boolean voegReiziger(Reiziger r) {
		return reizigers.add(r);
	}
 
}
