package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.performance.PerfAddCommand;
import seedu.address.logic.commands.performance.PerfDeleteCommand;
import seedu.address.logic.commands.performance.PerfEditCommand;
import seedu.address.logic.commands.performance.PerfNotes;
import seedu.address.logic.commands.performance.PerfViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.StudentId;

/**
 * Parses arguments for the 'perf' command.
 * Supports subflags: -a (add), -v (view), -e (edit), -d (delete).
 */
public class PerfCommandParser implements Parser<Command> {

    @Override
    public Command parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmed = args.trim();
        if (trimmed.isEmpty() || !trimmed.startsWith("-")) {
            throw new ParseException(PerfNotes.MESSAGE_USAGE);
        }

        ArgumentMultimap map = ArgumentTokenizer.tokenize(
                args, PREFIX_STUDENTID, PREFIX_DATE, PREFIX_NOTE, PREFIX_INDEX);

        if (trimmed.startsWith("-a")) {
            // perf -a s/STUDENT_ID d/DDMMYYYY pn/NOTE
            String studentIdString = require(map, PREFIX_STUDENTID, "Missing student id: use s/STUDENT_ID");
            String date = require(map, PREFIX_DATE, "Missing date: use d/DDMMYYYY");
            String note = require(map, PREFIX_NOTE, "Missing note: use pn/NOTE");

            StudentId studentId;
            try {
                studentId = new StudentId(studentIdString);
            } catch (IllegalArgumentException e) {
                throw new ParseException("Invalid student id. " + e.getMessage());
            }

            validateNoteLen(note);
            validateDateFormat(date);

            return new PerfAddCommand(studentId, date, note);

        } else if (trimmed.startsWith("-v")) {
            // perf -v s/STUDENT_ID
            String studentIdString = require(map, PREFIX_STUDENTID, "Missing student id: use s/STUDENT_ID");

            StudentId studentId;
            try {
                studentId = new StudentId(studentIdString);
            } catch (IllegalArgumentException e) {
                throw new ParseException("Invalid student id. " + e.getMessage());
            }

            return new PerfViewCommand(studentId);

        } else if (trimmed.startsWith("-e")) {
            // perf -e s/STUDENT_ID i/INDEX pn/NOTE
            String studentIdString = require(map, PREFIX_STUDENTID, "Missing student id: use s/STUDENT_ID");
            String idx = require(map, PREFIX_INDEX, "Missing index: use i/INDEX");
            String note = require(map, PREFIX_NOTE, "Missing note: use pn/NOTE");

            StudentId studentId;
            try {
                studentId = new StudentId(studentIdString);
            } catch (IllegalArgumentException e) {
                throw new ParseException("Invalid student id. " + e.getMessage());
            }

            int oneBasedIndex = parseOneBasedIndex(idx);
            validateNoteLen(note);

            return new PerfEditCommand(studentId, oneBasedIndex, note);

        } else if (trimmed.startsWith("-d")) {
            // perf -d s/STUDENT_ID i/INDEX
            String studentIdString = require(map, PREFIX_STUDENTID, "Missing student id: use s/STUDENT_ID");
            String idx = require(map, PREFIX_INDEX, "Missing index: use i/INDEX");

            StudentId studentId;
            try {
                studentId = new StudentId(studentIdString);
            } catch (IllegalArgumentException e) {
                throw new ParseException("Invalid student id. " + e.getMessage());
            }

            int oneBasedIndex = parseOneBasedIndex(idx);

            return new PerfDeleteCommand(studentId, oneBasedIndex);
        }

        throw new ParseException(PerfNotes.MESSAGE_USAGE);
    }

    private static String require(ArgumentMultimap map, Prefix pfx, String missingMsg) throws ParseException {
        map.verifyNoDuplicatePrefixesFor(pfx);
        return map.getValue(pfx).orElseThrow(() -> new ParseException(missingMsg));
    }

    private static int parseOneBasedIndex(String s) throws ParseException {
        try {
            int v = Integer.parseInt(s.trim());
            if (v < 1) throw new NumberFormatException();
            return v;
        } catch (NumberFormatException e) {
            throw new ParseException("Error: Invalid performance note index.");
        }
    }

    private static void validateNoteLen(String note) throws ParseException {
        if (note.length() > 200) {
            throw new ParseException("Error: performance note exceeds maximum length of 200 characters");
        }
    }

    private static void validateDateFormat(String date) throws ParseException {
        if (!date.matches("\\d{8}")) {
            throw new ParseException("Invalid date format. Use: DDMMYYYY");
        }
        // No future-date check here yet (we'll centralize that in the model value type).
    }
}
