package org.tigam.railcab.algoritme;

import java.io.Serializable;
import java.util.Iterator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author Mustapha Bouzaidi
 *
 */
public class ReizigerWachtLijst implements Comparable<ReizigerWachtLijst>, Iterable<Reiziger>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4453408500656552185L;
	private PriorityBlockingQueue<Reiziger> reizigers;
	private long wachtTijden;

	/**
	 * Creëert een wachtlijst voor reizigers met hetzelfde vertrekpunt en bestemming.
	 */
	public ReizigerWachtLijst() {
		reizigers = new PriorityBlockingQueue<Reiziger>();
		wachtTijden = 0;
	}

	/** Voegt een reiziger toe aan de wachtlijst.
	 * @param r Reiziger om toe te voegen
	 * @return Is waar als de reiziger is teogevoegd en anders onwaar.
	 */
	public boolean add(Reiziger r) {
		return reizigers.add(r);
	}

	/** Geeft de grootte van de wachtlijst terug.
	 * @return grootte van de wachtlijst
	 */
	public int size() {
		return reizigers.size();
	}

	public Iterator<Reiziger> iterator() {
		return reizigers.iterator();
	}

	/** Neemt een kijkje van de laatste reiziger in de wachtlijst.
	 * @return Laatste reiziger in de wachtlijst
	 */
	public Reiziger peek() {
		return reizigers.peek();
	}

	/** Haalt de laatste reiziger uit de wachtlijst.
	 * @return Laatste reiziger uit de wachtlijst
	 */
	public Reiziger poll() {
		return reizigers.poll();
	}

	/** Geeft aan of de wachtlijst leeg is.
	 * @return Is waar als de wachtlijst leeg is en anders onwaar.
	 */
	public boolean isEmpty() {
		return reizigers.isEmpty();
	}
	
	/**
	 * Werkt de wachttijden van alle reizigers bij in de wachtlijst. 
	 * Bovendien wordt de cumulatieve wachttijd in de Reizigerwachtlijst ook bijgewerkt.
	 */
	public void updateWachtTijden() {
		wachtTijden = 0;
		for (Reiziger r : reizigers) {
			r.updateWachtTijd();
			wachtTijden += r.getWachtTijd();
		}
	}
	
	/** Geeft de cumulatieve wachttijd van de reizigers in de wachtlijst terug.
	 * @return Cumulatieve wachttijd
	 */
	public long getWachtTijden() {
		return wachtTijden;
	}

	public int compareTo(ReizigerWachtLijst rLijst) {
		if (isEmpty())
			if (rLijst.isEmpty()) return 0;
			else return -1;
		else if (rLijst.isEmpty()) return 1;
		
		if (getWachtTijden() == rLijst.getWachtTijden()) return 0;
		if (getWachtTijden() > rLijst.getWachtTijden()) return 1;
		if (getWachtTijden() < rLijst.getWachtTijden()) return -1;
		else return 0;
	}

}