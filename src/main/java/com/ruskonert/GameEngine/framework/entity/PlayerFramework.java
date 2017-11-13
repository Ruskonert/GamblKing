package com.ruskonert.GameEngine.framework.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ruskonert.GameEngine.entity.MessageDispatcher;
import com.ruskonert.GameEngine.entity.Player;
import com.ruskonert.GameEngine.util.SecurityUtil;

import java.net.InetAddress;

public class PlayerFramework implements Player, MessageDispatcher
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

    private String lastMessage;
    @Override public String getLastMessage() { return this.lastMessage; }

    @Override
    public InetAddress getInetAddress() {
        return null;
    }


    @Override
    public void sendMessage(String message)
    {

    }

    private PlayerFramework() { }

    private void register(String id, String nickname, String password)
    {
        PlayerFramework framework = new PlayerFramework();
        framework.setId(id);
        framework.setNickname(nickname);
        framework.setPassword(password);

        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        gson.toJson(this);
    }

    private static void finish(Player player)
    {

    }
}
