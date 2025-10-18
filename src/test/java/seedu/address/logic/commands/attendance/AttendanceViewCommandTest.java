package seedu.address.logic.commands.attendance;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

public class AttendanceViewCommandTest {

    private static final StudentId VALID_STUDENT_ID = new StudentId("0123");
    //for personbuilder
    private static final String VALID_STUDENT_ID_STRING = "1111";
    private static final Date VALID_DATE_1 = new Date("13012025");
    private static final Date VALID_DATE_2 = new Date("14012025");

    @Test
    public void constructor_nullStudentId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AttendanceViewCommand(null));
    }

    @Test
    public void execute_validStudentWithRecords_viewSuccessful() throws Exception {
        ModelStubWithPerson modelStub = new ModelStubWithPerson();
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID_STRING).build();
        // Mark some attendance records
        AttendanceList attendanceList = new AttendanceList();
        attendanceList.markAttendance(VALID_DATE_1);
        attendanceList.markAttendance(VALID_DATE_2);
        attendanceList.unmarkAttendance(VALID_DATE_2); // Mark one as absent
        validPerson = validPerson.withAttendanceList(attendanceList);
        modelStub.person = validPerson;

        CommandResult commandResult = new AttendanceViewCommand(VALID_STUDENT_ID).execute(modelStub);

        String feedback = commandResult.getFeedbackToUser();
        assertTrue(feedback.contains(validPerson.getName().toString()));
        assertTrue(feedback.contains(VALID_DATE_1.getFormattedDate()));
        assertTrue(feedback.contains(VALID_DATE_2.getFormattedDate()));
    }

    @Test
    public void execute_validStudentNoRecords_showsNoRecordsMessage() throws Exception {
        ModelStubWithPerson modelStub = new ModelStubWithPerson();
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID_STRING).build();
        // Don't mark any attendance
        modelStub.person = validPerson;

        CommandResult commandResult = new AttendanceViewCommand(VALID_STUDENT_ID).execute(modelStub);

        assertEquals(String.format(AttendanceViewCommand.MESSAGE_NO_RECORDS, validPerson.getName()),
                commandResult.getFeedbackToUser());
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        ModelStubWithoutPerson modelStub = new ModelStubWithoutPerson();

        AttendanceViewCommand command = new AttendanceViewCommand(VALID_STUDENT_ID);

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
