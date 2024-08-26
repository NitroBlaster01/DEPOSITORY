package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.model.ItemInCart;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit test for the User File DAO
 * 
 * @author Elijah Thibodeau, Quan Quy
 * 
 */
@Tag("Persistence-tier")
public class UserFileDAOTest {
    UserFileDAO userFileDAO;
    ObjectMapper mockObjectMapper;
    User[] tUsers;

    @BeforeEach
    public void setupUserFileDAO() throws IOException {// Setup

        mockObjectMapper = mock(ObjectMapper.class);// initialize the mock object mapper used in UserFileDAO Constructor
        tUsers = new User[3];

        tUsers[0] = new User(1, "user1", "test",
                new ArrayList<ItemInCart>(Arrays.asList(new ItemInCart(11, 1), new ItemInCart(12, 2))));
        tUsers[1] = new User(2, "user2", "test", new ArrayList<ItemInCart>());
        tUsers[2] = new User(3, "bill", "password123",
                new ArrayList<ItemInCart>(Arrays.asList(new ItemInCart(1, 2), new ItemInCart(13, 5))));
        // Create the mock list of Users

        when(mockObjectMapper.readValue(new File("asdf.txt"), User[].class)).thenReturn(tUsers);
        // Whenever the objectMapper tries to read the user file (indicating that
        // something in the DAO wants a list of all the users), they instead are given
        // the mock list of users.

        userFileDAO = new UserFileDAO("asdf.txt", mockObjectMapper);
        // Make a new mock userFileDAO in such a way that when the mock DAO wants to get
        // the users, its given our fake list of users.
    }

    @Test
    public void testGetUsers() {
        // No setup needed, already done in the setupUserFileDAO() method.

        User[] users = userFileDAO.getUsers();
        // Invoke- actualUsers represents the users that are actually produced by the
        // current version of our DAO

        assertEquals(users.length, tUsers.length);
        // Analyze
        for (int i = 0; i < tUsers.length;++i)
            assertEquals(users[i], tUsers[i]);
    }

    @Test
    public void testFindUsers() {
        // Invoke
        User[] users = userFileDAO.findUsers("ll");

        // Analyze
        assertEquals(users.length,1);
        assertEquals(users[0],tUsers[2]);
    }

    @Test
    public void testGetUser() {
        // Invoke
        User user = userFileDAO.getUser(1);

        // Analzye
        assertEquals(user,tUsers[0]);
    }

    @Test
    public void testCreateUser(){
        // Setup
        User user = new User(4, "created", "07041776", new ArrayList<ItemInCart>());

        // Invoke
        User result = assertDoesNotThrow(() -> userFileDAO.createUser(user),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        User actual = userFileDAO.getUser(user.getId());
        assertEquals(actual.getId(),user.getId());
        assertEquals(actual.getUsername(),user.getUsername());
        assertEquals(actual.getPassword(),user.getPassword());
    }
    
    @Test
    public void testUpdateUser() {
        // Setup
        User user = new User(1,"updateduser", "updatedpass", new ArrayList<ItemInCart>());

        // Invoke
        User result = assertDoesNotThrow(() -> userFileDAO.updateUser(user),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        User actual = userFileDAO.getUser(user.getId());
        assertEquals(actual,user);
    }

    /**
     * The save method is the part that will throw IOExceptions in:
     * createUser, updateUser and deleteUser. Therefore, we only need to do one exception test. 
     */
    @Test
    public void testSaveUserException() throws IOException {
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(User[].class));

        User user = new User(4, "user4", "password4", new ArrayList<ItemInCart>());

        assertThrows(IOException.class, () -> userFileDAO.createUser(user), "IOException not thrown");
    }

    @Test
    public void testGetUserNotFound() {
        // Invoke
        User user = userFileDAO.getUser(99);

        // Analyze
        assertEquals(user,null);
    }

    @Test
    public void testDeleteUserNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser(98),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(userFileDAO.users.size(),tUsers.length);
    }

    @Test
    public void testUpdateUserNotFound() {
        // Setup
        User user = new User(9,"user9","pass9", new ArrayList<ItemInCart>());

        // Invoke
        User result = assertDoesNotThrow(() -> userFileDAO.updateUser(user),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the UserFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("asdf.txt"),User[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new UserFileDAO("asdf.txt",mockObjectMapper),
                        "IOException not thrown");
    }

     @Test
     public void testDeleteUser(){
        // Invoke
        boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser(1),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test useres array - 1 (because of the delete)
        // Because useres attribute of userFileDAO is package private
        // we can access it directly
        assertEquals(userFileDAO.users.size(),tUsers.length-1);
     }


     @Test
     public void testGetCart() throws IOException{
        // Invoke
        ArrayList<ItemInCart> items = userFileDAO.getCart(1);

        // Analzye
        assertEquals(items,tUsers[0].getCart());
     }

     @Test
     public void testAddItemInCart() throws IOException{
        ItemInCart item = userFileDAO.addItemInCart(1, new ItemInCart(4, 44));

        ItemInCart result = assertDoesNotThrow(() -> userFileDAO.addItemInCart(1, item),"Unexpected exception thrown");

        // Analzye
        assertNotNull(result);
        ArrayList<ItemInCart> actual = userFileDAO.getCart(1);
        assertEquals(actual.get(actual.size()-1).getId(), item.getId());
        assertEquals(actual.get(actual.size()-1).getQuantity(), item.getQuantity());
     }

     @Test
     public void testDeleteItemInCart() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser(1),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test users array - 1 (because of the delete)
        // Because users attribute of HeroFileDAO is package private
        // we can access it directly
        assertEquals(userFileDAO.users.size(),tUsers.length-1);
     }

     @Test
    public void testDeleteItemInCartNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser(1111),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(userFileDAO.users.size(),tUsers.length);
    }

 
     @Test
     public void testUpdateItemInCart(){
        // Setup
        ItemInCart item = new ItemInCart(11,44);

        // Invoke
        ItemInCart result = assertDoesNotThrow(() -> userFileDAO.updateItemInCart(1, item),
                                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        assertEquals(item.getId(),result.getId());
        assertEquals(item.getQuantity(),result.getQuantity());
     }
     
     @Test
     public void testUpdateItemInCartNotFound() throws IOException{
        ItemInCart item = new ItemInCart(9999,44);

        // Invoke
        ItemInCart result = assertDoesNotThrow(() -> userFileDAO.updateItemInCart(1, item),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
     }

}
