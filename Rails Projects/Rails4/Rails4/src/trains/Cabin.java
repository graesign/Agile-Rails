package trains;

import controller.TrackController;
import controller.TrainController;
import database.MySQL;
import java.awt.Color;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
/**
 * Class which handles trains of the type Cabin.
 * This class handles everithing related to the train.
 */
public class Cabin extends AbstractTrain
{
    /**
     * Constructor
     * @param trainId - A unique key to indentify the train
     */
    public Cabin(String trainId)
    {
        this.trainMaxPassengers     = 10;
        this.trainMinForwardSpace   = 4;
        this.trainLabelWidth        = 15;
        this.trainLabelHeight       = 15;
        this.trainUsed              = false;
        this.trainSleep             = 50;
        
        this.trainId    = trainId;
        
        // Create a label for this train
        this.trainLabel = new JLabel();
        this.trainLabel.setOpaque (true);
        Random r = new Random();
        this.trainColour = new Color (r.nextInt (200), r.nextInt (200), r.nextInt (200));
        this.trainLabel.setBackground (this.trainColour);
        this.trainLabel.setForeground (new Color (0xffffff));
    }
    /**
     * At some point the train need to start doing things. This method will
     * start the inner class and thread.
     */
    public void startTrain()
    {
        this.trainUsed  = true;
        this.inner      = new RunTrain (this);
        trainThread     = new Thread (this.inner);
        trainThread.start();
    }
    public void orderTrain(int orderId)
    {
        if (this.trainThread == null || this.trainUsed == false)
        {
            this.startTrain();
        }
        
        this.inner.trainOrderId = orderId;
        return;
    }
    /**
     * This inner class handles the train while moving
     */
    private class RunTrain extends AbstractRunTrain
    {
        /**
         * Main constructor
         * @param instance, The parent class so we can use everything that Cabin uses
         */
        private TrackController trackCont;
        private MySQL db = new MySQL();
        protected RunTrain(Cabin instance)
        {
            this.parent = instance;
            trackCont = TrackController.getInstance();
        }

        /**
         * Main method, this method is called when a new thread is created.
         */
        boolean b = false;
        @Override
        public void run()
        {        
            while (true)
            {
                try {
                    // Wait for orders
                    while (this.trainOrderId == -1) 
                    {
                        try 
                        {
                            this.parent.trainThread.sleep(this.parent.trainSleep);
                        } catch (Exception e) 
                        {
                            e.printStackTrace();
                        }
                    }
                    // Set train as active
                    this.parent.trainMoves = true;
                    int[] trainOrder = this.db.getTrainOrder(trainOrderId);
                    this.parent.trainPassengers = TrainController.getNewPassengerCount(false, trainOrderId);
                    // System.out.println(trainOrder[0] + "   " + trainOrder[1]);
                    // Do something with the orders
                    //if (this.parent.trainCurrentPosition != trainOrder[1])
                    //{
                        this.parent.algoritme.createRoute(this.parent.trainCurrentPosition, trainOrder[0], this.parent.trainId);

                        for (int i = 0; i < this.parent.xRoute.length; i++) {
                            this.move(this.parent.xRoute[i].intValue(), this.parent.yRoute[i].intValue());
                            int stationId = 0;
                            if (this.parent.trainPositionType.equals("station")) {
                                stationId = this.parent.trainPosition;
                                trackCont.stationSet[stationId] = false;
                            }
                            try {
                                this.parent.trainThread.sleep(this.parent.trainSleep);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    //}
                    this.parent.trainThread.sleep(3000);
                    this.parent.trainPassengers = TrainController.getNewPassengerCount(true, trainOrderId);
                    this.parent.algoritme.createRoute(trainOrder[0], trainOrder[1], this.parent.trainId);

                    for (int i = 0; i < this.parent.xRoute.length; i++) {
                        this.move(this.parent.xRoute[i].intValue(), this.parent.yRoute[i].intValue());
                        int stationId = 0;
                        if (this.parent.trainPositionType.equals("station")) {
                            stationId = this.parent.trainPosition;
                            trackCont.stationSet[stationId] = false;
                        }
                        try {
                            this.parent.trainThread.sleep(this.parent.trainSleep);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    this.parent.trainCurrentPosition = trainOrder[1];
                    this.parent.trainThread.sleep(2000);
                    this.parent.trainPassengers = TrainController.getNewPassengerCount(false, trainOrderId);
                    // Unet train as active
                    this.db.setOrderDone(trainOrderId);
                    this.parent.xRoute = null;
                    this.parent.yRoute = null;
                    this.trainOrderId = -1;
                    this.parent.trainUsed = true;
                    this.parent.trainMoves = false;
                } catch (InterruptedException ex) {
                    Logger.getLogger(Cabin.class.getName()).log(Level.SEVERE, null, ex);
                }
                } 
            }
        @Override
        protected void move(int xPos, int yPos) 
        {
            this.parent.trainLabel.setBounds((xPos - (this.parent.trainLabelHeight/2)), (yPos - (this.parent.trainLabelWidth/2)), this.parent.trainLabelHeight, this.parent.trainLabelWidth);
        }
    }
}
