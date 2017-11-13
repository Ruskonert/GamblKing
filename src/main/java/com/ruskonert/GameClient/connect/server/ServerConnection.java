package com.ruskonert.GameClient.connect.server;

import com.ruskonert.GameClient.connect.PacketConnection;

import java.net.InetAddress;

public class ServerConnection extends PacketConnection
{
    public ServerConnection(InetAddress serverAddress, int statusNumber)
    {
        super(statusNumber);
    }

}
