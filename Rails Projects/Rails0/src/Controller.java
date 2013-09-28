import java.util.ArrayList;
import java.util.Observer;
import java.util.StringTokenizer;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.tigam.railcab.algoritme.*;


/**
 * @author dareal
 *Deze klasse verzorgd het communicatie tussen het algoritme en de UserInterface
 */
public class Controller {
	private int meters, wissel_sp, km, pixel_x, pixel_y;
	private double pixels, afst_stations, bocht, station;
	private String statistieken, ReizigersOpstation;

	private UserInterface userInterface;
	private ArrayList<String> reiziger;
	private BaanManager baanManager;
	private ReisManager reisManager;
	private Simulatie simulatie;
	private TaxiStats taxiStats;
	private TekenSpoor tekenSpoor;
	private Taxi taxi;

	//stations attributten
	private String station1, station2, station3, station4, station5, station6,
			station7, station8;

	//attibuten voor taxistats
	private int aantal, r_inT, wT, rT, gB, g_wT, g_rT, aantalTaxi = 0;

	//attributen voor stationstats

	private int r_station1, r_station2, r_station3, r_station4, r_station5,
			r_station6, r_station7, r_station8, iterator;
	private String vertrek, bestemming, pos;
	private Paneel paneel;

	public Controller() {
		simulatie = Simulatie.getInstantie();
		baanManager = simulatie.getBaanManager();
		reisManager = simulatie.getReisManager();
		tekenSpoor = new TekenSpoor();
	}

	public Controller(Paneel paneel) {

		//1 pixel = 12.5 meters

		this.wissel_sp = 50; //meters
		this.meters = 24000; //meters
		this.pixels = 12.5; //m/pixel
		this.afst_stations = 62.5 * 13; //pixels
		this.bocht = 13 * 62.5; //pixels
		this.station = 10 * 62.5; //pixels
		simulatie = Simulatie.getInstantie();
		baanManager = simulatie.getBaanManager();
		reisManager = simulatie.getReisManager();
		this.paneel = paneel;

	}

	/**
	 * Deze methode voegd de reizigers toe aan de algoritme d.m.v. een forloop
	 * */
	public void voegReizigerToe(int aantal, String vertrek, String bestemming) {
		for (int i = 0; i < aantal; i++) {
			reisManager.addReiziger(baanManager.getStation(vertrek),
					baanManager.getStation(bestemming));
		}
	}

	/**
	 * Deze methode stelt de snelheid in voor de taxi. Hij zet de km om naar meters per seconde
	 * */
	public void setSnelheid(double snelheid) {
		System.out.println("Ingevoerde snelheid: " + snelheid + "KM/h\n");
		snelheid = snelheid / 3.6;
		try {
			baanManager.setTaxiSnelheid((int) snelheid);
			//doorsturen naar ReisManager
		} catch (Exception e) {
			System.out.println(" de ingevoerde snelheid " + snelheid
					+ " komt niet aan");
		}
	}

	/**
	 * Deze methode wijzig de reizigers taxi instaptijd.
	 * */
	public void setWachttijd(int wachttijd) {
		System.out.println(" de wachttijd is" + wachttijd);
		wachttijd = wachttijd * 1000;
		System.out.println(wachttijd);
		try {
			baanManager.setWachtTijdStation(wachttijd);
			//doorsturen naar ReisManager
		} catch (Exception e) {
			System.out.println(" de wachtijd  " + wachttijd + " komt niet aan");
		}
	}

