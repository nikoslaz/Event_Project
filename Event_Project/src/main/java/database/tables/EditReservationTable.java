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
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.Reservation;

/**
 *
 * @author nikos
 */
public class EditReservationTable {

    public void addReservationFromJSON(String json) throws ClassNotFoundException {
        Reservation r = jsonToReservation(json);
        createNewReservation(r);
    }

    public Reservation databaseToReservation(int id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM reservations WHERE reservation_id= '" + id + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Reservation bt = gson.fromJson(json, Reservation.class);
            return bt;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Reservation jsonToReservation(String json) {
        Gson gson = new Gson();
        Reservation r = gson.fromJson(json, Reservation.class);
        return r;
    }

    public String ReservationToJSON(Reservation r) {
        Gson gson = new Gson();

        String json = gson.toJson(r, Reservation.class);
        return json;
    }

    public void updateReservation(int reservationID, String status) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String updateQuery = "UPDATE reservations SET status='" + status + "' WHERE reservation_id= '" + reservationID + "'";
        stmt.executeUpdate(updateQuery);
        stmt.close();
        con.close();
    }

    public void createReservationTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE reservations ("
                + "reservation_id INTEGER NOT NULL AUTO_INCREMENT, "
                + "reservation_tickets INTEGER NOT NULL, "
                + "reservation_date DATE NOT NULL, "
                + "reservation_payment_amount INTEGER NOT NULL, "
                + "reservation_status ENUM('ACTIVE', 'CANCELED', 'COMPLETE') NOT NULL, "
                + "client_username VARCHAR(50) NOT NULL, "
                + "event_id INTEGER NOT NULL, "
                + "FOREIGN KEY (client_username) REFERENCES clients(client_username), "
                + "FOREIGN KEY (event_id) REFERENCES events(event_id), "
                + "PRIMARY KEY (reservation_id)"
                + ")";
        stmt.execute(sql);
        stmt.close();
        con.close();
    }

    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void createNewReservation(Reservation res) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO reservations "
                    + "(reservation_tickets, reservation_date, reservation_payment_amount, reservation_status, client_username, event_ID) "
                    + "VALUES ("
                    + "'" + res.getReservationTickets() + "', "
                    + "'" + res.getReservationDate() + "', "
                    + "'" + res.getReservationPaymentAmount() + "', "
                    + "'ACTIVE',"
                    + "'" + res.getClientUsername() + "',"
                    + "'" + res.getEventID() + "')";
            //stmt.execute(table);

            stmt.executeUpdate(insertQuery);
            System.out.println("# The reservation was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditReservationTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
