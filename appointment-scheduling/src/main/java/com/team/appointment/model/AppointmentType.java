package com.team.appointment.model;

/**
 * Enumerates the supported appointment types in the scheduling system.
 * Each type may have different capacity and duration rules applied by the
 * {@code TypeRule} strategy.
 *
 * <ul>
 *   <li>{@link #URGENT} – high-priority appointment, max 1 participant</li>
 *   <li>{@link #FOLLOW_UP} – follow-up visit, max 2 participants</li>
 *   <li>{@link #ASSESSMENT} – evaluation session, max 1 participant</li>
 *   <li>{@link #VIRTUAL} – online meeting, max 3 participants</li>
 *   <li>{@link #IN_PERSON} – physical visit, max 2 participants</li>
 *   <li>{@link #INDIVIDUAL} – one-on-one session, exactly 1 participant</li>
 *   <li>{@link #GROUP} – group session, max 3 participants</li>
 * </ul>
 *
 * @author Team
 * @version 1.0
 */
public enum AppointmentType {

    /** High-priority appointment; limited to 1 participant. */
    URGENT,

    /** Follow-up appointment; limited to 2 participants. */
    FOLLOW_UP,

    /** Assessment/evaluation session; limited to 1 participant. */
    ASSESSMENT,

    /** Virtual (online) meeting; up to 3 participants. */
    VIRTUAL,

    /** In-person visit; limited to 2 participants. */
    IN_PERSON,

    /** Individual one-on-one session; exactly 1 participant. */
    INDIVIDUAL,

    /** Group session; up to 3 participants. */
    GROUP
}
