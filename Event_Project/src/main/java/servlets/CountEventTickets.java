/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import database.tables.EditTicketTable;
import java.io.BufferedReader;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CountEventTickets extends HttpServlet {

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
        String jsonData = sb.toString();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);

        int eventId = jsonObject.get("eventId").getAsInt();
        System.out.println("Received Event ID: " + eventId);

        try {
            EditTicketTable ett = new EditTicketTable();
            int regularCount = ett.getRegularCount(eventId);
            int vipCount = ett.getVIPCount(eventId);
            int balconyCount = ett.getBalconyCount(eventId);

            response.setContentType("application/json");
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("regular", regularCount);
            responseJson.addProperty("vip", vipCount);
            responseJson.addProperty("balcony", balconyCount);
            response.getWriter().write(responseJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Failed to fetch regular ticket count\"}");
        }
    }
}
