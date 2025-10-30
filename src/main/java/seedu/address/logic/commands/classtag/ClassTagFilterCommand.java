package seedu.address.logic.commands.classtag;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.ClassTag;

/**
 * Filters and lists all persons in address book who have the specified ClassTag.
 * ClassTag matching is case-insensitive.
 */
public class ClassTagFilterCommand extends FilterCommand {

    public static final String COMMAND_FLAG = "-t";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + COMMAND_FLAG + " t/CLASS_TAG : "
            + "Filters the list to show only students who have the specified class tag.\n"
            + "Example: " + COMMAND_WORD + " " + COMMAND_FLAG + " t/Sec3_Maths";
    public static final String MESSAGE_SUCCESS = "Listed all persons with class tag: %1$s";

    private final ClassTag toFilter;

    /**
     * Creates a ClassTagFilterCommand to filter by the specified {@code ClassTag}
     */
    public ClassTagFilterCommand(ClassTag classTag) {
        requireNonNull(classTag);
        this.toFilter = classTag;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Optional<ClassTag> foundTag = model.findClassTag(toFilter);

        if (foundTag.isEmpty()) {
            throw new CommandException(String.format(Messages.MESSAGE_TAG_NOT_FOUND, toFilter.tagName));
        }

        ClassTag actualTag = foundTag.get();

        Predicate<Person> personHasTagPredicate = person -> person.hasTag(actualTag);

        model.updateFilteredPersonList(personHasTagPredicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
                        model.getFilteredPersonList().size())
                        + "\n" + String.format(MESSAGE_SUCCESS, actualTag.tagName)
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ClassTagFilterCommand)) {
            return false;
        }

        ClassTagFilterCommand otherFilterByTagCommand = (ClassTagFilterCommand) other;
        return toFilter.equals(otherFilterByTagCommand.toFilter);
    }
}
