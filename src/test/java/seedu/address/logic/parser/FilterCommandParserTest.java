package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.commands.classtag.ClassTagFilterCommand;
import seedu.address.logic.commands.fee.FeeFilterPaidCommand;
import seedu.address.logic.commands.fee.FeeFilterUnpaidCommand;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Month;
import seedu.address.testutil.TypicalClassTags;

public class FilterCommandParserTest {

    private FilterCommandParser parser = new FilterCommandParser();

    private final Month validMonth = new Month("0925");
    private final ClassTag validTag = TypicalClassTags.SEC3_MATHS;
    private final ClassTag validTag2 = TypicalClassTags.JC1_PHYSICS;

    @Test
    public void parse_validPaidCommand_success() {
        // standard input
        String userInput = " " + FeeFilterPaidCommand.COMMAND_FLAG + " m/" + "0925";
        assertParseSuccess(parser, userInput, new FeeFilterPaidCommand(validMonth));

        // with extra whitespace
        userInput = "   " + FeeFilterPaidCommand.COMMAND_FLAG + "    m/" + "0925" + "   ";
        assertParseSuccess(parser, userInput, new FeeFilterPaidCommand(validMonth));
    }

    @Test
    public void parse_validUnpaidCommand_success() {
        // standard input
        String userInput = " " + FeeFilterUnpaidCommand.COMMAND_FLAG + " m/" + "0925";
        assertParseSuccess(parser, userInput, new FeeFilterUnpaidCommand(validMonth));

        // with extra whitespace
        userInput = "   " + FeeFilterUnpaidCommand.COMMAND_FLAG + "    m/" + "0925" + "   ";
        assertParseSuccess(parser, userInput, new FeeFilterUnpaidCommand(validMonth));
    }

    @Test
    public void parse_validClassTagCommand_success() {
        String userInput = " " + ClassTagFilterCommand.COMMAND_FLAG + " t/" + validTag.tagName;
        assertParseSuccess(parser, userInput, new ClassTagFilterCommand(validTag));

        // with extra spaces
        userInput = "   " + ClassTagFilterCommand.COMMAND_FLAG + "    t/" + validTag.tagName + "   ";
        assertParseSuccess(parser, userInput, new ClassTagFilterCommand(validTag));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // no flag
        assertParseFailure(parser, " m/" + "0925",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));

        // unknown flag
        assertParseFailure(parser, " -x m/" + "0925",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));

        // no preamble
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));

        // whitespace only (e.g., "filter    ")
        assertParseFailure(parser, "    ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_extraPreambleText_throwsParseException() {
        String expectedErrorPaid = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeFilterPaidCommand.MESSAGE_USAGE);
        String expectedErrorUnpaid = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FeeFilterUnpaidCommand.MESSAGE_USAGE);
        String expectedErrorTag = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClassTagFilterCommand.MESSAGE_USAGE);

        // --- PAID Command with extra text after flag ---
        assertParseFailure(parser, " "
                        + FeeFilterPaidCommand.COMMAND_FLAG + " extra m/" + validMonth.toString(),
                expectedErrorPaid);
        assertParseFailure(parser, " "
                        + FeeFilterPaidCommand.COMMAND_FLAG + " s/123 m/" + validMonth.toString(),
                expectedErrorPaid);

        // --- UNPAID Command with extra text after flag ---
        assertParseFailure(parser, " "
                        + FeeFilterUnpaidCommand.COMMAND_FLAG + " extra m/" + validMonth.toString(),
                expectedErrorUnpaid);

        // --- TAG Command with extra text after flag ---
        assertParseFailure(parser, " " + ClassTagFilterCommand.COMMAND_FLAG + " extra t/" + validTag.tagName,
                expectedErrorTag);
    }

    @Test
    public void parse_missingPrefixOrValue_throwsParseException() {
        // -p flag without m/ or its value
        assertParseFailure(parser, " " + FeeFilterPaidCommand.COMMAND_FLAG,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeFilterPaidCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " " + FeeFilterPaidCommand.COMMAND_FLAG + " m/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeFilterPaidCommand.MESSAGE_USAGE));

        // -up flag without m/ or its value
        assertParseFailure(parser, " " + FeeFilterUnpaidCommand.COMMAND_FLAG,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeFilterUnpaidCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " " + FeeFilterUnpaidCommand.COMMAND_FLAG + " m/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeFilterUnpaidCommand.MESSAGE_USAGE));

        // -t flag without t/ or its value
        assertParseFailure(parser, " " + ClassTagFilterCommand.COMMAND_FLAG,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClassTagFilterCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " " + ClassTagFilterCommand.COMMAND_FLAG + " t/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClassTagFilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_conflictingPrefixes_throwsParseException() {
        // -p flag used with t/ prefix
        assertParseFailure(parser, " " + FeeFilterPaidCommand.COMMAND_FLAG + " m/" + validMonth.toString()
                        + " t/" + validTag.tagName,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeFilterPaidCommand.MESSAGE_USAGE));

        // -up flag used with t/ prefix
        assertParseFailure(parser, " " + FeeFilterUnpaidCommand.COMMAND_FLAG + " m/" + validMonth.toString()
                        + " t/" + validTag.tagName,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeeFilterUnpaidCommand.MESSAGE_USAGE));

        // -t flag used with m/ prefix
        assertParseFailure(parser, " " + ClassTagFilterCommand.COMMAND_FLAG + " t/" + validTag.tagName
                        + " m/" + validMonth.toString(),
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClassTagFilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicatePrefixes_throwsParseException() {
        // Duplicate m/ prefix
        String userInputMonth = " " + FeeFilterPaidCommand.COMMAND_FLAG
                + " m/" + validMonth.toString() + " m/" + validMonth.toString();
        assertParseFailure(parser, userInputMonth, Messages.getErrorMessageForDuplicatePrefixes(new Prefix("m/")));

        // Duplicate t/ prefix
        String userInputTag = " " + ClassTagFilterCommand.COMMAND_FLAG
                + " t/" + validTag.tagName + " t/" + validTag2.tagName;
        assertParseFailure(parser, userInputTag, Messages.getErrorMessageForDuplicatePrefixes(new Prefix("t/")));
    }


}
