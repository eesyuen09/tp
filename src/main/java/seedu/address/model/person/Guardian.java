package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a Guardian in the address book.
 * Both fields may be null if no guardian details are provided.
 * This class is immutable once created.
 */
public class Guardian {

    // Identity fields
    private final Name name;
    private final Phone phone;

    /**
     * Either field may be null to indicate that the information is not provided.
     */
    public Guardian(Name name, Phone phone) {
        requireAllNonNull(name, phone);
        this.name = name;
        this.phone = phone;

    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    /**
     * Returns true if both guardians have the same name and phone number.
     *
     * @param otherGuardian the other guardian to compare with
     * @return true if both guardians have identical identifying information
     */
    public boolean isSamePerson(Guardian otherGuardian) {
        if (otherGuardian == this) {
            return true;
        }

        return otherGuardian != null
                && otherGuardian.getName().equals(getName())
                && otherGuardian.getPhone().equals(getPhone());
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
        if (!(other instanceof Guardian)) {
            return false;
        }

        Guardian otherGuardian = (Guardian) other;
        return name.equals(otherGuardian.name)
                && phone.equals(otherGuardian.phone);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .toString();
    }

}
