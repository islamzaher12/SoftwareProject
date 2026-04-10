package com.team.appointment.service;

import com.team.appointment.model.AppointmentSlot;

/**
 * A {@link BookingRuleStrategy} that enforces a fixed appointment duration.
 * Only slots whose duration equals exactly {@value #REQUIRED_DURATION} minutes
 * will pass validation.
 *
 * @author Team
 * @version 1.0
 */
public class DurationRule implements BookingRuleStrategy {

    /** The only permitted appointment duration, in minutes. */
    private static final int REQUIRED_DURATION = 30;

    /**
     * Returns {@code true} only if the slot duration is exactly 30 minutes.
     *
     * @param slot         the appointment slot being evaluated
     * @param participants ignored by this rule
     * @return {@code true} if slot duration equals 30
     */
    @Override
    public boolean isValid(AppointmentSlot slot, int participants) {
        return slot.getDuration() == REQUIRED_DURATION;
    }
}
