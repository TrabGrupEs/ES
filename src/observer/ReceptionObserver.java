package observer;

import domain.Room;

public class ReceptionObserver implements RoomObserver {
    @Override
    public void onRoomStatusChanged(Room room) {
        System.out.println("[RECEPTION] Room " + room.getRoomNumber() + 
                         " status changed to: " + room.getStatus());
    }
}
