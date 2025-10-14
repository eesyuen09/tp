package seedu.address.model.person.performance;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class PerformanceNote {
    public static final int MAX_NOTE_LEN = 200;
    private static final DateTimeFormatter IN = DateTimeFormatter.ofPattern("ddMMyyyy");
    private static final DateTimeFormatter OUT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final LocalDate date;
    private final String note;

    /**
     * Creates a PerformanceNote object with the given date and note.
     *
     * @param dateStr in the format DDMMYYYY
     * @param note the performance note, max length 200 characters
     */
    public PerformanceNote(String dateStr, String note) {
        requireNonNull(dateStr);
        requireNonNull(note);
        this.date = parse(dateStr);
        this.note = validateNote(note);
    }

    /**
     * Creates a PerformanceNote object with the given date and note.
     *
     * @param date in LocalDate format
     * @param note the performance note, max length 200 characters
     */
    public PerformanceNote(LocalDate date, String note) {
        requireNonNull(date);
        requireNonNull(note);
        this.date = notFuture(date);
        this.note = validateNote(note);
    }

    private static LocalDate parse(String s) {
        try {
            return notFuture(LocalDate.parse(s, IN));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use: DDMMYYYY");
        }
    }

    private static LocalDate notFuture(LocalDate d) {
        if (d.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot record performance for a future date.");
        }
        return d;
    }

    private static String validateNote(String n) {
        if (n.length() > MAX_NOTE_LEN) {
            throw new IllegalArgumentException("Error: performance note exceeds maximum length of 200 characters");
        }
        return n;
    }

    /**
     * Formats the date to a more human-readable format.
     *
     * @return the date in the format DD-MM-YYYY
     */
    public String printableDate() {
        return date.format(OUT);

    }

    /**
     * Gets the date of the performance note.
     *
     * @return the date as a LocalDate object
     */
    public LocalDate getDate() {
        return date;

    }

    /**
     * Gets the performance note.
     *
     * @return the performance note as a String
     */
    public String getNote() {
        return note;
    }

    /**
     * Checks if two PerformanceNote objects have the same date and note.
     *
     * @param other the other PerformanceNote to compare to
     * @return true if both PerformanceNote objects have the same date and note, false otherwise
     */
    public boolean isSameContent(PerformanceNote other) {
        return other != null && date.equals(other.date) && note.equals(other.note);
    }

    @Override
    public String toString() {
        return printableDate() + ": " + note;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof PerformanceNote p && date.equals(p.date) && note.equals(p.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, note);
    }
}
