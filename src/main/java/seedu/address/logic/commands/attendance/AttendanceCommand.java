package seedu.address.logic.commands.attendance;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import seedu.address.logic.commands.Command;
import seedu.address.model.person.StudentId;


/**
 * Mark attendance of existing student in class
 */
public abstract class AttendanceCommand extends Command {

    public static final String COMMAND_WORD = "att";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Manages attendance for the students.\n "
            + "Requires a flag to specify the action.\n"
            + "Parameters:\n"
            + "  Mark: -m " + PREFIX_STUDENTID + "STUDENTID " + PREFIX_DATE + "DATE (format: DDMMYYYY)\n"
            + "  Unmark: -u " + PREFIX_STUDENTID + "STUDENTID " + PREFIX_DATE + "DATE (format: DDMMYYYY)\n"
            + "  View: -v " + PREFIX_STUDENTID + "STUDENTID\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " -m " + PREFIX_STUDENTID + "0123 " + PREFIX_DATE + "15092025\n"
            + "  " + COMMAND_WORD + " -u " + PREFIX_STUDENTID + "0123 " + PREFIX_DATE + "15092025\n"
            + "  " + COMMAND_WORD + " -v " + PREFIX_STUDENTID + "0123 ";

    public final StudentId studentId;
    /**
     * Creates an AttendanceCommand with the specified student ID.
     */
    public AttendanceCommand(StudentId studentId) {
        requireNonNull(studentId);
        this.studentId = studentId;
    }
}





