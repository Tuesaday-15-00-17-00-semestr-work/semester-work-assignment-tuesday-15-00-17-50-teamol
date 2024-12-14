package Client.library.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Transaction {
    private final IntegerProperty transactionId = new SimpleIntegerProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty bookId = new SimpleStringProperty();
    private final StringProperty action = new SimpleStringProperty();
    private final StringProperty date = new SimpleStringProperty();

    // Constructor
    public Transaction(int transactionId, String username, String bookId, String action, String date) {
        this.transactionId.set(transactionId);
        this.username.set(username);
        this.bookId.set(bookId);
        this.action.set(action);
        this.date.set(date);
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId.get();
    }

    public void setTransactionId(int transactionId) {
        this.transactionId.set(transactionId);
    }

    public IntegerProperty transactionIdProperty() {
        return transactionId;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public String getBookTitle() {
        return bookId.get();
    }

    public void setBookTitle(String bookId) {
        this.bookId.set(bookId);
    }

    public StringProperty bookTitleProperty() {
        return bookId;
    }

    public String getAction() {
        return action.get();
    }

    public void setAction(String action) {
        this.action.set(action);
    }

    public StringProperty actionProperty() {
        return action;
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public StringProperty dateProperty() {
        return date;
    }
}