package com.team.appointment.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.team.appointment.model.AppointmentSlot;
import com.team.appointment.model.AppointmentType;

/**
 * Unit tests for the booking rule strategies:
 * {@link DurationRule}, {@link CapacityRule}, and {@link TypeRule}.
 *
 * @author Team
 * @version 1.0
 */
public class RuleTest {

    private AppointmentSlot slot30;
    private AppointmentSlot slot60;

    @BeforeEach
    void setUp() {
        slot30 = new AppointmentSlot("2026-05-01", "09:00", 30);
        slot60 = new AppointmentSlot("2026-05-01", "09:00", 60);
    }

    // ── DurationRule ──────────────────────────────────────────────────────────

    @Test
    void durationRule_slot30_shouldPass() {
        assertTrue(new DurationRule().isValid(slot30, 1));
    }

    @Test
    void durationRule_slot60_shouldFail() {
        assertFalse(new DurationRule().isValid(slot60, 1));
    }

    // ── CapacityRule ──────────────────────────────────────────────────────────

    @Test
    void capacityRule_threeParticipants_shouldPass() {
        assertTrue(new CapacityRule().isValid(slot30, 3));
    }

    @Test
    void capacityRule_fourParticipants_shouldFail() {
        assertFalse(new CapacityRule().isValid(slot30, 4));
    }

    @Test
    void capacityRule_oneParticipant_shouldPass() {
        assertTrue(new CapacityRule().isValid(slot30, 1));
    }

    // ── TypeRule – max per type ───────────────────────────────────────────────

    @Test
    void typeRule_urgent_oneParticipant_passes() {
        assertTrue(new TypeRule(AppointmentType.URGENT).isValid(slot30, 1));
    }

    @Test
    void typeRule_urgent_twoParticipants_fails() {
        assertFalse(new TypeRule(AppointmentType.URGENT).isValid(slot30, 2));
    }

    @Test
    void typeRule_individual_oneParticipant_passes() {
        assertTrue(new TypeRule(AppointmentType.INDIVIDUAL).isValid(slot30, 1));
    }

    @Test
    void typeRule_individual_twoParticipants_fails() {
        assertFalse(new TypeRule(AppointmentType.INDIVIDUAL).isValid(slot30, 2));
    }

    @Test
    void typeRule_assessment_oneParticipant_passes() {
        assertTrue(new TypeRule(AppointmentType.ASSESSMENT).isValid(slot30, 1));
    }

    @Test
    void typeRule_assessment_twoParticipants_fails() {
        assertFalse(new TypeRule(AppointmentType.ASSESSMENT).isValid(slot30, 2));
    }

    @Test
    void typeRule_followUp_twoParticipants_passes() {
        assertTrue(new TypeRule(AppointmentType.FOLLOW_UP).isValid(slot30, 2));
    }

    @Test
    void typeRule_followUp_threeParticipants_fails() {
        assertFalse(new TypeRule(AppointmentType.FOLLOW_UP).isValid(slot30, 3));
    }

    @Test
    void typeRule_inPerson_twoParticipants_passes() {
        assertTrue(new TypeRule(AppointmentType.IN_PERSON).isValid(slot30, 2));
    }

    @Test
    void typeRule_inPerson_threeParticipants_fails() {
        assertFalse(new TypeRule(AppointmentType.IN_PERSON).isValid(slot30, 3));
    }

    @Test
    void typeRule_virtual_threeParticipants_passes() {
        assertTrue(new TypeRule(AppointmentType.VIRTUAL).isValid(slot30, 3));
    }

    @Test
    void typeRule_group_threeParticipants_passes() {
        assertTrue(new TypeRule(AppointmentType.GROUP).isValid(slot30, 3));
    }

    @Test
    void typeRule_zeroParticipants_fails() {
        assertFalse(new TypeRule(AppointmentType.GROUP).isValid(slot30, 0));
    }

    // ── TypeRule.maxForType static method ─────────────────────────────────────

    @Test
    void maxForType_urgent_isOne() {
        assertEquals(1, TypeRule.maxForType(AppointmentType.URGENT));
    }

    @Test
    void maxForType_group_isThree() {
        assertEquals(3, TypeRule.maxForType(AppointmentType.GROUP));
    }

    @Test
    void maxForType_followUp_isTwo() {
        assertEquals(2, TypeRule.maxForType(AppointmentType.FOLLOW_UP));
    }

    @Test
    void maxForType_individual_isOne() {
        assertEquals(1, TypeRule.maxForType(AppointmentType.INDIVIDUAL));
    }

    @Test
    void maxForType_assessment_isOne() {
        assertEquals(1, TypeRule.maxForType(AppointmentType.ASSESSMENT));
    }

    @Test
    void maxForType_inPerson_isTwo() {
        assertEquals(2, TypeRule.maxForType(AppointmentType.IN_PERSON));
    }

    @Test
    void maxForType_virtual_isThree() {
        assertEquals(3, TypeRule.maxForType(AppointmentType.VIRTUAL));
    }

    @Test
    void typeRule_directTypeOverload_followUp_twoParticipants_passes() {
        TypeRule rule = new TypeRule(AppointmentType.GROUP);
        assertTrue(rule.isValid(slot30, 2, AppointmentType.FOLLOW_UP));
    }

    @Test
    void typeRule_directTypeOverload_followUp_threeParticipants_fails() {
        TypeRule rule = new TypeRule(AppointmentType.GROUP);
        assertFalse(rule.isValid(slot30, 3, AppointmentType.FOLLOW_UP));
    }

    @Test
    void typeRule_directTypeOverload_virtual_threeParticipants_passes() {
        TypeRule rule = new TypeRule(AppointmentType.VIRTUAL);
        assertTrue(rule.isValid(slot30, 3, AppointmentType.VIRTUAL));
    }
}
