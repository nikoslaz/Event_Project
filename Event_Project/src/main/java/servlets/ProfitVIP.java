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
public class ProfitVIP extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // SQL query to calculate the total payment amount for VIP tickets
        String selectQuery = "SELECT SUM(ticket_price) AS total_payment_amount "
                + "FROM tickets WHERE ticket_type = 'VIP' AND ticket_availability = 0";

        try (Connection conn = DB_Connection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(selectQuery)) {

            JSONObject result = new JSONObject();

            // Extract the total payment amount from the result set
            if (rs.next()) {
                double totalPayment = rs.getDouble("total_payment_amount");
                result.put("total_payment_amount", totalPayment);
            } else {
                // If no data is found, set total payment amount to 0
                result.put("total_payment_amount", 0.0);
            }

            response.setContentType("application/json");
            response.getWriter().write(result.toString());
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Database error: " + ex.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }
}
