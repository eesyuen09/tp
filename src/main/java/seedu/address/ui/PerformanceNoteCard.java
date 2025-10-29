package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.performance.PerformanceNote;

/**
 * An UI component that displays information of a {@code PerformanceNote}.
 */
public class PerformanceNoteCard extends UiPart<Region> {

    private static final String FXML = "PerformanceNoteCard.fxml";

    public final PerformanceNote note;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label date;
    @FXML
    private Label classTag;
    @FXML
    private Label content;

    /**
     * Creates a {@code PerformanceNoteCard} with the given {@code PerformanceNote} and index to display.
     */
    public PerformanceNoteCard(PerformanceNote note, int displayedIndex) {
        super(FXML);
        this.note = note;
        id.setText(displayedIndex + ".");
        date.setText(note.getDate().getFormattedDate());
        classTag.setText(note.getClassTag().toString());
        content.setText(note.getNote());
    }
}
