package seedu.address.model.fee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.model.attendance.AttendanceList;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.performance.PerformanceList;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Month;

public class FeeTrackerTest {

    private static Person mkPerson(String id, String enrolledMmYY, String nameSuffix) {
        return new Person(
            new Name("Student " + nameSuffix),
            new Phone("91234567"),
            new Email("student" + nameSuffix + "@ex.com"),
            new Address("Somewhere " + nameSuffix),
            Collections.<ClassTag>emptySet(),
            new StudentId(id),
            enrolledMmYY == null ? Month.now() : new Month(enrolledMmYY),
            new AttendanceList(),
            new PerformanceList());


    }

    @Test
    public void markPaid_and_getExplicitStatusOfMonth() {
        FeeTracker ft = new FeeTracker();
        StudentId id = new StudentId("0001");
        Month m = new Month("0925");

        // initially no explicit record
        assertTrue(ft.getExplicitStatusOfMonth(id, m).isEmpty());

        // mark paid and retrieve explicitly
        ft.markPaid(id, m);
        assertEquals(Optional.of(FeeState.PAID), ft.getExplicitStatusOfMonth(id, m));
    }

    @Test
    public void markUnpaid_and_getExplicitStatusOfMonth() {
        FeeTracker ft = new FeeTracker();
        StudentId id = new StudentId("0002");
        Month m = new Month("1025");

        ft.markUnpaid(id, m);
        assertEquals(Optional.of(FeeState.UNPAID), ft.getExplicitStatusOfMonth(id, m));
    }

    @Test
    public void getDerivedStatusofMonth_beforeEnrollment_returnsEmpty() {
        FeeTracker ft = new FeeTracker();
        Person p = mkPerson("0003", "0925", "A");
        Month earlier = new Month("0825"); // before enrolled 09/25

        assertTrue(ft.getDerivedStatusOfMonth(p, earlier).isEmpty());
    }

    @Test
    public void getDerivedStatusofMonth_afterEnrollment() {
        FeeTracker ft = new FeeTracker();
        Person p = mkPerson("0004", "0825", "B");
        Month sept = new Month("0925");

        assertEquals(Optional.of(FeeState.UNPAID), ft.getDerivedStatusOfMonth(p, sept));
    }

    @Test
    public void getDerivedStatusofMonth_explicitPaid_overridesDefault() {
        FeeTracker ft = new FeeTracker();
        Person p = mkPerson("0005", "0825", "C");
        Month sept = new Month("0925");

        ft.markPaid(p.getStudentId(), sept);
        assertEquals(Optional.of(FeeState.PAID), ft.getDerivedStatusOfMonth(p, sept));
    }

    @Test
    public void getDerivedStatusofMonth_explicitUnpaid_isUnpaid() {
        FeeTracker ft = new FeeTracker();
        Person p = mkPerson("0006", "0825", "D");
        Month sept = new Month("0925");

        ft.markUnpaid(p.getStudentId(), sept);
        assertEquals(Optional.of(FeeState.UNPAID), ft.getDerivedStatusOfMonth(p, sept));
    }

    @Test
    public void getDerivedStatusofMonth_noEnrolledMonth_returnsEmpty() {
        FeeTracker ft = new FeeTracker();
        Person p = mkPerson("0007", null, "E"); // no enrollment
        Month any = new Month("0925");

        assertTrue(ft.getDerivedStatusOfMonth(p, any).isEmpty());
    }

    @Test
    public void getPaymentHistory_startBeforeEnroll() {
        FeeTracker ft = new FeeTracker();
        Person p = mkPerson("0100", "0825", "H");

        // Ask for history 06/25..09/25 -> should start from 08/25
        Month start = new Month("0625");
        Month end = new Month("0925");

        var history = ft.getPaymentHistory(p, start, end);

        var keys = new java.util.ArrayList<>(history.keySet());
        assertEquals(new Month("0825"), keys.get(0), "Start should clamp to enrolled month");
        assertEquals(new Month("0925"), keys.get(keys.size() - 1));
        assertEquals(2 /* 08 & 09 */, keys.size());
        // Because nothing was marked, defaults are UNPAID
        assertTrue(history.values().stream().allMatch(state -> state == FeeState.UNPAID));
    }

    @Test
    public void getPaymentHistory_endBeforeEffectiveStart_returnsEmpty() {
        FeeTracker ft = new FeeTracker();
        Person p = mkPerson("0101", "1025", "I"); // enrolled Oct 2025

        // Ask for 08/25..09/25 (both before enrolled) -> empty
        var history = ft.getPaymentHistory(p, new Month("0825"), new Month("0925"));
        assertTrue(history.isEmpty(), "When end < effectiveStart, history must be empty");
    }

    @Test
    public void getPaymentHistory_inclusiveAndOrdered_success() {
        FeeTracker ft = new FeeTracker();
        Person p = mkPerson("0102", "0825", "J");

        Month start = new Month("0825");
        Month end = new Month("1125"); // 08,09,10,11 -> 4 months

        // Mark 09/25 as PAID; others default to UNPAID
        ft.markPaid(p.getStudentId(), new Month("0925"));

        var history = ft.getPaymentHistory(p, start, end);

        // Inclusive count and ascending order (start..end)
        var keys = new java.util.ArrayList<>(history.keySet());
        assertEquals(4, keys.size());
        assertEquals(start, keys.get(0));
        assertEquals(end, keys.get(keys.size() - 1));

        // States: 08=UNPAID(default), 09=PAID(marked), 10=UNPAID(default), 11=UNPAID(default)
        assertEquals(FeeState.UNPAID, history.get(new Month("0825")));
        assertEquals(FeeState.PAID, history.get(new Month("0925")));
        assertEquals(FeeState.UNPAID, history.get(new Month("1025")));
        assertEquals(FeeState.UNPAID, history.get(new Month("1125")));
    }

    @Test
    public void getPaymentHistory_startEqualsEnd_singleMonth() {
        FeeTracker ft = new FeeTracker();
        Person p = mkPerson("0104", "0825", "K");

        Month m = new Month("1025");
        // Mark explicitly to ensure override is surfaced
        ft.markUnpaid(p.getStudentId(), m);

        var history = ft.getPaymentHistory(p, m, m);
        assertEquals(1, history.size(), "When start==end, one month should be returned");
        assertEquals(FeeState.UNPAID, history.get(m));
    }


}
