package domain;

import domain.enums.UserRole;

public class Administrator extends User {
    public Administrator(String name, String email, String password) {
        super(name, email, password, UserRole.ADMIN);
    }

    @Override
    public void displayPermissions() {
        System.out.println("Administrator permissions: Full system access, User management, System configuration");
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "userId='" + getUserId() + '\'' +
                ", name='" + getName() + '\'' +
                '}';
    }
}
