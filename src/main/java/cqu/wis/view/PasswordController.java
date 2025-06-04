package cqu.wis.view;

import cqu.wis.roles.SceneCoordinator;
import cqu.wis.roles.UserDataManager;
import cqu.wis.roles.UserDataValidator;
import cqu.wis.roles.ValidationResponse;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Controller for the change password scene GUI in the Whiskey Information System.
 */
@SuppressWarnings("unused")
public class PasswordController {
    @FXML private TextField usernameField;
    @FXML private PasswordField oldPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private TextArea messageArea;
    @FXML private Button submitButton;
    @FXML private Button clearButton;
    @FXML private Button exitButton;

    private SceneCoordinator sceneCoordinator;
    private UserDataManager userDataManager;
    private UserDataValidator userDataValidator;

    /**
     * Injects dependencies into the controller.
     * @param sceneCoordinator The SceneCoordinator for scene management
     * @param userDataManager The UserDataManager for user data operations
     * @param userDataValidator The UserDataValidator for input validation
     */
    public void inject(SceneCoordinator sceneCoordinator, UserDataManager userDataManager, UserDataValidator userDataValidator) {
        this.sceneCoordinator = sceneCoordinator;
        this.userDataManager = userDataManager;
        this.userDataValidator = userDataValidator;
    }

    @FXML
    private void handleSubmit() {
        String username = usernameField.getText();
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();

        try {
            // Check if all fields are present
            ValidationResponse fieldsCheck = userDataValidator.checkForFieldsPresent(username, oldPassword, newPassword);
            if (!fieldsCheck.isValid()) {
                messageArea.setText(fieldsCheck.message());
                return;
            }

            // Validate current details and new password
            ValidationResponse detailsCheck = userDataValidator.checkNewDetails(username, oldPassword, newPassword, userDataManager);
            if (!detailsCheck.isValid()) {
                messageArea.setText(detailsCheck.message());
                return;
            }

            // Encrypt the new password and update it in the database
            String encryptedPassword = userDataValidator.generateSHA1(newPassword);
            userDataManager.updatePassword(username, encryptedPassword);
            messageArea.setText("Password updated successfully");
            sceneCoordinator.setScene(SceneCoordinator.SceneKey.LOGIN);
        } catch (Exception e) {
            messageArea.setText("Error updating password: " + e.getMessage() + "\nCheck console for details.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClear() {
        usernameField.clear();
        oldPasswordField.clear();
        newPasswordField.clear();
        messageArea.clear();
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}