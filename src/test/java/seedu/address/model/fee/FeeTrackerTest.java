package seedu.address.model.fee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.model.attendance.AttendanceList;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Month;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.performance.PerformanceList;
import seedu.address.model.tag.ClassTag;

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

        assertTrue(ft.getDerivedStatusofMonth(p, earlier).isEmpty());
    }

    @Test
    public void getDerivedStatusofMonth_afterEnrollment() {
        FeeTracker ft = new FeeTracker();
        Person p = mkPerson("0004", "0825", "B");
        Month sept = new Month("0925");

        assertEquals(Optional.of(FeeState.UNPAID), ft.getDerivedStatusofMonth(p, sept));
    }

    @Test
    public void getDerivedStatusofMonth_explicitPaid_overridesDefault() {
        FeeTracker ft = new FeeTracker();
        Person p = mkPerson("0005", "0825", "C");
        Month sept = new Month("0925");

        ft.markPaid(p.getStudentId(), sept);
        assertEquals(Optional.of(FeeState.PAID), ft.getDerivedStatusofMonth(p, sept));
    }

    @Test
    public void getDerivedStatusofMonth_explicitUnpaid_isUnpaid() {
        FeeTracker ft = new FeeTracker();
        Person p = mkPerson("0006", "0825", "D");
        Month sept = new Month("0925");

        ft.markUnpaid(p.getStudentId(), sept);
        assertEquals(Optional.of(FeeState.UNPAID), ft.getDerivedStatusofMonth(p, sept));
    }

    @Test
    public void getDerivedStatusofMonth_noEnrolledMonth_returnsEmpty() {
        FeeTracker ft = new FeeTracker();
        Person p = mkPerson("0007", null, "E"); // no enrollment
        Month any = new Month("0925");

        assertTrue(ft.getDerivedStatusofMonth(p, any).isEmpty());
    }

    @Test
    public void getPaymentHistory_beforeEnrollment() {
        FeeTracker ft = new FeeTracker();
        Person p = mkPerson("0008", "1025", "F");
        Month latest = new Month("0925"); // before enrolled 10/25

        Map<Month, FeeState> history = ft.getPaymentHistory(p, latest);
        assertTrue(history.isEmpty());
    }

    @Test
    public void getPaymentHistory_inclusiveRange_defaultsAndOverrides() {
        FeeTracker ft = new FeeTracker();
        Person p = mkPerson("0010", "0825", "H");
        Month latest = new Month("1025"); // 08/25..10/25 inclusive: 3 months

        // Mark only September paid
        Month aug = new Month("0825");
        Month sep = new Month("0925");
        Month oct = new Month("1025");
        ft.markPaid(p.getStudentId(), sep);

        Map<Month, FeeState> history = ft.getPaymentHistory(p, latest);

        // Size and keys order
        assertEquals(3, history.size());
        List<Month> keys = new ArrayList<>(history.keySet());
        assertEquals(aug, keys.get(0));
        assertEquals(sep, keys.get(1));
        assertEquals(oct, keys.get(2));

        // Values (defaults to UNPAID except explicit PAID for Sep)
        assertEquals(FeeState.UNPAID, history.get(aug));
        assertEquals(FeeState.PAID, history.get(sep));
        assertEquals(FeeState.UNPAID, history.get(oct));
    }
}
