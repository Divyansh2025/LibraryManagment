package com.library.dao;

import java.sql.*;
import com.library.model.Student;
import com.library.util.DBUtil;

public class StudentDAO {
    private Connection connection;

    public StudentDAO() {
        connection = DBUtil.getConnection();
    }

    public Student getStudentByUsername(String username) throws SQLException {
        String query = "SELECT * FROM students WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Student(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password"));
            }
        }
        return null;
    }
}
