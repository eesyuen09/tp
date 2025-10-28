package seedu.address.model.attendance;


import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Date;

/**
 * Represents an attendance record for a specific date and class.
 * Each record indicates whether a student was present or absent on that date for a specific class.
 */
public class Attendance {
    private final Date date;
    private final ClassTag classTag;
    private final boolean isPresent;

    /**
     * Constructs an Attendance record.
     *
     * @param date The date of the attendance record.
     * @param classTag The class tag for the attendance record.
     * @param isPresent True if the student was present, false otherwise.
     */
    public Attendance(Date date, ClassTag classTag, boolean isPresent) {
        requireNonNull(date);
        requireNonNull(classTag);
        this.date = date;
        this.classTag = classTag;
        this.isPresent = isPresent;
    }

    public Date getDate() {
        return date;
    }

    public ClassTag getClassTag() {
        return classTag;
    }

    public boolean isStudentPresent() {
        return isPresent;
    }

    public boolean notPresent() {
        return !this.isPresent;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Attendance)) {
            return false;
        }

        Attendance otherAttendance = (Attendance) other;
        return date.equals(otherAttendance.date)
                && classTag.equals(otherAttendance.classTag)
                && isPresent == otherAttendance.isPresent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, classTag, isPresent);
    }

    @Override
    public String toString() {
        return date.toString() + " " + classTag.toString() + ": " + (isPresent ? "Present" : "Absent");
    }
}
