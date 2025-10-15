package seedu.address.logic.commands.attendance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Date;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.testutil.ModelStub;
import seedu.address.testutil.PersonBuilder;

public class AttendanceUnmarkCommandTest {

    private static final StudentId VALID_STUDENT_ID = new StudentId("0123");
    //for personbuilder
    private static final String VALID_STUDENT_ID_STRING = "1111";
    private static final Date VALID_DATE = new Date("13012025");

    @Test
    public void constructor_nullStudentId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AttendanceUnmarkCommand(null, VALID_DATE));
    }

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AttendanceUnmarkCommand(VALID_STUDENT_ID, null));
    }

    @Test
    public void execute_validStudentAndDate_unmarkSuccessful() throws Exception {
        ModelStubWithPerson modelStub = new ModelStubWithPerson();
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID_STRING).build();
        // Mark attendance first so we can unmark it
        validPerson.markAttendance(VALID_DATE);
        modelStub.person = validPerson;

        CommandResult commandResult = new AttendanceUnmarkCommand(VALID_STUDENT_ID, VALID_DATE).execute(modelStub);

        assertEquals(String.format(AttendanceUnmarkCommand.MESSAGE_UNMARK_SUCCESS,
                        validPerson.getName(), VALID_DATE),
                commandResult.getFeedbackToUser());
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        ModelStubWithoutPerson modelStub = new ModelStubWithoutPerson();

        AttendanceUnmarkCommand command = new AttendanceUnmarkCommand(VALID_STUDENT_ID, VALID_DATE);

        assertThrows(CommandException.class, () -> command.execute(modelStub));
    }

    @Test
    public void execute_attendanceNotMarked_throwsCommandException() {
        ModelStubWithPerson modelStub = new ModelStubWithPerson();
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID_STRING).build();
        // Don't mark attendance - person has no attendance records
        modelStub.person = validPerson;

        AttendanceUnmarkCommand command = new AttendanceUnmarkCommand(VALID_STUDENT_ID, VALID_DATE);

        assertThrows(CommandException.class, () -> command.execute(modelStub));
    }

    /**
     * A Model stub that contains a person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private Person person;

        @Override
        public Optional<Person> getPersonById(StudentId studentId) {
            return Optional.of(person);
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
