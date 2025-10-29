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

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

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

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

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

<puml src="diagrams/DeleteStudentSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

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

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores a list of `ClassTag` objects (which are contained in a `UniqueClassTagList` object).
* manages fee-related data through the `FeeTracker`, which records each student's monthly `FeeState` (`PAID` or `UNPAID`) and supports derived payment status queries.
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

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

---

#### Implementation

**Model Component:**
- `Person`: Represents a student with immutable fields:
    - Name, Phone, Email, Address, ClassTags, StudentId, AttendanceList, PerformanceList
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
- Deserialization ensures ClassTag references are restored for each student

**Logic Component:**

1. **AddCommand (`add`)**: Adds a new student
    - Validates all fields (Name, Phone, Email, Address)
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

---

#### Sequence Diagrams

**1. Adding a Student (`add`)**
<puml src="diagrams/AddStudentSequenceDiagram.puml" alt="AddStudentSequenceDiagram" />

**2. Editing a Student (`edit`)**
<puml src="diagrams/EditStudentSequenceDiagram.puml" alt="EditStudentSequenceDiagram" />

**3. Deleting a Student (`delete`)**
<puml src="diagrams/DeleteStudentSequenceDiagram.puml" alt="DeleteStudentSequenceDiagram" />

---

#### Design Considerations

**Aspect: Unique Student Identification**
- **Choice:** Use `StudentId` as a unique identifier
    - Pros:
        - Ensures uniqueness across all students
        - Simplifies operations like edit and delete
        - Auto-generation reduces manual errors
    - Cons:
        - Requires management of next available ID

**Aspect: Integration with ClassTags**
- **Choice:** Assign ClassTags during Add/Edit commands
    - Pros:
        - Reduces number of steps for tutors
        - Maintains referential integrity with `UniqueClassTagList`
    - Cons:
        - Increases responsibility of Add/Edit commands
        - Slightly more complex validation

**Aspect: Command Design**
- **Choice:** Separate commands for Add, Edit, Delete
    - Pros:
        - Clear responsibilities
        - Easy to maintain and extend
    - Cons:
        - Tutors need to remember three commands (mitigated by clear usage instructions)

---

#### Error Handling

**Adding Students:**
- Duplicate student record
- Invalid or missing field(s)
- Non-existent ClassTags
- Maximum number of students exceeded (StudentId > 9999)

**Editing Students:**
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

1. **AttendanceMarkCommand (triggered by `att -m`)**: Marks a student as present for a class on a specific date
    - Validates student exists and ClassTag exists
    - Prevents duplicate "Present" records (throws error if already marked present)
    - Replaces any existing "Absent" record for the same date and class with a "Present" record

2. **AttendanceUnmarkCommand (triggered by `att -u`)**: Marks a student as absent for a class on a specific date
    - Validates student exists and ClassTag exists
    - Prevents duplicate "Absent" records (throws error if already marked absent)
    - Replaces any existing "Present" record for the same date and class with an "Absent" record

3. **AttendanceDeleteCommand (triggered by `att -d`)**: Completely removes an attendance record
    - Validates the attendance record exists
    - Removes the record from the student's AttendanceList
    - Useful for correcting mistakes or handling cancelled classes

4. **AttendanceViewCommand (triggered by `att -v`)**: Displays a student's attendance history
    - Retrieves all attendance records for a specific student
    - Records are sorted by date, then by ClassTag name alphabetically

#### Sequence Diagram: Marking Attendance

The following sequence diagram illustrates the interactions when a tutor marks a student as present using the `att -m` command:

<puml src="diagrams/AttendanceMarkSequenceDiagram.puml" alt="Attendance Mark Sequence Diagram" />

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

**Aspect: How to represent "absent" status:**

* **Alternative 1 (current choice):** Flag-based commands - `att -m` for present, `att -u` for absent
  * Pros: Consolidated under single `att` command word, fewer top-level commands to remember, consistent with payment feature's flag-based design (`fee -p`, `fee -up`)
  * Cons: Potentially confusing terminology ("unmark" suggests deletion rather than marking absent), requires users to remember flag meanings

* **Alternative 2:** Separate command words - `present s/0001 d/10112025 t/Math` and `absent s/0001 d/10112025 t/Math`
  * Pros: Crystal clear intent from command word itself, highly intuitive (command literally describes the action), no ambiguity about what each command does
  * Cons: Increases top-level command count (now users need to remember `present`, `absent`, instead of just `att`), breaks the feature grouping pattern used throughout the app (attendance: `att`, performance: `perf`, fees: `fee`), inconsistent with the design goal of organizing related commands under a single namespace

