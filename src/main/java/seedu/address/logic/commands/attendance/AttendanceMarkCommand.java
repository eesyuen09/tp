package seedu.address.logic.commands.attendance;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Date;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.ClassTag;

/**
 * Marks a student as present on a specific date for a specific class.
 * If an attendance record already exists for that date and class, it will be updated to present.
 */
public class AttendanceMarkCommand extends AttendanceCommand {

    public static final String MESSAGE_MARK_SUCCESS = "Marked attendance for: %1$s on %2$s for class %3$s";
    public static final String MESSAGE_ALREADY_MARKED = "Attendance for %1$s on %2$s for class %3$s already exists.";
    public static final String MESSAGE_STUDENT_DOES_NOT_HAVE_TAG = "Student %1$s does not have the class tag: %2$s";

    private final Date date;
    private final ClassTag classTag;

    /**
     * Creates a AttendanceMarkCommand to mark attendance for the specified student.
     */
    public AttendanceMarkCommand(StudentId studentId, Date date, ClassTag classTag) {
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

        if (!personToEdit.getTags().contains(classTag)) {
            throw new CommandException(String.format(MESSAGE_STUDENT_DOES_NOT_HAVE_TAG,
                    personToEdit.getName(), classTag.tagName));
        }

        if (personToEdit.getAttendanceList().hasAttendanceMarked(date, classTag)) {
            throw new CommandException(String.format(MESSAGE_ALREADY_MARKED,
                    personToEdit.getName(), date.getFormattedDate(), classTag.tagName));
        }
        model.markAttendance(studentId, date, classTag);

        return new CommandResult(String.format(MESSAGE_MARK_SUCCESS, personToEdit.getName(),
                date.getFormattedDate(), classTag.tagName));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AttendanceMarkCommand)) {
            return false;
        }

        AttendanceMarkCommand otherCommand = (AttendanceMarkCommand) other;
        return studentId.equals(otherCommand.studentId)
                && date.equals(otherCommand.date)
                && classTag.equals(otherCommand.classTag);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(studentId, date, classTag);
    }

}
