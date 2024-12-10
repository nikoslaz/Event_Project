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

public class EventTimePeriod extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONArray reservationsArray = new JSONArray(); // JSON array to store reservations

        // Get the time period from the request parameters
        String startDate = request.getParameter("start");
        String endDate = request.getParameter("end");

        // Validate the input
        if (startDate == null || endDate == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Start date and end date are required.");
            return;
        }

        // Construct SQL query
        String selectQuery = "SELECT reservation_id, client_name, event_id, reservation_date, reservation_payment_amount "
                + "FROM reservations "
                + "WHERE reservation_date BETWEEN '" + startDate + "' AND '" + endDate + "'";

        try (Connection conn = DB_Connection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(selectQuery)) {

            // Loop through the results and add them to the JSON array
            while (rs.next()) {
                JSONObject reservationObject = new JSONObject(); // Create a JSON object for each reservation
                reservationObject.put("reservation_id", rs.getInt("reservation_id"));
                reservationObject.put("client_name", rs.getString("client_name"));
                reservationObject.put("event_id", rs.getInt("event_id"));
                reservationObject.put("reservation_date", rs.getString("reservation_date"));
                reservationObject.put("reservation_payment_amount", rs.getDouble("reservation_payment_amount"));

                reservationsArray.put(reservationObject); // Add the JSON object to the array
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
