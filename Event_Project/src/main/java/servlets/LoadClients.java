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

/**
 *
 * @author nikos, nikoletta, michalis
 */
public class LoadClients extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONArray clientsArray = new JSONArray();
        String query = "SELECT * FROM clients";

        try (Connection conn = DB_Connection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                JSONObject client = new JSONObject();
                client.put("client_username", rs.getString("client_username"));
                client.put("client_password", rs.getString("client_password"));
                client.put("client_firstname", rs.getString("client_firstname"));
                client.put("client_lastname", rs.getString("client_lastname"));
                client.put("client_email", rs.getString("client_email"));
                client.put("client_phone", rs.getInt("client_phone"));
                client.put("client_balance", rs.getInt("client_balance"));
                client.put("card_number", rs.getInt("card_number"));
                client.put("card_expdate", rs.getDate("card_expdate").toString());
                client.put("card_cvv", rs.getInt("card_cvv"));

                clientsArray.put(client);
            }

            response.setContentType("application/json");
            response.getWriter().write(clientsArray.toString());
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Database error: " + ex.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }
}
