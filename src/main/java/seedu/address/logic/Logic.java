package seedu.address.logic;

import java.nio.file.Path;
import java.util.Optional;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.fee.FeeHistoryEntry;
import seedu.address.model.fee.FeeHistorySummary;
import seedu.address.model.fee.FeeState;
import seedu.address.model.person.Person;
import seedu.address.model.person.performance.PerformanceNote;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns the AddressBook.
     *
     * @see seedu.address.model.Model#getAddressBook()
     */
    ReadOnlyAddressBook getAddressBook();

    /** Returns an unmodifiable view of the filtered list of persons */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    Optional<FeeState> getCurrentFeeState(Person person);

    /** Returns an unmodifiable view of the performance notes currently displayed. */
    ObservableList<PerformanceNote> getDisplayedPerformanceNotes();

    /** Returns an unmodifiable view of the fee history currently displayed. */
    ObservableList<FeeHistoryEntry> getDisplayedFeeHistory();

    /** Returns metadata describing the current fee history selection. */
    ReadOnlyObjectProperty<FeeHistorySummary> feeHistorySummaryProperty();

    ReadOnlyIntegerProperty feeStateVersionProperty();
}
