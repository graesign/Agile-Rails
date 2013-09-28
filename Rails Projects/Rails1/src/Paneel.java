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

import org.tigam.railcab.algoritme.*;


//Het eigenlijke scherm!
/**
 * @author dareal
 *
 */
class Paneel extends JPanel implements Observer{

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


	private JFrame frame;
	private javax.swing.Timer timer;
	private ArrayList<String> _station;
	private ArrayListComboBoxModel station1, station2;
	private BaanManager baanManager;
	private ReisManager reisManager;
	private int pixels = 0;
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

		//haal plaatje op
		achtergrond = new ImageIcon( "src/achtergrond.gif" );
		taxi_img = new ImageIcon("src/taxi_stipje.gif");
		station_img = new ImageIcon("src/station_knop.gif");
		station_img_vert = new ImageIcon("src/station_knop_vert.gif");

		s123_img = new ImageIcon("src/stations/station123.gif");
		s4_img = new ImageIcon("src/stations/station4.gif");
		s567_img = new ImageIcon("src/stations/station567.gif");
		s8_img = new ImageIcon("src/stations/station8.gif");


		//voeg de hoofdbaan toe
		hoofdbaan = new ImageIcon( "src/hoofdbaan.gif" );
		hBaan = new JLabel(hoofdbaan);
		hBaan.setLocation(60,130);
		hBaan.setSize(672, 333);
		hBaan.setVisible( false );

		//plak alle stations aan de baan vast  
		s1_label = new JLabel(s123_img);
		s1_label.setLocation(110,97);
		s1_label.setSize(80, 40);
		s1_label.setVisible( false );

		s2_label = new JLabel(s123_img);
		s2_label.setLocation(351, 97);
		s2_label.setSize(80, 40);
		s2_label.setVisible( false );

		s3_label = new JLabel(s123_img);
		s3_label.setLocation(593, 97);
		s3_label.setSize(80, 40);
		s3_label.setVisible( false );

		s4_label = new JLabel(s4_img);
		s4_label.setLocation(714, 250);
		s4_label.setSize(40, 80);
		s4_label.setVisible( false );

		s5_label = new JLabel(s567_img);
		s5_label.setLocation(595, 454);
		s5_label.setSize(80, 40);
		s5_label.setVisible( false );

		s6_label = new JLabel(s567_img);
		s6_label.setLocation(357, 454);
		s6_label.setSize(80, 40);
		s6_label.setVisible( false );

		s7_label = new JLabel(s567_img);
		s7_label.setLocation(115, 454);
		s7_label.setSize(80, 40);
		s7_label.setVisible( false );

		s8_label = new JLabel(s8_img);
		s8_label.setLocation(37, 255);
		s8_label.setSize(40, 80);
		s8_label.setVisible( false );

		//maak alle station naam label
		s_NaamLabel1 = new JLabel();
		s_NaamLabel1.setLocation(130, 135);
		s_NaamLabel1.setSize(126, 25);
		s_NaamLabel1.setVisible(false);

		s_NaamLabel2 = new JLabel();
		s_NaamLabel2.setLocation(371, 135);
		s_NaamLabel2.setSize(126, 25);
		s_NaamLabel2.setVisible(false);

		s_NaamLabel3 = new JLabel();
		s_NaamLabel3.setLocation(610, 135);
		s_NaamLabel3.setSize(126, 25);
		s_NaamLabel3.setVisible(false);

		s_NaamLabel4 = new JLabel();
		s_NaamLabel4.setLocation(620, 255);
		s_NaamLabel4.setSize(126, 25);
		s_NaamLabel4.setVisible(false);

		s_NaamLabel5 = new JLabel();
		s_NaamLabel5.setLocation(610, 430);
		s_NaamLabel5.setSize(126, 25);
		s_NaamLabel5.setVisible(false);

		s_NaamLabel6 = new JLabel();
		s_NaamLabel6.setLocation(371, 430);
		s_NaamLabel6.setSize(126, 25);
		s_NaamLabel6.setVisible(false);

		s_NaamLabel7 = new JLabel();
		s_NaamLabel7.setLocation(130, 430);
		s_NaamLabel7.setSize(126, 25);
		s_NaamLabel7.setVisible(false);

		s_NaamLabel8 = new JLabel();
		s_NaamLabel8.setLocation(90, 255);
		s_NaamLabel8.setSize(126, 25);
		s_NaamLabel8.setVisible(false);

		//voeg de taxisknoppen toe
		t1_kn = new JButton(taxi_img);
		t1_kn.setSize(10,10);
		t1_kn.addActionListener( new Taxi_1());
		t1_kn.setVisible( false );


		t2_kn = new JButton(taxi_img);
		t2_kn.setSize(10,10);
		t2_kn.addActionListener( new Taxi_2());
		t2_kn.setVisible( false );

		t3_kn = new JButton(taxi_img);
		t3_kn.setSize(10,10);
		t3_kn.addActionListener( new Taxi_3());
		t3_kn.setVisible( false );

		t4_kn = new JButton(taxi_img);
		t4_kn.setSize(10,10);
		t4_kn.addActionListener( new Taxi_4());
		t4_kn.setVisible( false );

		t5_kn = new JButton(taxi_img);
		t5_kn.setSize(10,10);
		t5_kn.addActionListener( new Taxi_5());
		t5_kn.setVisible( false );

		t6_kn = new JButton(taxi_img);
		t6_kn.setSize(10,10);
		t6_kn.addActionListener( new Taxi_6());
		t6_kn.setVisible( false );

		t7_kn = new JButton(taxi_img);
		t7_kn.setSize(10,10);
		t7_kn.addActionListener( new Taxi_7());
		t7_kn.setVisible( false );

		t8_kn = new JButton(taxi_img);
		t8_kn.setSize(10,10);
		t8_kn.addActionListener( new Taxi_8());
		t8_kn.setVisible( false );

		//voeg stationsknoppen toe  
		s1_kn = new JButton(station_img);
		s1_kn.setSize(70,10);
		s1_kn.setLocation(110, 77);
		s1_kn.addActionListener( new Amstel());
		s1_kn.setVisible( false );

		s2_kn = new JButton(station_img);
		s2_kn.setSize(70,10);
		s2_kn.setLocation(357, 77);
		s2_kn.addActionListener( new Sloterdijk());
		s2_kn.setVisible( false );

		s3_kn = new JButton(station_img);
		s3_kn.setSize(70,10);
		s3_kn.setLocation(593, 77);
		s3_kn.addActionListener( new AmsterdamCS());
		s3_kn.setVisible( false );

		s4_kn = new JButton(station_img_vert);
		s4_kn.setSize(10,70);
		s4_kn.setLocation(765, 255);
		s4_kn.addActionListener( new Lelylaan());
		s4_kn.setVisible( false );

		s5_kn = new JButton(station_img);
		s5_kn.setSize(70,10);
		s5_kn.setLocation(613, 510);
		s5_kn.addActionListener( new Muiderpoort());
		s5_kn.setVisible( false );

