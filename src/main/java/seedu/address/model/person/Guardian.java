package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Guardian {

    // Identity fields
    private final Name name;
    private final Phone phone;

    /**
     * Every field must be present and not null.
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
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Guardian otherGuardian) {
        if (otherGuardian == this) {
            return true;
        }

        return otherGuardian != null
                && otherGuardian.getName().equals(getName());
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
