package org.tigam.railcab.algoritme;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Observer;

/** De <code>BaanManager</code> bevat de baanstructuur, stations en taxi's. De baanstructuur bevat hulppmiddelen voor de baan zoals afstandsberekening, controleren op aanrijdingen of het schakelen en terugschakelen van wisselsporen.
 * @author Mustapha Bouzaidi
 *
 */
public class BaanManager implements Serializable {
	private static final long serialVersionUID = -5484287432697036714L;
	/**
	 * De minimale afstand waarna een taxi een wissel mag schakelen.
	 */
	public static final int MIN_AFSTAND_TOT_WISSEL = 500;
	/**
	 * De minimale afstand dat taxi's van elkaar moeten nemen.
	 */
	public static final int MIN_AFSTAND_TOT_TAXI = 500;

	private int wachtTijdStation = 10000;
	private int taxiSnelheid = 22;
	private int taxiTeller;
	private Spoor sporen;
	private ArrayList<Station> stations;
	private ArrayList<Taxi> taxis;
	private HashMap<Taxi, Wisselspoor> wisselLijst;

	/**
	 * Cre�ert een instantie van de baanmanager zonder een baanstructuur, stations en taxi's.
	 */
	public BaanManager() {
		stations = new ArrayList<Station>();
		taxis = new ArrayList<Taxi>();
		wisselLijst = new HashMap<Taxi, Wisselspoor>();
		taxiTeller = 0;
	}

	/** Geeft de baanstructuur terug.
	 * @return De baanstructuur, beginnend bij de eerste spoordeel
	 */
	public Spoor getSporen() {
		return sporen;
	}

	/** Plaatst de baanstructuur die door de baanmanager gebruikt wordt.
	 * @param spoor De baanstructuur
	 */
	public void setSporen(Spoor spoor) {
		this.sporen = spoor;
	}

	/** Geeft alle taxi's in de baanmanager terug.
	 * @return Alle taxi's in de baanmanager
	 */
	public ArrayList<Taxi> getTaxis() {
		return taxis;
	}

	/** Plaatst de taxi's die door de baanmanager gebruikt worden.
	 * @param taxis Lijst met taxi objecten
	 */
	public void setTaxi(ArrayList<Taxi> taxis) {
		this.taxis = taxis;
	}

	/** Voegt een taxi object toe die in de baanmanager gebruikt wordt.
	 * @param taxi De taxi
	 */
	public void voegTaxi(Taxi taxi) {
            System.out.println( "Taxi toegevoegd met id: "+taxi.getID() );
		taxis.add(taxi);
	}

	/** Voegt een onbezette taxi in de baanmanager en aan de betreffende station.
	 * @param taxi De onbezette taxi
	 * @param station Het station waar de onbezette taxi moet staan
	 */
	public synchronized void voegOnbezetteTaxi(Taxi taxi, Station station) {
		taxi.setStatus(TaxiStatus.ONBEZET);
		voegTaxi(taxi);
		station.voegTaxiIn(taxi);
	}

	/** plaatst de stations in de baanmanager
	 * @param stations De stations
	 */
	public void setStations(ArrayList<Station> stations) {
		this.stations = stations;
	}

	/** Geeft de namen van alle stations in de baanmanager terug.
	 * @return namen van alle stations in
	 */
	public String[] getStations() {
		String[] stationsNamen = new String[stations.size()];
		for (int i = 0; i < stations.size(); i++)
			stationsNamen[i] = stations.get(i).getNaam();
		return stationsNamen;
	}

	/** Geeft alle beschikbare taxi's terug.
	 * @return alle beschikbare (onbezette) taxi's
	 */
	public ArrayList<Taxi> getBeschikbareTaxis() {
		ArrayList<Taxi> oTaxis = new ArrayList<Taxi>();
		for (Taxi t : taxis) {
			if (t.getStatus() == TaxiStatus.ONBEZET) {
				try {
					if (t.getReis() == null) { }
				} catch (RuntimeException e) {
					oTaxis.add(t);
				}
			}
		}
		return oTaxis;
	}

	/** Bepaalt de afstand tussen 2 stations.
	 * @param begin Station waar de afstand vanaf bepaald wordt
	 * @param eind Station waar de afstand tot bepaald wordt
	 * @return Afstand tussen het begin en eindstation
	 */
	public long bepaalAfstand(Station begin, Station eind) {
		long afstand = 0;
		if (begin == eind) return afstand;
		boolean overslaan = false, beginBereikt = false;
		for (Spoor spoor : sporen) {
			if (!overslaan) {
				if (spoor.getType() == SpoorType.WISSEL_SPOOR && ((Wisselspoor)spoor).getRichting() == Wisselspoor.RICHTING_VOOR_ZIJSPOOR) {
					Wisselspoor wSpoor = (Wisselspoor) spoor;
					if (getStation(wSpoor) == begin) {
						Stationspoor sSpoor = (Stationspoor) wSpoor.getZijSpoor();
						afstand += sSpoor.getLengte() - sSpoor.getStation().getPositie();
						afstand += sSpoor.volgende().getLengte();
						overslaan = true;
						beginBereikt = true;
					}
					else if (getStation(wSpoor) == eind && beginBereikt) {
						afstand += wSpoor.getLengte();
						afstand += wSpoor.getZijSpoor().getLengte() - ((Stationspoor)wSpoor.getZijSpoor()).getStation().getPositie();
						return afstand;
					}
				}
				else if (beginBereikt) {
					afstand += spoor.getLengte();
				}
			}
			else overslaan = false;
		}
		return afstand;
	}

