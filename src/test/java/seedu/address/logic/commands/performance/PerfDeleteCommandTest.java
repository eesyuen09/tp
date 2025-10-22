package seedu.address.logic.commands.performance;

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

public class PerfDeleteCommandTest {

    private static final StudentId VALID_STUDENT_ID = new StudentId("0123");
    private static final Date VALID_DATE_1 = new Date("15032024");
    private static final String VALID_NOTE_1 = "Good performance in class";
    private static final Date VALID_DATE_2 = new Date("20032024");
    private static final String VALID_NOTE_2 = "Excellent homework submission";
    private static final Date VALID_DATE_3 = new Date("25032024");
    private static final String VALID_NOTE_3 = "Participated actively in discussion";
    private static final int VALID_INDEX = 1;

    @Test
    public void constructor_nullStudentId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PerfDeleteCommand(null, VALID_INDEX));
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        PerfDeleteCommand perfDeleteCommand = new PerfDeleteCommand(VALID_STUDENT_ID, VALID_INDEX);

        assertCommandFailure(perfDeleteCommand, model,
                String.format(Messages.MESSAGE_STUDENT_ID_NOT_FOUND, VALID_STUDENT_ID));
    }

    @Test
    public void execute_validIndexOneNote_deleteSuccessful() throws Exception {
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString()).build();

        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_NOTE_1);
        PerformanceList performanceList = new PerformanceList();
        performanceList.add(note);
        Person personWithNote = validPerson.withPerformanceList(performanceList);

        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(personWithNote);

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        Person personWithoutNote = validPerson.withPerformanceList(new PerformanceList());
        expectedModel.addPerson(personWithoutNote);

        String expectedMessage = String.format(PerfCommand.DELETED, VALID_INDEX, validPerson.getName());

        assertCommandSuccess(new PerfDeleteCommand(VALID_STUDENT_ID, VALID_INDEX),
                model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexMultipleNotes_deleteSuccessful() throws Exception {
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

        // Expected: delete note at index 2 (note2), leaving note1 and note3
        PerformanceList expectedList = new PerformanceList();
        expectedList.add(note1);
        expectedList.add(note3);
        Person personAfterDelete = validPerson.withPerformanceList(expectedList);

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        expectedModel.addPerson(personAfterDelete);

        String expectedMessage = String.format(PerfCommand.DELETED, 2, validPerson.getName());

        assertCommandSuccess(new PerfDeleteCommand(VALID_STUDENT_ID, 2),
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

        PerfDeleteCommand perfDeleteCommand = new PerfDeleteCommand(VALID_STUDENT_ID, 5);

        assertCommandFailure(perfDeleteCommand, model, "Error: Invalid performance note index.");
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

        PerfDeleteCommand perfDeleteCommand = new PerfDeleteCommand(VALID_STUDENT_ID, 0);

        assertCommandFailure(perfDeleteCommand, model, "Error: Invalid performance note index.");
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

        PerfDeleteCommand perfDeleteCommand = new PerfDeleteCommand(VALID_STUDENT_ID, -1);

        assertCommandFailure(perfDeleteCommand, model, "Error: Invalid performance note index.");
    }

    @Test
    public void execute_deleteFromEmptyList_throwsCommandException() {
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString()).build();

        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(validPerson);

        PerfDeleteCommand perfDeleteCommand = new PerfDeleteCommand(VALID_STUDENT_ID, VALID_INDEX);

        assertCommandFailure(perfDeleteCommand, model, "Error: Invalid performance note index.");
    }

    @Test
    public void execute_deleteUsingModelStub_success() throws Exception {
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString()).build();

        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_NOTE_1);
        PerformanceList performanceList = new PerformanceList();
        performanceList.add(note);
        Person personWithNote = validPerson.withPerformanceList(performanceList);

        ModelStubAcceptingPerformanceNoteDeleted modelStub =
                new ModelStubAcceptingPerformanceNoteDeleted(personWithNote);

        new PerfDeleteCommand(VALID_STUDENT_ID, VALID_INDEX).execute(modelStub);

        assertTrue(modelStub.updatedPerson.getPerformanceList().asUnmodifiableList().isEmpty());
    }

    @Test
    public void equals() {
        StudentId studentIdA = new StudentId("0123");
        StudentId studentIdB = new StudentId("0234");

        PerfDeleteCommand deleteCommand1 = new PerfDeleteCommand(studentIdA, 1);
        PerfDeleteCommand deleteCommand2 = new PerfDeleteCommand(studentIdB, 1);
        PerfDeleteCommand deleteCommand3 = new PerfDeleteCommand(studentIdA, 2);

        // same object -> returns true
        assertTrue(deleteCommand1.equals(deleteCommand1));

        // same values -> returns true
        PerfDeleteCommand deleteCommand1Copy = new PerfDeleteCommand(studentIdA, 1);
        assertTrue(deleteCommand1.equals(deleteCommand1Copy));

        // different types -> returns false
        assertFalse(deleteCommand1.equals(1));

        // null -> returns false
        assertFalse(deleteCommand1.equals(null));

        // different student ID -> returns false
        assertFalse(deleteCommand1.equals(deleteCommand2));

        // different index -> returns false
        assertFalse(deleteCommand1.equals(deleteCommand3));
    }

    /**
     * A Model stub that always accepts the performance note being deleted.
     */
    private class ModelStubAcceptingPerformanceNoteDeleted extends ModelStub {
        private final Person person;
        private Person updatedPerson;

        ModelStubAcceptingPerformanceNoteDeleted(Person person) {
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
