package domain;

import domain.enums.MaintenanceStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public class MaintenanceRequest {
    private String requestId;
    private Room room;
    private String description;
    private MaintenanceStatus status;
    private String priority;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private String assignedTo;

    public MaintenanceRequest(Room room, String description, String priority) {
        this.requestId = UUID.randomUUID().toString();
        this.room = room;
        this.description = description;
        this.status = MaintenanceStatus.PENDING;
        this.priority = priority;
        this.createdAt = LocalDateTime.now();
    }

    public String getRequestId() {
        return requestId;
    }

    public Room getRoom() {
        return room;
    }

    public String getDescription() {
        return description;
    }

    public MaintenanceStatus getStatus() {
        return status;
    }

    public void setStatus(MaintenanceStatus status) {
        this.status = status;
        if (status == MaintenanceStatus.COMPLETED) {
            this.completedAt = LocalDateTime.now();
        }
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    @Override
    public String toString() {
        return "MaintenanceRequest{" +
                "requestId='" + requestId + '\'' +
                ", room=" + room.getRoomNumber() +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", priority='" + priority + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
