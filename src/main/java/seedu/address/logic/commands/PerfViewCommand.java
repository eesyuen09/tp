package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Views performance notes of a student.
 */
public class PerfViewCommand extends Command {
    public static final String COMMAND_WORD = "perf";

    public static final String MESSAGE_USAGE = COMMAND_WORD + "-v"
            + ": Deletes a note of the student and date indicated. "
            + "Parameters: "
            + PREFIX_STUDENTID + "STUDENTID ";

    public static final String MESSAGE_SUCCESS = "Performance notes successfully viewed.";
    public static final String MESSAGE_NOT_IMPLEMENTED_YET = "PerfView command not implemented yet";

    private final String studentId;

    /**
     * Creates an PerfViewCommand to view performance notes of the student of given {@code studentId}.
     * @param studentId ID of the student to view the performance notes
     */
    public PerfViewCommand(String studentId) {
        this.studentId = studentId;
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

        if (!(other instanceof PerfViewCommand)) {
            return false;
        }

        PerfViewCommand otherPerfViewCommand = (PerfViewCommand) other;
        return studentId.equals(otherPerfViewCommand.studentId);
    }

}
