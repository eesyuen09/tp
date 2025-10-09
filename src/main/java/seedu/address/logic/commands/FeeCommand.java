package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Marks a student's fee status (Paid/Unpaid) for a specific month (MMYY) using his/her student ID.
 */
public class FeeCommand extends Command {
    public static final String COMMAND_WORD = "fee";

    private final Action action;
    private final Index index;
    private final String month;

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Update the payment status of a student for a specific month"
            + "by using Student ID. "
            + "Parameters: STUDENT_ID and MONTH"
            + "fee (-p | -up) s/[STUDENT_ID] m/[MMYY]\n"
            + "Example: " + COMMAND_WORD + " -p s/0123 m/1025";

    public static final String MESSAGE_ARGUMENTS = "Index: %1$d, Remark: %2$s";

    public enum Action { SET_PAID, SET_UNPAID }

    public static final String MESSAGE_MARK_PAID_SUCCESS =
            "%s has been successfully marked as Paid for %s.";
    public static final String MESSAGE_MARK_UNPAID_SUCCESS =
            "%s has been successfully marked as Unpaid for %s.";
    public static final String MESSAGE_ALREADY_PAID =
            "%s is already marked as Paid for %s.";
    public static final String MESSAGE_ALREADY_UNPAID =
            "%s is already marked as Unpaid for %s.";

    /**
     * @param action
     * @param index
     * @param month
     */
    public FeeCommand(Action action, Index index, String month) {
        requireAllNonNull(action, index);
        this.action = action;
        this.index = index;
        this.month = month;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = new Person(
                personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), personToEdit.getTags(), remark);

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    /**
     * Generates a command execution success message based on whether
     * the remark is added to or removed from
     * {@code personToEdit}.
     */
    private String generateSuccessMessage(Person personToEdit) {
        String message = !remark.value.isEmpty() ? MESSAGE_MARK_PAID_SUCCESS : MESSAGE_DELETE_REMARK_SUCCESS;
        return String.format(message, Messages.format(personToEdit));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RemarkCommand)) {
            return false;
        }

        RemarkCommand e = (RemarkCommand) other;
        return index.equals(e.index)
                && remark.equals(e.remark);
    }
}