		s6_kn = new JButton(station_img);
		s6_kn.setSize(70,10);
		s6_kn.setLocation(367, 510);
		s6_kn.addActionListener( new ZuidWTC());
		s6_kn.setVisible( false );

		s7_kn = new JButton(station_img);
		s7_kn.setSize(70,10);
		s7_kn.setLocation(120, 510);
		s7_kn.addActionListener( new Rai());
		s7_kn.setVisible( false );

		s8_kn = new JButton(station_img_vert);
		s8_kn.setSize(10,70);
		s8_kn.setLocation(15, 255);
		s8_kn.addActionListener( new Duivendrecht());
		s8_kn.setVisible( false );

		//maak knoppen aan
		start = new JButton("Simulatie Starten");
		start.setLocation(136,650);
		start.setSize(160,40);
		start.addActionListener( new StartLabelWissel());

		pauzeer = new JButton ("Hervatten/Pauzeren");
		pauzeer.setLocation(432,650);
		pauzeer.setSize(160,40);
		pauzeer.addActionListener( new HervatLabelWissel());

		versnel = new JButton ("Normaal/Versnellen");
		versnel.setLocation(728,650);
		versnel.setSize(160,40);
		versnel.addActionListener( new VernselLabelWissel());

		invoegen = new JButton ("Invoegen");
		invoegen.setLocation(750,610);
		invoegen.setSize(120,25);
		invoegen.addActionListener(new ReizigerInvoegen());

		moment = new JButton("MomentOpnameLaden");
		moment.setLocation(830,420);
		moment.setSize(163,25);
		moment.addActionListener(new MomentOpenen());

		//maak textvakken aan
		aantal = new JTextField();
		aantal.setLocation(145,610);
		aantal.setSize(120,25);
		aantal.addActionListener(new GetAantalReis());

		//maak combobox aan
		vertrek = new JComboBox(station1);
		vertrek.setLocation(370,610);
		vertrek.setSize(120,25);
		vertrek.setSelectedIndex(-1);

		aankomst = new JComboBox(station2);
		aankomst.setLocation(560,610);
		aankomst.setSize(120,25);
		aankomst.setSelectedIndex(-1);
		voegToeStation();

		//maak timer aan
		tijdVeld = new JTextField(5);
		tijdVeld.setSize(120, 40);
		tijdVeld.setLocation(400, 10);
		tijdVeld.setEditable(false);
		tijdVeld.setFont(new Font("sansserif", Font.PLAIN, 30));
		
		status = new JLabel("Gestopt");
		status.setSize(100,25);
		status.setLocation(790,43);
		
		//voeg stations_labels (de plaatjes) toe
		add( status );
		add (moment);

		add( s1_kn );
		add( s2_kn );
		add( s3_kn );
		add( s4_kn );
		add( s5_kn );
		add( s6_kn );
		add( s7_kn );
		add( s8_kn );

		add( t1_kn );
		add( t2_kn );
		add( t3_kn );
		add( t4_kn );
		add( t5_kn );
		add( t6_kn );
		add( t7_kn );
		add( t8_kn );

		add( s1_label );
		add( s2_label );
		add( s3_label );
		add( s4_label );
		add( s5_label );
		add( s6_label );
		add( s7_label );
		add( s8_label );

		add(s_NaamLabel1);
		add(s_NaamLabel2);
		add(s_NaamLabel3);
		add(s_NaamLabel4);
		add(s_NaamLabel5);
		add(s_NaamLabel6);
		add(s_NaamLabel7);
		add(s_NaamLabel8);

		add( hBaan );

		add( aantal );
		add( start );
		add( versnel );
		add( pauzeer );
		add( invoegen );
		add( vertrek );
		add( aankomst );
		//add( tijdVeld );



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
	public void verwijder(int verwijder){//deze mothode heeft betrekking op de verwijdering van de stations in de combobox
		switch(verwijder){
		case 8:
			verwijder();
			break;
		case 7:
			while(!(_station.size()== 7)){
				_station.remove(0);
			}
			break;
		case 6:
			while(!(_station.size()== 6)){
				_station.remove(0);
			}
			break;
		case 5:
			while(!(_station.size()== 5)){
				_station.remove(0);
			}
			break;
		case 4:
			while(!(_station.size()== 4)){
				_station.remove(0);
			}
			break;
		case 3:
			while(!(_station.size()== 3)){
				_station.remove(0);
			}
			break;
		case 2:
			while(!(_station.size()== 2)){
				_station.remove(0);
			}
			break;
		case 1:
			while(!(_station.size()== 1)){
				_station.remove(0);
			}
			break;
		}
	}

	/**
	 * Deze methode represeteert de gemiddil statiestiek met behulp van een optionPane
	 */
	public void gemiddelStatistiek(){
		String bericht = controller.getGemStatistieken();
		JOptionPane optionPane = new GemmidelstatistiekScherm();
		optionPane.setMessage(bericht);
		optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
		JDialog dialog = optionPane.createDialog(null, "Gemiddelde statistieken");
		dialog.setVisible(true);  
	}

	/**
	 * Deze code roep de methode moment opslaan in de controller klasse. om dat te doen
	 */
	public void momentOpnameOpslaan(){
		controller.momentOpnameOpslaan();

	}
	//methods voor de stations

