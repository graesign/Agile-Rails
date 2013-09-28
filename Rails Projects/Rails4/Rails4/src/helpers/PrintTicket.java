package helpers;

import gui.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import controller.TrackController;
import java.io.FileOutputStream;

/**
 * @author RailCab07_4
 */
public class PrintTicket 
{
    //private static final String tickedLoc   = "/home/erik/rcab/Ticket.pdf";
    private static final String tickedLoc   = "c:/Ticket.pdf";
    TrackController trackCont = TrackController.getInstance();
    
    public void drawTicket(String Location, String EndLocation, String VertrekTijd)
    {
        try{
            Time time           = new Time();
            Document document   = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance (document, new FileOutputStream(tickedLoc));
            document.open();
            document.add (new Paragraph ("Geldig: "        + time.getCurrentCalendar().toString().substring (0, 10), FontFactory.getFont (FontFactory.COURIER, 14, Font.BOLD)));
            document.add (new Paragraph ("VertrekTijd: "   + VertrekTijd, FontFactory.getFont (FontFactory.COURIER, 14, Font.BOLD)));
            document.add (new Paragraph ("Prijs: "         + this.CalcPrice (EndLocation) + " €", FontFactory.getFont (FontFactory.COURIER, 14, Font.BOLD)));
            document.add (new Paragraph ("BeginLocatie: "  + Location, FontFactory.getFont (FontFactory.COURIER, 14, Font.BOLD)));
            document.add (new Paragraph ("EindLocatie: "   + EndLocation, FontFactory.getFont (FontFactory.COURIER, 14, Font.BOLD)));

            document.close();
        } 
        catch (Exception e2) 
        { 
            e2.printStackTrace(); 
        } 
    }

    public int CalcPrice(String Location)
    {
        int price = 0;
        for (int i = 0; i < 8; i++)
        {
            Passenger ps = new Passenger();
            if (Location.equals (ps.CurrentLocation))
            {
                price = 8;
            }
            else if (TrackController.getInstance().stations[i].equals (Location))
            {
                price = (1 * i);
            }
        }

        return price;
    }
}
