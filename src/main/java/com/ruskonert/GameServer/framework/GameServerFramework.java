package com.ruskonert.GameServer.framework;

import com.ruskonert.GameServer.Channel;
import com.ruskonert.GameServer.entity.OfflinePlayer;
import com.ruskonert.GameServer.entity.Player;
import com.ruskonert.GameServer.server.ConsoleSender;
import com.ruskonert.GameServer.server.Server;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

public class GameServerFramework implements Server
{

    @Override
    public Collection<? extends OfflinePlayer> getOfflinePlayer()
    {
        return null;
    }

    @Override
    public Player getPlayer()
    {
        return null;
    }

    @Override
    public Collection<? extends Player> getPlayers()
    {
        return null;
    }

    @Override
    public ConsoleSender getConsoleSender()
    {
        return null;
    }

    @Override
    public SimpleDateFormat getDateFormat()
    {
        return null;
    }

    @Override
    public Channel getChannel(String channelName)
    {
        return null;
    }

    @Override
    public Map<String, Channel> getChannels()
    {
        return null;
    }
}
