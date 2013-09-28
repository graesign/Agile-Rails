package business;

import java.sql.*;

/*
 * @version 0.13
 */
public class DbManager {
	
	
	/* Attributes */
	private ResultSet rs;
	private Connection conn = null;
	private String username;
	private String password;
	private String host;
	private String database;

	/* Constructors */
	
	/**
	 * Creates a database manager
	 * @param username
	 * @param password
	 * @param host
	 * @param database
	 */
	public DbManager(String username, String password, String host, String database){
		this.username = username;
		this.password = password;
		this.host = host;
		this.database = database;
		this.connect();
	}
	
	
	/* Methods */

	/**
	 * Method to connect to the database
	 */
	public void connect(){
		try {
			String url = "jdbc:mysql://" + host + "/" + database;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, username, password);
			System.out.println("Database connection established");
		} catch (Exception e){
			System.err.println("Cannot connect to database server" + "\n" + e);
		}
	}
	
	/**
	 * Method to close the connection to the database
	 */
	public void close(){
		if(conn != null){
			try{
				conn.close();
				conn = null;
				System.out.println("Database connection terminated");
			} catch (Exception e){
				System.out.println("Could not close connection to database");
			}
		}
	}
	
	/**
	 * @return resultset of the stations with next station
	 */
	public ResultSet getStations(){
		rs = null;
		try{
			PreparedStatement stmt = null;
			stmt = conn.prepareStatement("SELECT * FROM Station s, NextStation n WHERE s.StationID = n.StationID ");
			rs = stmt.executeQuery();
		}catch(Exception e){
			System.err.println("Could not get Stations: " + e);
		}
		return rs;
	}
	
	/**
	 * Set traveler in database
	 * @param
	 */
	public void setReizigers( String startpunt, String eindpunt, long vertrekTijd, int aantalPersonen){
		PreparedStatement stmt = null;
		
		try {
			stmt = conn.prepareStatement("INSERT INTO Reizigers (ContactNR, Startpunt, Eindpunt, VertrekTijd, AantalPersonen) VALUE (?, ?, ?, ?, ?)");
			stmt.setInt(1, 0);
			stmt.setString(2, startpunt);
			stmt.setString(3, eindpunt);
			stmt.setLong(4, vertrekTijd);
			stmt.setInt(5, aantalPersonen);
			stmt.executeUpdate();
			stmt.close();
		} catch (Exception e) {
			System.err.println("Could not get travelers: " + e);
		}	
	}
	
	/**
	 * Get the travelers from the database
	 * @return List of travelers
	 * @param begin
	 * @param eind
	 */
	public ResultSet getReizigers( long eind ){
		rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM Reizigers WHERE VertrekTijd <= ? ORDER BY VertrekTijd ASC");

			stmt.setLong(1, eind );
			rs= stmt.executeQuery();
			verwijderReizigers( eind );
		} catch (Exception e) {
			System.err.println("Could not get travelers: " + e);
		}	
		return rs;
	}
	
	/**
	 * Removes tavelers from database to prevent that travelers will be allocated to a route again
	 * @param begin
	 * @param eind
	 */
	public void verwijderReizigers( long eind){
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("DELETE FROM Reizigers WHERE VertrekTijd <= ? ORDER BY VertrekTijd ASC");
			stmt.setLong(1, eind);
			stmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("Could not get travelers: " + e);
		}	
	}
	
	public ResultSet getVoorWissels(String stationid){
		PreparedStatement stmt = null;
		rs = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM VoorSensoren WHERE StationID LIKE ?  ORDER BY SensorNR ASC");
			stmt.setString(1, stationid);
			rs = stmt.executeQuery();
		} catch (Exception e) {
			System.err.println("Could not get travelers: " + e);
		}
		return rs;
	}
	
	/**
	 * Gives list of values of time till next sensors of tussenWisselSensor
	 * @param stationid
	 * @return rs
	 */
	public ResultSet getTussenWisselWaarde(String stationid){
		PreparedStatement stmt = null;
		rs = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM TussenWisselSensor WHERE StationID LIKE ?");
			stmt.setString(1, stationid);
			rs = stmt.executeQuery();
		} catch (Exception e) {
			System.err.println("Could not get TussenWissels: " + e);
		}
		return rs;
	}

}
