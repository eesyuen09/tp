package seedu.address.logic.commands.fee;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.FeeTestUtil.payAllMonthsBefore;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandTestUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.StudentId;
import seedu.address.model.time.Month;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code FeeMarkUnpaidCommand}.
 */
public class FeeMarkUnpaidCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        AddressBook ab = getTypicalAddressBook();
        model = new ModelManager(new AddressBook(ab), new UserPrefs());
        expectedModel = new ModelManager(new AddressBook(ab), new UserPrefs());
    }

    @Test
    public void execute_markUnpaid_success() {
        StudentId id = ALICE.getStudentId();
        Month month = new Month("0925");

        payAllMonthsBefore(model, ALICE, month);
        payAllMonthsBefore(expectedModel, ALICE, month);
        model.markPaid(ALICE.getStudentId(), month);
        expectedModel.markPaid(ALICE.getStudentId(), month);

        FeeMarkUnpaidCommand command = new FeeMarkUnpaidCommand(id, month);
        expectedModel.markUnpaid(id, month);

        String expectedMessage = String.format(
            FeeMarkUnpaidCommand.MESSAGE_SUCCESS,
            ALICE.getName().fullName,
            month.toHumanReadable()
        );

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        StudentId missing = new StudentId("3250");
        Month month = new Month("0925");

        FeeMarkUnpaidCommand command = new FeeMarkUnpaidCommand(missing, month);
        String expectedMessage = String.format(
            seedu.address.logic.Messages.MESSAGE_STUDENT_ID_NOT_FOUND,
            missing
        );
        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_monthBeforeEnrollment_throwsCommandException() {
        // Assuming ALICE’s enrolled month in TypicalPersons is "0825"
        StudentId id = ALICE.getStudentId();
        String name = ALICE.getName().fullName;
        Month enrollment = ALICE.getEnrolledMonth();
        Month beforeEnrollment = new Month("0725");
        FeeMarkUnpaidCommand command = new FeeMarkUnpaidCommand(id, beforeEnrollment);
        String expectedMessage = String.format(FeeCommand.MESSAGE_INVALID_MONTH, name, enrollment.toHumanReadable());
        CommandTestUtil.assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_alreadyUnpaid_throwsCommandException() {
        StudentId id = ALICE.getStudentId();
        Month month = ALICE.getEnrolledMonth(); // default UNPAID if not marked

        FeeMarkUnpaidCommand command = new FeeMarkUnpaidCommand(id, month);

        // Should fail because effective state is already UNPAID by default
        assertThrows(IllegalStateException.class, () -> model.markUnpaid(ALICE.getStudentId(), month));
        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_futureMonth_throwsCommandException() {
        StudentId id = ALICE.getStudentId();
        Month future = Month.now().plusMonths(1);
        FeeMarkUnpaidCommand command = new FeeMarkUnpaidCommand(id, future);

        String expectedMessage = "You can’t mark unpaid for a future month. "
            + "Please try again when the month has started.";

        CommandTestUtil.assertCommandFailure(command, model, expectedMessage);
    }


    @Test
    public void equals() {
        StudentId id1 = ALICE.getStudentId();
        Month m1 = new Month("0925");
        Month m2 = new Month("1025");

        FeeMarkUnpaidCommand cmd1 = new FeeMarkUnpaidCommand(id1, m1);
        FeeMarkUnpaidCommand cmd1Copy = new FeeMarkUnpaidCommand(id1, m1);
        FeeMarkUnpaidCommand cmdDiffMonth = new FeeMarkUnpaidCommand(id1, m2);
        FeeMarkUnpaidCommand cmdDiffId = new FeeMarkUnpaidCommand(new StudentId("7777"), m1);

        assertTrue(cmd1.equals(cmd1));
        assertTrue(cmd1.equals(cmd1Copy));
        assertFalse(cmd1.equals(null));
        assertFalse(cmd1.equals("not a command"));
        assertFalse(cmd1.equals(cmdDiffMonth));
        assertFalse(cmd1.equals(cmdDiffId));
    }
}
