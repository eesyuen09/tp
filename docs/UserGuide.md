---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# Tuto User Guide

Tuto is a **desktop app for managing contacts, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, Tuto can get your contact management tasks done faster than traditional GUI apps.

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar tuto.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the Address Book.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Each student added will be automatically assigned a unique `Student ID` in a 4-digit format, which is used to identify the student for edit and delete commands.
  e.g. `0020`, `2413`

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</box>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`

---
## Student Management

The Student Management commands allow you to **add, edit, and delete students** in the address book.  
Each student added is automatically assigned a unique **Student ID** (in 4-digit format), which is used in other commands such as fees, attendance, and performance tracking.

<box type="info" seamless>

**Overview of Student Management Commands**

| Command                                                                       | Description                             |
|-------------------------------------------------------------------------------|-----------------------------------------|
| `add n/NAME p/PHONE e/EMAIL a/ADDRESS [t/CLASS_TAG]...`                       | Add a new student to the address book   |
| `list`                                                                        | List all students                       |
| `edit s/STUDENT_ID [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/CLASS_TAG]...` | Edit details of an existing student     |
| `find KEYWORD [MORE_KEYWORDS]`                                                | Find students by name                   |
| `delete s/STUDENT_ID`                                                         | Delete a student using their Student ID |
| `clear`                                                                       | Clear all student entries               |

</box>

### 1. Adding a student: `add`

Adds a student to the address book.

Format: `add n/NAME p/PHONE e/EMAIL a/ADDRESS [t/CLASS_TAG]...`

- Adds a new student with the specified name, phone number, email, and address.
- Class tags (`t/CLASS_TAG`) are optional; a student can have any number of tags, including none.
- Tags must exist in the system. If a specified tag does not exist, the command will fail.

<box type="tip" seamless>

**Tip:** You can add multiple tags by specifying `t/TAG1 t/TAG2 ...`.  
A student can also have zero tags.
</box>

**Examples:**

- `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`  
  Adds a student with no tags.

- `add n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567 t/criminal`  
  Adds a student with two tags: `friend` and `criminal`.

### 2. Listing all students : `list`

Shows a list of all students in the address book.

Format: `list`

### 3. Editing a student : `edit`

Edits the details of an existing student in the address book using their student ID.

Format: `edit s/STUDENT_ID [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/CLASS_TAG]...`

* Edits the student identified by the given `STUDENT_ID`.
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing class tags:
    - Adding new tags: type `t/TAG1 t/TAG2 ...` (existing tags **remain**; new ones are **added**).
    - Clearing all tags: type `t/` with no tags specified.

**Examples:**

- `edit s/2042 p/91234567 e/johndoe@example.com`  
  Edits the phone number and email of the student with ID `2042`.

- `edit s/2042 n/Betsy Crower t/`  
  Edits the name of the student with ID `2042` to `Betsy Crower` and clears all existing class tags.

- `edit s/2042 t/MATH101 t/CS102`  
  Updates the class tags of the student with ID `2042` to `MATH101` and `CS102`.

### 4. Locating students by name: `find`

Finds students whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### 5. Deleting a Person: `delete`

Deletes a specified student from the address book using their student ID.

Format: `delete s/STUDENT_ID`

- Removes the student with the matching `STUDENT_ID` from the address book.
- The `STUDENT_ID` must correspond to an existing student in the list.

Examples:
* `delete s/0230` deletes the student with ID `0230` from the address book.
* `find Betsy` followed by `delete s/2042` deletes the student with ID `2042` from the search results.


### 6. Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`
---

## Class Tag Management

The Class Tag Management commands allow you to **create, delete, and list class tags** in the system.  
Class tags are useful for organizing students by their class groups (e.g. `Sec3_Maths`, `JC1_Physics`).

<box type="info" seamless>

**Overview of Class Tag Management Commands**

| Command             | Description                  |
|---------------------|------------------------------|
| `tag -a t/TAG_NAME` | Add a new class tag          |
| `tag -d t/TAG_NAME` | Delete an existing class tag |
| `tag -l`            | List all existing class tags |

