package Client.library.Service;

import Client.library.model.Book;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private static final String BASE_URL = "http://localhost:8080/api/books";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public List<Book> getAllBooks() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .GET()
                    .build();
            logDebug("Sending GET Request to: " + BASE_URL);

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logDebug("Response Code: " + response.statusCode());
            logDebug("Response Body: " + response.body());

            Type bookListType = new TypeToken<List<Book>>() {}.getType();
            return gson.fromJson(response.body(), bookListType);
        } catch (Exception e) {
            logError("Error fetching books: ", e);
            return List.of();
        }
    }

    public boolean addBook(String title, String author, String isbn, int availableCopies, int totalCopies) {
        try {
            // Create a separate request object to exclude bookId during serialization
            BookCreationRequest bookRequest = new BookCreationRequest(title, author, isbn, availableCopies, totalCopies);
            String jsonPayload = gson.toJson(bookRequest);

            // Construct the POST request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/add")) // Use the base URL for book creation as per REST principles
                    .header("Content-Type", "application/json") // Set the header for JSON request
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload)) // Add JSON as request body
                    .build();

            // Send the request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Log debug information
            logDebug("Request sent to " + BASE_URL);
            logDebug("Request Payload: " + jsonPayload);
            logDebug("Response Code: " + response.statusCode());
            logDebug("Response Body: " + response.body());

            // Check if the response code indicates success (HTTP 201 Created)
            return response.statusCode() == 201;
        } catch (Exception e) {
            // Handle errors and log them
            logError("Error occurred while adding the book.", e);
            return false;
        }
    }

    // Separate DTO for Book Creation
    private static class BookCreationRequest {
        private String title;
        private String author;
        private String isbn;
        private int availableCopies;
        private int totalCopies;

        public BookCreationRequest(String title, String author, String isbn, int availableCopies, int totalCopies) {
            this.title = title;
            this.author = author;
            this.isbn = isbn;
            this.availableCopies = availableCopies;
            this.totalCopies = totalCopies;
        }
    }

    public boolean updateBook(long bookId, Book updatedBook) {
        try {
            String json = gson.toJson(updatedBook);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/update/" + bookId)) // Admin-specific endpoint
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            logDebug("Sending PUT Request to: " + BASE_URL + "/update/" + bookId);
            logDebug("Request Body: " + json);

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logDebug("Response Code: " + response.statusCode());
            logDebug("Response Body: " + response.body());

            return response.statusCode() == 200; // Returns true if book is successfully updated
        } catch (Exception e) {
            logError("Error updating book (ID: " + bookId + "): ", e);
            return false;
        }
    }

    public boolean deleteBook(long bookId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/delete/" + bookId)) // Admin-specific endpoint
                    .DELETE()
                    .build();

            logDebug("Sending DELETE Request to: " + BASE_URL + "/delete/" + bookId);

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logDebug("Response Code: " + response.statusCode());
            logDebug("Response Body: " + response.body());

            return response.statusCode() == 200 || response.statusCode() == 204; // Successful delete response codes
        } catch (Exception e) {
            logError("Error deleting book (ID: " + bookId + "): ", e);
            return false;
        }
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