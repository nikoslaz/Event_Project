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
        allTickets = regularTickets + vipTickets + balconyTickets;

        EditTicketTable edit_tick = new EditTicketTable();
        try {
            for (int i = 0; i < regularTickets; i++) {
                edit_tick.updateTicketStatus(0);
            }
            for (int i = 0; i < vipTickets; i++) {
                edit_tick.updateTicketStatus(1);
            }
            for (int i = 0; i < balconyTickets; i++) {
                edit_tick.updateTicketStatus(2);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        Reservation res = new Reservation();
        EditReservationTable edit_res = new EditReservationTable();
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = currentDate.format(formatter);

        res.setReservationTickets(allTickets);
        res.setReservationDate(dateString);
        // Maybe do a function to return each price base on Event_ID from a QUERY !!!
        // This is an example
        res.setReservationPaymentAmount(regularTickets * 10 + vipTickets * 50 + balconyTickets * 20);

        try {
            edit_res.createNewReservation(res);
        } catch (Exception e) {
            System.out.println(e);
        }

        // Respond with JSON
        response.setContentType("application/json");
        response.getWriter().write("{\"status\": \"success\", \"message\": \"Tickets processed successfully!\"}");
    }
}
