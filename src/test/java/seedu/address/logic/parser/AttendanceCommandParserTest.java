package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.attendance.AttendanceCommand;
import seedu.address.logic.commands.attendance.AttendanceDeleteCommand;
import seedu.address.logic.commands.attendance.AttendanceMarkAbsentCommand;
import seedu.address.logic.commands.attendance.AttendanceMarkPresentCommand;
import seedu.address.logic.commands.attendance.AttendanceViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Date;

public class AttendanceCommandParserTest {

    private final AttendanceCommandParser parser = new AttendanceCommandParser();

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AttendanceCommand.MESSAGE_USAGE), () -> parser.parse(""));
    }

    @Test
    public void parse_whitespaceOnly_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AttendanceCommand.MESSAGE_USAGE), () -> parser.parse("   "));
    }

    @Test
    public void parse_invalidFlag_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AttendanceCommand.MESSAGE_USAGE), () -> parser.parse("-x s/0123 d/13012025"));
    }

    @Test
    public void parse_markPresentCommand_success() throws Exception {
        String studentId = "0123";
        String date = "13012025";
        String classTag = "Math";
        AttendanceCommand command = parser.parse("-p s/" + studentId + " d/" + date + " t/" + classTag);
        AttendanceMarkPresentCommand expectedCommand = new AttendanceMarkPresentCommand(new StudentId(studentId),
                new Date(date), new ClassTag(classTag));
        assertEquals(expectedCommand, command);
    }

    @Test
    public void parse_markPresentCommandMissingStudentId_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AttendanceMarkPresentCommand.MESSAGE_USAGE), () -> parser.parse("-p d/13012025"));
    }

    @Test
    public void parse_markPresentCommandMissingDate_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AttendanceMarkPresentCommand.MESSAGE_USAGE), () -> parser.parse("-p s/0123"));
    }

    @Test
    public void parse_markPresentCommandWithPreamble_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AttendanceMarkPresentCommand.MESSAGE_USAGE), () -> parser.parse("-p extra s/0123 d/13012025"));
    }

    @Test
    public void parse_markAbsentCommand_success() throws Exception {
        String studentId = "0456";
        String date = "15022025";
        String classTag = "Science";
        AttendanceCommand command = parser.parse("-a s/" + studentId + " d/" + date + " t/" + classTag);
        AttendanceMarkAbsentCommand expectedCommand = new AttendanceMarkAbsentCommand(new StudentId(studentId),
                new Date(date), new ClassTag(classTag));
        assertEquals(expectedCommand, command);
    }

    @Test
    public void parse_markAbsentCommandMissingStudentId_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AttendanceMarkAbsentCommand.MESSAGE_USAGE), () -> parser.parse("-a d/15022025"));
    }

    @Test
    public void parse_markAbsentCommandMissingDate_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AttendanceMarkAbsentCommand.MESSAGE_USAGE), () -> parser.parse("-a s/0456"));
    }

    @Test
    public void parse_markAbsentCommandWithPreamble_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AttendanceMarkAbsentCommand.MESSAGE_USAGE), () -> parser.parse("-a preamble s/0456 d/15022025"));
    }

    @Test
    public void parse_viewCommand_success() throws Exception {
        String studentId = "0789";
        AttendanceCommand command = parser.parse("-v s/" + studentId);
        AttendanceViewCommand expectedCommand = new AttendanceViewCommand(new StudentId(studentId));
        assertEquals(expectedCommand, command);
    }

    @Test
    public void parse_viewCommandMissingStudentId_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AttendanceViewCommand.MESSAGE_USAGE), () -> parser.parse("-v"));
    }

    @Test
    public void parse_viewCommandWithPreamble_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AttendanceViewCommand.MESSAGE_USAGE), () -> parser.parse("-v extra s/0789"));
    }

    @Test
    public void parse_deleteCommand_success() throws Exception {
        String studentId = "0321";
        String date = "20032025";
        String classTag = "English";
        AttendanceCommand command = parser.parse("-d s/" + studentId + " d/" + date + " t/" + classTag);
        AttendanceDeleteCommand expectedCommand = new AttendanceDeleteCommand(new StudentId(studentId),
                new Date(date), new ClassTag(classTag));
        assertEquals(expectedCommand, command);
    }

    @Test
    public void parse_deleteCommandMissingStudentId_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AttendanceDeleteCommand.MESSAGE_USAGE), () -> parser.parse("-d d/20032025 t/English"));
    }

    @Test
    public void parse_deleteCommandMissingDate_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AttendanceDeleteCommand.MESSAGE_USAGE), () -> parser.parse("-d s/0321 t/English"));
    }

    @Test
    public void parse_deleteCommandMissingClassTag_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AttendanceDeleteCommand.MESSAGE_USAGE), () -> parser.parse("-d s/0321 d/20032025"));
    }

    @Test
    public void parse_deleteCommandWithPreamble_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AttendanceDeleteCommand.MESSAGE_USAGE), () -> parser.parse("-d extra s/0321 d/20032025 t/English"));
    }

    @Test
    public void parse_markPresentCommandMissingClassTag_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AttendanceMarkPresentCommand.MESSAGE_USAGE), () -> parser.parse("-p s/0123 d/13012025"));
    }

    @Test
    public void parse_markAbsentCommandMissingClassTag_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AttendanceMarkAbsentCommand.MESSAGE_USAGE), () -> parser.parse("-a s/0456 d/15022025"));
    }

}
