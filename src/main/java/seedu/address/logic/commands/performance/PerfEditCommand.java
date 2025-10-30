package seedu.address.logic.commands.performance;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.performance.PerformanceList;
import seedu.address.model.person.performance.PerformanceNote;
import seedu.address.model.person.performance.exceptions.PerformanceNoteNotFoundException;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Date;

/**
 * Edits a performance note of a student.
 */
public class PerfEditCommand extends PerfCommand {

    public static final String MESSAGE_USAGE = COMMAND_WORD + " -e "
            + PREFIX_STUDENTID + "STUDENT_ID "
            + PREFIX_DATE + "DATE "
            + PREFIX_CLASSTAG + "CLASS_TAG "
            + PREFIX_NOTE + "PERFORMANCE_NOTE \n"
            + "Example: perf -e s/0000 d/29102025 t/Sec3_Math pn/Shows consistent improvement.";

    private final StudentId studentId;
    private final Date date;
    private final ClassTag classTag;
    private final String note;

    /**
     * Creates a {@code PerfEditCommand} to edit a performance note
     * of a student identified by the given {@code studentId} for the
     * specified {@code date} and {@code classTag}.
     *
     * @param studentId ID of the student whose performance note is to be edited
     * @param date The date of the performance note to edit
     * @param classTag The class tag of the performance note to edit
     * @param note The new content of the performance note
     */
    public PerfEditCommand(StudentId studentId, Date date, ClassTag classTag, String note) {
        requireNonNull(studentId);
        requireNonNull(note);

        this.studentId = studentId;
        this.date = date;
        this.classTag = classTag;
        this.note = note;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person student = model.getPersonById(studentId)
                .orElseThrow(() -> new CommandException(
                        String.format(Messages.MESSAGE_STUDENT_ID_NOT_FOUND, studentId)));

        List<PerformanceNote> current = student.getPerformanceList().asUnmodifiableList();
        PerformanceList copy = new PerformanceList(new ArrayList<>(current));

        try {
            copy.editPerformanceNote(date, classTag, note);
        } catch (PerformanceNoteNotFoundException e) {
            throw new CommandException("No performance note found for the given date and class tag.");
        }

        model.setPerson(student, student.withPerformanceList(copy));

        return new CommandResult(String.format(PerfCommand.EDITED, student.getName(),
                classTag.tagName, date.getFormattedDate()));
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
        return studentId.equals(otherPerfEditCommand.studentId)
                && classTag.equals(otherPerfEditCommand.classTag)
                && date.equals(otherPerfEditCommand.date)
                && note.equals(otherPerfEditCommand.note);
    }

}
