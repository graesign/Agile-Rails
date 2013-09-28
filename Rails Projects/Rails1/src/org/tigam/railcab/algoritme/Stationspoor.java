package org.tigam.railcab.algoritme;

/**
 * @author Mustapha Bouzaidi
 *
 */
public class Stationspoor extends Spoor {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4274766648729085085L;
	private Station station;

    /** Creëert een <code>Stationspoor</code> met een ID, spoorlengte en het <code>Station</code> dat erop staat.
     * @param ID Alphanumerieke identificatienummer
     * @param lengte Lengte van dit spoordeel
     * @param station <code>Station</code> dat op dit <code>Stationspoor</code> staat.
     */
    public Stationspoor(String ID, long lengte, Station station) {
    	super(ID, lengte);
    	this.station = station;
    	this.station.setSpoor(this);
    	type = SpoorType.STATION_SPOOR;
    }
    
    /** Creëert een <code>Stationspoor</code> met een ID, spoorlengte en het <code>Station</code> dat erop staat.
     * Bovendien wordt het vorige en volgende spoor van dit spoordeel gekoppeld.
     * @param ID Alphanumerieke identificatienummer
     * @param lengte Lengte van dit spoordeel
     * @param voor Vorige spoordeel
     * @param na Volgende spoordeel
     * @param station <code>Station</code> dat op dit <code>Stationspoor</code> staat.
     */
    public Stationspoor(String ID, long lengte, Spoor voor, Spoor na, Station station) {
    	super(ID, lengte, voor, na);
    	this.station = station;
    	this.station.setSpoor(this);
    	type = SpoorType.STATION_SPOOR;
    }

    /** Geeft het <code>Station</code> terug dat op dit <code>Stationspoor</code> staat.
     * @return Het <code>Station</code> op dit <code>Stationspoor</code>.
     */
    public Station getStation() {
        return station;
    }

    /** Stelt het nieuwe <code>Station</code> in dat op dit <code>Stationspoor</code> moet staan.
     * @param station Nieuwe station
     */
    public void setStation(Station station) {
        this.station = station;
    }
}
