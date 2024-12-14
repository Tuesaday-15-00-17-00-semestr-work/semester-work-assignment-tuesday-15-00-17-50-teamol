package Client.library.Controller;

import Client.library.Service.BookService;
import Client.library.Service.TransactionService;
import Client.library.model.Book;
import Client.library.util.UserSession;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;

public class BookListController {

    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, Integer> bookIdColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> isbnColumn;
    @FXML
    private TableColumn<Book, Integer> availableCopiesColumn;

    @FXML
    private TextField searchBar;
    @FXML
    private Label statusLabel;

    // Admin-only UI components
    @FXML
    private Button addBookButton;
    @FXML
    private Button updateBookButton;
    @FXML
    private Button deleteBookButton;
    @FXML
    private Button manageUsersButton;
    @FXML
    private Button viewTransactionsButton;

    // Borrow/Return buttons (for regular users)
    @FXML
    private Button borrowBookButton;
    @FXML
    private Button viewBorrowedBooksButton;

    @FXML
    private HBox adminActionsBox; // Parent container for admin-only actions

    private final BookService bookService = new BookService();
    private final TransactionService transactionService = new TransactionService();
    private ObservableList<Book> books = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        adjustUIForRole();
        loadBooks();
        setupSearchListener();
    }

    private void setupTableColumns() {
        bookIdColumn.setCellValueFactory(cellData -> cellData.getValue().bookIdProperty().asObject());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        isbnColumn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty());
        availableCopiesColumn.setCellValueFactory(cellData -> cellData.getValue().availableCopiesProperty().asObject());
    }

    private void adjustUIForRole() {
        // Check if the logged-in user is an admin
        boolean isAdmin = UserSession.getInstance().isAdmin();

        if (isAdmin) {
            // Show admin buttons and actions
            adminActionsBox.setVisible(true);
            adminActionsBox.setManaged(true); // Ensure it's included in layout
            borrowBookButton.setVisible(true);
        } else {
            // Hide admin-only actions for regular users
            adminActionsBox.setVisible(false);
            adminActionsBox.setManaged(false);
        }
    }

    private void loadBooks() {
        new Thread(() -> {
            try {
                List<Book> serverBooks = bookService.getAllBooks();
                Platform.runLater(() -> {
                    books.setAll(serverBooks);
                    bookTable.setItems(books);
                });
            } catch (Exception e) {
                Platform.runLater(() -> showStatus("Failed to fetch books. Please try again.", true));
                e.printStackTrace();
            }
        }).start();
    }

    private void setupSearchListener() {
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> filterBooks(newValue));
    }

    private void filterBooks(String query) {
        String lowerCaseQuery = query.toLowerCase();
        ObservableList<Book> filteredBooks = books.filtered(book ->
                book.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                        book.getAuthor().toLowerCase().contains(lowerCaseQuery) ||
                        book.getIsbn().toLowerCase().contains(lowerCaseQuery)
        );
        bookTable.setItems(filteredBooks);
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String query = searchBar.getText(); // Assuming searchBar is a TextField in your FXML
        filterBooks(query); // Calls the method that performs filtering
    }

    @FXML
    private void handleAddBook() {
        openBookForm(null); // Pass null to indicate a new book is being added
    }

    @FXML
    private void handleUpdateBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showStatus("Please select a book to update.", true);
            return;
        }
        openBookForm(selectedBook); // Pass the selected book for editing
    }

    @FXML
    private void handleDeleteBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            statusLabel.setText("Please select a book to delete.");
            logDebug("Delete operation failed: No book selected.");
            return;
        }

        logDebug("Attempting to delete book (ID: " + selectedBook.getBookId() + "): " + selectedBook.getTitle());
        boolean success = bookService.deleteBook(selectedBook.getBookId());
        if (success) {
            logDebug("Book successfully deleted (ID: " + selectedBook.getBookId() + ")");
            statusLabel.setText("Book deleted successfully.");
            loadBooks(); // Reload the book list after deletion
        } else {
            logDebug("Failed to delete book (ID: " + selectedBook.getBookId() + ")");
            statusLabel.setText("Failed to delete book. Try again.");
        }
    }

    // Utility method to open the Add/Edit Book modal
    private void openBookForm(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_edit_book.fxml"));
            Parent root = loader.load();
            AddEditBookController controller = loader.getController();
            controller.setBook(book);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(book == null ? "Add Book" : "Edit Book");
            stage.show();

            // Reload book list after closing the modal
            stage.setOnHiding(event -> loadBooks());
        } catch (Exception e) {
            e.printStackTrace();
            showStatus("Could not open the book form.", true);
        }
    }

    @FXML
    private void handleManageUsers(ActionEvent event) {
        try {
            // Load the ManageUsers.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/manage_users.fxml"));
            Parent root = loader.load();

            // Set up a new stage for Manage Users interface
            Stage stage = new Stage();
            stage.setTitle("Manage Users");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showStatus("Failed to open Manage Users panel.", true);
        }
    }

    @FXML
    private void handleBorrowBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showStatus("Please select a book to borrow.", true);
            return;
        }

        Long currentUserId = UserSession.getInstance().getCurrentUserId();
        if (currentUserId == -1) {
            showStatus("You need to log in to borrow a book.", true);
            return;
        }

        new Thread(() -> {
            boolean success = transactionService.borrowBook(currentUserId.intValue(), selectedBook.getBookId());
            Platform.runLater(() -> {
                if (success) {
                    showStatus("Book borrowed successfully.", false);
                    loadBooks();
                } else {
                    showStatus("Failed to borrow the book. Please try again.", true);
                }
            });
        }).start();
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    @FXML
    private void handleViewTransactions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/transaction_list.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Transaction List");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showStatus("Failed to open transaction list.", true);
        }
    }

    @FXML
    private void handleViewBorrowedBooks() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/borrowed_books.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Borrowed Books");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showStatus("Failed to open borrowed books view.", true);
        }
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            // Load the login screen (assuming FXMLLoader is used to load login.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();

            // Get the current stage.
            Stage stage = (Stage) bookTable.getScene().getWindow();

            // Set the new scene in the stage.
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showStatus("Failed to log out. Please try again.", true);
        }
    }
    // Helper method to log debug info
    private void logDebug(String message) {
        System.out.println("[DEBUG] " + message);
    }

}