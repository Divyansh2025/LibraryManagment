package com.library.service;

import java.sql.SQLException;
import com.library.dao.AdminDAO;
import com.library.dao.BookDAO;
import com.library.model.Admin;
import com.library.model.Book;

public class AdminService {
    private AdminDAO adminDAO = new AdminDAO();
    private BookDAO bookDAO = new BookDAO();

    // Admin login
    public boolean login(String username, String password) throws SQLException {
        Admin admin = adminDAO.getAdminByUsername(username);
        return admin != null && admin.getPassword().equals(password);
    }

    // Add a new book to the library
    public void addBook(Book book) throws SQLException {
        bookDAO.addBook(book);
    }

    // Approve a book and update its status to 'approved', then add it to the borrowed_books table
    public void approveBook(int bookId, int studentId) throws SQLException {
        // Check if the book exists
        Book book = bookDAO.getBookById(bookId);
        if (book != null) {
            // Update book status to 'approved'
            bookDAO.approveBook(bookId, studentId);
            System.out.println("Book approved and added to borrowed_books table.");
        } else {
            System.out.println("The requested book does not exist.");
        }
    }

    // Get all books (admin can see all books)
    public void showAllBooks() throws SQLException {
        bookDAO.getAllBooks();
    }

    // Show pending books (books that are requested by students)
    public void showPendingBooks() throws SQLException {
        bookDAO.getPendingBooks();
    }

    // Show borrowed books (admin can see which student borrowed which book)
    public void showBorrowedBooks() throws SQLException {
        bookDAO.getBorrowedBooks();
    }
}
