package Client.library.util;

import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient {

    public static String borrowBook(Long bookId) {
        return sendRequest("http://localhost:8080/api/borrow-return/borrow?book_id=" + bookId);
    }

    public static String returnBook(Long bookId) {
        return sendRequest("http://localhost:8080/api/borrow-return/return?book_id=" + bookId);
    }

    private static String sendRequest(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return "Operation successful.";
            } else {
                return "Operation failed.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error connecting to server.";
        }
    }
}