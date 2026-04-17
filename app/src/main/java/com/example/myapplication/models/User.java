package com.example.myapplication.models;

import com.example.myapplication.models.enums.Gender;
import com.example.myapplication.models.enums.MaritalStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents an authenticated end-user of TefilinTracker.
 *
 * <p>Holds demographic attributes (gender, marital status) that drive the
 * personalization of the habit catalog, the selected UI language, the list
 * of habits the user has committed to, and a notification preference.
 * Instances are serialized to and deserialized from Firestore documents at
 * path {@code users/{uid}}.</p>
 *
 * <p>Bagrut Requirement Fulfilled: Object-Oriented Programming — a
 * non-trivial class containing both state and two algorithmic methods
 * ({@link #isHabitRelevantScope(String, String)} and
 * {@link #addHabit(String)}).</p>
 */
public class User {

    /**
     * Maximum number of habits a user may have active at one time.
     * Enforced to keep the Daily Dashboard visually manageable and the
     * user's spiritual commitment realistic (positive-reinforcement UX).
     */
    public static final int MAX_ACTIVE_HABITS = 10;

    private String uid;
    private String displayName;
    private String email;
    private Gender gender;
    private MaritalStatus maritalStatus;
    private String preferredLanguage;
    private boolean notificationsEnabled;
    private List<String> activeHabitIds;
    private Date createdAt;

    /**
     * No-argument constructor required by Firebase Firestore for
     * automatic deserialization of documents into User instances.
     * Initializes {@code activeHabitIds} to an empty list so clients
     * never encounter a null collection.
     */
    public User() {
        this.activeHabitIds = new ArrayList<>();
    }

    /**
     * Full constructor used when creating a brand-new user immediately
     * after the onboarding flow completes.
     *
     * @param uid               Firebase Auth unique identifier.
     * @param displayName       Name displayed in the UI.
     * @param email             Email used for authentication.
     * @param gender            Gender — drives habit relevance filtering.
     * @param maritalStatus     Marital status — drives habit filtering.
     * @param preferredLanguage ISO language code ("en","iw","ru","fr","de").
     */
    public User(String uid, String displayName, String email,
                Gender gender, MaritalStatus maritalStatus,
                String preferredLanguage) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.preferredLanguage = preferredLanguage;
        this.notificationsEnabled = true;
        this.activeHabitIds = new ArrayList<>();
        this.createdAt = new Date();
    }

    // =============================================================
    //  Complex logic methods (non-trivial — Bagrut OOP requirement)
    // =============================================================

    /**
     * Decides whether a habit described by the given scope strings is
     * relevant to this user.
     *
     * <p>Algorithm: the habit passes the filter if and only if BOTH scope
     * predicates are satisfied:
     * <ul>
     *   <li>Gender scope: {@code "ALL"} passes unconditionally; otherwise
     *       {@code "MALE_ONLY"} requires {@link Gender#MALE} and
     *       {@code "FEMALE_ONLY"} requires {@link Gender#FEMALE}.</li>
     *   <li>Marital scope: {@code "ALL"} passes unconditionally; otherwise
     *       {@code "MARRIED_ONLY"} requires {@link MaritalStatus#MARRIED}
     *       and {@code "SINGLE_ONLY"} requires {@link MaritalStatus#SINGLE}.</li>
     * </ul>
     * Any unknown scope value is treated as non-matching (fail-closed).</p>
     *
     * @param genderScope  one of {@code "ALL" | "MALE_ONLY" | "FEMALE_ONLY"}.
     * @param maritalScope one of {@code "ALL" | "MARRIED_ONLY" | "SINGLE_ONLY"}.
     * @return {@code true} if the habit should be offered to this user.
     */
    public boolean isHabitRelevantScope(String genderScope, String maritalScope) {
        boolean genderOk =
                "ALL".equals(genderScope)
                || ("MALE_ONLY".equals(genderScope)   && gender == Gender.MALE)
                || ("FEMALE_ONLY".equals(genderScope) && gender == Gender.FEMALE);

        boolean maritalOk =
                "ALL".equals(maritalScope)
                || ("MARRIED_ONLY".equals(maritalScope) && maritalStatus == MaritalStatus.MARRIED)
                || ("SINGLE_ONLY".equals(maritalScope)  && maritalStatus == MaritalStatus.SINGLE);

        return genderOk && maritalOk;
    }

    /**
     * Adds a habit ID to the user's active list, enforcing two invariants.
     *
     * <p>Algorithm: rejects the addition with {@code false} in either of
     * these cases, and leaves the list unchanged:
     * <ol>
     *   <li>The habit ID is already present (no duplicates allowed).</li>
     *   <li>The list already holds {@link #MAX_ACTIVE_HABITS} items.</li>
     * </ol>
     * Otherwise appends the ID and returns {@code true}. Lazily initializes
     * the underlying list in case the instance came from a Firestore
     * deserialization path that did not populate it.</p>
     *
     * @param habitId the catalog document ID of the habit being added.
     * @return {@code true} if the habit was added; {@code false} otherwise.
     */
    public boolean addHabit(String habitId) {
        if (activeHabitIds == null) {
            activeHabitIds = new ArrayList<>();
        }
        if (activeHabitIds.contains(habitId)) {
            return false;
        }
        if (activeHabitIds.size() >= MAX_ACTIVE_HABITS) {
            return false;
        }
        activeHabitIds.add(habitId);
        return true;
    }

    /**
     * Removes a habit ID from the user's active list.
     *
     * @param habitId the catalog document ID of the habit to remove.
     * @return {@code true} if a matching entry was removed;
     *         {@code false} if the ID was not in the list.
     */
    public boolean removeHabit(String habitId) {
        if (activeHabitIds == null) {
            return false;
        }
        return activeHabitIds.remove(habitId);
    }

    // ==================== Getters / Setters ====================

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public MaritalStatus getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(MaritalStatus maritalStatus) { this.maritalStatus = maritalStatus; }

    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }

    public boolean isNotificationsEnabled() { return notificationsEnabled; }
    public void setNotificationsEnabled(boolean notificationsEnabled) { this.notificationsEnabled = notificationsEnabled; }

    public List<String> getActiveHabitIds() { return activeHabitIds; }
    public void setActiveHabitIds(List<String> activeHabitIds) { this.activeHabitIds = activeHabitIds; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
