package org.tigam.railcab.algoritme;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Mustapha Bouzaidi
 *
 */
public class Simulatie implements Serializable, Observer {
	private final static Simulatie INSTANTIE = new Simulatie();
	/**
	 * 
	 */
	private static final long serialVersionUID = -7046255051548718419L;
	private BaanManager baanManager;
	private ReisManager reisManager;
	private transient Thread runner;
	private ArrayList<Reis> voltooideReizen;
	private long taxiBezettingen, wachtTijden, reisTijden;
	private int taxiBezettingsAantal, wachtTijdenAantal, reisTijdenAantal;

	private Simulatie() {
		voltooideReizen = new ArrayList<Reis>();
		baanManager = new BaanManager();
		reisManager = new ReisManager(baanManager);
	}

	/** Geeft de instantie van de Simulatie klasse terug. Gebruikt de singleton pattern.
	 * @return Instantie van simulatie
	 */
	public static Simulatie getInstantie() {
		return INSTANTIE;
	}

	public static void main(String args[]) {
		Simulatie simulatie = Simulatie.getInstantie();
		//BaanManager baanManager = simulatie.getBaanManager();
		//ReisManager reisManager = simulatie.getReisManager();
		simulatie.getBaanManager().maakStations(5);
		simulatie.getBaanManager().maakTaxis(2, simulatie);
		//simulatie.getBaanManager().voegOnbezetteTaxi(new Taxi(simulatie.getBaanManager(), 1, "Taxi 1", simulatie.getBaanManager().getStation("ZuidWTC")), simulatie.getBaanManager().getStation("ZuidWTC"));
		//simulatie.getBaanManager().voegOnbezetteTaxi(new Taxi(simulatie.getBaanManager(), 2, "Taxi 2", simulatie.getBaanManager().getStation("ZuidWTC")), simulatie.getBaanManager().getStation("ZuidWTC"));
		//simulatie.getBaanManager().voegOnbezetteTaxi(new Taxi(simulatie.getBaanManager(), 3, "Taxi 3", simulatie.getBaanManager().getStation("ZuidWTC")), simulatie.getBaanManager().getStation("ZuidWTC"));
		//simulatie.getBaanManager().voegOnbezetteTaxi(new Taxi(simulatie.getBaanManager(), 4, "Taxi 4", simulatie.getBaanManager().getStation("ZuidWTC")), simulatie.getBaanManager().getStation("ZuidWTC"));
		//simulatie.getBaanManager().voegOnbezetteTaxi(new Taxi(simulatie.getBaanManager(), 5, "Taxi 5", simulatie.getBaanManager().getStation("ZuidWTC")), simulatie.getBaanManager().getStation("ZuidWTC"));
		try {
			simulatie.start();
			for (int i = 0; i < 10; i++) simulatie.getReisManager().addReiziger(simulatie.getBaanManager().getStation("ZuidWTC"), simulatie.getBaanManager().getStation("RAI"));
			Thread.sleep(5000);
			//for (int i = 0; i < 10; i++) simulatie.getReisManager().addReiziger(simulatie.getBaanManager().getStation("RAI"), simulatie.getBaanManager().getStation("ZuidWTC"));
			//for (int i = 0; i < 6; i++) simulatie.getReisManager().addReiziger(simulatie.getBaanManager().getStation("Duivendrecht"), simulatie.getBaanManager().getStation("RAI"));
			//Thread.sleep(500);
			//for (int i = 0; i < 8; i++) simulatie.getReisManager().addReiziger(simulatie.getBaanManager().getStation("Duivendrecht"), simulatie.getBaanManager().getStation("ZuidWTC"));
			//Thread.sleep(1000);
			// simulatie.versnel();
			//while (true) {
			//	Thread.sleep(70000);
			//	System.out.println(simulatie.gemiddeldeWachtTijd());
				simulatie.pauzeer();
				Thread.sleep(10000);
				simulatie.hervat();
			//}
			//Thread.sleep(5000);
			//simulatie.stop();
			//simulatie.opslaan("test.dat");
			//Thread.sleep(5000);
			//simulatie.getBaanManager().maakStations(3);
			//simulatie.getBaanManager().maakTaxis(2, simulatie);
			//0for (int i = 0; i < 6; i++) simulatie.getReisManager().addReiziger(simulatie.getBaanManager().getStation("RAI"), simulatie.getBaanManager().getStation("ZuidWTC"));
			//simulatie.start();
			//simulatie.laden("test.dat");
			//Thread.sleep(5000);
			//simulatie.getBaanManager().maakTaxis(2, simulatie);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** Geeft de baanmanager van de simulatie terug.
	 * @return Baanmanager in de simulatie
	 */
	public synchronized BaanManager getBaanManager() {
		return baanManager;
	}

	/** Geeft de reismanager van de simulatie terug.
	 * @return Reismanager in de simulatie
	 */
	public synchronized ReisManager getReisManager() {
		return reisManager;
	}

	/** Voegt de reis aan de lijst met voltooide reizen. Gebruikt vervolgens de reistijd, wachttijden van de reizigers en het aantal reizigers in de reis object om de gemiddelde statistieken aan te vullen.
	 * @param reis Voltooide reis
	 */
	public void voegReis(Reis reis) {
		voltooideReizen.add(reis);

		taxiBezettingen += (reis.getAantalReizigers() / ReisManager.MAX_REIZIGERS_IN_TAXI) * 100;
		taxiBezettingsAantal++;

		for (Reiziger r : reis.getReizigers()) {
			wachtTijden += r.getWachtTijd();
			wachtTijdenAantal++;
		}

		reisTijden += reis.getReisTijd();
		reisTijdenAantal++;
	}

	/** Geeft de gemiddelde taxi bezetting (gemiddeld aantal reizigers in taxi) terug.
	 * @return Taxibezetting
	 */
	public synchronized long gemiddeldeTaxiBezetting() {
		if (taxiBezettingsAantal > 0) return taxiBezettingen / taxiBezettingsAantal;
		else return 0;
	}

	/** Geeft de gemiddelde wachttijd van reizigers terug.
	 * @return Gemiddelde wachttijd van reizigers.
	 */
	public synchronized long gemiddeldeWachtTijd() {
		if (wachtTijdenAantal > 0) return wachtTijden / wachtTijdenAantal;
		else return 0;
	}

	/** Geeft de gemiddelde reistijd van reizen.
	 * @return Gemiddelde reistijd van voltooide reizen
	 */
	public synchronized long gemiddeldeReisTijd() {
		if (reisTijdenAantal > 0) return reisTijden / reisTijdenAantal;
		else return 0;
	}

	/**
	 * Start de simulatie door de reismanager aan een nieuwe thread te koppelen en te starten.
	 */
	public synchronized void start() {
		runner = new Thread(reisManager);
		reisManager.setStatus(ReisManager.START);
		runner.start();
	}

	/**
	 * Stopt de simulatie. Status van de reismanager wordt op stop gezet en er wordt gewacht tot de thread is uitgestorven.
	 */
	public synchronized void stop() {
		reisManager.setStatus(ReisManager.STOP);
		while (runner.isAlive()) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		baanManager = new BaanManager();
		reisManager = new ReisManager(baanManager);

		System.out.println("Simulatie gestopt!");
	}

	/**
	 * Pauzeert de simulatie.
	 */
	public void pauzeer() {
		synchronized (runner) { reisManager.pauzeer(); }
		System.out.println("Gepauzeerd");
	}

	/**
	 * Hervat de simulatie.
	 */
	public void hervat() {
		synchronized (reisManager) {
		reisManager.hervat();
		reisManager.notify();
		}
		System.out.println("Hervat");
	}

	/**
	 * Simulatie wordt versneld of genormaliseerd. 
	 */
	public synchronized void versnel() {
		if (ReisManager.getVersnelFactor() <= 1) ReisManager.setVersnelFactor(5);
		else ReisManager.setVersnelFactor(1);
	}

	/** Laad een eerder opgeslagen simulatie d.m.v. serialisatie. 
	 * De huidige simulatie moet hierbij wel gepauzeerd zijn.
	 * Gebruikt een ObjectInptutStream in combinatie met een FileInputStream.
	 * @param bestand Bestandsnaam (en eventueel een pad) van een opgeslagen bestand
	 */
	public synchronized void laden(String bestand) {
		if (reisManager.getStatus() == ReisManager.PAUZE) {
			ObjectInputStream objectInputStream;
			try {
				objectInputStream = new ObjectInputStream(new FileInputStream(bestand));
				reisManager = null;
				baanManager = null;
				reisManager = (ReisManager) objectInputStream.readObject();
				baanManager = reisManager.getBaanManager();
				objectInputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			runner = new Thread(reisManager);
			runner.start();
		}
	}

	/** Slaat een lopende simulatie op naar de opgegeven bestand d.m.v. serialisatie. 
	 * De simulatie wordt tijdelijk gepauzeerd om de simulatie naar het bestand te schrijven. 
	 * Gebruikt een ObjectOutputStream in combinatie met een FileOutPutStream.
	 * @param bestand Bestandsnaam (en eventueel een pad) waarnaar de simulatie weggeschreven moet worden.
	 */
	public synchronized void opslaan(String bestand) {
		ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new ObjectOutputStream(new FileOutputStream(bestand));
			pauzeer();
			objectOutputStream.writeObject(reisManager);
			objectOutputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hervat();
	}

	public void update(Observable arg0, Object arg1) {
		Taxi t = (Taxi) arg0;
		System.out.println("Observer geupdate, " + t.getNaam());
	}
}
