package cqu.wis.roles;

import cqu.wis.data.UserData;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.regex.Pattern;
import java.nio.charset.StandardCharsets;

public class UserDataValidator {
    private static final String DEFAULT_PASSWORD = "password";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?!.*\\s).{8,}$"
    );

    public String generateSHA1(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not found", e);
        }
    }

    public ValidationResponse checkForFieldsPresent(String username, String password) {
        if (username == null || username.trim().isEmpty()) return new ValidationResponse(false, "Username cannot be empty");
        if (password == null || password.trim().isEmpty()) return new ValidationResponse(false, "Password cannot be empty");
        return new ValidationResponse(true, "Fields are present");
    }

    public ValidationResponse checkForFieldsPresent(String username, String oldPassword, String newPassword) {
        if (username == null || username.trim().isEmpty()) return new ValidationResponse(false, "Username cannot be empty");
        if (oldPassword == null || oldPassword.trim().isEmpty()) return new ValidationResponse(false, "Old password cannot be empty");
        if (newPassword == null || newPassword.trim().isEmpty()) return new ValidationResponse(false, "New password cannot be empty");
        return new ValidationResponse(true, "Fields are present");
    }

    public ValidationResponse checkCurrentDetails(String username, String password, UserDataManager manager) throws SQLException {
        ValidationResponse check = checkForFieldsPresent(username, password);
        if (!check.isValid()) return check;

        UserData.UserDetails user = manager.findUser(username);
        if (user == null) return new ValidationResponse(false, "Username not found");

        if (user.password().equals(DEFAULT_PASSWORD)) {
            if (password.equals(DEFAULT_PASSWORD)) {
                return new ValidationResponse(true, "Please change your default password");
            } else {
                return new ValidationResponse(false, "Invalid password");
            }
        }

        String encryptedInput = generateSHA1(password);
        if (user.password().equals(encryptedInput)) return new ValidationResponse(true, "Login successful");
        return new ValidationResponse(false, "Invalid password");
    }

    public ValidationResponse checkNewDetails(String username, String oldPassword, String newPassword, UserDataManager manager) throws SQLException {
        ValidationResponse fieldCheck = checkForFieldsPresent(username, oldPassword, newPassword);
        if (!fieldCheck.isValid()) return fieldCheck;

        ValidationResponse currentCheck = checkCurrentDetails(username, oldPassword, manager);
        if (!currentCheck.isValid()) return currentCheck;

        if (newPassword.equals(DEFAULT_PASSWORD)) return new ValidationResponse(false, "New password cannot be the default password");
        if (!PASSWORD_PATTERN.matcher(newPassword).matches()) {
            return new ValidationResponse(false, "New password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one digit, one special character, and no spaces");
        }

        return new ValidationResponse(true, "New password is valid");
    }
}