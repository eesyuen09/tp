package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class MonthTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Month(null));
    }

    @Test
    public void constructor_invalidMonth_throwsIllegalArgumentException() {
        // empty
        assertThrows(IllegalArgumentException.class, () -> new Month(""));
        // spaces
        assertThrows(IllegalArgumentException.class, () -> new Month(" "));
        // non-digits
        assertThrows(IllegalArgumentException.class, () -> new Month("AB25"));
        assertThrows(IllegalArgumentException.class, () -> new Month("09A5"));
        // wrong length
        assertThrows(IllegalArgumentException.class, () -> new Month("925")); // 3 digits
        assertThrows(IllegalArgumentException.class, () -> new Month("09255")); // 5 digits
        // invalid month range
        assertThrows(IllegalArgumentException.class, () -> new Month("0025")); // 00 not allowed
        assertThrows(IllegalArgumentException.class, () -> new Month("1325")); // 13 not allowed
    }

    @Test
    public void isValidMonth_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Month.isValidMonth(null));
    }

    @Test
    public void isValidMonth_invalidStrings_returnsFalse() {
        // empty / spaces
        assertFalse(Month.isValidMonth(""));
        assertFalse(Month.isValidMonth(" "));
        // non-digits
        assertFalse(Month.isValidMonth("9/25"));
        assertFalse(Month.isValidMonth("0a25"));
        assertFalse(Month.isValidMonth("AB25"));
        // wrong length
        assertFalse(Month.isValidMonth("925"));
        assertFalse(Month.isValidMonth("09255"));
        // invalid month boundary
        assertFalse(Month.isValidMonth("0025"));
        assertFalse(Month.isValidMonth("1325"));
        // stray spaces
        assertFalse(Month.isValidMonth(" 0925 "));
    }

    @Test
    public void isValidMonth_validStrings_returnsTrue() {
        assertTrue(Month.isValidMonth("0124")); // Jan 2024
        assertTrue(Month.isValidMonth("0224")); // Feb 2024
        assertTrue(Month.isValidMonth("0925")); // Sep 2025
        assertTrue(Month.isValidMonth("1229")); // Dec 2029
        assertTrue(Month.isValidMonth("1111")); // Nov '11 (interpretation depends on class, but format OK)
        assertTrue(Month.isValidMonth("0199")); // Jan '99 (format OK)
    }

    @Test
    public void equalsAndHashCode() {
        Month m1 = new Month("0925");
        Month m2 = new Month("0925");
        Month m3 = new Month("1025");

        // same value
        assertTrue(m1.equals(m2));
        assertEquals(m1.hashCode(), m2.hashCode());

        // self
        assertTrue(m1.equals(m1));

        // null / different type
        assertFalse(m1.equals(null));
        assertFalse(m1.equals("0925"));

        // different value
        assertFalse(m1.equals(m3));
    }

    @Test
    public void toString_returnsStoredValue() {
        Month m = new Month("0925");
        assertEquals("0925", m.toString());
    }

    @Test
    public void toHumanReadable_validMonths_correctFormatting() {
        // Standard test cases
        Month jan = new Month("0124");
        assertEquals("January 2024", jan.toHumanReadable());

        Month sep = new Month("0925");
        assertEquals("September 2025", sep.toHumanReadable());

        Month dec = new Month("1229");
        assertEquals("December 2029", dec.toHumanReadable());

        Month nov = new Month("1122");
        assertEquals("November 2022", nov.toHumanReadable());
    }
}
