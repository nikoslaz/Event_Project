/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainClasses;

enum Status {
    SCHEDULED, CANCELED, COMPLETED
}

enum Type {
    CONCERT, PERFOMANCE, COMEDYNIGHT
}

/**
 *
 * @author nikos
 */
public class Event {
    private int event_id, event_capacity;
    private String event_name, event_date, event_time;
    private Type event_type;
    private Status event_status;

    public int getEventId() {
        return event_id;
    }

    public void setEventId(int event_id) {
        this.event_id = event_id;
    }

    public int getEventCapacity() {
        return event_capacity;
    }

    public void setEventCapacity(int event_capacity) {
        this.event_capacity = event_capacity;
    }

    public String getEventName() {
        return event_name;
    }

    public void setEventName(String event_name) {
        this.event_name = event_name;
    }

    public String getEventDate() {
        return event_date;
    }

    public void setEventDate(String event_date) {
        this.event_date = event_date;
    }

    public String getEventTime() {
        return event_time;
    }

    public void setEventTime(String event_time) {
        this.event_time = event_time;
    }

    public Type getEventType() {
        return event_type;
    }

    public void setEventType(Type event_type) {
        this.event_type = event_type;
    }

    public Status getEventStatus() {
        return event_status;
    }

    public void setEventStatus(Status event_status) {
        this.event_status = event_status;
    }
}
