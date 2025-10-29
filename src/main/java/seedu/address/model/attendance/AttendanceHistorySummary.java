package seedu.address.model.attendance;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.model.person.StudentId;
import seedu.address.model.time.Date;

/**
 * Metadata describing the range of a student's attendance history view.
 */
public class AttendanceHistorySummary {
    private final String studentName;
    private final StudentId studentId;
    private final Date startDate;
    private final Date endDate;
    private final int recordCount;

    /**
     * Constructs an {@code AttendanceHistorySummary}.
     */
    public AttendanceHistorySummary(String studentName, StudentId studentId,
                                    Date startDate, Date endDate, int recordCount) {
        this.studentName = requireNonNull(studentName);
        this.studentId = requireNonNull(studentId);
        this.startDate = requireNonNull(startDate);
        this.endDate = requireNonNull(endDate);
        if (recordCount < 0) {
            throw new IllegalArgumentException("recordCount must be non-negative");
        }
        this.recordCount = recordCount;
    }

    public String getStudentName() {
        return studentName;
    }

    public StudentId getStudentId() {
        return studentId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getRecordCount() {
        return recordCount;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AttendanceHistorySummary)) {
            return false;
        }
        AttendanceHistorySummary otherSummary = (AttendanceHistorySummary) other;
        return studentName.equals(otherSummary.studentName)
                && studentId.equals(otherSummary.studentId)
                && startDate.equals(otherSummary.startDate)
                && endDate.equals(otherSummary.endDate)
                && recordCount == otherSummary.recordCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentName, studentId, startDate, endDate, recordCount);
    }
}
