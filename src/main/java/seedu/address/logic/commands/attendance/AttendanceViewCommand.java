package seedu.address.logic.commands.attendance;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.attendance.AttendanceHistoryEntry;
import seedu.address.model.attendance.AttendanceHistorySummary;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;

/**
 * Displays all attendance records for a specific student.
 * Shows whether the student was present or absent on each recorded date.
 */
public class AttendanceViewCommand extends AttendanceCommand {

    public static final String MESSAGE_VIEW_SUCCESS = "Attendance records for: %1$s\n%2$s";
    public static final String MESSAGE_NO_RECORDS = "No attendance record found for: %1$s";

    /**
     * Creates an AttendanceViewCommand to view attendance history for the specified student.
     *
     * @param studentId The student ID of the student to view attendance for.
     */
    public AttendanceViewCommand(StudentId studentId) {
        super(studentId);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person person = model.getPersonById(studentId)
                .orElseThrow(() -> new CommandException(
                        String.format(Messages.MESSAGE_STUDENT_ID_NOT_FOUND, studentId)));

        if (person.getAttendanceList().size() == 0) {
            return new CommandResult(String.format(MESSAGE_NO_RECORDS, person.getName()));
        }

        List<AttendanceHistoryEntry> entries = person.getAttendanceList().asUnmodifiableList().stream()
                .sorted(Comparator
                        .comparing((Attendance attendance) -> attendance.getDate().toLocalDate())
                        .thenComparing(attendance -> attendance.getClassTag().tagName))
                .map(attendance -> new AttendanceHistoryEntry(attendance.getDate(), attendance.getClassTag(),
                        attendance.isStudentPresent()))
                .collect(Collectors.toList());

        AttendanceHistorySummary summary = new AttendanceHistorySummary(person.getName().toString(),
                person.getStudentId(),
                entries.get(0).getDate(),
                entries.get(entries.size() - 1).getDate(),
                entries.size());
        model.setDisplayedAttendanceHistory(entries, summary);

        String attendanceHistory = formatAttendanceRecords(entries);
        String header = String.format("Attendance history for %s (Student ID %s) displayed.",
                person.getName().fullName, studentId);

        return new CommandResult(header);
    }

    /**
     * Formats the attendance records for display.
     */
    private String formatAttendanceRecords(List<AttendanceHistoryEntry> entries) {
        StringBuilder sb = new StringBuilder();
        entries.forEach(entry -> {
            sb.append(entry.getDate().getFormattedDate())
                    .append(" - ")
                    .append(entry.getClassTag().tagName)
                    .append(": ")
                    .append(entry.getStatusLabel())
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
