package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Book;
import repository.BookRepository;

public class MainScreenController {
    @FXML
    private TableView<Book> tableViewBooks;

    @FXML
    private TableColumn<Book, Integer> tableColumnid;

    @FXML
    private TableColumn<Book, String> tableColumntitle;

    @FXML
    private TableColumn<Book, String> tableColumnauthor;

    @FXML
    private TableColumn<Book, String> tableColumngenre;

    @FXML
    private TextField searchField;

    @FXML
    private Button buttonViewBooks, buttonSearchBook, buttonDeleteBook, buttonAddBook;

    private BookRepository bookRepository = new BookRepository();
    private ObservableList<Book> booksList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        tableColumnid.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumntitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tableColumnauthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        tableColumngenre.setCellValueFactory(new PropertyValueFactory<>("genre"));

        // Load data into TableView (example):
        tableViewBooks.setItems(FXCollections.observableArrayList(
                new Book(1, "1984", "George Orwell", "Dystopian"),
                new Book(2, "To Kill a Mockingbird", "Harper Lee", "Fiction")
        ));
    }

    @FXML
    public void handleAddBook(ActionEvent event) {
        // Example Add Book Logic (use a popup for real implementation)
        bookRepository.addBook("Example Title", "Example Author", "Example Genre");
        loadBooks();
    }

    @FXML
    public void handleDeleteBook(ActionEvent event) {
        Book selectedBook = tableViewBooks.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            bookRepository.deleteBook(selectedBook.getId());
            loadBooks();
        }
    }

    @FXML
    public void handleRefresh(ActionEvent event) {
        loadBooks();
    }

    private void loadBooks() {
        booksList.clear();
        booksList.addAll(bookRepository.getAllBooks());
        tableViewBooks.setItems(booksList);
    }
}