	/**
	 * Deze methode bereken de gemiddelde statistieken en geeft het terug in minuten en secondes
	 * */
	public String getGemStatistieken() {
		int gemReistijdSec = (int) (simulatie.gemiddeldeReisTijd() / 1000), gemReistijdMin = (int) (simulatie
				.gemiddeldeReisTijd() / 1000 / 60), gemiddelWachtijdMin = (int) (simulatie
				.gemiddeldeWachtTijd() / 1000 / 60), gemiddelWachtijdSec = (int) (simulatie
				.gemiddeldeWachtTijd() / 1000);
		long gemiddelBezetting = simulatie.gemiddeldeTaxiBezetting();
		statistieken = "Gemiddelde TaxiBezetting: " + gemiddelBezetting + " %"
				+ "\n" + "Gemiddelde WachtTijd: " + gemiddelWachtijdMin + "."
				+ secondeCheck(gemiddelWachtijdSec) + " Minuten" + "\n"
				+ "Gemiddelde ReisTijd: " + gemReistijdMin + "."
				+ secondeCheck(gemReistijdSec) + " Minuten";

		return statistieken;
	}

	public int versnelTimer() {
		int reken = 1000 / ReisManager.getVersnelFactor();
		return reken;
	}

	/**
	 * Deze methode versnelt de simulatie
	 * 
	 * */
	public void setVersnelSimulatie(int i) {
		if (i == 1) {
			simulatie.versnel();
			versnelTimer();
			paneel.VersnelOfNormaliceerTimer(versnelTimer());
		} else if (i == 0) {
			simulatie.versnel();
			paneel.VersnelOfNormaliceerTimer(1000);
		}
	}

	/**
	 * Deze methode start de simulatie op vanuit de algoritme
	 * */
	public void startSimulatie(int i) {
		if (i == 1) {

			baanManager = simulatie.getBaanManager();
			reisManager = simulatie.getReisManager();
			simulatie.start();
			System.out.println("simulatie start op");
		} else {
			simulatie.stop();
			System.out.println("simulatie stopt");

		}
	}

	/**
	 * Deze methode pauzeer of hervat de simulatie. Het wordt in de paneel klasse aangeroepen. 
	 * */
	public void pauzeerSimulatie(int i) {
		if (i == 1) {
			simulatie.pauzeer();
		} else {
			simulatie.hervat();
		}
	}

	/**
	 * Deze methode geeft het positie terug van een taxi. Een taxi is dan op een spoor of op een station
	 * */
	public String getPositie(int i) {
		if (baanManager.getTaxi(i).getStation() == null) {
			return "Op spoor";
		} else {
			return "Op station";

		}

	}

	/**
	 * Deze methode geeft het aantal reiziger terug die in een taxi bevinden
	 * */
	public int getReizInTaxi(int i) {
		try {
			return baanManager.getTaxi(i).getAantalReizigers();
		} catch (Exception e) {
			return 0;
		}

	}

	/**
	 * Deze methode zorgd ervoor dat de secondes aangegeven wordt als de standaard. dus van 1 tot 60
	 * */
	public int secondeCheck(int sec) {
		for (int i = 0; sec > 60; i++) {
			sec = sec - 60;
		}
		return sec;
	}

	/**
	 * Deze methode geeft de reistijd terug van een taxi. Tevens mmaakt hij gebruikt van een berekening die de milisecondes omzet
	 * in secondes en minuten. 
	 * */
	public String getReisTijd(int i) {

		try {
			int minuut = (int) (baanManager.getTaxi(i).getReis().getReisTijd() / 1000 / 60);
			int seconde = (int) (baanManager.getTaxi(i).getReis().getReisTijd() / 1000);
			return minuut + "." + secondeCheck(seconde);
		} catch (Exception e) {
			return "0";
		}
	}

	/**
	 * Deze methode geeft een station naam terug
	 * */

	public String getNaam(int i) {
		try {
			return baanManager.getStation(i).getNaam();
		} catch (Exception e) {
			return "Geen Station";
		}
	}

	/**
	 * Geeft de aantal reizigers op station
	 * */
	public int getA_reizigers(int i) {
		try {
			return baanManager.getStation(i).getAantalReizigers();
		} catch (Exception e) {
			return 0;
		}

	}

	/**
	 * Geeft de aantal taxi op een station terug.
	 * */
	public int getA_taxis(int i) {
		try {
			return baanManager.getStation(i).getAantalTaxis();
		} catch (Exception e) {
			return 0;
		}

	}

