package seedu.address.model.person.performance;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import seedu.address.model.person.Date;
import seedu.address.model.person.performance.exceptions.PerformanceNoteNotFoundException;
import seedu.address.model.tag.ClassTag;

/**
 * A list of performance notes.
 */
public class PerformanceList {
    private final List<PerformanceNote> notes = new ArrayList<>();

    /**
     * Creates an empty PerformanceList.
     */
    public PerformanceList() {}

    /**
     * Creates a PerformanceList with the given initial notes.
     *
     * @param initial Initial list of performance notes.
     */
    public PerformanceList(List<PerformanceNote> initial) {
        requireNonNull(initial);
        for (var n : initial) {
            add(n);
        }
    }

    /**
     * Adds a performance note to the list.
     *
     * @param note The performance note to be added.
     */
    public void add(PerformanceNote note) {
        requireNonNull(note);
        if (duplivateValidation(note.getDate(), note.getClassTag()) != -1) {
            throw new IllegalArgumentException("A performance note already exists for this date and class tag.");
        }
        notes.add(note);
    }

    /**
     * Edits an existing performance note identified by its date and class tag.
     *
     * @param date The date of the performance note to edit.
     * @param classTag The class tag of the performance note to edit.
     * @param newNote The new note content.
     * @throws IllegalArgumentException if no matching performance note is found.
     */
    public void editPerformanceNote(Date date, ClassTag classTag, String newNote) {
        for (int i = 0; i < notes.size(); i++) {
            PerformanceNote note = notes.get(i);
            if (note.getDate().equals(date) && note.getClassTag().equals(classTag)) {
                PerformanceNote edited = new PerformanceNote(date, classTag, newNote);
                notes.set(i, edited);
                return;
            }
        }
        throw new PerformanceNoteNotFoundException();
    }



    /**
     * Removes the performance note matching the given date and class tag.
     *
     * @param date The date of the performance note to remove.
     * @param classTag The class tag of the performance note to remove.
     * @return The removed {@code PerformanceNote}.
     * @throws IllegalArgumentException if no matching performance note is found.
     */
    public PerformanceNote remove(Date date, ClassTag classTag) {
        for (int i = 0; i < notes.size(); i++) {
            PerformanceNote note = notes.get(i);
            if (note.getDate().equals(date) && note.getClassTag().equals(classTag)) {
                return notes.remove(i);
            }
        }
        throw new PerformanceNoteNotFoundException();
    }

    private int duplivateValidation(Date date, ClassTag classTag) {
        for (int i = 0; i < notes.size(); i++) {
            PerformanceNote note = notes.get(i);
            if (note.getDate().equals(date) && note.getClassTag().equals(classTag)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the performance notes as an unmodifiable list.
     *
     * @return Unmodifiable list of performance notes.
     */
    public List<PerformanceNote> asUnmodifiableList() {
        return Collections.unmodifiableList(notes);
    }

    /**
     * Returns the number of performance notes in the list.
     *
     * @return Number of performance notes.
     */
    public int size() {
        return notes.size();
    }

    @Override
    public int hashCode() {
        return notes.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PerformanceList)) {
            return false;
        }

        PerformanceList otherPerformamnceList = (PerformanceList) other;
        return notes.equals(otherPerformamnceList.notes);
    }
}
