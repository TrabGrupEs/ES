package repository;

import domain.Room;
import domain.enums.RoomStatus;
import domain.enums.RoomType;
import java.time.LocalDate;
import java.util.List;

public interface RoomRepository {
    void save(Room room);
    Room findByRoomNumber(String roomNumber);
    List<Room> findAll();
    List<Room> findAvailableRooms();
    List<Room> findByType(RoomType type);
    List<Room> findByStatus(RoomStatus status);
    List<Room> findAvailableByTypeAndDates(RoomType type, LocalDate checkIn, LocalDate checkOut);
}
