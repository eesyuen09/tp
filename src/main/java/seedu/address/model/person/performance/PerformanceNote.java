package seedu.address.model.person.performance;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Date;

/**
 * Represents a performance note for a student, consisting of a date and a note.
 */
public class PerformanceNote {
    public static final int MAX_NOTE_LEN = 200;
    public static final String MESSAGE_CONSTRAINTS =
            "Performance note must be provided, cannot be empty, "
                    + "and must be at most " + MAX_NOTE_LEN + " characters long.";

    private final Date date;
    private final ClassTag classTag;
    private final String note;

    /**
     * Creates a PerformanceNote object with the given date and note.
     *
     * @param date the date of the performance note
     * @param classTag the class tag of the performance note
     * @param note the performance note, max length 200 characters
     */
    public PerformanceNote(Date date, ClassTag classTag, String note) {
        requireAllNonNull(date, classTag, note);
        this.date = date;
        this.classTag = classTag;
        this.note = validateNote(note);
    }

    private static String validateNote(String n) {
        if (n.trim().isEmpty()) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }

        if (n.length() > MAX_NOTE_LEN) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
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
     * Gets the class tag of performance note .
     *
     * @return the class tag as a ClassTag object
     */
    public ClassTag getClassTag() {
        return classTag;
    }

    /**
     * Checks if two PerformanceNote objects have the same date and note.
     *
     * @param other the other PerformanceNote to compare to
     * @return true if both PerformanceNote objects have the same date, class tag and note, false otherwise
     */
    public boolean isSameContent(PerformanceNote other) {
        return other != null && date.equals(other.date) && note.equals(other.note) && classTag.equals(other.classTag);
    }

    @Override
    public String toString() {
        return date.getFormattedDate() + " " + classTag.toString() + ": " + note;
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
        return note.equals(otherPerformamnceNote.note)
                && date.equals(otherPerformamnceNote.date)
                && classTag.equals(otherPerformamnceNote.classTag);

    }

    @Override
    public int hashCode() {
        return Objects.hash(date, note, classTag);
    }
}
