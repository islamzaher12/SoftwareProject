package com.team.appointment.model;

public class AppointmentSlot {
    private final String date;
    private final String time;
    private boolean booked;

    public AppointmentSlot(String date, String time) {
        this.date = date;
        this.time = time;
        this.booked = false;
    }

    public boolean isBooked() { return booked; }
    public void book() { this.booked = true; }

    @Override
    public String toString() {
        return date + " " + time;
    }
}