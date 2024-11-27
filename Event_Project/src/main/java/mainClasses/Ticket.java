/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainClasses;

enum Type {
    REGULAR, VIP, BALCONY
};

/**
 *
 * @author nikos
 */
public class Ticket {

    private int ticketID;
    private int ticketPrice;
    private Type ticketType;
    private boolean ticketAvailability;

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Type getTicketType() {
        return ticketType;
    }

    public void setTicketType(Type ticketType) {
        this.ticketType = ticketType;
    }

    public boolean isTicketAvailability() {
        return ticketAvailability;
    }

    public void setTicketAvailability(boolean ticketAvailability) {
        this.ticketAvailability = ticketAvailability;
    }
}
