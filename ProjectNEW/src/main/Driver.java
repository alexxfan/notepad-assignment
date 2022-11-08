package main;

import controllers.NoteAPI;

import models.Item;
import models.Note;

import utils.ScannerInput;
import utils.Utilities;
import utils.CategoryUtility;


public class Driver {

    private NoteAPI noteAPI = new NoteAPI();

    public static void main(String[] args) {
        new Driver();
    }

    public Driver() {
        runMenu();
    }

    private int mainMenu() {
        return ScannerInput.readNextInt("""
                ------------------------------------------------------------------
                |                         NOTE KEEPER APP                        |
                ------------------------------------------------------------------
                | NOTE MENU                                                      |
                |   1) Add a note                                                |
                |   2) List all notes (all, active, archived)                    |
                |   3) Update a note                                             |
                |   4) Delete a note                                             |
                |   5) Archive a note                                            |
                ------------------------------------------------------------------
                | ITEM MENU                                                      |
                |   6) Add an item to a note                                     |
                |   7) Update item description on a note                         |
                |   8) Delete an item from a note                                |
                |   9) Mark item as complete/todo                                |
                ------------------------------------------------------------------
                | REPORT MENU FOR NOTES                                          |
                |  10) All notes and their items (active & archived)             | 
                |  11) Archive notes whose items are all complete                |
                |  12) All notes within a selected category                      | 
                |  13) All notes within a selected priority                      |
                |  14) Search for all notes (by note title)                      | 
                ------------------------------------------------------------------
                | REPORT MENU FOR ITEMS                                          |
                |  15) All items that are todo (with note title)                 | 
                |  16) Overall number of items todo/complete                     |
                |  17) Todo/complete items (by specific category)                | 
                |  18) Search for all items (by item description)                |
                ------------------------------------------------------------------
                | SETTINGS MENU                                                  |
                |  19) Save                                                      | 
                |  20) Load                                                      |
                |   0) EXIT                                                      |
                ------------------------------------------------------------------
                ==>>  """);

    }

    private void runMenu() {
        int option = mainMenu();

        while (option != 0) {

            switch (option) {
                case 1 -> addNote();
                case 2 -> viewNotes();
                case 3 -> updateNote();
                case 4 -> deleteNote();
                case 5 -> archiveNote();
                case 6 -> addItemToNote();
                case 7 -> updateItemDescInNote();
                case 8 -> deleteItemFromNote();
                case 9 -> markCompletionOfItem();
                case 10 -> printActiveAndArchivedReport();
                case 11 -> archiveNotesWithAllItemsComplete();
                case 12 -> printNotesBySelectedCategory();
                case 13 -> printNotesByPriority();
                case 14 -> searchNotesByTitle();
                case 15 -> printAllTodoItems();
                case 16 -> printOverallItemsTodoComplete();
                case 17 -> printItemCompletionStatusByCategory();
                case 18 -> searchItemsByDescription();
                case 19 -> saveNotes();
                case 20 -> loadNotes();
                default -> System.out.println("Invalid option entered: " + option);
            }

            //pause the program so that the user can read what we just printed to the terminal window
            ScannerInput.readNextLine("\nPress enter key to continue...");

            //display the main menu again
            option = mainMenu();
        }

        //the user chose option 0, so exit the program
        System.out.println("Exiting...bye");
        System.exit(0);
    }

    private int subMenu() {
        return ScannerInput.readNextInt("""
                ------------------------------------------------------------------
                |   1) View ALL Notes                                            |
                |   2) View ACTIVE Notes                                         |
                |   3) View ARCHIVED Notes                                       |
                ------------------------------------------------------------------
                """);
    }

    private void addNote() {

        String noteTitle = ScannerInput.readNextLine("Enter the Note Title:  ");
        //Truncate the entered title
        noteTitle = Utilities.truncateString(noteTitle, 20);
        int notePriority = ScannerInput.readNextInt("Enter the Note Priority :  ");
        //Check that it is a valid priority
        while (!Utilities.validRange(notePriority, 1, 5)) {
            notePriority = ScannerInput.readNextInt("Enter the Note Priority :  ");
        }

        String noteCategory = ScannerInput.readNextLine("Enter the Note Category:  ");
        //Check that it is a valid category
        while (!CategoryUtility.isValidCategory(noteCategory)) {
            noteCategory = ScannerInput.readNextLine("Enter the Note Category:  ");
        }



        boolean isAdded = noteAPI.add(new Note(noteTitle, notePriority, noteCategory));
        if (isAdded) {
            System.out.println("Note Added Successfully");
        } else {
            System.out.println("No Note Added");
        }
    }


    //print the products stored in the collection
    private void viewNotes() {

        if (noteAPI.numberOfNotes() == 0) {

            System.out.println("There are no notes stored yet");
            ScannerInput.readNextLine("\nPress enter key to continue...");

            runMenu();
        }
        int option = subMenu();

            switch (option) {
                case 1 -> printAllNotes();
                case 2 -> printAllActiveNotes();
                case 3 -> printAllArchivedNotes();
                default -> System.out.println("Invalid option entered: " + option);
            }

            ScannerInput.readNextLine("\nPress enter key to continue...");

            runMenu();

    }




