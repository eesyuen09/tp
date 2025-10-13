package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class DateTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Date(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidDate = "";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidDate));
    }


    @Test
    public void isValidDate() {
        // null date
        assertThrows(NullPointerException.class, () -> Date.isValidDate(null));

        // invalid dates - wrong format
        assertFalse(Date.isValidDate("")); // empty string
        assertFalse(Date.isValidDate(" ")); // spaces only
        assertFalse(Date.isValidDate("12-09-2025")); // wrong format with dashes
        assertFalse(Date.isValidDate("12/09/2025")); // wrong format with slashes
        assertFalse(Date.isValidDate("2025-09-12")); // wrong format YYYY-MM-DD
        assertFalse(Date.isValidDate("1209202")); // missing digit
        assertFalse(Date.isValidDate("120920255")); // extra digit
        assertFalse(Date.isValidDate("12092025a")); // contains letter

        // invalid dates - wrong day/month
        assertFalse(Date.isValidDate("00092025")); // day 00
        assertFalse(Date.isValidDate("32092025")); // day 32
        assertFalse(Date.isValidDate("12002025")); // month 00
        assertFalse(Date.isValidDate("12132025")); // month 13

        // invalid dates - logically invalid
        assertFalse(Date.isValidDate("31022025")); // Feb 31 doesn't exist
        assertFalse(Date.isValidDate("29022025")); // 2025 not leap year
        assertFalse(Date.isValidDate("31042025")); // April has 30 days
        assertFalse(Date.isValidDate("31062025")); // June has 30 days
        assertFalse(Date.isValidDate("31092025")); // September has 30 days
        assertFalse(Date.isValidDate("31112025")); // November has 30 days

        // valid dates
        assertTrue(Date.isValidDate("01012025")); // Jan 1
        assertTrue(Date.isValidDate("15092025")); // Sep 15
        assertTrue(Date.isValidDate("31122025")); // Dec 31
        assertTrue(Date.isValidDate("29022024")); // Feb 29 leap year
        assertTrue(Date.isValidDate("28022025")); // Feb 28 non-leap year
        assertTrue(Date.isValidDate("31012025")); // Jan 31
        assertTrue(Date.isValidDate("30042025")); // April 30
    }

    @Test
    public void getFormattedDate() {
        Date date = new Date("15092025");
        assertTrue(date.getFormattedDate().equals("15-09-2025"));

        Date date2 = new Date("01012025");
        assertTrue(date2.getFormattedDate().equals("01-01-2025"));
    }

    @Test
    public void equals() {
        Date date = new Date("15092025");

        // same values -> returns true
        assertTrue(date.equals(new Date("15092025")));

        // same object -> returns true
        assertTrue(date.equals(date));

        // null -> returns false
        assertFalse(date.equals(null));

        // different types -> returns false
        assertFalse(date.equals(5.0f));

        // different values -> returns false
        assertFalse(date.equals(new Date("16092025")));
    }

    @Test
    public void debugTest() {
        System.out.println("Testing 31022025: " + Date.isValidDate("31022025"));
        System.out.println("Testing 15092025: " + Date.isValidDate("15092025"));
    }
}
