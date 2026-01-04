package domain;

import domain.enums.UserRole;

public class Receptionist extends User {
    private String employeeId;
    private String shift;

    public Receptionist(String name, String email, String password, String employeeId, String shift) {
        super(name, email, password, UserRole.RECEPTIONIST);
        this.employeeId = employeeId;
        this.shift = shift;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    @Override
    public void displayPermissions() {
        System.out.println("Receptionist permissions: Manage reservations, Check-in/Check-out, Assign rooms, View all reservations");
    }

    @Override
    public String toString() {
        return "Receptionist{" +
                "userId='" + getUserId() + '\'' +
                ", name='" + getName() + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", shift='" + shift + '\'' +
                '}';
    }
}
