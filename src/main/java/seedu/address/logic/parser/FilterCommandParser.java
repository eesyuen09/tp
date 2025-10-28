package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MONTH;

import java.util.stream.Stream;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.commands.classtag.ClassTagFilterCommand;
import seedu.address.logic.commands.fee.FeeFilterPaidCommand;
import seedu.address.logic.commands.fee.FeeFilterUnpaidCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Month;

/**
 * Parses input arguments for the {@code filter} command family.
 * Supported:
 *   filter -p  m/MMYY   -> show PAID students for month
 *   filter -up m/MMYY   -> show UNPAID students for month
 *   filter -t t/CLASS_TAG    -> show students with CLASS_TAG
 */
public class FilterCommandParser implements Parser<Command> {

    @Override
    public Command parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(args, PREFIX_MONTH, PREFIX_CLASSTAG);

        String preamble = argMultimap.getPreamble().trim();

        if (preamble.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        String commandFlag = preamble.split("\\s+")[0];

        switch (commandFlag.toLowerCase()) {

        case FeeFilterPaidCommand.COMMAND_FLAG: // "-p"
            return parseFeeFilter(argMultimap, true);

        case FeeFilterUnpaidCommand.COMMAND_FLAG: // "-up"
            return parseFeeFilter(argMultimap, false);

        case ClassTagFilterCommand.COMMAND_FLAG: // "-t"
            return parseTagFilter(argMultimap);

        default:
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Parses arguments for Fee related filters (-p, -up).
     * @param argMultimap Argument multimap containing the parsed arguments.
     * @param isPaid True if parsing for paid filter, false for unpaid.
     * @return The corresponding FeeFilter command.
     * @throws ParseException If arguments are invalid.
     */
    private Command parseFeeFilter(ArgumentMultimap argMultimap, boolean isPaid) throws ParseException {

        String usage = isPaid ? FeeFilterPaidCommand.MESSAGE_USAGE : FeeFilterUnpaidCommand.MESSAGE_USAGE;
        String expectedPreamble = isPaid ? FeeFilterPaidCommand.COMMAND_FLAG : FeeFilterUnpaidCommand.COMMAND_FLAG;

        if (!argMultimap.getPreamble().trim().equalsIgnoreCase(expectedPreamble)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, usage));
        }

        if (!arePrefixesPresent(argMultimap, PREFIX_MONTH)
                || argMultimap.getValue(PREFIX_MONTH).get().isEmpty()
                || argMultimap.getValue(PREFIX_CLASSTAG).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, usage));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_MONTH);

        Month month = ParserUtil.parseMonth(argMultimap.getValue(PREFIX_MONTH).get());
        return isPaid ? new FeeFilterPaidCommand(month) : new FeeFilterUnpaidCommand(month);
    }

    /**
     * Parses arguments for Tag related filter (-t).
     * @param argMultimap Argument multimap containing the parsed arguments.
     * @return The FilterByTagCommand.
     * @throws ParseException If arguments are invalid.
     */
    private Command parseTagFilter(ArgumentMultimap argMultimap) throws ParseException {

        if (!argMultimap.getPreamble().trim().equalsIgnoreCase(ClassTagFilterCommand.COMMAND_FLAG)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ClassTagFilterCommand.MESSAGE_USAGE));
        }

        if (!arePrefixesPresent(argMultimap, PREFIX_CLASSTAG)
                || argMultimap.getValue(PREFIX_CLASSTAG).get().isEmpty()
                || argMultimap.getValue(PREFIX_MONTH).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ClassTagFilterCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLASSTAG);

        ClassTag tag = ParserUtil.parseClassTag(argMultimap.getValue(PREFIX_CLASSTAG).get());
        return new ClassTagFilterCommand(tag);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap m, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(p -> m.getValue(p).isPresent());
    }
}
