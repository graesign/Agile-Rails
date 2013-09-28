package algorithm;

import controller.TrackController;
import controller.TrainController;
import database.MySQL;
import gui.ViewTrack;
import java.util.HashMap;
import java.util.Stack;
import trains.AbstractTrain;

/**
 * The Algorithm class makes all decicions for the trains.
 * @author RailCab07_4
 */
public class Algorithm {
    private static Algorithm instance       = null;
    private MySQL db              = new MySQL();
    private TrackController trackCont;
    private TrainController trainCont;
    private static Thread algorithmThread   = null;
    /**
     * Main constructor, this constructor is private due to the singleton loader.
     */
    private Algorithm()
    {
        this.trackCont              = TrackController.getInstance();
        this.trackCont.createTrack();
        this.trainCont              = TrainController.getInstance();
        innerAlgorithm inner        = new innerAlgorithm (this);
        algorithmThread             = new Thread (inner);
        algorithmThread.start();
    }
    /**
     * Singleton loader, the construction of this class is handled by a singleton loader
     * this prevents double instances of this class
     * @return Algorithm.instance, the instance of this class.
     */
    public static Algorithm getInstance()
    {
        if (Algorithm.instance == null)
        {
            // Synchronize for the threads
            synchronized (Algorithm.class)
            {
                if (Algorithm.instance == null)
                {
                    Algorithm.instance = new Algorithm();
                }
            }
        }
        
        return Algorithm.instance;
    }
    /**
     * this method decides which trains will be used to transport a group of people from a station to another.
     * @param startStation - pick up the passengers on this station.
     * @param endStation - bring the passengers to this station.
     * @param passengerCount - number of passengers to transport.
     */
    public void orderTrain(int startStation, int endStation, int passengerCount)
    {
        // We only need to store this data in the database.
        this.db.storeRouteData(startStation, endStation, passengerCount);
    }
    private void handleOrder(int[] orderData)
    {
        // Set the data
        int startStation    = orderData[1];
        int endStation      = orderData[2];
        int passengerCount  = orderData[3];
        
        // Do some checking for the passengers (request/free places).
        String closestTrainId = this.getNearestFreeTrain (startStation);
        
        int currentPassengerCount   = new Integer ((Integer) this.trainCont.getTrainData(closestTrainId).get("TrainPassengers")),
            freePlaces              = (new Integer ((Integer) this.trainCont.getTrainData(closestTrainId).get("TrainMaxPassengers")) - currentPassengerCount),
            passengerDiff           = passengerCount - freePlaces;
        
        if (passengerDiff < 0)
        {
            // Set the route
            this.createRoute(startStation, endStation, closestTrainId);
        }
        else
        {
            // Set the route
            this.createRoute(startStation, endStation, closestTrainId);
            
            // Make a new order for the remaining passengers
            orderData[3] = passengerDiff;
            this.handleOrder(orderData);
        }
        closestTrainId = null;
    }