* **Alternative 3:** Explicit status parameter - `att s/0001 d/10112025 t/Math status/present` or `att s/0001 d/10112025 t/Math status/absent`
  * Pros: Self-documenting commands, very clear semantics, easily extensible (could add `status/late` or `status/excused` in future)
  * Cons: More verbose, longer command syntax, requires typing "status/" every time

### Performance Management

#### Overview

The Performance Notes Management feature lets tutors capture qualitative feedback about a student's progress for specific
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
- `JsonAdaptedPerformanceNote`: Serialises/deserialises `PerformanceNote` objects to and from JSON, validating date, class tag, and note length constraints during conversion
- Performance notes are persisted as part of each student's JSON record via `JsonAdaptedPerson`, ensuring notes stay in sync with the owning student
- During deserialisation, class tags referenced in performance notes are re-validated against the student's tag set so that orphaned notes cannot be reconstructed

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

### ClassTag Management

#### Overview

The ClassTag feature allows tutors to organize students into classes or groups. Each ClassTag represents a distinct class (e.g., "Sec_3_A_Math", "P5_Science") that can be assigned to multiple students.

#### Implementation

ClassTag management is implemented through several key components:

**Model Component:**
- `ClassTag`: Represents a class with a unique name
- `UniqueClassTagList`: Maintains all ClassTags in the system, ensuring no duplicates
- The `Model` interface provides methods:
    - `hasClassTag(ClassTag)`: Checks if a ClassTag exists
    - `addClassTag(ClassTag)`: Adds a new ClassTag
    - `deleteClassTag(ClassTag)`: Removes a ClassTag
    - `findClassTag(String)`: Finds and returns a ClassTag by its name
    - `getClassTagList()`: Returns an unmodifiable list of all ClassTags
- `Person` objects maintain a `Set<ClassTag>` field that references ClassTags from the central `UniqueClassTagList`
  
**Storage Component:**
- `JsonAdaptedClassTag`: Converts ClassTag objects to/from JSON format for persistence
- `JsonSerializableAddressBook`: Serializes both the central ClassTag list and student-ClassTag associations
- ClassTags are persisted in two ways:
    1. As a complete list in the `classTags` field of the address book
    2. As references within each student's `tags` field
- During deserialization, ClassTags are loaded first into the system, then students reference them by name

**Logic Component:**

The following commands handle ClassTag operations:

1. **AddClassTagCommand (triggered by `tag -a`)**: Creates a new ClassTag in the system
    - Validates the ClassTag name format (1-30 alphanumeric characters and underscores)
    - Checks for duplicates via `Model#hasClassTag()`
    - Adds to `UniqueClassTagList` via `Model#addClassTag()`

2. **DeleteClassTagCommand (triggered by `tag -d`)**: Deletes an existing ClassTag
    - Validates the ClassTag exists via `Model#findClassTag()`
    - Ensures no students are currently assigned to it
    - Removes from `UniqueClassTagList` via `Model#deleteClassTag()`

3. **ListClassTagCommand (triggered by `tag -l`)**: Lists all ClassTags in the system
    - Retrieves all ClassTags from the Model via `Model#getClassTagList()`
    - Displays them in a numbered list

4. **ClassTagFilterCommand (triggered by `filter -t`)**: Filters students by ClassTag
    - Validates the ClassTag exists via `Model#findClassTag()`
    - Updates the filtered person list to show only students with that ClassTag

5. **AddCommand**: Adds a new student with optional ClassTag assignments
    - Validates all specified ClassTags exist before creating the student
    - Links student to ClassTags via references

6. **EditCommand**: Edits student details including ClassTag assignments
    - Can add, remove, or replace ClassTag assignments
    - Validates all ClassTags exist before updating
    - Empty ClassTag list (`t/`) removes all ClassTag assignments

#### Sequence Diagram: Adding a ClassTag

The following sequence diagram illustrates the interactions between components when a tutor creates a new ClassTag using the `tag -a` command:

<puml src="diagrams/AddClassTagSequenceDiagram.puml" alt="AddClassTagSequenceDiagram" />

#### Sequence Diagram: Filtering Students by ClassTag

The following sequence diagram illustrates how the system filters students by a specific ClassTag:

<puml src="diagrams/ClassTagFilterSequenceDiagram.puml" alt="ClassTagFilterSequenceDiagram" />

#### Activity Diagram: Editing Student ClassTags

The activity diagram below illustrates the workflow when a tutor edits a student's ClassTag assignments using the `edit` command:

<puml src="diagrams/EditClassTagOfExistingStudentActivityDiagram.puml"/>

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
        - Requires additional validation when assigning ClassTags to students
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
        - Slightly more complex implementation (need to override both `equals()` and `hashCode()` consistently)
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
- Attempting to assign a ClassTag that doesn't exist

**Filtering by ClassTag:**
- Non-existent ClassTag name
- Invalid command format
- Invalid ClassTag format

**General Validation Rules:**
- All tag commands enforce **strict parameter checking** — any extra text beyond the expected format is rejected
- When multiple flags are present, only the **first valid flag** determines the command type; subsequent flags are treated as invalid parameters and cause the command to fail
- Tag names are trimmed of leading/trailing whitespace before validation

Each validation error provides clear, actionable feedback to help users correct their input.

---
### Fee Management

#### Overview

The Fee Management feature allows tutors to track, update, and review students’ monthly tuition fee payments.  
Each student has a corresponding `FeeState` (**PAID** or **UNPAID**) for each month starting from their `enrolledMonth`.  
This feature is managed by the central `FeeTracker` component within the `Model`.

#### Implementation

Fee Management is implemented through several key components:

**Model Component:**

- `FeeTracker`: Serves as the central manager for all student fee records.
      - Internally stores data in a nested structure:  
      `Map<StudentId, Map<Month, FeeState>>`, where each student’s fee history is organized by month.
    - The tracker only stores months that have been explicitly marked.
      Any month after enrollment without a record is automatically considered UNPAID by default.
    - Months **before** the student’s `enrolledMonth` are **not tracked** and return no fee status.
    - Provides methods to generate **payment history** between two months.
    - This design avoids redundant data storage and enables on-demand computation of fee states, ensuring efficient lookups and minimal memory use.
    - The `Model` interacts with the `FeeTracker` when marking payments, undoing payments, filtering Paid/Unpaid students, or retrieving historical fee data.

- `FeeState`: Represents a student’s payment status for a particular month. (either **PAID** or **UNPAID**)

- `Month`: Represents a calendar month in `MMYY` format and provides utilities for date comparison and traversal.

- `Person`: Each student stores an `enrolledMonth`, which defines when fee tracking begins.  
  Fee states are only derived for months on or after this enrollment month.

- The `Model` interface provides methods:
    - `markPaid(StudentId, Month)`: Marks a student’s fee as Paid for a given month.
    - `markUnpaid(StudentId, Month)`: Marks a student’s fee as Unpaid for a given month.
    - `paidStudents(Month)`: Returns a predicate for filtering students who have Paid for the specified month.
    - `unpaidStudents(Month)`: Returns a predicate for filtering students who have not Paid for the specified month.
    - `getCurrentFeeState(Person)`: Returns the student’s fee state for the current month.
    - `feeStateVersionProperty()`: Returns a read-only observable property that increments whenever fee data changes, allowing the UI to refresh automatically.

---

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
    - Any student without explicit fee entries is assumed to have **no recorded payments**, and their post-enrollment months are treated as **UNPAID** by default.

---

**Logic Component:**

The following commands handle Fee Management operations:

1. **FeeMarkPaidCommand (triggered by `fee -p`)**: Marks a student’s fee as **PAID** for a specific month.
    - Validates that the student exists via `Model#getPersonById()`.
    - Ensures all previous months up to the target month are already **PAID**.
    - Updates the `FeeTracker` via `Model#markPaid()` and refreshes the UI.

2. **FeeMarkUnpaidCommand (triggered by `fee -up`)**: Marks a student’s fee as **UNPAID** for a specific month.
    - Validates that the student exists via `Model#getPersonById()`.
    - Updates the `FeeTracker` via `Model#markUnpaid()` and refreshes the UI.

3. **FeeFilterPaidCommand (triggered by `filter -p`)**:  
   Filters students who have **Paid** for a specified month.
    - Validates that the input month is valid and not in the future.
    - Updates the filtered student list to display only students marked as **PAID** for that month.

4. **FeeFilterUnpaidCommand (triggered by `filter -up`)**:  
   Filters students who have **not Paid** (Unpaid) for a specified month.
    - Validates that the input month is valid and not in the future.
    - Updates the filtered student list to display only students marked as **UNPAID** for that month.

5. **FeeViewCommand (triggered by `fee -v`)**:  
   Displays a student’s **payment history** across a specified range of months.
    - Validates that the student exists and that the month range is valid.
    - Retrieves payment data using `FeeTracker#getPaymentHistory()`.
    - Outputs a chronological list of months with corresponding fee states.

#### Sequence Diagram: Marking a Student as Paid

The following sequence diagram illustrates how the system processes the `fee -p` command to mark a student’s fee as Paid:

<puml src="diagrams/FeeMarkPaidSequenceDiagram.puml" alt="FeeMarkPaidSequenceDiagram" />

#### Sequence Diagram: Filtering Students by Payment Status

The following sequence diagram illustrates how the system filters students by payment status:

<puml src="diagrams/FeeFilterSequenceDiagram.puml" alt="FeeFilterSequenceDiagram" />

#### Activity Diagram: Viewing a Student’s Payment History

The activity diagram below illustrates the workflow for viewing a student’s fee history using the `fee -v` command:

<puml src="diagrams/FeeViewActivityDiagram.puml" alt="FeeViewActivityDiagram" />

#### Design Considerations

#### **Aspect: Sequential Payment Enforcement (Marking later months as PAID)**

**Alternative 1 (current choice):**  
Require all previous months to be **PAID** before a tutor can mark a later month as **PAID**.

- **Pros:**
    - Reflects **real-world payment flow** — students should settle earlier dues before paying for upcoming lessons.
    - Prevents **skipped or inconsistent payment sequences**, such as marking August as PAID while July remains UNPAID.
    - Ensures **data integrity** — once a month is marked PAID, all earlier months are guaranteed to be cleared.
    - Simplifies **validation and reporting**, since the payment timeline always progresses forward without gaps.
    - Makes it easy to determine a student’s **most recent paid month** at a glance.

- **Cons:**
    - Tutors cannot record payments **out of sequence** (e.g., skipping July and paying for August directly).
    - Adds a small validation step when marking multiple months.

---

**Alternative 2:**  
Allow tutors to mark any month as **PAID**, regardless of whether earlier months are still UNPAID.

- **Pros:**
    - Provides **maximum flexibility** for unusual or irregular payment situations.
    - Allows quick entry for multiple months without enforcing chronological checks.

- **Cons:**
    - Can result in **gaps or inconsistencies** in the payment timeline — e.g., later months marked PAID while earlier ones remain UNPAID.
    - Makes it **easier for tutors to overlook unpaid months**, leading to incomplete financial records.
    - Reduces **data reliability**, since it becomes unclear whether all payments up to a given point have been fully settled.
---
#### **Aspect: Future Month Restriction (Advance Payments)**

**Alternative 1 (current choice):**  
Restrict marking months that are **beyond the current calendar month**.

- **Pros:**
    - Prevents **premature or speculative payments**, maintaining realistic, time-based validation.
    - Keeps the system aligned with **actual payment periods** and avoids confusion with future billing.
    - Simplifies error handling and prevents data entry mistakes.

- **Cons:**
    - Tutors cannot record **advance payments** for future months, even if a student has pre-paid.
    - May require future system updates to support legitimate early payments.

---

**Alternative 2:**  
Allow tutors to mark **future months** as PAID, provided that all previous months have already been settled.

- **Pros:**
    - Supports **prepaid tuition scenarios**, where students pay several months in advance.
    - Convenient for tutors managing **long-term payment plans** or scheduling future billing cycles in advance.
    - Reduces repetitive data entry when tutors want to record multiple future payments at once.

- **Cons:**
    - Increases the risk of **accidental marking of future months**, especially if tutors mistype the month or forget the current date.
    - Requires additional **validation safeguards** (e.g., confirmation prompts or warning messages) to prevent unintentional future entries.
    - Adds complexity to the **FeeViewCommand** logic, since it must distinguish between completed months and prepaid future months when displaying fee history.

#### **Aspect: Backdated Correction (Marking an earlier month as UNPAID)**

**Alternative 1 (current choice):**  
Allow tutors to mark an earlier month as **UNPAID**, even if later months are already **PAID**.

- **Pros:**
    - Supports **real-world correction scenarios** — e.g., the tutor realizes a payment was incorrectly recorded.
    - Allows quick edits without needing to unmark all subsequent months first.
    - Keeps flexibility: once corrected, the tutor must still settle earlier UNPAID months before new payments are accepted.

- **Cons:**
    - May cause a **temporary inconsistency** (e.g., later months PAID while an earlier one is UNPAID) until resolved.
    - Might confuse tutors reviewing the timeline during the correction phase.

---

**Alternative 2:**  
Block marking an earlier month as **UNPAID** if any later month is already **PAID**.

- **Pros:**
    - Maintains a **strictly chronological** payment timeline with no anomalies.
    - Simplifies record validation and summary generation.

- **Cons:**
    - Restrictive — tutors cannot fix genuine mis-entries without first unmarking all later months.


---

#### Error Handling

Fee Management includes comprehensive validation across all operations:

**Marking Fees**
- Invalid or missing `StudentId`
- Month before enrollment or after the current month
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

