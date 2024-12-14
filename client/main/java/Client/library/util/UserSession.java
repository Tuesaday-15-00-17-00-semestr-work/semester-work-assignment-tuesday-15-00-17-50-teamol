package Client.library.util;

import Client.library.model.User;

public class UserSession {

    private static UserSession instance; // Singleton instance
    private static User loggedInUser;    // Logged-in user details

    // Private constructor to ensure Singleton pattern
    private UserSession() {}

    /**
     * Get the Singleton instance of UserSession.
     * @return UserSession instance
     */
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    /**
     * Set the logged-in user.
     * @param user User object representing the logged-in user
     */
    public void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    /**
     * Get the logged-in user.
     * @return User object representing the logged-in user
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Get the current user's ID (session ID).
     * @return The ID of the logged-in user, or -1 if no user is logged in
     */
    public Long getCurrentUserId() {
        return loggedInUser != null ? loggedInUser.getUserId() : -1L;
    }

    /**
     * Check if the logged-in user has an admin role.
     * @return True if the user is an admin, otherwise false
     */
    public boolean isAdmin() {
        return loggedInUser != null && "ADMIN".equalsIgnoreCase(loggedInUser.getRoleId());
    }
}