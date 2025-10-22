package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.performance.PerformanceNote;

/**
 * Panel containing the list of performance notes for a student.
 */
public class PerformanceListPanel extends UiPart<Region> {
    private static final String FXML = "PerformanceListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PerformanceListPanel.class);

    @FXML
    private ListView<PerformanceNote> performanceListView;

    /**
     * Creates a {@code PerformanceListPanel} with the given list of performance notes.
     *
     * @param notes The list of performance notes to display.
     */
    public PerformanceListPanel(ObservableList<PerformanceNote> notes) {
        super(FXML);
        performanceListView.setItems(notes);
        performanceListView.setCellFactory(listView -> new PerformanceListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code PerformanceNote}
     * using a {@code PerformanceNoteCard}.
     */
    class PerformanceListViewCell extends ListCell<PerformanceNote> {
        @Override
        protected void updateItem(PerformanceNote note, boolean empty) {
            super.updateItem(note, empty);

            if (empty || note == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PerformanceNoteCard(note, getIndex() + 1).getRoot());
            }
        }
    }
}
