package seedu.address.logic.commands.classtagcommands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.tag.ClassTag;
import seedu.address.testutil.ModelStub;

public class AddClassTagCommandTest {

    @Test
    public void constructor_nullClassTag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddClassTagCommand(null));
    }

    @Test
    public void execute_classTagAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingClassTagAdded modelStub = new ModelStubAcceptingClassTagAdded();
        ClassTag validClassTag = new ClassTag("Sec4_Maths");

        CommandResult commandResult = new AddClassTagCommand(validClassTag).execute(modelStub);

        assertEquals(String.format(AddClassTagCommand.MESSAGE_SUCCESS, validClassTag.tagName),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validClassTag), modelStub.classTagsAdded);
    }

    @Test
    public void execute_duplicateClassTag_throwsCommandException() {
        ClassTag validClassTag = new ClassTag("Sec4_Maths");
        AddClassTagCommand addClassTagCommand = new AddClassTagCommand(validClassTag);
        ModelStub modelStub = new ModelStubWithClassTag(validClassTag);

        assertThrows(CommandException.class, AddClassTagCommand.MESSAGE_DUPLICATE_TAG, () ->
                addClassTagCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        ClassTag tagA = new ClassTag("TagA");
        ClassTag tagB = new ClassTag("TagB");
        AddClassTagCommand addTagACommand = new AddClassTagCommand(tagA);
        AddClassTagCommand addTagBCommand = new AddClassTagCommand(tagB);

        // same object -> returns true
        assertTrue(addTagACommand.equals(addTagACommand));

        // same values -> returns true
        AddClassTagCommand addTagACommandCopy = new AddClassTagCommand(tagA);
        assertTrue(addTagACommand.equals(addTagACommandCopy));

        // different types -> returns false
        assertFalse(addTagACommand.equals(1));

        // null -> returns false
        assertFalse(addTagACommand.equals(null));

        // different class tag -> returns false
        assertFalse(addTagACommand.equals(addTagBCommand));
    }

    /**
     * A Model stub that contains a single class tag.
     */
    private class ModelStubWithClassTag extends ModelStub {
        private final ClassTag classTag;

        ModelStubWithClassTag(ClassTag classTag) {
            this.classTag = classTag;
        }

        @Override
        public boolean hasClassTag(ClassTag classTag) {
            return this.classTag.equals(classTag);
        }
    }

    /**
     * A Model stub that always accepts the class tag being added.
     */
    private class ModelStubAcceptingClassTagAdded extends ModelStub {
        final ArrayList<ClassTag> classTagsAdded = new ArrayList<>();

        @Override
        public boolean hasClassTag(ClassTag classTag) {
            return classTagsAdded.stream().anyMatch(classTag::equals);
        }

        @Override
        public void addClassTag(ClassTag classTag) {
            classTagsAdded.add(classTag);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
