package seedu.address.model.person.performance;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PerformanceList {
    private final List<PerformanceNote> notes = new ArrayList<>();

    public PerformanceList() {}
    public PerformanceList(List<PerformanceNote> initial) { requireNonNull(initial); for (var n : initial) add(n); }

    public void add(PerformanceNote note) {
        requireNonNull(note);
        if (indexOfDate(note.getDate()) != -1) {
            throw new IllegalArgumentException("A performance note already exists for this date.");
        }
        notes.add(note);
    }

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

    public PerformanceNote remove(int oneBasedIndex) {
        int i = oneBasedIndex - 1;
        if (i < 0 || i >= notes.size()) {
            throw new IndexOutOfBoundsException("Error: Invalid performance note index.");
        }
        return notes.remove(i);
    }
    private int indexOfDate(LocalDate date) {
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getDate().equals(date)) return i;
        }
        return -1;
    }

    public List<PerformanceNote> asUnmodifiableList() { return Collections.unmodifiableList(notes); }
    public int size() { return notes.size(); }
}
