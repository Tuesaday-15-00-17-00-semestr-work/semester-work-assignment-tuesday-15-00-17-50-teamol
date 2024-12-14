package Client.library.model;

import com.google.gson.annotations.SerializedName;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Transaction {
    @SerializedName("transactionId")
    private int transactionId;
    @SerializedName("userId")
    private int userId;
    @SerializedName("bookId")
    private int bookId;
    @SerializedName("action")
    private String action;
    @SerializedName("date")
    private String date;

    private final IntegerProperty transactionIdProperty = new SimpleIntegerProperty();
    private final IntegerProperty userIdProperty = new SimpleIntegerProperty();
    private final IntegerProperty bookIdProperty = new SimpleIntegerProperty();
    private final StringProperty actionProperty = new SimpleStringProperty();
    private final StringProperty dateProperty = new SimpleStringProperty();

    public Transaction() {}

    public Transaction(int transactionId, int userId, int bookId, String action, String date) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.bookId = bookId;
        this.action = action;
        this.date = date;

        updateProperties();
    }

    public void updateProperties() {
        this.transactionIdProperty.set(transactionId);
        this.userIdProperty.set(userId);
        this.bookIdProperty.set(bookId);
        this.actionProperty.set(action);
        this.dateProperty.set(date);
    }

    public int getUserId() {
        return userId;
    }

    public int getBookId() {
        return bookId;
    }

    public String getAction() {
        return action;
    }

    // JavaFX property getters for GUI binding
    public IntegerProperty transactionIdProperty() {
        return transactionIdProperty;
    }

    public IntegerProperty bookIdProperty() {
        return bookIdProperty;
    }

    public StringProperty actionProperty() {
        return actionProperty;
    }

    public StringProperty dateProperty() {
        return dateProperty;
    }
    private final StringProperty bookTitleProperty = new SimpleStringProperty();

    public StringProperty bookTitleProperty() {
        return bookTitleProperty;
    }

    public String getBookTitle() {
        return bookTitleProperty.get();
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitleProperty.set(bookTitle);
    }

    private final StringProperty usernameProperty = new SimpleStringProperty();

    public StringProperty usernameProperty() {
        return usernameProperty;
    }

    public String getUsername() {
        return usernameProperty.get();
    }

    public void setUsername(String username) {
        this.usernameProperty.set(username);
    }
}