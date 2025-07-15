package com.library.service;

import java.sql.SQLException;
import com.library.dao.StudentDAO;   // Import StudentDAO for checking student credentials
import com.library.dao.BookDAO;
import com.library.model.Student;
import com.library.model.Book;

public class StudentService implements UserService {
    private BookDAO bookDAO = new BookDAO();
    private StudentDAO studentDAO = new StudentDAO();   // Add StudentDAO to check login

    // Student login - using the same logic as AdminService
    @Override
    public boolean login(String username, String password) throws SQLException {
        // Check if the student exists in the database
        Student student = studentDAO.getStudentByUsername(username);
        return student != null && student.getPassword().equals(password);   // Match the password
    }

    // Override the default showAllBooks if customization is needed
    @Override
    public void showAllBooks() throws SQLException {
        System.out.println("Student showing all books:");
        bookDAO.getAllBooks(); // Custom behavior for Student
    }

    // Request a book for approval (update its status to 'pending')
    public void requestBookApproval(int bookId) throws SQLException {
        Book book = bookDAO.getBookById(bookId);
        
        if (book != null && "available".equals(book.getStatus())) {
            book.setStatus("pending");
            bookDAO.updateBookStatus(book);
            System.out.println("Book requested for approval.");
        } else {
            System.out.println("This book cannot be requested for approval.");
        }
    }
}
