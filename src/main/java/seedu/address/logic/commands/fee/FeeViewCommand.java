package seedu.address.logic.commands.fee;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_STUDENT_ID_NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.fee.FeeHistoryEntry;
import seedu.address.model.fee.FeeHistorySummary;
import seedu.address.model.fee.FeeState;
import seedu.address.model.fee.FeeTracker;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.time.Month;

/**
 * View student's payment history
 */
public class FeeViewCommand extends FeeCommand {

    public static final String COMMAND_FLAG = "-v";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + COMMAND_FLAG
        + " s/STUDENT_ID [m/MMYY]\n"
        + "Shows payment history for the student from the given month up to the current month.\n"
        + "If m/MMYY is omitted, shows from enrollment month to the current month.\n"
        + "Examples:\n"
        + "  " + COMMAND_WORD + " " + COMMAND_FLAG + " s/0001\n"
        + "  " + COMMAND_WORD + " " + COMMAND_FLAG + " s/0001 m/0525";

    public static final String MESSAGE_NO_HISTORY_IN_RANGE =
        "Cannot display payment history for future months."
            + "\nPlease select a month up to the current month.";

    private final StudentId studentId;

    private final Optional<Month> startMonthOpt;

    /**
     * Constructor for FeeViewCommand
     */
    public FeeViewCommand(StudentId studentId, Optional<Month> startMonthOpt) {
        requireNonNull(studentId);
        this.studentId = studentId;
        // Handle possible null Optional
        this.startMonthOpt = startMonthOpt == null ? Optional.empty() : startMonthOpt;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person person = model.getPersonById(studentId)
            .orElseThrow(() -> new CommandException(String.format(MESSAGE_STUDENT_ID_NOT_FOUND, studentId)));

        Month enrolled = person.getEnrolledMonth();
        if (enrolled == null) {
            throw new CommandException("Student has no enrolled month recorded.");
        }

        Month endMonth = Month.now();
        Month requestedStart = startMonthOpt.orElse(enrolled);
        Month effectiveStart = requestedStart.isBefore(enrolled) ? enrolled : requestedStart;

        FeeTracker tracker = model.getAddressBook().getFeeTracker();
        Map<Month, FeeState> history = tracker.getPaymentHistory(person, effectiveStart, endMonth);

        if (history.isEmpty()) {
            return new CommandResult(MESSAGE_NO_HISTORY_IN_RANGE);
        }
        List<FeeHistoryEntry> entries = new ArrayList<>();
        for (Map.Entry<Month, FeeState> entry : history.entrySet()) {
            Month month = entry.getKey();
            FeeState state = entry.getValue();
            boolean isExplicit = tracker.getExplicitStatusOfMonth(studentId, month).isPresent();
            entries.add(new FeeHistoryEntry(month, state, isExplicit));
        }

        FeeHistorySummary summary = new FeeHistorySummary(
                person.getName().fullName,
                studentId,
                effectiveStart,
                endMonth,
                enrolled,
                entries.size());

        model.setDisplayedFeeHistory(entries, summary);

        String header = String.format("Payment history for %s (%s) displayed.",
                person.getName().fullName, studentId);

        return new CommandResult(header);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FeeViewCommand)) {
            return false;
        }
        FeeViewCommand o = (FeeViewCommand) other;
        return studentId.equals(o.studentId) && startMonthOpt.equals(o.startMonthOpt);
    }
}
