package pims.config.dao;

import pims.config.DBConnection;

import java.sql.*;
import java.util.*;


public class UserDAO {
    public UserLoginResult login(String username, String password) throws Exception {

        String sql = """
                    SELECT
                    user_id,
                    username,
                    role,
                    full_name
                FROM users
                WHERE username = ?
                  AND password = ?
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserLoginResult(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("role"),
                            rs.getString("full_name")
                    );
                }
            }
        }

        return null;
    }

    //Validation methods for preventing duplicate usernames when adding or updating users
    public boolean usernameExists(String username) throws Exception {
        String sql = """
                SELECT user_id
                FROM users
                WHERE username = ?
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean usernameExistsForAnotherUser(String username, int userId) throws Exception {
        String sql = """
                SELECT user_id
                FROM users
                WHERE username = ?
                  AND user_id <> ?
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, username);
            ps.setInt(2, userId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void addUser(String username, String password, String role, String fullName) throws Exception {
        String sql = """
                INSERT INTO users
                (
                    username,
                    password,
                    role,
                    full_name
                )
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ps.setString(4, fullName);

            ps.executeUpdate();
        }
    }

    public List<UserListItem> getAllUsers() throws Exception {
        String sql = """
                SELECT
                    user_id,
                    username,
                    role,
                    full_name
                FROM users
                ORDER BY user_id
                """;

        List<UserListItem> users = new ArrayList<>();

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);

                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                users.add(new UserListItem(
                                rs.getInt("user_id"),
                                rs.getString("username"),
                                rs.getString("role"),
                                rs.getString("full_name")
                        )
                );
            }
        }
        return users;
    }

    public void updateUser(int userId, String username, String password, String role, String fullName) throws Exception {
        String sql = """
                UPDATE users
                SET username = ?,
                    password = ?,
                    role = ?,
                    full_name = ?
                WHERE user_id = ?
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ps.setString(4, fullName);
            ps.setInt(5, userId);

            ps.executeUpdate();
        }
    }
    public void deleteUser(int userId) throws Exception {
        String sql = """
                DELETE FROM users
                WHERE user_id = ?
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    public record UserLoginResult(int userId, String username, String role, String fullName) { }

    public record UserListItem(int userId, String username, String role, String fullName) { }
}