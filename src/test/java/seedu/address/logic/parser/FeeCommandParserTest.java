package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.fee.FeeCommand;
import seedu.address.logic.commands.fee.FeeMarkPaidCommand;
import seedu.address.logic.commands.fee.FeeMarkUnpaidCommand;
import seedu.address.logic.commands.fee.FeeViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Month;
import seedu.address.model.person.StudentId;

/**
 * Unit tests for {@link FeeCommandParser}.
 */
public class FeeCommandParserTest {

    private FeeCommandParser parser;

    @BeforeEach
    public void setUp() {
        parser = new FeeCommandParser();
    }

    @Test
    public void parse_markPaid_validArgs() throws Exception {
        String input = "-p s/0001 m/0925";
        FeeCommand result = parser.parse(input);

        FeeMarkPaidCommand expected =
            new FeeMarkPaidCommand(new StudentId("0001"), new Month("0925"));
        assertEquals(expected, result);
    }

    @Test
    public void parse_markPaid_missingMonth() {
        String input = "-p s/0001";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_markPaid_missingStudentId() {
        String input = "-p m/0925";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_markPaid_extraPreamble() {
        String input = "extra -p s/0001 m/0925";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_markUnpaid_validArgs() throws Exception {
        String input = "-up s/0002 m/0825";
        FeeCommand result = parser.parse(input);

        FeeMarkUnpaidCommand expected =
            new FeeMarkUnpaidCommand(new StudentId("0002"), new Month("0825"));
        assertEquals(expected, result);
    }

    @Test
    public void parse_markUnpaid_missingMonth() {
        String input = "-up s/0002";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_markUnpaid_missingStudentId() {
        String input = "-up m/0825";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_markUnpaid_wrongPreamble() {
        String input = "x -up s/0002 m/0825";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_view_withMonth() throws Exception {
        String input = "-v s/0003 m/0525";
        FeeCommand result = parser.parse(input);

        FeeViewCommand expected =
            new FeeViewCommand(new StudentId("0003"), Optional.of(new Month("0525")));
        assertEquals(expected, result);
    }

    @Test
    public void parse_view_withoutMonth() throws Exception {
        String input = "-v s/0003";
        FeeCommand result = parser.parse(input);

        FeeViewCommand expected =
            new FeeViewCommand(new StudentId("0003"), Optional.empty());
        assertEquals(expected, result);
    }

    @Test
    public void parse_view_invalidMonthFormat() {
        String input = "-v s/0003 m/13AA"; // invalid month
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_view_missingStudentId() {
        String input = "-v m/0925";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_view_wrongPreamble() {
        String input = "random -v s/0001 m/0825";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_unknownFlag_throwsParseException() {
        String input = "-x s/0001 m/0825";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        String input = "";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_whitespaceArgs_throwsParseException() {
        String input = "   ";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }
}