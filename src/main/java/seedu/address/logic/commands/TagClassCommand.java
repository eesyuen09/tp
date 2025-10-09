package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Tags an existing student in Tuto with a class tag
 */
public class TagClassCommand extends Command {

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult("Add class tag");
    }
}
