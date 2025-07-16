package com.library.service;

import java.sql.SQLException;
import com.library.dao.AdminDAO;
import com.library.dao.BookDAO;
import com.library.dao.BookRequestDAO;
import com.library.model.Admin;
import com.library.model.Book;
import com.library.model.BookRequest;

public class AdminService implements UserService {
    private AdminDAO adminDAO = new AdminDAO();
    private BookDAO bookDAO = new BookDAO();
    private BookRequestDAO bookRequestDAO = new BookRequestDAO(); 

    // Admin login
    @Override
    public boolean login(String username, String password) throws SQLException {
        Admin admin = adminDAO.getAdminByUsername(username);
        return admin != null && admin.getPassword().equals(password);
    }

   
    @Override
    public void showAllBooks() throws SQLException {
        System.out.println("Admin showing all books:");
        bookDAO.getAllBooks(); // Custom behavior for Admin
    }

    // Add a new book to the library
    public void addBook(Book book) throws SQLException {
        bookDAO.addBook(book);
    }

    public void approveBook(int requestId) throws SQLException {
        // Step 1: Get the book request by ID
        BookRequest bookRequest = bookRequestDAO.getBookRequestById(requestId);
        if (bookRequest != null) {
            int bookId = bookRequest.getBookId();
            int studentId = bookRequest.getStudentId();

            // Step 2: Approve the book request by updating the book's status
            Book book = bookDAO.getBookById(bookId);
            if (book != null) {
                bookDAO.approveBook(bookId, studentId); // Update book status to approved

                // Step 3: Move the book to the borrowed_books table
                bookDAO.borrowBook(bookId); // Update the book status in the books table to 'borrowed'

                // Step 4: Delete the book request from the book_requests table
                bookRequestDAO.deleteBookRequest(requestId);
                System.out.println("Book approved and added to borrowed_books table.");
            } else {
                System.out.println("The requested book does not exist.");
            }
        } else {
            System.out.println("Invalid request ID.");
        }
    }

    // Show all pending book requests (books requested by students)
    public boolean showPendingBookRequests() throws SQLException {
    var pendingRequests = bookRequestDAO.getPendingBookRequests();
    if (pendingRequests.isEmpty()) {
        System.out.println("No requests to approve.");
        return false; // No pending requests
    } else {
        for (BookRequest request : pendingRequests) {
            System.out.println("Request ID: " + request.getId() +
                    ", Book ID: " + request.getBookId() +
                    ", Student ID: " + request.getStudentId());
        }
        return true; // There are pending requests
    }
}

    // Show borrowed books (admin can see which student borrowed which book)
    public void showBorrowedBooks() throws SQLException {
        bookDAO.getBorrowedBooks();
    }
}