	/** Geeft het station terug met de betreffende naam.
	 * @param naam De naam van de station
	 * @return De station met de opgegeven naam
	 */
	public Station getStation(String naam) {
		for (Station s : stations) {
			if (s.getNaam().equals(naam))
				return s;
		}
		return null;
	}

	/** Maakt het aantal stations aan en de baanstructuur eromheen. 
	 * Kan minimaal 2 en maximaal 8 stations aanmaken. Hoofdbaan is altijd 24 km breed. 
	 * Zijbanen zijn altijd 1 km breed incl. wisselsporen. De wisselsporen zijn elk 50 meter breed.
	 * @param aantal De hoeveelheid stations die aangemaakt moeten worden.
	 * @return Is waar als de stations en baanstructuur is aangemaakt en anders onwaar.
	 */
	public synchronized boolean maakStations(int aantal) {
		Wisselspoor ws[] = new Wisselspoor[aantal * 2];
		for (int i = 0; i < aantal; i++) {
			ws[i + i] = new Wisselspoor("WS" + (i + i + 1), 50, Wisselspoor.RICHTING_VOOR_ZIJSPOOR);
			ws[i + i + 1] = new Wisselspoor("WS" + (i + i + 2), 50, Wisselspoor.RICHTING_NA_ZIJSPOOR);
		}

		Spoor spoor1, spoor2, spoor3, spoor4, spoor5, spoor6, spoor7, spoor8, 
		spoor9, spoor10, spoor11, spoor12, spoor13, spoor14, spoor15, spoor16, spoor17;
		Stationspoor sSpoor1, sSpoor2, sSpoor3, sSpoor4, sSpoor5, sSpoor6, sSpoor7, sSpoor8;

		Station station1, station2, station3, station4, station5, station6, station7, station8;

		switch (aantal) {
		case 2:
			station1 = new Station(1, "RAI", 450);
			station2 = new Station(2, "Duivendrecht", 450);
			stations.add(station1);
			stations.add(station2);

			spoor1 = new Spoor("HS1", 500);
			spoor1.setNaSpoor(ws[0]);

			spoor2 = new Spoor("HS2", 900, ws[0], ws[1]);
			sSpoor1 = new Stationspoor("SS1", 900, ws[0], ws[1], station1);
			spoor3 = new Spoor("HS3", 11000, ws[1], ws[2]);
			spoor4 = new Spoor("HS4", 900, ws[2], ws[3]);
			sSpoor2 = new Stationspoor("SS2", 900, ws[2], ws[3], station2);
			spoor5 = new Spoor("HS5", 10500, ws[3], spoor1);
			ws[0].setZijSpoor(sSpoor1);
			ws[0].setNaSpoor(spoor2);
			ws[1].setZijSpoor(sSpoor1);
			ws[1].setNaSpoor(spoor3);
			ws[2].setZijSpoor(sSpoor2);
			ws[2].setNaSpoor(spoor4);
			ws[3].setZijSpoor(sSpoor2);
			ws[3].setNaSpoor(spoor5);
			setSporen(spoor1);
			System.out.println("Baanstructuur aangemaakt!");
			return true;
		case 3: 
			station1 = new Station(1, "RAI", 450);
			station2 = new Station(2, "Duivendrecht", 450);
			station3 = new Station(3, "ZuidWTC", 450);
			stations.add(station1);
			stations.add(station2);
			stations.add(station3);

			spoor1 = new Spoor("HS1", 3500);
			spoor1.setNaSpoor(ws[0]);

			spoor2 = new Spoor("HS2", 900, ws[0], ws[1]);
			sSpoor1 = new Stationspoor("SS1", 900, ws[0], ws[1], station1);
			spoor3 = new Spoor("HS3", 8000, ws[1], ws[2]);
			spoor4 = new Spoor("HS4", 900, ws[2], ws[3]);
			sSpoor2 = new Stationspoor("SS2", 900, ws[2], ws[3], station2);
			spoor5 = new Spoor("HS5", 5000, ws[3], ws[4]);
			spoor6 = new Spoor("HS6", 900, ws[4], ws[5]);
			sSpoor3 = new Stationspoor("SS3", 900, ws[4], ws[5], station3);
			spoor7 = new Spoor("HS7", 4500, ws[5], spoor1);

			ws[0].setZijSpoor(sSpoor1);
			ws[0].setNaSpoor(spoor2);
			ws[1].setZijSpoor(sSpoor1);
			ws[1].setNaSpoor(spoor3);
			ws[2].setZijSpoor(sSpoor2);
			ws[2].setNaSpoor(spoor4);
			ws[3].setZijSpoor(sSpoor2);
			ws[3].setNaSpoor(spoor5);
			ws[4].setZijSpoor(sSpoor3);
			ws[4].setNaSpoor(spoor6);
			ws[5].setZijSpoor(sSpoor3);
			ws[5].setNaSpoor(spoor7);

			setSporen(spoor1);
			System.out.println("Baanstructuur aangemaakt!");
			return true;
		case 4:
			station1 = new Station(1, "Muiderpoort", 450);
			station2 = new Station(2, "ZuidWTC", 450);
			station3 = new Station(3, "RAI", 450);
			station4 = new Station(4, "Duivendrecht", 450);
			stations.add(station1);
			stations.add(station2);
			stations.add(station3);
			stations.add(station4);

			spoor1 = new Spoor("HS1", 500);
			spoor1.setNaSpoor(ws[0]);

			spoor2 = new Spoor("HS2", 900, ws[0], ws[1]);
			sSpoor1 = new Stationspoor("SS1", 900, ws[0], ws[1], station1);
			spoor3 = new Spoor("HS3", 5000, ws[1], ws[2]);
			spoor4 = new Spoor("HS4", 900, ws[2], ws[3]);
			sSpoor2 = new Stationspoor("SS2", 900, ws[2], ws[3], station2);
			spoor5 = new Spoor("HS5", 5000, ws[3], ws[4]);
			spoor6 = new Spoor("HS6", 900, ws[4], ws[5]);
			sSpoor3 = new Stationspoor("SS3", 900, ws[4], ws[5], station3);
			spoor7 = new Spoor("HS7", 5000, ws[5], ws[6]);
			spoor8 = new Spoor("HS8", 900, ws[6], ws[7]);
			sSpoor4 = new Stationspoor("SS4", 900, ws[6], ws[7], station4);
			spoor9 = new Spoor("HS9", 4500, ws[7], spoor1);

			ws[0].setZijSpoor(sSpoor1);
			ws[0].setNaSpoor(spoor2);
			ws[1].setZijSpoor(sSpoor1);
			ws[1].setNaSpoor(spoor3);
			ws[2].setZijSpoor(sSpoor2);
			ws[2].setNaSpoor(spoor4);
			ws[3].setZijSpoor(sSpoor2);
			ws[3].setNaSpoor(spoor5);
			ws[4].setZijSpoor(sSpoor3);
			ws[4].setNaSpoor(spoor6);
			ws[5].setZijSpoor(sSpoor3);
			ws[5].setNaSpoor(spoor7);
			ws[6].setZijSpoor(sSpoor4);
			ws[6].setNaSpoor(spoor8);
			ws[7].setZijSpoor(sSpoor4);
			ws[7].setNaSpoor(spoor9);

			setSporen(spoor1);
			System.out.println("Baanstructuur aangemaakt!");
			return true;
		case 5:
			station1 = new Station(1, "AmsterdamCS", 450);
			station2 = new Station(2, "Muiderpoort", 450);
			station3 = new Station(3, "ZuidWTC", 450);
			station4 = new Station(4, "RAI", 450);
			station5 = new Station(5, "Duivendrecht", 450);
			stations.add(station1);
			stations.add(station2);
			stations.add(station3);
			stations.add(station4);
			stations.add(station5);

			spoor1 = new Spoor("HS1", 3500);
			spoor1.setNaSpoor(ws[0]);

			spoor2 = new Spoor("HS2", 900, ws[0], ws[1]);
			sSpoor1 = new Stationspoor("SS1", 900, ws[0], ws[1], station1);
			spoor3 = new Spoor("HS3", 5000, ws[1], ws[2]);
			spoor4 = new Spoor("HS4", 900, ws[2], ws[3]);
			sSpoor2 = new Stationspoor("SS2", 900, ws[2], ws[3], station2);
			spoor5 = new Spoor("HS5", 2000, ws[3], ws[4]);
			spoor6 = new Spoor("HS6", 900, ws[4], ws[5]);
			sSpoor3 = new Stationspoor("SS3", 900, ws[4], ws[5], station3);
			spoor7 = new Spoor("HS7", 5000, ws[5], ws[6]);
			spoor8 = new Spoor("HS8", 900, ws[6], ws[7]);
			sSpoor4 = new Stationspoor("SS4", 900, ws[6], ws[7], station4);
			spoor9 = new Spoor("HS9", 2000, ws[7], ws[8]);
			spoor10 = new Spoor("HS10", 900, ws[8], ws[9]);
			sSpoor5 = new Stationspoor("SS5", 900, ws[8], ws[9], station5);
			spoor11 = new Spoor("HS11", 1500, ws[9], spoor1);

			ws[0].setZijSpoor(sSpoor1);
			ws[0].setNaSpoor(spoor2);
			ws[1].setZijSpoor(sSpoor1);
			ws[1].setNaSpoor(spoor3);
			ws[2].setZijSpoor(sSpoor2);
			ws[2].setNaSpoor(spoor4);
			ws[3].setZijSpoor(sSpoor2);
			ws[3].setNaSpoor(spoor5);
			ws[4].setZijSpoor(sSpoor3);
			ws[4].setNaSpoor(spoor6);
			ws[5].setZijSpoor(sSpoor3);
			ws[5].setNaSpoor(spoor7);
			ws[6].setZijSpoor(sSpoor4);
			ws[6].setNaSpoor(spoor8);
			ws[7].setZijSpoor(sSpoor4);
			ws[7].setNaSpoor(spoor9);
			ws[8].setZijSpoor(sSpoor5);
			ws[8].setNaSpoor(spoor10);
			ws[9].setZijSpoor(sSpoor5);
			ws[9].setNaSpoor(spoor11);

			setSporen(spoor1);
			System.out.println("Baanstructuur aangemaakt!");
			return true;
		case 6:
			station1 = new Station(1, "Sloterdijk", 450);
			station2 = new Station(2, "AmsterdamCS", 450);
			station3 = new Station(3, "Muiderpoort", 450);
			station4 = new Station(4, "ZuidWTC", 450);
			station5 = new Station(5, "RAI", 450);
			station6 = new Station(6, "Duivendrecht", 450);
			stations.add(station1);
			stations.add(station2);
			stations.add(station3);
			stations.add(station4);
			stations.add(station5);
			stations.add(station6);

			spoor1 = new Spoor("HS1", 500);
			spoor1.setNaSpoor(ws[0]);

			spoor2 = new Spoor("HS2", 900, ws[0], ws[1]);
			sSpoor1 = new Stationspoor("SS1", 900, ws[0], ws[1], station1);
			spoor3 = new Spoor("HS3", 5000, ws[1], ws[2]);
			spoor4 = new Spoor("HS4", 900, ws[2], ws[3]);
			sSpoor2 = new Stationspoor("SS2", 900, ws[2], ws[3], station2);
			spoor5 = new Spoor("HS5", 2000, ws[3], ws[4]);
			spoor6 = new Spoor("HS6", 900, ws[4], ws[5]);
			sSpoor3 = new Stationspoor("SS3", 900, ws[4], ws[5], station3);
			spoor7 = new Spoor("HS7", 2000, ws[5], ws[6]);
			spoor8 = new Spoor("HS8", 900, ws[6], ws[7]);
			sSpoor4 = new Stationspoor("SS4", 900, ws[6], ws[7], station4);
			spoor9 = new Spoor("HS9", 5000, ws[7], ws[8]);
			spoor10 = new Spoor("HS10", 900, ws[8], ws[9]);
			sSpoor5 = new Stationspoor("SS5", 900, ws[8], ws[9], station5);
			spoor11 = new Spoor("HS11", 2000, ws[9], ws[10]);
			spoor12 = new Spoor("HS12", 900, ws[10], ws[11]);
			sSpoor6 = new Stationspoor("SS6", 900, ws[10], ws[11], station6);
			spoor13 = new Spoor("HS13", 1500, ws[11], spoor1);

			ws[0].setZijSpoor(sSpoor1);
			ws[0].setNaSpoor(spoor2);
			ws[1].setZijSpoor(sSpoor1);
			ws[1].setNaSpoor(spoor3);
			ws[2].setZijSpoor(sSpoor2);
			ws[2].setNaSpoor(spoor4);
			ws[3].setZijSpoor(sSpoor2);
			ws[3].setNaSpoor(spoor5);
			ws[4].setZijSpoor(sSpoor3);
			ws[4].setNaSpoor(spoor6);
			ws[5].setZijSpoor(sSpoor3);
			ws[5].setNaSpoor(spoor7);
			ws[6].setZijSpoor(sSpoor4);
			ws[6].setNaSpoor(spoor8);
			ws[7].setZijSpoor(sSpoor4);
			ws[7].setNaSpoor(spoor9);
			ws[8].setZijSpoor(sSpoor5);
			ws[8].setNaSpoor(spoor10);
			ws[9].setZijSpoor(sSpoor5);
			ws[9].setNaSpoor(spoor11);
			ws[10].setZijSpoor(sSpoor6);
			ws[10].setNaSpoor(spoor12);
			ws[11].setZijSpoor(sSpoor6);
			ws[11].setNaSpoor(spoor13);

			setSporen(spoor1);
			System.out.println("Baanstructuur aangemaakt!");
			return true;
		case 7:
			station1 = new Station(1, "Lelylaan", 450);
			station2 = new Station(2, "Sloterdijk", 450);
			station3 = new Station(3, "AmsterdamCS", 450);
			station4 = new Station(4, "Muiderpoort", 450);
			station5 = new Station(5, "ZuidWTC", 450);
			station6 = new Station(6, "RAI", 450);
			station7 = new Station(7, "Duivendrecht", 450);
			stations.add(station1);
			stations.add(station2);
			stations.add(station3);
			stations.add(station4);
			stations.add(station5);
			stations.add(station6);
			stations.add(station7);

			spoor1 = new Spoor("HS1", 500);
			spoor1.setNaSpoor(ws[0]);

			spoor2 = new Spoor("HS2", 900, ws[0], ws[1]);
			sSpoor1 = new Stationspoor("SS1", 900, ws[0], ws[1], station1);
			spoor3 = new Spoor("HS3", 2000, ws[1], ws[2]);
			spoor4 = new Spoor("HS4", 900, ws[2], ws[3]);
			sSpoor2 = new Stationspoor("SS2", 900, ws[2], ws[3], station2);
			spoor5 = new Spoor("HS5", 2000, ws[3], ws[4]);
			spoor6 = new Spoor("HS6", 900, ws[4], ws[5]);
			sSpoor3 = new Stationspoor("SS3", 900, ws[4], ws[5], station3);
			spoor7 = new Spoor("HS7", 2000, ws[5], ws[6]);
			spoor8 = new Spoor("HS8", 900, ws[6], ws[7]);
			sSpoor4 = new Stationspoor("SS4", 900, ws[6], ws[7], station4);
			spoor9 = new Spoor("HS9", 2000, ws[7], ws[8]);
			spoor10 = new Spoor("HS10", 900, ws[8], ws[9]);
			sSpoor5 = new Stationspoor("SS5", 900, ws[8], ws[9], station5);
			spoor11 = new Spoor("HS11", 5000, ws[9], ws[10]);
			spoor12 = new Spoor("HS12", 900, ws[10], ws[11]);
			sSpoor6 = new Stationspoor("SS6", 900, ws[10], ws[11], station6);
			spoor13 = new Spoor("HS13", 2000, ws[11], ws[12]);
			spoor14 = new Spoor("HS14", 900, ws[12], ws[13]);
			sSpoor7 = new Stationspoor("SS7", 900, ws[12], ws[13], station7);
			spoor15 = new Spoor("HS15", 1500, ws[13], spoor1);

			ws[0].setZijSpoor(sSpoor1);
			ws[0].setNaSpoor(spoor2);
			ws[1].setZijSpoor(sSpoor1);
			ws[1].setNaSpoor(spoor3);
			ws[2].setZijSpoor(sSpoor2);
			ws[2].setNaSpoor(spoor4);
			ws[3].setZijSpoor(sSpoor2);
			ws[3].setNaSpoor(spoor5);
			ws[4].setZijSpoor(sSpoor3);
			ws[4].setNaSpoor(spoor6);
			ws[5].setZijSpoor(sSpoor3);
			ws[5].setNaSpoor(spoor7);
			ws[6].setZijSpoor(sSpoor4);
			ws[6].setNaSpoor(spoor8);
			ws[7].setZijSpoor(sSpoor4);
			ws[7].setNaSpoor(spoor9);
			ws[8].setZijSpoor(sSpoor5);
			ws[8].setNaSpoor(spoor10);
			ws[9].setZijSpoor(sSpoor5);
			ws[9].setNaSpoor(spoor11);
			ws[10].setZijSpoor(sSpoor6);
			ws[10].setNaSpoor(spoor12);
			ws[11].setZijSpoor(sSpoor6);
			ws[11].setNaSpoor(spoor13);
			ws[12].setZijSpoor(sSpoor7);
			ws[12].setNaSpoor(spoor14);
			ws[13].setZijSpoor(sSpoor7);
			ws[13].setNaSpoor(spoor15);

			setSporen(spoor1);
			System.out.println("Baanstructuur aangemaakt!");
			return true;
		case 8:
			station1 = new Station(1, "Amstel", 450);
			station2 = new Station(2, "Lelylaan", 450);
			station3 = new Station(3, "Sloterdijk", 450);
			station4 = new Station(4, "AmsterdamCS", 450);
			station5 = new Station(5, "Muiderpoort", 450);
			station6 = new Station(6, "ZuidWTC", 450);
			station7 = new Station(7, "RAI", 450);
			station8 = new Station(8, "Duivendrecht", 450);
			stations.add(station1);
			stations.add(station2);
			stations.add(station3);
			stations.add(station4);
			stations.add(station5);
			stations.add(station6);
			stations.add(station7);
			stations.add(station8);

			spoor1 = new Spoor("HS1", 500);
			spoor1.setNaSpoor(ws[0]);

			spoor2 = new Spoor("HS2", 900, ws[0], ws[1]);
			sSpoor1 = new Stationspoor("SS1", 900, ws[0], ws[1], station1);
			spoor3 = new Spoor("HS3", 2000, ws[1], ws[2]);
			spoor4 = new Spoor("HS4", 900, ws[2], ws[3]);
			sSpoor2 = new Stationspoor("SS2", 900, ws[2], ws[3], station2);
			spoor5 = new Spoor("HS5", 2000, ws[3], ws[4]);
			spoor6 = new Spoor("HS6", 900, ws[4], ws[5]);
			sSpoor3 = new Stationspoor("SS3", 900, ws[4], ws[5], station3);
			spoor7 = new Spoor("HS7", 2000, ws[5], ws[6]);
			spoor8 = new Spoor("HS8", 900, ws[6], ws[7]);
			sSpoor4 = new Stationspoor("SS4", 900, ws[6], ws[7], station4);
			spoor9 = new Spoor("HS9", 2000, ws[7], ws[8]);
			spoor10 = new Spoor("HS10", 900, ws[8], ws[9]);
			sSpoor5 = new Stationspoor("SS5", 900, ws[8], ws[9], station5);
			spoor11 = new Spoor("HS11", 2000, ws[9], ws[10]);
			spoor12 = new Spoor("HS12", 900, ws[10], ws[11]);
			sSpoor6 = new Stationspoor("SS6", 900, ws[10], ws[11], station6);
			spoor13 = new Spoor("HS13", 2000, ws[11], ws[12]);
			spoor14 = new Spoor("HS14", 900, ws[12], ws[13]);
			sSpoor7 = new Stationspoor("SS7", 900, ws[12], ws[13], station7);
			spoor15 = new Spoor("HS15", 2000, ws[13], ws[14]);
			spoor16 = new Spoor("HS16", 900, ws[14], ws[15]);
			sSpoor8 = new Stationspoor("SS8", 900, ws[14], ws[15], station8);
			spoor17 = new Spoor("HS17", 1500, ws[15], spoor1);

			ws[0].setZijSpoor(sSpoor1);
			ws[0].setNaSpoor(spoor2);
			ws[1].setZijSpoor(sSpoor1);
			ws[1].setNaSpoor(spoor3);
			ws[2].setZijSpoor(sSpoor2);
			ws[2].setNaSpoor(spoor4);
			ws[3].setZijSpoor(sSpoor2);
			ws[3].setNaSpoor(spoor5);
			ws[4].setZijSpoor(sSpoor3);
			ws[4].setNaSpoor(spoor6);
			ws[5].setZijSpoor(sSpoor3);
			ws[5].setNaSpoor(spoor7);
			ws[6].setZijSpoor(sSpoor4);
			ws[6].setNaSpoor(spoor8);
			ws[7].setZijSpoor(sSpoor4);
			ws[7].setNaSpoor(spoor9);
			ws[8].setZijSpoor(sSpoor5);
			ws[8].setNaSpoor(spoor10);
			ws[9].setZijSpoor(sSpoor5);
			ws[9].setNaSpoor(spoor11);
			ws[10].setZijSpoor(sSpoor6);
			ws[10].setNaSpoor(spoor12);
			ws[11].setZijSpoor(sSpoor6);
			ws[11].setNaSpoor(spoor13);
			ws[12].setZijSpoor(sSpoor7);
			ws[12].setNaSpoor(spoor14);
			ws[13].setZijSpoor(sSpoor7);
			ws[13].setNaSpoor(spoor15);
			ws[14].setZijSpoor(sSpoor8);
			ws[14].setNaSpoor(spoor16);
			ws[15].setZijSpoor(sSpoor8);
			ws[15].setNaSpoor(spoor17);

			setSporen(spoor1);
			System.out.println("Baanstructuur aangemaakt!");
			return true;
		}
		return false;
	}

