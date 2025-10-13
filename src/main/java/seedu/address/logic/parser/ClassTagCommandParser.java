package seedu.address.logic.parser;


import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSTAG;

import java.util.stream.Stream;

import seedu.address.logic.commands.AddClassTagCommand;
import seedu.address.logic.commands.ClassTagCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.ClassTag;

/**
 * Parses input arguments and creates the correct Command object for tag-related operations.
 */
public class ClassTagCommandParser implements Parser<Command> {

    /**
     * Parses the given {@code String} of arguments in the context of the ClassTagCommand
     * and returns a command object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CLASSTAG);

        // Check for the add flag '-a'
        if (argMultimap.getPreamble().equalsIgnoreCase(AddClassTagCommand.COMMAND_WORD)) {
            return parseAddCommand(argMultimap);
        }

        // Add logic for other flags like '-d', '-r' here in the future

        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClassTagCommand.MESSAGE_USAGE));
    }

    /**
     * Parses the arguments for the add class tag command.
     * @param argMultimap The argument multimap containing the parsed arguments.
     * @return A new AddClassTagCommand.
     * @throws ParseException If the arguments are invalid.
     */
    private AddClassTagCommand parseAddCommand(ArgumentMultimap argMultimap) throws ParseException {
        if (!arePrefixesPresent(argMultimap, PREFIX_CLASSTAG)
                || argMultimap.getValue(PREFIX_CLASSTAG).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddClassTagCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLASSTAG);
        ClassTag classTag = ParserUtil.parseClassTag(argMultimap.getValue(PREFIX_CLASSTAG).get());

        return new AddClassTagCommand(classTag);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}

