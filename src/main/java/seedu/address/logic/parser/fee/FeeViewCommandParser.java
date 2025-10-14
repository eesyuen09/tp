//package seedu.address.logic.parser.fee;
//
//import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
//import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;
//
//import java.util.stream.Stream;
//
//import seedu.address.logic.commands.Command;
//import seedu.address.logic.commands.fee.FeeViewCommand;
//import seedu.address.logic.parser.ArgumentMultimap;
//import seedu.address.logic.parser.ArgumentTokenizer;
//import seedu.address.logic.parser.Parser;
//import seedu.address.logic.parser.ParserUtil;
//import seedu.address.logic.parser.Prefix;
//import seedu.address.logic.parser.exceptions.ParseException;
//import seedu.address.model.person.StudentId;
//
//public class FeeViewCommandParser implements Parser<Command> {
//
//    @Override
//    public Command parse(String args) throws ParseException {
//        ArgumentMultimap map = ArgumentTokenizer.tokenize(args, PREFIX_STUDENTID);
//
//        if (!arePrefixesPresent(map, PREFIX_STUDENTID) || !map.getPreamble().isEmpty()) {
//            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeViewCommand.MESSAGE_USAGE));
//        }
//
//        map.verifyNoDuplicatePrefixesFor(PREFIX_STUDENTID);
//
//        StudentId id = ParserUtil.parseStudentId(map.getValue(PREFIX_STUDENTID).get());
//        return new FeeViewCommand(id);
//    }
//
//    private static boolean arePrefixesPresent(ArgumentMultimap m, Prefix... prefixes) {
//        return Stream.of(prefixes).allMatch(p -> m.getValue(p).isPresent());
//    }
//}