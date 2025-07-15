package com.library.ui;

import java.sql.*;
import java.util.Scanner;
import com.library.service.AdminService;
import com.library.service.StudentService;
import com.library.model.Book;

public class LibrarySystem {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Ask the user for their role
            System.out.println("Are you an Admin or a Student? (Enter 'admin' or 'student', or 'exit' to quit)");
            String userType = scanner.nextLine().trim().toLowerCase();

            // Exit the program if the user enters "exit"
            if ("exit".equals(userType)) {
                System.out.println("Exiting the system...");
                break;
            }

            if ("admin".equals(userType)) {
                // Admin login
                AdminService adminService = new AdminService();
                System.out.print("Admin Username: ");
                String adminUsername = scanner.next();
                System.out.print("Admin Password: ");
                String adminPassword = scanner.next();

                if (adminService.login(adminUsername, adminPassword)) {
                    System.out.println("Admin Logged In.");
                    boolean exit = false;
                    while (!exit) {
                        // Admin options
                        System.out.println("What would you like to do?");
                        System.out.println("1. Add Book");
                        System.out.println("2. Approve Book");
                        System.out.println("3. Show Pending Books");
                        System.out.println("4. Show Borrowed Books");
                        System.out.println("5. Logout");
                        int choice = scanner.nextInt();

                        switch (choice) {
                            case 1:
                                // Add a new book
                                scanner.nextLine();  // Clear buffer
                                System.out.print("Enter book title: ");
                                String title = scanner.nextLine();
                                System.out.print("Enter book author: ");
                                String author = scanner.nextLine();
                                Book book = new Book(0, title, author, "available");
                                adminService.addBook(book);
                                System.out.println("Book added successfully.");
                                break;
                            case 2:
                                // Approve a book
                                System.out.print("Enter book ID to approve: ");
                                int bookIdToApprove = scanner.nextInt();
                                System.out.print("Enter student ID who requested the book: ");
                                int studentId = scanner.nextInt();
                                adminService.approveBook(bookIdToApprove, studentId);
                                System.out.println("Book approved and added to borrowed_books table.");
                                break;
                            case 3:
                                // Show pending books
                                adminService.showPendingBooks();
                                break;
                            case 4:
                                // Show borrowed books
                                adminService.showBorrowedBooks();
                                break;
                            case 5:
                                exit = true;
                                System.out.println("Logging out...");
                                break;
                            default:
                                System.out.println("Invalid choice.");
                                break;
                        }
                    }
                } else {
                    System.out.println("Invalid credentials.");
                }
            } else if ("student".equals(userType)) {
                // Student login
                StudentService studentService = new StudentService();
                System.out.print("Student Username: ");
                String studentUsername = scanner.next();
                System.out.print("Student Password: ");
                String studentPassword = scanner.next();

                if (studentService.login(studentUsername, studentPassword)) {
                    System.out.println("Student Logged In.");
                    boolean exit = false;
                    while (!exit) {
                        // Student options
                        System.out.println("What would you like to do?");
                        System.out.println("1. Request Book for Approval");
                        System.out.println("2. Show All Books");
                        System.out.println("3. Logout");
                        int choice = scanner.nextInt();

                        switch (choice) {
                            case 1:
                                // Request a book for approval
                                System.out.print("Enter book ID to request for approval: ");
                                int bookIdToRequest = scanner.nextInt();
                                studentService.requestBookApproval(bookIdToRequest);
                                break;
                            case 2:
                                // Show all books
                                studentService.showAllBooks();
                                break;
                            case 3:
                                exit = true;
                                System.out.println("Logging out...");
                                break;
                            default:
                                System.out.println("Invalid choice.");
                                break;
                        }
                    }
                } else {
                    System.out.println("Invalid credentials.");
                }
            } else {
                System.out.println("Invalid user type. Please enter 'admin' or 'student'.");
            }
        }

        scanner.close();
    }
}
