package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.Locale;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import seedu.address.model.fee.FeeHistoryEntry;
import seedu.address.model.fee.FeeHistorySummary;

/**
 * Panel showing a student's fee payment history.
 */
public class FeeHistoryPanel extends UiPart<Region> {

    private static final String FXML = "FeeHistoryPanel.fxml";

    @FXML
    private Label titleLabel;

    @FXML
    private Label subtitleLabel;

    @FXML
    private TableView<FeeHistoryEntry> historyTable;

    @FXML
    private TableColumn<FeeHistoryEntry, String> monthColumn;

    @FXML
    private TableColumn<FeeHistoryEntry, String> statusColumn;


    /**
     * Constructs a {@code FeeHistoryPanel} backed by the given observable data.
     */
    public FeeHistoryPanel(ObservableList<FeeHistoryEntry> history,
                           ReadOnlyObjectProperty<FeeHistorySummary> summaryProperty) {
        super(FXML);
        requireNonNull(history);
        requireNonNull(summaryProperty);

        historyTable.setItems(history);
        historyTable.setPlaceholder(new Label("No payment history to display"));

        monthColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMonth().toHumanReadable()));
        statusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatStatus(cellData.getValue())));

        summaryProperty.addListener((observable, oldSummary, newSummary) -> updateSummary(newSummary));
        updateSummary(summaryProperty.getValue());
    }

    private void updateSummary(FeeHistorySummary summary) {
        if (summary == null) {
            titleLabel.setText("Payment history");
            subtitleLabel.setText("Enter fee -v to display a student's payment history.");
            return;
        }

        titleLabel.setText(String.format("Payment history for %s (Student ID %s)",
                summary.getStudentName(), summary.getStudentId()));

        int count = summary.getMonthCount();
        String monthLabel = count == 1 ? "month" : "months";
        subtitleLabel.setText(String.format("%s — %s · %d %s · Enrolled in %s",
                summary.getStartMonth().toHumanReadable(),
                summary.getEndMonth().toHumanReadable(),
                count,
                monthLabel,
                summary.getEnrolledMonth().toHumanReadable()));
    }

    private String formatStatus(FeeHistoryEntry entry) {
        String lower = entry.getState().name().toLowerCase(Locale.ENGLISH);
        String capitalized = Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
        return String.format("%s (%s)", capitalized, entry.getSourceLabel());
    }
}
