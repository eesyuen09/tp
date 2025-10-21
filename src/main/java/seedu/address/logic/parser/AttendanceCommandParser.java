package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import seedu.address.logic.commands.attendance.AttendanceCommand;
import seedu.address.logic.commands.attendance.AttendanceMarkCommand;
import seedu.address.logic.commands.attendance.AttendanceUnmarkCommand;
import seedu.address.logic.commands.attendance.AttendanceViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Date;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.ClassTag;

/**
 * Parses input arguments and creates the appropriate AttendanceCommand object
 */
public class AttendanceCommandParser implements Parser<AttendanceCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AttendanceCommand
     * and returns the appropriate AttendanceCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public AttendanceCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceCommand.MESSAGE_USAGE));
        }

        String[] splitArgs = trimmedArgs.split("\\s+", 2);
        String flag = splitArgs[0];
        String arguments = splitArgs.length > 1 ? splitArgs[1] : "";

        switch (flag) {
        case "-m":
            return parseMarkCommand(arguments);
        case "-u":
            return parseUnmarkCommand(arguments);
        case "-v":
            return parseViewCommand(arguments);
        default:
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Parses arguments for the mark attendance command.
     */
    private AttendanceMarkCommand parseMarkCommand(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + args,
                PREFIX_STUDENTID, PREFIX_DATE, PREFIX_CLASSTAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_STUDENTID, PREFIX_DATE, PREFIX_CLASSTAG)
                || !argMultimap.getPreamble().trim().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceCommand.MESSAGE_USAGE));
        }

        StudentId studentId = ParserUtil.parseStudentId(argMultimap.getValue(PREFIX_STUDENTID).get());
        Date date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get());
        ClassTag classTag = ParserUtil.parseClassTag(argMultimap.getValue(PREFIX_CLASSTAG).get());

        return new AttendanceMarkCommand(studentId, date, classTag);
    }

    /**
     * Parses arguments for the unmark attendance command.
     */
    private AttendanceUnmarkCommand parseUnmarkCommand(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + args,
                PREFIX_STUDENTID, PREFIX_DATE, PREFIX_CLASSTAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_STUDENTID, PREFIX_DATE, PREFIX_CLASSTAG)
                || !argMultimap.getPreamble().trim().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceCommand.MESSAGE_USAGE));
        }

        StudentId studentId = ParserUtil.parseStudentId(argMultimap.getValue(PREFIX_STUDENTID).get());
        Date date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get());
        ClassTag classTag = ParserUtil.parseClassTag(argMultimap.getValue(PREFIX_CLASSTAG).get());

        return new AttendanceUnmarkCommand(studentId, date, classTag);
    }

    /**
     * Parses arguments for the view attendance command.
     */
    private AttendanceViewCommand parseViewCommand(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + args, PREFIX_STUDENTID);

        if (!arePrefixesPresent(argMultimap, PREFIX_STUDENTID)
                || !argMultimap.getPreamble().trim().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceCommand.MESSAGE_USAGE));
        }

        StudentId studentId = ParserUtil.parseStudentId(argMultimap.getValue(PREFIX_STUDENTID).get());

        return new AttendanceViewCommand(studentId);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return java.util.stream.Stream.of(prefixes)
                .allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}

