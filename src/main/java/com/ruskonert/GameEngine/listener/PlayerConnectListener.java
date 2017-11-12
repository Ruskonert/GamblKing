package com.ruskonert.GameEngine.listener;

import com.ruskonert.GameEngine.entity.Player;
import com.ruskonert.GameEngine.event.EventListener;
import com.ruskonert.GameEngine.event.Handle;
import com.ruskonert.GameEngine.event.connect.PlayerLoginAttemptEvent;

public class PlayerConnectListener implements EventListener
{
    @Handle
    public void sendConnect(PlayerLoginAttemptEvent e)
    {
        Player player = e.getPlayer();
        player.getNickname();
    }
}
