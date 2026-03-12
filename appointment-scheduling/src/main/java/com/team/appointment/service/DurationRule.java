package com.team.appointment.service;

import com.team.appointment.model.AppointmentSlot;

public class DurationRule implements BookingRuleStrategy {

    private static final int REQUIRED_DURATION = 30;

    @Override
    public boolean isValid(AppointmentSlot appointment, int participants) {
        return appointment.getDuration() == REQUIRED_DURATION;
    }
}