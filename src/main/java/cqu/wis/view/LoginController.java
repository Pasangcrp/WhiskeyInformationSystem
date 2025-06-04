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
 * Controller for the login scene GUI in the Whiskey Information System.
 */
@SuppressWarnings("unused")
public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextArea messageArea;
    @FXML private Button loginButton;
    @FXML private Button changePasswordButton;
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
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Check if fields are present
        ValidationResponse fieldsCheck = userDataValidator.checkForFieldsPresent(username, password);
        if (!fieldsCheck.isValid()) {
            messageArea.setText(fieldsCheck.message());
            return;
        }

        // Check if user exists and credentials are valid
        try {
            ValidationResponse detailsCheck = userDataValidator.checkCurrentDetails(username, password, userDataManager);
            messageArea.setText(detailsCheck.message());
            if (detailsCheck.isValid()) {
                if (detailsCheck.message().equals("Login successful")) {
                    // Navigate to query scene for valid login with encrypted password
                    sceneCoordinator.setScene(SceneCoordinator.SceneKey.QUERY);
                }
                // If message is "Please change your default password", stay on login scene
            }
        } catch (Exception e) {
            messageArea.setText("Error during login: " + e.getMessage() + "\nCheck console for details.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleChangePassword() {
        sceneCoordinator.setScene(SceneCoordinator.SceneKey.PASSWORD);
    }

    @FXML
    private void handleClear() {
        usernameField.clear();
        passwordField.clear();
        messageArea.clear();
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
