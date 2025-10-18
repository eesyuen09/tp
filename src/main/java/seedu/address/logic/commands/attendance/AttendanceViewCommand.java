package seedu.address.logic.commands.attendance;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;

/**
 * Displays all attendance records for a specific student.
 * Shows whether the student was present or absent on each recorded date.
 */
public class AttendanceViewCommand extends AttendanceCommand {

    public static final String MESSAGE_VIEW_SUCCESS = "Showing attendance history for: %1$s\n%2$s";
    public static final String MESSAGE_NO_RECORDS = "No attendance records found for: %1$s";

    /**
     * Creates an AttendanceViewCommand to view attendance history for the specified student.
     */
    public AttendanceViewCommand(StudentId studentId) {
        super(studentId);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person person = model.getPersonById(studentId)
                .orElseThrow(() -> new CommandException("Student ID not found: " + studentId));

        String attendanceHistory = formatAttendanceRecords(person);

        if (person.getAttendanceList().size() == 0) {
            return new CommandResult(String.format(MESSAGE_NO_RECORDS, person.getName()));
        }

        return new CommandResult(String.format(MESSAGE_VIEW_SUCCESS, person.getName(), attendanceHistory));
    }

    /**
     * Formats the attendance records for display.
     */
    private String formatAttendanceRecords(Person person) {
        if (person.getAttendanceList().size() == 0) {
            return "No records";
        }

        StringBuilder sb = new StringBuilder();
        person.getAttendanceList().asUnmodifiableList().stream()
                .sorted((a1, a2) -> a1.getDate().toString().compareTo(a2.getDate().toString()))
                .forEach(attendance -> {
                    sb.append(attendance.getDate().getFormattedDate())
                            .append(": ")
                            .append(attendance.isStudentPresent() ? "Present" : "Absent")
                            .append("\n");
                });
        return sb.toString().trim();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AttendanceViewCommand)) {
            return false;
        }

        AttendanceViewCommand otherCommand = (AttendanceViewCommand) other;
        return studentId.equals(otherCommand.studentId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(studentId);
    }
}
