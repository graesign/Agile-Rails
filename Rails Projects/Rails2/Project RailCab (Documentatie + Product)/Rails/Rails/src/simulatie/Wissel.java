package simulatie;

/*
 * @version 0.4
 */

public class Wissel{
	
	/* Attributes */
	private boolean status;
	
	
	/* Constructors */
	/**
	 * Creates the wissel and sets default status to false
	 */
	public Wissel(){
		status = false ; // sets the default status of the switch to low
	}
	
	/* Methods */
	
	/**
	 * Sets the status of the wissel high
	 */
	public void setWisselHoog(){
		status = true;
	}
	
	/**
	 * Sets the status of the wissel low
	 */
	public void setWisselLaag(){
		status = false;
	}
	
	/**
	 * Returns the wissel status
	 * @return status
	 */
	public boolean getWaarde(){
		return status;
	}
}