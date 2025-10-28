package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.performance.PerformanceNote;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Date;

public class JsonAdaptedPerformanceNoteTest {

    private static final String VALID_DATE = "19102025";
    private static final String VALID_CLASS_TAG = "CS2103T";
    private static final String VALID_NOTE = "Scored 85% on mock test.";

    private static final String INVALID_DATE = "19-10-2025";
    private static final String INVALID_CLASS_TAG = "CS@1010"; // invalid due to special char
    private static final String LONG_NOTE = "x".repeat(PerformanceNote.MAX_NOTE_LEN + 1);

    @Test
    public void toModelType_validPerformanceNoteDetails_returnsPerformanceNote() throws Exception {
        PerformanceNote note = new PerformanceNote(new Date(VALID_DATE), new ClassTag(VALID_CLASS_TAG), VALID_NOTE);
        JsonAdaptedPerformanceNote jsonNote = new JsonAdaptedPerformanceNote(note);
        assertEquals(note, jsonNote.toModelType());
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {
        JsonAdaptedPerformanceNote jsonNote =
                new JsonAdaptedPerformanceNote(INVALID_DATE, VALID_CLASS_TAG, VALID_NOTE);
        assertThrows(IllegalValueException.class, jsonNote::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        JsonAdaptedPerformanceNote jsonNote =
                new JsonAdaptedPerformanceNote(null, VALID_CLASS_TAG, VALID_NOTE);
        assertThrows(IllegalValueException.class, jsonNote::toModelType);
    }

    @Test
    public void toModelType_invalidClassTag_throwsIllegalValueException() {
        JsonAdaptedPerformanceNote jsonNote =
                new JsonAdaptedPerformanceNote(VALID_DATE, INVALID_CLASS_TAG, VALID_NOTE);
        assertThrows(IllegalValueException.class, jsonNote::toModelType);
    }

    @Test
    public void toModelType_nullClassTag_throwsIllegalValueException() {
        JsonAdaptedPerformanceNote jsonNote =
                new JsonAdaptedPerformanceNote(VALID_DATE, null, VALID_NOTE);
        assertThrows(IllegalValueException.class, jsonNote::toModelType);
    }

    @Test
    public void toModelType_noteTooLong_throwsIllegalValueException() {
        // Assuming your PerformanceNote constructor or validation handles note length internally.
        JsonAdaptedPerformanceNote jsonNote =
                new JsonAdaptedPerformanceNote(VALID_DATE, VALID_CLASS_TAG, LONG_NOTE);
        assertThrows(IllegalArgumentException.class, jsonNote::toModelType);
    }

    @Test
    public void toModelType_nullNote_throwsIllegalValueException() {
        JsonAdaptedPerformanceNote jsonNote =
                new JsonAdaptedPerformanceNote(VALID_DATE, VALID_CLASS_TAG, null);
        assertThrows(IllegalValueException.class, jsonNote::toModelType);
    }
}
