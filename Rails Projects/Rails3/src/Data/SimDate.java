package Data;

import java.util.*;
import Business.*;

public class SimDate extends Thread {
	private static long ms;
	private int dateAcceleration = 100;
	private boolean pause = false;
	
	/**
	 * Initialiseerd de variabele ms.
	 */
	public SimDate() {
		ms = System.currentTimeMillis();
	}

	/**
	 * Door middel van de "dateAcceleration" word het 
	 * programma hier versneld of vertraagt
	 */
	public void run() {
		while (true) {
			try 
			{
				if(!pause) ms += (1000);
				Thread.sleep(1000 / dateAcceleration);
			} catch (InterruptedException e) {}
		}
	}

	/**
	 * @return Long
	 */
	public long getSimDate() {
		return ms;
	}
	
	/**
	 * @return Boolean
	 */
	public boolean getPause(){
		return pause;
	}
	
	/**
	 * zet "pause" flag op true.
	 */
	public void setPauseOn(){
		pause = true;
	}
	
	/**
	 * zet "pause" flag op flase.
	 */
	public void setPauseOff(){
		pause = false;
	}
	
	/**
	 * @return Long
	 */
	public long getYear() {
		return (getTimer().get(Calendar.YEAR));
	}

	/**
	 * @return Long
	 */
	public long getMonth() {
		return (getTimer().get(Calendar.MONTH));
	}

	/**
	 * @return Long
	 */
	public long getDay() {
		return (getTimer().get(Calendar.DAY_OF_WEEK));
	}

	/**
	 * @return Long
	 */
	public long getHours() {
		return (getTimer().get(Calendar.HOUR_OF_DAY));
	}

	/**
	 * @return Long
	 */
	public long getMinute() {
		return (getTimer().get(Calendar.MINUTE));
	}

	/**
	 * @return Long
	 */
	public long getSeconds() {
		return (getTimer().get(Calendar.SECOND));
	}

	/**
	 * @return Integer
	 */
	public int getDateAcceleration() {
		return dateAcceleration;
	}

	/**
	 * Zet de "dateAcceleration" de tijd versneld of 
	 * vertraagt ten opzichte van de realiteit.
	 * 
	 * @param Integer - val
	 */
	public void setDateAcceleration(int val) {
		this.dateAcceleration = val;
	}
	
	/**
	 * Geeft de tijd in het aantal milliseconden 
	 * vanaf January 1, 1970, 00:00:00 GMT 
	 * 
	 * @return long
	 */
	public static long getTimeInMillis() {
		return ms;
	}

	/**
	 * @param Integer - minus
	 * 
	 * @return Calendar
	 */
	public static Calendar getTimer(int minus) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(ms-minus));
		return calendar;
	}

	/**
	 * @return Calendar
	 */
	public static Calendar getTimer() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(ms));
		return calendar;
	}
}