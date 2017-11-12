package com.ruskonert.GameEngine.connect.server;

import com.ruskonert.GameEngine.GameServer;
import com.ruskonert.GameEngine.connect.DatagramServerPacket;
import com.ruskonert.GameEngine.server.ConsoleSender;

public class PlayerMessagePacket extends DatagramServerPacket
{
    @Override
    protected void onReceive(Object handleInstance)
    {
        String message = (String)handleInstance;
        ConsoleSender sender = GameServer.getServer().getConsoleSender();

    }
}
