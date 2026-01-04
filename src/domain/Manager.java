package domain;

import domain.enums.UserRole;

public class Manager extends User {
    private String department;

    public Manager(String name, String email, String password, String department) {
        super(name, email, password, UserRole.MANAGER);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public void displayPermissions() {
        System.out.println("Manager permissions: All receptionist permissions + Manage pricing, Generate reports, View financial data");
    }

    @Override
    public String toString() {
        return "Manager{" +
                "userId='" + getUserId() + '\'' +
                ", name='" + getName() + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
