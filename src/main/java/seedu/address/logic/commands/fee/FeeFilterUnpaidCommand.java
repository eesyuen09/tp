package seedu.address.logic.commands.fee;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.Model;
import seedu.address.model.time.Month;

/**
 * Shows only students marked as {@code UNPAID} for the specified month.
 */
public class FeeFilterUnpaidCommand extends FilterCommand {

    public static final String COMMAND_FLAG = "-up";

    public static final String MESSAGE_USAGE =
            COMMAND_WORD + " " + COMMAND_FLAG + " m/MMYY\n"
                    + "Example: filter -up m/0925"
                    + "Filters the list to show only students who is UNPAID for the specified month.";

    private final Month month;

    public FeeFilterUnpaidCommand(Month month) {
        this.month = requireNonNull(month);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(model.unpaidStudents(month));
        return new CommandResult(String.format("Showing UNPAID students for %s.", month.toHumanReadable()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof FeeFilterUnpaidCommand
                && month.equals(((FeeFilterUnpaidCommand) other).month));
    }
}

