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

    /**
     * Utility: Find the student by ID in the model.
     * Subclasses can use this once we introduce StudentId later.
     */
    protected Person findStudentById(Model model, String studentId) {
        return model.getAddressBook().getPersonList().stream()
                .filter(p -> p.getTags().stream().anyMatch(t -> t.tagName.equals(studentId)))
                .findFirst()
                .orElse(null); // placeholder; will be replaced when StudentId is added
    }
}
