package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author RailCab07_4
 */
public class MySQLQuery {
    private String query            = "";
    private Connection con          = null;
    private Statement statement     = null;
    private MySQLResult mysqlResult = null;
    
    public MySQLQuery(String query, boolean result, Connection con)
    {
        if (query.equals ("") || con == null)
        {
            System.out.println ("Geen geldige waarden ingevoerd");
            return;
        }
        this.query  = query;
        this.con    = con;
        
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
    
    public MySQLResult getMySQLResult()
    {
        return this.mysqlResult;
    }
}

class MySQLResult {
    ResultSet queryResult = null;
    
    protected MySQLResult (ResultSet r)
    {
        this.queryResult = r;
    }
}
