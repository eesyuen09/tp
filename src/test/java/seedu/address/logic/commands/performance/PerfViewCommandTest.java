package seedu.address.logic.commands.performance;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandResult;
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

public class PerfViewCommandTest {

    private static final StudentId VALID_STUDENT_ID = new StudentId("0123");
    private static final Date VALID_DATE_1 = new Date("15032024");
    private static final String VALID_NOTE_1 = "Good performance in class";
    private static final Date VALID_DATE_2 = new Date("20032024");
    private static final String VALID_NOTE_2 = "Excellent homework submission";

    @Test
    public void constructor_nullStudentId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PerfViewCommand(null));
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        PerfViewCommand perfViewCommand = new PerfViewCommand(VALID_STUDENT_ID);

        assertCommandFailure(perfViewCommand, model, Messages.MESSAGE_STUDENT_ID_NOT_FOUND);
    }

    @Test
    public void execute_studentWithNoNotes_returnsNoneMessage() throws Exception {
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString()).build();
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(validPerson);

        String expectedMessage = validPerson.getName() + " Performance Notes:\n(none)";

        assertCommandSuccess(new PerfViewCommand(VALID_STUDENT_ID), model, expectedMessage, model);
    }

    @Test
    public void execute_studentWithOneNote_displaysNote() throws Exception {
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString()).build();

        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_NOTE_1);
        PerformanceList performanceList = new PerformanceList();
        performanceList.add(note);
        Person personWithNote = validPerson.withPerformanceList(performanceList);

        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(personWithNote);

        String expectedMessage = personWithNote.getName() + " Performance Notes:\n"
                + "1. " + note.getDate().getFormattedDate() + ": " + VALID_NOTE_1;

        assertCommandSuccess(new PerfViewCommand(VALID_STUDENT_ID), model, expectedMessage, model);
    }

    @Test
    public void execute_studentWithMultipleNotes_displaysAllNotes() throws Exception {
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString()).build();

        PerformanceNote note1 = new PerformanceNote(VALID_DATE_1, VALID_NOTE_1);
        PerformanceNote note2 = new PerformanceNote(VALID_DATE_2, VALID_NOTE_2);
        PerformanceList performanceList = new PerformanceList();
        performanceList.add(note1);
        performanceList.add(note2);
        Person personWithNotes = validPerson.withPerformanceList(performanceList);

        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(personWithNotes);

        String expectedMessage = personWithNotes.getName() + " Performance Notes:\n"
                + "1. " + note1.getDate().getFormattedDate() + ": " + VALID_NOTE_1 + "\n"
                + "2. " + note2.getDate().getFormattedDate() + ": " + VALID_NOTE_2;

        assertCommandSuccess(new PerfViewCommand(VALID_STUDENT_ID), model, expectedMessage, model);
    }

    @Test
    public void execute_studentWithNotesUsingModelStub_success() throws Exception {
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString()).build();

        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_NOTE_1);
        PerformanceList performanceList = new PerformanceList();
        performanceList.add(note);
        Person personWithNote = validPerson.withPerformanceList(performanceList);

        ModelStubWithPerson modelStub = new ModelStubWithPerson(personWithNote);

        CommandResult result = new PerfViewCommand(VALID_STUDENT_ID).execute(modelStub);

        String expectedMessage = personWithNote.getName() + " Performance Notes:\n"
                + "1. " + note.getDate().getFormattedDate() + ": " + VALID_NOTE_1;

        assertTrue(result.getFeedbackToUser().equals(expectedMessage));
    }

    @Test
    public void equals() {
        StudentId studentIdA = new StudentId("0123");
        StudentId studentIdB = new StudentId("0234");

        PerfViewCommand viewCommandA = new PerfViewCommand(studentIdA);
        PerfViewCommand viewCommandB = new PerfViewCommand(studentIdB);

        // same object -> returns true
        assertTrue(viewCommandA.equals(viewCommandA));

        // same values -> returns true
        PerfViewCommand viewCommandACopy = new PerfViewCommand(studentIdA);
        assertTrue(viewCommandA.equals(viewCommandACopy));

        // different types -> returns false
        assertFalse(viewCommandA.equals(1));

        // null -> returns false
        assertFalse(viewCommandA.equals(null));

        // different student ID -> returns false
        assertFalse(viewCommandA.equals(viewCommandB));
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            this.person = person;
        }

        @Override
        public Optional<Person> getPersonById(StudentId studentId) {
            if (person.getStudentId().equals(studentId)) {
                return Optional.of(person);
            }
            return Optional.empty();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
