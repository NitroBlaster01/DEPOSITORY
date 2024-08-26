package com.estore.api.estoreapi.model;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")

public class AuthenticationTest {

    @Test
    public void testCtor(){

        //setup
        int expected_user1_id = 1;
        String expected_user1_username = "user1";
        String expected_user1_password = "password1";
        ArrayList<ItemInCart> expected_user1_cart = new ArrayList<ItemInCart>();
        User user1 = new User(expected_user1_id,expected_user1_username,
                            expected_user1_password,expected_user1_cart);

        int expected_user2_id = 2;
        String expected_user2_username = "user2";
        String expected_user2_password = "password2";
        ArrayList<ItemInCart> expected_user2_cart = new ArrayList<ItemInCart>();
        User user2 = new User(expected_user2_id,expected_user2_username,
                            expected_user2_password,expected_user2_cart);

        ArrayList<User> userlist = new ArrayList<>();
        userlist.add(user1);
        userlist.add(user2);

        //invoke
        Authentication authentication = new Authentication(userlist);

        //Analyze
        assertEquals(userlist,authentication.getUserList());
    }

    @Test
    // test correct username and password
    public void testCheckUser(){

        //setup
        int expected_user1_id = 1;
        String expected_user1_username = "user1";
        String expected_user1_password = "password1";
        ArrayList<ItemInCart> expected_user1_cart = new ArrayList<ItemInCart>();
        User user1 = new User(expected_user1_id,expected_user1_username,
                            expected_user1_password,expected_user1_cart);

        int expected_user2_id = 2;
        String expected_user2_username = "user2";
        String expected_user2_password = "password2";
        ArrayList<ItemInCart> expected_user2_cart = new ArrayList<ItemInCart>();
        User user2 = new User(expected_user2_id,expected_user2_username,
                            expected_user2_password,expected_user2_cart);

        ArrayList<User> userlist = new ArrayList<>();
        userlist.add(user1);
        userlist.add(user2);
        Authentication authentication = new Authentication(userlist);

        //invoke
        User expectedUser1 = user1;
        User expectedUser2 = user2;
        User expectedUserNull1 = null;
        User expectedUserNull2 = null;

        //Analyze
        assertEquals(expectedUser1,authentication.CheckUser(expected_user1_username, expected_user1_password));
        assertEquals(expectedUser2,authentication.CheckUser(expected_user2_username, expected_user2_password));
        assertEquals(expectedUserNull1,authentication.CheckUser(expected_user1_username, expected_user2_password));
        assertEquals(expectedUserNull2,authentication.CheckUser(" " , " "));
    }
    

}
