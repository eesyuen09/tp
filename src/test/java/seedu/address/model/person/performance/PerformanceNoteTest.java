package seedu.address.model.person.performance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Date;

public class PerformanceNoteTest {

    private static final Date VALID_DATE_1 = new Date("17102025");
    private static final Date VALID_DATE_2 = new Date("18102025");
    private static final String VALID_NOTE = "Scored 85% on mock test.";
    private static final ClassTag VALID_TAG_1 = new ClassTag("Math");
    private static final ClassTag VALID_TAG_2 = new ClassTag("Science");

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PerformanceNote(null, VALID_TAG_1, VALID_NOTE));
    }

    @Test
    public void constructor_nullClassTag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PerformanceNote(VALID_DATE_1, null, VALID_NOTE));
    }

    @Test
    public void constructor_nullNote_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PerformanceNote(VALID_DATE_1, VALID_TAG_1, null));
    }

    @Test
    public void constructor_noteTooLong_throwsIllegalArgumentException() {
        String overLengthNote = "a".repeat(PerformanceNote.MAX_NOTE_LEN + 1);
        assertThrows(IllegalArgumentException.class,
                PerformanceNote.MESSAGE_CONSTRAINTS, () -> new PerformanceNote(VALID_DATE_1,
                        VALID_TAG_1, overLengthNote));
    }

    @Test
    public void constructor_validInputs_success() {
        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_TAG_1, VALID_NOTE);
        assertEquals(VALID_DATE_1, note.getDate());
        assertEquals(VALID_NOTE, note.getNote());
        assertEquals(VALID_TAG_1, note.getClassTag());
    }

    @Test
    public void isSameContent() {
        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_TAG_1, VALID_NOTE);
        PerformanceNote sameContent = new PerformanceNote(VALID_DATE_1, VALID_TAG_1, VALID_NOTE);
        PerformanceNote differentDate = new PerformanceNote(VALID_DATE_2, VALID_TAG_1, VALID_NOTE);
        PerformanceNote differentTag = new PerformanceNote(VALID_DATE_1, VALID_TAG_2, VALID_NOTE);
        PerformanceNote differentNote = new PerformanceNote(VALID_DATE_1, VALID_TAG_1, "Different note");

        assertTrue(note.isSameContent(sameContent));
        assertFalse(note.isSameContent(differentDate));
        assertFalse(note.isSameContent(differentTag));
        assertFalse(note.isSameContent(differentNote));
        assertFalse(note.isSameContent(null));
    }

    @Test
    public void toString_returnsFormattedString() {
        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_TAG_1, VALID_NOTE);
        String expected = VALID_DATE_1.getFormattedDate() + " " + VALID_TAG_1.toString() + ": " + VALID_NOTE;
        assertEquals(expected, note.toString());
    }

    @Test
    public void equals() {
        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_TAG_1, VALID_NOTE);
        PerformanceNote sameValues = new PerformanceNote(VALID_DATE_1, VALID_TAG_1, VALID_NOTE);
        PerformanceNote differentDate = new PerformanceNote(VALID_DATE_2, VALID_TAG_1, VALID_NOTE);
        PerformanceNote differentNote = new PerformanceNote(VALID_DATE_1, VALID_TAG_1, "Different note");
        PerformanceNote differentTag = new PerformanceNote(VALID_DATE_1, VALID_TAG_2, VALID_NOTE);

        assertTrue(note.equals(note));
        assertTrue(note.equals(sameValues));
        assertFalse(note.equals(1));
        assertFalse(note.equals(null));
        assertFalse(note.equals(differentDate));
        assertFalse(note.equals(differentNote));
        assertFalse(note.equals(differentTag));
    }

    @Test
    public void hashCode_sameValues_sameHash() {
        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_TAG_1, VALID_NOTE);
        PerformanceNote sameValues = new PerformanceNote(VALID_DATE_1, VALID_TAG_1, VALID_NOTE);

        assertEquals(note.hashCode(), sameValues.hashCode());
    }

    @Test
    public void hashCode_differentValues_differentHash() {
        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_TAG_1, VALID_NOTE);
        PerformanceNote differentNote = new PerformanceNote(VALID_DATE_1, VALID_TAG_2, VALID_NOTE);

        assertNotEquals(note.hashCode(), differentNote.hashCode());
    }
}
