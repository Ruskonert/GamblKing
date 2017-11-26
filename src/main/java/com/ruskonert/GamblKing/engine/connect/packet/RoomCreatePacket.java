package com.ruskonert.GamblKing.engine.connect.packet;

import com.ruskonert.GamblKing.adapter.RoomAdapter;
import com.ruskonert.GamblKing.engine.connect.ConnectionBackground;
import com.ruskonert.GamblKing.entity.Player;
import com.ruskonert.GamblKing.entity.Room;
import com.ruskonert.GamblKing.property.ServerProperty;

public class RoomCreatePacket extends ServerPacket
{
    private Room room;
    public Room getRoom() { return room; }

    public RoomCreatePacket(Player player, Room room)
    {
        super(ConnectionBackground.getPlayerOutputStream(player), ServerProperty.ROOM_CREATE);
        this.room = room;
        this.getJsonSerializers().put(Room.class, new RoomAdapter());
    }
}
