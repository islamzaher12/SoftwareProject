package com.team.appointment.model;

public class AppointmentSlot {

    private final String date;
    private final String time;
    private final int duration;
    private boolean booked;
    private int participants;

    public AppointmentSlot(String date, String time, int duration) {
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.booked = false;
        this.participants = 0;
    }

    public boolean isBooked() {
        return booked;
    }

    public void book(int participants) {
        this.booked = true;
        this.participants = participants;
    }

    public int getDuration() {
        return duration;
    }

    public String toString() {
        return date + " " + time + " (" + duration + " min)";
    }
}