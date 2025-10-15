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
        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(args, PREFIX_MONTH);

        String preamble = argMultimap.getPreamble().trim();

        if (preamble.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }
        if (!arePrefixesPresent(argMultimap, PREFIX_MONTH)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_MONTH);

        String commandFlag = preamble.split("\\s+")[0];
        Month month = ParserUtil.parseMonth(argMultimap.getValue(PREFIX_MONTH).get());

        switch (commandFlag.toLowerCase()) {
        case FeeFilterPaidCommand.COMMAND_FLAG: // "-p"
            return new FeeFilterPaidCommand(month);
        case FeeFilterUnpaidCommand.COMMAND_FLAG: // "-up"
            return new FeeFilterUnpaidCommand(month);
        default:
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }
    }

    private static boolean arePrefixesPresent(ArgumentMultimap m, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(p -> m.getValue(p).isPresent());
    }
}
