package seedu.address.logic.commands.performance;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.performance.PerformanceList;
import seedu.address.model.person.performance.PerformanceNote;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Date;
import seedu.address.testutil.ModelStub;
import seedu.address.testutil.PersonBuilder;

public class PerfAddCommandTest {

    private static final StudentId VALID_STUDENT_ID = new StudentId("0123");
    private static final Date VALID_DATE = new Date("15032024");
    private static final ClassTag VALID_CLASS_TAG = new ClassTag("Math");
    private static final String VALID_NOTE = "Good performance in class";

    @Test
    public void constructor_nullStudentId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new PerfAddCommand(null, VALID_DATE, VALID_CLASS_TAG, VALID_NOTE));
    }

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new PerfAddCommand(VALID_STUDENT_ID, null, VALID_CLASS_TAG, VALID_NOTE));
    }

    @Test
    public void constructor_dateBeforeEnrolledMonth_throwsCommandException() {
        Person person = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString())
                .withEnrolledMonth("0324")
                .withClassTags(VALID_CLASS_TAG.tagName).build();
        ModelStub modelStub = new ModelStubAcceptingPerformanceNoteAdded(person);

        Date invalidDate = new Date("15022024");

        PerfAddCommand perfAddCommand = new PerfAddCommand(VALID_STUDENT_ID, invalidDate,
                VALID_CLASS_TAG, VALID_NOTE);

        assertThrows(CommandException.class, () ->
                perfAddCommand.execute(modelStub));
    }

    @Test
    public void constructor_dateInFuture_throwsCommandException() {
        Person person = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString())
                .withEnrolledMonth("0324")
                .withClassTags(VALID_CLASS_TAG.tagName).build();
        ModelStub modelStub = new ModelStubAcceptingPerformanceNoteAdded(person);

        Date invalidDate = new Date("15032030");

        PerfAddCommand perfAddCommand = new PerfAddCommand(VALID_STUDENT_ID, invalidDate,
                VALID_CLASS_TAG, VALID_NOTE);

        assertThrows(CommandException.class, () ->
                perfAddCommand.execute(modelStub));
    }

    @Test
    public void constructor_nullClassTag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new PerfAddCommand(VALID_STUDENT_ID, VALID_DATE, null, VALID_NOTE));
    }

    @Test
    public void constructor_nullNote_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new PerfAddCommand(VALID_STUDENT_ID, VALID_DATE, VALID_CLASS_TAG, null));
    }

    @Test
    public void execute_performanceNoteAcceptedByModel_addSuccessful() throws Exception {
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString())
                .withEnrolledMonth("0324")
                .withClassTags(VALID_CLASS_TAG.tagName).build();
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(validPerson);
        PerformanceNote note = new PerformanceNote(VALID_DATE, VALID_CLASS_TAG, VALID_NOTE);
        PerformanceList performanceList = new PerformanceList();
        performanceList.add(note);

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        Person personWithNote = validPerson.withPerformanceList(performanceList);
        expectedModel.addPerson(personWithNote);

        String expectedMessage = String.format(
                PerfCommand.ADDED,
                validPerson.getName(),
                VALID_CLASS_TAG.tagName,
                note.getDate().getFormattedDate()
        );

        assertCommandSuccess(new PerfAddCommand(VALID_STUDENT_ID, VALID_DATE, VALID_CLASS_TAG, VALID_NOTE),
                model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        PerfAddCommand perfAddCommand = new PerfAddCommand(VALID_STUDENT_ID, VALID_DATE, VALID_CLASS_TAG, VALID_NOTE);

        assertCommandFailure(perfAddCommand, model,
                String.format(Messages.MESSAGE_STUDENT_ID_NOT_FOUND, VALID_STUDENT_ID));
    }

    @Test
    public void execute_studentMissingClassTag_throwsCommandException() {
        // Student missing required class tag
        Person personWithoutTag = new PersonBuilder()
                .withStudentId(VALID_STUDENT_ID.toString())
                .withClassTags("Science") // different tag
                .build();

        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(personWithoutTag);

        PerfAddCommand command = new PerfAddCommand(VALID_STUDENT_ID, VALID_DATE, VALID_CLASS_TAG, VALID_NOTE);

        String expectedMessage = String.format(
                PerfAddCommand.MESSAGE_STUDENT_DOES_NOT_HAVE_TAG,
                personWithoutTag.getName(),
                VALID_CLASS_TAG.tagName
        );

        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void equals() {
        StudentId idA = new StudentId("0123");
        StudentId idB = new StudentId("0234");
        Date dateA = new Date("15032024");
        Date dateB = new Date("16032024");
        ClassTag tagA = new ClassTag("Math");
        ClassTag tagB = new ClassTag("Science");
        String noteA = "Good performance";
        String noteB = "Needs improvement";

        PerfAddCommand commandA = new PerfAddCommand(idA, dateA, tagA, noteA);
        PerfAddCommand commandACopy = new PerfAddCommand(idA, dateA, tagA, noteA);
        PerfAddCommand commandDifferentId = new PerfAddCommand(idB, dateA, tagA, noteA);
        PerfAddCommand commandDifferentDate = new PerfAddCommand(idA, dateB, tagA, noteA);
        PerfAddCommand commandDifferentTag = new PerfAddCommand(idA, dateA, tagB, noteA);
        PerfAddCommand commandDifferentNote = new PerfAddCommand(idA, dateA, tagA, noteB);

        // same object -> true
        assertTrue(commandA.equals(commandA));

        // same values -> true
        assertTrue(commandA.equals(commandACopy));

        // different types -> false
        assertFalse(commandA.equals(1));

        // null -> false
        assertFalse(commandA.equals(null));

        // different studentId -> false
        assertFalse(commandA.equals(commandDifferentId));

        // different date -> false
        assertFalse(commandA.equals(commandDifferentDate));

        // different tag -> false
        assertFalse(commandA.equals(commandDifferentTag));

        // different note -> false
        assertFalse(commandA.equals(commandDifferentNote));
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
