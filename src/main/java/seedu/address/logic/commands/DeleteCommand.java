package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;

/**
 * Deletes a person identified by their {@link StudentId} from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the specified STUDENT_ID.\n"
            + "Parameters: "
            + PREFIX_STUDENTID + "STUDENT_ID\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_STUDENTID + "2042";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted student: %1$s";

    private final StudentId targetId;

    /**
     * Creates a {@code DeleteCommand} to remove the person with the specified {@code StudentId}.
     *
     * @param targetId The unique ID of the person to be deleted.
     */
    public DeleteCommand(StudentId targetId) {
        this.targetId = targetId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (!model.hasPersonWithId(targetId)) {
            throw new CommandException(String.format(Messages.MESSAGE_STUDENT_ID_NOT_FOUND, targetId));
        }

        Person personToDelete = model.getPersonById(targetId).get();
        model.deletePerson(personToDelete);

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetId.equals(otherDeleteCommand.targetId);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetId", targetId)
                .toString();
    }
}
