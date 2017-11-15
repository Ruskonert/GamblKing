package com.ruskonert.GamblKing.client.connect.packet;

import com.ruskonert.GamblKing.property.ServerProperty;

public class RegisterConnectionPacket extends ClientPacket
{
    private String id;
    public String getId() { return id; }

    private String nickname;
    public String getNickname() { return nickname; }

    private String password;
    public String getPassword() { return password; }

    public RegisterConnectionPacket(String id, String nickname, String password)
    {
        super(ServerProperty.CHECK_REGISTER_CONNECTION);
        this.id = id;
        this.nickname = nickname;
        this.password = password;
    }
}
