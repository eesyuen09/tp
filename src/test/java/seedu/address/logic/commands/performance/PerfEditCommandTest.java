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
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.performance.PerformanceList;
import seedu.address.model.person.performance.PerformanceNote;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Date;
import seedu.address.testutil.ModelStub;
import seedu.address.testutil.PersonBuilder;

public class PerfEditCommandTest {

    private static final StudentId VALID_STUDENT_ID = new StudentId("0123");
    private static final Date VALID_DATE_1 = new Date("15032024");
    private static final ClassTag VALID_CLASS_TAG_1 = new ClassTag("CS2103T");
    private static final String VALID_NOTE_1 = "Good performance in class";

    private static final String EDITED_NOTE = "Updated performance note";

    @Test
    public void constructor_nullStudentId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new PerfEditCommand(null, VALID_DATE_1, VALID_CLASS_TAG_1, EDITED_NOTE));
    }

    @Test
    public void constructor_nullNote_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new PerfEditCommand(VALID_STUDENT_ID, VALID_DATE_1, VALID_CLASS_TAG_1, null));
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        PerfEditCommand command = new PerfEditCommand(VALID_STUDENT_ID, VALID_DATE_1, VALID_CLASS_TAG_1, EDITED_NOTE);

        assertCommandFailure(command, model, String.format(Messages.MESSAGE_STUDENT_ID_NOT_FOUND, VALID_STUDENT_ID));
    }

    @Test
    public void execute_editPerformanceNote_success() throws Exception {
        Person student = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString())
                .withClassTags(VALID_CLASS_TAG_1.tagName).build();

        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_CLASS_TAG_1, VALID_NOTE_1);
        PerformanceList list = new PerformanceList();
        list.add(note);
        Person studentWithNote = student.withPerformanceList(list);

        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addClassTag(VALID_CLASS_TAG_1);
        model.addPerson(studentWithNote);

        // Expected edited performance note
        PerformanceNote editedNote = new PerformanceNote(VALID_DATE_1, VALID_CLASS_TAG_1, EDITED_NOTE);
        PerformanceList expectedList = new PerformanceList();
        expectedList.add(editedNote);
        Person expectedPerson = student.withPerformanceList(expectedList);

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        expectedModel.addClassTag(VALID_CLASS_TAG_1);
        expectedModel.addPerson(expectedPerson);

        String expectedMessage = String.format(PerfCommand.EDITED,
                student.getName(), VALID_CLASS_TAG_1.tagName, VALID_DATE_1.getFormattedDate());

        assertCommandSuccess(new PerfEditCommand(VALID_STUDENT_ID, VALID_DATE_1, VALID_CLASS_TAG_1, EDITED_NOTE),
                model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editUsingModelStub_success() throws Exception {
        Person student = new PersonBuilder().withStudentId(VALID_STUDENT_ID.toString())
                .withClassTags(VALID_CLASS_TAG_1.tagName).build();

        PerformanceNote note = new PerformanceNote(VALID_DATE_1, VALID_CLASS_TAG_1, VALID_NOTE_1);
        PerformanceList list = new PerformanceList();
        list.add(note);
        Person studentWithNote = student.withPerformanceList(list);

        ModelStubAcceptingPerformanceNoteEdited modelStub =
                new ModelStubAcceptingPerformanceNoteEdited(studentWithNote);

        new PerfEditCommand(VALID_STUDENT_ID, VALID_DATE_1, VALID_CLASS_TAG_1, EDITED_NOTE).execute(modelStub);

        assertEquals(EDITED_NOTE,
                modelStub.updatedPerson.getPerformanceList().asUnmodifiableList().get(0).getNote());
    }

    @Test
    public void equals() {
        PerfEditCommand commandA = new PerfEditCommand(VALID_STUDENT_ID,
                VALID_DATE_1, VALID_CLASS_TAG_1, EDITED_NOTE);
        PerfEditCommand commandB = new PerfEditCommand(new StudentId("0234"),
                VALID_DATE_1, VALID_CLASS_TAG_1, EDITED_NOTE);
        PerfEditCommand commandC = new PerfEditCommand(VALID_STUDENT_ID,
                new Date("01012025"), VALID_CLASS_TAG_1, EDITED_NOTE);
        PerfEditCommand commandD = new PerfEditCommand(VALID_STUDENT_ID,
                VALID_DATE_1, new ClassTag("CS1231"), EDITED_NOTE);
        PerfEditCommand commandE = new PerfEditCommand(VALID_STUDENT_ID,
                VALID_DATE_1, VALID_CLASS_TAG_1, "Different note");

        // same object -> true
        assertTrue(commandA.equals(commandA));

        // same values -> true
        assertTrue(commandA.equals(new PerfEditCommand(VALID_STUDENT_ID,
                VALID_DATE_1, VALID_CLASS_TAG_1, EDITED_NOTE)));

        // different types -> false
        assertFalse(commandA.equals(1));

        // null -> false
        assertFalse(commandA.equals(null));

        // different student ID -> false
        assertFalse(commandA.equals(commandB));

        // different date -> false
        assertFalse(commandA.equals(commandC));

        // different classTag -> false
        assertFalse(commandA.equals(commandD));

        // different note -> false
        assertFalse(commandA.equals(commandE));
    }

    /**
     * A Model stub that always accepts the performance note being edited.
     */
    private static class ModelStubAcceptingPerformanceNoteEdited extends ModelStub {
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

        @Override
        public boolean hasClassTag(ClassTag classTag) {
            return person.getTags().contains(classTag);
        }
    }
}