1. **Bulk attendance marking for entire class:** Currently, tutors must mark attendance for each student individually using `att -m s/STUDENT_ID d/DATE t/CLASS`. For a class with 20-30 students, this becomes tedious and time-consuming. We plan to add a bulk marking feature that allows tutors to mark attendance for all students in a specific class at once. For example, `att -m d/10112025 t/Math` would mark all students enrolled in the Math ClassTag as present for that date. This would significantly reduce the time needed to take attendance at the beginning of each lesson.
2. **Individual class tag assignment and unassignment on top of current add/edit:** Currently, when editing a student's class tags using the edit command, all existing tags are replaced with the new list provided (or cleared if t/ is empty). This makes it cumbersome to add or remove a single tag without re-specifying all others. We plan to introduce new commands tag -assign s/STUDENT_ID t/TAG_NAME and tag -unassign s/STUDENT_ID t/TAG_NAME that allow adding or removing individual tags without affecting previously assigned ones. For example, tag -assign s/0001 t/Sec_3_A_Math would add the "Sec_3_A_Math" tag to student 0001 if they don't already have it, leaving other tags intact. Similarly, tag -unassign s/0001 t/Sec_3_A_Math would remove only that tag. Success messages would confirm the action, e.g., "Successfully assigned class tag [Sec_3_A_Math] to student ID 0001." Error messages would handle cases like non-existent students or tags. This enhancement addresses the frequent need for precise, incremental changes to student records, improving tutor workflow efficiency.
3. **ClassTag renaming:** Currently, once a ClassTag is created, its name cannot be changed. If a tutor wishes to rename a ClassTag (e.g., from "Sec_3_A_Math" to "Sec_3_A_Advanced_Math"), they must delete the existing ClassTag and create a new one. This process is cumbersome and risks losing the association with students if not handled carefully. We plan to implement a `tag -r` command that allows tutors to rename an existing ClassTag while preserving all student associations. For example, `tag -r oldt/Sec_3_A_Math newt/Sec_3_A_Advanced_Math` would rename the ClassTag accordingly. This feature would enhance flexibility in managing class names as course structures evolve.
4. **Introduce third fee state — WAIVED/SKIPPED:**  
   At present, fee tracking uses only two states: **PAID** and **UNPAID**.  
   In future releases, we plan to introduce a third state, **WAIVED** (or **SKIPPED**), to handle non-billable months such as holidays, term breaks, or periods without lessons.  
   This enhancement will:
    - Accurately reflect months where no tuition fees are due.
    - Allow tutors to “skip” months without breaking the sequential payment validation rule.
    - Improve clarity in fee reports by distinguishing “not billed” months from “unpaid” ones.
   This addition will also enhance flexibility in long-term record management and improve real-world applicability for tutoring scenarios involving variable schedules.
5. **Integrate Fee and Attendance Systems:**  
   Currently, fee tracking and attendance operate independently.  
   We plan to introduce light integration between both modules to make payment tracking more context-aware.
    - When viewing a student’s fee history, tutors will also see the **number of lessons held** for each month.
    - When marking a month as **PAID** with no recorded attendance, the system will show a **confirmation prompt** to avoid mistakes.
    - When marking a month as **UNPAID** while lessons are recorded, a **reminder** will appear to alert the tutor of possible inconsistencies.
    - Months **without any recorded attendance** will automatically be assigned a **WAIVED** status instead of UNPAID, ensuring skipped months (e.g., holidays or term breaks) do not block future payments.
   This enhancement improves **accuracy** and **consistency** between financial and attendance records, while keeping full flexibility for tutors to override when necessary.
4. **Unified student history view (view s/STUDENT_ID):** Introduce a consolidated view command that shows every performance note, attendance record, and fee transaction for the specified student, allowing tutors to review a learner’s full journey without hopping between modules.
5. **Targeted performance and attendance filters (perf -v / att -v):** Extend the existing view flags to accept optional m/MMYY or t/CLASS_TAG parameters so tutors can zero in on a specific month or class when analysing historical performance or attendance data.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


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

**Value proposition**: Helps freelance tutors manage students, parents, schedules, and tuition fees in one place, streamlining lesson planning, tracking progress, and simplifying communication, so they can focus on teaching, not admin.



### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority  | As a …​                                    | I want to …​                                              | So that I can…​                                                         |
|-----------|------------------------------------------- |-----------------------------------------------------------|-------------------------------------------------------------------------|
| `* * * `  | tutor handling lesson fees                 | tag a student as paid for a given month                   | keep track of students who have settled their tuition fees              |
| `* * *`   | tutor handling lesson fees                 | tag a student as unpaid for a given month                 | identify students who still owe lesson fees                             |
| `* * *`   | tutor handling lesson fees                 | filter students who have paid by month                    | view all students who have completed payment for that month at a glance |
| `* * *`   | tutor handling lesson fees                 | filter students who have not paid by month                | follow up with students who have outstanding tuition fees               |
| `* * *`   | tutor handling lesson fees                 | view a student's payment history up to the current month  | review their past payment behaviour and identify missed months          |
| `* * *`   | tutor who teaches multiple classes         | create a class tag                                        | keep track of a new class I am teaching                                 |
| `* * *`   | tutor who teaches multiple classes         | assign class tags to a student during creation or editing | manage all students of the same subject together                        |
| `* * *`   | tutor who teaches multiple classes         | remove class tags from a student through editing          | remove students not in a particular class                               |
| `* * *`   | tutor who teaches multiple classes         | filter students by class tag (eg. Sec_3_A_Math)           | I can focus on a precise teaching group                                 |
| `* * *`   | tutor who teaches multiple classes         | list all the class tags                                   | I can know what classes I am teaching                                   |
| `* * `    | tutor who teaches multiple classes         | delete a class tag                                        | keep only the classes I am still teaching                               |
| `* * * `  | tutor     | add a performance note for a student on a given date      | I can record their progress                                             |
| `* * * `  | tutor     | view all performance notes for a student                  | I can review their progress                                             |
| `* * * `  | tutor     | edit a specific performance note for a student            | I can correct or update it                                              |
| `* * * `  | tutor     | delete a specific performance note for a student          | I can remove it if needed                                               |
| `* * *`   | tutor who teaches multiple classes         | take attendance of each student                           | I can track their attendance record                                     |
| `* * *`   | tutor who teaches multiple classes         | view students' attendance history                         | I can track if students are consistently attending lessons              |
| `* * *`   | tutor who teaches multiple classes         | unmark a student's attendance                             | correct mistakes or changes if attendance was marked wrongly            |
| `* *`     | tutor who teaches multiple classes         | delete an attendance record                               | remove records for cancelled classes or fix erroneous entries           |
| `* *`     | new tutor user                                           | view sample data                                          | understand how the app looks when populated                             |
| `* *`     | tutor starting fresh                                     | purge sample/old data                                     | start fresh with only my real student info                              |                                                                  |
| `* * *`   | tutor managing students                                  | add students                                              | quickly add my students into the address book                           |
| `* * *`   | tutor managing students                                  | view students                                             | see all the students I am teaching and their details at a glance        |
| `* *`     | tutor managing students                                  | delete students                                           | remove students who are no longer taking lessons                        |
| `* * *`   | tutor handling many students across classes and subjects | edit student information                                  | update my contact list                                                  |
| `* * *`   | tutor handling many students across classes and subjects | search for a student by name                              | quickly locate their information                                        |


### Use cases