	public int getStaat(int a) {
		return a;
		// TODO Auto-generated method stub

	}

	/**
	 * Deze methode geeft alle station atributten een verwijzing naar een station naam. 
	 * */
	public void setStation(String nm) {

		if (station1 == null) {
			station1 = nm;
		} else if (station2 == null) {
			station2 = nm;
		} else if (station3 == null) {
			station3 = nm;
		} else if (station4 == null) {
			station4 = nm;
		} else if (station5 == null) {
			station5 = nm;
		} else if (station6 == null) {
			station6 = nm;
		} else if (station7 == null) {
			station7 = nm;
		} else if (station8 == null) {
			station8 = nm;
		}

	}

	/**
	 * Deze methode voegt de aantal station in de algritme toe
	 * */
	public void setStations(int aantalStation) {
		baanManager.maakStations(aantalStation);
		System.out
				.println("aantalStatation die naar baanmanager wordt gestuurd"
						+ aantalStation);
	}

	/**
	 * Deze methode voegt de aantal taxis toe in de algoritme. en teken ook de taxis op het UI.
	 * */
	public void setTaxi(int aantalTaxi) {
		System.out.println(" aantalTaxi In controller" + aantalTaxi);
                baanManager.maakTaxis(aantalTaxi, paneel);

		for (int b = 0; b < baanManager.getTaxis().size(); b++) {
                    Taxi taxi = baanManager.getTaxis().get(b);
			paneel.setInitLoc(taxi.getAbsolutePositie(), taxi.getZijPositie(), taxi.getID());
		}
	}

	/**
	 * Deze methode pas observable toe op de aantal taxis die toegevoegd worden vanuit de momentopname
	 * 
	 * */
	public void voegObservToe(int aantalTaxi) {
		ArrayList<Taxi> taxis = baanManager.getTaxis();
		for (Taxi t : taxis) {
			t.addObserver(paneel);
		}

	}

	/**
	 * Deze methode wordt aangeroepen om de taxi gegevens op te halen voor de UI. 
	 * Hij geeft de aantal taxis aan die in data.dat opgeslagen is zodat het in de UI teogevoegd 
	 * kan worden.
	 **/
	public int taxiGegevensOphalen() {
		simulatie.laden("data.dat");
		baanManager = Simulatie.getInstantie().getBaanManager();
		reisManager = Simulatie.getInstantie().getReisManager();

		return baanManager.getAantalTaxis();
	}

	/**
	 * Deze methode wordt aangeroepen om de stations gegevens op te halen voor de UI. 
	 * Hij geeft de aantal stations die in data.dat opgeslagen is zodat het in de UI teogevoegd 
	 * kan worden.
	 **/
	public int stationGegevensOphalen() {
		simulatie.laden("data.dat");
		baanManager = Simulatie.getInstantie().getBaanManager();
                reisManager = Simulatie.getInstantie().getReisManager();
		return baanManager.getStations().length;
	}

	/*Deze methode wordt gebruikt om een momentOpname op te slaan. De UI klasse roept dit aan.
	 **/
	public void momentOpnameOpslaan() {
		simulatie.opslaan("data.dat");
	}

	/**
	 *Deze methode geeft alle attributen een null/0 verwijzing. Dit wordt pas aangeroepen als je de simulatie stop.
	 *En is noodzakelijk zodat je de simulatie in een lege status kan opstarten vanuit de UI.
	 * */
	public void zetAllStationNull() {
		station1 = null;
		station2 = null;
		station3 = null;
		station4 = null;
		station5 = null;
		station6 = null;
		station7 = null;
		station8 = null;
		r_station1 = 0;
		r_station2 = 0;
		r_station3 = 0;
		r_station4 = 0;
		r_station5 = 0;
		r_station6 = 0;
		r_station7 = 0;
		r_station8 = 0;
	}

}
