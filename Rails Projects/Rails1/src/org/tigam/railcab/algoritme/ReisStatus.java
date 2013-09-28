package org.tigam.railcab.algoritme;

import java.io.Serializable;

/**
 * @author Mustapha Bouzaidi
 *
 */
public enum ReisStatus implements Serializable {
	/**
	 * De taxi voor de reis is op weg naar het vertrekpunt.
	 */
	NAAR_VERTREKPUNT, 
	/**
	 * De reizigers van de reis zijn opgehaald.
	 */
	REIZIGERS_OPGEHAALD, 
	/**
	 * De taxi voor de reis is op weg naar de bestemming.
	 */
	NAAR_BESTEMMING, 
	/**
	 * De reis is voltooid.
	 */
	VOLTOOID;
}