	/** Maakt onbezette taxi's aan die verdeelt worden over de beschikbare stations. 
	 * De observer wordt toegevoegd aan de taxi's, maar een null waarde kan als argument bij de observer parameter worden meegegeven wanneer dit niet gewenst is.
	 * @param aantal Aantal taxi's om aan te maken.
	 * @param observer De observer dat de aan de taxi's wordt toegevoegd
	 * @throws RuntimeException Wanneer er geen stations beschikbaar zijn
	 */
	public synchronized void maakTaxis(int aantal, Observer observer) throws RuntimeException {	
            if (stations.isEmpty()) throw new RuntimeException("Geen stations beschikbaar om taxi's te plaatsen");
		for (int i = 0; i < aantal; i++) {
			Station s = stations.get(i % stations.size());
			taxiTeller++;
			Taxi t;
			if (observer == null) t = new Taxi(this, taxiTeller, "Taxi " + taxiTeller, s);
			else t = new Taxi(this, taxiTeller, "Taxi " + taxiTeller, s, observer);
			voegOnbezetteTaxi(t, s);
		}
	}

	/** Geeft het station met de opgegeven ID terug.
	 * @param ID De ID nummer van het station
	 * @return Station met de opgegeven ID
	 * @throws RuntimeException Wanneer het station met de opgegeven ID niet gevonden kan worden
	 */
	public Station getStation(int ID) throws RuntimeException {
		for (Station s : stations) {
			if (s.getID() == ID)
				return s;
		}
		throw new RuntimeException("Geen station gevonden met ID: " + ID);
	}

