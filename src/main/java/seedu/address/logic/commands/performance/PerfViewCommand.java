package seedu.address.logic.commands.performance;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import java.util.List;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.performance.PerformanceNote;

/**
 * Views performance notes of a student.
 */
public class PerfViewCommand extends PerfCommand {

    public static final String MESSAGE_USAGE = COMMAND_WORD + "-v"
            + ": Views all notes of a student. "
            + "Parameters: "
            + PREFIX_STUDENTID + "STUDENTID ";

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
        requireNonNull(model);

        Person student = findStudentById(model, studentId);
        if (student == null) {
            throw new CommandException(PerfNotes.STUDENT_NOT_FOUND);
        }

        List<PerformanceNote> notes = student.getPerformanceList().asUnmodifiableList();

        model.setDisplayedPerformanceNotes(notes);

        if (notes.isEmpty()) {
            return new CommandResult(student.getName() + " Performance Notes:\n(none)");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(student.getName()).append(" Performance Notes:\n");
        for (PerformanceNote n : notes) {
            sb.append(n.printableDate()).append(": ").append(n.getNote()).append("\n");
        }
        return new CommandResult(sb.toString().trim());
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
