package Client.library.Controller;

import Client.library.Service.UserService;
import Client.library.model.User;
import Client.library.util.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
                loadMainApplication();
            } else {
                errorLabel.setText("Invalid credentials. Please try again.");
            }
        } catch (Exception e) {
            errorLabel.setText("An error occurred during login. Please try again later.");
        }
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