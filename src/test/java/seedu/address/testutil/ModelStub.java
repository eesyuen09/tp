package seedu.address.testutil;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.fee.FeeState;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.performance.PerformanceNote;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Date;
import seedu.address.model.time.Month;

/**
 * A default model stub that has all of the methods failing.
 * This is meant to be extended by other specific Model Stubs for testing.
 */
public class ModelStub implements Model {
    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public GuiSettings getGuiSettings() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Path getAddressBookFilePath() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasPerson(Person person) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deletePerson(Person target) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addPerson(Person person) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasClassTag(ClassTag classTag) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addClassTag(ClassTag classTag) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deleteClassTag(ClassTag classTag) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Optional<ClassTag> findClassTag(ClassTag classTag) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Optional<Person> getPersonById(StudentId studentId) {
        throw new AssertionError("This method should not be called.");
    }
    @Override
    public Predicate<Person> paidStudents(Month month) {
        throw new AssertionError("This method should not be called.");
    }
    @Override
    public Predicate<Person> unpaidStudents(Month month) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void markPaid(StudentId studentId, Month month) {
        throw new AssertionError("This method should not be called.");
    }
    @Override
    public void markUnpaid(StudentId studentId, Month month) {
        throw new AssertionError("This method should not be called.");
    }
    @Override
    public void markAttendance(StudentId studentId, Date date, ClassTag classTag) {
        throw new AssertionError("This method should not be called.");
    }
    @Override
    public void unmarkAttendance(StudentId studentId, Date date, ClassTag classTag) {
        throw new AssertionError("This method should not be called.");
    }
    @Override
    public Optional<FeeState> getCurrentFeeState(Person person) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasPersonWithId(StudentId studentId) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<PerformanceNote> getDisplayedPerformanceNotes() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setDisplayedPerformanceNotes(List<PerformanceNote> notes) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void clearDisplayedPerformanceNotes() {
        throw new AssertionError("This method should not be called.");
    }
}

