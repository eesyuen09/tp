package seedu.address.logic.commands.performance;

public class PerfNotes {
    private PerfNotes() {}

    public static final String MESSAGE_USAGE =
            "perf -a s/STUDENT_ID d/DDMMYYYY pn/NOTE | "
                    + "perf -v s/STUDENT_ID | "
                    + "perf -e s/STUDENT_ID i/INDEX pn/NOTE | "
                    + "perf -d s/STUDENT_ID i/INDEX";

    public static final String STUDENT_NOT_FOUND = "Error: STUDENT_ID does not exist.";
    public static final String ADDED   = "Performance note successfully added for %s on %s.";
    public static final String EDITED  = "Performance note %d successfully edited for %s.";
    public static final String DELETED = "Performance note %d successfully deleted for %s.";
}
