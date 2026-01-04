package util;

import domain.*;
import service.*;
import java.time.LocalDate;
import java.util.List;

public class ReportGenerator {
    private RoomService roomService;
    private InvoiceService invoiceService;
    private ReservationService reservationService;

    public ReportGenerator(RoomService roomService, 
                          InvoiceService invoiceService, 
                          ReservationService reservationService) {
        this.roomService = roomService;
        this.invoiceService = invoiceService;
        this.reservationService = reservationService;
    }

    public void generateOccupancyReport() {
        System.out.println("\n========== OCCUPANCY REPORT ==========");
        System.out.println("Report Date: " + LocalDate.now());
        
        List<Room> allRooms = roomService.getAllRooms();
        List<Room> availableRooms = roomService.getAvailableRooms();
        
        double occupancyRate = roomService.calculateOccupancyRate();
        
        System.out.println("Total Rooms: " + allRooms.size());
        System.out.println("Available Rooms: " + availableRooms.size());
        System.out.println("Occupancy Rate: " + String.format("%.2f", occupancyRate) + "%");
        
        System.out.println("\nRoom Status Breakdown:");
        System.out.println("- Available: " + roomService.getRoomsByStatus(domain.enums.RoomStatus.AVAILABLE).size());
        System.out.println("- Occupied: " + roomService.getRoomsByStatus(domain.enums.RoomStatus.OCCUPIED).size());
        System.out.println("- Reserved: " + roomService.getRoomsByStatus(domain.enums.RoomStatus.RESERVED).size());
        System.out.println("- Dirty: " + roomService.getRoomsByStatus(domain.enums.RoomStatus.DIRTY).size());
        System.out.println("- Under Maintenance: " + roomService.getRoomsByStatus(domain.enums.RoomStatus.UNDER_MAINTENANCE).size());
        
        System.out.println("======================================\n");
    }

    public void generateFinancialReport() {
        System.out.println("\n========== FINANCIAL REPORT ==========");
        System.out.println("Report Date: " + LocalDate.now());
        
        List<Invoice> allInvoices = invoiceService.getAllInvoices();
        List<Invoice> unpaidInvoices = invoiceService.getUnpaidInvoices();
        double totalRevenue = invoiceService.getTotalRevenue();
        
        double pendingAmount = unpaidInvoices.stream()
                .mapToDouble(Invoice::getTotalAmount)
                .sum();
        
        System.out.println("Total Invoices: " + allInvoices.size());
        System.out.println("Paid Invoices: " + (allInvoices.size() - unpaidInvoices.size()));
        System.out.println("Unpaid Invoices: " + unpaidInvoices.size());
        System.out.println("\nTotal Revenue: €" + String.format("%.2f", totalRevenue));
        System.out.println("Pending Amount: €" + String.format("%.2f", pendingAmount));
        System.out.println("======================================\n");
    }

    public void generateCompleteReport() {
        generateOccupancyReport();
        generateFinancialReport();
    }
}
