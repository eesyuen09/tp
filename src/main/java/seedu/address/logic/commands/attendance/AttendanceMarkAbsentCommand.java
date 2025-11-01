package seedu.address.logic.commands.attendance;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Date;

/**
 * Marks a student as absent on a specific date for a specific class.
 * If the student was previously marked as present on that date for that class, the record is updated to absent.
 * If the attendance is already marked as absent for that date and class, an exception is thrown.
 */
public class AttendanceMarkAbsentCommand extends AttendanceCommand {

    public static final String COMMAND_FLAG = "-a";

    public static final String MESSAGE_USAGE = "Marks a student's attendance as absent.\n"
            + "Parameters: " + PREFIX_STUDENTID + "STUDENT_ID " + PREFIX_DATE + "DATE "
            + PREFIX_CLASSTAG + "CLASS_TAG\n"
            + "Example: " + COMMAND_WORD + " " + COMMAND_FLAG + " " + PREFIX_STUDENTID + "0123 "
            + PREFIX_DATE + "15092025 " + PREFIX_CLASSTAG + "Sec3_AMath";

    public static final String MESSAGE_MARK_ABSENT_SUCCESS = "Marked %1$s as absent on %2$s for class %3$s.";
    public static final String MESSAGE_ALREADY_MARKED_ABSENT = "%1$s is already marked absent on %2$s for class %3$s.";

    private final Date date;
    private final ClassTag classTag;

    /**
     * Creates an AttendanceMarkAbsentCommand to mark the specified student as absent.
     */
    public AttendanceMarkAbsentCommand(StudentId studentId, Date date, ClassTag classTag) {
        super(studentId);
        requireNonNull(date);
        requireNonNull(classTag);
        this.date = date;
        this.classTag = classTag;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToEdit = model.getPersonById(studentId)
                .orElseThrow(() -> new CommandException(
                        String.format(Messages.MESSAGE_STUDENT_ID_NOT_FOUND, studentId)));

        validateAttendanceDate(date, personToEdit);
        validateClassTagForAttendanceEdit(model, personToEdit, classTag, date);

        if (personToEdit.getAttendanceList().hasAttendanceMarkedAbsent(date, classTag)) {
            throw new CommandException(String.format(MESSAGE_ALREADY_MARKED_ABSENT,
                    personToEdit.getName(), date.getFormattedDate(), classTag.tagName));
        }

        model.markAttendanceAbsent(studentId, date, classTag);

        return new CommandResult(String.format(MESSAGE_MARK_ABSENT_SUCCESS, personToEdit.getName(),
                date.getFormattedDate(), classTag.tagName));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AttendanceMarkAbsentCommand)) {
            return false;
        }

        AttendanceMarkAbsentCommand otherCommand = (AttendanceMarkAbsentCommand) other;
        return studentId.equals(otherCommand.studentId)
                && date.equals(otherCommand.date)
                && classTag.equals(otherCommand.classTag);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(studentId, date, classTag);
    }
}
