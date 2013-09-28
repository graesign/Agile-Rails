package gui; 

/**
 * Write a description of class Layer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import business.Datum;
import simulatie.*;

public class Frame extends JFrame
{
    private static final long serialVersionUID = 1L;
    private GUI gui;
	private MenuItem menuFileConfig,menuFileExit;
	private Menu menuFile;
	private MenuBar menuBar;
	OptionScherm os;

    public Frame(Simulatie sim) {
    	//Menubar setup
		menuBar = new MenuBar();
        menuFile = new Menu();
        menuFileConfig= new MenuItem();
        menuFileExit = new MenuItem();
        
        menuFile.setLabel("Menu");
        menuFileExit.setLabel("Exit");
        menuFileConfig.setLabel("Configureer");

        menuFileExit.addActionListener(new FileMenuHandler());
        menuFileConfig.addActionListener(new FileMenuHandler());

        this.addWindowListener
        (
        	new WindowAdapter(){
			    public void windowClosing(WindowEvent e){
					Frame.this.windowClosed();
				}
        	}
        );

        menuBar.add(menuFile);
        menuFile.add(menuFileConfig);
        menuFile.add(menuFileExit);
        setMenuBar(menuBar);
        
    	gui = new GUI(sim); // Er wordt een panneel gemaakt met daarin de grafishe interface en de statistieken.
        setContentPane(gui); // maak paneel
        
        os = new OptionScherm(sim);
        os.setBounds(50,50,400,200);

    }

	//private class for menufile items
    class FileMenuHandler implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		// Menufile exit
    		if(e.getSource() == menuFileExit){
    			Frame.this.windowClosed();
    		}
    		// Menufile add
    		if(e.getSource() == menuFileConfig){
    	        os.setVisible(true);
    		}

    	}
    }
	
    protected void windowClosed(){
        // Exit application.
    	System.exit(0); 
    }
}

/**
 * Er wordt een aparte JFrame aangemaakt voor het optie scherm. 
 */
class OptionScherm extends JFrame {
	private static final long serialVersionUID = 1L;//Deze wordt aangeraden door java.
	JPanel p;
	JTextField minimaleWachtTijdField, maakReizigerField;
	JButton ok, stop;
	JLabel versnellingsfactor,  minimaleWachtTijd, maakReiziger;
	JRadioButton radio1, radio2, radio3, radio4, radio5, radio6;
	Simulatie sim;
	int aantalPersonenPerMin = 0;
	
	public OptionScherm(Simulatie sim){
		this.sim = sim;
		p = new JPanel();
		setContentPane(p);
		setLayout(null);
		p.setBackground(Color.YELLOW);
		
		//Er worden 6 radio buttons in het optiescherm geplaatst
		radio1 = new JRadioButton("1", false);
		radio1.setBackground(Color.YELLOW);
		radio1.setBounds(120,6,35,20);
		radio1.addActionListener(new Radio1Handler());
		radio2 = new JRadioButton("2", false);
		radio2.setBackground(Color.YELLOW);
		radio2.setBounds(155,6,35,20);
		radio2.addActionListener(new Radio2Handler());
		radio3 = new JRadioButton("3", false);
		radio3.setBackground(Color.YELLOW);
		radio3.setBounds(190,6,35,20);
		radio3.addActionListener(new Radio3Handler());
		radio4 = new JRadioButton("4", false);
		radio4.setBackground(Color.YELLOW);
		radio4.setBounds(225,6,35,20);
		radio4.addActionListener(new Radio4Handler());
		radio5 = new JRadioButton("5", false);
		radio5.setBackground(Color.YELLOW);
		radio5.setBounds(260,6,35,20);
		radio5.addActionListener(new Radio5Handler());
		radio6 = new JRadioButton("6", true);
		radio6.setBackground(Color.YELLOW);
		radio6.setBounds(295,6,35,20);
		radio6.addActionListener(new Radio6Handler());
		
		//Hier wordt een label en een invoerveld gemaakt voor het aantal passengier dat per min wordt gegenereerd
		maakReiziger = new JLabel("Aantal reiziger per min. : ");
		maakReiziger.setBounds(5, 30, 140, 20);
		versnellingsfactor = new JLabel("Versnellingsfactor : ");
		versnellingsfactor.setBounds(5,5,120,20);
		aantalPersonenPerMin = sim.getAantalPersonenPerMinuut();
		maakReizigerField = new JTextField();
		maakReizigerField.setText(""+aantalPersonenPerMin);
		maakReizigerField.setBounds(145, 30, 70, 20);

		ok = new JButton("Ok");
		ok.setBounds(290,90,52,26);
		ok.addActionListener(new OkHandler());

		p.add(maakReiziger);
		p.add(versnellingsfactor);
		p.add(radio1);
		p.add(radio2);
		p.add(radio3);
		p.add(radio4);
		p.add(radio5);
		p.add(radio6);
		p.add(maakReizigerField);
		p.add(ok);		
		}
	
