package com.example.myapplication.models.enums;

/**
 * Represents the user's marital status as used for filtering relevant mitzvot
 * (e.g. certain habits are oriented to married individuals only).
 *
 * <p>Stored in Firestore as the enum value's string name.</p>
 *
 * <p>Bagrut Requirement Fulfilled: OOP — typed enumeration used as a
 * filter input to the Habit relevance algorithm.</p>
 */
public enum MaritalStatus {
    SINGLE,
    MARRIED
}
