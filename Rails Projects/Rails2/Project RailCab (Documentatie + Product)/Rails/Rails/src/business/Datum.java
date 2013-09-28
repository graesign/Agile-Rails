package business;

import java.util.Calendar;

/*
 * @version 0.7
 */
public class Datum{
	Calendar calendar;
	public static int versnellingsFactor = 6;

	String datum = "";

	/* Constructor */
	
	public Datum(){
		calendar = Calendar.getInstance();
	}
	
	/* methods */
	
	/**
	 * Gives the date as an integer. The interger value makes it easier to calculate the time. 
	 * @param jaar
	 * @param maand
	 * @param dag
	 * @param uur
	 * @param minuut
	 * @param seconde
	 */
	public long getDatumInSec( int jaar, int maand, int dag, int uur, int minuut, int seconde , int millisec ){
		datum = "";
		if( maand < 10 ) datum += "0";
		datum += maand;
		if( dag < 10 ) datum += "0";
		datum += dag;
		if( uur < 10 ) datum += "0";
		datum += uur;
		if( minuut < 10 ) datum += "0";
		datum += minuut;
		if( seconde < 10 ) datum += "0";
		datum += seconde;
		if( millisec < 10 ) datum += "00";
		else if( millisec < 100 ) datum += "0";
		datum += millisec; 

		calendar.set(jaar, maand, dag, uur, minuut, seconde );

		return calendar.getTimeInMillis();
	}

	public long getActueleDatumInSec(){
		Calendar c = Calendar.getInstance();
		return c.getTimeInMillis();
		
		//return Long.parseLong(datum);
	}

	public long addTijd( long tijd, int addTime ){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tijd);

		int addSec = (addTime % 100000);
		int addMin = addTime %  10000000 - addSec;
		
		addMin = addMin / 100000;
		c.add(Calendar.MINUTE, addMin);

		
		addSec = addSec / 1000;
		c.add(Calendar.SECOND, addSec);

		return c.getTimeInMillis();
	}
	
	public long addTijd( long tijd, int addMin, int addSec ){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tijd);
		c.add(Calendar.MINUTE, addMin);
		c.add(Calendar.SECOND, addSec);
		return c.getTimeInMillis();
	}
	
	public int versnelTijd(int tijd){	
		long tempTijd = getActueleDatumInSec();
		long tempTijd2 = addTijd(tempTijd, tijd);
		long tempTijd3 = (tempTijd2 - tempTijd) /  versnellingsFactor;
		return (int)tempTijd3;
	}
	
	public void setVersnellingsfactor(int versnellingsFactor){
		 this.versnellingsFactor = versnellingsFactor;
	}
	
	public int getVersnellingsfactor(){
		return versnellingsFactor;
	}
}