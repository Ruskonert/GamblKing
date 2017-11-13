package com.ruskonert.GameEngine.framework;

import com.ruskonert.GameEngine.GameServer;
import com.ruskonert.GameEngine.connect.BindConnection;
import com.ruskonert.GameEngine.entity.OfflinePlayer;
import com.ruskonert.GameEngine.entity.Player;
import com.ruskonert.GameEngine.server.Channel;
import com.ruskonert.GameEngine.server.ConsoleSender;
import com.ruskonert.GameEngine.server.Server;
import com.ruskonert.GameEngine.util.ReflectionUtil;
import com.ruskonert.GameEngine.util.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GameServerFramework implements Server
{
    public synchronized static void generate()
    {
        if(GameServer.getServer() == null) new GameServerFramework();
        else GameServer.getServer().getConsoleSender().sendMessage("GameServer was already initialized!");
    }

    private GameServerFramework()
    {
        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

        ConsoleSender consoleSenderFramework = new ConsoleSenderFramework(this);
        this.consoleSender = consoleSenderFramework;

        BindConnection connection = null;
        try {
            connection = new BindConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.connectionServer = connection;

        try
        {
            ReflectionUtil.Companion.setStaticField(GameServer.class, "server", this);
        }
        catch (IllegalAccessException | NoSuchFieldException e)
        {
            SystemUtil.Companion.error(e);
        }
    }

    private List<Player> onlinePlayer;

    private Map<String, Channel> channelMap;

    private void generateChannel(String channelName)
    {
        ChannelFramework framework = new ChannelFramework(channelName);
        channelMap.put(channelName, framework);
    }


    private SimpleDateFormat simpleDateFormat;
    @Override public SimpleDateFormat getDateFormat()
    {
        return this.simpleDateFormat;
    }

    private ConsoleSender consoleSender;
    @Override public ConsoleSender getConsoleSender() { return this.consoleSender; }

    private BindConnection connectionServer;
    @Override public BindConnection getBindConnection() { return this.connectionServer; }

    @Override
    public Collection<? extends OfflinePlayer> getOfflinePlayer()
    {
        return null;
    }

    @Override
    public Player getPlayer(String id)
    {
        return null;
    }

    @Override
    public Collection<? extends Player> getPlayers()
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

    @Override
    public File getDataFolder() { return new File("data"); }
}
