package seedu.address.model;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.attendance.AttendanceHistoryEntry;
import seedu.address.model.attendance.AttendanceHistorySummary;
import seedu.address.model.fee.FeeHistoryEntry;
import seedu.address.model.fee.FeeHistorySummary;
import seedu.address.model.fee.FeeState;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.performance.PerformanceNote;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Date;
import seedu.address.model.time.Month;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    public javafx.beans.property.ReadOnlyIntegerProperty feeStateVersionProperty();

    /**
     * Retrieves a {@link Person} from the filtered list by their {@link StudentId}.
     *
     */
    Optional<Person> getPersonById(StudentId studentId);

    /**
     * Returns true if a class tag with the same name as {@code classTag} exists in the address book.
     */
    boolean hasClassTag(ClassTag toAdd);

    /**
     * Adds the given class tag.
     * {@code classTag} must not already exist in the address book.
     */
    void addClassTag(ClassTag toAdd);

    /**
     * Deletes the given class tag.
     * The class tag must exist.
     */
    void deleteClassTag(ClassTag toDelete);

    /**
     * Finds and returns the existing ClassTag that matches the given tag case-insensitively.
     *
     * @param classTag The ClassTag (potentially with user-input casing) to search for.
     * @return An Optional containing the existing ClassTag with its original casing if found,
     *         otherwise Optional.empty().
     */
    Optional<ClassTag> findClassTag(ClassTag classTag);

    /**
     * Returns true if the given class tag is assigned to any person in the address book.
     */
    boolean isClassTagInUse(ClassTag classTag);

    /**
     * Returns an unmodifiable view of the class tag list.
     */
    List<ClassTag> getClassTagList();

    /**
     * Returns {@code true} if the address book contains a {@code Person} with the specified {@code StudentId}.
     */
    boolean hasPersonWithId(StudentId studentId);
    /**
     * Marks the given student as PAID for the given month.
     * The student must already exist in the address book.
     */
    void markPaid(StudentId studentId, Month month);

    /**
     * Marks the given student as UNPAID for the given month.
     * The student must already exist in the address book.
     */
    void markUnpaid(StudentId studentId, Month month);

    Predicate<Person> paidStudents(Month month);

    Predicate<Person> unpaidStudents(Month month);

    Optional<FeeState> getCurrentFeeState(Person person);

    /**
     * Marks the given student as present on the given date for a specific class.
     * The student must already exist in the address book.
     *
     * @param studentId The student ID of the student to mark attendance for.
     * @param date The date to mark attendance on.
     * @param classTag The class tag for the attendance.
     */
    void markAttendance(StudentId studentId, Date date, ClassTag classTag);

    /**
     * Marks the given student as absent on the given date for a specific class.
     * The student must already exist in the address book.
     *
     * @param studentId The student ID of the student to unmark attendance for.
     * @param date The date to unmark attendance on.
     * @param classTag The class tag for the attendance.
     */
    void unmarkAttendance(StudentId studentId, Date date, ClassTag classTag);

    /**
     * Deletes the attendance record for the given student on the given date for a specific class.
     * The student must already exist in the address book.
     *
     * @param studentId The student ID of the student to delete attendance for.
     * @param date The date to delete attendance on.
     * @param classTag The class tag for the attendance.
     */
    void deleteAttendance(StudentId studentId, Date date, ClassTag classTag);

    /** Returns an unmodifiable view of the performance notes currently displayed in the UI. */
    ObservableList<PerformanceNote> getDisplayedPerformanceNotes();

    /** Replaces the currently displayed performance notes with {@code notes}. */
    void setDisplayedPerformanceNotes(List<PerformanceNote> notes);

    /** Clears all performance notes currently displayed in the UI. */
    void clearDisplayedPerformanceNotes();

    /** Returns an unmodifiable view of the fee history rows currently displayed in the UI. */
    ObservableList<FeeHistoryEntry> getDisplayedFeeHistory();

    /** Replaces the currently displayed fee history with {@code entries} and {@code summary}. */
    void setDisplayedFeeHistory(List<FeeHistoryEntry> entries, FeeHistorySummary summary);

    /** Clears all fee history data currently displayed in the UI. */
    void clearDisplayedFeeHistory();

    javafx.beans.property.ReadOnlyObjectProperty<FeeHistorySummary> feeHistorySummaryProperty();

    /** Returns an unmodifiable view of the attendance history rows currently displayed in the UI. */
    ObservableList<AttendanceHistoryEntry> getDisplayedAttendanceHistory();

    /** Replaces the currently displayed attendance history with {@code entries} and {@code summary}. */
    void setDisplayedAttendanceHistory(List<AttendanceHistoryEntry> entries, AttendanceHistorySummary summary);

    /** Clears all attendance history data currently displayed in the UI. */
    void clearDisplayedAttendanceHistory();

    javafx.beans.property.ReadOnlyObjectProperty<AttendanceHistorySummary> attendanceHistorySummaryProperty();

}
