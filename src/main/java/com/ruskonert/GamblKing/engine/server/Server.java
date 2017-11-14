package com.ruskonert.GamblKing.engine.server;

import com.ruskonert.GamblKing.engine.connect.BindConnection;
import com.ruskonert.GamblKing.engine.entity.OfflinePlayer;
import com.ruskonert.GamblKing.engine.entity.Player;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

public interface Server
{
    BindConnection getBindConnection();

    Collection<? extends OfflinePlayer> getOfflinePlayer();

    Player getPlayer(String name);

    Collection<? extends Player> getPlayers();

    ConsoleSender getConsoleSender();

    SimpleDateFormat getDateFormat();

    Channel getChannel(String channelName);

    Map<String,Channel> getChannels();

    File getDataFolder();

    File getUpdateFolder();
}
