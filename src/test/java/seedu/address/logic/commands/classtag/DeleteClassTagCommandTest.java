package seedu.address.logic.commands.classtag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.tag.ClassTag;
import seedu.address.testutil.ModelStub;
import seedu.address.testutil.PersonBuilder;

public class DeleteClassTagCommandTest {

    private final ClassTag tagToDelete = new ClassTag("Sec3_Maths");

    @Test
    public void constructor_nullClassTag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DeleteClassTagCommand(null));
    }

    @Test
    public void execute_tagExistsAndUnused_deleteSuccessful() throws Exception {
        ModelStubWithClassTag modelStub = new ModelStubWithClassTag(tagToDelete);

        CommandResult commandResult = new DeleteClassTagCommand(tagToDelete).execute(modelStub);

        assertEquals(String.format(DeleteClassTagCommand.MESSAGE_SUCCESS, tagToDelete.tagName),
                commandResult.getFeedbackToUser());
        assertFalse(modelStub.hasClassTag(tagToDelete));
    }

    @Test
    public void execute_tagDoesNotExist_throwsCommandException() {
        ModelStub modelStub = new ModelStubAcceptingClassTagAdded(); // An empty stub
        DeleteClassTagCommand deleteClassTagCommand = new DeleteClassTagCommand(tagToDelete);

        assertThrows(CommandException.class, () -> deleteClassTagCommand.execute(modelStub));
    }

    @Test
    public void execute_tagExistsDifferentCase_deleteSuccessful() throws Exception {
        ModelStubWithClassTag modelStub = new ModelStubWithClassTag(tagToDelete); // "Sec3_Maths"
        ClassTag tagToDeleteLower = new ClassTag("sec3_maths");

        CommandResult commandResult = new DeleteClassTagCommand(tagToDeleteLower).execute(modelStub);

        // Check that the original tag is gone
        assertFalse(modelStub.hasClassTag(tagToDelete));
        // Check that the success message uses the *original* casing from the tag that was found and deleted
        assertEquals(String.format(DeleteClassTagCommand.MESSAGE_SUCCESS, tagToDelete.tagName),
                commandResult.getFeedbackToUser());
    }

    // Test deleting a tag that is still in use (Option A)
    @Test
    public void execute_tagInUse_throwsCommandException() {
        // Create a person using the tag
        Person personWithTag = new PersonBuilder(ALICE).withClassTags("Sec3_Maths").build();
        ModelStubWithPersonAndTag modelStub = new ModelStubWithPersonAndTag(personWithTag, tagToDelete);

        DeleteClassTagCommand deleteClassTagCommand = new DeleteClassTagCommand(tagToDelete);

        assertThrows(CommandException.class, () ->
                deleteClassTagCommand.execute(modelStub));
    }

    // Test deleting a tag in use (different case) (Option A)
    @Test
    public void execute_tagInUseDifferentCase_throwsCommandException() {
        // Person has "Sec3_Maths"
        Person personWithTag = new PersonBuilder(ALICE).withClassTags("Sec3_Maths").build();
        ModelStubWithPersonAndTag modelStub = new ModelStubWithPersonAndTag(personWithTag, tagToDelete);

        // Command tries to delete "sec3_maths"
        ClassTag tagToDeleteLower = new ClassTag("sec3_maths");
        DeleteClassTagCommand deleteClassTagCommand = new DeleteClassTagCommand(tagToDeleteLower);

        assertThrows(CommandException.class, () ->
                deleteClassTagCommand.execute(modelStub));
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

    /**
     * A Model stub that always accepts the class tag being added (for empty state).
     */
    private class ModelStubAcceptingClassTagAdded extends ModelStub {
        final ArrayList<ClassTag> classTagsAdded = new ArrayList<>();
        @Override
        public boolean hasClassTag(ClassTag classTag) {
            return classTagsAdded.stream().anyMatch(existingTag -> existingTag.equals(classTag));
        }
        @Override
        public void addClassTag(ClassTag classTag) {
            classTagsAdded.add(classTag);
        }
        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
        @Override
        public Optional<ClassTag> findClassTag(ClassTag classTag) {
            return classTagsAdded.stream()
                    .filter(existingTag -> existingTag.equals(classTag))
                    .findFirst();
        }
    }

    /**
     * A Model stub that contains a single class tag.
     */
    private class ModelStubWithClassTag extends ModelStub {
        private final List<ClassTag> classTags;
        ModelStubWithClassTag(ClassTag classTag) {
            this.classTags = new ArrayList<>(Arrays.asList(classTag));
        }
        @Override
        public boolean hasClassTag(ClassTag classTag) {
            return classTags.stream().anyMatch(existingTag -> existingTag.equals(classTag));
        }
        @Override
        public Optional<ClassTag> findClassTag(ClassTag classTag) {
            return classTags.stream()
                    .filter(existingTag -> existingTag.equals(classTag))
                    .findFirst();
        }
        @Override
        public void deleteClassTag(ClassTag toDelete) {
            classTags.removeIf(tag -> tag.equals(toDelete));
        }
        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook(); // Return empty book for simplicity
        }
        @Override
        public ObservableList<Person> getFilteredPersonList() {
            // Return an empty list, as no person has this tag
            return FXCollections.observableArrayList();
        }
    }

    /**
     * A Model stub that contains a single person and a single tag.
     */
    private class ModelStubWithPersonAndTag extends ModelStub {
        private final Person person;
        private final List<ClassTag> classTags;
        ModelStubWithPersonAndTag(Person person, ClassTag classTag) {
            this.person = person;
            this.classTags = new ArrayList<>(Arrays.asList(classTag));
        }
        @Override
        public boolean hasClassTag(ClassTag classTag) {
            return classTags.stream().anyMatch(existingTag -> existingTag.equals(classTag));
        }
        @Override
        public Optional<ClassTag> findClassTag(ClassTag classTag) {
            return classTags.stream()
                    .filter(existingTag -> existingTag.equals(classTag))
                    .findFirst();
        }
        @Override
        public void deleteClassTag(ClassTag toDelete) {
            classTags.removeIf(tag -> tag.equals(toDelete));
        }
        @Override
        public ReadOnlyAddressBook getAddressBook() {
            AddressBook ab = new AddressBook();
            ab.addPerson(person);
            return ab;
        }
        @Override
        public ObservableList<Person> getFilteredPersonList() {
            // Simulates the person list
            return FXCollections.observableArrayList(person);
        }
    }
}

