package seedu.address.logic.commands.classtagcommands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSTAG;

import java.util.List;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.ClassTag;

/**
 * Deletes a class tag from the address book.
 */
public class DeleteClassTagCommand extends ClassTagCommand {

    public static final String COMMAND_WORD = "-d";

    public static final String MESSAGE_USAGE = "Deletes an existing class tag from the system.\n"
            + "Parameters: " + PREFIX_CLASSTAG + "TAG_NAME\n"
            + "Example: tag " + COMMAND_WORD + " " + PREFIX_CLASSTAG + "Sec3_AMath";

    public static final String MESSAGE_SUCCESS = "Tag deleted: %1$s";
    public static final String MESSAGE_TAG_NOT_FOUND = "This class tag does not exist.";
    public static final String MESSAGE_TAG_IN_USE = "Cannot delete tag '%1$s' because it is still assigned to one "
            + "or more students. Please remove the tag from all students first.";

    private final ClassTag toDelete;

    /**
     * Creates a DeleteClassTagCommand to delete the specified {@code ClassTag}.
     */
    public DeleteClassTagCommand(ClassTag classTag) {
        requireNonNull(classTag);
        toDelete = classTag;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.hasClassTag(toDelete)) {
            throw new CommandException(MESSAGE_TAG_NOT_FOUND);
        }

        // Check if any person in the address book has this tag
        List<Person> personList = model.getAddressBook().getPersonList();
        boolean isTagInUse = personList.stream()
                .anyMatch(person -> person.getTags().contains(toDelete));

        if (isTagInUse) {
            throw new CommandException(String.format(MESSAGE_TAG_IN_USE, toDelete.tagName));
        }

        model.deleteClassTag(toDelete);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toDelete.tagName));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteClassTagCommand)) {
            return false;
        }

        DeleteClassTagCommand otherDelete = (DeleteClassTagCommand) other;
        return toDelete.equals(otherDelete.toDelete);
    }
}
