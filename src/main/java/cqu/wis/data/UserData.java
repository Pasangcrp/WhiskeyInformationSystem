package cqu.wis.data;

import java.sql.*;

/**
 * Manages user data access in the Whiskey Information System.
 */
public class UserData {
    private Connection connection;
    private PreparedStatement findUserStmt;
    private PreparedStatement updatePasswordStmt;
    private final String url;
    private final String username;
    private final String password;

    public UserData(String url, String username, String password) throws SQLException {
        this.url = url;
        this.username = username;
        this.password = password;
        this.connection = DriverManager.getConnection(url, username, password);
        this.connection.setAutoCommit(true); // Ensure auto-commit is enabled
        // Initialize prepared statements
        this.findUserStmt = connection.prepareStatement(
            "SELECT * FROM PASSWORDS WHERE USERNAME = ?"
        );
        this.updatePasswordStmt = connection.prepareStatement(
            "UPDATE PASSWORDS SET PASSWORD = ? WHERE USERNAME = ?"
        );
        System.out.println("UserData: Connected to database at " + url);
    }

    /**
     * Connects to the database. Called by App.start() after instantiation.
     * @throws SQLException if the connection cannot be established
     */
    public void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            this.connection = DriverManager.getConnection(url, username, password);
            this.connection.setAutoCommit(true); // Ensure auto-commit is enabled
            this.findUserStmt = connection.prepareStatement(
                "SELECT * FROM PASSWORDS WHERE USERNAME = ?"
            );
            this.updatePasswordStmt = connection.prepareStatement(
                "UPDATE PASSWORDS SET PASSWORD = ? WHERE USERNAME = ?"
            );
            System.out.println("UserData: Reconnected to database at " + url);
        }
    }

    /**
     * A record representing user details.
     * @param username The user's username
     * @param password The user's password
     */
    public record UserDetails(String username, String password) {
    }

    /**
     * Finds a user by username in the PASSWORDS table.
     * @param username The username to search for
     * @return UserDetails if the user exists, null otherwise
     * @throws SQLException if a database error occurs
     */
    public UserDetails findUser(String username) throws SQLException {
        System.out.println("UserData: Searching for user: " + username);
        findUserStmt.setString(1, username);
        try (ResultSet rs = findUserStmt.executeQuery()) {
            if (rs.next()) {
                String foundUsername = rs.getString("USERNAME");
                String foundPassword = rs.getString("PASSWORD");
                System.out.println("UserData: Found user: " + foundUsername + " with password: " + foundPassword);
                return new UserDetails(foundUsername, foundPassword);
            }
            System.out.println("UserData: User not found: " + username);
            return null;
        }
    }

    /**
     * Updates the password for a user in the PASSWORDS table.
     * @param username The username of the user
     * @param newPassword The new password (encrypted)
     * @throws SQLException if a database error occurs
     */
    public void updatePassword(String username, String newPassword) throws SQLException {
        System.out.println("UserData: Updating password for user: " + username);
        updatePasswordStmt.setString(1, newPassword);
        updatePasswordStmt.setString(2, username);
        int rowsAffected = updatePasswordStmt.executeUpdate();
        if (rowsAffected == 1) {
            System.out.println("UserData: Password updated successfully for user: " + username);
        } else {
            System.out.println("UserData: Failed to update password for user: " + username + ", user not found");
            throw new SQLException("User not found: " + username);
        }
    }
}