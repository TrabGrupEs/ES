package factory;

import domain.*;
import domain.enums.UserRole;

public class UserFactory {
    
    public static User createUser(UserRole role, String name, String email, String password, String... additionalInfo) {
        switch (role) {
            case GUEST:
                String phoneNumber = additionalInfo.length > 0 ? additionalInfo[0] : "";
                String address = additionalInfo.length > 1 ? additionalInfo[1] : "";
                return new Guest(name, email, password, phoneNumber, address);
                
            case RECEPTIONIST:
                String employeeId = additionalInfo.length > 0 ? additionalInfo[0] : "EMP-" + System.currentTimeMillis();
                String shift = additionalInfo.length > 1 ? additionalInfo[1] : "Morning";
                return new Receptionist(name, email, password, employeeId, shift);
                
            case MANAGER:
                String department = additionalInfo.length > 0 ? additionalInfo[0] : "General";
                return new Manager(name, email, password, department);
                
            case ADMIN:
                return new Administrator(name, email, password);
                
            default:
                throw new IllegalArgumentException("Unknown user role: " + role);
        }
    }
}
