package gui;

import business.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

import simulatie.*;

public class Paneel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Station
	private JButton getRoute, clearReizigers, maakReizigers;
	private ImageIcon treinbaan;
	private Simulatie sim;
	private Station stations;
	private int aantalRailCabs = 0;
	private Datum datum;
	private RefreshGui refresh;
	private Calendar calendar;
	
	private int xy_stations[][];
	private int xy_stations_node[][][];
	private int xy_tussenSens[][];
	private int xy_voorSens[][][];
	private int xy_treinen[][];
	private int begin_x = 0;
	private int begin_y = 0;
	private ArrayList<Trein> trein;
    
	public Paneel(Simulatie sim){
		datum = new Datum();
		calendar = Calendar.getInstance();

		treinbaan = new ImageIcon("treinbaan.jpg");

		this.sim = sim;
		stations = sim.getStations();
		xy_stations_node = new int [8][2][2];
		xy_stations = new int[8][2];
		xy_tussenSens = new int[8][2];
		xy_voorSens = new int[8][6][2];
		
		/*
		 * Het aanmaken van de station posities. Het is een dubbele array waarbij de eerste index het stationnummer is en de tweede de x of y as.
		 */
		xy_stations[0][0] = 264; xy_stations[0][1] = 50; 
        xy_stations[1][0] = 555; xy_stations[1][1] = 50;
        xy_stations[2][0] = 413; xy_stations[2][1] = 219;
        xy_stations[3][0] = 422; xy_stations[3][1] = 400;
        xy_stations[4][0] = 545; xy_stations[4][1] = 575;
        xy_stations[5][0] = 255; xy_stations[5][1] = 575;
        xy_stations[6][0] = 41; xy_stations[6][1] = 420;
        xy_stations[7][0] = 41; xy_stations[7][1] = 169;
        
        /*
         * Deze array bepaald waar de wissels komen.
         * De eerste index bepaalt het station waarbij de wissel hoort. 
         * De tweede bepaalt of het een wissel voor of na het station is.
         * De derde bepaalt de x of de y as. 
         */
        xy_stations_node[0][0][0] = 210;  xy_stations_node[0][0][1] = 50; 
        xy_stations_node[1][0][0] = 505; xy_stations_node[1][0][1] = 50;
        xy_stations_node[2][0][0] = 439;  xy_stations_node[2][0][1] = 219;
        xy_stations_node[3][0][0] = 371;  xy_stations_node[3][0][1] = 400;
        xy_stations_node[4][0][0] = 571; xy_stations_node[4][0][1] = 575;
        xy_stations_node[5][0][0] = 279;  xy_stations_node[5][0][1] = 575;
        xy_stations_node[6][0][0] = 41;  xy_stations_node[6][0][1] = 442;
        xy_stations_node[7][0][0] = 41;  xy_stations_node[7][0][1] = 187;

        xy_stations_node[0][1][0] = 278;  xy_stations_node[0][1][1] = 50; 
        xy_stations_node[1][1][0] = 570; xy_stations_node[1][1][1] = 50;
        xy_stations_node[2][1][0] = 380;  xy_stations_node[2][1][1] = 219;
        xy_stations_node[3][1][0] = 430;  xy_stations_node[3][1][1] = 400;
        xy_stations_node[4][1][0] = 508; xy_stations_node[4][1][1] = 575;
        xy_stations_node[5][1][0] = 213;  xy_stations_node[5][1][1] = 575;
        xy_stations_node[6][1][0] = 41;  xy_stations_node[6][1][1] = 385;
        xy_stations_node[7][1][0] = 41;  xy_stations_node[7][1][1] = 140;
        
        /*
         * De tussensensoren zitten tussen twee wissels in. Elke sensor hoort bij een station.
         * De eerste index is het station waarbij het hoort.
         * De tweede index is de x of y as. 
         */
        xy_tussenSens[0][0] = 264;  xy_tussenSens[0][1] = 31; 
        xy_tussenSens[1][0] = 555; xy_tussenSens[1][1] = 31;
        xy_tussenSens[2][0] = 413;  xy_tussenSens[2][1] = 199;
        xy_tussenSens[3][0] = 422;  xy_tussenSens[3][1] = 420;
        xy_tussenSens[4][0] = 545; xy_tussenSens[4][1] = 595;
        xy_tussenSens[5][0] = 255;  xy_tussenSens[5][1] = 595;
        xy_tussenSens[6][0] = 60;  xy_tussenSens[6][1] = 420;
        xy_tussenSens[7][0] = 60;  xy_tussenSens[7][1] = 169;
        
        /*
         * Voorsensoren zij de sensoren voor een station. Elke sensor hoort bij een station.
         * De eerste index is het station waarbij het hoort.
         * De tweede index is de x of y as. 
         */
        xy_voorSens[0][0][0] = 60; xy_voorSens[0][0][1] = 74; 
        xy_voorSens[0][1][0] = 113; xy_voorSens[0][1][1] = 31;
		xy_voorSens[0][2][0] = 179; xy_voorSens[0][2][1] = 31;
		
		xy_voorSens[1][0][0] = 352;  xy_voorSens[1][0][1] = 31;
		xy_voorSens[1][1][0] = 467; xy_voorSens[1][1][1] = 31;
		
		xy_voorSens[2][0][0] = 658; xy_voorSens[2][0][1] = 31;
		xy_voorSens[2][1][0] = 690; xy_voorSens[2][1][1] = 75; 
        xy_voorSens[2][2][0] = 690; xy_voorSens[2][2][1] = 160;
		xy_voorSens[2][3][0] = 649; xy_voorSens[2][3][1] = 199;
		xy_voorSens[2][4][0] = 501; xy_voorSens[2][4][1] = 199;

		xy_voorSens[3][0][0] = 323; xy_voorSens[3][0][1] = 199;
		xy_voorSens[3][1][0] = 203; xy_voorSens[3][1][1] = 199;
		xy_voorSens[3][2][0] = 158; xy_voorSens[3][2][1] = 241; 
        xy_voorSens[3][3][0] = 158; xy_voorSens[3][3][1] = 382;
		xy_voorSens[3][4][0] = 200; xy_voorSens[3][4][1] = 420;
		xy_voorSens[3][5][0] = 334; xy_voorSens[3][5][1] = 420;

		xy_voorSens[4][0][0] = 514; xy_voorSens[4][0][1] = 420;
		xy_voorSens[4][1][0] = 647; xy_voorSens[4][1][1] = 420;
		xy_voorSens[4][2][0] = 690; xy_voorSens[4][2][1] = 458; 
        xy_voorSens[4][3][0] = 690; xy_voorSens[4][3][1] = 560;
		xy_voorSens[4][4][0] = 645; xy_voorSens[4][4][1] = 595;

		xy_voorSens[5][0][0] = 459; xy_voorSens[5][0][1] = 595;
		xy_voorSens[5][1][0] = 340;  xy_voorSens[5][1][1] = 595;

		xy_voorSens[6][0][0] = 165; xy_voorSens[6][0][1] = 595;
		xy_voorSens[6][1][0] = 103; xy_voorSens[6][1][1] = 595; 
        xy_voorSens[6][2][0] = 60; xy_voorSens[6][2][1] = 545;
		xy_voorSens[6][3][0] = 60; xy_voorSens[6][3][1] = 501;

		xy_voorSens[7][0][0] = 60; xy_voorSens[7][0][1] = 332;
		xy_voorSens[7][1][0] = 60; xy_voorSens[7][1][1] = 249;
		
		aantalRailCabs = sim.getAantalTreinen();	//Is nodig voor het bepalen van de arraygrootte.
		xy_treinen = new int[aantalRailCabs][2];	//Deze array houdt de x en de y as van de treintjes bij.
		
		maakReizigers = new JButton("start");					//Deze knop start het genereren van de reizigers
		maakReizigers.addActionListener(new KnopHandler());
		maakReizigers.setBounds( 130, 640, 100, 20 );
		add(maakReizigers);
		
		setLayout(null);

		refresh = new RefreshGui(this);	//Een aparte thread die de GUI refreshed.
		refresh.start();
	}
	
	/**
	 * paintComponent is Swing's manier om te tekenen.
	 */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        treinbaan.paintIcon(this, g, 20, 20);
        
        while(!stations.getStationNaam().equalsIgnoreCase("station1")){
      		stations = stations.getNextStation();									//Zorgt ervoor dat het if statement naar het volgende station gaat	
      	}
      	if(stations.getStationNaam().equalsIgnoreCase("station1")){
      		if(stations.getVoorWisselStatus()==true){						//Dit zijn de lampjes voor de VOORWissel
      			g.setColor(Color.green);									//De kleur wordt op groen gezet (Als de Railcab afslaat richting het station)
      			g.fillOval(179, 55, 10, 10);
      		}
      		else{
      			g.setColor(Color.red);										//Anders op rood (Als de Railcab Rechtdoor gaat)
      			g.fillOval(179, 55, 10, 10);
      		}																//De coordinaten overschrijven elkaar, daarom dezelfde coordinaten bij rood en groen
      		if(stations.getAchterWisselStatus()==true){						//Dit zijn de lampjes voor de AchterWissel
      			g.setColor(Color.green);									
      			g.fillOval(340, 55, 10, 10);
      			g.setColor(Color.BLUE);
      		}
      		else{
      			g.setColor(Color.red);
      			g.fillOval(340, 55, 10, 10);
      		}
        }
      	
      	stations = stations.getNextStation();									//Zorgt ervoor dat het if statement naar het volgende station gaat
      	if(stations.getStationNaam().equalsIgnoreCase("station2")){
      		if(stations.getVoorWisselStatus()==true){
      			g.setColor(Color.green);
      			g.fillOval(470, 55, 10, 10);
      		}
      		else{
      			g.setColor(Color.red);
      			g.fillOval(470, 55, 10, 10);
      		}
      		if(stations.getAchterWisselStatus()==true){
      			g.setColor(Color.green);
      			g.fillOval(625, 55, 10, 10);
      		}
      		else{
      			g.setColor(Color.red);
      			g.fillOval(625, 55, 10, 10);
      		}
     	}
      	stations = stations.getNextStation();
      	if(stations.getStationNaam().equalsIgnoreCase("station3")){
      		if(stations.getVoorWisselStatus()==true){
      			g.setColor(Color.green);
      			g.fillOval(495, 225, 10, 10);
      		}
      		else{
      			g.setColor(Color.red);
      			g.fillOval(495, 225, 10, 10);
      		}
      		if(stations.getAchterWisselStatus()==true){
      			g.setColor(Color.green);
      			g.fillOval(334, 225, 10, 10);
      		}
      		else{
      			g.setColor(Color.red);
      			g.fillOval(334, 225, 10, 10);
      		}
      	}
      	stations = stations.getNextStation();
      	if(stations.getStationNaam().equalsIgnoreCase("station4")){
      		if(stations.getVoorWisselStatus()==true){
      			g.setColor(Color.green);
      			g.fillOval(334, 394, 10, 10);
      		}
      		else{
      			g.setColor(Color.red);
      			g.fillOval(334, 394, 10, 10);
      		}
      		if(stations.getAchterWisselStatus()==true){
      			g.setColor(Color.green);
      			g.fillOval(495, 394, 10, 10);
      		}
      		else{
      			g.setColor(Color.red);
      			g.fillOval(495, 394, 10, 10);
      		}
      	}
      	stations = stations.getNextStation();
      	if(stations.getStationNaam().equalsIgnoreCase("station5")){
      		if(stations.getVoorWisselStatus()==true){
      			g.setColor(Color.green);
      			g.fillOval(625, 572, 10, 10);
      		}
      		else{
      			g.setColor(Color.red);
      			g.fillOval(625, 568, 10, 10);
      		}
      		if(stations.getAchterWisselStatus()==true){
      			g.setColor(Color.green);
      			g.fillOval(468, 568, 10, 10);
      		}
      		else{
      			g.setColor(Color.red);
      			g.fillOval(468, 568, 10, 10);
      		}
      	}
      	stations = stations.getNextStation();
     	if(stations.getStationNaam().equalsIgnoreCase("station6")){
      		if(stations.getVoorWisselStatus()==true){
      			g.setColor(Color.green);
      			g.fillOval(340, 568, 10, 10);
      		}
      		else{
      			g.setColor(Color.red);
      			g.fillOval(340, 568, 10, 10);
      		}
      		if(stations.getAchterWisselStatus()==true){
      			g.setColor(Color.green);
      			g.fillOval(179, 568, 10, 10);
      		}
      		else{
      			g.setColor(Color.red);
      			g.fillOval(179, 568, 10, 10);
      		}
      	}
      	stations = stations.getNextStation();
      	if(stations.getStationNaam().equalsIgnoreCase("station7")){
      		if(stations.getVoorWisselStatus()==true){
      			g.setColor(Color.green);
      			g.fillOval(42, 501, 10, 10);
      		}
      		else{
      			g.setColor(Color.red);
      			g.fillOval(42, 501, 10, 10);
      		}
      		if(stations.getAchterWisselStatus()==true){
      			g.setColor(Color.green);
      			g.fillOval(42, 345, 10, 10);
      		}
      		else{
      			g.setColor(Color.red);
      			g.fillOval(42, 345, 10, 10);
      		}
      	}
      	stations = stations.getNextStation();
      	if(stations.getStationNaam().equalsIgnoreCase("station8")){
      		if(stations.getVoorWisselStatus()==true){
      			g.setColor(Color.green);
      			g.fillOval(42, 249, 10, 10);
      		}
      		else{
      			g.setColor(Color.red);
      			g.fillOval(42, 249, 10, 10);
      		}
      		if(stations.getAchterWisselStatus()==true){
      			g.setColor(Color.green);
      			g.fillOval(42, 91, 10, 10);
      		}
      		else{
      			g.setColor(Color.red);
      			g.fillOval(42, 91, 10, 10);
      		}
      	
      	}
      	
        teken(g);
    } 
    
    /**
     * In deze methode worden de treintje getekend.
     * @param g
     */
	public void teken( Graphics g ){
		trein = sim.getTreinen();	//

		long reistijd_totaal = 0;	// Van elke trein wordt de totale reistijd opgehaald.
        long reistijd_actueel = 0;	// Van elke trein wordt de actuele reistijd berekend.
        							// Aan de hand van de bovenstaande variabele wordt de positie bepaald.
        
    	int begin_tx = 0;			// Het x-punt van van waar de trein begon met rijden.
        int begin_ty = 0;			// Het y-punt van van waar de trein begon met rijden. 
        int eind_tx = 0;			// Het x-punt waar de trein heen moet.
        int eind_ty = 0;			// Het y-punt waar de trein heen moet.
        int node_x = 0;				// Bij de wissels is een extra punt zodat de animatie er wat mooier uit ziet.
        int node_y = 0;				// Bij de wissels is een extra punt zodat de animatie er wat mooier uit ziet.

        
    	for( int i = 0; i < trein.size() ; i++ ){

    		Trein t = trein.get(i);
    		boolean bijStation = false;

        	reistijd_totaal = t.getAankomstTijd()- t.getTijdVertrokken(); //bereken de totale reistijd
        	reistijd_actueel = reistijd_totaal - (t.getAankomstTijd() - datum.getActueleDatumInSec()); //bereken de actuele reistijd        	
            int stationnummer = 0;
    		boolean deur = false;
    		
    		//bereken de afstand
    		while(!stations.getStationNaam().equalsIgnoreCase("Station1")){
    			stations = stations.getNextStation();
    		}
    		
    		/*
    		 * Dmv deze while loop wordt bepaald op welk traject de trein rijdt. Een trein rijdt tussen twee punten(of drie als de trein naar het station moet).
    		 * Er wordt gekeken bij welke sensor een trein voor het laatst is geregistreerd. Daarna wordt bepaald waar de trein heen moet.
    		 */
    		do{
    			stationnummer = Integer.parseInt(stations.getStationNaam().substring(stations.getStationNaam().length()-1))-1; // bepaal het stationnummer.
    			for(int i1 = 0; i1 < stations.getVoorSensors().size(); i1++ ){
    				VoorSensor vs = stations.getVoorSensors().get( i1 );
    				// Als een trein op of voorbij een voorsensor is.
    				if(t.getTreinID() == vs.getSensor().getSensorWaarde() || t.getTreinID() == vs.getSensor().getAfgehandeld()){
    					deur = true;
    					begin_tx = xy_voorSens[stationnummer][i1][0];
						begin_ty = xy_voorSens[stationnummer][i1][1];
						// Als hij niet bij de laatste voorsensor is.	
    					if( i1 < stations.getVoorSensors().size()-1 ){ 
    						if( i1 + 1 <= stations.getVoorSensors().size() ){
        						eind_tx = xy_voorSens[stationnummer][i1+1][0];
        						eind_ty = xy_voorSens[stationnummer][i1+1][1];
    						}
    					}
    					// Als hij bij de laatste voorsensor is.
    					if( i1 == stations.getVoorSensors().size()-1 ){
    						begin_tx = xy_voorSens[stationnummer][i1][0];
    						begin_ty = xy_voorSens[stationnummer][i1][1];
    						// Als de trein naar het station moet.
    						if(t.gaNaarStation(stations.getStationNaam())){ 
    							bijStation = true;
    							node_x = xy_stations_node[stationnummer][0][0]; // Hij moet niet rechtstreeks naar het station gaan maar met een omweg.
    							node_y = xy_stations_node[stationnummer][0][1]; // Hij moet niet rechtstreeks naar het station gaan maar met een omweg.
    							
    							eind_tx = xy_stations[stationnummer][0];
    							eind_ty = xy_stations[stationnummer][1];
    						}else{ // ga naar een tussen sensor
    							eind_tx = xy_tussenSens[stationnummer][0];
    							eind_ty = xy_tussenSens[stationnummer][1];
    						}
    					}
    				}
    			}
    			// Als een trein op een stationsensor is of net van een station is weggereden.
    			if(t.getTreinID() == stations.getStationSensor() || t.getTreinID() == stations.getStationSensorAfgehandeld()){
    				deur = true;
    				bijStation = true;
    				begin_tx = xy_stations[stationnummer][0];
					begin_ty = xy_stations[stationnummer][1];
					node_x = xy_stations_node[stationnummer][1][0]; // Er zit een punt tussen het station en de laatste sensor. Dit is de x as.
					node_y = xy_stations_node[stationnummer][1][1]; // Er zit een punt tussen het station en de laatste sensor. Dit is de y as.
					// De baan is rond dus na het laatste station moet de eerste voorsensor van het eerste station worden geselecteerd.
					if(stationnummer + 1 <= 7){
						eind_tx = xy_voorSens[stationnummer+1][0][0];
						eind_ty = xy_voorSens[stationnummer+1][0][1];
					}
					else{ // Zorgt ervoor dat na het laatste station in de array het eerste station weer wordt gepakt.
						eind_tx = xy_voorSens[0][0][0];
						eind_ty = xy_voorSens[0][0][1];
					}
    			}
    			
				if(t.getTreinID() == stations.getTussenWisselSensorAfgehandeld() || t.getTreinID() == stations.getTussenWisselSensor()){
					deur = true;
    				begin_tx = xy_tussenSens[stationnummer][0];
					begin_ty = xy_tussenSens[stationnummer][1];
					if(stationnummer + 1 <= 7){
						eind_tx = xy_voorSens[stationnummer+1][0][0];
						eind_ty = xy_voorSens[stationnummer+1][0][1];
					}else{
						eind_tx = xy_voorSens[0][0][0];
						eind_ty = xy_voorSens[0][0][1];
					}
				}
				
    			if(deur) // als het station is gevonden mag hij uit de loop gaan.
    				break;
    			
    			stations = stations.getNextStation();
    		}while(!stations.getStationNaam().equalsIgnoreCase("Station1"));  
    		
	    	if(t.getIsRijden() && reistijd_actueel > 0 && reistijd_actueel < reistijd_totaal){
				double afstand_x  = 0; // de totale x afstand die de trein rijdt tussen twee sensoren
				double afstand_y  = 0; // de totale y afstand die de trein rijdt tussen twee sensoren
				double verplaatsing_x = 0; // de x afstand die de trein rijdt in een bepaalde tijd
				double verplaatsing_y = 0; // de y afstand die de trein rijdt in een bepaalde tijd
				
				// voor het geval afstand_x en afstand_y beiden 0 zijn. 
				xy_treinen[i][0] = begin_tx  ;
				xy_treinen[i][1] = begin_ty  ;

				/*
				 * Als een trein naar een station moet of van een station weggaat komt hij in het onderstaande statement  
				 */
				if(bijStation){ 
					 // Voor en na het station zit een bocht in de rails. Deze is opgebouwd uit twee afstanden met daartussen een knik.
					// stelling van pythagoras
					double afstand1 = Math.sqrt( (((node_x - begin_tx) )*((node_x - begin_tx) )) + (((node_y - begin_ty) )*((node_y - begin_ty) ))) ;
					// stelling van pythagoras
					double afstand2 = Math.sqrt( (((eind_tx - node_x) )*((eind_tx - node_x) )) + (((eind_ty - node_y) )*((eind_ty - node_y) )));
					double afstandTotaal = afstand1 + afstand2; //De totaal afstand.
					double reistijd; // de reistijd is nodig om te bepalen of de trein zich na of voor de knik in de bocht bevind

					if(afstand1 < 0)
						afstand1 = afstand1*-1; // maak een positief getal
					if(afstand2 < 0)
						afstand2 = afstand2*-1; // maak een positief getal
					if(afstandTotaal < 0)
						afstandTotaal = afstandTotaal*-1; // maak een positief getal

					if(afstandTotaal != 0 ){ // Delen door null mag niet als deze beveiliging er niet in zit krijg je fouten.
						reistijd = (reistijd_totaal / afstandTotaal) * afstand1; // Bereken de reistijd van de eerste afstand.
						
						/*
						 * Eerst wordt de afstand tussen twee punten berekend. Vervolgens de verplaatsing.
						 * als je de verplaatsing nu vermenigvuldigd met de actuele reistijd krijg je de actuele afstand.
						 * 
						 * Voor en na het station zit zit een hoek. eerst berekend het systeem het gedeelte voor de hoek en vervolgend het gedeelte na de hoek
						 */
						afstand_x = (node_x - begin_tx); 
						verplaatsing_x = afstand_x / reistijd;
						
						xy_treinen[i][0] = (int)( verplaatsing_x * reistijd_actueel ) + begin_tx;
						if( reistijd_actueel > reistijd ){
							double tmp = reistijd;
							reistijd = ( reistijd_totaal - reistijd );
							afstand_x = (eind_tx - node_x)  ;
							verplaatsing_x = afstand_x / reistijd;
							xy_treinen[i][0] = (int)( verplaatsing_x * (reistijd_actueel-tmp)) + node_x;
						}
					}

					//bepaal de y positie
					if( afstandTotaal != 0 ){
						reistijd = (reistijd_totaal / afstandTotaal) * afstand1;
						afstand_y = (node_y - begin_ty)  ;
						verplaatsing_y = afstand_y / reistijd;
						xy_treinen[i][1] = (int)( verplaatsing_y * reistijd_actueel ) + begin_ty;
							
						if( reistijd_actueel > reistijd ){
							double tmp = reistijd;
							reistijd = ( reistijd_totaal - reistijd );
							afstand_y = (eind_ty - node_y)  ;
							verplaatsing_y = afstand_y / reistijd;
							xy_treinen[i][1] = (int)( verplaatsing_y * (reistijd_actueel-tmp) ) + node_y;
						}
					}
				}else{ // als een trein niet naar een station moet maar naar een voorsensor
					afstand_x = eind_tx - begin_tx;
					afstand_y = eind_ty - begin_ty;
					
					//bepaal de x positie
					if(afstand_x != 0 ){
						verplaatsing_x = afstand_x / reistijd_totaal;
						xy_treinen[i][0] = (int)( verplaatsing_x * reistijd_actueel ) + begin_tx;
					}
					//bepaal de y positie
					if( afstand_y != 0 ){
						verplaatsing_y = afstand_y / reistijd_totaal;
							xy_treinen[i][1] = (int)( verplaatsing_y * reistijd_actueel ) + begin_ty;
					}
				}
	    	}
	    	
	    	// De kleur van de trein veranderd aan de hand van het aantal passengiers.
	    	if(t.getAantalZitplaatsen() == 0)
	    		g.setColor(Color.black);
	    	if(t.getAantalZitplaatsen() == 1)
	    		g.setColor(Color.green);
	    	if(t.getAantalZitplaatsen() > 1)
	    		g.setColor(Color.red);
	    	if(t.getAantalZitplaatsen() == t.getMaxZitPlaatsen())
	    		g.setColor(Color.blue);
	    	
	    	// bij de initializatie worden de treintjes op positie 0, 0 gezet. Dit hoeft hij niet te tekenen 
	    	if(xy_treinen[i][0] != 0 && xy_treinen[i][1] != 0){
	    		g.fillOval(xy_treinen[i][0] + begin_x - 5, xy_treinen[i][1] + begin_y - 5, 20, 20);
	    		g.setColor(Color.white);
	    		if(i>=9)
	    			g.drawString(""+t.getTreinID(), 2 + xy_treinen[i][0] + begin_x-4, 10+ begin_y + xy_treinen[i][1]);
	    		else
	    			g.drawString(""+t.getTreinID(), 2 + xy_treinen[i][0] + begin_x, 10+ begin_y + xy_treinen[i][1]);
	    	}
	    	
	    	// Het aantal personen in het systeem en het aantal vervoerde personen.
    		String time = "";
    		g.setColor(Color.black);
    		g.drawString("Genereer reizigers:", 20, 654 );
    		g.drawString("Aantal reizigers in het systeem: "+sim.getAantalPersonenInSysteem(), 250, 645 );
    		g.drawString("Totaal aantal vervoerde reizigers: "+sim.getTotaalAantalVervoerdePassengiers(), 250, 663 );
    		
    		
    		//De legenda 
    		g.setColor(Color.black);
    		g.fillRect( 560, 630, 20, 20 );
    		g.setColor(Color.green);
    		g.fillRect( 560, 652, 20, 20 );
    		g.setColor(Color.red);
    		g.fillRect( 560, 674, 20, 20 );
    		g.setColor(Color.blue);
    		g.fillRect( 560, 696, 20, 20 );
    		g.setColor(Color.black);
    		g.drawString("Lege Rail-Cab", 582, 647 );
    		g.drawString("…Èn reiziger", 582, 669 );
    		g.drawString("Meer dan ÈÈn reiziger", 582, 691 );
    		g.drawString("Volle Rail-Cab", 582, 713 );
    		
    		//Klokje
    		calendar.setTimeInMillis(datum.getActueleDatumInSec());
    		time = calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);
			g.drawString(time, 20, 15);
        }
	}
	
	class KnopHandler implements ActionListener {
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == getRoute){
			
			}
			if(e.getSource() == clearReizigers){
				sim.verwijderReizigers();
			}
			if(e.getSource() == maakReizigers){
				sim.maakRandomReizigers();
				if(sim.getMaakRandomReizigers())
					maakReizigers.setText("Stop");
				else
					maakReizigers.setText("Start");
				
			}
			
		}
	}
}