package database.tables;

import com.google.gson.Gson;
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
 * @author nikos, nikoletta, michalis
 */

public class EditTicketTable {

    public static class Regulars {
        public String title;
        public int REGULARCOUNT;
    };

    public void addTicketFromJSON(String json) throws ClassNotFoundException {
        Ticket ticket = jsonToTicket(json);
        createNewTicket(ticket);
    }

    public Ticket jsonToTicket(String json) {
        Gson gson = new Gson();
        Ticket ticket = gson.fromJson(json, Ticket.class);
        return ticket;
    }

    public ArrayList<Ticket> getTickets(String type) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM tickets");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Ticket tick = gson.fromJson(json, Ticket.class);
                tickets.add(tick);
            }
            return tickets;

        } catch (Exception e) {
            System.err.println("Got an exception! ");

        }
        return null;
    }

    public int getRegularCount(int event_id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT COUNT(ticket_id) AS REGULARCOUNT FROM tickets WHERE event_id=" + event_id + " AND ticket_availability=1 AND ticket_type='REGULAR'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            EditTicketTable.Regulars bt = gson.fromJson(json, EditTicketTable.Regulars.class);
            return bt.REGULARCOUNT;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return -1;
    }

    public int getVIPCount(int event_id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT COUNT(ticket_id) AS REGULARCOUNT FROM tickets WHERE event_id=" + event_id + " AND ticket_availability=1 AND ticket_type='VIP'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            EditTicketTable.Regulars bt = gson.fromJson(json, EditTicketTable.Regulars.class);
            return bt.REGULARCOUNT;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return -1;
    }

    public int getBalconyCount(int event_id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT COUNT(ticket_id) AS REGULARCOUNT FROM tickets WHERE event_id=" + event_id + " AND ticket_availability=1 AND ticket_type='BALCONY'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            EditTicketTable.Regulars bt = gson.fromJson(json, EditTicketTable.Regulars.class);
            return bt.REGULARCOUNT;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return -1;
    }

    public void updateTicketStatus(int type, int res_id, int event_id) throws SQLException, ClassNotFoundException {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = DB_Connection.getConnection();
            stmt = con.createStatement();

            // Query to select the first tickets with ticket_availability = 1
            if (type == 0) {
                String selectQuery = "SELECT ticket_id FROM Tickets WHERE ticket_availability = 1 AND ticket_type = 'REGULAR' AND event_id = " + event_id + " LIMIT 1";
                rs = stmt.executeQuery(selectQuery);
            } else if (type == 1) {
                String selectQuery = "SELECT ticket_id FROM Tickets WHERE ticket_availability = 1 AND ticket_type = 'VIP' AND event_id = " + event_id + " LIMIT 1";
                rs = stmt.executeQuery(selectQuery);
            } else if (type == 2) {
                String selectQuery = "SELECT ticket_id FROM Tickets WHERE ticket_availability = 1 AND ticket_type = 'BALCONY' AND event_id = " + event_id + " LIMIT 1";
                rs = stmt.executeQuery(selectQuery);
            }

            if (rs.next()) {
                int ticketId = rs.getInt("ticket_id");

                String updateQuery = "UPDATE Tickets " + "SET ticket_availability = 0, reservation_id = " + res_id + " WHERE ticket_id = " + ticketId;
                int rowsAffected = stmt.executeUpdate(updateQuery);

                if (rowsAffected > 0) {
                    System.out.println("Successfully updated ticket with ID: " + ticketId);
                } else {
                    System.out.println("No rows were updated.");
                }
            } else {
                System.out.println("No available tickets found.");
            }
        } finally {
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
    }

    public void createTicketTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE tickets "
                + "(ticket_id INTEGER NOT NULL AUTO_INCREMENT, "
                + "ticket_type ENUM('REGULAR', 'VIP', 'BALCONY') NOT NULL, "
                + "ticket_price DECIMAL(10, 2) NOT NULL, "
                + "ticket_availability INTEGER NOT NULL, "
                + "event_id INTEGER NOT NULL, "
                + "reservation_id INTEGER NULL, "
                + "FOREIGN KEY (event_id) REFERENCES events(event_id), "
                + "FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id), "
                + "PRIMARY KEY (ticket_id))";

        stmt.execute(sql);
        stmt.close();
        con.close();
    }

    public void createNewTicket(Ticket tick) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO tickets (ticket_type, ticket_price, ticket_availability, event_id) "
                    + "VALUES ("
                    + "'" + tick.getTicketType() + "', "
                    + "'" + tick.getTicketPrice() + "', "
                    + "'" + tick.getTicketAvailability() + "', "
                    + "'" + tick.getEventID() + "'"
                    + ")";

            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The ticket was successfully added in the database.");

            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditTicketTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
