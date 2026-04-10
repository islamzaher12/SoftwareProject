package com.team.appointment.service;

import java.util.Arrays;
import java.util.List;
import com.team.appointment.model.AppointmentSlot;
import com.team.appointment.model.AppointmentType;

/**
 * Core business-logic service for booking, cancelling, and modifying
 * appointments.  All booking decisions are delegated to a chain of
 * {@link BookingRuleStrategy} objects (Strategy pattern).
 *
 * <p>Rules applied on every booking:
 * <ol>
 *   <li>{@link DurationRule}  – slot must be exactly 30 minutes</li>
 *   <li>{@link CapacityRule}  – global max of 3 participants</li>
 *   <li>{@link TypeRule}      – per-type participant limits</li>
 * </ol>
 *
 * @author Team
 * @version 1.0
 */
public class BookingService {

    /** Required appointment duration in minutes. */
    private static final int REQUIRED_DURATION = 30;

    /** Rule: duration must equal 30 minutes. */
    private final BookingRuleStrategy durationRule = new DurationRule();

    /** Rule: global participant cap. */
    private final BookingRuleStrategy capacityRule = new CapacityRule();

    /** Source of all slot data. */
    private final SlotService slotService;

    /**
     * Constructs a BookingService backed by the given SlotService.
     *
     * @param slotService the service that provides appointment slots
     */
    public BookingService(SlotService slotService) {
        this.slotService = slotService;
    }

    /**
     * Attempts to book an available slot by 1-based index.
     * All three rules (duration, capacity, type) are checked before booking.
     *
     * @param index        1-based index into the available-slots list
     * @param duration     requested duration in minutes (must be 30)
     * @param participants number of participants (must satisfy type limits)
     * @param type         the {@link AppointmentType} for this booking
     * @return {@code true} if the booking succeeded, {@code false} on any validation failure
     */
    public boolean bookAppointment(int index, int duration, int participants, AppointmentType type) {
        List<AppointmentSlot> availableSlots = slotService.getAvailableSlots();

        if (index < 1 || index > availableSlots.size()) return false;
        if (duration != REQUIRED_DURATION) return false;

        AppointmentSlot slot = availableSlots.get(index - 1);

        if (slot.isBooked()) return false;
        if (!durationRule.isValid(slot, participants)) return false;
        if (!capacityRule.isValid(slot, participants)) return false;

        // Type-specific rule
        TypeRule typeRule = new TypeRule(type);
        if (!typeRule.isValid(slot, participants)) return false;

        slot.book(participants, type);
        return true;
    }

    /**
     * Convenience overload that applies only duration and capacity rules (no type rule).
     * Preserves backward compatibility with existing tests and books with INDIVIDUAL type.
     *
     * @param index        1-based index into the available-slots list
     * @param duration     requested duration in minutes
     * @param participants number of participants (global max 3 applies)
     * @return {@code true} if booking succeeded
     */
    public boolean bookAppointment(int index, int duration, int participants) {
        List<AppointmentSlot> availableSlots = slotService.getAvailableSlots();
        if (index < 1 || index > availableSlots.size()) return false;
        if (duration != REQUIRED_DURATION) return false;
        AppointmentSlot slot = availableSlots.get(index - 1);
        if (slot.isBooked()) return false;
        if (!slot.canAcceptParticipants(participants)) return false;
        if (!durationRule.isValid(slot, participants)) return false;
        if (!capacityRule.isValid(slot, participants)) return false;
        slot.book(participants);
        return true;
    }

    /**
     * Cancels an existing booking identified by its 1-based index in the
     * full (all-slots) list.
     *
     * @param index 1-based index into the all-slots list
     * @return {@code true} if the slot was booked and is now cancelled
     */
    public boolean cancelBooking(int index) {
        List<AppointmentSlot> allSlots = slotService.getAllSlots();

        if (index < 1 || index > allSlots.size()) return false;

        AppointmentSlot slot = allSlots.get(index - 1);
        if (!slot.isBooked()) return false;

        slot.cancel();
        return true;
    }

    /**
     * Modifies an existing booking by moving it to a different available slot.
     * Both slot numbers reference the all-slots list (1-based).
     *
     * @param oldSlotNumber  index of the currently booked slot to vacate
     * @param newSlotNumber  index of the new slot to occupy
     * @param duration       duration in minutes (must be 30)
     * @param participants   number of participants for the new booking
     * @param type           the {@link AppointmentType} for the new booking
     * @return {@code true} if the modification succeeded
     */
    public boolean modifyBooking(int oldSlotNumber, int newSlotNumber,
                                  int duration, int participants, AppointmentType type) {
        List<AppointmentSlot> allSlots = slotService.getAllSlots();

        if (oldSlotNumber < 1 || oldSlotNumber > allSlots.size()) return false;
        if (newSlotNumber < 1 || newSlotNumber > allSlots.size()) return false;
        if (duration != REQUIRED_DURATION) return false;

        AppointmentSlot oldSlot = allSlots.get(oldSlotNumber - 1);
        AppointmentSlot newSlot = allSlots.get(newSlotNumber - 1);

        if (!oldSlot.isBooked()) return false;
        if (newSlot.isBooked()) return false;
        if (!newSlot.canAcceptParticipants(participants)) return false;

        TypeRule typeRule = new TypeRule(type);
        if (!typeRule.isValid(newSlot, participants)) return false;

        oldSlot.cancel();
        newSlot.book(participants, type);
        return true;
    }

    /**
     * Convenience overload for modifyBooking without a type parameter.
     * Applies only duration and capacity rules (no type-specific rule).
     *
     * @param oldSlotNumber index of the currently booked slot
     * @param newSlotNumber index of the new slot
     * @param duration      duration in minutes
     * @param participants  number of participants (global max 3 applies)
     * @return {@code true} if modification succeeded
     */
    public boolean modifyBooking(int oldSlotNumber, int newSlotNumber,
                                  int duration, int participants) {
        List<AppointmentSlot> allSlots = slotService.getAllSlots();
        if (oldSlotNumber < 1 || oldSlotNumber > allSlots.size()) return false;
        if (newSlotNumber < 1 || newSlotNumber > allSlots.size()) return false;
        if (duration != REQUIRED_DURATION) return false;
        AppointmentSlot oldSlot = allSlots.get(oldSlotNumber - 1);
        AppointmentSlot newSlot = allSlots.get(newSlotNumber - 1);
        if (!oldSlot.isBooked()) return false;
        if (newSlot.isBooked()) return false;
        if (!newSlot.canAcceptParticipants(participants)) return false;
        oldSlot.cancel();
        newSlot.book(participants);
        return true;
    }
}
