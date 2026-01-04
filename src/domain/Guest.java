package domain;

import domain.enums.UserRole;

public class Guest extends User {
    private String phoneNumber;
    private String address;

    public Guest(String name, String email, String password, String phoneNumber, String address) {
        super(name, email, password, UserRole.GUEST);
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void displayPermissions() {
        System.out.println("Guest permissions: View rooms, Make reservations, View own reservations");
    }

    @Override
    public String toString() {
        return "Guest{" +
                "userId='" + getUserId() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
