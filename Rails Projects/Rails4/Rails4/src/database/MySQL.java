package database;

import controller.TrackController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *this class connects to the SQL database 
 * @author RailCab07_4
 */
public class MySQL
{
    
    private static final String dbUser = "railcab",
                                dbPass = "railcab",
                                dbPath = "jdbc:mysql://127.0.0.1/railcab";
     
    /*
     private static final String dbUser = "dik11",
                                dbPass = "ei$s0VI4j",
                                dbPath = "jdbc:mysql://oege.ie.hva.nl/zdik11";
    */
    private static final boolean MySQLUsable = true;
    
    private Connection connection = null;

    public MySQL()
    {
        if (!MySQLUsable)
        {
            return;
        }
        
        if (this.connection == null)
        {
            try
            {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                this.connection = DriverManager.getConnection(dbPath, dbUser, dbPass);
            }



            catch (SQLException ex)
            {
                Logger.getLogger (MySQL.class.getName()).log (Level.SEVERE, null, ex);
            }            catch (InstantiationException ex)
            {
                Logger.getLogger (MySQL.class.getName()).log (Level.SEVERE, null, ex);
            }            catch (IllegalAccessException ex)
            {
                Logger.getLogger (MySQL.class.getName()).log (Level.SEVERE, null, ex);
            }            catch (ClassNotFoundException ex)
            {
                Logger.getLogger (MySQL.class.getName()).log (Level.SEVERE, null, ex);
            }
        }
    }
    
    public void MySQLDisconnect()
    {
        if (!MySQLUsable)
        {
            return;
        }
        
        // Close it
        if (this.connection != null)
        {
            try {
                this.connection.close();
                this.connection = null;
            }
            catch (SQLException ex)
            {
                Logger.getLogger (MySQL.class.getName()).log (Level.SEVERE, null, ex);
            }

            // Get rid of this object
            try {
                this.finalize();
            }
            catch (Throwable ex)
            {
                Logger.getLogger (MySQL.class.getName()).log (Level.SEVERE, null, ex);
            }
        }
    }
    
    public int[] getTrainOrder(int orderId)
    {
        if (!MySQLUsable)
        {
            int[] route = new int[2];
            route[0]    = 3;
            route[1]    = 4;
            
            return route;
        }
        
        if (this.connection != null)
        {
            try
            {
                int[] route = new int[2];
                
                String sql = "SELECT pickupStation, destinationStation\n\tFROM treinOpdrachten\n\t\tWHERE id = " + orderId + "\nLIMIT 0, 1";
                MySQLQuery mq       = new MySQLQuery (sql, true, this.connection);
                MySQLResult result  = mq.getMySQLResult();
                
                if (result.queryResult.first() == false)
                {
                    route = null;
                }
                else
                {
                    route[0] = result.queryResult.getInt (1);
                    route[1] = result.queryResult.getInt (2);
                }
                
                result.MySQLFreeresult();
                
                return route;
            }
            catch (SQLException ex)
            {
                Logger.getLogger (MySQL.class.getName()).log (Level.SEVERE, null, ex);
            }
        }
        
        return null;
    }
    
    public String[] fetchStations() 
    {
        if (!MySQLUsable)
        {
            String[] station    = new String[TrackController.MAX_STATION_COUNT];
            
            // Set some names for the stations to prefend problems when running this programm outside the oege.
            station[0] = "1";
            station[1] = "2";
            station[2] = "3";
            station[3] = "4";
            station[4] = "5";
            station[5] = "6";
            station[6] = "7";
            station[7] = "8";
            
            return station;
        }

        if (this.connection != null)
        {
            try
            {
                String[] station    = new String[TrackController.MAX_STATION_COUNT];
                
                int number          = 0;
                String sql          = "SELECT * FROM stations";
                MySQLQuery mq       = new MySQLQuery (sql, true, this.connection);
                MySQLResult result  = mq.getMySQLResult();

                while (result.queryResult.next() && number < TrackController.MAX_STATION_COUNT)
                { 
                    station[number] = result.queryResult.getString ("station");
                    number++;
                }
                
                result.MySQLFreeresult();
                
                return station;
            }
            catch (SQLException ex)
            {
                Logger.getLogger (MySQL.class.getName()).log (Level.SEVERE, null, ex);
            }
        }
        
        return null;
    }
    
    public void storeRouteData(int startStation, int endStation, int passengerCount)
    {
        Date date       = new Date();
        long timestamp  = date.getTime();
        
        if (this.connection != null)
        {
            String sql  = "INSERT INTO reizen (" +
                          "id, " +
                          "timestamp, " +
                          "startStation, " +
                          "endStation, " +
                          "passengers" +
                          ")" +
                          "\tVALUES (" +
                          "\t\tNULL, " + timestamp + ", " + startStation + ", " + endStation + ", " + passengerCount +
                          "\t);";
           new MySQLQuery (sql, false, this.connection);
        }
    }
    
