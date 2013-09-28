package gui;

import controller.SimulationController;
import controller.TrackController;
import controller.TrainController;
import database.MySQL;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ViewTrack {
    public static JFrame trackFrame         = null;
    private static final String trackPicLoc = "c:/Track.png";
    //private static final String trackPicLoc = "c:/Track.png";
    private boolean trainSet                = false;
    private BufferedImage off_Image         = new BufferedImage (800, 600, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g                            = off_Image.createGraphics();
    TrackController trackCont               = TrackController.getInstance();
    final TrainController trainCont         = TrainController.getInstance();
    private Dimension screenSize            = Toolkit.getDefaultToolkit().getScreenSize();
    
    JLabel idLabel  = new JLabel(),
           idLabelT = new JLabel(),
           mPLabel  = new JLabel(),
           mPLabelT = new JLabel(),
           pLabel   = new JLabel(),
           pLabelT  = new JLabel(),
           oLabel   = new JLabel(),
           oLabelT  = new JLabel(),
           aLabel   = new JLabel(),
           aLabelT  = new JLabel();
    
    JButton startSimButton  = new JButton ("Start simulation"),
            stopSimButton   = new JButton ("Stop simulatie");
    
    private MySQL db = null;
    
    /**
     * Var has a value to prefend memory heaps when running the passenger console.
     */
    public static int simSpeed = 500;
    
    public ViewTrack(int trains)
    {
        // Set up a connection to the mysql database
        this.db = new MySQL();
        // this.db.prepareDatabase();
        // Init the track
        this.trackCont.createTrack();
        
        this.initFrame (trains);
        
        // Prepare full screen
        GraphicsEnvironment env     = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices    = env.getScreenDevices();
        GraphicsDevice device       = devices[0];
        boolean allowFullScreen     = device.isFullScreenSupported();
        
        // Frame settings
        ViewTrack.trackFrame.setUndecorated (allowFullScreen);
        ViewTrack.trackFrame.setResizable (!allowFullScreen);
        ViewTrack.trackFrame.setCursor (new Cursor (Cursor.CROSSHAIR_CURSOR));
        ViewTrack.trackFrame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

        // Set full screen
        if (allowFullScreen)
        {
            device.setFullScreenWindow (ViewTrack.trackFrame);
            ViewTrack.trackFrame.validate();
        }
        // Make visible when not allowed to go to fullscreen.
        else
        {
            ViewTrack.trackFrame.setSize (screenSize);
            ViewTrack.trackFrame.setVisible (true);
            ViewTrack.trackFrame.validate();
        }
    }

    private JFrame initFrame(int trains)
    {
        if (ViewTrack.trackFrame != null)
        {
            return ViewTrack.trackFrame;
        }
        
        JFrame frame = new JFrame ("RailCab :: Simulatie");
        
        // Create the panels
        JPanel mainPanel        = new JPanel(),
               trackPanel       = new JPanel(),
               legPanel         = new JPanel(),
               contrPanel       = new JPanel();
        final JSlider slider    = new JSlider();

        // Add the track && trains
        trackPanel.setLayout (null);
        
        // Set namelabels at the stations
        JLabel[] stationLables = new JLabel[8];
                 
        slider.setBounds (300, 380, 200, 20);
        slider.setVisible( true);
        trackPanel.add (slider);
        
        for (int i = 0; i < 8; i++)
        {
            boolean onTop = (i < 4) ? true : false;
            
            stationLables[i] = new JLabel (this.trackCont.stations[i]);
            
            if (onTop)
            {
                stationLables[i].setBounds((116 + (i * 102)), 105, 74, 20);
                stationLables[i].setVisible (true);
                trackPanel.add (stationLables[i]);
            }
            else
            {
                stationLables[i].setBounds((422 - ((i - 4) * 102)), 325, 74, 20);
                stationLables[i].setVisible (true);
                trackPanel.add (stationLables[i]);
            }
        }

        for (int i = 1; i <= trains; i++)
        {
            trackPanel.add (this.getTrainLabel (i));
        }
        trackPanel.add (this.getTrackLabel ());
        trackPanel.setVisible (true);
        int left = ((this.screenSize.width / 2) - (650 / 2));
        trackPanel.setBounds (left, 0, 650, 400);
        trackPanel.validate();
        
        // Create the legend
        legPanel.setLayout (null);
        for (int i = 1; i <= trains; i++)
        {
            legPanel.add (this.getTrainLegend (i));
            legPanel.add (this.startButton (i));
        }
        
        // Create the start simulation button
        this.startSimButton.setOpaque (true);
        this.startSimButton.setBackground (new Color(0x000000));
        this.startSimButton.setForeground (new Color(0xffffff));
        this.startSimButton.setCursor (new Cursor (Cursor.HAND_CURSOR));
        this.startSimButton.setBounds (5, (5 + ((trains + 1) * 25)), 140, 23);
        this.startSimButton.setEnabled (false);
        this.startSimButton.setFocusable (false);
        this.startSimButton.setVisible (true);
        this.startSimButton.addMouseListener (new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                startSim();
            }
        });
        slider.setValue (200);
        slider.addChangeListener (new ChangeListener() {
            public void stateChanged (ChangeEvent e) {
                int numberOfTrains = trainCont.trains.size();
                for (int i = 1; i <= numberOfTrains; i++){
                    trainCont.setTrainData (TrainController.trainName + i, -1, slider.getValue(), -1, false, false, -1, null);
                }
                simSpeed = slider.getValue();
            }
        });

        legPanel.add (this.startSimButton);
        slider.setMinimum (20);
        slider.setMaximum (500);
        slider.setCursor (new Cursor (Cursor.HAND_CURSOR));
        simSpeed = slider.getValue();
        legPanel.setLayout (null);
        legPanel.setVisible (true);
        left = ((this.screenSize.width / 2) - (650 / 2));
        legPanel.setBounds (left, 450, 150, 500);
        legPanel.validate ();
        
        // Create the controle panel
        this.idLabelT.setText ("Trein id:");
        this.mPLabelT.setText ("Maximale capaciteit:");
        this.pLabelT.setText ("Aantal passagiers:");
        this.oLabelT.setText ("Trein order id:");
        this.aLabelT.setText ("Trein actief?");
        
        this.idLabel.setBounds (130, 5, 125, 25);
        this.idLabelT.setBounds (5, 5, 125, 25);
        this.mPLabel.setBounds (130, 30, 125, 25);
        this.mPLabelT.setBounds (5, 30, 125, 25);
        this.pLabel.setBounds (130, 55, 125, 25);
        this.pLabelT.setBounds (5, 55, 125, 25);
        this.oLabel.setBounds (130, 80, 125, 25);
        this.oLabelT.setBounds (5, 80, 125, 25);
        this.aLabel.setBounds (130, 105, 125, 25);
        this.aLabelT.setBounds (5, 105, 125, 25);
        
        this.idLabelT.setVisible (true);
        this.mPLabelT.setVisible (true);
        this.pLabelT.setVisible (true);
        this.oLabelT.setVisible (true);
        this.aLabelT.setVisible (true);
        
        contrPanel.add (this.idLabel);
        contrPanel.add (this.idLabelT);
        contrPanel.add (this.mPLabel);
        contrPanel.add (this.mPLabelT);
        contrPanel.add (this.pLabel);
        contrPanel.add (this.pLabelT);
        contrPanel.add (this.oLabel);
        contrPanel.add (this.oLabelT);
        contrPanel.add (this.aLabel);
        contrPanel.add (this.aLabelT);
        
        contrPanel.setLayout (null);
        contrPanel.add (this.getContrPanel());
        contrPanel.setVisible (true);
        left = ((this.screenSize.width / 2) - (650 / 2));
        contrPanel.setBounds (left + 250, 450, 250, 600);
        contrPanel.validate();
        
        // Add the panels
        mainPanel.add (contrPanel);
        mainPanel.add (trackPanel);
        mainPanel.add (legPanel);
        frame.setContentPane (mainPanel);
        
        frame.getContentPane().setLayout (null);
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        
        ViewTrack.trackFrame = frame;
        return ViewTrack.trackFrame;
    }
    

    private JLabel getTrackLabel()
    {
        BufferedImage img = null;
        
        // Check for the image
        File imgFile = new File (trackPicLoc);
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
        else
        {
            this.initTrack();
            try
            {
                img = ImageIO.read (imgFile);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        Icon trackIcon  = new ImageIcon (img);
        JLabel label    = new JLabel (trackIcon);
        label.setBounds (0, 0, 800, 600);

        return label;
    }
    
    private void initTrack()
    {
        // Create track array
        this.trackCont.createTrack();
        
        // Transform the track arrayLists in something usable for this method
        int[] x     = new int[trackCont.xTrackCoords.length],
              y     = new int[trackCont.yTrackCoords.length];
        int[][] xS  = new int[trackCont.stationCount][16],
                yS  = new int[trackCont.stationCount][16];
        
        // X coords
        int i = 0;
        for (Double pos : trackCont.xTrackCoords)
        {
            x[i] = pos.intValue();
            i++;
        }
        
        // Y coords
        i = 0;
        for (Double pos : trackCont.yTrackCoords)
        {
            y[i] = pos.intValue();
            i++;
        }
        
        // Stations
        Double[] xStation,
                 yStation;
        
        i = 0;
        int j = 0;
        for (; i < trackCont.stationCount; i++)
        {
            xStation = trackCont.getStationXCoords(i);
            yStation = trackCont.getStationYCoords(i);

            for (int k = 0; k < xStation.length; k++)
            {
                // null
                if (xStation[k] == null)
                {
                    break;
                }

                xS[i][k] = xStation[k].intValue();
            }
            
            for (int k = 0; k < yStation.length; k++)
            {
                // null
                if (yStation[k] == null)
                {
                    break;
                }
                
                yS[i][k] = yStation[k].intValue();
            }
        }
        
        // Prepare the track field
        g.setColor (new Color (0xeeeeee));
        g.fillRect (0, 0, 800, 325);
        g.setColor (new Color (0xeeeeee));
        g.setStroke (new BasicStroke (5));
        g.fillPolygon (x, y, x.length);
        g.setColor (Color.BLACK);

        // Draw the stations
        i = 0;
        for (; i < xS.length; i++)
        {
            Color c = ((i == 0) ? new Color (0xff0000) : new Color (0x000000));
            g.setColor(c);
            g.drawPolygon (xS[i], yS[i], xS[i].length);
        }

        // Draw the track
        g.drawPolygon (x, y, x.length);
        
        // Create the image
        g.drawImage (this.off_Image, 0, 0, null);
        
        // Save the image
        try {
            File outputfile = new File (trackPicLoc);
            ImageIO.write (off_Image, "png", outputfile);
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    private final void showTrainDetails(int trainNr)
    {
        // Fetch the train
        HashMap<String, Object> trainData = this.trainCont.getTrainData (TrainController.trainName + trainNr);
        
        // Set it
        this.idLabel.setText (trainData.get ("TrainId").toString());
        this.mPLabel.setText (trainData.get ("TrainMaxPassengers").toString());
        this.pLabel.setText (trainData.get ("TrainPassengers").toString());
//        this.oLabel.setText ((trainData.get ("TrainOrderId").toString() != null) ? trainData.get ("TrainOrderId").toString() : "0");
        this.aLabel.setText (trainData.get ("TrainUsed").toString());

        this.idLabel.validate();
    }
    
    private JLabel getTrainLabel (final int trainNr)
    {
        JLabel cabin;
        
        trainCont.createCabin (TrainController.trainName + trainNr);
        cabin = (JLabel) trainCont.getTrainData(TrainController.trainName + trainNr).get ("TrainLabel");
        cabin.setBounds ((50 + (trainNr * 17)), 340, 15, 15);
        cabin.setCursor (new Cursor (Cursor.HAND_CURSOR));
        cabin.setBorder (BorderFactory.createLineBorder (Color.black));

        cabin.addMouseListener (new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                showTrainDetails (trainNr);
            }
        });
        
        return cabin;
    }
    
    private JLabel getTrainLegend(final int trainNr)
    {
        JLabel cabin = new JLabel();
        
        cabin.setText ("  " + TrainController.trainName + trainNr);
        cabin.setOpaque (true);
        cabin.setForeground (new Color (0xffffff));
        cabin.setBackground ((Color) this.trainCont.getTrainData(TrainController.trainName + trainNr).get ("TrainColour"));
        cabin.setBounds (5, (5 + (trainNr * 25)), 70, 23);
        cabin.setCursor (new Cursor (Cursor.HAND_CURSOR));
        cabin.setVisible (true);
        cabin.setBorder (BorderFactory.createLineBorder (Color.black));
        
        cabin.addMouseListener (new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                showTrainDetails (trainNr);
            }
        });
        
        return cabin;
    }
    
    private JButton startButton(final int trainNr)
    {
        JButton start = new JButton();
        
        start.setText ("Start trein");
        start.setOpaque (true);
        start.setForeground (new Color (0xffffff));
        start.setBackground ((Color) this.trainCont.getTrainData(TrainController.trainName + trainNr).get("TrainColour"));
        start.setBounds(80, (5 + (trainNr * 25)), 65, 23);
        start.setCursor (new Cursor (Cursor.HAND_CURSOR));
        start.setVisible (true);
        start.setBorder (BorderFactory.createLineBorder (Color.black));
        
        start.addMouseListener (new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                // Is the train already on the track?
                Object value = trainCont.getTrainData(TrainController.trainName + trainNr).get ("TrainUsed");
                boolean bool = new Boolean (value.toString());
                if (bool == true)
                {
                    
                }
                else
                {
                   setTrainBeginBounds (trainNr);
                }
            }
        });
        return start;
    }
    
    private void setTrainBeginBounds(int trainNr)
    {
        // Find an empty station
        int stationId = this.trackCont.getEmptyStation();
        if (stationId > -1)
        {
            Double[] x = this.trackCont.getStationXCoords (stationId),
                     y = this.trackCont.getStationYCoords (stationId);
            
            Double xCenter = x[(x.length/2)],
                   yCenter = y[(y.length/2)];

            HashMap<String, Object> train = this.trainCont.getTrainData (TrainController.trainName + trainNr);
            JLabel label = (JLabel) train.get ("TrainLabel");
            label.setBounds (xCenter.intValue(), yCenter.intValue() - (15/2), 15, 15);
            
            this.trackCont.stationSet[stationId] = true;
            this.trainCont.setTrainData (TrainController.trainName + trainNr, -1, -1, -1, true, true, stationId, "station");
            this.trainCont.startTrain (TrainController.trainName + trainNr);
            
            if (this.trainSet == false)
            {
                this.trainSet = true;
                this.startSimButton.setEnabled (true);
                this.startSimButton.setFocusable (true);
            }
        }
    }
    
    private JPanel getContrPanel()
    {
        JPanel panel = new JPanel();
        panel.setOpaque (true);
        panel.setBackground (new Color (0xff0000));
        return panel;
    }
    
    private void startSim()
    {
        if (this.trainSet)
        {
            new SimulationController();
            this.startSimButton.setEnabled (false);
            this.startSimButton.setFocusable (false);
        }
    }
}
