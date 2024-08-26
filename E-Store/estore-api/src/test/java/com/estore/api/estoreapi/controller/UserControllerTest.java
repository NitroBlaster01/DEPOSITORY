package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;

import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.model.ItemInCart;
import com.estore.api.estoreapi.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the User Controller class
 * 
 * @author Aydan Reyes, Quan Quy
 */
@Tag("Controller-tier")
public class UserControllerTest {
    private UserController userController;
    private UserDAO mockUserDAO;

    /**
     * Before each test, create a new UserController object and inject
     * a mock User DAO
     */
    @BeforeEach
    public void setupUserController() {
        mockUserDAO = mock(UserDAO.class);
        userController = new UserController(mockUserDAO);
    }

    @Test
    public void testGetUser() throws IOException {
        //Setup
        ArrayList<ItemInCart> cart = new ArrayList<ItemInCart>();
        User user = new User(99, "Test", "password", cart);

        when(mockUserDAO.getUser(user.getId())).thenReturn(user);

        //Invoke
        ResponseEntity<User> response = userController.getUser(user.getId());

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testGetUserNotFound() throws IOException {
        //Setup
        int userId = 99;

        when(mockUserDAO.getUser(userId)).thenReturn(null);

        //Invoke
        ResponseEntity<User> response = userController.getUser(userId);

        //Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetUserHandleException() throws Exception {
        //Setup 
        int userId = 99;

        doThrow(new IOException()).when(mockUserDAO).getUser(userId);

        //Invoke
        ResponseEntity<User> response = userController.getUser(userId);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreateUser() throws IOException {
        //Setup
        ArrayList<ItemInCart> cart = new ArrayList<ItemInCart>();
        User user = new User(99, "Test", "password", cart);

        when(mockUserDAO.createUser(user)).thenReturn(user);

        //Invoke
        ResponseEntity<User> response = userController.createUser(user);

        //Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testCreateUserFailed() throws IOException {
        //Setup
        ArrayList<ItemInCart> cart = new ArrayList<ItemInCart>();
        User user = new User(99, "Test", "password", cart);

        when(mockUserDAO.createUser(user)).thenReturn(null);

        //Invoke
        ResponseEntity<User> response = userController.createUser(user);

        //Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateUserHandleException() throws Exception {
        //Setup
        ArrayList<ItemInCart> cart = new ArrayList<ItemInCart>();
        User user = new User(99, "Test", "password", cart);

        doThrow(new IOException()).when(mockUserDAO).createUser(user);

        //Invoke
        ResponseEntity<User> response = userController.createUser(user);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateUser() throws IOException {
        //Setup
        ArrayList<ItemInCart> cart = new ArrayList<ItemInCart>();
        User user = new User(99, "Test", "password", cart);

        when(mockUserDAO.updateUser(user)).thenReturn(user);
        ResponseEntity<User> response = userController.updateUser(user);
        user.setUsername("Rename");

        //Invoke
        response = userController.updateUser(user);

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testUpdateUserFailed() throws IOException {
        //Setup
        ArrayList<ItemInCart> cart = new ArrayList<ItemInCart>();
        User user = new User(99, "Test", "password", cart);

        when(mockUserDAO.updateUser(user)).thenReturn(null);

        //Invoke
        ResponseEntity<User> response = userController.updateUser(user);

        //Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateUserHandleException() throws Exception {
        //Setup
        ArrayList<ItemInCart> cart = new ArrayList<ItemInCart>();
        User user = new User(99, "Test", "password", cart);

        doThrow(new IOException()).when(mockUserDAO).updateUser(user);

        //Invoke
        ResponseEntity<User> response = userController.updateUser(user);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetUsers() throws IOException {
        //Setup
        ArrayList<ItemInCart> cart1 = new ArrayList<ItemInCart>();
        ArrayList<ItemInCart> cart2 = new ArrayList<ItemInCart>();
        User[] users = new User[2];
        users[0] = new User(99, "Test1", "pass1", cart1);
        users[1] = new User(100, "Test2", "pass2", cart2);

        when(mockUserDAO.getUsers()).thenReturn(users);

        //Invoke
        ResponseEntity<User[]> response = userController.getUsers();

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    public void testGetUsersHandleException() throws IOException {
        doThrow(new IOException()).when(mockUserDAO).getUsers();

        //Invoke
        ResponseEntity<User[]> response = userController.getUsers();

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchUsers() throws IOException {
        //Setup
        String searchString = "es";
        ArrayList<ItemInCart> cart1 = new ArrayList<ItemInCart>();
        ArrayList<ItemInCart> cart2 = new ArrayList<ItemInCart>();
        User[] users = new User[2];
        users[0] = new User(99, "Test1", "pass1", cart1);
        users[1] = new User(100, "Test2", "pass2", cart2);

        when(mockUserDAO.findUsers(searchString)).thenReturn(users);

        //Invoke
        ResponseEntity<User[]> response = userController.searchUser(searchString);

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    public void testSearchUsersHandleException() throws IOException {
        String searchString = "es";

        doThrow(new IOException()).when(mockUserDAO).findUsers(searchString);

        //Invoke
        ResponseEntity<User[]> response = userController.searchUser(searchString);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteUser() throws IOException {
        //Setup
        int userId = 99;

        when(mockUserDAO.deleteUser(userId)).thenReturn(true);

        //Invoke
        ResponseEntity<User> response = userController.deleteUser(userId);

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteUserNotFound() throws IOException {
        //Setup
        int userId = 99;

        when(mockUserDAO.deleteUser(userId)).thenReturn(false);

        //Invoke
        ResponseEntity<User> response = userController.deleteUser(userId);

        //Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteUserHandleException() throws IOException {
        //Setup
        int userId = 99;

        doThrow(new IOException()).when(mockUserDAO).deleteUser(userId);

        //Invoke
        ResponseEntity<User> response = userController.deleteUser(userId);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetCart() throws IOException {
        //Setup
        ArrayList<ItemInCart> cart = new ArrayList<ItemInCart>();
        cart.add(new ItemInCart(11, 12));
        User user = new User(99, "Test", "password", cart);

        when(mockUserDAO.getCart(user.getId())).thenReturn(cart);

        //Invoke
        ResponseEntity<ArrayList<ItemInCart>> response = userController.getCart(user.getId());

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    public void testGetCartEmpty() throws IOException {
        //Setup
        int userId = 99;

        when(mockUserDAO.getCart(userId)).thenReturn(null);

        //Invoke
        ResponseEntity<ArrayList<ItemInCart>> response = userController.getCart(userId);

        //Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetCartHandleException() throws IOException {
        //Setup
        int cartId = 99;

        doThrow(new IOException()).when(mockUserDAO).getCart(cartId);

        //Invoke
        ResponseEntity<ArrayList<ItemInCart>> response = userController.getCart(cartId);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddItemInCart() throws IOException {
        //Setup
        ArrayList<ItemInCart> cart = new ArrayList<ItemInCart>();
        User user = new User(99, "Test", "password", cart);
        ItemInCart itemInCart = new ItemInCart(100, 100);

        when(mockUserDAO.addItemInCart(user.getId(), itemInCart)).thenReturn(itemInCart);

        //Invoke
        ResponseEntity<ItemInCart> response = userController.addItemInCart(user.getId(), itemInCart);

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(itemInCart, response.getBody());
    }

    @Test
    public void testItemInCartFailed() throws IOException {
        //Setup
        ArrayList<ItemInCart> cart = new ArrayList<ItemInCart>();
        User user = new User(99, "Test", "password", cart);
        ItemInCart itemInCart = new ItemInCart(100, 100);

        when(mockUserDAO.addItemInCart(user.getId(), itemInCart)).thenReturn(null);

        //Invoke
        ResponseEntity<ItemInCart> response = userController.addItemInCart(user.getId(), itemInCart);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testAddItemInCartHandleException() throws IOException {
        //Setup
        ArrayList<ItemInCart> cart = new ArrayList<ItemInCart>();
        User user = new User(99, "Test", "password", cart);
        ItemInCart itemInCart = new ItemInCart(100, 100);

        doThrow(new IOException()).when(mockUserDAO).addItemInCart(user.getId(), itemInCart);

        //Invoke
        ResponseEntity<ItemInCart> response = userController.addItemInCart(user.getId(), itemInCart);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    //TODO: the case where 2 items have the same id

    @Test
    public void testDeleteItemInCart() throws IOException {
        //Setup
        int userId = 99;
        int productId = 99;

        when(mockUserDAO.deleteItemInCart(userId, productId)).thenReturn(true);

        //Invoke
        ResponseEntity<ItemInCart> response = userController.deleteItemInCart(userId, productId);
        
        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteItemInCartNotFound() throws IOException {
        //Setup
        int userId = 99;
        int productId = 99;

        when(mockUserDAO.deleteItemInCart(userId, productId)).thenReturn(false);

        //Invoke
        ResponseEntity<ItemInCart> response = userController.deleteItemInCart(userId, productId);

        //Anaylze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteProductInCartHandleException() throws IOException {
        //Setup
        int userId = 99;
        int productId = 99;

        doThrow(new IOException()).when(mockUserDAO).deleteItemInCart(userId, productId);

        //Invoke
        ResponseEntity<ItemInCart> response = userController.deleteItemInCart(userId, productId);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateItemInCart() throws IOException {
        //Setup
        int userId = 99;
        ItemInCart itemInCart = new ItemInCart(100, 100);

        when(mockUserDAO.updateItemInCart(userId, itemInCart)).thenReturn(itemInCart);
        ResponseEntity<ItemInCart> response = userController.updateItemInCart(userId, itemInCart);
        itemInCart.setQuantity(20);

        //Invoke
        response = userController.updateItemInCart(userId, itemInCart);

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(itemInCart, response.getBody());
    }

    @Test
    public void testUpdateItemInCartNotFound() throws IOException {
        //Setup
        int userId = 99;
        ItemInCart itemInCart = new ItemInCart(99, 100);

        when(mockUserDAO.updateItemInCart(userId, itemInCart)).thenReturn(null);

        //Invoke
        ResponseEntity<ItemInCart> response = userController.updateItemInCart(userId, itemInCart);

        //Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateItemInCartHandleException() throws IOException {
        //Setup
        int userId = 99;
        ItemInCart itemInCart = new ItemInCart(99, 100);

        doThrow(new IOException()).when(mockUserDAO).updateItemInCart(userId, itemInCart);

        //Invoke
        ResponseEntity<ItemInCart> response = userController.updateItemInCart(userId, itemInCart);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
