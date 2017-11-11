package com.ruskonert.GameServer.server;

import com.ruskonert.GameServer.Channel;
import com.ruskonert.GameServer.entity.OfflinePlayer;
import com.ruskonert.GameServer.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

public interface Server
{
    Collection<? extends OfflinePlayer> getOfflinePlayer();

    Player getPlayer();

    Collection<? extends Player> getPlayers();

    ConsoleSender getConsoleSender();

    SimpleDateFormat getDateFormat();

    Channel getChannel(String channelName);

    Map<String,Channel> getChannels();
}
