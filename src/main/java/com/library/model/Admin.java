package com.library.model;

public class Admin extends User {
    
    // Constructor
    public Admin(int id, String username, String password) {
        super(id, username, password);  // Call the parent class constructor
    }

    // Implement the login method for Admin
    @Override
    public boolean login(String username, String password) {
        // Logic for admin login
        return getUsername().equals(username) && getPassword().equals(password);
    }
}
