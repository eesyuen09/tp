package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Month;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.fee.FeeState;
import seedu.address.model.fee.FeeTracker;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FeeTracker feeTracker = new FeeTracker();

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);
    }

    @Override
    public boolean hasClassTag(ClassTag classTag) {
        requireNonNull(classTag);
        return addressBook.hasClassTag(classTag);
    }

    @Override
    public void addClassTag(ClassTag classTag) {
        addressBook.addClassTag(classTag);
    }

    @Override
    public void deleteClassTag(ClassTag classTag) {
        addressBook.deleteClassTag(classTag);
    }

    @Override
    public void markPaid(StudentId studentId, Month month) {
        requireNonNull(studentId);
        requireNonNull(month);
        feeTracker.markPaid(studentId, month);
    }

    @Override
    public void markUnpaid(StudentId studentId, Month month) {
        requireNonNull(studentId);
        requireNonNull(month);
        feeTracker.markUnpaid(studentId, month);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }


    /**
     * Retrieves a {@link Person} from the filtered list by their {@link StudentId}.
     *
     */
    public Optional<Person> getPersonById(StudentId studentId) {
        requireNonNull(studentId);
        return this.addressBook.getPersonList().stream()
                .filter(p -> p.getStudentId().equals(studentId)).findFirst();
    }

    /**
     * Returns true if a person with the given {@code studentId} exists.
     */
    public boolean hasStudentId(StudentId studentId) {
        return getPersonById(studentId).isPresent();
    }

    @Override
    public Predicate<Person> paidStudents(Month month) {
        requireNonNull(month);
        return p -> feeTracker.getDerivedStatusofMonth(p, month).orElse(null) == FeeState.PAID;
    }

    @Override
    public Predicate<Person> unpaidStudents(Month month) {
        requireNonNull(month);
        return p -> feeTracker.getDerivedStatusofMonth(p, month).orElse(null) == FeeState.UNPAID;
    }


}
