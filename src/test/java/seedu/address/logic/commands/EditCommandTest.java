package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_STUDENT_ID_NOT_FOUND;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASS_TAG_MATHS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASS_TAG_PHYSICS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.ClassTag;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        model.addClassTag(new ClassTag("Sec4_Maths"));
        Person editedPerson = new PersonBuilder().withTags("Sec4_Maths").build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        StudentId studentId = ALICE.getStudentId();
        EditCommand editCommand = new EditCommand(studentId, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getPersonById(studentId).get(), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {

        Person personToEdit = BENSON;
        StudentId studentId = personToEdit.getStudentId();

        // Expected person keeps original tags and adds new name/phone
        Person editedPerson = new PersonBuilder(personToEdit).withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withTags(VALID_CLASS_TAG_MATHS, VALID_CLASS_TAG_PHYSICS)
                .build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withTags(VALID_CLASS_TAG_PHYSICS, VALID_CLASS_TAG_MATHS)
                .build();

        EditCommand editCommand = new EditCommand(studentId, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editTagsWithDifferentCase_successUsesCorrectCase() {

        Person personToEdit = ALICE;
        StudentId studentId = personToEdit.getStudentId();

        String correctMathTagLowercase = VALID_CLASS_TAG_MATHS.toLowerCase();
        String correctPhysicsTagLowercase = VALID_CLASS_TAG_PHYSICS.toLowerCase();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withTags(correctMathTagLowercase, correctPhysicsTagLowercase)
                .build();

        EditCommand editCommand = new EditCommand(studentId, descriptor);


        Person editedPerson = new PersonBuilder(personToEdit)
                .withTags(VALID_CLASS_TAG_MATHS, VALID_CLASS_TAG_PHYSICS)
                .build();

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        Person personToEditInExpectedModel = expectedModel.getPersonById(studentId).get();
        expectedModel.setPerson(personToEditInExpectedModel, editedPerson);


        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);

        Person personAfterEdit = model.getPersonById(studentId).get();
        assertTrue(personAfterEdit.getTags().contains(new ClassTag(VALID_CLASS_TAG_MATHS)));
        assertTrue(personAfterEdit.getTags().contains(new ClassTag(VALID_CLASS_TAG_PHYSICS)));
    }

    @Test
    public void execute_clearTags_success() {

        Person personToEdit = BENSON;
        StudentId studentId = personToEdit.getStudentId();
        assertTrue(personToEdit.getTags().size() > 0); // Pre-condition check

        // Descriptor to clear tags (empty tag set via builder)
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withTags().build();
        EditCommand editCommand = new EditCommand(studentId, descriptor);

        // Expected person has no tags
        Person editedPerson = new PersonBuilder(personToEdit).withTags().build();

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);

        // Verify tags are actually empty in the model
        Person personAfterEdit = model.getPersonById(studentId).get();
        assertTrue(personAfterEdit.getTags().isEmpty());
    }


    //Perhaps a Buggy Test Case
    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        StudentId studentId = ALICE.getStudentId();
        EditCommand editCommand = new EditCommand(studentId, new EditPersonDescriptor());

        Person editedPerson = model.getPersonById(studentId).get();
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidStudentId_failure() {
        StudentId invalidId = new StudentId("45");
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(invalidId, descriptor);

        assertCommandFailure(editCommand, model, String.format(MESSAGE_STUDENT_ID_NOT_FOUND, invalidId));
    }


    @Test
    public void execute_filteredList_success() {
        Person personInFilteredList = model.getFilteredPersonList().get(0);
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(personInFilteredList.getStudentId(),
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        // edit person in filtered list into a duplicate in address book
        Person personToEdit = model.getAddressBook().getPersonList().get(0);
        Person personInList = model.getAddressBook().getPersonList().get(1);
        EditCommand editCommand = new EditCommand(personToEdit.getStudentId(),
                new EditPersonDescriptorBuilder(personInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        // Try editing ALICE to have the same details as BENSON
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(BENSON).build();
        EditCommand editCommand = new EditCommand(ALICE.getStudentId(), descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_editPersonWithNonExistentTag_throwsCommandException() {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(BENSON)
                .withTags("NonExistentTag").build();
        EditCommand editCommand = new EditCommand(BENSON.getStudentId(), descriptor);

        assertCommandFailure(editCommand, model,
                String.format(EditCommand.MESSAGE_TAG_NOT_FOUND, "NonExistentTag"));
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(ALICE.getStudentId(), DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(ALICE.getStudentId(), copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different type -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different studentId -> returns false
        assertFalse(standardCommand.equals(new EditCommand(BENSON.getStudentId(), DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(ALICE.getStudentId(), DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        StudentId studentId = ALICE.getStudentId();
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditCommand editCommand = new EditCommand(studentId, editPersonDescriptor);

        String expected = EditCommand.class.getCanonicalName()
                + "{studentId=" + studentId
                + ", editPersonDescriptor=" + editPersonDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

}


