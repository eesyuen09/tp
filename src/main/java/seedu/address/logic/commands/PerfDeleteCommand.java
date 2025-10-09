package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Deletes a performance note of a student.
 */
public class PerfDeleteCommand extends Command {
    public static final String COMMAND_WORD = "perf";

    public static final String MESSAGE_USAGE = COMMAND_WORD + "-d"
            + ": Deletes a note of the student and date indicated. "
            + "Parameters: "
            + PREFIX_STUDENTID + "STUDENTID "
            + PREFIX_INDEX + "INDEX ";

    public static final String MESSAGE_SUCCESS = "Performance note successfully deleted.";
    public static final String MESSAGE_NOT_IMPLEMENTED_YET = "PerfDelete command not implemented yet";

    private final String studentId;
    private final int index;

    public PerfDeleteCommand(String studentId, int index) {
        requireNonNull(studentId);

        this.studentId = studentId;
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException(MESSAGE_NOT_IMPLEMENTED_YET);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PerfDeleteCommand)) {
            return false;
        }

        PerfDeleteCommand otherPerfDeleteCommand = (PerfDeleteCommand) other;
        return studentId.equals(otherPerfDeleteCommand.studentId) && index == otherPerfDeleteCommand.index;
    }
}
