package com.library.dao;

import com.library.model.BorrowedBooks;
import com.library.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowedBooksDAO {
    private Connection connection;

    public BorrowedBooksDAO() {
        connection = DBUtil.getConnection();
    }

    // Insert a borrowed book record
    public void insertBorrowedBook(BorrowedBooks borrowedBook) throws SQLException {
        String query = "INSERT INTO borrowed_books (student_id, book_id, borrow_date) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, borrowedBook.getStudentId());
            statement.setInt(2, borrowedBook.getBookId());
            statement.setDate(3, borrowedBook.getBorrowDate());
            statement.executeUpdate();
        }
    }

    // Get borrowed books for a student
    public List<BorrowedBooks> getBorrowedBooksByStudent(int studentId) throws SQLException {
        List<BorrowedBooks> borrowedBooks = new ArrayList<>();
        String query = "SELECT * FROM borrowed_books WHERE student_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
               
                borrowedBooks.add(new BorrowedBooks(resultSet.getInt("id"),
                                                    resultSet.getInt("student_id"),
                                                    resultSet.getInt("book_id"),
                                                    resultSet.getDate("borrow_date"),
                                                    resultSet.getDate("return_date")));  // Ensure return_date is properly retrieved
            }
        }
        return borrowedBooks;
    }


    // Delete a borrowed book record
    public void deleteBorrowedBook(int bookId, int studentId) throws SQLException {
        String query = "DELETE FROM borrowed_books WHERE book_id = ? AND student_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            statement.setInt(2, studentId);
            statement.executeUpdate();
        }
    }
}
