package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import database.tables.EditClientTable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.Client;
import org.json.JSONObject;

/**
 *
 * @author nikos, nikoletta, michalis
 */
public class Login extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get parameters from the request
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();
        EditClientTable clientTable = new EditClientTable();

        // Prepare the JSON response
        JSONObject jsonResponse = new JSONObject();
        response.setContentType("application/json");

        try {
            // Fetch the client from the database
            Client client = clientTable.databaseToClients(username, password);

            if (client != null) {
                System.out.println("Client exists!");
                // Store client details in session
                session.setAttribute("client_username", client.getClientUsername());
                session.setMaxInactiveInterval(120); // Set session timeout to 2 minutes

                // Send success response
                jsonResponse.put("success", true);
                jsonResponse.put("userType", "client");
                response.getWriter().write(jsonResponse.toString());
            } else {
                // Invalid credentials
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Invalid username or password");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(jsonResponse.toString());
            }
        } catch (SQLException | ClassNotFoundException ex) {
            // Log and send an error response
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Internal server error. Please try again later.");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(jsonResponse.toString());
        }
    }
}
