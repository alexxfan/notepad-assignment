package controllers;

import jdk.jfr.Category;
import models.Item;
import models.Note;

import java.util.ArrayList;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NoteAPI {
    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    private ArrayList<Note> notes = new ArrayList<Note>();

    //Helper Method

    //This method takes in a number and checks if it is a valid index in the ArrayList.
    public boolean isValidIndex(int index) {
        return (index >= 0) && (index < notes.size());
    }

    ////CRUD Methods
    // add item object, passed as a parameter, through the array list
    public boolean add(Note note) {
        return notes.add(note);
    }


    //Update a models.Note in the ArrayList with the contents passed in the models.Note object parameter
    public boolean updateNote(int indexToUpdate, String noteTitle, int notePriority, String noteCategory) {
        Note foundNote = findNote(indexToUpdate);

        if (foundNote != null) {
            foundNote.setNoteTitle(noteTitle);
            foundNote.setNotePriority(notePriority);
            foundNote.setNoteCategory(noteCategory);
            return true;
        }
        return false;
    }

    // This method deletes a note at the index parameter (if that index exists) and returns the deleted note object.
    // If the index does not exist in the notes list, then null should be returned.

    public Note deleteNote(int indexToDelete) {
        if (isValidIndex(indexToDelete)) {
            return notes.remove(indexToDelete);
        }
        return null;
    }

    //checks if the note is archived or not and if the to do list is finished or not
    //archives the note if true, else returns false
    public boolean archiveNote(int indexToArchive) {
        if (isValidIndex(indexToArchive)
                && !notes.get(indexToArchive).isNoteArchived()
                && notes.get(indexToArchive).checkNoteCompletionStatus()) {
            notes.get(indexToArchive).setNoteArchived(true);
            return true;
        }
        return false;
    }

    // checks if the notes in the array list are active or not. makes a string of all the archived notes
    public String archiveNotesWithAllItemsComplete() {
        String results = "";
        for (int i = 0; i < notes.size(); i++) {
            if (!notes.get(i).isNoteArchived() && notes.get(i).checkNoteCompletionStatus()) {
                archiveNote(i);
                results += notes.get(i);
            }
        }
        if (results.length() == 0) return "No active notes stored";
        return results;
    }

    //Counting Methods

    //This method returns the number of note objects stored in the ArrayList.
    public int numberOfNotes() {
        return notes.size();
    }

    //This method returns the number of note objects stored in the ArrayList that are archived
    public int numberOfArchivedNotes(){
        int total = 0;
        for (Note note: notes) {
            if (note.isNoteArchived()) total++;
        }
        return total;

    }

    //This method returns the number of note objects stored in the ArrayList that are NOT archived (active)
    public int numberOfActiveNotes(){
        int total = 0;
        for (Note note: notes) {
            if (!note.isNoteArchived()) total++;
        }
        return total;
    }

    //This method returns the number of note objects stored in the ArrayList by category
    public int numberOfNotesByCategory(String category){
        int total = 0;
        for (Note note: notes) {
            if (note.getNoteCategory().equals(category)) total++;
        }
        return total;
    }

    //This method returns the number of note objects stored in the ArrayList by category
    public int numberOfNotesByPriority(int priority){
        int total = 0;
        for (Note note: notes) {
            if (note.getNotePriority() == priority) total++;
        }
        return total;
    }

    // runs a loop through notes and returns the total number of items in the notes
    public int numberOfItems() {
        int total = 0;
        for (Note note: notes) {
            total += note.getItems().size();
            }
        return total;
    }

    //runs a loop through notes and checks the number of completed items and returns the amount of completed items
    public int numberOfCompleteItems() {
        int total = 0;
        for (Note note: notes) {
            ArrayList<Item> items = note.getItems();
            for (Item item: items){
                if (item.isItemCompleted()) total++;
            }
        }
        return total;
    }


    //runs a loop through notes and checks the number of to do items and returns the amount of to do items
    public int numberOfTodoItems() {
        int total = 0;
        for (Note note: notes) {
            ArrayList<Item> items = note.getItems();
            for (Item item: items){
                if (!item.isItemCompleted()) total++;
            }
        }
        return total;
    }


    // Listing methods

    // This method returns "no notes stored" if the list is empty and returns the list of products if it is not empty
    public String listAllNotes() {
        String listOfNotes = "";
        if (notes.isEmpty()) {
            return "No notes stored";
        } else {
            for(int i = 0; i < notes.size(); i++) {
                if (!listOfNotes.contains(notes.get(i).getNoteTitle())) listOfNotes += notes.get(i).getNoteTitle() + "\n";
                ArrayList<Item> items = notes.get(i).getItems();
                for (int j = 0; j < items.size(); j++){
                    //Check if the title already exists, if not add it
                    listOfNotes += i + ": " + items.get(j).getItemDescription() + "\n";
                }
            }
        }
        return listOfNotes;
    }

    // runs a for loop through notes and checks if the note isn't archived
    public String listActiveNotes() {
        if(notes.isEmpty()) return "no active notes";
        String listOfActiveNotes = "";
        for (int i = 0; i < notes.size(); i++){
            ArrayList<Item> items = notes.get(i).getItems();
            if (!notes.get(i).isNoteArchived()) {
                if(!listOfActiveNotes.contains(notes.get(i).getNoteTitle())) listOfActiveNotes += notes.get(i).getNoteTitle() + "\n";
                for (int j = 0; j < items.size(); j++){
                    listOfActiveNotes += i + ": " + items.get(j).getItemDescription() + "\n";
                }
            }

        }
        return listOfActiveNotes;
    }

    // runs a for loop through notes and checks if the note is archived
    public String listArchivedNotes() {
        if (notes.isEmpty()) return "no archived notes";
        String listOfArchivedNotes = "";
        for (int i = 0; i < notes.size(); i++){
            ArrayList<Item> items = notes.get(i).getItems();
            if (notes.get(i).isNoteArchived()) {
                if(!listOfArchivedNotes.contains(notes.get(i).getNoteTitle())) listOfArchivedNotes += notes.get(i).getNoteTitle() + "\n";
                for (int j = 0; j < items.size(); j++){
                    listOfArchivedNotes += i + ": " + items.get(j).getItemDescription() + "\n";
                }
            }

        }
        return listOfArchivedNotes;
    }

    // runs a loop through the notes array and returns the list of notes by selected category
    public String listNotesBySelectedCategory(String category) {
        String result = "";
        int count = 0;
        if (notes.isEmpty()) {
            result = "No notes stored for category" + category;
        }

        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getNoteCategory().equals(category)) {
                count++;
                result += i + ": " + notes.get(i).getNoteTitle() + "\n";
                ArrayList<Item> items =  notes.get(i).getItems();
                for (int j = 0; j < items.size(); j++) {
                    result += j + ": " + items.get(j).getItemDescription();
                }
            }
        }
        if (count > 0) {
            result = count + " notes with category: " + category + result;
        } else {
            result = "no notes stored for category " + category;
        }
        return result;
    }

    // runs a loop through the notes array and returns the list of notes selected by priority
    public String listNotesBySelectedPriority(int priority) {
        String result = "";
        int count = 0;
        if (notes.isEmpty()) {
            result = "no notes stored for priority " + priority;
        }
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getNotePriority() == priority) {
                count++;
                result += i + ": " + notes.get(i).getNoteTitle() + "\n";
                ArrayList<Item> items =  notes.get(i).getItems();
                for (int j = 0; j < items.size(); j++) {
                    result += j + ": " + items.get(j);
                }
            }
        }
        if (count > -1) {
            result = count + " notes with priority " + priority + result;
        } else {
            result = "no notes stored for priority " + priority;
        }
        return result;
    }

    // running a loop through notes and items to check if the item is completed and returns the list of incomplete items
    public String listTodoItems(){
        String result = "";
        if (notes.isEmpty()) {
            return "No notes stored";
        }
        for (Note note: notes){
            ArrayList<Item> items = note.getItems();
            for (int i = 0; i < items.size(); i++){
                if (!items.get(i).isItemCompleted()) {
                    if (!result.contains(note.getNoteTitle())) result += note.getNoteTitle() + "\n";
                    result += i + ": " + items.get(i).getItemDescription()+ "\n";
                }
            }
        }
//        System.out.println(result);
        return result;
    }


    // running a loop through notes and items to check what category the items are in and if the notes are complete or not
    // returns lists of item status by category
    public String listItemStatusByCategory(String category) {
        if (notes.isEmpty()) {
            return "No notes stored";
        }
        String toDo = "";
        String completed = "";
        int toDoCount = 0;
        int completedCount = 0;

        for (Note note: notes) {
            if (note.getNoteCategory().equals(category)) {
                ArrayList<Item> items = note.getItems();
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isItemCompleted()) {
                        completedCount++;
                        completed += items.get(i).getItemDescription() + " (Note: " + note.getNoteTitle() + ") \n";

                    } else {
                        toDoCount++;
                        toDo += items.get(i).getItemDescription() + " (Note: " + note.getNoteTitle() + ") \n";
                    }
                }
            }
        }
        String result = "Number completed: " + completedCount + "\n" + completed
                + "Number TODO: " + toDoCount + "\n" + toDo;
        return result;
    }


    //finding / searching methods

    //find note at specific index location, returns item object or null if no object at index location
    public Note findNote(int index) {
        if (isValidIndex(index)) {
            return notes.get(index);
        }
        return null;
    }

    //runs a loop through notes and returns the title that contains the search string
    public String searchNotesByTitle(String searchString) {
        String results = "";
        if (notes.isEmpty()) return "No notes stored";
        for (int i = 0; i < notes.size(); i++) {
           if (notes.get(i).getNoteTitle().contains(searchString)) {
               results += i + notes.get(i).getNoteTitle();
           }
        }
        if (results.length() == 0) return "No notes found for: " + searchString;
        return results;
    }

    public String searchItemByDescription(String searchString) {
        String results = "";
        if (notes.isEmpty()) return "No notes stored";
        for (Note note: notes){
            ArrayList<Item> items = note.getItems();
            for (int i = 0; i < items.size(); i++) {
                if(items.get(i).getItemDescription().contains(searchString)) {
                    results += i + ": " + items.get(i).getItemDescription();
                    if (items.get(i).isItemCompleted()) {
                        results += ". [Completed] \n";
                    } else {
                        results += ". [TODO] \n";
                    }
                }
            }
        }
        if (results.length() == 0) return "No items found for: " + searchString;
        return results;
    }

    // persistence methods
    @SuppressWarnings("unchecked")
    public void load() throws Exception {
        //list of classes that you wish to include in the serialisation, separated by a comma
        Class<?>[] classes = new Class[] { Note.class };

        //setting up the xstream object with default security and the above classes
        XStream xstream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(classes);

        //doing the actual serialisation to an XML file
        ObjectInputStream is = xstream.createObjectInputStream(new FileReader("notes.xml"));
        notes = (ArrayList<Note>) is.readObject();
        is.close();
    }

    public void save() throws Exception {
        XStream xstream = new XStream(new DomDriver());
        ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter("notes.xml"));
        out.writeObject(notes);
        out.close();
    }

    }



