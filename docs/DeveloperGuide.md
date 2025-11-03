---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# Tuto Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**
    
We follow the project design and documentation structure of the AddressBook-Level3 project by SE-EDU.   
In addition, the structure and certain sections of this Developer Guide were inspired by and adapted from [a team project developed by a previous cohort](https://ay2425s1-cs2103t-w08-1.github.io/tp/).
We used GitHub Copilot as an auto-complete tool, primarily to assist in writing boilerplate code and suggest code completions. All generated code was reviewed and verified by the team before inclusion.

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

<box type="warning" seamless>
<b>Warning:</b>
For CS2103T practical exam purpose, the filename of the jar file might be different from the file name given in UG, e.g.: [CS2103T-F10-3][Tuto].jar
<br>
For MacOS and Linux users, use the command, java -jar \[CS2103T-F10-3\]\[Tuto\].jar to execute the jar file during PE if necessary.
</box>

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete s/0000`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.)

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete s/0002")` API call as an example.

<puml src="diagrams/DeleteStudentSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete s/0002` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

<box type="info" seamless>

**Note on Extra Parameters:** When users provide extra parameters beyond what a command expects, the parser's `ArgumentTokenizer` treats any unrecognized text (including invalid parameter prefixes) as part of the value of the parameter that precedes it. This means that if an invalid or unrecognized parameter is added after a valid parameter, the validation error message will reference the valid parameter that comes immediately before the invalid input, rather than indicating that extra parameters were provided. Developers should be aware of this behavior when designing commands and crafting error messages, as it may affect how validation errors are communicated to users.

</box>

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores a list of `ClassTag` objects (which are contained in a `UniqueClassTagList` object).
* manages fee-related data through the `FeeTracker`, which records each student's monthly `FeeState` (`PAID` or `UNPAID`) and supports derived payment status queries.
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml"  />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)
* manages serialization of complex model data as part of the Address Book:
    * The `UniqueClassTagList` is stored within the Address Book JSON file.  
      Conversion is handled by `JsonAdaptedClassTag`, and `JsonSerializableAddressBook` includes the `classTags` list during serialization and deserialization.
    * Each `Person` is serialized using `JsonAdaptedPerson`, which in turn serializes:
        * `JsonAdaptedAttendance` — attendance data
        * `JsonAdaptedFeeRecord` — fee payment records
        * `JsonAdaptedPerformanceNote` — performance-related notes
        * and associated class tags (`JsonAdaptedClassTag`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

<box type= "info" seamless>

Please note that certain aspects, such as UML classes, may have been simplified to fit within the diagram's constraints and maintain readability.

</box>

### Student Management

#### Overview

The Student Management feature allows tutors to manage student records efficiently.  
Key operations include **adding, editing, and deleting students**.

Each student is uniquely identified by a **Student ID** and can have associated attributes such as **name, phone, email, address, and ClassTags**.

#### Implementation

**Model Component:**
- `Person`: Represents a student with immutable fields:
    - Name, Phone, Email, Address, ClassTags, StudentId, AttendanceList, EnrolledMonth, PerformanceList
- `StudentId`: Ensures uniqueness of students
- `Model` interface provides methods:
    - `addPerson(Person)`: Adds a student
    - `setPerson(Person, Person)`: Updates a student record
    - `deletePerson(Person)`: Deletes a student
    - `hasPerson(Person)`: Checks for duplicate student
    - `hasPersonWithId(StudentId)`: Checks existence of student by ID
    - `getPersonById(StudentId)`: Retrieves student by ID
- `AttendanceList` and `PerformanceList` maintain attendance and performance records

**Storage Component:**
- `JsonAdaptedPerson`: Converts Person objects to/from JSON
- `JsonSerializableAddressBook`: Serializes the student list along with ClassTag references
- Deserialization restores each student’s data, including personal details, ClassTag references, and fee records.

**Logic Component:**

1. **AddCommand (`add`)**: Adds a new student
    - Validates all fields (Name, Phone, Email, Address, EnrolledMonth, ClassTags)  
    - Ensures no duplicate students
    - Checks all ClassTags exist before assignment
    - Automatically assigns a unique Student ID
    - Updates the model with the new student

2. **EditCommand (`edit`)**: Modifies existing student details
    - Identifies student by Student ID
    - Edits any combination of fields (Name, Phone, Email, Address, ClassTags)
    - Validates all input fields and ClassTags
    - Empty ClassTag list (`t/`) removes all ClassTag assignments
    - Prevents duplicate records after editing

3. **DeleteCommand (`delete`)**: Removes a student
    - Identifies student by Student ID
    - Ensures the student exists before deletion
    - Updates model to remove the student
   
4. **ListCommand (`list`)**: Displays all students
    - Clears any active filters from previous `find` commands
    - Shows the complete list of students stored in the model

5. **FindCommand (`find`)**: Searches for students by keywords
    - Supports keyword-based matching on Name field
    - Case-insensitive search
    - Displays a filtered list of matching students

6. **ClearCommand (`clear`)**: Clears all student records and class tags
    - Removes all students and class tags from the model

#### Activity Diagram: Adding a Student (`add`)

This activity diagram shows the process flow when a user adds a new student using the `add` command, including validation, duplication checks, and successful addition to the system.

<puml src="diagrams/AddStudentActivityDiagram.puml" alt="AddStudentActivityDiagram" />

#### Sequence Diagram: Editing a Student (`edit`)

This sequence diagram illustrates the interactions between components when a user edits a student’s details using the `edit` command, covering validation, tag resolution, and model update.

<puml src="diagrams/EditStudentSequenceDiagram.puml" alt="EditStudentSequenceDiagram" />

#### Sequence Diagram: Deleting a Student (`delete`)

This sequence diagram demonstrates how the system processes the `delete` command, from identifying the student by ID to removing the student record and returning a success message.

<puml src="diagrams/DeleteStudentSequenceDiagram.puml" alt="DeleteStudentSequenceDiagram" />

#### Design Considerations

**Aspect: Phone Number Validation**

* **Alternative 1 (current choice):** Limit to Singapore numbers (8 digits, starting with 6, 8, or 9)
    * Pros: Ensures only valid Singapore phone numbers are accepted, prevents accidental entry of incorrect numbers
    * Cons: Cannot accept overseas numbers

* **Alternative 2:** Allow any numeric input of 8–10 digits
    * Pros: More flexible, supports overseas or mobile numbers with country codes
    * Cons: Less strict, may allow invalid or mistyped numbers



**Aspect: Name Validation**

* **Alternative 1 (current choice):** Only alphabetic characters, spaces, hyphens(-) , and apostrophes('); max 100 characters
    * Pros: Prevents invalid or malicious input, ensures readability and consistent formatting
    * Cons: Cannot accept names with unusual symbols outside this set

* **Alternative 2:** Allow any Unicode characters, with length limit
    * Pros: Supports all valid names globally
    * Cons: May allow emojis or unexpected symbols, harder to validate and may affect GUI display

**Aspect: Address Validation**

* **Alternative 1 (current choice):** Accepts alphanumeric characters, `#`, `-`, `,`, `'` and spaces
    * Pros: Covers most Singapore addresses while keeping input simple and consistent
    * Cons: Cannot accept exotic symbols or foreign address formats

* **Alternative 2:** Free-text input
    * Pros: Maximum flexibility
    * Cons: Harder to validate and maintain consistent formatting and may cause GUI display issues

**Aspect: Email Validation**

* **Alternative 1 (current choice):** Use detailed format rules — the part before “@” allows letters, numbers, and the symbols `+`, `_`, `.`, and `-`.  
  The domain part after “@” is made up of labels separated by dots, and the last label must be at least 2 characters long.
    * Pros: Ensures proper email format, prevents invalid entries, aligns with common standards
    * Cons: Slightly complex regex, may reject rare valid emails, such as:
      - `john_doe@example.com` (underscore in local part)
      - `"john.doe"@example.com` (quoted local part)
      - `alice+mailbox/department@example.com` (slashes in local part)
      - `a@b.c` (final domain label less than 2 characters)
      - `test@sub_domain.example.com` (underscore in domain label)
      - `üser@exämple.com` (non-ASCII characters)

* **Alternative 2:** Simple regex `.+@.+\..+`
    * Pros: Very permissive, easier to implement
    * Cons: Allows many invalid emails

**Aspect: Enrolled Month Validation and Handling**

* **Alternative 1 (current choice):** Accept input in `MMYY` format only, ensuring the month is valid and not in the future. The enrolled month field is optional — if not provided, it defaults to the current month. When specified, enrollment is assumed to occur on the first day of that month. Once set, the enrolled month cannot be edited afterwards.
    * Pros: Prevents invalid or inconsistent enrolments, ensures every student has a valid month even if omitted, and simplifies data handling.
    * Cons: Does not allow pre-entering future enrolments, assumes first-day enrolment, and requires deletion to correct wrong entries.

* **Alternative 2:** Validate only the `MMYY` format and allow future months; permit editing of the enrolled month after creation.
    * Pros: More flexible for future scheduling and correction of mistakes.
    * Cons: Increases risk of data inconsistency and requires additional logic for validation across linked records.

* **Alternative 3:** Use full date input (`ddMMyy`) instead of `MMYY`, allowing mid-month enrolments.
    * Pros: Provides higher precision for analysis and reporting.
    * Cons: More cumbersome for users and unnecessary if month-level accuracy is sufficient.


**Aspect: ClassTag Validation**

* **Alternative 1 (current choice):** Optional, must exist before assignment
    * Pros: Ensures referential integrity, prevents dangling class references
    * Cons: Adds step to create tag first

* **Alternative 2:** Auto-create class tag if it does not exist
    * Pros: Simplifies user workflow
    * Cons: May create unintended tags, risk of typos creating duplicates

**Aspect: Student ID Assignment**

* **Alternative 1 (current choice):** Auto-generate 4-digit IDs after validating all other fields
    * Pros: Guarantees unique IDs, simple format, avoids hitting maximum limit (9999), no JSON tracking needed
    * Cons: Recycles the highest deleted ID only if it was the latest one and no other add/delete operations occurred before the next app launch

* **Alternative 2:** Store last assigned ID in JSON and always increment
    * Pros: Preserves historical uniqueness across launches
    * Cons: Hits maximum limit faster, extra complexity in JSON handling

* **Alternative 3:** User-specified IDs
    * Pros: Flexibility, users can follow their own numbering system
    * Cons: Higher risk of duplicates and input errors, more complex validation

**Aspect: Non-Duplication of Students**

* **Alternative 1 (current choice):** Uniqueness checked on phone number and name combination
    * Pros: Reduces accidental duplicates by allowing students with the same name but different contact details, preventing false duplicates when siblings share a phone number, and ensuring that neither name nor phone number alone is treated as unique
    * Cons: Cannot detect duplicates with incorrect phone numbers

* **Alternative 2:** Check uniqueness using all fields (name, phone, email, address)
    * Pros: Stronger duplicate detection
    * Cons: More restrictive, may prevent legitimate multiple students with similar details


#### Error Handling

**Adding Students:**
- Invalid command format
- Duplicate student record
- Invalid or missing field(s)
- Non-existent ClassTags
- Maximum number of students exceeded (StudentId > 9999)

**Editing Students:**
- Invalid command format
- Student ID not found
- No fields provided to edit
- Invalid input fields or ClassTags
- Resulting record duplicates an existing student

**Deleting Students:**
- Student ID not found
- Invalid command format

**General Validation Rules:**
- Fields are trimmed and validated according to their respective rules
- Commands reject extra/unrecognized input
- All validation errors provide **clear, actionable feedback** to the user

### Attendance Management

#### Overview

The Attendance Management feature enables tutors to track student attendance across different classes and dates. Each attendance record captures whether a student was present or absent for a specific class on a particular date.

#### Implementation

Attendance management is implemented through several key components:

**Model Component:**
- `Attendance`: Represents a single attendance record with a date, ClassTag reference, and status (Present/Absent)
- `AttendanceList`: Maintains all attendance records for a student
- Each `Person` object contains an `AttendanceList` field that stores their attendance history
- The `Model` interface provides methods to retrieve students and validate ClassTags

**Storage Component:**
- `JsonAdaptedAttendance`: Converts Attendance objects to/from JSON format for persistence
- Attendance records are stored within each student's record in the JSON file
- During deserialization, ClassTag references in attendance records are validated against the system's ClassTag list

**Logic Component:**

The following commands handle attendance operations:

1. **AttendanceMarkPresentCommand (triggered by `att -p`)**: Marks a student as present for a class on a specific date
    - Validates student exists 
    - Checks if student has the ClassTag assigned OR if an attendance record exists for that student, date, and class
    - If student doesn't have the tag and no record exists, rejects the command
    - Prevents duplicate "Present" records (throws error if already marked present)
    - Replaces any existing "Absent" record for the same date and class with a "Present" record

2. **AttendanceMarkAbsentCommand (triggered by `att -a`)**: Marks a student as absent for a class on a specific date
    - Validates student exists 
    - Checks if student has the ClassTag assigned OR if an attendance record exists for that student, date, and class
    - If student doesn't have the tag and no record exists, rejects the command
    - Prevents duplicate "Absent" records (throws error if already marked absent)
    - Replaces any existing "Present" record for the same date and class with an "Absent" record

3. **AttendanceDeleteCommand (triggered by `att -d`)**: Completely removes an attendance record
    - Validates the attendance record exists
    - Removes the record from the student's AttendanceList
    - Useful for correcting mistakes or handling cancelled classes

4. **AttendanceViewCommand (triggered by `att -v`)**: Displays a student's attendance history
    - Retrieves all attendance records for a specific student
    - Records are sorted by date, then by ClassTag name alphabetically

#### Activity Diagram: Attendance Command Workflow

The activity diagram below illustrates the high-level workflow for attendance management, showing how the system routes different attendance operations based on command flags:

<puml src="diagrams/AttendanceActivityDiagram.puml" alt="Attendance Activity Diagram" />

#### Sequence Diagram: Marking Attendance as Present

The following sequence diagram illustrates the interactions when a tutor marks a student as present using the `att -p` command:

<puml src="diagrams/AttendanceMarkPresentSequenceDiagram.puml" alt="Attendance Mark Present Sequence Diagram" />

<box type="info" seamless>

**Note:** The lifeline for `AttendanceCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.

</box>


#### Design Considerations

**Aspect: How attendance data is stored:**

* **Alternative 1 (current choice):** Store attendance within each `Person` object
  * Pros: Natural encapsulation, easy to retrieve all attendance for a specific student, simpler deletion logic (student deletion automatically removes attendance)
  * Cons: Harder to query attendance across all students for a specific date/class, less efficient for class-centric reports

* **Alternative 2:** Store attendance in a centralized `AttendanceBook`
  * Pros: Easier to query by date/class across all students, better for class-level reports
  * Cons: More complex referential integrity, risk of orphaned records, attendance disconnected from student profiles

#### Error Handling

Attendance operations include comprehensive validation:

**Marking Attendance (Present/Absent):**
- Invalid command format
- Student ID not found
- Invalid Student ID format
- ClassTag does not exist
- ClassTag not assigned to the student and no existing attendance record for that student, date, and class
- Date in the future
- Date before student's enrolment month
- Invalid date format (e.g., 30th February, non-existent dates)
- Attempting to mark Present when already marked Present
- Attempting to mark Absent when already marked Absent

**Deleting Attendance:**
- Invalid command format
- Student ID not found
- Invalid Student ID format
- Invalid date format
- Attendance record does not exist for the specified date and class

**Viewing Attendance:**
- Invalid command format
- Student ID not found
- Invalid Student ID format
- No attendance records found for the student (informational message, not an error)

**General Validation Rules:**
- All attendance commands enforce **strict parameter checking** — any extra text beyond the expected format is rejected
- Date validation ensures only real calendar dates are accepted
- ClassTag references are validated against both system-wide ClassTag list and student's assigned tags
- All validation errors provide **clear, actionable feedback** to guide users in correcting their input

### Performance Management

#### Overview

The Performance Management feature lets tutors capture qualitative feedback about a student's progress for specific
classes on particular dates. Each note records the class context, the date of the observation, and a short free-form remark
highlighting achievements or areas for improvement.

#### Implementation

Performance note management is implemented through the following components:

**Model Component:**
- `PerformanceNote`: Represents a single performance entry consisting of a `Date`, `ClassTag`, and note body capped at 200 characters
- `PerformanceList`: Maintains all performance notes for a student, automatically sorting entries by date (ascending) then by class tag name, and preventing duplicate (date, class) pairs
- Each `Person` object embeds a `PerformanceList`, keeping performance history alongside other student attributes
- The `Model` interface exposes helpers to retrieve students, replace updated `Person` instances, and manage the list of performance notes currently shown in the UI (`setDisplayedPerformanceNotes`, `clearDisplayedPerformanceNotes`)

**Storage Component:**
- `JsonAdaptedPerformanceNote`: Serialises/deserializes `PerformanceNote` objects to and from JSON, validating date, class tag, and note length constraints during conversion
- Performance notes are persisted as part of each student's JSON record via `JsonAdaptedPerson`, ensuring notes stay in sync with the owning student
- During deserialization, class tags referenced in performance notes are re-validated against the student's tag set so that orphaned notes cannot be reconstructed

**Logic Component:**

The `perf` command word is a namespace for the following operations:

1. **PerfAddCommand (triggered by `perf -a`)**: Adds a new performance note
    - Validates the student exists and that the specified `ClassTag` belongs to the student
    - Rejects notes dated before the student's enrolment month or in the future
    - Prevents duplicate entries for the same date and class combination
    - Creates a new `PerformanceList` copy, appends the note, re-sorts it, and replaces the student in the model

2. **PerfEditCommand (triggered by `perf -e`)**: Edits an existing performance note's text
    - Validates the student and class tag as per the add command
    - Ensures the targeted note exists before applying the new content and re-validating constraints
    - Persists changes by replacing the student's `PerformanceList` with an updated copy

3. **PerfDeleteCommand (triggered by `perf -d`)**: Removes a performance note entirely
    - Validates the student and class tag and ensures the note exists for the given date/class pair
    - Updates the student's `PerformanceList` without mutating the original list instance

4. **PerfViewCommand (triggered by `perf -v`)**: Displays a student's performance history
    - Retrieves all performance notes for the student and exposes them through `Model#setDisplayedPerformanceNotes`
    - Returns a contextual message indicating whether notes were found, and allows the UI to render the sorted list

#### Sequence Diagram: Adding a Performance Note

The sequence diagram below illustrates the interactions when a tutor adds a performance note using the `perf -a` command:

<puml src="diagrams/PerformanceAddSequenceDiagram.puml" alt="Performance Add Sequence Diagram" />

<box type="info" seamless>

**Note:** The lifeline for `PerfCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the
lifeline continues till the end of diagram.

</box>

#### Design Considerations

**Aspect: Where performance notes are stored:**

* **Alternative 1 (current choice):** Embed performance notes within each `Person`
    * Pros: Keeps academic history co-located with the student profile, simplifies cloning and persistence, and makes student deletion automatically clean up notes
    * Cons: Less efficient for queries that aggregate performance across classes or cohorts, requires iterating through every student for global analytics

* **Alternative 2:** Maintain a central `PerformanceBook`
    * Pros: Facilitates global reporting (e.g. all notes for a class), makes it easier to enforce cross-student constraints
    * Cons: Complicates referential integrity, adds risk of orphaned notes if students are removed, and scatters student-related data

**Aspect: Command structure for performance operations:**

* **Alternative 1 (current choice):** Sub-command flags under `perf` (`-a`, `-e`, `-d`, `-v`)
    * Pros: Keeps related operations grouped, mirrors other feature families (`att`, `fee`), and reduces the number of top-level commands to memorise
    * Cons: Requires tutors to remember what each flag stands for and to supply the correct combination of prefixes

* **Alternative 2:** Dedicated verbs per action (`perfadd`, `perfedit`, etc.)
    * Pros: Self-descriptive command words, reduces reliance on flags
    * Cons: Bloats the command set and deviates from the app's namespace-based command organisation

**Aspect: Tying performance notes to Attendance records:**
* **Alternative 1 (current choice):** Independent performance notes
    * Pros: Greater flexibility to record observations outside of attendance events, simplifies data model
    * Cons: Misses opportunity to link academic feedback directly to class participation

* **Alternative 2:** Link performance notes to attendance entries
    * Pros: Provides richer context for feedback, enables analysis of attendance-performance correlations
    * Cons: Increases complexity, may restrict note-taking to only attended classes

### ClassTag Management

#### Overview

The ClassTag feature allows tutors to organize students into classes or groups. Each ClassTag represents a distinct class (e.g., "Sec_3_A_Math", "P5_Science") that can be assigned to multiple students.

#### Implementation

ClassTag management is implemented through several key components:

**Model Component:**
- `ClassTag`: Represents a class with a unique name
- `UniqueClassTagList`: Maintains all ClassTags in the system, ensuring no duplicates
- The `Model` interface provides methods:
  - `hasClassTag(ClassTag)`: Checks if a ClassTag exists in the system
  - `addClassTag(ClassTag)`: Adds a new ClassTag to the system
  - `deleteClassTag(ClassTag)`: Removes a ClassTag from the system
  - `findClassTag(ClassTag)`: Finds and returns an `Optional` containing the canonical ClassTag instance from the UniqueClassTagList that matches the given ClassTag
  (case-insensitive). Returns `Optional.empty()` if not found. This ensures all references use the same ClassTag object with the original casing.
  - `getClassTagList()`: Returns an unmodifiable list of all ClassTags
  - `isClassTagInUse(ClassTag)`: Checks if any student is assigned the ClassTag
- `Person` objects maintain a `Set<ClassTag>` field that stores references to ClassTag instances from the central `UniqueClassTagList`, ensuring each student can be
assigned multiple unique classes

  
**Storage Component:**
- `JsonAdaptedClassTag`: Converts ClassTag objects to/from JSON format for persistence
- `JsonSerializableAddressBook`: Serializes both the central ClassTag list and student-ClassTag associations
- ClassTags are persisted in two ways:
    1. As a complete list in the `classTags` field of the address book
    2. As references within each student's `tags` field
- During deserialization, ClassTags are loaded first into the UniqueClassTagList, then student records resolve their tag names to the corresponding ClassTag objects using
  case-insensitive matching

**Logic Component:**

The following commands handle ClassTag operations:

1. **AddClassTagCommand (triggered by `tag -a`)**: Creates a new ClassTag in the system
   - Validates the ClassTag name format (1-30 characters: alphanumeric and underscores only, no spaces)
   - Checks for duplicates via `Model#hasClassTag()` (case-insensitive)
   - Adds to `UniqueClassTagList` via `Model#addClassTag()`

2. **DeleteClassTagCommand (triggered by `tag -d`)**: Deletes an existing ClassTag
   - Validates the ClassTag name format
   - Checks that the ClassTag exists via `Model#findClassTag()`
   - Ensures no students are currently assigned to it via `Model#isClassTagInUse()`
   - Removes from `UniqueClassTagList` via `Model#deleteClassTag()`


3. **ListClassTagCommand (triggered by `tag -l`)**: Lists all ClassTags in the system
   - Retrieves all ClassTags from the Model via `Model#getClassTagList()`
   - Displays them in a numbered list
   - If no tags exist, displays "No class tags found."

4. **ClassTagFilterCommand (triggered by `filter -t`)**: Filters students by ClassTag
   - Validates the ClassTag name format
   - Checks that the ClassTag exists via `Model#findClassTag()`
   - Creates a predicate to filter persons who have the ClassTag
   - Updates the filtered person list via `Model#updateFilteredPersonList(predicate)`

5. **AddCommand**: Adds a new student with optional ClassTag assignments
   - Validates the format of all specified ClassTags
   - Checks that all specified ClassTags exist before creating the student
   - Links student to ClassTags via references to canonical instances

6. **EditCommand**: Edits student details including ClassTag assignments
   - Can add, remove, or replace ClassTag assignments
   - Validates the format of all ClassTags specified
   - Checks that all ClassTags exist before updating
   - Empty ClassTag list (`t/`) removes all ClassTag assignments

#### Sequence Diagram: Adding a ClassTag

The following sequence diagram illustrates the interactions between components when a tutor creates a new ClassTag using the `tag -a` command:

<puml src="diagrams/AddClassTagSequenceDiagram.puml" alt="AddClassTagSequenceDiagram" />

 <box type="info" seamless>

**Note:** The diagram shows the successful case where the ClassTag does not already exist. If `hasClassTag()` returns `true`, a `CommandException` is thrown with the 
message "This class tag already exists."

</box>

#### Sequence Diagram: Filtering Students by ClassTag

The following sequence diagram illustrates how the system filters students by a specific ClassTag:

<puml src="diagrams/ClassTagFilterSequenceDiagram.puml" alt="ClassTagFilterSequenceDiagram" />

<box type="info" seamless>

**Note:** The diagram shows the successful case where the ClassTag exists. If `findClassTag()` returns `Optional.empty()`, a `CommandException` is thrown with the message
"Class tag not found".
     
</box>

#### Activity Diagram: Editing Student ClassTags

The activity diagram below illustrates the workflow when a tutor edits a student's ClassTag assignments using the `edit` command:

<puml src="diagrams/EditClassTagOfExistingStudentActivityDiagram.puml"/>

<box type="info" seamless>

**Note:** The diagram simplifies some validation steps for clarity. In the actual implementation, tag format validation occurs before checking existence, and each 
validation failure results in a specific error message being displayed to the user.

</box>

#### Design Considerations

**Aspect: ClassTag Storage Architecture:**

* **Alternative 1 (current choice):** Central storage in `UniqueClassTagList` with student references
    * Pros:
        - Single source of truth for all ClassTags
        - Enforces uniqueness at system level
        - Prevents orphaned or duplicate ClassTag names
        - Easy to list all classes in the system
        - Referential integrity - can prevent deletion of in-use ClassTags
    * Cons:
        - Requires additional existence validation when assigning ClassTags to students (must check tags exist in UniqueClassTagList first)
        - Slightly more complex implementation

* **Alternative 2:** Store ClassTags only within each Student object
    * Pros:
        - Simpler data structure
        - No need for separate ClassTag list
        - Automatic cleanup when student is deleted
    * Cons:
        - No centralized management or validation
        - Potential for duplicate ClassTag names with slight variations
        - Cannot list all classes without scanning all students
        - Cannot enforce consistent naming
        - No way to track which classes exist in the system

**Aspect: ClassTag Command Design:**

* **Alternative 1 (current choice):** Single `tag` command with flags (`-a`, `-d`, `-l`)
    * Pros:
        - Consolidates related operations under one command word
        - Reduces the number of top-level commands users need to remember
        - Clear semantic grouping of ClassTag operations
        - Single ClassTagCommandParser handles all tag operations, reducing parser classes and maintaining consistency
    * Cons:
        - More complex parsing logic to handle different flags

* **Alternative 2:** Separate commands (`addtag`, `deletetag`, `listtag`)
    * Pros:
        - Simpler individual parsers
        - No flag parsing needed
    * Cons:
        - More top-level commands to remember
        - Less semantic grouping

**Aspect: ClassTag Assignment Workflow:**

* **Alternative 1 (current choice):** Integrate with Add and Edit commands
    * Pros:
        - Fewer commands for users to learn
        - Natural workflow - assign classes when creating/modifying students
        - Reduces command clutter
    * Cons:
        - Add and Edit commands have more responsibilities
        - More complex command parsing

* **Alternative 2:** Separate AssignClassTag and UnassignClassTag commands
    * Pros:
        - Single responsibility per command
        - Simpler individual command implementations
    * Cons:
        - More commands for users to remember
        - Extra steps in workflow (create student, then assign classes)
        - Verbose for bulk operations
      
**Aspect: Case Sensitivity for ClassTag Comparison:**

* **Alternative 1 (current choice):** Case-insensitive comparison while preserving user input casing
  * Pros:
    - Prevents duplicate tags with different cases (e.g., "Math", "math", "MATH" are treated as the same tag)
    - More user-friendly - users don't need to remember exact casing when filtering or assigning tags
    - Preserves the tutor's preferred formatting/casing when they first create the tag (e.g., "Sec_3_A_Math" remains visible as entered)
    - Matches real-world expectations - class names are typically case-insensitive identifiers
  * Cons:
    - Slightly more complex implementation (requires overriding both `equals()` and `hashCode()` consistently for case-insensitive comparison)
    - Users cannot create tags that differ only in case (e.g., cannot have both "Math" and "MATH" as separate tags)

* **Alternative 2:** Normalize all ClassTag names to a single case (e.g., lowercase)
    * Pros:
        - Simpler implementation - no need for special `equals()` and `hashCode()` overrides
        - Completely eliminates case ambiguity
        - Consistent display format across the system
    * Cons:
        - Less flexible - tutors cannot use their preferred casing/formatting
        - May look less professional (e.g., "sec_3_a_math" vs "Sec_3_A_Math")
        - Loses semantic information conveyed through capitalization

* **Alternative 3:** Case-sensitive comparison (exact match required)
    * Pros:
        - Simplest implementation - use default String comparison
        - Allows maximum flexibility for different naming schemes
    * Cons:
        - User-unfriendly - requires remembering exact casing
        - Prone to duplicate tags with slight case variations
        - Could confuse users when "Math" and "math" are treated as different classes
        - More error-prone during filtering and assignment


#### Error Handling

ClassTag operations include comprehensive validation:

**Creating ClassTags:**
- Duplicate ClassTag names (case-insensitive)
- Invalid ClassTag name format (must be 1-30 alphanumeric characters with underscores, no spaces)
- Empty ClassTag name
- Missing or invalid flag

**Deleting ClassTags:**
- Non-existent ClassTag
- ClassTag still assigned to one or more students
- Invalid command format
- Invalid ClassTag format

**Assigning ClassTags to Students:**
- Non-existent ClassTag name when adding/editing students
- Invalid ClassTag format in student commands

**Filtering by ClassTag:**
- Non-existent ClassTag name
- Invalid command format
- Invalid ClassTag format

**General Validation Rules:**
- All tag commands enforce **strict parameter checking** — any extra text beyond the expected format is rejected
- When multiple flags are present, only the **first valid flag** determines the command type; subsequent flags are treated as invalid parameters and cause the command to fail
- Tag names are trimmed of leading/trailing whitespace before validation

Each validation error provides clear, actionable feedback to help users correct their input.

### Fee Management

#### Overview

The Fee Management feature allows tutors to track, update, and review students’ monthly tuition fee payments.  
Each student has a corresponding `FeeState` (**PAID** or **UNPAID**) for each month starting from their `enrolledMonth`.  
This feature is managed by the central `FeeTracker` component within the `Model`.

#### Implementation

Fee Management is implemented through several key components:

**Model Component:**

- `FeeTracker`: Serves as the central manager for all student fee records.
    - Internally stores data in a nested structure: `Map<StudentId, Map<Month, FeeState>>`, where each student’s fee history is organized by month.
    - The tracker only stores months that have been explicitly marked.
      Any month after enrolment without a record is automatically considered UNPAID by default.
      - Months before a student’s enrolledMonth are excluded from fee tracking, as no payment records exist prior to enrolment.
      - Provides methods to generate payment history between two months.
      - This design avoids redundant data storage and enables on-demand computation of fee states, ensuring efficient lookups and minimal memory use.
      - The `Model` interacts with the `FeeTracker` when marking payments, undoing payments, filtering Paid/Unpaid students, or retrieving historical fee data.

- `FeeState`: Represents a student’s payment status for a particular month. (either PAID or UNPAID)

- `Month`: Represents a calendar month in `MMYY` format and provides utilities for date comparison and traversal.

- `Person`: Each student stores an `enrolledMonth`, which defines when fee tracking begins.  
  Fee states are only derived for months on or after this enrollment month.

- The `Model` interface provides methods:
    - `markPaid(StudentId, Month)`: Marks a student’s fee as Paid for a given month.
    - `markUnpaid(StudentId, Month)`: Marks a student’s fee as Unpaid for a given month.
    - `paidStudents(Month)`: Returns a predicate for filtering students who have Paid for the specified month.
    - `unpaidStudents(Month)`: Returns a predicate for filtering students who have not Paid for the specified month.
    - `getCurrentFeeState(Person)`: Returns whether the student has any outstanding fees.
    - `feeStateVersionProperty()`: Returns a read-only observable property that increments whenever fee data changes, allowing the UI to refresh automatically.


**Storage Component:**

- `JsonAdaptedFeeRecord`: Converts fee records to and from JSON format for persistence. Each record stores:
    - `studentId`: the unique identifier of the student,
    - `monthString`: the month in `MMYY` format,
    - `feeState`: the payment status (`PAID` or `UNPAID`).
- `JsonAdaptedPerson`: Serializes each student’s data, including their attendance, performance notes, class tags, and **associated fee records**.
- `JsonSerializableAddressBook`: Handles serialization and deserialization of the entire address book, including all persons, class tags, and fee records.

- During deserialization:
    - The list of `feeRecords` is read first and each entry is re-registered into the in-memory `FeeTracker`.
    - Each person’s `StudentId` is matched to their corresponding fee entries to rebuild accurate payment histories.
    - Any student without explicit fee entries is assumed to have **no recorded payments**, and their post-enrolment months are treated as **UNPAID** by default.

**Logic Component:**

The following commands handle Fee Management operations:

1. **FeeMarkPaidCommand (triggered by `fee -p`)**: Marks a student’s fee as PAID for a specific month.
    - Validates that the student exists via `Model#getPersonById()`.
    - Validates that the target month falls between the student’s enrolled month and the current month (inclusive).
    - Ensures all previous months up to the target month are already PAID. 
    - Attempts to mark future months or months before enrolment will be rejected.
    - Updates the `FeeTracker` via `Model#markPaid()` and refreshes the UI.

2. **FeeMarkUnpaidCommand (triggered by `fee -up`)**: Marks a student’s fee as UNPAID for a specific month.
    - Validates that the student exists via `Model#getPersonById()`.
    - Validates that the target month falls between the student’s enrolled month and the current month (inclusive).
    - Tutors cannot mark future months or pre-enrolment months as Unpaid.
    - Updates the `FeeTracker` via `Model#markUnpaid()` and refreshes the UI.

3. **FeeFilterPaidCommand (triggered by `filter -p`)**: Filters students who have Paid for a specified month.
    - Validates that the input month is valid and not in the future.
    - Updates the filtered student list to display only students marked as PAID for that month.

4. **FeeFilterUnpaidCommand (triggered by `filter -up`)**: Filters students who have not Paid (Unpaid) for a specified month.
    - Validates that the input month is valid and not in the future.
    - Updates the filtered student list to display only students marked as UNPAID for that month.

5. **FeeViewCommand (triggered by `fee -v`)**: Displays a student’s payment history across a specified range of months.
    - Validates that the student exists and that the month range is valid.
    - Retrieves payment data using `FeeTracker#getPaymentHistory()`.
    - Outputs a reverse-chronological list of months (latest month first), with corresponding fee states.

#### Sequence Diagram: Marking a Student as Paid

The following sequence diagram illustrates how the system processes the `fee -p` command to mark a student’s fee as Paid:

<puml src="diagrams/FeeMarkPaidSequenceDiagram.puml" alt="FeeMarkPaidSequenceDiagram" />

#### Design Considerations

**Aspect: Sequential Payment Enforcement (Marking later months as PAID)**

* **Alternative 1 (current choice):** Require all previous months to be PAID before a tutor can mark a later month as PAID.
    * Pros:
      - Reflects real-world payment flow — students should settle earlier dues before paying for upcoming lessons.
      - Prevents skipped or inconsistent payment sequences, such as marking August as PAID while July remains UNPAID.
      - Ensures data integrity — once a month is marked PAID, all earlier months are guaranteed to be cleared.
      - Simplifies validation and reporting, since the payment timeline always progresses forward without gaps.
      - Makes it easy to determine a student’s most recent paid month at a glance.

    * Cons:
      - Tutors cannot record payments out of sequence (e.g., skipping July and paying for August directly).
      - Adds a small validation step when marking multiple months.

* **Alternative 2:** Allow tutors to mark any month as PAID, regardless of whether earlier months are still UNPAID.
    * Pros:
        - Provides maximum flexibility for unusual or irregular payment situations.
        - Allows quick entry for multiple months without enforcing chronological checks.

    * Cons:
      - Can result in gaps or inconsistencies in the payment timeline — e.g., later months marked PAID while earlier ones remain UNPAID.
      - Makes it easier for tutors to overlook unpaid months, leading to incomplete financial records.
      - Reduces data reliability, since it becomes unclear whether all payments up to a given point have been fully settled.

**Aspect: Future Month Restriction (Advance Payments)**

* **Alternative 1 (current choice):** Restrict marking months that are beyond the current calendar month.
    * Pros:
      - Prevents premature or speculative payments, maintaining realistic, time-based validation.
      - Keeps the system aligned with actual payment periods and avoids confusion with future billing.
      - Simplifies error handling and prevents data entry mistakes.

    * Cons:
      - Tutors cannot record advance payments for future months, even if a student has pre-paid.
      - May require future system updates to support legitimate early payments.

* **Alternative 2:** Allow tutors to mark future months as PAID, provided that all previous months have already been settled.
    * Pros:
      - Supports prepaid tuition scenarios, where students pay several months in advance.
      - Convenient for tutors managing long-term payment plans or scheduling future billing cycles in advance.
      - Reduces repetitive data entry when tutors want to record multiple future payments at once.

    * Cons:
      - Increases the risk of accidental marking of future months, especially if tutors mistype the month or forget the current date.
      - Requires additional validation safeguards (e.g., confirmation prompts or warning messages) to prevent unintentional future entries.
      - Adds complexity to the FeeViewCommand logic, since it must distinguish between completed months and prepaid future months when displaying fee history.

**Aspect: Backdated Correction (Marking an earlier month as UNPAID)**

* **Alternative 1 (current choice):** Allow tutors to mark an earlier month as UNPAID, even if later months are already PAID.
    * Pros:
      - Supports real-world correction scenarios — e.g., the tutor realizes a payment was incorrectly recorded.
      - Allows quick edits without needing to unmark all subsequent months first.
      - Keeps flexibility: once corrected, the tutor must still settle earlier UNPAID months before new payments are accepted.

    * Cons:
      - May cause a temporary inconsistency (e.g., later months PAID while an earlier one is UNPAID) until resolved.
      - Might confuse tutors reviewing the timeline during the correction phase.

* **Alternative 2:** Block marking an earlier month as UNPAID if any later month is already PAID.
    * Pros:
      - Maintains a strictly chronological payment timeline with no anomalies.
      - Simplifies record validation and summary generation.

    * Cons:
      - Restrictive — tutors cannot fix genuine mis-entries without first unmarking all later months.

**Aspect: Display Order of Payment History**

* **Alternative 1 (current choice):** Display payment history in reverse-chronological order (newest-first).  
  The computed range still spans from the effective start month (typically the enrolment month) up to the **current month**.

    * Pros:
        - Places the most relevant and recent months at the top, matching tutor workflows.
        - Reduces scrolling effort for checking recent payments or following up on unpaid students.
        - Maintains consistency across panels (e.g., performance notes), which also prioritize recent data.

    * Cons:
        - Tutors reviewing long-term records need to scroll to the bottom to reach the **earliest (enrolment) months**.


* **Alternative 2:** Display payment history in chronological order (oldest-first).  
  The earliest month (enrolment) appears first, progressing toward the current month.

    * Pros:
        - Provides a narratively intuitive timeline, helping tutors review payment history from the student’s start date.

    * Cons:
        - Recent months, which tutors most often need, appear at the bottom — requiring extra scrolling.
        - Inconsistent with other panels that already emphasize newest-first ordering.

---

#### Error Handling

Fee Management includes comprehensive validation across all operations:

**Marking Fees**
- Invalid or missing `StudentId`
- Month before enrolment or after the current month is invalid
- Attempting to mark Paid when already Paid
- Attempting to mark Unpaid when already Unpaid
- Attempting to mark Paid while an earlier month remains Unpaid

**Filtering Fees**
- Invalid or future month input

**Viewing Fee History**
- Invalid `StudentId`
- Invalid or future start month
- No payment data available

Each validation error produces clear and descriptive messages to guide user correction.

---
## **Planned Enhancements**

**Team size:** 5

Given below is a list of enhancements we plan to implement in future versions of Tuto:

1. **Bulk attendance marking for entire class:** Currently, tutors must mark attendance for each student individually using `att -p s/STUDENT_ID d/DATE t/CLASS`. For tutors managing small group classes, this process can still become repetitive and time-consuming when marking multiple students in the same class. We plan to add a bulk marking feature that allows tutors to mark attendance for all students in a specific class at once. For example, `att -pA d/10112025 t/Math` would mark all students as present enrolled in the Math ClassTag as present for that date. This would significantly reduce the time needed to take attendance at the beginning of each lesson.
2. **Bulk deletion of old attendance records:** Currently, attendance records accumulate indefinitely, and tutors can only delete them one by one using `att -d s/STUDENT_ID d/DATE t/CLASS`. For students enrolled for extended periods (e.g., multiple years), their attendance lists can become very long and cluttered with old records that are no longer relevant for day-to-day tutoring. We plan to add a bulk deletion feature that allows tutors to remove attendance records older than a specified date or within a date range. For example, `att -clear d/BEFORE_DATE` or `att -clear d/FROM_DATE d/TO_DATE` would remove old records in bulk. This would help tutors maintain clean, relevant data while preserving important recent attendance history, improving both performance and usability when viewing attendance records.
3. **Individual class tag assignment and unassignment on top of current add/edit:** Currently, when editing a student's class tags using the edit command, all existing tags are replaced with the new list provided (or cleared if `t/` is empty). This makes it cumbersome to add or remove a single tag without re-specifying all others. We plan to introduce new commands `tag -assign s/STUDENT_ID t/TAG_NAME` and `tag -unassign s/STUDENT_ID t/TAG_NAME` that allow adding or removing individual tags without affecting previously assigned ones. For example, `tag -assign s/0001 t/Sec_3_A_Math` would add the `Sec_3_A_Math` tag to student 0001 if they don't already have it, leaving other tags intact. Similarly, `tag -unassign s/0001 t/Sec_3_A_Math` would remove only that tag. This enhancement addresses the frequent need for precise, incremental changes to student records, improving tutor workflow efficiency.
4. **Bulk ClassTag operations:** Currently, assigning or removing a ClassTag requires editing each student individually using the `edit` command. We plan to introduce bulk tag operations with two new commands: `tag -ba t/TAG_NAME s/ID1 s/ID2 ...` (bulk assign) to assign a single ClassTag to multiple students at once, and `tag -rall t/TAG_NAME` (remove from all) to remove a specific ClassTag from every student who currently has it. For example, `tag -ba t/Sec_3_A_Math s/0001 s/0002 s/0010` would assign the "Sec_3_A_Math" tag to students 0001, 0002, and 0010 in a single command, while `tag -rall t/Sec_3_A_Math` would remove that tag from all students currently assigned to it. This enhancement would significantly improve efficiency when managing class enrollments, course transitions, renaming classes and academic year updates.
5. **Introduce third fee state — WAIVED/SKIPPED:**  
   At present, fee tracking uses only two states: **PAID** and **UNPAID**.  
   In future releases, we plan to introduce a third state, **WAIVED** (or **SKIPPED**), to handle non-billable months such as holidays, term breaks, or periods without lessons.  
   This enhancement will:
    - Accurately reflect months when no tuition fees are due.
    - Allow tutors to “skip” months without breaking the sequential payment validation rule.
    - Improve clarity in fee reports by distinguishing “not billed” months from “unpaid” ones.

   This addition will also enhance flexibility in long-term record management and improve real-world applicability for tutoring scenarios involving variable schedules.
6. **Integrate Fee and Attendance Systems:**  
   Currently, fee tracking and attendance operate independently.  
   We plan to introduce light integration between both modules to make payment tracking more context-aware.

    - When viewing a student’s fee history, tutors will also see the **number of lessons held** for each month.
    - When marking a month as **PAID** with no recorded attendance, the system will show a **confirmation prompt** to avoid mistakes.
    - When marking a month as **UNPAID** while lessons are recorded, a **reminder** will appear to alert the tutor of possible inconsistencies.
    - Months **without any recorded attendance** will automatically be assigned a **WAIVED** status instead of UNPAID, ensuring skipped months (e.g., holidays or term breaks) do not block future payments.
   This enhancement improves **accuracy** and **consistency** between financial and attendance records, while keeping full flexibility for tutors to override when necessary.
7. **Unified student history view (view s/STUDENT_ID):** Introduce a consolidated view command that shows every performance note, attendance record, and fee transaction for the specified student, allowing tutors to review a learner’s full journey without hopping between modules.
8. **Targeted performance and attendance filters (perf -v / att -v):** Extend the existing view flags to accept optional m/MMYY or t/CLASS_TAG parameters so tutors can zero in on a specific month or class when analysing historical performance or attendance data.
9. **Enhanced Name Validation:** Currently, the system only allows alphabetic characters, spaces, hyphens, and apostrophes in student names, with a maximum limit of 100 characters. While this prevents invalid or malicious input and ensures consistent formatting, it also rejects legitimate names containing cultural or linguistic symbols, or relational notations like “s/o”. 
We plan to expand the validation rules to allow Unicode characters in names, enabling broader support for global naming conventions while still excluding emojis and unsupported symbols. For example, names such as S/O Rajesh, Zoë-Marie, and Renée d’Olivier would be accepted under the new rules.
This enhancement will make Tuto more inclusive, accurate, and realistic for users managing students with diverse backgrounds and naming conventions.
10. **Persistent Student ID Assignment (JSON Tracking):** Currently, Student IDs are auto-generated in a 4-digit sequence (e.g., 0001, 0002, …) after validating all other fields. However, the system only recycles the highest deleted ID if it was the most recent one and if no other add/delete operations occurred before the next app launch. This can occasionally lead to duplicate or reused IDs across sessions. 
We plan to implement JSON-based persistent ID tracking, which stores the last assigned ID and always increments from it when new students are added. This ensures IDs remain unique across sessions, even after restarts or deletions.
This enhancement will strengthen data integrity, prevent ID conflicts, and ensure a more consistent and reliable record-keeping process for long-term tutoring management.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* Freelance Secondary School Tutor in Singapore
* has a need to manage a significant number of contacts
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: Helps freelance tutors organise students, track attendance and performance, and manage tuition fees, all in one platform designed to reduce admin work so they can focus on teaching.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                    | I want to …​                                              | So that I can…​                                                        |
|----------|------------------------------------------- |-----------------------------------------------------------|------------------------------------------------------------------------|
| `* * * ` | tutor handling lesson fees                 | tag a student as paid for a given month                   | keep track of students who have settled their tuition fees             |
| `* * *`  | tutor handling lesson fees                 | tag a student as unpaid for a given month                 | fix mistakes and keep payment records accurate                         |
| `* * *`  | tutor handling lesson fees                 | filter students who have paid by month                    | view all students who have completed payment for that month at a glance |
| `* * *`  | tutor handling lesson fees                 | filter students who have not paid by month                | follow up with students who have outstanding tuition fees              |
| `* * *`  | tutor handling lesson fees                 | view a student's payment history up to the current month  | review their past payment behaviour and identify missed months         |
| `* * *`  | tutor who teaches multiple classes         | create a class tag                                        | keep track of a new class I am teaching                                |
| `* * *`  | tutor who teaches multiple classes         | assign class tags to a student during creation or editing | manage all students of the same class together                         |
| `* * *`  | tutor who teaches multiple classes         | remove class tags from a student through editing          | remove students not in a particular class                              |
| `* * *`  | tutor who teaches multiple classes         | filter students by class tag (eg. Sec_3_A_Math)           | easily find students in a specific class                               |
| `* * *`  | tutor who teaches multiple classes         | list all the class tags                                   | know what classes I am teaching                                        |
| `* * *`  | tutor who teaches multiple classes         | delete a class tag                                        | keep track of the classes I am still teaching                          |
| `* * * ` | tutor     | add a performance note for a student on a given date      | I can record their progress                                            |
| `* * * ` | tutor     | view all performance notes for a student                  | I can review their progress                                            |
| `* * * ` | tutor     | edit a specific performance note for a student            | I can correct or update it                                             |
| `* * * ` | tutor     | delete a specific performance note for a student          | I can remove it if needed                                              |
| `* * *`  | tutor who teaches multiple classes         | take attendance of each student                           | I can track their attendance record                                    |
| `* * *`  | tutor who teaches multiple classes         | view students' attendance history                         | I can track if students are consistently attending lessons             |
| `* * *`  | tutor who teaches multiple classes         | mark a student's attendance as absent                     | correct mistakes or changes if attendance was marked wrongly           |
| `* * *`  | tutor who teaches multiple classes         | delete an attendance record                               | remove records for cancelled classes or fix erroneous entries          |
| `* *`    | new tutor user                                           | view sample data                                          | understand how the app looks when populated                            |
| `* *`    | tutor starting fresh                                     | purge sample/old data                                     | start fresh with only my real student info                             |                                                                  |
| `* * *`  | tutor managing students                                  | add students                                              | quickly add my students into the address book                          |
| `* * *`  | tutor managing students                                  | view students                                             | see all the students I am teaching and their details at a glance       |
| `* *`    | tutor managing students                                  | delete students                                           | remove students who are no longer taking lessons                       |
| `* * *`  | tutor handling many students across classes and subjects | edit student information                                  | update my contact list                                                 |
| `* * *`  | tutor handling many students across classes and subjects | search for a student by name                              | quickly locate their information                                       |


### Use cases

(For all use cases below, the **System** is the `Tuto` and the **Actor** is the `Tutor`, unless specified otherwise)

**Use case: Create a new class tag**

 **MSS**
 1. Tutor requests to create a new class tag, providing a tag name.
 2. Tuto validates the class tag name format.
 3. Tuto checks that no class tag with the same name (case-insensitive) exists.
 4. Tuto creates the new class tag.
 5. Tuto shows a success message.
 
    Use case ends.
 
 **Extensions**
* 1a. The command format is invalid.

    * 1a1. Tuto shows an error message with the correct usage format.

      Use case ends.
  
 * 2a. The provided class tag name is invalid.
 
     * 2a1.  Tuto shows an error message indicating invalid class tag format.
 
       Use case ends.
 
 * 3a. A class tag with the same name already exists (case-insensitive match).
 
     * 3a1. Tuto shows an error message indicating the tag already exists.
 
       Use case ends.

**Use case: Delete a class tag**

 **MSS**
 1. Tutor requests to delete a class tag, providing its name.
 2. Tuto validates the class tag name format.
 3. Tuto checks that the class tag exists.
 4. Tuto checks that the class tag is not assigned to any students.
 5. Tuto deletes the class tag.
 6. Tuto shows a success message.
 
    Use case ends.
 
 **Extensions**
* 1a. The command format is invalid.

    * 1a1. Tuto shows an error message with the correct usage format.

      Use case ends.
  
 * 2a. The provided class tag name format is invalid.
 
     * 2a1.  Tuto shows an error message indicating invalid class tag format.
 
       Use case ends.
 
 * 3a. The specified class tag does not exist.
 
     * 3a1. Tuto shows an error message indicating the class tag was not found.
 
       Use case ends.
 
 * 4a. The specified class tag is still assigned to one or more students.
 
     * 4a1. Tuto shows an error message suggesting to unassign the tag from all students before deletion.
 
       Use case ends.
 
 **Use case: List all class tags**
 
 **MSS**
 1. Tutor requests to list all class tags.
 2. Tuto retrieves all existing class tags.
 3. Tuto displays the tags in a numbered list.
 
    Use case ends.
 
 **Extensions**
* 1a. The command format is invalid (extra parameters provided).

    * 1a1. Tuto shows an error message with the correct usage format.

      Use case ends.
  
 * 2a. No class tags exist in the system.
 
     * 2a1. Tuto shows a message indicating that no tags have been created.
 
       Use case ends.


**Use case: Filter students by class tag**

 **MSS**
 1. Tutor requests to filter students by a specific class tag name.
 2. Tuto validates class tag name format.
 3. Tuto checks that the class tag exists.
 4. Tuto filters the student list to show only those assigned to the class tag.
 5. Tuto displays the filtered list.
 
    Use case ends.
 
 **Extensions**
 * 1a. The command format is invalid.
 
     * 1a1. Tuto shows an error message with the correct usage format.
 
       Use case ends.
 
 * 2a. The class tag name format is invalid.
 
     * 2a1. Tuto shows an error message indicating invalid class tag format.
 
       Use case ends.
 
 * 3a. The specified class tag does not exist.
 
     * 3a1. Tuto shows an error message indicating the class tag was not found.
 
       Use case ends.
 
 * 5a. No students are assigned to the specified class tag.
 
     * 5a1. Tuto displays an empty list.
 
       Use case ends.

**Use case: Add a performance note**

**MSS**
1. Tutor requests to add a performance note for a student on a specific date for a specific class.
2. Tuto adds the performance note for the student.
3. Tuto shows a success message indicating the note has been added.

   Use case ends.

**Extensions**
* 1a. The provided student ID does not match any existing student.

  * 1a1. Tuto shows an error message indicating student not found.
  
    Use case ends.
  
* 1b. The command format is invalid (e.g. missing prefixes).

  * 1b1. Tuto shows an error message with the correct usage format.
  
    Use case ends.

* 1c. The student ID format is invalid.

  * 1c1. Tuto shows an error message indicating invalid student ID format.
  
    Use case ends.

* 1d. The specified class tag does not exist.

  * 1d1. Tuto shows an error message indicating class tag not found.
  
    Use case ends.

* 1e. The specified class tag is not assigned to the student.

  * 1e1. Tuto shows an error message indicating class tag not assigned to student.
  
    Use case ends.

* 1f. The provided date fails validation.

    * 1f1. The date is in the future.

        * 1f1a. Tuto shows an error message indicating date cannot be in the future.

          Use case ends.

    * 1f2. The date format is earlier than the student's enrolment month.

        * 1f2a. Tuto shows an error message indicating date cannot be before enrolment month.

            Use case ends.

    * 1f3. The date does not correspond to a real calendar day (e.g. 30th February).

        * 1f3a. Tuto shows an error message indicating invalid date.

            Use case ends.
  
* 1g. Performance note exceeds the 200-character limit.

  * 1g1. Tuto shows an error message indicating character limit.
  
    Use case ends.
  
* 1h. A performance note already exists for the same student on the same date for the same class.

  * 1h1. Tuto shows an error message.
  
    Use case ends.
 
**Use case: View performance notes of a student**

**MSS**
1. Tutor requests to view all performance notes of a student.
2. Tuto displays all performance notes of the student in chronological order, with newest at the top.

   Use case ends.

**Extensions**
* 1a. The provided student ID does not match any existing student.

    * 1a1. Tuto shows an error message indicating student not found.

      Use case ends.

* 1b. The command format is invalid (e.g. missing prefixes).

    * 1b1. Tuto shows an error message with the correct usage format.

      Use case ends.

* 1c. The student ID format is invalid.

    * 1c1. Tuto shows an error message indicating invalid student ID format.

      Use case ends.

* 1d. The student has no performance notes.

    * 1d1. Tuto shows a message indicating that the student has no performance notes.
  
      Use case ends.

**Use case: Edit a performance note**

**MSS**
1. Tutor requests to edit a specific performance note of a student on a specific date for a specific class.
2. Tuto updates the performance note with the new content.
3. Tuto shows a success message.

   Use case ends.

**Extensions**
* 1a. The provided student ID does not match any existing student.

    * 1a1. Tuto shows an error message indicating student not found.

      Use case ends.

* 1b. The command format is invalid (e.g. missing prefixes).

    * 1b1. Tuto shows an error message with the correct usage format.

      Use case ends.

* 1c. The student ID format is invalid.

    * 1c1. Tuto shows an error message indicating invalid student ID format.

      Use case ends.

* 1d. The provided date fails validation.

    * 1d1. The date does not correspond to a real calendar day (e.g. 30th February).

        * 1e3a. Tuto shows an error message indicating invalid date.

          Use case ends.

* 1e. Performance note exceeds the 200-character limit.

    * 1e1. Tuto shows an error message indicating character limit.

      Use case ends.

* 1f. Performance note does not exist for the specified student on the given date for the given class.

    * 1f1. Tuto shows an error message.

      Use case ends.

**Use case: Delete a performance note**

**MSS**
1. Tutor requests to delete a specific performance note of a student on a specific date for a specific class.
2. Tuto deletes the performance note.
3. Tuto shows a success message.

    Use case ends.

**Extensions**
* 1a. The provided student ID does not match any existing student.

    * 1a1. Tuto shows an error message indicating student not found.

    Use case ends.

* 1b. The command format is invalid (e.g. missing prefixes).

    * 1b1. Tuto shows an error message with the correct usage format.

      Use case ends.

* 1c. The student ID format is invalid.

    * 1c1. Tuto shows an error message indicating invalid student ID format.

      Use case ends.

* 1d. The provided date fails validation.

    * 1d1. The date does not correspond to a real calendar day (e.g. 30th February).

        * 1d1a. Tuto shows an error message indicating invalid date.

          Use case ends.

* 1e. A performance note does not exist for the same student on the same date for the same class.

    * 1e1. Tuto shows an error message.

      Use case ends.

    
**Use case: Mark Student as Paid**

**MSS**
1. Tutor requests to mark a student as **PAID** for a specific month.
2. Tuto validates the request: student exists, command format is valid, and the month is **between the enrolled month and the current month (inclusive)**.
3. Tuto verifies that **all earlier months** (from enrolment up to the previous month) are already **PAID**.
4. Tuto records the **PAID** status for the month and displays a success message.

   Use case ends.

**Extensions**
* 2a. Command format is invalid.
    * 2a1. Tuto shows the correct usage format.
      Use case ends.

* 2b. Student ID does not exist.
    * 2b1. Tuto shows an error that the student cannot be found.
      Use case ends.

* 2c. The student ID format is invalid.
    * 2c1. Tuto shows an error message indicating invalid student ID format.
      Use case ends.

* 2d. The selected month is **before enrolment** or **after the current month**.
    * 2d1. Tuto shows an error that the month is invalid.
      Use case ends.

* 2e. The selected month is **already marked as PAID**.
    * 2e1. Tuto indicates that the payment has already been recorded.
      Use case ends.

* 3a. An **earlier month** is **UNPAID**.
    * 3a1. Tuto shows an error indicating the earliest unpaid month that blocks the operation.
      Use case ends.

**Use case: Mark Student as Unpaid**

**MSS**
1. Tutor requests to mark a student as **UNPAID** for a specific month.
2. Tuto validates the request: student exists, command format is valid, and the month is **between the enrolled month and the current month (inclusive)**.
3. Tuto records the **UNPAID** status and displays a success message.

   Use case ends.

**Extensions**
* 2a. Command format is invalid.
    * 2a1. Tuto shows the correct usage format.
      Use case ends.

* 2b. Student ID does not exist.
    * 2b1. Tuto shows an error that the student cannot be found.
      Use case ends.

* 2c. The student ID format is invalid.
    * 2c1. Tuto shows an error message indicating invalid student ID format.
      Use case ends.

* 2d. The selected month is **before enrolment** or **after the current month**.
    * 2d1. Tuto shows an error that the month is invalid.
      Use case ends.

* 2e. The selected month is **already marked as UNPAID**.
    * 2e1. Tuto indicates that the month is already unpaid.
      Use case ends.

**Use case: Filter Paid Students by Month**

**MSS**
1. Tutor requests to filter students marked **PAID** for a specific month.
2. Tuto validates the request: command format is valid and the month is **not in the future**.
3. Tuto applies the predicate to the model to filter students by **PAID** status for that month.
4. Tuto displays the filtered list.

   Use case ends.

**Extensions**
* 2a. Command format is invalid.
    * 2a1. Tuto shows the correct usage format.
      Use case ends.

* 2b. The month is **in the future**.
    * 2b1. Tuto shows an error that future months cannot be filtered.
      Use case ends.

* 2c. The month format is invalid.
    * 2c1. Tuto shows an error message indicating invalid month format.
      Use case ends.

**Use case: Filter Unpaid Students by Month**

**MSS**
1. Tutor requests to filter students marked **UNPAID** for a specific month.
2. Tuto validates the request: command format is valid and the month is **not in the future**.
3. Tuto applies the predicate to the model to filter students by **UNPAID** status for that month.
4. Tuto displays the filtered list.

   Use case ends.

**Extensions**
* 2a. Command format is invalid.
    * 2a1. Tuto shows the correct usage format.
      Use case ends.

* 2b. The month is **in the future**.
    * 2b1. Tuto shows an error that future months cannot be filtered.
      Use case ends.

* 2c. The month format is invalid.
    * 2c1. Tuto shows an error message indicating invalid month format.
      Use case ends.

**Use case: View Payment History of a Student**

**MSS**
1. Tutor requests to view a student's payment history (optionally with a start month).
2. Tuto validates the request: command format is valid, the student exists, and the provided start month (if any) is **not in the future**.
3. Tuto determines the effective start month:
    * If a start month is provided, the range starts from the **later** of the provided month and the **enrolled month**.
    * Otherwise, the range starts from the **enrolled month**.
4. Tuto retrieves the month-by-month history from the effective start to the current month.
5. Tuto displays the history in reverse-chronological order (current month at the top, effective start at the bottom), indicating whether each month is an **explicit** mark or a **default** (unmarked → UNPAID).

   Use case ends.

**Extensions**
* 2a. Command format is invalid.
    * 2a1. Tuto shows the correct usage format.
      Use case ends.

* 2b. Student ID does not exist.
    * 2b1. Tuto shows an error that the student cannot be found.
      Use case ends.

* 2c. The student ID format is invalid.
    * 2c1. Tuto shows an error message indicating invalid student ID format.
      Use case ends.

* 2d. The provided start month is **after the current month**.
    * 2d1. Tuto shows an error that future months cannot be displayed.
      Use case ends.

* 2e. The month format is invalid.
    * 2e1. Tuto shows an error message indicating invalid month format.
      Use case ends.

* 3a. The provided start month is **before enrolment**.
    * 3a1. Tuto automatically adjusts the start to the enrolment month.
      Use case continues at Step 4.


**Use case: Mark Student as Present**

**MSS**
1. Tutor requests to mark a student as **Present** for a specific date and class.
2. Tuto validates the request: student exists, command format is valid, and date is valid.
3. Tuto checks that the student has the class tag assigned or has an existing attendance record for that student, date, and class.
4. Tuto checks that the attendance is not already marked as **Present** for that date and class.
5. Tuto records the **Present** attendance and displays a success message.

   Use case ends.

**Extensions**
* 2a. Command format is invalid.
    * 2a1. Tuto shows the correct usage format.
      Use case ends.

* 2b. Student ID does not exist.
    * 2b1. Tuto shows an error that the student cannot be found.
      Use case ends.

* 2c. The student ID format is invalid.
    * 2c1. Tuto shows an error message indicating invalid student ID format.
      Use case ends.

* 2d. The provided date is in the future.
    * 2d1. Tuto shows an error message indicating date cannot be in the future.
      Use case ends.

* 2e. The provided date is before the student's enrolment month.
    * 2e1. Tuto shows an error message indicating date cannot be before enrolment.
      Use case ends.

* 2f. The date does not correspond to a real calendar day (e.g. 30th February).
    * 2f1. Tuto shows an error message indicating invalid date.
      Use case ends.

* 3a. The specified class tag is not assigned to the student and no attendance record exists for that student, date, and class.
    * 3a1. Tuto shows an error message indicating class tag not assigned to student.
      Use case ends.

* 4a. The attendance is already marked as **Present** for that date and class.
    * 4a1. Tuto shows an error message indicating the student is already marked present.
      Use case ends.

**Use case: Mark Student as Absent**

**MSS**
1. Tutor requests to mark a student as **Absent** for a specific date and class.
2. Tuto validates the request: student exists, command format is valid, and date is valid.
3. Tuto checks that the student has the class tag assigned or has an existing attendance record for that student, date, and class.
4. Tuto checks that the attendance is not already marked as **Absent** for that date and class.
5. Tuto records the **Absent** attendance and displays a success message.

   Use case ends.

**Extensions**
* 2a. Command format is invalid.
    * 2a1. Tuto shows the correct usage format.
      Use case ends.

* 2b. Student ID does not exist.
    * 2b1. Tuto shows an error that the student cannot be found.
      Use case ends.

* 2c. The student ID format is invalid.
    * 2c1. Tuto shows an error message indicating invalid student ID format.
      Use case ends.

* 2d. The provided date is in the future.
    * 2d1. Tuto shows an error message indicating date cannot be in the future.
      Use case ends.

* 2e. The provided date is before the student's enrolment month.
    * 2e1. Tuto shows an error message indicating date cannot be before enrolment.
      Use case ends.

* 2f. The date does not correspond to a real calendar day (e.g. 30th February).
    * 2f1. Tuto shows an error message indicating invalid date.
      Use case ends.

* 3a. The specified class tag is not assigned to the student AND no attendance record exists for that student, date, and class.
    * 3a1. Tuto shows an error message indicating class tag not assigned to student.
      Use case ends.

* 4a. The attendance is already marked as **Absent** for that date and class.
    * 4a1. Tuto shows an error message indicating the student is already marked absent.
      Use case ends.

**Use case: Delete an Attendance Record**

**MSS**
1. Tutor requests to delete an attendance record for a student on a specific date and class.
2. Tuto validates the request: student exists, command format is valid, date is valid, and attendance record exists for that date and class.
3. Tuto verifies that an attendance record exists for that date and class.
4. Tuto deletes the attendance record and displays a success message.

   Use case ends.

**Extensions**
* 2a. Command format is invalid.
    * 2a1. Tuto shows the correct usage format.
      Use case ends.

* 2b. Student ID does not exist.
    * 2b1. Tuto shows an error that the student cannot be found.
      Use case ends.

* 2c. The student ID format is invalid.
    * 2c1. Tuto shows an error message indicating invalid student ID format.
      Use case ends.

* 2d. The date does not correspond to a real calendar day (e.g. 30th February).
    * 2d1. Tuto shows an error message indicating invalid date.
      Use case ends.

* 3a. No attendance record exists for the specified date and class.
    * 3a1. Tuto shows an error message indicating no attendance record found.
      Use case ends.

**Use case: View a Student's Attendance History**

**MSS**
1. Tutor requests to view the attendance history of a student.
2. Tuto validates the request: student exists and command format is valid.
3. Tuto retrieves all attendance records for the student.
4. Tuto displays the attendance history sorted by date (newest first), then by class tag name alphabetically.

   Use case ends.

**Extensions**
* 2a. Command format is invalid.
    * 2a1. Tuto shows the correct usage format.
      Use case ends.

* 2b. Student ID does not exist.
    * 2b1. Tuto shows an error that the student cannot be found.
      Use case ends.

* 2c. The student ID format is invalid.
    * 2c1. Tuto shows an error message indicating invalid student ID format.
      Use case ends.

* 3a. No attendance records exist for the student.
    * 3a1. Tuto shows a message indicating no attendance records found.
      Use case ends.

**Use case: Add Student**

**MSS**

1. Tutor requests to create a new student, providing all required details and one or more optional class tags.
2. Tuto adds student to records.
3. The new student record is successfully created.

   Use case ends.

**Extensions**

* 1a. The command format is invalid
    * 1a1. Tuto shows an error message with the correct usage format.

      Use case ends.

* 1b. Duplicate student detected.
    * 1b1. Tuto shows an error message.

      Use case ends.
  
* 1c. One or more of the provided class tag names do not exist.

  * 1c1. Tuto shows an error message.

    Use case ends.

**Use case: View Students**

**MSS**

1. Tutor retrieves student list.
2. Tuto displays the list of students.

   Use case ends.

**Extensions**

* 1a. No student exist.
    * 1a1. Tuto shows a message indicating no students exist.

      Use case ends.

**Use case: Edit Student**

**MSS**

1. Tutor requests to edit a student's details, providing the student's ID and at least one new field to update.
2. Tuto updates all specified fields for the student.
3. Tuto confirms the student's info has been updated.

   Use case ends.

**Extensions**

* 1a. The command format is invalid
    * 1a1. Tuto shows an error message with the correct usage format.

      Use case ends.

* 1b. One or more of the given class tag names do not exist.

    * 1b1. Tuto shows an error message

      Use case ends.

**Use case: Delete Student**

**MSS**

1. Tutor deletes student.
2. Tuto removes student from records.
3. Tuto confirms the student's record has been deleted.

   Use case ends.

**Extensions**

* 1a. The command format is invalid
    * 1a1. Tuto shows an error message with the correct usage format.

      Use case ends.

**Use case: Search Student**

**MSS**

1. Tutor searches student by name.
2. Tuto searches records for matching students.
3. Tuto outputs search results.

   Use case ends.

**Extensions**

* 1a. The command format is invalid
    * 1a1. Tuto shows an error message with the correct usage format.

      Use case ends.

* 2a. No matching students.
    * 2a. Tuto shows a message indicating no students found.

      Use case ends.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 students without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  The application should operate entirely offline, without requiring a database server or internet connection.
5.  The system should operate without requiring additional libraries.
6.  Data integrity must be maintained, ensuring no loss or duplication of records after operations or application restarts.
7.  The application should run locally on the user’s device.
8.  The system should be packaged into a single executable JAR file ≤ 200 MB, requiring no installer.
9.  User documentation should be clear and concise for beginners.
10. The application shall follow object-oriented design principles, allowing new commands or modules to be added with minimal changes to existing code.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, macOS.
* **Private contact detail**: A contact detail that is not meant to be shared with others.
* **Student ID**: A 4-digit unique numeric identifier (0000–9999) assigned to each student when added to the system.
* **Payment History**: A record covering a range from a start month (either the student’s enrolment month or an explicitly provided m/MMYY) up to the current month (inclusive). The UI displays this range in reverse-chronological order (newest month first). Months after enrolment with no explicit record are derived as UNPAID by default.
* **Performance note**: A short textual record of a student's performance on a given date for a specific class.
* **Class Tag/ClassTag**: A label representing a class or subject that can be assigned to students (e.g. "Sec3_Maths").
* **Attendance History**: A complete record of student's attendance across all dates and classes from the time of enrolment, with no time limit on historical data.
* **Executable JAR**: A Java Archive file that contains all compiled classes and resources, which can be run directly without installation.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

#### Steps for testing a tP JAR file (please follow closely)

1. **Prepare a writable folder**
   
   Put the JAR file in an empty folder where the app is allowed to create files (i.e., do not use a write-protected folder).

2. **Verify Java version (must be Java 17)**
   
   Open a command window/terminal and run:
   
   ```
   java -version
   ```
   
   Do this even if you checked before, as your OS may have auto-updated the default Java version.

3. **Check the User Guide (UG) for prerequisites**
   
   See if there are extra steps before launching (e.g., downloading another file). You may also visit the team's GitHub Releases page for any additional files provided.

4. **Launch from the command line (recommended)**
   
   Use `java -jar` to ensure the JAR runs with the Java version you verified. Double-click only as a last resort.
   
   * Surround the JAR filename with quotes in case it contains special characters:
     
     ```
     java -jar "[CS2103-F10-3][Tuto].jar"
     ```
   
   * **Windows:** Use Command Prompt or PowerShell (not the WSL terminal).
   
   * **Linux (Wayland):** If you see a Gdk-CRITICAL error, try:
     
     ```
     GDK_BACKEND=x11 java -jar jar_file_name.jar
     ```

5. **Saving window preferences**
   
   1. Resize the window to an optimum size. Move the window to a different location. Close the window.
   
   2. Re-launch the app using the command line method above.<br>
      Expected: The most recent window size and location is retained.

### Managing Students

#### Adding a Student

1. Adding a new student with valid fields

    1. Prerequisites: Ensure the ClassTags you intend to assign exist (`tag -l` to list tags). The student does **not** need to exist yet.

    1. Test case: `add n/John Doe p/91234567 e/johndoe@example.com a/123, Clementi Rd t/Sec3_Maths`  
       **Expected:** New student is added. Status message confirms addition, Student ID auto-generated (e.g., `0001`), student appears in the list.

1. Adding a student with missing required fields

    1. Test case: `add n/ p/91234567`  
       **Expected:** Command rejected. Error message explains missing Name, Email, or Address.

1. Adding a student with non-existent ClassTags

    1. Test case: `add n/John Doe p/91234567 e/johndoe@example.com a/123, Clementi Rd t/NonExistentTag`  
       **Expected:** Command rejected. Error message indicates ClassTag does not exist.

---

#### Editing a Student

1. Editing a student with a valid Student ID

    1. Prerequisites: Student must already exist in the system. For example, Student ID `0001` exists (from previous `add` test). Use `list` to verify.

    1. Test case: `edit s/0001 p/98765432 e/john.doe@newmail.com`  
       **Expected:** Student’s phone and email updated. Status message confirms edit.

1. Editing with empty ClassTag (`t/`) to remove all tags

    1. Prerequisites: Student must already exist (e.g., Student ID `0001`).

    1. Test case: `edit s/0001 t/`  
       **Expected:** All ClassTags removed for this student. Status message confirms edit.

1. Editing with invalid Student ID

    1. Test case: `edit s/9999 p/91234567`  
       **Expected:** Command rejected. Error message indicates Student ID not found.

---

#### Deleting a Student

1. Deleting an existing student

    1. Prerequisites: Student must already exist. For example, Student ID `0001`.

    1. Test case: `delete s/0001`  
       **Expected:** Student with ID `0001` deleted. Status message confirms deletion. List updates.

1. Deleting a non-existent student

    1. Test case: `delete s/9999`  
       **Expected:** Command rejected. Error message indicates Student ID not found. List unchanged.

1. Attempting invalid delete commands

    1. Test case: `delete`, `delete abc`, `delete 10000`  
       **Expected:** Command rejected. Error message explains invalid or missing Student ID.

---
### Managing ClassTags

#### Creating ClassTags

1. Creating a new class tag with a valid name

    1. Prerequisites: The tag name must be 1-30 characters, alphanumeric with underscores allowed.

    1. Test case: `tag -a t/Sec_3_A_Math`  
       **Expected:** New class tag created. Status message confirms creation, tag appears in list.

1. Creating a duplicate class tag

    1. Prerequisites: Class tag `Sec_3_A_Math` already exists (from previous test).

    1. Test case: `tag -a t/Sec_3_A_Math`  
       **Expected:** Command rejected. Error message indicates tag already exists.

---

#### Listing ClassTags

1. Listing all class tags

    1. Prerequisites: At least one class tag exists (e.g., `Sec_3_A_Math` from previous test).

    1. Test case: `tag -l`  
       **Expected:** Displays numbered list of all existing class tags.

--- 

#### Filtering Students by ClassTag

1. Filtering students with an existing class tag

    1. Prerequisites: Class tag exists and is assigned to at least one student (e.g., assign `Sec_3_A_Math` to a student via `add` or `edit`).

    1. Test case: `filter -t t/Sec_3_A_Math`  
       **Expected:** List shows only students with that class tag. Status message indicates number of students listed.

1. Filtering with non-existent class tag

    1. Test case: `filter -t t/NonExistentTag`  
       **Expected:** Command rejected. Error message indicates tag does not exist.

---

#### Deleting ClassTags

1. Deleting an unused class tag

    1. Prerequisites: Class tag exists but is not assigned to any students (remove from all students first via `edit`).

    1. Test case: `tag -d t/Sec_3_A_Math`  
       **Expected:** Class tag deleted. Status message confirms deletion.

1. Deleting a class tag still in use

    1. Prerequisites: Class tag is assigned to at least one student.

    1. Test case: `tag -d t/Sec_3_A_Math`  
       **Expected:** Command rejected. Error message indicates tag is still in use by students.

---
### Managing Fees

#### Marking a Student as Paid

1. Marking a student as PAID for a valid month

    1. Prerequisites: The student exists and has an enrolled month earlier than the target month (e.g., enrolled in August 2025).

    1. Test case: `fee -p s/0001 m/0925`  
       **Expected:** The student's payment status for September 2025 is marked as **PAID**.  
       A success message appears confirming the update.

1. Attempting to mark a month **before the student’s enrolled month**

    1. Test case: `fee -p s/0001 m/0725`  
       **Expected:** Command rejected. Error message states that months before the student’s enrolment cannot be marked.

1. Attempting to mark a **future month**

    1. Test case: `fee -p s/0001 m/1225` (if the current month is October 2025)  
       **Expected:** Command rejected. Error message states that future months cannot be marked as paid.

1. Attempting to skip an unpaid month

    1. Prerequisites: Ensure the student has an unpaid month before the target month (e.g., September 2025 is unpaid).

    1. Test case: `fee -p s/0001 m/1025`  
       **Expected:** Command rejected. Error message indicates that earlier unpaid months must be marked first.

---

#### Marking a Student as Unpaid

1. Marking a previously paid month as **UNPAID**

    1. Prerequisites: The student has been marked as paid for a month (e.g., September 2025).

    1. Test case: `fee -up s/0001 m/0925`  
       **Expected:** The payment status for September 2025 changes to **UNPAID**.  
       A success message confirms the correction.

1. Attempting to mark a **future month** as unpaid

    1. Test case: `fee -up s/0001 m/1225` (if the current month is October 2025)  
       **Expected:** Command rejected. Error message states that future months cannot be marked.

1. Attempting to mark a month **before enrolment**

    1. Test case: `fee -up s/0001 m/0725`  
       **Expected:** Command rejected. Error message indicates that months before enrolment cannot be marked.

1. Attempting to mark an already **UNPAID** month

    1. Test case: `fee -up s/0001 m/0925` (if it is already unpaid)  
       **Expected:** Command rejected. Error message indicates that the month is already unpaid.

---

#### Viewing a Student’s Payment History

1. Viewing complete payment history from enrolment

    1. Test case: `fee -v s/0001`  
       **Expected:** Displays all months from the enrolment month up to the current month, ordered from newest to oldest (current month first, enrolment month last). Each row shows the payment status (e.g., PAID or UNPAID) and whether it was **explicitly marked** or **set by default**.
   
1. Viewing payment history with a **custom start month**

    1. Test case: `fee -v s/0001 m/0525`  
       **Expected:** Displays payment history starting from the given month (or enrolment month if the given month is before enrolment),  ordered newest to oldest.   
       The system automatically adjusts the start month.

1. Attempting to view **future month history**

    1. Test case: `fee -v s/0001 m/1225` (if the current month is October 2025)  
       **Expected:** Command rejected. Error message states that future months cannot be displayed.

1. Attempting to view payment history of a **non-existent student**

    1. Test case: `fee -v s/9999`  
       **Expected:** Command rejected. Error message indicates that the student ID was not found.

---

#### Filtering Students by Payment Status

1. Filtering students who have **Paid**

    1. Test case: `filter -p m/0925`  
       **Expected:** Displays only students who have been marked as **PAID** for September 2025.  
       Status message confirms the number of students listed.

1. Filtering students who are **Unpaid**

    1. Test case: `filter -up m/0925`  
       **Expected:** Displays only students who are **UNPAID** for September 2025.  
       Status message confirms the number of students listed.

1. Attempting to filter using a **future month**

    1. Test case: `filter -p m/1225` (if the current month is October 2025)  
       **Expected:** Command rejected. Error message states that future months cannot be filtered.

1. Filtering with **no matching students**

    1. Test case: `filter -p m/0925` (when no students are paid for that month)  
       **Expected:** Displays message: “No matching students found.”

---
---

### Managing Performance Notes

#### Adding a Performance Note

1. Adding a new performance note for an existing student

    1. Prerequisites: Student exists with the specified Student ID (e.g., `0001`) and is assigned the relevant class tag (e.g., `Sec3_Maths`).<br>
       Use `add`/`edit` commands beforehand to set up the student and class tag if necessary.

    1. Test case: `perf -a s/0001 d/18092025 t/Sec3_Maths pn/Scored 85% on mock test`  
       **Expected:** Performance note added. Status message confirms success and the note appears in the performance panel.

1. Adding a duplicate performance note

    1. Prerequisites: Performance note already exists for the same student, date, and class tag as above.

    1. Test case: `perf -a s/0001 d/18092025 t/Sec3_Maths pn/Scored 85% on mock test`  
       **Expected:** Command rejected. Error message indicates a note already exists for that date and class tag.

1. Adding a performance note that exceeds the note length limit

    1. Test case: `perf -a s/0001 d/18092025 t/Sec3_Maths pn/` followed by a note longer than 200 characters  
       **Expected:** Command rejected. Error message indicates the performance note exceeds the maximum length.

---

#### Viewing Performance Notes

1. Viewing performance notes for a student with existing notes

    1. Prerequisites: Student `0001` has at least one performance note.

    1. Test case: `perf -v s/0001`  
       **Expected:** Performance panel updates to show all notes for the student in chronological order. Status message confirms number of notes shown.

1. Viewing performance notes for a student without notes

    1. Prerequisites: Student `0002` exists but has no performance notes.

    1. Test case: `perf -v s/0002`  
       **Expected:** Command succeeds. Status message indicates no performance notes found, and the performance panel is empty.

1. Viewing performance notes for a non-existent student

    1. Test case: `perf -v s/9999`  
       **Expected:** Command rejected. Error message indicates the student cannot be found.

---

#### Editing a Performance Note

1. Editing an existing performance note

    1. Prerequisites: Student `0001` has a performance note on `18092025` for `Sec3_Maths` with any content.

    1. Test case: `perf -e s/0001 d/18092025 t/Sec3_Maths pn/Improved to 90% after review`  
       **Expected:** Performance note updated. Status message confirms edit and performance panel reflects new note text.

1. Editing a non-existent performance note

    1. Test case: `perf -e s/0001 d/19092025 t/Sec3_Maths pn/Test`  
       **Expected:** Command rejected. Error message indicates no matching performance note exists for the given date and class tag.

1. Editing with a note exceeding the length limit

    1. Test case: `perf -e s/0001 d/18092025 t/Sec3_Maths pn/` followed by a note longer than 200 characters  
       **Expected:** Command rejected. Error message indicates the performance note exceeds the maximum length.

---

#### Deleting a Performance Note

1. Deleting an existing performance note

    1. Prerequisites: Student `0001` has a performance note on `18092025` for `Sec3_Maths`.

    1. Test case: `perf -d s/0001 d/18092025 t/Sec3_Maths`  
       **Expected:** Performance note removed. Status message confirms deletion and the note disappears from the performance panel.

1. Deleting a non-existent performance note

    1. Test case: `perf -d s/0001 d/19092025 t/Sec3_Maths`  
       **Expected:** Command rejected. Error message indicates no matching performance note exists.

1. Deleting a performance note for a non-existent student

    1. Test case: `perf -d s/9999 d/18092025 t/Sec3_Maths`  
       **Expected:** Command rejected. Error message indicates the student cannot be found.

---

### Managing Attendance

#### Marking Attendance as Present

1. Marking a student as present for a valid class and date

    1. Prerequisites: Student exists with the specified Student ID (e.g., `0001`) and is assigned the relevant class tag (e.g., `Sec3_Math`).

    1. Test case: `att -p s/0001 d/18092025 t/Sec3_Math`<br>
       **Expected:** Attendance marked as present. Status message confirms the attendance has been recorded.

1. Marking a student as present when already marked present

    1. Prerequisites: Student `0001` already has a present attendance record for `18092025` and `Sec3_Math`.

    1. Test case: `att -p s/0001 d/18092025 t/Sec3_Math`<br>
       **Expected:** Command rejected. Error message indicates the student is already marked as present for that date and class.

1. Attempting to mark attendance for a future date

    1. Test case: `att -p s/0001 d/31122025 t/Sec3_Math` (assuming current date is before 31 Dec 2025)<br>
       **Expected:** Command rejected. Error message indicates that attendance cannot be marked for future dates.

1. Attempting to mark attendance before student's enrolled month

    1. Prerequisites: Student `0001` is enrolled in September 2025 (enrolled month: `0925`).

    1. Test case: `att -p s/0001 d/15082025 t/Sec3_Math`<br>
       **Expected:** Command rejected. Error message indicates that attendance cannot be marked before the student's enrollment month.

1. Attempting to mark attendance with invalid date

    1. Test case: `att -p s/0001 d/30022025 t/Sec3_Math` (30th February does not exist)<br>
       **Expected:** Command rejected. Error message indicates the date is invalid.

    2. Test case: `att -p s/0001 d/2025-02-28 t/Sec3_Math` (wrong format, should be DDMMYYYY)<br>
       **Expected:** Command rejected. Error message indicates the date format is invalid.

1. Marking attendance for a class tag not assigned to the student

    1. Prerequisites: Student `0001` exists but is not assigned to class tag `Sec3_Science`.

    1. Test case: `att -p s/0001 d/18092025 t/Sec3_Science`<br>
       **Expected:** Command rejected. Error message indicates the class tag is not assigned to the student.

1. Marking attendance for a non-existent student

    1. Test case: `att -p s/9999 d/18092025 t/Sec3_Math`<br>
       **Expected:** Command rejected. Error message indicates the student cannot be found.

---

#### Marking Attendance as Absent

1. Marking a student as absent for a valid class and date

    1. Prerequisites: Student exists with the specified Student ID (e.g., `0001`) and is assigned the relevant class tag (e.g., `Sec3_Math`).

    1. Test case: `att -a s/0001 d/19092025 t/Sec3_Math`<br>
       **Expected:** Attendance marked as absent. Status message confirms the attendance has been recorded.

1. Changing attendance from present to absent

    1. Prerequisites: Student `0001` is currently marked as present for `18092025` and `Sec3_Math`.

    1. Test case: `att -a s/0001 d/18092025 t/Sec3_Math`<br>
       **Expected:** Attendance status updated from present to absent. Status message confirms the change.

1. Attempting to mark attendance as absent for a future date

    1. Test case: `att -a s/0001 d/31122025 t/Sec3_Math` (assuming current date is before 31 Dec 2025)<br>
       **Expected:** Command rejected. Error message indicates that attendance cannot be marked for future dates.

1. Attempting to mark attendance as absent before student's enrolled month

    1. Prerequisites: Student `0001` is enrolled in September 2025 (enrolled month: `0925`).

    1. Test case: `att -a s/0001 d/15082025 t/Sec3_Math`<br>
       **Expected:** Command rejected. Error message indicates that attendance cannot be marked before the student's enrollment month.

1. Attempting to mark attendance as absent with invalid date

    1. Test case: `att -a s/0001 d/32012025 t/Sec3_Math` (32nd January does not exist)<br>
       **Expected:** Command rejected. Error message indicates the date is invalid.

    2. Test case: `att -a s/0001 d/2025-01-31 t/Sec3_Math` (wrong format, should be DDMMYYYY)<br>
       **Expected:** Command rejected. Error message indicates the date format is invalid.

---

#### Viewing Attendance History

1. Viewing attendance history for a student with existing records

    1. Prerequisites: Student `0001` has at least one attendance record.

    1. Test case: `att -v s/0001`<br>
       **Expected:** Attendance panel updates to show all attendance records for the student sorted by date (newest first), then by class tag alphabetically. Status message confirms number of records shown.

1. Viewing attendance history for a student without records

    1. Prerequisites: Student `0002` exists but has no attendance records.

    1. Test case: `att -v s/0002`<br>
       **Expected:** Command succeeds. Status message indicates no attendance records found.

1. Viewing attendance history for a non-existent student

    1. Test case: `att -v s/9999`<br>
       **Expected:** Command rejected. Error message indicates the student cannot be found.

---

#### Deleting an Attendance Record

1. Deleting an existing attendance record

    1. Prerequisites: Student `0001` has an attendance record on `18092025` for `Sec3_Math`.

    1. Test case: `att -d s/0001 d/18092025 t/Sec3_Math`<br>
       **Expected:** Attendance record deleted. Status message confirms deletion and the record disappears from the attendance panel.

1. Deleting a non-existent attendance record

    1. Test case: `att -d s/0001 d/20092025 t/Sec3_Math`<br>
       **Expected:** Command rejected. Error message indicates no matching attendance record exists for the given date and class tag.

1. Deleting an attendance record for a non-existent student

    1. Test case: `att -d s/9999 d/18092025 t/Sec3_Math`<br>
       **Expected:** Command rejected. Error message indicates the student cannot be found.

---

### Saving data

1.	Dealing with missing data files
      1.	Simulate a missing data file:
      - Close the application.
      - Navigate to the data directory where the application stores its data files.
      - Delete the data file (e.g., addressbook.json).
      2.	Re-launch the application.   
      Expected: The application starts with an empty data set. A new data file is created automatically.
      2.	Dealing with corrupted data files
      1.	Simulate a corrupted data file:
      - Close the application.
      -	Open the data file (e.g., addressbook.json) with a text editor.
      -	Introduce invalid JSON syntax (e.g., delete a closing brace or add random text).
      -	Save the file.
      2.	Re-launch the application.  
      Expected: The application detects the corrupted data file and displays an error message in the terminal. It will then start with an empty data set.

