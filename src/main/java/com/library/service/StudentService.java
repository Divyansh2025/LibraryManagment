package com.library.service;

import java.sql.SQLException;
import com.library.dao.BookDAO;
import com.library.model.Book;

public class StudentService {
    private BookDAO bookDAO = new BookDAO();

    // Student login
    public boolean login(String username, String password) throws SQLException {
        // Logic for student login (for simplicity, assuming it's always valid)
        return true;
    }

    // Request a book for approval (update its status to 'pending')
    public void requestBookApproval(int bookId) throws SQLException {
        // Get the book details using bookId
        Book book = bookDAO.getBookById(bookId);
        
        // Check if the book is available for requesting approval
        if (book != null && "available".equals(book.getStatus())) {
            // Update the status of the existing book to 'pending'
            book.setStatus("pending");
            bookDAO.updateBookStatus(book);
            System.out.println("Book requested for approval.");
        } else {
            System.out.println("This book cannot be requested for approval.");
        }
    }

    // Show all books for students (and their availability)
    public void showAllBooks() throws SQLException {
        bookDAO.getAllBooks();
    }
}
