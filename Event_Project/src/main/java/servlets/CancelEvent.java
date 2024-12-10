package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import database.tables.EditEventTable;

public class CancelEvent extends HttpServlet {

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
        int eventID = jsonObject.get("eventID").getAsInt();

        EditEventTable ev = new EditEventTable();
        try {
            ev.updateEvent(eventID, "CANCELLED");
        } catch (Exception e) {
            System.out.println(e);
        }


        // Respond with JSON
        response.setContentType("application/json");
        response.getWriter().write("{\"status\": \"success\", \"message\": \"Event cancelled successfully!\"}");
    }
}
