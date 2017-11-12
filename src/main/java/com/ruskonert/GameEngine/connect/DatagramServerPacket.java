package com.ruskonert.GameEngine.connect;

import com.ruskonert.GameEngine.GameServer;
import com.ruskonert.GameEngine.util.SystemUtil;
import javafx.concurrent.Task;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public abstract class DatagramServerPacket extends Task<Void>
{
    private DatagramSocket socket;
    public DatagramSocket getSocket() { return this.socket; }

    public DatagramServerPacket() { this(GameServer.getServer().getBindConnection().getPort()); }
    public DatagramServerPacket(int port)
    {
        try { socket = new DatagramSocket(port); }
        catch (SocketException e)
        {
            SystemUtil.Companion.error(e);
        }
    }

    @Override
    protected final Void call() throws Exception
    {
        while(true)
        {
            byte[] inbuf = new byte[256];
            DatagramPacket packet = new DatagramPacket(inbuf, inbuf.length);
            socket.receive(packet);
            onReceive(SystemUtil.Companion.arrayToObject(packet.getData()));

            socket.close();
        }
    }

    protected void onReceive(Object handleInstance)
    {

    }
}
