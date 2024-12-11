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

/**
 *
 * @author nikos, nikoletta, michalis
 */
public class ProfitTimePeriod extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the time period from the request parameters
        String startDate = request.getParameter("start");
        String endDate = request.getParameter("end");

        // Validate the input
        if (startDate == null || endDate == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Start date and end date are required.");
            return;
        }

        // Construct SQL query
        String selectQuery = "SELECT SUM(reservation_payment_amount) AS total_payment_amount "
                + "FROM reservations "
                + "WHERE reservation_date BETWEEN '" + startDate + "' AND '" + endDate + "' "
                + "AND reservation_status = 'ACTIVE'";

        try (Connection conn = DB_Connection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(selectQuery)) {

            JSONObject resultObject = new JSONObject();

            if (rs.next()) {
                int totalPaymentAmount = rs.getInt("total_payment_amount");
                resultObject.put("total_payment_amount", totalPaymentAmount);
            } else {
                resultObject.put("total_payment_amount", 0.0); // Default to 0 if no data
            }

            response.setContentType("application/json");
            response.getWriter().write(resultObject.toString());

        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Database error: " + ex.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }
}
