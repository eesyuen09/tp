package seedu.address.model.person.exceptions;

/**
 * Signals that the maximum number of student IDs has been reached.
 */
public class ExceedMaxStudentsException extends RuntimeException {
    public ExceedMaxStudentsException() {
        super("Cannot add more students as the maximum limit of 9999 has been reached.");
    }
}
