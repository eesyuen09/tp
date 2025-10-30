package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.ClassTag;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_STUDENT_ID = "Rachel";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_STUDENTID = BENSON.getStudentId().toString();
    private static final List<JsonAdaptedClassTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedClassTag::new)
            .collect(Collectors.toList());
    private static final String VALID_ENROLLED_MONTH = BENSON.getEnrolledMonth().toString();

    private static final List<JsonAdaptedPerformanceNote> VALID_PERFORMANCENOTES =
            BENSON.getPerformanceList().asUnmodifiableList()
                .stream()
                .map(JsonAdaptedPerformanceNote::new)
                .collect(Collectors.toList());

    // Valid class tags list for testing - must contain all tags used by BENSON
    private static final List<ClassTag> VALID_CLASS_TAGS_LIST = BENSON.getTags().stream()
            .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType(VALID_CLASS_TAGS_LIST));
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE,
                        VALID_EMAIL, VALID_ADDRESS, VALID_TAGS, VALID_STUDENTID,
                        VALID_ENROLLED_MONTH, new ArrayList<>(), VALID_PERFORMANCENOTES);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CLASS_TAGS_LIST));
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS,
                    VALID_STUDENTID, VALID_ENROLLED_MONTH, new ArrayList<>(), VALID_PERFORMANCENOTES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CLASS_TAGS_LIST));
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE,
                        VALID_EMAIL, VALID_ADDRESS, VALID_TAGS, VALID_STUDENTID,
                        VALID_ENROLLED_MONTH, new ArrayList<>(), VALID_PERFORMANCENOTES);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CLASS_TAGS_LIST));
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, null, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS,
                    VALID_STUDENTID, VALID_ENROLLED_MONTH, new ArrayList<>(), VALID_PERFORMANCENOTES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CLASS_TAGS_LIST));
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE,
                        INVALID_EMAIL, VALID_ADDRESS, VALID_TAGS, VALID_STUDENTID,
                        VALID_ENROLLED_MONTH, new ArrayList<>(), VALID_PERFORMANCENOTES);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CLASS_TAGS_LIST));
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, null, VALID_ADDRESS,
                    VALID_TAGS, VALID_STUDENTID, VALID_ENROLLED_MONTH, new ArrayList<>(), VALID_PERFORMANCENOTES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CLASS_TAGS_LIST));
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE,
                        VALID_EMAIL, INVALID_ADDRESS, VALID_TAGS, VALID_STUDENTID,
                        VALID_ENROLLED_MONTH, new ArrayList<>(), VALID_PERFORMANCENOTES);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CLASS_TAGS_LIST));
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, null, VALID_TAGS,
                    VALID_STUDENTID, VALID_ENROLLED_MONTH, new ArrayList<>(), VALID_PERFORMANCENOTES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CLASS_TAGS_LIST));
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedClassTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedClassTag(INVALID_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE,
                        VALID_EMAIL, VALID_ADDRESS, invalidTags,
                        VALID_STUDENTID, VALID_ENROLLED_MONTH, new ArrayList<>(), VALID_PERFORMANCENOTES);
        assertThrows(IllegalValueException.class, () -> person.toModelType(VALID_CLASS_TAGS_LIST));
    }

    @Test
    public void toModelType_invalidStudentId_throwsIllegalValueException() {
        String invalidStudentId = "12a2";
        JsonAdaptedPerson person = new JsonAdaptedPerson(
            VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS,
            INVALID_STUDENT_ID, VALID_ENROLLED_MONTH, new ArrayList<>(), VALID_PERFORMANCENOTES);
        String expectedMessage = StudentId.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CLASS_TAGS_LIST));
    }

    @Test
    public void toModelType_nullStudentId_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
            VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS,
            null, VALID_ENROLLED_MONTH, new ArrayList<>(), VALID_PERFORMANCENOTES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, StudentId.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CLASS_TAGS_LIST));
    }

    @Test
    public void toModelType_nonExistentTag_throwsIllegalValueException() {
        // Test with a tag that doesn't exist in the valid tags list
        List<JsonAdaptedClassTag> tagsWithNonExistent = new ArrayList<>();
        tagsWithNonExistent.add(new JsonAdaptedClassTag("NonExistentTag"));
        JsonAdaptedPerson person = new JsonAdaptedPerson(
            VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, tagsWithNonExistent,
            VALID_STUDENTID, VALID_ENROLLED_MONTH, new ArrayList<>(), VALID_PERFORMANCENOTES);
        String expectedMessage = "Tag 'NonExistentTag' does not exist in class tags list";
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CLASS_TAGS_LIST));
    }

    @Test
    public void toModelType_wrongCasingTag_usesCorrectCasing() throws Exception {
        // Test that tags with wrong casing are corrected to match the valid class tags list
        List<ClassTag> validTags = new ArrayList<>();
        validTags.add(new ClassTag("Sec3_Maths")); // Correct casing

        List<JsonAdaptedClassTag> tagsWithWrongCasing = new ArrayList<>();
        tagsWithWrongCasing.add(new JsonAdaptedClassTag("sec3_maths")); // Wrong casing

        JsonAdaptedPerson person = new JsonAdaptedPerson(
            VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, tagsWithWrongCasing,
            VALID_STUDENTID, VALID_ENROLLED_MONTH, new ArrayList<>(), VALID_PERFORMANCENOTES);

        seedu.address.model.person.Person modelPerson = person.toModelType(validTags);
        // Verify that the tag uses the correct casing from the valid tags list
        assertEquals(1, modelPerson.getTags().size());
        ClassTag resultTag = modelPerson.getTags().iterator().next();
        assertEquals("Sec3_Maths", resultTag.tagName); // Should use correct casing
    }
}

