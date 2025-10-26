package seedu.address.logic.commands.fee;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_STUDENT_ID_NOT_FOUND;

import java.util.Optional;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Month;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;

/**
 * Marks a student as UNPAID for a specific month.
 */
public class FeeMarkUnpaidCommand extends FeeCommand {

    public static final String COMMAND_FLAG = "-up";
    public static final String MESSAGE_USAGE =
            COMMAND_WORD + " " + COMMAND_FLAG + " s/STUDENT_ID m/MMYY\n"
                    + "Example: fee -up s/0123 m/0925";
    public static final String MESSAGE_SUCCESS = "%1$s has been successfully marked as Unpaid for %2$s.";

    private final StudentId studentId;
    private final Month month;

    /**
     * Creates a new {@code FeeMarkUnpaidCommand} to mark a student as paid.
     *
     * @param studentId The unique student ID of the student.
     * @param month The month to be marked as unpaid.
     */
    public FeeMarkUnpaidCommand(StudentId studentId, Month month) {
        requireNonNull(studentId);
        requireNonNull(month);
        this.studentId = studentId;
        this.month = month;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Optional<Person> personOpt = model.getPersonById(studentId);
        if (personOpt.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_STUDENT_ID_NOT_FOUND, studentId));
        }
        Person person = personOpt.get();
        String name = personOpt.get().getName().fullName;
        Month enrolledMonth = person.getEnrolledMonth();
        if (month.isBefore(person.getEnrolledMonth())) {
            throw new CommandException(String.format(MESSAGE_INVALID_MONTH, name, enrolledMonth.toHumanReadable()));
        }
        try {
            model.markUnpaid(studentId, month);
        } catch (IllegalStateException e) {
            throw new CommandException(e.getMessage());
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, name, month.toHumanReadable()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FeeMarkUnpaidCommand)) {
            return false;
        }
        FeeMarkUnpaidCommand o = (FeeMarkUnpaidCommand) other;
        return studentId.equals(o.studentId) && month.equals(o.month);
    }
}

