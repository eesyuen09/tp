package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.performance.PerfAddCommand;
import seedu.address.logic.commands.performance.PerfCommand;
import seedu.address.logic.commands.performance.PerfDeleteCommand;
import seedu.address.logic.commands.performance.PerfEditCommand;
import seedu.address.logic.commands.performance.PerfViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Date;
import seedu.address.model.person.StudentId;

public class PerfCommandParserTest {

    private static final String VALID_DATE = "15032024";
    private static final String VALID_INDEX = "1";
    private static final String VALID_NOTE = "Good performance in class";
    private static final String VALID_STUDENT_ID = "0123";

    private final PerfCommandParser parser = new PerfCommandParser();

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                PerfCommand.MESSAGE_USAGE), () -> parser.parse(""));
    }

    @Test
    public void parse_whitespaceOnly_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                PerfCommand.MESSAGE_USAGE), () -> parser.parse("   "));
    }

    @Test
    public void parse_invalidFlag_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                PerfCommand.MESSAGE_USAGE), () -> parser.parse("-x s/0123 d/15032024 pn/note"));
    }

    @Test
    public void parse_noFlag_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                PerfCommand.MESSAGE_USAGE), () -> parser.parse("s/" + VALID_STUDENT_ID + " d/" + VALID_DATE
                + " pn/" + VALID_NOTE));
    }

    @Test
    public void parse_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    // ==================== PerfAddCommand Tests ====================

    @Test
    public void parse_addCommand_success() throws Exception {
        String studentId = "0123";
        String date = "15032024";
        String note = "Good performance";
        PerfCommand command = (PerfAddCommand) parser.parse("-a s/" + studentId + " d/" + date + " pn/" + note);
        PerfAddCommand expectedCommand = new PerfAddCommand(new StudentId(studentId), new Date(date), note);
        assertEquals(expectedCommand, command);
    }

    @Test
    public void parse_addCommandMissingStudentId_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                PerfCommand.MESSAGE_USAGE), () -> parser.parse("-a d/15032024 pn/note"));
    }

    @Test
    public void parse_addCommandMissingDate_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                PerfCommand.MESSAGE_USAGE), () -> parser.parse("-a s/0123 pn/note"));
    }

    @Test
    public void parse_addCommandMissingNote_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                PerfCommand.MESSAGE_USAGE), () -> parser.parse("-a s/0123 d/15032024"));
    }

    @Test
    public void parse_addCommandInvalidStudentId_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("-a s/Invalid123 d/15032024 pn/note"));
    }

    @Test
    public void parse_addCommandNoteExceedsMaxLength_throwsParseException() {
        String longNote = "a".repeat(201);
        assertThrows(ParseException.class, () -> parser.parse("-a s/0123 d/15032024 pn/" + longNote));
    }

    @Test
    public void parse_addCommandNoteAtMaxLength_success() throws Exception {
        String maxNote = "a".repeat(200);
        PerfCommand command = (PerfAddCommand) parser.parse("-a s/0123 d/15032024 pn/" + maxNote);
        PerfAddCommand expectedCommand = new PerfAddCommand(new StudentId("0123"), new Date("15032024"), maxNote);
        assertEquals(expectedCommand, command);
    }

    @Test
    public void parse_addCommandWithPreamble_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                PerfCommand.MESSAGE_USAGE), () -> parser.parse("-a extra s/0123 d/15032024 pn/note"));
    }

    @Test
    public void parse_addCommandDuplicatePrefixes_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("-a s/0123 s/0234 d/15032024 pn/note"));
    }

    // ==================== PerfViewCommand Tests ====================

    @Test
    public void parse_viewCommand_success() throws Exception {
        String studentId = "0123";
        PerfCommand command = (PerfViewCommand) parser.parse("-v s/" + studentId);
        PerfViewCommand expectedCommand = new PerfViewCommand(new StudentId(studentId));
        assertEquals(expectedCommand, command);
    }

    @Test
    public void parse_viewCommandMissingStudentId_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                PerfCommand.MESSAGE_USAGE), () -> parser.parse("-v"));
    }

    @Test
    public void parse_viewCommandInvalidStudentId_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("-v s/Invalid123"));
    }

    @Test
    public void parse_viewCommandWithPreamble_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                PerfCommand.MESSAGE_USAGE), () -> parser.parse("-v extra s/0123"));
    }

    // ==================== PerfEditCommand Tests ====================

    @Test
    public void parse_editCommand_success() throws Exception {
        String studentId = "0123";
        String index = "1";
        String note = "Updated note";
        PerfCommand command = (PerfEditCommand) parser.parse("-e s/" + studentId + " i/" + index + " pn/" + note);
        PerfEditCommand expectedCommand = new PerfEditCommand(new StudentId(studentId), Integer.parseInt(index), note);
        assertEquals(expectedCommand, command);
    }

    @Test
    public void parse_editCommandMissingStudentId_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                PerfCommand.MESSAGE_USAGE), () -> parser.parse("-e i/1 pn/note"));
    }

    @Test
    public void parse_editCommandMissingIndex_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                PerfCommand.MESSAGE_USAGE), () -> parser.parse("-e s/0123 pn/note"));
    }

    @Test
    public void parse_editCommandMissingNote_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                PerfCommand.MESSAGE_USAGE), () -> parser.parse("-e s/0123 i/1"));
    }

    @Test
    public void parse_editCommandInvalidStudentId_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("-e s/Invalid123 i/1 pn/note"));
    }

    @Test
    public void parse_editCommandInvalidIndexNegative_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("-e s/0123 i/-1 pn/note"));
    }

    @Test
    public void parse_editCommandInvalidIndexZero_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("-e s/0123 i/0 pn/note"));
    }

    @Test
    public void parse_editCommandInvalidIndexNotNumeric_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("-e s/0123 i/abc pn/note"));
    }

    @Test
    public void parse_editCommandNoteExceedsMaxLength_throwsParseException() {
        String longNote = "a".repeat(201);
        assertThrows(ParseException.class, () -> parser.parse("-e s/0123 i/1 pn/" + longNote));
    }

    @Test
    public void parse_editCommandWithPreamble_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                PerfCommand.MESSAGE_USAGE), () -> parser.parse("-e extra s/0123 i/1 pn/note"));
    }

    // ==================== PerfDeleteCommand Tests ====================

    @Test
    public void parse_deleteCommand_success() throws Exception {
        String studentId = "0123";
        String index = "1";
        PerfCommand command = (PerfDeleteCommand) parser.parse("-d s/" + studentId + " i/" + index);
        PerfDeleteCommand expectedCommand = new PerfDeleteCommand(new StudentId(studentId), Integer.parseInt(index));
        assertEquals(expectedCommand, command);
    }

    @Test
    public void parse_deleteCommandMissingStudentId_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                PerfCommand.MESSAGE_USAGE), () -> parser.parse("-d i/1"));
    }

    @Test
    public void parse_deleteCommandMissingIndex_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                PerfCommand.MESSAGE_USAGE), () -> parser.parse("-d s/0123"));
    }

    @Test
    public void parse_deleteCommandInvalidStudentId_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("-d s/Invalid123 i/1"));
    }

    @Test
    public void parse_deleteCommandInvalidIndexNegative_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("-d s/0123 i/-1"));
    }

    @Test
    public void parse_deleteCommandInvalidIndexZero_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("-d s/0123 i/0"));
    }

    @Test
    public void parse_deleteCommandInvalidIndexNotNumeric_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("-d s/0123 i/xyz"));
    }

    @Test
    public void parse_deleteCommandWithPreamble_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                PerfCommand.MESSAGE_USAGE), () -> parser.parse("-d preamble s/0123 i/1"));
    }
}
