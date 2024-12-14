package Client.library.Service;

import Client.library.model.Transaction;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class TransactionService {

    private static final String BASE_URL = "http://localhost:8080/api/borrow-return";
    private final HttpClient client = HttpClient.newHttpClient();

    public boolean borrowBook(int userId, int bookId) {
        return performTransaction(userId, bookId, "borrow");
    }

    public boolean returnBook(int userId, int bookId) {
        return performTransaction(userId, bookId, "return");
    }

    private boolean performTransaction(int userId, int bookId, String action) {
        try {
            JsonObject payload = new JsonObject();
            payload.addProperty("user_id", userId);
            payload.addProperty("book_id", bookId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + action))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Transaction> getAllTransactions() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/transactions"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                List<Transaction> transactions = new Gson().fromJson(response.body(), new TypeToken<List<Transaction>>() {}.getType());
                transactions.forEach(Transaction::updateProperties);
                return transactions;
            }
        } catch (Exception e) {
            // Exception handling is silent to avoid debugging outputs
        }
        return FXCollections.observableArrayList();
    }
}