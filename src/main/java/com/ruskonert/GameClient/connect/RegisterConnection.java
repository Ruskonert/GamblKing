package com.ruskonert.GameClient.connect;

import com.ruskonert.GameClient.ClientLoader;
import com.ruskonert.GameEngine.property.ServerProperty;

public class RegisterConnection extends PacketConnection
{
    private String id;
    public String getId() { return id; }

    public RegisterConnection(String id)
    {
        super(ServerProperty.CHECK_REGISTER_CONNECTION);
        this.id = id;
    }

    public void send() { this.send(ClientLoader.getBackgroundConnection(), this); }

    @Override
    public void send(ClientBackground clientSocket, Object handleInstance)
    {
        super.send(clientSocket, handleInstance);
    }
}
