package Client.library.Controller;

import Client.library.Service.UserService;
import Client.library.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddEditUserController {

    @FXML
    private TextField usernameField, emailField, passwordField, roleField;
    @FXML
    private Label formTitle, statusLabel;

    private final UserService userService = new UserService();
    private User selectedUser; // The user being edited, null for new user

    // Initialize the form for adding or editing
    public void setUser(User user) {
        if (user != null) {
            this.selectedUser = user;
            formTitle.setText("Edit User");
            usernameField.setText(user.getUsername());
            emailField.setText(user.getEmail());
            passwordField.setText(user.getPassword());
            roleField.setText(user.getRoleId());
        } else {
            formTitle.setText("Add User");
        }
    }

    @FXML
    private void handleSave() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String roleId = roleField.getText(); // Role ID input from the field

        // Input validation
        if (username.isBlank() || email.isBlank() || roleId.isBlank() || (selectedUser == null && password.isBlank())) {
            statusLabel.setText("All fields are required.");
            return;
        }

        boolean success;
        if (selectedUser == null) { // Adding a new user
            success = userService.registerUser(username, email, password, roleId);
            if (success) {
                closeWindow();
            } else {
                statusLabel.setText("Failed to add user. Try again.");
            }
        } else { // Updating an existing user
            success = userService.updateUser(selectedUser.getUserId(), username, email, password, roleId);
            if (success) {
                closeWindow();
            } else {
                statusLabel.setText("Failed to update user. Try again.");
            }
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}