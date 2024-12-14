package Client.library.model;

import com.google.gson.annotations.SerializedName;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Book {

    // Fields mapped with JSON annotations
    @SerializedName("bookId")
    private int bookId;

    @SerializedName("title")
    private String title;

    @SerializedName("author")
    private String author;

    @SerializedName("isbn")
    private String isbn;

    @SerializedName("availableCopies")
    private int availableCopies;

    @SerializedName("totalCopies")
    private int totalCopies;

    // Default Constructor
    public Book() {}

    // Parameterized Constructor
    public Book(int bookId, String title, String author, String isbn, int availableCopies, int totalCopies) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    // Properties for JavaFX GUI bindings (UI integration)
    public IntegerProperty bookIdProperty() {
        return new SimpleIntegerProperty(bookId);
    }

    public StringProperty titleProperty() {
        return new SimpleStringProperty(title);
    }

    public StringProperty authorProperty() {
        return new SimpleStringProperty(author);
    }

    public StringProperty isbnProperty() {
        return new SimpleStringProperty(isbn);
    }

    public IntegerProperty availableCopiesProperty() {
        return new SimpleIntegerProperty(availableCopies);
    }

    @Override
    public String toString() {
        return String.format(
                "Book [ID=%d, Title='%s', Author='%s', ISBN='%s', Available Copies=%d, Total Copies=%d]",
                bookId, title, author, isbn, availableCopies, totalCopies
        );
    }
}