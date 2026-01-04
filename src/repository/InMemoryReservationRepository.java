package repository;

import domain.Guest;
import domain.Reservation;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryReservationRepository implements ReservationRepository {
    private Map<String, Reservation> reservations = new HashMap<>();

    @Override
    public void save(Reservation reservation) {
        reservations.put(reservation.getReservationId(), reservation);
    }

    @Override
    public Reservation findById(String reservationId) {
        return reservations.get(reservationId);
    }

    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(reservations.values());
    }

    @Override
    public List<Reservation> findByGuest(Guest guest) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getGuest().getUserId().equals(guest.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public void update(Reservation reservation) {
        reservations.put(reservation.getReservationId(), reservation);
    }

    @Override
    public void delete(String reservationId) {
        reservations.remove(reservationId);
    }
}
