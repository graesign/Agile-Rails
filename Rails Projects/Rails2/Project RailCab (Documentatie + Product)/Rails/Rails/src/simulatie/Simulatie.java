package simulatie;

import java.util.*;
import java.sql.*;
import business.*; 

/*
 * @version 0.12
 */
public class Simulatie{
	/* atributen */
	private ArrayList<Trein> treinen;
	private Station stations;
	private DbManager dbm;
	private Datum datumLong;
	private long tijdLaatsteReiziger;
	private int aantalStations = 0;
	private int aantalRailCabs = 20;
	private int aantalPersonenPerMinuut = 20;
	private int aantalPersonenInSysteem = 0;
	private boolean maakRandomReizigers = false;
	
	/* constructor */
	public Simulatie(){
		dbm = new DbManager("railcab", "railcab", "localhost", "railcab");
		tijdLaatsteReiziger = 0;
		datumLong = new Datum();				// Maak een datum object aan.
		treinen = new ArrayList<Trein>();		// Maak een arraylist voor de treinen.
		
		maakStation( dbm.getStations() );		// Maak een lijst van de stations.
		
		for(int i = 1; i < aantalRailCabs+1; i++){				// Maak de rail-cabs.
			treinen.add(new Trein( i , 8));
		}
		
		/*
		 * Hier worden te treintjes op de baan geplaatst. De treinen staan tijdens de initializatie op de stations.
		 * Er kunnen maar een bepaald aantal treinen op een station staan dus de rest van de treinen worden op het volgende station geplaatst 
		 * example: 
		 */
		int i = 0;
		while(true){
			for(; i < aantalRailCabs; i++){				
				if ( stations.isMaximumAantalTreinen() )
					break;
				else
					stations.setStationSensorWaarde(treinen.get(i).getTreinID());
			}
			if(i == aantalRailCabs )
				break;
			stations = stations.getNextStation();
		}
		
		//Voordat de simulatie begint worden alle reizigers uit de database verwijderd.
		verwijderReizigers();
	}
	
