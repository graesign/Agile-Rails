/*-----------------------------*/
/* This Program was made by:  */
/* Isabelle Hanraads           */
/* Any kind of fraude will be  */
/* seen as a serious crime and */
/* will be punished by a money */
/* penalty of 25,-             */
/*_____________________________*/
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComponent;

import org.tigam.railcab.algoritme.*;


//Het eigenlijke scherm!
/**
 * @author dareal
 *
 */
class Paneel extends View implements Observer{

	protected boolean simulatieGestart = false, simulatieStatus;
	private JButton start, stop, pauzeer, versnel, invoegen, moment;
	protected JButton t1_kn, t2_kn, t3_kn, t4_kn, t5_kn, t6_kn, t7_kn, t8_kn,
	s1_kn, s2_kn, s3_kn, s4_kn, s5_kn, s6_kn, s7_kn, s8_kn;
	public JLabel aant_reiziger, vert_punt, bestemming, status;
	protected JLabel hBaan, s1_label, s2_label, s3_label, s4_label, s5_label, s6_label, s7_label, s8_label, s_NaamLabel1,
	s_NaamLabel2, s_NaamLabel3, s_NaamLabel4 , s_NaamLabel5, s_NaamLabel6, s_NaamLabel7, s_NaamLabel8;;
	private JTextField aantal, tijdVeld;
	private JComboBox vertrek, aankomst;
	public ImageIcon achtergrond, hoofdbaan, taxi_img, station_img, station_img_vert,
	s123_img, s4_img, s567_img, s8_img;
	private Taxi taxi1, taxi2, taxi3, taxi4, taxi5, taxi6, taxi7, taxi8;
	private int i,y,z = 0, sec , min, uur, aantalStation; 
	private static final int LIMIET_REIZIGER = 101;
	private Controller controller;
	public String pos;
	private int tijd;
	private int pexels, teller, n = 0;
	public int r_inT, wT, rT, gB, g_wT, g_rT, taxi, tem;
	private boolean simIsGestart = false, simIsPauze = false;
        private JComponent[] taxiItems;

	private JFrame frame;
	private javax.swing.Timer timer;
	private ArrayList<String> _station;
	private ArrayListComboBoxModel station1, station2;
	private BaanManager baanManager;
	private ReisManager reisManager;

	public Paneel(){
		//Maak een arrayList aan voor de combobox
		_station = new ArrayList<String>();
		station1 = new ArrayListComboBoxModel(_station);
		station2 = new ArrayListComboBoxModel(_station);
		baanManager = Simulatie.getInstantie().getBaanManager();

		//nieuw object van Controllerklasse
		controller = new Controller(this);
		reisManager = Simulatie.getInstantie().getReisManager();
		frame = new JFrame();
		setLayout( null );

		timer = new javax.swing.Timer(1000, new ClockListener());//maakt timer aan

                maakGUIItems();
                
                voegToeStation();
	}
        
