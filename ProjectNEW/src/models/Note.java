package models;

import utils.CategoryUtility;
import utils.Utilities;

import java.util.ArrayList;
import java.util.Objects;

public class Note {

    //fields

    private String noteTitle = "No Title";

    private int notePriority = 1;

    private String noteCategory = "";

    private boolean isNoteArchived = false;

    private ArrayList<Item> items = new ArrayList<> ();

    // constructor
    public Note(String noteTitle, int notePriority, String noteCategory) {
        this.noteTitle = Utilities.truncateString(noteTitle, 20);
        setNotePriority(notePriority);
        this.noteCategory = noteCategory;
        setNoteArchived(false);
    }

    // getters


    public String getNoteTitle() {
        return noteTitle;
    }

    public int getNotePriority() {
        return notePriority;
    }

    public String getNoteCategory() {
        return noteCategory;
    }

    public boolean isNoteArchived() {
        return isNoteArchived;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    //setters

    public void setNoteTitle(String noteTitle) {
        if (Utilities.validateStringLength(noteTitle, 20)) {
            this.noteTitle = noteTitle;
        }
    }

    public void setNotePriority(int notePriority) {
        if (Utilities.validRange(notePriority,1, 5)) {
            this.notePriority = notePriority;
        }
    }


    public void setNoteCategory(String noteCategory) {
        if (CategoryUtility.isValidCategory(noteCategory)) {
            this.noteCategory = noteCategory;
        }
    }

    public void setNoteArchived(boolean noteArchived) {
        isNoteArchived = noteArchived;
    }


    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    // add item object, passed as a parameter, through the array list

    public boolean addItem(Item item) {
        return items.add(item);
    }


    /**
     * This method builds and returns a String containing all the items in the ArrayList.
     * For each item stored, the associated index number is included.
     * If no items are stored in the ArrayList, the String "No items added" is returned.
     *
     * @return A String containing all the products in the ArrayList, or "No items in the note",
     * if empty.
     */

    public String listItems() {
        if (items.isEmpty()) {
            return "No items added";
        } else {
            String listOfItems = "";
            for (int i = 0; i < items.size(); i++) {
                listOfItems += i + ": " + items.get(i) + "\n";
            }
            return listOfItems;
        }
    }


    //This method takes in a number and checks if it is a valid index in the items ArrayList.
    public boolean isValidIndex(int index) {
        return (index >= 0) && (index < items.size());
    }


    // find item at specific index location. returns the item object or null if no object at index location
    public Item findItem(int index) {
        if(isValidIndex(index)) {
            return items.get(index);
        }
        return null;
    }

    // This method deletes an item at the index parameter (if that index exists) and returns the deleted item object.
    // If the index does not exist in the items list, then null should be returned.

    public Item deleteItem(int indexToDelete) {
        if (isValidIndex(indexToDelete)) {
            return items.remove(indexToDelete);
        }
        return null;
    }

    // returns number of items stored in the Item arraylist
    public int numberOfItems() {
        return items.size();
    }


    // Update a models.Item in the ArrayList with the contents passed in the models.Item object parameter
    public boolean updateItem (int indexToUpdate, String itemDescription, boolean isItemCompleted) {
        Item foundItem = findItem(indexToUpdate);


        if (foundItem != null) {
            foundItem.setItemDescription(itemDescription);
            foundItem.setItemCompleted(isItemCompleted);
            return true;
        }
        return false;
    }
    // return true if empty, run for each loop through array, return false if any are not completed
    public boolean checkNoteCompletionStatus() {
        if (items.isEmpty()) return true;

        for (Item item : items) {
            if (!item.isItemCompleted()) return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return notePriority == note.notePriority
                && isNoteArchived
                == note.isNoteArchived
                && Objects.equals(noteTitle, note.noteTitle)
                && Objects.equals(noteCategory, note.noteCategory)
                && Objects.equals(items, note.items);
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteTitle='" + noteTitle + '\'' +
                ", notePriority=" + notePriority +
                ", noteCategory='" + noteCategory + '\'' +
                ", isNoteArchived=" + Utilities.booleanIsNoteArchived(isNoteArchived) +
                ", items=" + items +
                '}';
    }
}
