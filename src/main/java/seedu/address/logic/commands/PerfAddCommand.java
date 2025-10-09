package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

public class PerfAddCommand extends Command {
    public static final String COMMAND_WORD = "perf";

    public static final String MESSAGE_USAGE = COMMAND_WORD + "-a"
            + ": Adds a note to the student indicated. "
            + "Parameters: "
            + PREFIX_STUDENTID + "STUDENTID "
            + PREFIX_DATE + "PHONE "
            + PREFIX_NOTE + "PERFORMANCE NOTE ";

    public static final String MESSAGE_SUCCESS = "Performance note successfully added.";
    public static final String MESSAGE_NOT_IMPLEMENTED_YET = "PerfAdd command not implemented yet";

    private final String studentId;
    private final String date;
    private final String note;

    public PerfAddCommand(String studentId, String date, String note) {
        requireNonNull(studentId);
        requireNonNull(date);
        requireNonNull(note);

        this.studentId = studentId;
        this.date = date;
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

        if (!(other instanceof PerfAddCommand)) {
            return false;
        }

        PerfAddCommand otherPerfAddCommand = (PerfAddCommand) other;
        return studentId.equals(otherPerfAddCommand.studentId) && date.equals(otherPerfAddCommand.date)
                && note.equals(otherPerfAddCommand.note);
    }
}
