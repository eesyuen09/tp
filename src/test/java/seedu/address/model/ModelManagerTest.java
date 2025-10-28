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
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.fee.FeeState;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.tag.exceptions.ClassTagNotFoundException;
import seedu.address.model.tag.exceptions.DuplicateClassTagException;
import seedu.address.model.time.Date;
import seedu.address.model.time.Month;
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
    public void deleteClassTag_tagExists_removesTag() {
        ClassTag tag = new ClassTag("ToDelete");
        modelManager.addClassTag(tag);
        assertTrue(modelManager.hasClassTag(tag));

        modelManager.deleteClassTag(tag);
        assertFalse(modelManager.hasClassTag(tag));
    }

    @Test
    public void deleteClassTag_tagDoesNotExist_throwsClassTagNotFoundException() {
        ClassTag tag = new ClassTag("NonExistent");
        assertThrows(ClassTagNotFoundException.class, () -> modelManager.deleteClassTag(tag));
    }

    @Test
    public void deleteClassTag_nullTag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.deleteClassTag(null));
    }

    @Test
    public void findClassTag_tagExistsExactCase_returnsTag() {
        AddressBook addressBook = new AddressBook();
        ClassTag tag = new ClassTag("Math_Sec3");
        addressBook.addClassTag(tag);
        modelManager = new ModelManager(addressBook, new UserPrefs());

        Optional<ClassTag> found = modelManager.findClassTag(new ClassTag("Math_Sec3"));
        assertTrue(found.isPresent());
        assertEquals(tag, found.get()); // Should return the original object
    }

    @Test
    public void findClassTag_tagExistsDifferentCase_returnsTag() {
        AddressBook addressBook = new AddressBook();
        ClassTag tag = new ClassTag("Math_Sec3");
        addressBook.addClassTag(tag);
        modelManager = new ModelManager(addressBook, new UserPrefs());

        Optional<ClassTag> found = modelManager.findClassTag(new ClassTag("math_sec3")); // Search lowercase
        assertTrue(found.isPresent());
        assertEquals(tag, found.get()); // Should return the original object ("Math_Sec3")
    }

    @Test
    public void findClassTag_tagDoesNotExist_returnsEmpty() {
        AddressBook addressBook = new AddressBook();
        addressBook.addClassTag(new ClassTag("Math_Sec3"));
        modelManager = new ModelManager(addressBook, new UserPrefs());

        Optional<ClassTag> found = modelManager.findClassTag(new ClassTag("Physics"));
        assertTrue(found.isEmpty());
    }

    @Test
    public void findClassTag_emptyTagList_returnsEmpty() {
        // modelManager is already initialized with an empty address book by default setup
        Optional<ClassTag> found = modelManager.findClassTag(new ClassTag("Anything"));
        assertTrue(found.isEmpty());
    }

    @Test
    public void findClassTag_nullTag_throwsNullPointerException() {
        // The implementation uses requireNonNull, so this confirms that check
        assertThrows(NullPointerException.class, () -> modelManager.findClassTag(null));
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
    public void paidAndUnpaidPredicates_currentMonth_basicBehaviour() {
        modelManager.addPerson(ALICE);
        modelManager.addPerson(BENSON);

        Month current = Month.now();

        modelManager.markPaid(ALICE.getStudentId(), current);

        Predicate<Person> paid = modelManager.paidStudents(current);
        Predicate<Person> unpaid = modelManager.unpaidStudents(current);

        assertTrue(paid.test(ALICE));
        assertFalse(unpaid.test(ALICE));

        assertFalse(paid.test(BENSON));
        assertTrue(unpaid.test(BENSON));
    }

    @Test
    public void getCurrentFeeState_defaultsAndOverrides() {
        modelManager.addPerson(ALICE);
        Month current = Month.now();

        assertEquals(java.util.Optional.of(FeeState.UNPAID), modelManager.getCurrentFeeState(ALICE));

        modelManager.markPaid(ALICE.getStudentId(), current);
        assertEquals(java.util.Optional.of(FeeState.PAID), modelManager.getCurrentFeeState(ALICE));
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

    //=========== Attendance Tests ===================================================================

    @Test
    public void markAttendance_validStudentAndDate_success() {
        modelManager.addPerson(ALICE);
        Date date = new Date("15012025");
        ClassTag classTag = new ClassTag("Math");

        modelManager.markAttendance(ALICE.getStudentId(), date, classTag);

        // Verify attendance was marked
        Person updatedPerson = modelManager.getPersonById(ALICE.getStudentId()).get();
        assertTrue(updatedPerson.getAttendanceList().hasAttendanceMarked(date, classTag));
    }

    @Test
    public void markAttendance_nullStudentId_throwsNullPointerException() {
        Date date = new Date("15012025");
        ClassTag classTag = new ClassTag("Math");
        assertThrows(NullPointerException.class, () -> modelManager.markAttendance(null, date, classTag));
    }

    @Test
    public void markAttendance_nullDate_throwsNullPointerException() {
        modelManager.addPerson(ALICE);
        ClassTag classTag = new ClassTag("Math");
        assertThrows(NullPointerException.class, () ->
                modelManager.markAttendance(ALICE.getStudentId(), null, classTag));
    }

    @Test
    public void unmarkAttendance_validStudentAndDate_success() {
        modelManager.addPerson(ALICE);
        Date date = new Date("15012025");
        ClassTag classTag = new ClassTag("Math");

        // Mark first
        modelManager.markAttendance(ALICE.getStudentId(), date, classTag);

        // Then unmark
        modelManager.unmarkAttendance(ALICE.getStudentId(), date, classTag);

        // Verify attendance was unmarked (marked as absent)
        Person updatedPerson = modelManager.getPersonById(ALICE.getStudentId()).get();
        assertFalse(updatedPerson.getAttendanceList().hasAttendanceMarked(date, classTag));
    }

    @Test
    public void unmarkAttendance_nullStudentId_throwsNullPointerException() {
        Date date = new Date("15012025");
        ClassTag classTag = new ClassTag("Math");
        assertThrows(NullPointerException.class, () -> modelManager.unmarkAttendance(null, date, classTag));
    }

    @Test
    public void unmarkAttendance_nullDate_throwsNullPointerException() {
        modelManager.addPerson(ALICE);
        ClassTag classTag = new ClassTag("Math");
        assertThrows(NullPointerException.class, () ->
                modelManager.unmarkAttendance(ALICE.getStudentId(), null, classTag));
    }

    @Test
    public void markAndUnmarkAttendance_multipleDates_success() {
        modelManager.addPerson(ALICE);
        Date date1 = new Date("15012025");
        Date date2 = new Date("16012025");
        ClassTag classTag = new ClassTag("Math");

        // Mark two dates
        modelManager.markAttendance(ALICE.getStudentId(), date1, classTag);
        modelManager.markAttendance(ALICE.getStudentId(), date2, classTag);

        Person updatedPerson = modelManager.getPersonById(ALICE.getStudentId()).get();
        assertTrue(updatedPerson.getAttendanceList().hasAttendanceMarked(date1, classTag));
        assertTrue(updatedPerson.getAttendanceList().hasAttendanceMarked(date2, classTag));

        // Unmark one date
        modelManager.unmarkAttendance(ALICE.getStudentId(), date1, classTag);

        updatedPerson = modelManager.getPersonById(ALICE.getStudentId()).get();
        assertFalse(updatedPerson.getAttendanceList().hasAttendanceMarked(date1, classTag));
        assertTrue(updatedPerson.getAttendanceList().hasAttendanceMarked(date2, classTag));
    }

    @Test
    public void deleteAttendance_validStudentAndDate_success() {
        modelManager.addPerson(ALICE);
        Date date = new Date("15012025");
        ClassTag classTag = new ClassTag("Math");

        // Mark first
        modelManager.markAttendance(ALICE.getStudentId(), date, classTag);

        // Verify attendance was marked
        Person markedPerson = modelManager.getPersonById(ALICE.getStudentId()).get();
        assertTrue(markedPerson.getAttendanceList().hasAttendanceMarked(date, classTag));

        // Delete attendance
        modelManager.deleteAttendance(ALICE.getStudentId(), date, classTag);

        // Verify attendance record was deleted (not just marked as absent)
        Person updatedPerson = modelManager.getPersonById(ALICE.getStudentId()).get();
        assertFalse(updatedPerson.getAttendanceList().hasAttendanceMarked(date, classTag));
    }

    @Test
    public void deleteAttendance_nullStudentId_throwsNullPointerException() {
        Date date = new Date("15012025");
        ClassTag classTag = new ClassTag("Math");
        assertThrows(NullPointerException.class, () -> modelManager.deleteAttendance(null, date, classTag));
    }

    @Test
    public void deleteAttendance_nullDate_throwsNullPointerException() {
        modelManager.addPerson(ALICE);
        ClassTag classTag = new ClassTag("Math");
        assertThrows(NullPointerException.class, () ->
                modelManager.deleteAttendance(ALICE.getStudentId(), null, classTag));
    }

    @Test
    public void deleteAttendance_nullClassTag_throwsNullPointerException() {
        modelManager.addPerson(ALICE);
        Date date = new Date("15012025");
        assertThrows(NullPointerException.class, () ->
                modelManager.deleteAttendance(ALICE.getStudentId(), date, null));
    }

    @Test
    public void deleteAttendance_nonExistentStudent_throwsIllegalArgumentException() {
        StudentId fakeId = new StudentId("9999");
        Date date = new Date("15012025");
        ClassTag classTag = new ClassTag("Math");
        assertThrows(IllegalArgumentException.class, () ->
                modelManager.deleteAttendance(fakeId, date, classTag));
    }

    @Test
    public void deleteAttendance_multipleRecords_deletesOnlySpecified() {
        modelManager.addPerson(ALICE);
        Date date1 = new Date("15012025");
        Date date2 = new Date("16012025");
        ClassTag classTag1 = new ClassTag("Math");
        ClassTag classTag2 = new ClassTag("Science");

        // Mark multiple attendance records
        modelManager.markAttendance(ALICE.getStudentId(), date1, classTag1);
        modelManager.markAttendance(ALICE.getStudentId(), date2, classTag1);
        modelManager.markAttendance(ALICE.getStudentId(), date1, classTag2);

        // Delete only one specific record
        modelManager.deleteAttendance(ALICE.getStudentId(), date1, classTag1);

        // Verify only the specified record was deleted
        Person updatedPerson = modelManager.getPersonById(ALICE.getStudentId()).get();
        assertFalse(updatedPerson.getAttendanceList().hasAttendanceMarked(date1, classTag1));
        assertTrue(updatedPerson.getAttendanceList().hasAttendanceMarked(date2, classTag1));
        assertTrue(updatedPerson.getAttendanceList().hasAttendanceMarked(date1, classTag2));
    }
}
