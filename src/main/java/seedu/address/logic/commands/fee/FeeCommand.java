package seedu.address.logic.commands.fee;

import seedu.address.logic.commands.Command;

/**
 * Represents an abstract base class for all fee-related commands.
 */
public abstract class FeeCommand extends Command {
    public static final String COMMAND_WORD = "fee";

    public static final String MESSAGE_USAGE =
            COMMAND_WORD + ": Manages student fee status.\n"
                    + "Mark paid: " + COMMAND_WORD + " -p s/STUDENT_ID m/MMYY\n"
                    + "Mark unpaid: " + COMMAND_WORD + " -up s/STUDENT_ID m/MMYY\n"
                    + "View history: " + COMMAND_WORD + " -v s/STUDENT_ID\n"
                    + "Examples:\n"
                    + "  fee -p s/0123 m/0925\n"
                    + "  fee -up s/1234 m/1025\n"
                    + "  fee -v s/0123";


}