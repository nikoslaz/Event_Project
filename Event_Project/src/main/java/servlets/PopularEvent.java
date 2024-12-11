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
public class PopularEvent extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String selectQuery = "SELECT event_id, COUNT(*) AS reservation_count "
                + "FROM reservations WHERE reservation_status = 'ACTIVE' "
                + "GROUP BY event_id ORDER BY reservation_count DESC LIMIT 1";

        try (Connection conn = DB_Connection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(selectQuery)) {

            JSONObject mostPopularEvent = new JSONObject();

            if (rs.next()) {
                int eventId = rs.getInt("event_id");
                int reservationCount = rs.getInt("reservation_count");

                mostPopularEvent.put("event_id", eventId);
                mostPopularEvent.put("reservation_count", reservationCount);
            } else {
                mostPopularEvent.put("message", "No active reservations found");
            }

            response.setContentType("application/json");
            response.getWriter().write(mostPopularEvent.toString());
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Database error: " + ex.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }
}