    private void updateNote() {
        printAllNotes();
        if (noteAPI.numberOfNotes() > 0) {
            //only ask the user to choose the note to update if notes exist
            int indexToUpdate = ScannerInput.readNextInt("Enter the index of the note to update ==> ");
            if (noteAPI.isValidIndex(indexToUpdate)) {
                String noteTitle = ScannerInput.readNextLine("Enter the Note Title:  ");
                int notePriority = ScannerInput.readNextInt("Enter the Note Priority:  ");
                String noteCategory = ScannerInput.readNextLine("Enter the Note Category:  ");


                //pass the index of the product and the new product details to controllers.Store for updating and check for success.
                if (noteAPI.updateNote(indexToUpdate, noteTitle, notePriority, noteCategory)) {
                    System.out.println("Update Successful");
                } else {
                    System.out.println("Update NOT Successful");
                }
            } else {
                System.out.println("There are no notes for this index number");
            }
        }
    }

    //ask the user to enter the index of the object to delete, and assuming it's valid, delete it.
    private void deleteNote() {
        printAllNotes();
        if (noteAPI.numberOfNotes() > 0) {
            //only ask the user to choose the product to delete if products exist
            int indexToDelete = ScannerInput.readNextInt("Enter the index of the note to delete ==> ");
            //pass the index of the product to controllers.NoteAPI for deleting and check for success.
            Note noteToDelete = noteAPI.deleteNote(indexToDelete);
            if (noteToDelete != null) {
                System.out.println("Delete Successful! Deleted note: " + noteToDelete.getNoteTitle());
            } else {
                System.out.println("Delete NOT Successful");
            }
        }
    }

    private void archiveNote() {
        printAllNotes();
        if (noteAPI.numberOfNotes() > 0) {
            int indexToArchive = ScannerInput.readNextInt("Enter the index of the note to archive ==> ");
            boolean res = noteAPI.archiveNote(indexToArchive);
            if (res) {
                System.out.println("Archive Successful! Archived note: " + noteAPI.findNote(indexToArchive));
            } else {
                System.out.println("Archive NOT Successful");
            }
        }
    }


    // for adding items to an active note
    private void addItemToNote() {
        printAllActiveNotes();
        if(noteAPI.numberOfNotes() > 0){
            int noteIndex = ScannerInput.readNextInt("Enter the index of the Note to add an item to it ==> ");
            if(noteAPI.isValidIndex(noteIndex)){
                String itemDescription = ScannerInput.readNextLine("Enter the item description");
                itemDescription = Utilities.truncateString(itemDescription, 50);
                char currentItem = ScannerInput.readNextChar("Is this item completed or not? (y/n): ");
                boolean isItemCompleted = Utilities.YNtoBoolean(currentItem);

                Item newItem = new Item(itemDescription, isItemCompleted);
                noteAPI.findNote(noteIndex).addItem(newItem);
            }
        } else {
            System.out.println("No Item Added");
        }

    }

    private void updateItemDescInNote() {
        printAllActiveNotes();
        if(noteAPI.numberOfNotes() > 0){
            int noteIndex = ScannerInput.readNextInt("Enter the index of the Note to update ==> ");
            if(noteAPI.isValidIndex(noteIndex) && (noteAPI.numberOfItems() > 0)) {
                int itemIndex = ScannerInput.readNextInt("Enter the index of the item to update ==> ");
                //Check that the item index is ok before updating the note
                if (noteAPI.findNote(noteIndex).isValidIndex(itemIndex)) {
                    String itemDescription = ScannerInput.readNextLine("Enter the new item description");
                    itemDescription = Utilities.truncateString(itemDescription, 50);

                    noteAPI.findNote(noteIndex).findItem(itemIndex).setItemDescription(itemDescription);
                    System.out.println("Update Successful");
                }

                else{
                    System.out.println("Update NOT Successful");
                }
            }
            else{
                System.out.println("There are no notes for this index number");
            }
        }


    }

    private void deleteItemFromNote() {
        printAllActiveNotes();
        if(noteAPI.numberOfItems() > 0){
            int noteIndex = ScannerInput.readNextInt("Enter the index of the Note to delete an item from ==> ");
            if((noteAPI.isValidIndex(noteIndex)) && (noteAPI.numberOfItems() > 0)){
                int itemIndex = ScannerInput.readNextInt("Enter the index of the item to delete ==> ");
                if (noteAPI.findNote(noteIndex).isValidIndex(itemIndex)) {
                    noteAPI.findNote(noteIndex).deleteItem(itemIndex);
                    System.out.println("Item successfully deleted");
                }
                else {
                    System.out.println("No Item Deleted");
                }
            }
        }
        else{
            System.out.println("No Items found");
        }

    }