	/** Geeft het aantal taxi's terug.
	 * @return aantal taxi's
	 */
	public int getAantalTaxis() {
		return taxis.size();
	}

	/** Geeft het aantal stations terug.
	 * @return aantal stations
	 */
	public int getAantalStations() {
		return stations.size();
	}

	/** Controleert op aanrijdingen met voorliggende taxi's. 
	 * Als een taxi op de hoofdbaan zit dan worden voorliggende taxi's tot een bepaalde afstand gecontroleerd.
	 * Als een taxi op het station zit dan worden taxi's  op de tussenspoor en wisselsporen gecontroleerd.
	 * @param taxi Taxi waarvan voorliggende taxi's voor gecontroleerd moet worden.
	 * @return Is waar als een mogelijke aanrijding is gevonden en anders onwaar.
	 */
	public boolean aanrijding(Taxi taxi) {
		if (taxi.opSpoor()) {
			if (taxi.getStatus() == TaxiStatus.RIJD || taxi.getStatus() == TaxiStatus.WACHT_OP_SPOOR) {
				if (taxi.opHoofdSpoor()) {
					double restAfstand = (taxi.getSpoorPositie() + MIN_AFSTAND_TOT_TAXI) - taxi.getSpoor().getLengte();
					if (restAfstand <= 0) {
						if (bevatTaxis(taxi.getSpoor(), taxi.getSpoorPositie(), taxi.getSpoor(), taxi.getSpoorPositie() + MIN_AFSTAND_TOT_TAXI)) return true;
					}
					else if (restAfstand > 0) {
						if (restAfstand < taxi.getSpoor().volgende().getLengte()) {
							if (bevatTaxis(taxi.getSpoor(), taxi.getSpoorPositie(), taxi.getSpoor().volgende(), restAfstand)) return true;
						}
						else 
							if (bevatTaxis(taxi.getSpoor(), taxi.getSpoorPositie(), taxi.getSpoor().volgende(), taxi.getSpoor().volgende().getLengte())) return true;
					}
				}
			}
		}
		else if (taxi.opStation()) {
			Stationspoor sSpoor = taxi.getStation().getSpoor();
			if (bevatTaxis(sSpoor, sSpoor.getStation().getPositie(), sSpoor.volgende(), sSpoor.volgende().getLengte())) return true;
			if (bevatTaxis(sSpoor.vorige(), 0, sSpoor.volgende(), sSpoor.volgende().getLengte())) return true;
		}
		return false;
	}

