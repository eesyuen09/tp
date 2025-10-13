package seedu.address.logic.commands.performance;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.performance.PerformanceList;
import seedu.address.model.person.performance.PerformanceNote;

/**
 * Edits a performance note of a student.
 */
public class PerfEditCommand extends PerfCommand {

    public static final String MESSAGE_USAGE = COMMAND_WORD + "-e"
            + ": Edits a note of a student on indicated date. "
            + "Parameters: "
            + PREFIX_STUDENTID + "STUDENTID "
            + PREFIX_INDEX + "INDEX "
            + PREFIX_NOTE + "PERFORMANCE NOTE ";

    private final String studentId;
    private final int index;
    private final String note;

    /**
     * Creates an PerfEditCommand to edit the specified {@code note} of the student of given {@code studentId}
     * at the specified {@code index}.
     * @param studentId ID of the student to edit the performance note from
     * @param index index of the performance note to be edited
     * @param note the new performance note
     */
    public PerfEditCommand(String studentId, int index, String note) {
        requireNonNull(studentId);
        requireNonNull(note);

        this.studentId = studentId;
        this.index = index;
        this.note = note;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person student = findStudentById(model, studentId);
        if (student == null) {
            throw new CommandException(PerfNotes.STUDENT_NOT_FOUND);
        }

        List<PerformanceNote> current = student.getPerformanceList().asUnmodifiableList();
        PerformanceList copy = new PerformanceList(new ArrayList<>(current));

        try {
            PerformanceNote old = copy.asUnmodifiableList().get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new CommandException("Error: Invalid performance note index.");
        }

        try {
            PerformanceNote old = copy.asUnmodifiableList().get(index - 1);
            PerformanceNote edited = new PerformanceNote(old.getDate(), note);
            copy.set(index, edited);
        } catch (IndexOutOfBoundsException e) {
            throw new CommandException("Error: Invalid performance note index.");
        } catch (IllegalArgumentException e) {
            throw new CommandException(e.getMessage());
        }

        model.setPerson(student, student.withPerformanceList(copy));
        return new CommandResult(String.format(PerfNotes.EDITED, index, student.getName()));
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
