package seedu.address.testutil;


import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.time.Month;

/** A utility class for fee commands. **/
public final class FeeTestUtil {
    private FeeTestUtil() {}

    /**
     * Marks every month from the person's enrolled month up to (but excluding) {@code target} as PAID.
     */
    public static void payAllMonthsBefore(Model model, Person person, Month target) {
        Month enrolled = person.getEnrolledMonth();
        if (enrolled == null) {
            return;
        }
        for (Month cur = enrolled; cur.isBefore(target); cur = cur.plusMonths(1)) {
            model.markPaid(person.getStudentId(), cur);
        }
    }
}
