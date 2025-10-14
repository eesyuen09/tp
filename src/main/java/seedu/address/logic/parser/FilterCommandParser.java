package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MONTH;

import java.util.stream.Stream;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.commands.fee.FeeFilterPaidCommand;
import seedu.address.logic.commands.fee.FeeFilterUnpaidCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Month;

/**
 * Parses input arguments for the {@code filter} command family.
 * Supported:
 *   filter -p  m/MMYY   -> show PAID students for month
 *   filter -up m/MMYY   -> show UNPAID students for month
 */
public class FilterCommandParser implements Parser<Command> {

    @Override
    public Command parse(String args) throws ParseException {
        final String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        // First token is the sub-flag; remainder carries prefixed args (e.g., m/0925)
        final String[] tokens = trimmed.split("\\s+", 2);
        final String flag = tokens[0];
        final String remainder = tokens.length > 1 ? tokens[1] : "";

        switch (flag) {
        case FeeFilterPaidCommand.COMMAND_FLAG: // "-p"
            return parsePaid(remainder);
        case FeeFilterUnpaidCommand.COMMAND_FLAG: // "-up"
            return parseUnpaid(remainder);
        default:
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }
    }

    private Command parsePaid(String remainder) throws ParseException {
        ArgumentMultimap map = ArgumentTokenizer.tokenize(remainder, PREFIX_MONTH);

        if (!map.getPreamble().isEmpty() || !arePrefixesPresent(map, PREFIX_MONTH)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FeeFilterPaidCommand.MESSAGE_USAGE));
        }
        map.verifyNoDuplicatePrefixesFor(PREFIX_MONTH);

        Month month = ParserUtil.parseMonth(map.getValue(PREFIX_MONTH).get());
        return new FeeFilterPaidCommand(month);
    }

    private Command parseUnpaid(String remainder) throws ParseException {
        ArgumentMultimap map = ArgumentTokenizer.tokenize(remainder, PREFIX_MONTH);

        if (!map.getPreamble().isEmpty() || !arePrefixesPresent(map, PREFIX_MONTH)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FeeFilterUnpaidCommand.MESSAGE_USAGE));
        }
        map.verifyNoDuplicatePrefixesFor(PREFIX_MONTH);

        Month month = ParserUtil.parseMonth(map.getValue(PREFIX_MONTH).get());
        return new FeeFilterUnpaidCommand(month);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap m, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(p -> m.getValue(p).isPresent());
    }

}