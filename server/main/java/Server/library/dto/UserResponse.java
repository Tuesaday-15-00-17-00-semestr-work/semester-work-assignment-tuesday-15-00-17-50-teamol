package Server.library.dto;

import Server.library.models.User;

public class UserResponse {

    private Long id;
    private String username;
    private String role;

    // Constructor to populate UserResponse from a User entity
    public UserResponse(User user) {
        this.id = user.getUserId();
        this.username = user.getUsername();
        this.role = user.getRoleName(); // Assuming User has a getRoleName() helper method
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}