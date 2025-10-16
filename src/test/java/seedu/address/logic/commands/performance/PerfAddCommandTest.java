package seedu.address.logic.commands.performance;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

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

public class PerfAddCommandTest {

    private static final StudentId VALID_STUDENT_ID = new StudentId("0123");
    private static final Date VALID_DATE = new Date("15032024");
    private static final String VALID_NOTE = "Good performance in class";

    @Test
    public void constructor_nullStudentId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new PerfAddCommand(null, VALID_DATE, VALID_NOTE));
    }

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new PerfAddCommand(VALID_STUDENT_ID, null, VALID_NOTE));
    }

    @Test
    public void constructor_nullNote_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new PerfAddCommand(VALID_STUDENT_ID, VALID_DATE, null));
    }

    @Test
    public void execute_performanceNoteAcceptedByModel_addSuccessful() throws Exception {
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString()).build();
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(validPerson);
        PerformanceNote note = new PerformanceNote(VALID_DATE, VALID_NOTE);
        PerformanceList performanceList = new PerformanceList();
        performanceList.add(note);

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        Person personWithNote = validPerson.withPerformanceList(performanceList);
        expectedModel.addPerson(personWithNote);

        String expectedMessage = String.format(PerfNotes.ADDED,
                validPerson.getName(), note.getDate().getFormattedDate());

        assertCommandSuccess(new PerfAddCommand(VALID_STUDENT_ID, VALID_DATE, VALID_NOTE),
                model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        PerfAddCommand perfAddCommand = new PerfAddCommand(VALID_STUDENT_ID, VALID_DATE, VALID_NOTE);

        assertCommandFailure(perfAddCommand, model, PerfNotes.STUDENT_NOT_FOUND);
    }

    @Test
    public void equals() {
        StudentId studentIdA = new StudentId("0123");
        StudentId studentIdB = new StudentId("0234");

        PerfAddCommand addNoteACommand = new PerfAddCommand(studentIdA, VALID_DATE, VALID_NOTE);
        PerfAddCommand addNoteBCommand = new PerfAddCommand(studentIdB, VALID_DATE, VALID_NOTE);
        PerfAddCommand addNoteDifferentDate = new PerfAddCommand(studentIdA, new Date("16032024"), VALID_NOTE);
        PerfAddCommand addNoteDifferentNote = new PerfAddCommand(studentIdA, VALID_DATE, "Different note");

        // same object -> returns true
        assertTrue(addNoteACommand.equals(addNoteACommand));

        // same values -> returns true
        PerfAddCommand addNoteACommandCopy = new PerfAddCommand(studentIdA, VALID_DATE, VALID_NOTE);
        assertTrue(addNoteACommand.equals(addNoteACommandCopy));

        // different types -> returns false
        assertFalse(addNoteACommand.equals(1));

        // null -> returns false
        assertFalse(addNoteACommand.equals(null));

        // different student ID -> returns false
        assertFalse(addNoteACommand.equals(addNoteBCommand));

        // different date -> returns false
        assertFalse(addNoteACommand.equals(addNoteDifferentDate));

        // different note -> returns false
        assertFalse(addNoteACommand.equals(addNoteDifferentNote));
    }

    /**
     * A Model stub that always accepts the performance note being added.
     */
    private class ModelStubAcceptingPerformanceNoteAdded extends ModelStub {
        private final Person person;
        private Person updatedPerson;

        ModelStubAcceptingPerformanceNoteAdded(Person person) {
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
