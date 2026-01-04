package service;

import domain.*;
import domain.enums.PaymentMethod;
import java.util.*;

public class InvoiceService {
    private Map<String, Invoice> invoices = new HashMap<>();
    private Map<String, String> reservationToInvoice = new HashMap<>();

    public Invoice createInvoice(Reservation reservation) {
        Invoice invoice = new Invoice(reservation);
        invoices.put(invoice.getInvoiceId(), invoice);
        reservationToInvoice.put(reservation.getReservationId(), invoice.getInvoiceId());
        
        System.out.println("Invoice created: " + invoice.getInvoiceId());
        return invoice;
    }

    public void addAdditionalCharge(String invoiceId, String description, double amount) {
        Invoice invoice = invoices.get(invoiceId);
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice not found");
        }
        invoice.addAdditionalCharge(description, amount);
        System.out.println("Additional charge added to invoice " + invoiceId + ": " + description);
    }

    public void processPayment(String invoiceId, PaymentMethod paymentMethod) {
        Invoice invoice = invoices.get(invoiceId);
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice not found");
        }
        
        if (invoice.isPaid()) {
            throw new IllegalStateException("Invoice already paid");
        }
        
        invoice.markAsPaid(paymentMethod);
        System.out.println("Payment processed for invoice " + invoiceId + 
                         " using " + paymentMethod + ". Amount: €" + invoice.getTotalAmount());
    }

    public Invoice getInvoiceById(String invoiceId) {
        return invoices.get(invoiceId);
    }

    public Invoice getInvoiceByReservation(String reservationId) {
        String invoiceId = reservationToInvoice.get(reservationId);
        return invoiceId != null ? invoices.get(invoiceId) : null;
    }

    public List<Invoice> getAllInvoices() {
        return new ArrayList<>(invoices.values());
    }

    public double getTotalRevenue() {
        return invoices.values().stream()
                .filter(Invoice::isPaid)
                .mapToDouble(Invoice::getTotalAmount)
                .sum();
    }

    public List<Invoice> getUnpaidInvoices() {
        List<Invoice> unpaidInvoices = new ArrayList<>();
        for (Invoice invoice : invoices.values()) {
            if (!invoice.isPaid()) {
                unpaidInvoices.add(invoice);
            }
        }
        return unpaidInvoices;
    }
}
