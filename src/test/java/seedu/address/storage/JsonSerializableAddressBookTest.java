package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.testutil.TypicalClassTags;
import seedu.address.testutil.TypicalPersons;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsAddressBook.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonAddressBook.json");
    private static final Path DUPLICATE_CLASSTAG_FILE = TEST_DATA_FOLDER.resolve("duplicateClassTagAddressBook.json");
    private static final Path TYPICAL_ADDRESS_BOOK_WITH_CLASSTAGS_FILE =
            TEST_DATA_FOLDER.resolve("typicalAddressBookWithClassTags.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonAddressBook.json");


    @Test
    public void toModelType_typicalAddressBookWithClassTagsFile_success() throws Exception {
        // This test reads the file containing both persons and class tags.
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_ADDRESS_BOOK_WITH_CLASSTAGS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();

        // The expected AddressBook must match the JSON data exactly.
        AddressBook expectedAddressBook = TypicalPersons.getTypicalAddressBook();
        expectedAddressBook.setClassTags(TypicalClassTags.getTypicalClassTags());

        assertEquals(expectedAddressBook, addressBookFromFile);
    }

    @Test
    public void toModelType_typicalPersonsFile_backwardCompatibilitySuccess() throws Exception {
        // This test reads the older JSON file that does NOT contain a classTags field.
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();

        // The expected AddressBook should contain the persons but have an EMPTY class tag list.
        AddressBook expectedAddressBook = TypicalPersons.getTypicalAddressBook();
        expectedAddressBook.setClassTags(java.util.Collections.emptyList());

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
}
