package seedu.address.logic.commands.fee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import seedu.address.model.fee.FeeHistoryEntry;
import seedu.address.model.fee.FeeHistorySummary;
import seedu.address.model.fee.FeeState;
import seedu.address.model.person.StudentId;
import seedu.address.model.time.Month;

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
        String expectedMessage = String.format("Payment history for %s (%s) displayed.",
                ALICE.getName().fullName, aliceId);
        assertEquals(expectedMessage, result.getFeedbackToUser());

        javafx.collections.ObservableList<FeeHistoryEntry> entries = model.getDisplayedFeeHistory();
        assertFalse(entries.isEmpty());

        Month now = Month.now();
        assertEquals(now, entries.get(0).getMonth());
        assertEquals(enrolled, entries.get(entries.size() - 1).getMonth());
        for (int i = 1; i < entries.size(); i++) {
            assertTrue(entries.get(i - 1).getMonth().isAfter(entries.get(i).getMonth()));
        }

        FeeHistorySummary summary = model.feeHistorySummaryProperty().getValue();
        assertNotNull(summary);
        assertEquals(ALICE.getName().fullName, summary.getStudentName());
        assertEquals(aliceId, summary.getStudentId());
        assertEquals(enrolled, summary.getStartMonth());
        assertEquals(Month.now(), summary.getEndMonth());
        assertEquals(entries.size(), summary.getMonthCount());

        assertTrue(entries.stream().anyMatch(entry -> entry.getMonth().equals(paidMonth)
                && entry.getState() == FeeState.PAID && entry.isExplicit()));
        assertTrue(entries.stream().anyMatch(entry -> entry.getMonth().equals(explicitUnpaidMonth)
                && entry.getState() == FeeState.UNPAID && entry.isExplicit()));
    }

    @Test
    public void execute_startBeforeEnrollment_clampsToEnrollment() throws Exception {
        StudentId bensonId = BENSON.getStudentId();
        Month enrolled = BENSON.getEnrolledMonth();
        Month requestedStart = enrolled.plusMonths(-4); // earlier than enrollment

        FeeViewCommand cmd = new FeeViewCommand(bensonId, Optional.of(requestedStart));
        CommandResult result = cmd.execute(model);
        String expectedMessage = String.format("Payment history for %s (%s) displayed.",
                BENSON.getName().fullName, bensonId);
        assertEquals(expectedMessage, result.getFeedbackToUser());

        FeeHistorySummary summary = model.feeHistorySummaryProperty().getValue();
        assertNotNull(summary);
        assertEquals(enrolled, summary.getStartMonth());
    }

    @Test
    public void execute_startAfterNow_returnsNoHistoryMessage() throws Exception {
        StudentId aliceId = ALICE.getStudentId();
        Month futureStart = Month.now().plusMonths(2); // start > end(now)

        FeeViewCommand cmd = new FeeViewCommand(aliceId, Optional.of(futureStart));
        CommandResult result = cmd.execute(model);
        String msg = result.getFeedbackToUser();

        String expected = FeeViewCommand.MESSAGE_NO_HISTORY_IN_RANGE;
        assertTrue(msg.equals(expected),
            "Should print the no-history message when start month is after end (now)");

        assertTrue(model.getDisplayedFeeHistory().isEmpty());
        assertNull(model.feeHistorySummaryProperty().getValue());
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
