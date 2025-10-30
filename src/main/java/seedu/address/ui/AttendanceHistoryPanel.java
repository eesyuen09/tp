package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import seedu.address.model.attendance.AttendanceHistoryEntry;
import seedu.address.model.attendance.AttendanceHistorySummary;

/**
 * Panel showing a student's attendance history.
 */
public class AttendanceHistoryPanel extends UiPart<Region> {

    private static final String FXML = "AttendanceHistoryPanel.fxml";

    @FXML
    private Label titleLabel;

    @FXML
    private Label subtitleLabel;

    @FXML
    private TableView<AttendanceHistoryEntry> historyTable;

    @FXML
    private TableColumn<AttendanceHistoryEntry, String> dateColumn;

    @FXML
    private TableColumn<AttendanceHistoryEntry, String> classColumn;

    @FXML
    private TableColumn<AttendanceHistoryEntry, String> statusColumn;

    /**
     * Constructs an {@code AttendanceHistoryPanel} backed by the given observable data.
     */
    public AttendanceHistoryPanel(ObservableList<AttendanceHistoryEntry> history,
                                  ReadOnlyObjectProperty<AttendanceHistorySummary> summaryProperty) {
        super(FXML);
        requireNonNull(history);
        requireNonNull(summaryProperty);

        historyTable.setItems(history);
        historyTable.setPlaceholder(new Label("No attendance records to display"));

        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate().getFormattedDate()));
        classColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getClassTag().tagName));
        statusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatusLabel()));

        summaryProperty.addListener((observable, oldSummary, newSummary) -> updateSummary(newSummary));
        updateSummary(summaryProperty.getValue());
    }

    private void updateSummary(AttendanceHistorySummary summary) {
        if (summary == null) {
            titleLabel.setText("Attendance history");
            subtitleLabel.setText("Enter attendance -v to display a student's attendance history.");
            return;
        }

        titleLabel.setText(String.format("Attendance history for %s (Student ID %s)",
                summary.getStudentName(), summary.getStudentId()));

        int count = summary.getRecordCount();
        String recordLabel = count == 1 ? "record" : "records";
        subtitleLabel.setText(String.format("%s — %s · %d %s",
                summary.getStartDate().getFormattedDate(),
                summary.getEndDate().getFormattedDate(),
                count,
                recordLabel));
    }
}
