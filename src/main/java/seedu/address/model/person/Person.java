package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueClassTagList;

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
    private final Set<Tag> tags = new HashSet<>();
    private final Set<Attendance> attendanceRecords;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags,
                  StudentId studentId, Set<Attendance> attendanceRecords) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.studentId = studentId;
        this.attendanceRecords = new HashSet<>(attendanceRecords);
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

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
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
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if attendance is already marked as present for this date.
     */
    public boolean hasAttendanceMarked(Date date) {
        return attendanceRecords.stream()
                .anyMatch(attendance -> attendance.getDate().equals(date));
    }

    /**
     * Marks student as present on this date.
     * If record exists, updates it. Otherwise,
     * creates new record.
     */
    public void markAttendance(Date date) {
        attendanceRecords.removeIf(attendance -> attendance.getDate().equals(date));
        // Add new present record
        attendanceRecords.add(new Attendance(date, true));
    }

    /**
     * Marks student as absent on this date.
     * If record exists, updates it. Otherwise, creates new record.
     */
    public void unmarkAttendance(Date date) {
        // Find and remove the present record for this date
        boolean removed = attendanceRecords.removeIf(attendance ->
                attendance.getDate().equals(date) && attendance.isPresent());

        if (removed) {
            // Add new absent record
            attendanceRecords.add(new Attendance(date, false));
        }
    }

    /**
     * Returns all attendance records.
     */
    public Set<Attendance> getAttendanceRecords() {
        return Collections.unmodifiableSet(attendanceRecords);
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
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags, studentId);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("student id", studentId)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .toString();
    }


    public UniqueClassTagList getClassTags() {
        return new UniqueClassTagList();
    }
}
