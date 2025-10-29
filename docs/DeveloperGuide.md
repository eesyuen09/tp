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

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

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

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores a list of `ClassTag` objects (which are contained in a `UniqueClassTagList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

**ClassTag Management:**
- Each `ClassTag` has a unique name that identifies a class or group
- `UniqueClassTagList` ensures no duplicate ClassTag names exist
- Students can be assigned multiple ClassTags via the `classTags` field in `Person`
- When a ClassTag is deleted, it must not be assigned to any student
- ClassTags provide a way to filter and organize students by class tag

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `ClassTag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `ClassTag` object per unique class tag, instead of each `Person` needing their own `ClassTag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)
* handles the `UniqueClassTagList` by saving/loading it as part of the `AddressBook` data. `JSONAdaptedClassTag` is used to convert between the `ClassTag` model type and its JSON-friendly format. `JSONSerializableAddressBook` includes the `classTags` list during serialization and deserialization. 

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Attendance Management

#### Overview

The Attendance Management feature enables tutors to track student attendance across different classes and dates. Each attendance record captures whether a student was present or absent for a specific class on a particular date.

The feature is implemented through an `AttendanceList` stored within each `Person` object. This list contains `Attendance` objects, each holding:
- A `Date` indicating when the class occurred
- A `ClassTag` indicating which class it was for
- An attendance status (Present/Absent)

The key design decision is to store attendance within each student's record rather than in a centralized attendance repository. This provides natural encapsulation and efficient retrieval of a student's complete attendance history.

#### Marking and Unmarking Attendance

Given below is an example usage scenario showing how the attendance marking mechanism works.

Step 1. The tutor launches the application. Each `Person` object in the `AddressBook` contains an `AttendanceList`, initially populated with any previously recorded attendance from storage.

Step 2. The tutor executes `att -m s/0001 d/10112025 t/Math` to mark student 0001 as present for Math class on 10 November 2025. The command is parsed by `AttendanceCommandParser`, which detects the `-m` flag and creates an `AttendanceMarkCommand`.

Step 3. `AttendanceMarkCommand` is executed. It first validates that:
   - Student with ID 0001 exists (via `Model#getPersonById()`)
   - The ClassTag "Math" exists in the system (via `Model#findClassTag()`)
   - The student is assigned to the Math class (via `Person#hasClassTag()`)

Step 4. The command checks for duplicate attendance records by examining the student's `AttendanceList`:
   - Searches for an existing attendance record with the same date (10/11/2025) and class (Math)
   - If a "Present" record already exists, the command returns a message indicating no change is needed
   - If an "Absent" record exists, it will be removed before adding the new "Present" record

Step 5. A new `Attendance` object is created with status "Present" and added to the student's `AttendanceList`. The model updates the filtered person list to reflect the changes.

Step 6. A `CommandResult` is returned with a success message: "Marked attendance for Student ID: 0001 on 10/11/2025 for Math"

The following sequence diagram shows how the marking attendance operation goes through the `Logic` component:

<puml src="diagrams/AttendanceMarkSequenceDiagram.puml" alt="Attendance Mark Sequence Diagram" />

<box type="info" seamless>

**Note:** The lifeline for `AttendanceCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.

</box>

**Unmarking attendance** (`att -u`) follows an identical execution flow, with the key difference being that `AttendanceCommandParser` creates an `AttendanceUnmarkCommand` and sets the attendance status to "Absent" instead of "Present".

#### Deleting Attendance Records

The `att -d` command allows tutors to completely remove an attendance record, useful for correcting mistakes or cancelled classes.

Step 1. The tutor executes `att -d s/0001 d/10112025 t/Math` to delete the attendance record.

Step 2. `AttendanceCommandParser` creates an `AttendanceDeleteCommand`.

Step 3. The command validates that an attendance record exists for the specified student, date, and class.

Step 4. If found, the attendance record is removed from the student's `AttendanceList`. If not found, a `CommandException` is thrown indicating no record exists.

<box type="info" seamless>

**Note:** Unlike mark/unmark which create or update records, delete completely removes the record. After deletion, the system has no record that attendance was ever taken for that date and class.

</box>

#### Viewing Attendance History

The `att -v` command displays a student's attendance records, optionally filtered by class.

Step 1. The tutor executes `att -v s/0001` to view all attendance records for student 0001, or `att -v s/0001 t/Math` to view only Math class attendance.

Step 2. `AttendanceCommandParser` creates an `AttendanceViewCommand`.

Step 3. The command retrieves the student's `AttendanceList` and filters the records:
   - If a ClassTag is specified, only records matching that class are included
   - Records from the last 6 months are shown
   - Records are sorted chronologically with the most recent first

Step 4. The filtered attendance records are formatted and displayed to the tutor, showing the date, class, and status (Present/Absent) for each record.

<box type="info" seamless>

**Note:** If the student has no attendance records, or no records matching the filter criteria, the system displays a message indicating no records are available.

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

### Activty Diagram: Editing Student ClassTags

The activity diagram below illustrates the workflow when a tutor edits a student's ClassTag assignments using the `edit` command:

<puml src="diagrams/EditStudentClassTagsActivityDiagram.puml" alt="EditStudentClassTagsActivityDiagram" />

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
| `* *`     | tutor who prioritise efficiency                          | recover recently deleted contact                          | fix accidental deletion                                                 |
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
1. Tutor requests to add a performance note for a student on a given date.
2. Tuto adds the performance note for the student.
3. Tuto shows a success message.

   Use case ends.

**Extensions**
* 1a. The provided student ID does not match any existing student.

  * 1a1. Tuto shows an error message.
  
    Use case ends.
  
* 1b. The command format is invalid.

  * 1b1. Tuto shows an error message with the correct usage format.
  
    Use case ends.
  
* 1c. Performance note exceeds character limit.

  * 1c1. Tuto shows an error message indicating character limit.
  
    Use case ends.
  
* 1d. A performance note for the student on the given date already exists.

  * 1d1. Tuto shows an error message.
  
    Use case ends.

    
**Use case: View performance notes of a student**

**MSS**
1. Tutor requests to view all performance notes of a student.
2. Tuto displays all performance notes of the student in chronological order, with newest at the top.

   Use case ends.

**Extensions**
* 1a. The provided student ID does not match any existing student.

    * 1a1. Tuto shows an error message.
  
      Use case ends.
* 1b. The command format is invalid.

    * 1b1. Tuto shows an error message with the correct usage format.
  
      Use case ends.
* 1c. The student has no performance notes.

    * 1c1. Tuto shows a message indicating that the student has no performance notes.
  
      Use case ends.
  
* 1d. Invalid index provided to view a specific performance note.

    * 1d1. Tuto shows an error message.
  
      Use case ends.



**Use case: Edit a performance note**

**MSS**
1. Tutor requests to edit a specific performance note of a student by index.
2. Tuto updates the performance note with the new content.
3. Tuto shows a success message.

   Use case ends.

**Extensions**
* 1a. The provided student ID does not match any existing student.

    * 1a1. Tuto shows an error message.
  
      Use case ends.
  
* 1b. The command format is invalid.

    * 1b1. Tuto shows an error message with the correct usage format.
  
      Use case ends.
  
* 1c. Performance note exceeds character limit.

    * 1d1. Tuto shows an error message indicating character limit.
  
      Use case ends.
  
* 1d. Invalid index provided to view a specific performance note.

    * 1e1. Tuto shows an error message.
  
      Use case ends.


**Use case: Delete a performance note**

**MSS**
1. Tutor requests to delete a specific performance note of a student by index.
2. Tuto deletes the performance note.
3. Tuto shows a success message.

    Use case ends.

**Extensions**
* 1a. The provided student ID does not match any existing student.

    * 1a1. Tuto shows an error message.
  
      Use case ends.
  
* 1b. The command format is invalid.

    * 1b1. Tuto shows an error message with the correct usage format.
  
      Use case ends.
  
* 1c. Invalid index provided to view a specific performance note.

    * 1c1. Tuto shows an error message.
  
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

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
