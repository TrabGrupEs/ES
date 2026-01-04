package service;

import domain.Room;
import domain.enums.RoomStatus;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingService {
    private RoomService roomService;
    private List<String> taskList = new ArrayList<>();

    public HousekeepingService(RoomService roomService) {
        this.roomService = roomService;
    }

    public List<Room> getRoomsThatNeedCleaning() {
        return roomService.getRoomsByStatus(RoomStatus.DIRTY);
    }

    public void markRoomAsCleaned(String roomNumber) {
        Room room = roomService.getRoomByNumber(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Room not found");
        }
        
        if (room.getStatus() != RoomStatus.DIRTY) {
            System.out.println("Warning: Room " + roomNumber + " was not marked as DIRTY");
        }
        
        roomService.updateRoomStatus(roomNumber, RoomStatus.AVAILABLE);
        System.out.println("Room " + roomNumber + " cleaned and now AVAILABLE");
    }

    public void addCleaningTask(String task) {
        taskList.add(task);
        System.out.println("Cleaning task added: " + task);
    }

    public List<String> getTaskList() {
        return new ArrayList<>(taskList);
    }

    public void completeTask(String task) {
        if (taskList.remove(task)) {
            System.out.println("Task completed: " + task);
        } else {
            System.out.println("Task not found: " + task);
        }
    }

    public void generateDailyCleaningReport() {
        System.out.println("\n=== Daily Cleaning Report ===");
        List<Room> dirtyRooms = getRoomsThatNeedCleaning();
        System.out.println("Rooms requiring cleaning: " + dirtyRooms.size());
        for (Room room : dirtyRooms) {
            System.out.println("- Room " + room.getRoomNumber() + " (" + room.getRoomType() + ")");
        }
        System.out.println("Pending tasks: " + taskList.size());
        System.out.println("============================\n");
    }
}
