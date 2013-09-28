package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *this class connects to the SQL database 
 * @author RailCab07_4
 */
public class MySQLConnection
{
    private static final String   dbUser = "railcab",
                                dbPass = "railcab",
                                dbPath = "jdbc:mysql://localhost/railcab";
    
    private static final boolean MySQLUsable = true;
    public int MAX_USERS = 15;
    
    private Connection connection = null;

    public MySQLConnection()
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
                Logger.getLogger (MySQLConnection.class.getName()).log (Level.SEVERE, null, ex);
            }
            catch (InstantiationException ex)
            {
                Logger.getLogger (MySQLConnection.class.getName()).log (Level.SEVERE, null, ex);
            }
            catch (IllegalAccessException ex)
            {
                Logger.getLogger (MySQLConnection.class.getName()).log (Level.SEVERE, null, ex);
            }
            catch (ClassNotFoundException ex)
            {
                Logger.getLogger (MySQLConnection.class.getName()).log (Level.SEVERE, null, ex);
            }
        }
       
    }
    
    
    public boolean checkUser(String naam, String wachtwoord)
    {
        boolean check = false;
        try {
      
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Gebruikers WHERE gebruikersnaam=? AND wachtwoord=? LIMIT 0,1" );
            pstmt.setString(1, naam);
            pstmt.setString(2, wachtwoord);
            ResultSet result = pstmt.executeQuery();
            
            if (result.first() == true) {
            check = true;

            }
        } catch (SQLException ex) {
            Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return check;
    }
    
    public String getErrorLog()
    {
        String log = "";
        String sql = "SELECT error FROM errorlog";
        MySQLQuery mq = new MySQLQuery(sql, true, this.connection);
        MySQLResult result = mq.getMySQLResult();
        
        try {
            while (result.queryResult.next()) {
            log += result.queryResult.getString("error");
            log += "\n";
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            return log;
    }
    
    public boolean ClearTable(String table)
    {
        boolean leeg = false;
        try {
            String sql = "TRUNCATE TABLE `" + table + "`";
            MySQLQuery mq = new MySQLQuery(sql, false, this.connection);

        } catch (Exception ex) {
            Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return leeg;
    }
        
    
    public int getSize(String trainid) {   
        int Size = 0;
        String sql = "SELECT COUNT(DISTINCT orderTime) AS tijdCount FROM stats WHERE trainId = '"+ trainid+"' GROUP BY trainId;";
        //String sql = "SELECT orderTime, COUNT(*) AS tijdCount FROM stats WHERE trainId = '"+ trainid+"' GROUP BY trainId;";
        MySQLQuery mq = new MySQLQuery(sql, true, this.connection);
        MySQLResult result = mq.getMySQLResult();

         try {
             if (result.queryResult.first() == false)
             {
                 System.out.println("Tabel is leeg");
             }
             else
             {
                 Size = result.queryResult.getInt("tijdCount");
             }
         }
         catch (SQLException ex)
         {
             Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
         }
         
        return Size;
    }
    
    public int getSize2() {   
        int Size = 0;
        String sql = "SELECT COUNT( id ) AS rowCount FROM reizen";
        
        MySQLQuery mq = new MySQLQuery(sql, true, this.connection);
        MySQLResult result = mq.getMySQLResult();

         try {
             if (result.queryResult.first() == false)
             {
                 System.out.println("Tabel is leeg");
             }
             else
             {
                 Size = result.queryResult.getInt("rowCount");
                 System.out.println(Size);
             }
         }
         catch (SQLException ex)
         {
             Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
         }
         
        return Size;
    }
    
    public int[] getIntData(String kolom)
    {
        int[] data = new int[getSize2()] ;
        
        String sql = "SELECT " + kolom + " FROM reizen";
        MySQLQuery mq = new MySQLQuery(sql, true, this.connection);
        MySQLResult result = mq.getMySQLResult();
        int i = 0;
        try {
            while (result.queryResult.next()) {
            data[i] = result.queryResult.getInt(kolom);
            i++;
            
            if(i == this.getSize2())
                {
                    break;
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return data;
    }
    
    
    public long[] getLongData(String kolom)
    {
        long[] data = new long[getSize2()] ;
        String sql = "SELECT " + kolom + " FROM reizen";
        MySQLQuery mq = new MySQLQuery(sql, true, this.connection);
        MySQLResult result = mq.getMySQLResult();
        int i = 0;
        try {
            while (result.queryResult.next()) {
            data[i] = result.queryResult.getLong(kolom);
            i++;
            
            if(i == this.getSize2())
                {
                    break;
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return data;
    }
    
    
    public int getUserAmount()
    {
    int users = 0;
    String sql = "SELECT COUNT(DISTINCT gebruikersnaam) AS users FROM Gebruikers";
    MySQLQuery mq = new MySQLQuery(sql, true, this.connection);
    MySQLResult result = mq.getMySQLResult();
    try {
             if (result.queryResult.first() == false)
             {
                 System.out.println("Tabel is leeg");
             }
             else
             {
                 users = result.queryResult.getInt("users");
             }
         }
         catch (SQLException ex)
         {
             Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
         }

    return users;
    }
    
    public void deleteUser(String User)
    {
    String sql = "DELETE FROM Gebruikers WHERE gebruikersnaam='" + User + "'";
    MySQLQuery mq = new MySQLQuery(sql, false, this.connection);
    }
    
    public String[] fetchUsers()
    {
    String[] string = new String[getUserAmount()];
    String sql = "SELECT gebruikersnaam FROM Gebruikers";
    MySQLQuery mq = new MySQLQuery(sql, true, this.connection);
    MySQLResult result = mq.getMySQLResult();
    int i = 0;
        try {
            while (result.queryResult.next()) {
            string[i] = result.queryResult.getString("gebruikersnaam");
            i++;
            
            if(i == this.getUserAmount())
                {
                    break;
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    return string;
    }
    
    public String[] fetchTrainids()
    {
        
    String[] string = new String[getTrains()];
    String sql = "SELECT trainId FROM stats GROUP BY trainId";
    MySQLQuery mq = new MySQLQuery(sql, true, this.connection);
    MySQLResult result = mq.getMySQLResult();
    int i = 0;
        try {
            while (result.queryResult.next()) {
            string[i] = result.queryResult.getString("trainId");
            i++;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    return string;
    }
    
    public int getTrains()
    {
    int Size = 0;
    String sql = "SELECT COUNT(DISTINCT trainId) AS trains FROM stats";
    MySQLQuery mq = new MySQLQuery(sql, true, this.connection);
    MySQLResult result = mq.getMySQLResult();
    
    try {
             if (result.queryResult.first() == false)
             {
                 System.out.println("Tabel is leeg");
             }
             else
             {
                 Size = result.queryResult.getInt("trains");
                 System.out.println(Size);
             }
         }
         catch (SQLException ex)
         {
             Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
         }
    
    return Size;
    }
    
    public long[] getTime(String trainid)
    {
    long[] time = new long[this.getSize(trainid)];
    String sql = "SELECT orderTime FROM stats WHERE trainId='"+trainid+"'";
    MySQLQuery mq = new MySQLQuery(sql, true, this.connection);
    MySQLResult result = mq.getMySQLResult();
    try {
             if (result.queryResult.first() == false)
             {
                 System.out.println("Tabel is leeg");
             }
             else
             {   
                 int i = 0;
                 while(result.queryResult.next())
                 {
                 System.out.println(result.queryResult.getLong("orderTime"));
                 time[i] = result.queryResult.getLong("orderTime");
                 i++;
                 }
             }
         }
         catch (SQLException ex)
         {
             Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
         }
    return time;
    }
    
    public long[] getTimeArrival(String trainid)
    {
    long[] time = new long[this.getSize(trainid)];
    String sql = "SELECT arrivalTime FROM stats WHERE trainId='"+trainid+"'";
    MySQLQuery mq = new MySQLQuery(sql, true, this.connection);
    MySQLResult result = mq.getMySQLResult();
    try {
             if (result.queryResult.first() == false)
             {
                 System.out.println("Tabel is leeg");
             }
             
             else
             {   
                 int i = 0;
                 while(result.queryResult.next())
                 {
                 System.out.println(result.queryResult.getLong("arrivalTime"));
                 time[i] = result.queryResult.getLong("arrivalTime");
                 i++;
                 }
             }
         }
         catch (SQLException ex)
         {
             Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
         }
    return time;
    }
    
    public void CreateUser(String gebruikersnaam, String wachtwoord)
    {
    String sql = "INSERT INTO `Gebruikers` (`id`, `gebruikersnaam`, `wachtwoord`, `groepie`) VALUES (NULL, '" + gebruikersnaam + "', '" + wachtwoord + "', '1');";
    MySQLQuery mq = new MySQLQuery(sql, false, this.connection);  
    }
    
    public void UpdateUser(String gebruikersnaam, String wachtwoord, String NG, String NW )
    {
    boolean check = false;
            try {
                    PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Gebruikers WHERE gebruikersnaam=? AND wachtwoord=? LIMIT 0,1" );
                    pstmt.setString(1, gebruikersnaam);
                    pstmt.setString(2, wachtwoord);
                    ResultSet result = pstmt.executeQuery();

                    if (result.first() == true) {
                        check = true;
                    }
                }
                catch (SQLException ex)
             {
                 Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
             }
        if(check == true)
        { 
            String sql = "UPDATE Gebruikers SET gebruikersnaam = '" + NG + "' , wachtwoord='" + NW + "'WHERE gebruikersnaam = '" + gebruikersnaam + "' AND wachtwoord='" + wachtwoord + "'";
            MySQLQuery mq = new MySQLQuery(sql, false, this.connection);
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
                Logger.getLogger (MySQLConnection.class.getName()).log (Level.SEVERE, null, ex);
            }

            // Get rid of this object
            try {
                this.finalize();
            }
            catch (Throwable ex)
            {
                Logger.getLogger (MySQLConnection.class.getName()).log (Level.SEVERE, null, ex);
            }
        }
    }
    
}
