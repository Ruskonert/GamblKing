package com.ruskonert.GameEngine.framework.entity;

import com.ruskonert.GameEngine.entity.Player;
import com.ruskonert.GameEngine.util.SecurityUtil;

import java.net.InetAddress;

public class PlayerFramework implements Player

{
    private String id;

    private String nickname;

    private String password;

    @Override
    public InetAddress getInetAddress() {
        return null;
    }

    @Override public String getId() { return this.id; }

    @Override public String getNickname() { return this.nickname; }

    @Override public String getPassword() { return this.password; }

    private void setId(String id) { this.id = id; }

    private void setNickname(String nickname) { this.nickname = nickname; }

    private void setPassword(String password) { this.password = SecurityUtil.Companion.sha256(password); }



    public static void register(String id, String nickname, String password)
    {
        PlayerFramework framework = new PlayerFramework();
        framework.setId(id);
        framework.setNickname(nickname);
        framework.setPassword(password);
    }


    @Override
    public String getLastMessage() {
        return null;
    }

    @Override
    public void sendMessage(String message) {

    }
}
