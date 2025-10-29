package seedu.address.logic.commands.classtag;

import seedu.address.logic.commands.Command;

/**
 * Create a class tag in Tuto
 */
public abstract class ClassTagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Manages class tags.\n"
            + "Requires a flag to specify the action.\n"
            + "Actions:\n"
            + "Add Class Tag: -a t/CLASSTAG\n"
            + "Delete Class Tag: -d t/CLASSTAG\n"
            + "List Class Tags: -l\n"
            + "Examples:\n"
            + COMMAND_WORD + " -a t/Sec3_Maths\n"
            + COMMAND_WORD + " -d t/Sec3_Maths\n"
            + COMMAND_WORD + " -l";

}