        protected void maakGUIItems() {
            achtergrond = new ImageIcon( "src/achtergrond.gif" );
            taxi_img = new ImageIcon("src/taxi_stipje.gif");
            station_img = new ImageIcon("src/station_knop.gif");
            station_img_vert = new ImageIcon("src/station_knop_vert.gif");

            s123_img = new ImageIcon("src/stations/station123.gif");
            s4_img = new ImageIcon("src/stations/station4.gif");
            s567_img = new ImageIcon("src/stations/station567.gif");
            s8_img = new ImageIcon("src/stations/station8.gif");
            
            JComponent[] stationLabelItems = {s1_label=new JLabel(s123_img), s2_label=new JLabel(s123_img), s3_label=new JLabel(s123_img), s4_label=new JLabel(s4_img), s5_label=new JLabel(s567_img), s6_label=new JLabel(s567_img), s7_label=new JLabel(s567_img), s8_label=new JLabel(s8_img)};
            Dimension[] stationLabelLokaties = {new Dimension(110,97), new Dimension(351,97), new Dimension(593,97), new Dimension(714,250), new Dimension(595,454), new Dimension(357,454), new Dimension(115,454), new Dimension(37,255)};
            Dimension[] stationLabelAfmetingen = { new Dimension(80,40), new Dimension(80,40), new Dimension(80,40), new Dimension(40,80), new Dimension(80,40), new Dimension(80,40), new Dimension(80,40), new Dimension(40,80) };
            setGUIItemsLokaties( stationLabelItems, stationLabelLokaties );
            setGUIItemsAfmetingen( stationLabelItems, stationLabelAfmetingen );
            setGUIItemsZichtbaarheid( stationLabelItems, false );
            voegGUIItemsToe( stationLabelItems );
            
            JComponent[] stationNaamItems = { s_NaamLabel1=new JLabel(), s_NaamLabel2=new JLabel(), s_NaamLabel3=new JLabel(), s_NaamLabel4=new JLabel(), s_NaamLabel5=new JLabel(), s_NaamLabel6=new JLabel(), s_NaamLabel7=new JLabel(), s_NaamLabel8=new JLabel() };
            Dimension[] stationNaamlokaties = { new Dimension( 130,135 ), new Dimension( 371,135 ), new Dimension( 610,135 ), new Dimension( 620,255 ), new Dimension( 610,430 ), new Dimension( 371,430 ), new Dimension( 130,430 ), new Dimension( 90,255 ) };
            setGUIItemsLokaties( stationNaamItems, stationNaamlokaties );
            setGUIItemsAfmetingen( stationNaamItems, new Dimension(126, 25) );
            setGUIItemsZichtbaarheid( stationNaamItems, false );
            voegGUIItemsToe( stationNaamItems );

            JComponent[] taxiItems = { t1_kn=new JButton(taxi_img),t2_kn=new JButton(taxi_img),t3_kn=new JButton(taxi_img),t4_kn=new JButton(taxi_img),t5_kn=new JButton(taxi_img),t6_kn=new JButton(taxi_img),t7_kn=new JButton(taxi_img),t8_kn=new JButton(taxi_img) };
            ActionListener[] taxiActieLuisteraars = { new TaxiButtonHandler(1),new TaxiButtonHandler(2),new TaxiButtonHandler(3),new TaxiButtonHandler(4),new TaxiButtonHandler(5),new TaxiButtonHandler(6),new TaxiButtonHandler(7),new TaxiButtonHandler(8) };
            setGUIItemsAfmetingen( taxiItems, new Dimension( 10, 10 ) );
            setGUIItemsActieLuisteraars( taxiItems, taxiActieLuisteraars );
            setGUIItemsZichtbaarheid( taxiItems, false );
            voegGUIItemsToe( taxiItems );

            JComponent[] stationItems = { s1_kn=new JButton(station_img), s2_kn=new JButton(station_img), s3_kn=new JButton(station_img), s4_kn=new JButton(station_img_vert), s5_kn=new JButton(station_img), s6_kn=new JButton(station_img), s7_kn=new JButton(station_img), s8_kn=new JButton(station_img_vert) };
            Dimension[] stationAfmetingen = { new Dimension(70,10), new Dimension(70,10), new Dimension(70,10), new Dimension(10,70), new Dimension(70,10), new Dimension(70,10), new Dimension(70,10), new Dimension(10,70) };
            Dimension[] stationLokaties = { new Dimension(110,77), new Dimension(357,77),new Dimension(593,77),new Dimension(765,255), new Dimension(613,510), new Dimension(367,510), new Dimension(120,510), new Dimension(15,255) };
            ActionListener[] stationActieLuisteraars = { new StationButtonHandler(1), new StationButtonHandler(2), new StationButtonHandler(3), new StationButtonHandler(4), new StationButtonHandler(5), new StationButtonHandler(6), new StationButtonHandler(7), new StationButtonHandler(8) };
            setGUIItemsAfmetingen( stationItems, stationAfmetingen );
            setGUIItemsLokaties( stationItems, stationLokaties );
            setGUIItemsActieLuisteraars( stationItems, stationActieLuisteraars );
            setGUIItemsZichtbaarheid( stationItems, false );
            voegGUIItemsToe( stationItems );

            JComponent[] actieKnoppen = { start=new JButton("SimulatieStarten"),pauzeer=new JButton("Hervatten/Pauzeren"),versnel=new JButton("Normaal/Versnellen"),invoegen=new JButton("Invoegen"),moment=new JButton("MomentOpnameLaden") };
            Dimension[] actieKnoppenLokaties = {new Dimension(136,650),new Dimension(432,650),new Dimension(728,650),new Dimension(750,610),new Dimension(830,420)};
            Dimension[] actieKnoppenAfmetingen = {new Dimension(160,40),new Dimension(160,40),new Dimension(160,40),new Dimension(120,25),new Dimension(163,25)};
            ActionListener[] actieKnoppenActieListeners = {new StartLabelWissel(),new HervatLabelWissel(),new VernselLabelWissel(),new ReizigerInvoegen(),new MomentOpenen()};
            setGUIItemsAfmetingen( actieKnoppen, actieKnoppenAfmetingen );
            setGUIItemsLokaties( actieKnoppen, actieKnoppenLokaties );
            setGUIItemsActieLuisteraars( actieKnoppen, actieKnoppenActieListeners );
            voegGUIItemsToe( actieKnoppen );

            JComponent[] invoegenItems = { aantal=new JTextField(),vertrek=new JComboBox(station1),aankomst=new JComboBox(station2),tijdVeld=new JTextField(5),status=new JLabel("Gestopt") };
            Dimension[] invoegenItemsLokaties = { new Dimension(145,610),new Dimension(370,610),new Dimension(560,610),new Dimension(400,10),new Dimension(790,43) };
            Dimension[] invoegenItemsAfmetingen = { new Dimension(120,25),new Dimension(120,25),new Dimension(120,25),new Dimension(120,40),new Dimension(100,25) };
            setGUIItemsAfmetingen( invoegenItems, invoegenItemsAfmetingen );
            setGUIItemsLokaties( invoegenItems, invoegenItemsLokaties );
            voegGUIItemsToe( invoegenItems );

            aantal.addActionListener(new GetAantalReis());
            vertrek.setSelectedIndex(-1);
            aankomst.setSelectedIndex(-1);
            tijdVeld.setEditable(false);
            tijdVeld.setFont(new Font("sansserif", Font.PLAIN, 30));
            status.setLocation(790,43);

            hoofdbaan = new ImageIcon( "src/hoofdbaan.gif" );
            hBaan = new JLabel(hoofdbaan);
            hBaan.setLocation(60,130);
            hBaan.setSize(672, 333);
            hBaan.setVisible( false );
            add( hBaan );
        }
	/**
	 * Deze methode voeg de stations toe als de grootte van de array 0 is.
	 */
	public void voegToeStation(){
		if(_station.size()== 0){
			_station.add("Amstel");
			_station.add("Lelylaan");
			_station.add("Sloterdijk");
			_station.add("AmsterdamCS");
			_station.add("Muiderpoort");
			_station.add("ZuidWTC");
			_station.add("RAI");
			_station.add("Duivendrecht");
		}

	}
	/**
	 * Deze methode voegd lege string stations toe
	 */
	public void voegToeLegeStation(){
		_station.add("");
		_station.add("");
		_station.add("");
		_station.add("");
		_station.add("");
		_station.add("");
		_station.add("");
		_station.add("");

	}

