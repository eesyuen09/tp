package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSTAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.classtagcommands.AddClassTagCommand;
import seedu.address.logic.commands.classtagcommands.ClassTagCommand;
import seedu.address.logic.commands.classtagcommands.DeleteClassTagCommand;
import seedu.address.logic.commands.classtagcommands.ListClassTagCommand;
import seedu.address.model.tag.ClassTag;

public class ClassTagCommandParserTest {

    private ClassTagCommandParser parser = new ClassTagCommandParser();

    @Test
    public void parse_add_success() {
        ClassTag expectedTag = new ClassTag("Sec3_Math");
        // standard command
        assertParseSuccess(parser, " -a " + PREFIX_CLASSTAG + "Sec3_Math",
                new AddClassTagCommand(expectedTag));

        // with extra whitespace
        assertParseSuccess(parser, "   -a   " + PREFIX_CLASSTAG + "   Sec3_Math   ",
                new AddClassTagCommand(expectedTag));
    }

    @Test
    public void parse_delete_success() {
        ClassTag expectedTag = new ClassTag("ToDelete");
        assertParseSuccess(parser, " -d " + PREFIX_CLASSTAG + "ToDelete",
                new DeleteClassTagCommand(expectedTag));
    }

    @Test
    public void parse_invalidFlag_failure() {
        assertParseFailure(parser, " -x " + PREFIX_CLASSTAG + "someTag",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClassTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingPrefix_failure() {
        // Missing prefix for add
        assertParseFailure(parser, " -a " + "someTag",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddClassTagCommand.MESSAGE_USAGE));

        // Missing prefix for delete
        assertParseFailure(parser, " -d " + "someTag",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteClassTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTagName_failure() {
        // Invalid tag name for add
        assertParseFailure(parser, " -a " + PREFIX_CLASSTAG + "Invalid-Tag", ClassTag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_list_success() {
        // standard command
        assertParseSuccess(parser, " -l", new ListClassTagCommand());

        // with extra whitespace
        assertParseSuccess(parser, "   -l   ", new ListClassTagCommand());
    }

    @Test
    public void parse_listWithArguments_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListClassTagCommand.MESSAGE_USAGE);

        // with extra text after flag
        assertParseFailure(parser, " -l extra", expectedMessage);

        // with prefix
        assertParseFailure(parser, " -l " + PREFIX_CLASSTAG + "someTag", expectedMessage);
    }
}
