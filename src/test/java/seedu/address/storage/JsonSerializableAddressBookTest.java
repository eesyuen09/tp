package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.fee.FeeState;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Month;
import seedu.address.testutil.PersonBuilder;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsAddressBook.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonAddressBook.json");
    private static final Path DUPLICATE_CLASSTAG_FILE = TEST_DATA_FOLDER.resolve("duplicateClassTagAddressBook.json");
    private static final Path TYPICAL_ADDRESS_BOOK_WITH_CLASSTAGS_FILE =
            TEST_DATA_FOLDER.resolve("typicalAddressBookWithClassTags.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonAddressBook.json");
    private static final Path TYPICAL_WITH_TAGS_AND_FEES_FILE =
        TEST_DATA_FOLDER.resolve("typicalAddressBookWithFees.json");

    private static final Path INVALID_FEERECORD_FILE =
        TEST_DATA_FOLDER.resolve("invalidFeeRecordAddressBook.json");


    @Test
    public void toModelType_typicalAddressBookWithClassTagsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_ADDRESS_BOOK_WITH_CLASSTAGS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();

        AddressBook expectedAddressBook = new AddressBook();
        Person alice = new PersonBuilder(ALICE).withStudentId("0001").withTags("Sec3_Maths").build();
        Person benson = new PersonBuilder(BENSON).withStudentId("0002")
                .withTags("JC1_Physics", "Sec3_Maths").build();
        expectedAddressBook.addPerson(alice);
        expectedAddressBook.addPerson(benson);
        expectedAddressBook.addClassTag(new ClassTag("Sec3_Maths"));
        expectedAddressBook.addClassTag(new ClassTag("JC1_Physics"));

        assertEquals(expectedAddressBook, addressBookFromFile);
    }

    @Test
    public void toModelType_typicalPersonsFile_backwardCompatibilitySuccess() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();

        // **FIX:** Construct the expected AddressBook with the exact data from the JSON file.
        AddressBook expectedAddressBook = new AddressBook();
        expectedAddressBook.addPerson(new PersonBuilder(ALICE).withTags("friends").build());
        expectedAddressBook.addPerson(new PersonBuilder(BENSON).withTags("owesMoney", "friends").build());
        expectedAddressBook.addPerson(CARL);
        expectedAddressBook.addPerson(new PersonBuilder(DANIEL).withTags("friends").build());
        expectedAddressBook.addPerson(ELLE);
        expectedAddressBook.addPerson(FIONA);
        expectedAddressBook.addPerson(GEORGE);
        expectedAddressBook.setClassTags(Collections.emptyList());

        assertEquals(expectedAddressBook, addressBookFromFile);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_PERSON,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateClassTags_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_CLASSTAG_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_CLASSTAG,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_typicalWithTagsAndFees_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
            TYPICAL_WITH_TAGS_AND_FEES_FILE, JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();

        StudentId id0000 = new StudentId("0000");
        StudentId id0001 = new StudentId("0001");
        Month oct25 = new Month("1025");

        assertEquals(FeeState.PAID, addressBookFromFile.getFeeTracker()
            .getExplicitStatusOfMonth(id0000, oct25)
            .orElseThrow(() -> new AssertionError("Expected fee record for 0000/1025")));

        assertEquals(FeeState.UNPAID, addressBookFromFile.getFeeTracker()
            .getExplicitStatusOfMonth(id0001, oct25)
            .orElseThrow(() -> new AssertionError("Expected fee record for 0001/1025")));
    }

    @Test
    public void toModelType_invalidFeeRecord_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
            INVALID_FEERECORD_FILE, JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }
}


