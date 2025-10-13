package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import seedu.address.model.person.performance.PerformanceNote;

public class JsonAdaptedPerformanceNote {
    private final String date; // ddMMyyyy
    private final String note;

    @JsonCreator
    public JsonAdaptedPerformanceNote(@JsonProperty("date") String date,
                                      @JsonProperty("note") String note) {
        this.date = date; this.note = note;
    }

    public JsonAdaptedPerformanceNote(PerformanceNote src) {
        this.date = src.getDate().format(java.time.format.DateTimeFormatter.ofPattern("ddMMyyyy"));
        this.note = src.getNote();
    }

    public PerformanceNote toModelType() {
        return new PerformanceNote(date, note);
    }
}
