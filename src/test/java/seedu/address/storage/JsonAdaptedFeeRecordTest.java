package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.fee.FeeState;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.time.Month;
import seedu.address.testutil.PersonBuilder;

/**
 * Unit tests for {@link JsonAdaptedFeeRecord}.
 */
public class JsonAdaptedFeeRecordTest {

    private AddressBook addressBook;
    private Person validPerson;
    private StudentId validStudentId;
    private Month validMonth;

    @BeforeEach
    public void setUp() {
        // Set up a minimal valid AddressBook with one student
        addressBook = new AddressBook();
        validPerson = new PersonBuilder().withName("Alex").withStudentId("0000").withEnrolledMonth("0825").build();
        addressBook.addPerson(validPerson);

        validStudentId = validPerson.getStudentId();
        validMonth = new Month("1025");
    }

    @Test
    public void applyToFeeTracker_validPaidRecord_success() {
        JsonAdaptedFeeRecord record =
            new JsonAdaptedFeeRecord(validStudentId, validMonth, FeeState.PAID);

        assertDoesNotThrow(() -> record.applyToFeeTracker(addressBook));

        // Verify that the tracker now contains a PAID record
        assertEquals(FeeState.PAID,
            addressBook.getFeeTracker()
                .getExplicitStatusOfMonth(validStudentId, validMonth)
                .orElseThrow(() -> new AssertionError("Fee not recorded")));
    }

    @Test
    public void applyToFeeTracker_validUnpaidRecord_success() {
        JsonAdaptedFeeRecord record =
            new JsonAdaptedFeeRecord(validStudentId, validMonth, FeeState.UNPAID);

        assertDoesNotThrow(() -> record.applyToFeeTracker(addressBook));

        assertEquals(FeeState.UNPAID,
            addressBook.getFeeTracker()
                .getExplicitStatusOfMonth(validStudentId, validMonth)
                .orElseThrow(() -> new AssertionError("Fee not recorded")));
    }

    @Test
    public void applyToFeeTracker_nullStudentId_throwsIllegalValueException() {
        JsonAdaptedFeeRecord record = new JsonAdaptedFeeRecord(
            null, validMonth.toString(), FeeState.PAID.name());
        assertThrows(IllegalValueException.class, () -> record.applyToFeeTracker(addressBook));
    }

    @Test
    public void applyToFeeTracker_blankStudentId_throwsIllegalValueException() {
        JsonAdaptedFeeRecord record = new JsonAdaptedFeeRecord(
            "   ", validMonth.toString(), FeeState.PAID.name());
        assertThrows(IllegalValueException.class, () -> record.applyToFeeTracker(addressBook));
    }

    @Test
    public void applyToFeeTracker_nullMonth_throwsIllegalValueException() {
        JsonAdaptedFeeRecord record = new JsonAdaptedFeeRecord(
            validStudentId.toString(), null, FeeState.PAID.name());
        assertThrows(IllegalValueException.class, () -> record.applyToFeeTracker(addressBook));
    }

    @Test
    public void applyToFeeTracker_blankMonth_throwsIllegalValueException() {
        JsonAdaptedFeeRecord record = new JsonAdaptedFeeRecord(
            validStudentId.toString(), "   ", FeeState.PAID.name());
        assertThrows(IllegalValueException.class, () -> record.applyToFeeTracker(addressBook));
    }

    @Test
    public void applyToFeeTracker_nullState_throwsIllegalValueException() {
        JsonAdaptedFeeRecord record = new JsonAdaptedFeeRecord(
            validStudentId.toString(), validMonth.toString(), null);
        assertThrows(IllegalValueException.class, () -> record.applyToFeeTracker(addressBook));
    }

    @Test
    public void applyToFeeTracker_blankState_throwsIllegalValueException() {
        JsonAdaptedFeeRecord record = new JsonAdaptedFeeRecord(
            validStudentId.toString(), validMonth.toString(), "  ");
        assertThrows(IllegalValueException.class, () -> record.applyToFeeTracker(addressBook));
    }

    @Test
    public void applyToFeeTracker_invalidStudentIdFormat_throwsIllegalValueException() {
        JsonAdaptedFeeRecord record = new JsonAdaptedFeeRecord(
            "invalid_id!", validMonth.toString(), FeeState.PAID.name());
        assertThrows(IllegalValueException.class, () -> record.applyToFeeTracker(addressBook));
    }

    @Test
    public void applyToFeeTracker_invalidMonthFormat_throwsIllegalValueException() {
        JsonAdaptedFeeRecord record = new JsonAdaptedFeeRecord(
            validStudentId.toString(), "13ZZ", FeeState.PAID.name());
        assertThrows(IllegalValueException.class, () -> record.applyToFeeTracker(addressBook));
    }

    @Test
    public void applyToFeeTracker_invalidState_throwsIllegalValueException() {
        JsonAdaptedFeeRecord record = new JsonAdaptedFeeRecord(
            validStudentId.toString(), validMonth.toString(), "PARTIAL");
        assertThrows(IllegalValueException.class, () -> record.applyToFeeTracker(addressBook));
    }

    @Test
    public void applyToFeeTracker_nonExistentStudent_throwsIllegalValueException() {
        JsonAdaptedFeeRecord record = new JsonAdaptedFeeRecord(
            new StudentId("0123"), validMonth, FeeState.PAID);
        assertThrows(IllegalValueException.class, () -> record.applyToFeeTracker(addressBook));
    }

    @Test
    public void getters_returnCorrectValues() {
        JsonAdaptedFeeRecord record =
            new JsonAdaptedFeeRecord(validStudentId, validMonth, FeeState.PAID);
        assertEquals(validStudentId.toString(), record.getStudentId());
        assertEquals(validMonth.toString(), record.getMonthString());
        assertEquals(FeeState.PAID.name(), record.getFeeState());
    }
}
