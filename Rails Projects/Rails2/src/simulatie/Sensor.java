package simulatie;

/*
 * @version 0.5
 */
public class Sensor{
	/* Attributes */
	private int treinID;
	private int afgehandeld;
	private int tijdTotVolgendeSensor;
	
	/* Constructors */
	/**
	 * Creates a sensor and set default values at 0
	 */
	public Sensor(int tijdTotVolgendeSensor){
		this.tijdTotVolgendeSensor = tijdTotVolgendeSensor;
		treinID = 0;
		afgehandeld = 0;
	}
	
	/*methodes */
	
	/**
	 * Sets the treinID of the train that is located at the sensor
	 * Also used to set treinID to null if there is no train
	 * @param treinID;
	 */
	public synchronized void setSensorWaarde( int treinID ){
		this.treinID = treinID;
	}
	
	/**
	 * Returns the treinID attribute value
	 * Will return null if there is no train
	 * @return treinID
	 */
	public synchronized int getSensorWaarde(){
		return treinID;
	}
	
	/**
	 * Sets afgehandeld if train has left sensor so the train is not editable
	 */
	public synchronized void setAfgehandeld(int treinid){
		afgehandeld = treinid;

	}
	
	/**
	 * @return isAfgehandeld
	 */
	public synchronized int getAfgehandeld(){
		return afgehandeld;
	}
	
	/**
	 * Returns time till next sensot
	 * @return tijdTotVolgendeSensor
	 */
	public synchronized int getTijdTotVolgendeSensor(){
		return tijdTotVolgendeSensor;
	}
}