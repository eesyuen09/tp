package seedu.address.logic.commands.performance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Date;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.performance.PerformanceList;
import seedu.address.model.person.performance.PerformanceNote;
import seedu.address.testutil.ModelStub;
import seedu.address.testutil.PersonBuilder;

public class PerfEditCommandTest {

    private static final StudentId VALID_STUDENT_ID = new StudentId("0123");
    private static final Date VALID_DATE_1 = new Date("15032024");
    private static final String VALID_NOTE_1 = "Good performance in class";
    private static final Date VALID_DATE_2 = new Date("20032024");
    private static final String VALID_NOTE_2 = "Excellent homework submission";
    private static final Date VALID_DATE_3 = new Date("25032024");
    private static final String VALID_NOTE_3 = "Participated actively in discussion";
    private static final String EDITED_NOTE = "Updated performance note";
    private static final int VALID_INDEX = 1;

    @Test
    public void constructor_nullStudentId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new PerfEditCommand(null, VALID_INDEX, EDITED_NOTE));
    }

    @Test
    public void constructor_nullNote_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new PerfEditCommand(VALID_STUDENT_ID, VALID_INDEX, null));
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        PerfEditCommand perfEditCommand = new PerfEditCommand(VALID_STUDENT_ID, VALID_INDEX, EDITED_NOTE);

        assertCommandFailure(perfEditCommand, model,
                String.format(Messages.MESSAGE_STUDENT_ID_NOT_FOUND, VALID_STUDENT_ID));
    }

    @Test
    public void execute_validIndexOneNote_editSuccessful() throws Exception {
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString()).build();

        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_NOTE_1);
        PerformanceList performanceList = new PerformanceList();
        performanceList.add(note);
        Person personWithNote = validPerson.withPerformanceList(performanceList);

        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(personWithNote);

        PerformanceNote editedNote = new PerformanceNote(VALID_DATE_1, EDITED_NOTE);
        PerformanceList expectedList = new PerformanceList();
        expectedList.add(editedNote);
        Person personWithEditedNote = validPerson.withPerformanceList(expectedList);

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        expectedModel.addPerson(personWithEditedNote);

        String expectedMessage = String.format(PerfCommand.EDITED, VALID_INDEX, validPerson.getName());

        assertCommandSuccess(new PerfEditCommand(VALID_STUDENT_ID, VALID_INDEX, EDITED_NOTE),
                model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexMultipleNotes_editSuccessful() throws Exception {
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString()).build();

        PerformanceNote note1 = new PerformanceNote(VALID_DATE_1, VALID_NOTE_1);
        PerformanceNote note2 = new PerformanceNote(VALID_DATE_2, VALID_NOTE_2);
        PerformanceNote note3 = new PerformanceNote(VALID_DATE_3, VALID_NOTE_3);
        PerformanceList performanceList = new PerformanceList();
        performanceList.add(note1);
        performanceList.add(note2);
        performanceList.add(note3);
        Person personWithNotes = validPerson.withPerformanceList(performanceList);

        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(personWithNotes);

        // Edit note at index 2 (note2)
        PerformanceNote editedNote2 = new PerformanceNote(VALID_DATE_2, EDITED_NOTE);
        PerformanceList expectedList = new PerformanceList();
        expectedList.add(note1);
        expectedList.add(editedNote2);
        expectedList.add(note3);
        Person personAfterEdit = validPerson.withPerformanceList(expectedList);

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        expectedModel.addPerson(personAfterEdit);

        String expectedMessage = String.format(PerfCommand.EDITED, 2, validPerson.getName());

        assertCommandSuccess(new PerfEditCommand(VALID_STUDENT_ID, 2, EDITED_NOTE),
                model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editLastNote_editSuccessful() throws Exception {
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString()).build();

        PerformanceNote note1 = new PerformanceNote(VALID_DATE_1, VALID_NOTE_1);
        PerformanceNote note2 = new PerformanceNote(VALID_DATE_2, VALID_NOTE_2);
        PerformanceNote note3 = new PerformanceNote(VALID_DATE_3, VALID_NOTE_3);
        PerformanceList performanceList = new PerformanceList();
        performanceList.add(note1);
        performanceList.add(note2);
        performanceList.add(note3);
        Person personWithNotes = validPerson.withPerformanceList(performanceList);

        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(personWithNotes);

        // Edit last note (index 3)
        PerformanceNote editedNote3 = new PerformanceNote(VALID_DATE_3, EDITED_NOTE);
        PerformanceList expectedList = new PerformanceList();
        expectedList.add(note1);
        expectedList.add(note2);
        expectedList.add(editedNote3);
        Person personAfterEdit = validPerson.withPerformanceList(expectedList);

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        expectedModel.addPerson(personAfterEdit);

        String expectedMessage = String.format(PerfCommand.EDITED, 3, validPerson.getName());

        assertCommandSuccess(new PerfEditCommand(VALID_STUDENT_ID, 3, EDITED_NOTE),
                model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexTooLarge_throwsCommandException() {
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString()).build();

        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_NOTE_1);
        PerformanceList performanceList = new PerformanceList();
        performanceList.add(note);
        Person personWithNote = validPerson.withPerformanceList(performanceList);

        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(personWithNote);

        PerfEditCommand perfEditCommand = new PerfEditCommand(VALID_STUDENT_ID, 5, EDITED_NOTE);

        assertCommandFailure(perfEditCommand, model, "Error: Invalid performance note index.");
    }

    @Test
    public void execute_invalidIndexZero_throwsCommandException() {
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString()).build();

        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_NOTE_1);
        PerformanceList performanceList = new PerformanceList();
        performanceList.add(note);
        Person personWithNote = validPerson.withPerformanceList(performanceList);

        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(personWithNote);

        PerfEditCommand perfEditCommand = new PerfEditCommand(VALID_STUDENT_ID, 0, EDITED_NOTE);

        assertCommandFailure(perfEditCommand, model, "Error: Invalid performance note index.");
    }

    @Test
    public void execute_invalidIndexNegative_throwsCommandException() {
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString()).build();

        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_NOTE_1);
        PerformanceList performanceList = new PerformanceList();
        performanceList.add(note);
        Person personWithNote = validPerson.withPerformanceList(performanceList);

        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(personWithNote);

        PerfEditCommand perfEditCommand = new PerfEditCommand(VALID_STUDENT_ID, -1, EDITED_NOTE);

        assertCommandFailure(perfEditCommand, model, "Error: Invalid performance note index.");
    }

    @Test
    public void execute_editFromEmptyList_throwsCommandException() {
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString()).build();

        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(validPerson);

        PerfEditCommand perfEditCommand = new PerfEditCommand(VALID_STUDENT_ID, VALID_INDEX, EDITED_NOTE);

        assertCommandFailure(perfEditCommand, model, "Error: Invalid performance note index.");
    }

    @Test
    public void execute_editUsingModelStub_success() throws Exception {
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString()).build();

        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_NOTE_1);
        PerformanceList performanceList = new PerformanceList();
        performanceList.add(note);
        Person personWithNote = validPerson.withPerformanceList(performanceList);

        ModelStubAcceptingPerformanceNoteEdited modelStub =
                new ModelStubAcceptingPerformanceNoteEdited(personWithNote);

        new PerfEditCommand(VALID_STUDENT_ID, VALID_INDEX, EDITED_NOTE).execute(modelStub);

        assertEquals(EDITED_NOTE,
                modelStub.updatedPerson.getPerformanceList().asUnmodifiableList().get(0).getNote());
    }

    @Test
    public void equals() {
        StudentId studentIdA = new StudentId("0123");
        StudentId studentIdB = new StudentId("0234");

        PerfEditCommand editCommand1 = new PerfEditCommand(studentIdA, 1, EDITED_NOTE);
        PerfEditCommand editCommand2 = new PerfEditCommand(studentIdB, 1, EDITED_NOTE);
        PerfEditCommand editCommand3 = new PerfEditCommand(studentIdA, 2, EDITED_NOTE);
        PerfEditCommand editCommand4 = new PerfEditCommand(studentIdA, 1, "Different note");

        // same object -> returns true
        assertTrue(editCommand1.equals(editCommand1));

        // same values -> returns true
        PerfEditCommand editCommand1Copy = new PerfEditCommand(studentIdA, 1, EDITED_NOTE);
        assertTrue(editCommand1.equals(editCommand1Copy));

        // different types -> returns false
        assertFalse(editCommand1.equals(1));

        // null -> returns false
        assertFalse(editCommand1.equals(null));

        // different student ID -> returns false
        assertFalse(editCommand1.equals(editCommand2));

        // different index -> returns false
        assertFalse(editCommand1.equals(editCommand3));

        // different note -> returns false
        assertFalse(editCommand1.equals(editCommand4));
    }

    /**
     * A Model stub that always accepts the performance note being edited.
     */
    private class ModelStubAcceptingPerformanceNoteEdited extends ModelStub {
        private final Person person;
        private Person updatedPerson;

        ModelStubAcceptingPerformanceNoteEdited(Person person) {
            this.person = person;
            this.updatedPerson = person;
        }

        @Override
        public Optional<Person> getPersonById(StudentId studentId) {
            if (person.getStudentId().equals(studentId)) {
                return Optional.of(updatedPerson);
            }
            return Optional.empty();
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            this.updatedPerson = editedPerson;
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
