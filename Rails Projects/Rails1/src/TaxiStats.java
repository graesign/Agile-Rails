       /*-----------------------------*/
      /* This Program was made by:	 */
     /* Isabelle Hanraads           */
    /* Any kind of fraude will be  */
   /* seen as a serious crime and */
  /* will be punished by a money */
 /* penalty of 25,-             */ 
/*_____________________________*/

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.tigam.railcab.algoritme.*;


public class TaxiStats extends Frame{	
	private JFrame frame;
	private JPanel tStats;
	private JLabel positie, r_inTaxi, wTijd, rTijd, error, message, message2, bestemming;
	private ImageIcon bg, reiziger;
	public String pos, Str_r_inT, Str_rT_t;
	public int r_inT;
	private String rT, Str_best, rNaam, naam, naamR1, naamR2, naamR3, naamR4, naamR5;
	private JButton close;
	private JButton reiziger1, reiziger2, reiziger3, reiziger4, reiziger5;
	private Reis reis;
	private Reiziger reizig;
	private Controller controller;
	private String[] stringArray;
	private BaanManager baanManager;
	private ReisManager reisManager;
	private int ID = 0, i, y;
	
	
	
	public TaxiStats(String oposs, int r_inT, String Str_rT_t, int ID){
		rNaam = rNaam;
		this.ID = ID;
		frame = new JFrame("Taxi Statistieken"); 
		bg = new ImageIcon( "src/taxi_bg.gif" );
		reiziger = new ImageIcon( "src/reiziger.gif");
		tStats = new JPanel(){
			protected void paintComponent(Graphics g)
			{
				g.drawImage(bg.getImage(), 0, 0, null);
				
				super.paintComponent(g);
			}
		};
		
		baanManager = Simulatie.getInstantie().getBaanManager();
		reisManager = Simulatie.getInstantie().getReisManager();
		
		// convert alle integer statistieken naar Strings
		String Str_r_inT = Integer.toString(r_inT);
			
		//voeg de knoppen voor de reizigerstats toe
		reiziger1 = new JButton(reiziger);
		reiziger1.setLocation(212, 106);
		reiziger1.setSize(15,30);
		reiziger1.setVisible( false );
		reiziger1.addActionListener(new Reiziger1());
		
		reiziger2 = new JButton(reiziger);
		reiziger2.setLocation(245, 106);
		reiziger2.setSize(15,30);
		reiziger2.setVisible( false );
		reiziger2.addActionListener(new Reiziger2());
		
		reiziger3 = new JButton(reiziger);
		reiziger3.setLocation(228, 135);
		reiziger3.setSize(15,30);
		reiziger3.setVisible( false );
		reiziger3.addActionListener(new Reiziger3());
		
		reiziger4 = new JButton(reiziger);
		reiziger4.setLocation(212, 165);
		reiziger4.setSize(15,30);
		reiziger4.setVisible( false );
		reiziger4.addActionListener(new Reiziger4());
		
		reiziger5 = new JButton(reiziger);
		reiziger5.setLocation(245, 165);
		reiziger5.setSize(15,30);
		reiziger5.setVisible( false );
		reiziger5.addActionListener(new Reiziger5());
		
		
		// voeg alle labels toe!
		positie = new JLabel(oposs);
		positie.setLocation(27,67);
		positie.setSize(200,100);
		positie.setFont( new Font( "Arial", Font.BOLD, 16) );

		r_inTaxi = new JLabel(Str_r_inT + " reizigers");
		r_inTaxi.setLocation(27,115);
		r_inTaxi.setSize(200,100);
		r_inTaxi.setFont( new Font( "Arial", Font.BOLD, 16) );
		
		rTijd = new JLabel (Str_rT_t + " minuten");
		rTijd.setLocation(27,170);
		rTijd.setSize(200,100);
		rTijd.setFont( new Font( "Arial", Font.BOLD, 16) );
		
		bestemming = new JLabel();
		bestemming.setLocation(27,220);
		bestemming.setSize(200,100);
		bestemming.setFont( new Font( "Arial", Font.BOLD, 16) );
		
		error= new JLabel ();
		error.setLocation(27,270);
		error.setSize(200,100);
		error.setFont( new Font( "Arial", Font.BOLD, 15) );
		
		message= new JLabel ();
		message.setLocation(27,290);
		message.setSize(200,100);
		message.setFont( new Font( "Arial", Font.BOLD, 15) );
		
		message2= new JLabel ();
		message2.setLocation(27,310);
		message2.setSize(200,100);
		message2.setFont( new Font( "Arial", Font.BOLD, 15) );
		
		close = new JButton ("Sluiten");
		close.setLocation(175, 370);
		close.setSize(100,25);
		close.addActionListener( new Close());
		
		frame.add(bestemming);
		frame.add(message2);
		frame.add(message);
		frame.add(error);
		frame.add(positie);
		frame.add(r_inTaxi);
		frame.add(rTijd);
		frame.add( close );
		frame.add( reiziger1 );
		frame.add( reiziger2 );
		frame.add( reiziger3 );
		frame.add( reiziger4 );
		frame.add( reiziger5 );

		tStats.setOpaque( false );
		frame.getContentPane().add(tStats, BorderLayout.CENTER);
		frame.setSize( 300, 450);
		frame.setVisible( true );
		
		//kijk of het aantal reizigers in een door 'ID' gekozen taxi, niet op nul staat.
		//set in de variabele Str_best, de bestemming uit de baanManager.
		//zet dan de tekst voor Label 'bestemming' op de bovenstaande variabele
		if(!(baanManager.getTaxi(ID).getAantalReizigers() == 0)){
			Str_best = baanManager.getTaxi(ID).getReis().getReisDetails().getBestemming().getNaam();
			bestemming.setText(Str_best);
			}
		
		//zet het aantal reizigerbuttons op variabele r_inT, wat doorgegeven wordt via de constructor
		switch(r_inT){
		case 0: break;
		case 1: reiziger1.setVisible(true); break;
		case 2: reiziger1.setVisible(true); reiziger2.setVisible(true); break;
		case 3: reiziger1.setVisible(true);reiziger2.setVisible(true);reiziger3.setVisible(true); break;
		case 4: reiziger1.setVisible(true);reiziger2.setVisible(true);reiziger3.setVisible(true);
				reiziger4.setVisible(true); break;
		case 5: reiziger1.setVisible(true);reiziger2.setVisible(true);reiziger3.setVisible(true);
				reiziger4.setVisible(true); reiziger5.setVisible(true); break;
		default: break;
		}
		
		//kijk voor iedere waarde van 'i' (totdat deze gelijk is het aantal reizigers in een taxi
		//gekozen met 'ID', zolang deze kleiner is, tel dan 1 bij 'i' op.
		
		//zet voor iedere keer dat 'i' kleiner is dan het aantal reizigers(gehaald uit de baanmanager)
		//de variabele String 'rNaam' op de naam van de reiziger (gehaald uit een array, uit de BaanManager)
		for (i=0; i < baanManager.getTaxi(ID).getAantalReizigers(); i++){
			System.out.println("Naam: " + baanManager.getTaxi(ID).getReizigerNamen()[i]);
			System.out.println("index: " +i);
			rNaam = baanManager.getTaxi(ID).getReizigerNamen()[i];
			//kijk of i voldoet aan de voorwaarde
			//en zet dan de variabele 'rNaam' in de variabele per reiziger 'naamR1, naamR2, etc'
			if( i == 0){
				naamR1 = rNaam;
			}
			
			if( i == 1){
				naamR2 = rNaam;
			}
			
			if( i == 2){
				naamR3 = rNaam;
			}
			
			if( i == 3){
				naamR4 = rNaam;
			}
			
			if( i == 4){
				naamR5 = rNaam;
			}
		}
	}
	//kijk of het aantal seconde niet boven de 60 uit komt...
	public int secondeCheck(int sec){
		for(int i = 0; sec > 60; i++){
			sec = sec - 60;
		}
		return sec;
	}
	
