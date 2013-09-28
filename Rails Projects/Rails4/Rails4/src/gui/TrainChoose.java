package gui;

import controller.TrainController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 * this class decides the right amount of active trains on the track.  
 * @author RailCab07_4
 */
public class TrainChoose {
    private static JFrame chooseFrame   = null;
    private JTextField tField           = new JTextField();
    private JLabel label                = new JLabel();
    private ViewTrack v;
    /**
     * Constructor of this class.
     */
    public TrainChoose()
    {
        this.initFrame();
        TrainChoose.chooseFrame.setVisible (true);
        
        //Center frame
        Dimension screenSize    = Toolkit.getDefaultToolkit().getScreenSize();
        screenSize.height       = screenSize.height/2;
        screenSize.width        = screenSize.width/2;
        int height              = 250,
            width               = 500;
        int lokY                = screenSize.height - (height/2);
        int lokX                = screenSize.width - (width/2);
        TrainChoose.chooseFrame.setBounds (lokX, lokY, width, height);
    }
    /**
     * this method creates a frame where you can enter the amount of active trains on the track.  
     * @return - TrainChoose.chooseFrame 
     */
    private JFrame initFrame()
    {
        if (TrainChoose.chooseFrame != null)
        {
            return TrainChoose.chooseFrame;
        }
        
        JFrame frame        = new JFrame ("RailCab :: Creeer treinen");
        JPanel panel        = new JPanel();
        JButton button      = new JButton();
        
        // set label
        this.label.setText ("Hoeveel treinen moet dit systeem bevatten? (1 / " + TrainController.MAX_TRAINCOUNT + ")");
        this.label.setForeground (new Color (0x00008b));
        
        // set button
        button.setText ("Bevestig");
        button.addActionListener (new ActionListener() 
        {
            public void actionPerformed (ActionEvent evt) 
            {
                submit ();
            }
        });
        // add components
        Dimension frameSize = frame.getSize();
        panel.setSize (frameSize.width, frameSize.height);
        panel.setBackground (new Color (0x00008B));

        this.label.setBounds (20, 20, 500, 25);
        this.tField.setBounds (20, 45, 100, 25);
        button.setBounds (20, 100, 100, 25);
        
        frame.getContentPane().add (this.label);
        frame.getContentPane().add (this.tField);
        frame.getContentPane().add (button);
        
        frame.getContentPane().setLayout (null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground (new Color (0xf9f9f9));
        TrainChoose.chooseFrame = frame;
        return TrainChoose.chooseFrame;
    }
    /**
     * this method handles the actions after the submit-button is pressed.
     */
    protected void submit ()
    {
        // Collect the number of trains to be created
        String maxTrainCount = this.tField.getText();
        // Prepare the regexsome vars
        Pattern p = Pattern.compile ("([0-9]+)");
        Matcher m = p.matcher (maxTrainCount);
        // Check the input
        if (m.matches())
        {
            int count = new Integer (maxTrainCount);
            if (count < 1 || count > TrainController.MAX_TRAINCOUNT)
            {
                this.label.setForeground (new Color (0xff0000));
                this.label.setText ("Het aantal treinen moet liggen tussen de 0 en " + TrainController.MAX_TRAINCOUNT + "!");
                return;
            }
            v = new ViewTrack (count);
            if (ViewTrack.trackFrame != null)
            {
                this.initFrame().dispose();
                // Don't need this object any more.
                try {
                    this.finalize();
                }
                catch (Throwable ex)
                {
                    Logger.getLogger(TrainChoose.class.getName()).log (Level.SEVERE, null, ex);
                }
            }
            else
            {
                System.out.println ("D'r is iets fout gegaan :(");
                Runtime.getRuntime().exit (1);
            }
        }
        else
        {
            this.label.setForeground (new Color (0xff0000));
            this.label.setText ("Voer een geheel getal in!");
        }
    }
}
