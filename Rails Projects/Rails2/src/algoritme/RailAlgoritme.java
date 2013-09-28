package algoritme;

/*
 * @version 0.5
 */

import gui.*;

import java.util.*;
import java.sql.*;

import javax.swing.JFrame;

import business.*;
import simulatie.*;

public class RailAlgoritme extends Thread{
	private Frame frame;
	DbManager dbm;
	Simulatie simulatie;
	ArrayList<Reiziger> reizigers; 
	Datum datum;

	/* Main */
	public static void main(String[] args){
		new RailAlgoritme().start();
	}

	/* Constructor */
	public void debug( String tekst ){
		boolean print = false;
		if(print)
			System.out.println(tekst);
	}

	/**
	 *	Initialization of the object 
	 */
	public RailAlgoritme(){
		dbm = new DbManager("railcab", "railcab", "localhost", "railcab");

		simulatie = new Simulatie();
		//simulatie.start();
		reizigers = new ArrayList<Reiziger>();
		datum = new Datum();
		frame = new Frame(simulatie);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 820);
		frame.setTitle("Rails test");
		frame.setVisible(true);
	}
	
	/**
	 * if the thread is started
	 */
	public void run(){
		while(true){
			simulatie.run();
			slaap(50);
			maakRoute();  //
			
			updateRouteLijst(); // eerst personen verwijderen uit de routelijst die er al in staan
			spelRegels();
			slaap(50);
			
			//debug("loop");
		}
	}

	/**
	 * This method defines the rules of the algoritme.
	 */
	public void spelRegels(){
		Station station = simulatie.getStations();
		String eersteStation = simulatie.getStations().getStationNaam();
		station.getVoorSensors();

		do{
			boolean bezig = false;
			 //kijkt of een trein bij een station staat
			if(station.getStationSensor() > 0 ){
				for (Trein t: simulatie.getTreinen()){
					//als de trein niet rijdt en de sensorid is gelijk aan de treinid
					if( t.getTreinID() == station.getStationSensor() && !t.getIsRijden()){
						//Als er niemand rijdt tussen het station en de volgende sensor
						if( station.getStationSensorAfgehandeld() == 0){
							//kijk of er een trein vertrokken is vanaf de tussensensor
							if(station.getTussenWisselSensorAfgehandeld() == 0){
								//kijkt of er al een trein vertrokken is vanaf voorsensor
								if(station.getNextStation().getVoorSensors().get(0).getSensor().getSensorWaarde() == 0){
									boolean magRijden = true;
									if(station.getTussenWisselSensor() > 0)
										if( stationControle(station.getTussenWisselSensor()))
											magRijden = false;
									if( magRijden ){
										if(datum.getActueleDatumInSec() >= datum.addTijd(t.getAankomstTijd(), 5000)){									
											if(station.getAchterWisselStatus() == false) // kijkt of wissel goed staat
												station.setAchterWissel();
											t.gaRijden();
											bezig = true;
										}
									}
								}
							}
						}
					}
				}
			}
			if(!bezig){
				//kijkt of een trein bij een station staat
				if(station.getTussenWisselSensor() > 0 ){
					for (Trein t: simulatie.getTreinen()){
						//als de trein niet rijdt en de sensorid is gelijk aan de treinid
						if( t.getTreinID() == station.getTussenWisselSensor() && !t.getIsRijden()){
							//Als er niemand rijdt tussen het station en de volgende sensor
							if( station.getStationSensorAfgehandeld() == 0){
								//kijk of er een trein vertrokken is vanaf de tussensensor
								if(station.getTussenWisselSensorAfgehandeld() == 0){
									//kijkt of er al een trein vertrokken is vanaf voorsensor
									if(station.getNextStation().getVoorSensors().get(0).getSensor().getSensorWaarde() == 0){
										boolean magRijden = true;
										if(station.getStationSensor() > 0)
											if( stationControle(station.getStationSensor()))
												magRijden = false;
										if( magRijden ){
											if(station.getAchterWisselStatus() == true) // kijkt of wissel goed staat
												station.setAchterWissel();
											t.gaRijden();
										}
									}
								}
							}
						}
					}
				}
			}
			ArrayList<VoorSensor> vs = station.getVoorSensors();
			for( int i = 0; i < station.getVoorSensors().size(); i++ ){
				int sensorID = vs.get(i).getSensor().getSensorWaarde();// voorsensorwaarde van een element
				int sensorIDAfgehandeld = vs.get(i).getSensor().getAfgehandeld();
				int nextSensor = vs.get(i).getNextSensor(); // geeft de index van de volgende sensor in de array als er geen volgende is geeft hij 10

				if( sensorID > 0 ){
					for (Trein t: simulatie.getTreinen()){
						// als deze trein bij de sensor staat
						if( t.getTreinID() ==  sensorID && !t.getIsRijden()){ 
							if( nextSensor < 10 ){ //als het niet het laatste element in de array is
								if( sensorIDAfgehandeld == 0 ){
									if( vs.get(nextSensor).getSensor().getSensorWaarde() == 0 ){
										t.gaRijden();											
									}
								}
							}else {// bij het laatste element
								if( sensorIDAfgehandeld == 0 ){
									// kijk of de trein naar het station moet
									if(t.gaNaarStation(station.getStationNaam())){
										//System.out.println("Aantal treinen: " + station.isMaximumAantalTreinen());
										
										// als het maximum aantal treinen op het station nog niet is bereikt
										if(!station.isMaximumAantalTreinen() ){
											// zet de wissel hoog
											if(station.getVoorWisselStatus() == false)
												station.setVoorWissel();
											// ga rijden
											t.gaRijden();
										}
									}
									else{ // als de trein niet naar het station moet
										if(station.getTussenWisselSensor() == 0){
//											 zet de wissel hoog
											if(station.getVoorWisselStatus() == true)
												station.setVoorWissel();
											// ga rijden
											t.gaRijden();
										}
									}										
								}
							}
						}
					}
				}
			}
			station = station.getNextStation();
		}while(!station.getStationNaam().equalsIgnoreCase(eersteStation));
	}

	/**
	 * 
	 * @param treinID
	 * @return
	 */
	public boolean stationControle(int treinID){
		for(Trein t: simulatie.getTreinen()){
			if( t.getTreinID() == treinID ){
				if(t.getIsRijden())
					return true;
				else
					return false;
					
			}
		}
		return false;
	}
	
	/**
	 * adds travelers to the train and removes the travelers that have arrived
	 */
	public void updateRouteLijst(){
		Station station = simulatie.getStations();
		String eersteStation = station.getStationNaam();
		do{
			if( station.getStationSensor() > 0 ){ // als de sensorwaarde hoog is
				for (Trein t: simulatie.getTreinen()){
					if( t.getTreinID() ==  station.getStationSensor() && !t.getIsRijden()){ // als deze trein bij de sensor staat
//						verwijder de personen uit de routelijst die bij de bestemming zijn en laat de mensen instappen die de trein inmoeten.
						simulatie.deleteAantalPersonenInSysteem(t.updateRouteLijst(station.getStationNaam())); 
					}
				}
			}
			station = station.getNextStation();
		}while(!station.getStationNaam().equalsIgnoreCase(eersteStation));
	}

	/**
	 * adds routes to the routetable of a train.
	 */
	public void maakRoute(){
		ArrayList<Reiziger> removereiziger = new ArrayList<Reiziger>();
		Station station = simulatie.getStations();
		String eersteStation = station.getStationNaam();
		int sensorID ;
		do{
			sensorID = station.getVoorSensors().get(station.getVoorSensors().size()-1).getSensor().getSensorWaarde();
			if( sensorID > 0  ){ // als de sensorwaarde hoog is
				for (Trein t: simulatie.getTreinen()){
					if( t.getTreinID() ==  sensorID && !t.getIsRijden() && !t.isRouteLijstGeUpdate()){ // als deze trein bij de sensor staat
						removereiziger = new ArrayList<Reiziger>();
						long actueleTijd = datum.getActueleDatumInSec();
						addReizigers(dbm.getReizigers( datum.addTijd( actueleTijd, datum.versnelTijd(60000) )));  // haal de reizigers op
						for ( Reiziger r: reizigers){
//							kijk of de trein binnen 15 min bij het vertrekstation kan zijn.
							if( simulatie.getReistijd(station.getStationNaam(), r.getStartpunt()) < datum.versnelTijd(30000) ){
								if(datum.addTijd(datum.getActueleDatumInSec(), simulatie.getReistijd(station.getStationNaam(), r.getStartpunt())) >= r.getVertrektijd()){
	//								kijk of de trein vol zit.
									if( t.aantalZitplaatsenAt( station, r.getStartpunt()) < t.getMaxZitPlaatsen() ){
	//									kijk of er iets in de routeliist staat
										if(!t.getLastStation().equalsIgnoreCase("")){
	//							 			kijk of het vertrekstation verder is als het laatste record in de route tabel
											if(simulatie.vergelijkStations(t.getLastStation(), r.getStartpunt())){
	//										 	voeg reiziger toe.
												t.addRoute(  r.getStartpunt(), r.getBestemming(), r.getVertrektijd() );
	//										 	delete de Reiziger uit de ArrayList
												removereiziger.add(r);
											}
										}else{	
	//									 	voeg reiziger toe.
											t.addRoute(  r.getStartpunt(), r.getBestemming(), r.getVertrektijd() );
	//									 	delete de Reiziger uit de ArrayList
											removereiziger.add(r);
										}
									}
								}
							}
						}
//						verwijder de reizigers die in de routetabel zijn geplaatst 
						for(Reiziger r :removereiziger )
							reizigers.remove(r);
						t.routeLijstGeUpdate();
						t.testRoute();
					}
					
				}
			}
			station = station.getNextStation();
			
		}while(!station.getStationNaam().equalsIgnoreCase(eersteStation));
	}
	
	/**
	 * Adds travelers to the ArrayList
	 * The resultset is a list of Reizigers from the database
	 * @param rs
	 */
	public void addReizigers( ResultSet rs ){
		try {
			while(rs.next()){
				simulatie.addAantalPersonenInSysteem();
				reizigers.add(new Reiziger(rs.getString("Eindpunt"), rs.getString("Startpunt"), rs.getLong("VertrekTijd"), rs.getString("ContactNR")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * if the amount of travelers is bigger than 1 the ArrayList needs to be sorted in time.
		 * The algoritme used for sorting the database is bubblesort
		 */
		if( reizigers.size() >= 2 ){
			for( int fase = 0; fase < reizigers.size() - 1; fase++){
				for(int i = 0; i < reizigers.size() - 1 - fase; i++){
					if(reizigers.get(i).getVertrektijd() > reizigers.get(i+1).getVertrektijd()){
						Reiziger temp = reizigers.get(i);
						reizigers.set(i, reizigers.get(i+1));
						reizigers.set(i+1, temp);
					}
				}
			}
		}
	}
	
	/**
	 * Lets the Thread sleep for a while.
	 * The given parameter sets the millisecond the Thread waits.
	 * @param millis
	 */
	public void slaap(int millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}