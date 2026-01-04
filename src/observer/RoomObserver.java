package observer;

import domain.Room;

public interface RoomObserver {
    void onRoomStatusChanged(Room room);
}
