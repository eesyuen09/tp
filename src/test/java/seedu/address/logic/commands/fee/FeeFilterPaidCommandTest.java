package seedu.address.logic.commands.fee;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.time.Month;

/**
 * Contains integration and unit tests for {@code FeeFilterPaidCommand}.
 */
public class FeeFilterPaidCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        AddressBook ab = getTypicalAddressBook();
        model = new ModelManager(new AddressBook(ab), new UserPrefs());
        expectedModel = new ModelManager(new AddressBook(ab), new UserPrefs());
    }

    @Test
    public void execute_showPaidStudents_success() {
        Month month = new Month("0925");
        model.markPaid(ALICE.getStudentId(), month);
        expectedModel.markPaid(ALICE.getStudentId(), month);
        expectedModel.updateFilteredPersonList(expectedModel.paidStudents(month));

        FeeFilterPaidCommand command = new FeeFilterPaidCommand(month);
        String expectedMessage = String.format("Showing PAID students for %s.", month.toHumanReadable());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noPaidStudents_successShowsEmptyList() {
        Month month = new Month("1025");
        expectedModel.updateFilteredPersonList(expectedModel.paidStudents(month));

        FeeFilterPaidCommand command = new FeeFilterPaidCommand(month);
        String expectedMessage = String.format("Showing PAID students for %s.", month.toHumanReadable());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertTrue(expectedModel.getFilteredPersonList().isEmpty());
    }

    @Test
    public void equals() {
        Month sep = new Month("0925");
        Month oct = new Month("1025");

        FeeFilterPaidCommand command1 = new FeeFilterPaidCommand(sep);
        FeeFilterPaidCommand command2 = new FeeFilterPaidCommand(sep);
        FeeFilterPaidCommand command3 = new FeeFilterPaidCommand(oct);

        assertTrue(command1.equals(command1));
        assertTrue(command1.equals(command2));
        assertFalse(command1.equals(command3));
        assertFalse(command1.equals(null));
        assertFalse(command1.equals(5));
    }
}
