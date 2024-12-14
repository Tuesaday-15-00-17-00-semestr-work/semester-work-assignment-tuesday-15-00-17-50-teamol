package Client.library.Controller;

import Client.library.Service.UserService;
import Client.library.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageUsersController {

    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, Long> userIdColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;

    private final UserService userService = new UserService();
    private final ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadUsers();
    }

    private void setupTableColumns() {
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("roleId"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
    }

    private void loadUsers() {
        try {
            users.setAll(userService.getAllUsers());
            userTable.setItems(users);
        } catch (Exception e) {
            showAlert("Error", "Failed to load users.");
        }
    }

    @FXML
    private void handleAddUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddEditUser.fxml"));
            Parent parent = loader.load();

            AddEditUserController controller = loader.getController();
            controller.setUser(null);

            Stage stage = new Stage();
            stage.setTitle("Add User");
            stage.setScene(new Scene(parent));
            stage.showAndWait();

            loadUsers();
        } catch (IOException e) {
            showAlert("Error", "Failed to open Add User window.");
        }
    }

    @FXML
    private void handleEditUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Warning", "Please select a user to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddEditUser.fxml"));
            Parent parent = loader.load();

            AddEditUserController controller = loader.getController();
            controller.setUser(selectedUser);

            Stage stage = new Stage();
            stage.setTitle("Edit User");
            stage.setScene(new Scene(parent));
            stage.showAndWait();

            loadUsers();
        } catch (IOException e) {
            showAlert("Error", "Failed to open Edit User window.");
        }
    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Warning", "Please select a user to delete.");
            return;
        }

        boolean confirmation = showConfirmation("Confirm Delete", "Are you sure you want to delete this user?");
        if (!confirmation) {
            return;
        }

        boolean success = userService.deleteUser(selectedUser.getUserId());
        if (success) {
            users.remove(selectedUser);
            showAlert("Success", "User deleted successfully!");
        } else {
            showAlert("Error", "Failed to delete user.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmation(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
        return alert.getResult() == javafx.scene.control.ButtonType.OK;
    }
}