package observer;

import domain.Room;
import domain.enums.RoomStatus;

public class HousekeepingObserver implements RoomObserver {
    private String teamName;

    public HousekeepingObserver(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public void onRoomStatusChanged(Room room) {
        if (room.getStatus() == RoomStatus.DIRTY) {
            System.out.println("[" + teamName + "] ALERT: Room " + room.getRoomNumber() + 
                             " needs cleaning!");
        } else if (room.getStatus() == RoomStatus.AVAILABLE) {
            System.out.println("[" + teamName + "] INFO: Room " + room.getRoomNumber() + 
                             " is now clean and available.");
        }
    }
}
