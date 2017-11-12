package com.ruskonert.GameEngine.connect;

import javafx.application.Platform;
public class BindConnection
{
    public void start()
    {
        Platform.runLater(() -> {
            ServerConnectionPacket serverPacket = new ServerConnectionPacket(7334);
            try {
                serverPacket.task();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

class ServerConnectionPacket extends DatagramServerPacket
{
    public ServerConnectionPacket(int port) { super(port); }

    @Override
    protected void onReceive(Object handleInstance)
    {
        super.onReceive(handleInstance);
    }
}
