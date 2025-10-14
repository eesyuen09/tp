package seedu.address.logic.parser.fee;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MONTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import java.util.stream.Stream;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.fee.FeeMarkPaidCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Month;
import seedu.address.model.person.StudentId;

/**
 * Parses input arguments and creates a new FeeMarkPaidCommand object
 */
public class FeeMarkPaidCommandParser implements Parser<Command> {
    /**
     * Parses the given {@code String} of arguments in the context of the FeeMarkPaidCommand
     * and returns a command object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public Command parse(String args) throws ParseException {
        ArgumentMultimap map = ArgumentTokenizer.tokenize(args, PREFIX_STUDENTID, PREFIX_MONTH);

        if (!arePrefixesPresent(map, PREFIX_STUDENTID, PREFIX_MONTH) || !map.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeMarkPaidCommand.MESSAGE_USAGE));
        }

        map.verifyNoDuplicatePrefixesFor(PREFIX_STUDENTID, PREFIX_MONTH);

        StudentId id = ParserUtil.parseStudentId(map.getValue(PREFIX_STUDENTID).get());
        Month month = ParserUtil.parseMonth(map.getValue(PREFIX_MONTH).get());

        return new FeeMarkPaidCommand(id, month);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap m, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(p -> m.getValue(p).isPresent());
    }
}
