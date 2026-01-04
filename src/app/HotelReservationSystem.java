package app;

import domain.*;
import domain.enums.*;
import factory.UserFactory;
import observer.*;
import repository.*;
import security.SecurityService;
import service.*;
import util.ReportGenerator;
import java.time.LocalDate;

public class HotelReservationSystem {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   HOTEL RESERVATION SYSTEM (HRS)");
        System.out.println("========================================\n");

        // Inicializar repositórios
        InMemoryRoomRepository roomRepository = new InMemoryRoomRepository();
        InMemoryReservationRepository reservationRepository = new InMemoryReservationRepository();
        roomRepository.setReservationRepository(reservationRepository);

        // Inicializar serviços (Singleton patterns)
        PricingService pricingService = PricingService.getInstance();
        SecurityService securityService = SecurityService.getInstance();
        
        RoomService roomService = new RoomService(roomRepository);
        ReservationService reservationService = new ReservationService(
            reservationRepository, roomService, pricingService);
        InvoiceService invoiceService = new InvoiceService();
        CheckInService checkInService = new CheckInService(
            reservationRepository, roomService, invoiceService);
        CheckOutService checkOutService = new CheckOutService(
            reservationRepository, roomService, invoiceService);
        HousekeepingService housekeepingService = new HousekeepingService(roomService);
        MaintenanceService maintenanceService = new MaintenanceService(roomService);
        ReportGenerator reportGenerator = new ReportGenerator(
            roomService, invoiceService, reservationService);

        // Criar quartos do hotel
        System.out.println("=== Initializing Hotel Rooms ===");
        Room room101 = new Room("101", RoomType.SINGLE, 1);
        Room room102 = new Room("102", RoomType.DOUBLE, 1);
        Room room201 = new Room("201", RoomType.SUITE, 2);
        Room room202 = new Room("202", RoomType.DELUXE, 2);
        Room room301 = new Room("301", RoomType.DOUBLE, 3);
        
        roomService.addRoom(room101);
        roomService.addRoom(room102);
        roomService.addRoom(room201);
        roomService.addRoom(room202);
        roomService.addRoom(room301);
        System.out.println("✓ 5 rooms added to the system\n");

        // Configurar padrão Observer para Room 102 e 301
        System.out.println("=== Setting up Observer Pattern ===");
        HousekeepingObserver housekeepingObserver = new HousekeepingObserver("Housekeeping Team A");
        ReceptionObserver receptionObserver = new ReceptionObserver();
        
        roomService.getRoomSubject("102").addObserver(housekeepingObserver);
        roomService.getRoomSubject("102").addObserver(receptionObserver);
        roomService.getRoomSubject("301").addObserver(housekeepingObserver);
        System.out.println("✓ Observers configured for rooms 102 and 301\n");

        // Criar utilizadores usando Factory Pattern
        System.out.println("=== Creating Users (Factory Pattern) ===");
        Guest guest1 = (Guest) UserFactory.createUser(
            UserRole.GUEST, "João Silva", "joao@email.com", "pass123", 
            "912345678", "Lisboa, Portugal");
        
        Guest guest2 = (Guest) UserFactory.createUser(
            UserRole.GUEST, "Maria Santos", "maria@email.com", "pass456", 
            "965432189", "Porto, Portugal");
        
        Receptionist receptionist = (Receptionist) UserFactory.createUser(
            UserRole.RECEPTIONIST, "Ana Costa", "ana@hotel.com", "recep123", 
            "REC001", "Morning");
        
        Manager manager = (Manager) UserFactory.createUser(
            UserRole.MANAGER, "Carlos Mendes", "carlos@hotel.com", "manager123", 
            "Operations");
        
        Administrator admin = (Administrator) UserFactory.createUser(
            UserRole.ADMIN, "System Admin", "admin@hotel.com", "admin123");
        
        System.out.println("✓ Users created:");
        System.out.println("  - " + guest1.getName() + " (Guest)");
        System.out.println("  - " + guest2.getName() + " (Guest)");
        System.out.println("  - " + receptionist.getName() + " (Receptionist)");
        System.out.println("  - " + manager.getName() + " (Manager)");
        System.out.println("  - " + admin.getName() + " (Administrator)\n");

        // Demonstração de permissões
        System.out.println("=== User Permissions ===");
        guest1.displayPermissions();
        receptionist.displayPermissions();
        manager.displayPermissions();
        System.out.println();

        // CENÁRIO 1: Guest cria uma reserva
        System.out.println("\n=== SCENARIO 1: Guest Creates Reservation ===");
        securityService.authenticateUser(guest1);
        
        LocalDate checkIn1 = LocalDate.now().plusDays(7);
        LocalDate checkOut1 = checkIn1.plusDays(3);
        
