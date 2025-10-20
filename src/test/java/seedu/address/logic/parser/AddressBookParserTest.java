package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.attendance.AttendanceCommand;
import seedu.address.logic.commands.attendance.AttendanceMarkCommand;
import seedu.address.logic.commands.attendance.AttendanceUnmarkCommand;
import seedu.address.logic.commands.attendance.AttendanceViewCommand;
import seedu.address.logic.commands.classtagcommands.AddClassTagCommand;
import seedu.address.logic.commands.classtagcommands.ClassTagCommand;
import seedu.address.logic.commands.fee.FeeCommand;
import seedu.address.logic.commands.fee.FeeFilterPaidCommand;
import seedu.address.logic.commands.fee.FeeFilterUnpaidCommand;
import seedu.address.logic.commands.fee.FeeMarkPaidCommand;
import seedu.address.logic.commands.fee.FeeMarkUnpaidCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Date;
import seedu.address.model.person.Month;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.ClassTag;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;


public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        StudentId studentId = new StudentId("2042");
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " s/" + studentId.toString());
        assertEquals(new DeleteCommand(studentId), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        // Create a sample person and descriptor
        Person person = new PersonBuilder().build();
        EditCommand.EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();

        // Get the student's ID for the command
        StudentId studentId = person.getStudentId();

        // Build the command string using the studentId and descriptor details
        String commandString = EditCommand.COMMAND_WORD + " "
                + PREFIX_STUDENTID + studentId + " "
                + PersonUtil.getEditPersonDescriptorDetails(descriptor);

        // Parse the command
        EditCommand command = (EditCommand) parser.parseCommand(commandString);

        // Assert that the parsed command matches the expected EditCommand
        assertEquals(new EditCommand(studentId, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_addClassTag() throws Exception {
        ClassTag tag = new ClassTag("MyClass");
        AddClassTagCommand command = (AddClassTagCommand) parser.parseCommand(ClassTagCommand.COMMAND_WORD
                + " -a " + PREFIX_CLASSTAG + "MyClass");
        assertEquals(new AddClassTagCommand(tag), command);
    }

    @Test
    public void parseCommand_feeMarkPaid_success() throws Exception {
        StudentId id = new StudentId("0001");
        Month m = new Month("0925");
        FeeMarkPaidCommand cmd = (FeeMarkPaidCommand) parser.parseCommand("fee -p s/0001 m/0925");
        assertTrue(parser.parseCommand(FeeCommand.COMMAND_WORD + " -p s/0001 m/0925") instanceof FeeCommand);
        assertTrue(parser.parseCommand(FeeCommand.COMMAND_WORD + " -p s/0001 m/0925") instanceof FeeMarkPaidCommand);
        assertEquals(new FeeMarkPaidCommand(id, m), cmd);
    }

    @Test
    public void parseCommand_feeMarkUnpaid_success() throws Exception {
        StudentId id = new StudentId("0002");
        Month m = new Month("1025");
        FeeMarkUnpaidCommand cmd = (FeeMarkUnpaidCommand) parser.parseCommand("fee -up s/0002 m/1025");
        assertTrue(parser.parseCommand(FeeCommand.COMMAND_WORD + " -up s/0002 m/1025") instanceof FeeCommand);
        assertTrue(parser.parseCommand(FeeCommand.COMMAND_WORD + " -up s/0002 m/1025") instanceof FeeMarkUnpaidCommand);
        assertEquals(new FeeMarkUnpaidCommand(id, m), cmd);
    }

    @Test
    public void parseCommand_invalidFeeCommands_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parseCommand("fee -p"));
        assertThrows(ParseException.class, () -> parser.parseCommand("fee -x s/0001 m/0925"));
    }

    @Test
    public void parseCommand_filterPaid_success() throws Exception {
        Month m = new Month("0925");
        assertEquals(new FeeFilterPaidCommand(m),
            parser.parseCommand("filter -p m/0925"));

        // tolerate extra whitespace
        assertEquals(new FeeFilterPaidCommand(m),
            parser.parseCommand("   filter    -p     m/0925   "));
    }

    @Test
    public void parseCommand_filterUnpaid_success() throws Exception {
        Month m = new Month("1025");
        assertEquals(new FeeFilterUnpaidCommand(m),
            parser.parseCommand("filter -up m/1025"));
    }

    @Test
    public void parseCommand_filterUnknownFlag_failure() {
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE), () -> parser.parseCommand(
                "filter -xx m/0925"));
    }

    @Test
    public void parseCommand_filterMissingMonth_failure() {
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE), () -> parser.parseCommand(
                "filter -p"));
    }

    @Test
    public void parseCommand_filterDuplicateMonthPrefix_failure() {
        assertThrows(ParseException.class, () -> parser.parseCommand(
            "filter -p m/0925 m/1025"));
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }

    @Test
    public void parseCommand_attendance() throws Exception {
        // Test that attendance commands are parsed correctly
        assertTrue(parser.parseCommand("att -v s/0123") instanceof AttendanceViewCommand);
        assertTrue(parser.parseCommand("att -m s/0123 d/13012025") instanceof AttendanceMarkCommand);
        assertTrue(parser.parseCommand("att -u s/0123 d/13012025") instanceof AttendanceUnmarkCommand);
    }

    @Test
    public void parseCommand_attendanceMark() throws Exception {
        String studentId = "0123";
        String date = "13012025";
        AttendanceMarkCommand command = (AttendanceMarkCommand) parser.parseCommand(
                AttendanceCommand.COMMAND_WORD + " -m s/" + studentId + " d/" + date);
        assertEquals(new AttendanceMarkCommand(new StudentId(studentId), new Date(date)), command);
    }

    @Test
    public void parseCommand_attendanceUnmark() throws Exception {
        String studentId = "0123";
        String date = "13012025";
        AttendanceUnmarkCommand command = (AttendanceUnmarkCommand) parser.parseCommand(
                AttendanceCommand.COMMAND_WORD + " -u s/" + studentId + " d/" + date);
        assertEquals(new AttendanceUnmarkCommand(new StudentId(studentId), new Date(date)), command);
    }

    @Test
    public void parseCommand_attendanceView() throws Exception {
        String studentId = "0123";
        AttendanceViewCommand command = (AttendanceViewCommand) parser.parseCommand(
                AttendanceCommand.COMMAND_WORD + " -v s/" + studentId);
        assertEquals(new AttendanceViewCommand(new StudentId(studentId)), command);
    }

    @Test
    public void parseCommand_attendanceEmptyArgs_throwsParseException() {
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceCommand.MESSAGE_USAGE), () ->
                parser.parseCommand(AttendanceCommand.COMMAND_WORD));
    }

    @Test
    public void parseCommand_attendanceInvalidFlag_throwsParseException() {
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceCommand.MESSAGE_USAGE), () ->
                parser.parseCommand(AttendanceCommand.COMMAND_WORD + " -x s/0123 d/13012025"));
    }

    @Test
    public void parseCommand_attendanceMarkMissingStudentId_throwsParseException() {
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceCommand.MESSAGE_USAGE), () ->
                parser.parseCommand(AttendanceCommand.COMMAND_WORD + " -m d/13012025"));
    }

    @Test
    public void parseCommand_attendanceMarkMissingDate_throwsParseException() {
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceCommand.MESSAGE_USAGE), () ->
                parser.parseCommand(AttendanceCommand.COMMAND_WORD + " -m s/0123"));
    }

    @Test
    public void parseCommand_attendanceMarkInvalidStudentId_throwsParseException() {
        assertThrows(ParseException.class, () ->
                parser.parseCommand(AttendanceCommand.COMMAND_WORD + " -m s/abc d/13012025"));
    }

    @Test
    public void parseCommand_attendanceMarkInvalidDate_throwsParseException() {
        assertThrows(ParseException.class, () ->
                parser.parseCommand(AttendanceCommand.COMMAND_WORD + " -m s/0123 d/99999999"));
    }

    @Test
    public void parseCommand_attendanceMarkWithPreamble_throwsParseException() {
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceCommand.MESSAGE_USAGE), () ->
                parser.parseCommand(AttendanceCommand.COMMAND_WORD + " -m extra s/0123 d/13012025"));
    }

    @Test
    public void parseCommand_attendanceUnmarkMissingStudentId_throwsParseException() {
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceCommand.MESSAGE_USAGE), () ->
                parser.parseCommand(AttendanceCommand.COMMAND_WORD + " -u d/13012025"));
    }

    @Test
    public void parseCommand_attendanceUnmarkMissingDate_throwsParseException() {
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceCommand.MESSAGE_USAGE), () ->
                parser.parseCommand(AttendanceCommand.COMMAND_WORD + " -u s/0123"));
    }

    @Test
    public void parseCommand_attendanceUnmarkInvalidStudentId_throwsParseException() {
        assertThrows(ParseException.class, () ->
                parser.parseCommand(AttendanceCommand.COMMAND_WORD + " -u s/12345 d/13012025"));
    }

    @Test
    public void parseCommand_attendanceUnmarkInvalidDate_throwsParseException() {
        assertThrows(ParseException.class, () ->
                parser.parseCommand(AttendanceCommand.COMMAND_WORD + " -u s/0123 d/31022025"));
    }

    @Test
    public void parseCommand_attendanceUnmarkWithPreamble_throwsParseException() {
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceCommand.MESSAGE_USAGE), () ->
                parser.parseCommand(AttendanceCommand.COMMAND_WORD + " -u preamble s/0123 d/13012025"));
    }

    @Test
    public void parseCommand_attendanceViewMissingStudentId_throwsParseException() {
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceCommand.MESSAGE_USAGE), () ->
                parser.parseCommand(AttendanceCommand.COMMAND_WORD + " -v"));
    }

    @Test
    public void parseCommand_attendanceViewInvalidStudentId_throwsParseException() {
        assertThrows(ParseException.class, () ->
                parser.parseCommand(AttendanceCommand.COMMAND_WORD + " -v s/invalid"));
    }

    @Test
    public void parseCommand_attendanceViewWithPreamble_throwsParseException() {
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceCommand.MESSAGE_USAGE), () ->
                parser.parseCommand(AttendanceCommand.COMMAND_WORD + " -v extra s/0123"));
    }
}
