package seedu.address.model.attendance;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import seedu.address.model.person.Date;

/**
 * A list of attendance records.
 * Encapsulates the management of attendance records for a person.
 */
public class AttendanceList {
    private final List<Attendance> records = new ArrayList<>();

    /**
     * Creates an empty AttendanceList.
     */
    public AttendanceList() {}

    /**
     * Creates an AttendanceList with the given initial records.
     *
     * @param initial Initial list of attendance records.
     */
    public AttendanceList(List<Attendance> initial) {
        requireNonNull(initial);
        records.addAll(initial);
    }

    /**
     * Returns true if attendance is already marked as present for this date.
     *
     * @param date The date to check.
     * @return true if the date has a present attendance record.
     */
    public boolean hasAttendanceMarked(Date date) {
        return records.stream()
                .anyMatch(attendance -> attendance.getDate().equals(date));
    }

    /**
     * Marks student as present on this date.
     * If record exists, updates it. Otherwise, creates new record.
     *
     * @param date The date to mark attendance.
     */
    public void markAttendance(Date date) {
        requireNonNull(date);
        records.removeIf(attendance -> attendance.getDate().equals(date));
        records.add(new Attendance(date, true));
    }

    /**
     * Marks student as absent on this date.
     * If a present record exists, removes it and adds an absent record.
     * If no record exists, creates a new absent record.
     *
     * @param date The date to unmark attendance.
     */
    public void unmarkAttendance(Date date) {
        requireNonNull(date);
        records.removeIf(attendance -> attendance.getDate().equals(date));
        records.add(new Attendance(date, false));
    }

    /**
     * Returns the attendance records as an unmodifiable list.
     *
     * @return Unmodifiable list of attendance records.
     */
    public List<Attendance> asUnmodifiableList() {
        return Collections.unmodifiableList(records);
    }

    /**
     * Returns the number of attendance records in the list.
     *
     * @return Number of attendance records.
     */
    public int size() {
        return records.size();
    }

    @Override
    public int hashCode() {
        return records.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AttendanceList)) {
            return false;
        }

        AttendanceList otherAttendanceList = (AttendanceList) other;
        return records.equals(otherAttendanceList.records);
    }
}
