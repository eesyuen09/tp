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

public class AttendanceMarkCommandTest {

    private static final StudentId VALID_STUDENT_ID = new StudentId("0123");
    //for personbuilder
    private static final String VALID_STUDENT_ID_STRING = "1111";
    private static final Date VALID_DATE = new Date("13012025");

    @Test
    public void constructor_nullStudentId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AttendanceMarkCommand(null, VALID_DATE));
    }

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AttendanceMarkCommand(VALID_STUDENT_ID, null));
    }

    @Test
    public void execute_validStudentAndDate_markSuccessful() throws Exception {
        ModelStubWithPerson modelStub = new ModelStubWithPerson();
        Person validPerson = new PersonBuilder().withStudentId(VALID_STUDENT_ID_STRING).build();
        modelStub.person = validPerson;

        CommandResult commandResult = new AttendanceMarkCommand(VALID_STUDENT_ID, VALID_DATE).execute(modelStub);

        assertEquals(String.format(AttendanceMarkCommand.MESSAGE_MARK_SUCCESS,
                        validPerson.getName(), VALID_DATE),
                commandResult.getFeedbackToUser());
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        ModelStubWithoutPerson modelStub = new ModelStubWithoutPerson();

        AttendanceMarkCommand command = new AttendanceMarkCommand(VALID_STUDENT_ID, VALID_DATE);

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

