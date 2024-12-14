package Client.library.Service;

import Client.library.model.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private static final String BASE_URL = "http://localhost:8080/api/users";

    public User authenticate(String username, String password) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Create JSON payload for credentials
            JSONObject credentials = new JSONObject();
            credentials.put("username", username);
            credentials.put("password", password);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/login")) // Adjust the API URL if needed
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(credentials.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Log the response for debugging
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());

            if (response.statusCode() == 200) {
                // Parse the response
                JSONObject jsonResponse = new JSONObject(response.body());

                // Check for and extract the expected fields
                if (jsonResponse.has("id") && jsonResponse.has("username") && jsonResponse.has("role")) {
                    // Create and return the User object based on the response fields
                    return new User(
                            jsonResponse.getLong("id"),        // Use "id" instead of "user_id"
                            jsonResponse.getString("username"),
                            jsonResponse.getString("role")     // Use "role" instead of "role_id"
                    );
                } else {
                    // Handle missing fields
                    System.out.println("Error: The response is missing required fields.");
                }
            } else {
                // Handle non-200 response codes
                System.out.println("Authentication failed: " + response.body());
            }
        } catch (Exception e) {
            // Handle exceptions gracefully
            e.printStackTrace();
        }

        return null; // Return null if authentication fails
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Authorization", "Bearer " + getUserToken()) // Add token for authorization
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONArray jsonArray = new JSONArray(response.body());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonUser = jsonArray.getJSONObject(i);
                    User user = new User(
                            jsonUser.getLong("id"),
                            jsonUser.getString("username"),
                            jsonUser.getString("role") // Assuming "role" is provided in the response
                    );
                    users.add(user);
                }
            } else {
                System.err.println("Failed to fetch users. Response: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean registerUser(String username, String email, String password, String roleId) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Create JSON payload for new user registration
            JSONObject roleJson = new JSONObject().put("roleId", Integer.parseInt(roleId)); // Role ID should be sent this way
            JSONObject newUserJson = new JSONObject()
                    .put("username", username)
                    .put("email", email)
                    .put("password", password)
                    .put("role", roleJson); // Role object with roleId

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(newUserJson.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 201) {
                System.out.println("User registered successfully.");
                return true; // Success
            } else {
                System.err.println("Failed to register user: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Registration failed
    }

    // Update a user's details
    public boolean updateUser(Long userId, String username, String email, String password, String roleId) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Create JSON payload for update
            JSONObject roleJson = new JSONObject().put("roleId", Integer.parseInt(roleId));
            JSONObject updateJson = new JSONObject()
                    .put("username", username)
                    .put("email", email)
                    .put("password", password)
                    .put("role", roleJson); // Role object with roleId

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + userId)) // PUT to the API URL with the user ID
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(updateJson.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("User updated successfully.");
                return true; // Success
            } else {
                System.err.println("Failed to update user: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Update failed
    }

    // Delete a user (Admin-only operation)
    public boolean deleteUser(Long userId) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + userId)) // DELETE to /api/users/{userId}
                    .header("Authorization", "Bearer " + getUserToken())
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) { // Success
                System.out.println("User deleted successfully.");
                return true;
            } else {
                System.err.println("Failed to delete user: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Return false if deletion fails
    }

    // Utility method to get the current user's token
    private String getUserToken() {
        // Fetch the user's token from session management or configuration (placeholder)
        return "currentLoggedInUserAuthToken";
    }
}