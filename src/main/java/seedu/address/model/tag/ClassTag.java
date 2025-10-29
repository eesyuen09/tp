package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;

/**
 * Represents a ClassTag in the application.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class ClassTag {

    public static final String MESSAGE_CONSTRAINTS =
            "Invalid tag name. Tags must be 1-30 characters long, alphanumeric with underscores allowed.";

    public static final String VALIDATION_REGEX = "[\\p{Alnum}_]+";

    private static final int MAX_LENGTH = 30;

    public final String tagName;

    /**
     * Constructs a {@code ClassTag}.
     *
     * @param tagName A valid tag name.
     */
    public ClassTag(String tagName) {
        requireNonNull(tagName);
        String trimmedTagName = tagName.trim();
        if (!isValidTagName(trimmedTagName)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        this.tagName = trimmedTagName;
    }

    /**
     * Returns true if a given string is a valid tag name.
     * A valid tag name must be 1-30 characters long and match the validation regex.
     */
    public static boolean isValidTagName(String test) {

        requireNonNull(test);

        if (test.isEmpty() || test.length() > MAX_LENGTH) {
            return false;
        }
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return '[' + tagName + ']';
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ClassTag)) {
            return false;
        }

        ClassTag otherTag = (ClassTag) other;
        return tagName.equalsIgnoreCase(otherTag.tagName);
    }

    @Override
    public int hashCode() {
        return tagName.toLowerCase().hashCode();
    }

}
