Lab 3 – Contact Manager App
EECS 4443 – Winter 2026
****************************************************************************************************************************
1.Choose a functional theme:
Our group chose: Contact Manager – Add contacts with name, notes, and birthday.

2.Describe Your Architecture: In your README, include a brief explanation of the App’s architecture pattern:
a. Which pattern are you following (e.g., MVC, MVVM, etc.)?
   This application follows the MVC (Model–View–Controller) architecture pattern.

b. How does your code reflect the adopted structure (Model, View, Controller, ViewModel)?
   The Contact class represents the Model and defines the structure of the contact data.
   The XML layout files act as the View layer by defining the UI components and user interface design.
   MainActivity and ContactsActivity serve as Controllers, handling user interactions, validating input, and coordinating between the UI and data storage layers (SharedPrefsStorage and DBHelper).
   This separation ensures modularity, maintainability, and clear responsibility.
****************************************************************************************************************************
Division of Work:

* Gabriel Beh-
  1. Implemented Gesture Handling: Implement tap and long press gestures on list items:
  -Tap: Show item details on a new screen.
  -Long Press: Optionally allow deletion or update of the entry.

* Manali Bisht-
  1. Created  a Data Entry Form:
  a. Built an activity with the asked UI components:
  -Activity description banner to show on the top of the screen (App name, brief description of the functionality)
  -Input fields (examples from a contact manager App):
  −Submit button to: Validate the required fields
  −Give the user feedback (via Toast or Snackbar)

* Erim Lee- 
  1. Implemented Display Stored Data: On App launch or return to the main activity:
     -Load and display all previously saved entries in a scrollable list (e.g., using ListView or RecyclerView)
     -Each item should show at least the two fields (Name and Phone Number for example).

* Vandana Suman (220094124)-
  1. Implemented Data Storage
  -Implement both of the following storage methods as separate implementations: SharedPreferences &SQLite
  Add a button or a check box (for the demo) to choose one method during runtime.
  -Upon submitting the form: Save the data locally, Clear the form, Provide confirmation to the user
  2. Implemented Error handling and validation
  3. Completed README file
****************************************************************************************************************************


  