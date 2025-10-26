package seedu.address.logic.commands.classtagcommands;

import seedu.address.logic.commands.Command;

/**
 * Create a class tag in Tuto
 */
public abstract class ClassTagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Manages class tags.\n"
            + "Flags:\n"
            + "  -a : Add a new class tag\n"
            + "  -d : Delete an existing class tag\n"
            + "  -l : List all class tags\n"
            + "Parameters for -a: t/CLASSTAG\n"
            + "Parameters for -d: t/CLASSTAG\n"
            + "Parameters for -l: (none)\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " -a t/Sec3_Maths\n"
            + "  " + COMMAND_WORD + " -d t/Sec3_Maths\n"
            + "  " + COMMAND_WORD + " -l";

}
