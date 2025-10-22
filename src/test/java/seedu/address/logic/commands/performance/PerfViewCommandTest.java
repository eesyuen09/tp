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
import seedu.address.model.tag.ClassTag;
import seedu.address.testutil.ModelStub;
import seedu.address.testutil.PersonBuilder;

public class PerfViewCommandTest {

    private static final StudentId VALID_STUDENT_ID = new StudentId("0123");
    private static final Date VALID_DATE_1 = new Date("15032024");
    private static final ClassTag VALID_CLASS_TAG_1 = new ClassTag("CS2103T");
    private static final String VALID_NOTE_1 = "Good performance in class";
    private static final Date VALID_DATE_2 = new Date("20032024");
    private static final ClassTag VALID_CLASS_TAG_2 = new ClassTag("CS1231");
    private static final String VALID_NOTE_2 = "Excellent homework submission";

    @Test
    public void constructor_nullStudentId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PerfViewCommand(null));
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        PerfViewCommand command = new PerfViewCommand(VALID_STUDENT_ID);

        assertCommandFailure(command, model, Messages.MESSAGE_STUDENT_ID_NOT_FOUND);
    }

    @Test
    public void execute_studentWithNoNotes_returnsNoneMessage() throws Exception {
        Person student = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString()).build();
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(student);

        String expectedMessage = student.getName() + " Performance Notes:\n(none)";
        assertCommandSuccess(new PerfViewCommand(VALID_STUDENT_ID), model, expectedMessage, model);
    }

    @Test
    public void execute_studentWithOneNote_displaysNote() throws Exception {
        Person student = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString()).build();

        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_CLASS_TAG_1, VALID_NOTE_1);
        PerformanceList list = new PerformanceList();
        list.add(note);
        Person studentWithNote = student.withPerformanceList(list);

        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(studentWithNote);

        String expectedMessage = studentWithNote.getName() + " Performance Notes:\n"
                + "1. " + note.getDate().getFormattedDate() + " " + note.getClassTag().tagName
                + ": " + note.getNote();

        assertCommandSuccess(new PerfViewCommand(VALID_STUDENT_ID), model, expectedMessage, model);
    }

    @Test
    public void execute_studentWithMultipleNotes_displaysAllNotes() throws Exception {
        Person student = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString()).build();

        PerformanceNote note1 = new PerformanceNote(VALID_DATE_1, VALID_CLASS_TAG_1, VALID_NOTE_1);
        PerformanceNote note2 = new PerformanceNote(VALID_DATE_2, VALID_CLASS_TAG_2, VALID_NOTE_2);
        PerformanceList list = new PerformanceList();
        list.add(note1);
        list.add(note2);
        Person studentWithNotes = student.withPerformanceList(list);

        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(studentWithNotes);

        String expectedMessage = studentWithNotes.getName() + " Performance Notes:\n"
                + "1. " + note1.getDate().getFormattedDate() + " " + note1.getClassTag().tagName
                + ": " + note1.getNote() + "\n"
                + "2. " + note2.getDate().getFormattedDate() + " " + note2.getClassTag().tagName
                + ": " + note2.getNote();

        assertCommandSuccess(new PerfViewCommand(VALID_STUDENT_ID), model, expectedMessage, model);
    }

    @Test
    public void execute_studentWithNotesUsingModelStub_success() throws Exception {
        Person student = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString()).build();

        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_CLASS_TAG_1, VALID_NOTE_1);
        PerformanceList list = new PerformanceList();
        list.add(note);
        Person studentWithNote = student.withPerformanceList(list);

        ModelStubWithPerson modelStub = new ModelStubWithPerson(studentWithNote);
        CommandResult result = new PerfViewCommand(VALID_STUDENT_ID).execute(modelStub);

        String expectedMessage = studentWithNote.getName() + " Performance Notes:\n"
                + "1. " + note.getDate().getFormattedDate() + " " + note.getClassTag().tagName
                + ": " + note.getNote();

        assertTrue(result.getFeedbackToUser().equals(expectedMessage));
    }

    @Test
    public void equals() {
        StudentId idA = new StudentId("0123");
        StudentId idB = new StudentId("0234");

        PerfViewCommand commandA = new PerfViewCommand(idA);
        PerfViewCommand commandB = new PerfViewCommand(idB);

        // same object -> true
        assertTrue(commandA.equals(commandA));

        // same values -> true
        assertTrue(commandA.equals(new PerfViewCommand(idA)));

        // different types -> false
        assertFalse(commandA.equals(1));

        // null -> false
        assertFalse(commandA.equals(null));

        // different student ID -> false
        assertFalse(commandA.equals(commandB));
    }

    /**
     * Model stub containing a single person.
     */
    private static class ModelStubWithPerson extends ModelStub {
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
