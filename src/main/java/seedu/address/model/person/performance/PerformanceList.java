package seedu.address.model.person.performance;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        if (indexOfDate(note.getDate()) != -1) {
            throw new IllegalArgumentException("A performance note already exists for this date.");
        }
        notes.add(note);
    }

    /**
     * Replaces the performance note at the given one-based index with the new note.
     *
     * @param oneBasedIndex One-based index of the performance note to be replaced.
     * @param newNote The new performance note.
     */
    public void set(int oneBasedIndex, PerformanceNote newNote) {
        int i = oneBasedIndex - 1;
        if (i < 0 || i >= notes.size()) {
            throw new IndexOutOfBoundsException("Error: Invalid performance note index.");
        }
        int clash = indexOfDate(newNote.getDate());
        if (clash != -1 && clash != i) {
            throw new IllegalArgumentException("A performance note already exists for this date.");
        }
        notes.set(i, newNote);
    }

    /**
     * Removes the performance note at the given one-based index.
     *
     * @param oneBasedIndex One-based index of the performance note to be removed.
     * @return The removed performance note.
     */
    public PerformanceNote remove(int oneBasedIndex) {
        int i = oneBasedIndex - 1;
        if (i < 0 || i >= notes.size()) {
            throw new IndexOutOfBoundsException("Error: Invalid performance note index.");
        }
        return notes.remove(i);
    }

    private int indexOfDate(LocalDate date) {
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getDate().equals(date)) {
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
}
