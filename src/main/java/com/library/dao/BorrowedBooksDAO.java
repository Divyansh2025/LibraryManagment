package com.library.dao;

import java.sql.*;
import com.library.model.BorrowedBooks;
import com.library.util.DBUtil;

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
}
