package org.tigam.railcab.algoritme;
import java.util.Iterator;

/** Een Iterator voor baanstructuren. Itereert over verschillende spoordelen uit de baanstructuur en maakt hierbij gebruik van de gezamenlijke methode getNaSpoor(). Door het gebruik van getNaSpoor wordt de geschakelde status van wisselsporen genegeerd. De <code>SpoorIterator</code> kan alleen richting vooruit een baanstructuur bewandelen.
 * @author Mustapha
 *
 */
public class SpoorIterator implements Iterator<Spoor> {
	private Spoor sporen;
	
	/** Creëert een <code>SpoorIterator</code> voor de opgegeven baanstructuur.
	 * @param spoor Baanstructuur (ofwel begin spoordeel van de baanstructuur)
	 */
	public SpoorIterator(Spoor spoor) {
		this.sporen = spoor;
	}
	
	public boolean hasNext() {
		if (sporen.getNaSpoor() != null) return true;
		else return false;
	}

	public Spoor next() {
		Spoor spoor = sporen;
		sporen = sporen.getNaSpoor();
		return spoor;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}
