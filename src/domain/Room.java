package domain;

import domain.enums.RoomStatus;
import domain.enums.RoomType;

public class Room {
    private String roomNumber;
    private RoomType roomType;
    private RoomStatus status;
    private int floor;
    private double currentPrice;

    public Room(String roomNumber, RoomType roomType, int floor) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.floor = floor;
        this.status = RoomStatus.AVAILABLE;
        this.currentPrice = roomType.getBasePrice();
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public int getFloor() {
        return floor;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public boolean isAvailable() {
        return status == RoomStatus.AVAILABLE;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber='" + roomNumber + '\'' +
                ", roomType=" + roomType +
                ", status=" + status +
                ", floor=" + floor +
                ", currentPrice=" + currentPrice +
                '}';
    }
}
