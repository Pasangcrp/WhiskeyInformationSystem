package cqu.wis.roles;

import cqu.wis.data.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserDataValidator class.
 */
public class UserDataValidatorTest {
    private UserDataValidator validator;
    private UserDataManager userDataManager;

    @BeforeEach
    void setUp() {
        validator = new UserDataValidator();
        userDataManager = new UserDataManager(null) {
            @Override
            public UserData.UserDetails findUser(String username) throws SQLException {
                // Mock behavior for testing
                if ("admin".equals(username)) {
                    return new UserData.UserDetails("admin", "password");
                }
                if ("jhj".equals(username)) {
                    return new UserData.UserDetails("jhj", "password");
                }
                if ("invalid".equals(username)) {
                    return new UserData.UserDetails("invalid", "notdefault");
                }
                if ("encrypted".equals(username)) {
                    // SHA-1 hash of "Test123!" on this system
                    return new UserData.UserDetails("encrypted", "0c6ba03885f3aae765fbf20f07f514a44dbda30a");
                }
                return null;
            }
        };
    }

    // Tests for generateSHA1
    @Test
    void generateSHA1WithKnownInputTest() {
        String result = validator.generateSHA1("Test123!");
        assertEquals("0c6ba03885f3aae765fbf20f07f514a44dbda30a", result);
    }

    @Test
    void generateSHA1WithEmptyInputTest() {
        String result = validator.generateSHA1("");
        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", result);
    }

    // Tests for checkForFieldsPresent (two parameters)
    @Test
    void checkForFieldsPresentWithNullUsernameTest() {
        ValidationResponse response = validator.checkForFieldsPresent(null, "password");
        assertFalse(response.isValid());
        assertEquals("Username cannot be empty", response.message());
    }

    @Test
    void checkForFieldsPresentWithEmptyUsernameTest() {
        ValidationResponse response = validator.checkForFieldsPresent("", "password");
        assertFalse(response.isValid());
        assertEquals("Username cannot be empty", response.message());
    }

    @Test
    void checkForFieldsPresentWithWhitespaceUsernameTest() {
        ValidationResponse response = validator.checkForFieldsPresent("  ", "password");
        assertFalse(response.isValid());
        assertEquals("Username cannot be empty", response.message());
    }

    @Test
    void checkForFieldsPresentWithNullPasswordTest() {
        ValidationResponse response = validator.checkForFieldsPresent("admin", null);
        assertFalse(response.isValid());
        assertEquals("Password cannot be empty", response.message());
    }

    @Test
    void checkForFieldsPresentWithEmptyPasswordTest() {
        ValidationResponse response = validator.checkForFieldsPresent("admin", "");
        assertFalse(response.isValid());
        assertEquals("Password cannot be empty", response.message());
    }

    @Test
    void checkForFieldsPresentWithWhitespacePasswordTest() {
        ValidationResponse response = validator.checkForFieldsPresent("admin", "  ");
        assertFalse(response.isValid());
        assertEquals("Password cannot be empty", response.message());
    }

    @Test
    void checkForFieldsPresentWithValidFieldsTest() {
        ValidationResponse response = validator.checkForFieldsPresent("admin", "password");
        assertTrue(response.isValid());
        assertEquals("Fields are present", response.message());
    }

    // Tests for checkForFieldsPresent (three parameters)
    @Test
    void checkForFieldsPresentThreeParamsWithNullUsernameTest() {
        ValidationResponse response = validator.checkForFieldsPresent(null, "oldpass", "newpass");
        assertFalse(response.isValid());
        assertEquals("Username cannot be empty", response.message());
    }

    @Test
    void checkForFieldsPresentThreeParamsWithEmptyUsernameTest() {
        ValidationResponse response = validator.checkForFieldsPresent("", "oldpass", "newpass");
        assertFalse(response.isValid());
        assertEquals("Username cannot be empty", response.message());
    }

