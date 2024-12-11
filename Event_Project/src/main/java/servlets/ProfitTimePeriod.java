package servlets;

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
import org.json.JSONArray;

/**
 *
 * @author nikos, nikoletta, michalis
 */
public class ProfitTimePeriod extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONArray reservationsArray = new JSONArray(); // JSON array to store reservations

        // Get the time period from the request parameters
        String startDate = request.getParameter("start");
        String endDate = request.getParameter("end");

        // Validate the input
        if (startDate == null || endDate == null) {
            return;
        }

        // Construct SQL query
        String highestProfitEventQuery = "SELECT event_id, SUM(reservation_payment_amount) AS total_profit "
                + "FROM reservations "
                + "WHERE reservation_date BETWEEN '" + startDate + "' AND '" + endDate + "' "
                + "AND reservation_status = 'ACTIVE' "
                + "GROUP BY event_id "
                + "ORDER BY total_profit DESC "
                + "LIMIT 1";

        try (Connection conn = DB_Connection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(highestProfitEventQuery)) {

            // Loop through the results and add them to the JSON array
            while (rs.next()) {
                JSONObject reservationObject = new JSONObject(); // Create a JSON object for each reservation
                reservationObject.put("event_id", rs.getInt("event_id"));
                reservationObject.put("total_profit", rs.getString("total_profit"));

                reservationsArray.put(reservationObject);
            }

            // Set the response type to JSON and write the JSON array to the response
            response.setContentType("application/json");
            response.getWriter().write(reservationsArray.toString());
        } catch (SQLException | ClassNotFoundException ex) {
            // Handle any database or connection errors
            System.err.println("Database error: " + ex.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }
}
