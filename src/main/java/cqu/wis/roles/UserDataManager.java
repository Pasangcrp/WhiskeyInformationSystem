package cqu.wis.roles;

import cqu.wis.data.UserData;
import java.sql.SQLException;

/**
 * Manages user data operations in the Whiskey Information System.
 */
public class UserDataManager {
    private UserData userData;

    public UserDataManager(UserData userData) {
        this.userData = userData;
    }

    /**
     * Finds a user by username.
     * @param username The username to search for
     * @return UserDetails if the user exists, null otherwise
     * @throws SQLException if a database error occurs
     */
    public UserData.UserDetails findUser(String username) throws SQLException {
        return userData.findUser(username);
    }

    /**
     * Updates the password for a user.
     * @param username The username of the user
     * @param newPassword The new password (encrypted)
     * @throws SQLException if a database error occurs
     */
    public void updatePassword(String username, String newPassword) throws SQLException {
        userData.updatePassword(username, newPassword);
    }
}