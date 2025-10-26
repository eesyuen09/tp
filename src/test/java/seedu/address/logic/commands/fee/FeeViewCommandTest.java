package seedu.address.logic.commands.fee;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_STUDENT_ID_NOT_FOUND;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Month;
import seedu.address.model.person.StudentId;

public class FeeViewCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_defaultFromEnrollmentToNow() throws Exception {
        StudentId aliceId = ALICE.getStudentId();
        Month enrolled = ALICE.getEnrolledMonth();

        // Make one month explicitly PAID (enrolled)
        Month paidMonth = enrolled;
        model.markPaid(aliceId, paidMonth);

        // Make the next month explicitly UNPAID
        Month explicitUnpaidMonth = enrolled.plusMonths(2);
        // Pay all months up to explicitUnpaidMonth
        Month cur = enrolled;
        while (cur.isBefore(explicitUnpaidMonth)) {
            // enrolled is already paid; loop will skip the first or pay it again only if needed
            cur = cur.plusMonths(1);
            model.markPaid(aliceId, cur);
        }
        // Now flip that month back to UNPAID to make it explicit
        model.markUnpaid(aliceId, explicitUnpaidMonth);

        FeeViewCommand cmd = new FeeViewCommand(aliceId, Optional.empty());
        CommandResult result = cmd.execute(model);
        String msg = result.getFeedbackToUser();

        // Header information
        assertTrue(msg.contains("Payment history for " + ALICE.getName().fullName),
            "Should include student's name");
        assertTrue(msg.contains("from " + enrolled.toHumanReadable() + " to "),
            "Should start from enrolled month when no start month provided");
        assertTrue(msg.contains("Enrolled Month: " + enrolled.toHumanReadable()),
            "Should echo enrolled month");

        // Rows: explicitly marked PAID and explicitly marked UNPAID
        assertTrue(msg.contains(paidMonth.toHumanReadable() + " : PAID (marked)"),
            "Should contain explicitly marked PAID row");
        assertTrue(msg.contains(explicitUnpaidMonth.toHumanReadable() + " : UNPAID (marked)"),
            "Should contain explicitly marked UNPAID row");
    }

    @Test
    public void execute_startBeforeEnrollment_clampsToEnrollment() throws Exception {
        StudentId bensonId = BENSON.getStudentId();
        Month enrolled = BENSON.getEnrolledMonth();
        Month requestedStart = enrolled.plusMonths(-4); // earlier than enrollment

        FeeViewCommand cmd = new FeeViewCommand(bensonId, Optional.of(requestedStart));
        CommandResult result = cmd.execute(model);
        String msg = result.getFeedbackToUser();

        // Effective start should be clamped to enrolled month in the header
        assertTrue(msg.contains("Payment history for " + BENSON.getName().fullName));
        assertTrue(msg.contains("from " + enrolled.toHumanReadable() + " to "),
            "Effective start should be the enrolled month");
        assertTrue(msg.contains("Enrolled Month: " + enrolled.toHumanReadable()));
    }

    @Test
    public void execute_startAfterNow_returnsNoHistoryMessage() throws Exception {
        StudentId aliceId = ALICE.getStudentId();
        Month futureStart = Month.now().plusMonths(2); // start > end(now)

        FeeViewCommand cmd = new FeeViewCommand(aliceId, Optional.of(futureStart));
        CommandResult result = cmd.execute(model);
        String msg = result.getFeedbackToUser();

        String expected = String.format(FeeViewCommand.MESSAGE_NO_HISTORY_IN_RANGE,
            ALICE.getEnrolledMonth().toHumanReadable());
        assertTrue(msg.equals(expected),
            "Should print the no-history message when start month is after end (now)");
    }

    @Test
    public void execute_studentNotFound_throws() {
        StudentId missing = new StudentId("0123");
        FeeViewCommand cmd = new FeeViewCommand(missing, Optional.empty());

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertTrue(ex.getMessage().equals(String.format(MESSAGE_STUDENT_ID_NOT_FOUND, missing)));
    }

    @Test
    public void equals_contract() {
        StudentId aliceId = ALICE.getStudentId();
        StudentId bensonId = BENSON.getStudentId();
        Month start = ALICE.getEnrolledMonth();

        FeeViewCommand a1 = new FeeViewCommand(aliceId, Optional.empty());
        FeeViewCommand a2 = new FeeViewCommand(aliceId, Optional.empty());
        FeeViewCommand a3 = new FeeViewCommand(aliceId, Optional.of(start));
        FeeViewCommand b1 = new FeeViewCommand(bensonId, Optional.empty());

        assertTrue(a1.equals(a1)); // same instance
        assertTrue(a1.equals(a2)); // same values
        assertFalse(a1.equals(a3)); // different optional start
        assertFalse(a1.equals(b1)); // different student
        assertFalse(a1.equals(null)); // null
        assertFalse(a1.equals(42)); // different type
    }
}
