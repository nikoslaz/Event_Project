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
public class EventTimePeriod extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONArray reservationsArray = new JSONArray();

        String startDate = request.getParameter("start");
        String endDate = request.getParameter("end");

        if (startDate == null || endDate == null) {
            return;
        }

        String selectQuery = "SELECT reservation_id, client_username, event_id, reservation_date, reservation_payment_amount "
                + "FROM reservations "
                + "WHERE reservation_date BETWEEN '" + startDate + "' AND '" + endDate + "'"
                + " AND reservation_status = 'ACTIVE'";

        try (Connection conn = DB_Connection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(selectQuery)) {

            while (rs.next()) {
                JSONObject reservationObject = new JSONObject();
                reservationObject.put("reservation_id", rs.getInt("reservation_id"));
                reservationObject.put("client_username", rs.getString("client_username"));
                reservationObject.put("event_id", rs.getInt("event_id"));
                reservationObject.put("reservation_date", rs.getString("reservation_date"));
                reservationObject.put("reservation_payment_amount", rs.getDouble("reservation_payment_amount"));

                reservationsArray.put(reservationObject);
            }

            response.setContentType("application/json");
            response.getWriter().write(reservationsArray.toString());
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Database error: " + ex.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }
}
