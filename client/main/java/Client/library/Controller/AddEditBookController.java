package Client.library.Controller;

import Client.library.Service.BookService;
import Client.library.model.Book;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddEditBookController {

    @FXML
    private TextField titleField, authorField, isbnField, totalCopiesField, availableCopiesField;
    @FXML
    private Label formTitle, statusLabel;

    private final BookService bookService = new BookService();
    private Book selectedBook; // Stores the book being edited (null for a new book)

    // Initialize the form for adding or editing
    public void setBook(Book book) {
        if (book != null) {
            this.selectedBook = book;
            formTitle.setText("Edit Book");
            titleField.setText(book.getTitle());
            authorField.setText(book.getAuthor());
            isbnField.setText(book.getIsbn());
            totalCopiesField.setText(String.valueOf(book.getTotalCopies()));
            availableCopiesField.setText(String.valueOf(book.getAvailableCopies()));
        } else {
            formTitle.setText("Add Book");
        }
    }

    @FXML
    private void handleSave() {
        String title = titleField.getText();
        String author = authorField.getText();
        String isbn = isbnField.getText();

        // Define totalCopiesStr and availableCopiesStr as inputs from the UI fields
        String totalCopiesStr = totalCopiesField.getText();
        String availableCopiesStr = availableCopiesField.getText();

        // Input validation
        if (title.isBlank() || author.isBlank() || isbn.isBlank() || totalCopiesStr.isBlank() || availableCopiesStr.isBlank()) {
            statusLabel.setText("All fields are required.");
            logDebug("Validation failed: Missing required fields.");
            return;
        }

        // Parse the numeric fields (totalCopies and availableCopies)
        int totalCopies, availableCopies;
        try {
            totalCopies = Integer.parseInt(totalCopiesStr);
            availableCopies = Integer.parseInt(availableCopiesStr);
        } catch (NumberFormatException e) {
            statusLabel.setText("Copies fields must be valid integers.");
            logError("Validation failed: Copies fields are invalid.", e);
            return;
        }

        // Add or update book logic
        if (selectedBook == null) { // Adding a new book
            logDebug("Attempting to add new book: " + title);
            boolean success = bookService.addBook(title, author, isbn, availableCopies, totalCopies);
            if (success) {
                logDebug("Book successfully added: " + title);
                closeWindow();
            } else {
                logDebug("Failed to add book: " + title);
                statusLabel.setText("Failed to add book. Try again.");
            }
        } else { // Updating an existing book
            selectedBook.setTitle(title);
            selectedBook.setAuthor(author);
            selectedBook.setIsbn(isbn);
            selectedBook.setTotalCopies(totalCopies);
            selectedBook.setAvailableCopies(availableCopies);

            logDebug("Attempting to update book (ID: " + selectedBook.getBookId() + "): " + title);
            boolean success = bookService.updateBook(selectedBook.getBookId(), selectedBook);
            if (success) {
                logDebug("Book successfully updated (ID: " + selectedBook.getBookId() + "): " + title);
                closeWindow();
            } else {
                logDebug("Failed to update book (ID: " + selectedBook.getBookId() + "): " + title);
                statusLabel.setText("Failed to update book. Try again.");
            }
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
    // Helper method to log debug info
    private void logDebug(String message) {
        System.out.println("[DEBUG] " + message);
    }

    // Helper method to log errors
    private void logError(String message, Exception e) {
        System.err.println("[ERROR] " + message);
        e.printStackTrace();
    }
}