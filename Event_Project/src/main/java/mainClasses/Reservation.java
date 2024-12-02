/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainClasses;

enum ResStatus {
    ACTIVE, CANCELED, COMPLETE
};

/**
 *
 * @author nikos
 */
public class Reservation {

    private int reservation_id;
    private int reservation_tickets;
    private String reservation_date;
    private int reservation_payment_amount;
    private ResStatus reservation_status;
    private String client_username;
    private int event_id;

    public int getReservationID() {
        return this.reservation_id;
    }

    public void setReservationID(int reservation_id) {
        this.reservation_id = reservation_id;
    }

    public int getReservationTickets() {
        return this.reservation_tickets;
    }

    public void setReservationTickets(int reservation_tickets) {
        this.reservation_tickets = reservation_tickets;
    }

    public String getReservationDate() {
        return this.reservation_date;
    }

    public void setReservationDate(String reservation_date) {
        this.reservation_date = reservation_date;
    }

    public int getReservationPaymentAmount() {
        return this.reservation_payment_amount;
    }

    public void setReservationPaymentAmount(int reservation_payment_amount) {
        this.reservation_payment_amount = reservation_payment_amount;
    }

    public ResStatus getReservationStatus() {
        return this.reservation_status;
    }

    public void setReservationStatus(ResStatus reservation_status) {
        this.reservation_status = reservation_status;
    }

    public String getClientUsername() {
        return this.client_username;
    }

    public void setClientUsername(String client_username) {
        this.client_username = client_username;
    }

    public int getEventID() {
        return this.event_id;
    }

    public void setEventID(int event_id) {
        this.event_id = event_id;
    }
}
