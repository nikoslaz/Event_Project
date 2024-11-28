/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database.tables;

import com.google.gson.Gson;
import database.DB_Connection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.Client;

/**
 *
 * @author nikos
 */
public class EditClientTable {

    public static void addClientFromJSON(String json) throws ClassNotFoundException {
        Client user = jsonToClient(json);
        addNewClient(user);
    }

    public static Client jsonToClient(String json) {
        Gson gson = new Gson();

        Client user = gson.fromJson(json, Client.class);
        return user;
    }

    public static String clientToJSON(Client user) {
        Gson gson = new Gson();

        String json = gson.toJson(user, Client.class);
        return json;
    }

    public static ArrayList<Client> getClients() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Client> clients = new ArrayList<Client>();
        ResultSet rs = null;
        try {
            // Build the query to fetch all clients
            String query = "SELECT * FROM clients";
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Client client = gson.fromJson(json, Client.class);
                clients.add(client);
            }
            return clients;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        } finally {
            // Close the ResultSet, Statement, and Connection
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return null;
    }

    public static void updateClient(String username, int balance) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE petowners SET balance='" + balance + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }

    public static Client databaseToClients(String username, String password) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM clients WHERE username = '" + username + "' AND password='" + password + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Client user = gson.fromJson(json, Client.class);
            return user;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static String databaseClientToJSON(String username, String password) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM clients WHERE username = '" + username + "' AND password='" + password + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            return json;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static void createClientsTable() throws SQLException, ClassNotFoundException {

        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        String createTableQuery = "CREATE TABLE clients ("
                + "client_username VARCHAR(50) NOT NULL UNIQUE, "
                + "client_password VARCHAR(50) NOT NULL, "
                + "client_firstname VARCHAR(50) NOT NULL, "
                + "client_lastname VARCHAR(50) NOT NULL, "
                + "client_email VARCHAR(50) NOT NULL UNIQUE, "
                + "client_balance INTEGER DEFAULT 0, "
                + "card_number INTEGER, "
                + "card_expdate DATE, "
                + "card_csv INTEGER,"
                + "PRIMARY KEY (client_username))";

        try {
            stmt.execute(createTableQuery);
            System.out.println("~Table 'clients' created successfully!");
        } catch (SQLException e) {
            System.err.println("Error while creating type or table: " + e.getMessage());
        } finally {
            stmt.close();
            con.close();
        }
    }

    /**
     * Establish a database connection and add in the database.
     *
     * @param user
     * @throws ClassNotFoundException
     */
    public static void addNewClient(Client user) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO clients (client_username, client_password, client_firstname, client_lastname, client_email, client_balance, client_card) "
                    + "VALUES ("
                    + "'" + user.getClientUsername() + "', "
                    + "'" + user.getClientPassword() + "', "
                    + "'" + user.getClientName() + "', "
                    + "'" + user.getClientLastname() + "', "
                    + "'" + user.getClientEmail() + "', "
                    + user.getClientBalance() + ", "
                    + "ROW("
                    + user.getCardNumber() + ", "
                    + "'" + user.getCardExpDate() + "', "
                    + user.getCardCvv() + ")::card_details"
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The client was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditClientTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        System.out.println("Started main. Creating client table!");
        try {
            createClientsTable();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

}
