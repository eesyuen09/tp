package seedu.address.logic.commands.performance;

import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import seedu.address.logic.commands.Command;

/**
 * Represents a generic 'perf' command that operates on a student’s performance records.
 * Specific actions (add/view/edit/delete) extend this.
 */
public abstract class PerfCommand extends Command {

    public static final String COMMAND_WORD = "perf";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Manages performance notes for the students.\n"
            + "Requires a flag to specify the action.\n"
            + "Actions:\n"
            + "Add performance note: -a " + PREFIX_STUDENTID + "STUDENT_ID " + PREFIX_DATE + "DATE "
            + PREFIX_CLASSTAG + "CLASS_TAG " + PREFIX_NOTE + "NOTE\n"
            + "View performance note: -v  " + PREFIX_STUDENTID + "STUDENT_ID\n"
            + "Edit performance note: -e  " + PREFIX_STUDENTID + "STUDENT_ID " + PREFIX_DATE + "DATE "
            + PREFIX_CLASSTAG + "CLASS_TAG " + PREFIX_NOTE + "NOTE\n"
            + "Delete performance note: -d  " + PREFIX_STUDENTID + "STUDENT_ID " + PREFIX_DATE + "DATE "
            + PREFIX_CLASSTAG + "CLASS_TAG";
    public static final String MESSAGE_STUDENT_DOES_NOT_HAVE_TAG = "Student %1$s does not have the class tag: %2$s";

    /**
     * Success message for adding a performance note.
     */
    public static final String ADDED = "Performance note successfully added for %s in %s on %s.";

    /**
     * Success message for deleting a performance note.
     */
    public static final String DELETED = "Performance note for %s in %s on %s successfully deleted.";

    /**
     * Success message for editing a performance note.
     */
    public static final String EDITED = "Performance note for %s in %s on %s successfully edited.";

}
