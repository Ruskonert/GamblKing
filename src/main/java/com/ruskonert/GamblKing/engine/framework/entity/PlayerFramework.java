package com.ruskonert.GamblKing.engine.framework.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ruskonert.GamblKing.engine.GameServer;
import com.ruskonert.GamblKing.entity.Player;
import com.ruskonert.GamblKing.util.SecurityUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;

public class PlayerFramework implements Player
{
    private String id;
    @Override public String getId() { return this.id; }
    private void setId(String id) { this.id = id; }

    private String nickname;
    @Override public String getNickname() { return this.nickname; }
    private void setNickname(String nickname) { this.nickname = nickname; }

    private String password;
    private void setPassword(String password) { this.password = SecurityUtil.Companion.sha256(password); }
    @Override public String getPassword() { return this.password; }

    private String lastConnectTime;
    @Override public String getLastMessage() { return this.lastConnectTime; }

    @Override
    public InetAddress getInetAddress() {
        return null;
    }


    @Override
    public void sendMessage(String message)
    {

    }

    public PlayerFramework() { }

    public static Player register(String id, String nickname, String password)
    {
        PlayerFramework framework = new PlayerFramework();
        framework.setId(id);
        framework.setNickname(nickname);
        framework.setPassword(password);

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonMessage = gson.toJson(framework);
        File dataFolder = GameServer.getServer().getDataFolder();
        File playerFile = new File(dataFolder, "/player/" + framework.getId() + ".json");
        try
        {
            File playerFolder = new File(dataFolder, "/player");
            if(! playerFolder.exists()) playerFolder.mkdir();
            if(playerFile.createNewFile())
            {
                GameServer.getConsoleSender().log("The user id: " + framework.getNickname() + " was created.");
            }
            else
            {
                GameServer.getConsoleSender().log(framework.getNickname() + " was failed creating. Please check your directory.");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(playerFile));
            writer.append(jsonMessage);
            writer.flush();
            writer.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return framework;
    }
}
