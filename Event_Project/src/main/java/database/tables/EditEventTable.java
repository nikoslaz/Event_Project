/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database.tables;

import com.google.gson.Gson;
import mainClasses.Event;
import database.DB_Connection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nikos
 */
public class EditEventTable {

    public void addEventFromJSON(String json) throws ClassNotFoundException {
        Event r = jsonToEvent(json);
        createNewEvent(r);
    }

    public Event databaseToEvent(int id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM events WHERE event_id= '" + id + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Event bt = gson.fromJson(json, Event.class);
            return bt;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Event jsonToEvent(String json) {
        Gson gson = new Gson();
        Event r = gson.fromJson(json, Event.class);
        return r;
    }

    public String eventToJSON(Event e) {
        Gson gson = new Gson();

        String json = gson.toJson(e, Event.class);
        return json;
    }

    public ArrayList<Event> getEvents(String type) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Event> events = new ArrayList<Event>();
        ResultSet rs = null;
        try {
            // Build the query to fetch all clients
            String query = "SELECT * FROM events";
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Event ev = gson.fromJson(json, Event.class);
                events.add(ev);
            }
            return events;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        } finally {
            // Close the ResultSet, Statement, and Connection
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return null;
    }

    public void updateEvent(int eventID, String status) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String updateQuery = "UPDATE events SET status='" + status + "' WHERE event_id= '" + eventID + "'";
        stmt.executeUpdate(updateQuery);
        stmt.close();
        con.close();
    }

    public  void createEventTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE events ("
                + "event_id INTEGER NOT NULL AUTO_INCREMENT, "
                + "event_name VARCHAR(50) NOT NULL, "
                + "event_date DATE NOT NULL, "
                + "event_time TIME NOT NULL, "
                + "event_type ENUM('CONCERT', 'PERFORMANCE', 'COMEDYNIGHT') NOT NULL, "
                + "event_capacity INTEGER NOT NULL, "
                + "event_status ENUM('SCHEDULED', 'CANCELED', 'COMPLETED') NOT NULL, "
                + "PRIMARY KEY (event_id))";
        stmt.execute(sql);
        stmt.close();
        con.close();

    }

    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void createNewEvent(Event ev) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " events (event_id,event_name,event_date,event_time,event_type,event_capacity,event_status)"
                    + " VALUES ("
                    + "'" + ev.getEventId() + "',"
                    + "'" + ev.getEventName() + "',"
                    + "'" + ev.getEventDate() + "',"
                    + "'" + ev.getEventTime() + "',"
                    + "'" + ev.getEventType() + "',"
                    + "'" + ev.getEventCapacity() + "',"
                    + "'" + ev.getEventStatus() + "'"
                    + ")";
            //stmt.execute(table);

            stmt.executeUpdate(insertQuery);
            System.out.println("# The event was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditEventTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        EditEventTable edit = new EditEventTable();
        try {
            edit.createEventTable();
            System.out.println("Reservation table created successfully.");
        } catch (Exception e) {
            // Handle any exception that occurs
            System.err.println("An error occurred while creating the reservation table: " + e.getMessage());
            e.printStackTrace(); // Optional: Print the full stack trace for debugging
        }
    }

}
