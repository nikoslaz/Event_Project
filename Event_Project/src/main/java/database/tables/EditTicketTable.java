/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
 * @author nikos
 */
public class EditTicketTable {

    public void addTicketFromJSON(String json) throws ClassNotFoundException {
        Ticket ticket = jsonToTicket(json);
        createNewTicket(ticket);
    }

    public Ticket jsonToTicket(String json) {
        Gson gson = new Gson();
        Ticket ticket = gson.fromJson(json, Ticket.class);
        return ticket;
    }

    public ArrayList<Ticket> databaseToTicket(int ticket_ID) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM tickets WHERE ticket_id= '" + ticket_ID + "'");
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

    public void createTicketTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE tickets "
                + "(ticket_id INTEGER NOT NULL AUTO_INCREMENT, "
                + "ticket_type ENUM('REGULAR', 'VIP', 'BALCONY') NOT NULL, "
                + "ticket_price DECIMAL(10, 2) NOT NULL, "
                + "ticket_availability BOOLEAN NOT NULL, "
                + "event_id INTEGER NOT NULL, "
                + "reservation_id INTEGER NOT NULL, "
                + "FOREIGN KEY (event_id) REFERENCES events(event_id), "
                + "FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id), "
                + "PRIMARY KEY (ticket_id))";

        stmt.execute(sql);
        stmt.close();
        con.close();
    }

    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void createNewTicket(Ticket tick) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO tickets (ticket_id, ticket_type, ticket_price, ticket_availability) "
                    + "VALUES ("
                    + tick.getTicketID() + ", "
                    + "'" + tick.getTicketType() + "', "
                    + tick.getTicketPrice() + ", "
                    + (tick.isTicketAvailability() ? "Available" : "Not Available")
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The ticket was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditTicketTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
