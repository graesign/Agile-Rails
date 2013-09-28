package org.tigam.railcab.algoritme;

import java.io.Serializable;

/** De <code>TaxiReden</code> enumeratie wordt door de <code>Taxi</code> klasse gebruikt om de reden van een handeling (zoals het stoppen of rijden van een taxi) aan te geven. Bovendien wordt <code>TaxiReden</code> gebruikt voor het identificeren van een wijziging of handeling aan Observers.
 * @author Mustapha Bouzaidi
 *
 */
public enum TaxiReden implements Serializable {
	/**
	 * De <code>Taxi</code> stopt vanwege mogelijke aanrijding tussen taxi's.
	 */
	MOGELIJKE_AANRIJDING, 
	/**
	 * De <code>Taxi</code> rijdt verder na een mogelijke aanrijding
	 */
	HERVAT_NA_MOGELIJKE_AANRIJDING, 
	/**
	 * De <code>Taxi</code> rijdt het <code>Station</code> uit.
	 */
	UIT_STATION, 
	/**
	 * De <code>Taxi</code> stopt op het <code>Station</code>.
	 */
	OP_STATION, 
	/**
	 * De <code>Simulatie</code> is gepauzeerd.
	 */
	PAUZE, 
	/**
	 * De <code>Simulatie</code> is hervat.
	 */
	HERVAT_NA_PAUZE;
}
