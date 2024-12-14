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

            JSONObject credentials = new JSONObject();
            credentials.put("username", username);
            credentials.put("password", password);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(credentials.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());
                if (jsonResponse.has("id") && jsonResponse.has("username") && jsonResponse.has("role")) {
                    return new User(
                            jsonResponse.getLong("id"),
                            jsonResponse.getString("username"),
                            jsonResponse.getString("role")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Authorization", "Bearer " + getUserToken())
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONArray jsonArray = new JSONArray(response.body());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonUser = jsonArray.getJSONObject(i);
                    users.add(new User(
                            jsonUser.getLong("userId"),
                            jsonUser.getString("username"),
                            jsonUser.getString("roleName"),
                            jsonUser.getString("email"),
                            jsonUser.getString("password")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean registerUser(String username, String email, String password, String roleId) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            JSONObject roleJson = new JSONObject().put("roleId", Integer.parseInt(roleId));
            JSONObject newUserJson = new JSONObject()
                    .put("username", username)
                    .put("email", email)
                    .put("password", password)
                    .put("role", roleJson);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(newUserJson.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 201;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUser(Long userId, String username, String email, String password, String roleId) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            JSONObject roleJson = new JSONObject().put("roleId", Integer.parseInt(roleId));
            JSONObject updateJson = new JSONObject()
                    .put("username", username)
                    .put("email", email)
                    .put("password", password)
                    .put("role", roleJson);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + userId))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(updateJson.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(Long userId) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + userId))
                    .header("Authorization", "Bearer " + getUserToken())
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getUserToken() {
        return "currentLoggedInUserAuthToken"; // Placeholder token implementation
    }
}