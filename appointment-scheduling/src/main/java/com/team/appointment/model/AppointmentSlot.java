package com.team.appointment.model;

/**
 * Represents a single appointment time slot in the scheduling system.
 * Each slot has a fixed date, time, and duration; it tracks booking status,
 * participant count, and the type of appointment that was booked.
 *
 * @author Team
 * @version 1.0
 */
public class AppointmentSlot {

    /** The date of this slot in YYYY-MM-DD format. */
    private final String date;

    /** The start time of this slot in HH:mm format. */
    private final String time;

    /** Duration of this slot in minutes. */
    private final int duration;

    /** Current number of participants booked for this slot. */
    private int participantCount;

    /** Maximum number of participants allowed per slot. */
    private final int maxParticipants;

    /** Whether this slot has been booked. */
    private boolean booked;

    /** Current status: "PENDING" or "CONFIRMED". */
    private String status;

    /** The appointment type selected at booking time; null if not yet booked. */
    private AppointmentType type;

    /**
     * Constructs an AppointmentSlot with a default capacity of 3 participants.
     *
     * @param date     the date in YYYY-MM-DD format
     * @param time     the start time in HH:mm format
     * @param duration the duration in minutes
     */
    public AppointmentSlot(String date, String time, int duration) {
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.participantCount = 0;
        this.maxParticipants = 3;
        this.booked = false;
        this.status = "PENDING";
        this.type = null;
    }

    /**
     * Returns whether this slot has been booked.
     *
     * @return {@code true} if booked, {@code false} otherwise
     */
    public boolean isBooked() {
        return booked;
    }

    /**
     * Books this slot with the given number of participants and appointment type.
     *
     * @param participants number of participants (must be 1..maxParticipants)
     * @param type         the {@link AppointmentType} chosen for this booking
     */
    public void book(int participants, AppointmentType type) {
        this.participantCount = participants;
        this.booked = true;
        this.status = "CONFIRMED";
        this.type = type;
    }

    /**
     * Books this slot with the given number of participants (type defaults to INDIVIDUAL).
     *
     * @param participants number of participants
     */
    public void book(int participants) {
        book(participants, AppointmentType.INDIVIDUAL);
    }

    /**
     * Cancels this slot, resetting all booking data to defaults.
     */
    public void cancel() {
        this.participantCount = 0;
        this.booked = false;
        this.status = "PENDING";
        this.type = null;
    }

    /**
     * Returns whether the given participant count is within the allowed range.
     *
     * @param participants the number of participants to check
     * @return {@code true} if participants is between 1 and maxParticipants inclusive
     */
    public boolean canAcceptParticipants(int participants) {
        return participants >= 1 && participants <= maxParticipants;
    }

    /**
     * Returns the duration of this slot in minutes.
     *
     * @return duration in minutes
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Returns the current participant count.
     *
     * @return number of participants booked
     */
    public int getParticipantCount() {
        return participantCount;
    }

    /**
     * Returns the maximum number of participants allowed.
     *
     * @return max participants
     */
    public int getMaxParticipants() {
        return maxParticipants;
    }

    /**
     * Returns the booking status string ("PENDING" or "CONFIRMED").
     *
     * @return status string
     */
    public String getStatus() {
        return status;
    }

    /**
     * Returns the date of this slot in YYYY-MM-DD format.
     *
     * @return date string
     */
    public String getDate() {
        return date;
    }

    /**
     * Returns the start time of this slot in HH:mm format.
     *
     * @return time string
     */
    public String getTime() {
        return time;
    }

    /**
     * Returns the appointment type set at booking time.
     *
     * @return the {@link AppointmentType}, or {@code null} if not yet booked
     */
    public AppointmentType getType() {
        return type;
    }

    /**
     * Returns a human-readable string representation of this slot.
     *
     * @return formatted slot details
     */
    @Override
    public String toString() {
        String typeStr = (type != null) ? type.name() : "N/A";
        return "Date: " + date +
               " | Time: " + time +
               " | Duration: " + duration + " min" +
               " | Participants: " + participantCount + "/" + maxParticipants +
               " | Type: " + typeStr +
               " | Status: " + status;
    }
}
