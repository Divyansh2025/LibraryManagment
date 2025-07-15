package com.library.model;


import java.sql.Date;

public class BorrowedBooks {
    private int id;
    private int studentId;
    private int bookId;
    private Date borrowDate;
    private Date returnDate;

    // No-argument constructor (required by frameworks and for default initialization)
    public BorrowedBooks() {}

    // Constructor with parameters for initializing an object
    public BorrowedBooks(int id, int studentId, int bookId, Date borrowDate, Date returnDate) {
        this.id = id;
        this.studentId = studentId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
}