</box>

### 1. Adding a class tag : `tag -a`

Adds a new class tag to the system. This allows you to categorize students by the classes they attend.

Format: `tag -a t/TAG_NAME`

Notes:
- Tag names must be 1\-30 characters long and can only contain alphanumeric characters and underscores (`_`). Spaces or other special characters are not allowed.
- Tag names are case\-insensitive when checking for duplicates, but the original casing is preserved when added.

Examples:
- `tag -a t/Sec3_Maths` — Adds a class tag named `Sec3_Maths`.
- `tag -a t/JC1_Physics` — Adds a class tag named `JC1_Physics`.

Expected output:
- `New class tag added: Sec3_Maths`  
  or  
  `This class tag already exists.`

---

### 2. Deleting a class tag : `tag -d`

Deletes an existing class tag from the system.

⚠️ Important: You can only delete a class tag if it is not currently assigned to any person in the address book. Remove the tag from all students first before deleting.

Format: `tag -d t/TAG_NAME`

Notes:
- Tag name matching is case\-insensitive.

Examples:
- `tag -d t/Sec3_Maths` — Deletes the class tag named `Sec3_Maths`.
- `tag -d t/jc1_physics` — Deletes the class tag named `JC1_Physics`.

Expected output:
- `Tag deleted: Sec3_Maths`  
  or  
  `This class tag does not exist.`  
  or  
  `Cannot delete tag 'Sec3_Maths' because it is still assigned to one or more students. Please remove the tag from all students first.`

---

### 3. Listing all class tags : `tag -l`

Shows a list of all class tags currently created in the address book.

Format: `tag -l`

Examples:
- `tag -l`

Expected output:
- If tags exist:  
  `Listed all class tags:`  
  `1. JC1_Physics`  
  `2. Sec3_Maths`
- If no tags exist:  
  `No class tags found. You can add one using the 'tag -a' command.`

---

### Managing students' payment: `fee` Commands
The `fee` command family allows you to **record, update, and view student payment statuses**.  
This helps tutors and administrators track monthly tuition fee payments efficiently and keep student records up to date.
<box type="info" seamless>

**Overview of fee commands**

| Command                        | Description                                         |
|--------------------------------|-----------------------------------------------------|
| `fee -p s/STUDENT_ID m/MMYY`   | Marks a student as **PAID** for a specified month   |
| `fee -up s/STUDENT_ID m/MMYY`  | Marks a student as **UNPAID** for a specified month |
| `fee -v s/STUDENT_ID [m/MMYY]` | Views a student’s **payment history**               |
</box>


### 1. Marking a student as PAID:

Marks a student’s payment status as **PAID** for a specific month.

**Format:** fee -p s/STUDENT_ID m/MMYY

**Examples:**
- `fee -p s/0001 m/0925` — marks student `0001`(Bernice Yu) as **paid** for **September 2025**.
- `fee -p s/0003 m/0825` — marks student `0003`(David Li) as **paid** for **August 2025**.

<box type="tip" seamless>
If the month precedes the student’s enrollment month, the command will be rejected with an error message.
</box>

**Expected output:**  
Bernice Yu has been successfully marked as Paid for September 2025.  
David Li has been successfully marked as Paid for August 2025.

### 2. Marking a student as UNPAID:

Marks a student’s payment status as **UNPAID** for one specific month.  
Use this for corrections or when a payment was previously marked as paid but has not been settled.

**Format:** fee -up s/STUDENT_ID m/MMYY

**Examples:**
- `fee -up s/0001 m/0925` — marks student `0001`(Bernice Yu) as **unpaid** for **September 2025**.
- `fee -up s/0003 m/0825` — marks student `0003`(David Li) as **unpaid** for **August 2025**.

<box type="tip" seamless>
If the month precedes the student’s enrollment month, the command will be rejected with an error message.
</box>

**Expected output:**  
Bernice Yu has been successfully marked as Unpaid for September 2025.   
David Li has been successfully marked as Unpaid for August 2025.

### 3. Viewing a student’s payment history:

