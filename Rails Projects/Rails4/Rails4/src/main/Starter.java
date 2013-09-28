package main;

import database.MySQL;
import gui.TrainChoose;
/**
 * this is the absolute starter class
 * @author RailCab07_4
 */
public class Starter {
    private static MySQL db = new MySQL();
    /**
     * Main method. This method is used to start the program.
     * @param args - arguments to use in the program
     */
    public static void main(String[] args)
    {
        // Before we do any thing make sure that the database is ready for the programm to start
        db.prepareDatabase();
        db.MySQLDisconnect();
        
        new TrainChoose();
    }
}
