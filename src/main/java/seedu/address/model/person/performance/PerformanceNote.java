package seedu.address.model.person.performance;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.model.person.Date;

/**
 * Represents a performance note for a student, consisting of a date and a note.
 */
public class PerformanceNote {
    public static final int MAX_NOTE_LEN = 200;

    private final Date date;
    private final String note;

    /**
     * Creates a PerformanceNote object with the given date and note.
     *
     * @param date the date of the performance note
     * @param note the performance note, max length 200 characters
     */
    public PerformanceNote(Date date, String note) {
        requireNonNull(date);
        requireNonNull(note);
        this.date = date;
        this.note = validateNote(note);
    }

    private static String validateNote(String n) {
        if (n.length() > MAX_NOTE_LEN) {
            throw new IllegalArgumentException("Error: performance note exceeds maximum length of 200 characters");
        }
        return n;
    }

    /**
     * Gets the date of the performance note.
     *
     * @return the date as a Date object
     */
    public Date getDate() {
        return date;
    }

    /**
     * Gets the performance note.
     *
     * @return the performance note as a String
     */
    public String getNote() {
        return note;
    }

    /**
     * Checks if two PerformanceNote objects have the same date and note.
     *
     * @param other the other PerformanceNote to compare to
     * @return true if both PerformanceNote objects have the same date and note, false otherwise
     */
    public boolean isSameContent(PerformanceNote other) {
        return other != null && date.equals(other.date) && note.equals(other.note);
    }

    @Override
    public String toString() {
        return date.getFormattedDate() + ": " + note;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PerformanceNote)) {
            return false;
        }

        PerformanceNote otherPerformamnceNote = (PerformanceNote) other;
        return note.equals(otherPerformamnceNote.note) && date.equals(otherPerformamnceNote.date);

    }

    @Override
    public int hashCode() {
        return Objects.hash(date, note);
    }
}
