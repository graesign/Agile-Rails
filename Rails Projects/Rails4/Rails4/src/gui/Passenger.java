package gui;

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
import java.awt.Font;

/**
 * This class initiates the passenger gui.
 * @author RailCab07_4
 */
public class Passenger implements ActionListener 
{
    private Algorithm algorithm             = Algorithm.getInstance();
    private static JPanel passengerPanel    = null;
    private JButton[] JButtonArray          = new JButton[30];
    private javax.swing.JComboBox jComboBox1,
                                  jComboBox2;
    private javax.swing.JTextArea jTextField1,
                                  jTextField2;
    int number                              = 0,
        number2                             = 0;
    public String CurrentLocation           = "AMSTERDAM";
    private TrackController trackCont       = TrackController.getInstance();
    public Character compare;
    private final MySQL db    = new MySQL();
    
    protected JPanel getPanel()
    {
        if (Passenger.passengerPanel != null)
        {
            return Passenger.passengerPanel;
        }
        
        // Set items
        JPanel panel    = new JPanel();
        panel.setLayout (null);
        JLabel head     = new JLabel();
        jTextField1     = new javax.swing.JTextArea();
        jTextField2     = new javax.swing.JTextArea();
        jComboBox1      = new javax.swing.JComboBox();
        jComboBox2      = new javax.swing.JComboBox();
        jTextField1.setEnabled(false);
        JLabel Labbel   = new JLabel();
        JLabel label1   = new JLabel();
        
        this.trackCont.stations = this.db.fetchStations();
        
        int i = 0;
        for (i = 0; i < 28; i++)
        {
            JButtonArray[i] = new javax.swing.JButton();
        }

        i = 0;
        for (char c = 'a'; c <= 'z'; c++)
        {
            JButtonArray[i].setText (String.valueOf (c));
            i++;
        }
        JButtonArray[26].setText ("Reset");
        JButtonArray[27].setText ("Verzenden");
        this.deactivateButtons();
        
        // Header
        head.setOpaque (true);
        head.setBackground (new Color (0x00008b));
        head.setForeground (new Color (0xffffff));
        head.setBounds (0, 0, 520, 25);
        head.setText ("    Passagier UI");
        head.setBorder (BorderFactory.createLineBorder (new Color (0xffffff)));
        head.setAlignmentX (JLabel.CENTER_ALIGNMENT);
        head.setVisible (true);
        panel.add (head);
        
        Labbel.setText ("U bent in:    " + CurrentLocation);
        Labbel.setFont (new Font ("Serif", Font.BOLD, 24));
        Labbel.setVisible (true);
        Labbel.setBounds (145, 100, 400, 50);
        panel.add (Labbel);
        
        label1.setText ("Aantal personen:  ");
        label1.setFont (new Font("Serif", Font.BOLD, 24));
        label1.setVisible (true);
        label1.setBounds (50, 200, 400, 50);
        panel.add (label1);

        jComboBox1.setVisible (true);
        jComboBox2.setVisible (true);
        jTextField1.setVisible (true);
        jTextField1.setEnabled (false);
        jTextField1.setDisabledTextColor (new Color (0x000000));
        panel.add (jComboBox1);
        panel.add (jComboBox2);
        panel.add (jTextField1);
        
        for (i = 0; i < 28; i++)
        {
            JButtonArray[i].setBackground (Color.WHITE);
            JButtonArray[i].setActionCommand (JButtonArray[i].getText());
            JButtonArray[i].addActionListener (this);
            JButtonArray[i].setCursor (new Cursor (Cursor.HAND_CURSOR));
            panel.add (JButtonArray[i]);
        }
        
        
        for (i = 0; i<this.trackCont.stations.length; i++)
        {
            jComboBox1.addItem (this.trackCont.stations[i]);
        }
        
        for (i = 1; i <= 10; i++)
        {
            jComboBox2.addItem(i);
        }
        
        for (i = 0; i<8; i++)
        {
            JButtonArray[i].setBounds(40 + (i*55),300,50,20);
            JButtonArray[i+8].setBounds(40 + (i*55),325,50,20);
            JButtonArray[i+16].setBounds(40 + (i*55),350,50,20);
        }
        
        jTextField1.setBounds (50, 50, 200, 50);
        jComboBox1.setBounds (260, 50, 200, 50);
        jComboBox2.setBounds (250, 200, 200, 50);
        jComboBox1.setCursor (new Cursor (Cursor.HAND_CURSOR));
        jComboBox2.setCursor (new Cursor (Cursor.HAND_CURSOR));
        JButtonArray[24].setBounds (205, 375, 50, 20);
        JButtonArray[25].setBounds (260, 375, 50, 20);
        JButtonArray[26].setBounds (155,400,100,20);
        JButtonArray[27].setBounds (260,400,100,20);
        
        return panel;
    }
    
    private void deactivateButtons()
    {
        int i;
        boolean[] stationChecked    = new boolean[26];
        java.util.Arrays.fill (stationChecked, false);
        Character[] title           = new Character[this.trackCont.stations.length];

        for (i = 0; i < this.trackCont.stations.length; i++)
        {
            title[i] = java.lang.Character.toLowerCase (this.trackCont.stations[i].charAt (number2));
        }

        for (i = 0; i < this.trackCont.stationCount; i++)
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
                    this.algorithm.orderTrain (0, (jComboBox1.getSelectedIndex()), (jComboBox2.getSelectedIndex() + 1));
                    System.out.println ("van:  " +
                                        1 +
                                        "   Naar:   " +
                                        (jComboBox1.getSelectedIndex() + 1) +
                                        "   Aantal personen:     " +
                                        (jComboBox2.getSelectedIndex() + 1));
                    jTextField1.setText ("U kunt het kaartje uit het\nbakje pakken prettige reis verder.");
                }
            }
        }
    }
}
