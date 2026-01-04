package service;

import domain.Room;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class PricingService {
    private static PricingService instance;
    private Map<String, Double> seasonalMultipliers = new HashMap<>();
    private double occupancyMultiplier = 1.0;

    private PricingService() {
        initializeDefaultMultipliers();
    }

    public static PricingService getInstance() {
        if (instance == null) {
            instance = new PricingService();
        }
        return instance;
    }

    private void initializeDefaultMultipliers() {
        seasonalMultipliers.put("SUMMER", 1.3);
        seasonalMultipliers.put("WINTER", 0.9);
        seasonalMultipliers.put("SPRING", 1.0);
        seasonalMultipliers.put("FALL", 1.0);
    }

    public double calculatePrice(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        long numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (numberOfNights <= 0) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        double basePrice = room.getRoomType().getBasePrice();
        double seasonMultiplier = getSeasonalMultiplier(checkInDate);
        
        double totalPrice = basePrice * numberOfNights * seasonMultiplier * occupancyMultiplier;
        
        return Math.round(totalPrice * 100.0) / 100.0;
    }

    public void adjustPriceBasedOnOccupancy(double occupancyRate) {
        if (occupancyRate > 80) {
            occupancyMultiplier = 1.2;
        } else if (occupancyRate > 60) {
            occupancyMultiplier = 1.1;
        } else if (occupancyRate < 30) {
            occupancyMultiplier = 0.9;
        } else {
            occupancyMultiplier = 1.0;
        }
        System.out.println("Pricing adjusted based on occupancy: " + occupancyRate + "% (multiplier: " + occupancyMultiplier + ")");
    }

    public void setManualPriceMultiplier(double multiplier) {
        this.occupancyMultiplier = multiplier;
        System.out.println("Manual price multiplier set to: " + multiplier);
    }

    private double getSeasonalMultiplier(LocalDate date) {
        int month = date.getMonthValue();
        if (month >= 6 && month <= 8) {
            return seasonalMultipliers.get("SUMMER");
        } else if (month >= 12 || month <= 2) {
            return seasonalMultipliers.get("WINTER");
        } else if (month >= 3 && month <= 5) {
            return seasonalMultipliers.get("SPRING");
        } else {
            return seasonalMultipliers.get("FALL");
        }
    }
}
