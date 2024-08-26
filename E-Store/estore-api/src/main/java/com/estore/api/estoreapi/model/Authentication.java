package com.estore.api.estoreapi.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Authentication {
    // Package private for tests
    static final String STRING_FORMAT = "Authentication [username=%s, password=%s]";

    @JsonProperty("UserList") private ArrayList<User> userlist;

    public Authentication(@JsonProperty("userlist") ArrayList<User> userlist) {
        if (userlist!= null){
            this.userlist = userlist;
        }
        else{
            this.userlist = new ArrayList<>();
        }
    }

    public User CheckUser (String username , String password){
        for(int i = 0; i < userlist.size(); i++){
            if(userlist.get(i).getUsername().equals(username) && 
            userlist.get(i).getPassword().equals(password)){
                return userlist.get(i);
            }
        }
        return null;
    }

    public ArrayList<User> getUserList(){
        return userlist;
    }
}
