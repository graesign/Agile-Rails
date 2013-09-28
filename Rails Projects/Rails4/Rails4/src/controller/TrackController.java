package controller;

import java.util.ArrayList;
import database.MySQL;

/**
 * The TrackController class handles everything at the track.
 * @author RailCab07_4
 */
public class TrackController {
    private static TrackController instance = null;
    
    public static final int START_POINT_X          = 100,
                            START_POINT_Y          = 150,
                            SPACE_BETWEEN          = 3,
                            MAX_STATION_COUNT      = 8,
                            STRAIGHT_TRACK_LENGTH  = 50,
                            CURVED_TRACK_RADIUS    = 75,
                            STATION_HEIGHT         = 10,
                            STATION_LENGTH         = 20;
 
    private ArrayList<Double> tempXTrackCoords      = new ArrayList<Double>(),
                              tempYTrackCoords      = new ArrayList<Double>();

    public Double[] xTrackCoords,
                    yTrackCoords;

    public int[] stationBegin  = new int[MAX_STATION_COUNT],
                 stationEnd    = new int[MAX_STATION_COUNT];
    
    public int[] station = new int[MAX_STATION_COUNT];
    
    public boolean[] stationSet = new boolean[MAX_STATION_COUNT];
    public Double[][] xStationCoords   = new Double[MAX_STATION_COUNT][16],
                      yStationCoords   = new Double[MAX_STATION_COUNT][16];
    
    public int trackCount           = 0,
               stationCount         = 0,
               stationEndCount      = 0,
               stationBegincount    = 0,
               stationPartCount     = 0;
    
    private MySQL db = new MySQL();
    
