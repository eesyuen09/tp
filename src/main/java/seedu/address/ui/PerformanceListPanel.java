// src/main/java/seedu/address/ui/PerformanceListPanel.java
package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.performance.PerformanceNote;

import java.util.logging.Logger;

public class PerformanceListPanel extends UiPart<Region> {
    private static final String FXML = "PerformanceListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private ListView<PerformanceNote> performanceListView;

    public PerformanceListPanel(ObservableList<PerformanceNote> performanceNotes) {
        super(FXML);
        performanceListView.setItems(performanceNotes);
        performanceListView.setCellFactory(listView -> new PerformanceNoteCell());
    }

    static class PerformanceNoteCell extends ListCell<PerformanceNote> {
        @Override
        protected void updateItem(PerformanceNote item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item.printableDate() + " — " + item.getNote());
            }
        }
    }
}
