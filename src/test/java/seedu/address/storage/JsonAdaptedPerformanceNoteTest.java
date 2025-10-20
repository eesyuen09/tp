package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Date;
import seedu.address.model.person.performance.PerformanceNote;

public class JsonAdaptedPerformanceNoteTest {

    private static final String VALID_DATE = "19102025";
    private static final String VALID_NOTE = "Scored 85% on mock test.";
    private static final String INVALID_DATE = "19-10-2025";
    private static final String LONG_NOTE = "x".repeat(PerformanceNote.MAX_NOTE_LEN + 1);

    @Test
    public void toModelType_validPerformanceNoteDetails_returnsPerformanceNote() {
        PerformanceNote note = new PerformanceNote(new Date(VALID_DATE), VALID_NOTE);
        JsonAdaptedPerformanceNote jsonNote = new JsonAdaptedPerformanceNote(note);

        assertEquals(note, jsonNote.toModelType());
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalArgumentException() {
        JsonAdaptedPerformanceNote jsonNote = new JsonAdaptedPerformanceNote(INVALID_DATE, VALID_NOTE);
        assertThrows(IllegalArgumentException.class, jsonNote::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsNullPointerException() {
        JsonAdaptedPerformanceNote jsonNote = new JsonAdaptedPerformanceNote(null, VALID_NOTE);
        assertThrows(NullPointerException.class, jsonNote::toModelType);
    }

    @Test
    public void toModelType_noteTooLong_throwsIllegalArgumentException() {
        JsonAdaptedPerformanceNote jsonNote = new JsonAdaptedPerformanceNote(VALID_DATE, LONG_NOTE);
        assertThrows(IllegalArgumentException.class, jsonNote::toModelType);
    }

    @Test
    public void toModelType_nullNote_throwsNullPointerException() {
        JsonAdaptedPerformanceNote jsonNote = new JsonAdaptedPerformanceNote(VALID_DATE, null);
        assertThrows(NullPointerException.class, jsonNote::toModelType);
    }
}