package security;

import domain.User;
import domain.enums.UserRole;
import java.util.HashMap;
import java.util.Map;

public class SecurityService {
    private static SecurityService instance;
    private Map<String, User> authenticatedUsers = new HashMap<>();
    private User currentUser;

    private SecurityService() {}

    public static SecurityService getInstance() {
        if (instance == null) {
            instance = new SecurityService();
        }
        return instance;
    }

    public void authenticateUser(User user) {
        this.currentUser = user;
        authenticatedUsers.put(user.getUserId(), user);
        System.out.println("User authenticated: " + user.getName() + " (" + user.getRole() + ")");
    }

    public void logout() {
        if (currentUser != null) {
            System.out.println("User logged out: " + currentUser.getName());
            currentUser = null;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public boolean hasRole(UserRole role) {
        return currentUser != null && currentUser.getRole() == role;
    }

    public boolean hasPermission(UserRole... allowedRoles) {
        if (currentUser == null) {
            return false;
        }
        for (UserRole role : allowedRoles) {
            if (currentUser.getRole() == role) {
                return true;
            }
        }
        return false;
    }

    public void checkPermission(UserRole... allowedRoles) throws SecurityException {
        if (!hasPermission(allowedRoles)) {
            throw new SecurityException("Access denied. Required roles: " + java.util.Arrays.toString(allowedRoles));
        }
    }
}
