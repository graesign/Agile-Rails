package org.tigam.railcab.algoritme;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;

/** De <code>Reis</code> klasse bevat reizigers en een taxi waarmee de reis wordt uitgevoerd. De <code>Reis</code> geeft aan waar een taxi moet gaan om reizigers op te halen en af te zetten. Bovendien houdt de <code>Reis</code> klasse de reistijd bij. 
 * @author Mustapha Bouzaidi
 *
 */
public class Reis implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1929403460392195371L;

	private EnumSet<ReisStatus> status;

	@SuppressWarnings("unused")
	private long startUpdateTijd = 0;
	@SuppressWarnings("unused")
	private long wachtTijd = 0, reisTijdVertrekpunt = 0, reisTijdBestemming = 0;
	private ArrayList<Reiziger> reizigers;
	private Taxi taxi;
	private ReisDetails reisDetails;

	/**
	 * Cre�ert een reis zonder een taxi, reizigers en reisdetails (vertrekpunt en bestemming).
	 */
	public Reis() {
		this.reizigers = new ArrayList<Reiziger>();
		status = EnumSet.noneOf(ReisStatus.class);
	}

	/** Cre�rt een reis met een taxi en reisdetails (vertrekpunt en bestemming).
	 * @param taxi De taxi die de reis uitvoert
	 * @param reisDetails het vertrekpunt en bestemming van de reis
	 */
	public Reis(Taxi taxi, ReisDetails reisDetails) {
		this.taxi = taxi;
		this.reisDetails = reisDetails;
		this.reizigers = new ArrayList<Reiziger>();
		status = EnumSet.noneOf(ReisStatus.class);
	}

	/** Haalt een reiziger aan de hand van een index op.
	 * @param index positie in de lijst met reizigers
	 * @return reiziger met de opgegeven index
	 */
	public Reiziger getReiziger(int index) {
		return reizigers.get(index);
	}

	/** Voegt een reiziger toe aan de reis
	 * @param r reiziger om toe te voegen
	 */
	public void voegReiziger(Reiziger r) {
		reizigers.add(r);
	}

	/** Geeft de taxi terug die voor de reis wordt gebruikt.
	 * @return Taxi
	 */
	public Taxi getTaxi() {
		return taxi;
	}

	/** Stelt de taxi in die door de reis object wordt gebruikt.
	 * @param t Taxi
	 */
	public void setTaxi(Taxi t) {
		this.taxi = t;
	}

	/** Geeft de status van de taxi terug.
	 * @return status (combinaties mogelijk) van de reis
	 */
	public EnumSet<ReisStatus> getStatus() {
		return status;
	}

	/** Stelt de status van de reis in.
	 * @param status nieuwe status (combinaties mogelijk) van de reis object
	 */
	public void setStatus(EnumSet<ReisStatus> status) {
		this.status = status;
	}

	/** Geeft aan of de reizigers voor de reis zijn opgehaald.
	 * @return Is waar als de reizigers zijn opgehaald en anders onwaar
	 */
	public boolean reizigersOpgehaald() {
		return status.contains(ReisStatus.REIZIGERS_OPGEHAALD);
	}

	/** Geeft aan of de taxi voor de reis op weg is naar het vertrekpunt.
	 * @return Is waar als de taxi op weg is naar het vertrekpunt en anders onwaar
	 */
	public boolean naarVertrekpunt() {
		return status.contains(ReisStatus.NAAR_VERTREKPUNT);
	}

	/** Geeft aan of de taxi voor de reis op weg is naar de bestemming.
	 * @return Is waar als de taxi op weg is naar de bestemming en anders onwaar
	 */
	public boolean naarBestemming() {
		return status.contains(ReisStatus.NAAR_BESTEMMING);
	}

	/** Geeft aan of de reis is voltooid.
	 * @return Is waar als de reis is voltooid en anders onwaar
	 */
	public boolean reisVoltooid() {
		return status.contains(ReisStatus.VOLTOOID);
	}

	/** Geeft de totale reistijd van de reis terug.
	 * @return Reistijd in milliseconden
	 */
	public long getReisTijd() {
		return reisTijdVertrekpunt + reisTijdBestemming + wachtTijd;
	}

	/** Geeft de reistijd naar het vertrekpunt terug.
	 * @return Reistijd in milliseconden
	 */
	public long getReisTijdVertrekpunt() {
		return reisTijdVertrekpunt;
	}

	/** Stelt de reistijd naar het vertrekpunt in.
	 * @param tijd Reistijd in milliseconden
	 */
	public void setReisTijdVertrekpunt(long tijd) {
		this.reisTijdVertrekpunt = tijd;
	}

	/** Geeft de reistijd naar de bestemming terug.
	 * @return Reistijd in milliseconden
	 */
	public long getReistijdBestemming() {
		return reisTijdBestemming;
	}

	/** Stelt de reistijd naar de bestemming in.
	 * @param tijd Reistijd in milliseconden
	 */
	public void setReisTijdBestemming(long tijd) {
		this.reisTijdBestemming = tijd;
	}

	/** Geeft de reisdetails van de reis terug.
	 * @return reisdetails
	 */
	public ReisDetails getReisDetails() {
		return reisDetails;
	}

	/** Stelt de reisdetails (vertrekpunt en bestemming) van de reis in.
	 * @param rDetails ReisDetails
	 */
	public void setReisDetails(ReisDetails rDetails) {
		this.reisDetails = rDetails;
	}

	/**
	 * Print een bericht naar de console om aan te geven dat de reis is gestart.
	 */
	public void start() {
		System.out.println("Reis gestart met " + taxi.getNaam() + " en " + reizigers.size() + " reizigers.");
	}

	/**
	 * Werkt de reistijd bij als de taxi tijdens de reis op weg is naar het vertrekpunt of bestemming.
	 */
	public void updateReisTijd() {
            try {
		if (taxi.opSpoor()) {
			if (naarVertrekpunt())
				reisTijdVertrekpunt += (System.currentTimeMillis() - startUpdateTijd) * ReisManager.getVersnelFactor();
			else if (naarBestemming())
				reisTijdBestemming += (System.currentTimeMillis() - startUpdateTijd) * ReisManager.getVersnelFactor();
			startUpdateTijd = System.currentTimeMillis();
		}
            } catch ( java.lang.NullPointerException e ) {
                System.out.println( "NULLPOINTER IN updateReisTijd: " + taxi.getNaam() );
            }
	}

	/**
	 * Pauzeert de reis door de wachttijden en reistijden van de taxi en reizigers vooraf bij te werken en de taxi te stoppen.
	 */
	public void pauzeer() {
		if (taxi.opStation()) taxi.updateWachtTijdOpStation();
		else if (taxi.opSpoor()) taxi.updatePositie();
		taxi.stop(TaxiReden.PAUZE);
		for (Reiziger r : reizigers) r.updateWachtTijd();
		updateReisTijd();
	}

	/**
	 * Hervat de reis door de starttijd voor het bijwerken van de reistijd en wachttijd op het station te resetten en de taxi weer te laten rijden als het op de spoor wacht.
	 */
	public void hervat() {
		resetStartUpdateTijd();
		if (taxi.opStation()) taxi.resetStartTijdOpStation();
		else if (taxi.opSpoor() && taxi.getStatus() == TaxiStatus.GEPAUZEERD_OP_SPOOR) taxi.rij(TaxiReden.HERVAT_NA_PAUZE);
	}

	/**
	 * Starttijd voor het bijwerken van de reistijd wordt ingesteld op de huidige tijd van uitvoeren (reset).
	 */
	public void resetStartUpdateTijd() {
		startUpdateTijd = System.currentTimeMillis();
	}

	/** Geeft het aantal reizigers van de reis terug.
	 * @return Het aantal reizigers
	 */
	public int getAantalReizigers() {
		return reizigers.size();
	}

	/** Geeft de reizigers van de reis terug.
	 * @return De reizigers
	 */
	public ArrayList<Reiziger> getReizigers() {
		return reizigers;
	}

	/** 
	 * Voltooid de reis door de taxi uit de reis te halen en onbezet maken. Bovendien wordt de status van de reis op voltooid gezet.
	 */
	public void voltooien() {
		System.out.println("Reis voltooid met " + taxi.getNaam() + "!");
		System.out.println("Reistijd naar vertrekpunt: " + ((double)reisTijdVertrekpunt / 1000) + " seconden.");
		System.out.println("Reistijd naar bestemming: " + ((double)reisTijdBestemming / 1000) + " seconden");
		System.out.println("Totale reisduur: " + ((double)getReisTijd() / 1000) + " seconden");
		System.out.println("Wachttijden van reizigers:");
		for (Reiziger r : reizigers) {
			r.stapUit(taxi);
			System.out.println("Wachttijd: " + ((double)r.getWachtTijd() / 1000) + " seconden");
		}
		taxi.setStatus(TaxiStatus.ONBEZET);
		taxi.setReis(null);
		this.setTaxi(null);
		setStatus(EnumSet.of(ReisStatus.VOLTOOID));
	}

	/**
	 * Werkt de wachttijd van alle reizigers bij.
	 */
	public void updateReizigers() {
		if (reizigers.get(0).getStatus() == ReizigerStatus.WACHT) {
			for (Reiziger r : reizigers) r.updateWachtTijd();
		}
	}

	/** Stelt de wachttijd van de taxi tijdens de reis in.
	 * @param wachtTijd Wachttijd op het station in milliseconden
	 */
	public void setWachtTijd(long wachtTijd) {
		this.wachtTijd = wachtTijd;
	}

	/** Geeft de wachttijd van de taxi tijdens de reis terug.
	 * @return Wachttijd op het station in milliseconden
	 */
	public long getWachtTijd() {
		return wachtTijd;
	}
}
