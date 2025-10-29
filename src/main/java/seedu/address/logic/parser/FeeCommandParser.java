package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MONTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import java.util.Optional;
import java.util.stream.Stream;

import seedu.address.logic.commands.fee.FeeCommand;
import seedu.address.logic.commands.fee.FeeMarkPaidCommand;
import seedu.address.logic.commands.fee.FeeMarkUnpaidCommand;
import seedu.address.logic.commands.fee.FeeViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.StudentId;
import seedu.address.model.time.Month;

/**
 * Parses input arguments for the {@code fee} command family.
 */
public class FeeCommandParser implements Parser<FeeCommand> {

    @Override
    public FeeCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(args, PREFIX_STUDENTID, PREFIX_MONTH);
        String preamble = argMultimap.getPreamble().trim();

        if (preamble.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeCommand.MESSAGE_USAGE));
        }
        String flag = preamble.split("\\s+")[0];

        switch (flag) {
        case FeeMarkPaidCommand.COMMAND_FLAG:
            return parseMarkPaid(argMultimap);
        case FeeMarkUnpaidCommand.COMMAND_FLAG:
            return parseMarkUnpaid(argMultimap);
        case FeeViewCommand.COMMAND_FLAG:
            return parseView(argMultimap);
        default:
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeCommand.MESSAGE_USAGE));
        }

    }

    /** Parse arguments for FeeMarkPaidCommand*/
    private FeeCommand parseMarkPaid(ArgumentMultimap argMultimap) throws ParseException {
        if (!arePrefixesPresent(argMultimap, PREFIX_STUDENTID, PREFIX_MONTH)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeMarkPaidCommand.MESSAGE_USAGE));
        }
        String expectedPreamble = FeeMarkPaidCommand.COMMAND_FLAG;
        if (!argMultimap.getPreamble().trim().equalsIgnoreCase(expectedPreamble)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeMarkPaidCommand.MESSAGE_USAGE));
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STUDENTID, PREFIX_MONTH);

        StudentId studentId = ParserUtil.parseStudentId(argMultimap.getValue(PREFIX_STUDENTID).get());
        Month month = ParserUtil.parseMonth(argMultimap.getValue(PREFIX_MONTH).get());

        return new FeeMarkPaidCommand(studentId, month);
    }

    /** Parse arguments for FeeMarkUnaidCommand*/
    private FeeCommand parseMarkUnpaid(ArgumentMultimap argMultimap) throws ParseException {
        if (!arePrefixesPresent(argMultimap, PREFIX_STUDENTID, PREFIX_MONTH)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeMarkUnpaidCommand.MESSAGE_USAGE));
        }
        String expectedPreamble = FeeMarkUnpaidCommand.COMMAND_FLAG;
        if (!argMultimap.getPreamble().trim().equalsIgnoreCase(expectedPreamble)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeMarkUnpaidCommand.MESSAGE_USAGE));
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STUDENTID, PREFIX_MONTH);

        StudentId studentId = ParserUtil.parseStudentId(argMultimap.getValue(PREFIX_STUDENTID).get());
        Month month = ParserUtil.parseMonth(argMultimap.getValue(PREFIX_MONTH).get());

        return new FeeMarkUnpaidCommand(studentId, month);
    }

    /** Parse arguments for FeeViewCommand*/
    private FeeCommand parseView(ArgumentMultimap argMultimap) throws ParseException {
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STUDENTID, PREFIX_MONTH);
        if (!arePrefixesPresent(argMultimap, PREFIX_STUDENTID)) {

            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeViewCommand.MESSAGE_USAGE));
        }
        String expectedPreamble = FeeViewCommand.COMMAND_FLAG;
        if (!argMultimap.getPreamble().trim().equalsIgnoreCase(expectedPreamble)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeViewCommand.MESSAGE_USAGE));
        }

        StudentId studentId = ParserUtil.parseStudentId(argMultimap.getValue(PREFIX_STUDENTID).get());
        Optional<Month> startMonthOpt = Optional.empty();
        if (argMultimap.getValue(PREFIX_MONTH).isPresent()) {
            try {
                startMonthOpt = Optional.of(ParserUtil.parseMonth(argMultimap.getValue(PREFIX_MONTH).get()));
            } catch (ParseException e) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeViewCommand.MESSAGE_USAGE));
            }
        }

        return new FeeViewCommand(studentId, startMonthOpt);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}

