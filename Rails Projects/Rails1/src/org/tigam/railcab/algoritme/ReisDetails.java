package org.tigam.railcab.algoritme;

import java.io.Serializable;

/**
 * @author Mustapha Bouzaidi
 *
 */
public class ReisDetails implements Comparable<ReisDetails>, Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3414913389095080426L;
	private Station vertrekpunt, bestemming;

    /**
     * Creëert een reisdetails object zonder een vertrekpunt en bestemming.
     */
    public ReisDetails() {
    }

    /** Creëert een reisdetails object met een vertrekpunt en bestemming.
     * @param vertrekpunt Station waar de reizigers worden opgehaald
     * @param bestemming Station waar de reizigers worden afgezet
     */
    public ReisDetails(Station vertrekpunt, Station bestemming) {
    	this.vertrekpunt = vertrekpunt;
    	this.bestemming = bestemming;
	}

	/** Geeft het vertrekpunt terug.
	 * @return Vertrekpunt station
	 */
	public Station getVertrekpunt() {
        return vertrekpunt;
    }

    /** Stelt het vertrekpunt in.
     * @param vertrekpunt Station
     */
    public void setVertrekpunt(Station vertrekpunt) {
        this.vertrekpunt = vertrekpunt;
    }

    /** Geeft de bestemming terug.
     * @return Bestemming station
     */
    public Station getBestemming() {
        return bestemming;
    }

    /** Stelt de bestemming in.
     * @param bestemming Station
     */
    public void setBestemming(Station bestemming) {
        this.bestemming = bestemming;
    }
    
    public boolean equals(Object o) {
    	ReisDetails r = (ReisDetails) o;
    	if (vertrekpunt.equals(r.vertrekpunt) && bestemming.equals(r.bestemming)) return true;
    	else return false;
    }
    
	public int compareTo(ReisDetails r) {
		if (this.equals(r)) return 0;
		else if (vertrekpunt.getID() > r.getVertrekpunt().getID()) return 1;
		else if (vertrekpunt.getID() < r.getVertrekpunt().getID()) return -1;
		else return -1;
	}
}
