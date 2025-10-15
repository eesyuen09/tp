package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MONTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import java.util.stream.Stream;

import seedu.address.logic.commands.fee.FeeCommand;
import seedu.address.logic.commands.fee.FeeMarkPaidCommand;
import seedu.address.logic.commands.fee.FeeMarkUnpaidCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Month;
import seedu.address.model.person.StudentId;

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
        if (!arePrefixesPresent(argMultimap, PREFIX_STUDENTID, PREFIX_MONTH)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeCommand.MESSAGE_USAGE));
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STUDENTID, PREFIX_MONTH);

        StudentId studentId = ParserUtil.parseStudentId(argMultimap.getValue(PREFIX_STUDENTID).get());
        Month month = ParserUtil.parseMonth(argMultimap.getValue(PREFIX_MONTH).get());
        String commandFlag = preamble.split("\\s+")[0];

        if (commandFlag.equalsIgnoreCase(FeeMarkPaidCommand.COMMAND_FLAG)) {
            return new FeeMarkPaidCommand(studentId, month);
        }
        if (commandFlag.equalsIgnoreCase(FeeMarkUnpaidCommand.COMMAND_FLAG)) {
            return new FeeMarkUnpaidCommand(studentId, month);
        }

        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeCommand.MESSAGE_USAGE));

    }


    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}

