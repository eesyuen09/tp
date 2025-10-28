package seedu.address.logic.commands.fee;

import seedu.address.logic.commands.Command;

/**
 * Represents an abstract base class for all fee-related commands.
 */
public abstract class FeeCommand extends Command {
    public static final String COMMAND_WORD = "fee";
    public static final String MESSAGE_INVALID_MONTH = "Cannot mark payment: %1$s's enrolment started in %2$s."
        + "\nEarlier months cannot be marked.";

    public static final String MESSAGE_USAGE =
        COMMAND_WORD + ": Manages payment status for students.\n"
            + "Requires a flag to specify the action.\n"
            + "Actions:\n"
            + "Mark Paid: -p s/STUDENT_ID m/MMYY\n"
            + "Mark Unpaid: -up s/STUDENT_ID m/MMYY\n"
            + "View Payment History: -v s/STUDENT_ID [m/MMYY]\n"
            + "  - If m/MMYY is provided, shows history from that month up to the current month.\n"
            + "  - If omitted, shows history from the student's enrolled month up to the current month.\n"
            + "Examples:\n"
            + "  fee -p s/0123 m/0925\n"
            + "  fee -up s/1234 m/1025\n"
            + "  fee -v s/0123\n"
            + "  fee -v s/0123 m/0825";
}
