package seedu.address.model.fee;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.model.person.StudentId;
import seedu.address.model.time.Month;

/**
 * Metadata describing the range of a student's payment history view.
 */
public class FeeHistorySummary {
    private final String studentName;
    private final StudentId studentId;
    private final Month startMonth;
    private final Month endMonth;
    private final Month enrolledMonth;
    private final int monthCount;

    /**
     * Constructs a {@code FeeHistorySummary}.
     */
    public FeeHistorySummary(String studentName, StudentId studentId, Month startMonth,
                             Month endMonth, Month enrolledMonth, int monthCount) {
        this.studentName = requireNonNull(studentName);
        this.studentId = requireNonNull(studentId);
        this.startMonth = requireNonNull(startMonth);
        this.endMonth = requireNonNull(endMonth);
        this.enrolledMonth = requireNonNull(enrolledMonth);
        if (monthCount < 0) {
            throw new IllegalArgumentException("monthCount must be non-negative");
        }
        this.monthCount = monthCount;
    }

    public String getStudentName() {
        return studentName;
    }

    public StudentId getStudentId() {
        return studentId;
    }

    public Month getStartMonth() {
        return startMonth;
    }

    public Month getEndMonth() {
        return endMonth;
    }

    public Month getEnrolledMonth() {
        return enrolledMonth;
    }

    public int getMonthCount() {
        return monthCount;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FeeHistorySummary)) {
            return false;
        }
        FeeHistorySummary otherSummary = (FeeHistorySummary) other;
        return studentName.equals(otherSummary.studentName)
                && studentId.equals(otherSummary.studentId)
                && startMonth.equals(otherSummary.startMonth)
                && endMonth.equals(otherSummary.endMonth)
                && enrolledMonth.equals(otherSummary.enrolledMonth)
                && monthCount == otherSummary.monthCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentName, studentId, startMonth, endMonth, enrolledMonth, monthCount);
    }
}
