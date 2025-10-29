package seedu.address.ui;

import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.fee.FeeState;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private ListView<Person> personListView;

    private final Function<Person, Optional<FeeState>> currentFeeStateGetter;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Person> personList, Function<Person,
        Optional<FeeState>> currentFeeStateGetter) {
        super(FXML);
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new PersonListViewCell());
        this.currentFeeStateGetter = currentFeeStateGetter;
    }

    /**
     * Binds the fee state version property to this panel.
     * When the fee state version changes (e.g. after marking a student as paid or unpaid),
     * the person list view is refreshed automatically to show the latest fee status.
     *
     * @param feeStateVersion the read-only property that updates whenever fee states change
     */
    public void bindFeeStateVersion(ReadOnlyIntegerProperty feeStateVersion) {
        feeStateVersion.addListener((obs, oldVal, newVal) -> {
            personListView.refresh();
        });
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(person, getIndex() + 1, currentFeeStateGetter).getRoot());
            }
        }
    }

}
