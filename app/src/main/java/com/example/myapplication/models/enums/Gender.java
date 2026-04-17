package com.example.myapplication.models.enums;

/**
 * Represents the user's gender as used for filtering relevant mitzvot
 * (e.g. Tefillin applies to MALE only; Hafrashat Challah applies to FEMALE only).
 *
 * <p>Stored in Firestore as the string name of the enum value
 * ("MALE" / "FEMALE") via the no-arg setter pattern required by Firestore.</p>
 *
 * <p>Bagrut Requirement Fulfilled: OOP — typed enumeration used as a
 * filter input to the Habit relevance algorithm.</p>
 */
public enum Gender {
    MALE,
    FEMALE
}
