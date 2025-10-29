package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    /**
     * Message to be shown when a name fails validation.
     */
    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain letters, apostrophes ('), hyphens (-), and spaces, "
                    + "must start with a letter, and be at most 100 characters long.";

    /**
     * The first character of the name must be a letter.
     * The rest of the name can contain letters, apostrophes ('), hyphens (-), or spaces,
     * up to a total length of 100 characters.
     * This ensures names like "O'Connor" or "Mary-Jane" are valid,
     * while preventing the name from being blank or starting with whitespace.
     */
    public static final String VALIDATION_REGEX = "[A-Za-z][A-Za-z'\\- ]{0,99}";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
