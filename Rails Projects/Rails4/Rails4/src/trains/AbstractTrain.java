package trains;

import algorithm.Algorithm;
import java.awt.Color;
import java.util.HashMap;
import javax.swing.JLabel;
/**
 * The abstract class which is extended by all train types
 */
abstract public class AbstractTrain {
    protected AbstractRunTrain inner;
    protected Algorithm algoritme       = Algorithm.getInstance();
    // Train specific
    protected Color trainColour                 = null;
    protected int trainCurrentPosition           = 0;
    protected int trainEndPoint                 = 0;
    protected String trainId                    = "";
    protected JLabel trainLabel                 = null;
    protected int trainLabelHeight              = 0;
    protected int trainLabelWidth               = 0;
    protected int trainMaxPassengers            = 0;
    protected boolean trainMoves                = false;
    protected int trainMinForwardSpace          = 0;
    protected int trainSleep                    = 0;
    protected int trainPassengers               = 0;
    protected int trainPosition                 = 0;
    protected String trainPositionType          = "";
    protected int trainStartPoint               = 0;
    protected static Thread trainThread         = null;
    protected boolean updateInterrupt           = false;
    protected Object[][] updateInterruptData    = null;
    protected boolean trainUsed                 = false;
    protected int updateInterruptType           = 0;
    public boolean onStation                    = false;
    protected int maxRuns;
    protected Double[] xRoute                   = null;
    protected Double[] yRoute                   = null;
    // Return data
    /**
     * At some point the train need to start doing things. This method will
     * start the inner class and thread.
     */
    abstract public void startTrain();
    
    abstract public void orderTrain(int orderId);
    /**
     * Set all data for the train, when the value equals -1 or null it is ignored
     * @param trainId - A unique key to indentify the train
     * @param endPoint
     * @param sleepTime - time in miliseconds between two actions of the train
     * @param startPoint
     * @param onStation
     * @param used
     * @param currentPos
     * @param posType
     */
    public void setData(String trainId, int endPoint, int sleepTime, int startPoint, boolean onStation, boolean used, int currentPos, String posType)
    {
        // Go through the params and assign the right value's
        this.trainEndPoint          = (endPoint >= 0) ? endPoint : this.trainEndPoint;
        this.trainId                = (trainId != null) ? trainId : this.trainId;
        this.trainSleep             = (sleepTime >= 0) ? sleepTime : this.trainSleep;
        this.trainStartPoint        = (startPoint >= 0) ? startPoint : this.trainStartPoint;
        this.onStation              = onStation;
        this.trainUsed              = used;
        //this.trainMoves             = used;
        this.trainCurrentPosition    = (currentPos >= 0) ? currentPos : this.trainPosition;
        this.trainPositionType      = (posType != null) ? posType : this.trainPositionType;
    }
    
    public void setRoute(Double[] xRoute, Double[] yRoute)
    {
        this.xRoute = xRoute;
        this.yRoute = yRoute;
    }
    /**
     * Return an Object array with all the data of the train
     * @return getData, array with train data
     */
    public HashMap<String, Object> getData()
    {
        HashMap<String, Object> trainData = new HashMap<String, Object>();
        
        // First add data that belongs to the AbstractTrain class
        trainData.put ("TrainColour", this.trainColour);
        trainData.put ("TrainId", this.trainId);
        trainData.put ("TrainLabel", this.trainLabel);
        trainData.put ("SaveDistance", this.trainMinForwardSpace);
        trainData.put ("TrainPosition", this.trainCurrentPosition);
        trainData.put ("TrainPassengers", this.trainPassengers);
        trainData.put ("TrainMaxPassengers", this.trainMaxPassengers);
        trainData.put ("TrainUsed", this.trainUsed);
        trainData.put ("TrainPositionType", this.trainPositionType);
        trainData.put ("trainMoves", this.trainMoves);
        
        // Now add data from the AbstractRunTrain class
        if (this.inner != null)
        {
            trainData.put ("TrainOrderId", this.inner.trainOrderId);
        }

        // return it :)
        return trainData;
    }
    
    public void moveTrain(Double[] routeX, Double[] routeY)
    {

    }
    
    public boolean trainStarted()
    {
        return this.trainUsed;
    }
    
    /**
     * The abstract inner class for handeling the movement of the trains.
     */
    abstract protected class AbstractRunTrain implements Runnable
    {
        protected AbstractTrain parent;
        protected int trainOrderId = -1;

        // The main method, this method is called when a new thread is created
        public void run()
        {
            // Never ending loop to make shure that this method, and the thread
            // stay alive
            while (this.parent.trainUsed)
            {
                
                // Just a way to break from the loop
                if (this.parent.trainMoves == false)
                {
                    this.parent.trainUsed = false;
                }
            }
        }

        protected boolean freeTrack()
        {
            return this.parent.algoritme.saveDistance (this.parent.trainId);
        }
        
        abstract protected void move(int xPos, int yPos);
    }
}