	/**
	 * Deze methode zorgt ervoor dat er 2 stations op de baan getekend worden
	 */
	public void tweeStations(){
		s1_label.setVisible( true );
		s1_kn.setVisible( true );
		s5_label.setVisible( true );
		s5_kn.setVisible( true );
		voegToeStation();
		verwijder(2);
		setStationController();
		aantalStation = 2;// geeft de waarde aan voor de switch case van de statistiek
		stationLabel(2);
		//1, 5  
	}
	/**
	 * Deze methode zorgt ervoor dat er 3 stations op de baan getekend worden
	 */
	public void drieStations(){
		s2_label.setVisible( true );
		s2_kn.setVisible( true );
		s5_label.setVisible( true );
		s5_kn.setVisible( true );
		s7_label.setVisible( true );
		s7_kn.setVisible( true );
		voegToeStation();
		verwijder(3);
		setStationController();
		aantalStation = 3;
		stationLabel(3);


		//2, 5, 7
	}
	/**
	 * Deze methode zorgt ervoor dat er 4 stations op de baan getekend worden
	 */
	public void vierStations(){
		s1_label.setVisible( true );
		s1_kn.setVisible( true );
		s3_label.setVisible( true );
		s3_kn.setVisible( true );
		s5_label.setVisible( true );
		s5_kn.setVisible( true );
		s7_label.setVisible( true );
		s7_kn.setVisible( true );
		voegToeStation();
		verwijder(4);
		setStationController();
		aantalStation = 4;
		stationLabel(4);

		//1, 3, 5, 7
	}
	/**
	 * Deze methode zorgt ervoor dat er 5 stations op de baan getekend worden
	 */
	public void vijfStations(){
		s2_label.setVisible( true );
		s2_kn.setVisible( true );
		s4_label.setVisible( true );
		s4_kn.setVisible( true );
		s5_label.setVisible( true );
		s5_kn.setVisible( true );
		s7_label.setVisible( true );
		s7_kn.setVisible( true );
		s8_label.setVisible( true );
		s8_kn.setVisible( true );
		voegToeStation();
		verwijder(5);
		setStationController();
		aantalStation = 5;
		stationLabel(5);

		//2, 4, 5, 7, 8
	}
	/**
	 * Deze methode zorgt ervoor dat er 6 stations op de baan getekend worden
	 */
	public void zesStations(){
		s1_label.setVisible( true );
		s1_kn.setVisible( true );
		s3_label.setVisible( true );
		s3_kn.setVisible( true );
		s4_label.setVisible( true );
		s4_kn.setVisible( true );
		s5_label.setVisible( true );
		s5_kn.setVisible( true );
		s7_label.setVisible( true );
		s7_kn.setVisible( true );
		s8_label.setVisible( true );
		s8_kn.setVisible( true );
		voegToeStation();
		verwijder(6);
		setStationController();
		aantalStation = 6;
		stationLabel(6);


		//1, 3, 4, 5, 7, 8
	}
	/**
	 * Deze methode zorgt ervoor dat er 7 stations op de baan getekend worden
	 */
	public void zevenStations(){
		s1_label.setVisible( true );
		s1_kn.setVisible( true );
		s2_label.setVisible( true );
		s2_kn.setVisible( true );
		s3_label.setVisible( true );
		s3_kn.setVisible( true );
		s4_label.setVisible( true );
		s4_kn.setVisible( true );
		s5_label.setVisible( true );
		s5_kn.setVisible( true );
		s7_label.setVisible( true );
		s7_kn.setVisible( true );
		s8_label.setVisible( true );
		s8_kn.setVisible( true );
		voegToeStation();
		verwijder(7);
		setStationController();
		aantalStation = 7;
		stationLabel(7);


		//1, 2, 3, 4, 5, 7, 8
	}
	/**
	 * Deze methode zorgt ervoor dat er 8 stations op de baan getekend worden
	 */
	public void achtStations(){
		s1_label.setVisible( true );
		s1_kn.setVisible( true );
		s2_label.setVisible( true );
		s2_kn.setVisible( true );
		s3_label.setVisible( true );
		s3_kn.setVisible( true );
		s4_label.setVisible( true );
		s4_kn.setVisible( true );
		s5_label.setVisible( true );
		s5_kn.setVisible( true );
		s6_label.setVisible( true );
		s6_kn.setVisible( true );
		s7_label.setVisible( true );
		s7_kn.setVisible( true );
		s8_label.setVisible( true );
		s8_kn.setVisible( true );
		voegToeStation();
		setStationController();
		aantalStation = 8;
		stationLabel(8);

		//alles
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
		pixels = meters; //meters naar pixels
		if (taxi == 1){

			//eerste stuk horizontaal
			if (meters <= 640 && zij == 0){  
				t1_kn.setLocation(70+pixels,130);
			}

			if( zij >0 && zij <= 40 && meters <=640){ //naar het station
				t1_kn.setLocation(meters+zij+70,130-zij);
			}

			if( zij > 40 && zij < 80 && meters <=640){ //van het station
				t1_kn.setLocation(meters+zij+70,90+(zij-40));
			}

			//eerste stuk vertikaal
			if( meters > 640 && meters <= 960  && zij == 0){
				t1_kn.setLocation(710,130+pixels-640);
			}

			if( zij >0 && zij <= 40 && meters > 640 && meters <= 960){ //naar het station
				t1_kn.setLocation(710+zij,250+zij);
			}

			if( zij > 40 && zij < 80 && meters > 640 && meters <= 960){ //van het station
				t1_kn.setLocation(790-(zij), 250+zij);
			}

			//tweed stuk horizontaal
			if( meters > 960 && meters <= 1600  && zij == 0){
				t1_kn.setLocation(710-(pixels-960),450);
			}
			if( zij >0 && zij <= 40 && meters > 960 && meters <= 1600){ //naar het station
				t1_kn.setLocation(710-((meters+zij)-960),450+zij);
			}
			if( zij > 40 && zij < 80 && meters > 960 && meters <= 1600){ //van het station
				t1_kn.setLocation(710-(meters-960)-(zij), 450-(zij-80));
			}


			//tweede stuk vertikaal
			if( meters >1600 && meters <=1920  && zij == 0){
				t1_kn.setLocation(70,450-(pixels-1600));
			}
			if( zij >0 && zij <= 40 && meters > 1600 && meters <= 1920){ //naar het station
				t1_kn.setLocation(70-zij,330-zij);
			}
			if( zij > 40 && zij < 80 && meters > 1600 && meters <= 1920){ //van het station
				t1_kn.setLocation(70+(zij-80), 330-zij);
			}

		}
		//taxi 2
		if (taxi == 2){

			//eerste stuk horizontaal
			if (meters <= 640 && zij == 0){  
				t2_kn.setLocation(70+pixels,130);
			}

			if( zij >0 && zij <= 40 && meters <=640){ //naar het station
				t2_kn.setLocation(meters+zij+70,130-zij);
			}

			if( zij > 40 && zij < 80 && meters <=640){ //van het station
				t2_kn.setLocation(meters+zij+70,90+(zij-40));
			}

			//eerste stuk vertikaal
			if( meters > 640 && meters <= 960  && zij == 0){
				t2_kn.setLocation(710,130+pixels-640);
			}

			if( zij >0 && zij <= 40 && meters > 640 && meters <= 960){ //naar het station
				t2_kn.setLocation(710+zij,250+zij);
			}

			if( zij > 40 && zij < 80 && meters > 640 && meters <= 960){ //van het station
				t2_kn.setLocation(790-(zij), 250+zij);
			}

			//tweed stuk horizontaal
			if( meters > 960 && meters <= 1600  && zij == 0){
				t2_kn.setLocation(710-(pixels-960),450);
			}
			if( zij >0 && zij <= 40 && meters > 960 && meters <= 1600){ //naar het station
				t2_kn.setLocation(710-((meters+zij)-960),450+zij);
			}
			if( zij > 40 && zij < 80 && meters > 960 && meters <= 1600){ //van het station
				t2_kn.setLocation(710-(meters-960)-(zij), 450-(zij-80));
			}


			//tweede stuk vertikaal
			if( meters >1600 && meters <=1920  && zij == 0){
				t2_kn.setLocation(70,450-(pixels-1600));
			}
			if( zij >0 && zij <= 40 && meters > 1600 && meters <= 1920){ //naar het station
				t2_kn.setLocation(70-zij,330-zij);
			}
			if( zij > 40 && zij < 80 && meters > 1600 && meters <= 1920){ //van het station
				t2_kn.setLocation(70+(zij-80), 330-zij);
			}

		}

		if (taxi == 3 ){

			//eerste stuk horizontaal
			if (meters <= 640 && zij == 0){  
				t3_kn.setLocation(70+pixels,130);
			}

			if( zij >0 && zij <= 40 && meters <=640){ //naar het station
				t3_kn.setLocation(meters+zij+70,130-zij);
			}

			if( zij > 40 && zij < 80 && meters <=640){ //van het station
				t3_kn.setLocation(meters+zij+70,90+(zij-40));
			}

			//eerste stuk vertikaal
			if( meters > 640 && meters <= 960  && zij == 0){
				t3_kn.setLocation(710,130+pixels-640);
			}

			if( zij >0 && zij <= 40 && meters > 640 && meters <= 960){ //naar het station
				t3_kn.setLocation(710+zij,250+zij);
			}

			if( zij > 40 && zij < 80 && meters > 640 && meters <= 960){ //van het station
				t3_kn.setLocation(790-(zij), 250+zij);
			}

			//tweed stuk horizontaal
			if( meters > 960 && meters <= 1600  && zij == 0){
				t3_kn.setLocation(710-(pixels-960),450);
			}
			if( zij >0 && zij <= 40 && meters > 960 && meters <= 1600){ //naar het station
				t3_kn.setLocation(710-((meters+zij)-960),450+zij);
			}
			if( zij > 40 && zij < 80 && meters > 960 && meters <= 1600){ //van het station
				t3_kn.setLocation(710-(meters-960)-(zij), 450-(zij-80));
			}


			//tweede stuk vertikaal
			if( meters >1600 && meters <=1920  && zij == 0){
				t3_kn.setLocation(70,450-(pixels-1600));
			}
			if( zij >0 && zij <= 40 && meters > 1600 && meters <= 1920){ //naar het station
				t3_kn.setLocation(70-zij,330-zij);
			}
			if( zij > 40 && zij < 80 && meters > 1600 && meters <= 1920){ //van het station
				t3_kn.setLocation(70+(zij-80), 330-zij);
			}

		}

		if (taxi == 4){

			//eerste stuk horizontaal
			if (meters <= 640 && zij == 0){  
				t4_kn.setLocation(70+pixels,130);
			}

			if( zij >0 && zij <= 40 && meters <=640){ //naar het station
				t4_kn.setLocation(meters+zij+70,130-zij);
			}

			if( zij > 40 && zij < 80 && meters <=640){ //van het station
				t4_kn.setLocation(meters+zij+70,90+(zij-40));
			}

			//eerste stuk vertikaal
			if( meters > 640 && meters <= 960  && zij == 0){
				t4_kn.setLocation(710,130+pixels-640);
			}

			if( zij >0 && zij <= 40 && meters > 640 && meters <= 960){ //naar het station
				t4_kn.setLocation(710+zij,250+zij);
			}

			if( zij > 40 && zij < 80 && meters > 640 && meters <= 960){ //van het station
				t4_kn.setLocation(790-(zij), 250+zij);
			}

			//tweed stuk horizontaal
			if( meters > 960 && meters <= 1600  && zij == 0){
				t4_kn.setLocation(710-(pixels-960),450);
			}
			if( zij >0 && zij <= 40 && meters > 960 && meters <= 1600){ //naar het station
				t4_kn.setLocation(710-((meters+zij)-960),450+zij);
			}
			if( zij > 40 && zij < 80 && meters > 960 && meters <= 1600){ //van het station
				t4_kn.setLocation(710-(meters-960)-(zij), 450-(zij-80));
			}


			//tweede stuk vertikaal
			if( meters >1600 && meters <=1920  && zij == 0){
				t4_kn.setLocation(70,450-(pixels-1600));
			}
			if( zij >0 && zij <= 40 && meters > 1600 && meters <= 1920){ //naar het station
				t4_kn.setLocation(70-zij,330-zij);
			}
			if( zij > 40 && zij < 80 && meters > 1600 && meters <= 1920){ //van het station
				t4_kn.setLocation(70+(zij-80), 330-zij);
			}

		}

		if (taxi == 5){

			//eerste stuk horizontaal
			if (meters <= 640 && zij == 0){  
				t5_kn.setLocation(70+pixels,130);
			}

			if( zij >0 && zij <= 40 && meters <=640){ //naar het station
				t5_kn.setLocation(meters+zij+70,130-zij);
			}

			if( zij > 40 && zij < 80 && meters <=640){ //van het station
				t5_kn.setLocation(meters+zij+70,90+(zij-40));
			}

			//eerste stuk vertikaal
			if( meters > 640 && meters <= 960  && zij == 0){
				t5_kn.setLocation(710,130+pixels-640);
			}

			if( zij >0 && zij <= 40 && meters > 640 && meters <= 960){ //naar het station
				t5_kn.setLocation(710+zij,250+zij);
			}

			if( zij > 40 && zij < 80 && meters > 640 && meters <= 960){ //van het station
				t5_kn.setLocation(790-(zij), 250+zij);
			}

			//tweed stuk horizontaal
			if( meters > 960 && meters <= 1600  && zij == 0){
				t5_kn.setLocation(710-(pixels-960),450);
			}
			if( zij >0 && zij <= 40 && meters > 960 && meters <= 1600){ //naar het station
				t5_kn.setLocation(710-((meters+zij)-960),450+zij);
			}
			if( zij > 40 && zij < 80 && meters > 960 && meters <= 1600){ //van het station
				t5_kn.setLocation(710-(meters-960)-(zij), 450-(zij-80));
			}


			//tweede stuk vertikaal
			if( meters >1600 && meters <=1920  && zij == 0){
				t5_kn.setLocation(70,450-(pixels-1600));
			}
			if( zij >0 && zij <= 40 && meters > 1600 && meters <= 1920){ //naar het station
				t5_kn.setLocation(70-zij,330-zij);
			}
			if( zij > 40 && zij < 80 && meters > 1600 && meters <= 1920){ //van het station
				t5_kn.setLocation(70+(zij-80), 330-zij);
			}

		}
		if (taxi == 6){

			//eerste stuk horizontaal
			if (meters <= 640 && zij == 0){  
				t6_kn.setLocation(70+pixels,130);
			}

			if( zij >0 && zij <= 40 && meters <=640){ //naar het station
				t6_kn.setLocation(meters+zij+70,130-zij);
			}

			if( zij > 40 && zij < 80 && meters <=640){ //van het station
				t6_kn.setLocation(meters+zij+70,90+(zij-40));
			}

			//eerste stuk vertikaal
			if( meters > 640 && meters <= 960  && zij == 0){
				t6_kn.setLocation(710,130+pixels-640);
			}

			if( zij >0 && zij <= 40 && meters > 640 && meters <= 960){ //naar het station
				t6_kn.setLocation(710+zij,250+zij);
			}

			if( zij > 40 && zij < 80 && meters > 640 && meters <= 960){ //van het station
				t6_kn.setLocation(790-(zij), 250+zij);
			}

			//tweed stuk horizontaal
			if( meters > 960 && meters <= 1600  && zij == 0){
				t6_kn.setLocation(710-(pixels-960),450);
			}
			if( zij >0 && zij <= 40 && meters > 960 && meters <= 1600){ //naar het station
				t6_kn.setLocation(710-((meters+zij)-960),450+zij);
			}
			if( zij > 40 && zij < 80 && meters > 960 && meters <= 1600){ //van het station
				t6_kn.setLocation(710-(meters-960)-(zij), 450-(zij-80));
			}


			//tweede stuk vertikaal
			if( meters >1600 && meters <=1920  && zij == 0){
				t6_kn.setLocation(70,450-(pixels-1600));
			}
			if( zij >0 && zij <= 40 && meters > 1600 && meters <= 1920){ //naar het station
				t6_kn.setLocation(70-zij,330-zij);
			}
			if( zij > 40 && zij < 80 && meters > 1600 && meters <= 1920){ //van het station
				t6_kn.setLocation(70+(zij-80), 330-zij);
			}

		}

		if (taxi == 7){

			//eerste stuk horizontaal
			if (meters <= 640 && zij == 0){  
				t7_kn.setLocation(70+pixels,130);
			}

			if( zij >0 && zij <= 40 && meters <=640){ //naar het station
				t7_kn.setLocation(meters+zij+70,130-zij);
			}

			if( zij > 40 && zij < 80 && meters <=640){ //van het station
				t7_kn.setLocation(meters+zij+70,90+(zij-40));
			}

			//eerste stuk vertikaal
			if( meters > 640 && meters <= 960  && zij == 0){
				t7_kn.setLocation(710,130+pixels-640);
			}

			if( zij >0 && zij <= 40 && meters > 640 && meters <= 960){ //naar het station
				t7_kn.setLocation(710+zij,250+zij);
			}

			if( zij > 40 && zij < 80 && meters > 640 && meters <= 960){ //van het station
				t7_kn.setLocation(790-(zij), 250+zij);
			}

			//tweed stuk horizontaal
			if( meters > 960 && meters <= 1600  && zij == 0){
				t7_kn.setLocation(710-(pixels-960),450);
			}
			if( zij >0 && zij <= 40 && meters > 960 && meters <= 1600){ //naar het station
				t7_kn.setLocation(710-((meters+zij)-960),450+zij);
			}
			if( zij > 40 && zij < 80 && meters > 960 && meters <= 1600){ //van het station
				t7_kn.setLocation(710-(meters-960)-(zij), 450-(zij-80));
			}


			//tweede stuk vertikaal
			if( meters >1600 && meters <=1920  && zij == 0){
				t7_kn.setLocation(70,450-(pixels-1600));
			}
			if( zij >0 && zij <= 40 && meters > 1600 && meters <= 1920){ //naar het station
				t7_kn.setLocation(70-zij,330-zij);
			}
			if( zij > 40 && zij < 80 && meters > 1600 && meters <= 1920){ //van het station
				t7_kn.setLocation(70+(zij-80), 330-zij);
			}

		}

		if (taxi == 8){

			//eerste stuk horizontaal
			if (meters <= 640 && zij == 0){  
				t8_kn.setLocation(70+pixels,130);
			}

			if( zij >0 && zij <= 40 && meters <=640){ //naar het station
				t8_kn.setLocation(meters+zij+70,130-zij);
			}

			if( zij > 40 && zij < 80 && meters <=640){ //van het station
				t8_kn.setLocation(meters+zij+70,90+(zij-40));
			}

			//eerste stuk vertikaal
			if( meters > 640 && meters <= 960  && zij == 0){
				t8_kn.setLocation(710,130+pixels-640);
			}

			if( zij >0 && zij <= 40 && meters > 640 && meters <= 960){ //naar het station
				t8_kn.setLocation(710+zij,250+zij);
			}

			if( zij > 40 && zij < 80 && meters > 640 && meters <= 960){ //van het station
				t8_kn.setLocation(790-(zij), 250+zij);
			}

			//tweed stuk horizontaal
			if( meters > 960 && meters <= 1600  && zij == 0){
				t8_kn.setLocation(710-(pixels-960),450);
			}
			if( zij >0 && zij <= 40 && meters > 960 && meters <= 1600){ //naar het station
				t8_kn.setLocation(710-((meters+zij)-960),450+zij);
			}
			if( zij > 40 && zij < 80 && meters > 960 && meters <= 1600){ //van het station
				t8_kn.setLocation(710-(meters-960)-(zij), 450-(zij-80));
			}


			//tweede stuk vertikaal
			if( meters >1600 && meters <=1920  && zij == 0){
				t8_kn.setLocation(70,450-(pixels-1600));
			}
			if( zij >0 && zij <= 40 && meters > 1600 && meters <= 1920){ //naar het station
				t8_kn.setLocation(70-zij,330-zij);
			}
			if( zij > 40 && zij < 80 && meters > 1600 && meters <= 1920){ //van het station
				t8_kn.setLocation(70+(zij-80), 330-zij);
			}

		}
	}


