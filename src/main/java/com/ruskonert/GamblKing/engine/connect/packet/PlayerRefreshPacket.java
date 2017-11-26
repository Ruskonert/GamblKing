package com.ruskonert.GamblKing.engine.connect.packet;

import com.ruskonert.GamblKing.adapter.PlayerAdapter;
import com.ruskonert.GamblKing.entity.Player;
import com.ruskonert.GamblKing.property.ServerProperty;

import java.io.DataOutputStream;

public class PlayerRefreshPacket extends ServerPacket
{

    private Player player;
    public Player getPlayer() { return this.player; }
    public PlayerRefreshPacket(DataOutputStream out, Player player)
    {
        super(out, ServerProperty.PLAYER_REFRESH);
        this.getJsonSerializers().put(Player.class, new PlayerAdapter());
        this.player = player;
    }
}