	/**
	 * De run methode wordt door het algoritme aangeroepen.
	 */
	public void run(){
		if( maakRandomReizigers )
			maakReiziger();
		for(Trein t: treinen){
			if( t.getIsRijden() == true ){
				// Een paar variabele die nodig zijn.
				int voorSensorID = 0;
				boolean bezig = false;
				boolean gevonden = false;

				/*
				 * Deze whileloop zoekt waar de treintjes zijn.
				 */
				while(true){
					for( VoorSensor vs : stations.getVoorSensors() ){
						try {
							if ( vs.getSensor().getSensorWaarde() == t.getTreinID() ) {
								voorSensorID = stations.getVoorSensors().indexOf(vs);// bewaar het nummer van het array
								gevonden = true;
								break;
							}
							if ( vs.getSensor().getAfgehandeld() == t.getTreinID()) {
								bezig = true;
								voorSensorID = stations.getVoorSensors().indexOf(vs);// bewaar het nummer van het array
								gevonden = true;
								break;
							}	
						} catch (Exception e) {
							// TODO: handle exception
							System.out.println("i = "+ stations.getVoorSensors().indexOf(vs) +"size = "+stations.getVoorSensors().size()+ "trein = "+ t.getTreinID() );
							break;
						}
					}
					//kijkt of hij al gevonden is om de while loop te stoppen
					if(gevonden)
						break;

					//Als hij nog niet gevonden is kijkt hij of de trein bij een tussenwissel staat
					if(stations.getTussenWisselSensor() == t.getTreinID()){
						break;
					}
					if(stations.getTussenWisselSensorAfgehandeld() == t.getTreinID()){
						bezig = true;
						break;
					}

					//kijkt of hij in de de stations lijst staat
					if(!stations.getStationSensors().isEmpty()){
						if(stations.getStationSensors().element().getSensorWaarde() == t.getTreinID()){
							break;
						}
						if(stations.getStationSensors().element().getAfgehandeld() == t.getTreinID()){
							bezig = true;
							break;
						}
					}
					stations = stations.getNextStation();	
				}

				/*
				 * Kijkt of de trein al aan het rijden is. Als dat zo is kijkt hij of de trein toevallig al bij de bestemming is.
				 * Als de trein is gearriveerd worden de sensoren goed gezet.
				 */
				if(bezig){
					if( datumLong.getActueleDatumInSec() >= t.getAankomstTijd()){// Kijkt of de trein bij de volgende sensor is.
						// voorsensor
						if(stations.getVoorSensors().get(voorSensorID).getSensor().getAfgehandeld() == t.getTreinID()){
							stations.getVoorSensors().get(voorSensorID).getSensor().setAfgehandeld(0);
							if(stations.getVoorSensors().get(voorSensorID).getNextSensor() != 10 ){
								//De trein is bij de volgende sensor
								stations.getVoorSensors().get((voorSensorID+1)).getSensor().setSensorWaarde(t.getTreinID());
							} else {
								if(stations.getVoorWisselStatus() == true){
									stations.setStationSensorWaarde(t.getTreinID());
								}
								if(stations.getVoorWisselStatus() == false){
									stations.setTussenWisselSensor(t.getTreinID());
								}
							}
						}
//							tussenwissel
						if(stations.getTussenWisselSensorAfgehandeld() == t.getTreinID()){
							stations.setTussenWisselSensorAfgehandeld(0);
							stations.getNextStation().getVoorSensors().get(0).getSensor().setSensorWaarde(t.getTreinID());
						} else { // station
							if(stations.getStationSensorAfgehandeld() == t.getTreinID()){
								if(!stations.getStationSensors().isEmpty()){	
									stations.setStationSensorAfgehandeld(0);
									stations.getNextStation().getVoorSensors().get(0).getSensor().setSensorWaarde(t.getTreinID());
								}
							}
						}
						t.stoppen();	// Stopt de trein
						t.setAankomstTijd(datumLong.getActueleDatumInSec());
					}
				} else {
					/*
					 * Kijkt DMV de wissels waar de trein heen moet.
					 */
					t.setTijdVertrokken(datumLong.getActueleDatumInSec());
					//VoorSensors
					if(stations.getVoorSensors().get(voorSensorID).getSensor().getSensorWaarde() == t.getTreinID()){
						if(stations.getVoorSensors().get(voorSensorID).getNextSensor() != 10 ){
							stations.getVoorSensors().get(voorSensorID).getSensor().setSensorWaarde(0);
							stations.getVoorSensors().get(voorSensorID).getSensor().setAfgehandeld(t.getTreinID());
							
							t.setAankomstTijd(datumLong.addTijd(t.getTijdVertrokken(), datumLong.versnelTijd(stations.getVoorSensors().get(voorSensorID).getSensor().getTijdTotVolgendeSensor())));
						} else {
							stations.getVoorSensors().get(voorSensorID).getSensor().setSensorWaarde(0);
							stations.getVoorSensors().get(voorSensorID).getSensor().setAfgehandeld(t.getTreinID());

							t.setAankomstTijd(datumLong.addTijd(t.getTijdVertrokken(), datumLong.versnelTijd(20000)));	//deze tijd geld voor het station en voor de tussenwissel
						}
						break;
					}
					//TussenWisselSensor
					if(stations.getTussenWisselSensor() == t.getTreinID()){
						stations.setTussenWisselSensor(0);
						stations.setTussenWisselSensorAfgehandeld(t.getTreinID());
						t.setAankomstTijd(datumLong.addTijd(t.getTijdVertrokken(), datumLong.versnelTijd(stations.getTijdVolgendeSensorVanTussenWissel())));//stations.getTijdVolgendeSensorVanTussenWissel()
					} else { 	// StationSensor
						if(stations.getStationSensor() == t.getTreinID()){
							if(!stations.getStationSensors().isEmpty()){
								stations.setStationSensorWaarde(0);
								stations.setStationSensorAfgehandeld(t.getTreinID());
							
								t.setAankomstTijd(datumLong.addTijd(t.getTijdVertrokken(), datumLong.versnelTijd(stations.getTijdVolgendeSensorVanTussenWissel())));//stations.getTijdVolgendeSensorVanTussenWissel()
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Deze methode maakt willekeurig reizigers aan. Het aantal reizigers per minuut kan worden ingesteld.
	 */
	public void maakReiziger(){
		int tijdPerPersoon = 0;
		if(aantalPersonenPerMinuut > 0)
			tijdPerPersoon = 60000 / aantalPersonenPerMinuut;
		if( datumLong.getActueleDatumInSec() > datumLong.addTijd(tijdLaatsteReiziger, tijdPerPersoon) ){
			tijdLaatsteReiziger = datumLong.getActueleDatumInSec();
			long vertrekTijd = 0;
			int randomTijd = 0;

			while (randomTijd < (double)(6 / datumLong.versnellingsFactor)){//vier min
				randomTijd = (int)( 10.0 * Math.random()); 
			}
			vertrekTijd = datumLong.addTijd( datumLong.getActueleDatumInSec(), randomTijd, 0 );
			int beginStationNr = (int)( 7.0 * Math.random());// Geeft het nummer van het station dat wordt toegevoegd
			int eindStationNr;
			do{
				eindStationNr = (int)( 7.0 * Math.random());
			}while (eindStationNr == beginStationNr );			
			
			String startpunt =	getStationAt( stations, beginStationNr ).getStationNaam();
			String eindpunt = getStationAt( stations, eindStationNr ).getStationNaam();

			//zet de reiziger in de database
			dbm.setReizigers(startpunt, eindpunt, vertrekTijd, 1);
		}

	}
	
	public void verwijderReizigers(){
		dbm.verwijderReizigers(999999999999999999L);
	}
	
	/**
	 * Geeft een lijst met treinen.
	 * @return ArrayList<Trein>
	 */
	public ArrayList<Trein> getTreinen(){
		return treinen;
	}

	/**
	 * Geeft de stations terug.
	 * @return stations
	 */
	public Station getStations(){
		return stations;
	}

	/**
	 * Maakt een linkedList van stations.
	 * Als parameter wordt er om een resultset gevraagd. hierin moeten de stations staan.
	 * @param rs_station
	 */
	public void maakStation( ResultSet rs_station ){
		Station leeg = new Station( 0, "", datumLong.versnelTijd(20000));
		stations = maakStation( rs_station, leeg, leeg);
		stations.setPreviousStation(getLastElement(stations));
		getLastElement(stations).setNextStation(stations);
	}

	/**
	 * Maakt een linkedList van stations.
	 * De eerst waarde 'previousStation' is gelijk aan null en de laatste waarde vannextStation is ook null.
	 * @param rs_station
	 * @param prevStat
	 * @param station
	 * @return 
	 */
	public Station maakStation( ResultSet rs_station, Station prevStat, Station station ){
		Station s = null;
		ResultSet voorwissels = null;
		ResultSet tussenSensor = null;
		try {// de try is omdat er sql gebruikt wordt.
			if( rs_station.next() ){//loop door de resultset
				aantalStations++;
				int tijdNextStation = rs_station.getInt("NextStationTijd");
				String stationNaam = rs_station.getString("StationNaam");
				
				tussenSensor = dbm.getTussenWisselWaarde(stationNaam);
				int ttvs = 0;
				while(tussenSensor.next()){
					ttvs = tussenSensor.getInt("TijdTotVolgendeSensor");
				}
				
				station = new Station( tijdNextStation, stationNaam, ttvs );
				voorwissels = dbm.getVoorWissels(station.getStationNaam());
				while(voorwissels.next()){
					station.addVoorSensor(voorwissels.getInt("TijdTotVolgendeSensor"), voorwissels.getInt("NextSensor"));
				}
				station.setPreviousStation(prevStat);
				station.setNextStation( maakStation(rs_station, station, s));
			}else{
				station = null; //bij het laatste station
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return station;
	}

	/**
	 * Geeft het laatste station terug. 
	 * @param station
	 * @return stations
	 */
	public Station getLastElement( Station station ){
		if(station.getNextStation() != null ){
			station = getLastElement( station.getNextStation() );
		}
		return station;
	}

	/**
	 * Geeft de reistijd tussen twee stations.
	 * param station1
	 * param station2
	 * @return travel time
	 */
	public int getReistijd( String station1, String station2 ){
		int reistijd = 0;
		do{// loop door de stations heen
			if(stations.getStationNaam().equalsIgnoreCase(station1)){ // als de stationnaam gelijk is aan station1.
				while(!stations.getStationNaam().equalsIgnoreCase(station2)){ // zoek station twee.
					reistijd += stations.getTijdNextStation(); // tel de reistijd er bij op
					stations = stations.getNextStation();
				}
			}
			stations = stations.getNextStation();
		}while(!stations.getStationNaam().equalsIgnoreCase(station1));
			return reistijd;
	}

	/**
	 * Geeft het 'nr' station uit de lijst. Deze methode is alleen nuttig voor maakReiziger omdat we daar met een Random waardes werken.
	 * @param station
	 * @param nr
	 * @return station
	 */
	public Station getStationAt( Station station, int nr ){
		if( nr > 0 ){
			nr--;
			station = getStationAt(station.getNextStation(), nr);
		}
		return station;
	}

	/**
	 * Geef true als station 2 voorbij statation 1 is.
	 * @param station1
	 * @param station2
	 * @return boolean
	 */
	public boolean vergelijkStations(String station1, String station2 ){
		int stationTeller = 0;
		do{// loop door de stations heen
			stations = stations.getNextStation();

			if(stations.getStationNaam().equalsIgnoreCase( station1 )){ // als de stationnaam gelijk is aan station1.
				while(!stations.getStationNaam().equalsIgnoreCase(station2)){ // zoek station twee.
					stationTeller++;
					if( stationTeller == ( aantalStations / 2 ) + 1 ){ // kijk een bepaalde tijd vooruit
						return false;
					}
					stations = stations.getNextStation();
				}
				return true;
			}
		}while( !stations.getStationNaam().equalsIgnoreCase(station1) );
			return true;
	}
	
	/**
	 * returned het aantal treinen
	 * @return aantalRailCabs
	 */
	public int getAantalTreinen(){
		return aantalRailCabs;
	}
	
	/**
	 * Het aantal reizigers dat per minuut wordt aangemaakt.
	 */
	public void setAantalPersonenPerMinuut(int aantalPersonenPerMinuut){
		this.aantalPersonenPerMinuut = aantalPersonenPerMinuut;
	}
	
	/**
	 * Het aantal reizigers dat per minuut wordt aangemaakt.
	 * @return aantalPersonenPerMinuut
	 */
	public int getAantalPersonenPerMinuut(){
		return aantalPersonenPerMinuut;
	}
	
	/**
	 * Schakel de methode maakReiziger uit
	 * @return boolean
	 */
	public boolean maakRandomReizigers(){
		maakRandomReizigers = !maakRandomReizigers;
		return maakRandomReizigers;
	}
	
	/**
	 * retuned true als er reizigers worden aangemaakt.
	 * @return maakRandomReizigers
	 */
	public boolean getMaakRandomReizigers(){
		return maakRandomReizigers;
	}
	
	/**
	 * Per reiziger die uit de database wordt gehaald wordt deze methode aangeroepen.
	 */
	public void addAantalPersonenInSysteem(){
		aantalPersonenInSysteem++;
	}
	
	/**
	 * Verwijderd het aantal personen dat is afgehandeld.
	 */
	public void deleteAantalPersonenInSysteem(int minPersonen){
		aantalPersonenInSysteem -= minPersonen;
	}
	
	/**
	 * Returned het aantal personen in het systeem.
	 * @return
	 */
	public int getAantalPersonenInSysteem(){
		return aantalPersonenInSysteem;
	}
	
	/**
	 * return het totaal aantal vervoerde passengiers.
	 * @return int
	 */
	public int getTotaalAantalVervoerdePassengiers(){
		int passengiers = 0;
		for(Trein t: treinen){
			passengiers += t.getTotaalAantalVervoerdePassengiers();
		}
		return passengiers;
	}
}