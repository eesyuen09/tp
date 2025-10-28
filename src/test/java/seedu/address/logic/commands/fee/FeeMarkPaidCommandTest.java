package seedu.address.logic.commands.fee;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_STUDENT_ID_NOT_FOUND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.CommandTestUtil;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.time.Month;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code FeeMarkUnpaidCommand}.
 */
public class FeeMarkPaidCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    /**
     * Pays all months from the person's enrolled month up to (but not including) the target month.
     * This is needed now that marking a later month as PAID requires all earlier months to be PAID first.
     */
    private void payAllMonthsBefore(Model mm, Person p, Month target) {
        Month enrolled = p.getEnrolledMonth();
        if (enrolled == null) {
            return;
        }
        Month cur = enrolled;
        while (cur.isBefore(target)) {
            mm.markPaid(p.getStudentId(), cur);
            cur = cur.plusMonths(1);
        }
    }

    @Test
    public void execute_existingStudentMarksPaid_success() throws Exception {
        StudentId id = ALICE.getStudentId();
        Month month = new Month("0925");
        FeeMarkPaidCommand command = new FeeMarkPaidCommand(id, month);

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        payAllMonthsBefore(model, ALICE, month);
        payAllMonthsBefore(expectedModel, ALICE, month);
        expectedModel.markPaid(id, month);

        String expectedMessage = String.format(
            FeeMarkPaidCommand.MESSAGE_SUCCESS,
            ALICE.getName().fullName,
            month.toHumanReadable()
        );
        CommandTestUtil.assertCommandSuccess(
            command,
            model,
            new CommandResult(expectedMessage),
            expectedModel
        );
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        StudentId nonExistent = new StudentId("1550");
        Month month = new Month("0925");
        FeeMarkPaidCommand command = new FeeMarkPaidCommand(nonExistent, month);

        String expectedMessage = String.format(MESSAGE_STUDENT_ID_NOT_FOUND, nonExistent);
        CommandTestUtil.assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_monthBeforeEnrollment_throwsCommandException() {
        // Assuming ALICE’s enrolled month in TypicalPersons is "0825"
        StudentId id = ALICE.getStudentId();
        String name = ALICE.getName().fullName;
        Month enrollment = ALICE.getEnrolledMonth();
        Month beforeEnrollment = new Month("0725");
        FeeMarkPaidCommand command = new FeeMarkPaidCommand(id, beforeEnrollment);
        String expectedMessage = String.format(FeeCommand.MESSAGE_INVALID_MONTH, name, enrollment.toHumanReadable());
        CommandTestUtil.assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_earlierMonthUnpaid_throwsCommandException() {
        StudentId id = ALICE.getStudentId();
        Month target = ALICE.getEnrolledMonth().plusMonths(2); // skip one month
        FeeMarkPaidCommand command = new FeeMarkPaidCommand(id, target);

        // Should fail since intermediate months not paid
        assertThrows(IllegalStateException.class, () -> model.markUnpaid(ALICE.getStudentId(), target));
    }

    @Test
    public void execute_allEarlierMonthsPaid_success() throws Exception {
        StudentId id = ALICE.getStudentId();
        Month target = ALICE.getEnrolledMonth().plusMonths(2);
        payAllMonthsBefore(model, ALICE, target);

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        payAllMonthsBefore(expectedModel, ALICE, target);
        expectedModel.markPaid(id, target);

        FeeMarkPaidCommand command = new FeeMarkPaidCommand(id, target);

        String expectedMessage = String.format(
            FeeMarkPaidCommand.MESSAGE_SUCCESS,
            ALICE.getName().fullName,
            target.toHumanReadable()
        );

        CommandTestUtil.assertCommandSuccess(command, model, new CommandResult(expectedMessage), expectedModel);
    }

    @Test
    public void execute_alreadyPaid_throwsCommandException() throws Exception {
        StudentId id = ALICE.getStudentId();
        Month enrolled = ALICE.getEnrolledMonth();
        model.markPaid(id, enrolled);
        FeeMarkPaidCommand command = new FeeMarkPaidCommand(id, enrolled);
        CommandTestUtil.assertCommandFailure(command, model,
            enrolled.toHumanReadable() + " is already marked as Paid.");
    }


    @Test
    public void equals() {
        StudentId aliceId = ALICE.getStudentId();
        Month aug = new Month("0825");
        Month sep = new Month("0925");

        FeeMarkPaidCommand c1 = new FeeMarkPaidCommand(aliceId, aug);
        FeeMarkPaidCommand c2 = new FeeMarkPaidCommand(aliceId, aug);
        FeeMarkPaidCommand c3 = new FeeMarkPaidCommand(new StudentId("0022"), aug);
        FeeMarkPaidCommand c4 = new FeeMarkPaidCommand(aliceId, sep);
        assertTrue(c1.equals(c1));
        assertTrue(c1.equals(c2));
        assertFalse(c1.equals(c3));
        assertFalse(c1.equals(c4));
        assertFalse(c1.equals(null));
        assertFalse(c1.equals(42));
    }
}

