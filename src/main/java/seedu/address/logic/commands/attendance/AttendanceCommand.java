package seedu.address.logic.commands.attendance;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import java.time.LocalDate;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Date;



/**
 * Manages attendance for students in the class.
 * Supports marking present, absent, viewing and deleting attendance records.
 */
public abstract class AttendanceCommand extends Command {

    public static final String COMMAND_WORD = "att";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Manages attendance for the students.\n"
            + "Requires a flag to specify the action.\n"
            + "Actions:\n"
            + "Mark Present: -p " + PREFIX_STUDENTID + "STUDENT_ID " + PREFIX_DATE + "DATE "
            + PREFIX_CLASSTAG + "CLASS_TAG\n"
            + "Mark Absent: -a " + PREFIX_STUDENTID + "STUDENT_ID " + PREFIX_DATE + "DATE "
            + PREFIX_CLASSTAG + "CLASS_TAG\n"
            + "View Attendance History: -v " + PREFIX_STUDENTID + "STUDENT_ID\n"
            + "Delete Attendance Record: -d " + PREFIX_STUDENTID + "STUDENT_ID " + PREFIX_DATE + "DATE "
            + PREFIX_CLASSTAG + "CLASS_TAG\n"
            + "Examples:\n"
            + COMMAND_WORD + " -p " + PREFIX_STUDENTID + "0123 " + PREFIX_DATE + "15092025 "
            + PREFIX_CLASSTAG + "Sec3_AMath\n"
            + COMMAND_WORD + " -a " + PREFIX_STUDENTID + "0123 " + PREFIX_DATE + "15092025 "
            + PREFIX_CLASSTAG + "Sec3_AMath\n"
            + COMMAND_WORD + " -v " + PREFIX_STUDENTID + "0123 \n"
            + COMMAND_WORD + " -d " + PREFIX_STUDENTID + "0123 " + PREFIX_DATE + "15092025 "
            + PREFIX_CLASSTAG + "Sec3_AMath\n";

    public static final String MESSAGE_FUTURE_DATE = "Cannot mark attendance for future date: %1$s";
    public static final String MESSAGE_BEFORE_ENROLLMENT =
            "Cannot mark attendance for %1$s on %2$s. Student enrolled in %3$s.";
    public static final String MESSAGE_STUDENT_DOES_NOT_HAVE_TAG = "Student %1$s does not have the class tag: %2$s";

    protected final StudentId studentId;

    /**
     * Creates an AttendanceCommand with the specified student ID.
     *
     * @param studentId The student ID of the student.
     */
    public AttendanceCommand(StudentId studentId) {
        requireNonNull(studentId);
        this.studentId = studentId;
    }

    /**
     * Validates that the attendance date is valid (not in the future and not before enrollment).
     *
     * @param date The date to validate.
     * @param person The person whose enrollment date to check against.
     * @throws CommandException if the date is in the future or before the person's enrollment month.
     */
    protected void validateAttendanceDate(Date date, Person person) throws CommandException {
        LocalDate attendanceDate = date.toLocalDate();

        if (attendanceDate.isAfter(LocalDate.now())) {
            throw new CommandException(String.format(MESSAGE_FUTURE_DATE, date.getFormattedDate()));
        }

        LocalDate enrollmentStartDate = person.getEnrolledMonth().toYearMonth().atDay(1);
        if (attendanceDate.isBefore(enrollmentStartDate)) {
            throw new CommandException(String.format(MESSAGE_BEFORE_ENROLLMENT,
                    person.getName(), date.getFormattedDate(),
                    person.getEnrolledMonth().toHumanReadable()));
        }
    }

    /**
     * Validates that attendance can be marked/edited for the given class tag and date.
     * Allows editing if an attendance record exists, even if the tag has been removed from the student
     * or deleted from the model. For new records, validates that the tag exists and student has it.
     *
     * @param model The model to check for class tag existence.
     * @param person The person to check for class tag membership or attendance record.
     * @param classTag The class tag to validate.
     * @param date The date to check for existing attendance record.
     * @throws CommandException if validation fails.
     */
    protected void validateClassTagForAttendanceEdit(Model model, Person person, ClassTag classTag, Date date)
            throws CommandException {
        // If attendance record exists, allow editing regardless of current tag status
        if (person.getAttendanceList().hasAttendanceRecord(date, classTag)) {
            return;
        }

        // For new records, validate tag exists in model and student has it
        if (!model.hasClassTag(classTag)) {
            throw new CommandException(String.format(Messages.MESSAGE_TAG_NOT_FOUND, classTag.tagName));
        }

        if (!person.getTags().contains(classTag)) {
            throw new CommandException(String.format(MESSAGE_STUDENT_DOES_NOT_HAVE_TAG,
                    person.getName(), classTag.tagName));
        }
    }
}
