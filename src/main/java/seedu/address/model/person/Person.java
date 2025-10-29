package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.attendance.AttendanceList;
import seedu.address.model.person.performance.PerformanceList;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Month;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final StudentId studentId;
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<ClassTag> tags = new HashSet<>();
    private final AttendanceList attendanceList;
    private final Month enrolledMonth;
    private final PerformanceList performanceList;

    /**
     * Constructs a {@code Person} with an automatically generated {@link StudentId}.
     * <p>
     * All fields must be non-null.
     *
     * @param name    The person's name.
     * @param phone   The person's phone number.
     * @param email   The person's email address.
     * @param address The person's address.
     * @param tags    A set of class tags associated with the person.
     * @param enrolledMonth The person's enrolled month
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<ClassTag> tags,
                  Month enrolledMonth, AttendanceList attendanceList, PerformanceList performanceList) {
        this(name, phone, email, address, tags, null, enrolledMonth,
                attendanceList, performanceList); // StudentId to be set later
    }

    /**
     * Constructs a {@code Person} with a specified {@link StudentId}.
     * <p>
     *
     * @param name       The person's name.
     * @param phone      The person's phone number.
     * @param email      The person's email address.
     * @param address    The person's address.
     * @param tags       A set of class tags associated with the person.
     * @param studentId  The student's unique ID.
     * @param enrolledMonth The person's enrolled month.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<ClassTag> tags,
                  StudentId studentId, Month enrolledMonth, AttendanceList attendanceList,
                  PerformanceList performanceList) {
        requireAllNonNull(name, phone, email, address, enrolledMonth);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.studentId = studentId;
        this.enrolledMonth = enrolledMonth;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        this.attendanceList = (attendanceList == null) ? new AttendanceList() : attendanceList;
        this.performanceList = (performanceList == null) ? new PerformanceList() : performanceList;
    }

    /**
     * Returns a new {@code Person} instance with a newly generated {@link StudentId}
     * if this person does not already have one.
     * <p>
     * If the current person already has a {@code StudentId}, this method simply
     * returns the same {@code Person} instance without any modification.
     * <p>
     * This method may throw an {@link seedu.address.model.person.exceptions.ExceedMaxStudentsException}
     * if the maximum number of student IDs has been reached.
     *
     */
    public Person withStudentId() {
        if (this.studentId != null) {
            return this;
        }
        return new Person(name, phone, email, address, tags, new StudentId(),
                enrolledMonth, attendanceList, performanceList);
    }

    /**
     * Returns a new Person object with the updated PerformanceList.
     * @param newList The new PerformanceList to be associated with the person.
     * @return A new Person object with the updated PerformanceList.
     */
    public Person withPerformanceList(PerformanceList newList) {
        return new Person(this.name, this.phone, this.email, this.address, this.tags, this.studentId,
                this.enrolledMonth, this.attendanceList, newList);
    }

    /**
     * Returns a new Person object with the updated AttendanceList.
     * @param newList The new AttendanceList to be associated with the person.
     * @return A new Person object with the updated AttendanceList.
     */
    public Person withAttendanceList(AttendanceList newList) {
        return new Person(this.name, this.phone, this.email, this.address, this.tags, this.studentId,
                this.enrolledMonth, newList, this.performanceList);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public StudentId getStudentId() {
        return studentId;
    }

    public Month getEnrolledMonth() {
        return enrolledMonth;
    }

    /**
     * Returns an immutable ClassTag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<ClassTag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns the list of performance notes of the person.
     * @return the PerformanceList of the person.
     */
    public PerformanceList getPerformanceList() {
        return performanceList;
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName())
                && otherPerson.getPhone().equals(getPhone());
    }

    /**
     * Returns the attendance list for this person.
     * @return the AttendanceList of the person.
     */
    public AttendanceList getAttendanceList() {
        return attendanceList;
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags)
                && performanceList.equals(otherPerson.performanceList);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags, studentId, performanceList);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("student id", studentId)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags.isEmpty() ? "" : tags)
                .add("performanceList", performanceList)
                .toString();
    }

}
