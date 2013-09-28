package org.tigam.railcab.algoritme;
import java.io.Serializable;
import java.util.ArrayList;

/** Het <code>Spoor</code> is een onderdeel van de baanstructuur en dient vaak als een hoofdspoordeel. 
 * Taxi's kunnen zich op een <code>Spoor</code> voortbewegen. 
 * <code>Spoor</code> objecten zijn d.m.v. een dubbel linked list implementatie aan elkaar gekoppeld.
 * @author Mustapha Bouzaidi
 *
 */
public class Spoor implements Iterable<Spoor>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5810133340497993491L;
	/**
	 * Alphanumerieke identficatienummer.
	 */
	protected String ID;
	/**
	 * Lengte van het spoordeel.
	 */
	protected long lengte;
	/**
	 * Het soort spoordeel, zie Spoortype enumeratie.
	 */
	protected SpoorType type;
	/**
	 * Een spoordeel dat gekoppeld is aan de voorzijde van dit spoordeel.
	 */
	protected Spoor voorSpoor;
	/**
	 * Een spoordeel dat gekoppeld is aan de achterzijde van dit spoordeel.
	 */
	protected Spoor naSpoor;
	/**
	 * De taxi(s) die zich op dit spoordeel bevinden.
	 */
	protected ArrayList<Taxi> taxis;
    
    /** Creëert een spoordeel met een identificatie nummer en een spoorlengte.
     * @param id Alphanumerieke identificatienummer
     * @param l Lengte van het spoordeel
     */
    public Spoor(String id, long l) {
    	ID = id;
    	lengte = l;
    	type = SpoorType.SPOOR;
    	taxis = new ArrayList<Taxi>();
    }
    
    /** Creëert een spoordeel met een identificatie nummer en een spoorlengte.
     * @param id Alphanumerieke identificatienummer
     * @param l Lengte van het spoordeel
     * @param voor Het spoordeel dat voor dit spoordeel gekoppeld is.
     * @param na Het spoordeel dat na dit spoordeel volgt
     */
    public Spoor(String id, long l, Spoor voor, Spoor na) {
    	this(id, l);
    	setVoorSpoor(voor);
    	setNaSpoor(na);
    }

    /** Geeft de lengte van het spoordeel terug.
     * @return Lengte van het spoordeel
     */
    public long getLengte() {
        return lengte;
    }

    /** Stelt de lengte van het spoordeel in.
     * @param lengte Nieuwe lengte van het spoordeel
     */
    public void setLengte(long lengte) {
        this.lengte = lengte;
    }

    /** Geeft het SpoorType van dit spoordeel terug. Verschillende soorten spoordelen hebben variërende methoden.
     * @return Type van het spoor
     */
    public SpoorType getType() {
        return type;
    }

    /** Stelt het soort spoor van dit spoordeel in.
     * @param type Type van het spoor
     */
    public void setType(SpoorType type) {
        this.type = type;
    }

    /** Geeft het volgende spoordeel terug waaraan dit spoordeel is verbonden.
     * @return Volgende spoordeel
     */
    public Spoor getNaSpoor() {
        return naSpoor;
    }
    
    /** geeft het volgende spoordeel terug maar dit kan bij een <code>Wisselspoor</code> variëren omdat het geschakeld kan worden.
     * @return Volgende spoordeel
     */
    public Spoor volgende() {
    	return naSpoor;
    }
    
    /** Geeft het vorige spoordeel terug maar dit kan bij een <code>Wisselspoor</code> variëren omdat het geschakeld kan worden.
     * @return Vorige spoordeel
     */
    public Spoor vorige() {
    	return voorSpoor;
    }

    /** Geeft de taxi met de opgegeven index terug.
     * @param index positie in de lijst met taxi's
     * @return De taxi met de opgegeven index in de lijst.
     */
    public Taxi getTaxi(int index) {
        return taxis.get(index);
    }
    
	/** Geeft aan of de opgegeven taxi zich op dit spoordeel bevindt.
	 * @param taxi De taxi
	 * @return Is waar als de taxi zich op dit spoordeel bevindt en anders onwaar
	 */
	public boolean bevatTaxi(Taxi taxi) {
    	return taxis.contains(taxi);
    }

	/** Geeft aan hoeveel taxi's zich op dit spoordeel bevinden.
	 * @return Het aantal taxi's
	 */
	public int aantalTaxis() {
    	return taxis.size();
    }

    /** Voegt de opgegeven taxi aan dit spoordeel toe.
     * @param taxi De toe te voegen taxi.
     */
    public void voegTaxi(Taxi taxi) {
        this.taxis.add(taxi);
    }
    
    /** Verwijdert de opgegeven taxi uit dit spoordeel.
     * @param taxi De te verwijderen taxi
     */
    public void verwijderTaxi(Taxi taxi) {
    	this.taxis.remove(taxi);
    }

	/** Geeft aan of taxi(s) zich wel of niet bevinden op dit spoordeel.
	 * @return Is waar als taxi(s) zich op het spoordeel bevinden en anders onwaar.
	 */
	public boolean bevatTaxis() {
    	return !taxis.isEmpty();
    }

	public SpoorIterator iterator() {
		return new SpoorIterator(this);
	}

	/** Geeft de taxi's op dit spoordeel terug.
	 * @return Lijst met taxi(s)
	 */
	public ArrayList<Taxi> getTaxis() {
		return taxis;
	}

	/** Geeft het alphanumerieke identificatienummer van dit spoordeel terug
	 * @return Identificatienummer
	 */
	public String getID() {
		return ID;
	}

	/** Stelt het alphanumerieke identificatienummer voor dit spoordeel in.
	 * @param ID Identificatienummer
	 */
	public void setID(String ID) {
		this.ID = ID;
	}

	/** Geeft het vorige spoordeel terug waaraan dit spoordeel verbonden is.
	 * @return Vorige spoordeel
	 */
	public Spoor getVoorSpoor() {
		return voorSpoor;
	}

	/** Stelt het vorige spoordeel in waaraan dit spoordeel is verbonden.
	 * @param spoor Vorige spoordeel
	 */
	public void setVoorSpoor(Spoor spoor) {
		this.voorSpoor = spoor;
	}

	/** Stelt het volgende spoordeel in waaraan dit spoordeel is verbonden.
	 * @param spoor Volgende spoordeel
	 */
	public void setNaSpoor(Spoor spoor) {
		spoor.setVoorSpoor(this);
		this.naSpoor = spoor;
	}
}
