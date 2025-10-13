package seedu.address.logic.commands.attendance;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Mark attendance of existing student in class
 */
public abstract class AttendanceCommand extends Command {

    public static final String COMMAND_WORD = "att";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Manages attendance for the students.\n "
            + "Requires a flag to specify the action.\n"
            + "Parameters:\n"
            + "  Mark: -m " + PREFIX_STUDENTID + "STUDENTID" + PREFIX_DATE + "DATE (format: DDMMYYYY)\n"
            + "  Unmark: -u " + PREFIX_STUDENTID + "STUDENTID" + PREFIX_DATE + "DATE (format: DDMMYYYY)\n"
            + "  View: -v " + PREFIX_STUDENTID + "STUDENTID\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " -m " + PREFIX_STUDENTID + "0123 " + PREFIX_DATE + "15092025\n"
            + "  " + COMMAND_WORD + " -u " + PREFIX_STUDENTID + "0123 " + PREFIX_DATE + "15092025\n"
            + "  " + COMMAND_WORD + " -v " + PREFIX_STUDENTID + "0123 ";

    public final StudentId studentId;

    public static final String MESSAGE_NOT_IMPLEMENTED_YET = "Attendance command not implemented yet";

    /**
     * Creates an AttendanceCommand with the specified student ID.
     */
    public AttendanceCommand(StudentId studentId) {
        this.studentId = studentId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException(MESSAGE_NOT_IMPLEMENTED_YET);
    }
}





