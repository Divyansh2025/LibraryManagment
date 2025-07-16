package com.library.service;

import java.sql.SQLException;

@FunctionalInterface
public interface UserService {
   
    boolean login(String username, String password) throws SQLException;

    // Default method to show all books, with a default implementation
    default void showAllBooks() throws SQLException {
        System.out.println("Showing all books...");
    }
}
