package org.tigam.railcab.algoritme;

import java.io.Serializable;

/** De <code>SpoorType</code> enumeratie geeft aan wat voor Spoordeel een instantie van de (super)klasse <code>Spoor</code> heeft.
 * @author Mustapha Bouzaidi
 *
 */
public enum SpoorType implements Serializable {
	/**
	 * Geeft aan dat het om een normale <code>Spoor</code> gaat, meestal is dit het hoofdspoor.
	 */
	SPOOR, 
	/**
	 * Geeft aan dat een het om een <code>Wisselspoor</code> gaat.
	 */
	WISSEL_SPOOR, 
	/**
	 * Geeft aan dat het om een <code>Stationspoor</code> gaat.
	 */
	STATION_SPOOR;
}
