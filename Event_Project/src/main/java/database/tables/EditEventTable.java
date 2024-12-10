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
import mainClasses.Ticket;

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

    public Event getMaxEventID() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM events WHERE event_id= (SELECT MAX(event_id) FROM events)");
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

    public void updateEvent(int eventID) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String updateQuery = "UPDATE events SET event_status = 'CANCELED' WHERE event_id= '" + eventID + "'";
        stmt.executeUpdate(updateQuery);
        stmt.close();
        con.close();
    }

    public void returnPaymentAmount(int eventID) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        try {
            // Query to fetch client username and payment amount for the given event ID
            String selectQuery = "SELECT client_username, SUM(reservation_payment_amount) AS total_payment_amount "
                    + "FROM reservations WHERE event_id = '" + eventID + "' GROUP BY client_username";

            String username = "";
            int paymentAmount = 0;
            int totalPaymentAmount = 0;

            // Execute the query and get the result
            ResultSet rs = stmt.executeQuery(selectQuery);
            while (rs.next()) {
                username = rs.getString("client_username"); // Get the client username
                paymentAmount = rs.getInt("total_payment_amount"); // Get the sum
                totalPaymentAmount += paymentAmount;
            }

            // If no results were found, log and return
            if (username.isEmpty()) {
                System.out.println("No reservation found for event ID: " + eventID);
                return;
            }

            // Update the client's balance
            String updateClientQuery = "UPDATE clients "
                    + "SET client_balance = client_balance + " + totalPaymentAmount
                    + " WHERE client_username = '" + username + "'";
            stmt.executeUpdate(updateClientQuery);
            System.out.println("# Updated client balance for username: " + username);

            // Update the reservation status
            String updateResQuery = "UPDATE reservations "
                    + "SET reservation_status = 'CANCELED', reservation_tickets = 0 "
                    + "WHERE client_username = '" + username + "' AND event_id = '" + eventID + "'";
            stmt.executeUpdate(updateResQuery);
            System.out.println("# Updated reservation status for username: " + username);

            // Update all tickets associated with the event ID
            String updateTicketQuery = "DELETE FROM tickets "
                    + "WHERE event_id = " + eventID;
            stmt.executeUpdate(updateTicketQuery);
            System.out.println("# Updated ticket availability for event ID: " + eventID);

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            stmt.close();
            con.close();
        }
    }


    public void createEventTable() throws SQLException, ClassNotFoundException {
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
                    + " events (event_name, event_date, event_time, event_type, event_capacity, event_status)"
                    + " VALUES ("
                    + "'" + ev.getEventName() + "',"
                    + "'" + ev.getEventDate() + "',"
                    + "'" + ev.getEventTime() + "',"
                    + "'" + ev.getEventType() + "',"
                    + "'" + ev.getEventCapacity() + "',"
                    + "'" + ev.getEventStatus() + "')";

            stmt.executeUpdate(insertQuery);
            System.out.println("# The event was successfully added in the database.");
            stmt.close();

            EditEventTable eventab = new EditEventTable();
            int eventid = eventab.getMaxEventID().getEventId();
            System.out.println("# Creating event " + eventid + " tickets.");
            EditTicketTable ticktab = new EditTicketTable();
            Ticket tick = new Ticket();
            tick.setTicketAvailability(1);
            tick.setEventID(eventid);
            for (int i = 0; i < ev.getEventCapacity(); i++) {
                if (i < 5) {
                    tick.setTicketType(Ticket.Type.VIP);
                    tick.setTicketPrice(50);
                } else if (i < ev.getEventCapacity() / 2) {
                    tick.setTicketType(Ticket.Type.BALCONY);
                    tick.setTicketPrice(20);
                } else {
                    tick.setTicketType(Ticket.Type.REGULAR);
                    tick.setTicketPrice(10);
                }
                ticktab.createNewTicket(tick);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EditEventTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
