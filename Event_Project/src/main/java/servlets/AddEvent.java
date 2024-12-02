/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import database.tables.EditClientTable;
import database.tables.EditEventTable;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.Event;
import mainClasses.Event.EventStatus;
import mainClasses.Event.EventType;
import org.json.JSONObject;

/**
 *
 * @author nikos
 */
public class AddEvent extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPost registration");
        // Parse JSON data from event
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

        Event event = new Event();
        event.setEventId(jsonObject.getInt("event_id"));
        event.setEventName(jsonObject.getString("event_name"));
        event.setEventDate(jsonObject.getString("event_date"));
        event.setEventTime(jsonObject.getString("event_time"));
        String eventTypeString = jsonObject.getString("event_type");
        EventType eventType = EventType.valueOf(eventTypeString.toUpperCase()); // Convert String to Enum
        event.setEventType(eventType);
        event.setEventCapacity(jsonObject.getInt("event_capacity"));
        String eventStatusString = jsonObject.getString("event_status");
        EventStatus eventStatus = EventStatus.valueOf(eventStatusString.toUpperCase()); // Convert String to Enum
        event.setEventStatus(eventStatus);

        EditEventTable editEventTable = new EditEventTable();
        JSONObject jsonResponse = new JSONObject();

        try {
            // Check if user already exists in the database
            if (editEventTable.databaseToEvent(event.getEventId() )!= null) {
                // User already exists, send error response
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "User with provided username or password already exists.");
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                editEventTable.createNewEvent(event);
                System.out.println("Event added");
                response.setStatus(HttpServletResponse.SC_OK);
                jsonResponse.put("status", "success");
                jsonResponse.put("message", "Registration successful.");
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (SQLException ex) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Database error: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Logger.getLogger(AddEvent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Class not found error: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Logger.getLogger(AddEvent.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Send JSON response
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }

}