Displays a student’s payment history from a starting month up to the current month.


**Format:** fee -v s/STUDENT_ID [m/MMYY]

**Examples:**
- `fee -v s/0001` — shows payment history for student `0001` from their enrollment month to the current month.
- `fee -v s/0001 m/0525` — shows payment history starting **May 2025** to the current month.


<box type="tip" seamless>

If the starting month is not provided or the starting month provided precedes the
student's enrollment month, the history will start from the student's enrollment month.

</box>

**Expected Output:**  
Payment history for Bernice Yu from June 2025 to October 2025 (5 months)  
Enrolled Month: June 2025  
June 2025 : UNPAID (default)  
July 2025 : UNPAID (default)  
August 2025 : UNPAID (default)  
September 2025 : PAID (marked)  
October 2025 : PAID (marked)

<box type="info" seamless>

**Interpreting the results:**
- `marked` — the payment was explicitly set (via `fee -p` or `fee -up`).
- `default` — the month had no explicit record and is assumed **UNPAID**.

</box>

---

### Filter students: `filter`

The `filter` command allows you to quickly find students based on specific criteria such as **payment status** or **class tags**.  
This is useful for tutors and administrators who want to check which students
have paid or are unpaid for a given month, or to focus on a specific class group.

### Overview

Filtering supports three main types of criteria:

| Flag  | Meaning                                               | Example                  |
|-------|-------------------------------------------------------|--------------------------|
| `-p`  | Show students marked as **PAID** for a specific month | `filter -p m/1025`       |
| `-up` | Show students marked (or defaulted) as **UNPAID**     | `filter -up m/1125`      |
| `-t`  | Show students belonging to a particular **class tag** | `filter -t t/Sec3_Maths` |

You can only use **one flag** per command.  
Each filter updates the main student list view to display only matching entries.

### Filter by PAID status : `filter -p`

Shows all students whose payment status is **PAID** for a given month.

**Format:**  
filter -p m/MMYY

**Example:**  
filter -p m/1025

**Expected Output:**  
Showing PAID students for October 2025.

### Filter by UNPAID status : `filter -up`

Shows all students whose payment status is **UNPAID** for a given month.  
<box type="info" seamless>
If a student has never been explicitly marked as PAID, their status is treated as **UNPAID by default**.
</box>

**Format:**  
filter -up m/MMYY

**Example:**  
filter -up m/1025

**Expected Output:**  
Showing UNPAID students for October 2025.

### Filtering persons by class tag : `filter -t`

Filters the main list to show only persons who are assigned the specified class tag.

Format: filter -t t/TAG_NAME

Notes:
- The class tag must already exist in the system.
- Tag name matching is case\-insensitive.

Examples:
- filter -t t/Sec3_Maths — Shows only students who have the Sec3_Maths tag.
- filter -t t/jc1_physics — Shows only students who have the JC1_Physics tag.

Expected output:
![filterByClassTag.jpg](images/filterByClassTag.jpg)

### Managing students' attendance: `att`
The `att` command family allows you to **record, update, and view student attendance**.
Each attendance record is tied to both a date and a class tag, allowing tutors to manage students who attend multiple classes efficiently.

<box type="info" seamless>

**Overview of att commands**

| Command                                     | Description                                                                                |
|---------------------------------------------|--------------------------------------------------------------------------------------------|
| `att -m s/STUDENT_ID d/DDMMYYYY t/TAG_NAME` | Marks a student as **PRESENT** for a given date and class tag                              |
| `att -u s/STUDENT_ID d/DDMMYYYY t/TAG_NAME` | Marks a student as **ABSENT** for a given date and class tag or undoes a marked attendance |
| `att -v s/STUDENT_ID`                       | Views a student's **attendance records**                                                   |
</box>


### 1. Marking a student as PRESENT:

Marks a student's attendance as **PRESENT** for a specific date and class.

**Format:** `att -m s/STUDENT_ID d/DDMMYYYY t/TAG_NAME`

