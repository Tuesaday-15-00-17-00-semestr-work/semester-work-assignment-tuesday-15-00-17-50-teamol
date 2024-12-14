package Client.library.util;

import Client.library.model.User;

public class UserSession {

    private static UserSession instance; // Singleton instance
    private static User loggedInUser;    // Logged-in user details

    // Private constructor to ensure Singleton pattern
    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public Long getCurrentUserId() {
        return loggedInUser != null ? loggedInUser.getUserId() : -1L;
    }

    public boolean isAdmin() {
        return loggedInUser != null && "ADMIN".equalsIgnoreCase(loggedInUser.getRoleId());
    }
}