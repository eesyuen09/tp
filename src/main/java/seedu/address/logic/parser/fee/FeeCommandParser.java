package seedu.address.logic.parser.fee;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.fee.FeeCommand;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.exceptions.ParseException;

public class FeeCommandParser implements Parser<Command> {

    @Override
    public Command parse(String args) throws ParseException {
        // Trim and split preamble to read the sub-flag (-p, -up, -v)
        String trimmed = args == null ? "" : args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeCommand.MESSAGE_USAGE));
        }

        // First token before prefixes is the subcommand flag
        String[] tokens = trimmed.split("\\s+", 2);
        String flag = tokens[0];
        String remainder = tokens.length > 1 ? tokens[1] : "";

        switch (flag.toLowerCase()) {
        case "-p":
            return new FeeMarkPaidCommandParser().parse(remainder);
        case "-up":
            return new FeeMarkUnpaidCommandParser().parse(remainder);
//        case "-v":
//            return new FeeViewCommandParser().parse(remainder);
        default:
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeCommand.MESSAGE_USAGE));
        }
    }
}