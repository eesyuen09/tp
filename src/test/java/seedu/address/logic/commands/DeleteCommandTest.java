package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(0);
        StudentId targetId = personToDelete.getStudentId();
        DeleteCommand deleteCommand = new DeleteCommand(targetId);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidStudentId_throwsCommandException() {
        StudentId invalidId = new StudentId("8800"); // assume this ID does not exist
        DeleteCommand deleteCommand = new DeleteCommand(invalidId);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_STUDENT_ID_NOT_FOUND);
    }

    @Test
    public void equals() {
        StudentId firstId = model.getFilteredPersonList().get(0).getStudentId();
        StudentId secondId = model.getFilteredPersonList().get(1).getStudentId();

        DeleteCommand deleteFirstCommand = new DeleteCommand(firstId);
        DeleteCommand deleteSecondCommand = new DeleteCommand(secondId);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(firstId);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }


    @Test
    public void toStringMethod() {
        StudentId targetId = model.getFilteredPersonList().get(0).getStudentId();
        DeleteCommand deleteCommand = new DeleteCommand(targetId);
        String expected = DeleteCommand.class.getCanonicalName() + "{targetId=" + targetId + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
