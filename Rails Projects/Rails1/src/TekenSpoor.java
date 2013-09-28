import java.awt.BorderLayout;

import java.awt.Container;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

import javax.swing.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TekenSpoor {
	private JFrame frame;
	private JPanel spoor;
	private JTextField aStation, aTaxi;
	private JButton submit;
	private ImageIcon pSpoor;
	private int x, y = 0;
	private Paneel paneel;
	private JLabel notification, taxi_not;
	private Controller controller;

	public TekenSpoor(){
	}

	public TekenSpoor(Paneel paneel){
		this.paneel = paneel;

		pSpoor = new ImageIcon("src/spoor.gif");
		frame = new JFrame("Spoor Instellingen"); 
		spoor = new JPanel()

		{
			protected void paintComponent(Graphics g)
			{

				g.drawImage(pSpoor.getImage(), 0, 0, null);

				super.paintComponent(g);
			}
		};

		notification = new JLabel();
		notification.setSize(300,25);
		notification.setLocation(20, 200);
		//controller = new Controller();

		taxi_not = new JLabel();
		taxi_not.setSize(300,25);
		taxi_not.setLocation(20, 220);

		aStation = new JTextField();
		aStation.setSize(120,25);
		aStation.setLocation(20, 90);

		aTaxi = new JTextField();
		aTaxi.setSize(120,25);
		aTaxi.setLocation(20,180);

		submit = new JButton("OK");
		submit.setSize(53,30);
		submit.setLocation(110,230);
		submit.addActionListener(new Submit());


		//voeg alles toe
		frame.add( taxi_not );
		frame.add( aStation );
		frame.add( aTaxi );
		frame.add( submit );
		frame.add( notification );

		//tekent het scherm
		spoor.setOpaque( false );	
		frame.getContentPane().add(spoor, BorderLayout.CENTER);
		frame.setSize( 300, 300);
		frame.setVisible( true );

	}

	class Submit implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//probeer de waarde uit de tekstvakken te halen...
			try{
				//haal de waardes op uit het tekstvak voor het station
				String aStat = aStation.getText();
				//...en zet het in een variabele
				int aantal = Integer.parseInt(aStat);	
				
				//haal de waardes op uit het tekstvak voor de taxi
				String aTax = aTaxi.getText();
				//...en zet het in een variabele
				int aantTaxi = Integer.parseInt(aTax);
				
				//kijk nu of het wel geldige waardes zijn
				if(aantTaxi > 1 && aantTaxi <= 8 && aantal >1 && aantal <=8){
					//en kies dan een situatie aan de hand van de ingevoerde waarde
					//vervolgens stuurt hij een opdracht naar de paneel klasse
					switch (aantal) {
					case 0: break;
					case 1: break;
					case 2: paneel.tweeStations(); break;
					case 3: paneel.drieStations(); break;
					case 4: paneel.vierStations(); break;
					case 5: paneel.vijfStations(); break;
					case 6: paneel.zesStations(); break;
					case 7: paneel.zevenStations(); break;
					case 8: paneel.achtStations(); break;
					default: break;
					}
				}
				//voor de taxi's
				//hier staat een extra opdracht, voor het doorgeven aan het algoritme
				if(aantTaxi > 1 && aantTaxi <= 8 && aantal >1 && aantal <=8){
					paneel.setTaxi(aantTaxi);

				}
				//en kies dan een situatie aan de hand van de ingevoerde waarde
				//vervolgens stuurt hij een opdracht naar de paneel klasse
				switch (aantTaxi) {
				case 0: break;
				case 1: break;
				case 2: paneel.tweeTaxi(); break;
				case 3: paneel.drieTaxi(); break;
				case 4: paneel.vierTaxi(); break;
				case 5: paneel.vijfTaxi(); break;
				case 6: paneel.zesTaxi(); break;
				case 7: paneel.zevenTaxi(); break;
				case 8: paneel.achtTaxi(); break;
				default: break;
				}

				//als alle waardes correct ingevuld zijn, sluit dan netjes het scherm af
				if(!( aantal <= 1 || aantTaxi < 2 || aantal > 8 || aantTaxi > 8)){
					frame.dispose();
				}
				//als er 1 of meerdere verkeerde waardes ingevoerd zijn, geef dan een waarschuwing
				if( aantal <= 1 || aantTaxi < 2 || aantal > 8 || aantTaxi > 8){
					JOptionPane.showMessageDialog(frame, "Niet meer dan 8 taxi's en minder dan 2.\nNiet meer dan 8 stations en minder dan 2.","Fout",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			
			//als er helemaal geen waardes ingevuld zijn, geef ook een waarschuwing
			catch(NumberFormatException e1){
				JOptionPane.showMessageDialog(frame, "Vul een geldige waarde in.","Fout",
						JOptionPane.ERROR_MESSAGE);
			}




		}		
	}
}
