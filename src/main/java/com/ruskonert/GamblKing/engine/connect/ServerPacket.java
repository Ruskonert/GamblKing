package com.ruskonert.GamblKing.engine.connect;

import com.ruskonert.GamblKing.connect.Packet;

public abstract class ServerPacket extends Packet
{
    public ServerPacket(int statusNumber)
    {
        super(statusNumber);
    }

    public void send()
    {
        sendPacket(this);
    }

    public static void sendPacket(Packet packet)
    {
        packet.send(ServerConnectionReceiver.getOutputStream(), packet);
    }
}