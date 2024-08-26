package com.estore.api.estoreapi.model;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a User entity
 * 
 * @author Quan Quy
 */

 /**
  * TODO: comment and get this doccumented
  */
public class User {
    private static final Logger LOG = Logger.getLogger(Product.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "user [id=%d, username=%s, password=%s, cart=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("username") private String username;
    @JsonProperty("password") private String password;
    @JsonProperty("cart") private ArrayList<ItemInCart> cart;


    public User(@JsonProperty("id") int id, @JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("cart") ArrayList<ItemInCart> cart) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.cart = cart;
    }

    public int getId() {return id;}

    public String getUsername() {return username;}

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword() {return password;}

    public void setPassword(String password){
        this.password = password;
    }

    public ArrayList<ItemInCart> getCart() {return cart;}

    public void setCart(ArrayList<ItemInCart> cart){this.cart = cart;}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,username,password,cart);
    }

}
