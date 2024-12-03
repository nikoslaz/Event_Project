/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainClasses;

/**
 *
 * @author nikos
 */
public class Ticket {

    public enum Type {
        REGULAR, VIP, BALCONY
    };

    private int ticket_id;
    private Type ticket_type;
    private int ticket_price;
    private int ticket_availability;
    private int event_id;
    private int reservation_id;

    public int getTicketID() {
        return this.ticket_id;
    }

    public void setTicketID(int ticket_id) {
        this.ticket_id = ticket_id;
    }

    public Type getTicketType() {
        return this.ticket_type;
    }

    public void setTicketType(Type ticket_type) {
        this.ticket_type = ticket_type;
    }

    public int getTicketPrice() {
        return this.ticket_price;
    }

    public void setTicketPrice(int ticket_price) {
        this.ticket_price = ticket_price;
    }

    public int getTicketAvailability() {
        return this.ticket_availability;
    }

    public void setTicketAvailability(int ticket_availability) {
        this.ticket_availability = ticket_availability;
    }

    public int getEventID() {
        return this.event_id;
    }

    public void setEventID(int event_id) {
        this.event_id = event_id;
    }

    public int getReservationID() {
        return this.reservation_id;
    }

    public void setReservationID() {
        this.reservation_id = reservation_id;
    }
}
