package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.ClassTag;

public class DeleteClassTagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validTagUnused_success() {
        ClassTag tagToDelete = new ClassTag("UnusedTag");
        model.addClassTag(tagToDelete); // Add a tag that no student has

        DeleteClassTagCommand deleteCommand = new DeleteClassTagCommand(tagToDelete);
        String expectedMessage = String.format(DeleteClassTagCommand.MESSAGE_SUCCESS, tagToDelete.tagName);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteClassTag(tagToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_nonExistentTag_throwsCommandException() {
        ClassTag nonExistentTag = new ClassTag("NonExistent");
        DeleteClassTagCommand deleteCommand = new DeleteClassTagCommand(nonExistentTag);

        assertCommandFailure(deleteCommand, model, DeleteClassTagCommand.MESSAGE_TAG_NOT_FOUND);
    }

    @Test
    public void equals() {
        ClassTag tagA = new ClassTag("TagA");
        ClassTag tagB = new ClassTag("TagB");
        DeleteClassTagCommand deleteTagACommand = new DeleteClassTagCommand(tagA);
        DeleteClassTagCommand deleteTagBCommand = new DeleteClassTagCommand(tagB);

        // same object -> returns true
        assertTrue(deleteTagACommand.equals(deleteTagACommand));

        // same values -> returns true
        DeleteClassTagCommand deleteTagACommandCopy = new DeleteClassTagCommand(tagA);
        assertTrue(deleteTagACommand.equals(deleteTagACommandCopy));

        // different types -> returns false
        assertFalse(deleteTagACommand.equals(1));

        // null -> returns false
        assertFalse(deleteTagACommand.equals(null));

        // different class tag -> returns false
        assertFalse(deleteTagACommand.equals(deleteTagBCommand));
    }
}
