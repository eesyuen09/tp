package seedu.address.logic.commands;

/**
 * Filter student list by specific criteria.
 */
public abstract class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters students based on criteria.\n"
            + "Parameters: [-p | -up | -t] m/MMYY | t/TAG_NAME\n"
            + "Examples: \n"
            + "  " + COMMAND_WORD + " -p m/1025 (Filters by PAID status for Oct 2025)\n"
            + "  " + COMMAND_WORD + " -up m/1125 (Filters by UNPAID status for Nov 2025)\n"
            + "  " + COMMAND_WORD + " -t t/Sec3_Maths (Filters by class tag 'Sec3_Maths')";

}
