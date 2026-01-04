package service;

import domain.*;
import domain.enums.MaintenanceStatus;
import domain.enums.RoomStatus;
import java.util.*;

public class MaintenanceService {
    private RoomService roomService;
    private Map<String, MaintenanceRequest> maintenanceRequests = new HashMap<>();

    public MaintenanceService(RoomService roomService) {
        this.roomService = roomService;
    }

    public MaintenanceRequest createMaintenanceRequest(String roomNumber, String description, String priority) {
        Room room = roomService.getRoomByNumber(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Room not found");
        }

        MaintenanceRequest request = new MaintenanceRequest(room, description, priority);
        maintenanceRequests.put(request.getRequestId(), request);
        
        // Atualizar status do quarto
        roomService.updateRoomStatus(roomNumber, RoomStatus.UNDER_MAINTENANCE);
        
        System.out.println("Maintenance request created: " + request.getRequestId());
        System.out.println("Room: " + roomNumber + " | Priority: " + priority);
        
        return request;
    }

    public void updateRequestStatus(String requestId, MaintenanceStatus status) {
        MaintenanceRequest request = maintenanceRequests.get(requestId);
        if (request == null) {
            throw new IllegalArgumentException("Maintenance request not found");
        }

        request.setStatus(status);
        
        // Se a manutenção foi concluída, marcar quarto como DIRTY (precisa de limpeza)
        if (status == MaintenanceStatus.COMPLETED) {
            roomService.updateRoomStatus(request.getRoom().getRoomNumber(), RoomStatus.DIRTY);
            System.out.println("Maintenance completed for room " + request.getRoom().getRoomNumber());
        }
    }

    public void assignMaintenanceRequest(String requestId, String technician) {
        MaintenanceRequest request = maintenanceRequests.get(requestId);
        if (request == null) {
            throw new IllegalArgumentException("Maintenance request not found");
        }

        request.setAssignedTo(technician);
        request.setStatus(MaintenanceStatus.IN_PROGRESS);
        System.out.println("Maintenance request " + requestId + " assigned to " + technician);
    }

    public MaintenanceRequest getRequestById(String requestId) {
        return maintenanceRequests.get(requestId);
    }

    public List<MaintenanceRequest> getAllRequests() {
        return new ArrayList<>(maintenanceRequests.values());
    }

    public List<MaintenanceRequest> getRequestsByStatus(MaintenanceStatus status) {
        List<MaintenanceRequest> filtered = new ArrayList<>();
        for (MaintenanceRequest request : maintenanceRequests.values()) {
            if (request.getStatus() == status) {
                filtered.add(request);
            }
        }
        return filtered;
    }

    public List<MaintenanceRequest> getPendingRequests() {
        return getRequestsByStatus(MaintenanceStatus.PENDING);
    }
}
