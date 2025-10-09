package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Mark attendance of existing student in class
 */
public class MarkAttendanceCommand extends Command {

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult("Marked attendance");
    }
}
