package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Attendance;
import seedu.address.model.person.Date;

/**
 * Jackson-friendly version of {@link Attendance}.
 */
class JsonAdaptedAttendance {

    private final String date;
    private final boolean isPresent;

    /**
     * Constructs a {@code JsonAdaptedAttendance} with the given attendance details.
     */
    @JsonCreator
    public JsonAdaptedAttendance(@JsonProperty("date") String date,
                                 @JsonProperty("isPresent") boolean isPresent) {
        this.date = date;
        this.isPresent = isPresent;
    }

    /**
     * Converts a given {@code Attendance} into this class for Jackson use.
     */
    public JsonAdaptedAttendance(Attendance source) {
        date = source.getDate().toString();
        isPresent = source.isStudentPresent();
    }

    /**
     * Converts this Jackson-friendly adapted attendance object into the model's {@code Attendance} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public Attendance toModelType() throws IllegalValueException {
        if (date == null) {
            throw new IllegalValueException("Attendance date cannot be null!");
        }
        if (!Date.isValidDate(date)) {
            throw new IllegalValueException(Date.MESSAGE_CONSTRAINTS);
        }
        final Date modelDate = new Date(date);
        return new Attendance(modelDate, isPresent);
    }
}
