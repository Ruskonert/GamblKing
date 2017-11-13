package com.ruskonert.GameClient.connect;

import com.google.gson.Gson;

public abstract class PacketConnection
{
    private int statusNumber;
    public int getStatusNumber() { return this.statusNumber; }

    public PacketConnection(int statusNumber)
    {
        this.statusNumber = statusNumber;
    }

    public void send(ClientBackground clientSocket, Object handleInstance)
    {
        Gson gson = new Gson();
        clientSocket.send(gson.toJson(handleInstance));
    }

}