    public void createRoute(int startStation, int endStation, String trainId)
    {
        Stack<Double> routeStackX = new Stack<Double>(),
                      routeStackY = new Stack<Double>();
        int i,
            trackBegin      = this.trackCont.stationEnd[startStation],
            trackEnd        = this.trackCont.stationBegin[endStation];
        
        if (new Integer ((Integer) this.trainCont.getTrainData(trainId).get("TrainPosition")) == endStation)
        {
            routeStackX.push (this.trackCont.xStationCoords[startStation][this.trackCont.xStationCoords[startStation].length/2]);
            routeStackY.push (this.trackCont.yStationCoords[startStation][this.trackCont.xStationCoords[startStation].length/2]);
        }
        else
        {
            // Fetch the start station route
            for (i = 0; i < this.trackCont.xStationCoords[startStation].length; i++)
            {
                if (i < (this.trackCont.xStationCoords[startStation].length/2))
                {
                    continue;
                }

                routeStackX.push (this.trackCont.xStationCoords[startStation][i]);
                routeStackY.push (this.trackCont.yStationCoords[startStation][i]);
            }

            // Get the indexes for the track between the stations.
            if (trackBegin > trackEnd)
            {
                // need two loops
                for (i = trackBegin; i < this.trackCont.trackCount; i++)
                {
                    // For somereason the array size doesn't fit all the time so build in a break
                    if (i == (this.trackCont.trackCount - 1))
                    {
                        break;
                    }

                    routeStackX.push (this.trackCont.xTrackCoords[i]);
                    routeStackY.push (this.trackCont.yTrackCoords[i]);
                }
                for (i = 0; i <= trackEnd; i++)
                {
                    // For somereason the array size doesn't fit all the time so build in a break
                    if (i == (this.trackCont.trackCount - 1))
                    {
                        break;
                    }

                    routeStackX.push (this.trackCont.xTrackCoords[i]);
                    routeStackY.push (this.trackCont.yTrackCoords[i]);
                }
            }
            else
            {
                // Need just one loop
                for (i = trackBegin; i <= trackEnd; i++)
                {
                    // For somereason the array size doesn't fit all the time so build in a break
                    if (i == (this.trackCont.trackCount - 1))
                    {
                        break;
                    }

                    routeStackX.push (this.trackCont.xTrackCoords[i]);
                    routeStackY.push (this.trackCont.yTrackCoords[i]);
                }
            }

            // Fetch the end station part
            for (i = 0; i < this.trackCont.xStationCoords[endStation].length; i++)
            {
                if (i > (this.trackCont.xStationCoords[endStation].length/2))
                {
                    continue;
                }

                routeStackX.push (this.trackCont.xStationCoords[endStation][i]);
                routeStackY.push (this.trackCont.yStationCoords[endStation][i]);
            }
        }

        Double[] routeX = new Double[routeStackX.size()];
        Double[] routeY = new Double[routeStackY.size()];
        routeStackX.toArray (routeX);
        routeStackY.toArray (routeY);

        // Send order
        this.trainCont.setTrainRoute(trainId, routeX, routeY);
        //System.out.println("createRoute");
         
    }
    /**
     * Search for the closest train (to a station), with place for at least 1 passenger.
     * @param requestPos - Index in the track-array from where the request is made
     * @return closestId - The id of the closest train
     */
    public String getNearestFreeTrain (int requestPos)
    {
        int i               = 0,
            diff            = 0,
            currPos         = 0,
            closest         = 0,
            currPass        = 0,
            maxPass         = 0,
            maxTrackKeys    = this.trackCont.trackCount;
        
        Boolean onStation   = false,
                trainUsed   = false,
                active      = false;
        
        String closestId    = "";

        HashMap<String, Object> train;

        for (String trainId : this.trainCont.getTrackerArray())
        {
            // Stops the loop when all the trains are checked. [null value in the array]
            if (trainId == null)
            {
                break;
            }
            
            // Fetch the train data
            train       = this.trainCont.getTrainData (trainId);
            currPos     = new Integer ((Integer) train.get ("TrainPosition"));
            currPass    = new Integer ((Integer) train.get ("TrainPassengers"));
            maxPass     = new Integer ((Integer) train.get ("TrainMaxPassengers"));
            onStation   = train.get("TrainPositionType").toString().equals ("station") ? true : false;
            trainUsed   = train.get("trainMoves").toString().equals ("true") ? true : false;
            active      = train.get ("TrainUsed").toString().equals ("true") ? true : false;
            
            // Continue when the train isn't active, has no space or already moves
            if (!active || (/*currPass == maxPass*/currPass > 0) || trainUsed)
            {
                continue;
            }
            
            int tempRequestPos = (this.trackCont.stationBegin[requestPos]);
            
            // Change currPos for trains on the station
            if (onStation)
            {
                 currPos = (this.trackCont.stationBegin[currPos] - 8);
            }
            
            // Calculate the distance between the trains and the [requestPos]
            if (currPos > tempRequestPos)
            {
                //System.out.println(trainId);
                diff = (maxTrackKeys - currPos) + tempRequestPos;
            }
            else
            {
                diff = tempRequestPos - currPos;
            }

            if (diff < closest || closest == 0)
            {
                closest     = diff;
                closestId   = trainId;
            }
        }

        return closestId;   
    }
    /**
     * Checks the distance between 2 trains, and checks if the distance meets with our safety measures.
     * @param trainId - The id of the train which makes the request
     * @return boolean - Is there a save distance between the trains?
     */
    public boolean saveDistance (String trainId)
    {
        if(this.trainCont.getTrainData(trainId).get("TrainPositionType").equals("station"))
        {
                return this.saveStationExit(trainId);
        }
        
        // Get the data of the request train
        int saveDistance    = new Integer ((Integer) this.trainCont.getTrainData(trainId).get ("SaveDistance"));
        int trainPos        = new Integer ((Integer) this.trainCont.getTrainData(trainId).get ("TrainPosition"));

        String[] tArray     = this.trainCont.getActiveArray();
        
        for (String train : tArray)
        {            
            // Stops the loop when all the trains are checked. [null value in the array]
            if (train == null)
            {
                break;
            }

            // We don't have to compare with the train itself
            if (train.equals (trainId))
            {
                continue;
            }

            // Get the position of the train to check with
            int checkPos = new Integer ((Integer) this.trainCont.getTrainData(train).get("TrainPosition"));  // position to check with
            
            System.out.println(trackCont.stationCount);
            for (int i = 0; i < trackCont.stationCount; i++){
                
            }
            // Check
            if (checkPos < trainPos)
            {
                continue;
            }
                          
            else if ((checkPos - trainPos) <= saveDistance)
            {
                return false;
            }
        }       
        return true;
    }
    
