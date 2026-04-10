package com.team.appointment.service;

import com.team.appointment.model.AppointmentSlot;

/**
 * A {@link BookingRuleStrategy} that enforces the global participant capacity.
 * No appointment may have more than {@value #MAX_PARTICIPANTS} participants
 * regardless of appointment type.
 *
 * @author Team
 * @version 1.0
 */
public class CapacityRule implements BookingRuleStrategy {

    /** Absolute maximum number of participants allowed per appointment. */
    private static final int MAX_PARTICIPANTS = 3;

    /**
     * Returns {@code true} only if the participant count does not exceed
     * the global maximum of 3.
     *
     * @param slot         ignored by this rule
     * @param participants the number of participants requested
     * @return {@code true} if participants &le; 3
     */
    @Override
    public boolean isValid(AppointmentSlot slot, int participants) {
        return participants <= MAX_PARTICIPANTS;
    }
}
