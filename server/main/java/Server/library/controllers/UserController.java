package Server.library.controllers;

import Server.library.dto.LoginRequest;
import Server.library.dto.UserResponse;
import Server.library.models.User;
import Server.library.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
        return ResponseEntity.ok(new UserResponse(user)); // Use DTO
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User newUser) {
        try {
            // Save the new user
            User registeredUser = userService.registerUser(newUser);

            // Return success response
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully with ID: " + registeredUser.getUserId());
        } catch (Exception e) {
            // Handle exceptions (e.g., database constraint violation)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error registering user: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers(); // Fetch the list of User entities directly
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<?> updateUser(@PathVariable Long user_id, @RequestBody User updatedUser, HttpServletRequest request) {
        User user = userService.updateUser(user_id, updatedUser);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.status(404).body("User not found.");
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long user_id, HttpServletRequest request) {
        boolean isDeleted = userService.deleteUser(user_id);
        return isDeleted ? ResponseEntity.ok("User deleted successfully.")
                : ResponseEntity.status(404).body("User not found.");
    }
}

