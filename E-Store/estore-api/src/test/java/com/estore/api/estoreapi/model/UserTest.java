package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


/**
 * Unit test for User class
 * 
 * @author Quan Quy
 */
@Tag("Model-tier")
public class UserTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 99;
        String expected_username = "test99";
        String expected_password = "password99";
        ArrayList<ItemInCart> expected_cart = new ArrayList<ItemInCart>();

        // Invoke
        User user = new User(expected_id,expected_username,expected_password,expected_cart);

        // Analyze
        assertEquals(expected_id,user.getId());
        assertEquals(expected_username,user.getUsername());
        assertEquals(expected_password,user.getPassword());
        assertEquals(expected_cart,user.getCart());
    }

    @Test
    public void testName() {
        // Setup
        int id = 99;
        String username = "test99";
        String password = "password99";
        ArrayList<ItemInCart> cart = new ArrayList<ItemInCart>();


        User user = new User(id,username,password,cart);

        String expected_name = "Galactic Agent";

        // Invoke
        user.setUsername(expected_name);

        // Analyze
        assertEquals(expected_name,user.getUsername());
    }

    @Test
    public void testPassword() {
        // Setup
        int id = 99;
        String username = "test99";
        String password = "passsssssss";
        ArrayList<ItemInCart> cart = new ArrayList<ItemInCart>();


        User user = new User(id,username,password,cart);

        String expected_password = "password99";

        // Invoke
        user.setPassword(expected_password);

        // Analyze
        assertEquals(expected_password,user.getPassword());
    }

    @Test
    public void testCart() {
        // Setup
        int id = 99;
        String username = "test99";
        String password = "password99";
        ArrayList<ItemInCart> cart = new ArrayList<ItemInCart>();


        User user = new User(id,username,password,cart);

        ArrayList<ItemInCart> expected_cart = new ArrayList<ItemInCart>();
        expected_cart.add(new ItemInCart(11, 22));

        // Invoke
        user.setCart(expected_cart);

        // Analyze
        assertEquals(expected_cart,user.getCart());
    }


    @Test
    public void testToString() {
        // Setup
        int id = 99;
        String username = "test99";
        String password = "password99";
        ArrayList<ItemInCart> cart = new ArrayList<ItemInCart>();


        String expected_string = String.format(User.STRING_FORMAT,id,username,password,cart);
        User user = new User(id,username,password,cart);

        // Invoke
        String actual_string = user.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }
}
