package com.library.dao;

import java.sql.*;
import com.library.model.Admin;
import com.library.util.DBUtil;

public class AdminDAO {
    private Connection connection;

    public AdminDAO() {
        connection = DBUtil.getConnection();
    }

    public Admin getAdminByUsername(String username) throws SQLException {
        String query = "SELECT * FROM admins WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Admin(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password"));
            }
        }
        return null;
    }
}
