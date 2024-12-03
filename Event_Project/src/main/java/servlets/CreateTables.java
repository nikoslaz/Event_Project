/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import database.tables.EditEventTable;
import database.tables.EditClientTable;
import database.tables.EditReservationTable;
import database.tables.EditTicketTable;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class CreateTables extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CreateTables.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();

        try {
            EditEventTable editEventTable = new EditEventTable();
            EditClientTable editClientTable = new EditClientTable();
            EditReservationTable editReservationTable = new EditReservationTable();
            EditTicketTable editTicketTable = new EditTicketTable();

            // Attempt to create each table
            editEventTable.createEventTable();
            LOGGER.info("Event table created successfully.");
            editClientTable.createClientsTable();
            LOGGER.info("Clients table created successfully.");
            editReservationTable.createReservationTable();
            LOGGER.info("Reservation table created successfully.");
            editTicketTable.createTicketTable();
            LOGGER.info("Ticket table created successfully.");

            // Success response
            jsonResponse.put("status", "success");
            jsonResponse.put("message", "All tables created successfully.");
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception ex) {
            // Log the exception and send an error response
            LOGGER.log(Level.SEVERE, "Error creating tables", ex);
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "An error occurred while creating tables: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        // Send JSON response
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }
}
