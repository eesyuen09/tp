package seedu.address.logic.commands.classtag;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;
import seedu.address.model.tag.ClassTag;

/**
 * Lists all class tags in the address book to the user.
 */
public class ListClassTagCommand extends ClassTagCommand {

    public static final String COMMAND_FLAG = "-l";

    public static final String MESSAGE_SUCCESS = "Listed all class tags:\n%1$s";
    public static final String MESSAGE_NO_TAGS_FOUND = "No class tags found. "
            + "You can add one using the 'tag -a' command.";
    public static final String MESSAGE_USAGE = "Lists all created class tags.\n"
            + "Does not take any arguments or prefixes.";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        List<ClassTag> classTagList = model.getClassTagList();

        if (classTagList.isEmpty()) {
            return new CommandResult(MESSAGE_NO_TAGS_FOUND);
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < classTagList.size(); i++) {
            builder.append(i + 1)
                    .append(". ")
                    .append(classTagList.get(i).tagName)
                    .append("\n");
        }

        // Trim the final newline character
        return new CommandResult(String.format(MESSAGE_SUCCESS, builder.toString().trim()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        return other instanceof ListClassTagCommand;
    }

}
