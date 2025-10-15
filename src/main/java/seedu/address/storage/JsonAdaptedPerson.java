package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Attendance;
import seedu.address.model.person.Email;
import seedu.address.model.person.Month;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.ClassTag;


import seedu.address.model.person.performance.PerformanceList;
import seedu.address.model.person.performance.PerformanceNote;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String studentId;
    private final List<JsonAdaptedClassTag> tags = new ArrayList<>();
    private final String enrolledMonth;
    private final List<JsonAdaptedAttendance> attendanceRecords = new ArrayList<>();
    private final List<JsonAdaptedPerformanceNote> performanceNotes = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("address") String address,
            @JsonProperty("tags") List<JsonAdaptedClassTag> tags, @JsonProperty("studentId") String studentId,
                             @JsonProperty("enrolledMonth") String enrolledMonth,
                             @JsonProperty("attendanceRecords") List<JsonAdaptedAttendance> attendanceRecords,
                             @JsonProperty("performanceNotes") List<JsonAdaptedPerformanceNote> performanceNotes) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.studentId = studentId;
        this.enrolledMonth = enrolledMonth;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (performanceNotes != null) {
            this.performanceNotes.addAll(performanceNotes);
        }
        if (attendanceRecords != null) {
            this.attendanceRecords.addAll(attendanceRecords);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        studentId = source.getStudentId().toString();
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        enrolledMonth = source.getEnrolledMonth().toString();
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedClassTag::new)
                .collect(Collectors.toList()));
        source.getPerformanceList().asUnmodifiableList().forEach(n ->
                performanceNotes.add(new JsonAdaptedPerformanceNote(n)));

        attendanceRecords.addAll(source.getAttendanceRecords().stream()
                .map(JsonAdaptedAttendance::new)
                .collect(Collectors.toList()));

    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<ClassTag> personTags = new ArrayList<>();
        for (JsonAdaptedClassTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        final List<Attendance> personAttendance = new ArrayList<>();
        for (JsonAdaptedAttendance attendance : attendanceRecords) {
            personAttendance.add(attendance.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        if (studentId == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    StudentId.class.getSimpleName()));
        }
        if (!StudentId.isValidStudentId(studentId)) {
            throw new IllegalValueException(StudentId.MESSAGE_CONSTRAINTS);
        }

        final StudentId modelStudentId = new StudentId(studentId);
        final Set<Attendance> modelAttendanceRecords = new HashSet<>(personAttendance);

        final Month modelEnrolledMonth = (enrolledMonth != null && Month.isValidMonth(enrolledMonth))
            ? new Month(enrolledMonth)
            : Month.now();

        final Set<ClassTag> modelTags = new HashSet<>(personTags);

        final java.util.List<PerformanceNote> modelPerformanceNotes = new ArrayList<>();
        for (JsonAdaptedPerformanceNote note : performanceNotes) {
            modelPerformanceNotes.add(note.toModelType());
        }

        final PerformanceList modelPerformanceList = new PerformanceList(modelPerformanceNotes);

        return new Person(modelName, modelPhone, modelEmail, modelAddress, modelTags, modelStudentId,
                modelEnrolledMonth, modelAttendanceRecords, modelPerformanceList);
    }

}
