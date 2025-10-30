package seedu.address.logic.commands.fee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_STUDENT_ID_NOT_FOUND;
import static seedu.address.testutil.FeeTestUtil.payAllMonthsBefore;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.fee.FeeHistoryEntry;
import seedu.address.model.fee.FeeHistorySummary;
import seedu.address.model.fee.FeeState;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.time.Month;

public class FeeViewCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    /** Makes a month explicitly UNPAID by paying then marking it unpaid. */
    private void makeExplicitUnpaid(Model model, Person person, Month month) {
        payAllMonthsBefore(model, person, month);
        model.markPaid(person.getStudentId(), month);
        model.markUnpaid(person.getStudentId(), month);
    }

    @Test
    public void execute_noStart_ordersNewestFirst() throws Exception {
        // ALICE with one explicit PAID (at enrolment) and one explicit UNPAID (a later month)
        StudentId aliceId = ALICE.getStudentId();
        Month enrolledMonth = ALICE.getEnrolledMonth();
        Month currentMonth = Month.now();

        Month explicitUnpaidMonth = enrolledMonth.plusMonths(2);
        while (explicitUnpaidMonth.isAfter(currentMonth) && !explicitUnpaidMonth.equals(enrolledMonth)) {
            explicitUnpaidMonth = explicitUnpaidMonth.plusMonths(-1);
        }

        // If we ended up at enrolment (rare: enrolment == now), don't create an explicit UNPAID.
        // Instead, just make enrolment explicitly PAID so the “explicit PAID at enrolment” assertion holds.
        if (explicitUnpaidMonth.equals(enrolledMonth)) {
            model.markPaid(aliceId, enrolledMonth);
        } else {
            makeExplicitUnpaid(model, ALICE, explicitUnpaidMonth); // also makes enrolment explicitly PAID
        }

        FeeViewCommand viewCommand = new FeeViewCommand(aliceId, Optional.empty());
        CommandResult result = viewCommand.execute(model);

        String expectedHeader = String.format(
            "Payment history for %s (Student ID %s) displayed.",
            ALICE.getName().fullName, aliceId);
        assertEquals(expectedHeader, result.getFeedbackToUser());

        ObservableList<FeeHistoryEntry> entries = model.getDisplayedFeeHistory();
        assertFalse(entries.isEmpty(), "Entries should not be empty");

        // Newest -> oldest, spanning now .. enrolment
        assertEquals(currentMonth, entries.get(0).getMonth());
        assertEquals(enrolledMonth, entries.get(entries.size() - 1).getMonth());
        for (int i = 1; i < entries.size(); i++) {
            assertTrue(entries.get(i - 1).getMonth().isAfter(entries.get(i).getMonth()),
                "List must be strictly newest-first");
        }

        FeeHistorySummary summary = model.feeHistorySummaryProperty().getValue();
        assertNotNull(summary, "Summary should be populated");
        assertEquals(ALICE.getName().fullName, summary.getStudentName());
        assertEquals(aliceId, summary.getStudentId());
        assertEquals(enrolledMonth, summary.getStartMonth());
        assertEquals(currentMonth, summary.getEndMonth());
        assertEquals(entries.size(), summary.getMonthCount());

        // Explicit flags: enrolment must be explicitly PAID
        assertTrue(entries.stream().anyMatch(e ->
                e.getMonth().equals(enrolledMonth)
                    && e.getState() == FeeState.PAID
                    && e.isExplicit()),
            "Enrolment month should contain an explicit PAID entry");

        // If we created an explicit UNPAID month, it should appear explicitly UNPAID.
        if (!explicitUnpaidMonth.equals(enrolledMonth)) {
            Month finalExplicitUnpaidMonth = explicitUnpaidMonth;
            assertTrue(entries.stream().anyMatch(e ->
                    e.getMonth().equals(finalExplicitUnpaidMonth)
                        && e.getState() == FeeState.UNPAID
                        && e.isExplicit()),
                "There should be an explicit UNPAID entry for the chosen month");
        }
    }

    @Test
    public void execute_startBeforeEnrollment_clampsToEnrollmentInSummary() throws Exception {
        StudentId bensonId = BENSON.getStudentId();
        Month enrolled = BENSON.getEnrolledMonth();
        Month requestedStart = enrolled.plusMonths(-6); // clearly before enrolment

        FeeViewCommand cmd = new FeeViewCommand(bensonId, Optional.of(requestedStart));

        CommandResult res = cmd.execute(model);
        String expectedHeader = String.format(
            "Payment history for %s (Student ID %s) displayed.",
            BENSON.getName().fullName, bensonId);
        assertEquals(expectedHeader, res.getFeedbackToUser());

        FeeHistorySummary summary = model.feeHistorySummaryProperty().getValue();
        assertNotNull(summary);
        assertEquals(enrolled, summary.getStartMonth(), "Start month should clamp to enrolment");
    }

    @Test
    public void execute_startAfterNow_throwsCommandException() {
        StudentId aliceId = ALICE.getStudentId();
        Month futureStart = Month.now().plusMonths(2);

        FeeViewCommand cmd = new FeeViewCommand(aliceId, Optional.of(futureStart));

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals(FeeViewCommand.MESSAGE_NO_HISTORY_IN_RANGE, ex.getMessage());
        // Model should not have been updated
        assertTrue(model.getDisplayedFeeHistory().isEmpty());
        assertEquals(null, model.feeHistorySummaryProperty().getValue());
    }

    @Test
    public void execute_studentNotFound_throws() {
        StudentId missing = new StudentId("9999"); // not in TypicalPersons
        FeeViewCommand cmd = new FeeViewCommand(missing, Optional.empty());

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals(String.format(MESSAGE_STUDENT_ID_NOT_FOUND, missing), ex.getMessage());
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

        assertTrue(a1.equals(a1)); // reflexive
        assertTrue(a1.equals(a2)); // same values
        assertFalse(a1.equals(a3)); // different optional start
        assertFalse(a1.equals(b1)); // different student
        assertFalse(a1.equals(null)); // null
        assertFalse(a1.equals("x")); // different type
    }
}


