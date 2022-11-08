package utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CategoryUtilityTest {

    @Test
    void getCategoriesReturnsFullCategoriesSet() {
        ArrayList<String> categories = CategoryUtility.getCategories();
        assertEquals(6, categories.size());
        assertTrue(categories.contains("Home"));
        assertTrue(categories.contains("Work"));
        assertTrue(categories.contains("Hobby"));
        assertTrue(categories.contains("Holiday"));
        assertTrue(categories.contains("College"));
        assertTrue(categories.contains(""));
    }

    @Test
    void isValidCategoryTrueWhenCategoryExists() {
        assertTrue(CategoryUtility.isValidCategory("Home"));
        assertTrue(CategoryUtility.isValidCategory("home"));
        assertTrue(CategoryUtility.isValidCategory("WORK"));
        assertTrue(CategoryUtility.isValidCategory(""));
    }

    @Test
    void isValidCategoryFalseWhenCategoryDoesNotExist() {
        assertFalse(CategoryUtility.isValidCategory("Hom"));
        assertFalse(CategoryUtility.isValidCategory("workk"));
    }
}