    public boolean saveStationEnter(String trainId)
    {
        String[] tArray     = this.trainCont.getTrackerArray();
        for (String train : tArray)
        {
            if(train == null){
                break;
            }
            if(train.equals(trainId))
            {
                continue;
            }
            HashMap<String, Object> trainData = this.trainCont.getTrainData(trainId);
            int trainPos    = new Integer ((Integer) trainData.get("TrainPosition"));
            int checkpos = trainPos - 6;
            if (trainPos == trainCont.getTrainData(trainId).get("TrainPosition"))
            {
                return false;
            }
        }
        return true;
    }
    /**
     * this method checks if it is safe for a train to exit a certain station.
     * @param trainId - ID number of the train
     * @return - is it safe or not?
     */
    public boolean saveStationExit(String trainId)
    {
        String[] tArray     = this.trainCont.getTrackerArray();
        for (String train : tArray)
        {
            if (train == null)
            {
                break;
            }
            if (train.equals (trainId))
            {
                continue;
            }
            HashMap<String, Object> trainData = this.trainCont.getTrainData(trainId);
            int trainPos   = new Integer ((Integer) trainData.get ("TrainPosition"));
            if((Integer)trainCont.getTrainData(train).get ("TrainPosition") > trackCont.stationBegin[trainPos] && (Integer)trainCont.getTrainData(train).get ("TrainPosition") < (trackCont.stationEnd[trainPos] + (Integer)trainCont.getTrainData(train).get("SaveDistance")))
            {
                return false;
            }
        }
        
        return true;
    }
    
    private class innerAlgorithm implements Runnable
    {
        private Algorithm parent    = null;
        private String trainId      = "";
        private MySQL db  = null;
        private int numberOfRuns;
        
        protected innerAlgorithm (Algorithm parent)
        {
            this.parent = parent;
            this.db     = new MySQL();
        }
        
        @SuppressWarnings("static-access")
        public void run()
        {
            int i = 0;
            while (true)
            {
                
                // Select the first train order in line, if none keep looping till we find one.
                int[] orderData = null;
                while (orderData == null)
                {
                    orderData = this.db.fetchOrder();
                    // Sleep to prefend overloading
                    try
                    {
                        this.parent.algorithmThread.sleep (ViewTrack.simSpeed);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                
                this.buildOrders (orderData);               
            }
        }
        
        private synchronized void buildOrders(int[] orderData)
        {
            
            // Get free train and request a route for as much passengers as possible at the moment.
            // Set the remaining back in the database.
            String nearestTrain = null;
            nearestTrain = this.parent.getNearestFreeTrain (orderData[1]);
            if (nearestTrain.equals(""))
            {
                return;
            }
            // How many passengers can we transport?
            AbstractTrain train = this.parent.trainCont.trains.get (nearestTrain);
            int tPassengers = (new Integer ((Integer) train.getData().get ("TrainMaxPassengers")) - new Integer ((Integer) train.getData().get("TrainPassengers")));

            // Update the order field
            int tempPassengerCount = (tPassengers < orderData[3]) ? orderData[3] - tPassengers : 0;
            // Store the start/end points in the database and send the orderId to the train.
            int orderId = this.db.generateOrder (orderData[1], orderData[2], (orderData[3] - tempPassengerCount));
            
            orderData[3] = tempPassengerCount;
            if (orderData[3] == 0)
            {
                System.out.println ("this.db.setOrderDone (orderId):\t" + orderId);
                this.db.setOrderDone (orderId);
            }
            this.db.updateOrder (orderData);
            this.parent.trainCont.orderTrain(nearestTrain, orderId);
        }
    }
}
