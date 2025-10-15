package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.tag.exceptions.DuplicateClassTagException;
import seedu.address.testutil.AddressBookBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
    }

    //=========== ClassTag Tests ===================================================================

    @Test
    public void hasClassTag_nullClassTag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasClassTag(null));
    }

    @Test
    public void hasClassTag_tagNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasClassTag(new ClassTag("NotInBook")));
    }

    @Test
    public void hasClassTag_tagInAddressBook_returnsTrue() {
        ClassTag tag = new ClassTag("InBook");
        modelManager.addClassTag(tag);
        assertTrue(modelManager.hasClassTag(tag));
    }

    @Test
    public void addClassTag_duplicateTag_throwsDuplicateClassTagException() {
        ClassTag tag = new ClassTag("Duplicate");
        modelManager.addClassTag(tag);
        assertThrows(DuplicateClassTagException.class, () -> modelManager.addClassTag(tag));
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }

    @Test
    public void getPersonById_existingAndNonExistingId_returnsCorrectResult() {
        modelManager.addPerson(ALICE);
        modelManager.addPerson(BENSON);

        // existing ID → should return Optional containing the correct person
        assertTrue(modelManager.getPersonById(ALICE.getStudentId()).isPresent());
        assertEquals(ALICE, modelManager.getPersonById(ALICE.getStudentId()).get());

        // another existing ID
        assertTrue(modelManager.getPersonById(BENSON.getStudentId()).isPresent());
        assertEquals(BENSON, modelManager.getPersonById(BENSON.getStudentId()).get());

        // non-existing ID → should return Optional.empty()
        StudentId fakeId = new StudentId("4444");
        assertTrue(modelManager.getPersonById(fakeId).isEmpty());
    }

    @Test
    public void getPersonById_nullId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.getPersonById(null));
    }

    @Test
    public void hasPersonWithId_existingAndNonExistingId_returnsCorrectResult() {
        modelManager.addPerson(ALICE);
        modelManager.addPerson(BENSON);

        // existing ID → should return true
        assertTrue(modelManager.hasPersonWithId(ALICE.getStudentId()));
        assertTrue(modelManager.hasPersonWithId(BENSON.getStudentId()));

        // non-existing ID → should return false
        StudentId fakeId = new StudentId("4444");
        assertFalse(modelManager.hasPersonWithId(fakeId));
    }

    @Test
    public void hasPersonWithId_nullId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPersonWithId(null));
    }
}
