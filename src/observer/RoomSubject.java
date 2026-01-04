package observer;

import domain.Room;
import domain.enums.RoomStatus;
import java.util.ArrayList;
import java.util.List;

public class RoomSubject {
    private List<RoomObserver> observers = new ArrayList<>();
    private Room room;

    public RoomSubject(Room room) {
        this.room = room;
    }

    public void addObserver(RoomObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(RoomObserver observer) {
        observers.remove(observer);
    }

    public void updateRoomStatus(RoomStatus newStatus) {
        room.setStatus(newStatus);
        notifyObservers();
    }

    private void notifyObservers() {
        for (RoomObserver observer : observers) {
            observer.onRoomStatusChanged(room);
        }
    }

    public Room getRoom() {
        return room;
    }
}
