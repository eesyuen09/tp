package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.performance.PerformanceList;
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
     * @param tags    A set of tags associated with the person.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags, PerformanceList performanceList) {
        this(name, phone, email, address, tags, new StudentId(), performanceList); // StudentId to be set later
    }

    /**
     * Constructs a {@code Person} with a specified {@link StudentId}.
     * <p>
     * All fields must be non-null.
     *
     * @param name       The person's name.
     * @param phone      The person's phone number.
     * @param email      The person's email address.
     * @param address    The person's address.
     * @param tags       A set of tags associated with the person.
     * @param studentId  The student's unique ID.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags, StudentId studentId, PerformanceList performanceList) {
        requireAllNonNull(name, phone, email, address, tags, studentId);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.studentId = studentId;
        this.performanceList = (performanceList == null) ? new PerformanceList() : performanceList;
    }

    public Person withPerformanceList(PerformanceList newList) {
        return new Person(this.name, this.phone, this.email, this.address, this.tags, this.studentId, newList);
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
                && otherPerson.getName().equals(getName());
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
                .add("tags", tags)
                .add("performanceList", performanceList)
                .toString();
    }


    public UniqueClassTagList getClassTags() {
        return new UniqueClassTagList();
    }
}
