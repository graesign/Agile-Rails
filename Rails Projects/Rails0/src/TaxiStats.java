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
import java.awt.Dimension;
import javax.swing.*;

import org.tigam.railcab.algoritme.*;


public class TaxiStats extends View {	
	private JFrame frame;
	private JPanel tStats;
	private JLabel positie, r_inTaxi, wTijd, rTijd, error, message, message2, bestemming;
	private ImageIcon bg, reiziger;
	public String pos, Str_r_inT, Str_rT_t;
	public int r_inT;
	private String rT, Str_best, naam;
	private JButton close;
	private Reis reis;
	private Reiziger reizig;
	private Controller controller;
	private String[] stringArray;
	private BaanManager baanManager;
	private ReisManager reisManager;
	private int ID = 0, i, y;
	
	private String oposs;
	
	public TaxiStats(String oposs, int r_inT, String Str_rT_t, int ID){
		this.ID = ID;
		this.oposs = oposs;
                this.r_inT = r_inT;
                this.Str_rT_t = Str_rT_t;
                this.Str_r_inT = Integer.toString(r_inT);
                
		baanManager = Simulatie.getInstantie().getBaanManager();
		reisManager = Simulatie.getInstantie().getReisManager();
		
		maakGUIItems();
		
		//kijk of het aantal reizigers in een door 'ID' gekozen taxi, niet op nul staat.
		//set in de variabele Str_best, de bestemming uit de baanManager.
		//zet dan de tekst voor Label 'bestemming' op de bovenstaande variabele
		if(!(baanManager.getTaxi(ID).getAantalReizigers() == 0)){
			Str_best = baanManager.getTaxi(ID).getReis().getReisDetails().getBestemming().getNaam();
			bestemming.setText(Str_best);
			}
	}
        protected void maakGUIItems() {
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
			
		maakReizigerButtons( r_inT );
		JComponent[] items = {positie = new JLabel(oposs),r_inTaxi = new JLabel(Str_r_inT + " reizigers"),rTijd = new JLabel (Str_rT_t + " minuten"),bestemming = new JLabel(),error= new JLabel (),message= new JLabel (),message2= new JLabel ()};
                Dimension[] itemsLokaties = {new Dimension(27,67),new Dimension(27,115),new Dimension(27,170),new Dimension(27,220),new Dimension(27,270),new Dimension(27,290),new Dimension(27,310)};
                Font[] itemsFont = { new Font( "Arial", Font.BOLD, 16),new Font( "Arial", Font.BOLD, 16),new Font( "Arial", Font.BOLD, 16),new Font( "Arial", Font.BOLD, 16),new Font( "Arial", Font.BOLD, 15),new Font( "Arial", Font.BOLD, 15),new Font( "Arial", Font.BOLD, 15) };
                setGUIItemsLokaties( items, itemsLokaties );
                setGUIItemsAfmetingen( items, new Dimension(200,100) );
                setGUIItemsFont( items, itemsFont );
                voegGUIItemsToe( frame.getContentPane(), items );
                
		close = new JButton ("Sluiten");
		close.setLocation(175, 370);
		close.setSize(100,25);
		close.addActionListener( new Close());
                
		tStats.setOpaque( false );
		frame.getContentPane().add(tStats, BorderLayout.CENTER);
		frame.setSize( 300, 450);
		frame.setVisible( true );
        }
        private void maakReizigerButtons( int aantalReizigers ) {
            final int[] xLokatie = {212, 245, 228, 212, 245}, yLokatie = {106, 106, 135, 165, 165};
            for( int i = 0; i < aantalReizigers; i++ ){
                maakReizigerButton( baanManager.getTaxi(ID).getReizigerNamen()[i],
                                    xLokatie[i], yLokatie[i], 15, 30, (i <= aantalReizigers)?true:false );
            }
        }
        private void maakReizigerButton(String reizigerNaam, int xLokatie, int yLokatie, int breedte, int hoogte, boolean zichtbaar ) {
            JButton reizigerButton = new JButton(reiziger);
            reizigerButton.setSize(breedte,hoogte);
            reizigerButton.setVisible( zichtbaar );
            reizigerButton.setLocation(xLokatie, yLokatie);
            reizigerButton.addActionListener(new ReizigerButtonHandler(reizigerNaam));
            frame.add( reizigerButton );
        }
	//kijk of het aantal seconde niet boven de 60 uit komt...
	public int secondeCheck(int sec){
		for(int i = 0; sec > 60; i++){
			sec = sec - 60;
		}
		return sec;
	}
	class ReizigerButtonHandler implements ActionListener {
            private String reizigerNaam;
            
            protected ReizigerButtonHandler( String rNaam ) {
                this.reizigerNaam = rNaam;
            }
            
            public void actionPerformed(ActionEvent e){
                if( baanManager.getTaxi(ID).bevatReiziger(this.reizigerNaam)){
			String Str_wT_r;
			String Str_rT_r;

			//wachttijd op station
			int minuut = (int) (baanManager.getTaxi(ID).getReiziger(reizigerNaam).getWachtTijd()/1000/60);
			int seconde = (int) (baanManager.getTaxi(ID).getReiziger(reizigerNaam).getWachtTijd()/1000);
			Str_wT_r = minuut +"."+secondeCheck(seconde);
						
			//reistijd + wachttijd op station
			int min = (int) (baanManager.getTaxi(ID).getReis().getReistijdBestemming()/1000/60);
			int sec = (int) (baanManager.getTaxi(ID).getReis().getReistijdBestemming()/1000);
			Str_rT_r = min + "."+secondeCheck(sec);
				new ReizerStat(reizigerNaam, Str_wT_r, Str_rT_r);
			}
			else{
				JOptionPane.showMessageDialog(frame, reizigerNaam + "is al uitgestapt!\nOpen het scherm opnieuw,\nvoor de juiste gegevens.","Fout",
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
