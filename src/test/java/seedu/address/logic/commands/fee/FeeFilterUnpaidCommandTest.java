package seedu.address.logic.commands.fee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.FeeTestUtil.payAllMonthsBefore;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandTestUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.time.Month;

/**
 * Integration tests for {@link FeeFilterUnpaidCommand}.
 */
public class FeeFilterUnpaidCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new AddressBook(), new UserPrefs());
        expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(ALICE);
        model.addPerson(BENSON);
        expectedModel.addPerson(ALICE);
        expectedModel.addPerson(BENSON);
    }

    @Test
    public void execute_filtersOnlyUnpaidForGivenMonth_success() {
        Month month = Month.now();

        payAllMonthsBefore(model, BENSON, month);
        payAllMonthsBefore(expectedModel, BENSON, month);

        model.markPaid(BENSON.getStudentId(), month);
        expectedModel.markPaid(BENSON.getStudentId(), month);

        FeeFilterUnpaidCommand cmd = new FeeFilterUnpaidCommand(month);

        expectedModel.updateFilteredPersonList(expectedModel.unpaidStudents(month));

        String expectedMessage = String.format(
            "Showing UNPAID students for %s.\n%s",
            month.toHumanReadable(),
            String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, expectedModel.getFilteredPersonList().size()));

        assertCommandSuccess(cmd, model, expectedMessage, expectedModel);
        assertEquals(1, model.getFilteredPersonList().size());
        assertTrue(model.getFilteredPersonList().contains(ALICE));
        assertFalse(model.getFilteredPersonList().contains(BENSON));
    }

    @Test
    public void execute_noUnpaidForGivenMonth_showsEmptyList() {
        Month month = Month.now();

        payAllMonthsBefore(model, ALICE, month);
        payAllMonthsBefore(model, BENSON, month);
        model.markPaid(ALICE.getStudentId(), month);
        model.markPaid(BENSON.getStudentId(), month);

        payAllMonthsBefore(expectedModel, ALICE, month);
        payAllMonthsBefore(expectedModel, BENSON, month);
        expectedModel.markPaid(ALICE.getStudentId(), month);
        expectedModel.markPaid(BENSON.getStudentId(), month);

        FeeFilterUnpaidCommand cmd = new FeeFilterUnpaidCommand(month);

        expectedModel.updateFilteredPersonList(expectedModel.unpaidStudents(month));
        String expectedMessage = String.format(
            "Showing UNPAID students for %s.\n%s",
            month.toHumanReadable(),
            String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, expectedModel.getFilteredPersonList().size()));

        assertCommandSuccess(cmd, model, expectedMessage, expectedModel);

        assertEquals(0, model.getFilteredPersonList().size());
    }

    @Test
    public void execute_futureMonth_throwsCommandException() {
        Month future = Month.now().plusMonths(1);
        FeeFilterUnpaidCommand command = new FeeFilterUnpaidCommand(future);

        String expectedMessage = "Cannot filter by future months. "
            + "\nPlease select a month up to the current month.";

        CommandTestUtil.assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void equals() {
        Month m1 = Month.now();
        Month m2 = m1.plusMonths(1);

        FeeFilterUnpaidCommand c1 = new FeeFilterUnpaidCommand(m1);
        FeeFilterUnpaidCommand c1Copy = new FeeFilterUnpaidCommand(m1);
        FeeFilterUnpaidCommand c2 = new FeeFilterUnpaidCommand(m2);

        assertTrue(c1.equals(c1));
        assertTrue(c1.equals(c1Copy));
        assertFalse(c1.equals(null));
        assertFalse(c1.equals("not a command"));
        assertFalse(c1.equals(c2));
    }
}
