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
        String query = "INSERT INTO book_requests (student_id, book_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookRequest.getStudentId());
            statement.setInt(2, bookRequest.getBookId());
            statement.executeUpdate();
        }
    }

    // Method to get a book request by its ID
    public BookRequest getBookRequestById(int id) throws SQLException {
        String query = "SELECT * FROM book_requests WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new BookRequest(resultSet.getInt("id"),
                                       resultSet.getInt("student_id"),
                                       resultSet.getInt("book_id"));
            }
        }
        return null;
    }

    // Method to get all book requests for a specific student
    public List<BookRequest> getBookRequestsByStudentId(int studentId) throws SQLException {
        List<BookRequest> bookRequests = new ArrayList<>();
        String query = "SELECT * FROM book_requests WHERE student_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                bookRequests.add(new BookRequest(resultSet.getInt("id"),
                                                 resultSet.getInt("student_id"),
                                                 resultSet.getInt("book_id")));
            }
        }
        return bookRequests;
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
}
