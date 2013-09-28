package Data;


import java.util.*;

public class ReservationUnion {
	
	private Map map;
	private HashMap<String, Stack> reservations;
	
    /**
     * Initialiseerd reservations met een nieuwe HashMap.
     */
	public ReservationUnion() {
		reservations = new HashMap<String, Stack>();
	}
	
	/**
	 * Voegt een reservering toe aan reservations
	 * met als key de ID's van het vertrek station en 
	 * aankomst station samengevoegd.
	 * 
	 * @param Reservation - res
	 */
	public void addReservation(Reservation res){
		String key = (""+res.getDepartStation().getStationID()+res.getArrivalStation().getStationID());
		if(!reservations.containsKey(key)){
			Stack<Reservation> stack = new Stack<Reservation>();
			stack.push(res);
			reservations.put(key, stack);
		}
		else{
			reservations.get(key).push(res);
		}
	}
	
	/**
	 * @return reservations
	 */
	public Map getReservation(){
		map = reservations;
		return map;
	}
	
	/**
	 * Geeft een stack met reserveringen 
	 * terug uit reservations.
	 * 
	 * @param String -
	 * 				key
	 * 
	 * @return Stack
	 */
	public Stack getReservations(String key){
		return reservations.get(key);
	}
	
	/**
	 * Verwijderd de stack de is gelocaliseerd 
	 * in de reservations op de mee gegeven key
	 * 
	 * @param Stack -
	 * 				key
	 */
	public void removeStack(Stack key){
		reservations.remove(key);
	}
}
