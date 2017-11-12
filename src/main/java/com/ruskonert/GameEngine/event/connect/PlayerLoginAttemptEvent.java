package com.ruskonert.GameEngine.event.connect;

import com.ruskonert.GameEngine.entity.Player;
import com.ruskonert.GameEngine.event.Event;

public class PlayerLoginAttemptEvent extends Event
{
    private Player player;
    public Player getPlayer() { return this.player; }

    public PlayerLoginAttemptEvent(Player player)
    {
        this.player = player;
    }
}
