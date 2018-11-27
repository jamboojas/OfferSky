package com.example.abhiraj.offersky.model;

/**
 * Created by Abhiraj on 20-04-2017.
 */

public class Event {
    private String eventId;
    private String name;
    private String date;
    private String time;
    private String location;
    private String description;

    public Event() {
        super();
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString(){

        StringBuilder sb = new StringBuilder();
        sb.append(eventId + "  " + description);
        return sb.toString();
    }
}