		/**
		 * Hieronder worden de handlers gemaakt voor de knoppen 
		 */
	   class OkHandler implements ActionListener{
	    	public void actionPerformed(ActionEvent e){	    			
	    		Datum datum;
	    		datum = new Datum();	    		
	    		
	    		if(!maakReizigerField.getText().equals("")){
	    			sim.setAantalPersonenPerMinuut(Integer.parseInt(maakReizigerField.getText()));
	    		}
	    		
	    		if(radio1.isSelected()){
	    			datum.setVersnellingsfactor(1);
	    		}
	    		else if(radio2.isSelected()){
	    			datum.setVersnellingsfactor(2);
	    		}
	    		else if(radio3.isSelected()){
	    			datum.setVersnellingsfactor(3);
	    		}
	    		else if(radio4.isSelected()){
	    			datum.setVersnellingsfactor(4);
	    		}
	    		else if(radio5.isSelected()){
	    			datum.setVersnellingsfactor(5);
	    		}
	    		else if(radio6.isSelected()){
	    			datum.setVersnellingsfactor(6);
	    		}
	    		
	    		setVisible(false);
	    	}
	    	
	    }
	   
	   	class Radio1Handler implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				if(radio1.isSelected()){
					radio2.setSelected(false);
					radio3.setSelected(false);
					radio4.setSelected(false);
					radio5.setSelected(false);
					radio6.setSelected(false);
				}				
			}
	   		
	   	}
	   	
	   	class Radio2Handler implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				if(radio2.isSelected()){
					radio1.setSelected(false);
					radio3.setSelected(false);
					radio4.setSelected(false);
					radio5.setSelected(false);
					radio6.setSelected(false);
				}				
			}
	   		
	   	}
	   	
	   	class Radio3Handler implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				if(radio3.isSelected()){
					radio1.setSelected(false);
					radio2.setSelected(false);
					radio4.setSelected(false);
					radio5.setSelected(false);
					radio6.setSelected(false);
				}				
			}
	   		
	   	}
	   	
	   	class Radio4Handler implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				if(radio4.isSelected()){
					radio1.setSelected(false);
					radio2.setSelected(false);
					radio3.setSelected(false);
					radio5.setSelected(false);
					radio6.setSelected(false);
				}				
			}
	   		
	   	}
	   	
	   	class Radio5Handler implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				if(radio5.isSelected()){
					radio1.setSelected(false);
					radio2.setSelected(false);
					radio3.setSelected(false);
					radio4.setSelected(false);
					radio6.setSelected(false);
				}				
			}
	   		
	   	}
	   	
	   	class Radio6Handler implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				if(radio6.isSelected()){
					radio1.setSelected(false);
					radio2.setSelected(false);
					radio3.setSelected(false);
					radio4.setSelected(false);
					radio5.setSelected(false);
				}				
			}
	   		
	   	}

}
