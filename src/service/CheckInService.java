package service;

import domain.*;
import domain.enums.ReservationStatus;
import domain.enums.RoomStatus;
import repository.ReservationRepository;
import security.SecurityService;
import domain.enums.UserRole;

public class CheckInService {
    private ReservationRepository reservationRepository;
    private RoomService roomService;
    private InvoiceService invoiceService;
    private SecurityService securityService;

    public CheckInService(ReservationRepository reservationRepository, 
                         RoomService roomService, 
                         InvoiceService invoiceService) {
        this.reservationRepository = reservationRepository;
        this.roomService = roomService;
        this.invoiceService = invoiceService;
        this.securityService = SecurityService.getInstance();
    }

    public Invoice performCheckIn(String reservationId) {
        securityService.checkPermission(UserRole.RECEPTIONIST, UserRole.MANAGER);
        
        Reservation reservation = reservationRepository.findById(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found");
        }

        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed reservations can be checked in");
        }

        // Atualizar status da reserva
        reservation.setStatus(ReservationStatus.CHECKED_IN);
        reservationRepository.update(reservation);

        // Atualizar status do quarto
        roomService.updateRoomStatus(reservation.getRoom().getRoomNumber(), RoomStatus.OCCUPIED);

        // Gerar fatura
        Invoice invoice = invoiceService.createInvoice(reservation);

        System.out.println("Check-in completed for reservation: " + reservationId);
        System.out.println("Guest: " + reservation.getGuest().getName());
        System.out.println("Room: " + reservation.getRoom().getRoomNumber());
        
        return invoice;
    }

    public boolean canCheckIn(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId);
        return reservation != null && 
               reservation.getStatus() == ReservationStatus.CONFIRMED;
    }
}
