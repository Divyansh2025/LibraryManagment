package com.library.dao;

import com.library.model.BookRequest;
import com.library.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRequestDAO {
    private Connection connection;

    public BookRequestDAO() {
        connection = DBUtil.getConnection();
    }

    // Method to add a book request
    public void addBookRequest(BookRequest bookRequest) throws SQLException {
        // Check if the book status is 'available'
        String checkStatusQuery = "SELECT status FROM books WHERE id = ?";
        try (PreparedStatement checkStatusStatement = connection.prepareStatement(checkStatusQuery)) {
            checkStatusStatement.setInt(1, bookRequest.getBookId());
            ResultSet resultSet = checkStatusStatement.executeQuery();
            
            if (resultSet.next() && "available".equals(resultSet.getString("status"))) {
                // Book is available, so we can add the request
                String query = "INSERT INTO book_requests (student_id, book_id) VALUES (?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, bookRequest.getStudentId());
                    statement.setInt(2, bookRequest.getBookId());
                    int rowsInserted = statement.executeUpdate();
                    
                    if (rowsInserted > 0) {
                        System.out.println("Book request added successfully.");
                    } else {
                        System.out.println("Failed to add book request.");
                    }
                }
            } else {
                System.out.println("The book is not available for request.");
            }
        }
    }

    // Method to get all book requests
    public List<BookRequest> getAllBookRequests() throws SQLException {
        List<BookRequest> bookRequests = new ArrayList<>();
        String query = "SELECT * FROM book_requests";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                bookRequests.add(new BookRequest(resultSet.getInt("id"),
                                                 resultSet.getInt("student_id"),
                                                 resultSet.getInt("book_id")));
            }
        }
        return bookRequests;
    }

    // Method to get pending book requests (books requested by students)
    public List<BookRequest> getPendingBookRequests() throws SQLException {
        List<BookRequest> bookRequests = new ArrayList<>();
        String query = "SELECT br.id, br.student_id, br.book_id, b.title, s.username " +
                       "FROM book_requests br " +
                       "JOIN books b ON br.book_id = b.id " +
                       "JOIN students s ON br.student_id = s.id";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                bookRequests.add(new BookRequest(resultSet.getInt("id"),
                                                 resultSet.getInt("student_id"),
                                                 resultSet.getInt("book_id")));
                // You can also include book title and student username if needed
                System.out.println("Request ID: " + resultSet.getInt("id") + 
                                   ", Book: " + resultSet.getString("title") + 
                                   ", Requested by: " + resultSet.getString("username"));
            }
        }
        return bookRequests;
    }

    // Method to approve a book request and move it to the borrowed_books table
    public void approveBookRequest(int requestId) throws SQLException {
        // Step 1: Get the book and student ID from the request
        String query = "SELECT student_id, book_id FROM book_requests WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, requestId);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                int studentId = resultSet.getInt("student_id");
                int bookId = resultSet.getInt("book_id");

                // Step 2: Update the status of the book to 'approved'
                String updateBookStatusQuery = "UPDATE books SET status = 'approved' WHERE id = ?";
                try (PreparedStatement updateBookStatusStatement = connection.prepareStatement(updateBookStatusQuery)) {
                    updateBookStatusStatement.setInt(1, bookId);
                    updateBookStatusStatement.executeUpdate();
                }

                // Step 3: Insert the record into borrowed_books table
                String insertBorrowedBookQuery = "INSERT INTO borrowed_books (student_id, book_id, borrow_date) VALUES (?, ?, ?)";
                try (PreparedStatement insertStatement = connection.prepareStatement(insertBorrowedBookQuery)) {
                    insertStatement.setInt(1, studentId);
                    insertStatement.setInt(2, bookId);
                    insertStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));  // Today's date
                    insertStatement.executeUpdate();
                }

                // Step 4: Delete the book request from the book_requests table
                String deleteRequestQuery = "DELETE FROM book_requests WHERE id = ?";
                try (PreparedStatement deleteStatement = connection.prepareStatement(deleteRequestQuery)) {
                    deleteStatement.setInt(1, requestId);
                    deleteStatement.executeUpdate();
                }
            }
        }
    }
    // Method to delete a book request by its ID
    public void deleteBookRequest(int requestId) throws SQLException {
        String query = "DELETE FROM book_requests WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, requestId);
            statement.executeUpdate();
        }
    }
    // Method to get a book request by its ID
    public BookRequest getBookRequestById(int requestId) throws SQLException {
        String query = "SELECT * FROM book_requests WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, requestId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new BookRequest(resultSet.getInt("id"),
                                       resultSet.getInt("student_id"),
                                       resultSet.getInt("book_id"));
            }
        }
        return null;  // Return null if no request is found with the given ID
    }

}
