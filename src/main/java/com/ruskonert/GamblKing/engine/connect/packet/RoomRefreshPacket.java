package com.ruskonert.GamblKing.engine.connect.packet;

import com.ruskonert.GamblKing.adapter.RoomAdapter;
import com.ruskonert.GamblKing.connect.Packet;
import com.ruskonert.GamblKing.engine.connect.ConnectionBackground;
import com.ruskonert.GamblKing.entity.Room;
import com.ruskonert.GamblKing.property.ServerProperty;

import java.io.DataOutputStream;

public class RoomRefreshPacket extends Packet
{
    private Room[] receivedRoom;
    public Room[] getReceivedRoom() { return this.receivedRoom; }

    public RoomRefreshPacket(Room[] list) {
        super(ServerProperty.ROOM_REFRESH);
        this.getJsonSerializers().put(Room.class, new RoomAdapter());
        receivedRoom = list;
    }
}
