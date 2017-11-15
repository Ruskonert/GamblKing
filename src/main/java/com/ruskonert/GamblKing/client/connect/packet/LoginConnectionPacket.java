package com.ruskonert.GamblKing.client.connect.packet;

import com.ruskonert.GamblKing.property.ServerProperty;

public class LoginConnectionPacket extends ClientPacket
{
    private String id;
    public String getId() { return this.id; }

    private String password;
    public String getPassword() { return this.password; }

    public LoginConnectionPacket(String id, String password)
    {
        super(ServerProperty.REQUEST_LOGIN);
        this.id = id;
        this.password = password;
    }
}
