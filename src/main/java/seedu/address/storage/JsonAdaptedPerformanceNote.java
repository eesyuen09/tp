package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.model.person.performance.PerformanceNote;

/**
 * Jackson-friendly version of {@link PerformanceNote}.
 */
public class JsonAdaptedPerformanceNote {
    private final String date; // ddMMyyyy
    private final String note;

    @JsonCreator
    public JsonAdaptedPerformanceNote(@JsonProperty("date") String date,
                                      @JsonProperty("note") String note) {
        this.date = date;
        this.note = note;
    }

    /**
     * Converts a given {@code PerformanceNote} into this class for Jackson use.
     *
     * @param src the PerformanceNote to be converted
     */
    public JsonAdaptedPerformanceNote(PerformanceNote src) {
        this.date = src.getDate().format(java.time.format.DateTimeFormatter.ofPattern("ddMMyyyy"));
        this.note = src.getNote();
    }

    /**
     * Converts this Jackson-friendly adapted performance note object into the model's {@code PerformanceNote} object.
     *
     * @return the PerformanceNote object
     */
    public PerformanceNote toModelType() {
        return new PerformanceNote(date, note);
    }
}
