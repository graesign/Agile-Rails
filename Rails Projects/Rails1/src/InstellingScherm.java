/*-----------------------------*/
/* This Program was made by:  */
/* Isabelle Hanraads           */
/* Any kind of fraude will be  */
/* seen as a serious crime and */
/* will be punished by a money */
/* penalty of 25,-             */
/*_____________________________*/

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


//Frame-klasse voor het Instellingen pop-up venster
/**
 * @author dareal
 * Deze klasse tekent een paneel in een frame en wordt gebruikt voor de taxi instellingen.
 * Je kan de snelheid van de taxi instellen en de reixigers instaptijd.
 * De snelheid moet tussen 60 en 300 km/u liggen en de instaptijd mag vanaf 2 seconde en
 * hoger zijn.
 *
 */
public class InstellingScherm extends Frame {
	private JFrame frame;
	private JPanel instellingscherm;
	private JTextField snelheid,wachttijd;
	private ImageIcon taxi;
	private JButton okay, annuleren;
	private Controller controller;


	public InstellingScherm(){
		taxi = new ImageIcon("src/taxi.gif");
		frame = new JFrame("Baaninstellingen");
		controller = new Controller();
		instellingscherm = new JPanel(){
			protected void paintComponent(Graphics g)
			{
				/**
				 * Teken het plaatje op het volledig scherm
				 **/
				g.drawImage(taxi.getImage(), 0, 0, null);

				super.paintComponent(g);
			}
		};
		addWindowListener( new Sluiten());

		/**
		 * knoppen toevoegen
		 * */
		okay = new JButton("OK");
		okay.setSize(53,30);
		okay.setLocation(110,230);
		okay.addActionListener(new Ok());

		annuleren = new JButton("Annuleren");
		annuleren.setSize(100,30);
		annuleren.setLocation(180,230);
		annuleren.addActionListener(new Annuleren());

		/**
		 * Textfield aanmaken
		 * */
		snelheid = new JTextField();
		snelheid.setSize(120,25);
		snelheid.setLocation(20, 100);

		wachttijd = new JTextField();
		wachttijd.setSize(120,25);
		wachttijd.setLocation(20,180);

		/**
		 * Objecten toevoegen in de frame
		 * */
		frame.add(snelheid);
		frame.add(wachttijd);
		frame.add(okay);
		frame.add(snelheid);
		frame.add(wachttijd);
		frame.add(annuleren);
		
		instellingscherm.setOpaque( false );
		frame.getContentPane().add(instellingscherm, BorderLayout.CENTER);
		frame.setSize( 300, 300);
		frame.setVisible( true );
	}
	
	/**
	 * inwendige klasse voor het sluiten van de scherm
	 * */
	class Annuleren implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			frame.dispose();

		}

	} 
	/**
	 * inwendige klasse voor als je op het ok knop druk om de gegevens toe te voegen
	 * 
	 * */
	class Ok implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try{
				String snelheid1 = snelheid.getText();
				int snelHeid = Integer.parseInt(snelheid1);
				if(snelHeid >= 60 && snelHeid <= 300){

					controller.setSnelheid(snelHeid);
				}
				else {
					JOptionPane.showMessageDialog(frame, " De betreffende snelheid " + snelheid1 + " is geen juiste invoer.\n Het moet tussen 60 en 300 km/u liggen","Fout",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			catch(NumberFormatException e1){  
			}
			frame.dispose();
			try{
				String wachttijd1 = wachttijd.getText();
				int wachttijd = Integer.parseInt(wachttijd1);
				if(wachttijd >= 2){
					controller.setWachttijd(wachttijd);
				}
				else {
					JOptionPane.showMessageDialog(frame, " De betreffende limiet, " + wachttijd1 + " is geen juiste invoer.\n U dient een getal groter of gelijk aan 2 in te voeren","Fout",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			catch(NumberFormatException e1){
			} 

			frame.dispose();
		}

	}
	/**
	 * inwendige klasse voor het sluiten van het scherm
	 * */
	private class Sluiten extends WindowAdapter {
		public void windowClosing( WindowEvent e){
			frame.dispose();

		}
	}

}

