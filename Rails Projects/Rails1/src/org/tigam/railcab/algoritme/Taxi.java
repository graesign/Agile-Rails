package org.tigam.railcab.algoritme;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/** De <code>Taxi</code> is het vervoersmiddel in de simulatie waarmee reizigers van punt A naar B worden verplaatst d.m.v. een Reis. De taxi beweegt zich voort op spoordelen en voegt zich in als het een station heeft bereikt. Reizigers kunnen in een <code>Taxi</code> stappen als de <code>Reis</code> van de 2 overeenkomt.
 * @author Mustapha Bouzaidi
 *
 */
public class Taxi extends Observable implements Comparable<Taxi>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2416958873446309484L;

	private int ID;

	private String naam;

	private TaxiStatus status;

	private double absoluteAfstand, spoorAfstand;
	private long startTijdStation, wachtTijdStation;

	private long startUpdateTijdSpoor;

	private Spoor spoor;
	private ArrayList<Reiziger> reizigers;
	private Reis reis;

	private BaanManager baanManager;
	private Station station;

	/** Creëert een <code>Taxi</code> met een ID, naam en het <code>Station</code> waar het zal staan. Bovendien wordt de baanmanager gebruikt voor afstandsberekeningen.
	 * @param baanManager De te gebruiken baanmanager
	 * @param ID Identificatienummer van de taxi
	 * @param naam Naam van de taxi
	 * @param station Station waar de taxi staat.
	 */
	public Taxi(BaanManager baanManager, int ID, String naam, Station station) {
		this.baanManager = baanManager;
		this.ID = ID;
		this.naam = naam;
		this.station = station;
		this.absoluteAfstand = baanManager.afstandTotZijSpoor(station);
		this.status = TaxiStatus.ONBEZET;
		this.spoorAfstand = station.getPositie();
		this.reizigers = new ArrayList<Reiziger>();
	}

	/** Creëert een <code>Taxi</code> met een ID, naam, een observer die op de hoogte gehouden wordt en het <code>Station</code> waar de taxi zal staan. Bovendien wordt de baanmanager gebruikt voor afstandsberekeningen.
	 * @param baanManager De te gebruiken baanmanager
	 * @param ID Identificatienummer van de taxi
	 * @param naam Naam van de taxi
	 * @param station Station waar de taxi staat.
	 * @param observer Wordt genotificeerd van wijzigingen aan de taxi.
	 */
	public Taxi(BaanManager baanManager, int ID, String naam, Station station, Observer observer) {
		this(baanManager, ID, naam, station);
		this.addObserver(observer);
	}

	/** Stelt het <code>Station</code> in waarop deze taxi staat.
	 * @param station Station
	 */
	public void setStation(Station station) {
		this.station = station;
	}

	/** Geeft de identificatienummer van deze taxi terug.
	 * @return Identificatienummer
	 */
	public int getID() {
		return ID;
	}

	/** Stelt een nieuwe identificatienummer voor deze taxi in.
	 * @param ID Nieuwe identificatienummer
	 */
	public void setID(int ID) {
		this.ID = ID;
	}

	/** Geeft de naam terug van deze taxi.
	 * @return Naam van de taxi
	 */
	public String getNaam() {
		return naam;
	}

	/** Stelt een nieuwe naam in voor deze taxi.
	 * @param naam Nieuwe naam
	 */
	public void setNaam(String naam) {
		this.naam = naam;
	}

	/** Geeft de status van deze taxi terug.
	 * @return Status van de taxi
	 */
	public TaxiStatus getStatus() {
		return status;
	}

	/** Stelt een nieuwe status in voor deze taxi.
	 * @param status Nieuwe status
	 */
	public void setStatus(TaxiStatus status) {
		this.status = status;
	}

	/** Geeft de reis terug dat deze taxi uitvoert.
	 * @return De reis
	 * @throws RuntimeException Als deze taxi geen reis uitvoert.
	 */
	public Reis getReis() throws RuntimeException {
		if (reis == null) throw new RuntimeException("Taxi is op dit moment niet gekoppeld aan een reis.");
		return reis;
	}

	/** Stelt een nieuwe <code>Reis</code> in dat deze taxi moet uitvoeren.
	 * @param r Nieuwe reis
	 */
	public void setReis(Reis r) {
		reis = r;
	}

	/** Laat de taxi op het spoor rijden. Als deze taxi op het station staat dan wordt het eerst uitgevoegd. Observer(s) worden genotificeerd met de opgegeven TaxiReden.
	 * @param reden Reden van rijden
	 * @return Is waar als deze taxi rijdt en anders onwaar
	 */
	public boolean rij(TaxiReden reden) {
		if (reden == TaxiReden.HERVAT_NA_MOGELIJKE_AANRIJDING || reden == TaxiReden.HERVAT_NA_PAUZE) {
			startUpdateTijdSpoor = System.currentTimeMillis();
			status = TaxiStatus.RIJD;
			setChanged();
			notifyObservers(reden);
			clearChanged();
			return true;
		}
		else if (reden == TaxiReden.UIT_STATION) {
			uitvoegen();
			startUpdateTijdSpoor = System.currentTimeMillis();
			status = TaxiStatus.RIJD;
			setChanged();
			notifyObservers(reden);
			clearChanged();
			return true;
		}
		return false;
	}

	/** Laat deze taxi op het spoor of op het station stoppen.
	 * @param reden Reden van stoppen
	 * @return Is waar als deze taxi is gestopt en anders onwaar
	 */
	public boolean stop(TaxiReden reden) {
		if (getStatus() == TaxiStatus.RIJD) {
			startUpdateTijdSpoor = 0;
			if (reden == TaxiReden.MOGELIJKE_AANRIJDING || reden == TaxiReden.OP_STATION) status = TaxiStatus.WACHT_OP_SPOOR;
			else if (reden == TaxiReden.PAUZE) status = TaxiStatus.GEPAUZEERD_OP_SPOOR;
			setChanged();
			notifyObservers(reden);
			clearChanged();
			return true;
		}
		return false;
	}

	/** Geeft de starttijd terug voor het bijwerken van de positie van deze taxi op het spoor.
	 * @return Starttijd voor het bijwerken van de spoorpositie
	 */
	public long getStartUpdateTijdSpoor() {
		return startUpdateTijdSpoor;
	}

	/** Stelt een nieuwe starttijd in voor het bijwerken van de positie van deze taxi op het spoor.
	 * @param tijd Nieuwe starttijd voor het bijwerken
	 */
	public void setStartUpdateTijdSpoor(long tijd) {
		this.startUpdateTijdSpoor = tijd;
	}

	/** Geeft de absolute positie (positie op de hoofdbaan vanuit het startpunt) van deze taxi.
	 * @return absolute positie
	 */
	public double getAbsolutePositie() {
		return absoluteAfstand;
	}

	/** Geeft de positie van deze taxi op de zijbaan terug. 
	 * De zijbaan is een combinatie van het stationspoor en de bijbehorende wisselsporen. 
	 * Als de taxi niet richting het stationspoor of uit het station gaat dan is de positie op de zijbaan gelijk aan 0.
	 * @return Positie op de zijbaan
	 */
	public double getZijPositie() {
		if (opSpoor()) {
			if (spoor.getType() == SpoorType.WISSEL_SPOOR) {
				Wisselspoor wSpoor = (Wisselspoor) spoor;
				if (wSpoor.getRichting() == Wisselspoor.RICHTING_VOOR_ZIJSPOOR && wSpoor.isGeschakeld())
					return spoorAfstand;
				else if (wSpoor.getRichting() == Wisselspoor.RICHTING_NA_ZIJSPOOR && wSpoor.isGeschakeld())
					return spoor.vorige().vorige().getLengte() + spoor.vorige().getLengte() + spoorAfstand;
			} else if (spoor.getType() == SpoorType.STATION_SPOOR) {
				return spoor.vorige().getLengte() + spoorAfstand;
			}
		} else if (opStation()) {
			return station.getZijPositieIn();
		}
		return 0;
	}

	/** Werkt de spoor positie en absolute positie van deze taxi bij. Als deze taxi een station bereikt dan wordt het ingevoegd.
	 * 
	 */
	public void updatePositie() {
		if (status == TaxiStatus.RIJD) {
			if (opStationSpoor()) {
				Stationspoor sSpoor = (Stationspoor) spoor;
				Station huidigeStation = sSpoor.getStation();
				if ((huidigeStation == reis.getReisDetails().getVertrekpunt() && !reis.reizigersOpgehaald()) || (huidigeStation == reis.getReisDetails().getBestemming() && reis.reizigersOpgehaald())) {
					if (spoorAfstand >= sSpoor.getStation().getPositie()) {
						stop(TaxiReden.OP_STATION);
						invoegen(huidigeStation);
						spoorAfstand = sSpoor.getStation().getPositie();
						System.out.println(naam + " zit op: " + huidigeStation.getNaam());
					}
				}
			}

			if (opSpoor()) {
				double verstrekenTijd = ((System.currentTimeMillis() - startUpdateTijdSpoor) / 1000.0);
				double afgelegdeAfstand = verstrekenTijd * (baanManager.getTaxiSnelheid() * ReisManager.getVersnelFactor());

				if ((spoorAfstand + afgelegdeAfstand) >= spoor.getLengte()) {
					if (spoor.getType() == SpoorType.SPOOR || (spoor.getType() == SpoorType.WISSEL_SPOOR && !((Wisselspoor)spoor).isGeschakeld())) {
						absoluteAfstand += spoor.getLengte() - spoorAfstand;
					}
					else if (spoor.getType() == SpoorType.WISSEL_SPOOR && ((Wisselspoor)spoor).getRichting() == Wisselspoor.RICHTING_NA_ZIJSPOOR) {
						absoluteAfstand += spoor.getLengte();
						absoluteAfstand += spoor.getVoorSpoor().getLengte();
						absoluteAfstand += spoor.getVoorSpoor().getVoorSpoor().getLengte();
					}
					double restAfstand = (spoorAfstand + afgelegdeAfstand) -  spoor.getLengte();
					spoorAfstand = 0;
					setSpoor(spoor.volgende());
					while (restAfstand >= spoor.getLengte()) {
						restAfstand -= spoor.getLengte();

						if (spoor.getType() == SpoorType.SPOOR || (spoor.getType() == SpoorType.WISSEL_SPOOR && !((Wisselspoor)spoor).isGeschakeld())) {
							if (spoor == baanManager.getSporen()) absoluteAfstand = 0;
							absoluteAfstand += spoor.getLengte();
						}
						else if (spoor.getType() == SpoorType.WISSEL_SPOOR && ((Wisselspoor)spoor).getRichting() == Wisselspoor.RICHTING_NA_ZIJSPOOR) {
							absoluteAfstand += spoor.getLengte();
							absoluteAfstand += spoor.getVoorSpoor().getLengte();
							absoluteAfstand += spoor.getVoorSpoor().getVoorSpoor().getLengte();
						}
						setSpoor(spoor.volgende());
					}
					if (restAfstand > 0) {
						if (spoor.getType() == SpoorType.SPOOR || (spoor.getType() == SpoorType.WISSEL_SPOOR && !((Wisselspoor)spoor).isGeschakeld())) {
							if (spoor == baanManager.getSporen()) absoluteAfstand = 0;
							absoluteAfstand += restAfstand;
						}
						spoorAfstand += restAfstand;
					}
				} else {
					if (spoor.getType() == SpoorType.SPOOR || (spoor.getType() == SpoorType.WISSEL_SPOOR && !((Wisselspoor)spoor).isGeschakeld())) {
						absoluteAfstand += afgelegdeAfstand;
					}
					spoorAfstand += afgelegdeAfstand;
				}
				startUpdateTijdSpoor = System.currentTimeMillis();
			}
			if (opStationSpoor() && naarStation() && (getSpoorPositie() > ((Stationspoor)spoor).getStation().getPositie()))
				updatePositie();
			else {
				if (opSpoor()) System.out.format("%s zit op: %s, SpoorAfstand: %.2f en AbsoluteAfstand: %.2f en ZijAfstand: %.2f. Versnelfactor: %d.%n", getNaam(), getSpoor().getID(), spoorAfstand, absoluteAfstand, getZijPositie(), ReisManager.getVersnelFactor());
				setChanged();
				notifyObservers();
				clearChanged();
			}
		}
	}

	/** Geeft aan of deze taxi op het station is.
	 * @return Is waar als deze taxi op het station is en anders onwaar
	 */
	public boolean opStation() {
		if ((status == TaxiStatus.WACHT_OP_STATION || status == TaxiStatus.ONBEZET ||status == TaxiStatus.GEPAUZEERD_OP_SPOOR) && station != null) return true;
		return false;
	}

	/** Geeft aan of deze taxi op de zijbaan naar het station rijdt.
	 * @return Is waar als deze taxi naar het station rijdt en anders onwaar
	 */
	public boolean naarStation() {
		if (opSpoor()) {
			if (spoor.getType() == SpoorType.WISSEL_SPOOR) {
				Wisselspoor wSpoor = (Wisselspoor) spoor;
				if (wSpoor.getRichting() == Wisselspoor.RICHTING_VOOR_ZIJSPOOR && wSpoor.isGeschakeld())
					return true;
			}
			else if (spoor.getType() == SpoorType.STATION_SPOOR) {
				Stationspoor sSpoor = (Stationspoor) spoor;
				if (sSpoor.getStation() == getReis().getReisDetails().getVertrekpunt() && !getReis().reizigersOpgehaald())
					return true;
				else if (sSpoor.getStation() == getReis().getReisDetails().getBestemming() && getReis().reizigersOpgehaald() && getReis().naarBestemming())
					return true;
				else if (getSpoorPositie() <= sSpoor.getStation().getPositie()) return true;
			}
		}
		return false;
	}

	/** Geeft aan of deze taxi op de zijbaan vanuit het station rijdt.
	 * @return Is waar als deze taxi uit het station en zijbaan rijdt en anders onwaar
	 */
	public boolean vanStation() {
		if (opSpoor()) {
			if (spoor.getType() == SpoorType.WISSEL_SPOOR) {
				Wisselspoor wSpoor = (Wisselspoor) spoor;
				if (wSpoor.getRichting() == Wisselspoor.RICHTING_NA_ZIJSPOOR && wSpoor.isGeschakeld())
					return true;
			}
			else if (spoor.getType() == SpoorType.STATION_SPOOR) {
				Stationspoor sSpoor = (Stationspoor) spoor;
				if (sSpoor.getStation() == getReis().getReisDetails().getVertrekpunt() && getReis().reizigersOpgehaald())
					return true;
				else if (sSpoor.getStation() == getReis().getReisDetails().getBestemming() && getSpoorPositie() > sSpoor.getStation().getPositie() && !getReis().naarBestemming())
					return true;
				else if (getSpoorPositie() > sSpoor.getStation().getPositie())
					return true;
			}
		}
		return false;
	}

	/** Geeft aan of deze taxi op een spoor staat.
	 * @return Is waar als deze taxi op een spoor staat en anders onwaar
	 */
	public boolean opSpoor() {
		if (spoor != null && (status == TaxiStatus.WACHT_OP_SPOOR || status == TaxiStatus.RIJD || status == TaxiStatus.GEPAUZEERD_OP_SPOOR)) return true;
		return false;
	}

	/** Geeft aan of deze taxi op een stationspoor staat.
	 * @return Is waar als deze taxi op een stationspoor staat en anders onwaar
	 */
	public boolean opStationSpoor() {
		if (spoor != null && spoor.getType() == SpoorType.STATION_SPOOR) return true;
		else return false;
	}

	/** Geeft aan of deze taxi op het hoofdspoor staat.
	 * @return Is waar als deze taxi op het hoofdspoor staat en anders onwaar
	 */
	public boolean opHoofdSpoor() {
		if (spoor != null && spoor.getType() == SpoorType.SPOOR) return true;
		else return false;
	}

	/** Geeft het spoor waar deze taxi op staat terug.
	 * @return Het spoor waar deze taxi op staat
	 * @throws RuntimeException Wanneer de taxi niet op een spoor staat
	 */
	public Spoor getSpoor() throws RuntimeException {
		if (opSpoor()) return spoor;
		throw new RuntimeException("De taxi staat niet op een spoor");
	}

	/** Stelt een nieuwe spoor voor deze taxi in en voegt de taxi aan het spoordeel toe.
	 * @param spoor Nieuwe spoor
	 */
	public void setSpoor(Spoor spoor) {
		if (this.spoor != null) this.spoor.verwijderTaxi(this);
		this.spoor = spoor;
		if (spoor != null) spoor.voegTaxi(this);
	}

	/** Geeft de positie terug op het huidige spoordeel waar deze taxi op staat.
	 * @return Positie op het huidige spoordeel
	 */
	public double getSpoorPositie() {
		return spoorAfstand;
	}

	/** Geeft het station waar deze taxi op staat terug.
	 * @return Het station
	 * @throws RuntimeException Wanneer deze taxi niet op een station staat.
	 */
	public Station getStation() throws RuntimeException {
		if (opStation()) return station;
		throw new RuntimeException("De taxi staat niet op het station");
	}

	/** Geeft de wachttijd van deze taxi op het station terug.
	 * @return wachttijd op het station
	 */
	public long getWachtTijdOpStation() {
		return wachtTijdStation;
	}

	/** Stelt een nieuwe starttijd in voor het bijwerken van de wachttijd van deze taxi op het station.
	 * @param startTijd Nieuwe starttijd
	 */
	public void setStartTijdOpStation(long startTijd) {
		this.startTijdStation = startTijd;
	}

	/** Stelt een nieuwe positie in voor deze taxi op het huidige spoordeel.
	 * @param positie Positie op het spoor
	 */
	public void setSpoorPositie(long positie) {
		spoorAfstand = positie;
	}

	/** Vergelijkt 2 Taxi's aan de hand van de ID, naam, spoordeel, station, reizigers en reis van de taxi's.
	 * @param t Taxi om mee te vergelijken
	 * @return Is waar als de 2 taxi's gelijk zijn aan elkaar en anders onwaar
	 */
	public boolean equals(Taxi t) {
		if (getID() != t.getID()) return false;
		if (!getNaam().equals(t.getNaam())) return false;
		if (opSpoor() != t.opSpoor()) return false;
		if (opStation() != t.opStation()) return false;
		if (getReizigers() != t.getReizigers()) return false;
		if (getReis() != t.getReis()) return false;
		return true;
	}

	public int compareTo(Taxi t) {
		if (!isOnbezet() && !t.isOnbezet()) {
			if (getWachtTijdOpStation() > t.getWachtTijdOpStation()) return 1;
			else if (getWachtTijdOpStation() < t.getWachtTijdOpStation()) return -1;
			else return 0;
		}
		else if (isOnbezet() && !t.isOnbezet()) return -1;
		else if (getWachtTijdOpStation() > t.getWachtTijdOpStation()) return 1;
		else if (getWachtTijdOpStation() < t.getWachtTijdOpStation()) return -1;
		return 0;
	}

	/** Voegt de reiziger toe aan deze taxi.
	 * @param reiziger Reiziger om toe te voegen
	 * @return Is waar als de reiziger is toegevoegd en anders onwaar
	 */
	public boolean voegReiziger(Reiziger reiziger) {
		return reizigers.add(reiziger);
	}

	/** Verwijdert de reiziger uit deze taxi.
	 * @param reiziger Reiziger om te verwijderen
	 * @return Is waar als de reiziger is verwijderd en anders onwaar
	 */
	public boolean verwijderReiziger(Reiziger reiziger) {
		return reizigers.remove(reiziger);
	}

	/** Geeft aan of deze taxi de opgegeven reiziger bevat.
	 * @param reiziger De reiziger 
	 * @return Is waar als de reiziger in de taxi zit/staat en anders onwaar
	 */
	public boolean bevatReiziger(Reiziger reiziger) {
		return reizigers.contains(reiziger);
	}

	/** Geeft de reizigers in deze taxi terug.
	 * @return Lijst met reizigers
	 */
	public ArrayList<Reiziger> getReizigers() {
		return reizigers;
	}

	/** Geeft het aantal reizigers in deze taxi terug.
	 * @return Het aantal reizigers
	 */
	public int getAantalReizigers() {
		return reizigers.size();
	}
	
	/** Geeft aan of deze taxi op het station op de reizigers van zijn reis wacht.
	 * @return Is waar als deze taxi op de reizigers van zijn reis wacht en anders onwaar.
	 */
	public boolean wachtOpReizigers() {
		if (opStation() && getStation() == getReis().getReisDetails().getVertrekpunt() && !getReis().reizigersOpgehaald()) return true;
		return false;
	}
	
	/** Geeft aan of deze taxi op het station wacht totdat het de deuren kan sluiten (wachttijd verstreken).
	 * @return Is waar als de wachttijd op het station is verstreken en anders onwaar
	 */
	public boolean wachtOpSluitingsTijd() {
		if (opStation() && getWachtTijdOpStation() < baanManager.getWachtTijdStation()) return true;
		return false;
	}

	/** Geeft aan of deze taxi wacht voordat het uit het station kan rijden.
	 * @return Is waar als deze taxi wacht op het uitrijden en anders onwaar
	 */
	public boolean wachtOpUitrijden() {
		if (opStation() && getWachtTijdOpStation() >= baanManager.getWachtTijdStation()) {
			if (getStation() != reis.getReisDetails().getVertrekpunt()) return true;
			else if (reis.reizigersOpgehaald()) return true;
		}
		return false;
	}

	/** Geeft aan of deze taxi onbezet en op een station staat. 
	 * @return Is waar als deze taxi onbezet is en anders onwaar
	 */
	public boolean isOnbezet() {
		if (opStation() && getStatus() == TaxiStatus.ONBEZET) return true;
		return false;
	}

	/** Voert het uitvoegingsprocedure van deze taxi bij een station uit. 
	 * Deze taxi rijdt uit het station en op het spoor. 
	 * 
	 */
	public void uitvoegen() {
		getStation().voegTaxiUit(this);
		setSpoorPositie(getStation().getPositie());
		setSpoor(getStation().getSpoor());
		setStation(null);
		setStartTijdOpStation(0);
		setWachtTijdOpStation(0);
	}

	/** Werkt de wachttijd van deze taxi op het station bij zolang het niet onbezet is.
	 * 
	 */
	public void updateWachtTijdOpStation() {
		if (opStation() && !isOnbezet()) {
			long verstrekenTijd = (System.currentTimeMillis() - startTijdStation) * ReisManager.getVersnelFactor();
			wachtTijdStation += verstrekenTijd;
			reis.setWachtTijd(reis.getWachtTijd() + verstrekenTijd);
			resetStartTijdOpStation();
		}
	}

	/** Voert het invoegingsprocedure van deze taxi als het op een station is aangekomen. Deze taxi rijdt het station binnen en uit het spoor.
	 * @param s Station om in te voegen
	 */
	public void invoegen(Station s) {
		setStartTijdOpStation(System.currentTimeMillis());
		setWachtTijdOpStation(0);
		if (getStatus() != TaxiStatus.ONBEZET) s.voegTaxiIn(this);
		setStation(s);
		setSpoor(null);
		setStatus(TaxiStatus.WACHT_OP_STATION);
	}

	/** Geeft de starttijd terug voor het bijwerken van de wachttijd op het station.
	 * @return Starttijd voor het bijwerken van de wachttijd
	 */
	public long getStartTijdOpStation() {
		return startTijdStation;
	}

	/**
	 * Stelt de starttijd van deze taxi (voor het bijwerken van de wachttijd op het station) in op de huidige tijd (reset).
	 */
	public void resetStartTijdOpStation() {
		startTijdStation = System.currentTimeMillis();
	}

	/** Stelt een nieuwe wachttijd voor deze taxi op het station in.
	 * @param tijd Nieuwe 
	 */
	public void setWachtTijdOpStation(long tijd) {
		wachtTijdStation = tijd;
	}
	
    /** Geeft de namen van de reizigers in deze taxi terug.
     * @return Namen van de reizigers in deze taxi
     */
    public String[] getReizigerNamen() {
    	String[] namen = new String[reizigers.size()]; 
    	for (int i = 0; i < reizigers.size(); i++) {
    		namen[i] = reizigers.get(i).getNaam();
    	}
    	return namen;
    }
    
    /** Geeft de reiziger in deze taxi met de opgegeven naam terug.
     * @param naam Naam van de reiziger
     * @return Reiziger in deze taxi met de opgegeven naam
     * @throws RuntimeException Wanneer de reiziger niet in deze taxi is gevonden
     */
    public Reiziger getReiziger(String naam) throws RuntimeException {
    	for (Reiziger r : reizigers) {
    		if (r.getNaam().equals(naam)) return r;
    	}
    	throw new RuntimeException("Reiziger is niet in de taxi gevonden");
    }
    
    /** Geeft aan of de reiziger met de opgegeven naam in deze taxi zit.
     * @param naam Naam van de reiziger
     * @return Is waar als de reiziger in deze taxi zit en anders onwaar
     */
    public boolean bevatReiziger(String naam) {
    	for (Reiziger r : reizigers) {
    		if (r.getNaam().equals(naam)) return true;
    	}
    	return false;
    }
}
