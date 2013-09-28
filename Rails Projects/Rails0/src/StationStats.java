

import java.awt.BorderLayout;
import java.awt.Component;

import java.awt.Frame;

import java.awt.Graphics;

import java.awt.Font;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.*;
import java.awt.Dimension;

import org.tigam.railcab.algoritme.BaanManager;

import org.tigam.railcab.algoritme.Simulatie;

/**
 * @author dareal
 *	Deze klasse toont de station statustieken. En wordt aangeroepen zorda je als gebruiker
 * op een station knop drukt.
 */
public class StationStats extends View {

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
        
        String Str_tS;
        
	/**
	 * Om de klasse aan te roepen wordt de constoctor gevult met de naam van de station en
	 * de aantal reiziger op het station en de aantal taxi op het station
	 **/
	public StationStats(String nStation, int r_inStat, int t_inStat){
		simulatie = Simulatie.getInstantie();

		baanManager = simulatie.getBaanManager();

		controller = new Controller();
		this.nStation = nStation;
                this.r_inStat = r_inStat;
                this.t_inStat = t_inStat;
                station = nStation;
                
                Str_tS = Integer.toString(t_inStat);
                maakGUIItems();
	}
        protected void maakGUIItems() {
            frame = new JFrame("Station Statistiek");

            bg = new ImageIcon( "src/station_stats.gif" );

            sStats = new JPanel(){
                    protected void paintComponent(Graphics g)
                    {
                            g.drawImage(bg.getImage(), 0, 0, null);
                            super.paintComponent(g);
                    }
            };
            sStats.setOpaque( false );
            
            JComponent[] items = { naam=new JLabel(nStation), r_inStation=new JLabel(r_inStat+"reizigers"), t_inStation=new JLabel(t_inStat+"taxi's"), reizigerCombo=new JComboBox() };
            Dimension[] lokaties = { new Dimension(20,60), new Dimension(20,125), new Dimension(20,200), new Dimension(140,165) };
            Dimension[] afmetingen = { new Dimension(200,100), new Dimension(200,100), new Dimension(200,100), new Dimension(100,25) };
            setGUIItemsLokaties( items, lokaties );
            setGUIItemsAfmetingen( items, afmetingen );
            voegGUIItemsToe( frame.getContentPane(), items );
            
            naam.setFont( new Font( "Arial", Font.BOLD, 16) );
            r_inStation.setFont( new Font( "Arial", Font.BOLD, 16) );
            t_inStation.setFont( new Font( "Arial", Font.BOLD, 16) );
            reizigerCombo.setModel(new DefaultComboBoxModel(baanManager.getStation(nStation).getReizigerNamen()));
            reizigerCombo.setSelectedIndex(-1);
            reizigerCombo.addActionListener( new ReizigerStat());

            close = new JButton ("Sluiten");
            close.addActionListener (new Close());

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


