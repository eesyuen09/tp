package seedu.address.model.fee;

import static java.util.Objects.requireNonNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import seedu.address.model.person.Month;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;

/**
 * Represents a tracker for student fee payments.
 * Each student can be marked as PAID or UNPAID for each month.
 * Unmarked months are treated as UNPAID by default once the student has enrolled.
 */
public class FeeTracker {

    private final Map<StudentId, Map<Month, FeeState>> records = new LinkedHashMap<>();

    /**
     * Mark the student with the studentId id as paid
     */
    public void markPaid(StudentId id, Month month) {
        requireNonNull(id);
        requireNonNull(month);
        records.computeIfAbsent(id, k -> new LinkedHashMap<>()).put(month, FeeState.PAID);
    }

    /**
     * Mark the student with the studentId id as unpaid
     */
    public void markUnpaid(StudentId id, Month month) {
        requireNonNull(id);
        requireNonNull(month);
        records.computeIfAbsent(id, k -> new LinkedHashMap<>()).put(month, FeeState.UNPAID);
    }

    /**
     * Retrieves the explicit fee status of a student for a specific month, if it was manually recorded.
     * This does NOT infer any default value.
     * It only returns data that was explicitly marked.
     *
     * @param id    The student's ID.
     * @param month The month to query.
     * @return {@code Optional.of(FeeState)} if a record exists, or {@code Optional.empty()} otherwise.
     */
    public Optional<FeeState> getExplicitStatusOfMonth(StudentId id, Month month) {
        Map<Month, FeeState> monthRecord = records.get(id);
        return Optional.ofNullable(monthRecord == null ? null : monthRecord.get(month));
    }

    /**
     * Determines the effective fee status of a student for a given month.
     * If the student is enrolled but no explicit record exists, the payment status is default to UNPAID.
     * If the student has not yet enrolled by that month, no status is returned.
     *
     * @param person The student whose status is being checked.
     * @param month  The month to check.
     * @return {@code Optional.of(PAID/UNPAID)} if the month is on or after enrollment,
     *         or {@code Optional.empty()} if before enrollment.
     */
    public Optional<FeeState> getDerivedStatusOfMonth(Person person, Month month) {
        Month start = person.getEnrolledMonth();
        if (start == null || month.isBefore(start)) {
            return Optional.empty(); // not tracked
        }
        return Optional.of(getExplicitStatusOfMonth(person.getStudentId(), month).orElse(FeeState.UNPAID));
    }

    /**
     * Builds a payment history of a student from the start month to the end month.
     *
     * @param person  The student whose payment history to generate.
     * @param start The earliest month to include in the report.
     * @param end The latest month to include in the report.
     * @return A map of {@code Month → FeeState}, or an empty map if the student is not yet enrolled.
     */
    public Map<Month, FeeState> getPaymentHistory(Person person, Month start, Month end) {
        requireNonNull(person);
        requireNonNull(start);
        requireNonNull(end);

        Month enrolled = person.getEnrolledMonth();
        if (enrolled == null) {
            return Map.of();
        }

        Month effectiveStart = start.isBefore(enrolled) ? enrolled : start;

        // Nothing to show if end precedes effectiveStart
        if (end.isBefore(effectiveStart)) {
            return Map.of();
        }

        Map<Month, FeeState> history = new LinkedHashMap<>();
        Month current = effectiveStart;
        while (current.isBefore(end) || current.equals(end)) {
            FeeState state = getDerivedStatusOfMonth(person, current).orElse(FeeState.UNPAID);
            history.put(current, state);
            current = current.plusMonths(1);
        }
        return history;
    }

    /**
     * Copy the data from other fee tracker to the current fee tracker.
     */
    public void copyFrom(FeeTracker other) {
        requireNonNull(other);
        this.records.clear();

        for (Map.Entry<StudentId, Map<Month, FeeState>> e : other.records.entrySet()) {
            this.records.put(e.getKey(), new LinkedHashMap<>(e.getValue()));
        }
    }
}
