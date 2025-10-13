package seedu.address.logic.commands.attendance;

import static java.util.Objects.requireNonNull;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Date;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;

public class AttendanceUnmarkCommand extends AttendanceCommand {

    public static final String MESSAGE_UNMARK_SUCCESS = "Unmarked attendance for: %1$s on %2$s";
    private final Date date;

    /**
     * Creates a AttendanceUnmarkCommand to unmark attendance for the specified student.
     */
    public AttendanceUnmarkCommand(StudentId studentId, Date date) {
        super(studentId);
        this.date = date;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToEdit = model.findPersonByStudentId(studentId)
                .orElseThrow(() -> new CommandException("Student ID not found: " + studentId));

        personToEdit.unmarkAttendance(date);
        model.setPerson(personToEdit, personToEdit);
        return new CommandResult(String.format(MESSAGE_UNMARK_SUCCESS, personToEdit.getName(), date));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AttendanceMarkCommand)) {
            return false;
        }

        AttendanceUnmarkCommand otherCommand = (AttendanceUnmarkCommand) other;
        return studentId.equals(otherCommand.studentId)
                && date.equals(otherCommand.date);
    }
}