	/** Schakelt de eerstvolgende wisselspoor voor de opgegeven taxi. Schakelt de wisselspoor terug als een taxi de wisselspoor voorbij is.
	 * @param taxi De taxi waarvoor de wisselspoor (terug)geschakeld moet worden
	 * @return Is waar als de wisselspoor voor de taxi is (terug)geschakeld en onwaar als er niets is geschakeld
	 */
	public boolean wisselaar(Taxi taxi) {
		if (wisselLijst.containsKey(taxi)) {
			Wisselspoor wSpoor = wisselLijst.get(taxi);
			if (wSpoor.volgende().bevatTaxi(taxi)) {
				wSpoor.schakel();
				wisselLijst.remove(taxi);
				System.out.println("Wissel: " + wSpoor.getID() + " is teruggeschakeld door Taxi: " + taxi.getNaam());
				return true;
			}
		}
		else {
			Spoor spoor;
			if (taxi.opSpoor()) spoor = taxi.getSpoor();
			else spoor = taxi.getStation().getSpoor();
			if (spoor.volgende().getType() == SpoorType.WISSEL_SPOOR) {
				Wisselspoor wSpoor = (Wisselspoor) spoor.volgende();
				if (!wisselLijst.containsValue(wSpoor)) {
					wSpoor.schakel();
					wisselLijst.put(taxi, wSpoor);
					System.out.println("Wissel: " + wSpoor.getID() + " is geschakeld door Taxi: " + taxi.getNaam());
					return true;
				}
			}
		}
		return false;
	}

