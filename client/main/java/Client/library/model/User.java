package Client.library.model;

public class User {
    private Long user_id;
    private String username;
    private String role_id;
    private String email;
    private String password;

public User(Long user_id, String username, String role_id) {
    this.user_id = user_id;
    this.username = username;
    this.role_id = role_id;
}

public User(Long user_id, String username, String role_id, String email, String password) {
    this.user_id = user_id;
    this.username = username;
    this.role_id = role_id;
    this.email = email;
    this.password = password;
}

    // Getters and setters
    public Long getUserId() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleId() {
        return role_id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
