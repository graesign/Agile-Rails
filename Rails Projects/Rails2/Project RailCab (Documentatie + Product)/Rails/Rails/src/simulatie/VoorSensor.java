package simulatie;

public class VoorSensor {
	private Sensor sensor;
	private int nextSensor;
	
	
	public VoorSensor(int tijdTotVolgendeSensor, int nextSensor){
		sensor = new Sensor( tijdTotVolgendeSensor );
		this.nextSensor = nextSensor;
	}
	
	public Sensor getSensor(){
		return sensor;
	}
	
	
	/**
	 * Returns next voorSensor ID
	 * @return nextSensor
	 */
	public int getNextSensor(){
		return nextSensor;
	}

	
}
