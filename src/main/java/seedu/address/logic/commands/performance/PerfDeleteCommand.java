package seedu.address.logic.commands.performance;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.performance.PerformanceList;
import seedu.address.model.person.performance.PerformanceNote;

/**
 * Deletes a performance note of a student.
 */
public class PerfDeleteCommand extends PerfCommand {

    public static final String MESSAGE_USAGE = COMMAND_WORD + "-d"
            + ": Deletes a note of the student and date indicated. "
            + "Parameters: "
            + PREFIX_STUDENTID + "STUDENTID "
            + PREFIX_INDEX + "INDEX ";

    private final StudentId studentId;
    private final int index;

    /**
     * Creates an PerfDeleteCommand to delete the specified {@code note} of the student of given {@code studentId}
     * at the specified {@code index}.
     * @param studentId ID of the student to delete the performance note from
     * @param index index of the performance note to be deleted
     */
    public PerfDeleteCommand(StudentId studentId, int index) {
        requireNonNull(studentId);

        this.studentId = studentId;
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person student = model.getPersonById(studentId)
                .orElseThrow(() -> new CommandException(PerfNotes.STUDENT_NOT_FOUND));

        List<PerformanceNote> current = student.getPerformanceList().asUnmodifiableList();
        PerformanceList copy = new PerformanceList(new ArrayList<>(current));

        try {
            copy.remove(index);
        } catch (IndexOutOfBoundsException e) {
            throw new CommandException("Error: Invalid performance note index.");
        }

        model.setPerson(student, student.withPerformanceList(copy));
        return new CommandResult(String.format(PerfNotes.DELETED, index, student.getName()));
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
