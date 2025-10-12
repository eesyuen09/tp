package seedu.address.model.person.exceptions;

/**
 * Signals that the maximum number of student IDs has been reached.
 */
public class ExceedMaxStudentsException extends RuntimeException {
    public ExceedMaxStudentsException() {
        super("Maximum number of student IDs reached");
    }
}
