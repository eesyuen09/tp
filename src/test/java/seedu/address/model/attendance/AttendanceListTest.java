package seedu.address.model.attendance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Date;

public class AttendanceListTest {

    private static final Date VALID_DATE_1 = new Date("01012024");
    private static final Date VALID_DATE_2 = new Date("02012024");
    private static final Date VALID_DATE_3 = new Date("03012024");
    private static final ClassTag VALID_CLASS_TAG_1 = new ClassTag("Math");
    private static final ClassTag VALID_CLASS_TAG_2 = new ClassTag("Science");

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AttendanceList(null));
    }

    @Test
    public void constructor_emptyList_success() {
        AttendanceList attendanceList = new AttendanceList();
        assertEquals(0, attendanceList.size());
    }

    @Test
    public void constructor_withInitialRecords_success() {
        List<Attendance> initialRecords = new ArrayList<>();
        initialRecords.add(new Attendance(VALID_DATE_1, VALID_CLASS_TAG_1, true));
        initialRecords.add(new Attendance(VALID_DATE_2, VALID_CLASS_TAG_1, false));

        AttendanceList attendanceList = new AttendanceList(initialRecords);
        assertEquals(2, attendanceList.size());
    }

    @Test
    public void hasAttendanceMarked_noRecords_returnsFalse() {
        AttendanceList attendanceList = new AttendanceList();
        assertFalse(attendanceList.hasAttendanceMarked(VALID_DATE_1, VALID_CLASS_TAG_1));
    }

    @Test
    public void hasAttendanceMarked_recordExists_returnsTrue() {
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.markAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);
        assertTrue(attendanceList.hasAttendanceMarked(VALID_DATE_1, VALID_CLASS_TAG_1));
    }

    @Test
    public void hasAttendanceMarked_differentDate_returnsFalse() {
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.markAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);
        assertFalse(attendanceList.hasAttendanceMarked(VALID_DATE_2, VALID_CLASS_TAG_1));
    }

    @Test
    public void hasAttendanceMarked_differentClassTag_returnsFalse() {
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.markAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);
        assertFalse(attendanceList.hasAttendanceMarked(VALID_DATE_1, VALID_CLASS_TAG_2));
    }

    @Test
    public void hasAttendanceUnmarked_noRecords_returnsFalse() {
        AttendanceList attendanceList = new AttendanceList();
        assertFalse(attendanceList.hasAttendanceUnmarked(VALID_DATE_1, VALID_CLASS_TAG_1));
    }

    @Test
    public void hasAttendanceUnmarked_recordExists_returnsTrue() {
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.unmarkAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);
        assertTrue(attendanceList.hasAttendanceUnmarked(VALID_DATE_1, VALID_CLASS_TAG_1));
    }

    @Test
    public void hasAttendanceUnmarked_differentDate_returnsFalse() {
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.unmarkAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);
        assertFalse(attendanceList.hasAttendanceUnmarked(VALID_DATE_2, VALID_CLASS_TAG_1));
    }

    @Test
    public void hasAttendanceUnmarked_differentClassTag_returnsFalse() {
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.unmarkAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);
        assertFalse(attendanceList.hasAttendanceUnmarked(VALID_DATE_1, VALID_CLASS_TAG_2));
    }

    @Test
    public void hasAttendanceUnmarked_markedAsPresent_returnsFalse() {
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.markAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);
        assertFalse(attendanceList.hasAttendanceUnmarked(VALID_DATE_1, VALID_CLASS_TAG_1));
    }

    @Test
    public void hasAttendanceUnmarked_null_throwsNullPointerException() {
        AttendanceList attendanceList = new AttendanceList();
        assertThrows(NullPointerException.class, () -> attendanceList.hasAttendanceUnmarked(null, VALID_CLASS_TAG_1));
        assertThrows(NullPointerException.class, () -> attendanceList.hasAttendanceUnmarked(VALID_DATE_1, null));
    }

    @Test
    public void markAttendance_null_throwsNullPointerException() {
        AttendanceList attendanceList = new AttendanceList();
        assertThrows(NullPointerException.class, () -> attendanceList.markAttendance(null, VALID_CLASS_TAG_1));
        assertThrows(NullPointerException.class, () -> attendanceList.markAttendance(VALID_DATE_1, null));
    }

    @Test
    public void markAttendance_validDate_success() {
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.markAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);

        assertEquals(1, attendanceList.size());
        assertTrue(attendanceList.hasAttendanceMarked(VALID_DATE_1, VALID_CLASS_TAG_1));
    }

    @Test
    public void markAttendance_duplicateDateAndClass_replacesExisting() {
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.markAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);
        attendanceList.markAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);

        // Should still have only 1 record (duplicate replaced)
        assertEquals(1, attendanceList.size());
    }

    @Test
    public void markAttendance_afterUnmark_replacesAbsentRecord() {
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.unmarkAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);
        attendanceList.markAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);

        // Should still have 1 record, but now marked as present
        assertEquals(1, attendanceList.size());
        List<Attendance> records = attendanceList.asUnmodifiableList();
        assertTrue(records.get(0).isStudentPresent());
    }

    @Test
    public void unmarkAttendance_null_throwsNullPointerException() {
        AttendanceList attendanceList = new AttendanceList();
        assertThrows(NullPointerException.class, () -> attendanceList.unmarkAttendance(null, VALID_CLASS_TAG_1));
        assertThrows(NullPointerException.class, () -> attendanceList.unmarkAttendance(VALID_DATE_1, null));
    }

    @Test
    public void unmarkAttendance_validDate_success() {
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.unmarkAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);

        assertEquals(1, attendanceList.size());
        List<Attendance> records = attendanceList.asUnmodifiableList();
        assertFalse(records.get(0).isStudentPresent());
    }

    @Test
    public void unmarkAttendance_afterMark_replacesRecord() {
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.markAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);
        attendanceList.unmarkAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);

        // Should still have 1 record, but now marked as absent
        assertEquals(1, attendanceList.size());
        List<Attendance> records = attendanceList.asUnmodifiableList();
        assertTrue(records.get(0).notPresent());
    }

    @Test
    public void asUnmodifiableList_returnsUnmodifiableList() {
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.markAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);

        List<Attendance> records = attendanceList.asUnmodifiableList();
        assertEquals(1, records.size());

        // Should throw UnsupportedOperationException when trying to modify
        assertThrows(UnsupportedOperationException.class, () ->
            records.add(new Attendance(VALID_DATE_2, VALID_CLASS_TAG_1, true)));
    }

    @Test
    public void size_emptyList_returnsZero() {
        AttendanceList attendanceList = new AttendanceList();
        assertEquals(0, attendanceList.size());
    }

    @Test
    public void size_multipleRecords_returnsCorrectSize() {
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.markAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);
        attendanceList.markAttendance(VALID_DATE_2, VALID_CLASS_TAG_1);
        attendanceList.markAttendance(VALID_DATE_3, VALID_CLASS_TAG_1);

        assertEquals(3, attendanceList.size());
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        AttendanceList attendanceList = new AttendanceList();
        assertTrue(attendanceList.equals(attendanceList));
    }

    @Test
    public void equals_null_returnsFalse() {
        AttendanceList attendanceList = new AttendanceList();
        assertFalse(attendanceList.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        AttendanceList attendanceList = new AttendanceList();
        assertFalse(attendanceList.equals("string"));
    }

    @Test
    public void equals_sameRecords_returnsTrue() {
        AttendanceList attendanceList1 = new AttendanceList();
        attendanceList1.markAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);
        attendanceList1.markAttendance(VALID_DATE_2, VALID_CLASS_TAG_1);

        AttendanceList attendanceList2 = new AttendanceList();
        attendanceList2.markAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);
        attendanceList2.markAttendance(VALID_DATE_2, VALID_CLASS_TAG_1);

        assertTrue(attendanceList1.equals(attendanceList2));
    }

    @Test
    public void equals_differentRecords_returnsFalse() {
        AttendanceList attendanceList1 = new AttendanceList();
        attendanceList1.markAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);

        AttendanceList attendanceList2 = new AttendanceList();
        attendanceList2.markAttendance(VALID_DATE_2, VALID_CLASS_TAG_1);

        assertFalse(attendanceList1.equals(attendanceList2));
    }

    @Test
    public void equals_emptyLists_returnsTrue() {
        AttendanceList attendanceList1 = new AttendanceList();
        AttendanceList attendanceList2 = new AttendanceList();

        assertTrue(attendanceList1.equals(attendanceList2));
    }

    @Test
    public void hashCode_sameRecords_returnsSameHashCode() {
        AttendanceList attendanceList1 = new AttendanceList();
        attendanceList1.markAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);

        AttendanceList attendanceList2 = new AttendanceList();
        attendanceList2.markAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);

        assertEquals(attendanceList1.hashCode(), attendanceList2.hashCode());
    }

    @Test
    public void hashCode_differentRecords_returnsDifferentHashCode() {
        AttendanceList attendanceList1 = new AttendanceList();
        attendanceList1.markAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);

        AttendanceList attendanceList2 = new AttendanceList();
        attendanceList2.markAttendance(VALID_DATE_2, VALID_CLASS_TAG_1);

        assertNotEquals(attendanceList1.hashCode(), attendanceList2.hashCode());
    }

    @Test
    public void deleteAttendance_null_throwsNullPointerException() {
        AttendanceList attendanceList = new AttendanceList();
        assertThrows(NullPointerException.class, () -> attendanceList.deleteAttendance(null, VALID_CLASS_TAG_1));
        assertThrows(NullPointerException.class, () -> attendanceList.deleteAttendance(VALID_DATE_1, null));
    }

    @Test
    public void deleteAttendance_existingRecord_success() {
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.markAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);

        assertEquals(1, attendanceList.size());
        assertTrue(attendanceList.hasAttendanceMarked(VALID_DATE_1, VALID_CLASS_TAG_1));

        attendanceList.deleteAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);

        assertEquals(0, attendanceList.size());
        assertFalse(attendanceList.hasAttendanceMarked(VALID_DATE_1, VALID_CLASS_TAG_1));
    }

    @Test
    public void deleteAttendance_nonExistingRecord_noChange() {
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.markAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);

        assertEquals(1, attendanceList.size());

        // Try to delete a non-existing record
        attendanceList.deleteAttendance(VALID_DATE_2, VALID_CLASS_TAG_1);

        // Size should remain the same
        assertEquals(1, attendanceList.size());
        assertTrue(attendanceList.hasAttendanceMarked(VALID_DATE_1, VALID_CLASS_TAG_1));
    }

    @Test
    public void deleteAttendance_multipleRecords_deletesOnlySpecified() {
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.markAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);
        attendanceList.markAttendance(VALID_DATE_2, VALID_CLASS_TAG_1);
        attendanceList.markAttendance(VALID_DATE_1, VALID_CLASS_TAG_2);

        assertEquals(3, attendanceList.size());

        // Delete only one specific record
        attendanceList.deleteAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);

        assertEquals(2, attendanceList.size());
        assertFalse(attendanceList.hasAttendanceMarked(VALID_DATE_1, VALID_CLASS_TAG_1));
        assertTrue(attendanceList.hasAttendanceMarked(VALID_DATE_2, VALID_CLASS_TAG_1));
        assertTrue(attendanceList.hasAttendanceMarked(VALID_DATE_1, VALID_CLASS_TAG_2));
    }

    @Test
    public void deleteAttendance_unmarkedRecord_success() {
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.unmarkAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);

        assertEquals(1, attendanceList.size());
        assertTrue(attendanceList.hasAttendanceUnmarked(VALID_DATE_1, VALID_CLASS_TAG_1));

        // Delete the unmarked (absent) record
        attendanceList.deleteAttendance(VALID_DATE_1, VALID_CLASS_TAG_1);

        assertEquals(0, attendanceList.size());
        assertFalse(attendanceList.hasAttendanceUnmarked(VALID_DATE_1, VALID_CLASS_TAG_1));
    }
}
