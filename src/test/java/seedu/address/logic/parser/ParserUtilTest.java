package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Date;
import seedu.address.model.time.Month;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_CLASS_TAG = "#Sec3_Math";
    private static final String INVALID_STUDENT_ID = "A123546"; // Missing 'A' prefix or checksum
    private static final String INVALID_DATE_FORMAT = "12-09-2025"; // Wrong format
    private static final String INVALID_DATE = "31022025"; // Feb 31 doesn't exist

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "87654321";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_CLASS_TAG_1 = "Sec3_Math";
    private static final String VALID_CLASS_TAG_2 = "Sec4_English";
    private static final String VALID_STUDENT_ID = "1234";
    private static final String VALID_DATE = "15092025";


    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((String) null));
    }

    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseStudentId_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseStudentId(INVALID_STUDENT_ID));
    }

    @Test
    public void parseStudentId_validValueWithoutWhitespace_returnsStudentId() throws Exception {
        StudentId expectedStudentId = new StudentId(VALID_STUDENT_ID);
        assertEquals(expectedStudentId, ParserUtil.parseStudentId(VALID_STUDENT_ID));
    }

    @Test
    public void parseStudentId_validValueWithWhitespace_returnsTrimmedStudentId() throws Exception {
        String idWithWhitespace = WHITESPACE + VALID_STUDENT_ID + WHITESPACE;
        StudentId expectedStudentId = new StudentId(VALID_STUDENT_ID);
        assertEquals(expectedStudentId, ParserUtil.parseStudentId(idWithWhitespace));
    }

    @Test
    public void parseDate_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseDate((String) null));
    }

    @Test
    public void parseDate_invalidFormat_throwsParseException() {
        // Test invalid format throws ParseException with MESSAGE_CONSTRAINTS
        assertThrows(ParseException.class, Date.MESSAGE_CONSTRAINTS, () ->
                ParserUtil.parseDate(INVALID_DATE_FORMAT));
    }

    @Test
    public void parseDate_invalidDate_throwsParseException() {
        // Test invalid calendar date throws ParseException with MESSAGE_INVALID_DATE
        assertThrows(ParseException.class, Date.MESSAGE_INVALID_DATE, () ->
                ParserUtil.parseDate(INVALID_DATE));
    }

    @Test
    public void parseDate_validValueWithoutWhitespace_returnsDate() throws Exception {
        Date expectedDate = new Date(VALID_DATE);
        assertEquals(expectedDate, ParserUtil.parseDate(VALID_DATE));
    }

    @Test
    public void parseDate_validValueWithWhitespace_returnsTrimmedDate() throws Exception {
        String dateWithWhitespace = WHITESPACE + VALID_DATE + WHITESPACE;
        Date expectedDate = new Date(VALID_DATE);
        assertEquals(expectedDate, ParserUtil.parseDate(dateWithWhitespace));
    }

    @Test
    public void parseClassTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseClassTag(null));
    }

    @Test
    public void parseClassTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseClassTag(INVALID_CLASS_TAG));
    }

    @Test
    public void parseClassTag_validValueWithoutWhitespace_returnsClassTag() throws Exception {
        ClassTag expectedClassTag = new ClassTag(VALID_CLASS_TAG_1);
        assertEquals(expectedClassTag, ParserUtil.parseClassTag(VALID_CLASS_TAG_1));
    }

    @Test
    public void parseClassTag_validValueWithWhitespace_returnsTrimmedClassTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_CLASS_TAG_1 + WHITESPACE;
        ClassTag expectedClassTag = new ClassTag(VALID_CLASS_TAG_1);
        assertEquals(expectedClassTag, ParserUtil.parseClassTag(tagWithWhitespace));
    }

    @Test
    public void parseClassTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseClassTags(null));
    }

    @Test
    public void parseClassTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () ->
                ParserUtil.parseClassTags(Arrays.asList(VALID_CLASS_TAG_1, INVALID_CLASS_TAG)));
    }

    @Test
    public void parseClassTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseClassTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseClassTags_collectionWithValidTags_returnsClassTagSet() throws Exception {
        Set<ClassTag> actualTagSet = ParserUtil.parseClassTags(
                Arrays.asList(VALID_CLASS_TAG_1, VALID_CLASS_TAG_2));
        Set<ClassTag> expectedTagSet = new HashSet<>(
                Arrays.asList(new ClassTag(VALID_CLASS_TAG_1), new ClassTag(VALID_CLASS_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }

    @Test
    public void parseEnrolledMonth_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEnrolledMonth(null));
    }

    @Test
    public void parseEnrolledMonth_currentMonth_returnsMonth() throws Exception {
        String currentMonthStr = Month.now().toString();
        Month parsedMonth = ParserUtil.parseEnrolledMonth(currentMonthStr);
        assertEquals(Month.now(), parsedMonth);

        // Test with whitespace
        Month parsedWithWhitespace = ParserUtil.parseEnrolledMonth("  " + currentMonthStr + "  ");
        assertEquals(Month.now(), parsedWithWhitespace);
    }

    @Test
    public void parseEnrolledMonth_futureMonth_throwsParseException() {
        String futureMonthStr = Month.now().plusMonths(1).toString();
        assertThrows(ParseException.class, () -> ParserUtil.parseEnrolledMonth(futureMonthStr));
    }


}
