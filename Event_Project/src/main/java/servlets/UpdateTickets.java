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
import database.tables.EditReservationTable;
import mainClasses.Reservation;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        int allTickets;
        int regularTickets = jsonObject.get("regularTickets").getAsInt();
        int vipTickets = jsonObject.get("vipTickets").getAsInt();
        int balconyTickets = jsonObject.get("balconyTickets").getAsInt();
        String clientUsername = jsonObject.get("clientUsername").getAsString();
        int eventID = jsonObject.get("eventID").getAsInt();
        allTickets = regularTickets + vipTickets + balconyTickets;

        EditTicketTable edit_tick = new EditTicketTable();
        int reservationID = -1;
        
        if (allTickets != 0) {

            Reservation res = new Reservation();
            EditReservationTable edit_res = new EditReservationTable();
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateString = currentDate.format(formatter);

            res.setReservationTickets(allTickets);
            res.setReservationDate(dateString);
            res.setReservationPaymentAmount(regularTickets * 10 + vipTickets * 50 + balconyTickets * 20);
            res.setClientUsername(clientUsername);
            res.setEventID(eventID);

            System.out.println(clientUsername);

        try {
            reservationID = edit_res.createNewReservation(res);

            try {
                for (int i = 0; i < regularTickets; i++) {
                    edit_tick.updateTicketStatus(0, reservationID);
                }
                for (int i = 0; i < vipTickets; i++) {
                    edit_tick.updateTicketStatus(1, reservationID);
                }
                for (int i = 0; i < balconyTickets; i++) {
                    edit_tick.updateTicketStatus(2, reservationID);
                }
            } catch (Exception e) {
                System.out.println(e);
            }

        } catch (Exception e) {
            System.out.println(e);
            }
        } else {
            System.out.println("NO TICKETS AVAILABLE");
            // We can put other things here to show the user <3
        }

        // Respond with JSON
        response.setContentType("application/json");
        if (reservationID != -1) {
            response.getWriter().write("{\"status\": \"success\", \"message\": \"Tickets processed successfully!\", \"reservation_id\": " + reservationID + "}");
        } else {
            response.getWriter().write("{\"status\": \"failure\", \"message\": \"Failed to process tickets.\"}");
        }        
        response.getWriter().write("{\"status\": \"success\", \"message\": \"Tickets processed successfully!\"}");
    }
}
