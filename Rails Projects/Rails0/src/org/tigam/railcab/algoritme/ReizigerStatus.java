package org.tigam.railcab.algoritme;

import java.io.Serializable;

/**
 * @author Mustapha Bouzaidi
 *
 */
public enum ReizigerStatus implements Serializable {
	/**
	 * Reiziger wacht op een taxi.
	 */
	WACHT, 
	/**
	 * Reiziger zit of reist in een taxi.
	 */
	REIST, 
	/**
	 * Reiziger is aangekomen op de bestemming.
	 */
	AANGEKOMEN;
}
