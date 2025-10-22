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
            + ": Manages performance notes for students.\n"
            + "Subcommands:\n"
            + "  " + COMMAND_WORD + " -a: Adds a performance note. Usage: "
            + COMMAND_WORD + " -a " + PREFIX_STUDENTID + "STUDENT_ID "
            + PREFIX_DATE + "DDMMYYYY " + PREFIX_NOTE + "NOTE\n"
            + "  " + COMMAND_WORD + " -v: Views performance notes. Usage: "
            + COMMAND_WORD + " -v " + PREFIX_STUDENTID + "STUDENT_ID\n"
            + "  " + COMMAND_WORD + " -e: Edits a performance note. Usage: "
            + COMMAND_WORD + " -e " + PREFIX_STUDENTID + "STUDENT_ID " + PREFIX_DATE + "DDMMYYYY "
            + PREFIX_CLASSTAG + "CLASS_TAG " + PREFIX_NOTE + "NOTE\n"
            + "  " + COMMAND_WORD + " -d: Deletes a performance note. Usage: "
            + COMMAND_WORD + " -d " + PREFIX_STUDENTID + "STUDENT_ID " + PREFIX_DATE + "DDMMYYYY "
            + PREFIX_CLASSTAG + "CLASS_TAG";

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
