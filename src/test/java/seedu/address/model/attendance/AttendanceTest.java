package seedu.address.model.attendance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Date;

public class AttendanceTest {

    private static final Date VALID_DATE_1 = new Date("01012024");
    private static final Date VALID_DATE_2 = new Date("02012024");
    private static final ClassTag VALID_CLASS_TAG_1 = new ClassTag("Math");
    private static final ClassTag VALID_CLASS_TAG_2 = new ClassTag("Science");

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Attendance(null, VALID_CLASS_TAG_1, true));
        assertThrows(NullPointerException.class, () -> new Attendance(null, VALID_CLASS_TAG_1, false));
    }

    @Test
    public void constructor_nullClassTag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Attendance(VALID_DATE_1, null, true));
        assertThrows(NullPointerException.class, () -> new Attendance(VALID_DATE_1, null, false));
    }

    @Test
    public void constructor_validDatePresent_success() {
        Attendance attendance = new Attendance(VALID_DATE_1, VALID_CLASS_TAG_1, true);
        assertEquals(VALID_DATE_1, attendance.getDate());
        assertEquals(VALID_CLASS_TAG_1, attendance.getClassTag());
        assertTrue(attendance.isStudentPresent());
        assertFalse(attendance.notPresent());
    }

    @Test
    public void constructor_validDateAbsent_success() {
        Attendance attendance = new Attendance(VALID_DATE_1, VALID_CLASS_TAG_1, false);
        assertEquals(VALID_DATE_1, attendance.getDate());
        assertEquals(VALID_CLASS_TAG_1, attendance.getClassTag());
        assertFalse(attendance.isStudentPresent());
        assertTrue(attendance.notPresent());
    }

    @Test
    public void getDate_returnsCorrectDate() {
        Attendance attendance = new Attendance(VALID_DATE_1, VALID_CLASS_TAG_1, true);
        assertEquals(VALID_DATE_1, attendance.getDate());
    }

    @Test
    public void getClassTag_returnsCorrectClassTag() {
        Attendance attendance = new Attendance(VALID_DATE_1, VALID_CLASS_TAG_1, true);
        assertEquals(VALID_CLASS_TAG_1, attendance.getClassTag());
    }

    @Test
    public void isStudentPresent_whenPresent_returnsTrue() {
        Attendance attendance = new Attendance(VALID_DATE_1, VALID_CLASS_TAG_1, true);
        assertTrue(attendance.isStudentPresent());
    }

    @Test
    public void isStudentPresent_whenAbsent_returnsFalse() {
        Attendance attendance = new Attendance(VALID_DATE_1, VALID_CLASS_TAG_1, false);
        assertFalse(attendance.isStudentPresent());
    }

    @Test
    public void notPresent_whenPresent_returnsFalse() {
        Attendance attendance = new Attendance(VALID_DATE_1, VALID_CLASS_TAG_1, true);
        assertFalse(attendance.notPresent());
    }

    @Test
    public void notPresent_whenAbsent_returnsTrue() {
        Attendance attendance = new Attendance(VALID_DATE_1, VALID_CLASS_TAG_1, false);
        assertTrue(attendance.notPresent());
    }

    @Test
    public void equals() {
        Attendance presentAttendance1 = new Attendance(VALID_DATE_1, VALID_CLASS_TAG_1, true);
        Attendance absentAttendance1 = new Attendance(VALID_DATE_1, VALID_CLASS_TAG_1, false);
        Attendance presentAttendance2 = new Attendance(VALID_DATE_2, VALID_CLASS_TAG_1, true);
        Attendance presentAttendanceDiffClass = new Attendance(VALID_DATE_1, VALID_CLASS_TAG_2, true);

        // same object -> returns true
        assertTrue(presentAttendance1.equals(presentAttendance1));

        // same values -> returns true
        Attendance presentAttendance1Copy = new Attendance(VALID_DATE_1, VALID_CLASS_TAG_1, true);
        assertTrue(presentAttendance1.equals(presentAttendance1Copy));

        // different types -> returns false
        assertFalse(presentAttendance1.equals(1));

        // null -> returns false
        assertFalse(presentAttendance1.equals(null));

        // different date -> returns false
        assertFalse(presentAttendance1.equals(presentAttendance2));

        // different class tag -> returns false
        assertFalse(presentAttendance1.equals(presentAttendanceDiffClass));

        // different presence status -> returns false
        assertFalse(presentAttendance1.equals(absentAttendance1));
    }
}
