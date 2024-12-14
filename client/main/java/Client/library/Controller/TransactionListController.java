package Client.library.Controller;

import Client.library.Service.BookService;
import Client.library.Service.TransactionService;
import Client.library.Service.UserService;
import Client.library.model.Book;
import Client.library.model.Transaction;
import Client.library.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionListController {

    @FXML
    private TableView<Transaction> transactionTable;
    @FXML
    private TableColumn<Transaction, Integer> transactionIdColumn;
    @FXML
    private TableColumn<Transaction, String> usernameColumn;
    @FXML
    private TableColumn<Transaction, String> bookTitleColumn;
    @FXML
    private TableColumn<Transaction, String> actionColumn;
    @FXML
    private TableColumn<Transaction, String> dateColumn;

    private final TransactionService transactionService = new TransactionService();
    private final ObservableList<Transaction> transactions = FXCollections.observableArrayList();

    private final BookService bookService = new BookService();
    private final Map<Integer, String> bookTitleMap = new HashMap<>();

    private final UserService userService = new UserService();
    private final Map<Integer, String> userMap = new HashMap<>();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadTransactions();
    }

    private void loadBookTitleMapping() {
        List<Book> books = bookService.getAllBooks();
        for (Book book : books) {
            bookTitleMap.put(book.getBookId(), book.getTitle());
        }
    }

    private void loadUserMapping() {
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            userMap.put(Math.toIntExact(user.getUserId()), user.getUsername());
        }
    }

    private void setupTableColumns() {
        transactionIdColumn.setCellValueFactory(cellData -> cellData.getValue().transactionIdProperty().asObject());
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        bookTitleColumn.setCellValueFactory(cellData -> cellData.getValue().bookTitleProperty());
        actionColumn.setCellValueFactory(cellData -> cellData.getValue().actionProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
    }

    private void loadTransactions() {
        try {
            loadBookTitleMapping();
            loadUserMapping();

            List<Transaction> serverTransactions = transactionService.getAllTransactions();

            for (Transaction transaction : serverTransactions) {
                String bookTitle = bookTitleMap.getOrDefault(transaction.getBookId(), "Unknown Book");
                transaction.setBookTitle(bookTitle);

                String username = userMap.getOrDefault(transaction.getUserId(), "Unknown User");
                transaction.setUsername(username);
            }

            transactions.setAll(serverTransactions);
            transactionTable.setItems(transactions);
        } catch (Exception e) {}
    }

    @FXML
    private void handleCloseWindow() {
        Stage stage = (Stage) transactionTable.getScene().getWindow();
        stage.close();
    }
}