    @Test
    void checkForFieldsPresentThreeParamsWithWhitespaceUsernameTest() {
        ValidationResponse response = validator.checkForFieldsPresent("  ", "oldpass", "newpass");
        assertFalse(response.isValid());
        assertEquals("Username cannot be empty", response.message());
    }

    @Test
    void checkForFieldsPresentThreeParamsWithNullOldPasswordTest() {
        ValidationResponse response = validator.checkForFieldsPresent("admin", null, "newpass");
        assertFalse(response.isValid());
        assertEquals("Old password cannot be empty", response.message());
    }

    @Test
    void checkForFieldsPresentThreeParamsWithEmptyOldPasswordTest() {
        ValidationResponse response = validator.checkForFieldsPresent("admin", "", "newpass");
        assertFalse(response.isValid());
        assertEquals("Old password cannot be empty", response.message());
    }

    @Test
    void checkForFieldsPresentThreeParamsWithWhitespaceOldPasswordTest() {
        ValidationResponse response = validator.checkForFieldsPresent("admin", "  ", "newpass");
        assertFalse(response.isValid());
        assertEquals("Old password cannot be empty", response.message());
    }

    @Test
    void checkForFieldsPresentThreeParamsWithNullNewPasswordTest() {
        ValidationResponse response = validator.checkForFieldsPresent("admin", "oldpass", null);
        assertFalse(response.isValid());
        assertEquals("New password cannot be empty", response.message());
    }

    @Test
    void checkForFieldsPresentThreeParamsWithEmptyNewPasswordTest() {
        ValidationResponse response = validator.checkForFieldsPresent("admin", "oldpass", "");
        assertFalse(response.isValid());
        assertEquals("New password cannot be empty", response.message());
    }

    @Test
    void checkForFieldsPresentThreeParamsWithWhitespaceNewPasswordTest() {
        ValidationResponse response = validator.checkForFieldsPresent("admin", "oldpass", "  ");
        assertFalse(response.isValid());
        assertEquals("New password cannot be empty", response.message());
    }

    @Test
    void checkForFieldsPresentThreeParamsWithValidFieldsTest() {
        ValidationResponse response = validator.checkForFieldsPresent("admin", "oldpass", "newpass");
        assertTrue(response.isValid());
        assertEquals("Fields are present", response.message());
    }

    // Tests for checkCurrentDetails
    @Test
    void checkCurrentDetailsWithNonExistentUserTest() throws SQLException {
        ValidationResponse response = validator.checkCurrentDetails("unknown", "password", userDataManager);
        assertFalse(response.isValid());
        assertEquals("Username not found", response.message());
    }

    @Test
    void checkCurrentDetailsWithDefaultPasswordTest() throws SQLException {
        ValidationResponse response = validator.checkCurrentDetails("admin", "password", userDataManager);
        assertTrue(response.isValid());
        assertEquals("Please change your default password", response.message());
    }

    @Test
    void checkCurrentDetailsWithDefaultPasswordButWrongInputTest() throws SQLException {
        ValidationResponse response = validator.checkCurrentDetails("admin", "wrongpassword", userDataManager);
        assertFalse(response.isValid());
        assertEquals("Invalid password", response.message());
    }

    @Test
    void checkCurrentDetailsWithEncryptedPasswordTest() throws SQLException {
        ValidationResponse response = validator.checkCurrentDetails("encrypted", "Test123!", userDataManager);
        assertTrue(response.isValid());
        assertEquals("Login successful", response.message());
    }

    @Test
    void checkCurrentDetailsWithEncryptedPasswordButWrongInputTest() throws SQLException {
        ValidationResponse response = validator.checkCurrentDetails("encrypted", "WrongPass!", userDataManager);
        assertFalse(response.isValid());
        assertEquals("Invalid password", response.message());
    }

