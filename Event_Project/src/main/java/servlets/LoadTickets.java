package servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import database.DB_Connection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoadTickets extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONArray ticketArray = new JSONArray();
        String query = "SELECT * FROM tickets";

        try (Connection conn = DB_Connection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                JSONObject ticket = new JSONObject();
                ticket.put("ticket_id", rs.getInt("ticket_id"));
                ticket.put("ticket_type", rs.getString("ticket_type"));
                ticket.put("ticket_price", rs.getInt("ticket_price"));
                ticket.put("ticket_availability", rs.getInt("ticket_availability"));
                ticket.put("event_id", rs.getInt("event_id"));
                ticket.put("reservation_id", rs.getInt("reservation_id"));

                ticketArray.put(ticket);
            }

            response.setContentType("application/json");
            response.getWriter().write(ticketArray.toString());
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Database error: " + ex.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }
}
