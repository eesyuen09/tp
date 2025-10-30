package seedu.address.logic.commands.attendance;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import java.time.LocalDate;

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
            + "Parameters: " + PREFIX_STUDENTID + "STUDENT_ID " + PREFIX_DATE + "DDMMYYYY "
            + PREFIX_CLASSTAG + "CLASS_TAG\n"
            + "Example: " + COMMAND_WORD + " " + COMMAND_FLAG + " " + PREFIX_STUDENTID + "0123 "
            + PREFIX_DATE + "15092025 " + PREFIX_CLASSTAG + "Sec3_AMath";

    public static final String MESSAGE_MARK_ABSENT_SUCCESS = "Marked %1$s as absent on %2$s for class %3$s.";
    public static final String MESSAGE_ALREADY_MARKED_ABSENT = "%1$s is already marked absent on %2$s for class %3$s.";
    public static final String MESSAGE_STUDENT_DOES_NOT_HAVE_TAG = "Student %1$s does not have the class tag: %2$s";
    public static final String MESSAGE_FUTURE_DATE = "Cannot mark attendance for future date: %1$s";
    public static final String MESSAGE_BEFORE_ENROLMENT =
            "Cannot mark attendance for %1$s on %2$s. Student enrolled in %3$s.";

    private final Date date;
    private final ClassTag classTag;

    /**
     * Creates a AttendanceMarkAbsentCommand to mark attendance as absent for the specified student.
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

        // Check if the date is in the future
        LocalDate attendanceDate = date.toLocalDate();
        if (attendanceDate.isAfter(LocalDate.now())) {
            throw new CommandException(String.format(MESSAGE_FUTURE_DATE, date.getFormattedDate()));
        }

        // Check if the date is before the student's enrollment month
        LocalDate enrollmentStartDate = personToEdit.getEnrolledMonth().toYearMonth().atDay(1);
        if (attendanceDate.isBefore(enrollmentStartDate)) {
            throw new CommandException(String.format(MESSAGE_BEFORE_ENROLMENT,
                    personToEdit.getName(), date.getFormattedDate(),
                    personToEdit.getEnrolledMonth().toHumanReadable()));
        }

        // Check if the class tag exists in the address book
        if (!model.hasClassTag(classTag)) {
            throw new CommandException(Messages.MESSAGE_TAG_NOT_FOUND);
        }

        if (!personToEdit.getTags().contains(classTag)) {
            throw new CommandException(String.format(MESSAGE_STUDENT_DOES_NOT_HAVE_TAG,
                    personToEdit.getName(), classTag.tagName));
        }

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
