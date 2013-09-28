package simulatie;

import java.util.*;
import java.sql.Date;

import business.Datum;
/*
 * @version 0.7
 */
public class Trein{
	/* attributes */
	private int treinID;
	private int MaxzitPlaatsen;
	private boolean isRijden = false;
	private long tijdVertrokken;
	private int tijdNextStation;
	private long aankomstTijd;
	private LinkedList<Route> route;
	boolean routeUpdated = false;
	int aantalPassagiersPer = 0;
	int totaalAantalVervoerdePassengiers = 0;
	private ArrayList<Integer> wachttijd;
	private ArrayList<Integer> reistijd;
	private Datum datumLong;
	
	/* Constructor */
	/**
	 * Creates a train with given information
	 * @param treinID
	 * @param MaxzitPlaatsen
	 */
	public Trein(int treinID, int MaxzitPlaatsen){
		wachttijd = new ArrayList<Integer>();
		reistijd = new ArrayList<Integer>();
		datumLong = new Datum();
		this.treinID = treinID;
		this.MaxzitPlaatsen = MaxzitPlaatsen;
		route = new LinkedList<Route>();
	}

	/* Methods */
	/**
	 * Returns the train id
	 * @return treinID
	 */
	public int getTreinID(){
		return treinID;
	}

	/**
	 * Start the train
	 */
	public void gaRijden(){
		isRijden = true;
		routeUpdated = false; // bij het volgende station mag de routelijst weer worden geupdate
	}

	/**
	 * this method can be used to update the routetable only once per station
	 */
	public void routeLijstGeUpdate(){
		routeUpdated = true; 
	}
	
	/**
	 * returns if the routelist is already updated.
	 * @return
	 */
	public boolean isRouteLijstGeUpdate(){
		return routeUpdated;
	}
	
	/**
	 * Stops the train
	 */
	public void stoppen(){
		isRijden = false;
	}

	/**
	 * Adds a route to the route table
	 * @param startpunt
	 * @param bestemming
	 * @param vertrektijd
	 */
	public void addRoute(  String startpunt, String bestemming, long vertrektijd ){
		route.addLast(new Route( startpunt, bestemming, vertrektijd ));
	}

	/**
	 * Gives availible seats left
	 * @param stationnaam
	 * @return aantal zitplaatsen
	 */
	public int aantalZitplaatsenAt( Station station, String stationnaam ){
		int aantalZitPlaatsenBezet = getAantalZitplaatsen();

		for( Route r: route ){
			Station loopStation = station; //begin bij elke route bij het actuele station
			boolean aanboord = r.getOpgehaald(); // als de reiziger al aan boord is
			while(true){//
				boolean wacht = false;
				if(aanboord){
					if(loopStation.getStationNaam().equalsIgnoreCase(stationnaam)){
						break;
					}
					if(loopStation.getStationNaam().equalsIgnoreCase(r.getBestemming())){
						aantalZitPlaatsenBezet--;
						break;
					}
				}else{
					if(loopStation.getStationNaam().equalsIgnoreCase(r.getStartpunt())){ //als er iemand instapt
						aantalZitPlaatsenBezet++;
						aanboord = true;
						wacht = true;
					}
				}
				if(!wacht){
					loopStation = loopStation.getNextStation();
				}
			}
		}
		return aantalZitPlaatsenBezet;
	}

	/**
	 * Returns max seats
	 * @return MaxzitPlaatsen
	 */
	public int getMaxZitPlaatsen(){
		return MaxzitPlaatsen;
	}

	/**
	 * Sets time of departure
	 * @param tijdVertrokken
	 */
	public void setTijdVertrokken( long tijdVertrokken ){
		this.tijdVertrokken = tijdVertrokken;
	}

	/**
	 * Returns time of departure
	 * @return tijdVertrokken
	 */
	public long getTijdVertrokken(){
		return tijdVertrokken;
	}

	/**
	 * Sets traveling time
	 * @param tijdNextStation
	 */
	public void setTijdNextStation( int tijdNextStation ){
		this.tijdNextStation = tijdNextStation;
	}

	
	/**
	 * Returns time till next station
	 * @return tijdNextsTation
	 */
	public int getTijdNextStation(){

		return tijdNextStation;

	}

	/**
	 * returns the last station in the route table.
	 * @return String last station
	 */
	public String getLastStation(){
		if (route.isEmpty())
			return "";
		return route.getLast().getStartpunt();
	}
	
	/**
	 * Returns if the train is moving
	 * @return isRijden
	 */
	public boolean getIsRijden(){
		return isRijden;
	}
	
	/**
	 * Sets the time of arrival
	 * @param aankomstTijd
	 */
	public void setAankomstTijd(long aankomstTijd){
		this.aankomstTijd = aankomstTijd;
	}
	
	/**
	 * Returns time of arrival
	 * @return aankomstTijd
	 */
	public long getAankomstTijd(){
		return aankomstTijd;
	}
	
