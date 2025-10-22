package seedu.address.logic.commands.performance;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
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
import seedu.address.model.person.Date;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.performance.PerformanceList;
import seedu.address.model.person.performance.PerformanceNote;
import seedu.address.model.tag.ClassTag;

/**
 * Adds a performance note to a student.
 */
public class PerfAddCommand extends PerfCommand {

    public static final String MESSAGE_USAGE = COMMAND_WORD + "-a"
            + ": Adds a note to the student indicated. "
            + "Parameters: "
            + PREFIX_STUDENTID + "STUDENTID "
            + PREFIX_DATE + "DATE "
            + PREFIX_CLASSTAG + "CLASS_TAG "
            + PREFIX_NOTE + "PERFORMANCE NOTE ";
    public static final String MESSAGE_STUDENT_DOES_NOT_HAVE_TAG = "Student %1$s does not have the class tag: %2$s";


    private final StudentId studentId;
    private final Date date;
    private final ClassTag classTag;
    private final String note;

    /**
     * Creates an PerfAddCommand to add the specified {@code note} to the student of given {@code studentId}
     * on the specified {@code date} & {@code classTag}.
     *
     * @param studentId ID of the student to add the performance note to
     * @param date date of the performance note
     * @param classTag the class tag of the performance note
     * @param note the performance note to be added
     */
    public PerfAddCommand(StudentId studentId, Date date, ClassTag classTag, String note) {
        requireAllNonNull(studentId, date, classTag, note);

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

        if (!student.getTags().contains(classTag)) {
            throw new CommandException(String.format(MESSAGE_STUDENT_DOES_NOT_HAVE_TAG,
                    student.getName(), classTag.tagName));
        }

        List<PerformanceNote> current = student.getPerformanceList().asUnmodifiableList();
        PerformanceList copy = new PerformanceList(new ArrayList<>(current));

        try {
            PerformanceNote newNote = new PerformanceNote(date, classTag, note);
            copy.add(newNote);
            model.setPerson(student, student.withPerformanceList(copy));
            return new CommandResult(String.format(PerfCommand.ADDED, student.getName(),
                    classTag.tagName, newNote.getDate().getFormattedDate()));
        } catch (IllegalArgumentException e) {
            throw new CommandException(e.getMessage());
        }

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
        return studentId.equals(otherPerfAddCommand.studentId)
                && classTag.equals(otherPerfAddCommand.classTag)
                && date.equals(otherPerfAddCommand.date)
                && note.equals(otherPerfAddCommand.note);
    }

}
