package simulatie;

import java.util.*;
/*
 * @version 0.8
 */
public class Station{
	/* Attributes */
//	private Sensor stationSensor;
	private Wissel voorWissel;
	private ArrayList<VoorSensor> voorSensors;
	//private Sensor voorSensor;
	private Sensor tussenWisselSensor;
	private Wissel achterWissel;
	private LinkedList<Sensor> stationSensoren;
	private Station nextStation;
	private Station previousStation;
	private int tijdNextStation;
	private String stationNaam;
	final int MAX_AANTAL_TREINEN = 3;
	/* Constructors */
	
	/**
	 * This constructor will set all the start information of the station
	 */
	public Station( int tijdNextStation, String stationNaam, int tijdTotVolgendeSensor){
		stationSensoren = new LinkedList<Sensor>();
		voorWissel =  new Wissel();
		voorSensors = new ArrayList<VoorSensor>();	
		tussenWisselSensor = new Sensor(tijdTotVolgendeSensor);
		achterWissel = new Wissel();
		this.nextStation = null;
		this.previousStation = null;
		this.tijdNextStation = tijdNextStation;
		this.stationNaam = stationNaam;
	}
	
	/* methods */

	/**
	 * returns the amount of trains at a station
	 */
	public int AantalTreinenBij(){
		int aantal = 0;
		for(int i = 0; i < stationSensoren.size(); i++){
			if(stationSensoren.element().getSensorWaarde() != 0  )
				aantal++;
		}
		return aantal;
	}
	
	/**
	 * check if the maximum amount of trains are at a station
	 */
	public boolean isMaximumAantalTreinen(){
		if( stationSensoren.size() >= MAX_AANTAL_TREINEN ){
			if(stationSensoren.element().getSensorWaarde() == 0)
					return false;
				return true;
		}
		else
			return false;
	}
	
	/**
	 * Sets the station sensor with a treinID
	 * @param treinID
	 */
	public void setStationSensorWaarde( int treinID ){
		if(treinID != 0){
			if(  stationSensoren.size() == 1 && stationSensoren.element().getSensorWaarde() == 0){

				stationSensoren.element().setSensorWaarde(treinID);
			}else 
			{
				Sensor s = new Sensor(0);
				s.setSensorWaarde(treinID);
				stationSensoren.add(s);
			}
		}else{
			if( stationSensoren.size() > 1){
				stationSensoren.remove();
			}else 
			{
				stationSensoren.element().setSensorWaarde(0);
			}
		}
		
	}
	
	/**
	 * Returns current treinID that is at the station.
	 * Returns null if there is no train at the sensor
	 * @return treinID
	 */
	public int getStationSensor(){
		if( stationSensoren.isEmpty())
			return  0;
		else
			return stationSensoren.element().getSensorWaarde();
	}
	
	/**
	 * Gets stationSensorAfgehandeld value
	 * @return int
	 */
	public int getStationSensorAfgehandeld(){
		if(!stationSensoren.isEmpty())
			return stationSensoren.element().getAfgehandeld();
		else 
			return 0;
	}
	
	/**
	 * Sets Value of stationSensorAfgehandeld
	 * @param treinid
	 */
	public void setStationSensorAfgehandeld(int treinid){
		if(!stationSensoren.isEmpty())
			stationSensoren.element().setAfgehandeld(treinid);
	}

	
	/**
	 * Method that sets the next station
	 * @param nextStation
	 */
	public void setNextStation(Station nextStation){
		this.nextStation = nextStation;
	}
	
	/**
	 * Method that sets the previous station
	 * @param previousStation
	 */
	public void setPreviousStation(Station previousStation){
		this.previousStation = previousStation;
	}
	
	/**
	 * Returns station name
	 * @return stationNaam
	 */
	public String getStationNaam(){
		return stationNaam;
	}
	
	/**
	 * Sets the voorWissel high or low
	 */
	public void setVoorWissel(){
		if(voorWissel.getWaarde() == true)
			voorWissel.setWisselLaag();
		else if(voorWissel.getWaarde() == false)
			voorWissel.setWisselHoog();
	}
	
	/**
	 * Returns the status of voorWissel
	 * @return boolean
	 */
	public boolean getVoorWisselStatus(){
		return voorWissel.getWaarde();
	}
	
	/**
	 * Sets the achterWissel high or low
	 */
	public void setAchterWissel(){
		if(achterWissel.getWaarde() == true)
			achterWissel.setWisselLaag();
		else if(achterWissel.getWaarde() == false)
			achterWissel.setWisselHoog();
	}
	
	/**
	 * Returns the status of achterWissel
	 * @return boolean
	 */
	public boolean getAchterWisselStatus(){
		return achterWissel.getWaarde();
	}
	
	
	/**
	 * Get the next station
	 * @return nextStation
	 */
	public Station getNextStation(){
		return nextStation;
	}
	
	/**
	 * Returns the previous station
	 * @return previousStation
	 */
	public Station getPreviousStation(){
		return previousStation;
	}
	
	/**
	 * Returns traveling time till next station
	 * @return tijdNextStation
	 */
	public int getTijdNextStation(){
		return tijdNextStation;
	}
	
	
	/**
	 * Sets Value of voorSensorAfgehandeld
	 * @param treinid
	 */
	public void setTussenWisselSensorAfgehandeld(int treinid){
		tussenWisselSensor.setAfgehandeld(treinid);
	}
	/**
	 * Gets voorSensorAfgehandeld value
	 * @return int
	 */
	public int getTussenWisselSensor(){
		return tussenWisselSensor.getSensorWaarde();
	}
	
	
	/**
	 * Sets Value of voorSensorAfgehandeld
	 * @param treinid
	 */
	public void setTussenWisselSensor(int treinid){
		tussenWisselSensor.setSensorWaarde(treinid);
	}
	/**
	 * Gets tussenWisselSensorAfgehandeld value
	 * @return int
	 */
	public int getTussenWisselSensorAfgehandeld(){
		return tussenWisselSensor.getAfgehandeld();
	}
	
	/**
	 * Sets Value of tussenWisselSensorAfgehandeld
	 * @param treinid
	 */
	public void setVoorSensorAfgehandeld(int treinid){
		tussenWisselSensor.setAfgehandeld(treinid);
	}

	/**
	 * Adds a VoorSensor in voorSensors list
	 * @param tijdTotVolgendeSensor
	 */
	public void addVoorSensor(int tijdTotVolgendeSensor, int nextSensor){
		voorSensors.add(new VoorSensor(tijdTotVolgendeSensor, nextSensor));
	}

	/**
	 * Gives a list of voorSensors
	 * @return voorSensors
	 */
	public ArrayList<VoorSensor> getVoorSensors(){
		return voorSensors;
	}

	/**
	 * Gives the current queue list of stationSensors
	 * @return stationSensoren
	 */
	public LinkedList<Sensor> getStationSensors(){
		return stationSensoren;
	}

	/**
	 * get value of TussenWisselSensor a
	 */
	public int getTijdVolgendeSensorVanTussenWissel(){
		return tussenWisselSensor.getTijdTotVolgendeSensor();
	}
}