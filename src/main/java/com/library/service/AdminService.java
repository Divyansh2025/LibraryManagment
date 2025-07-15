package com.library.service;

import java.sql.SQLException;
import com.library.dao.AdminDAO;
import com.library.dao.BookDAO;
import com.library.model.Admin;
import com.library.model.Book;

public class AdminService implements UserService {
    private AdminDAO adminDAO = new AdminDAO();
    private BookDAO bookDAO = new BookDAO();

    // Admin login
    @Override
    public boolean login(String username, String password) throws SQLException {
        Admin admin = adminDAO.getAdminByUsername(username);
        return admin != null && admin.getPassword().equals(password);
    }

    // Override the default showAllBooks if customization is needed
    @Override
    public void showAllBooks() throws SQLException {
        System.out.println("Admin showing all books:");
        bookDAO.getAllBooks(); // Custom behavior for Admin
    }

    // Add a new book to the library
    public void addBook(Book book) throws SQLException {
        bookDAO.addBook(book);
    }

    // Approve a book and update its status to 'approved', then add it to the borrowed_books table
    public void approveBook(int bookId, int studentId) throws SQLException {
        Book book = bookDAO.getBookById(bookId);
        if (book != null) {
            bookDAO.approveBook(bookId, studentId);
            System.out.println("Book approved and added to borrowed_books table.");
        } else {
            System.out.println("The requested book does not exist.");
        }
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
