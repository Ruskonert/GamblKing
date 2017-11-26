package com.ruskonert.GamblKing.engine.connect.packet;

import com.ruskonert.GamblKing.connect.Packet;
import com.ruskonert.GamblKing.engine.connect.ConnectionBackground;

import java.io.DataOutputStream;

public abstract class ServerPacket extends Packet
{
    private transient DataOutputStream out;

    public ServerPacket(DataOutputStream out, int statusNumber)
    {
        super(statusNumber);
        this.out = out;
    }

    public void send()
    {
        sendPacket(this);
    }

    public void sendPacket(Packet packet)
    {
        packet.send(out, packet);
    }

    @Override
    public void exit(DataOutputStream stream)
    {
        ConnectionBackground.leaveFromStream(stream);
    }
}