package Gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import Database.MySQLConnection;
import java.awt.BasicStroke;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
/**
 *
 * @author Thomas
 */


public class DrawGraph {
    
private static final String GraphLocation = "c:/Graph.png";
private BufferedImage buff_Graph         = new BufferedImage (830, 480, BufferedImage.TYPE_INT_ARGB);
Graphics2D g                            = buff_Graph.createGraphics();


public DrawGraph()
{
}

public BufferedImage createGraph(String Objectname)
{   
    MySQLConnection ms = new MySQLConnection();
    System.out.println(ms.getSize(Objectname));
    int Size  = ( ms.getSize( Objectname ) );
    int Stepx = 900 / (Size * 2);
    int Stepy = 4;
    
    int[] y  = new int [Size],
          y2 = new int [Size],
          y3 = new int [Size * 2],
          x  = new int [Size * 2];
    
    long[] YCoords  = new long[Size],
           YCoords2 = new long[Size];
           YCoords  = ms.getTime(Objectname);
           YCoords2 = ms.getTimeArrival(Objectname);
           System.out.print("Wopper" + YCoords[0] +"\n");
    
    int k = 0;
    
    for (Long pos : YCoords)
    {
        y[k] = (pos.intValue());
        k++;
    }
    
    k = 0;
    for (Long pos : YCoords2)
    {
        y2[k] = (pos.intValue());
        k++;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    k = 0;
    while(k < y.length)
    {   
        //System.out.println("1 _" + y [ k ] );
        if(k + 1 < Size ){y[k]  = (y [k + 1]  - y[k]);}
        if(k+1 < Size){y[k]     = 240 - ( y [k] * Stepy );}
        k++;
    }
    
    k = 0;
    while(k < y.length)
    {   
        //System.out.println("2 _" + y2[ k ] );
        if(k + 1 < Size ){y2[k] = (y2[k + 1] - y2[k]);}
        if(k+1 < Size){y2[k]    = 240 - ( y2[k] * Stepy );}
        k++;
    }
    
    k=0;
    while(k < Size)
    {   
        if(k+1 < Size)
        {
            y3[k ]    = y [ k ];
            System.out.println( y  [ k ] );
            y3[k + 1] = y2[ k ];
            System.out.println( y2 [ k ] );
            k++;
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////
    
    for(int i = 0; i < x.length; i++ )
    {
        x[i] = Stepx*i;
    }
    
    g.setColor (Color.WHITE);
    g.fillRect (0, 0, 830, 480);
    g.setColor(Color.LIGHT_GRAY);
    g.setStroke (new BasicStroke (1));
    
    //draw Grid lines
    for(int i=1; i<20; i++)
    {
        g.drawLine(0, (24*i), 830, (24*i));
        g.drawLine((42*i), 0, (42*i), 480);
    }
    
    g.setColor(Color.RED);
    g.drawString("Grafiek van: " + Objectname, 10, 20);
    g.setColor(Color.RED);
    g.setStroke (new BasicStroke (10));
    g.drawPolyline(x, y3, (Size - 1));
    
    g.drawImage (this.buff_Graph, 0, 0, null);
    
    try {
            File outputfile = new File (GraphLocation);
            ImageIO.write (buff_Graph, "png", outputfile);
        } catch (IOException e) {
           e.printStackTrace();
        }
    return buff_Graph;
    
    }
    
}
