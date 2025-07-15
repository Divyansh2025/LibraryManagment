package com.library.model;

public class Student extends User {
    
    // Constructor
    public Student(int id, String username, String password) {
        super(id, username, password);  // Call the parent class constructor
    }

    // Implement the login method for Student
    @Override
    public boolean login(String username, String password) {
        // Logic for student login (for simplicity, assuming it's always valid)
        return getUsername().equals(username) && getPassword().equals(password);
    }
}