	/**
	 * Updates the Routelist if the train is at the station. The arrived travelers are deleted from the routelist
	 * and the travelers who's train has arrived can go in the train.  
	 * @param stationID
	 */
	public int updateRouteLijst( String stationID ){
		LinkedList<Route> removeRoutes = new LinkedList<Route>();
		int aantalPersonenUitgestapt = 0;
		for(Route r: route){
//		kijk of de trein bij een eindstation in de routetabel is.
			if(r.getBestemming().equalsIgnoreCase(stationID)){
//		kijk of de reiziger in de trein zit.
				if(r.getOpgehaald()){
//		verwijder de rij uit de arrayList
					aantalPersonenUitgestapt++;
					this.addReisTijd(r.getVertrektijd(), datumLong.getActueleDatumInSec());
					removeRoutes.add(r);
				}
			}
		}
// tempr is nodig zodat hij alleen niet onnodige personen als opgehaald meerekend. 
// stel de routelijst heeft voor 3 rondes aan data dan zal hij alleen de eerste ronde pakken en niet de tweede
		Route tempr = new Route("", "", 0);
		tempr.setOpgehaald();
		for(Route r: route){
			if(tempr.getOpgehaald() || tempr.getStartpunt().equalsIgnoreCase("")){
				// 		kijk of er iemand moet instappen		
				if(r.getStartpunt().equalsIgnoreCase(stationID)){
					//		kijk of de reiziger niet al in de trein zit.
					if(!r.getOpgehaald()){
						//		persoon stapt in trein
						r.setOpgehaald();
						this.addWachtTijd(r.getVertrektijd(), datumLong.getActueleDatumInSec());
						totaalAantalVervoerdePassengiers++;
						aantalPassagiersPer++;
					}
				}
				tempr = r;
			}
		}
		for(Route r: removeRoutes){
			route.remove(r);
		}
		return aantalPersonenUitgestapt;
	}
	
	/**
	 * 
	 * @param stationID
	 * @return
	 */
	public synchronized boolean gaNaarStation( String stationID ){
		for(Route r: route){
		//	kijk of de trein bij een eindstation in de routetabel is.
			if(r.getBestemming().equalsIgnoreCase(stationID)){
		//		kijk of de reiziger in de trein zit.
				if(r.getOpgehaald()){
		//			trein moet naar het station om iemand uit te laten
					return true;
				}
			}
		}
		Route tempr = new Route("", "", 0); // tijdelijke nodig om de vorige route te onthouden
		tempr.setOpgehaald();
		for(Route r: route){
			//Als de vorige persoon in de trein zit of het startpunt is leeg(eerste element in arraylist)
			if(tempr.getOpgehaald() || tempr.getStartpunt().equalsIgnoreCase("")){
		// 		kijk of er iemand moet instappen		
				if(r.getStartpunt().equalsIgnoreCase(stationID)){
		//			kijk of de reiziger niet al in de trein zit.
					if(!r.getOpgehaald()){
		//				er wil een reiziger instappen
						return true;
					}
				}
				tempr = r;
			}
		}
		// als er niemand in of uit moet. 
		return false;
	}

	/**
	 * returns the amount of taken seats
	 * @return
	 */
	public int getAantalZitplaatsen(){
		int plek = 0;
		for(Route r : route){
			if(r.getOpgehaald())
				plek++;
		}
		return plek;
	}

	/**
	 * returns the amount of routes in het route table
	 * @return
	 */
	public int getAantalroutes(){
		int plek = 0;
		for(Route r : route){
				plek++;
		}
		return plek;
	}
	
	/**
	 * returns the routelist of a train
	 * @return LinkedList<Route>
	 */
	public LinkedList<Route> getRoutes(){
		return route;
	}
	
	/**
	 * temporary method to print the routelist of the train
	 */
	public void testRoute(){

			System.out.println("\tTrein: " + treinID + " heeft: " + route.size()+ " routes");

	}
	
	public long getVertrekTijd(){
		for(Route r: route){
//			kijk of de trein bij een eindstation in de routetabel is.
			if(!r.getOpgehaald()){
				return r.getVertrektijd();
			}
		}
		return 0;
	}
	
	public void resetAantalPassagiersPer(){
		aantalPassagiersPer = 0;
	}
	
	public int getAantalPassagiersPer(){
		return aantalPassagiersPer;
	}
	
	public ArrayList<Integer> getWachtTijden(){
		return wachttijd;
	}
	
	public ArrayList<Integer> getReisTijden(){
		return reistijd;
	}
	
	public void addWachtTijd(long vertrek, long actueleTijd){
		Calendar v = Calendar.getInstance();
		Calendar a = Calendar.getInstance();
		v.clear();
		a.clear();
		v.setTimeInMillis(vertrek);
		a.setTimeInMillis(actueleTijd);
		int tijd = elapsedSeconds(v,a);
		int min = tijd / 60;
		int sec = tijd % 60;
		
		int tijd2 = elapsedSeconds(a,v);

		wachttijd.add(tijd);
	}
	
	public void addReisTijd(long vertrek, long actueleTijd){
		
		Calendar v = Calendar.getInstance();
		Calendar a = Calendar.getInstance();
		v.clear();
		a.clear();
		v.setTimeInMillis(vertrek);
		a.setTimeInMillis(actueleTijd);
		int tijd = elapsedSeconds(v,a);
		int min = tijd / 60;
		int sec = tijd % 60;
		
		int tijd2 = elapsedSeconds(a,v);

		reistijd.add(tijd);
	}
	
    // Bereken het verschil in seconden tussen de twee Calendars.
    public static int elapsedSeconds(Calendar before, Calendar after) {
        return (int) elapsedMillis(before, after, 1000);
    }
    
    // De centrale hulpmethode om milliseconden terug te rekenen. Dit retourneert -1
    // indien de eerste Calendar niet van een eerdere datum is dan de tweede Calendar.
    private static long elapsedMillis(Calendar before, Calendar after, final int FACTOR) {
        if (before.after(after)) {
            return -1;
        }
        return (after.getTimeInMillis() - before.getTimeInMillis()) / FACTOR;
    }
    
    public int getTotaalAantalVervoerdePassengiers(){
		return totaalAantalVervoerdePassengiers;
	}
}