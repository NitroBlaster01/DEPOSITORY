package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.estore.api.estoreapi.model.ItemInCart;
import com.estore.api.estoreapi.model.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implements the functionality for JSON file-based peristance for users
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author Quan Quy
 */
@Component
public class UserFileDAO implements UserDAO {
    private static final Logger LOG = Logger.getLogger(UserFileDAO.class.getName());
    Map<Integer,User> users;   // Provides a local cache of the User objects
                                // so that we don't need to read from the file
                                // each time
    Map<Integer, ItemInCart> items;
    
    private ObjectMapper objectMapper;  // Provides conversion between User
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new User
    private String filename;    // Filename to read from and write to

    /**
     * Creates a User File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public UserFileDAO(@Value("${users.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the users from the file
    }

    /**
     * Generates the next id for a new {@linkplain User User}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain User users} from the tree map
     * 
     * @return  The array of {@link User users}, may be empty
     */
    private User[] getUsersArray() {
        return getUsersArray(null);
    }

    /**
     * Generates an array of {@linkplain User users} from the tree map for any
     * {@linkplain User users} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain User users}
     * in the tree map
     * 
     * @return  The array of {@link User users}, may be empty
     */
    private User[] getUsersArray(String containsText) { // if containsText == null, no filter
        ArrayList<User> userArrayList = new ArrayList<>();

        for (User user : users.values()) {
            if (containsText == null || user.getUsername().contains(containsText)) {
                userArrayList.add(user);
            }
        }

        User[] userArray = new User[userArrayList.size()];
        userArrayList.toArray(userArray);
        return userArray;
    }

    /**
     * Saves the {@linkplain User users} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link User users} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        User[] UserArray = getUsersArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),UserArray);
        return true;
    }

    /**
     * Loads {@linkplain User users} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        users = new TreeMap<>();
        // items = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of users
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        User[] userArray = objectMapper.readValue(new File(filename),User[].class);

        // ItemInCart[] itemArray = objectMapper.readValue(new File(filename), ItemInCart[].class);

        // Add each User to the tree map and keep track of the greatest id
        for (User user : userArray) {
            users.put(user.getId(),user);
            if (user.getId() > nextId)
                nextId = user.getId();
        }

        // for (ItemInCart item : itemArray){
        //     items.put(item.getId(),item);
        // }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public User[] getUsers() {
        synchronized(users) {
            return getUsersArray();
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public User[] findUsers(String containsText) {
        synchronized(users) {
            return getUsersArray(containsText);
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public User getUser(int id) {
        synchronized(users) {
            if (users.containsKey(id))
                return users.get(id);
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public User createUser(User User) throws IOException {
        synchronized(users) {
            // Added this to check same name
            for (User u : users.values()){
                if(u.getUsername().equals(User.getUsername())){
                    save();
                    return null;
                }
            }
            // We create a new User object because the id field is immutable
            // and we need to assign the next unique id
            User newUser = new User(nextId(),User.getUsername(),User.getPassword(),User.getCart()== null ? new ArrayList<ItemInCart>() : User.getCart());
            users.put(newUser.getId(),newUser);
            save(); // may throw an IOException
            return newUser;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public User updateUser(User User) throws IOException {
        synchronized(users) {
            if (users.containsKey(User.getId()) == false)
                return null;  // User does not exist

            users.put(User.getId(),User);
            save(); // may throw an IOException
            return User;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteUser(int id) throws IOException {
        synchronized(users) {
            if (users.containsKey(id)) {
                users.remove(id);
                return save();
            }
            else
                return false;
        }
    }

    @Override
    public User logInAsUser(String username, String password) throws IOException{
        synchronized(users) {
            for (User u : users.values()){
                if(u.getUsername().equals(username) && u.getPassword().equals(password)){
                    return u;
                }
            }
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////// ////////////////// Cart ////////////////// ////////////

    /**
     * Get cart with id and return null if cannot find
     */
    @Override
    public ArrayList<ItemInCart> getCart(int id) throws IOException{
        synchronized(users) {
            if (users.containsKey(id)){
                return users.get(id).getCart();
            }
            else {
                return null;
            }
        }
    }

    @Override
    public ItemInCart addItemInCart(int id, ItemInCart item) throws IOException {
        // synchronized(item) {
            ItemInCart newItem = new ItemInCart(item.getId(),item.getQuantity());
            getCart(id).add(newItem);
            // items.put(item.getId(), newItem);
            save(); // may throw an IOException
            return newItem;
        // }
    }

    @Override
    public boolean deleteItemInCart(int userId, int itemId) throws IOException{
        ArrayList<ItemInCart> cart = getCart(userId);
        for (ItemInCart product : cart){
            if(product.getId() == itemId){
                cart.remove(product);
                save();
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemInCart updateItemInCart(int userId, ItemInCart item) throws IOException {
        ArrayList<ItemInCart> cart = getCart(userId);
        for (ItemInCart product : cart){
            if(product.getId() == item.getId()){
                product.setId(item.getId());
                product.setQuantity(item.getQuantity());
                save();
                return product;
            }
        }
        return null;
    }

}
