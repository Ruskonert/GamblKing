package com.ruskonert.GamblKing.engine.connect.packet;

import com.ruskonert.GamblKing.engine.GameServer;
import com.ruskonert.GamblKing.engine.connect.ConnectionBackground;
import com.ruskonert.GamblKing.entity.Player;
import com.ruskonert.GamblKing.property.ServerProperty;

import java.io.DataOutputStream;

/**
 * 특정 플레이어에게 메시지를 보내는 패킷입니다.
 */
public class PlayerMessagePacket extends ServerPacket
{
    private String message;
    public String getMessage() { return this.message; }

    public PlayerMessagePacket(String playerId, String message)
    {
        super(ConnectionBackground.getPlayerOutputStream(GameServer.getPlayer(playerId)), ServerProperty.PLAYER_MESSAGE_RECEIVED);
        this.message = message;
    }

    @Override
    public void send() {
        //SendMessageEvent event = new SendMessageEvent(player, message);
        //if(event.isCanceled()) return;
        //event.start();
        super.send();
    }

    public PlayerMessagePacket(Player player, String message)
    {
        super(ConnectionBackground.getPlayerOutputStream(player), ServerProperty.PLAYER_MESSAGE_RECEIVED);
        this.message = message;
    }

    public PlayerMessagePacket(DataOutputStream out, String message)
    {
        super(out, ServerProperty.PLAYER_MESSAGE_RECEIVED);
        this.message = message;
    }
}
