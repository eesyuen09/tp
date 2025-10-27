package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
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

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {

    private static final ClassTag TAG_MATH_SEC3 = new ClassTag("Math_Sec3");
    private static final ClassTag TAG_ENGLISH_J1 = new ClassTag("English_J1");

    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                    new Address("Blk 30 Geylang Street 29, #06-40"),
                    Set.of(TAG_MATH_SEC3), new StudentId(), new Month("0825"),
                    new AttendanceList(), new PerformanceList()),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                    new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                    Set.of(TAG_MATH_SEC3, TAG_ENGLISH_J1), new StudentId(), new Month("0625"),
                    new AttendanceList(), new PerformanceList()), // Assign multiple
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"),
                    new Email("charlotte@example.com"),
                    new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                    Set.of(TAG_ENGLISH_J1), new StudentId(), new Month("0325"),
                    new AttendanceList(), new PerformanceList()),
            new Person(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                    new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                    Set.of(), new StudentId(), new Month("0325"),
                    new AttendanceList(), new PerformanceList()), // No tags
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                    new Address("Blk 47 Tampines Street 20, #17-35"),
                    Set.of(TAG_MATH_SEC3), new StudentId(), new Month("0125"),
                    new AttendanceList(), new PerformanceList()),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                    new Address("Blk 45 Aljunied Street 85, #11-31"),
                    Set.of(), new StudentId(), new Month("0625"),
                    new AttendanceList(), new PerformanceList())
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();

        sampleAb.addClassTag(TAG_MATH_SEC3);
        sampleAb.addClassTag(TAG_ENGLISH_J1);

        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<ClassTag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(ClassTag::new)
                .collect(Collectors.toSet());
    }

}
