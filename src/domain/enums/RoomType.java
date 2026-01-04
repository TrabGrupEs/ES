package domain.enums;

public enum RoomType {
    SINGLE(1, 50.0),
    DOUBLE(2, 80.0),
    SUITE(4, 150.0),
    DELUXE(2, 120.0);

    private final int capacity;
    private final double basePrice;

    RoomType(int capacity, double basePrice) {
        this.capacity = capacity;
        this.basePrice = basePrice;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getBasePrice() {
        return basePrice;
    }
}
