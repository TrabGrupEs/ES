package repository;

import domain.Reservation;
import domain.Guest;
import java.util.List;

public interface ReservationRepository {
    void save(Reservation reservation);
    Reservation findById(String reservationId);
    List<Reservation> findAll();
    List<Reservation> findByGuest(Guest guest);
    void update(Reservation reservation);
    void delete(String reservationId);
}
