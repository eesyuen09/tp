package seedu.address.logic.commands.fee;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_STUDENT_ID_NOT_FOUND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MONTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import java.util.Optional;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.time.Month;

/**
 * Marks a student's fee as PAID for a given month.
 * Usage: fee -p s/STUDENT_ID m/MMYY
 */
public class FeeMarkPaidCommand extends FeeCommand {

    public static final String COMMAND_FLAG = "-p";
    public static final String MESSAGE_USAGE =
        String.format("%s %s %sSTUDENT_ID %sMMYY\nExample: %s %s %s0123 %s0925",
            COMMAND_WORD, COMMAND_FLAG, PREFIX_STUDENTID, PREFIX_MONTH,
            COMMAND_WORD, COMMAND_FLAG, PREFIX_STUDENTID, PREFIX_MONTH);

    public static final String MESSAGE_SUCCESS = "%1$s has been successfully marked as Paid for %2$s.";

    private final StudentId studentId;
    private final Month month;

    /**
     * Creates a new {@code FeeMarkPaidCommand} to mark a student as paid.
     *
     * @param studentId The unique student ID of the student.
     * @param month The month to be marked as paid.
     */
    public FeeMarkPaidCommand(StudentId studentId, Month month) {
        this.studentId = requireNonNull(studentId);
        this.month = requireNonNull(month);
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

        model.markPaid(studentId, month);
        return new CommandResult(String.format(MESSAGE_SUCCESS, name, month.toHumanReadable()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FeeMarkPaidCommand)) {
            return false;
        }
        FeeMarkPaidCommand o = (FeeMarkPaidCommand) other;
        return studentId.equals(o.studentId) && month.equals(o.month);
    }
}