	/**
	 * 
	 * Hij zet alle taxi's op hun initiele plek, waarna ze vervolgens verplaatsen kunnen.
	 * hij berekend de initiele plek aan de hand van de absolute positie.
	 * @param absolute
	 * @param zij
	 * @param ID
	 */
	public void setInitLoc(double absolute, double zij, int ID){
		int ab = 0;
		int z = 0;

		ab = new Double((absolute)/12.5).intValue(); 
		z = new Double((zij)/12.5).intValue();


		System.out.println("Taxi: " + ID);
		System.out.println(ab);
		System.out.println(z);

		if(ID == 1){
			if(ab <= 640){
				t1_kn.setLocation(110+ab,50+z);
			}
			if(ab> 640 && ab <= 960 ){
				t1_kn.setLocation(750,130+(ab-640)+40);
			}
			if(ab> 960 && ab <=1600){
				t1_kn.setLocation(710-(ab-920),450+z);
			}
			if(ab>1600 && ab <1920){
				t1_kn.setLocation(30,450-(ab-1560));
			}    
		}
		if(ID == 2){

			if(ab <= 640){
				t2_kn.setLocation(110+ab,50+z);
			}
			if(ab> 640 && ab <= 960 ){
				t2_kn.setLocation(750,130+(ab-640)+40);
			}
			if(ab> 960 && ab <=1600){
				t2_kn.setLocation(710-(ab-920),450+z);
			}
			if(ab>1600 && ab <1920){
				t2_kn.setLocation(30,450-(ab-1560));
			}    
		}

		if(ID == 3){
			if(ab <= 640){
				t3_kn.setLocation(110+ab,50+z);
			}
			if(ab> 640 && ab <= 960 ){
				t3_kn.setLocation(750,130+(ab-640)+40);
			}
			if(ab> 960 && ab <=1600){
				t3_kn.setLocation(710-(ab-920),450+z);
			}
			if(ab>1600 && ab <1920){
				t3_kn.setLocation(30,450-(ab-1560));
			}    
		}
		if(ID == 4){
			if(ab <= 640){
				t4_kn.setLocation(110+ab,50+z);
			}
			if(ab> 640 && ab <= 960 ){
				t4_kn.setLocation(750,130+(ab-640)+40);
			}
			if(ab> 960 && ab <=1600){
				t4_kn.setLocation(710-(ab-920),450+z);
			}
			if(ab>1600 && ab <1920){
				t4_kn.setLocation(30,450-(ab-1560));
			}    
		}
		if(ID == 5){
			if(ab <= 640){
				t5_kn.setLocation(110+ab,50+z);
			}
			if(ab> 640 && ab <= 960 ){
				t5_kn.setLocation(750,130+(ab-640)+40);
			}
			if(ab> 960 && ab <=1600){
				t5_kn.setLocation(710-(ab-920),450+z);
			}
			if(ab>1600 && ab <1920){
				t5_kn.setLocation(30,450-(ab-1560));
			}    
		}
		if(ID == 6){
			if(ab <= 640){
				t6_kn.setLocation(110+ab,50+z);
			}
			if(ab> 640 && ab <= 960 ){
				t6_kn.setLocation(750,130+(ab-640)+40);
			}
			if(ab> 960 && ab <=1600){
				t6_kn.setLocation(710-(ab-920),450+z);
			}
			if(ab>1600 && ab <1920){
				t6_kn.setLocation(30,450-(ab-1560));
			}    
		}
		if(ID == 7){
			if(ab <= 640){
				t7_kn.setLocation(110+ab,50+z);
			}
			if(ab> 640 && ab <= 960 ){
				t7_kn.setLocation(750,130+(ab-640)+40);
			}
			if(ab> 960 && ab <=1600){
				t7_kn.setLocation(710-(ab-920),450+z);
			}
			if(ab>1600 && ab <1920){
				t7_kn.setLocation(30,450-(ab-1560));
			}    
		}
		if(ID == 8){
			if(ab <= 640){
				t8_kn.setLocation(110+ab,50+z);
			}
			if(ab> 640 && ab <= 960 ){
				t8_kn.setLocation(750,130+(ab-640)+40);
			}
			if(ab> 960 && ab <=1600){
				t8_kn.setLocation(710-(ab-920),450+z);
			}
			if(ab>1600 && ab <1920){
				t8_kn.setLocation(30,450-(ab-1560));
			}    
		}
	}
	//methodes voor de taxi's