(For all use cases below, the **System** is the `Tuto` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Create a new class tag**

**MSS**
1.  Tutor requests to create a new class tag, providing a valid tag name.
2.  Tuto creates the new class tag
3.  Tuto shows a success message.

    Use case ends.

**Extensions**
* 1a. The provided class tag name already exists.

    * 1a1. Tuto shows an error message.

      Use case ends.

* 1b. The provided class tag name is invalid.

    * 1b1. Tuto shows an error message

      Use case ends.

* 1c.  The command format is invalid.

    * 1c1. Tuto shows an error message with the correct usage format.

      Use case ends.

**Use case: Delete a class tag**

**MSS**
1.  Tutor requests to delete an existing class tag, providing its name.
2.  Tuto deletes the class tag
3.  Tuto shows a success message.

    Use case ends.

**Extensions**
* 1a. The specified class tag does not exist.

    * 1a1. Tuto shows an error message.

      Use case ends.

* 1b. The specified class tag is still assigned to one or more students.

    * 1b1. Tuto shows an error message.

      Use case ends.

* 1c. The command format is invalid.

    * 1c1. Tuto shows an error message with the correct usage format.

      Use case ends.

**Use case: List all class tags**

**MSS**
1.  Tutor requests to list all class tags.
2.  Tuto shows a list of all existing class tags.

    Use case ends.

**Extensions**
* 1a.  The command format is invalid.

    * 1a1. Tuto shows an error message with the correct usage format.

      Use case ends.

* 2a. The list of class tags is empty.

    * 2a1. Tuto shows a message indicating that no tags have been created.

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

* 1d. The specified class tag does not exist.

    * 1d1. Tuto shows an error message indicating class tag not found.

      Use case ends.

* 1e. The provided date fails validation.

    * 1e1. The date does not correspond to a real calendar day (e.g. 30th February).

        * 1e3a. Tuto shows an error message indicating invalid date.

          Use case ends.

* 1f. Performance note exceeds the 200-character limit.

    * 1f1. Tuto shows an error message indicating character limit.

      Use case ends.

* 1g. Performance note does not exist for the specified student on the given date for the given class.

    * 1g1. Tuto shows an error message.

      Use case ends.

**Use case: Delete a performance note**

**MSS**
1. Tutor requests to delete a specific performance note of a student on a specific date for a specific class.
2. Tuto deletes the performance note.
3. Tuto shows a success message.

    Use case ends.

**Extensions**
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

* 1e. The provided date fails validation.

    * 1e1. The date does not correspond to a real calendar day (e.g. 30th February).

        * 1e1a. Tuto shows an error message indicating invalid date.

          Use case ends.

* 1f. A performance note does not exist for the same student on the same date for the same class.

    * 1f1. Tuto shows an error message.

      Use case ends.



**Use case: Mark Student as Paid**

**Guarantees**
1. A Paid status for the particular month is recorded for the given student only if the inputs are valid and the student is not already marked Paid for that month.

**MSS**
1. Tutor requests to mark a student as paid for a specific month.
2. Tuto records the Paid status for that student at that specific month.
3. Tuto shows an success message.

   Use case ends.

**Extensions**
* 1a. The provided student ID does not match any existing student.
    * 1a1. Tuto shows an error message.

      Use case ends.
- 1b. The command format is invalid.
    - 1b1. Tuto shows an error message with the correct usage format.

      Use case ends.
- 2a. The student is already marked *Paid* for that month.
    - 2a1. Tuto shows an error message.

      Use case ends.


**Use case: Mark Student as Unpaid**

**Guarantees**
1. An Unpaid status for the particular month is recorded for the given student only if the inputs are valid and the student is not already marked Unpaid for that month.

**MSS**
1. Tutor requests to mark a student as Unpaid for a specific month.
2. Tuto records the Unpaid status for that student at that specific month.
3. Tuto shows an success message.

   Use case ends.

**Extensions**
* 1a. The provided student ID does not match any existing student.
    * 1a1. Tuto shows an error message.

      Use case ends.
* 1b. The command format is invalid.
    * 1b1. Tuto shows an error message with the correct usage format.

      Use case ends.
* 2a. The student is already marked Unpaid for that month.
    * 2a1. Tuto shows an error message.

      Use case ends.


**Use case: Filter Paid Students by Month**

**Guarantees**
1. Displays a list of students that are marked as Paid for the given month.

**MSS**
1. Tutor requests to filter students that are marked as Paid for a specific month.
2. Tuto displays a list of students that are marked as Paid for that month.

   Use case ends.

**Extensions**
* 1a. The command format is invalid.
    * 1a1. Tuto shows an error message with the correct usage format.

      Use case ends.
  
* 2a. No Paid students found for that month.
    * 2a1. Tuto displays a message indicating no records found.

      Use case ends.


**Use case: Filter Unpaid Students by Month**

**Guarantees**
1. Displays a list of students that are marked as Unpaid for the given month.

**MSS**
1. Tutor requests to filter students that are marked as Unpaid for a specific month.
2. System displays a list of students that are marked as Unpaid for that month.

   Use case ends.

**Extensions**
* 1a. The command format is invalid.
    * 1a1. Tuto shows an error message with the correct usage format.

      Use case ends.
  
* 2a. No Unpaid students found for that month.
    * 2a1. Tuto displays a message indicating no records found.

      Use case ends.


**Use case: View Payment History of a Student**

**Guarantees**
1.	Displays the payment history of the student for up to six months prior to the current month.

**MSS**
1. Tutor requests to view the payment history of a student.
2. System retrieves and the student’s month-by-month payment status for the past six months, up to the current month.

   Use case ends.

**Extensions**
* 1a. The student ID is invalid or missing.
    * 1a1. Tuto shows an error message.

      Use case ends.
  
* 2a. The student has no payment records yet.
    * 2a1. Tuto displays a message indicating no records found.

      Use case ends.


**Use case: Mark attendance for a student**

**MSS**
1. User marks attendance of student.
2. Tuto records the attendance as present for the specified student and date.
3. Tuto confirms that the attendance has been recorded.

   Use case ends.

**Extension**
* 1a. The command format is invalid.
    * 1a1. Tuto shows an error message with the correct usage format.

      Use case ends.

* 2a. An attendance record for the same student and date already exists with the ***Present*** status.
    * 2a1. Tuto informs the user that no change is necessary.

      Use case ends.


**Use case: Unmark attendance for a student**

**MSS**
1. User unmarks attendance of a student.
2. Tuto records the attendance as absent for the specified student and date.
3. Tuto confirms that the attendance has been updated.

   Use case ends.

**Extensions**
* 1a. The command format is invalid.
    * 1a1. Tuto shows an error message with the correct usage format.

      Use case ends.

* 2a. The attendance record for the same student and date already exists with the ***Absent*** status or no prior record exists.
    * 2a1. Tuto informs the user that no change is necessary.

      Use case ends.

**Use case: Delete an attendance record**

**MSS**
1. User deletes an attendance record for a student on a specific date and class.
2. Tuto deletes the attendance record.
3. Tuto confirms that the attendance record has been deleted.

   Use case ends.

**Extensions**
* 1a. The command format is invalid.
    * 1a1. Tuto shows an error message with the correct usage format.

      Use case ends.

* 2a. No attendance record exists for the specified date and class.
    * 2a1. Tuto shows an error message.

      Use case ends.

**Use case: View a student's attendance history**

**MSS**
1. User views the attendance history of a student.
2. Tuto retrieves the attendance record for that student.
3. Tuto displays the attendance history in chronological order.

   Use case ends.

**Extensions**
* 1a.  The command format is invalid.

    * 1a1. Tuto shows an error message with the correct usage format.

      Use case ends.

* 2a. No attendance records exist for the student.
    * 2a1. System informs the user that no records are available.

      Use case ends.

**Use case: Add Student**

**MSS**

1. Tutor requests to create a new student, providing all required details and one or more optional class tags.
2. Tuto adds student to records.
3. Tuto confirms the student has been added.

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

1. User retrieves student list.
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

1. User deletes student.
2. Tuto removes student from records.
3. Tuto confirms the student's has been deleted.

   Use case ends.

**Extensions**

* 1a. The command format is invalid
    * 1a1. Tuto shows an error message with the correct usage format.

      Use case ends.

**Use case: Search Student**

**MSS**

1. User searches student by name.
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

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Student ID**: A 4-digit unique numeric identifier (0000–9999) assigned to each student when added to the system.
* **Payment History**: A record that shows a student’s Paid or Unpaid fee status for each month, covering up to the six most recent months before the current month.
* **Performance note**: A short textual record of a student's performance on a given date
* **Attendance History**: A record that shows a student's attendance history, covering up to the six most recent months before the current month.
* **Executable JAR**: A Java Archive file that contains all compiled classes and resources, which can be run directly without installation.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
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
### Managing Class Tags

#### Creating Class Tags

1. Creating a new class tag with a valid name

    1. Prerequisites: The tag name must be 1-30 characters, alphanumeric with underscores allowed.

    1. Test case: `tag -a t/Sec_3_A_Math`  
       **Expected:** New class tag created. Status message confirms creation, tag appears in list.

1. Creating a duplicate class tag

    1. Prerequisites: Class tag `Sec_3_A_Math` already exists (from previous test).

    1. Test case: `tag -a t/Sec_3_A_Math`  
       **Expected:** Command rejected. Error message indicates tag already exists.

---

#### Listing Class Tags

1. Listing all class tags

    1. Prerequisites: At least one class tag exists (e.g., `Sec_3_A_Math` from previous test).

    1. Test case: `tag -l`  
       **Expected:** Displays numbered list of all existing class tags.

--- 

#### Filtering Students by Class Tag

1. Filtering students with an existing class tag

    1. Prerequisites: Class tag exists and is assigned to at least one student (e.g., assign `Sec_3_A_Math` to a student via `add` or `edit`).

    1. Test case: `filter -t t/Sec_3_A_Math`  
       **Expected:** List shows only students with that class tag. Status message indicates number of students listed.

1. Filtering with non-existent class tag

    1. Test case: `filter -t t/NonExistentTag`  
       **Expected:** Command rejected. Error message indicates tag does not exist.

---

#### Deleting Class Tags

1. Deleting an unused class tag

    1. Prerequisites: Class tag exists but is not assigned to any students (remove from all students first via `edit`).

    1. Test case: `tag -d t/Sec_3_A_Math`  
       **Expected:** Class tag deleted. Status message confirms deletion.

1. Deleting a class tag still in use

    1. Prerequisites: Class tag is assigned to at least one student.

    1. Test case: `tag -d t/Sec_3_A_Math`  
       **Expected:** Command rejected. Error message indicates tag is still in use by students.

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

### Saving data

1. Dealing with missing/corrupted data files

    1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_