**Examples:**
- `att -m s/0001 d/15092025 t/Math` — marks student `0001`(Bernice Yu) as **present** for **15 September 2025** in **Math** class.
- `att -m s/0003 d/20082025 t/Science` — marks student `0003`(David Li) as **present** for **20 August 2025** in **Science** class.

<box type="tip" seamless>
The student must have the specified class tag. If the student doesn't have the tag, the command will be rejected with an error message.
</box>

**Expected output:**<br>
Marked attendance for: Bernice Yu on 15/09/2025 for class Math<br>
Marked attendance for: David Li on 20/08/2025 for class Science

### 2. Marking a student as ABSENT:

Marks a student's attendance as **ABSENT** for a specific date and class.
Use this to record absences or to change a previously marked **PRESENT** attendance to **ABSENT**.

**Format:** `att -u s/STUDENT_ID d/DDMMYYYY t/TAG_NAME`

**Examples:**
- `att -u s/0001 d/15092025 t/Math` — marks student `0001`(Bernice Yu) as **absent** for **15 September 2025** in **Math** class.
- `att -u s/0003 d/20082025 t/Science` — marks student `0003`(David Li) as **absent** for **20 August 2025** in **Science** class.

<box type="tip" seamless>
The student must have the specified class tag. If the student doesn't have the tag, the command will be rejected with an error message.
</box>

**Expected output:**<br>
Unmarked attendance for: Bernice Yu on 15/09/2025 for class Math<br>
Unmarked attendance for: David Li on 20/08/2025 for class Science

### 3. Viewing a student's attendance records:

Displays all attendance records for a specific student across all their classes.

**Format:** `att -v s/STUDENT_ID`

**Examples:**
- `att -v s/0001` — shows all attendance records for student `0001`(Bernice Yu).
- `att -v s/0003` — shows all attendance records for student `0003`(David Li).


<box type="tip" seamless>

If no attendance records exist for the student, a message will be displayed indicating no records were found.

</box>

**Expected Output:**<br>
Showing attendance records for: Bernice Yu<br>
15/09/2025 - Math: Present<br>
16/09/2025 - Math: Absent<br>
20/09/2025 - Science: Present

<box type="info" seamless>

</box>

### Tracking students' performance: `perf` commands
The `perf` command family allows you to track students' performance in class.
This helps you to monitor and manage students' academic progress effectively.

**Overview of perf commands**

| Command                                                      | Description                                  |
|--------------------------------------------------------------|----------------------------------------------|
| `perf -a s/STUDENT_ID d/DATE t/TAG_NAME pn/PERFORMANCE_NOTE` | Add performance data for a student           |
| `perf -v s/STUDENT_ID`                                       | View performance data for a student          |
| `perf -e s/STUDENT_ID d/DATE t/TAG_NAME pn/PERFORMANCE_NOTE` | Edit existing performance data for a student |
| `perf -d s/STUDENT_ID d/DATE t/TAG_NAME`                     | Delete performance data for a student        |


### 1. Adding a performance note for a student:

Adds a performance note for a student in a specific class on a specific date.

**Format**: `perf -a s/STUDENT_ID d/DATE t/TAG_NAME pn/PERFORMANCE_NOTE`

**Examples:**
- `perf -a s/0001 d/18092025 t/Sec3_Maths pn/Scored 85% on mock test`

**Expected output:**
Performance note successfully added for John Tan in Sec3_Maths on 18-09-2025.

### 2. Viewing performance notes for a student:

Displays all performance notes for a student.

**Format**: `perf -v s/STUDENT_ID`

**Examples:**
- `perf -v s/0001`

**Expected output:**
Performance notes for John Tan:
- Sec3_Maths on 18-09-2025: Scored 85% on mock test

### 3. Editing a performance note for a student:

Edits an existing performance note for a student.

**Format**: `perf -e s/STUDENT_ID d/DATE t/TAG_NAME pn/PERFORMANCE_NOTE`

**Examples:**
- `perf -e s/0001 d/18092025 t/Sec3_Maths pn/Scored 90% on mock test after re-evaluation`

**Expected output:**
- Performance note for %s in %s on %s successfully edited.

