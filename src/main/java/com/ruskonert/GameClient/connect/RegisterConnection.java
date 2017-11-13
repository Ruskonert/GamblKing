package com.ruskonert.GameClient.connect;

import com.ruskonert.GameClient.ClientLoader;
import com.ruskonert.GameEngine.property.ServerProperty;

public class RegisterConnection extends PacketConnection
    {
        private String id;
    public String getId() { return id; }

    private String nickname;
    public String getNickname() { return nickname; }

    private String password;
    public String getPassword() { return password; }

    public RegisterConnection(String id, String nickname, String password)
    {
        super(ServerProperty.CHECK_REGISTER_CONNECTION);
        this.id = id;
        this.nickname = nickname;
        this.password = password;
    }

    public void send()
    {
        this.send(ClientLoader.getBackgroundConnection(), this);
    }
}
