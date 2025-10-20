package seedu.address.logic.commands.classtagcommands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.ClassTag;
import seedu.address.testutil.TypicalClassTags;

public class ClassTagFilterCommandTest {
    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validClassTagExists_personsFound() {

        ClassTag tagToFilter = TypicalClassTags.SEC3_MATHS;
        String expectedMessage = String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, 2) + "\n"
                + String.format(ClassTagFilterCommand.MESSAGE_SUCCESS, tagToFilter.tagName);

        ClassTagFilterCommand command = new ClassTagFilterCommand(tagToFilter);

        Predicate<Person> personHasTagPredicate = person -> person.getTags().stream()
                .anyMatch(tag -> tag.equals(tagToFilter));

        expectedModel.updateFilteredPersonList(personHasTagPredicate);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON), model.getFilteredPersonList());
    }

    @Test
    public void execute_validClassTagNoPersons_noPersonsFound() {
        // A tag that exists but is not used by any typical person.
        ClassTag lonelyTag = new ClassTag("LonelyTag");
        model.addClassTag(lonelyTag);
        expectedModel.addClassTag(lonelyTag);

        String expectedMessage = String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, 0) + "\n"
                + String.format(ClassTagFilterCommand.MESSAGE_SUCCESS, lonelyTag.tagName);
        ClassTagFilterCommand command = new ClassTagFilterCommand(lonelyTag);
        expectedModel.updateFilteredPersonList(p -> p.getTags().contains(lonelyTag));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_classTagDoesNotExist_throwsCommandException() {
        ClassTag nonExistentTag = new ClassTag("NonExistentTag");
        ClassTagFilterCommand command = new ClassTagFilterCommand(nonExistentTag);
        String expectedMessage = String.format(ClassTagFilterCommand.MESSAGE_TAG_NOT_FOUND, nonExistentTag.tagName);
        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void equals() {
        ClassTag sec3Tag = TypicalClassTags.SEC3_MATHS;
        ClassTag physicsTag = TypicalClassTags.JC1_PHYSICS;

        ClassTagFilterCommand filterSec3Command = new ClassTagFilterCommand(sec3Tag);
        ClassTagFilterCommand filterPhysicsCommand = new ClassTagFilterCommand(physicsTag);

        // same object -> returns true
        assertTrue(filterSec3Command.equals(filterSec3Command));

        // same values -> returns true
        ClassTagFilterCommand filterSec3CommandCopy = new ClassTagFilterCommand(sec3Tag);
        assertTrue(filterSec3Command.equals(filterSec3CommandCopy));

        // different types -> returns false
        assertFalse(filterSec3Command.equals(1));

        // null -> returns false
        assertFalse(filterSec3Command.equals(null));

        // different tag -> returns false
        assertFalse(filterSec3Command.equals(filterPhysicsCommand));
    }

}

