package seedu.address.model.attendance;


import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.model.person.Date;

/**
 * Represents an attendance record for a specific date.
 * Each record indicates whether a student was present or absent on that date.
 */
public class Attendance {
    private final Date date;
    private final boolean isPresent;

    /**
     * Constructs an Attendance record.
     *
     * @param date The date of the attendance record.
     * @param isPresent True if the student was present, false otherwise.
     */
    public Attendance(Date date, boolean isPresent) {
        requireNonNull(date);
        this.date = date;
        this.isPresent = isPresent;
    }

    public Date getDate() {
        return date;
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
                && isPresent == otherAttendance.isPresent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, isPresent);
    }

    @Override
    public String toString() {
        return date.toString() + ": " + (isPresent ? "Present" : "Absent");
    }
}