    @Test
    void checkCurrentDetailsWithNonDefaultPasswordTest() throws SQLException {
        ValidationResponse response = validator.checkCurrentDetails("invalid", "notdefault", userDataManager);
        assertFalse(response.isValid());
        assertEquals("Invalid password", response.message());
    }

    // Tests for checkNewDetails
    @Test
    void checkNewDetailsWithNonExistentUserTest() throws SQLException {
        ValidationResponse response = validator.checkNewDetails("unknown", "password", "NewPass123!", userDataManager);
        assertFalse(response.isValid());
        assertEquals("Username not found", response.message());
    }

    @Test
    void checkNewDetailsWithInvalidOldPasswordTest() throws SQLException {
        ValidationResponse response = validator.checkNewDetails("admin", "wrongpassword", "NewPass123!", userDataManager);
        assertFalse(response.isValid());
        assertEquals("Invalid password", response.message());
    }

    @Test
    void checkNewDetailsWithNewPasswordAsDefaultTest() throws SQLException {
        ValidationResponse response = validator.checkNewDetails("admin", "password", "password", userDataManager);
        assertFalse(response.isValid());
        assertEquals("New password cannot be the default password", response.message());
    }

    @Test
    void checkNewDetailsWithNewPasswordTooShortTest() throws SQLException {
        ValidationResponse response = validator.checkNewDetails("admin", "password", "Pass1!", userDataManager);
        assertFalse(response.isValid());
        assertTrue(response.message().startsWith("New password must be at least"));
    }

    @Test
    void checkNewDetailsWithNewPasswordNoUppercaseTest() throws SQLException {
        ValidationResponse response = validator.checkNewDetails("admin", "password", "pass123!word", userDataManager);
        assertFalse(response.isValid());
        assertTrue(response.message().startsWith("New password must be at least"));
    }

    @Test
    void checkNewDetailsWithNewPasswordNoLowercaseTest() throws SQLException {
        ValidationResponse response = validator.checkNewDetails("admin", "password", "PASS123!WORD", userDataManager);
        assertFalse(response.isValid());
        assertTrue(response.message().startsWith("New password must be at least"));
    }

    @Test
    void checkNewDetailsWithNewPasswordNoDigitTest() throws SQLException {
        ValidationResponse response = validator.checkNewDetails("admin", "password", "PassWord!", userDataManager);
        assertFalse(response.isValid());
        assertTrue(response.message().startsWith("New password must be at least"));
    }

    @Test
    void checkNewDetailsWithNewPasswordNoSpecialCharTest() throws SQLException {
        ValidationResponse response = validator.checkNewDetails("admin", "password", "PassWord123", userDataManager);
        assertFalse(response.isValid());
        assertTrue(response.message().startsWith("New password must be at least"));
    }

    @Test
    void checkNewDetailsWithNewPasswordContainingSpacesTest() throws SQLException {
        ValidationResponse response = validator.checkNewDetails("admin", "password", "Pass 123! Word", userDataManager);
        assertFalse(response.isValid());
        assertTrue(response.message().startsWith("New password must be at least"));
    }

    @Test
    void checkNewDetailsWithValidNewPasswordTest() throws SQLException {
        ValidationResponse response = validator.checkNewDetails("admin", "password", "NewPass123!", userDataManager);
        assertTrue(response.isValid());
        assertEquals("New password is valid", response.message());
    }

    @Test
    void checkNewDetailsWithValidEncryptedOldPasswordTest() throws SQLException {
        ValidationResponse response = validator.checkNewDetails("encrypted", "Test123!", "NewPass123!", userDataManager);
        assertTrue(response.isValid());
        assertEquals("New password is valid", response.message());
    }

    @Test
    void checkNewDetailsWithInvalidEncryptedOldPasswordTest() throws SQLException {
        ValidationResponse response = validator.checkNewDetails("encrypted", "WrongPass!", "NewPass123!", userDataManager);
        assertFalse(response.isValid());
        assertEquals("Invalid password", response.message());
    }
}