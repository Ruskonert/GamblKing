package com.ruskonert.GameEngine.connect.server;

import com.ruskonert.GameEngine.GameServer;
import com.ruskonert.GameEngine.connect.DatagramServerPacket;
import com.ruskonert.GameEngine.entity.Player;
import com.ruskonert.GameEngine.server.ConsoleSender;

public class PlayerMessagePacket extends DatagramServerPacket
{
    @Override
    protected void onReceive(Object handleInstance)
    {
        Player player = (Player)handleInstance;
        String message = player.getLastMessage();
        ConsoleSender sender = GameServer.getServer().getConsoleSender();
        sender.sendMessage(player.getNickname() + ": " + message);
    }
}
