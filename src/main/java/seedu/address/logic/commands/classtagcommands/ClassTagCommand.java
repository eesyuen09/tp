package seedu.address.logic.commands.classtagcommands;

import seedu.address.logic.commands.Command;

/**
 * Create a class tag in Tuto
 */
public abstract class ClassTagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Manages class tags. "
            + "Requires a flag to specify the action (e.g., -a for add, -d for delete).\n"
            + "Example: " + COMMAND_WORD + " -a t/Sec3_Maths";

}
