package org.tigam.railcab.algoritme;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/** De reismanager bevat de reizigers en reizen in de simulatie. De reismanager maakt (plant) reizen aan voor beschikbare reizigers en voert de reizen vervolgens uit. Reizigers kunnen via de <code>ReisManager</code> aan de simulatie toegevoegd worden.
 * @author Mustapha Bouzaidi
 *
 */
public class ReisManager implements Serializable, Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4919934401284854375L;
	private BaanManager baanManager;
	/**
	 * Maximaal aantal reizigers in een taxi.
	 */
	public static final int MAX_REIZIGERS_IN_TAXI = 5;
	/**
	 * Slaaptijd van de thread na het plannen van een reis en/of bijwerken van alle reizen.
	 */
	public static final int THREAD_DELAY = 200;

	public static final int NIET_INGESTELD = 0;
	public static final int START = 1;
	public static final int WACHT_OP_PAUZE = 2;
	public static final int PAUZE = 4;
	public static final int STOP = 8;
	public static final int VERSNELD = 16;

	private static int versnelFactor;
	private double wachtTijden;
	private int wachtTijdenAantal;
	private int reizigerTeller;
	private int status = NIET_INGESTELD;

	private ArrayList<Reis> reizen;
	private LinkedList<ReizigerWachtLijst> reizigerSet;

	/** Creëert de reismanager met de opgegeven baanmanager.
	 * @param baanManager baanmanager dat door de reismanager wordt gebruikt
	 */
	public ReisManager(BaanManager baanManager) {
		this.baanManager = baanManager;
		reizigerSet = new LinkedList<ReizigerWachtLijst>();
		reizen = new ArrayList<Reis>();
		status = NIET_INGESTELD;

		versnelFactor = 1;
		wachtTijden = 0;
		wachtTijdenAantal = 0;
		reizigerTeller = 0;
	}

	/** Geeft de status van de reismanager terug.
	 * @return Status van de reismanager
	 */
	public synchronized int getStatus() {
		return status;
	}

	/** Stelt de status van de reismanager in.
	 * @param status Status voor de reismanager
	 */
	public synchronized void setStatus(int status) {
		this.status = status;
	}

	/** Voert de reisplannings procedure uit. Vergelijkt maximaal MAX_REIZIGERS_IN_TAXI reizigers met de hoogste wachttijd en hetzelfde vertrekpunt/bestemmings- combinatie met de gemiddelde wachttijd. 
	 * Vervolgens wordt het wachttijdpercentage van de eerdere vergelijking vergeleken met hoeveel taxi's er obezet zijn. Als het wachttijdpercentage hoger of gelijk is aan de taxibezetting dan wordt een reis gepland. 
	 * Een reis object wordt aangemaakt en vervolgens wordt de taxi genomen die het dichtst bij het vertrekpunt staat. Bovendien worden de reizigers aan de reis object toegevoegd.
	 * 
	 */
	public void planReis() {
		if (!reizigerSet.isEmpty()) {
			Collections.sort(reizigerSet);
			ReizigerWachtLijst rWachtLijst = reizigerSet.getLast();
			double cWachtTijden = 0;
			int aantalReizigers = 0;
			ArrayList<Taxi> beschikbareTaxis = baanManager.getBeschikbareTaxis();
			if (!beschikbareTaxis.isEmpty() && !rWachtLijst.isEmpty()) {
				for (Reiziger r : rWachtLijst) {
					cWachtTijden += r.getWachtTijd();
					if (++aantalReizigers == MAX_REIZIGERS_IN_TAXI) {
						break;
					}
				}
				cWachtTijden /= aantalReizigers;
				double gWachtTijden = wachtTijden / wachtTijdenAantal;
				float aantalBeschikbareTaxis = beschikbareTaxis.size();
				float aantalTaxis = baanManager.getAantalTaxis();
				int wachtTijdPercentage = 0;
				if (wachtTijdenAantal != 0)
					wachtTijdPercentage = new Double(100 * (cWachtTijden / gWachtTijden)).intValue();
				else wachtTijdPercentage = 100;
				int taxiBezetting = new Float(100 * (1 - (aantalBeschikbareTaxis / aantalTaxis))).intValue();
				if (wachtTijdPercentage >= taxiBezetting) {
					Reis reis = new Reis();
					reis.setReisDetails(rWachtLijst.peek().getReisDetails());
					for (int i = 0; i < aantalReizigers; i++) {
						wachtTijden += rWachtLijst.peek().getWachtTijd();
						wachtTijdenAantal++;
						reis.voegReiziger(rWachtLijst.poll());
					}

					if (rWachtLijst.isEmpty()) reizigerSet.remove(rWachtLijst);

					Taxi taxi = null;
					Station vertrekpunt = reis.getReisDetails().getVertrekpunt();
					for (Taxi t : beschikbareTaxis) {
						if (taxi == null)
							taxi = t;
						else if (baanManager.afstand(taxi, vertrekpunt) > baanManager.afstand(t, vertrekpunt))
							taxi = t;
					}
					reis.setTaxi(taxi);
					taxi.setReis(reis);
					reizen.add(reis);
					reis.start();
				}
			}
		}
	}

	/**
	 * Werkt de wachttijd bij van alle reizigers zonder een reis.
	 */
	public void updateOngeplandeReizigers() {
		for (ReizigerWachtLijst rWachtLijst : reizigerSet) {
			rWachtLijst.updateWachtTijden();
		}
	}

	/** Pauzeert de reismanager door de thread in een wachtende loop te plaatsen en vervolgens de tijden van reizigers en reizen bij te werken.
	 * @return Is waar als de reismanager is gepauzeerd en anders onwaar
	 */
	public boolean pauzeer() {
		if (getStatus() != PAUZE || getStatus() != WACHT_OP_PAUZE) {
			status = WACHT_OP_PAUZE;
			while (getStatus() != ReisManager.PAUZE) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			updateOngeplandeReizigers();
			for (Reis reis : reizen) reis.pauzeer();
			return true;
		}
		return false;
	}

	/** Hervat de reismanager als het is gepauzeerd. De starttijden van reizigers en reizen worden gereset en vervolgens wordt de status aangepast. 
	 * De thread van de reismanager wordt genotificeerd zodat het uit de pauze stand gaat.
	 * @return Is waar als de reismanager is hervat en anders onwaar.
	 */
	public boolean hervat() {
		if (status == PAUZE) {
			for (ReizigerWachtLijst rWachtLijst : reizigerSet) {
				for (Reiziger r : rWachtLijst) {
					r.resetStartTijd();
				}
			}
			for (Reis reis : reizen) reis.hervat();
			status = START;
			return true;
		}
		return false;
	}

	/** Geeft de baanmanager van de reismanager terug.
	 * @return BaanManager
	 */
	public BaanManager getBaanManager() {
		return baanManager;
	}

	/** Stelt de baanmanager van de reismanager in.
	 * @param baanManager
	 */
	public void setBaanManager(BaanManager baanManager) {
		this.baanManager = baanManager;
	}

	/** Geeft de reizen in de reismanager terug.
	 * @return reis objecten
	 */
	public ArrayList<Reis> getReizen() {
		return reizen;
	}

	/** Stelt de reizen van de reismanager in.
	 * @param reizen reis objecten
	 */
	public void setReizen(ArrayList<Reis> reizen) {
		this.reizen = reizen;
	}

	private ReizigerWachtLijst vindReizigers(ReisDetails rDetails) throws NoSuchElementException {
		for (ReizigerWachtLijst rWachtLijst : reizigerSet) {
			if (rWachtLijst.peek().getReisDetails().equals(rDetails)) return rWachtLijst;
		}
		throw new NoSuchElementException("Geen reizigers gevonden met de opgegeven vertrek- en bestemmingspunt.");
	}

	/** Voegt een reiziger met het vertrekpunt en bestemming aan de reismanager toe. Gecontroleerd word of een reiziger aan een bestaande reis kan worden toegevoegd, anders wordt de reiziger in de lijst met ongeplande reizigers gezet.
	 * @param vertrekpunt Station
	 * @param bestemming Station
	 */
	public synchronized void addReiziger(Station vertrekpunt, Station bestemming) {
		reizigerTeller++;
		Reiziger r = new Reiziger(reizigerTeller, "Reiziger " + reizigerTeller, ReizigerStatus.WACHT);
		if (status != PAUZE) r.setStartTijd(System.currentTimeMillis());
		ReisDetails rDetails = new ReisDetails(vertrekpunt, bestemming);
		r.setReisDetails(rDetails);
		if (!voegAanReis(r)) {
			ReizigerWachtLijst rWachtLijst;
			try {
				rWachtLijst = vindReizigers(rDetails);
			} catch (NoSuchElementException e) {
				rWachtLijst = new ReizigerWachtLijst();
				reizigerSet.add(rWachtLijst);
			}
			vertrekpunt.voegReiziger(r);
			rWachtLijst.add(r);
		}
	}

	private boolean voegAanReis(Reiziger r) {
		for (Reis reis : reizen) {
			if (reis.getReisDetails().equals(r.getReisDetails())) {
				if (reis.naarVertrekpunt() && reis.getAantalReizigers() < MAX_REIZIGERS_IN_TAXI) {
					reis.voegReiziger(r);
					return true;
				}
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public void run() {
		while (status != STOP) {
			synchronized (this) {
				while (status == WACHT_OP_PAUZE || status == PAUZE) {
					try {
						status = PAUZE;
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			updateOngeplandeReizigers();
			planReis();

			for (Reis reis : reizen) {
				Taxi taxi = reis.getTaxi();

				reis.updateReizigers();
				reis.updateReisTijd();

				if (baanManager.aanrijding(taxi)) {
					if (taxi.getStatus() == TaxiStatus.RIJD) {
						taxi.stop(TaxiReden.MOGELIJKE_AANRIJDING);
						System.out.println("Mogelijke aanrijding gedecteerd, " + taxi.getNaam() + " is gestopt!");
					}
				}

				else {

					if (taxi.getStatus() == TaxiStatus.WACHT_OP_SPOOR) {
						taxi.rij(TaxiReden.HERVAT_NA_MOGELIJKE_AANRIJDING);
						System.out.println(taxi.getNaam() + " hervat reis na mogelijke aanrijding.");
					}

					else if (taxi.opStation()) {
						Station station = taxi.getStation();
						taxi.updateWachtTijdOpStation();
						if (taxi.isOnbezet()) {
							if (station == reis.getReisDetails().getVertrekpunt()) {
								reis.setStatus(EnumSet.of(ReisStatus.NAAR_VERTREKPUNT));
								taxi.invoegen(station);
							} else {
								reis.setStatus(EnumSet.of(ReisStatus.NAAR_VERTREKPUNT));
								taxi.setWachtTijdOpStation(baanManager.getWachtTijdStation());
								taxi.setStatus(TaxiStatus.WACHT_OP_STATION);
							}
						}

						if (station == reis.getReisDetails().getVertrekpunt()) {
							if (taxi.wachtOpReizigers()) {
								for (Reiziger r : reis.getReizigers())
									r.stapIn(taxi);
								reis.setStatus(EnumSet.of(ReisStatus.NAAR_VERTREKPUNT, ReisStatus.REIZIGERS_OPGEHAALD));
							} else if (!taxi.wachtOpSluitingsTijd())
								reis.setStatus(EnumSet.of(ReisStatus.NAAR_BESTEMMING, ReisStatus.REIZIGERS_OPGEHAALD));
						}

						else if (station == reis.getReisDetails().getBestemming() && reis.naarBestemming()) {
							reis.voltooien();
							Simulatie.getInstantie().voegReis(reis);
						}

						if (taxi.wachtOpUitrijden() && station.voorrang(taxi)) {
							if (baanManager.wisselaar(taxi) && baanManager.getGeschakeldeWisselspoor(taxi) == baanManager.getVolgendeWisselspoor(station.getSpoor()) && baanManager.getGeschakeldeWisselspoor(taxi).isGeschakeld()) {
								taxi.rij(TaxiReden.UIT_STATION);
								reis.resetStartUpdateTijd();
							}
						}
					}

					else if (taxi.opSpoor()) {
						if (!baanManager.isGeschakeldVoor(taxi)) {
							if (taxi.opHoofdSpoor() && (taxi.getSpoor().getLengte() - taxi.getSpoorPositie() <= BaanManager.MIN_AFSTAND_TOT_WISSEL)) {
								Station station = baanManager.getStation(baanManager.getVolgendeWisselspoor(taxi.getSpoor()));
								if (reis.naarVertrekpunt() && reis.getReisDetails().getVertrekpunt() == station || reis.naarBestemming() && reis.getReisDetails().getBestemming() == station)
									if (!baanManager.bevatTaxis(taxi)) baanManager.wisselaar(taxi);
							}
						}
						else baanManager.wisselaar(taxi);
					}

					if (taxi.getStatus() == TaxiStatus.RIJD)
						taxi.updatePositie();
				}
			}
			for (int i = 0; i < reizen.size(); i++)
				if (reizen.get(i).reisVoltooid()) reizen.remove(i);

			try {
				Thread.sleep(THREAD_DELAY);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/** Geeft de versnellingsfactor terug.
	 * @return versnellingsfactor in aantal maal versnelling
	 */
	public static synchronized int getVersnelFactor() {
		return versnelFactor;
	}

	/** Stelt de versnellingsfactor in.
	 * @param versnelFactor aantal maal versnelling
	 */
	public static synchronized void setVersnelFactor(int versnelFactor) {
		ReisManager.versnelFactor = versnelFactor;
	}
}