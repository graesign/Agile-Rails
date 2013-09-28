package Data;

import Business.*;

import java.util.*;

public class Reservation { 
  
    private Station departStation, arrivalStation;
    private Calendar departTime;
    private int code;  
    private boolean departed = false;
    private boolean zelfreis;
    private boolean waiting = false;

    /**
     * Initialisatie van variabelen.
     * 
     * @param Station - departStation
     * @param Station - arrivalStation
     * @param Calendar - departTime
     * @param Integer - code
     * @param Boolean - zelfreis
     */
    public Reservation(Station departStation, Station arrivalStation, Calendar departTime, int code, boolean zelfreis) {
    	  
    	this.departStation = departStation;
    	this.zelfreis = zelfreis;
    	this.arrivalStation = arrivalStation;
    	this.code = code;
    	this.departTime = departTime;	
       } 
   
    /**
     * @return Integer
     */
    public int getCode() {
        return code;
    }
    
    /**
     *	Zet departed flag op true.
     */
    public void setDeparted(){
    	departed = true;
    }
    
    
    /**
     * @return Boolean
     */
    public boolean getDeparted(){
    	return departed;
    }
    
    /**
     * @return Boolean
     */
    public boolean getZelfreis(){
    	return zelfreis;
    }
    
    /**
     * @return Station
     */
    public Station getDepartStation() {
        return departStation;
    }

    public void setWaiting(){
    	waiting = true;
    }
    
    public boolean wasWaiting(){
    	return waiting;
    }
    /**
     * @return Station
     */
    public Station getArrivalStation() {
        return arrivalStation;
    }
    
    /**
     * @return Calendar
     */
    public Calendar getDepartTime() {
        return departTime;
    }
}
