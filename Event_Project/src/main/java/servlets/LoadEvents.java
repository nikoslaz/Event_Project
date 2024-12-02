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

public class LoadEvents extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONArray eventsArray = new JSONArray();
        String query = "SELECT * FROM events WHERE event_status = 'SCHEDULED'";

        try (Connection conn = DB_Connection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                JSONObject event = new JSONObject();
                event.put("event_id", rs.getInt("event_id"));
                event.put("event_name", rs.getString("event_name"));
                event.put("event_date", rs.getDate("event_date").toString());
                event.put("event_time", rs.getTime("event_time").toString());
                event.put("event_type", rs.getString("event_type"));
                event.put("event_capacity", rs.getInt("event_capacity"));
                event.put("event_status", rs.getString("event_status"));

                eventsArray.put(event);
            }

            response.setContentType("application/json");
            response.getWriter().write(eventsArray.toString());
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Database error: " + ex.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }
}
