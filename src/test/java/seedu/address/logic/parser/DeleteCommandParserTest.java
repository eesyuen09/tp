package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.testutil.TypicalPersons;

/**
 * Unit tests for {@link DeleteCommandParser}.
 *
 * Tests cover:
 * - valid student id with prefix (success)
 * - missing prefix (should return MESSAGE_INVALID_COMMAND_FORMAT + usage)
 * - invalid student id string with prefix (should return MESSAGE_INVALID_COMMAND_FORMAT + usage
 *   because the parser wraps ParseExceptions)
 */
public class DeleteCommandParserTest {

    private DeleteCommandParser parser = new DeleteCommandParser();

    // helper model to obtain a real StudentId from typical data
    private Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());

    @Test
    public void parse_validArgs_returnsDeleteCommand() {
        Person person = model.getFilteredPersonList().get(0);
        StudentId validId = person.getStudentId();

        // form input using the prefix and the student id text
        String userInput = " " + PREFIX_STUDENTID + validId.toString();
        assertParseSuccess(parser, userInput, new DeleteCommand(validId));
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        // provide student id without the required prefix -> triggers the "not present" branch
        Person person = model.getFilteredPersonList().get(0);
        String studentIdText = person.getStudentId().toString();

        String userInput = studentIdText; // missing prefix
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_invalidStudentId_throwsParseException() {
        // provide the prefix but an invalid student id string -> ParserUtil.parseStudentId throws,
        // which the parser wraps; we assert the wrapped usage message
        String invalidId = "not-a-valid-id";
        String userInput = " " + PREFIX_STUDENTID + invalidId;
        String expectedMessage = String.format(StudentId.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, userInput, expectedMessage);
    }
}
