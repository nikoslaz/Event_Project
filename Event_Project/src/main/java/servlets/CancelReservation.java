package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import database.DB_Connection;
import java.io.BufferedReader;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import database.tables.EditReservationTable;
import database.tables.EditTicketTable;
import java.sql.Connection;
import java.sql.Statement;
import mainClasses.Reservation;

public class CancelReservation extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        // Parse JSON using GSON
        String jsonData = sb.toString();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);

        // Extract values from JSON
        int reservationID = jsonObject.get("reservation_ID").getAsInt();
        EditReservationTable edit_res = new EditReservationTable();
        EditTicketTable edit_tick = new EditTicketTable();

        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            Reservation res = edit_res.databaseToReservation(reservationID);
            String username = res.getClientUsername();

            String updateClientQuery = "UPDATE clients "
                    + "SET client_balance = client_balance + " + (int) (res.getReservationPaymentAmount() * 0.8)
                    + " WHERE client_username = '" + username + "'";
            stmt.executeUpdate(updateClientQuery);
            System.out.println("# Updated client balance for username: " + res.getClientUsername());

            String updateResQuery = "UPDATE reservations "
                    + "SET reservation_status = 'CANCELED' "
                    + "WHERE reservation_id =" + reservationID;
            stmt.executeUpdate(updateResQuery);
            System.out.println("# Updated reservation status for reservation: " + reservationID);

            String updateTicketQuery = "UPDATE Tickets " + "SET ticket_availability = 1, reservation_id=NULL WHERE reservation_id = " + reservationID;
            stmt.executeUpdate(updateTicketQuery);
            System.out.println("# Updated ticket status for reservation: " + reservationID);

        } catch (Exception e) {
            System.out.println(e);
        }

        // Respond with JSON
        response.setContentType("application/json");
        response.getWriter().write("{\"status\": \"success\", \"message\": \"Event cancelled successfully!\"}");
    }
}
