package seedu.address.model.attendance;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Date;

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
     * Returns true if attendance is already marked as present for this date and class tag.
     *
     * @param date The date to check.
     * @param classTag The class tag to check.
     * @return True if the date and class tag have a present attendance record.
     */
    public boolean hasAttendanceMarkedPresent(Date date, ClassTag classTag) {
        requireNonNull(date);
        requireNonNull(classTag);
        return records.stream()
                .anyMatch(attendance -> attendance.getDate().equals(date)
                        && attendance.getClassTag().equals(classTag)
                        && attendance.isStudentPresent());
    }

    /**
     * Returns true if attendance is already marked as absent for this date and class tag.
     *
     * @param date The date to check.
     * @param classTag The class tag to check.
     * @return True if the date and class tag have an absent attendance record.
     */
    public boolean hasAttendanceMarkedAbsent(Date date, ClassTag classTag) {
        requireNonNull(date);
        requireNonNull(classTag);
        return records.stream()
                .anyMatch(attendance -> attendance.getDate().equals(date)
                        && attendance.getClassTag().equals(classTag)
                        && attendance.notPresent());
    }


    /**
     * Marks student as present on this date for a specific class.
     * If a record exists for this date and class, it is replaced with a present record.
     * Otherwise, a new present record is created.
     *
     * @param date The date to mark the student as present.
     * @param classTag The class tag for the attendance.
     */
    public void markAttendancePresent(Date date, ClassTag classTag) {
        requireNonNull(date);
        requireNonNull(classTag);
        records.removeIf(attendance -> attendance.getDate().equals(date)
                && attendance.getClassTag().equals(classTag));
        records.add(new Attendance(date, classTag, true));
    }

    /**
     * Marks student as absent on this date for a specific class.
     * If a record exists for this date and class, it is replaced with an absent record.
     * Otherwise, a new absent record is created.
     *
     * @param date The date to mark the student as absent.
     * @param classTag The class tag for the attendance.
     */
    public void markAttendanceAbsent(Date date, ClassTag classTag) {
        requireNonNull(date);
        requireNonNull(classTag);
        records.removeIf(attendance -> attendance.getDate().equals(date)
                && attendance.getClassTag().equals(classTag));
        records.add(new Attendance(date, classTag, false));
    }

    /**
     * Returns true if an attendance record exists for this date and class tag.
     *
     * @param date The date to check.
     * @param classTag The class tag to check.
     * @return True if an attendance record exists for the date and class tag.
     */
    public boolean hasAttendanceRecord(Date date, ClassTag classTag) {
        requireNonNull(date);
        requireNonNull(classTag);
        return records.stream()
                .anyMatch(attendance -> attendance.getDate().equals(date)
                        && attendance.getClassTag().equals(classTag));
    }

    /**
     * Deletes the attendance record for this date and class tag.
     * If no record exists, no action is taken.
     *
     * @param date The date of the attendance record to delete.
     * @param classTag The class tag of the attendance record to delete.
     */
    public void deleteAttendance(Date date, ClassTag classTag) {
        requireNonNull(date);
        requireNonNull(classTag);
        records.removeIf(attendance -> attendance.getDate().equals(date)
                && attendance.getClassTag().equals(classTag));
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
