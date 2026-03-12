package com.team.appointment.service;

import com.team.appointment.model.AppointmentSlot;

public class CapacityRule implements BookingRuleStrategy {

    private static final int MAX_PARTICIPANTS = 3;

    @Override
    public boolean isValid(AppointmentSlot appointment, int participants) {
        return participants <= MAX_PARTICIPANTS;
    }
}