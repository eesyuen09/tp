package seedu.address.logic.commands.attendance;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Date;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;

/**
 * Marks a student as absent on a specific date.
 * If the student was previously marked as present on that date, the record is updated to absent.
 * If no present record exists for that date, an exception is thrown.
 */
public class AttendanceUnmarkCommand extends AttendanceCommand {

    public static final String MESSAGE_UNMARK_SUCCESS = "Unmarked attendance for: %1$s on %2$s";
    public static final String MESSAGE_ALREADY_UNMARKED = "Attendance for %1$s on %2$s is already unmarked.";

    private final Date date;

    /**
     * Creates a AttendanceUnmarkCommand to unmark attendance for the specified student.
     */
    public AttendanceUnmarkCommand(StudentId studentId, Date date) {
        super(studentId);
        requireNonNull(date);
        this.date = date;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToEdit = model.getPersonById(studentId)
                .orElseThrow(() -> new CommandException(Messages.MESSAGE_STUDENT_ID_NOT_FOUND));

        if (personToEdit.getAttendanceList().hasAttendanceUnmarked(date)) {
            throw new CommandException(String.format(MESSAGE_ALREADY_UNMARKED, personToEdit.getName(), date));
        }

        model.unmarkAttendance(studentId, date);

        return new CommandResult(String.format(MESSAGE_UNMARK_SUCCESS, personToEdit.getName(),
                date.getFormattedDate()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AttendanceUnmarkCommand)) {
            return false;
        }

        AttendanceUnmarkCommand otherCommand = (AttendanceUnmarkCommand) other;
        return studentId.equals(otherCommand.studentId)
                && date.equals(otherCommand.date);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(studentId, date);
    }
}
