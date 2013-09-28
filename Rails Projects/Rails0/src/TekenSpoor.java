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

public class TekenSpoor extends View  {
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
                maakGUIItems();
	}
        
        protected void maakGUIItems() {
            
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
                spoor.setOpaque( false );
                
                JComponent[] items = {notification=new JLabel(),taxi_not=new JLabel(),aStation=new JTextField(),aTaxi=new JTextField(),submit=new JButton("OK")};
                Dimension[] lokaties = {new Dimension(20,200),new Dimension(20,220),new Dimension(20,90),new Dimension(20,180),new Dimension(110,230)};
                Dimension[] afmetingen = {new Dimension(300,25),new Dimension(300,25),new Dimension(120,25),new Dimension(120,25), new Dimension(53,30)};
                setGUIItemsLokaties(items,lokaties);
                setGUIItemsAfmetingen(items, afmetingen);
                voegGUIItemsToe(frame.getContentPane(), items);
                submit.addActionListener(new Submit());
	
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
					paneel.maakStations(aantal);
				}
				//voor de taxi's
				//hier staat een extra opdracht, voor het doorgeven aan het algoritme
				if(aantTaxi > 1 && aantTaxi <= 8 && aantal >1 && aantal <=8){
					paneel.setTaxi(aantTaxi);

				}
				//en kies dan een situatie aan de hand van de ingevoerde waarde
				//vervolgens stuurt hij een opdracht naar de paneel klasse
				paneel.maakTaxisZichtbaar(aantTaxi);

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
