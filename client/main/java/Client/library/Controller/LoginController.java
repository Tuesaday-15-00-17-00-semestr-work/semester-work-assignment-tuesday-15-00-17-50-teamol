package Client.library.Controller;

import Client.library.Service.UserService;
import Client.library.model.User;
import Client.library.util.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    private final UserService userService = new UserService();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username and password must not be empty.");
            return;
        }

        try {
            User loggedInUser = userService.authenticate(username, password);

            if (loggedInUser != null) {
                UserSession.getInstance().setLoggedInUser(loggedInUser);
                loadMainApplication(); // Redirect to the main application after login
            } else {
                errorLabel.setText("Invalid credentials. Please try again.");
            }
        } catch (Exception e) {
            errorLabel.setText("An error occurred during login. Please try again later.");
        }
    }

    private void showError(String message) {
        // Option 1: Using the errorLabel to display errors
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: red;");

        // Option 2 (Optional): Showing errors using an Alert dialog
        showAlert(Alert.AlertType.ERROR, message);
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadMainApplication() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/book_list.fxml"));
            stage.getScene().setRoot(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
