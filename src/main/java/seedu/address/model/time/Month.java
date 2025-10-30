package seedu.address.model.time;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/**
 * Represents a Month (billing month) in MMYY format, e.g. "0925" = September 2025.
 * Guarantees: immutable; is valid as declared in {@link #isValidMonth(String)}.
 */
public final class Month {

    /**
     * Accepts exactly 4 digits. Further semantic checks (01–12 for month) are done in {@link #isValidMonth(String)}.
     */
    public static final String VALIDATION_REGEX = "\\d{4}";

    public static final String MESSAGE_CONSTRAINTS =
        "Month must be exactly 4 digits in MMYY format: "
            + "MM is 01–12 and YY is 00–99 (interpreted as years 2000–2099). "
            + "Example: 0925 = September 2025.";

    public static final String MESSAGE_INVALID_FUTURE_ENROLLED_MONTH = "Enrolled month cannot be in the future.";

    // Human-readable formatter, e.g., "September 2025"
    private static final DateTimeFormatter HUMAN = DateTimeFormatter.ofPattern("MMMM yyyy");

    // We interpret YY as 2000–2099 to keep things simple and future-proof for this app.
    private static final int YEAR_BASE = 2000;

    private final YearMonth yearMonth;

    /**
     * Constructs a {@code Month} from a MMYY string.
     */
    public Month(String mmYY) {
        requireNonNull(mmYY);
        checkArgument(isValidMonth(mmYY), MESSAGE_CONSTRAINTS);
        int mm = Integer.parseInt(mmYY.substring(0, 2));
        int yy = Integer.parseInt(mmYY.substring(2, 4));
        this.yearMonth = YearMonth.of(YEAR_BASE + yy, mm);
    }

    /**
     * Returns true if a given string is a valid month in MMYY format.
     * Only checks for 4 digits and that MM is 01–12. YY (00–99) is accepted and mapped to 2000–2099.
     */
    public static boolean isValidMonth(String test) {
        requireNonNull(test);
        if (!test.matches(VALIDATION_REGEX)) {
            return false;
        }
        int mm = Integer.parseInt(test.substring(0, 2));
        // 01–12 are valid months
        return mm >= 1 && mm <= 12;
    }

    /** Returns the canonical storage representation "MMYY". */
    @Override
    public String toString() {
        int mm = yearMonth.getMonthValue();
        int yy = yearMonth.getYear() - YEAR_BASE; // back to 00–99
        return String.format("%02d%02d", mm, yy);
    }

    /** Returns a human-friendly representation, e.g., "September 2025". */
    public String toHumanReadable() {
        return HUMAN.format(yearMonth);
    }

    /** Exposes the underlying YearMonth if needed elsewhere. */
    public YearMonth toYearMonth() {
        return yearMonth;
    }

    /** Current month in MMYY. */
    public static Month now() {
        YearMonth ym = YearMonth.now();
        String mm = String.format("%02d", ym.getMonthValue());
        String yy = String.format("%02d", ym.getYear() % 100);
        return new Month(mm + yy);
    }

    /**
     * Returns a new {@code Month} instance offset by the specified number of months.
     * Year rollover is handled automatically.
     *
     * @param offset number of months to add (negative to subtract)
     * @return a new {@code Month} that is {@code const} months from this one
     *      Eg: new Month("0925").plusMonths(1) // "1025"
     */
    public Month plusMonths(int offset) {
        YearMonth ym = yearMonth.plusMonths(offset);
        String mm = String.format("%02d", ym.getMonthValue());
        String yy = String.format("%02d", ym.getYear() % 100);
        return new Month(mm + yy);
    }

    /**
     * Returns a boolean to check if the other month is before this month.
     */
    public boolean isBefore(Month other) {
        requireNonNull(other);
        return this.yearMonth.isBefore(other.yearMonth);
    }

    /**
     * Returns a boolean to check if the other month is after this month.
     */
    public boolean isAfter(Month other) {
        requireNonNull(other);
        return this.yearMonth.isAfter(other.yearMonth);
    }

    /**
     * Factory methods for constructing {@code Month} objects.
     */
    public static Month of(String monthYear) {
        return new Month(monthYear);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Month)) {
            return false;
        }
        Month otherMonth = (Month) other;
        return yearMonth.equals(otherMonth.yearMonth);
    }

    @Override
    public int hashCode() {
        return yearMonth.hashCode();
    }
}