	/**
	 * Deze methode geeft aan de controller door hoeveel de aantal stations die zijn toegevoegd
	 * en het voeg ook de station namen toe in de controller klasse 
	 */
	public void setStationController(){
		controller.setStations(_station.size());
		String nm;
		//probeer hiermee alle stations in te voeren in de controller klasse
		for(int v = 0; v < _station.size(); v++){
			nm = _station.get(v);
			controller.setStation(nm); 
		}
	}
	/**
	 * Deze methode geeft aan de controller door de aantal taxi die zijn toegevoegd
	 * @param aantTaxi
	 */
	public void setTaxi(int aantTaxi){
		taxi = aantTaxi;
		controller.setTaxi(taxi);
		System.out.println(taxi);
	}

	/**
	 * Deze methode verwijder alle taxi in de array
	 */
	public void verwijder(){
		while(!(_station.size()== 0)){//deze mothode heeft betrekking op de verwijdering van de stations in de combobox
			_station.remove(0);
		}
	}

	/**
	 * Deze methode maakt de status van de simulatie true of false
	 * @param simulatie
	 */
	public void simulatieGestart(boolean simulatie){
		simulatieGestart = simulatie;
	}

	//Dit roep het scherm aan om meer taxis toe te voegen
	public void voegMeerTaxiToe(){
		String TaxiToevoegen = JOptionPane.showInputDialog(null, "Aantal taxi's:","Meerdere Taxi's Toevoegen in het simulatie",JOptionPane.YES_NO_CANCEL_OPTION);
		int aantalTaxis = Integer.parseInt(TaxiToevoegen);
		//hier worden de taxi in de simulatie toegevoegd

	}

	/**
	 * Deze methode blijft stations verwijderen vanaf  de 0-de index tot hij aan de opgegeven in de param voldoed.
	 * @param verwijder
	 */
	public void verwijder(int aantal){//deze mothode heeft betrekking op de verwijdering van de stations in de combobox
            while(_station.size()!= ((aantal==8) ? 0 :aantal)) {
                _station.remove(0);
            }
	}