### 4. Deleting a performance note for a student:

Deletes a performance note for a student.

**Format**: `perf -d s/STUDENT_ID d/DATE t/TAG_NAME`

**Examples:**
- `perf -d s/0001 d/18092025 t/Sec3_Maths`

**Expected output:**
- Performance note for John Tan in Sec3_Maths on 18-09-2025 successfully deleted.

---

### Exiting the program : `exit`

Exits the program.

Format: `exit`

---

### Saving the data

AddressBook data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

AddressBook data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, AddressBook will discard all data and start with an empty data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.

</box>

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous AddressBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

| Action                      | Format, Examples                                                                                                                                                 |
|-----------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Add**                     | `add n/NAME p/PHONE e/EMAIL a/ADDRESS [t/CLASS_TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/Math t/Science` |
| **Clear**                   | `clear`                                                                                                                                                          |
| **Delete**                  | `delete s/STUDENT_ID`<br> e.g., `delete s/0230`                                                                                                                  |
| **Edit**                    | `edit s/STUDENT_ID [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g.,`edit s/1234 n/James Lee e/jameslee@example.com`                          |
| **Find**                    | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`                                                                                                       |
| **List**                    | `list`                                                                                                                                                           |
| **Help**                    | `help`                                                                                                                                                           |
| **Class Tag (add)**         | `tag -a t/TAG_NAME`<br> e.g., `tag -a t/Sec3_Maths`                                                                                                              |
| **Class Tag (delete)**      | `tag -d t/TAG_NAME`<br> e.g., `tag -d t/Sec3_Maths`                                                                                                              |
| **Class Tag (list)**        | `tag -l`<br> e.g., `tag -l`                                                                                                                                      |
| **Mark as PAID**            | `fee -p s/STUDENT_ID m/MMYY` <br> e.g., `fee -p s/0001 m/0925`                                                                                                   |
| **Mark as UNPAID**          | `fee -up s/STUDENT_ID m/MMYY` <br> e.g., `fee -up s/0001 m/0925`                                                                                                 |
| **View payment history**    | `fee -v s/STUDENT_ID [m/MMYY]` <br> e.g., `fee -v s/0001 m/0525`                                                                                                 |
| **Mark as PRESENT**         | `att -m s/STUDENT_ID d/DDMMYYYY t/TAG_NAME` <br> e.g., `att -m s/0001 d/15092025 t/Math`                                                                         |
| **Mark as ABSENT**          | `att -u s/STUDENT_ID d/DDMMYYYY t/TAG_NAME` <br> e.g., `att -u s/0001 d/15092025 t/Math`                                                                         |
| **View attendance**         | `att -v s/STUDENT_ID` <br> e.g., `att -v s/0001`                                                                                                                 |
| **Filter by PAID status**   | `filter -p m/MMYY` <br> e.g., `filter -p m/1025`                                                                                                                 |
| **Filter by UNPAID status** | `filter -up m/MMYY` <br> e.g., `filter -up m/1025`                                                                                                               |
| **Filter by class tag**     | `filter -t t/TAG_NAME` <br> e.g., `filter -t t/Sec3_Maths`                                                                                                       |
| **Add performance note**    | `perf -a s/STUDENT_ID d/DATE t/TAG_NAME pn/PERFORMANCE_NOTE` <br> e.g., `perf -a s/0001 d/18092025 t/Sec3_Maths pn/Scored 85% on mock test`                      |
| **View performance notes**  | `perf -v s/STUDENT_ID` <br> e.g., `perf -v s/0001`                                                                                                               |
| **Edit performance note**   | `perf -e s/STUDENT_ID d/DATE t/TAG_NAME pn/PERFORMANCE_NOTE` <br> e.g., `perf -e s/0001 d/18092025 t/Sec3_Maths pn/Scored 90% on mock test after re-evaluation`  |
| **Delete performance note** | `perf -d s/STUDENT_ID d/DATE t/TAG_NAME` <br> e.g., `perf -d s/0001 d/18092025 t/Sec3_Maths`                                                                     |
