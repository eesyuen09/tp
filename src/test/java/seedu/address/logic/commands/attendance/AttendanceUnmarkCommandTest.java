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
import seedu.address.testutil.ModelStub;
import seedu.address.testutil.PersonBuilder;

public class AttendanceUnmarkCommandTest {

    private static final StudentId VALID_STUDENT_ID = new StudentId("0123");
    private static final StudentId ANOTHER_STUDENT_ID = new StudentId("0752");
    //for personbuilder
    private static final String VALID_STUDENT_ID_STRING = "0123";
    private static final Date VALID_DATE = new Date("13012025");
    private static final Date ANOTHER_DATE = new Date("14012025");

    @Test
    public void constructor_nullStudentId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AttendanceUnmarkCommand(null, VALID_DATE));
    }

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AttendanceUnmarkCommand(VALID_STUDENT_ID, null));
    }

    //attendance at that date already mark as present, now change to absent
    @Test
    public void execute_validStudentAndDate_unmarkSuccessful() throws Exception {
        ModelStubWithPerson modelStub = new ModelStubWithPerson();
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID_STRING).build();
        // Mark attendance first so we can unmark it
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.markAttendance(VALID_DATE);
        validPerson = validPerson.withAttendanceList(attendanceList);
        modelStub.person = validPerson;

        CommandResult commandResult = new AttendanceUnmarkCommand(VALID_STUDENT_ID, VALID_DATE).execute(modelStub);

        assertEquals(String.format(AttendanceUnmarkCommand.MESSAGE_UNMARK_SUCCESS,
                        validPerson.getName(), VALID_DATE),
                commandResult.getFeedbackToUser());
        assertTrue(modelStub.unmarkAttendanceCalled);
    }

    //no attendance record at that date, mark as absent
    @Test
    public void execute_attendanceNotMarked_marksAsAbsent() throws CommandException {
        ModelStubWithPerson modelStub = new ModelStubWithPerson();
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID_STRING).build();
        // Don't mark attendance - person has no attendance records
        modelStub.person = validPerson;

        AttendanceUnmarkCommand command = new AttendanceUnmarkCommand(VALID_STUDENT_ID, VALID_DATE);

        CommandResult result = command.execute(modelStub);

        // Verify the command executes successfully
        assertEquals(String.format(AttendanceUnmarkCommand.MESSAGE_UNMARK_SUCCESS,
                validPerson.getName(), VALID_DATE), result.getFeedbackToUser());
        assertTrue(modelStub.unmarkAttendanceCalled);
    }

    //attendance record at that date already absent, throw exception
    @Test
    public void execute_alreadyMarkedAbsent_throwsCommandException() throws Exception {
        ModelStubWithPerson modelStub = new ModelStubWithPerson();
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID_STRING).build();

        // Mark attendance as absent first
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.unmarkAttendance(VALID_DATE);
        validPerson = validPerson.withAttendanceList(attendanceList);
        modelStub.person = validPerson;

        AttendanceUnmarkCommand command = new AttendanceUnmarkCommand(VALID_STUDENT_ID, VALID_DATE);

        // Should throw exception because attendance is already marked as absent
        assertThrows(CommandException.class, () -> command.execute(modelStub));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        AttendanceUnmarkCommand command = new AttendanceUnmarkCommand(VALID_STUDENT_ID, VALID_DATE);
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        ModelStubWithoutPerson modelStub = new ModelStubWithoutPerson();
        AttendanceUnmarkCommand command = new AttendanceUnmarkCommand(VALID_STUDENT_ID, VALID_DATE);
        assertThrows(CommandException.class, () -> command.execute(modelStub));
    }

    @Test
    public void equals() {
        AttendanceUnmarkCommand unmarkCommand1 = new AttendanceUnmarkCommand(VALID_STUDENT_ID, VALID_DATE);
        AttendanceUnmarkCommand unmarkCommand2 = new AttendanceUnmarkCommand(ANOTHER_STUDENT_ID, VALID_DATE);
        AttendanceUnmarkCommand unmarkCommand3 = new AttendanceUnmarkCommand(VALID_STUDENT_ID, ANOTHER_DATE);

        // same object -> returns true
        assertTrue(unmarkCommand1.equals(unmarkCommand1));

        // same values -> returns true
        AttendanceUnmarkCommand unmarkCommand1Copy = new AttendanceUnmarkCommand(VALID_STUDENT_ID, VALID_DATE);
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
        public void unmarkAttendance(StudentId studentId, Date date) {
            unmarkAttendanceCalled = true;
            AttendanceList updatedAttendance = new AttendanceList(person.getAttendanceList().asUnmodifiableList());
            updatedAttendance.unmarkAttendance(date);
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
