package com.team.appointment.service;

import com.team.appointment.model.AppointmentSlot;

public interface BookingRuleStrategy {

    boolean isValid(AppointmentSlot appointment, int participants);

}