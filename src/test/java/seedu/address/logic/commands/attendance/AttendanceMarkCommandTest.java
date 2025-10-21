package seedu.address.logic.commands.attendance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.attendance.AttendanceList;
import seedu.address.model.person.Date;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.ClassTag;
import seedu.address.testutil.ModelStub;
import seedu.address.testutil.PersonBuilder;

public class AttendanceMarkCommandTest {

    private static final StudentId VALID_STUDENT_ID = new StudentId("0123");
    private static final StudentId ANOTHER_STUDENT_ID = new StudentId("0899");
    //for personbuilder
    private static final String VALID_STUDENT_ID_STRING = "0123";
    private static final Date VALID_DATE = new Date("13012025");
    private static final Date ANOTHER_DATE = new Date("14012025");
    private static final ClassTag VALID_CLASS_TAG = new ClassTag("Math");
    private static final ClassTag ANOTHER_CLASS_TAG = new ClassTag("Science");

    @Test
    public void constructor_nullStudentId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AttendanceMarkCommand(null, VALID_DATE, VALID_CLASS_TAG));
    }

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AttendanceMarkCommand(VALID_STUDENT_ID,
                null, VALID_CLASS_TAG));
    }

    @Test
    public void constructor_nullClassTag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AttendanceMarkCommand(VALID_STUDENT_ID,
                VALID_DATE, null));
    }

    //check mark attendance
    @Test
    public void execute_validStudentAndDate_markSuccessful() throws Exception {
        ModelStubWithPerson modelStub = new ModelStubWithPerson();
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID_STRING)
                .withTags("Math").build();
        modelStub.person = validPerson;

        CommandResult commandResult = new AttendanceMarkCommand(VALID_STUDENT_ID,
                VALID_DATE, VALID_CLASS_TAG).execute(modelStub);

        assertEquals(String.format(AttendanceMarkCommand.MESSAGE_MARK_SUCCESS,
                        validPerson.getName(), VALID_DATE.getFormattedDate(), VALID_CLASS_TAG.tagName),
                commandResult.getFeedbackToUser());
        assertTrue(modelStub.markAttendanceCalled);

    }

    //attendance at that date already mark as absent, now change to present
    @Test
    public void execute_markedAsAbsent_markSuccessful() throws Exception {
        ModelStubWithPerson modelStub = new ModelStubWithPerson();
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID_STRING)
                .withTags("Math").build();

        // Mark attendance as absent first
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.unmarkAttendance(VALID_DATE, VALID_CLASS_TAG);
        validPerson = validPerson.withAttendanceList(attendanceList);
        modelStub.person = validPerson;

        AttendanceMarkCommand command = new AttendanceMarkCommand(VALID_STUDENT_ID, VALID_DATE, VALID_CLASS_TAG);
        CommandResult commandResult = command.execute(modelStub);

        // Should succeed - can change from absent to present
        assertEquals(String.format(AttendanceMarkCommand.MESSAGE_MARK_SUCCESS,
                        validPerson.getName(), VALID_DATE.getFormattedDate(), VALID_CLASS_TAG.tagName),
                commandResult.getFeedbackToUser());
        assertTrue(modelStub.markAttendanceCalled);
    }


    @Test
    public void execute_studentNotFound_throwsCommandException() {
        ModelStubWithoutPerson modelStub = new ModelStubWithoutPerson();

        AttendanceMarkCommand command = new AttendanceMarkCommand(VALID_STUDENT_ID, VALID_DATE, VALID_CLASS_TAG);

        assertThrows(CommandException.class, () -> command.execute(modelStub));
    }

    //attendance record at that date already present, throw exception
    @Test
    public void execute_alreadyMarked_throwsCommandException() throws Exception {
        ModelStubWithPerson modelStub = new ModelStubWithPerson();
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID_STRING)
                .withTags("Math").build();
        // Mark attendance first
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.markAttendance(VALID_DATE, VALID_CLASS_TAG);
        validPerson = validPerson.withAttendanceList(attendanceList);
        modelStub.person = validPerson;

        AttendanceMarkCommand command = new AttendanceMarkCommand(VALID_STUDENT_ID, VALID_DATE, VALID_CLASS_TAG);

        assertThrows(CommandException.class, () -> command.execute(modelStub));
    }

    //student does not have the specified class tag
    @Test
    public void execute_studentDoesNotHaveTag_throwsCommandException() {
        ModelStubWithPerson modelStub = new ModelStubWithPerson();
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID_STRING)
                .withTags("Science").build(); // Student has Science tag, not Math
        modelStub.person = validPerson;

        AttendanceMarkCommand command = new AttendanceMarkCommand(VALID_STUDENT_ID, VALID_DATE, VALID_CLASS_TAG);

        assertThrows(CommandException.class, () -> command.execute(modelStub));
    }


    @Test
    public void execute_nullModel_throwsNullPointerException() {
        AttendanceMarkCommand command = new AttendanceMarkCommand(VALID_STUDENT_ID, VALID_DATE, VALID_CLASS_TAG);
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void equals() {
        AttendanceMarkCommand markCommand1 = new AttendanceMarkCommand(VALID_STUDENT_ID, VALID_DATE, VALID_CLASS_TAG);
        AttendanceMarkCommand markCommand2 = new AttendanceMarkCommand(ANOTHER_STUDENT_ID, VALID_DATE, VALID_CLASS_TAG);
        AttendanceMarkCommand markCommand3 = new AttendanceMarkCommand(VALID_STUDENT_ID, ANOTHER_DATE, VALID_CLASS_TAG);
        AttendanceMarkCommand markCommand4 = new AttendanceMarkCommand(VALID_STUDENT_ID, VALID_DATE, ANOTHER_CLASS_TAG);

        // same object -> returns true
        assertTrue(markCommand1.equals(markCommand1));

        // same values -> returns true
        AttendanceMarkCommand markCommand1Copy = new AttendanceMarkCommand(VALID_STUDENT_ID,
                VALID_DATE, VALID_CLASS_TAG);
        assertTrue(markCommand1.equals(markCommand1Copy));

        // different types -> returns false
        assertFalse(markCommand1.equals(1));

        // null -> returns false
        assertFalse(markCommand1.equals(null));

        // different student ID -> returns false
        assertFalse(markCommand1.equals(markCommand2));

        // different date -> returns false
        assertFalse(markCommand1.equals(markCommand3));

        // different class tag -> returns false
        assertFalse(markCommand1.equals(markCommand4));
    }

    /**
     * A Model stub that contains a person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private Person person;
        private boolean markAttendanceCalled = false;

        @Override
        public Optional<Person> getPersonById(StudentId studentId) {
            return Optional.of(person);
        }

        @Override
        public void markAttendance(StudentId studentId, Date date, ClassTag classTag) {
            markAttendanceCalled = true;
            AttendanceList updatedAttendance = new AttendanceList(person.getAttendanceList().asUnmodifiableList());
            updatedAttendance.markAttendance(date, classTag);
            person = person.withAttendanceList(updatedAttendance);
        }
    }


    /**
     * A Model stub that does not contain any person.
     */
    private class ModelStubWithoutPerson extends ModelStub {
        @Override
        public Optional<Person> getPersonById(StudentId studentId) {
            return Optional.empty();
        }
    }
}

