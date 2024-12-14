package Client.library.Service;

import Client.library.model.Book;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class BookService {

    private static final String BASE_URL = "http://localhost:8080/api/books";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public List<Book> getAllBooks() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Type bookListType = new TypeToken<List<Book>>() {}.getType();
                return gson.fromJson(response.body(), bookListType);
            }
        } catch (Exception e) {
            // Handle exception silently
        }
        return List.of();
    }

    public boolean addBook(String title, String author, String isbn, int availableCopies, int totalCopies) {
        try {
            BookCreationRequest bookRequest = new BookCreationRequest(title, author, isbn, availableCopies, totalCopies);
            String jsonPayload = gson.toJson(bookRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/add"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 201;
        } catch (Exception e) {
            // Handle exception silently
        }
        return false;
    }

    public boolean updateBook(long bookId, Book updatedBook) {
        try {
            String json = gson.toJson(updatedBook);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/update/" + bookId))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            // Handle exception silently
        }
        return false;
    }

    public boolean deleteBook(long bookId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/delete/" + bookId))
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200 || response.statusCode() == 204;
        } catch (Exception e) {
            // Handle exception silently
        }
        return false;
    }

    // Inner DTO class for book creation
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
}