	/** Controleert of taxi's op de tussenspoor van een station rijden of wachten.
	 * @param station Het station waarvan de tussenspoor op taxi's gecontroleerd moet worden.
	 * @return is waar als er taxi's op de tussenspoor rijden/wachten en anders onwaar.
	 */
	public boolean taxisTussenSpoor(Station station) {
		if (station.getSpoor().vorige().getNaSpoor().bevatTaxis()) return true;
		else return false;
	}

	/** Berekent afstand vanaf de positie van de taxi tot het station.
	 * @param t Taxi waar de afstand vanaf moet worden berekent.
	 * @param station Station waar de afstand tot moet worden berekent.
	 * @return afstand tussen de taxi en het station.
	 */
	public long afstand(Taxi t, Station station) {
		if (t.opStation()) {
			return bepaalAfstand(t.getStation(), station);
		}
		else if (t.opSpoor()) {
			long afstand = 0;
			boolean begin = false;
			Spoor taxiSpoor = t.getSpoor();
			for (Spoor s : sporen) {
				if (s == taxiSpoor) {
					afstand += s.getLengte() - t.getSpoorPositie();
					begin = true;
				}
				else if (begin) {
					if (s.getType() == SpoorType.WISSEL_SPOOR) {
						Wisselspoor wSpoor = (Wisselspoor) s;
						if (getStation(wSpoor) == station) {
							afstand += wSpoor.getLengte();
							afstand += wSpoor.getZijSpoor().getLengte() - ((Stationspoor)wSpoor.getZijSpoor()).getStation().getPositie();
							return afstand;
						}
					}
					else if (s.getType() == SpoorType.SPOOR) {
						afstand += s.getLengte();
					}
				}
			}

		}
		return 0;
	}

