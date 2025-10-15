package seedu.address.logic.commands.performance;

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
            + "  " + COMMAND_WORD + " -a: Adds a performance note. Usage: perf -a s/STUDENT_ID d/DDMMYYYY pn/NOTE\n"
            + "  " + COMMAND_WORD + " -v: Views performance notes. Usage: perf -v s/STUDENT_ID\n"
            + "  " + COMMAND_WORD + " -e: Edits a performance note. Usage: perf -e s/STUDENT_ID i/INDEX pn/NOTE\n"
            + "  " + COMMAND_WORD + " -d: Deletes a performance note. Usage: perf -d s/STUDENT_ID i/INDEX";

}
