package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.fee.FeeState;
import seedu.address.model.person.StudentId;
import seedu.address.model.time.Month;

/**
 * Jackson-friendly serializable entry representing one fee record.
 * Each record links a specific student to a payment status for a particular month.
 * Only explicit markings (PAID or UNPAID) are persisted. Derived defaults (e.g., UNPAID after enrolment)
 * are not saved to storage.
 */
public class JsonAdaptedFeeRecord {

    private final String studentId;
    private final String monthString;
    private final String feeState;

    /** Jackson constructor for reading */
    @JsonCreator
    public JsonAdaptedFeeRecord(
        @JsonProperty("studentId") String studentId,
        @JsonProperty("monthString") String monthString,
        @JsonProperty("feeState") String feeState) {
        this.studentId = studentId;
        this.monthString = monthString;
        this.feeState = feeState;
    }

    /**
     * Converts a model FeeRecord into this JSON-friendly adapted object.
     */
    public JsonAdaptedFeeRecord(StudentId studentId, Month month, FeeState feeState) {
        requireNonNull(studentId);
        requireNonNull(month);
        requireNonNull(feeState);
        this.studentId = studentId.toString();
        this.monthString = month.toString();
        this.feeState = feeState.name();
    }

    /**
     * Applies this stored record to the given AddressBook’s FeeTracker.
     *
     * @throws IllegalValueException if any field is missing, invalid, or refers to a non-existent student.
     */
    public void applyToFeeTracker(AddressBook addressBook) throws IllegalValueException {
        requireNonNull(addressBook);
        if (studentId == null || studentId.isBlank()) {
            throw new IllegalValueException("FeeRecord is missing the studentId field.");
        }
        if (monthString == null || monthString.isBlank()) {
            throw new IllegalValueException("FeeRecord is missing the month field.");
        }
        if (feeState == null || feeState.isBlank()) {
            throw new IllegalValueException("FeeRecord is missing the state field.");
        }
        StudentId studentId;
        Month month;
        FeeState feeState;

        try {
            studentId = new StudentId(this.studentId);
        } catch (IllegalArgumentException exception) {
            throw new IllegalValueException("FeeRecord has an invalid studentId: " + this.studentId);
        }

        try {
            month = new Month(monthString);
        } catch (IllegalArgumentException exception) {
            throw new IllegalValueException("FeeRecord has an invalid month format (expected MMYY): " + monthString);
        }

        try {
            feeState = FeeState.valueOf(this.feeState);
        } catch (IllegalArgumentException exception) {
            throw new IllegalValueException("FeeRecord has an invalid state (expected PAID or UNPAID): "
                + this.feeState);
        }

        boolean studentExists = addressBook.getPersonList().stream().anyMatch(person
            -> person.getStudentId().equals(studentId));
        if (!studentExists) {
            throw new IllegalValueException("FeeRecord refers to a non-existent studentId: " + studentId);
        }

        // Apply record into the FeeTracker
        switch (feeState) {
        case PAID:
            addressBook.getFeeTracker().markPaid(studentId, month);
            break;
        case UNPAID:
            addressBook.getFeeTracker().markUnpaid(studentId, month);
            break;
        default:
            throw new IllegalValueException("Unsupported fee state: " + feeState);
        }
    }

    /**
     * Getter for studentID in string
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * Getter for month in string
     */
    public String getMonthString() {
        return monthString;
    }
    /**
     * Getter for fee state
     */
    public String getFeeState() {
        return feeState;
    }
}
