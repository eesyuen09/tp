package seedu.address.model.attendance;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Date;

/**
 * Represents one row in a student's attendance history view.
 */
public class AttendanceHistoryEntry {

    private final Date date;
    private final ClassTag classTag;
    private final boolean present;

    /**
     * Constructs an {@code AttendanceHistoryEntry}.
     */
    public AttendanceHistoryEntry(Date date, ClassTag classTag, boolean present) {
        this.date = requireNonNull(date);
        this.classTag = requireNonNull(classTag);
        this.present = present;
    }

    public Date getDate() {
        return date;
    }

    public ClassTag getClassTag() {
        return classTag;
    }

    public boolean isPresent() {
        return present;
    }

    /**
     * Returns a short label describing whether the student was present.
     */
    public String getStatusLabel() {
        return present ? "Present" : "Absent";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AttendanceHistoryEntry)) {
            return false;
        }
        AttendanceHistoryEntry otherEntry = (AttendanceHistoryEntry) other;
        return date.equals(otherEntry.date)
                && classTag.equals(otherEntry.classTag)
                && present == otherEntry.present;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, classTag, present);
    }
}
