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

import seedu.address.model.person.Date;

public class PerformanceListTest {

    private static final Date VALID_DATE_1 = new Date("17102025");
    private static final Date VALID_DATE_2 = new Date("18102025");
    private static final Date VALID_DATE_3 = new Date("19102025");
    private static final String NOTE_1 = "Scored 85% on mock test.";
    private static final String NOTE_2 = "Incomplete homework.";
    private static final String NOTE_3 = "Need to revise this topic more.";

    @Test
    public void constructor_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PerformanceList(null));
    }

    @Test
    public void constructor_withInitialNotes_success() {
        List<PerformanceNote> initial = new ArrayList<>();
        initial.add(new PerformanceNote(VALID_DATE_1, NOTE_1));
        initial.add(new PerformanceNote(VALID_DATE_2, NOTE_2));

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
    public void add_duplicateDate_throwsIllegalArgumentException() {
        PerformanceList list = new PerformanceList();
        list.add(new PerformanceNote(VALID_DATE_1, NOTE_1));
        assertThrows(IllegalArgumentException.class,
                "A performance note already exists for this date.",
                () -> list.add(new PerformanceNote(VALID_DATE_1, NOTE_2)));
    }

    @Test
    public void add_validNote_success() {
        PerformanceList list = new PerformanceList();
        PerformanceNote note = new PerformanceNote(VALID_DATE_1, NOTE_1);
        list.add(note);

        assertEquals(1, list.size());
        assertSame(note, list.asUnmodifiableList().get(0));
    }

    @Test
    public void set_invalidIndex_throwsIndexOutOfBoundsException() {
        PerformanceList list = new PerformanceList();
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(1, new PerformanceNote(VALID_DATE_1, NOTE_1)));
    }

    @Test
    public void set_duplicateDate_throwsIllegalArgumentException() {
        PerformanceList list = new PerformanceList();
        list.add(new PerformanceNote(VALID_DATE_1, NOTE_1));
        list.add(new PerformanceNote(VALID_DATE_2, NOTE_2));

        assertThrows(IllegalArgumentException.class,
                "A performance note already exists for this date.",
                () -> list.set(1, new PerformanceNote(VALID_DATE_2, NOTE_3)));
    }

    @Test
    public void set_validIndex_success() {
        PerformanceList list = new PerformanceList();
        list.add(new PerformanceNote(VALID_DATE_1, NOTE_1));
        PerformanceNote replacement = new PerformanceNote(VALID_DATE_1, NOTE_2);

        list.set(1, replacement);
        assertEquals(NOTE_2, list.asUnmodifiableList().get(0).getNote());
    }

    @Test
    public void remove_invalidIndex_throwsIndexOutOfBoundsException() {
        PerformanceList list = new PerformanceList();
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
    }

    @Test
    public void remove_validIndex_returnsRemovedNote() {
        PerformanceList list = new PerformanceList();
        PerformanceNote note = new PerformanceNote(VALID_DATE_1, NOTE_1);
        list.add(note);

        PerformanceNote removed = list.remove(1);
        assertSame(note, removed);
        assertTrue(list.asUnmodifiableList().isEmpty());
    }

    @Test
    public void asUnmodifiableList_cannotModify() {
        PerformanceList list = new PerformanceList();
        list.add(new PerformanceNote(VALID_DATE_1, NOTE_1));

        List<PerformanceNote> unmodifiable = list.asUnmodifiableList();
        assertThrows(UnsupportedOperationException.class,
                () -> unmodifiable.add(new PerformanceNote(VALID_DATE_2, NOTE_2)));
    }

    @Test
    public void size_multipleNotes_returnsCount() {
        PerformanceList list = new PerformanceList();
        list.add(new PerformanceNote(VALID_DATE_1, NOTE_1));
        list.add(new PerformanceNote(VALID_DATE_2, NOTE_2));
        list.add(new PerformanceNote(VALID_DATE_3, NOTE_3));

        assertEquals(3, list.size());
    }

    @Test
    public void equals() {
        PerformanceList list = new PerformanceList();
        list.add(new PerformanceNote(VALID_DATE_1, NOTE_1));

        PerformanceList sameValues = new PerformanceList();
        sameValues.add(new PerformanceNote(VALID_DATE_1, NOTE_1));
        PerformanceList differentValues = new PerformanceList();
        differentValues.add(new PerformanceNote(VALID_DATE_2, NOTE_2));

        assertTrue(list.equals(list));
        assertTrue(list.equals(sameValues));
        assertFalse(list.equals(null));
        assertFalse(list.equals(1));
        assertFalse(list.equals(differentValues));
    }

    @Test
    public void hashCode_sameValues_sameHash() {
        PerformanceList list = new PerformanceList();
        list.add(new PerformanceNote(VALID_DATE_1, NOTE_1));

        PerformanceList sameValues = new PerformanceList();
        sameValues.add(new PerformanceNote(VALID_DATE_1, NOTE_1));

        assertEquals(list.hashCode(), sameValues.hashCode());
    }

    @Test
    public void hashCode_differentValues_differentHash() {
        PerformanceList list = new PerformanceList();
        list.add(new PerformanceNote(VALID_DATE_1, NOTE_1));

        PerformanceList differentValues = new PerformanceList();
        differentValues.add(new PerformanceNote(VALID_DATE_2, NOTE_2));

        assertNotEquals(list.hashCode(), differentValues.hashCode());
    }
}
