package seedu.address.logic.commands.classtag;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSTAG;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.tag.ClassTag;

/**
 * Adds a ClassTag to the address book.
 */
public class AddClassTagCommand extends ClassTagCommand {

    public static final String COMMAND_FLAG = "-a";

    public static final String MESSAGE_USAGE = "Adds a class tag to the system. "
            + "Parameters: " + PREFIX_CLASSTAG + "TAG_NAME\n"
            + "Example: " + COMMAND_WORD + " " + COMMAND_FLAG + " " + PREFIX_CLASSTAG + "Sec3_AMath";
    public static final String MESSAGE_SUCCESS = "New class tag added: %1$s";
    public static final String MESSAGE_DUPLICATE_TAG = "This class tag already exists.";

    private final ClassTag toAdd;

    /**
     * Creates an AddClassTagCommand to add the specified {@code ClassTag}
     */
    public AddClassTagCommand(ClassTag classTag) {
        requireNonNull(classTag);
        toAdd = classTag;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasClassTag(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_TAG);
        }

        model.addClassTag(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd.tagName));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddClassTagCommand)) {
            return false;
        }

        AddClassTagCommand otherAddClassTagCommand = (AddClassTagCommand) other;
        return toAdd.equals(otherAddClassTagCommand.toAdd);
    }
}