    public String[] stations = new String[/*tracks.TrackLoader.getInstance().stationCount*/8];
    /**
     * The main constuctor,
     * This constructor is private because we use a singleton loader for this class
     */
    private TrackController() 
    {
        java.util.Arrays.fill (this.stationSet, false);
        
        // Set station names
        this.stations = this.db.fetchStations();
    }
    /**
     * The singleton loader
     * @return TrackController.instance, the instance of this class
     */
    public static TrackController getInstance()
    {
        if (TrackController.instance == null)
        {
            // Do some synchronizing for the threads :)
            synchronized (TrackController.class)
            {
                if (TrackController.instance == null)
                {
                    TrackController.instance = new TrackController();
                }
            }
        }
        return TrackController.instance;
    }
    /**
     * This method checks if its necessary to create the track itself.
     * If so, this method calls its private version to create a track.
     */
    public void createTrack()
    {
        // Check whether the array's are of the same size, if not empty them both
        if (this.tempXTrackCoords.size() != this.tempYTrackCoords.size())
        {
            this.tempXTrackCoords.clear();
            this.tempYTrackCoords.clear();
        }
        
        if (this.tempXTrackCoords.isEmpty() || this.tempYTrackCoords.isEmpty() || this.tempXTrackCoords == null || this.tempYTrackCoords == null)
        {
            this._createTrack();
        }

        return;
    }
    /**
     * this is the actual method of createTrack()
     * this method creates the right elements for the track.
     */
    private void _createTrack()
    {
        // Add the starting point to the array
        this.tempXTrackCoords.add (START_POINT_X + 0.0);
        this.tempYTrackCoords.add (START_POINT_Y + 0.0);
        this.trackCount++;
        
        // creates 8 straight track pieces to the right [---->]
        this.createStraightTrack ("right", 8);
        // creates a curved track piece at the last straight piece on the right side of the track
        this.createCurvedTrack ("right");
        // creates 8 straight track pieces to the left [<----]
        this.createStraightTrack ("left", 8);
        // creates a curved track piece at the last straight piece on the left side of the track
        this.createCurvedTrack ("left");

        // Make something usefull of the Arraylists
        this.xTrackCoords   = new Double[this.trackCount - 1];
        this.yTrackCoords   = new Double[this.trackCount - 1];
        
        for (int tt = 0; tt < this.trackCount - 1; tt++)
        {
            this.xTrackCoords[tt] = this.tempXTrackCoords.get (tt);
            this.yTrackCoords[tt] = this.tempYTrackCoords.get (tt);
        }

        for (int i = 0; i < this.stationCount; i++)
        {
            this.createStation (this.stationBegin[i], i);
        }

        return;
    }
    /**
     * this method creates a straigt piece of the track.
     * @param direction - the direction where the train is going to
     * @param count - the number of pieces to create
     */
    private void createStraightTrack (String direction, int count)
    {
        double newXCoord = 0.0,
               newYCoord = 0.0;
        
        for (int i = 0; i < count; i++)
        {
            if (i % 2 == 0)
            {
                // Mark as station
                this.stationBegin [this.stationCount++]  = this.trackCount;
            }
            
            for (int j = 0; j < STRAIGHT_TRACK_LENGTH; j++)
            {
                if (j % SPACE_BETWEEN == 0)
                {
                    if (direction.equals ("right"))
                    {
                        newXCoord = this.tempXTrackCoords.get ((this.trackCount - 1)) + SPACE_BETWEEN;
                        newYCoord = this.tempYTrackCoords.get ((this.trackCount - 1));
                    }
                    else if (direction.equals ("left"))
                    {
                        newXCoord = this.tempXTrackCoords.get ((this.trackCount - 1)) - SPACE_BETWEEN;
                        newYCoord = this.tempYTrackCoords.get ((this.trackCount - 1));
                    }
                    
                    // store them
                    this.tempXTrackCoords.add (this.trackCount, newXCoord);
                    this.tempYTrackCoords.add (this.trackCount, newYCoord);
                    this.trackCount++;
                }
            }
        }
    }
    /**
     * this method creates a curved piece of the track.
     * @param direction - the direction where the train is going to
     */
    private void createCurvedTrack (String direction)
    {
        double xBegin       = this.tempXTrackCoords.get ((this.trackCount -1)),
               yBegin       = this.tempYTrackCoords.get ((this.trackCount - 1)),
               base         = xBegin - CURVED_TRACK_RADIUS,
               newXCoord    = 0.0,
               newYCoord    = 0.0,
               sinValue     = 0.0,
               cosValue     = 0.0,
               redians      = 0.0;
        
        
        // calculates the coÃ¶rdinates if the track on for all possible degrees on the circle.
        // 1 degrees means the top, 181 degrees means the bottom.
        int begin   = (direction.equals ("right")) ? 1 : 181,
            end     = (direction.equals ("right")) ? 181 : 361;
        
        for (int i = begin; i < end; i++)
        {
            if (i % SPACE_BETWEEN == 0)
            {
                // Do some calculation
                redians     = (i * Math.PI / 180.0);
                sinValue    = Math.sin (redians);
                cosValue    = Math.cos (redians);
                
                // Calculate new coÃ¶rdinates
                if (direction.equals ("right"))
                {
                    newXCoord = (sinValue * CURVED_TRACK_RADIUS) + xBegin;
                    newYCoord = yBegin + (CURVED_TRACK_RADIUS - (cosValue * CURVED_TRACK_RADIUS));
                }
                else if (direction.equals ("left"))
                {
                    newXCoord = (sinValue * CURVED_TRACK_RADIUS) + xBegin;
                    newYCoord = yBegin - (CURVED_TRACK_RADIUS + (cosValue * CURVED_TRACK_RADIUS));
                }
                
                // Add coÃ¶rdinates to the array
                this.tempXTrackCoords.add (this.trackCount, newXCoord);
                this.tempYTrackCoords.add (this.trackCount, newYCoord);
                this.trackCount++;
            }
        }
    }
    /**
     * this method creates the stations on the track.
     * @param beginPoint - the coÃ¶rdinate on the main track that will be marked 
     * as the beginpoint of the station.
     * @param count - this is the ID-number of the station.
     */
    private void createStation(int beginPoint, int count)
    {
        // calculate the begin point.
        Double xBegin = this.getTrackXCoord (beginPoint),
               yBegin = this.getTrackYCoord (beginPoint);
        
        // Set some vars
        double i,
               newXCoord,
               newYCoord;
        int parts   = 0;
        Double[] xCoords = new Double[16],
                 yCoords = new Double[16];
        
        // Set begin
        xCoords[parts] = xBegin;
        yCoords[parts] = yBegin;
        parts++;

        // Set the begin coords
        for (int sC = 0; sC < this.xTrackCoords.length; sC++)
        {
            if (this.xTrackCoords[sC].equals(xCoords[parts - 1]) && this.yTrackCoords[sC].equals(yCoords[parts - 1]))
            {
                this.stationBegin[this.stationBegincount++] = sC;
                break;
            }
        }

        // Set switch
        boolean onTop = count < (this.stationCount/2) ? true : false;
        for (i = 0; i < STATION_HEIGHT; i++)
        {
            if (i % SPACE_BETWEEN == 0)
            {
                if (onTop)
                {
                    newXCoord = xCoords[(parts-1)] + i;
                    newYCoord = yCoords[(parts-1)] - i;
                }
                else
                {
                    newXCoord = xCoords[(parts-1)] - i;
                    newYCoord = yCoords[(parts-1)] + i;
                }
                
                xCoords[parts] = newXCoord;
                yCoords[parts] = newYCoord;
                parts++;
            }
        }

        // Set station
        for (i = 0; i < STATION_LENGTH; i++)
        {
            if (i % SPACE_BETWEEN == 0)
            {
                if (onTop)
                {
                    newXCoord = xCoords[(parts-1)] + i;
                    newYCoord = yCoords[(parts-1)];
                }
                else
                {
                    newXCoord = xCoords[(parts-1)] - i;
                    newYCoord = yCoords[(parts-1)];
                }
                
                xCoords[parts] = newXCoord;
                yCoords[parts] = newYCoord;
                parts++;
            }
        }
        
        // Set switch
        for (i = 0; i < STATION_HEIGHT; i++)
        {
            if (i % SPACE_BETWEEN == 0)
            {
                if (onTop)
                {
                    newXCoord = xCoords[(parts-1)] + i;
                    newYCoord = yCoords[(parts-1)] + i;
                }
                else
                {
                    newXCoord = xCoords[(parts-1)] - i;
                    newYCoord = yCoords[(parts-1)] - i;
                }

                xCoords[parts] = newXCoord;
                yCoords[parts] = newYCoord;
                parts++;
            }
        }

        // Store the station
        this.xStationCoords[count] = xCoords;
        this.yStationCoords[count] = yCoords;
        
        // Set the begin coords
        for (int sC = 0; sC < this.xTrackCoords.length; sC++)
        {
            if (this.xTrackCoords[sC].equals(xCoords[parts - 1]) && this.yTrackCoords[sC].equals(yCoords[parts - 1]))
            {
                this.stationEnd[this.stationEndCount++] = sC;
                break;
            }
        }
    }
    /**
     * Getter command for the track X-coÃ¶rdinate.
     * @param key - track X-axle coÃ¶rdinate-array keynumber 
     * @return - xTrackCoords.get(key)
     */
    public double getTrackXCoord(int key)
    {
        return this.tempXTrackCoords.get (key);
    }
    /**
     * Getter command for the track Y-coÃ¶rdinate.
     * @param key - track Y-axle coÃ¶rdinate-array keynumber 
     * @return - yTrackCoords.get(key)
     */
    public double getTrackYCoord(int key)
    {
        return this.tempYTrackCoords.get (key);
    }
    /**
     * getter command for the X coÃ¶rdinates of the Station array
     * @param station - station ID-number
     * @return - xStationCoords[station]
     */
    public Double[] getStationXCoords(int station)
    {
        return this.xStationCoords[station];
    }
    /**
     * getter command for the Y coÃ¶rdinates of the Station array
     * @param station - station ID-number
     * @return - yStationCoords[station]
     */
    public Double[] getStationYCoords(int station)
    {
        return this.yStationCoords[station];
    }
    /**
     * with this getter you can find the first empty station on the track to place the train
     * on initialisation of the track. This prevents more trains on a single station. 
     * @return - [int i] the ID-number of the empty station
     */
    public int getEmptyStation()
    {
        for (int i = 0; i < MAX_STATION_COUNT; i++)
        {
            if (this.stationSet[i] == false)
            {
                return i;
            }
        }
        // Not found
        return -1;
    }
}
