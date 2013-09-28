package controller;

import database.MySQL;
import helpers.Array;
import java.util.HashMap;
import javax.swing.JLabel;
import trains.AbstractTrain;
import trains.Cabin;
import trains.NormalTrain;
/**
 * The TrainController class is responsible for all communications between the trains and the rest
 * of the software.
 * @author RailCab07_4
 */
public class TrainController {
    private static TrainController trainCont = null;
    public static final int MAX_TRAINCOUNT   = 10;
    private MySQL db                         = new MySQL();
    
    /**
     * Global name for all the trains (will get an id as postfix).
     */
    public static final String trainName    = "RailCab";

    private static TrainController instance     = null;
    // Train storage
    public HashMap<String, AbstractTrain> trains    = new HashMap<String, AbstractTrain>();
    private String[] trainTracker                   = new String[MAX_TRAINCOUNT];
    public int trainCount                          = 0;
    /**
     * Main constructor, this constructor is private due to the singleton loader
     */
    private TrainController() { }
    /**
     * Singleton loader, the construction of this class is handled by a singleton loader
     * this prevents double instances of this class
     * @return TrainController.instance, the instance of this class.
     */
    public static TrainController getInstance()
    {
        if (TrainController.instance == null)
        {
            // Synchronize for the threats
            synchronized (TrainController.class)
            {
                if (TrainController.instance == null)
                {
                    TrainController.instance = new TrainController();
                }
            }
        }
        
        return TrainController.instance;
    }
    /**
     * Create a new train of the type cabin
     * @param trainId, A unique key to indentify the train
     */
    public void createCabin(String trainId)
    {
        // Can we create an other train?
        if (this.trainCount == MAX_TRAINCOUNT)
        {
            System.out.println ("Te veel treinen in het systeem!");
            return;
        }
        
        // Does the key already exists?
        if (Array.in_array (trainId, this.trainTracker) == true)
        {
            System.out.println ("Er bestaat al een trein met dit id");
            return;
        }
        
        // Create the train
        this.trains.put (trainId, (new Cabin (trainId)));
        this.trainTracker[this.trainCount++] = trainId;
        
        return;
    }
    
    public void createNormalTrain(String trainId)
    {
        if (this.trainCount == MAX_TRAINCOUNT)
        {
            System.out.println("Te veel treinen in het systeem!");
            return;
        }
        
        if (Array.in_array(trainId, this.trainTracker) == true)
        {
            System.out.println("Er bestaat al een trein met dit id");
            return;
        }
        
        // Create the normal train
        this.trains.put(trainId, (new NormalTrain(trainId)));
        this.trainTracker[this.trainCount++] = trainId;
    }
    /**
     * Set all data for the train, when the value equals -1 or null it is ignored
     * @param trainId - A unique key to indentify the train
     * @param endPoint - The endpoint for this train (this value is key from the track array)
     * @param sleepTime - time in miliseconds between two actions of the train
     * @param startPoint - The startpoint for this train (this value is key from the track array)
     * @param isAtStation - 
     * @param used - 
     */
    public void setTrainData(String trainId, int endPoint, int sleepTime, int startPoint, boolean isAtStation, boolean used, int pos, String posType)
    {
        // Is there a train with this key.
        if (Array.in_array (trainId, this.trainTracker) == false)
        {
            System.out.println ("De opgevraagde trein bestaat niet in het systeem");
            return;
        }
        
        AbstractTrain train = this.trains.get (trainId);
        train.setData (trainId,endPoint, sleepTime, startPoint, isAtStation, used, pos, posType);
    }
    /**
     * Get all the data holded by one train.
     * @param trainId, A unique key to indentify the train
     * @return train.getData(), Object array containing all the train data
     */
    public HashMap<String, Object> getTrainData(String trainId)
    {
        // Is there a train with this key.
        if (Array.in_array (trainId, this.trainTracker) == false)
        {
            System.out.println ("De opgevraagde trein bestaat niet in het systeem");
            return null;
        }
        AbstractTrain train = this.trains.get (trainId);
        return train.getData();
    }
    /**
     * Starts the train.
     * @param trainId - ID-number of the train.
     */
    public void startTrain (String trainId)
    {
        this.trains.get(trainId).startTrain();
    }
    public void orderTrain (String trainId, int orderId)
    {
        this.trains.get(trainId).orderTrain(orderId);
        return;
    }
    /**
     * this method moves the train to a specific position
     * @param trainId - ID-number of the train.
     * @param x - X coördinate to move the train to.
     * @param y - Y coördinate to move the train to.
     */
    public void moveTrain (String trainId, int x, int y)
    {
        // Get the label
        JLabel trainLabel = (JLabel) this.getTrainData(trainId).get ("TrainLabel");
        // set the new bounds
        trainLabel.setBounds (x, y, 15, 15);
        trainLabel.validate();
    }
    
    public void setTrainRoute (String trainId, Double[] xRoute, Double[] yRoute)
    {
        this.trains.get(trainId).setRoute(xRoute, yRoute);
    }
    /**
     * this method returns an Array with all the train ID's that are currently in use.
     * @return - trainTracker
     */
    public String[] getTrackerArray()
    {
        return this.trainTracker;
    }
    
    public static int getNewPassengerCount(boolean pickUp, Integer order_id)
    {
        if (trainCont == null)
        {
            trainCont = new TrainController();
        }
        
        if (pickUp)
        {
            return trainCont.db.getOrderPassengerCount (order_id);
        }
        
        return 0;
    }
    
    public String[] getActiveArray()
    {
        String[] temp = new String[MAX_TRAINCOUNT];
        int count = 0;
        
        for (String train : this.trainTracker)
        {
            if (train == null)
            {
                break;
            }

            if (this.trains.get(train).trainStarted())
            {
                temp[count++] = train;
            }
        }

        return temp;
    }
}
