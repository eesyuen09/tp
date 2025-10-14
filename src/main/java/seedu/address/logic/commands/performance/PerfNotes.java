package seedu.address.logic.commands.performance;

/**
 * Contains command notes for performance-related commands.
 */
public class PerfNotes {

    /**
     * Usage message for performance notes commands.
     */
    public static final String MESSAGE_USAGE =
            "perf -a s/STUDENT_ID d/DDMMYYYY pn/NOTE | "
                    + "perf -v s/STUDENT_ID | "
                    + "perf -e s/STUDENT_ID i/INDEX pn/NOTE | "
                    + "perf -d s/STUDENT_ID i/INDEX";

    /**
     * Success message for adding a performance note.
     */
    public static final String ADDED = "Performance note successfully added for %s on %s.";

    /**
     * Success message for deleting a performance note.
     */
    public static final String DELETED = "Performance note %d successfully deleted for %s.";

    /**
     * Success message for editing a performance note.
     */
    public static final String EDITED = "Performance note %d successfully edited for %s.";

    /**
     * Error message when student with given ID is not found.
     */
    public static final String STUDENT_NOT_FOUND = "Error: STUDENT_ID does not exist.";

    private PerfNotes() {}
}
