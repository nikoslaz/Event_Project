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

    private int reservationID;
    private int reservationTickets;
    private int reservationPaymentAmount;
    private String reservationDate;
    private ResStatus reservationStatus;

    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public int getReservationTickets() {
        return reservationTickets;
    }

    public void setReservationTickets(int reservationTickets) {
        this.reservationTickets = reservationTickets;
    }

    public int getReservationPaymentAmount() {
        return reservationPaymentAmount;
    }

    public void setReservationPaymentAmount(int reservationPaymentAmount) {
        this.reservationPaymentAmount = reservationPaymentAmount;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public ResStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(ResStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
}
