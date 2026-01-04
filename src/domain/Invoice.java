package domain;

import domain.enums.PaymentMethod;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Invoice {
    private String invoiceId;
    private Reservation reservation;
    private List<String> additionalCharges;
    private double totalAmount;
    private boolean isPaid;
    private PaymentMethod paymentMethod;
    private LocalDateTime issuedAt;
    private LocalDateTime paidAt;

    public Invoice(Reservation reservation) {
        this.invoiceId = UUID.randomUUID().toString();
        this.reservation = reservation;
        this.additionalCharges = new ArrayList<>();
        this.totalAmount = reservation.getTotalPrice();
        this.isPaid = false;
        this.issuedAt = LocalDateTime.now();
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public List<String> getAdditionalCharges() {
        return additionalCharges;
    }

    public void addAdditionalCharge(String description, double amount) {
        additionalCharges.add(description + ": €" + amount);
        totalAmount += amount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void markAsPaid(PaymentMethod paymentMethod) {
        this.isPaid = true;
        this.paymentMethod = paymentMethod;
        this.paidAt = LocalDateTime.now();
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId='" + invoiceId + '\'' +
                ", reservation=" + reservation.getReservationId() +
                ", totalAmount=" + totalAmount +
                ", isPaid=" + isPaid +
                ", paymentMethod=" + paymentMethod +
                ", issuedAt=" + issuedAt +
                '}';
    }
}
