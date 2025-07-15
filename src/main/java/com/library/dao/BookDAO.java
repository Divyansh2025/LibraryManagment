package com.library.dao;

import java.sql.*;
import com.library.model.Book;
import com.library.util.DBUtil;

public class BookDAO {
    private Connection connection;

    public BookDAO() {
        connection = DBUtil.getConnection();
    }

    // Add a book to the database
    public void addBook(Book book) throws SQLException {
        String query = "INSERT INTO books (title, author, status) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getStatus());
            statement.executeUpdate();
        }
    }

    // Update the status of an existing book (for student requesting approval)
    public void updateBookStatus(Book book) throws SQLException {
        String query = "UPDATE books SET status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, book.getStatus());  // 'pending', 'approved', etc.
            statement.setInt(2, book.getId());  // Book's ID
            statement.executeUpdate();
        }
    }

    // Approve a book (update its status to 'approved' and add to borrowed_books)
    public void approveBook(int bookId, int studentId) throws SQLException {
        // Step 1: Update the book's status to 'approved'
        String query = "UPDATE books SET status = 'approved' WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            statement.executeUpdate();
        }

        // Step 2: Add an entry into the borrowed_books table for the student
        String insertQuery = "INSERT INTO borrowed_books (student_id, book_id, borrow_date) VALUES (?, ?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setInt(1, studentId);  // Associate with a student
            insertStatement.setInt(2, bookId);  // Book ID
            insertStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));  // Today's date as borrow date
            insertStatement.executeUpdate();
        }
    }

    // Get book details by ID
    public Book getBookById(int id) throws SQLException {
        String query = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Book(resultSet.getInt("id"), resultSet.getString("title"),
                        resultSet.getString("author"), resultSet.getString("status"));
            }
        }
        return null;
    }

    // Get all books (admin can see)
    public void getAllBooks() throws SQLException {
        String query = "SELECT * FROM books";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println("Book ID: " + resultSet.getInt("id") + ", Title: " + resultSet.getString("title") + ", Status: " + resultSet.getString("status"));
            }
        }
    }

    // Get all books pending approval (for admin)
    public void getPendingBooks() throws SQLException {
        String query = "SELECT * FROM books WHERE status = 'pending'";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println("Book ID: " + resultSet.getInt("id") + ", Title: " + resultSet.getString("title"));
            }
        }
    }

    // Get borrowed books (for admin to see who borrowed what)
    public void getBorrowedBooks() throws SQLException {
        String query = "SELECT b.title, s.username, bb.borrow_date FROM borrowed_books bb " +
                       "JOIN books b ON bb.book_id = b.id " +
                       "JOIN students s ON bb.student_id = s.id";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println("Book: " + resultSet.getString("title") + ", Borrowed by: " + resultSet.getString("username") + ", Date: " + resultSet.getDate("borrow_date"));
            }
        }
    }

    // Update book status to "borrowed" when a student borrows it
    public void borrowBook(int bookId) throws SQLException {
        String query = "UPDATE books SET status = 'borrowed' WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            statement.executeUpdate();
        }
    }
}
