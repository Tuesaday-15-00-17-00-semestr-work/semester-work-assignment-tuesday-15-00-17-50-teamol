package Server.library.dto;

public class LoginRequest {

    private String username;
    private String password;

    // Default constructor (required for JSON serialization/deserialization)
    public LoginRequest() {}

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}