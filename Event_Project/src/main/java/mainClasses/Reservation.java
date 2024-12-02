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
    private int reservation_payment_amount;
    private String reservation_date;
    private ResStatus reservation_status;

    public int getReservationID() {
        return reservation_id;
    }

    public void setReservationID(int reservation_id) {
        this.reservation_id = reservation_id;
    }

    public int getReservationTickets() {
        return reservation_tickets;
    }

    public void setReservationTickets(int reservation_tickets) {
        this.reservation_tickets = reservation_tickets;
    }

    public int getReservationPaymentAmount() {
        return reservation_payment_amount;
    }

    public void setReservationPaymentAmount(int reservation_payment_amount) {
        this.reservation_payment_amount = reservation_payment_amount;
    }

    public String getReservationDate() {
        return reservation_date;
    }

    public void setReservationDate(String reservation_date) {
        this.reservation_date = reservation_date;
    }

    public ResStatus getReservationStatus() {
        return reservation_status;
    }

    public void setReservationStatus(ResStatus reservation_status) {
        this.reservation_status = reservation_status;
    }
}
