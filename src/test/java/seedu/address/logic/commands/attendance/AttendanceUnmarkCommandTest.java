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

public class AttendanceUnmarkCommandTest {

    private static final StudentId VALID_STUDENT_ID = new StudentId("0123");
    private static final StudentId ANOTHER_STUDENT_ID = new StudentId("0752");
    //for personbuilder
    private static final String VALID_STUDENT_ID_STRING = "0123";
    private static final Date VALID_DATE = new Date("13012025");
    private static final Date ANOTHER_DATE = new Date("14012025");
    private static final ClassTag VALID_CLASS_TAG = new ClassTag("Math");
    private static final ClassTag ANOTHER_CLASS_TAG = new ClassTag("Science");

    @Test
    public void constructor_nullStudentId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AttendanceUnmarkCommand(null, VALID_DATE, VALID_CLASS_TAG));
    }

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AttendanceUnmarkCommand(VALID_STUDENT_ID,
                null, VALID_CLASS_TAG));
    }

    //attendance at that date already mark as present, now change to absent
    @Test
    public void execute_validStudentAndDate_unmarkSuccessful() throws Exception {
        ModelStubWithPerson modelStub = new ModelStubWithPerson();
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID_STRING)
                .withTags("Math").build();
        // Mark attendance first so we can unmark it
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.markAttendance(VALID_DATE, VALID_CLASS_TAG);
        validPerson = validPerson.withAttendanceList(attendanceList);
        modelStub.person = validPerson;

        CommandResult commandResult = new AttendanceUnmarkCommand(VALID_STUDENT_ID, VALID_DATE,
                VALID_CLASS_TAG).execute(modelStub);

        assertEquals(String.format(AttendanceUnmarkCommand.MESSAGE_UNMARK_SUCCESS,
                        validPerson.getName(), VALID_DATE.getFormattedDate(), VALID_CLASS_TAG.tagName),
                commandResult.getFeedbackToUser());
        assertTrue(modelStub.unmarkAttendanceCalled);
    }

    //no attendance record at that date, mark as absent
    @Test
    public void execute_attendanceNotMarked_marksAsAbsent() throws CommandException {
        ModelStubWithPerson modelStub = new ModelStubWithPerson();
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID_STRING)
                .withTags("Math").build();
        // Don't mark attendance - person has no attendance records
        modelStub.person = validPerson;

        AttendanceUnmarkCommand command = new AttendanceUnmarkCommand(VALID_STUDENT_ID, VALID_DATE, VALID_CLASS_TAG);

        CommandResult result = command.execute(modelStub);

        // Verify the command executes successfully
        assertEquals(String.format(AttendanceUnmarkCommand.MESSAGE_UNMARK_SUCCESS,
                validPerson.getName(), VALID_DATE.getFormattedDate(), VALID_CLASS_TAG.tagName),
                result.getFeedbackToUser());
        assertTrue(modelStub.unmarkAttendanceCalled);
    }

    //attendance record at that date already absent, throw exception
    @Test
    public void execute_alreadyMarkedAbsent_throwsCommandException() throws Exception {
        ModelStubWithPerson modelStub = new ModelStubWithPerson();
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID_STRING)
                .withTags("Math").build();

        // Mark attendance as absent first
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.unmarkAttendance(VALID_DATE, VALID_CLASS_TAG);
        validPerson = validPerson.withAttendanceList(attendanceList);
        modelStub.person = validPerson;

        AttendanceUnmarkCommand command = new AttendanceUnmarkCommand(VALID_STUDENT_ID, VALID_DATE, VALID_CLASS_TAG);

        // Should throw exception because attendance is already marked as absent
        assertThrows(CommandException.class, () -> command.execute(modelStub));
    }

    //student does not have the specified class tag
    @Test
    public void execute_studentDoesNotHaveTag_throwsCommandException() {
        ModelStubWithPerson modelStub = new ModelStubWithPerson();
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID_STRING)
                .withTags("Science").build(); // Student has Science tag, not Math
        modelStub.person = validPerson;

        AttendanceUnmarkCommand command = new AttendanceUnmarkCommand(VALID_STUDENT_ID, VALID_DATE, VALID_CLASS_TAG);

        assertThrows(CommandException.class, () -> command.execute(modelStub));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        AttendanceUnmarkCommand command = new AttendanceUnmarkCommand(VALID_STUDENT_ID, VALID_DATE, VALID_CLASS_TAG);
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        ModelStubWithoutPerson modelStub = new ModelStubWithoutPerson();
        AttendanceUnmarkCommand command = new AttendanceUnmarkCommand(VALID_STUDENT_ID, VALID_DATE,
                VALID_CLASS_TAG);
        assertThrows(CommandException.class, () -> command.execute(modelStub));
    }

    @Test
    public void equals() {
        AttendanceUnmarkCommand unmarkCommand1 = new AttendanceUnmarkCommand(VALID_STUDENT_ID, VALID_DATE,
                VALID_CLASS_TAG);
        AttendanceUnmarkCommand unmarkCommand2 = new AttendanceUnmarkCommand(ANOTHER_STUDENT_ID, VALID_DATE,
                VALID_CLASS_TAG);
        AttendanceUnmarkCommand unmarkCommand3 = new AttendanceUnmarkCommand(VALID_STUDENT_ID, ANOTHER_DATE,
                VALID_CLASS_TAG);

        // same object -> returns true
        assertTrue(unmarkCommand1.equals(unmarkCommand1));

        // same values -> returns true
        AttendanceUnmarkCommand unmarkCommand1Copy = new AttendanceUnmarkCommand(VALID_STUDENT_ID,
                VALID_DATE, VALID_CLASS_TAG);
        assertTrue(unmarkCommand1.equals(unmarkCommand1Copy));

        // different types -> returns false
        assertFalse(unmarkCommand1.equals(1));

        // null -> returns false
        assertFalse(unmarkCommand1.equals(null));

        // different student ID -> returns false
        assertFalse(unmarkCommand1.equals(unmarkCommand2));

        // different date -> returns false
        assertFalse(unmarkCommand1.equals(unmarkCommand3));
    }

    /**
     * A Model stub that contains a person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private Person person;
        private boolean unmarkAttendanceCalled = false;

        @Override
        public Optional<Person> getPersonById(StudentId studentId) {
            return Optional.of(person);
        }

        @Override
        public void unmarkAttendance(StudentId studentId, Date date, ClassTag classTag) {
            unmarkAttendanceCalled = true;
            AttendanceList updatedAttendance = new AttendanceList(person.getAttendanceList().asUnmodifiableList());
            updatedAttendance.unmarkAttendance(date, classTag);
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
