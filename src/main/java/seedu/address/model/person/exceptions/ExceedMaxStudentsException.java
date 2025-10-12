package seedu.address.model.person.exceptions;

public class ExceedMaxStudentsException extends RuntimeException {
    public ExceedMaxStudentsException() {
        super("Maximum number of student IDs reached");
    }
}
