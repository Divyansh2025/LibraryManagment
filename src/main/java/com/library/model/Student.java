package com.library.model;

public class Student extends User {
    
    // Constructor
    public Student(int id, String username, String password) {
        super(id, username, password);  // Call the parent class constructor
    }


    @Override
    public boolean login(String username, String password) {
        
        return getUsername().equals(username) && getPassword().equals(password);
    }
}
