package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.ClassTag;

public class JsonAdaptedClassTagTest {
    private static final String INVALID_TAG_NAME_EMPTY = "";
    private static final String INVALID_TAG_NAME_WHITESPACE = "   ";
    private static final String INVALID_TAG_NAME_SYMBOLS = "Maths!";
    private static final String INVALID_TAG_NAME_TOO_LONG = "a123456789012345678901234567890";

    private static final String VALID_TAG_NAME = "Sec3_AMath";
    private static final String VALID_TAG_NAME_LENGTH_1 = "a";
    private static final String VALID_TAG_NAME_LENGTH_30 = "This_is_a_valid_tag_of_30chars";

    // Test JsonAdaptedClassTag constructor with valid ClassTag
    @Test
    public void constructor_validClassTag_success() {
        ClassTag validTag = new ClassTag(VALID_TAG_NAME);
        JsonAdaptedClassTag jsonTag = new JsonAdaptedClassTag(validTag);
        assertEquals(VALID_TAG_NAME, jsonTag.getTagName());
    }

    // Test JsonAdaptedClassTag constructor with valid ClassTag (boundary case)
    @Test
    public void constructor_validClassTagLength30_success() {
        ClassTag validTag = new ClassTag(VALID_TAG_NAME_LENGTH_30);
        JsonAdaptedClassTag jsonTag = new JsonAdaptedClassTag(validTag);
        assertEquals(VALID_TAG_NAME_LENGTH_30, jsonTag.getTagName());
    }

    @Test
    public void toModelType_validClassTagDetails_returnsClassTag() throws Exception {
        JsonAdaptedClassTag tag = new JsonAdaptedClassTag(VALID_TAG_NAME);
        assertEquals(new ClassTag(VALID_TAG_NAME), tag.toModelType());
    }

    // Test valid boundary length 1
    @Test
    public void toModelType_validBoundaryLength1_returnsClassTag() throws Exception {
        JsonAdaptedClassTag tag = new JsonAdaptedClassTag(VALID_TAG_NAME_LENGTH_1);
        assertEquals(new ClassTag(VALID_TAG_NAME_LENGTH_1), tag.toModelType());
    }

    // Test valid boundary length 30
    @Test
    public void toModelType_validBoundaryLength30_returnsClassTag() throws Exception {
        JsonAdaptedClassTag tag = new JsonAdaptedClassTag(VALID_TAG_NAME_LENGTH_30);
        assertEquals(new ClassTag(VALID_TAG_NAME_LENGTH_30), tag.toModelType());
    }

    @Test
    public void toModelType_nullTagName_throwsIllegalValueException() {
        JsonAdaptedClassTag tag = new JsonAdaptedClassTag((String) null);
        String expectedMessage = ClassTag.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, tag::toModelType);
    }

    // Test invalid empty tag name
    @Test
    public void toModelType_invalidEmptyTagName_throwsIllegalValueException() {
        JsonAdaptedClassTag tag = new JsonAdaptedClassTag(INVALID_TAG_NAME_EMPTY);
        String expectedMessage = ClassTag.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, tag::toModelType);
    }

    // Test invalid whitespace tag name
    @Test
    public void toModelType_invalidWhitespaceTagName_throwsIllegalValueException() {
        JsonAdaptedClassTag tag = new JsonAdaptedClassTag(INVALID_TAG_NAME_WHITESPACE);
        String expectedMessage = ClassTag.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, tag::toModelType);
    }

    // Test invalid symbols tag name
    @Test
    public void toModelType_invalidSymbolsTagName_throwsIllegalValueException() {
        JsonAdaptedClassTag tag = new JsonAdaptedClassTag(INVALID_TAG_NAME_SYMBOLS);
        String expectedMessage = ClassTag.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, tag::toModelType);
    }

    // Test invalid long tag name
    @Test
    public void toModelType_invalidLongTagName_throwsIllegalValueException() {
        JsonAdaptedClassTag tag = new JsonAdaptedClassTag(INVALID_TAG_NAME_TOO_LONG);
        String expectedMessage = ClassTag.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, tag::toModelType);
    }
}
