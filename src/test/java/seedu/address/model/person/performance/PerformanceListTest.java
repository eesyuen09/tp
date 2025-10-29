package seedu.address.model.person.performance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.performance.exceptions.PerformanceNoteNotFoundException;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Date;

public class PerformanceListTest {

    private static final Date DATE_1 = new Date("17102025");
    private static final Date DATE_2 = new Date("18102025");
    private static final Date DATE_3 = new Date("19102025");

    private static final ClassTag CLASS_1 = new ClassTag("CS1010");
    private static final ClassTag CLASS_2 = new ClassTag("CS2030");
    private static final ClassTag CLASS_3 = new ClassTag("CS2103");

    private static final String NOTE_1 = "Scored 85% on mock test.";
    private static final String NOTE_2 = "Incomplete homework.";
    private static final String NOTE_3 = "Needs improvement in coding speed.";

    @Test
    public void constructor_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PerformanceList(null));
    }

    @Test
    public void constructor_withInitialNotes_success() {
        List<PerformanceNote> initial = new ArrayList<>();
        initial.add(new PerformanceNote(DATE_1, CLASS_1, NOTE_1));
        initial.add(new PerformanceNote(DATE_2, CLASS_2, NOTE_2));

        PerformanceList list = new PerformanceList(initial);
        assertEquals(2, list.size());
        assertEquals(NOTE_1, list.asUnmodifiableList().get(0).getNote());
    }

    @Test
    public void add_nullNote_throwsNullPointerException() {
        PerformanceList list = new PerformanceList();
        assertThrows(NullPointerException.class, () -> list.add(null));
    }

    @Test
    public void add_duplicateDateAndClassTag_throwsIllegalArgumentException() {
        PerformanceList list = new PerformanceList();
        list.add(new PerformanceNote(DATE_1, CLASS_1, NOTE_1));
        String messageDuplicateNote = "A performance note already exists for this date and class tag.";
        assertThrows(IllegalArgumentException.class,
                messageDuplicateNote, () -> list.add(new PerformanceNote(DATE_1, CLASS_1, NOTE_2)));
    }

    @Test
    public void add_validNote_success() {
        PerformanceList list = new PerformanceList();
        PerformanceNote note = new PerformanceNote(DATE_1, CLASS_1, NOTE_1);
        list.add(note);

        assertEquals(1, list.size());
        assertSame(note, list.asUnmodifiableList().get(0));
    }

    @Test
    public void add_notesStoredChronologically() {
        PerformanceList list = new PerformanceList();
        PerformanceNote latest = new PerformanceNote(DATE_3, CLASS_1, NOTE_1);
        PerformanceNote earliest = new PerformanceNote(DATE_1, CLASS_2, NOTE_2);
        PerformanceNote middle = new PerformanceNote(DATE_2, CLASS_3, NOTE_3);

        list.add(latest);
        list.add(earliest);
        list.add(middle);

        assertEquals(List.of(earliest, middle, latest), list.asUnmodifiableList());
    }

    @Test
    public void editPerformanceNote_valid_success() {
        PerformanceList list = new PerformanceList();
        list.add(new PerformanceNote(DATE_1, CLASS_1, NOTE_1));

        list.editPerformanceNote(DATE_1, CLASS_1, NOTE_2);
        assertEquals(NOTE_2, list.asUnmodifiableList().get(0).getNote());
    }

    @Test
    public void editPerformanceNote_nonexistent_throwsException() {
        PerformanceList list = new PerformanceList();
        list.add(new PerformanceNote(DATE_1, CLASS_1, NOTE_1));

        assertThrows(PerformanceNoteNotFoundException.class,
                null, () -> list.editPerformanceNote(DATE_2, CLASS_2, NOTE_3));
    }

    @Test
    public void remove_valid_success() {
        PerformanceList list = new PerformanceList();
        PerformanceNote note = new PerformanceNote(DATE_1, CLASS_1, NOTE_1);
        list.add(note);

        PerformanceNote removed = list.remove(DATE_1, CLASS_1);
        assertSame(note, removed);
        assertTrue(list.asUnmodifiableList().isEmpty());
    }

    @Test
    public void remove_nonexistent_throwsException() {
        PerformanceList list = new PerformanceList();
        list.add(new PerformanceNote(DATE_1, CLASS_1, NOTE_1));

        assertThrows(PerformanceNoteNotFoundException.class,
                null, () -> list.remove(DATE_2, CLASS_2));
    }

    @Test
    public void asUnmodifiableList_cannotModify() {
        PerformanceList list = new PerformanceList();
        list.add(new PerformanceNote(DATE_1, CLASS_1, NOTE_1));

        List<PerformanceNote> unmodifiable = list.asUnmodifiableList();
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.add(new PerformanceNote(DATE_2,
                CLASS_2, NOTE_2)));
    }

    @Test
    public void size_multipleNotes_returnsCount() {
        PerformanceList list = new PerformanceList();
        list.add(new PerformanceNote(DATE_1, CLASS_1, NOTE_1));
        list.add(new PerformanceNote(DATE_2, CLASS_2, NOTE_2));
        list.add(new PerformanceNote(DATE_3, CLASS_3, NOTE_3));

        assertEquals(3, list.size());
    }

    @Test
    public void equals_and_hashCode() {
        PerformanceList list1 = new PerformanceList();
        list1.add(new PerformanceNote(DATE_1, CLASS_1, NOTE_1));

        PerformanceList list2 = new PerformanceList();
        list2.add(new PerformanceNote(DATE_1, CLASS_1, NOTE_1));

        PerformanceList list3 = new PerformanceList();
        list3.add(new PerformanceNote(DATE_2, CLASS_2, NOTE_2));

        assertTrue(list1.equals(list2));
        assertFalse(list1.equals(list3));
        assertEquals(list1.hashCode(), list2.hashCode());
        assertNotEquals(list1.hashCode(), list3.hashCode());
    }
}
