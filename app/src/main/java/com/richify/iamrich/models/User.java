package com.richify.iamrich.models;

/**
 * Created by thomaslin on 26/02/2018.
 */

public class User {

    public String fullName;
    public String email;
    public Double wealth;

    public User() {

    }

    public User(String fullName, String email, Double wealth) {
        this.fullName = fullName;
        this.email = email;
        this.wealth = wealth;
    }

}
