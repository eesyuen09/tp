---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# Tuto User Guide

Tuto is a **desktop app for managing contacts, optimized for use via a  Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, Tuto can get your contact management tasks done faster than traditional GUI apps.

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

### Managing students' payment: `fee` Commands
The `fee` command family allows you to **record, update, and view student payment statuses**.  
This helps tutors and administrators track monthly tuition fee payments efficiently and keep student records up to date.
<box type="info" seamless>

**Overview of fee commands**

| Command                        | Description |
|--------------------------------|--------------|
| `fee -p s/STUDENT_ID m/MMYY`   | Marks a student as **PAID** for a specified month |
| `fee -up s/STUDENT_ID m/MMYY`  | Marks a student as **UNPAID** for a specified month |
| `fee -v s/STUDENT_ID [m/MMYY]` | Views a student’s **payment history** |
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

| Flag | Meaning | Example |
|------|----------|----------|
| `-p` | Show students marked as **PAID** for a specific month | `filter -p m/1025` |
| `-up` | Show students marked (or defaulted) as **UNPAID** | `filter -up m/1125` |
| `-t` | Show students belonging to a particular **class tag** | `filter -t t/Sec3_Maths` |

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
-----------|----------------------------------------------------------------------------------------------------------------------------------------------------------
**Add**    | `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague`
**Clear**  | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit**   | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
**Find**   | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**List**   | `list`
**Help**   | `help`
| **Mark as PAID** | `fee -p s/STUDENT_ID m/MMYY` <br> e.g., `fee -p s/0001 m/1025` <br>|
| **Mark as UNPAID** | `fee -up s/STUDENT_ID m/MMYY` <br> e.g., `fee -up s/0001 m/1125` <br>|
| **View Payment History** | `fee -v s/STUDENT_ID [m/MMYY]` <br> e.g., `fee -v s/0001` <br> or `fee -v s/0001 m/0725`|
| **Filter by PAID** | `filter -p m/MMYY` <br> e.g., `filter -p m/1025` <br> |
| **Filter by UNPAID** | `filter -up m/MMYY` <br> e.g., `filter -up m/1125` <br> |
| **Filter by Class Tag** | `filter -t t/CLASS_TAG` <br> e.g., `filter -t t/Sec3_Maths` <br> |

<box type="tip" seamless>

*Tip:* Use `list` to reset the view after filtering or updating payment histories.

</box>

