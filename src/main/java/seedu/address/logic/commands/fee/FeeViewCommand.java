package seedu.address.logic.commands.fee;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_STUDENT_ID_NOT_FOUND;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
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
 * Views student's payment history
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

    private static final Logger logger = LogsCenter.getLogger(FeeViewCommand.class);

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

        if (endMonth.isBefore(effectiveStart)) {
            logger.info(() -> String.format(
                "Rejected fee -v: start month %s is a future month",
                effectiveStart.toHumanReadable()));
            throw new CommandException(MESSAGE_NO_HISTORY_IN_RANGE);
        }

        logger.fine(() -> String.format(
            "FeeViewCommand: id=%s start=%s end=%s",
            studentId, effectiveStart, endMonth));
        List<FeeHistoryEntry> entries =
            buildHistoryEntries(model.getAddressBook().getFeeTracker(), person, effectiveStart, endMonth);
        FeeHistorySummary summary = new FeeHistorySummary(
                person.getName().fullName,
                studentId,
                effectiveStart,
                endMonth,
                enrolled,
                entries.size());

        model.setDisplayedFeeHistory(entries, summary);

        String header = String.format("Payment history for %s (Student ID %s) displayed.",
                person.getName().fullName, studentId);

        return new CommandResult(header);
    }

    /** Builds UI rows (newest first) from the tracker’s map. */
    private List<FeeHistoryEntry> buildHistoryEntries(FeeTracker tracker, Person person, Month start, Month end) {
        final Map<Month, FeeState> map = tracker.getPaymentHistory(person, start, end);
        final List<FeeHistoryEntry> result = new ArrayList<>(map.size());
        for (Map.Entry<Month, FeeState> entry : map.entrySet()) {
            final Month m = entry.getKey();
            final boolean explicit = tracker.getExplicitStatusOfMonth(person.getStudentId(), m).isPresent();
            result.add(new FeeHistoryEntry(m, entry.getValue(), explicit));
        }
        Collections.reverse(result);
        return result;
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
