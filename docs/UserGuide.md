---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# AB-3 User Guide

AddressBook Level 3 (AB3) is a **desktop app for managing contacts, optimized for use via a  Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, AB3 can get your contact management tasks done faster than traditional GUI apps.

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar addressbook.jar` command to run the application.<br>
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


### Adding a person: `add`

Adds a person to the address book.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​`

<box type="tip" seamless>

**Tip:** A person can have any number of tags (including 0)
</box>

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567 t/criminal`

### Listing all persons : `list`

Shows a list of all persons in the address book.

Format: `list`

### Editing a person : `edit`

Edits an existing person in the address book.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed i.e adding of tags is not cumulative.
* You can remove all the person’s tags by typing `t/` without
    specifying any tags after it.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.

### Locating persons by name: `find`

Finds persons whose names contain any of the given keywords.

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

### Deleting a person : `delete`

Deletes the specified person from the address book.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

## Managing students' attendance: `att` 
The `att` command family allows you to **record, update, and view student attendance**.
Each attendance record is tied to both a date and a class tag, allowing tutors to manage students who attend multiple classes efficiently.

<box type="info" seamless>

**Overview of att commands**

| Command                                    | Description                                                                                |
|--------------------------------------------|--------------------------------------------------------------------------------------------|
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

### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

AddressBook data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

AddressBook data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, AddressBook will discard all data and start with an empty data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

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

Action     | Format, Examples
-----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------
**Add**    | `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague`
**Clear**  | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit**   | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
**Find**   | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**List**   | `list`
**Help**   | `help`
**Mark Attendance as PRESENT** | `att -m s/STUDENT_ID d/DDMMYYYY t/TAG_NAME` <br> e.g., `att -m s/0001 d/15092025 t/Math`
**Mark Attendance as ABSENT** | `att -u s/STUDENT_ID d/DDMMYYYY t/TAG_NAME` <br> e.g., `att -u s/0001 d/15092025 t/Math`
**View Attendance Records** | `att -v s/STUDENT_ID` <br> e.g., `att -v s/0001`

