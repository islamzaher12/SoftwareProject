package com.team.appointment.service;

import com.team.appointment.model.AppointmentSlot;
import com.team.appointment.model.AppointmentType;

/**
 * A {@link BookingRuleStrategy} that enforces per-type participant limits.
 * Different appointment types impose different maximum participant counts:
 *
 * <ul>
 *   <li>URGENT     – max 1 participant</li>
 *   <li>ASSESSMENT – max 1 participant</li>
 *   <li>INDIVIDUAL – max 1 participant</li>
 *   <li>FOLLOW_UP  – max 2 participants</li>
 *   <li>IN_PERSON  – max 2 participants</li>
 *   <li>VIRTUAL    – max 3 participants</li>
 *   <li>GROUP      – max 3 participants</li>
 * </ul>
 *
 * <p>This class requires the slot's type to be pre-set (via
 * {@link AppointmentSlot#book(int, AppointmentType)}) before validation,
 * or the type is passed directly to {@link #isValid(AppointmentSlot, int, AppointmentType)}.
 *
 * @author Team
 * @version 1.0
 */
public class TypeRule implements BookingRuleStrategy {

    /** The appointment type this rule instance is validating against. */
    private final AppointmentType type;

    /**
     * Constructs a TypeRule for a specific appointment type.
     *
     * @param type the {@link AppointmentType} whose constraints to enforce
     */
    public TypeRule(AppointmentType type) {
        this.type = type;
    }

    /**
     * Validates participant count against the type-specific limit.
     * The slot parameter is not used by this rule; only participants is checked.
     *
     * @param slot         ignored by this rule
     * @param participants the number of participants requested
     * @return {@code true} if within the type-specific limit
     */
    @Override
    public boolean isValid(AppointmentSlot slot, int participants) {
        return participants >= 1 && participants <= maxForType(type);
    }

    /**
     * Validates participant count for the given appointment type directly.
     *
     * @param slot         ignored
     * @param participants the number of participants requested
     * @param apptType     the appointment type to evaluate
     * @return {@code true} if participants is within the limit for apptType
     */
    public boolean isValid(AppointmentSlot slot, int participants, AppointmentType apptType) {
        return participants >= 1 && participants <= maxForType(apptType);
    }

    /**
     * Returns the maximum allowed participants for the given appointment type.
     *
     * @param apptType the appointment type
     * @return maximum participant count (1, 2, or 3)
     */
    public static int maxForType(AppointmentType apptType) {
        switch (apptType) {
            case URGENT:
            case ASSESSMENT:
            case INDIVIDUAL:
                return 1;
            case FOLLOW_UP:
            case IN_PERSON:
                return 2;
            case VIRTUAL:
            case GROUP:
            default:
                return 3;
        }
    }
}
