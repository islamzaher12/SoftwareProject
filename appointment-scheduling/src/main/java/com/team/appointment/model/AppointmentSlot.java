package com.team.appointment.model;

public class AppointmentSlot {

	private final String date;
	private final String time;
	private final int duration;

	private int participantCount;
	private final int maxParticipants;

	private boolean booked;
	private String status;

	public AppointmentSlot(String date, String time, int duration) {
	    this.date = date;
	    this.time = time;
	    this.duration = duration;
	    this.participantCount = 0;
	    this.maxParticipants = 3;
	    this.booked = false;
	    this.status = "PENDING";
	}

    public boolean isBooked() {
        return booked;
    }

    public void book(int participants) {
        this.participantCount = participants;
        this.booked = true;
        this.status = "CONFIRMED";
    }
    public void cancel() {
        this.participantCount = 0;
        this.booked = false;
        this.status = "PENDING";
    }

    public boolean canAcceptParticipants(int participants) {
        return participants >= 1 && participants <= maxParticipants;
    }

   
    public int getDuration() {
        return duration;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public String getStatus() {
        return status;
    }
    
    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    } 
    
    @Override
    public String toString() {
        return "Date: " + date +
               ", Time: " + time +
               ", Duration: " + duration +
               " mins, Participants: " + participantCount + "/" + maxParticipants +
               ", Status: " + status;
    }
    
}