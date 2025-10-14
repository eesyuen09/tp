package seedu.address.logic.commands.performance;

import seedu.address.logic.commands.Command;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Represents a generic 'perf' command that operates on a student’s performance records.
 * Specific actions (add/view/edit/delete) extend this.
 */
public abstract class PerfCommand extends Command {

    public static final String COMMAND_WORD = "perf";

}
