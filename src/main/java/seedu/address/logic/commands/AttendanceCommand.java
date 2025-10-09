package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Mark attendance of existing student in class
 */
public class AttendanceCommand extends Command {

    public static final String COMMAND_WORD = "att";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Manages attendance for the student identified by the index number "
            + "used in the displayed person list.\n"
            + "Parameters:\n"
            + "  Mark: -m INDEX " + PREFIX_DATE + "DATE (format: DDMMYYYY)\n"
            + "  Unmark: -u INDEX " + PREFIX_DATE + "DATE (format: DDMMYYYY)\n"
            + "  View: -v INDEX\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " -m 1 " + PREFIX_DATE + "15092025\n"
            + "  " + COMMAND_WORD + " -u 1 " + PREFIX_DATE + "15092025\n"
            + "  " + COMMAND_WORD + " -v 1";

    public static final String MESSAGE_MARK_SUCCESS = "Marked attendance for: %1$s on %2$s";
    public static final String MESSAGE_UNMARK_SUCCESS = "Unmarked attendance for: %1$s on %2$s";
    public static final String MESSAGE_VIEW_SUCCESS = "Showing attendance history for: %1$s";

    /**
     * Represents the type of attendance operation to perform.
     */
    public enum Operation {
        MARK, UNMARK, VIEW
    }

    public static final String MESSAGE_NOT_IMPLEMENTED_YET = "Attendance command not implemented yet";

    /**
     * Creates an AttendanceCommand with the specified operation, index, and date.
     */
    public AttendanceCommand() {
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException(MESSAGE_NOT_IMPLEMENTED_YET);
    }
}





