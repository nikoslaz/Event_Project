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

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();
        EditClientTable clientTable = new EditClientTable();

        JSONObject jsonResponse = new JSONObject();
        response.setContentType("application/json");

        try {
            Client client = clientTable.databaseToClients(username, password);

            if (client != null) {
                System.out.println("Client exists!");
                session.setAttribute("client_username", client.getClientUsername());
                session.setMaxInactiveInterval(120);

                jsonResponse.put("success", true);
                jsonResponse.put("userType", "client");
                response.getWriter().write(jsonResponse.toString());
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Invalid username or password");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(jsonResponse.toString());
            }
        } catch (SQLException | ClassNotFoundException ex) {

            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Internal server error. Please try again later.");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(jsonResponse.toString());
        }
    }
}
