package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.performance.PerfAddCommand;
import seedu.address.logic.commands.performance.PerfCommand;
import seedu.address.logic.commands.performance.PerfDeleteCommand;
import seedu.address.logic.commands.performance.PerfEditCommand;
import seedu.address.logic.commands.performance.PerfViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Date;

/**
 * Parses arguments for the 'perf' command.
 * Supports subflags: -a (add), -v (view), -e (edit), -d (delete).
 */
public class PerfCommandParser implements Parser<Command> {

    @Override
    public Command parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    PerfCommand.MESSAGE_USAGE));
        }

        String[] splitArgs = trimmedArgs.split("\\s+", 2);
        String flag = splitArgs[0];
        String arguments = splitArgs.length > 1 ? splitArgs[1] : "";

        switch (flag) {
        case "-a":
            return parseAddCommand(arguments);
        case "-v":
            return parseViewCommand(arguments);
        case "-e":
            return parseEditCommand(arguments);
        case "-d":
            return parseDeleteCommand(arguments);
        default:
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    PerfCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Parses arguments for the add performance command.
     */
    private PerfAddCommand parseAddCommand(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + args,
                PREFIX_STUDENTID, PREFIX_DATE, PREFIX_CLASSTAG, PREFIX_NOTE);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STUDENTID, PREFIX_DATE, PREFIX_CLASSTAG, PREFIX_NOTE);

        if (!arePrefixesPresent(argMultimap, PREFIX_STUDENTID, PREFIX_DATE, PREFIX_CLASSTAG, PREFIX_NOTE)
                || !argMultimap.getPreamble().trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    PerfCommand.MESSAGE_USAGE));
        }

        String studentIdString = argMultimap.getValue(PREFIX_STUDENTID).get();
        String dateString = argMultimap.getValue(PREFIX_DATE).get();
        String classTagString = argMultimap.getValue(PREFIX_CLASSTAG).get();
        String note = argMultimap.getValue(PREFIX_NOTE).get();


        StudentId studentId = ParserUtil.parseStudentId(studentIdString);
        Date date = ParserUtil.parseDate(dateString);
        ClassTag classTag = ParserUtil.parseClassTag(classTagString);
        validateNoteLen(note);

        return new PerfAddCommand(studentId, date, classTag, note);
    }

    /**
     * Parses arguments for the view performance command.
     */
    private PerfViewCommand parseViewCommand(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + args, PREFIX_STUDENTID);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STUDENTID);

        if (!arePrefixesPresent(argMultimap, PREFIX_STUDENTID)
                || !argMultimap.getPreamble().trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    PerfCommand.MESSAGE_USAGE));
        }

        String studentIdString = argMultimap.getValue(PREFIX_STUDENTID).get();

        StudentId studentId = ParserUtil.parseStudentId(studentIdString);

        return new PerfViewCommand(studentId);
    }

    /**
     * Parses arguments for the edit performance command.
     */
    private PerfEditCommand parseEditCommand(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + args,
                PREFIX_STUDENTID, PREFIX_DATE, PREFIX_CLASSTAG, PREFIX_NOTE);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STUDENTID, PREFIX_DATE, PREFIX_CLASSTAG, PREFIX_NOTE);

        if (!arePrefixesPresent(argMultimap, PREFIX_STUDENTID, PREFIX_DATE, PREFIX_CLASSTAG, PREFIX_NOTE)
                || !argMultimap.getPreamble().trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    PerfCommand.MESSAGE_USAGE));
        }

        String studentIdString = argMultimap.getValue(PREFIX_STUDENTID).get();
        String dateString = argMultimap.getValue(PREFIX_DATE).get();
        String classTagString = argMultimap.getValue(PREFIX_CLASSTAG).get();
        String note = argMultimap.getValue(PREFIX_NOTE).get();

        StudentId studentId = ParserUtil.parseStudentId(studentIdString);
        Date date = ParserUtil.parseDate(dateString);
        ClassTag classTag = ParserUtil.parseClassTag(classTagString);
        validateNoteLen(note);

        return new PerfEditCommand(studentId, date, classTag, note);
    }

    /**
     * Parses arguments for the delete performance command.
     */
    private PerfDeleteCommand parseDeleteCommand(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + args,
                PREFIX_STUDENTID, PREFIX_DATE, PREFIX_CLASSTAG);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STUDENTID, PREFIX_DATE, PREFIX_CLASSTAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_STUDENTID, PREFIX_DATE, PREFIX_CLASSTAG)
                || !argMultimap.getPreamble().trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    PerfCommand.MESSAGE_USAGE));
        }

        String studentIdString = argMultimap.getValue(PREFIX_STUDENTID).get();
        String dateString = argMultimap.getValue(PREFIX_DATE).get();
        String classTagString = argMultimap.getValue(PREFIX_CLASSTAG).get();

        StudentId studentId = ParserUtil.parseStudentId(studentIdString);
        Date date = ParserUtil.parseDate(dateString);
        ClassTag classTag = ParserUtil.parseClassTag(classTagString);

        return new PerfDeleteCommand(studentId, date, classTag);
    }

    /**
     * Returns true if all the prefixes contain non-empty {@code Optional} values.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return java.util.stream.Stream.of(prefixes)
                .allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }


    private static void validateNoteLen(String note) throws ParseException {
        if (note.length() > 200) {
            throw new ParseException("Error: performance note exceeds maximum length of 200 characters");
        }
    }

}
