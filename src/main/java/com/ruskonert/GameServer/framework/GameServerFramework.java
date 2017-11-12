package com.ruskonert.GameServer.framework;

import com.ruskonert.GameServer.Channel;
import com.ruskonert.GameServer.GameServer;
import com.ruskonert.GameServer.entity.OfflinePlayer;
import com.ruskonert.GameServer.entity.Player;
import com.ruskonert.GameServer.server.ConsoleSender;
import com.ruskonert.GameServer.server.Server;
import com.ruskonert.GameServer.util.ReflectionUtil;
import com.ruskonert.GameServer.util.SystemUtil;

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
    @Override public ConsoleSender getConsoleSender()
    {
        return this.consoleSender;
    }


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
