package helpers;

import java.util.GregorianCalendar;

/**
 * @author RailCab07_4
 */
public class Time 
{
    private long Start;

    public Time()
    {

    }

    public void setTime(String Trainid)
    {
        String date = (new GregorianCalendar().getTime().toString());
        String tijd = date.substring(11, 19);
        Start       = System.currentTimeMillis();
    }

    public String getCurrentCalendar()
    {
        String date = (new GregorianCalendar().getTime().toString());
        return date;
    }

    public String getTimeHoursSeconds()
    {
        String date = (new GregorianCalendar().getTime().toString());
        String tijd = date.substring(11, 19);
        return tijd;
    }

    public long getMillis()
    {
        long tijd = System.currentTimeMillis();
        return tijd;
    }
}