    public int[] fetchOrder()
    {
        synchronized (this)
        {
            if (this.connection != null)
            {
                //String sql          = "SELECT id, startStation, endStation, passengers, passengersMoved\n\tFROM reizen\n\t\tWHERE used = 0\n\t\t\tLIMIT 0, 1\n";
                String sql          = "SELECT id, startStation, endStation, passengers, passengersMoved\n\tFROM reizen\n\t\tWHERE passengers > 0\n\t\t\tLIMIT 0, 1\n";
                MySQLQuery mq       = new MySQLQuery (sql, true, this.connection);
                MySQLResult result  = mq.getMySQLResult();
                int[] orderData     = new int[5];

                try
                {
                    if (result.queryResult.first() == false)
                    {
                        orderData = null;
                    }
                    else
                    {
                        orderData[0] = result.queryResult.getInt (1);
                        orderData[1] = result.queryResult.getInt (2);
                        orderData[2] = result.queryResult.getInt (3);
                        orderData[3] = result.queryResult.getInt (4);
                        orderData[4] = result.queryResult.getInt (5);
                    }
                }
                catch (SQLException ex)
                {
                    Logger.getLogger (MySQL.class.getName()).log (Level.SEVERE, null, ex);
                }

                result.MySQLFreeresult();
                
                return orderData;
            }
        }
        
        return null;
    }
    
    public void updateOrder (int[] orderData)
    {
        synchronized (this)
        {
            if (this.connection != null)
            {
                String sql = "UPDATE reizen\n\tSET startStation = " + orderData[1] + ", endStation = " + orderData[2] + ", passengers = " + orderData[3] + ", passengersMoved = " + orderData[4] + "\n\t\tWHERE id = " + orderData[0];
                new MySQLQuery (sql, false, this.connection);
            }
        }
    }
    
    public void setOrderDone (int orderId)
    {
        String sql = "UPDATE reizen\n\tSET used = 1\n\t\t WHERE id = " + orderId;
        new MySQLQuery (sql, false, this.connection);
    }

    public void prepareDatabase()
    {
        //String sql = "UPDATE reizen\n\tSET used = 1\n\t\tWHERE 1 = 1\n\t\t\tAND used = 0";
        String sql = "TRUNCATE reizen";
        new MySQLQuery (sql, false, this.connection);
        sql = "TRUNCATE treinOpdrachten";
        new MySQLQuery (sql, false, this.connection);
    }
    
    public synchronized int generateOrder(int startStation, int endStation, int passengers)
    {
        int last_id = -1;
        
        try
        {
            PreparedStatement pstmt = connection.prepareStatement ("INSERT INTO treinOpdrachten (id, pickupStation, destinationStation, passengers) VALUES (NULL, " + startStation + ", " + endStation + ", " + passengers + ");");
            pstmt.execute();
            ResultSet result = pstmt.getGeneratedKeys();
            if (result.next())
            {
                System.err.println (result.getInt (1));
                last_id = result.getInt (1);
            }
            result.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(MySQL.class.getName()).log (Level.SEVERE, null, ex);
        }

        return (last_id == -1) ? 1 : last_id;
    }
    
    public int getOrderPassengerCount (int orderId)
    {
        if (this.connection != null)
        {
            String sql = "SELECT passengers FROM treinOpdrachten WHERE id = " + orderId + " LIMIT 0,1;";
            MySQLQuery mq       = new MySQLQuery (sql, true, this.connection);
            MySQLResult result  = mq.getMySQLResult();
    
            try
            {
                if (result.queryResult.next())
                {
                    return result.queryResult.getInt (1);
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(MySQL.class.getName()).log (Level.SEVERE, null, ex);
            }
        }
        
        return 0;
    }
}

class MySQLQuery {
    private String query            = "";
    private Connection con          = null;
    private Statement statement     = null;
    private PreparedStatement pstmt = null;
    private MySQLResult mysqlResult = null;
    protected int lastIndex         = -1;
    
    public MySQLQuery(String query, boolean result, Connection connection)
    {
        if (query.equals ("") || connection == null)
        {
            return;
        }
        this.query  = query;
        this.con    = connection;
        
        if (result == true)
        {
            try
            {
                this.statement     = this.con.createStatement();
                this.statement.executeQuery(this.query);
                this.mysqlResult   = new MySQLResult (this.statement.getResultSet());
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        else if (result == false)
        {
            try
            {
                this.statement = this.con.createStatement ();
                this.statement.executeUpdate(this.query);
                return;
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public MySQLQuery (PreparedStatement pstmt, boolean result, Connection connection)
    {
        if (query.equals ("") || connection == null)
        {
            return;
        }
        this.con = connection;
        
        if (result == true)
        {
            try
            {
                this.pstmt = pstmt;
                this.pstmt.executeQuery();
                this.mysqlResult = new MySQLResult (pstmt/*.getResultSet()*/);
                return;
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                this.pstmt = pstmt;
                this.pstmt.executeUpdate();
                return;
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    protected MySQLResult getMySQLResult()
    {
        return this.mysqlResult;
    }
}

class MySQLResult {
    ResultSet queryResult = null;
    Integer lastIndex = 0;

    protected MySQLResult (ResultSet r)
    {
        this.queryResult = r;
    }
    
    protected MySQLResult (PreparedStatement pstmt)
    {
        try
        {
            this.queryResult = pstmt.getResultSet();
            //this.lastIndex = pstmt.getL
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    protected void MySQLFreeresult()
    {
        try
        {
            this.queryResult.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
