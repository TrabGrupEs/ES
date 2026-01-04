package service;

import domain.*;
import domain.enums.ReservationStatus;
import domain.enums.RoomStatus;
import domain.enums.PaymentMethod;
import repository.ReservationRepository;
import security.SecurityService;
import domain.enums.UserRole;

public class CheckOutService {
    private ReservationRepository reservationRepository;
    private RoomService roomService;
    private InvoiceService invoiceService;
    private SecurityService securityService;

    public CheckOutService(ReservationRepository reservationRepository, 
                          RoomService roomService, 
                          InvoiceService invoiceService) {
        this.reservationRepository = reservationRepository;
        this.roomService = roomService;
        this.invoiceService = invoiceService;
        this.securityService = SecurityService.getInstance();
    }

    public Invoice performCheckOut(String reservationId, PaymentMethod paymentMethod) {
        securityService.checkPermission(UserRole.RECEPTIONIST, UserRole.MANAGER);
        
        Reservation reservation = reservationRepository.findById(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found");
        }

        if (reservation.getStatus() != ReservationStatus.CHECKED_IN) {
            throw new IllegalStateException("Only checked-in reservations can be checked out");
        }

        // Atualizar status da reserva
        reservation.setStatus(ReservationStatus.CHECKED_OUT);
        reservationRepository.update(reservation);

        // Atualizar status do quarto para DIRTY (precisa de limpeza)
        roomService.updateRoomStatus(reservation.getRoom().getRoomNumber(), RoomStatus.DIRTY);

        // Processar pagamento
        Invoice invoice = invoiceService.getInvoiceByReservation(reservationId);
        if (invoice != null && !invoice.isPaid()) {
            invoiceService.processPayment(invoice.getInvoiceId(), paymentMethod);
        }

        System.out.println("Check-out completed for reservation: " + reservationId);
        System.out.println("Guest: " + reservation.getGuest().getName());
        System.out.println("Room " + reservation.getRoom().getRoomNumber() + " marked as DIRTY");
        
        return invoice;
    }

    public boolean canCheckOut(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId);
        return reservation != null && 
               reservation.getStatus() == ReservationStatus.CHECKED_IN;
    }
}
