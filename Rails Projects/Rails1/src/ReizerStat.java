import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.tigam.railcab.algoritme.BaanManager;
import org.tigam.railcab.algoritme.Reis;
import org.tigam.railcab.algoritme.ReisManager;
import org.tigam.railcab.algoritme.Reiziger;
import org.tigam.railcab.algoritme.Simulatie;

public class ReizerStat {
	private JFrame frame;
	private JPanel rStats;
	private JLabel naam, reisTijd, wachtTijd;
	public int rTijd, wTijd, ID, minuut = 0, seconde=0;
	public String nReiziger, Str_wT, Str_rT;
	private ImageIcon bg;
	private JButton close;
	
	//krijg de waardes van TaxiStat en zet ze in variabele
	public ReizerStat(String nReiziger, String Str_wT, String Str_rT){
		frame = new JFrame("Reiziger Statistieken"); 
		bg = new ImageIcon( "src/reiziger_stats.gif" );

		this.nReiziger = nReiziger;
		this.ID = ID;
		
		rStats = new JPanel(){
			protected void paintComponent(Graphics g)
			{
				g.drawImage(bg.getImage(), 0, 0, null);
				
				super.paintComponent(g);
			}
		};
			
		
		//zet in de label de naam van de reiziger (meegekregen van TaxiStat)
		naam = new JLabel(nReiziger);
		naam.setLocation(20,60);
		naam.setSize(200,100);
		naam.setFont( new Font( "Arial", Font.BOLD, 16) );	
		
		//de totale reistijd(meegekregen van TaxiStats)
		reisTijd = new JLabel (Str_rT + " minuten");
		reisTijd.setLocation(20,200);
		reisTijd.setSize(200,100);
		reisTijd.setFont( new Font( "Arial", Font.BOLD, 16) );
		
		//wachttijd in station(meegekregen van TaxiStats)
		wachtTijd = new JLabel (Str_wT + " minuten");
		wachtTijd.setLocation(20,125);
		wachtTijd.setSize(200,100);
		wachtTijd.setFont( new Font( "Arial", Font.BOLD, 16) );
		
		//voeg alles toe aan het frame
	frame.add(naam);
	frame.add(reisTijd);
	frame.add(wachtTijd);
	
	
	rStats.setOpaque( false );
	frame.getContentPane().add(rStats, BorderLayout.CENTER);
	frame.setSize( 300, 300);
	frame.setVisible( true );
	
	}
	
//inwendige klasse voor het afhandelen van het sluiten van het frame
class Close implements ActionListener{
	public void actionPerformed(ActionEvent e){
		frame.dispose();
		}
	}
	
}
