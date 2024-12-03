package servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import com.google.gson.Gson;
import database.tables.EditReservationTable;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.Reservation;

public class LoadReservations extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Get the type parameter from the request
        String type = request.getParameter("type");
        if (type == null || type.isEmpty()) {
            // Send an error if the type parameter is missing
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Type parameter is missing");
            return;
        }

        try {
            // Create an instance of EditPetKeepersTable to fetch data
            EditReservationTable editReservations = new EditReservationTable();
            ArrayList<Reservation> rev = editReservations.getReservations(type);

            // Check if the keepers list is empty
            if (rev.isEmpty()) {
                // Respond with 404 Not Found if no keepers are found
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // Convert the list of keepers to JSON
            Gson gson = new Gson();
            String json = gson.toJson(rev);

            // Send the JSON response back to the client
            try (PrintWriter out = response.getWriter()) {
                out.print(json);
            }

            // Set the response status to OK
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (ClassNotFoundException | SQLException e) {
            // Log the exception and send a 500 Internal Server Error response
            Logger.getLogger(LoadReservations.class.getName()).log(Level.SEVERE, null, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching data");
        }
    }
}
