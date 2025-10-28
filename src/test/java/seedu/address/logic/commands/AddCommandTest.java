package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.tag.ClassTag;
import seedu.address.testutil.ModelStub;
import seedu.address.testutil.PersonBuilder;

public class AddCommandTest {

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPerson = new PersonBuilder().build();

        CommandResult commandResult = new AddCommand(validPerson).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPerson)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person validPerson = new PersonBuilder().build();
        AddCommand addCommand = new AddCommand(validPerson);
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_addPersonWithExactCaseTag_addsWithCorrectCaseTag() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        ClassTag existingTag = new ClassTag("ExactCaseTag");
        modelStub.addClassTag(existingTag);


        Person personWithExactCaseTag = new PersonBuilder().withName("Exact Case Test")
                .withClassTags("ExactCaseTag")
                .build();

        AddCommand addCommand = new AddCommand(personWithExactCaseTag);
        addCommand.execute(modelStub);

        assertTrue(modelStub.personsAdded.size() == 1);
        Person addedPerson = modelStub.personsAdded.get(0);

        // Create the expected tag set with the original casing
        Set<ClassTag> expectedTags = new HashSet<>();
        expectedTags.add(existingTag);

        assertEquals(expectedTags, addedPerson.getTags());
    }

    @Test
    public void execute_addPersonWithDifferentCaseTag_addsWithCorrectCaseTag() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        ClassTag existingTag = new ClassTag("ExistingTag");
        modelStub.addClassTag(existingTag);

        // Build person with the tag in lowercase
        Person personWithLowercaseTag = new PersonBuilder().withName("Case Test")
                .withClassTags("existingtag") // Input with lowercase
                .build();

        // Create the AddCommand
        AddCommand addCommand = new AddCommand(personWithLowercaseTag);
        addCommand.execute(modelStub);

        // Verify the person added has the tag with the ORIGINAL casing
        assertTrue(modelStub.personsAdded.size() == 1);
        Person addedPerson = modelStub.personsAdded.get(0);

        // Create the expected tag set with the original casing
        Set<ClassTag> expectedTags = new HashSet<>();
        expectedTags.add(existingTag); // Expect "ExistingTag"

        assertEquals(expectedTags, addedPerson.getTags());
    }

    @Test
    public void execute_addPersonWithNonExistentTag_throwsCommandException() {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded(); // Use a stub that allows adding
        Person personWithBadTag = new PersonBuilder()
                .withName("Bad Tag Person")
                .withClassTags("NonExistentTag")
                .build();
        AddCommand addCommand = new AddCommand(personWithBadTag);

        assertThrows(CommandException.class,
                String.format(AddCommand.MESSAGE_TAG_NOT_FOUND, "NonExistentTag"), () -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(ALICE);
        String expected = AddCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addCommand.toString());
    }


    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();
        final ArrayList<ClassTag> classTagsAvailable = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public void addClassTag(ClassTag tag) {
            requireNonNull(tag);
            if (!classTagsAvailable.contains(tag)) {
                classTagsAvailable.add(tag);
            }
        }

        @Override
        public boolean hasClassTag(ClassTag tag) {
            requireNonNull(tag);
            return classTagsAvailable.stream().anyMatch(existing -> existing.equals(tag));
        }

        @Override
        public Optional<ClassTag> findClassTag(ClassTag userTag) {
            requireNonNull(userTag);
            return classTagsAvailable.stream()
                    .filter(existingTag -> existingTag.equals(userTag))
                    .findFirst();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
