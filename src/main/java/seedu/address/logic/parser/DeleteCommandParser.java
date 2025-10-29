package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.StudentId;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@link DeleteCommand}
     * and returns a {@link DeleteCommand} object for execution.
     *
     * @param args user input arguments
     * @return a {@link DeleteCommand} to execute the delete operation
     * @throws ParseException if the user input does not conform to the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_STUDENTID);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STUDENTID);

        if (!argMultimap.getPreamble().trim().isEmpty()
                || !arePrefixesPresent(argMultimap, PREFIX_STUDENTID)
                || argMultimap.getValue(PREFIX_STUDENTID).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        String studentIdArg = argMultimap.getValue(PREFIX_STUDENTID).get();
        StudentId studentId = ParserUtil.parseStudentId(studentIdArg);
        return new DeleteCommand(studentId);
    }

    public static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix prefix) {
        return argumentMultimap.getValue(prefix).isPresent();
    }
}


