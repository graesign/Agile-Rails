package Business;
import java.util.*;
import Data.*;

public class AI extends Thread {
private Central central;
private ArrayList<Station> stations;
private Station beginStation;
private Station eindStation;
private boolean interruptAI = true;
private int agressie = 50;

	public AI(Central central){
		this.central = central;
		this.stations = central.getStationsArrayList();
	
		
	}
	 
	public boolean interruptAI(){
	   interruptAI = !interruptAI;
	   return interruptAI;
	}
	  
	public void setAgression(int agr){
		agressie = agr;
	}
	
	public int getAgressie(){
		return agressie;
	}
	public void run(){
		
		while(true){
			
			if(!interruptAI)
			{
				try 
				{
					sleep((1000/central.getAcceleration())*(int)(Math.random()*(10000/agressie))); // Slaapt 1 seconde gedeeld door de tijdacceleratie maal een random waarde tussen de 0 en de 500
					if(stations.size() != 0)
					{
						beginStation = stations.get((int) (Math.floor(Math.random()*stations.size()))  ); // Pak een willekeurig begin station
						eindStation = stations.get((int) (Math.floor(Math.random()*stations.size()))  ); // Pak een willekeurig begin station
						while(beginStation.equals(eindStation) || eindStation == null) eindStation = stations.get((int) (Math.floor(Math.random()*stations.size())) );
						if((beginStation.getStationDrukte() > (int) Math.floor(Math.random()*10) && eindStation.getStationDrukte() > (int) Math.floor(Math.random()*10) ))
						{
							Calendar tmp = Calendar.getInstance();
							
							int tmpvalue = (int) Math.floor(Math.random()*(1000*60*10)); // TUSSEN NU 0 * 160000 tot 1 * 160000
							tmp.setTimeInMillis(SimDate.getTimeInMillis()+tmpvalue); // Calendar nieuwe tijd geven.
							
							for(int i = 1; i < (int)(Math.floor(Math.random()*(2*((beginStation.getStationDrukte()+eindStation.getStationDrukte())/1.5))))+1; i++) 	//Soms gaan er mensen in een groepje naar een bestemming toe.
							{
								central.createReservation(beginStation, eindStation, tmp, central.generateCode(beginStation));
								central.echo("AI CREATED NEW PASSENGER : " + eindStation.getStationName());
							}
						}
					}
				
				} 
				catch (InterruptedException e) {}
			
			}
			
			else
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
