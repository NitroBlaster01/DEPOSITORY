package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


/**
 * Unit test for ItemInCart class
 * 
 * @author Quan Quy
 */
@Tag("Model-tier")
public class ItemInCartTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 99;
        int expected_quantity = 11;

        // Invoke
        ItemInCart hero = new ItemInCart(expected_id,expected_quantity);

        // Analyze
        assertEquals(expected_id,hero.getId());
        assertEquals(expected_quantity,hero.getQuantity());
    }

    @Test
    public void testId() {
        // Setup
        int id = 99;
        int quantity = 11;
        ItemInCart item = new ItemInCart(id,quantity);

        int expected_id = 100;

        // Invoke
        item.setId(expected_id);

        // Analyze
        assertEquals(expected_id,item.getId());
    }


    @Test
    public void testQuantity() {
        // Setup
        int id = 99;
        int quantity = 11;
        ItemInCart item = new ItemInCart(id,quantity);

        int expected_quantity = 100;

        // Invoke
        item.setId(expected_quantity);

        // Analyze
        assertEquals(expected_quantity,item.getId());
    }

    

    @Test
    public void testToString() {
        // Setup
        int id = 99;
        int quantity = 11;
        String expected_string = String.format(ItemInCart.STRING_FORMAT,id,quantity);
        ItemInCart item = new ItemInCart(id, quantity);

        // Invoke
        String actual_string = item.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }
    
}
