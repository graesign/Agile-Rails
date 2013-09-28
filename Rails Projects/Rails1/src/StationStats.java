

import java.awt.BorderLayout;
import java.awt.Component;

import java.awt.Frame;

import java.awt.Graphics;

import java.awt.Font;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.*;

import org.tigam.railcab.algoritme.BaanManager;

import org.tigam.railcab.algoritme.Simulatie;

/**
 * @author dareal
 *	Deze klasse toont de station statustieken. En wordt aangeroepen zorda je als gebruiker
 * op een station knop drukt.
 */
public class StationStats {

	private JFrame frame;

	private JPanel sStats;

	private JLabel naam, r_inStation, t_inStation;

	public int r_inStat, t_inStat;

	public String nStation,station;

	private ImageIcon bg;

	private JButton close;

	private JComboBox reizigerCombo;

	private ArrayListComboBoxModel reizigerID;

	private ArrayList<String> reizigerNaam;

	private Simulatie simulatie;

	private BaanManager baanManager;

	private Controller controller;
	private String [] spatie;
	/**
	 * Om de klasse aan te roepen wordt de constoctor gevult met de naam van de station en
	 * de aantal reiziger op het station en de aantal taxi op het station
	 **/
	public StationStats(String nStation, int r_inStat, int t_inStat){

		frame = new JFrame("Station Statistiek");

		bg = new ImageIcon( "src/station_stats.gif" );

		sStats = new JPanel(){

			protected void paintComponent(Graphics g)

			{

				g.drawImage(bg.getImage(), 0, 0, null);

				super.paintComponent(g);

			}

		};

		simulatie = Simulatie.getInstantie();

		baanManager = simulatie.getBaanManager();

		controller = new Controller();
		/* 
		 * Hieronder wordt een label aangemaakt met de aantal reizigers op het station
		 * */
		String Str_rS = Integer.toString(r_inStat);

		String Str_tS = Integer.toString(t_inStat);
		/*Hier wordt een label gemaakt met de station naam
		 * */
		naam = new JLabel(nStation);

		naam.setLocation(20,60);

		naam.setSize(200,100);

		naam.setFont( new Font( "Arial", Font.BOLD, 16) );
		//label wordt aangemaakt
		r_inStation = new JLabel (Str_rS +" reizigers");

		r_inStation.setLocation(20,125);

		r_inStation.setSize(200,100);

		r_inStation.setFont( new Font( "Arial", Font.BOLD, 16) );
		// Label wordt aangemaakt met de aantal taxis
		t_inStation = new JLabel (Str_tS + " taxi's");

		t_inStation.setLocation(20,200);

		t_inStation.setSize(200,100);

		t_inStation.setFont( new Font( "Arial", Font.BOLD, 16) );
		/**
		 * Hieronder wordt een combobox aangemaakt en gevult met de aantal rezeizigers
		 * op dat station.
		 * */
		reizigerCombo = new JComboBox();
		reizigerCombo.setLocation(140, 165);
		reizigerCombo.setSize(100,25);
		reizigerCombo.setModel(new DefaultComboBoxModel(baanManager.getStation(nStation).getReizigerNamen()));
		reizigerCombo.setSelectedIndex(-1);
		
		reizigerCombo.addActionListener( new ReizigerStat());

		station = nStation;

		close = new JButton ("Sluiten");

		close.addActionListener (new Close());
		
		// Alles wordt toegevoegd
		frame.add(naam);

		frame.add(r_inStation);

		frame.add(t_inStation);

		frame.add(reizigerCombo);

		sStats.setOpaque( false );

		frame.getContentPane().add(sStats, BorderLayout.CENTER);

		frame.setSize( 300, 300);

		frame.setVisible( true );


	}
	public String getStation(){

		return nStation;

	}
	/**
	 * inwendige klasse voor het sluiten van het scherm
	 * */
	class Close implements ActionListener{

		public void actionPerformed(ActionEvent e){

			frame.dispose();

		}

	}
	/**
	 * inwendige klasse voor het aanroepen van de reiziger statustieken. Je ziet hoelang een reiziger wacht op een station
	 * Je ziet de bestemming van een reiziger
	 * Tevens verschijnt deze scherm als je op een reiziger klikt in de combobox. De getselectedItem zorgd daarvoor
	 * 
	 * */

	class ReizigerStat implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			String reis = (String) reizigerCombo.getSelectedItem();

			int ReisTijdUur = (int) (baanManager.getStation(station).getReiziger(reis).getWachtTijd()/1000/60/60);

			int ReistijdSec = (int) (baanManager.getStation(station).getReiziger(reis).getWachtTijd()/1000);

			int ReistijdMin = (int) baanManager.getStation(station).getReiziger(reis).getWachtTijd()/1000/60;

			System.out.println(reis +

					baanManager.getStation(station).getReiziger(reis).getReisDetails().getBestemming().getNaam()

					+

					baanManager.getStation(station).getReiziger(reis).getWachtTijd());

			System.out.println(reis);

			System.out.println(station);

			JOptionPane optionPane = new ReizigerStatistiek();

			optionPane.setMessage("Naam: " + reis + "\n" + "Bestemming: " +

					baanManager.getStation(station).getReiziger(reis).getReisDetails().getBestemming().getNaam()

					+ "\n"+ "Wachttijd: "+ ReisTijdUur +":"+ controller.secondeCheck(ReistijdMin) +":"+ controller.secondeCheck(ReistijdSec));

			optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);

			JDialog dialog = optionPane.createDialog(null, "Reizigers Statistiek");

			dialog.setVisible(true);
			reizigerCombo.setSelectedIndex(-1);

		}

	}

	class ReizigerStatistiek extends JOptionPane {

		ReizigerStatistiek() {

		}

	}

}


