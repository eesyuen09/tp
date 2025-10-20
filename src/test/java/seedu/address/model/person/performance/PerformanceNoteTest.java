package seedu.address.model.person.performance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Date;

public class PerformanceNoteTest {

    private static final Date VALID_DATE_1 = new Date("17102025");
    private static final Date VALID_DATE_2 = new Date("18102025");
    private static final String VALID_NOTE = "Scored 85% on mock test.";

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PerformanceNote(null, VALID_NOTE));
    }

    @Test
    public void constructor_nullNote_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PerformanceNote(VALID_DATE_1, null));
    }

    @Test
    public void constructor_noteTooLong_throwsIllegalArgumentException() {
        String overLengthNote = "a".repeat(PerformanceNote.MAX_NOTE_LEN + 1);
        assertThrows(IllegalArgumentException.class,
                "Error: performance note exceeds maximum length of 200 characters", () ->
                        new PerformanceNote(VALID_DATE_1, overLengthNote));
    }

    @Test
    public void constructor_validInputs_success() {
        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_NOTE);
        assertEquals(VALID_DATE_1, note.getDate());
        assertEquals(VALID_NOTE, note.getNote());
    }

    @Test
    public void isSameContent() {
        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_NOTE);
        PerformanceNote sameContent = new PerformanceNote(VALID_DATE_1, VALID_NOTE);
        PerformanceNote differentDate = new PerformanceNote(VALID_DATE_2, VALID_NOTE);
        PerformanceNote differentNote = new PerformanceNote(VALID_DATE_1, "Different note");

        assertTrue(note.isSameContent(sameContent));
        assertFalse(note.isSameContent(differentDate));
        assertFalse(note.isSameContent(differentNote));
        assertFalse(note.isSameContent(null));
    }

    @Test
    public void toString_returnsFormattedString() {
        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_NOTE);
        String expected = VALID_DATE_1.getFormattedDate() + ": " + VALID_NOTE;
        assertEquals(expected, note.toString());
    }

    @Test
    public void equals() {
        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_NOTE);
        PerformanceNote sameValues = new PerformanceNote(VALID_DATE_1, VALID_NOTE);
        PerformanceNote differentDate = new PerformanceNote(VALID_DATE_2, VALID_NOTE);
        PerformanceNote differentNote = new PerformanceNote(VALID_DATE_1, "Different note");

        assertTrue(note.equals(note));
        assertTrue(note.equals(sameValues));
        assertFalse(note.equals(1));
        assertFalse(note.equals(null));
        assertFalse(note.equals(differentDate));
        assertFalse(note.equals(differentNote));
    }

    @Test
    public void hashCode_sameValues_sameHash() {
        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_NOTE);
        PerformanceNote sameValues = new PerformanceNote(VALID_DATE_1, VALID_NOTE);

        assertEquals(note.hashCode(), sameValues.hashCode());
    }

    @Test
    public void hashCode_differentValues_differentHash() {
        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_NOTE);
        PerformanceNote differentNote = new PerformanceNote(VALID_DATE_1, "Different note");

        assertNotEquals(note.hashCode(), differentNote.hashCode());
    }
}
