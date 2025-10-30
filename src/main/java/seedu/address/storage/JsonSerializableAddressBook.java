package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.fee.FeeTracker;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.ClassTag;
import seedu.address.model.time.Month;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";
    public static final String MESSAGE_DUPLICATE_CLASSTAG = "ClassTags list contains duplicate class tag(s).";
    public static final String MESSAGE_DUPLICATE_FEERECORD =
        "Fee records contain duplicate entries for the same studentId and month.";


    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedClassTag> classTags = new ArrayList<>();
    private final List<JsonAdaptedFeeRecord> feeRecords = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
                                       @JsonProperty("classTags") List<JsonAdaptedClassTag> classTags,
                                       @JsonProperty("feeRecords") List<JsonAdaptedFeeRecord> feeRecords) {
        this.persons.addAll(persons);
        if (classTags != null) {
            this.classTags.addAll(classTags);
        }
        if (feeRecords != null) {
            this.feeRecords.addAll(feeRecords);
        }
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
        classTags.addAll(source.getClassTagList().stream().map(JsonAdaptedClassTag::new).collect(Collectors.toList()));
        FeeTracker feeTracker = source.getFeeTracker();

        for (Person person : source.getPersonList()) {
            Month enrolledMonth = person.getEnrolledMonth();
            if (enrolledMonth == null) {
                continue;
            }

            Month latestMonth = Month.now();
            Month currentMonth = enrolledMonth;

            while (currentMonth.isBefore(latestMonth) || currentMonth.equals(latestMonth)) {
                final Month monthSnapshot = currentMonth;
                final StudentId studentIdSnapshot = person.getStudentId();

                feeTracker.getExplicitStatusOfMonth(studentIdSnapshot, monthSnapshot)
                    .ifPresent(feeState -> feeRecords.add(
                        new JsonAdaptedFeeRecord(studentIdSnapshot, monthSnapshot, feeState)));

                currentMonth = currentMonth.plusMonths(1);
            }
        }
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();

        List<ClassTag> validClassTags = loadClassTags(addressBook);
        loadPersons(addressBook, validClassTags);
        loadFeeRecords(addressBook);

        return addressBook;
    }

    /**
     * Loads class tags into the address book.
     *
     * @param addressBook the address book to load class tags into
     * @return list of valid class tags that were loaded
     * @throws IllegalValueException if there are duplicate class tags
     */
    private List<ClassTag> loadClassTags(AddressBook addressBook) throws IllegalValueException {
        List<ClassTag> validClassTags = new ArrayList<>();
        for (JsonAdaptedClassTag jsonAdaptedClassTag : classTags) {
            ClassTag classTag = jsonAdaptedClassTag.toModelType();
            if (addressBook.hasClassTag(classTag)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_CLASSTAG);
            }
            addressBook.addClassTag(classTag);
            validClassTags.add(classTag);
        }
        return validClassTags;
    }

    /**
     * Loads persons into the address book.
     *
     * @param addressBook the address book to load persons into
     * @param validClassTags list of valid class tags for validation
     * @throws IllegalValueException if there are duplicate persons
     */
    private void loadPersons(AddressBook addressBook, List<ClassTag> validClassTags)
            throws IllegalValueException {
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType(validClassTags);
            if (addressBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addPerson(person);
        }
    }

    /**
     * Loads fee records into the address book's fee tracker.
     *
     * @param addressBook the address book whose fee tracker will receive the records
     * @throws IllegalValueException if there are duplicate fee records
     */
    private void loadFeeRecords(AddressBook addressBook) throws IllegalValueException {
        Set<String> uniqueRecordKeys = new HashSet<>();
        for (JsonAdaptedFeeRecord record : feeRecords) {
            String key = record.getStudentId() + "#" + record.getMonthString();
            if (!uniqueRecordKeys.add(key)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_FEERECORD + " Offender: " + key);
            }
            record.applyToFeeTracker(addressBook);
        }
    }

}
