package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.person.Date;
import seedu.address.model.tag.ClassTag;

public class JsonAdaptedAttendanceTest {

    private static final String VALID_DATE = "13012025";
    private static final boolean VALID_IS_PRESENT = true;
    private static final String INVALID_DATE = "13-01-2025";
    private static final ClassTag VALID_CLASS_TAG = new ClassTag("Math");

    @Test
    public void toModelType_validAttendanceDetails_returnsAttendance() throws Exception {
        Attendance attendance = new Attendance(new Date(VALID_DATE), VALID_CLASS_TAG, VALID_IS_PRESENT);
        JsonAdaptedAttendance jsonAttendance = new JsonAdaptedAttendance(attendance);

        assertEquals(attendance, jsonAttendance.toModelType());
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {
        JsonAdaptedAttendance jsonAttendance =
                new JsonAdaptedAttendance(INVALID_DATE, VALID_CLASS_TAG.tagName, VALID_IS_PRESENT);

        assertThrows(IllegalValueException.class, jsonAttendance::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        JsonAdaptedAttendance jsonAttendance = new JsonAdaptedAttendance(null, VALID_CLASS_TAG.tagName,
                VALID_IS_PRESENT);

        assertThrows(IllegalValueException.class, jsonAttendance::toModelType);
    }
}

