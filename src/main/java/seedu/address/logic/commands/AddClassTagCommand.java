package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Create a class tag in Tuto
 */
public class AddClassTagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Creates a class tag in Tuto\n"
            + "Parameters: TAG_NAME (must be alphanumeric (letters, numbers) and "
            + "may contain underscores, length < 30)\n"
            + "Example: " + "tag -a t/TAG_NAME";

    public static final String MESSAGE_NOT_IMPLEMENTED_YET = "Add class tag command not implemented yet";

    @Override
    public CommandResult execute(Model model) throws CommandException{
        throw new CommandException(MESSAGE_NOT_IMPLEMENTED_YET);
    }
}
