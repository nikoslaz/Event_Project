/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
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

public class Login extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        EditClientTable clientTable = new EditClientTable();
        try {
            JSONObject jsonResponse = new JSONObject();
            Client clientID = clientTable.databaseToClients(username, password);
            

            if (clientID != null) {
                session.setAttribute("username", username);
                session.setAttribute("userId", clientID);
                session.setMaxInactiveInterval(120);

                jsonResponse.put("success", true);
                jsonResponse.put("userType", "client");
                jsonResponse.put("userId",  clientID );
                

                response.setContentType("application/json");
                response.getWriter().write(jsonResponse.toString());
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Invalid credentials");

                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(jsonResponse.toString());
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public String getServletInfo() {
        return "Login servlet for handling user login requests.";
    }
}