	/** Berekent de afstand vanaf het begin van de baan tot aan de wisselspoor van het station.
	 * @param station Station waaraan de wisselspoor gekoppeld is.
	 * @return afstand tot het wisselspoor.
	 */
	public long afstandTotZijSpoor(Station station) {
		long afstand = 0;
		for (Spoor s : sporen) {
			if (s.getType() == SpoorType.WISSEL_SPOOR) {
				if (getStation((Wisselspoor)s) == station) break;
				else afstand += s.getLengte();
			}
			else if (s.getType() == SpoorType.SPOOR) afstand += s.getLengte();
		}
		return afstand;
	}

	/** Geeft waar als taxi's vanaf de positie op het eerste spoor en tot de positie op het laatste spoor rijden of wachten en anders onwaar. Deze methode kan ook binnen ��n spoor controleren.
	 * @param s1 Eerste spoor
	 * @param van Beginpositie van de eerste spoor
	 * @param s2 Laatste spoor
	 * @param tot Eindpositie van de laatste spoor
	 * @return Is waar als een taxi is gevonden en anders onwaar.
	 */
	public boolean bevatTaxis(Spoor s1, double van, Spoor s2, double tot) {
		if (s1 == s2) {
			for (Taxi t : s1.getTaxis())
				if (t.getSpoorPositie() > van && t.getSpoorPositie() < tot) return true;
		}
		else {
			for (Spoor s : s1) {
				for (Taxi t : s.getTaxis()) {
					if (s == s1) { if (t.getSpoorPositie() > van) return true; }
					else if (s == s2) { if (t.getSpoorPositie() < tot) return true; }
					else return true;
				}
				if (s == s2) break;
			}
		}
		return false;
	}

