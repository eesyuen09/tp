package seedu.address.model.person.performance;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PerformanceList {
    private final List<PerformanceNote> notes = new ArrayList<>();

    public PerformanceList() {}
    public PerformanceList(List<PerformanceNote> initial) { requireNonNull(initial); for (var n : initial) add(n); }

    public void add(PerformanceNote note) {
        requireNonNull(note);
        if (notes.stream().anyMatch(n -> n.isSameContent(note))) {
            throw new IllegalArgumentException("Duplicate performance note for the same date.");
        }
        notes.add(note);
    }

    public void set(int oneBasedIndex, PerformanceNote newNote) {
        int i = oneBasedIndex - 1;
        if (i < 0 || i >= notes.size()) {
            throw new IndexOutOfBoundsException("Error: Invalid performance note index.");
        }
        for (int k = 0; k < notes.size(); k++) if (k != i && notes.get(k).isSameContent(newNote)) {
            throw new IllegalArgumentException("Duplicate performance note for the same date.");
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

    public List<PerformanceNote> asUnmodifiableList() { return Collections.unmodifiableList(notes); }
    public int size() { return notes.size(); }
}
