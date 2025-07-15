package com.library.service;

import java.sql.SQLException;
import java.util.*;
import com.library.dao.BookDAO;
import com.library.dao.BookRequestDAO;
import com.library.dao.BorrowedBooksDAO;
import com.library.dao.StudentDAO;
import com.library.model.Book;
import com.library.model.BookRequest;
import com.library.model.BorrowedBooks;
import com.library.model.Student;

public class StudentService implements UserService {
    private BookDAO bookDAO = new BookDAO();
    private BookRequestDAO bookRequestDAO = new BookRequestDAO();
    private BorrowedBooksDAO borrowedBooksDAO = new BorrowedBooksDAO();  // Add BorrowedBooksDAO
    private StudentDAO studentDAO = new StudentDAO();  // Add StudentDAO

    private String loggedInUsername;

    // Student login
    @Override
    public boolean login(String username, String password) throws SQLException {
        Student student = studentDAO.getStudentByUsername(username);
        if (student != null && student.getPassword().equals(password)) {
            this.loggedInUsername = username;
            return true;
        }
        return false;
    }

    // Show all books for the student
    @Override
    public void showAllBooks() throws SQLException {
        System.out.println("Student showing all books:");
        bookDAO.getAllBooks();
    }

    // Request a book for approval
    public void requestBookApproval(int bookId) throws SQLException {
        Student student = studentDAO.getStudentByUsername(this.loggedInUsername);
        if (student != null) {
            Book book = bookDAO.getBookById(bookId);
            if (book != null && "available".equals(book.getStatus())) {
                BookRequest bookRequest = new BookRequest(0, student.getId(), book.getId());
                bookRequestDAO.addBookRequest(bookRequest);
                book.setStatus("pending");
                bookDAO.updateBookStatus(book);
                System.out.println("Book requested for approval.");
            } else {
                System.out.println("This book cannot be requested for approval.");
            }
        } else {
            System.out.println("Student not found.");
        }
    }

    // Return a borrowed book
    public void returnBook() throws SQLException {
        Student student = studentDAO.getStudentByUsername(this.loggedInUsername);
        if (student != null) {
            List<BorrowedBooks> borrowedBooks = borrowedBooksDAO.getBorrowedBooksByStudent(student.getId());
            if (borrowedBooks.isEmpty()) {
                System.out.println("You have no borrowed books. Please borrow a book first.");
                return;
            }

            // Display borrowed books
            System.out.println("Borrowed books:");
            for (BorrowedBooks borrowedBook : borrowedBooks) {
                System.out.println("Book ID: " + borrowedBook.getBookId() + " | Borrowed Date: " + borrowedBook.getBorrowDate());
            }

            // Ask for book ID to return
            System.out.print("Enter book ID to return: ");
            @SuppressWarnings("resource")
            int bookIdToReturn = new Scanner(System.in).nextInt();

            // Check if the book is borrowed by the student
            boolean bookFound = false;
            for (BorrowedBooks borrowedBook : borrowedBooks) {
                if (borrowedBook.getBookId() == bookIdToReturn) {
                    borrowedBooksDAO.deleteBorrowedBook(bookIdToReturn, student.getId());
                    Book book = bookDAO.getBookById(bookIdToReturn);
                    if (book != null) {
                        book.setStatus("available");
                        bookDAO.updateBookStatus(book);  // Update the status of the book to 'available'
                        System.out.println("Book returned successfully.");
                    }
                    bookFound = true;
                    break;
                }
            }

            if (!bookFound) {
                System.out.println("You did not borrow this book.");
            }
            
        } else {
            System.out.println("Student not found.");
        }
    }
}
