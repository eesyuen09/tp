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
import seedu.address.model.tag.ClassTag;
import seedu.address.testutil.ModelStub;
import seedu.address.testutil.PersonBuilder;

public class PerfDeleteCommandTest {

    private static final StudentId VALID_STUDENT_ID = new StudentId("0123");
    private static final Date VALID_DATE_1 = new Date("15032024");
    private static final String VALID_NOTE_1 = "Good performance in class";
    private static final ClassTag VALID_TAG_1 = new ClassTag("Math");


    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PerfDeleteCommand(null, VALID_DATE_1, VALID_TAG_1));
        assertThrows(NullPointerException.class, () -> new PerfDeleteCommand(VALID_STUDENT_ID, null, VALID_TAG_1));
        assertThrows(NullPointerException.class, () -> new PerfDeleteCommand(VALID_STUDENT_ID, VALID_DATE_1, null));
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        PerfDeleteCommand command = new PerfDeleteCommand(VALID_STUDENT_ID, VALID_DATE_1, VALID_TAG_1);

        assertCommandFailure(command, model, String.format(Messages.MESSAGE_STUDENT_ID_NOT_FOUND, VALID_STUDENT_ID));
    }

    @Test
    public void execute_studentWithoutTag_throwsCommandException() {
        Person person = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString())
                .withTags("Science") // missing "Math"
                .build();
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(person);

        PerfDeleteCommand command = new PerfDeleteCommand(VALID_STUDENT_ID, VALID_DATE_1, VALID_TAG_1);

        String expectedMessage = String.format(PerfDeleteCommand.MESSAGE_STUDENT_DOES_NOT_HAVE_TAG,
                person.getName(), VALID_TAG_1.tagName);

        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_validDelete_successful() throws Exception {
        Person validPerson = new PersonBuilder()
                .withStudentId(VALID_STUDENT_ID.toString())
                .withTags("Math")
                .build();

        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_TAG_1, VALID_NOTE_1);
        PerformanceList performanceList = new PerformanceList();
        performanceList.add(note);
        Person personWithNote = validPerson.withPerformanceList(performanceList);

        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(personWithNote);

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        expectedModel.addPerson(validPerson.withPerformanceList(new PerformanceList()));

        String expectedMessage = String.format(PerfCommand.DELETED,
                validPerson.getName(), VALID_TAG_1.tagName, VALID_DATE_1.getFormattedDate());

        assertCommandSuccess(new PerfDeleteCommand(VALID_STUDENT_ID, VALID_DATE_1, VALID_TAG_1),
                model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noteNotFound_throwsCommandException() {
        // Create a student with the class tag but no performance notes
        Person validPerson = new PersonBuilder()
                .withStudentId(VALID_STUDENT_ID.toString())
                .withTags(VALID_TAG_1.tagName) // ensure student has the class tag
                .build();

        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(validPerson);

        // Attempt to delete a performance note for a date where none exists
        PerfDeleteCommand command = new PerfDeleteCommand(VALID_STUDENT_ID, VALID_DATE_1, VALID_TAG_1);

        assertCommandFailure(command, model,
                "No performance note found for the given date and class tag.");
    }

    @Test
    public void execute_deleteUsingModelStub_success() throws Exception {
        Person validPerson = new PersonBuilder()
                .withStudentId(VALID_STUDENT_ID.toString())
                .withTags("Math")
                .build();

        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_TAG_1 , VALID_NOTE_1);
        PerformanceList list = new PerformanceList();
        list.add(note);
        Person personWithNote = validPerson.withPerformanceList(list);

        ModelStubAcceptingPerformanceNoteDeleted modelStub =
                new ModelStubAcceptingPerformanceNoteDeleted(personWithNote);

        new PerfDeleteCommand(VALID_STUDENT_ID, VALID_DATE_1, VALID_TAG_1).execute(modelStub);

        assertTrue(modelStub.updatedPerson.getPerformanceList().asUnmodifiableList().isEmpty());
    }

    @Test
    public void equals() {
        StudentId studentA = new StudentId("0123");
        StudentId studentB = new StudentId("0456");
        Date dateA = new Date("15032024");
        Date dateB = new Date("16032024");
        ClassTag tagA = new ClassTag("Math");
        ClassTag tagB = new ClassTag("Science");

        PerfDeleteCommand cmd1 = new PerfDeleteCommand(studentA, dateA, tagA);
        PerfDeleteCommand cmd2 = new PerfDeleteCommand(studentB, dateA, tagA);
        PerfDeleteCommand cmd3 = new PerfDeleteCommand(studentA, dateB, tagA);
        PerfDeleteCommand cmd4 = new PerfDeleteCommand(studentA, dateA, tagB);

        // same object
        assertTrue(cmd1.equals(cmd1));

        // same values
        assertTrue(cmd1.equals(new PerfDeleteCommand(studentA, dateA, tagA)));

        // different studentId
        assertFalse(cmd1.equals(cmd2));

        // different date
        assertFalse(cmd1.equals(cmd3));

        // different tag
        assertFalse(cmd1.equals(cmd4));

        // null
        assertFalse(cmd1.equals(null));

        // different type
        assertFalse(cmd1.equals(1));
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
