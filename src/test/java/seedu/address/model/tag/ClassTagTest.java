package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ClassTagTest {

    // Constructor Tests

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ClassTag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        // Test with an empty string
        String emptyTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new ClassTag(emptyTagName));

        // Test with a string of only spaces (gets trimmed to empty)
        String whitespaceTagName = "   ";
        assertThrows(IllegalArgumentException.class, () -> new ClassTag(whitespaceTagName));

        // Test with forbidden characters
        String specialCharTagName = "Sec3@Math";
        assertThrows(IllegalArgumentException.class, () -> new ClassTag(specialCharTagName));

        // Test with a name that is too long (over 30 chars)
        String longTagName = "This_tag_name_is_way_too_long_to_be_valid";
        assertThrows(IllegalArgumentException.class, () -> new ClassTag(longTagName));
    }

    @Test
    public void constructor_validTagName_success() {
        // No exception should be thrown for valid tag names
        new ClassTag("ValidTag");
        new ClassTag("Sec3_AMath");
        new ClassTag("  PaddedTag  "); // Should be trimmed and succeed
    }

    // isValidTagName Tests

    @Test
    public void isValidTagName_null_throwsNullPointerException() {
        // The static method itself will throw an NPE if a null is passed
        assertThrows(NullPointerException.class, () -> ClassTag.isValidTagName(null));
    }

    @Test
    public void isValidTagName_invalidNames_returnsFalse() {
        // EP: empty string
        assertFalse(ClassTag.isValidTagName(""));

        // EP: whitespace only
        assertFalse(ClassTag.isValidTagName(" "));

        // EP: contains forbidden characters
        assertFalse(ClassTag.isValidTagName("Sec3-Math")); // contains hyphen
        assertFalse(ClassTag.isValidTagName("Math!")); // contains '!'
        assertFalse(ClassTag.isValidTagName("Physics Group")); // contains space

        // EP: exceeds max length
        assertFalse(ClassTag.isValidTagName("a123456789012345678901234567890")); // 31 chars
    }

    @Test
    public void isValidTagName_validNames_returnsTrue() {
        // EP: alphanumeric
        assertTrue(ClassTag.isValidTagName("Math"));

        // EP: alphanumeric with numbers
        assertTrue(ClassTag.isValidTagName("Sec3Physics"));

        // EP: alphanumeric with underscores
        assertTrue(ClassTag.isValidTagName("JC1_GP"));
        assertTrue(ClassTag.isValidTagName("chem_tutorial_g1"));

        // EP: exactly 1 character (boundary value)
        assertTrue(ClassTag.isValidTagName("a"));

        // EP: exactly 30 characters (boundary value)
        assertTrue(ClassTag.isValidTagName("a12345678901234567890123456789"));
    }

    // equals and hashCode Tests

    @Test
    public void equals() {
        ClassTag tag = new ClassTag("Sec3_AMath");

        // same object -> returns true
        assertTrue(tag.equals(tag));

        // same value -> returns true
        ClassTag tagCopy = new ClassTag("Sec3_AMath");
        assertTrue(tag.equals(tagCopy));

        // same value, different case -> returns true (case-insensitive check)
        ClassTag tagLowercase = new ClassTag("sec3_amath");
        assertTrue(tag.equals(tagLowercase));

        // different type -> returns false
        assertFalse(tag.equals(5));
        assertFalse(tag.equals("Sec3_AMath"));

        // null -> returns false
        assertFalse(tag.equals(null));

        // different tag name -> returns false
        ClassTag differentTag = new ClassTag("Sec4_EMath");
        assertFalse(tag.equals(differentTag));
    }

    @Test
    public void hashCode_consistencyAndEquality() {
        ClassTag tag1 = new ClassTag("TestTag");
        ClassTag tag2 = new ClassTag("TestTag");
        ClassTag tag3 = new ClassTag("testtag"); // Different case

        // Hashcode should be consistent
        assertEquals(tag1.hashCode(), tag1.hashCode());

        // Hashcode for equal objects should be the same
        assertEquals(tag1.hashCode(), tag2.hashCode());

        // Hashcode for case-insensitively equal objects should be the same
        assertEquals(tag1.hashCode(), tag3.hashCode());
    }

    // toString Test

    @Test
    public void toString_correctFormat() {
        ClassTag tag = new ClassTag("MyTestTag");
        String expectedString = "[MyTestTag]";
        assertEquals(expectedString, tag.toString());
    }
}