	//interne klasse voor reiziger 1, voor de andere reizigers is het precies hetzelfde
	class Reiziger1 implements ActionListener{
		public void actionPerformed(ActionEvent e){
			
			//kijk of de taxi reiziger met de naam 'naamR1' bevat.
			if( baanManager.getTaxi(ID).bevatReiziger(naamR1)){
				
			String Str_wT_r;
			String Str_rT_r;
			
			//haal de wachttijd op station uit de baanManager.
			int minuut = (int) (baanManager.getTaxi(ID).getReiziger(naamR1).getWachtTijd()/1000/60);
			int seconde = (int) (baanManager.getTaxi(ID).getReiziger(naamR1).getWachtTijd()/1000);
			Str_wT_r = minuut +"."+secondeCheck(seconde);
			
			//haal de reistijd uit de baanManager.
			int min = (int) (baanManager.getTaxi(ID).getReis().getReistijdBestemming()/1000/60);
			int sec = (int) (baanManager.getTaxi(ID).getReis().getReistijdBestemming()/1000);
			Str_rT_r = min + "."+secondeCheck(sec);
			//maak een nieuwe instantie met de zojuist gekregen waardes
			new ReizerStat(naamR1,Str_wT_r, Str_rT_r);
			
			}
			//zodra de reiziger is uitgestapt, geef dan een waarschuwing.
			else{
				JOptionPane.showMessageDialog(frame, naamR1 + "is al uitgestapt!\nOpen het scherm opnieuw,\nvoor de juiste gegevens.","Fout",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	class Reiziger2 implements ActionListener{
		public void actionPerformed(ActionEvent e){
			
			if( baanManager.getTaxi(ID).bevatReiziger(naamR2)){
			String Str_wT_r ;
			String Str_rT_r;
			
			//wachttijd op station
			int minuut = (int) (baanManager.getTaxi(ID).getReiziger(naamR2).getWachtTijd()/1000/60);
			int seconde = (int) (baanManager.getTaxi(ID).getReiziger(naamR2).getWachtTijd()/1000);
			Str_wT_r = minuut +"."+secondeCheck(seconde);
			
			//reistijd + wachttijd op station
			int min = (int) (baanManager.getTaxi(ID).getReis().getReistijdBestemming()/1000/60);
			int sec = (int) (baanManager.getTaxi(ID).getReis().getReistijdBestemming()/1000);
			Str_rT_r = min + "."+secondeCheck(sec);
			new ReizerStat(naamR2, Str_wT_r, Str_rT_r);
			}
			else{
				JOptionPane.showMessageDialog(frame, naamR2 + "is al uitgestapt!\nOpen het scherm opnieuw,\nvoor de juiste gegevens.","Fout",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	class Reiziger3 implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if( baanManager.getTaxi(ID).bevatReiziger(naamR3)){

			String Str_wT_r;
			String Str_rT_r;

			//wachttijd op station
			int minuut = (int) (baanManager.getTaxi(ID).getReiziger(naamR3).getWachtTijd()/1000/60);
			int seconde = (int) (baanManager.getTaxi(ID).getReiziger(naamR3).getWachtTijd()/1000);
			Str_wT_r = minuut +"."+secondeCheck(seconde);
			
			//reistijd + wachttijd op station
			int min = (int) (baanManager.getTaxi(ID).getReis().getReistijdBestemming()/1000/60);
			int sec = (int) (baanManager.getTaxi(ID).getReis().getReistijdBestemming()/1000);
			Str_rT_r = min + "."+secondeCheck(sec);
				new ReizerStat(naamR3, Str_wT_r, Str_rT_r);
			}
			else{
				JOptionPane.showMessageDialog(frame, naamR3 + "is al uitgestapt!\nOpen het scherm opnieuw,\nvoor de juiste gegevens.","Fout",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	class Reiziger4 implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if( baanManager.getTaxi(ID).bevatReiziger(naamR4)){

			String Str_wT_r;
			String Str_rT_r;

			//wachttijd op station
			int minuut = (int) (baanManager.getTaxi(ID).getReiziger(naamR4).getWachtTijd()/1000/60);
			int seconde = (int) (baanManager.getTaxi(ID).getReiziger(naamR4).getWachtTijd()/1000);
			Str_wT_r = minuut +"."+secondeCheck(seconde);
			
			//reistijd + wachttijd op station
			int min = (int) (baanManager.getTaxi(ID).getReis().getReistijdBestemming()/1000/60);
			int sec = (int) (baanManager.getTaxi(ID).getReis().getReistijdBestemming()/1000);
			Str_rT_r = min + "."+secondeCheck(sec);
			
			new ReizerStat(naamR4, Str_wT_r, Str_rT_r);
			}
			else{
				JOptionPane.showMessageDialog(frame, naamR4 + "is al uitgestapt!\nOpen het scherm opnieuw,\nvoor de juiste gegevens.","Fout",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}
	class Reiziger5 implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if( baanManager.getTaxi(ID).bevatReiziger(naamR5)){

			String Str_wT_r;
			String Str_rT_r;

			//wachttijd op station
			int minuut = (int) (baanManager.getTaxi(ID).getReiziger(naamR5).getWachtTijd()/1000/60);
			int seconde = (int) (baanManager.getTaxi(ID).getReiziger(naamR5).getWachtTijd()/1000);
			Str_wT_r = minuut +"."+secondeCheck(seconde);
						
			//reistijd + wachttijd op station
			int min = (int) (baanManager.getTaxi(ID).getReis().getReistijdBestemming()/1000/60);
			int sec = (int) (baanManager.getTaxi(ID).getReis().getReistijdBestemming()/1000);
			Str_rT_r = min + "."+secondeCheck(sec);
				new ReizerStat(naamR5, Str_wT_r, Str_rT_r);
			}
			else{
				JOptionPane.showMessageDialog(frame, naamR5 + "is al uitgestapt!\nOpen het scherm opnieuw,\nvoor de juiste gegevens.","Fout",
						JOptionPane.ERROR_MESSAGE);
			}
		
		}
	}
	
	class Close implements ActionListener{
		public void actionPerformed(ActionEvent e){
			frame.dispose();
		}
		
	}
}
