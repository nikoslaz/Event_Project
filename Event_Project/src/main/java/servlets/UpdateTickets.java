/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import database.tables.EditTicketTable;

public class UpdateTickets extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        // Parse JSON using GSON
        String jsonData = sb.toString();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);

        // Extract values from JSON
        int regularTickets = jsonObject.get("regularTickets").getAsInt();
        int vipTickets = jsonObject.get("vipTickets").getAsInt();
        int balconyTickets = jsonObject.get("balconyTickets").getAsInt();

        EditTicketTable edit = new EditTicketTable();
        try {
            for (int i = 0; i < regularTickets; i++) {
                edit.updateTicketStatus(0);
            }
            for (int i = 0; i < vipTickets; i++) {
                edit.updateTicketStatus(1);
            }
            for (int i = 0; i < balconyTickets; i++) {
                edit.updateTicketStatus(2);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        response.setContentType("application/json");
        response.getWriter().write("{\"status\": \"success\", \"message\": \"Tickets processed successfully!\"}");
    }
}
