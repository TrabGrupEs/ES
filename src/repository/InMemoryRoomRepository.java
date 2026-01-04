package repository;

import domain.Room;
import domain.Reservation;
import domain.enums.RoomStatus;
import domain.enums.RoomType;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryRoomRepository implements RoomRepository {
    private Map<String, Room> rooms = new HashMap<>();
    private ReservationRepository reservationRepository;

    public void setReservationRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void save(Room room) {
        rooms.put(room.getRoomNumber(), room);
    }

    @Override
    public Room findByRoomNumber(String roomNumber) {
        return rooms.get(roomNumber);
    }

    @Override
    public List<Room> findAll() {
        return new ArrayList<>(rooms.values());
    }

    @Override
    public List<Room> findAvailableRooms() {
        return rooms.values().stream()
                .filter(Room::isAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findByType(RoomType type) {
        return rooms.values().stream()
                .filter(room -> room.getRoomType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findByStatus(RoomStatus status) {
        return rooms.values().stream()
                .filter(room -> room.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findAvailableByTypeAndDates(RoomType type, LocalDate checkIn, LocalDate checkOut) {
        return rooms.values().stream()
                .filter(room -> room.getRoomType() == type)
                .filter(room -> isRoomAvailableForDates(room, checkIn, checkOut))
                .collect(Collectors.toList());
    }

    private boolean isRoomAvailableForDates(Room room, LocalDate checkIn, LocalDate checkOut) {
        if (reservationRepository == null) {
            return room.isAvailable();
        }
        
        List<Reservation> allReservations = reservationRepository.findAll();
        for (Reservation reservation : allReservations) {
            if (reservation.getRoom().getRoomNumber().equals(room.getRoomNumber())) {
                if (datesOverlap(checkIn, checkOut, reservation.getCheckInDate(), reservation.getCheckOutDate())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean datesOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !start1.isAfter(end2) && !end1.isBefore(start2);
    }
}
