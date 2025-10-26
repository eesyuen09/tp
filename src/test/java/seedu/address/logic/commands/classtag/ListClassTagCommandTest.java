package seedu.address.logic.commands.classtag;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.tag.ClassTag;

/**
 * Contains integration tests (interaction with the Model) for ListClassTagCommand.
 */
public class ListClassTagCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        expectedModel = new ModelManager();
    }

    @Test
    public void execute_noTagsInAddressBook_showsNoTagsMessage() {
        assertCommandSuccess(new ListClassTagCommand(),
                model, ListClassTagCommand.MESSAGE_NO_TAGS_FOUND, expectedModel);
    }

    @Test
    public void execute_tagsExistInAddressBook_showsTagList() {

        ClassTag tag1 = new ClassTag("Sec3_Maths");
        ClassTag tag2 = new ClassTag("JC1_Physics");
        model.addClassTag(tag1);
        model.addClassTag(tag2);
        expectedModel.addClassTag(tag1);
        expectedModel.addClassTag(tag2);

        String expectedMessage = String.format(ListClassTagCommand.MESSAGE_SUCCESS, "1. Sec3_Maths\n2. JC1_Physics");

        assertCommandSuccess(new ListClassTagCommand(), model, expectedMessage, expectedModel);
    }
}
