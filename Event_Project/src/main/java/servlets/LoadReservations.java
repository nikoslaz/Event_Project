package servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import database.DB_Connection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import mainClasses.Reservation.ResStatus;
import org.json.JSONArray;
import org.json.JSONObject;


public class LoadReservations extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONArray reservationArray = new JSONArray();
        String query = "SELECT * FROM reservations";

        try (Connection conn = DB_Connection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                JSONObject reservation = new JSONObject();
                reservation.put("reservation_id", rs.getInt("reservation_id"));
                reservation.put("reservation_tickets", rs.getInt("reservation_tickets"));
                reservation.put("reservation_date", rs.getDate("reservation_date").toString());
                reservation.put("reservation_payment_amount", rs.getInt("reservation_payment_amount"));
                reservation.put("reservation_status", ResStatus.valueOf(rs.getString("reservation_status")));
                reservation.put("client_username", rs.getString("client_username"));
                reservation.put("event_id", rs.getInt("event_id"));
                
                reservationArray.put(reservation);
            }

            response.setContentType("application/json");
            response.getWriter().write(reservationArray.toString());
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Database error: " + ex.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }
}
