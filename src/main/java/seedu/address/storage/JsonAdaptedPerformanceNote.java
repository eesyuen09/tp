package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Date;
import seedu.address.model.person.performance.PerformanceNote;
import seedu.address.model.tag.ClassTag;

/**
 * Jackson-friendly version of {@link PerformanceNote}.
 */
public class JsonAdaptedPerformanceNote {
    private final String date; // ddMMyyyy
    private final String classTag;
    private final String note;

    /**
     * Constructs a {@code JsonAdaptedPerformanceNote} with the given performance note details.
     *
     * @param date the date of the performance note
     * @param classTag the class tag of the performance note
     * @param note the content of the performance note
     */
    @JsonCreator
    public JsonAdaptedPerformanceNote(@JsonProperty("date") String date,
                                      @JsonProperty("classTag") String classTag,
                                      @JsonProperty("note") String note) {
        this.date = date;
        this.classTag = classTag;
        this.note = note;
    }

    /**
     * Converts a given {@code PerformanceNote} into this class for Jackson use.
     *
     * @param src the PerformanceNote to be converted
     */
    public JsonAdaptedPerformanceNote(PerformanceNote src) {
        this.date = src.getDate().toString();
        this.classTag = src.getClassTag().tagName;
        this.note = src.getNote();
    }

    /**
     * Converts this Jackson-friendly adapted performance note object into the model's {@code PerformanceNote} object.
     *
     * @return the PerformanceNote object
     */
    public PerformanceNote toModelType() throws IllegalValueException {
        if (date == null) {
            throw new IllegalValueException("Performance note's date cannot be null!");
        }
        if (!Date.isValidDate(date)) {
            throw new IllegalValueException(Date.MESSAGE_CONSTRAINTS);
        }
        if (classTag == null) {
            throw new IllegalValueException("Performance note's class tag cannot be null!");
        }
        if (!ClassTag.isValidTagName(classTag)) {
            throw new IllegalValueException(ClassTag.MESSAGE_CONSTRAINTS);
        }
        if (note == null) {
            throw new IllegalValueException("Performance note cannot be null!");
        }
        return new PerformanceNote(new Date(date), new ClassTag(classTag), note);
    }
}
