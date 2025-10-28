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
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Date;
import seedu.address.testutil.ModelStub;
import seedu.address.testutil.PersonBuilder;

public class AttendanceDeleteCommandTest {

    private static final StudentId VALID_STUDENT_ID = new StudentId("0123");
    private static final StudentId ANOTHER_STUDENT_ID = new StudentId("0899");
    private static final String VALID_STUDENT_ID_STRING = "0123";
    private static final Date VALID_DATE = new Date("13012025");
    private static final Date ANOTHER_DATE = new Date("14012025");
    private static final ClassTag VALID_CLASS_TAG = new ClassTag("Math");
    private static final ClassTag ANOTHER_CLASS_TAG = new ClassTag("Science");

    @Test
    public void constructor_nullStudentId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new AttendanceDeleteCommand(null, VALID_DATE, VALID_CLASS_TAG));
    }

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new AttendanceDeleteCommand(VALID_STUDENT_ID, null, VALID_CLASS_TAG));
    }

    @Test
    public void constructor_nullClassTag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new AttendanceDeleteCommand(VALID_STUDENT_ID, VALID_DATE, null));
    }

    @Test
    public void execute_validStudentWithAttendanceRecord_deleteSuccessful() throws Exception {
        ModelStubWithPerson modelStub = new ModelStubWithPerson();
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID_STRING)
                .withClassTags("Math").build();

        // Add an attendance record first
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.markAttendance(VALID_DATE, VALID_CLASS_TAG);
        validPerson = validPerson.withAttendanceList(attendanceList);
        modelStub.person = validPerson;

        CommandResult commandResult = new AttendanceDeleteCommand(VALID_STUDENT_ID,
                VALID_DATE, VALID_CLASS_TAG).execute(modelStub);

        assertEquals(String.format(AttendanceDeleteCommand.MESSAGE_DELETE_SUCCESS,
                        validPerson.getName(), VALID_DATE.getFormattedDate(), VALID_CLASS_TAG.tagName),
                commandResult.getFeedbackToUser());
        assertTrue(modelStub.deleteAttendanceCalled);
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        ModelStubWithoutPerson modelStub = new ModelStubWithoutPerson();

        AttendanceDeleteCommand command = new AttendanceDeleteCommand(VALID_STUDENT_ID,
                VALID_DATE, VALID_CLASS_TAG);

        assertThrows(CommandException.class, () -> command.execute(modelStub));
    }

    @Test
    public void execute_noAttendanceRecord_throwsCommandException() {
        ModelStubWithPerson modelStub = new ModelStubWithPerson();
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID_STRING)
                .withClassTags("Math").build();
        modelStub.person = validPerson;

        AttendanceDeleteCommand command = new AttendanceDeleteCommand(VALID_STUDENT_ID,
                VALID_DATE, VALID_CLASS_TAG);

        assertThrows(CommandException.class, () -> command.execute(modelStub));
    }

    @Test
    public void execute_studentDoesNotHaveTag_throwsCommandException() {
        ModelStubWithPerson modelStub = new ModelStubWithPerson();
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID_STRING)
                .withClassTags("Science").build(); // Student has Science tag, not Math
        modelStub.person = validPerson;

        AttendanceDeleteCommand command = new AttendanceDeleteCommand(VALID_STUDENT_ID,
                VALID_DATE, VALID_CLASS_TAG);

        assertThrows(CommandException.class, () -> command.execute(modelStub));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        AttendanceDeleteCommand command = new AttendanceDeleteCommand(VALID_STUDENT_ID,
                VALID_DATE, VALID_CLASS_TAG);
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void equals() {
        AttendanceDeleteCommand deleteCommand1 = new AttendanceDeleteCommand(VALID_STUDENT_ID,
                VALID_DATE, VALID_CLASS_TAG);
        AttendanceDeleteCommand deleteCommand2 = new AttendanceDeleteCommand(ANOTHER_STUDENT_ID,
                VALID_DATE, VALID_CLASS_TAG);
        AttendanceDeleteCommand deleteCommand3 = new AttendanceDeleteCommand(VALID_STUDENT_ID,
                ANOTHER_DATE, VALID_CLASS_TAG);
        AttendanceDeleteCommand deleteCommand4 = new AttendanceDeleteCommand(VALID_STUDENT_ID,
                VALID_DATE, ANOTHER_CLASS_TAG);

        // same object -> returns true
        assertTrue(deleteCommand1.equals(deleteCommand1));

        // same values -> returns true
        AttendanceDeleteCommand deleteCommand1Copy = new AttendanceDeleteCommand(VALID_STUDENT_ID,
                VALID_DATE, VALID_CLASS_TAG);
        assertTrue(deleteCommand1.equals(deleteCommand1Copy));

        // different types -> returns false
        assertFalse(deleteCommand1.equals(1));

        // null -> returns false
        assertFalse(deleteCommand1.equals(null));

        // different student ID -> returns false
        assertFalse(deleteCommand1.equals(deleteCommand2));

        // different date -> returns false
        assertFalse(deleteCommand1.equals(deleteCommand3));

        // different class tag -> returns false
        assertFalse(deleteCommand1.equals(deleteCommand4));
    }

    /**
     * A Model stub that contains a person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private Person person;
        private boolean deleteAttendanceCalled = false;

        @Override
        public Optional<Person> getPersonById(StudentId studentId) {
            return Optional.of(person);
        }

        @Override
        public void deleteAttendance(StudentId studentId, Date date, ClassTag classTag) {
            deleteAttendanceCalled = true;
            AttendanceList updatedAttendance = new AttendanceList(person.getAttendanceList().asUnmodifiableList());
            updatedAttendance.deleteAttendance(date, classTag);
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
