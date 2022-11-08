package models;

import utils.Utilities;

import java.util.Objects;

public class Item {
    // fields

    private String itemDescription = "No Description";
    private boolean isItemCompleted = false;

    // constructors
    public Item(String itemDescription) {

        this.itemDescription = Utilities.truncateString(itemDescription, 50);
    }

    public Item(String itemDescription, boolean isItemCompleted) {
        this.itemDescription = Utilities.truncateString(itemDescription, 50);
        this.isItemCompleted = isItemCompleted;
    }

    // getters
    public String getItemDescription() {

        return itemDescription;
    }

    public boolean isItemCompleted() {
        return isItemCompleted;
    }

    // setters
    public void setItemDescription(String itemDescription) {
        if (Utilities.validateStringLength(itemDescription, 50)) {
            this.itemDescription = itemDescription;
        }
    }


    public void setItemCompleted(boolean itemCompleted) {
        isItemCompleted = itemCompleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return isItemCompleted == item.isItemCompleted && Objects.equals(itemDescription, item.itemDescription);
    }

    // toString
    @Override
    public String toString() {
        return itemDescription
                + Utilities.booleanToDoCompleted(isItemCompleted);
    }

}
