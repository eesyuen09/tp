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
 * Marks a student as present on a specific date.
 * If an attendance record already exists for that date, it will be updated to present.
 */
public class AttendanceMarkCommand extends AttendanceCommand {

    public static final String MESSAGE_MARK_SUCCESS = "Marked attendance for: %1$s on %2$s";
    public static final String MESSAGE_ALREADY_MARKED = "Attendance for %1$s on %2$s already exists.";

    private final Date date;

    /**
     * Creates a AttendanceMarkCommand to mark attendance for the specified student.
     */
    public AttendanceMarkCommand(StudentId studentId, Date date) {
        super(studentId);
        requireNonNull(date);
        this.date = date;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToEdit = model.getPersonById(studentId)
                .orElseThrow(() -> new CommandException(Messages.MESSAGE_STUDENT_ID_NOT_FOUND));

        if (personToEdit.getAttendanceList().hasAttendanceMarked(date)) {
            throw new CommandException(String.format(MESSAGE_ALREADY_MARKED,
                    personToEdit.getName(), date.getFormattedDate()));
        }
        model.markAttendance(studentId, date);

        return new CommandResult(String.format(MESSAGE_MARK_SUCCESS, personToEdit.getName(), date.getFormattedDate()));
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
                && date.equals(otherCommand.date);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(studentId, date);
    }

}
