package servlets;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import database.DB_Connection;

/**
 *
 * @author nikos, nikoletta, michalis
 */
public class ShowEventsProfit extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONArray eventsArray = new JSONArray(); // JSON array to store event profits

        // SQL query to calculate the total payment amount for each event
        String selectQuery = "SELECT event_id, SUM(reservation_payment_amount) AS total_payment_amount "
                + "FROM reservations WHERE reservation_status = 'ACTIVE' GROUP BY event_id";

        try (Connection conn = DB_Connection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(selectQuery)) {

            // Loop through the results and add them to the JSON array
            while (rs.next()) {
                JSONObject eventObject = new JSONObject(); // Create a JSON object for each event
                int eventId = rs.getInt("event_id");
                double totalPayment = rs.getDouble("total_payment_amount");

                eventObject.put("event_id", eventId);
                eventObject.put("total_payment_amount", totalPayment);

                eventsArray.put(eventObject); // Add the JSON object to the array
            }

            // Set the response type to JSON and write the JSON array to the response
            response.setContentType("application/json");
            response.getWriter().write(eventsArray.toString());
        } catch (SQLException | ClassNotFoundException ex) {
            // Handle any database or connection errors
            System.err.println("Database error: " + ex.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }
}
