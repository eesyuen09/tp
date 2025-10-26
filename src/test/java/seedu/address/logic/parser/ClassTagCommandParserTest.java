package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSTAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
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


        assertParseFailure(parser, " -l -l", expectedMessage);

        assertParseFailure(parser, " -l extra", expectedMessage);

        assertParseFailure(parser, " -l " + PREFIX_CLASSTAG + "someTag", expectedMessage);

        assertParseFailure(parser, " -l -a " + PREFIX_CLASSTAG + "someTag", expectedMessage);

    }

    @Test
    public void parse_multipleFlags_failure() {

        String expectedAddMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddClassTagCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " -a -t " + PREFIX_CLASSTAG + "Sec3_Math", expectedAddMessage);
        assertParseFailure(parser, " -a -l " + PREFIX_CLASSTAG + "Sec3_Math", expectedAddMessage);

        String expectedDeleteMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteClassTagCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " -d -a " + PREFIX_CLASSTAG + "Sec3_Math", expectedDeleteMessage);

        String expectedTopLevelMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClassTagCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " -x -a " + PREFIX_CLASSTAG + "Sec3_Math", expectedTopLevelMessage);
    }

    @Test
    public void parse_unexpectedPrefix_failure() {

        assertParseFailure(parser, " -a s/Sec3_Math", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddClassTagCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " -d s/Sec3_Math",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteClassTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_repeatedPrefix_failure() {
        String expectedMessage = String.format(Messages.MESSAGE_DUPLICATE_FIELDS + PREFIX_CLASSTAG);

        assertParseFailure(parser, " -a " + PREFIX_CLASSTAG + "Tag1 "
                + PREFIX_CLASSTAG + "Tag2", expectedMessage);
    }

    @Test
    public void parse_emptyInput_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClassTagCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, "   ", expectedMessage);
    }

    @Test
    public void parse_preambleOnlyWord_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClassTagCommand.MESSAGE_USAGE);
        // lone word (simulates "tag  " when top-level parser passes args)
        assertParseFailure(parser, "tag", expectedMessage);
        assertParseFailure(parser, " tag ", expectedMessage);
    }

    @Test
    public void parse_onlyHyphen_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClassTagCommand.MESSAGE_USAGE);
        // stray hyphen or whitespace around it
        assertParseFailure(parser, "-", expectedMessage);
        assertParseFailure(parser, " - ", expectedMessage);
    }

    @Test
    public void parse_flagWithoutPrefix_failure() {
        // flags with no prefix/value should fail with the command-specific usage message
        assertParseFailure(parser, " -a ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddClassTagCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " -d ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteClassTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_flagWithEmptyPrefixValue_failure() {
        String expectedMessage = ClassTag.MESSAGE_CONSTRAINTS;
        // flag followed by the class tag prefix but no value
        assertParseFailure(parser, " -a " + PREFIX_CLASSTAG,
                expectedMessage);
        assertParseFailure(parser, " -d " + PREFIX_CLASSTAG,
                expectedMessage);
    }

}

