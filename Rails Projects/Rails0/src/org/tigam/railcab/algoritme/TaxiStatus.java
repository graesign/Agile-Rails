package org.tigam.railcab.algoritme;

import java.io.Serializable;

/** De status van een <code>Taxi</code>.
 * @author Mustapha Bouzaidi
 *
 */
public enum TaxiStatus implements Serializable {
	/**
	 * De <code>Taxi</code> rijdt op het <code>Spoor</code>.
	 */
	RIJD, 
	/**
	 * De <code>Taxi</code> wacht/is gestopt op het <code>Spoor</code>.
	 */
	WACHT_OP_SPOOR, 
	/**
	 * De <code>Taxi</code> wacht op het <code>Station</code>.
	 */
	WACHT_OP_STATION, 
	/**
	 * De <code>Taxi</code> is onbezet op het <code>Station</code>.
	 */
	ONBEZET, 
	/**
	 * De <code>Taxi</code> is bezet.
	 */
	BEZET, 
	/**
	 * De <code>Taxi</code> staat gepauzeerd op het <code>Spoor</code>.
	 */
	GEPAUZEERD_OP_SPOOR;
}