	/**
	 * Tekend 1 taxi
	 */
	public void eenTaxi(){
		t1_kn.setVisible( true );
	}
	/**
	 * Tekend 2 taxi's
	 */
	public void tweeTaxi(){
		t1_kn.setVisible( true );
		t2_kn.setVisible( true );

	}
	/**
	 * Tekend 3 taxi's
	 */
	public void drieTaxi(){
		t1_kn.setVisible( true );
		t2_kn.setVisible( true );
		t3_kn.setVisible( true );

	}
	/**
	 * Tekend 4 taxi's
	 */
	public void vierTaxi(){
		t1_kn.setVisible( true );
		t2_kn.setVisible( true );
		t3_kn.setVisible( true );
		t4_kn.setVisible( true );

	}
	/**
	 * Tekend 5 taxi's
	 */
	public void vijfTaxi(){
		t1_kn.setVisible( true );
		t2_kn.setVisible( true );
		t3_kn.setVisible( true );
		t4_kn.setVisible( true );
		t5_kn.setVisible( true );

	}
	/**
	 * Tekend 6 taxi's
	 */
	public void zesTaxi(){
		t1_kn.setVisible( true );
		t2_kn.setVisible( true );
		t3_kn.setVisible( true );
		t4_kn.setVisible( true );
		t5_kn.setVisible( true );
		t6_kn.setVisible( true );

	}
	/**
	 * Tekend 7 taxi's
	 */
	public void zevenTaxi(){
		t1_kn.setVisible( true );
		t2_kn.setVisible( true );
		t3_kn.setVisible( true );
		t4_kn.setVisible( true );
		t5_kn.setVisible( true );
		t6_kn.setVisible( true );
		t7_kn.setVisible( true );

	}
	/**
	 * Tekend 8 taxi's
	 */
	public void achtTaxi(){
		t1_kn.setVisible( true );
		t2_kn.setVisible( true );
		t3_kn.setVisible( true );
		t4_kn.setVisible( true );
		t5_kn.setVisible( true );
		t6_kn.setVisible( true );
		t7_kn.setVisible( true );
		t8_kn.setVisible( true );

	}
	/**
	 * Opend een nieuw object van TekenSpoor, met this als argument, dat wil zeggen, de constructor van paneel.
	 */
	public void openPaneel(){
		new TekenSpoor(this);

	}
	/**
	 * Deze methode vult de label text in met de behoordestation naam die afkomstig is vanuit de controller. Om dat de doen maak
	 * hij gebruik van de aantalStation parameter die hij krijgt als je de aantal statioon invoerd
	 * @param aantalStation
	 */
	public void stationLabel(int aantalStation){
		switch(aantalStation){
		case 8:
			s_NaamLabel1.setText(controller.getNaam(1));
			s_NaamLabel2.setText(controller.getNaam(2));
			s_NaamLabel3.setText(controller.getNaam(3));
			s_NaamLabel4.setText(controller.getNaam(4));
			s_NaamLabel5.setText(controller.getNaam(5));
			s_NaamLabel6.setText(controller.getNaam(6));
			s_NaamLabel7.setText(controller.getNaam(7));
			s_NaamLabel8.setText(controller.getNaam(8));
			break;
		case 7:
			s_NaamLabel1.setText(controller.getNaam(1));
			s_NaamLabel2.setText(controller.getNaam(2));
			s_NaamLabel3.setText(controller.getNaam(3));
			s_NaamLabel4.setText(controller.getNaam(4));
			s_NaamLabel5.setText(controller.getNaam(5));
			s_NaamLabel7.setText(controller.getNaam(6));
			s_NaamLabel8.setText(controller.getNaam(7));
			break;
		case 6:
			s_NaamLabel1.setText(controller.getNaam(1));
			s_NaamLabel3.setText(controller.getNaam(2));
			s_NaamLabel4.setText(controller.getNaam(3));
			s_NaamLabel5.setText(controller.getNaam(4));
			s_NaamLabel7.setText(controller.getNaam(5));
			s_NaamLabel8.setText(controller.getNaam(6));
			break;
		case 5:
			s_NaamLabel2.setText(controller.getNaam(1));
			s_NaamLabel4.setText(controller.getNaam(2));
			s_NaamLabel5.setText(controller.getNaam(3));
			s_NaamLabel7.setText(controller.getNaam(4));
			s_NaamLabel8.setText(controller.getNaam(5));
			break;
		case 4:
			s_NaamLabel1.setText(controller.getNaam(1));
			s_NaamLabel3.setText(controller.getNaam(2));
			s_NaamLabel5.setText(controller.getNaam(3));
			s_NaamLabel7.setText(controller.getNaam(4));
			break;
		case 3:
			s_NaamLabel2.setText(controller.getNaam(1));
			s_NaamLabel5.setText(controller.getNaam(2));
			s_NaamLabel7.setText(controller.getNaam(3));
			break;
		case 2:
			s_NaamLabel1.setText(controller.getNaam(1));
			s_NaamLabel5.setText(controller.getNaam(2));
			break;
		}
	}
	/**
	 * Deze methode zorg ervopor dat je de label kan zien op het paneel
	 */
	public void zetLabelOpTrue(){
		s_NaamLabel1.setVisible(true);
		s_NaamLabel2.setVisible(true);
		s_NaamLabel3.setVisible(true);
		s_NaamLabel4.setVisible(true);
		s_NaamLabel5.setVisible(true);
		s_NaamLabel6.setVisible(true);
		s_NaamLabel7.setVisible(true);
		s_NaamLabel8.setVisible(true);
	}
	/**
	 * Deze methode zet de label op spatie
	 */
	public void zetLabelTextOpNull(){
		s_NaamLabel1.setText(" ");
		s_NaamLabel2.setText(" ");
		s_NaamLabel3.setText(" ");
		s_NaamLabel4.setText(" ");
		s_NaamLabel5.setText(" ");
		s_NaamLabel6.setText(" ");
		s_NaamLabel7.setText(" ");
		s_NaamLabel8.setText(" ");
	}
	/**
	 * Deze methode zorg ervoor dat je de label onzichtbaar worden
	 */
	public void zelLabelOpFalse(){
		s_NaamLabel1.setVisible(false);
		s_NaamLabel2.setVisible(false);
		s_NaamLabel3.setVisible(false);
		s_NaamLabel4.setVisible(false);
		s_NaamLabel5.setVisible(false);
		s_NaamLabel6.setVisible(false);
		s_NaamLabel7.setVisible(false);
		s_NaamLabel8.setVisible(false);
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
	public void voegStationsToe(int station){

		switch (station) {
		case 2: tweeStations(); break;
		case 3: drieStations(); break;
		case 4: vierStations(); break;
		case 5: vijfStations(); break;
		case 6: zesStations(); break;
		case 7: zevenStations(); break;
		case 8: achtStations(); break;
		default: break;
		}
	}
	/**
	 * Deze methode voeg de aantal taxi toe... Wordt vooor moment opname gebruikt
	 * @param t_axi
	 */
	public void voegTaxiToe(int t_axi){
		switch (t_axi) {
		case 1: eenTaxi();
		setTaxi(t_axi);
		break;
		case 2: tweeTaxi();
		setTaxi(t_axi);
		break;
		case 3: drieTaxi();
		setTaxi(t_axi);
		break;
		case 4: vierTaxi();
		setTaxi(t_axi);
		break;
		case 5: vijfTaxi();
		setTaxi(t_axi);
		break;
		case 6: zesTaxi();
		setTaxi(t_axi);
		break;
		case 7: zevenTaxi();
		setTaxi(t_axi);
		break;
		case 8: achtTaxi();
		setTaxi(t_axi);
		break;
		default: break;
		}
	}

	/**
	 * Deze methode wordt gebruikt om de status van de simulatie bij te houden
	 * @return true / false
	 */
	public boolean isSimGestart(){

		return simIsGestart;

	}

	/**
	 * Deze methiode wordt gebruikt om de status van de simulatie te weergeven als je wel of niet gepauzeerd is
	 * @return hij geeft een true of false terug
	 */
	public boolean isSimGepazeerd(){

		return simIsPauze;
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
				zetLabelOpTrue();
				start.setText("Simulatie Stoppen");
				openPaneel();
				hBaan.setVisible( true );    
				i++;
				startTimer();
				verwijder();
				controller.startSimulatie(i);
				zetLabelOpTrue();
			}
			else if(i == 1 && simIsPauze == false){
				y = 0;
				z = 0;
				status.setText("Gestopt");

				simIsGestart = false;
				isSimGestart();
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
				zelLabelOpFalse();
				controller.startSimulatie(i);
				zetLabelTextOpNull();
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
				isSimGepazeerd();
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
					zetLabelOpTrue();
					start.setText("Simulatie Stoppen");
					y = 0;
					z = 0;
					i++;
					simIsGestart = true;
					UserInterface.isGestart(simIsGestart);
					isSimGestart(); 
					simIsPauze = false;
					UserInterface.isGepauzeerd(simIsPauze);
					isSimGepazeerd();
					voegStationsToe(controller.stationGegevensOphalen());
					voegTaxiToe(controller.taxiGegevensOphalen());
					controller.voegObservToe(controller.taxiGegevensOphalen());
					hBaan.setVisible( true );    
					startTimer();
					zetLabelOpTrue();

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
	class Taxi_1 implements ActionListener {
		public void actionPerformed(ActionEvent e){
			new TaxiStats(controller.getPositie(1), controller.getReizInTaxi(1), controller.getReisTijd(1), 1);
		}
	}
	/**
	 * @author dareal
	 * Deze methode roept vult de controctor van de taxistats klasse(taxiu statustiek) met de juiste gegevens. 
	 *
	 */
	class Taxi_2 implements ActionListener {
		public void actionPerformed(ActionEvent e){
			//new TaxiStats(controller.getPositie(), controller.getReizInTaxi(2), controller.getWachtTijd(), controller.getReisTijd(), controller.getGemBezet(), controller.getGemWachtTijd(), controller.getGemReisTijd());
			new TaxiStats(controller.getPositie(2), controller.getReizInTaxi(2), controller.getReisTijd(2), 2);
		}
	}
	/**
	 * @author dareal
	 * Deze methode roept vult de controctor van de taxistats klasse(taxiu statustiek) met de juiste gegevens. 
	 *
	 */
	class Taxi_3 implements ActionListener {
		public void actionPerformed(ActionEvent e){
			//new TaxiStats(controller.getPositie(), controller.getReizInTaxi(3), controller.getWachtTijd(), controller.getReisTijd(), controller.getGemBezet(), controller.getGemWachtTijd(), controller.getGemReisTijd());
			new TaxiStats(controller.getPositie(3), controller.getReizInTaxi(3), controller.getReisTijd(3), 3);
		}
	}
	/**
	 * @author dareal
	 * Deze methode roept vult de controctor van de taxistats klasse(taxiu statustiek) met de juiste gegevens. 
	 *
	 */
	class Taxi_4 implements ActionListener {
		public void actionPerformed(ActionEvent e){
			//new TaxiStats(controller.getPositie(), controller.getReizInTaxi(4), controller.getWachtTijd(), controller.getReisTijd(), controller.getGemBezet(), controller.getGemWachtTijd(), controller.getGemReisTijd());
			new TaxiStats(controller.getPositie(4), controller.getReizInTaxi(4), controller.getReisTijd(4), 4);
		}
	}
	/**
	 * @author dareal
	 * Deze methode roept vult de controctor van de taxistats klasse(taxiu statustiek) met de juiste gegevens. 
	 *
	 */
	class Taxi_5 implements ActionListener {
		public void actionPerformed(ActionEvent e){
			//new TaxiStats(controller.getPositie(), controller.getReizInTaxi(5), controller.getWachtTijd(), controller.getReisTijd(), controller.getGemBezet(), controller.getGemWachtTijd(), controller.getGemReisTijd());
			new TaxiStats(controller.getPositie(5), controller.getReizInTaxi(5), controller.getReisTijd(5), 5);
		}
	}
	/**
	 * @author dareal
	 * Deze methode roept vult de controctor van de taxistats klasse(taxiu statustiek) met de juiste gegevens. 
	 *
	 */
	class Taxi_6 implements ActionListener {
		public void actionPerformed(ActionEvent e){
			//new TaxiStats(controller.getPositie(), controller.getReizInTaxi(6), controller.getWachtTijd(), controller.getReisTijd(), controller.getGemBezet(), controller.getGemWachtTijd(), controller.getGemReisTijd());
			new TaxiStats(controller.getPositie(6), controller.getReizInTaxi(6), controller.getReisTijd(6), 6);
		}
	}
	/**
	 * @author dareal
	 * Deze methode roept vult de controctor van de taxistats klasse(taxiu statustiek) met de juiste gegevens. 
	 *
	 */
	class Taxi_7 implements ActionListener {
		public void actionPerformed(ActionEvent e){
			//new TaxiStats(controller.getPositie(), controller.getReizInTaxi(7), controller.getWachtTijd(), controller.getReisTijd(), controller.getGemBezet(), controller.getGemWachtTijd(), controller.getGemReisTijd());
			new TaxiStats(controller.getPositie(7), controller.getReizInTaxi(7), controller.getReisTijd(7), 7);
		}
	}
	/**
	 * @author dareal
	 * Deze methode roept vult de controctor van de taxistats klasse(taxiu statustiek) met de juiste gegevens. 
	 *
	 */
	class Taxi_8 implements ActionListener {
		public void actionPerformed(ActionEvent e){
			//new TaxiStats(controller.getPositie(), controller.getReizInTaxi(8), controller.getWachtTijd(), controller.getReisTijd(), controller.getGemBezet(), controller.getGemWachtTijd(), controller.getGemReisTijd());
			new TaxiStats(controller.getPositie(8), controller.getReizInTaxi(8), controller.getReisTijd(8), 8);
		}
	}
	/**
	 * @author dareal
	 * Deze methode roept vult de controctor van de taxistats klasse(taxiu statustiek) met de juiste gegevens. 
	 *
	 */
	class Amstel implements ActionListener {
		public void actionPerformed(ActionEvent a ){
			new StationStats(controller.getNaam(1), controller.getA_reizigers(1), controller.getA_taxis(1));
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

	class Sloterdijk implements ActionListener {
		public void actionPerformed(ActionEvent a ){
			
			switch(aantalStation){
			case 5:
				new StationStats(controller.getNaam(1), controller.getA_reizigers(1), controller.getA_taxis(1));
				break;
			case 3:
				new StationStats(controller.getNaam(3), controller.getA_reizigers(3), controller.getA_taxis(3));
				break;
			default:
				new StationStats(controller.getNaam(2), controller.getA_reizigers(2), controller.getA_taxis(2));
			break;
			}              
		}
	}
	class AmsterdamCS implements ActionListener {
		public void actionPerformed(ActionEvent a ){
			switch(aantalStation){
			case 6:
				new StationStats(controller.getNaam(2), controller.getA_reizigers(2), controller.getA_taxis(2));
				break;
			case 4:
				new StationStats(controller.getNaam(2), controller.getA_reizigers(2), controller.getA_taxis(2));
				break;
			default:
				new StationStats(controller.getNaam(3), controller.getA_reizigers(3), controller.getA_taxis(3));
			//baanManager.getStations();
			break;
			}

		}
	}
	class Lelylaan implements ActionListener {
		public void actionPerformed(ActionEvent a ){
			switch(aantalStation){
			case 6:
				new StationStats(controller.getNaam(3), controller.getA_reizigers(3), controller.getA_taxis(3));
				break;
			case 5:
				new StationStats(controller.getNaam(2), controller.getA_reizigers(2), controller.getA_taxis(2));
				break;
			default:
				new StationStats(controller.getNaam(4), controller.getA_reizigers(4), controller.getA_taxis(4));
			break;
			}                       
		}
	}

	class Muiderpoort implements ActionListener {
		public void actionPerformed(ActionEvent a ){
			switch(aantalStation){
			case 6:
				new StationStats(controller.getNaam(4), controller.getA_reizigers(4), controller.getA_taxis(4));
				break;
			case 5:
				new StationStats(controller.getNaam(3), controller.getA_reizigers(3), controller.getA_taxis(3));
				break;
			case 4:
				new StationStats(controller.getNaam(3), controller.getA_reizigers(3), controller.getA_taxis(3));
				break;
			case 3:
				new StationStats(controller.getNaam(1), controller.getA_reizigers(1), controller.getA_taxis(1));
				break;
			case 2:
				new StationStats(controller.getNaam(2), controller.getA_reizigers(2), controller.getA_taxis(2));
				break;
			default:
				new StationStats(controller.getNaam(5), controller.getA_reizigers(5), controller.getA_taxis(5));
			break;

			}                       
		}
	}
	class ZuidWTC implements ActionListener {
		public void actionPerformed(ActionEvent a ){
			new StationStats(controller.getNaam(6), controller.getA_reizigers(6), controller.getA_taxis(6));
		}
	}
	class Rai implements ActionListener {
		public void actionPerformed(ActionEvent a ){
			switch(aantalStation){
			case 7:
				new StationStats(controller.getNaam(6), controller.getA_reizigers(6), controller.getA_taxis(6));
				break;
			case 6:
				new StationStats(controller.getNaam(5), controller.getA_reizigers(5), controller.getA_taxis(5));
				break;
			case 5:
				new StationStats(controller.getNaam(4), controller.getA_reizigers(4), controller.getA_taxis(4));
				break;
			case 4:
				new StationStats(controller.getNaam(4), controller.getA_reizigers(4), controller.getA_taxis(4));
				break;
			case 3:
				new StationStats(controller.getNaam(3), controller.getA_reizigers(3), controller.getA_taxis(3));
				break;
			default:
				new StationStats(controller.getNaam(7), controller.getA_reizigers(7), controller.getA_taxis(7));
			break;
			}                       
		}
	}
	class Duivendrecht implements ActionListener {
		public void actionPerformed(ActionEvent a ){
			switch(aantalStation){
			case 7:
				new StationStats(controller.getNaam(7), controller.getA_reizigers(7), controller.getA_taxis(7));
				break;
			case 6:
				new StationStats(controller.getNaam(6), controller.getA_reizigers(6), controller.getA_taxis(6));
				break;
			case 5:
				new StationStats(controller.getNaam(5), controller.getA_reizigers(5), controller.getA_taxis(5));
				break;
			default:
				new StationStats(controller.getNaam(8), controller.getA_reizigers(8), controller.getA_taxis(8));
			break;
			}                                              //ALs ke 7 station kiest dna moet je hier 7 invullen
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
