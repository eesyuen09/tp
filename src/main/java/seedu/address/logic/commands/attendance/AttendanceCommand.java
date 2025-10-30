package seedu.address.logic.commands.attendance;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import seedu.address.logic.commands.Command;
import seedu.address.model.person.StudentId;



/**
 * Manages attendance for students in the class.
 * Supports marking, unmarking, and viewing attendance records.
 */
public abstract class AttendanceCommand extends Command {

    public static final String COMMAND_WORD = "att";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Manages attendance for the students.\n"
            + "Requires a flag to specify the action.\n"
            + "Actions:\n"
            + "Mark: -m " + PREFIX_STUDENTID + "STUDENT_ID " + PREFIX_DATE + "DDMMYYYY "
            + PREFIX_CLASSTAG + "CLASS_TAG\n"
            + "Unmark: -u " + PREFIX_STUDENTID + "STUDENT_ID " + PREFIX_DATE + "DDMMYYYY "
            + PREFIX_CLASSTAG + "CLASS_TAG\n"
            + "View: -v " + PREFIX_STUDENTID + "STUDENT_ID\n"
            + "Delete: -d " + PREFIX_STUDENTID + "STUDENT_ID " + PREFIX_DATE + "DDMMYYYY "
            + PREFIX_CLASSTAG + "CLASS_TAG\n"
            + "Examples:\n"
            + COMMAND_WORD + " -m " + PREFIX_STUDENTID + "0123 " + PREFIX_DATE + "15092025 "
            + PREFIX_CLASSTAG + "Sec3_AMath\n"
            + COMMAND_WORD + " -u " + PREFIX_STUDENTID + "0123 " + PREFIX_DATE + "15092025 "
            + PREFIX_CLASSTAG + "Sec3_AMath\n"
            + COMMAND_WORD + " -v " + PREFIX_STUDENTID + "0123 \n"
            + COMMAND_WORD + " -d " + PREFIX_STUDENTID + "0123 " + PREFIX_DATE + "15092025 "
            + PREFIX_CLASSTAG + "Sec3_AMath\n";

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
}
