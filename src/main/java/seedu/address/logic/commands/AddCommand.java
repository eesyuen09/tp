package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.ExceedMaxStudentsException;
import seedu.address.model.tag.ClassTag;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a student to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_CLASSTAG + "CLASS_TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_CLASSTAG + "Math_Sec3 ";

    public static final String MESSAGE_SUCCESS = "New student added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This student already exists in the address book";
    public static final String MESSAGE_TAG_NOT_FOUND = "One or more class tags do not exist: %s. "
            + "Please create them first with the 'tag -a' command.";
    public static final String MESSAGE_MAX_STUDENTS_EXCEEDED =
            "Cannot add more students as the maximum limit of 9999 has been reached.";

    private final Person toAdd;

    /**
     * Constructs an {@code AddCommand} that adds the specified {@link Person} to the address book.
     *
     * @param person The person to be added. Must not be {@code null}.
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        Person finalPersonToAdd;
        try {
            finalPersonToAdd = createPersonWithCorrectlyCasedTags(model);
        } catch (CommandException e) {
            throw e;
        }

        try {
            model.addPerson(finalPersonToAdd.withStudentId());
        } catch (ExceedMaxStudentsException e) {
            throw new CommandException(MESSAGE_MAX_STUDENTS_EXCEEDED);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    private Person createPersonWithCorrectlyCasedTags(Model model) throws CommandException {
        Set<ClassTag> correctlyCasedTags = new HashSet<>();

        for (ClassTag tag : toAdd.getTags()) {
            Optional<ClassTag> foundTag = model.findClassTag(tag);

            if (foundTag.isPresent()) {
                correctlyCasedTags.add(foundTag.get());
            } else {
                throw new CommandException(String.format(MESSAGE_TAG_NOT_FOUND, tag.tagName));
            }
        }

        return new Person(
                toAdd.getName(),
                toAdd.getPhone(),
                toAdd.getEmail(),
                toAdd.getAddress(),
                correctlyCasedTags,
                toAdd.getEnrolledMonth(),
                toAdd.getAttendanceList(),
                toAdd.getPerformanceList()
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
