package PassengerConsole;

import helpers.PrintTicket;
import algorithm.Algorithm;
import controller.TrackController;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import helpers.Time;
import database.MySQL;
import helpers.Array;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * This class initiates the passenger gui.
 * @author RailCab07_4
 */
public class PassengerConsole implements ActionListener 
{
    private Algorithm algoritm                          = Algorithm.getInstance();
    private JButton[]               JButtonArray        = new JButton[30];
    private javax.swing.JComboBox   jComboBox1,
                                    jComboBox2;
    private javax.swing.JTextArea   jTextField1;
    private JFrame                  frame1              = new JFrame();
    int                             number              = 0,
                                    number2             = 0;
    public String                   CurrentLocation     = "AMSTERDAM";
    private TrackController         trackCont           = TrackController.getInstance();
    private final MySQL   db                            = new MySQL();
    private Dimension               screenSize          = Toolkit.getDefaultToolkit().getScreenSize();
    private static final String     logoPicLoc          = "c:/logo_movares.jpg";
    //private static final String     logoPicLoc          = "/home/erik/rcab/logo_movares.jpg";
    

    
    private PassengerConsole(){      
        // Prepare full screen
        GraphicsEnvironment env     = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices    = env.getScreenDevices();
        GraphicsDevice device       = devices[0];
        boolean allowFullScreen     = device.isFullScreenSupported();
        
        
        
        // Frame settings
        frame1.setUndecorated (allowFullScreen);
        frame1.setResizable (!allowFullScreen);
        frame1.setCursor (new Cursor (Cursor.CROSSHAIR_CURSOR));
        frame1.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame1.setBackground(Color.WHITE);
        

        
        
        // Set full screen
        if (allowFullScreen)
        {
            this.frame1.setContentPane(this.getPanel());
            device.setFullScreenWindow (frame1);
            frame1.validate();
        }
        // Make visible when not allowed to go to fullscreen.
        else
        {
            this.frame1.setContentPane(this.getPanel());
            frame1.setSize (screenSize);
            frame1.setVisible (true);
            frame1.validate();
        }
        
    }
    protected JPanel getPanel()
    {
        // Set items
        JPanel panel    = new JPanel(null);
        panel.setBackground(Color.WHITE);

        // Header
        JLabel head     = new JLabel();
        head.setOpaque (true);
        head.setBackground (new Color (0x00008b));
        head.setForeground (new Color (0xffffff));
        head.setBounds (0, 0, 1280, 25);
        head.setText ("    Passagier UI");
        head.setBorder (BorderFactory.createLineBorder (new Color (0xffffff)));
        head.setAlignmentX (JLabel.CENTER_ALIGNMENT);
        head.setVisible (true);
        panel.add (head);
        
        //TextField 1
        jTextField1     = new javax.swing.JTextArea();
        jTextField1.setEnabled(false);
        jTextField1.setVisible (true);
        jTextField1.setEnabled (false);
        jTextField1.setDisabledTextColor (new Color (0x000000));
        jTextField1.setBounds (310, 250, 200, 50);
        panel.add (jTextField1);
        
        //jComboBox 1
        //fetch the stations to put on the drop down menu
        this.trackCont.stations = this.db.fetchStations();
        jComboBox1      = new javax.swing.JComboBox();
        jComboBox1.setVisible (true);
        panel.add (jComboBox1);
        for (int i = 0; i<this.trackCont.stations.length; i++)
        {
            jComboBox1.addItem (this.trackCont.stations[i]);
        }
        
        //jComboBox 2
        jComboBox2      = new javax.swing.JComboBox();
        jComboBox2.setVisible (true);      
        panel.add (jComboBox2);
        for (int i = 1; i <= 10; i++)
        {
            jComboBox2.addItem(i);
        }
        
        //label
        JLabel label    = new JLabel();
        label.setText ("U bent in:    " + CurrentLocation);
        label.setFont (new Font ("Serif", Font.BOLD, 24));
        label.setVisible (true);
        label.setBounds (500, 300, 400, 50);
        panel.add (label);

        //label1
        JLabel label1   = new JLabel();
        label1.setText ("Aantal personen:  ");
        label1.setFont (new Font("Serif", Font.BOLD, 24));
        label1.setVisible (true);
        label1.setBounds (310, 350, 400, 50);
        panel.add (label1);
        
        //28 new JButtons for the letters a > z
        for (int i = 0; i < 28; i++)
        {
            JButtonArray[i] = new javax.swing.JButton();
        }
        int i = 0;
        for (char c = 'a'; c <= 'z'; c++)
        {
            JButtonArray[i].setText (String.valueOf (c));
            i++;
        }
        JButtonArray[26].setText ("Reset");
        JButtonArray[27].setText ("Verzenden");
      
        for (i = 0; i < 28; i++)
        {
            JButtonArray[i].setBackground (Color.WHITE);
            JButtonArray[i].setActionCommand (JButtonArray[i].getText());
            JButtonArray[i].addActionListener (this);
            JButtonArray[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
            panel.add (JButtonArray[i]);
        }
              
        for (i = 0; i<8; i++)
        {
            JButtonArray[i].setBounds(400 + (i*55),612,50,20);
            JButtonArray[i+8].setBounds(400 + (i*55),637,50,20);
            JButtonArray[i+16].setBounds(400 + (i*55),662,50,20);
        }

        jComboBox1.setBounds (700, 250, 200, 50);
        jComboBox2.setBounds (700, 350, 200, 50);
        jComboBox1.setCursor (new Cursor (Cursor.HAND_CURSOR));
        jComboBox2.setCursor (new Cursor (Cursor.HAND_CURSOR));
        JButtonArray[24].setBounds (565, 687, 50, 20);
        JButtonArray[25].setBounds (620, 687, 50, 20);
        JButtonArray[26].setBounds (515,712,100,20);
        JButtonArray[27].setBounds (620,712,100,20);
        
        panel.add (this.getLogoLabel ());
        panel.setVisible (true);
        panel.setBounds (0, 0, 650, 400);
        panel.validate();
        
        this.deactivateButtons();
        return panel;
    }
    
    private void deactivateButtons()
    {
        boolean[] stationChecked    = new boolean[26];
        java.util.Arrays.fill (stationChecked, false);
        Character[] title           = new Character[this.trackCont.stations.length];

        for (int i = 0; i < this.trackCont.stations.length; i++)
        {
            title[i] = java.lang.Character.toLowerCase (this.trackCont.stations[i].charAt (number2));
        }
        char compare;

        for (int i = 0; i < this.trackCont.stationCount; i++)
        {
            for (int j = 0; j < 26; j++)
            {
                compare = java.lang.Character.toLowerCase (this.JButtonArray[j].getText().charAt (0));
                if (Array.in_array(compare, title))
                {
                    this.JButtonArray[j].setEnabled (true);
                    stationChecked[j] = true;
                }
                else if (stationChecked[j] == false)
                {
                    this.JButtonArray[j].setEnabled (false);
                }
            }
        }
        this.number2++;
    }
    
    private void checkTextField()     
    {
        jComboBox1.removeAllItems();
        String temp     = jTextField1.getText();
        char compare    = java.lang.Character.toLowerCase (temp.charAt (number));

        for(int i=0; i<this.trackCont.stations.length; i++)
        {
            if(compare == java.lang.Character.toLowerCase (this.trackCont.stations[i].charAt(number)))
            {
                jComboBox1.addItem(this.trackCont.stations[i]);
            }
            else
            {
                this.trackCont.stations[i] = "              ";
            }
        }
        number++;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().length() == 1)
        {
            char letter = e.getActionCommand().charAt (0);
            jTextField1.setText (jTextField1.getText() + letter);
            checkTextField();
            deactivateButtons();
        }
        else
        {
            if (e.getSource() == JButtonArray[26])
            {
                this.number2 = 0;
                this.number  = 0;
                int i = 0;
                jTextField1.setText("");
                jComboBox1.removeAllItems();

                this.trackCont.stations = db.fetchStations();
                
                for (i = 0; i < this.trackCont.stations.length; i++)
                {
                    jComboBox1.addItem(this.trackCont.stations[i]);
                }
                
                for (i = 0; i < 26; i++)
                {   
                    JButtonArray[i].setEnabled(true);
                }

                this.deactivateButtons();
            }
            else if (e.getSource() == JButtonArray[27])
            {
                if (jComboBox1.getSelectedItem() == null)
                {
                    jTextField1.setText("Geen bestaande locatie druk reset.");}
                else
                {
                    Time time           = new Time();
                    PrintTicket pt      = new PrintTicket();
                    String tempString   = jComboBox1.getSelectedItem().toString();
                    jTextField1.setText ("Uw reis bestemming is " +
                                          tempString +
                                          ".\nUw kaartje word gedrukt even\ngeduld A.U.B.\n");
                    pt.drawTicket (CurrentLocation, tempString, time.getTimeHoursSeconds());
                    //this.algorithm.orderTrain (0, (jComboBox1.getSelectedIndex()), (jComboBox2.getSelectedIndex() + 1));
                    this.algoritm.orderTrain(0, (jComboBox1.getSelectedIndex()), (jComboBox2.getSelectedIndex() + 1));
                    System.out.println ("van:  " +
                                        1 +
                                        "   Naar:   " +
                                        (jComboBox1.getSelectedIndex() + 1) +
                                        "   Aantal personen:     " +
                                        (jComboBox2.getSelectedIndex() + 1));
                    jTextField1.setText ("U kunt het kaartje uit het\nbakje pakken prettige reis verder.");

                    // Sleep it to prefend overloading.
                    try
                    {
                        Thread.sleep(1500);
                    }
                    catch (InterruptedException ie)
                    {
                        ie.printStackTrace();
                    }
                }
            }
        }
    }
    
    private JLabel getLogoLabel()
    {
        BufferedImage img = null;
        
        // Check for the image
        File imgFile = new File (logoPicLoc);
        if (imgFile.canRead())
        {
            try
            {
                img = ImageIO.read (imgFile);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        

        Icon logoIcon  = new ImageIcon (img);
        JLabel label    = new JLabel (logoIcon);
        label.setBounds (310, 25, logoIcon.getIconWidth(), logoIcon.getIconHeight());

        return label;
    }
    
    public static void main(String args[]){
        new PassengerConsole();
    }
}
