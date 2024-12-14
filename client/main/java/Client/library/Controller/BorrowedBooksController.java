package Client.library.Controller;

import Client.library.Service.BookService;
import Client.library.Service.TransactionService;
import Client.library.model.Book;
import Client.library.model.Transaction;
import Client.library.util.UserSession;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class BorrowedBooksController {

    @FXML
    private TableView<Transaction> borrowedBooksTable;
    @FXML
    private TableColumn<Transaction, Integer> bookIdColumn;
    @FXML
    private TableColumn<Transaction, String> titleColumn;
    @FXML
    private TableColumn<Transaction, String> authorColumn;
    @FXML
    private TableColumn<Transaction, String> borrowedDateColumn;

    @FXML
    private Label statusLabel;

    private final TransactionService transactionService = new TransactionService();
    private final BookService bookService = new BookService();

    private final ObservableList<Transaction> borrowedBooks = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadBorrowedBooks();
    }

    private void setupTableColumns() {
        bookIdColumn.setCellValueFactory(cellData -> cellData.getValue().bookIdProperty().asObject());
        titleColumn.setCellValueFactory(cellData -> {
            Book book = bookService.getAllBooks().stream()
                    .filter(b -> b.getBookId() == cellData.getValue().getBookId())
                    .findFirst()
                    .orElse(null);
            return new SimpleStringProperty(book != null ? book.getTitle() : "Unknown");
        });
        authorColumn.setCellValueFactory(cellData -> {
            Book book = bookService.getAllBooks().stream()
                    .filter(b -> b.getBookId() == cellData.getValue().getBookId())
                    .findFirst()
                    .orElse(null);
            return new SimpleStringProperty(book != null ? book.getAuthor() : "Unknown");
        });
        borrowedDateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
    }

    private void loadBorrowedBooks() {
        Long currentUserId = UserSession.getInstance().getCurrentUserId();

        new Thread(() -> {
            try {
                List<Transaction> transactions = transactionService.getAllTransactions();
                List<Transaction> userBorrowedBooks = transactions.stream()
                        .filter(t -> t.getUserId() == currentUserId.intValue() && t.getAction().equals("BORROW"))
                        .collect(Collectors.toList());
                Platform.runLater(() -> {
                    borrowedBooks.setAll(userBorrowedBooks);
                    borrowedBooksTable.setItems(borrowedBooks);
                });
            } catch (Exception e) {
                Platform.runLater(() -> showStatus("Error loading borrowed books.", true));
            }
        }).start();
    }

    @FXML
    private void handleReturnBook() {
        Transaction selectedTransaction = borrowedBooksTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            showStatus("Please select a book to return.", true);
            return;
        }

        Long currentUserId = UserSession.getInstance().getCurrentUserId();
        if (currentUserId == -1) {
            showStatus("You need to log in to return a book.", true);
            return;
        }

        new Thread(() -> {
            boolean success = transactionService.returnBook(currentUserId.intValue(), selectedTransaction.getBookId());
            Platform.runLater(() -> {
                if (success) {
                    showStatus("Book returned successfully.", false);
                    borrowedBooks.remove(selectedTransaction);
                } else {
                    showStatus("Failed to return the book. Try again.", true);
                }
            });
        }).start();
    }

    @FXML
    private void handleCloseWindow() {
        Stage stage = (Stage) borrowedBooksTable.getScene().getWindow();
        stage.close();
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }
}