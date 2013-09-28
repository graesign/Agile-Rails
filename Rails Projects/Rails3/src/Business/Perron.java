package Business;

import java.util.*;
import Data.*;

public class Perron {
	private PerronTerminal terminal;
    private Shuttle shuttle;
    private Station station;
    private Station destinationStation;
    private Central central;
    private int perronID,passengersamount;
    private ArrayList<Passenger> passengers;
    //private Date departTime;

    /**
	 * initialiseerd een Perron
	 * @param - Central, Station, int
	 * @return - Boolean
	 */
    public Perron(Central central, Station station, int perronID) {
    	passengers = new ArrayList<Passenger>();
    	this.perronID = perronID;
    	terminal = new PerronTerminal(central, station, this, 1);
    	this.shuttle = null;
    	this.central = central;   
    	this.station = station;
    	
    }

    public PerronTerminal getTerminal() {
        return terminal;
    }
    
    public void setDestinationStation(Station s) {
    	this.destinationStation = s;
    }
    
    public Station getDestinationStation(){
    	return destinationStation;
    }

    public void setTerminal(PerronTerminal val) {
        this.terminal = val;
    }

    public Shuttle getShuttle() {
        return shuttle;
    }

    public void setShuttle(Shuttle val) {
        this.shuttle = val;
        //shuttle.clearShuttle();
       central.echo("Shuttle is op het perron gearriveerd");
    }

    public int getPerronID() {
        return perronID;
    }

    public void setPerronID(int val) {
        this.perronID = val;
    }

    public int getPassengers() {
        return passengersamount;
    }

    public void addPassenger() {
    	addPassenger(1);
    }
    
    public void addPassenger(int amount){
    	//for(int i = 0; i < amount; i++) central.getDatabase().addPassengerWaiting(SimDate.getTimeInMillis(), station);
    	passengersamount +=amount;
    }
    public int getPassengerAmount(){
    	return passengersamount;
    } 
    
    public void fillShuttle(){
    	for(int i = 0; i < passengers.size()-1; i++){
    		shuttle.insertPassenger(passengers.get(i));
    		passengers.remove(i);
    	}
    }
    
  
    
   // public void sentShuttlseToShuntArea(){
    //	station.getShuntArea().addShuttle(shuttle);
    	//shuttle=null;
    //}
    
    public void sentShuttle(boolean zelfreis){

    	if(destinationStation != null && station != null)
    	{
    		java.awt.Color colorShuttle;
//    		System.out.println(shuttle+" vertrekt naar zijn bestemming met "+this.getPassengerAmount()+ "MENSEN");
    		destinationStation.acceptShuttle(shuttle);  // FIX THIS

    		while(passengersamount != 0 && shuttle != null)
    		{
    			int passengersInShuttle = this.getPassengerAmount() >= 8 ? 8 : this.getPassengerAmount();
    			shuttle.insertPassengers(passengersInShuttle);
    		
    			if(zelfreis) {
    				colorShuttle = java.awt.Color.YELLOW;
    		
    			}
    			else
    			{
    				if(this.getPassengerAmount() > 1) colorShuttle = java.awt.Color.GREEN; 
    				else colorShuttle = java.awt.Color.black;
    			}
    			
    			central.addVisualShuttle(station, destinationStation, shuttle, colorShuttle);
    			
    			
    			central.getDatabase().addPassengerTraveling(SimDate.getTimeInMillis(), passengersInShuttle, station);
    				
    			passengersamount -= passengersInShuttle;
    			
    			if(passengersamount > 0)
					try {
						setShuttle(station.getShuntArea().sentShuttleToDestination());
						Thread.sleep(120000/central.getAcceleration());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			
    		}
    		
    		station.pushFreePerron(this);
    		passengersamount = 0;
    	}
    }
}
