package com.ruskonert.GameEngine.server;

import com.ruskonert.GameEngine.connect.BindConnection;
import com.ruskonert.GameEngine.entity.OfflinePlayer;
import com.ruskonert.GameEngine.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

public interface Server
{
    BindConnection getBindConnection();

    Collection<? extends OfflinePlayer> getOfflinePlayer();

    Player getPlayer();

    Collection<? extends Player> getPlayers();

    ConsoleSender getConsoleSender();

    SimpleDateFormat getDateFormat();

    Channel getChannel(String channelName);

    Map<String,Channel> getChannels();
}