    private void markCompletionOfItem() {
        printAllActiveNotes();
        if (noteAPI.numberOfNotes() > 0) {
            int noteIndex = ScannerInput.readNextInt("Enter the index of the note to mark an item as completed ==>");
            if(noteAPI.isValidIndex(noteIndex) && (noteAPI.numberOfItems()> 0 )){
                int itemIndex = ScannerInput.readNextInt("Enter the index of the item to mark as completed ==> ");
                if(noteAPI.findNote(noteIndex).isValidIndex(itemIndex)) {
                    char currentItem = ScannerInput.readNextChar("Is this item completed? (y/n): ");
                    boolean status = Utilities.YNtoBoolean(currentItem);
                    noteAPI.findNote(noteIndex).findItem(itemIndex).setItemCompleted(status);
                    System.out.println("Item completion status updated");
                } else {
                    System.out.println("Incorrect item index");
                }
            } else {
                System.out.println("Incorrect note index, or no items found in the note");
            }

        } else {
            System.out.println("No Items found");
        }
    }


    private void printAllNotes() {
        System.out.println("List of all notes: ");
        System.out.println(noteAPI.listAllNotes());

    }

    private void printAllActiveNotes() {
        if(noteAPI.numberOfActiveNotes() > 0) {
            System.out.println("List of all active notes: ");
            System.out.println(noteAPI.listActiveNotes());
        }
        else{
            System.out.println("There are no active notes");
        }
    }

    private void printAllArchivedNotes() {
        if(noteAPI.numberOfArchivedNotes() > 0) {
            System.out.println("List of all archived notes: ");
            System.out.println(noteAPI.listArchivedNotes());
        }
        else{
            System.out.println("There are no archived notes");
        }
    }

    private void printActiveAndArchivedReport() {
        System.out.println(noteAPI.numberOfActiveNotes() + " active notes" + "\n" + noteAPI.listActiveNotes() +"\n" + noteAPI.listTodoItems()
        + "\n" + noteAPI.numberOfArchivedNotes() + " archived notes" + "\n" + noteAPI.listArchivedNotes());

    }

    private void archiveNotesWithAllItemsComplete() {
        noteAPI.archiveNotesWithAllItemsComplete();
    }

    private void printNotesBySelectedCategory() {
        if (noteAPI.numberOfNotes() > 0){
            String category = ScannerInput.readNextLine("Enter the category ==> ");
            if(CategoryUtility.isValidCategory(category)) {
                System.out.println("List of all notes by category: \n"+ noteAPI.listNotesBySelectedCategory(category));

            }
        }
        else{
            System.out.println("Invalid category");
        }
    }


    private void printNotesByPriority() {
        if (noteAPI.numberOfNotes() > 0){
            int priority = ScannerInput.readNextInt("Enter the priority");
            if(noteAPI.isValidIndex(priority)){
                System.out.println("List of all notes by priority \n" + noteAPI.listNotesBySelectedPriority(priority));
            }
        }
        else {
            System.out.println("Invalid priority");
        }
    }

    private void searchNotesByTitle() {
        if (noteAPI.numberOfNotes() > 0){
            String noteTitle = ScannerInput.readNextLine("Enter the Title of the note you wish to search for ");
            System.out.println(noteAPI.searchNotesByTitle(noteTitle));
        }

    }

    private void printAllTodoItems() {
        if (noteAPI.numberOfNotes() > 0 && noteAPI.numberOfItems() > 0) {
            System.out.println("Items to do: " + noteAPI.listTodoItems());
        }
        else{
            System.out.println("No items to do");
        }
    }

    private void printOverallItemsTodoComplete() {
        if (noteAPI.numberOfNotes() > 0 && noteAPI.numberOfItems() > 0) {
            System.out.println("Items completed: " + noteAPI.numberOfCompleteItems() + " Items to do: " + noteAPI.numberOfTodoItems());
        }
        else{
            System.out.println("No Items to do or no notes");
        }
    }

    private void printItemCompletionStatusByCategory() {
        if (noteAPI.numberOfNotes() > 0) {
            String category = ScannerInput.readNextLine("Enter the Note Category");
            System.out.println("Items completed by Category: " + noteAPI.listItemStatusByCategory(category));
        }
        else{
            System.out.println("Items are not stored yet");
        }
    }

    private void searchItemsByDescription() {
    if(noteAPI.numberOfItems() > 0){
        String itemDescription = ScannerInput.readNextLine("Enter the Description of the note you wish to search for");
        System.out.println(noteAPI.searchItemByDescription(itemDescription));
        }
    else{
        System.out.println("Item does not exist");
    }

    }

    private void saveNotes() {
        try {
            noteAPI.save();
        } catch (Exception e) {
            System.err.println("Error writing to file: " + e);
        }
    }

    private void loadNotes() {
        try {
            noteAPI.load();
        } catch (Exception e) {
            System.err.println("Error reading from file: " + e);
        }
    }
}



