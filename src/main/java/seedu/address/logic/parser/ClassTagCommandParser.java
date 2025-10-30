package seedu.address.logic.parser;


import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSTAG;

import java.util.logging.Logger;
import java.util.stream.Stream;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.classtag.AddClassTagCommand;
import seedu.address.logic.commands.classtag.ClassTagCommand;
import seedu.address.logic.commands.classtag.DeleteClassTagCommand;
import seedu.address.logic.commands.classtag.ListClassTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.ClassTag;


/**
 * Parses input arguments and creates the correct Command object for ClassTag-related operations.
 */
public class ClassTagCommandParser implements Parser<Command> {

    private static final Logger logger = LogsCenter.getLogger(ClassTagCommandParser.class);

    /**
     * Parses the given {@code String} of arguments in the context of the ClassTagCommand
     * and returns a command object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_CLASSTAG);

        String preamble = argMultimap.getPreamble().trim();

        logger.info(() -> String.format("Parsing ClassTagCommand; raw args: \"%s\", preamble: \"%s\"", args, preamble));

        if (preamble.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClassTagCommand.MESSAGE_USAGE));
        }

        String commandFlag = preamble.split("\\s+")[0];

        logger.info(() -> String.format("Detected command flag: %s", commandFlag));

        switch (commandFlag) {

        case AddClassTagCommand.COMMAND_FLAG:
            return parseAddCommand(argMultimap);

        case DeleteClassTagCommand.COMMAND_FLAG:
            return parseDeleteCommand(argMultimap);

        case ListClassTagCommand.COMMAND_FLAG:
            return parseListCommand(argMultimap, preamble);

        default:
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClassTagCommand.MESSAGE_USAGE));
        }

    }

    /**
     * Parses the arguments for the add class tag command.
     * @param argMultimap The argument multimap containing the parsed arguments.
     * @return A new AddClassTagCommand.
     * @throws ParseException If the arguments are invalid.
     */
    private AddClassTagCommand parseAddCommand(ArgumentMultimap argMultimap) throws ParseException {

        requireNonNull(argMultimap);

        assert argMultimap != null : "argMultimap must not be null for add";

        validatePrefixesForCommand(argMultimap, AddClassTagCommand.COMMAND_FLAG, AddClassTagCommand.MESSAGE_USAGE);
        ClassTag classTag = ParserUtil.parseClassTag(argMultimap.getValue(PREFIX_CLASSTAG).get());

        logger.info(() -> String.format("AddClassTagCommand parsed with classTag: %s", classTag));
        return new AddClassTagCommand(classTag);
    }

    /**
     * Parses the arguments for the delete class tag command.
     * @param argMultimap The argument multimap containing the parsed arguments.
     * @return A new DeleteClassTagCommand.
     * @throws ParseException If the arguments are invalid.
     */
    private DeleteClassTagCommand parseDeleteCommand(ArgumentMultimap argMultimap) throws ParseException {

        requireNonNull(argMultimap);

        assert argMultimap != null : "argMultimap must not be null for delete";

        validatePrefixesForCommand(argMultimap, DeleteClassTagCommand.COMMAND_FLAG,
                DeleteClassTagCommand.MESSAGE_USAGE);
        ClassTag classTag = ParserUtil.parseClassTag(argMultimap.getValue(PREFIX_CLASSTAG).get());

        logger.info(() -> String.format("DeleteClassTagCommand parsed with classTag: %s", classTag));
        return new DeleteClassTagCommand(classTag);
    }

    /**
     * Parses arguments for the {@code ListClassTagCommand}.
     */
    private ListClassTagCommand parseListCommand(ArgumentMultimap argMultimap, String preamble) throws ParseException {

        requireNonNull(argMultimap);

        assert preamble != null : "preamble must not be null for list";

        // The list command should not have any arguments after the flag or any prefixes.
        if (!preamble.equalsIgnoreCase(ListClassTagCommand.COMMAND_FLAG)
                || argMultimap.getValue(PREFIX_CLASSTAG).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListClassTagCommand.MESSAGE_USAGE));
        }

        logger.info(() -> "ListClassTagCommand parsed successfully");
        return new ListClassTagCommand();
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Validates that the required prefixes are present and checks for duplicate prefixes.
     * @param argMultimap The argument multimap to validate.
     * @param expectedFlag The expected command flag in the preamble.
     * @param commandUsage The usage message to display if validation fails.
     * @throws ParseException If validation fails.
     */
    private void validatePrefixesForCommand(ArgumentMultimap argMultimap, String expectedFlag, String commandUsage)
            throws ParseException {
        requireNonNull(argMultimap);

        if (!argMultimap.getPreamble().trim().equalsIgnoreCase(expectedFlag)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, commandUsage));
        }

        if (!arePrefixesPresent(argMultimap, PREFIX_CLASSTAG)
                || argMultimap.getValue(PREFIX_CLASSTAG).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, commandUsage));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLASSTAG);
    }

}

