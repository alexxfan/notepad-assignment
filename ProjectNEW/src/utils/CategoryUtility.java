package utils;

import java.util.ArrayList;


public class CategoryUtility {
//    https://alvinalexander.com/source-code/java/how-create-populate-static-list-arraylist-linkedlist-syntax-in-java/
    static ArrayList<String> categories = new ArrayList<String>() {{
        add("Home");
        add("Work");
        add("Hobby");
        add("Holiday");
        add("College");
        add("");
    }};
    static ArrayList<String> getCategories() {
        return categories;
    }


    public static boolean isValidCategory(String category){
        for (String c : categories) {
            if (c.toUpperCase().equals(category.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

}