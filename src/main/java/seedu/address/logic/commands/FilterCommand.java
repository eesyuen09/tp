package seedu.address.logic.commands;

/**
 * Filter student list by specific criteria.
 */
public abstract class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":  Filters the student list by specific criteria.\n"
            + "Flags available:\n"
            + "  -p  : Show students marked as PAID for a given month.\n"
            + "  -up : Show students marked (or inferred) as UNPAID for a given month.\n"
            + "Example: " + COMMAND_WORD + "-p m/0925 \n";

}