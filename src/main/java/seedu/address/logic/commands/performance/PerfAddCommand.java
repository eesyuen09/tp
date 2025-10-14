package seedu.address.logic.commands.performance;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.performance.PerformanceList;
import seedu.address.model.person.performance.PerformanceNote;

/**
 * Adds a performance note to a student.
 */
public class PerfAddCommand extends PerfCommand {

    public static final String MESSAGE_USAGE = COMMAND_WORD + "-a"
            + ": Adds a note to the student indicated. "
            + "Parameters: "
            + PREFIX_STUDENTID + "STUDENTID "
            + PREFIX_DATE + "DATE "
            + PREFIX_NOTE + "PERFORMANCE NOTE ";

    private final StudentId studentId;
    private final String date;
    private final String note;

    /**
     * Creates an PerfAddCommand to add the specified {@code note} to the student of given {@code studentId}
     * on the specified {@code date}.
     * @param studentId ID of the student to add the performance note to
     * @param date date of the performance note
     * @param note the performance note to be added
     */
    public PerfAddCommand(StudentId studentId, String date, String note) {
        requireNonNull(studentId);
        requireNonNull(date);
        requireNonNull(note);

        this.studentId = studentId;
        this.date = date;
        this.note = note;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person student = model.getPersonById(studentId)
                .orElseThrow(() -> new CommandException(PerfNotes.STUDENT_NOT_FOUND));

        List<PerformanceNote> current = student.getPerformanceList().asUnmodifiableList();
        PerformanceList copy = new PerformanceList(new ArrayList<>(current));

        PerformanceNote newNote;
        try {
            newNote = new PerformanceNote(date, note);
            copy.add(newNote);
        } catch (IllegalArgumentException e) {
            throw new CommandException(e.getMessage());
        }

        model.setPerson(student, student.withPerformanceList(copy));
        return new CommandResult(String.format(PerfNotes.ADDED, student.getName(), newNote.printableDate()));
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
