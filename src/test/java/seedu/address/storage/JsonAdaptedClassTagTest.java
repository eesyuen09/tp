package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.ClassTag;

public class JsonAdaptedClassTagTest {
    private static final String INVALID_CLASS_TAG = "Invalid-Tag!";
    private static final String VALID_CLASS_TAG = "Sec3_Maths";

    @Test
    public void toModelType_validClassTagDetails_returnsClassTag() throws Exception {
        JsonAdaptedClassTag tag = new JsonAdaptedClassTag(VALID_CLASS_TAG);
        assertEquals(new ClassTag(VALID_CLASS_TAG), tag.toModelType());
    }

    @Test
    public void toModelType_invalidClassTagName_throwsIllegalValueException() {
        JsonAdaptedClassTag tag = new JsonAdaptedClassTag(INVALID_CLASS_TAG);
        String expectedMessage = ClassTag.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, tag::toModelType);
    }

    @Test
    public void constructor_fromValidClassTag_success() {
        ClassTag source = new ClassTag(VALID_CLASS_TAG);
        JsonAdaptedClassTag adaptedTag = new JsonAdaptedClassTag(source);
        assertEquals(VALID_CLASS_TAG, adaptedTag.getTagName());
    }
}
