package seedu.address.logic.commands.classtag;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSTAG;

import java.util.List;
import java.util.Optional;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.ClassTag;

/**
 * Deletes a class tag from the address book.
 */
public class DeleteClassTagCommand extends ClassTagCommand {

    public static final String COMMAND_FLAG = "-d";

    public static final String MESSAGE_USAGE = "Deletes an existing class tag from the system.\n"
            + "Parameters: " + PREFIX_CLASSTAG + "TAG_NAME\n"
            + "Example: " + COMMAND_WORD + " " + COMMAND_FLAG + " " + PREFIX_CLASSTAG + "Sec3_AMath";

    public static final String MESSAGE_SUCCESS = "Tag deleted: %1$s";
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

        Optional<ClassTag> foundTag = model.findClassTag(toDelete);

        if (foundTag.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_TAG_NOT_FOUND);
        }

        ClassTag actualTag = foundTag.get();

        // Check if any person in the address book has this tag
        List<Person> personList = model.getAddressBook().getPersonList();
        boolean isTagInUse = personList.stream()
                .anyMatch(person -> person.getTags().contains(actualTag));

        if (isTagInUse) {
            throw new CommandException(String.format(MESSAGE_TAG_IN_USE, actualTag.tagName));
        }

        model.deleteClassTag(toDelete);
        return new CommandResult(String.format(MESSAGE_SUCCESS, actualTag.tagName));
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
