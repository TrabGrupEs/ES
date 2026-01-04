package service;

import domain.*;
import domain.enums.ReservationStatus;
import domain.enums.RoomStatus;
import domain.enums.RoomType;
import repository.ReservationRepository;
import security.SecurityService;
import domain.enums.UserRole;
import java.time.LocalDate;
import java.util.List;

public class ReservationService {
    private ReservationRepository reservationRepository;
    private RoomService roomService;
    private PricingService pricingService;
    private SecurityService securityService;

    public ReservationService(ReservationRepository reservationRepository, 
                            RoomService roomService, 
                            PricingService pricingService) {
        this.reservationRepository = reservationRepository;
        this.roomService = roomService;
        this.pricingService = pricingService;
        this.securityService = SecurityService.getInstance();
    }

    public Reservation createReservation(Guest guest, RoomType roomType, 
                                        LocalDate checkInDate, LocalDate checkOutDate) {
        securityService.checkPermission(UserRole.GUEST, UserRole.RECEPTIONIST, UserRole.MANAGER);
        
        // Verificar disponibilidade
        List<Room> availableRooms = roomService.findAvailableRoomsByTypeAndDates(
            roomType, checkInDate, checkOutDate);
        
        if (availableRooms.isEmpty()) {
            throw new IllegalStateException("No rooms available for the selected dates and type");
        }

        // Prevenir overbooking
        Room selectedRoom = availableRooms.get(0);
        
        // Calcular preço
        double totalPrice = pricingService.calculatePrice(selectedRoom, checkInDate, checkOutDate);
        
        // Criar reserva
        Reservation reservation = new Reservation(guest, selectedRoom, checkInDate, checkOutDate, totalPrice);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        
        // Atualizar estado do quarto
        roomService.updateRoomStatus(selectedRoom.getRoomNumber(), RoomStatus.RESERVED);
        
        reservationRepository.save(reservation);
        
        System.out.println("Reservation created successfully: " + reservation.getReservationId());
        return reservation;
    }

    public void modifyReservation(String reservationId, LocalDate newCheckIn, LocalDate newCheckOut) {
        securityService.checkPermission(UserRole.GUEST, UserRole.RECEPTIONIST, UserRole.MANAGER);
        
        Reservation reservation = reservationRepository.findById(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found");
        }

        reservation.setCheckInDate(newCheckIn);
        reservation.setCheckOutDate(newCheckOut);
        
        double newPrice = pricingService.calculatePrice(
            reservation.getRoom(), newCheckIn, newCheckOut);
        reservation.setTotalPrice(newPrice);
        
        reservationRepository.update(reservation);
        System.out.println("Reservation modified successfully");
    }

    public void cancelReservation(String reservationId) {
        securityService.checkPermission(UserRole.GUEST, UserRole.RECEPTIONIST, UserRole.MANAGER);
        
        Reservation reservation = reservationRepository.findById(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        roomService.updateRoomStatus(reservation.getRoom().getRoomNumber(), RoomStatus.AVAILABLE);
        
        reservationRepository.update(reservation);
        System.out.println("Reservation cancelled successfully");
    }

    public Reservation getReservationById(String reservationId) {
        return reservationRepository.findById(reservationId);
    }

    public List<Reservation> getAllReservations() {
        securityService.checkPermission(UserRole.RECEPTIONIST, UserRole.MANAGER, UserRole.ADMIN);
        return reservationRepository.findAll();
    }

    public List<Reservation> getReservationsByGuest(Guest guest) {
        return reservationRepository.findByGuest(guest);
    }
}
