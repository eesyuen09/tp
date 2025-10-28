package seedu.address.logic.commands.fee;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.Model;
import seedu.address.model.time.Month;

/**
 * Shows only students marked as {@code PAID} for the specified month.
 */
public class FeeFilterPaidCommand extends FilterCommand {

    public static final String COMMAND_FLAG = "-p";

    public static final String MESSAGE_USAGE =
            COMMAND_WORD + " " + COMMAND_FLAG + " m/MMYY\n"
                    + "Example: filter -p m/0925"
                    + "Filters the list to show only students who have PAID for the specified month.";

    private final Month month;

    public FeeFilterPaidCommand(Month month) {
        this.month = requireNonNull(month);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(model.paidStudents(month));
        return new CommandResult(String.format("Showing PAID students for %s.", month.toHumanReadable()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof FeeFilterPaidCommand
                && month.equals(((FeeFilterPaidCommand) other).month));
    }
}

