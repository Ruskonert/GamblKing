package com.ruskonert.GameEngine.framework;

import com.ruskonert.GameEngine.server.Channel;
import com.ruskonert.GameEngine.util.SystemUtil;
import com.ruskonert.GameEngine.GameServer;
import com.ruskonert.GameEngine.entity.OfflinePlayer;
import com.ruskonert.GameEngine.entity.Player;
import com.ruskonert.GameEngine.server.ConsoleSender;
import com.ruskonert.GameEngine.server.Server;
import com.ruskonert.GameEngine.util.ReflectionUtil;

import java.net.DatagramPacket;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Collection;
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

        try
        {
            ReflectionUtil.Companion.setStaticField(GameServer.class, "server", this);
        }
        catch (IllegalAccessException | NoSuchFieldException e)
        {
            SystemUtil.Companion.error(e);
        }
    }

    private ServerSocket socket;
    private DatagramPacket serverPacket;
    private int port;

    private SimpleDateFormat simpleDateFormat;
    @Override public SimpleDateFormat getDateFormat()
    {
        return this.simpleDateFormat;
    }

    private ConsoleSender consoleSender;
    @Override public ConsoleSender getConsoleSender() { return this.consoleSender; }

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
