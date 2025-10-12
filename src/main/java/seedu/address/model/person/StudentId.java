package seedu.address.model.person;

import seedu.address.model.person.exceptions.ExceedMaxStudentsException;

/**
 * Represents a Student ID in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidStudentId(String)}.
 */
public class StudentId {

    public static final String MESSAGE_CONSTRAINTS =
            "Student ID should be a 4-digit number, and it should not be blank";

    public static final String VALIDATION_REGEX = "\\d{4}";

    /** Tracks the most recently assigned student ID to ensure uniqueness. */
    private static int lastId = 0;

    /** The maximum allowed student ID number. */
    private static final int MAX = 9999;

    /** The numerical value of the student ID. */
    public final Integer value;


    /**
     * Constructs a new {@code StudentId} with an auto-generated 4-digit number.
     * <p>
     * Each time this constructor is called, {@code LAST_ID} is incremented by 1.
     * If the maximum number of student IDs (9999) is exceeded, an
     * {@link ExceedMaxStudentsException} is thrown.
     *
     * @throws ExceedMaxStudentsException if the number of students exceeds {@code MAX}.
     */
    public StudentId() {
        if (lastId > MAX) {
            throw new ExceedMaxStudentsException();
        }
        value = lastId++;
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
        if (value > lastId) {
            lastId = value + 1;
        }
    }

    public static void rollbackId() {
        lastId = lastId - 1;
    }

    /**
     * Returns true if a given string is a valid student id.
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
