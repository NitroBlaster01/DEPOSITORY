package com.estore.api.estoreapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.model.ItemInCart;
import com.estore.api.estoreapi.persistence.UserDAO;

/**
 * Handles the REST API requests for the User resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author Quan Quy
 */

@RestController
@RequestMapping("users")
public class UserController {
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());
    private UserDAO userDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param userDao The {@link UserDAO User Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public UserController(UserDAO userDao) {
        this.userDao = userDao;
    }

    /**
     * Responds to the GET request for a {@linkplain User User} for the given id
     * 
     * @param id The id used to locate the {@link User User}
     * 
     * @return ResponseEntity with {@link User User} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        LOG.info("GET /users/" + id);
        try {
            User user = userDao.getUser(id);
            LOG.info("GET RESULTS:"+user);
            if (user != null)
                return new ResponseEntity<User>(user,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain User User}
     * 
     * @return ResponseEntity with array of {@link User User} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<User[]> getUsers() {
        LOG.info("GET /users");
        try {
            User[] users = userDao.getUsers();
            return new ResponseEntity<User[]>(users,HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain User User} whose name contains
     * the text in name
     * 
     * @param name The name parameter which contains the text used to find the {@link User User}
     * 
     * @return ResponseEntity with array of {@link User User} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all User that contain the text "ma"
     * GET http://localhost:8080/User/?name=ma
     */
    @GetMapping("/")
    public ResponseEntity<User[]> searchUser(@RequestParam String name) {
        LOG.info("GET /users/?name="+name);
        try {
            User[] user = userDao.findUsers(name);
            return new ResponseEntity<User[]>(user,HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain User user} with the provided User object
     * 
     * @param User - The {@link User user} to create
     * 
     * @return ResponseEntity with created {@link User user} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link User user} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        LOG.info("POST /Users " + user);
        try{
            User result = userDao.createUser(user);
            if(result!=null)
                return new ResponseEntity<User>(result, HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch(IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //FIXME: fix this, this suppose to check for the same user but it has trash algorithm
    // @PostMapping("")
    // public ResponseEntity<User> createUser(@RequestBody User user) {
    //     LOG.info("POST /users " + user);
    //     Boolean isValid = true;
    //     try{
    //         User[] userSearched = userDao.getUsers();
    //         for (User userIter : userSearched) {
    //             if(userIter.getUsername().equals(user.getUsername())){
    //                 isValid = false;
    //             }
    //         }
    //         if (isValid) {
    //             User result = userDao.createUser(user);
    //             if(result!=null)
    //                 return new ResponseEntity<User>(result, HttpStatus.CREATED);
    //             else
    //                 return new ResponseEntity<>(HttpStatus.CONFLICT);
    //         }
    //         else {
    //             return new ResponseEntity<>(HttpStatus.CONFLICT);
    //         }
    //     }
    //     catch(IOException e){
    //         LOG.log(Level.SEVERE,e.getLocalizedMessage());
    //         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }
    

    /**
     * Updates the {@linkplain User User} with the provided {@linkplain User User} object, if it exists
     * 
     * @param User The {@link User User} to update
     * 
     * @return ResponseEntity with updated {@link User User} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        LOG.info("PUT /users " + user);
        try{
            User userUpdated = userDao.updateUser(user);
            if (userUpdated != null)
                return new ResponseEntity<User>(userUpdated,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain User User} with the given id
     * 
     * @param id The id of the {@link User User} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable int id) {
        LOG.info("DELETE /users/" + id);
        try{
            if (userDao.deleteUser(id) == true)
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/authorization/{username}/{password}")
    public ResponseEntity<User> getCurrentUser(@PathVariable String username, @PathVariable String password) {
        LOG.info("GET users/authorization/" + username + "/" + password);
        try {
            User user = userDao.logInAsUser(username, password);
            if (user != null) {
                return new ResponseEntity<User>(user, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////Cart////////////////////////////////////

    @GetMapping("/{userId}/cart")
    public ResponseEntity<ArrayList<ItemInCart>> getCart(@PathVariable int userId) {
        LOG.info("GET /users/" + userId + "/cart");
        try {
            ArrayList<ItemInCart> itemsInCart = userDao.getCart(userId);
            if (itemsInCart != null && itemsInCart.size() > 0)
                return new ResponseEntity<ArrayList<ItemInCart>>(itemsInCart,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{userId}/cart")
    public ResponseEntity<ItemInCart> addItemInCart(@PathVariable int userId, @RequestBody ItemInCart item) {
        LOG.info("POST /users/ " + userId + "/cart/" + item);
        Boolean isValid = true;
        try{
            ArrayList<ItemInCart> itemsSearched = userDao.getCart(userId);
            for (ItemInCart itemIter : itemsSearched) {
                if(itemIter.getId() == item.getId()){
                    isValid = false;
                }
            }
            if (isValid) {
                ItemInCart itemUpdated = userDao.addItemInCart(userId, item);
                if (itemUpdated != null)
                    return new ResponseEntity<ItemInCart>(itemUpdated,HttpStatus.OK);
                else
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{userId}/cart/{itemId}")
    public ResponseEntity<ItemInCart> deleteItemInCart(@PathVariable int userId, @PathVariable int itemId) {
        LOG.info("DELETE /users/" + userId + "/cart/" + itemId);
        try{
            if (userDao.deleteItemInCart(userId, itemId) == true)
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{userId}/cart")
    public ResponseEntity<ItemInCart> updateItemInCart(@PathVariable int userId, @RequestBody ItemInCart item) {
        LOG.info("PUT /users/" + userId + "/cart/" + item);
        try{
            ItemInCart itemUpdated = userDao.updateItemInCart(userId, item);
            if (itemUpdated != null)
                return new ResponseEntity<ItemInCart>(itemUpdated,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

