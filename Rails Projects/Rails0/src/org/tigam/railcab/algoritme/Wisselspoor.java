package org.tigam.railcab.algoritme;
/**
 * @author Mustapha Bouzaidi
 *
 */
public class Wisselspoor extends Spoor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2320308939392597123L;
	/**
	 * Geeft aan dat het <code>Wisselspoor</code> is gekoppeld aan het hoofdspoor.
	 */
	public static final boolean STAND_HOOFD_SPOOR = false;
	/**
	 * Geeft aan dat het <code>Wisselspoor</code> is geschakeld en gekoppeld aan het zijspoor.
	 */
	public static final boolean STAND_ZIJ_SPOOR = true;
	/**
	 * Geeft aan dat het <code>Wisselspoor</code> voor het rijden naar de zijbaan gebruikt wordt.
	 */
	public static final boolean RICHTING_VOOR_ZIJSPOOR = false;
	/**
	 * Geeft aan dat het <code>Wisselspoor</code> voor het rijden uit de zijbaan gebruikt wordt.
	 */
	public static final boolean RICHTING_NA_ZIJSPOOR = true;
	
    private boolean stand;
    private boolean richting;
    private Spoor zijSpoor;

    /** Creëert een <code>Wisselspoor</code> met een ID, lengte en een richting.
     * @param ID Alphanumerieke Identificatienummer
     * @param lengte Lengte van het wisselspoor
     * @param richting Richting voor of na de zijbaan.
     */
    public Wisselspoor(String ID, long lengte, boolean richting) {
    	super(ID, lengte);
    	stand = STAND_HOOFD_SPOOR;
    	this.richting = richting;
    	type = SpoorType.WISSEL_SPOOR;
    }
    
    /** Creëert een <code>Wisselspoor</code> met een ID, lengte, richting en een voor- na- en zijspoor.
     * @param ID Alphanumerieke Identificatienummer
     * @param lengte Lengte van het wisselspoor
     * @param voor Vorige spoordeel
     * @param na Volgende spoordeel
     * @param zij Zijspoordeel
     * @param richting Richting voor of na de zijbaan.
     */
    public Wisselspoor(String ID, long lengte, Spoor voor, Spoor na, Spoor zij, boolean richting) {
    	super(ID, lengte, voor, na);
    	this.richting = richting;
    	setZijSpoor(zij);
    	stand = STAND_HOOFD_SPOOR;
    	type = SpoorType.WISSEL_SPOOR;
    }

    /** Stelt in op welke stand dit wisselspoor moet staan.
     * @param stand Stand van het wisselspoor
     */
    public void setStand(boolean stand) {
        this.stand = stand;
    }
    
    /**
     * Schakelt dit wisselspoor in of schakelt het terug als het al is geschakelt.
     */
    public void schakel() {
    	stand = !stand;
    }
    
    /** Geeft aan of het wisselspoor is geschakeld.
     * @return Is waar als het wisselspoor naar het zijspoor wijst en onwaar als het naar het hoofdspoor wijst.
     */
    public boolean isGeschakeld() {
    	return stand;
    }

    /** Geeft het zijspoor terug waarnaar dit wisselspoor verwijst.
     * @return Zijspoor
     */
    public Spoor getZijSpoor() {
        return zijSpoor;
    }

    /** Stelt het zijspoor in waarnaar dit wisselspoor moet verwijzen.
     * @param spoor Zijspoor
     */
    public void setZijSpoor(Spoor spoor) {
    	if (richting == RICHTING_VOOR_ZIJSPOOR) spoor.setVoorSpoor(this);
    	else spoor.setNaSpoor(this);
        this.zijSpoor = spoor;
    }
    
    public Spoor volgende() {
    	if (richting == RICHTING_VOOR_ZIJSPOOR)
    		if (stand) return zijSpoor;
    		else return naSpoor;
    	else return naSpoor;
    }
    
    public Spoor vorige() {
    	if (richting == RICHTING_NA_ZIJSPOOR)
    		if (stand) return zijSpoor;
    		else return voorSpoor;
    	else return voorSpoor;
    }

	/** Geeft aan of dit <code>Wisselspoor</code> voor het rijden naar- of voor het rijden uit- de zijbaan bedoeld is.
	 * @return Is waar als dit <code>Wisselspoor</code> voor het rijden uit de zijbaan gebruikt wordt en onwaar als het voor het inrijden in de zijbaan gebruikt wordt.
	 */
	public boolean getRichting() {
		return richting;
	}
}
