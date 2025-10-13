
package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Person;


/**
 * An UI component that displays attendance information of a {@code Person}.
 */

public class AttendanceCard extends UiPart<Region>{

    private static final String FXML = "AttendanceCard.fxml";

    public final Person person;

    @FXML
    private VBox attendancePane;
    @FXML
    private Label header;
    @FXML
    private Label attendanceRecord;

    /**
     * Creates an {@code AttendanceCard} with the given {@code Person} to display attendance history.
     */
    public AttendanceCard(Person person) {
        super(FXML);
        this.person = person;
        header.setText("Attendance history for " + person.getName() + " (" + person.getStudentId() + ")");
        //Display list here
        attendanceRecord.setText(person.getAttendanceRecords().toString());
    }

}