	public void gemiddelStatistiek(){
		String bericht = controller.getGemStatistieken();
		JOptionPane optionPane = new GemmidelstatistiekScherm();
		optionPane.setMessage(bericht);
		optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
		JDialog dialog = optionPane.createDialog(null, "Gemiddelde statistieken");
		dialog.setVisible(true);  
	}

        public void maakStations( int aantal ) {
            JLabel[][] labels = {{s1_label,s5_label},{s2_label,s5_label,s7_label},{s1_label,s3_label,s5_label,s7_label},{s2_label,s4_label,s5_label,s7_label,s8_label},{s1_label,s3_label,s4_label,s5_label,s7_label,s8_label},{s1_label,s2_label,s3_label,s4_label,s5_label,s7_label,s8_label},{s1_label,s2_label,s3_label,s4_label,s5_label,s6_label,s7_label,s8_label}};
            JButton[][] buttons = {{s1_kn,s5_kn},{s2_kn,s5_kn,s7_kn},{s1_kn,s3_kn,s5_kn,s7_kn},{s2_kn,s4_kn,s5_kn,s7_kn,s8_kn},{s1_kn,s3_kn,s4_kn,s5_kn,s7_kn,s8_kn},{s1_kn,s2_kn,s3_kn,s4_kn,s5_kn,s7_kn,s8_kn},{s1_kn,s2_kn,s3_kn,s4_kn,s5_kn,s6_kn,s7_kn,s8_kn}};
            for( JLabel label : labels[aantal-2] ) {
                label.setVisible( true );
            }
            for( JButton button : buttons[aantal-2] ) {
                button.setVisible( true );
            }
            voegToeStation();
            if ( aantal != 8 ) verwijder( aantal );
            setStationController();
            aantalStation = aantal;
            stationLabel( aantal );
        }
	/**
	 * 
	 * Deze methode zal aan de hand van de doorgekregen absolute en zijpositie berekenen wat
	 * de positie op het scherm is, per taxi.
	 * @param taxi
	 * @param meters
	 * @param zij
	 * @param opStat
	 * @param naarStat
	 * @param vanStat
	 * @param test
	 * @param versnel
	 */
	public void setLocation(int taxi, int meters, int zij, boolean opStat, boolean naarStat, boolean vanStat, int test, int versnel){
            JButton[] taxis = {null, t1_kn,t2_kn,t3_kn,t4_kn,t5_kn,t6_kn,t7_kn,t8_kn};
            int x = 0, y = 0;
            
            //eerste stuk horizontaal
            if (meters <= 640) {
                x = 70+meters+zij;
                y = 130 - ((zij <= 40) ? zij : 80-zij);
            }
            
            //eerste stuk vertikaal
            if( meters > 640 && meters <= 960 ) {
                x = 710 + ((zij <= 40) ? zij : 80-zij);
                y = 130+meters-640+zij;
            }

            //tweed stuk horizontaal
            if( meters > 960 && meters <= 1600 ) {
                x = 710-(meters-960)-zij;
                y = 450 + ((zij <= 40) ? zij : 80-zij);
            }

            //tweede stuk vertikaal
            if( meters > 1600 && meters <= 1920 ) {
                x = 70 - ((zij <= 40) ? zij : 80-zij);
                y = 450-(meters-1600)-zij;
            }
            
            taxis[taxi].setLocation( x, y );
	}


