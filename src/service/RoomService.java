package service;

import domain.Room;
import domain.enums.RoomStatus;
import domain.enums.RoomType;
import observer.RoomSubject;
import repository.RoomRepository;
import java.time.LocalDate;
import java.util.*;

public class RoomService {
    private RoomRepository roomRepository;
    private Map<String, RoomSubject> roomSubjects = new HashMap<>();

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void addRoom(Room room) {
        roomRepository.save(room);
        RoomSubject subject = new RoomSubject(room);
        roomSubjects.put(room.getRoomNumber(), subject);
    }

    public Room getRoomByNumber(String roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public List<Room> getAvailableRooms() {
        return roomRepository.findAvailableRooms();
    }

    public List<Room> getRoomsByType(RoomType type) {
        return roomRepository.findByType(type);
    }

    public List<Room> getRoomsByStatus(RoomStatus status) {
        return roomRepository.findByStatus(status);
    }

    public List<Room> findAvailableRoomsByTypeAndDates(RoomType type, LocalDate checkIn, LocalDate checkOut) {
        return roomRepository.findAvailableByTypeAndDates(type, checkIn, checkOut);
    }

    public void updateRoomStatus(String roomNumber, RoomStatus newStatus) {
        RoomSubject subject = roomSubjects.get(roomNumber);
        if (subject != null) {
            subject.updateRoomStatus(newStatus);
        } else {
            Room room = roomRepository.findByRoomNumber(roomNumber);
            if (room != null) {
                room.setStatus(newStatus);
            }
        }
    }

    public RoomSubject getRoomSubject(String roomNumber) {
        return roomSubjects.get(roomNumber);
    }

    public double calculateOccupancyRate() {
        List<Room> allRooms = getAllRooms();
        if (allRooms.isEmpty()) {
            return 0.0;
        }
        long occupiedRooms = allRooms.stream()
                .filter(room -> room.getStatus() == RoomStatus.OCCUPIED || 
                              room.getStatus() == RoomStatus.RESERVED)
                .count();
        return (double) occupiedRooms / allRooms.size() * 100;
    }
}