	/** Geeft een station terug aan de hand van een wisselspoor.
	 * @param spoor Wisselspoor dat naar een station leidt.
	 * @return station waarnaar de opgegeven wisselspoor leidt.
	 */
	public Station getStation(Wisselspoor spoor) {
		return ((Stationspoor)spoor.getZijSpoor()).getStation();
	}

	/** Geeft aan of er voorliggende taxi's op hetzelfde spoor als de opgegeven taxi zijn.
	 * @param taxi Taxi
	 * @return is waar als een voorliggende taxi is gevonden en anders onwaar
	 */
	public boolean bevatTaxis(Taxi taxi) {
		for (Taxi t : taxi.getSpoor().getTaxis())
			if (t.getSpoorPositie() >= taxi.getSpoorPositie() && t != taxi) return true;
		return false;
	}

	/** Geeft de wisselspoor die door de taxi is geschakeld.
	 * @param taxi Taxi dat een wissel heeft geschakeld
	 * @return de geschakelde wisselspoor
	 */
	public Wisselspoor getGeschakeldeWisselspoor(Taxi taxi) {
		return wisselLijst.get(taxi);
	}

	/** Geeft aan of een wisselspoor door de taxi is geschakeld.
	 * @param taxi - taxi
	 * @return Is waar als een wisselspoor door de taxi is geschakeld en anders onwaar.
	 */
	public boolean isGeschakeldVoor(Taxi taxi) {
		return wisselLijst.containsKey(taxi);
	}

	/** Geeft het eerstvolgende wisselspoor terug na de opgegeven spoor.
	 * @param huidigeSpoor Huidige spoor van een taxi.
	 * @return eerstvolgende wisselspoor
	 */
	public Wisselspoor getVolgendeWisselspoor(Spoor huidigeSpoor) {
		for (Spoor s : huidigeSpoor) {
			if (s.getType() == SpoorType.WISSEL_SPOOR) return (Wisselspoor)s;
		}
		return null;
	}

	/** Geeft de tijd (in milliseconden) dat taxi's op een station moeten wachten.
	 * @return wachttijd van taxi's op een station.
	 */
	public long getWachtTijdStation() {
		return wachtTijdStation;
	}

	/** Stelt de tijd in dat taxi's op een station moeten wachten.
	 * @param wachtTijdStation Wachttijd (in milliseconden) op een station
	 */
	public void setWachtTijdStation(int wachtTijdStation) {
		this.wachtTijdStation = wachtTijdStation;
	}

	/** Geeft de snelheid (in meters per seconde) van taxi's
	 * @return snelheid van taxi's
	 */
	public long getTaxiSnelheid() {
		return taxiSnelheid;
	}

	/** Stelt de snelheid van taxi's in.
	 * @param taxiSnelheid Snelheid (in meters per seconde) van taxi's
	 */
	public void setTaxiSnelheid(int taxiSnelheid) {
		this.taxiSnelheid = taxiSnelheid;
	}

	/** Geeft de taxi met de opgegeven ID nummer terug.
	 * @param ID - ID nummer van een taxi
	 * @return taxi met de betreffende ID
	 * @throws NoSuchElementException Wanneer geen taxi met de opgegeven ID nummer is gevonden
	 */
	public Taxi getTaxi(int ID) throws NoSuchElementException {
		for (Taxi t : taxis) {
			if (t.getID() == ID) return t;
		}
		throw new NoSuchElementException("Geen taxi met de ID: " + ID + " gevonden!");
	}

	/** Geeft de station met de opgegeven naam terug.
	 * @param naam Naam van een taxi
	 * @return taxi met de betreffende naam.
	 * @throws NoSuchElementException wanneer geen taxi met de opgegeven naam is gevonden
	 */
	public Taxi getTaxi(String naam) throws NoSuchElementException {
		for (Taxi t : taxis) {
			if (t.getNaam().equals(naam)) return t;
		}
		throw new NoSuchElementException("Geen taxi met de naam: " + naam + " gevonden!");
	}
}
