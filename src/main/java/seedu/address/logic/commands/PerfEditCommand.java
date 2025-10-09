package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

public class PerfEditCommand extends Command {
    public static final String COMMAND_WORD = "perf";

    public static final String MESSAGE_USAGE = COMMAND_WORD + "-e"
            + ": Adds a note to the student indicated. "
            + "Parameters: "
            + PREFIX_STUDENTID + "STUDENTID "
            + PREFIX_INDEX + "INDEX "
            + PREFIX_NOTE + "PERFORMANCE NOTE ";


    public static final String MESSAGE_SUCCESS = "Performance note successfully added.";
    public static final String MESSAGE_NOT_IMPLEMENTED_YET = "PerfEdit command not implemented yet";

    private final String studentId;
    private final int index;
    private final String note;

    public PerfEditCommand(String studentId, int index, String note) {
        requireNonNull(studentId);
        requireNonNull(note);

        this.studentId = studentId;
        this.index = index;
        this.note = note;
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

        if (!(other instanceof PerfEditCommand)) {
            return false;
        }

        PerfEditCommand otherPerfEditCommand = (PerfEditCommand) other;
        return studentId.equals(otherPerfEditCommand.studentId) && index == otherPerfEditCommand.index
                && note.equals(otherPerfEditCommand.note);
    }
}
