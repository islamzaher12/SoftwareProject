package com.team.appointment.service;

import com.team.appointment.model.AppointmentSlot;

/**
 * Strategy interface for appointment booking validation rules.
 * Implementations enforce specific constraints (e.g. duration, capacity,
 * type-specific limits) before a slot is confirmed.
 *
 * <p>This follows the <em>Strategy</em> design pattern: new rules can be
 * added without modifying {@link BookingService}.
 *
 * @author Team
 * @version 1.0
 * @see DurationRule
 * @see CapacityRule
 * @see TypeRule
 */
public interface BookingRuleStrategy {

    /**
     * Validates whether the given slot and participant count satisfy this rule.
     *
     * @param slot         the appointment slot being evaluated
     * @param participants the number of participants requested
     * @return {@code true} if the rule is satisfied, {@code false} otherwise
     */
    boolean isValid(AppointmentSlot slot, int participants);
}