	/**
	 * 
	 * Hij zet alle taxi's op hun initiele plek, waarna ze vervolgens verplaatsen kunnen.
	 * hij berekend de initiele plek aan de hand van de absolute positie.
	 * @param absolute
	 * @param zij
	 * @param ID
	 */
	public void setInitLoc(double absolute, double zijAfstand, int ID){
		JButton[] taxis = {null, t1_kn,t2_kn,t3_kn,t4_kn,t5_kn,t6_kn,t7_kn,t8_kn};
                int meters = new Double((absolute)/12.5).intValue(); 
		int zij = new Double((zijAfstand)/12.5).intValue();
                int x = 0, y = 0;
                
		System.out.println("Taxi: " + ID);
		System.out.println(meters);
		System.out.println(zij);

                if(meters <= 640){
                    x = 110+meters;
                    y = 50+zij;
                }
                if(meters > 640 && meters <= 960 ){
                    x = 750;
                    y = 170+(meters-640);
                }
                if(meters > 960 && meters <= 1600){
                    x = 710-(meters-920);
                    y = 450+zij;
                }
                if(meters >1600 && meters < 1920){
                    x = 30;
                    y = 450-(meters-1560);
                }    

                taxis[ID].setLocation(x, y);
	}
	public void maakTaxisZichtbaar(int aantal){
		JButton[] taxis = {t1_kn,t2_kn,t3_kn,t4_kn,t4_kn,t5_kn,t6_kn,t7_kn,t8_kn};
                for( int i = 0; i < aantal ; i++ ) {
                    taxis[i].setVisible(true);
                }
	}
	/**
	 * Deze methode vult de label text in met de behoordestation naam die afkomstig is vanuit de controller. Om dat de doen maak
	 * hij gebruik van de aantalStation parameter die hij krijgt als je de aantal statioon invoerd
	 * @param aantalStation
	 */
	public void stationLabel(int aantalStation){
            int[][] matrix = { {1,5}, {2,5,7}, {1,3,5,7}, {2,4,5,7,8}, {1,3,4,5,7,8}, {1,2,3,4,5,7,8}, {1,2,3,4,5,6,7,8} };
            JLabel[] labels = { s_NaamLabel1, s_NaamLabel2, s_NaamLabel3, s_NaamLabel4, s_NaamLabel5, s_NaamLabel6, s_NaamLabel7, s_NaamLabel8 };
            System.out.println( "!!!!!!!!!" );
            for( int i : matrix[aantalStation-2] ) {
                System.out.println( i );
                labels[i-1].setText(controller.getNaam(i));
            }
            System.out.println( "!!!!!!!!!" );
	}
	public void setStationLabelZichtbaarheid( boolean z ){
		JLabel[] labels = { s_NaamLabel1, s_NaamLabel2, s_NaamLabel3, s_NaamLabel4, s_NaamLabel5, s_NaamLabel6, s_NaamLabel7, s_NaamLabel8 };
                for( JLabel label : labels ) {
                    label.setVisible(z);
                }
	}
	public void setLabelTextOpNull(){
		JLabel[] labels = { s_NaamLabel1, s_NaamLabel2, s_NaamLabel3, s_NaamLabel4, s_NaamLabel5, s_NaamLabel6, s_NaamLabel7, s_NaamLabel8 };
                for( JLabel label : labels ) {
                    label.setText(" ");
                }
	}
	public void startTimer(){//start timer
		timer.start();
	}
	public void stopTimer(){//stop timer
		timer.stop();
		uur = 0;
		min = 0;
		sec = 0;

	}
	public void setLocation(){

	}
	public void pauzeTimer(){
		timer.stop();
	}
	public void VersnelOfNormaliceerTimer(int i){
		if(i != 1000){
			timer.setDelay(controller.versnelTimer());
		}
		else {
			timer.setDelay(1000);
		}

	}
	public String TimertoString (){

		return tijdVeld.getText();
	}
	/**
	 * Deze methode voegt de stations toe. Wordt voor momentladen gebruikt
	 * @param station
	 */
	public void voegStationsToe(int aantal){
            maakStations( aantal );
	}
	/**
	 * Deze methode voeg de aantal taxi toe... Wordt vooor moment opname gebruikt
	 * @param t_axi
	 */
	public void voegTaxiToe(int t_axi){
		maakTaxisZichtbaar(t_axi);
	}

