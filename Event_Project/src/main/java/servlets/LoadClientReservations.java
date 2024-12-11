package servlets;

import database.DB_Connection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author nikos, nikoletta, michalis
 */
public class LoadClientReservations extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");

        if (username == null || username.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username parameter is missing or invalid.");
            return;
        }

        JSONArray clientsArray = new JSONArray();
        String query = "SELECT * FROM reservations WHERE client_username='" + username + "' AND reservation_status='ACTIVE'";

        try (Connection conn = DB_Connection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                JSONObject reserv = new JSONObject();
                reserv.put("reservation_id", rs.getInt("reservation_id"));
                reserv.put("event_id", rs.getInt("event_id"));
                reserv.put("reservation_tickets", rs.getInt("reservation_tickets"));
                reserv.put("reservation_date", rs.getString("reservation_date"));
                reserv.put("reservation_payment_amount", rs.getInt("reservation_payment_amount"));
                clientsArray.put(reserv);
            }

            response.setContentType("application/json");
            response.getWriter().write(clientsArray.toString());
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Database error: " + ex.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }
}
