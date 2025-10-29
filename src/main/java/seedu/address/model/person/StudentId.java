package seedu.address.model.person;

import seedu.address.model.person.exceptions.ExceedMaxStudentsException;

/**
 * Represents a Student ID in the address book.
 * Guarantees:
 *  - Immutable
 *  - Valid according to {@link #isValidStudentId(String)}
 */
public class StudentId {

    public static final String MESSAGE_CONSTRAINTS =
            "Student ID should be a 4-digit number, and it should not be blank";

    public static final String VALIDATION_REGEX = "\\d{4}";

    /** Tracks the most recently assigned student ID to ensure uniqueness. */
    private static int nextId = 0;

    /** The maximum allowed student ID number. */
    private static final int MAX = 9999;

    /** The numerical value of the student ID. */
    public final Integer value;


    /**
     * Constructs a new {@code StudentId} with an auto-generated 4-digit number.
     * <p>
     * The {@code nextId} is incremented automatically.
     * @throws ExceedMaxStudentsException if the number of students exceeds {@code MAX}.
     */
    public StudentId() {
        if (nextId > MAX) {
            throw new ExceedMaxStudentsException();
        }
        value = nextId++;
    }

    /**
     * Constructs a {@code StudentId} from a given string value.
     * <p>
     * If the provided student ID number is greater than the current {@code LAST_ID},
     * {@code LAST_ID} is updated to one more than this value to maintain unique IDs
     * for future auto-generated student IDs.
     *
     * @param studentId A string representing a valid 4-digit student ID.
     * @throws NumberFormatException if {@code studentId} is not a valid integer.
     */
    public StudentId(String studentId) {
        value = Integer.parseInt(studentId);
        if (value >= nextId) {
            nextId = value + 1;
        }
    }

    /**
     * Returns true if the given string is a valid student ID (4-digit number).
     *
     * @param test the string to test
     * @return true if {@code test} matches {@link #VALIDATION_REGEX}
     */
    public static boolean isValidStudentId(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return String.format("%04d", value);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof StudentId)) {
            return false;
        }

        StudentId otherStudentId = (StudentId) other;
        return value.equals(otherStudentId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
