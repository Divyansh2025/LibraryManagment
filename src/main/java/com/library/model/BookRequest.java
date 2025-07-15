package com.library.model;

public class BookRequest {
    private int id;
    private int studentId;
    private int bookId;

    // Constructor
    public BookRequest(int id, int studentId, int bookId) {
        this.id = id;
        this.studentId = studentId;
        this.bookId = bookId;
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
}
