package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.exceptions.ExceedMaxStudentsException;

public class StudentIdTest {

    @Test
    public void constructor_defaultConstructor_exceedMax_throwsExceedMaxStudentsException() {
        // Simulate reaching the limit
        for (int i = 0; i <= 10000; i++) {
            new StudentId(String.format("%04d", i));
        }

        // Next student should exceed max
        assertThrows(ExceedMaxStudentsException.class, () -> new StudentId());
    }

    @Test
    public void constructor_invalidStudentId_throwsNumberFormatException() {
        // invalid student IDs
        assertThrows(NumberFormatException.class, () -> new StudentId("abcd"));
        assertThrows(NumberFormatException.class, () -> new StudentId("12 3"));
    }

    @Test
    public void isValidStudentId() {
        // null studentId
        assertThrows(NullPointerException.class, () -> StudentId.isValidStudentId(null));

        // invalid student IDs
        assertFalse(StudentId.isValidStudentId(""));       // empty
        assertFalse(StudentId.isValidStudentId(" "));      // spaces only
        assertFalse(StudentId.isValidStudentId("123"));    // less than 4 digits
        assertFalse(StudentId.isValidStudentId("12345"));  // more than 4 digits
        assertFalse(StudentId.isValidStudentId("12a4"));   // non-numeric characters

        // valid student IDs
        assertTrue(StudentId.isValidStudentId("0000"));
        assertTrue(StudentId.isValidStudentId("1234"));
        assertTrue(StudentId.isValidStudentId("9999"));
    }

    @Test
    public void equals() {
        StudentId id1 = new StudentId("0001");
        StudentId id2 = new StudentId("0001");
        StudentId id3 = new StudentId("0002");

        // same values -> returns true
        assertTrue(id1.equals(id2));

        // same object -> returns true
        assertTrue(id1.equals(id1));

        // null -> returns false
        assertFalse(id1.equals(null));

        // different types -> returns false
        assertFalse(id1.equals(1234));

        // different values -> returns false
        assertFalse(id1.equals(id3));
    }

    @Test
    public void toString_validFormat() {
        StudentId id = new StudentId("0075");
        assertTrue(id.toString().equals("0075"));
    }
}
