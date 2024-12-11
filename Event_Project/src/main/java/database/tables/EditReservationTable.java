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
import mainClasses.Reservation;

/**
 *
 * @author nikos, nikoletta, michalis
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

    public ArrayList<Reservation> getReservations(String type) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Reservation> reservations = new ArrayList<Reservation>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM reservations");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Reservation rev = gson.fromJson(json, Reservation.class);
                reservations.add(rev);
            }
            return reservations;

        } catch (Exception e) {
            System.err.println("Got an exception! ");

        }
        return null;
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
                + "client_username VARCHAR(50) , "
                + "event_id INTEGER , "
                + "FOREIGN KEY (client_username) REFERENCES clients(client_username), "
                + "FOREIGN KEY (event_id) REFERENCES events(event_id), "
                + "PRIMARY KEY (reservation_id)"
                + ")";
        stmt.execute(sql);
        stmt.close();
        con.close();
    }

    public int createNewReservation(Reservation res) throws ClassNotFoundException {
        int reservationID = -1;
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO reservations "
                    + "(reservation_tickets, reservation_date, reservation_payment_amount, reservation_status, client_username, event_id) "
                    + "VALUES ("
                    + "'" + res.getReservationTickets() + "', "
                    + "'" + res.getReservationDate() + "', "
                    + "'" + res.getReservationPaymentAmount() + "', "
                    + "'ACTIVE',"
                    + "'" + res.getClientUsername() + "',"
                    + "'" + res.getEventID() + "'"
                    + ")";

            stmt.executeUpdate(insertQuery, Statement.RETURN_GENERATED_KEYS);
            // Fetch the auto-incremented key from the result set
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                reservationID = rs.getInt(1); // Get the generated reservation_id
                System.out.println("# Generated Reservation ID: " + reservationID);
            }
            
            System.out.println("# The reservation was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            rs.close();
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditReservationTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return reservationID;
    }

}
