package com.ruskonert.GamblKing.engine.listener;

import com.ruskonert.GamblKing.engine.GameServer;
import com.ruskonert.GamblKing.entity.Player;
import com.ruskonert.GamblKing.event.EventListener;
import com.ruskonert.GamblKing.event.Handle;
import com.ruskonert.GamblKing.engine.event.connect.PlayerLoginAttemptEvent;

public class PlayerConnectListener implements EventListener
{
    @Handle
    public void sendConnect(PlayerLoginAttemptEvent e)
    {
        Player player = e.getPlayer();
        GameServer.getConsoleSender().sendAll(player.getNickname() + "(" + player.getId() + ")님이 게임에 접속하셨습니다.");
    }
}
