package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        // empty string
        assertThrows(IllegalArgumentException.class, () -> new Name(""));
        // starts with non-letter
        assertThrows(IllegalArgumentException.class, () -> new Name("  LeadingSpace"));
        assertThrows(IllegalArgumentException.class, () -> new Name("1NumberStart"));
        // exceeds 200 characters
        String longName = "A".repeat(201);
        assertThrows(IllegalArgumentException.class, () -> new Name(longName));
        // invalid symbols
        assertThrows(IllegalArgumentException.class, () -> new Name("Peter*"));
        assertThrows(IllegalArgumentException.class, () -> new Name("^Invalid"));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));

        // invalid names
        assertFalse(Name.isValidName("")); // empty string
        assertFalse(Name.isValidName(" ")); // spaces only
        assertFalse(Name.isValidName("^")); // only non-allowed symbol
        assertFalse(Name.isValidName("peter*")); // contains invalid symbol
        assertFalse(Name.isValidName("1StartWithNumber")); // starts with non-letter
        assertFalse(Name.isValidName("  LeadingSpace")); // starts with whitespace
        assertFalse(Name.isValidName("A".repeat(201))); // exceeds 200 characters

        // valid names
        assertTrue(Name.isValidName("O'Connor")); // apostrophe
        assertTrue(Name.isValidName("Mary-Jane")); // hyphen
        assertTrue(Name.isValidName("Capital Tan")); // spaces and capital letters
        assertTrue(Name.isValidName("David Roger Jackson Ray Jr")); // long but <200 chars
        assertTrue(Name.isValidName("A")); // minimum valid length
    }

    @Test
    public void equals() {
        Name name = new Name("Valid Name");

        // same values -> returns true
        assertTrue(name.equals(new Name("Valid Name")));

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different types -> returns false
        assertFalse(name.equals(5.0f));

        // different values -> returns false
        assertFalse(name.equals(new Name("Other Valid Name")));
    }
}
