package seedu.address.model.tag.exceptions;

/**
 * Signals that the operation will result in duplicate ClassTags. (Class Tags are considered duplicates if they
 * have same tag name.
 */
public class DuplicateClassTagException extends RuntimeException {
    public DuplicateClassTagException() {
        super("Operation would result in duplicate class tags");
    }
}
