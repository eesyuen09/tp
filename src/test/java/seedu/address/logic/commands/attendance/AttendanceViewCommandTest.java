package seedu.address.logic.commands.attendance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.attendance.AttendanceHistoryEntry;
import seedu.address.model.attendance.AttendanceHistorySummary;
import seedu.address.model.attendance.AttendanceList;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Date;
import seedu.address.testutil.ModelStub;
import seedu.address.testutil.PersonBuilder;

public class AttendanceViewCommandTest {

    private static final StudentId VALID_STUDENT_ID = new StudentId("0123");
    private static final StudentId ANOTHER_STUDENT_ID = new StudentId("5432");
    //for personbuilder
    private static final String VALID_STUDENT_ID_STRING = "1111";
    private static final Date VALID_DATE_1 = new Date("13012025");
    private static final Date VALID_DATE_2 = new Date("14012025");
    private static final ClassTag VALID_CLASS_TAG = new ClassTag("Math");

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
        attendanceList.markAttendancePresent(VALID_DATE_1, VALID_CLASS_TAG);
        attendanceList.markAttendancePresent(VALID_DATE_2, VALID_CLASS_TAG);
        attendanceList.markAttendanceAbsent(VALID_DATE_2, VALID_CLASS_TAG); // Mark one as absent
        validPerson = validPerson.withAttendanceList(attendanceList);
        modelStub.person = validPerson;

        CommandResult commandResult = new AttendanceViewCommand(VALID_STUDENT_ID).execute(modelStub);

        String feedback = commandResult.getFeedbackToUser();
        String expectedHeader = String.format("Attendance history for %s (Student ID %s) displayed.",
                validPerson.getName().fullName, VALID_STUDENT_ID);
        assertEquals(expectedHeader, feedback);
        assertEquals(2, modelStub.displayedEntries.size());
        AttendanceHistorySummary summary = modelStub.displayedSummary;
        assertEquals(validPerson.getName().toString(), summary.getStudentName());
        assertEquals(validPerson.getStudentId(), summary.getStudentId());
        assertEquals(VALID_DATE_1, summary.getStartDate());
        assertEquals(VALID_DATE_2, summary.getEndDate());
        assertEquals(2, summary.getRecordCount());
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

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        AttendanceViewCommand command = new AttendanceViewCommand(VALID_STUDENT_ID);
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void equals() {
        AttendanceViewCommand viewCommand1 = new AttendanceViewCommand(VALID_STUDENT_ID);
        AttendanceViewCommand viewCommand2 = new AttendanceViewCommand(ANOTHER_STUDENT_ID);

        // same object -> returns true
        assertTrue(viewCommand1.equals(viewCommand1));

        // same values -> returns true
        AttendanceViewCommand viewCommand1Copy = new AttendanceViewCommand(VALID_STUDENT_ID);
        assertTrue(viewCommand1.equals(viewCommand1Copy));

        // different types -> returns false
        assertFalse(viewCommand1.equals(1));

        // null -> returns false
        assertFalse(viewCommand1.equals(null));

        // different student ID -> returns false
        assertFalse(viewCommand1.equals(viewCommand2));
    }

    /**
     * A Model stub that contains a person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private Person person;
        private List<AttendanceHistoryEntry> displayedEntries = List.of();
        private AttendanceHistorySummary displayedSummary;

        @Override
        public Optional<Person> getPersonById(StudentId studentId) {
            return Optional.of(person);
        }

        @Override
        public void setDisplayedAttendanceHistory(List<AttendanceHistoryEntry> entries,
                                                  AttendanceHistorySummary summary) {
            this.displayedEntries = entries;
            this.displayedSummary = summary;
        }

        @Override
        public void clearDisplayedAttendanceHistory() {
            this.displayedEntries = List.of();
            this.displayedSummary = null;
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
