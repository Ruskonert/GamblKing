package com.ruskonert.GamblKing.engine.framework;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ruskonert.GamblKing.engine.GameServer;
import com.ruskonert.GamblKing.engine.connect.BindConnection;
import com.ruskonert.GamblKing.engine.entity.OfflinePlayer;
import com.ruskonert.GamblKing.engine.framework.entity.PlayerFramework;
import com.ruskonert.GamblKing.engine.server.Channel;
import com.ruskonert.GamblKing.program.ConsoleSender;
import com.ruskonert.GamblKing.engine.server.Server;
import com.ruskonert.GamblKing.entity.Player;
import com.ruskonert.GamblKing.util.ReflectionUtil;
import com.ruskonert.GamblKing.util.SystemUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class GameServerFramework implements Server
{
    public synchronized static void generate()
    {
        if(GameServer.getServer() == null) new GameServerFramework();
        else GameServer.getServer().getConsoleSender().sendMessage("GameServer was already initialized!");
    }

    private GameServerFramework()
    {
        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

        this.consoleSender = new ConsoleSenderFramework(this);

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
        if(! new File("data/player/").exists())
        {
            return null;
        }
        else
        {
            File[] files = new File("data/Player").listFiles();
            for(File f: files != null ? files : new File[0])
                if(f.getName().equalsIgnoreCase(id + ".json")) {
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(f));
                        StringBuilder total = new StringBuilder();
                        String s;
                        while ((s = reader.readLine()) != null) {
                            total.append(s);
                        }
                        Gson gson = new GsonBuilder().create();
                        return gson.fromJson(total.toString(), PlayerFramework.class);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }
                return null;
        }
    }

    @Override
    public Collection<? extends Player> getPlayers()
    {
        List<Player> players = new ArrayList<>();
        File[] files = new File("data/player").listFiles();
        for(File f : files != null ? files : new File[0])
        {
            players.add(this.getPlayer(f.getName().replaceAll(".json", "")));
        }
        return players;
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

    @Override
    public File getUpdateFolder() { return new File("update"); }
}