	//start & stop button
	/**
	 * @author dareal
	 * Deze inwedigge klasse met methode start of stop de simulatie. Het gebreurd met behulp van de iterator I. en het moet ook aan enkele
	 * voorwaarde voldoen. ALs de simulatie gepauzeerd is dan gaat het niet door. 
	 */
	class StartLabelWissel implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			if(simIsPauze){
				JOptionPane.showMessageDialog(frame, "Hervat eerst de sim, alvorens te stoppen.","Fout",
						JOptionPane.ERROR_MESSAGE);
			}
			if(i == 0){
				status.setText("Gestart");
				simIsGestart = true;
				setStationLabelZichtbaarheid( true );
				start.setText("Simulatie Stoppen");
				new TekenSpoor((Paneel)((JComponent) e.getSource() ).getParent());
				hBaan.setVisible( true );    
				i++;
				startTimer();
				verwijder();
				controller.startSimulatie(i);
				setStationLabelZichtbaarheid( true );
			}
			else if(i == 1 && simIsPauze == false){
				y = 0;
				z = 0;
				status.setText("Gestopt");

				simIsGestart = false;
				TimertoString();
				gemiddelStatistiek();
				start.setText("Simulatie Starten");
				i--;
				stopTimer();
				verwijder();
				controller.zetAllStationNull();

				//zet de stations, baan en knoppen weer op invisible
				s1_kn.setVisible( false );
				s2_kn.setVisible( false );
				s3_kn.setVisible( false );
				s4_kn.setVisible( false );
				s5_kn.setVisible( false );
				s6_kn.setVisible( false );
				s7_kn.setVisible( false );
				s8_kn.setVisible( false );

				t1_kn.setVisible( false );
				t2_kn.setVisible( false );
				t3_kn.setVisible( false );
				t4_kn.setVisible( false );
				t5_kn.setVisible( false );
				t6_kn.setVisible( false );
				t7_kn.setVisible( false );
				t8_kn.setVisible( false );

				s1_label.setVisible( false );
				s2_label.setVisible( false );
				s3_label.setVisible( false );
				s4_label.setVisible( false );
				s5_label.setVisible( false );
				s6_label.setVisible( false );
				s7_label.setVisible( false );
				s8_label.setVisible( false );
				hBaan.setVisible( false );
				setStationLabelZichtbaarheid( false );
				controller.startSimulatie(i);
				setLabelTextOpNull();
				_station.clear();
				//nieuw object van Controllerklasse
			}
		}
	}


	//hervat/pauzeer button
	/**
	 * @author dareal
	 *	Deze methode in de inwendigge klasse pauzeer of hervat de simulatuie d.m.v. een iterator genaamd y. Maar het voldoet wel aan een voorwaarde.
	 * als de simulatie nog niet gestart is dan kan je deze methode niet uitvoeren
	 */
 	class HervatLabelWissel implements ActionListener {
		public void actionPerformed(ActionEvent e){
			System.out.println(reisManager.getStatus());

			if(!(simIsGestart)){
				JOptionPane.showMessageDialog(frame, "Start eerst de sim!","Fout",
						JOptionPane.ERROR_MESSAGE);   }

			if(y == 0 && simIsGestart ){
				status.setText("Gepauzeerd");

				simIsPauze = true;
				UserInterface.isGepauzeerd(simIsPauze);
				System.out.println(reisManager.getStatus());
				y++;
				controller.pauzeerSimulatie(y);
				pauzeTimer();
			}

			else if(y == 1){
				status.setText("Gestart");

				simIsPauze = false;
				UserInterface.isGepauzeerd(simIsPauze);
				y--;
				controller.pauzeerSimulatie(y);
				timer.start();
			}
		}
	}
	//vernsel en normaal button
	/**
	 * @author dareal
	 * Deze methode in de inwendigge klasse versnel en normaliceer de simulatuie d.m.v een iterator genaamd z. Maar het moet wel aan een voorwaarde voldoen
	 * betreft jey simulatie mnoet wel gestart zijn
	 *
	 */
	class VernselLabelWissel implements ActionListener {
		public void actionPerformed(ActionEvent e){

			if(!(simIsGestart)){
				JOptionPane.showMessageDialog(frame, "Start eerst de sim!","Fout",
						JOptionPane.ERROR_MESSAGE);
			}


			if(z == 0 && simIsGestart){
				status.setText("Versneld");

				z++;
				controller.setVersnelSimulatie(z);


			}
			else if(z == 1){
				status.setText("Gestart");

				z--;
				controller.setVersnelSimulatie(z);
			}
		}
	}

	/**
	 * @author dareal
	 *	Deze methode laad een moment opname. Maar voor dat hij dat doet moet er wel een data.dat bestand aanwezig zijn. 
	 */
	class MomentOpenen implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			boolean exists = (new File("data.dat")).exists();
			
			if(exists){
			if(i == 0){
				status.setText("Gestart");
					verwijder();
					//start het simulatie
					controller.startSimulatie(1);
					controller.pauzeerSimulatie(1);
					setStationLabelZichtbaarheid( true );
					start.setText("Simulatie Stoppen");
					y = 0;
					z = 0;
					i++;
					simIsGestart = true;
					UserInterface.isGestart(simIsGestart);
					simIsPauze = false;
					UserInterface.isGepauzeerd(simIsPauze);
                                        maakStations( controller.stationGegevensOphalen() );
                                        maakTaxisZichtbaar(controller.taxiGegevensOphalen());
					controller.voegObservToe(controller.taxiGegevensOphalen());
					hBaan.setVisible( true );    
					startTimer();
					setStationLabelZichtbaarheid( true );

					controller.pauzeerSimulatie(0);
					//controller.startSimulatie(1);
					System.out.println(" statuscheckdd"+ reisManager.getStatus());
				}
			else {

				JOptionPane.showMessageDialog(frame, "De simulatie is al gestart.","Fout",
						JOptionPane.ERROR_MESSAGE);
				}
			}
			else{
				JOptionPane.showMessageDialog(frame, "Bestand niet gevonden","Fout",
						JOptionPane.ERROR_MESSAGE);
				}
		}
	}
	//textvak acties!
	/**
	 * @author dareal
	 *	Deze inwendigge klasse methode voegt de aantal reiziger toe in de simulatie. Als de simulatie niet gestart is dam kan je dit niet uitvoeren
	 * Verder moet het aan enkele voorwaardes voldoen wat in de if statements verwerk zijn. Hier kan je invoegen door op enter te drukken
	 * 
	 */
	public class GetAantalReis implements ActionListener {
		public void actionPerformed(ActionEvent e){
			
			if(!(simIsGestart)){
				JOptionPane.showMessageDialog(frame, "Start eerst de sim!","Fout",
						JOptionPane.ERROR_MESSAGE);
			}
			

			try{
				String vertrekpunt = (String)vertrek.getSelectedItem();
			String bestemming = (String)aankomst.getSelectedItem();
			String aantalReis = aantal.getText();
			int aantalReiz = Integer.parseInt(aantalReis);
			if(aantalReiz < LIMIET_REIZIGER){
				if(i == 1){
					if(!(vertrekpunt == null) && !(bestemming == null)){
						if(!(vertrekpunt == bestemming)){  
							controller.voegReizigerToe(aantalReiz, vertrekpunt, bestemming);
							JOptionPane.showMessageDialog(frame, "Reiziger is toegevoegd");
							aantal.setText(null);
							System.out.println(aantalReiz +  vertrekpunt + bestemming);
							vertrek.setSelectedItem(null);
							aankomst.setSelectedItem(null);
							repaint();
						}
						else {
							JOptionPane.showMessageDialog(frame, "vertrekpunt en bestemming zijn gelijk","Fout",
									JOptionPane.ERROR_MESSAGE);
						}
					}
					else {
						JOptionPane.showMessageDialog(frame, "vertrekpunt of bestemming is niet geslecteert","Fout",
								JOptionPane.ERROR_MESSAGE);
					}
				}
				else {
					JOptionPane.showMessageDialog(frame, "Simulatie is nog niet gestart","Fout",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			else {
				JOptionPane.showMessageDialog(frame, "Limiet is overschreden","Fout",
						JOptionPane.ERROR_MESSAGE);
			}
			}catch(NumberFormatException e1){
				JOptionPane.showMessageDialog(frame, "Vul een geldige waarde in.","Fout",
						JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}
	/**
	 * @author dareal
	 *	Deze inwendigge klasse methode voegt de aantal reiziger toe in de simulatie. Als de simulatie niet gestart is dam kan je dit niet uitvoeren
	 * Verder moet het aan enkele voorwaardes voldoen wat in de if statements verwerk zijn. Hier kan je invoegen door op het knop te drukken
	 * 
	 */
	//Invoegen button acties
	class ReizigerInvoegen implements ActionListener {
		public void actionPerformed(ActionEvent e){

			if(!(simIsGestart)){
				JOptionPane.showMessageDialog(frame, "Start eerst de sim!","Fout",
						JOptionPane.ERROR_MESSAGE);
			}

			try{ 
				String vertrekpunt = (String)vertrek.getSelectedItem();
			String bestemming = (String)aankomst.getSelectedItem();
			String aantalReis = aantal.getText();
			int aantalReiz = Integer.parseInt(aantalReis);
			if(aantalReiz < LIMIET_REIZIGER){
				if(i == 1){
					if(!(vertrekpunt == null) && !(bestemming == null)){
						if(!(vertrekpunt == bestemming)){  
							controller.voegReizigerToe(aantalReiz, vertrekpunt, bestemming);
							JOptionPane.showMessageDialog(frame, "Reiziger is toegevoegd");
							aantal.setText(null);
							System.out.println(aantalReiz +  vertrekpunt + bestemming);
							vertrek.setSelectedItem(null);
							aankomst.setSelectedItem(null);
							repaint();
						}
						else {
							JOptionPane.showMessageDialog(frame, "vertrekpunt en bestemming zijn gelijk","Fout",
									JOptionPane.ERROR_MESSAGE);
						}
					}
					else {
						JOptionPane.showMessageDialog(frame, "vertrekpunt of bestemming is niet geslecteert","Fout",
								JOptionPane.ERROR_MESSAGE);
					}
				}
				else {
					JOptionPane.showMessageDialog(frame, "Simulatie is nog niet gestart","Fout",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			else {
				JOptionPane.showMessageDialog(frame, "Limiet is overschreden","Fout",
						JOptionPane.ERROR_MESSAGE);
			}
			}
			catch(NumberFormatException e1){
				JOptionPane.showMessageDialog(frame, "Vul een geldige waarde in.","Fout",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	} 
	/**
	 * @author dareal
	 * Deze methode roept vult de controctor van de taxistats klasse(taxiu statustiek) met de juiste gegevens. 
	 *
	 */
        class TaxiButtonHandler implements ActionListener {
            private int nr;
            
                public TaxiButtonHandler( int taxiNr ) {
                    this.nr = taxiNr;
                }
            
		public void actionPerformed(ActionEvent e){
			new TaxiStats(controller.getPositie(this.nr), controller.getReizInTaxi(this.nr), controller.getReisTijd(this.nr), this.nr);
		}
	}
        /**
	 * @author dareal
	 * 
	 * Hier wordt bepaald welke statistiek aangeroepen dient te worden zodat het klopt volgens de gegevens
	 * Dit heeft de maken met de statustieken van de station. Het wordt geactiveerd als je op een station knop drukt. Wat dan meegegeven wordt 
	 * is dan de aantal station die toegevoegd is in de somulatie.
	 */
	//Dit is het versie onafhangkelijk van de algoritme
        class StationButtonHandler implements ActionListener {
            private int nr;
            private int[][] matrix = {
                                    { 1, 1, 1, 1, 1, 1, 1, 1 },
                                    { 2, 2, 3, 2, 1, 2, 2, 2 },
                                    { 3, 3, 3, 2, 3, 2, 3, 3 },
                                    { 4, 4, 4, 4, 2, 3, 4, 4 },
                                    { 5, 2, 1, 3, 3, 4, 5, 5 },
                                    { 6, 6, 6, 6, 6, 6, 6, 6 },
                                    { 7, 7, 3, 4, 4, 5, 6, 7 },
                                    { 8, 8, 8, 8, 5, 6, 7, 8 }
            };
            public StationButtonHandler( int stationNr ) {
                this.nr = stationNr;
            }
		public void actionPerformed(ActionEvent a ){
                    int statNr = this.matrix[this.nr-1][aantalStation-1];
                    new StationStats(controller.getNaam(statNr), controller.getA_reizigers(statNr), controller.getA_taxis(statNr));
		}
	}
	//inwendige klasse voor de timer!
	class ClockListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			++sec;//telt sec op
			if( sec == 60){
				min++;//telt min op
				sec = 0;//zet sec op 0
			}
			if(min == 60){
				uur++;
				min = 0;
			}
			tijdVeld.setVisible(true);
			//System.out.println("" + uur + ":" + min + ":" + sec);
			tijdVeld.setText("" + uur + ":" + min + ":" + sec);

		}

	}
	/**
	 * @author dareal
	 *	Hier wordt de inwendigge klasse aangeroepen die alleen de optionPane klasse aanroept
	 */
	@SuppressWarnings("serial")
	class GemmidelstatistiekScherm extends JOptionPane {

		/**
		 * De optionpane wordt opgeroepen
		 */
		GemmidelstatistiekScherm() {

		}
	}


	//teken het achtergrondplaatje
	public void paintComponent( Graphics g) {
		super.paintComponent(g);
		achtergrond.paintIcon( this, g, 0, 0);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		Taxi t = (Taxi) o;
		teller++;
		if(teller == 5){
			teller = 0;
		}
		setLocation(t.getID(), new Double(t.getAbsolutePositie()/12.5).intValue(), new Double(t.getZijPositie()/12.5).intValue(),t.opStation(),t.naarStation(), t.vanStation(), teller, reisManager.getVersnelFactor());
		System.out.println("-------------------------------------------------");
		System.out.println("geupdate");
		System.out.println("Absolute positie: " + t.getAbsolutePositie());
		System.out.println("Taxi nummer: " + t.getID());
		System.out.println("Zij positie: "+t.getZijPositie());
		System.out.println("Op station? " + t.opStation());
		System.out.println("naar station? " + t.naarStation());
		System.out.println("van station? " + t.vanStation());
		System.out.println(teller);
		System.out.println(reisManager.getVersnelFactor());
		System.out.println("-------------------------------------------------");
	}
}
