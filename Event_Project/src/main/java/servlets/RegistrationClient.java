/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.Client;
import database.tables.EditClientTable;
import jakarta.servlet.http.HttpServlet;
import org.json.JSONObject;

/**
 *
 * @author nikos
 */
public class RegistrationClient extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPost registration");
        // Parse JSON data from client
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request data");
            return;
        }

        JSONObject jsonObject = new JSONObject(sb.toString());
        System.out.println(jsonObject);

        Client client = new Client();
        client.setClientUsername(jsonObject.getString("client_username"));
        client.setClientName(jsonObject.getString("client_firstname"));
        client.setClientLastname(jsonObject.getString("client_lastname"));
        client.setClientEmail(jsonObject.getString("client_email"));
        client.setClientBalance(jsonObject.getInt("client_balance"));
        client.setCardNumber(jsonObject.getString("card_number"));
        client.setCardExpDate(jsonObject.getString("card_expdate"));
        client.setCardCvv(jsonObject.getString("card_cvv"));

        EditClientTable editClientTable = new EditClientTable();
        JSONObject jsonResponse = new JSONObject();

        try {
            // Check if user already exists in the database
            if (editClientTable.databaseToClients(client.getClientUsername(), client.getClientPassword()) != null) {
                // User already exists, send error response
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "User with provided username or password already exists.");
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                editClientTable.addNewClient(client);
                response.setStatus(HttpServletResponse.SC_OK);
                jsonResponse.put("status", "success");
                jsonResponse.put("message", "Registration successful.");
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (SQLException ex) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Database error: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Logger.getLogger(RegistrationClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Class not found error: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Logger.getLogger(RegistrationClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Send JSON response
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
}