        try {
            Reservation reservation1 = reservationService.createReservation(
                guest1, RoomType.DOUBLE, checkIn1, checkOut1);
            System.out.println("✓ Reservation created:");
            System.out.println("  - Reservation ID: " + reservation1.getReservationId());
            System.out.println("  - Room: " + reservation1.getRoom().getRoomNumber());
            System.out.println("  - Dates: " + checkIn1 + " to " + checkOut1);
            System.out.println("  - Total Price: €" + reservation1.getTotalPrice());
        } catch (Exception e) {
            System.err.println("✗ Error: " + e.getMessage());
        }

        // CENÁRIO 2: Prevenção de Overbooking
        System.out.println("\n=== SCENARIO 2: Overbooking Prevention ===");
        try {
            // Tentar reservar o mesmo quarto nas mesmas datas
            Reservation overbook = reservationService.createReservation(
                guest2, RoomType.DOUBLE, checkIn1, checkOut1.plusDays(1));
            System.out.println("✓ Second reservation created (different room): " + 
                             overbook.getRoom().getRoomNumber());
        } catch (Exception e) {
            System.err.println("✗ Overbooking prevented: " + e.getMessage());
        }

        // CENÁRIO 3: Check-in Process
        System.out.println("\n=== SCENARIO 3: Check-In Process ===");
        securityService.authenticateUser(receptionist);
        
        // Criar reserva para check-in imediato
        LocalDate todayCheckIn = LocalDate.now();
        LocalDate todayCheckOut = todayCheckIn.plusDays(2);
        Reservation reservation2 = reservationService.createReservation(
            guest2, RoomType.SUITE, todayCheckIn, todayCheckOut);
        
        try {
            Invoice invoice = checkInService.performCheckIn(reservation2.getReservationId());
            System.out.println("✓ Check-in successful");
            System.out.println("  - Invoice ID: " + invoice.getInvoiceId());
            System.out.println("  - Amount: €" + invoice.getTotalAmount());
        } catch (Exception e) {
            System.err.println("✗ Check-in failed: " + e.getMessage());
        }

        // CENÁRIO 4: Adicionar cobranças extras e Check-out
        System.out.println("\n=== SCENARIO 4: Additional Charges & Check-Out ===");
        Invoice invoice2 = invoiceService.getInvoiceByReservation(reservation2.getReservationId());
        invoiceService.addAdditionalCharge(invoice2.getInvoiceId(), "Room Service", 25.0);
        invoiceService.addAdditionalCharge(invoice2.getInvoiceId(), "Minibar", 15.50);
        System.out.println("✓ Additional charges added. New total: €" + invoice2.getTotalAmount());
        
        try {
            checkOutService.performCheckOut(reservation2.getReservationId(), PaymentMethod.CREDIT_CARD);
            System.out.println("✓ Check-out completed and payment processed");
        } catch (Exception e) {
            System.err.println("✗ Check-out failed: " + e.getMessage());
        }

        // CENÁRIO 5: Housekeeping - Limpeza de quartos (Observer notificado)
        System.out.println("\n=== SCENARIO 5: Housekeeping ===");
        housekeepingService.generateDailyCleaningReport();
        
        for (Room dirtyRoom : housekeepingService.getRoomsThatNeedCleaning()) {
            housekeepingService.markRoomAsCleaned(dirtyRoom.getRoomNumber());
        }

        // CENÁRIO 6: Manutenção
        System.out.println("\n=== SCENARIO 6: Maintenance Request ===");
        MaintenanceRequest maintenanceReq = maintenanceService.createMaintenanceRequest(
            "101", "Air conditioning not working", "HIGH");
        
        maintenanceService.assignMaintenanceRequest(maintenanceReq.getRequestId(), "Technician José");
        maintenanceService.updateRequestStatus(maintenanceReq.getRequestId(), MaintenanceStatus.COMPLETED);
        System.out.println("✓ Maintenance completed for room 101");

        // CENÁRIO 7: Preços Dinâmicos (Singleton)
        System.out.println("\n=== SCENARIO 7: Dynamic Pricing (Singleton Pattern) ===");
        securityService.authenticateUser(manager);
        double occupancyRate = roomService.calculateOccupancyRate();
        System.out.println("Current occupancy rate: " + String.format("%.2f", occupancyRate) + "%");
        pricingService.adjustPriceBasedOnOccupancy(occupancyRate);

        // CENÁRIO 8: Relatórios (Manager access)
        System.out.println("\n=== SCENARIO 8: Reports Generation ===");
        reportGenerator.generateCompleteReport();

        // CENÁRIO 9: Teste de Segurança - Guest tentando acessar relatórios
        System.out.println("\n=== SCENARIO 9: Security Test ===");
        securityService.authenticateUser(guest1);
        try {
            reservationService.getAllReservations();
            System.out.println("✓ Access granted (should not happen)");
        } catch (SecurityException e) {
            System.out.println("✗ Access denied (expected): " + e.getMessage());
        }

        // Limpar autenticação
        securityService.logout();
        
        System.out.println("\n========================================");
        System.out.println("   SYSTEM DEMONSTRATION COMPLETED");
        System.out.println("========================================");
    }
}
